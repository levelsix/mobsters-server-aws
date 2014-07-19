//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
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
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsRequestProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsRequestProto.FacebookInviteStructure;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto.InviteFbFriendsForSlotsStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.InviteFbFriendsForSlotsRequestEvent;
//import com.lvl6.mobsters.events.response.InviteFbFriendsForSlotsResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithFacebookId;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.UserFacebookInviteForSlotProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class InviteFbFriendsForSlotsController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(InviteFbFriendsForSlotsController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public InviteFbFriendsForSlotsController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new InviteFbFriendsForSlotsRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final InviteFbFriendsForSlotsRequestProto reqProto =
//		    ((InviteFbFriendsForSlotsRequestEvent) event).getInviteFbFriendsForSlotsRequestProto();
//
//		// get values sent from the client (the request proto)
//		final MinimumUserProtoWithFacebookId senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getMinUserProto()
//		    .getUserUuid();
//		final List<FacebookInviteStructure> invites = reqProto.getInvitesList();
//
//		final Map<String, Integer> fbIdsToUserStructUuids = new HashMap<String, Integer>();
//		final Map<String, Integer> fbUuidsToUserStructFbLvl = new HashMap<String, Integer>();
//		final List<String> fbUuidsOfFriends =
//		    demultiplexFacebookInviteStructure(invites, fbIdsToUserStructUuids,
//		        fbUuidsToUserStructFbLvl);
//
//		final Timestamp curTime = new Timestamp((new Date()).getTime());
//
//		// set some values to send to the client (the response proto)
//		final InviteFbFriendsForSlotsResponseProto.Builder resBuilder =
//		    InviteFbFriendsForSlotsResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(InviteFbFriendsForSlotsStatus.FAIL_OTHER); // default
//
//		svcTxManager.beginTransaction();
//		try {
//			final User aUser = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			// get all the invites the user sent
//			final List<Integer> specificUuids = null;
//			final boolean filterByAccepted = false;
//			final boolean isAccepted = false;
//			final boolean filterByRedeemed = false;
//			final boolean isRedeemed = false;
//			final Map<Integer, UserFacebookInviteForSlot> idsToInvites =
//			    UserFacebookInviteForSlotRetrieveUtils.getSpecificOrAllInvitesForInviter(
//			        userUuid, specificUuids, filterByAccepted, isAccepted, filterByRedeemed,
//			        isRedeemed);
//
//			// will contain the facebook ids of new users the user can invite
//			// new is defined as: for each facebookId the tuple
//			// (inviterId, recipientId)=(userUuid, facebookId)
//			// doesn't already exist in the table
//			final List<String> newFacebookIdsToInvite = new ArrayList<String>();
//			final boolean legit =
//			    checkLegit(resBuilder, userUuid, aUser, fbUuidsOfFriends, idsToInvites,
//			        newFacebookIdsToInvite);
//
//			boolean successful = false;
//			final List<Integer> inviteUuids = new ArrayList<Integer>();
//			if (legit) {
//				// will populate inviteUuids
//				successful =
//				    writeChangesToDb(aUser, newFacebookIdsToInvite, curTime,
//				        fbIdsToUserStructUuids, fbUuidsToUserStructFbLvl, inviteUuids);
//			}
//
//			if (successful) {
//				final Map<Integer, UserFacebookInviteForSlot> newUuidsToInvites =
//				    UserFacebookInviteForSlotRetrieveUtils.getInviteForId(inviteUuids);
//				// client needs to know what the new invites are;
//				for (final Integer id : newUuidsToInvites.keySet()) {
//					final UserFacebookInviteForSlot invite = newUuidsToInvites.get(id);
//					final UserFacebookInviteForSlotProto inviteProto =
//					    CreateInfoProtoUtils.createUserFacebookInviteForSlotProtoFromInvite(
//					        invite, aUser, senderProto);
//					resBuilder.addInvitesNew(inviteProto);
//				}
//				resBuilder.setStatus(InviteFbFriendsForSlotsStatus.SUCCESS);
//			}
//
//			final InviteFbFriendsForSlotsResponseEvent resEvent =
//			    new InviteFbFriendsForSlotsResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setInviteFbFriendsForSlotsResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in InviteFbFriendsForSlotsController.processRequestEvent",
//				    e);
//			}
//
//			if (successful) {
//				// send this to all the recipients in fbUuidsOfFriends that have
//				// a user id
//				// if want to send to the new ones use newFacebookIdsToInvite
//				final List<Integer> recipientUserUuids = RetrieveUtils.userRetrieveUtils()
//				    .getUserUuidsForFacebookIds(newFacebookUuidsToInvite);
//
//				final InviteFbFriendsForSlotsResponseProto responseProto = resBuilder.build();
//				for (final Integer recipientUserId : recipientUserUuids) {
//					final InviteFbFriendsForSlotsResponseEvent newResEvent =
//					    new InviteFbFriendsForSlotsResponseEvent(recipientUserId);
//					newResEvent.setTag(0);
//					newResEvent.setInviteFbFriendsForSlotsResponseProto(responseProto);
//					// write to client
//					LOG.info("Writing event: "
//					    + newResEvent);
//					try {
//						eventWriter.writeEvent(newResEvent);
//					} catch (final Throwable e) {
//						LOG.error(
//						    "fatal exception in InviteFbFriendsForSlotsController.processRequestEvent",
//						    e);
//					}
//				}
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in InviteFbFriendsForSlotsController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(InviteFbFriendsForSlotsStatus.FAIL_OTHER);
//				final InviteFbFriendsForSlotsResponseEvent resEvent =
//				    new InviteFbFriendsForSlotsResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setInviteFbFriendsForSlotsResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in InviteFbFriendsForSlotsController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in InviteFbFriendsForSlotsController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private List<String> demultiplexFacebookInviteStructure(
//	    final List<FacebookInviteStructure> invites,
//	    final Map<String, Integer> fbIdsToUserStructUuids,
//	    final Map<String, Integer> fbUuidsToUserStructFbLvl )
//	{
//
//		final List<String> retVal = new ArrayList<String>();
//		for (final FacebookInviteStructure fis : invites) {
//			final String fbId = fis.getFbFriendId();
//
//			final String userStructUuid = fis.getUserStructUuid();
//			final int userStructFbLvl = fis.getUserStructFbLvl();
//
//			retVal.add(fbId);
//			fbIdsToUserStructUuids.put(fbId, userStructId);
//			fbUuidsToUserStructFbLvl.put(fbId, userStructFbLvl);
//		}
//		return retVal;
//	}
//
//	/*
//	 * Return true if user request is valid; false otherwise and set the builder
//	 * status to the appropriate value. newUserUuidsToInvite will be modified
//	 */
//	private boolean checkLegit( final Builder resBuilder, final String userUuid, final User u,
//	    final List<String> fbUuidsOfFriends,
//	    final Map<Integer, UserFacebookInviteForSlot> idsToInvites,
//	    final List<String> newFacebookIdsToInvite )
//	{
//
//		if (null == u) {
//			LOG.error("user is null. no user exists with id="
//			    + userUuid);
//			return false;
//		}
//
//		// if the user already invited some friends, don't invite again, keep
//		// only new ones
//		final List<String> newFacebookIdsToInviteTemp =
//		    getNewInvites(fbUuidsOfFriends, idsToInvites);
//		newFacebookIdsToInvite.addAll(newFacebookUuidsToInviteTemp);
//		return true;
//	}
//
//	// keeps and returns the facebook ids that have not been invited yet
//	private List<String> getNewInvites( final List<String> fbUuidsOfFriends,
//	    final Map<Integer, UserFacebookInviteForSlot> idsToInvites )
//	{
//		// CONTAINS THE DUPLICATE INVITES, THAT NEED TO BE DELETED
//		// E.G. TWO INVITES EXIST WITH SAME INVITERID AND RECIPIENTID
//		final List<Integer> inviteUuidsOfDuplicateInvites = new ArrayList<Integer>();
//
//		// running collection of recipient ids already seen
//		final Set<String> processedRecipientUuids = new HashSet<String>();
//
//		// for each recipientId separate the unique ones from the duplicates
//		for (final Integer inviteId : idsToInvites.keySet()) {
//			final UserFacebookInviteForSlot invite = idsToInvites.get(inviteId);
//			final String recipientId = invite.getRecipientFacebookId();
//
//			// if seen this recipientId place it in the duplicates list
//			if (processedRecipientUuids.contains(recipientId)) {
//				// done to ensure a user does not invite another user more than
//				// once
//				// i.e. tuple (inviterId, recipientId) is unique
//				inviteUuidsOfDuplicateInvites.add(inviteId);
//			} else {
//				// keep track of the recipientUuids seen so far (the unique
//				// ones)
//				processedRecipientUuids.add(recipientId);
//			}
//		}
//
//		// DELETE THE DUPLICATE INVITES THAT ARE ALREADY IN DB
//		// maybe need to determine which invites should be deleted, as in most
//		// recent or somethings
//		// because right now, any of the nonunique invites could be deleted
//		if (!inviteUuidsOfDuplicateInvites.isEmpty()) {
//			final int num = DeleteUtils.get()
//			    .deleteUserFacebookInvitesForSlots(inviteUuidsOfDuplicateInvites);
//			LOG.warn("num duplicate invites deleted: "
//			    + num);
//		}
//
//		final List<String> newFacebookIdsToInvite = new ArrayList<String>();
//		// don't want to generate an invite to a recipient the user has already
//		// invited
//		// going through the facebook ids client sends and select only the new
//		// ones
//		for (final String prospectiveRecipientId : fbUuidsOfFriends) {
//
//			// keep only the recipient ids that have not been seen/invited
//			if (!processedRecipientUuids.contains(prospectiveRecipientId)) {
//				newFacebookIdsToInvite.add(prospectiveRecipientId);
//			}
//		}
//		return newFacebookIdsToInvite;
//	}
//
//	private boolean writeChangesToDb( final User aUser,
//	    final List<String> newFacebookIdsToInvite, final Timestamp curTime,
//	    final Map<String, Integer> fbIdsToUserStructUuids,
//	    final Map<String, Integer> fbUuidsToUserStructsFbLvl, final List<Integer> inviteUuids )
//	{
//		if (newFacebookIdsToInvite.isEmpty()) {
//			return true;
//		}
//		final String userUuid = aUser.getId();
//		final List<Integer> inviteUuidsTemp =
//		    InsertUtils.get()
//		        .insertIntoUserFbInviteForSlot(userUuid, newFacebookIdsToInvite, curTime,
//		            fbIdsToUserStructUuids, fbUuidsToUserStructsFbLvl);
//		final int numInserted = inviteUuidsTemp.size();
//
//		final int expectedNum = newFacebookIdsToInvite.size();
//		if (numInserted != expectedNum) {
//			LOG.error("problem with updating user monster inventory slots and diamonds."
//			    + " num inserted: "
//			    + numInserted
//			    + "\t should have been: "
//			    + expectedNum);
//		}
//		LOG.info("num inserted: "
//		    + numInserted
//		    + "\t ids="
//		    + inviteUuidsTemp);
//
//		inviteUuids.addAll(inviteUuidsTemp);
//		return true;
//	}
//
//}
