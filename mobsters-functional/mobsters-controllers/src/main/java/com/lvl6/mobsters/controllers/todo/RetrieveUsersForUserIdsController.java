package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserCurrentMonsterTeamProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class RetrieveUsersForUserUuidsController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(RetrieveUsersForUserUuidsController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected HazelcastPvpUtil hazelcastPvpUtil;

	public RetrieveUsersForUserUuidsController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RetrieveUsersForUserUuidsRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_RETRIEVE_USERS_FOR_USER_IDS_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final RetrieveUsersForUserUuidsRequestProto reqProto =
		    ((RetrieveUsersForUserUuidsRequestEvent) event).getRetrieveUsersForUserUuidsRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final List<Integer> requestedUserUuids = reqProto.getRequestedUserUuidsList();
		final boolean includeCurMonsterTeam = reqProto.getIncludeCurMonsterTeam();

		final RetrieveUsersForUserUuidsResponseProto.Builder resBuilder =
		    RetrieveUsersForUserUuidsResponseProto.newBuilder();
		resBuilder.setSender(senderProto);

		// boolean includePotentialPoints =
		// reqProto.getIncludePotentialPointsForClanTowers();
		// User sender = includePotentialPoints ?
		// RetrieveUtils.userRetrieveUtils().getUserById(senderProto.getUserUuid())
		// : null;
		final Map<Integer, User> usersByUuids = RetrieveUtils.userRetrieveUtils()
		    .getUsersByUuids(requestedUserUuids);
		if (usersByUuids != null) {
			for (final User user : usersByUuids.values()) {

				// TODO: consider getting from db
				// pull from hazelcast for now
				final String userUuid = user.getId();
				final PvpUser pu = getHazelcastPvpUtil().getPvpUser(userUuid);
				PvpLeagueForUser plfu = null;

				if (null != pu) {
					plfu = new PvpLeagueForUser(pu);
				}
				resBuilder.addRequestedUsers(CreateInfoProtoUtils.createFullUserProtoFromUser(
				    user, plfu));

			}

			List<UserCurrentMonsterTeamProto> teams = null;
			if (includeCurMonsterTeam) {
				teams = constructTeamsForUsers(requestedUserUuids);
			}

			if ((null != teams)
			    && !teams.isEmpty()) {
				resBuilder.addAllCurTeam(teams);
			}

		} else {
			LOG.error("no users with the ids "
			    + requestedUserUuids);
		}
		final RetrieveUsersForUserUuidsResponseProto resProto = resBuilder.build();
		final RetrieveUsersForUserUuidsResponseEvent resEvent =
		    new RetrieveUsersForUserUuidsResponseEvent(senderProto.getUserUuid());
		resEvent.setTag(event.getTag());
		resEvent.setRetrieveUsersForUserUuidsResponseProto(resProto);
		// write to client
		LOG.info("Writing event: "
		    + resEvent);
		try {
			eventWriter.writeEvent(resEvent);
		} catch (final Throwable e) {
			LOG.error(
			    "fatal exception in RetrieveUsersForUserUuidsController.processRequestEvent", e);
		}
	}

	private List<UserCurrentMonsterTeamProto> constructTeamsForUsers(
	    final List<Integer> userUuids )
	{
		final Map<Integer, List<MonsterForUser>> userUuidsToCurrentTeam =
		    RetrieveUtils.monsterForUserRetrieveUtils()
		        .getUserUuidsToMonsterTeamForUserUuids(userUuids);

		// for each user construct his current team
		final List<UserCurrentMonsterTeamProto> retVal =
		    new ArrayList<UserCurrentMonsterTeamProto>();
		for (final Integer userUuid : userUuidsToCurrentTeam.keySet()) {
			final List<MonsterForUser> currentTeam = userUuidsToCurrentTeam.get(userUuid);

			final List<FullUserMonsterProto> currentTeamProto =
			    CreateInfoProtoUtils.createFullUserMonsterProtoList(currentTeam);

			// create the proto via the builder
			UserCurrentMonsterTeamProto.Builder teamForUser =
			    UserCurrentMonsterTeamProto.newBuilder();
			teamForUser.setUserUuid(userUuid);
			teamForUser.addAllCurrentTeam(currentTeamProto);

			retVal.add(teamForUser.build());
		}

		return retVal;
	}

	public HazelcastPvpUtil getHazelcastPvpUtil()
	{
		return hazelcastPvpUtil;
	}

	public void setHazelcastPvpUtil( final HazelcastPvpUtil hazelcastPvpUtil )
	{
		this.hazelcastPvpUtil = hazelcastPvpUtil;
	}

}
