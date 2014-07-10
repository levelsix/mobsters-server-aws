package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan;
import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanResponseProto.RequestJoinClanStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.RequestJoinClanRequestEvent;
import com.lvl6.mobsters.events.response.RequestJoinClanResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.MinimumUserProtoForClans;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventClanInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventUserInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserCurrentMonsterTeamProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class RequestJoinClanController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(RequestJoinClanController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil;

	public RequestJoinClanController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RequestJoinClanRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_REQUEST_JOIN_CLAN_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final RequestJoinClanRequestProto reqProto =
		    ((RequestJoinClanRequestEvent) event).getRequestJoinClanRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final int clanId = reqProto.getClanUuid();
		final String userUuid = senderProto.getUserUuid();

		final RequestJoinClanResponseProto.Builder resBuilder =
		    RequestJoinClanResponseProto.newBuilder();
		resBuilder.setStatus(RequestJoinClanStatus.FAIL_OTHER);
		resBuilder.setSender(senderProto);
		resBuilder.setClanId(clanId);

		boolean lockedClan = false;
		if (0 != clanId) {
			lockedClan = getLocker().lockClan(clanId);
		}
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			final Clan clan = ClanRetrieveUtils.getClanWithId(clanId);
			boolean requestToJoinRequired = false; // to be set if request is
			                                       // legit

			final List<Integer> clanSizeList = new ArrayList<Integer>();
			final boolean legitRequest =
			    checkLegitRequest(resBuilder, lockedClan, user, clan, clanSizeList);

			boolean successful = false;
			if (legitRequest) {
				requestToJoinRequired = clan.isRequestToJoinRequired();
				final int battlesWon =
				    getPvpLeagueForUserRetrieveUtil().getPvpBattlesWonForUser(userUuid);

				// setting minimum user proto for clans based on clan join type
				if (requestToJoinRequired) {
					// clan raid contribution stuff
					final MinimumUserProtoForClans mupfc =
					    CreateInfoProtoUtils.createMinimumUserProtoForClans(user,
					        UserClanStatus.REQUESTING, 0F, battlesWon);
					resBuilder.setRequester(mupfc);
				} else {
					// clan raid contribution stuff
					final MinimumUserProtoForClans mupfc =
					    CreateInfoProtoUtils.createMinimumUserProtoForClans(user,
					        UserClanStatus.MEMBER, 0F, battlesWon);
					resBuilder.setRequester(mupfc);
				}
				successful = writeChangesToDB(resBuilder, user, clan);
			}

			if (successful) {
				setResponseBuilderStuff(resBuilder, clan, clanSizeList);
				sendClanRaidStuff(resBuilder, clan, user);
			}
			final RequestJoinClanResponseEvent resEvent =
			    new RequestJoinClanResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setRequestJoinClanResponseProto(resBuilder.build());
			/*
			 * I think I meant write to the clan leader if leader is not on
			 * 
			 * //in case user is not online write an apns
			 * server.writeAPNSNotificationOrEvent(resEvent);
			 * //server.writeEvent(resEvent);
			 */
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in RequestJoinClanController.processRequestEvent", e);
			}

			if (successful) {
				final List<Integer> userUuids = new ArrayList<Integer>();
				userUuids.add(userUuid);
				// get user's current monster team
				final Map<Integer, List<MonsterForUser>> userIdToTeam =
				    RetrieveUtils.monsterForUserRetrieveUtils()
				        .getUserUuidsToMonsterTeamForUserUuids(userUuids);
				final UserCurrentMonsterTeamProto curTeamProto =
				    CreateInfoProtoUtils.createUserCurrentMonsterTeamProto(userUuid,
				        userIdToTeam.get(userUuid));
				resBuilder.setRequesterMonsters(curTeamProto);

				resBuilder.clearEventDetails(); // could just get rid of this
				                                // line
				resBuilder.clearClanUsersDetails(); // could just get rid of
				                                    // this line
				resEvent.setRequestJoinClanResponseProto(resBuilder.build());
				server.writeClanEvent(resEvent, clan.getId());

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
					LOG.error(
					    "fatal exception in RequestJoinClanController.processRequestEvent", e);
				}

				notifyClan(user, clan, requestToJoinRequired); // write to clan
				                                               // leader or clan
			}
		} catch (final Exception e) {
			LOG.error("exception in RequestJoinClan processEvent", e);
			try {
				resBuilder.setStatus(RequestJoinClanStatus.FAIL_OTHER);
				final RequestJoinClanResponseEvent resEvent =
				    new RequestJoinClanResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setRequestJoinClanResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in RequestJoinClanController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in RequestJoinClan processEvent", e);
			}
		} finally {
			if (0 != clanId) {
				getLocker().unlockClan(clanId);
			}
		}
	}

	private boolean checkLegitRequest( final Builder resBuilder, final boolean lockedClan,
	    final User user, final Clan clan, final List<Integer> clanSizeList )
	{

		if (!lockedClan) {
			LOG.error("couldn't obtain clan lock");
			return false;
		}
		if ((user == null)
		    || (clan == null)) {
			resBuilder.setStatus(RequestJoinClanStatus.FAIL_OTHER);
			LOG.error("user is "
			    + user
			    + ", clan is "
			    + clan);
			return false;
		}
		int clanId = user.getClanId();
		if (clanId > 0) {
			resBuilder.setStatus(RequestJoinClanStatus.FAIL_ALREADY_IN_CLAN);
			LOG.error("user is already in clan with id "
			    + clanId);
			return false;
		}

		// user does not have clan id, so use the clan's id
		clanId = clan.getId();
		// if (clan.isGood() != MiscMethods.checkIfGoodSide(user.getType())) {
		// resBuilder.setStatus(RequestJoinClanStatus.WRONG_SIDE);
		// LOG.error("user is good " + user.getType() + ", user type is good " +
		// user.getType());
		// return false;
		// }
		final ClanForUser uc = RetrieveUtils.userClanRetrieveUtils()
		    .getSpecificUserClan(user.getId(), clanId);
		if (uc != null) {
			resBuilder.setStatus(RequestJoinClanStatus.FAIL_REQUEST_ALREADY_FILED);
			LOG.error("user clan already exists for this: "
			    + uc);
			return false;
		}

		if ((ControllerConstants.CLAN__ALLIANCE_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT == clanId)
		    || (ControllerConstants.CLAN__LEGION_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT == clanId)) {
			return true;
		}

		// can request as much as desired
		if (clan.isRequestToJoinRequired()) {
			return true;
		}

		// check out the size of the clan since user can just join

		final List<Integer> clanIdList = Collections.singletonList(clanId);
		final List<String> statuses = new ArrayList<String>();
		statuses.add(UserClanStatus.LEADER.name());
		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
		statuses.add(UserClanStatus.CAPTAIN.name());
		statuses.add(UserClanStatus.MEMBER.name());
		final Map<Integer, Integer> clanIdToSize = RetrieveUtils.userClanRetrieveUtils()
		    .getClanSizeForClanUuidsAndStatuses(clanIdList, statuses);

		final int size = clanIdToSize.get(clanId);
		final int maxSize = ControllerConstants.CLAN__MAX_NUM_MEMBERS;
		if (size >= maxSize) {
			resBuilder.setStatus(RequestJoinClanStatus.FAIL_CLAN_IS_FULL);
			LOG.warn("user error: trying to join full clan with id "
			    + clanId
			    + " cur size="
			    + maxSize);
			return false;
		}
		clanSizeList.add(size);
		// resBuilder.setStatus(RequestJoinClanStatus.SUCCESS);
		return true;
	}

	private boolean writeChangesToDB( final Builder resBuilder, final User user, final Clan clan )
	{
		// clan can be open, or user needs to send a request to join the clan
		final boolean requestToJoinRequired = clan.isRequestToJoinRequired();
		final String userUuid = user.getId();
		final int clanId = clan.getId(); // user.getClanId(); //this is null
										 // still...
		String userClanStatus;
		if (requestToJoinRequired) {
			userClanStatus = UserClanStatus.REQUESTING.name();
			resBuilder.setStatus(RequestJoinClanStatus.SUCCESS_REQUEST);
		} else {
			userClanStatus = UserClanStatus.MEMBER.name();
			resBuilder.setStatus(RequestJoinClanStatus.SUCCESS_JOIN);
		}

		if (!InsertUtils.get()
		    .insertUserClan(userUuid, clanId, userClanStatus,
		        new Timestamp(new Date().getTime()))) {
			LOG.error("unexpected error: problem with inserting user clan data for user "
			    + user
			    + ", and clan id "
			    + clanId);
			resBuilder.setStatus(RequestJoinClanStatus.FAIL_OTHER);
			return false;
		}

		boolean deleteUserClanInserted = false;
		// update user to reflect he joined clan if the clan does not require a
		// request to join
		if (!requestToJoinRequired) {
			if (!user.updateRelativeCoinsAbsoluteClan(0, clanId)) {
				// could not change clan_id for user
				LOG.error("unexpected error: could not change clan id for requester "
				    + user
				    + " to "
				    + clanId
				    + ". Deleting user clan that was just created.");
				deleteUserClanInserted = true;
			} else {
				// successfully changed clan_id in current user
				// get rid of all other join clan requests
				// don't know if this next line will always work...
				DeleteUtils.get()
				    .deleteUserClansForUserExceptSpecificClan(userUuid, clanId);
			}
		}

		boolean successful = true;
		// in case things above didn't work out
		if (deleteUserClanInserted) {
			if (!DeleteUtils.get()
			    .deleteUserClan(userUuid, clanId)) {
				LOG.error("unexpected error: could not delete user clan inserted.");
			}
			resBuilder.setStatus(RequestJoinClanStatus.FAIL_OTHER);
			successful = false;
		}
		return successful;
	}

	private void notifyClan( final User aUser, final Clan aClan,
	    final boolean requestToJoinRequired )
	{
		final int clanId = aUser.getClanId();
		final List<String> statuses = Collections.singletonList(UserClanStatus.LEADER.name());
		final List<Integer> userUuids = RetrieveUtils.userClanRetrieveUtils()
		    .getUserUuidsWithStatuses(clanId, statuses);
		// should just be one id
		int clanOwnerId = 0;
		if ((null != userUuids)
		    && !userUuids.isEmpty()) {
			clanOwnerId = userUuids.get(0);
		}

		final int level = aUser.getLevel();
		final String requester = aUser.getName();
		final Notification aNote = new Notification();

		if (requestToJoinRequired) {
			// notify leader someone requested to join clan
			aNote.setAsUserRequestedToJoinClan(level, requester);
		} else {
			// notify whole clan someone joined the clan <- too annoying, just
			// leader
			// TODO: Maybe exclude the guy who joined from receiving the
			// notification?
			aNote.setAsUserJoinedClan(level, requester);
		}
		MiscMethods.writeNotificationToUser(aNote, server, clanOwnerId);

		// GeneralNotificationResponseProto.Builder notificationProto =
		// aNote.generateNotificationBuilder();
		// GeneralNotificationResponseEvent aNotification =
		// new GeneralNotificationResponseEvent(clanOwnerId);
		// aNotification.setGeneralNotificationResponseProto(notificationProto.build());
		//
		// server.writeAPNSNotificationOrEvent(aNotification);
	}

	private void setResponseBuilderStuff( final Builder resBuilder, final Clan clan,
	    final List<Integer> clanSizeList )
	{

		resBuilder.setMinClan(CreateInfoProtoUtils.createMinimumClanProtoFromClan(clan));
		final int size = clanSizeList.get(0);
		resBuilder.setFullClan(CreateInfoProtoUtils.createFullClanProtoWithClanSize(clan, size));
	}

	private void sendClanRaidStuff( final Builder resBuilder, final Clan clan, final User user )
	{
		if (!RequestJoinClanStatus.SUCCESS_JOIN.equals(resBuilder.getStatus())) {
			return;
		}

		final int clanId = clan.getId();
		final ClanEventPersistentForClan cepfc =
		    ClanEventPersistentForClanRetrieveUtils.getPersistentEventForClanId(clanId);

		// send to the user the current clan raid details for the clan
		if (null != cepfc) {
			final PersistentClanEventClanInfoProto updatedEventDetails =
			    CreateInfoProtoUtils.createPersistentClanEventClanInfoProto(cepfc);
			resBuilder.setEventDetails(updatedEventDetails);
		}

		final Map<Integer, ClanEventPersistentForUser> userIdToCepfu =
		    ClanEventPersistentForUserRetrieveUtils.getPersistentEventUserInfoForClanId(clanId);

		// send to the user the current clan raid details for all the users
		if (!userIdToCepfu.isEmpty()) {
			// whenever server has this information send it to the clients
			final List<Long> userMonsterUuids =
			    MonsterStuffUtils.getUserMonsterUuidsInClanRaid(userIdToCepfu);

			final Map<Long, MonsterForUser> idsToUserMonsters =
			    RetrieveUtils.monsterForUserRetrieveUtils()
			        .getSpecificUserMonsters(userMonsterUuids);

			for (final ClanEventPersistentForUser cepfu : userIdToCepfu.values()) {
				final PersistentClanEventUserInfoProto pceuip =
				    CreateInfoProtoUtils.createPersistentClanEventUserInfoProto(cepfu,
				        idsToUserMonsters, null);
				resBuilder.addClanUsersDetails(pceuip);
			}

		}
	}

	public PvpLeagueForUserRetrieveUtil getPvpLeagueForUserRetrieveUtil()
	{
		return pvpLeagueForUserRetrieveUtil;
	}

	public void setPvpLeagueForUserRetrieveUtil(
	    final PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil )
	{
		this.pvpLeagueForUserRetrieveUtil = pvpLeagueForUserRetrieveUtil;
	}

}
