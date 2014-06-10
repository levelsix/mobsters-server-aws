package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityRequestProto;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityResponseProto;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityResponseProto.LoadPlayerCityStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.LoadPlayerCityRequestEvent;
import com.lvl6.mobsters.events.response.LoadPlayerCityResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.UserObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class LoadPlayerCityController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(LoadPlayerCityController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil;

	public LoadPlayerCityController()
	{
		numAllocatedThreads = 10;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new LoadPlayerCityRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_LOAD_PLAYER_CITY_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final LoadPlayerCityRequestProto reqProto =
		    ((LoadPlayerCityRequestEvent) event).getLoadPlayerCityRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final int cityOwnerId = reqProto.getCityOwnerUuid();

		final LoadPlayerCityResponseProto.Builder resBuilder =
		    LoadPlayerCityResponseProto.newBuilder();
		resBuilder.setSender(senderProto);

		resBuilder.setStatus(LoadPlayerCityStatus.SUCCESS);

		// I guess in case someone attacks this guy while loading the city, want
		// both people to have one consistent view
		svcTxManager.beginTransaction();
		try {
			final User owner = RetrieveUtils.userRetrieveUtils()
			    .getUserById(cityOwnerId);

			final List<StructureForUser> userStructs = RetrieveUtils.userStructRetrieveUtils()
			    .getUserStructsForUser(cityOwnerId);
			setResponseUserStructs(resBuilder, userStructs);
			setObstacleStuff(resBuilder, cityOwnerId);

			final List<ExpansionPurchaseForUser> userCityExpansionDataList =
			    ExpansionPurchaseForUserRetrieveUtils.getUserCityExpansionDatasForUserUuid(senderProto.getUserUuid());
			final List<UserCityExpansionDataProto> userCityExpansionDataProtoList =
			    new ArrayList<UserCityExpansionDataProto>();
			if (userCityExpansionDataList != null) {
				for (final ExpansionPurchaseForUser uced : userCityExpansionDataList) {
					userCityExpansionDataProtoList.add(CreateInfoProtoUtils.createUserCityExpansionDataProtoFromUserCityExpansionData(uced));
				}
				resBuilder.addAllUserCityExpansionDataProtoList(userCityExpansionDataProtoList);
			}

			if (owner == null) {
				LOG.error("owner is null for ownerId = "
				    + cityOwnerId);
			} else {
				resBuilder.setCityOwner(CreateInfoProtoUtils.createMinimumUserProtoFromUser(owner));

				final LoadPlayerCityResponseEvent resEvent =
				    new LoadPlayerCityResponseEvent(senderProto.getUserUuid());
				resEvent.setTag(event.getTag());
				resEvent.setLoadPlayerCityResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in LoadPlayerCityController.processRequestEvent", e);
				}

			}
		} catch (final Exception e) {
			LOG.error("exception in LoadPlayerCity processEvent", e);
		} finally {
			svcTxManager.commit();
		}
	}

	private void setResponseUserStructs( final Builder resBuilder,
	    final List<StructureForUser> userStructs )
	{
		if (userStructs != null) {
			for (final StructureForUser userStruct : userStructs) {
				resBuilder.addOwnerNormStructs(CreateInfoProtoUtils.createFullUserStructureProtoFromUserstruct(userStruct));
			}
		} else {
			resBuilder.setStatus(LoadPlayerCityStatus.FAIL_OTHER);
			LOG.error("user structs found for user is null");
		}
	}

	private void setObstacleStuff( final Builder resBuilder, final String userUuid )
	{
		final List<ObstacleForUser> ofuList =
		    getObstacleForUserRetrieveUtil().getUserObstacleForUser(userUuid);

		if (null == ofuList) {
			return;
		}

		for (final ObstacleForUser ofu : ofuList) {
			final UserObstacleProto uop = CreateInfoProtoUtils.createUserObstacleProto(ofu);
			resBuilder.addObstacles(uop);
		}

	}

	public ObstacleForUserRetrieveUtil getObstacleForUserRetrieveUtil()
	{
		return obstacleForUserRetrieveUtil;
	}

	public void setObstacleForUserRetrieveUtil(
	    final ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil )
	{
		this.obstacleForUserRetrieveUtil = obstacleForUserRetrieveUtil;
	}

}
