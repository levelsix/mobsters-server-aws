package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto.SpawnObstacleStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SpawnObstacleRequestEvent;
import com.lvl6.mobsters.events.response.SpawnObstacleResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.MinimumObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.UserObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
public class SpawnObstacleController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(SpawnObstacleController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected StructureStuffUtil structureStuffUtil;

	public SpawnObstacleController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new SpawnObstacleRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_SPAWN_OBSTACLE_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final SpawnObstacleRequestProto reqProto =
		    ((SpawnObstacleRequestEvent) event).getSpawnObstacleRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final Timestamp clientTime = new Timestamp(reqProto.getCurTime());
		final List<MinimumObstacleProto> mopList = reqProto.getProspectiveObstaclesList();

		final SpawnObstacleResponseProto.Builder resBuilder =
		    SpawnObstacleResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(SpawnObstacleStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());

			final boolean legitComplete = checkLegit(resBuilder, userUuid, user, mopList);

			boolean success = false;
			final List<ObstacleForUser> ofuList = new ArrayList<ObstacleForUser>();
			if (legitComplete) {
				success = writeChangesToDB(user, userUuid, clientTime, mopList, ofuList);
			}

			if (success) {
				// client needs the object protos but with the ids set
				for (final ObstacleForUser ofu : ofuList) {
					final UserObstacleProto obp =
					    CreateInfoProtoUtils.createUserObstacleProto(ofu);
					resBuilder.addSpawnedObstacles(obp);
				}
			}

			final SpawnObstacleResponseEvent resEvent =
			    new SpawnObstacleResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setSpawnObstacleResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in SpawnObstacleController.processRequestEvent", e);
			}

			if (success) {
				// modified the user, the last obstacle removed time
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error("fatal exception in SpawnObstacleController.processRequestEvent",
					    e);
				}

			}

		} catch (final Exception e) {
			LOG.error("exception in SpawnObstacleController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(SpawnObstacleStatus.FAIL_OTHER);
				final SpawnObstacleResponseEvent resEvent =
				    new SpawnObstacleResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setSpawnObstacleResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in SpawnObstacleController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in SpawnObstacleController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegit( final Builder resBuilder, final String userUuid,
	    final User user, final List<MinimumObstacleProto> mopList )
	{

		if (null == user) {
			LOG.error("unexpected error: user is null. user="
			    + user
			    + "\t userUuid="
			    + userUuid);
			return false;
		}

		if ((null == mopList)
		    || mopList.isEmpty()) {
			LOG.error("no obstacles to spawn for user. mopList="
			    + mopList
			    + "\t user="
			    + user);
			return false;
		}

		resBuilder.setStatus(SpawnObstacleStatus.SUCCESS);
		return true;
	}

	private boolean writeChangesToDB( final User user, final String userUuid,
	    final Timestamp clientTime, final List<MinimumObstacleProto> mopList,
	    final List<ObstacleForUser> ofuList )
	{
		// convert the protos to java objects
		final List<ObstacleForUser> ofuListTemp =
		    getStructureStuffUtil().createObstacleForUserFromUserObstacleProtos(userUuid,
		        mopList);
		LOG.info("inserting obstacles into obstacle_for_user. ofuListTemp="
		    + ofuListTemp);

		// need to get the ids in order to set the objects' ids so client will
		// know how
		// to reference said objects
		final List<Integer> ofuIdList = InsertUtils.get()
		    .insertIntoObstaclesForUserGetUuids(userUuid, ofuListTemp);
		if ((null == ofuIdList)
		    || ofuIdList.isEmpty()) {
			LOG.error("could not insert into obstacle for user obstacles="
			    + ofuListTemp);
			return false;
		}

		LOG.info("updating last obstacle spawned time:"
		    + clientTime);
		if (!user.updateRelativeGemsObstacleTimeNumRemoved(0, clientTime, 0)) {
			LOG.error("could not update last obstacle spawned time to "
			    + clientTime);
			return false;
		}

		getStructureStuffUtil().setObstacleForUserUuids(ofuIdList, ofuListTemp);

		ofuList.addAll(ofuListTemp);
		return true;
	}

	public StructureStuffUtil getStructureStuffUtil()
	{
		return structureStuffUtil;
	}

	public void setStructureStuffUtil( final StructureStuffUtil structureStuffUtil )
	{
		this.structureStuffUtil = structureStuffUtil;
	}

}
