//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.StructureForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotRequestProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotRequestProto.IncreaseSlotType;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto.IncreaseMonsterInventorySlotStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.IncreaseMonsterInventorySlotRequestEvent;
//import com.lvl6.mobsters.events.response.IncreaseMonsterInventorySlotResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.info.Structure;
//import com.lvl6.mobsters.info.StructureResidence;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructureInfoProto.StructType;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class IncreaseMonsterInventorySlotController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(IncreaseMonsterInventorySlotController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public IncreaseMonsterInventorySlotController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new IncreaseMonsterInventorySlotRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_INCREASE_MONSTER_INVENTORY_SLOT_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final IncreaseMonsterInventorySlotRequestProto reqProto =
//		    ((IncreaseMonsterInventorySlotRequestEvent) event).getIncreaseMonsterInventorySlotRequestProto();
//
//		// EVERY TIME USER BUYS SLOTS RESET user_facebook_invite_for_slot table
//
//		// get values sent from the client (the request proto)
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final IncreaseSlotType increaseType = reqProto.getIncreaseSlotType();
//		final int userStructId = reqProto.getUserStructUuid();
//		// the invites to redeem
//		final List<Integer> userFbInviteUuids = reqProto.getUserFbInviteForSlotUuidsList();
//		final Timestamp curTime = new Timestamp((new Date()).getTime());
//
//		// set some values to send to the client (the response proto)
//		final IncreaseMonsterInventorySlotResponseProto.Builder resBuilder =
//		    IncreaseMonsterInventorySlotResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(IncreaseMonsterInventorySlotStatus.FAIL_OTHER); // default
//
//		svcTxManager.beginTransaction();
//		try {
//			int previousGems = 0;
//			// get stuff from the db
//			final User aUser = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			final StructureForUser sfu = RetrieveUtils.userStructRetrieveUtils()
//			    .getSpecificUserStruct(userStructId);
//
//			// will be populated if user is successfully redeeming fb invites
//			final Map<Integer, UserFacebookInviteForSlot> idsToAcceptedInvites =
//			    new HashMap<Integer, UserFacebookInviteForSlot>();
//
//			final boolean legit =
//			    checkLegit(resBuilder, userUuid, aUser, userStructId, sfu, increaseType,
//			        userFbInviteUuids, idsToAcceptedInvites);
//
//			int gemCost = 0;
//			boolean successful = false;
//			final Map<String, Integer> changeMap = new HashMap<String, Integer>();
//			if (legit) {
//				previousGems = aUser.getGems();
//				gemCost = getGemPriceFromStruct(sfu);
//				successful =
//				    writeChangesToDb(aUser, sfu, increaseType, gemCost, curTime,
//				        idsToAcceptedInvites, changeMap);
//			}
//
//			if (successful) {
//				resBuilder.setStatus(IncreaseMonsterInventorySlotStatus.SUCCESS);
//			}
//
//			final IncreaseMonsterInventorySlotResponseEvent resEvent =
//			    new IncreaseMonsterInventorySlotResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setIncreaseMonsterInventorySlotResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in IncreaseMonsterInventorySlotController.processRequestEvent",
//				    e);
//			}
//
//			if (successful) {
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in IncreaseMonsterInventorySlotController.processRequestEvent",
//					    e);
//				}
//
//				if (increaseType == IncreaseSlotType.PURCHASE) {
//					writeToUserCurrencyHistory(aUser, sfu, increaseType, curTime, changeMap,
//					    previousGems);
//				}
//				// delete the user's facebook invites for slots
//				deleteInvitesForSlotsAfterPurchase(userUuid, changeMap);
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in IncreaseMonsterInventorySlotController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(IncreaseMonsterInventorySlotStatus.FAIL_OTHER);
//				final IncreaseMonsterInventorySlotResponseEvent resEvent =
//				    new IncreaseMonsterInventorySlotResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setIncreaseMonsterInventorySlotResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in IncreaseMonsterInventorySlotController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in IncreaseMonsterInventorySlotController processEvent",
//				    e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	/*
//	 * Return true if user request is valid; false otherwise and set the builder
//	 * status to the appropriate value.
//	 */
//	private boolean checkLegit( final Builder resBuilder, final String userUuid, final User u,
//	    final int userStructId, final StructureForUser sfu, final IncreaseSlotType aType,
//	    final List<Integer> userFbInviteUuids,
//	    final Map<Integer, UserFacebookInviteForSlot> idsToAcceptedInvites )
//	{
//		if (null == u) {
//			LOG.error("user is null. no user exists with id="
//			    + userUuid);
//			return false;
//		}
//		if (null == sfu) {
//			LOG.error("doesn't exist, user struct with id="
//			    + userStructId);
//			return false;
//		}
//
//		// THE CHECK IF USER IS REDEEMING FACEBOOK INVITES
//		if (IncreaseSlotType.REDEEM_FACEBOOK_INVITES == aType) {
//			// get accepted and unredeemed invites
//			final Map<Integer, UserFacebookInviteForSlot> idsToAcceptedTemp =
//			    getInvites(userUuid, userFbInviteUuids);
//			// check if requested invites even exist
//			if ((null == idsToAcceptedTemp)
//			    || idsToAcceptedTemp.isEmpty()) {
//				LOG.error("no invites exist with ids: "
//				    + userFbInviteUuids);
//				return false;
//			}
//
//			final int userStructIdFromInvites = getUserStructId(idsToAcceptedTemp);
//			if (userStructId != userStructIdFromInvites) {
//				resBuilder.setStatus(IncreaseMonsterInventorySlotStatus.FAIL_INCONSISTENT_INVITE_DATA);
//				LOG.error("data across invites aren't consistent: user struct id/fb lvl. invites="
//				    + idsToAcceptedTemp
//				    + "\t expectedUserStructId="
//				    + userStructId);
//				return false;
//			}
//
//			// check if user struct is already at its max fb invite lvl,
//			final int structId = sfu.getStructId();
//			final Structure struct = StructureRetrieveUtils.getStructForStructId(structId);
//			final int structLvl = struct.getLevel();
//
//			final int nextUserStructFbInviteLvl = sfu.getFbInviteStructLvl() + 1;
//			if (nextUserStructFbInviteLvl > structLvl) {
//				resBuilder.setStatus(IncreaseMonsterInventorySlotStatus.FAIL_STRUCTURE_AT_MAX_FB_INVITE_LVL);
//				LOG.error("user struct maxed fb invite lvl. userStruct="
//				    + sfu
//				    + "\t struct="
//				    + struct);
//				return false;
//			}
//
//			// required min num invites depends on the structure and the
//			// UserStructure's fb lvl
//			final int minNumInvites =
//			    getMinNumInvitesFromStruct(sfu, structId, nextUserStructFbInviteLvl);
//			// check if user has enough invites to gain a slot
//			final int acceptedAmount = idsToAcceptedTemp.size();
//			if (acceptedAmount < minNumInvites) {
//				resBuilder.setStatus(IncreaseMonsterInventorySlotStatus.FAIL_INSUFFICIENT_FACEBOOK_INVITES);
//				LOG.error("user doesn't meet num accepted facebook invites to increase slots. "
//				    + "minRequired="
//				    + minNumInvites
//				    + "\t has:"
//				    + acceptedAmount);
//				return false;
//			}
//			// give the caller the invites, at this point, the number of invites
//			// is at least
//			// equal to minNumInvites and could be more
//			idsToAcceptedInvites.putAll(idsToAcceptedTemp);
//
//			// THE CHECK IF USER IS BUYING SLOTS
//		} else if (IncreaseSlotType.PURCHASE == aType) {
//			// gemprice depends on the structure;
//			final int gemPrice = getGemPriceFromStruct(sfu);
//
//			// check if user has enough money
//			final int userGems = u.getGems();
//			if (userGems < gemPrice) {
//				resBuilder.setStatus(IncreaseMonsterInventorySlotStatus.FAIL_INSUFFICIENT_FUNDS);
//				LOG.error("user does not have enough gems to buy more monster inventory slots. userGems="
//				    + userGems
//				    + "\t gemPrice="
//				    + gemPrice);
//				return false;
//			}
//
//		} else {
//			return false;
//		}
//
//		return true;
//	}
//
//	private Map<Integer, UserFacebookInviteForSlot> getInvites( final String userUuid,
//	    final List<Integer> userFbInviteUuids )
//	{
//		// get accepted and unredeemed invites
//		final boolean filterByAccepted = true;
//		final boolean isAccepted = true;
//		final boolean filterByRedeemed = true;
//		final boolean isRedeemed = false;
//		final Map<Integer, UserFacebookInviteForSlot> idsToAcceptedTemp =
//		    UserFacebookInviteForSlotRetrieveUtils.getSpecificOrAllInvitesForInviter(userUuid,
//		        userFbInviteUuids, filterByAccepted, isAccepted, filterByRedeemed, isRedeemed);
//		return idsToAcceptedTemp;
//	}
//
//	// if user struct ids and user struct fb lvls are inconsistent, return
//	// non-positive value;
//	private int getUserStructId( final Map<Integer, UserFacebookInviteForSlot> idsToAcceptedTemp )
//	{
//		int prevUserStructId = -1;
//		int prevUserStructFbLvl = -1;
//
//		for (final UserFacebookInviteForSlot invite : idsToAcceptedTemp.values()) {
//			final String tempUserStructUuid = invite.getUserStructUuid();
//			final int tempUserStructFbLvl = invite.getUserStructFbLvl();
//			if (-1 == prevUserStructId) {
//				prevUserStructId = tempUserStructId;
//				prevUserStructFbLvl = tempUserStructFbLvl;
//
//			} else if ((prevUserStructId != tempUserStructId)
//			    || (prevUserStructFbLvl != tempUserStructFbLvl)) {
//				// since the userStructUuids or userStructFbLvl's are
//				// inconsistent, return failure
//				return -1;
//			}
//
//		}
//		return prevUserStructId;
//	}
//
//	private int getMinNumInvitesFromStruct( final StructureForUser sfu, final int structId,
//	    final int userStructFbInviteLvl )
//	{
//		// since userStructFbInviteLvl and structure level are one to one,
//		// essentially
//		// they are one and the same
//		// e.g. userStructFbInviteLvl = 1, also means 1 = structure level
//		// get the structure with the struct lvl= userStructFbInviteLvl
//		final Structure structForFbInviteLvl =
//		    StructureRetrieveUtils.getPredecessorStructForStructIdAndLvl(structId,
//		        userStructFbInviteLvl);
//		final String structType = structForFbInviteLvl.getStructType();
//
//		LOG.info("StructureForUser="
//		    + sfu);
//		LOG.info("structId="
//		    + structForFbInviteLvl);
//		LOG.info("userStructFbInviteLvl="
//		    + userStructFbInviteLvl);
//		LOG.info("resulting structure for structId and level: "
//		    + structForFbInviteLvl);
//
//		int minNumInvites = -1;
//		// at the moment, invites are only for residences
//		if (StructType.valueOf(structType) == StructType.RESIDENCE) {
//			final int structIdForUserStructFbInviteLvl = structForFbInviteLvl.getId();
//			final StructureResidence residence =
//			    StructureResidenceRetrieveUtils.getResidenceForStructId(structIdForUserStructFbInviteLvl);
//			minNumInvites = residence.getNumAcceptedFbInvites();
//		} else {
//			LOG.error("invalid struct type for increasing monster slots. structType="
//			    + structType);
//		}
//
//		LOG.info("getMinNumInvitesFromStruct returns minNumInvites="
//		    + minNumInvites);
//		return minNumInvites;
//	}
//
//	private int getGemPriceFromStruct( final StructureForUser sfu )
//	{
//		// get the structure
//		final int structId = sfu.getStructId();
//		final Structure struct = StructureRetrieveUtils.getStructForStructId(structId);
//		final String structType = struct.getStructType();
//
//		int gemPrice = Integer.MAX_VALUE;
//		// at the moment, invites are only for residences
//		if (StructType.valueOf(structType) == StructType.RESIDENCE) {
//			final StructureResidence residence =
//			    StructureResidenceRetrieveUtils.getResidenceForStructId(structId);
//			gemPrice = residence.getNumGemsRequired();
//		}
//
//		return gemPrice;
//	}
//
//	private boolean writeChangesToDb( final User aUser, final StructureForUser sfu,
//	    final IncreaseSlotType increaseType, final int gemCost, final Timestamp curTime,
//	    final Map<Integer, UserFacebookInviteForSlot> idsToAcceptedInvites,
//	    final Map<String, Integer> changeMap )
//	{
//		boolean success = false;
//
//		if (IncreaseSlotType.REDEEM_FACEBOOK_INVITES == increaseType) {
//			final int structId = sfu.getStructId();
//			final int nextUserStructFbInviteLvl = sfu.getFbInviteStructLvl() + 1;
//
//			final int minNumInvites =
//			    getMinNumInvitesFromStruct(sfu, structId, nextUserStructFbInviteLvl);
//			// if num accepted invites more than min required, just take the
//			// earliest ones
//			final List<Integer> inviteUuidsTheRest = new ArrayList<Integer>();
//			final List<UserFacebookInviteForSlot> nEarliestInvites =
//			    nEarliestInvites(idsToAcceptedInvites, minNumInvites, inviteUuidsTheRest);
//
//			// redeem the nEarliestInvites
//			int num = UpdateUtils.get()
//			    .updateRedeemUserFacebookInviteForSlot(curTime, nEarliestInvites);
//			LOG.info("num saved: "
//			    + num);
//
//			if (num != minNumInvites) {
//				LOG.error("expected updated: "
//				    + minNumInvites
//				    + "\t actual updated: "
//				    + num);
//				return false;
//			}
//			// delete all the remaining invites
//			final int numCurInvites = inviteUuidsTheRest.size();
//			if (numCurInvites > 0) {
//				LOG.info("num current invites: "
//				    + numCurInvites
//				    + " invitesToDelete= "
//				    + inviteUuidsTheRest);
//				num = DeleteUtils.get()
//				    .deleteUserFacebookInvitesForSlots(inviteUuidsTheRest);
//				LOG.info("num deleted: "
//				    + num);
//			}
//			success = true;
//		}
//
//		if (IncreaseSlotType.PURCHASE == increaseType) {
//			final int cost = -1
//			    * gemCost;
//			success = aUser.updateRelativeGemsNaive(cost);
//
//			if (!success) {
//				LOG.error("problem with updating user monster inventory slots and diamonds");
//				return false;
//			}
//			if (success
//			    && (0 != cost)) {
//				changeMap.put(MiscMethods.gems, cost);
//			}
//		}
//
//		// increase the user structs fb invite lvl
//		final int userStructId = sfu.getId();
//		final int fbInviteLevelChange = 1;
//		if (!UpdateUtils.get()
//		    .updateUserStructLevel(userStructId, fbInviteLevelChange)) {
//			LOG.error("(won't continue processing) couldn't update fbInviteLevel for user struct="
//			    + sfu);
//			return false;
//		}
//
//		return success;
//	}
//
//	private List<UserFacebookInviteForSlot> nEarliestInvites(
//	    final Map<Integer, UserFacebookInviteForSlot> idsToAcceptedInvites, final int n,
//	    final List<Integer> inviteUuidsTheRest )
//	{
//
//		final List<UserFacebookInviteForSlot> earliestAcceptedInvites =
//		    new ArrayList<UserFacebookInviteForSlot>(idsToAcceptedInvites.values());
//		orderUserFacebookAcceptedInvitesForSlots(earliestAcceptedInvites);
//
//		if (n < earliestAcceptedInvites.size()) {
//			final int amount = earliestAcceptedInvites.size();
//
//			// want to get the remaining invites after the first n
//			for (final UserFacebookInviteForSlot invite : earliestAcceptedInvites.subList(n,
//			    amount)) {
//				final Integer id = invite.getId();
//				inviteUuidsTheRest.add(id);
//			}
//
//			// get first n invites
//			return earliestAcceptedInvites.subList(0, n);
//		} else {
//			// num invites guaranteed to not be less than n
//			return earliestAcceptedInvites;
//		}
//	}
//
//	private void orderUserFacebookAcceptedInvitesForSlots(
//	    final List<UserFacebookInviteForSlot> invites )
//	{
//
//		Collections.sort(invites, new Comparator<UserFacebookInviteForSlot>() {
//			@Override
//			public int compare( final UserFacebookInviteForSlot lhs,
//			    final UserFacebookInviteForSlot rhs )
//			{
//				// sorting by accept time, which should not be null
//				final Date lhsDate = lhs.getTimeAccepted();
//				final Date rhsDate = rhs.getTimeAccepted();
//
//				if ((null == lhsDate)
//				    && (null == rhsDate)) {
//					return 0;
//				} else if (null == lhsDate) {
//					return -1;
//				} else if (null == rhsDate) {
//					return 1;
//				} else if (lhsDate.getTime() < rhsDate.getTime()) {
//					return -1;
//				} else if (lhsDate.getTime() == rhsDate.getTime()) {
//					return 0;
//				} else {
//					return 1;
//				}
//			}
//		});
//	}
//
//	private void writeToUserCurrencyHistory( final User aUser, final StructureForUser sfu,
//	    final IncreaseSlotType increaseType, final Timestamp curTime,
//	    final Map<String, Integer> changeMap, final int previousGems )
//	{
//		if (changeMap.isEmpty()) {
//			return;
//		}
//
//		final String userUuid = aUser.getId();
//		final Map<String, Integer> previousCurrencyMap = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrencyMap = new HashMap<String, Integer>();
//		final Map<String, String> changeReasonsMap = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//		final String gems = MiscMethods.gems;
//		final String reasonForChange = ControllerConstants.UCHRFC__INCREASE_MONSTER_INVENTORY;
//
//		final StringBuilder sb = new StringBuilder();
//		sb.append("increaseType=");
//		sb.append(increaseType.name());
//		sb.append(" prevFbInviteStructLvl=");
//		sb.append(sfu.getFbInviteStructLvl());
//		final String details = sb.toString();
//
//		previousCurrencyMap.put(gems, previousGems);
//		currentCurrencyMap.put(gems, aUser.getGems());
//		changeReasonsMap.put(gems, reasonForChange);
//		detailsMap.put(gems, details);
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, changeMap,
//		    previousCurrencyMap, currentCurrencyMap, changeReasonsMap, detailsMap);
//
//	}
//
//	// after user buys slots, delete all accepted and unaccepted invites for
//	// slots
//	private void deleteInvitesForSlotsAfterPurchase( final String userUuid,
//	    final Map<String, Integer> money )
//	{
//		if (money.isEmpty()) {
//			return;
//		}
//
//		final int num = DeleteUtils.get()
//		    .deleteUnredeemedUserFacebookInvitesForUser(userUuid);
//		LOG.info("num invites deleted after buying slot. userUuid="
//		    + userUuid
//		    + " numDeleted="
//		    + num);
//	}
//
//}
