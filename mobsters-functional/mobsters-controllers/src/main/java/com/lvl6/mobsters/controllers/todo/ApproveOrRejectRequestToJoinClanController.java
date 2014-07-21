//package com.lvl6.mobsters.controllers.todo;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.Clan;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.ClanForUser;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanRequestProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto.ApproveOrRejectRequestToJoinClanStatus;
//import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto.Builder;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.ApproveOrRejectRequestToJoinClanRequestEvent;
//import com.lvl6.mobsters.events.response.ApproveOrRejectRequestToJoinClanResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class ApproveOrRejectRequestToJoinClanController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(ApproveOrRejectRequestToJoinClanController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public ApproveOrRejectRequestToJoinClanController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new ApproveOrRejectRequestToJoinClanRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final ApproveOrRejectRequestToJoinClanRequestProto reqProto =
//		    ((ApproveOrRejectRequestToJoinClanRequestEvent) event).getApproveOrRejectRequestToJoinClanRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final int requesterId = reqProto.getRequesterUuid();
//		final boolean accept = reqProto.getAccept();
//
//		final ApproveOrRejectRequestToJoinClanResponseProto.Builder resBuilder =
//		    ApproveOrRejectRequestToJoinClanResponseProto.newBuilder();
//		resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.FAIL_OTHER);
//		resBuilder.setSender(senderProto);
//		resBuilder.setAccept(accept);
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
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			final User requester = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(requesterId);
//
//			final List<Integer> clanSizeList = new ArrayList<Integer>();
//			final boolean legitDecision =
//			    checkLegitDecision(resBuilder, lockedClan, user, requester, accept,
//			        clanSizeList);
//
//			boolean success = false;
//			if (legitDecision) {
//				success = writeChangesToDB(user, requester, accept);
//			}
//
//			if (success) {
//				resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.SUCCESS);
//				setResponseBuilderStuff(resBuilder, clanId, clanSizeList);
//				final MinimumUserProto requestMup =
//				    CreateInfoProtoUtils.createMinimumUserProtoFromUser(requester);
//				resBuilder.setRequester(requestMup);
//			}
//
//			final ApproveOrRejectRequestToJoinClanResponseEvent resEvent =
//			    new ApproveOrRejectRequestToJoinClanResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setApproveOrRejectRequestToJoinClanResponseProto(resBuilder.build());
//
//			// if fail only to sender
//			if (!success) {
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in ApproveOrRejectRequestToJoinClanController.processRequestEvent",
//					    e);
//				}
//			} else {
//				// if success to clan and the requester
//				server.writeClanEvent(resEvent, clanId);
//				// Send message to the new guy
//				final ApproveOrRejectRequestToJoinClanResponseEvent resEvent2 =
//				    new ApproveOrRejectRequestToJoinClanResponseEvent(requesterId);
//				resEvent2.setApproveOrRejectRequestToJoinClanResponseProto(resBuilder.build());
//				// in case user is not online write an apns
//				server.writeAPNSNotificationOrEvent(resEvent2);
//				// server.writeEvent(resEvent2);
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in ApproveOrRejectRequestToJoinClan processEvent", e);
//			try {
//				resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.FAIL_OTHER);
//				final ApproveOrRejectRequestToJoinClanResponseEvent resEvent =
//				    new ApproveOrRejectRequestToJoinClanResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setApproveOrRejectRequestToJoinClanResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in ApproveOrRejectRequestToJoinClanController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in ApproveOrRejectRequestToJoinClan processEvent", e);
//			}
//		} finally {
//			if ((0 != clanId)
//			    && lockedClan) {
//				getLocker().unlockClan(clanId);
//			}
//		}
//	}
//
//	private boolean checkLegitDecision( final Builder resBuilder, final boolean lockedClan,
//	    final User user, final User requester, final boolean accept,
//	    final List<Integer> clanSizeList )
//	{
//
//		if (!lockedClan) {
//			LOG.error("could not get lock for clan.");
//			return false;
//		}
//
//		if ((user == null)
//		    || (requester == null)) {
//			resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.FAIL_OTHER);
//			LOG.error("user is "
//			    + user
//			    + ", requester is "
//			    + requester);
//			return false;
//		}
//		// Clan clan = ClanRetrieveUtils.getClanWithId(user.getClanId());
//		final int clanId = user.getClanId();
//		final List<String> statuses = new ArrayList<String>();
//		statuses.add(UserClanStatus.LEADER.name());
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
//		final List<Integer> userUuids = RetrieveUtils.userClanRetrieveUtils()
//		    .getUserUuidsWithStatuses(clanId, statuses);
//		// should just be one id
//		final Set<Integer> uniqUserUuids = new HashSet<Integer>();
//		if ((null != userUuids)
//		    && !userUuids.isEmpty()) {
//			uniqUserUuids.addAll(userUuids);
//		}
//
//		final String userUuid = user.getId();
//		if (!uniqUserUuids.contains(userUuid)) {
//			resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.FAIL_NOT_AUTHORIZED);
//			LOG.error("clan member can't approve clan join request. member="
//			    + user
//			    + "\t requester="
//			    + requester);
//			return false;
//		}
//		// check if requester is already in a clan
//		if (0 < requester.getClanId()) {
//			resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.FAIL_ALREADY_IN_A_CLAN);
//			LOG.error("trying to accept a user that is already in a clan");
//			// the other requests in user_clans table that have a status of 2
//			// (requesting to join clan)
//			// are deleted later on in writeChangesToDB
//			return false;
//		}
//
//		final ClanForUser uc = RetrieveUtils.userClanRetrieveUtils()
//		    .getSpecificUserClan(requester.getId(), clanId);
//		if ((uc == null)
//		    || !UserClanStatus.REQUESTING.name()
//		        .equals(uc.getStatus())) {
//			resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.FAIL_NOT_A_REQUESTER);
//			LOG.error("requester has not requested for clan with id "
//			    + user.getClanId());
//			return false;
//		}
//		if ((ControllerConstants.CLAN__ALLIANCE_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT == clanId)
//		    || (ControllerConstants.CLAN__LEGION_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT == clanId)) {
//			return true;
//		}
//
//		// check out the size of the clan
//		final List<Integer> clanIdList = Collections.singletonList(clanId);
//		// add in captain and member to existing leader and junior leader list
//		statuses.add(UserClanStatus.CAPTAIN.name());
//		statuses.add(UserClanStatus.MEMBER.name());
//		final Map<Integer, Integer> clanIdToSize = RetrieveUtils.userClanRetrieveUtils()
//		    .getClanSizeForClanUuidsAndStatuses(clanIdList, statuses);
//
//		final int size = clanIdToSize.get(clanId);
//		final int maxSize = ControllerConstants.CLAN__MAX_NUM_MEMBERS;
//		if ((size >= maxSize)
//		    && accept) {
//			resBuilder.setStatus(ApproveOrRejectRequestToJoinClanStatus.FAIL_CLAN_IS_FULL);
//			LOG.warn("user error: trying to add user into already full clan with id "
//			    + user.getClanId());
//			return false;
//		}
//
//		clanSizeList.add(size);
//		return true;
//	}
//
//	private boolean writeChangesToDB( final User user, final User requester,
//	    final boolean accept )
//	{
//		if (accept) {
//			if (!requester.updateRelativeCoinsAbsoluteClan(0, user.getClanId())) {
//				LOG.error("problem with change requester "
//				    + requester
//				    + " clan id to "
//				    + user.getClanId());
//				return false;
//			}
//			if (!UpdateUtils.get()
//			    .updateUserClanStatus(requester.getId(), user.getClanId(),
//			        UserClanStatus.MEMBER)) {
//				LOG.error("problem with updating user clan status to member for requester "
//				    + requester
//				    + " and clan id "
//				    + user.getClanId());
//				return false;
//			}
//			DeleteUtils.get()
//			    .deleteUserClansForUserExceptSpecificClan(requester.getId(), user.getClanId());
//			return true;
//		} else {
//			if (!DeleteUtils.get()
//			    .deleteUserClan(requester.getId(), user.getClanId())) {
//				LOG.error("problem with deleting user clan info for requester with id "
//				    + requester.getId()
//				    + " and clan id "
//				    + user.getClanId());
//				return false;
//			}
//			return true;
//		}
//	}
//
//	private void setResponseBuilderStuff( final Builder resBuilder, final int clanId,
//	    final List<Integer> clanSizeList )
//	{
//		final Clan clan = ClanRetrieveUtils.getClanWithId(clanId);
//		resBuilder.setMinClan(CreateInfoProtoUtils.createMinimumClanProtoFromClan(clan));
//
//		final int size = clanSizeList.get(0);
//		resBuilder.setFullClan(CreateInfoProtoUtils.createFullClanProtoWithClanSize(clan, size));
//	}
//
//}
