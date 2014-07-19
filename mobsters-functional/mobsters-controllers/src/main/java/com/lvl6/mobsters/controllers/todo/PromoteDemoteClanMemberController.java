//package com.lvl6.mobsters.controllers.todo;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.ClanForUser;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberRequestProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberResponseProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberResponseProto.PromoteDemoteClanMemberStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.PromoteDemoteClanMemberRequestEvent;
//import com.lvl6.mobsters.events.response.PromoteDemoteClanMemberResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class PromoteDemoteClanMemberController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(PromoteDemoteClanMemberController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public PromoteDemoteClanMemberController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new PromoteDemoteClanMemberRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_PROMOTE_DEMOTE_CLAN_MEMBER_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final PromoteDemoteClanMemberRequestProto reqProto =
//		    ((PromoteDemoteClanMemberRequestEvent) event).getPromoteDemoteClanMemberRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final int victimId = reqProto.getVictimUuid();
//		final UserClanStatus newUserClanStatus = reqProto.getUserClanStatus();
//		final List<Integer> userUuids = new ArrayList<Integer>();
//		userUuids.add(userUuid);
//		userUuids.add(victimId);
//
//		final PromoteDemoteClanMemberResponseProto.Builder resBuilder =
//		    PromoteDemoteClanMemberResponseProto.newBuilder();
//		resBuilder.setStatus(PromoteDemoteClanMemberStatus.FAIL_OTHER);
//		resBuilder.setSender(senderProto);
//		resBuilder.setUserClanStatus(newUserClanStatus);
//
//		int clanId = 0;
//		if (senderProto.hasClan()
//		    && (null != senderProto.getClan())) {
//			clanId = senderProto.getClan()
//			    .getClanId();
//		}
//
//		boolean lockedClan = false;
//		if (0 != clanId) {
//			lockedClan = getLocker().lockClan(clanId);
//		}
//		try {
//			final Map<Integer, User> users = RetrieveUtils.userRetrieveUtils()
//			    .getUsersByUuids(userUuids);
//			final Map<Integer, ClanForUser> userClans = RetrieveUtils.userClanRetrieveUtils()
//			    .getUserClanForUsers(clanId, userUuids);
//
//			final boolean legitRequest =
//			    checkLegitRequest(resBuilder, lockedClan, userUuid, victimId,
//			        newUserClanStatus, users, userClans);
//
//			boolean success = false;
//			if (legitRequest) {
//				final User victim = users.get(victimId);
//				final ClanForUser oldInfo = userClans.get(victimId);
//				try {
//					final UserClanStatus ucs = UserClanStatus.valueOf(oldInfo.getStatus());
//					resBuilder.setPrevUserClanStatus(ucs);
//				} catch (final Exception e) {
//					LOG.error("incorrect user clan status. userClan="
//					    + oldInfo);
//				}
//
//				success =
//				    writeChangesToDB(victim, victimId, clanId, oldInfo, newUserClanStatus);
//			}
//
//			final PromoteDemoteClanMemberResponseEvent resEvent =
//			    new PromoteDemoteClanMemberResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			// only write to user if failed
//			if (!success) {
//				resEvent.setPromoteDemoteClanMemberResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in PromoteDemoteClanMemberController.processRequestEvent",
//					    e);
//				}
//
//			} else {
//				// only write to clan if success
//				resBuilder.setStatus(PromoteDemoteClanMemberStatus.SUCCESS);
//				final User victim = users.get(victimId);
//				final MinimumUserProto mup =
//				    CreateInfoProtoUtils.createMinimumUserProtoFromUser(victim);
//				resBuilder.setVictim(mup);
//
//				resEvent.setPromoteDemoteClanMemberResponseProto(resBuilder.build());
//				server.writeClanEvent(resEvent, clanId);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in PromoteDemoteClanMember processEvent", e);
//			try {
//				resBuilder.setStatus(PromoteDemoteClanMemberStatus.FAIL_OTHER);
//				final PromoteDemoteClanMemberResponseEvent resEvent =
//				    new PromoteDemoteClanMemberResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setPromoteDemoteClanMemberResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in PromoteDemoteClanMemberController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in PromoteDemoteClanMember processEvent", e);
//			}
//		} finally {
//			if (0 != clanId) {
//				getLocker().unlockClan(clanId);
//			}
//		}
//	}
//
//	private boolean checkLegitRequest( final Builder resBuilder, final boolean lockedClan,
//	    final String userUuid, final int victimId, final UserClanStatus newUserClanStatus,
//	    final Map<Integer, User> userUuidsToUsers,
//	    final Map<Integer, ClanForUser> userUuidsToUserClans )
//	{
//
//		if (!lockedClan) {
//			LOG.error("couldn't obtain clan lock");
//			return false;
//		}
//		if ((null == userUuidsToUsers)
//		    || (userUuidsToUsers.size() != 2)
//		    || (null == userUuidsToUserClans)
//		    || (userUuidsToUserClans.size() != 2)) {
//			LOG.error("user or userClan objects do not total 2. users="
//			    + userUuidsToUsers
//			    + "\t userUuidsToUserClans="
//			    + userUuidsToUserClans);
//			return false;
//		}
//
//		// check if users are in the db
//		if (!userUuidsToUserClans.containsKey(userUuid)
//		    || !userUuidsToUsers.containsKey(userUuid)) {
//			LOG.error("user promoting or demoting not in clan or db. userUuid="
//			    + userUuid
//			    + "\t userUuidsToUserClans="
//			    + userUuidsToUserClans
//			    + "\t userUuidsToUsers="
//			    + userUuidsToUsers);
//			resBuilder.setStatus(PromoteDemoteClanMemberStatus.FAIL_NOT_IN_CLAN);
//			return false;
//		}
//		if (!userUuidsToUserClans.containsKey(victimId)
//		    || !userUuidsToUsers.containsKey(victimId)) {
//			LOG.error("user to be promoted or demoted not in clan or db. victim="
//			    + victimId
//			    + "\t userUuidsToUserClans="
//			    + userUuidsToUserClans
//			    + "\t userUuidsToUsers="
//			    + userUuidsToUsers);
//			resBuilder.setStatus(PromoteDemoteClanMemberStatus.FAIL_NOT_IN_CLAN);
//			return false;
//		}
//
//		// check if user can demote/promote the other one
//		final ClanForUser promoterDemoter = userUuidsToUserClans.get(userUuid);
//		final ClanForUser victim = userUuidsToUserClans.get(victimId);
//
//		final UserClanStatus first = UserClanStatus.valueOf(promoterDemoter.getStatus());
//		final UserClanStatus second = UserClanStatus.valueOf(victim.getStatus());
//		if (UserClanStatus.CAPTAIN.equals(first)
//		    || !ClanStuffUtils.firstUserClanStatusAboveSecond(first, second)) {
//			LOG.error("user not authorized to promote or demote otherUser. clanStatus of user="
//			    + first
//			    + "\t clanStatus of other user="
//			    + second);
//			resBuilder.setStatus(PromoteDemoteClanMemberStatus.FAIL_NOT_AUTHORIZED);
//			return false;
//		}
//		if (!ClanStuffUtils.firstUserClanStatusAboveSecond(first, newUserClanStatus)) {
//			LOG.error("user not authorized to promote or demote otherUser. clanStatus of user="
//			    + first
//			    + "\t clanStatus of other user="
//			    + second
//			    + "\t newClanStatus of other user="
//			    + newUserClanStatus);
//			resBuilder.setStatus(PromoteDemoteClanMemberStatus.FAIL_NOT_AUTHORIZED);
//			return false;
//		}
//		if (UserClanStatus.REQUESTING.equals(second)) {
//			LOG.error("user can't promote, demote a non-clan member. UserClan for user="
//			    + promoterDemoter
//			    + "\t UserClan for victim="
//			    + victim
//			    + "\t users="
//			    + userUuidsToUsers);
//			resBuilder.setStatus(PromoteDemoteClanMemberStatus.FAIL_NOT_AUTHORIZED);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean writeChangesToDB( final User victim, final int victimId, final int clanId,
//	    final ClanForUser oldInfo, final UserClanStatus newUserClanStatus )
//	{
//		if (!UpdateUtils.get()
//		    .updateUserClanStatus(victimId, clanId, newUserClanStatus)) {
//			LOG.error("problem with updating user clan status for user="
//			    + victim
//			    + "\t oldInfo="
//			    + oldInfo
//			    + "\t newUserClanStatus="
//			    + newUserClanStatus);
//			return false;
//		}
//		return true;
//	}
//
//}
