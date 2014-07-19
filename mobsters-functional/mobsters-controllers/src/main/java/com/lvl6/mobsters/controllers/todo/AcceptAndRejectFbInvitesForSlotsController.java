//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
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
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsRequestProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto.AcceptAndRejectFbInviteForSlotsStatus;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto.Builder;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.AcceptAndRejectFbInviteForSlotsRequestEvent;
//import com.lvl6.mobsters.events.response.AcceptAndRejectFbInviteForSlotsResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithFacebookId;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.UserFacebookInviteForSlotProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class AcceptAndRejectFbInvitesForSlotsController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(AcceptAndRejectFbInvitesForSlotsController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public AcceptAndRejectFbInvitesForSlotsController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new AcceptAndRejectFbInviteForSlotsRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final AcceptAndRejectFbInviteForSlotsRequestProto reqProto =
//		    ((AcceptAndRejectFbInviteForSlotsRequestEvent) event).getAcceptAndRejectFbInviteForSlotsRequestProto();
//
//		LOG.info("reqProto="
//		    + reqProto);
//		// get values sent from the client (the request proto)
//		final MinimumUserProtoWithFacebookId senderProto = reqProto.getSender();
//		final MinimumUserProto sender = senderProto.getMinUserProto();
//		final String userUuid = sender.getUserUuid();
//		final String userFacebookId = senderProto.getFacebookId();
//
//		// just accept these
//		List<String> acceptedInviteUuids = reqProto.getAcceptedInviteUuidsList();
//		if (null == acceptedInviteUuids) {
//			acceptedInviteUuids = new ArrayList<String>();
//		} else {
//			acceptedInviteUuids = new ArrayList<String>(acceptedInviteUuids);
//		}
//
//		// delete these from the table
//		List<String> rejectedInviteUuids = reqProto.getRejectedInviteUuidsList();
//		if (null == rejectedInviteUuids) {
//			rejectedInviteUuids = new ArrayList<String>();
//		} else {
//			rejectedInviteUuids = new ArrayList<String>(rejectedInviteUuids);
//		}
//		final Timestamp acceptTime = new Timestamp((new Date()).getTime());
//
//		// set some values to send to the client (the response proto)
//		final AcceptAndRejectFbInviteForSlotsResponseProto.Builder resBuilder =
//		    AcceptAndRejectFbInviteForSlotsResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(AcceptAndRejectFbInviteForSlotsStatus.FAIL_OTHER); // default
//
//		svcTxManager.beginTransaction();
//		try {
//			// these will be populated. by checkLegit()
//			final Map<Integer, UserFacebookInviteForSlot> idsToInvitesInDb =
//			    new HashMap<Integer, UserFacebookInviteForSlot>();
//
//			final boolean legit =
//			    checkLegit(resBuilder, userUuid, userFacebookId, acceptedInviteUuids,
//			        rejectedInviteUuids, idsToInvitesInDb);
//
//			boolean successful = false;
//			if (legit) {
//				successful =
//				    writeChangesToDb(userUuid, userFacebookId, acceptedInviteUuids,
//				        rejectedInviteUuids, idsToInvitesInDb, acceptTime);
//			}
//
//			if (successful) {
//				// need to retrieve all the inviters from the db, set the
//				// accepted time for accepted invites
//				final Collection<UserFacebookInviteForSlot> invites = idsToInvitesInDb.values();
//				final List<String> userUuids = getInviterUuids(invites);
//				final Map<String, User> idsToInviters = RetrieveUtils.userRetrieveUtils()
//				    .getUsersByUuids(userUuids);
//
//				for (final UserFacebookInviteForSlot invite : invites) {
//					invite.setTimeAccepted(acceptTime);
//
//					final String inviterUuid = invite.getInviterUserUuid();
//					final User inviter = idsToInviters.get(inviterUuid);
//					final MinimumUserProtoWithFacebookId inviterProto = null;
//
//					// create the proto for the invites
//					final UserFacebookInviteForSlotProto inviteProto =
//					    CreateInfoProtoUtils.createUserFacebookInviteForSlotProtoFromInvite(
//					        invite, inviter, inviterProto);
//
//					resBuilder.addAcceptedInvites(inviteProto);
//				}
//				resBuilder.setStatus(AcceptAndRejectFbInviteForSlotsStatus.SUCCESS);
//			}
//
//			final AcceptAndRejectFbInviteForSlotsResponseEvent resEvent =
//			    new AcceptAndRejectFbInviteForSlotsResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setAcceptAndRejectFbInviteForSlotsResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in AcceptAndRejectFbInvitesForSlotsController.processRequestEvent",
//				    e);
//			}
//
//			if (successful) {
//				// write to the inviters this user accepted
//				final AcceptAndRejectFbInviteForSlotsResponseProto responseProto =
//				    resBuilder.build();
//				for (final Integer inviteId : acceptedInviteUuids) {
//					final UserFacebookInviteForSlot invite = idsToInvitesInDb.get(inviteId);
//					final String inviterUuid = invite.getInviterUserUuid();
//
//					final AcceptAndRejectFbInviteForSlotsResponseEvent newResEvent =
//					    new AcceptAndRejectFbInviteForSlotsResponseEvent(inviterId);
//					newResEvent.setTag(0);
//					newResEvent.setAcceptAndRejectFbInviteForSlotsResponseProto(responseProto);
//					// write to client
//					LOG.info("Writing event: "
//					    + newResEvent);
//					try {
//						eventWriter.writeEvent(newResEvent);
//					} catch (final Throwable e) {
//						LOG.error(
//						    "fatal exception in AcceptAndRejectFbInvitesForSlotsController.processRequestEvent",
//						    e);
//					}
//
//				}
//			}
//
//		} catch (final Throwable e) {
//			LOG.error("exception in AcceptAndRejectFbInviteForSlotsController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(AcceptAndRejectFbInviteForSlotsStatus.FAIL_OTHER);
//				final AcceptAndRejectFbInviteForSlotsResponseEvent resEvent =
//				    new AcceptAndRejectFbInviteForSlotsResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setAcceptAndRejectFbInviteForSlotsResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e2) {
//					LOG.error(
//					    "fatal exception in AcceptAndRejectFbInvitesForSlotsController.processRequestEvent",
//					    e2);
//				}
//			} catch (final Throwable e2) {
//				LOG.error(
//				    "exception2 in AcceptAndRejectFbInviteForSlotsController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	/*
//	 * Return true if user request is valid; false otherwise and set the builder
//	 * status to the appropriate value. accepetedInviteUuids,
//	 * rejectedInviteUuids, and idsToAcceptedInvites might be modified
//	 */
//	private boolean checkLegit( final Builder resBuilder, final String userUuid,
//	    final String userFacebookId, final List<String> acceptedInviteUuids,
//	    final List<String> rejectedInviteUuids,
//	    final Map<String, UserFacebookInviteForSlot> idsToInvites )
//	{
//
//		if ((null == userFacebookId)
//		    || userFacebookId.isEmpty()) {
//			LOG.error("facebookId is null. id="
//			    + userFacebookId
//			    + "\t acceptedInvitesUuids="
//			    + acceptedInviteUuids
//			    + "\t rejectedInviteUuids="
//			    + rejectedInviteUuids);
//			return false;
//		}
//		// search for these invites accepted and rejected
//		final List<String> inviteUuids = new ArrayList<String>(acceptedInviteUuids);
//		inviteUuids.addAll(rejectedInviteUuids);
//
//		// retrieve the invites for this recipient that haven't been accepted
//		// nor redeemed
//		final boolean filterByAccepted = true;
//		boolean isAccepted = false;
//		final boolean filterByRedeemed = true;
//		final boolean isRedeemed = false;
//		final Map<String, UserFacebookInviteForSlot> idsToInvitesInDb =
//		    UserFacebookInviteForSlotRetrieveUtils.getSpecificOrAllInvitesForRecipient(
//		        userFacebookId, inviteUuids, filterByAccepted, isAccepted, filterByRedeemed,
//		        isRedeemed);
//		final Set<String> validUuids = idsToInvitesInDb.keySet();
//
//		// only want the acceptedInvite ids that aren't yet accepted nor
//		// redeemed
//		LOG.info("acceptedInviteUuids before filter: "
//		    + acceptedInviteUuids);
//		acceptedInviteUuids.retainAll(validUuids);
//		LOG.info("acceptedInviteUuids after filter: "
//		    + acceptedInviteUuids);
//
//		// only want the rejectedInvite ids that aren't yet accepted nor
//		// redeemed
//		rejectedInviteUuids.retainAll(validUuids);
//
//		// check to make sure this user is not accepting any invites from an
//		// inviter
//		// this user has already accepted, or in other words
//		// check to make sure this user has not previously accepted any invites
//		// from
//		// any of the inviters of the acceptedInviteUuids
//
//		// pair up inviterUserUuids with the acceptedInviteUuids
//		final Map<String, String> acceptedInviterUuidsToInviteUuids =
//		    getInviterUserUuids(acceptedInviteUuids, idsToInvitesInDb);
//
//		// look in the invite table for accepted invites (includes redeemed),
//		// select the inviter user ids that have recipientFacebookId =
//		// userFacebookId
//		isAccepted = true;
//		final Set<String> redeemedInviterUuids =
//		    UserFacebookInviteForSlotRetrieveUtils.getUniqueInviterUserUuidsForRequesterId(
//		        userFacebookId, filterByAccepted, isAccepted);
//
//		// if any of the acceptedInviteUuids contains an inviterId this user has
//		// already accepted
//		// an invite from,
//		// delete inviteId from the acceptedInviteUuids list and put the
//		// inviteId into the rejectedInviteUuids list,
//		// done so because the db probably has recorded that the inviter used up
//		// this user
//		// and is trying to use this user again
//		LOG.info("acceptedInviteUuids before inviter used check: "
//		    + acceptedInviteUuids);
//		retainInvitesFromUnusedInviters(redeemedInviterUuids,
//		    acceptedInviterUuidsToInviteUuids, acceptedInviteUuids, rejectedInviteUuids);
//		LOG.info("acceptedInviteUuids after inviter used check: "
//		    + acceptedInviteUuids);
//
//		idsToInvites.putAll(idsToInvitesInDb);
//
//		return true;
//	}
//
//	private void retainIfInExistingUuids( final Set<String> existingInts,
//	    final List<String> someUuids )
//	{
//		final int lastIndex = someUuids.size() - 1;
//		for (int index = lastIndex; index >= 0; index--) {
//			final int someInt = someUuids.get(index);
//
//			if (!existingInts.contains(someInt)) {
//				// since the int is not in existingInts, remove it.
//				someUuids.remove(index);
//			}
//		}
//	}
//
//	private Map<String, String> getInviterUserUuids( final List<String> inviteUuids,
//	    final Map<String, UserFacebookInviteForSlot> uuidsToInvites )
//	{
//		final Map<String, String> inviterUserIdsToInviteUuids = new HashMap<String, String>();
//
//		for (final String inviteUuid : inviteUuids) {
//			final UserFacebookInviteForSlot invite = uuidsToInvites.get(inviteUuid);
//			final String inviterUserUuid = invite.getInviterUserUuid();
//			// what if this guy is used more than once? meh, fuck it
//			inviterUserIdsToInviteUuids.put(inviterUserUuid, inviteUuid);
//		}
//
//		return inviterUserIdsToInviteUuids;
//	}
//
//	// recordedInviterUuids are the inviterUuids in the invite table that belong
//	// to invites
//	// accepted by a user
//	private void retainInvitesFromUnusedInviters( final Set<String> recordedInviterUuids,
//	    final Map<String, Integer> acceptedInviterUuidsToInviteUuids,
//	    final List<String> acceptedInviteUuids, final List<String> rejectedInviteUuids )
//	{
//		// if any of the inviter ids in acceptedInviterIdsToInviteUuids are in
//		// recordedInviterUuids, delete inviteId from the
//		// acceptedInviteUuids list and put the inviteId into the
//		// rejectedInviteUuids list
//
//		// keep track of the inviterUuids that this user has previously already
//		// accepted
//		final Map<String, String> invalidInviteUuidsToUserUuids = new HashMap<String, String>();
//		for (final String potentialNewInviterUuid : acceptedInviterUuidsToInviteUuids.keySet()) {
//			if (recordedInviterUuids.contains(potentialNewInviterUuid)) {
//				// userA trying to accept an invite from a person userA has
//				// already accepted an invite from
//				final String inviteUuid =
//				    acceptedInviterUuidsToInviteUuids.get(potentialNewInviterUuid);
//
//				invalidInviteUuidsToUserUuids.put(inviteUuid, potentialNewInviterUuid);
//			}
//		}
//
//		final Set<String> invalidInviteUuids = invalidInviteUuidsToUserUuids.keySet();
//		if (invalidInviteUuids.isEmpty()) {
//			return;
//		}
//		LOG.warn("user tried accepting invites from users he has already accepted."
//		    + "invalidInviteIdsToUserUuids="
//		    + invalidInviteUuidsToUserUuids);
//
//		LOG.warn("before: rejectedInviteUuids="
//		    + acceptedInviteUuids);
//		LOG.warn("before: acceptedInviteUuids="
//		    + acceptedInviteUuids);
//		// go through acceptedInviteUuids and remove invalid inviteUuids
//		final int lastIndex = acceptedInviteUuids.size() - 1;
//		for (int index = lastIndex; index >= 0; index--) {
//			final String acceptedInviteUuid = acceptedInviteUuids.get(index);
//
//			if (invalidInviteUuids.contains(acceptedInviteUuid)) {
//				acceptedInviteUuids.remove(index);
//
//				// after removing it put it into rejectedInviteUuids
//				rejectedInviteUuids.add(acceptedInviteUuid);
//			}
//		}
//		LOG.warn("after: acceptedInviteUuids="
//		    + acceptedInviteUuids);
//		LOG.warn("after: rejectedInviteUuids="
//		    + rejectedInviteUuids);
//	}
//
//	private boolean writeChangesToDb( final String userUuid, final String userFacebookId,
//	    final List<Integer> acceptedInviteUuids, final List<Integer> rejectedInviteUuids,
//	    final Map<Integer, UserFacebookInviteForSlot> idsToInvitesInDb,
//	    final Timestamp acceptTime )
//	{
//		LOG.info("idsToInvitesInDb="
//		    + idsToInvitesInDb
//		    + "\t acceptedInviteUuids="
//		    + acceptedInviteUuids
//		    + "\t rejectedInviteUuids="
//		    + rejectedInviteUuids);
//
//		// update the acceptTimes for the acceptedInviteUuids
//		// these acceptedInviteUuids are for non-accepted, unredeemed invites
//		if (!acceptedInviteUuids.isEmpty()) {
//			final int num =
//			    UpdateUtils.get()
//			        .updateUserFacebookInviteForSlotAcceptTime(userFacebookId,
//			            acceptedInviteUuids, acceptTime);
//			LOG.info("\t\t\t\t\t\t\t num acceptedInviteUuids updated: "
//			    + num
//			    + "\t invites="
//			    + acceptedInviteUuids);
//		}
//
//		// DELETE THE rejectedInviteUuids THAT ARE ALREADY IN DB
//		// these deleted invites are for non-accepted, unredeemed invites
//		if (!rejectedInviteUuids.isEmpty()) {
//			final int num = DeleteUtils.get()
//			    .deleteUserFacebookInvitesForSlots(rejectedInviteUuids);
//			LOG.info("num rejectedInviteUuids deleted: "
//			    + num
//			    + "\t invites="
//			    + rejectedInviteUuids);
//		}
//
//		return true;
//	}
//
//	private List<String> getInviterUuids( final Collection<UserFacebookInviteForSlot> invites )
//	{
//		final List<String> inviterUuids = new ArrayList<String>();
//
//		for (final UserFacebookInviteForSlot invite : invites) {
//			final String inviterUuid = invite.getInviterUserUuid();
//			inviterUuids.add(inviterUuid);
//		}
//		return inviterUuids;
//	}
//}
