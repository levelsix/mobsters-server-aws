//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.HashMap;
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
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureRequestProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto.UpgradeNormStructureStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.UpgradeNormStructureRequestEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.events.response.UpgradeNormStructureResponseEvent;
//import com.lvl6.mobsters.info.Structure;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.ResourceType;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class UpgradeNormStructureController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(UpgradeNormStructureController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public UpgradeNormStructureController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new UpgradeNormStructureRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_UPGRADE_NORM_STRUCTURE_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final UpgradeNormStructureRequestProto reqProto =
//		    ((UpgradeNormStructureRequestEvent) event).getUpgradeNormStructureRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final int userStructId = reqProto.getUserStructUuid();
//		final Timestamp timeOfUpgrade = new Timestamp(reqProto.getTimeOfUpgrade());
//		final int gemsSpent = reqProto.getGemsSpent();
//		// positive means refund, negative means charge user
//		final int resourceChange = reqProto.getResourceChange();
//		final ResourceType rt = reqProto.getResourceType();
//
//		final UpgradeNormStructureResponseProto.Builder resBuilder =
//		    UpgradeNormStructureResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_OTHER);
//
//		svcTxManager.beginTransaction();
//		try {
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			Structure currentStruct = null;
//			Structure nextLevelStruct = null;
//			final StructureForUser userStruct = RetrieveUtils.userStructRetrieveUtils()
//			    .getSpecificUserStruct(userStructId);
//
//			if (userStruct != null) {
//				currentStruct =
//				    StructureRetrieveUtils.getStructForStructId(userStruct.getStructId());
//				nextLevelStruct =
//				    StructureRetrieveUtils.getUpgradedStructForStructId(userStruct.getStructId());
//			}
//			int previousCash = 0;
//			int previousOil = 0;
//			int previousGems = 0;
//
//			final boolean legitUpgrade =
//			    checkLegitUpgrade(resBuilder, user, userStruct, currentStruct, nextLevelStruct,
//			        gemsSpent, resourceChange, rt, timeOfUpgrade);
//			final UpgradeNormStructureResponseEvent resEvent =
//			    new UpgradeNormStructureResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setUpgradeNormStructureResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in UpgradeNormStructureController.processRequestEvent", e);
//			}
//
//			if (legitUpgrade) {
//				previousCash = user.getCash();
//				previousOil = user.getOil();
//				previousGems = user.getGems();
//
//				final Map<String, Integer> money = new HashMap<String, Integer>();
//				writeChangesToDB(user, userStruct, nextLevelStruct, gemsSpent, resourceChange,
//				    rt, timeOfUpgrade, money);
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in UpgradeNormStructureController.processRequestEvent",
//					    e);
//				}
//
//				writeToUserCurrencyHistory(user, userStruct, currentStruct, nextLevelStruct,
//				    timeOfUpgrade, money, previousCash, previousOil, previousGems);
//			}
//		} catch (final Throwable e) {
//			LOG.error("exception in UpgradeNormStructure processEvent", e);
//			try {
//				resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_OTHER);
//				final UpgradeNormStructureResponseEvent resEvent =
//				    new UpgradeNormStructureResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setUpgradeNormStructureResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in UpgradeNormStructureController.processRequestEvent",
//					    e);
//				}
//			} catch (final Throwable e2) {
//				LOG.error("exception2 in UpgradeNormStructure processEvent", e2);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegitUpgrade( final Builder resBuilder, final User user,
//	    final StructureForUser userStruct, final Structure currentStruct,
//	    final Structure nextLevelStruct, final int gemsSpent, final int resourceChange,
//	    final ResourceType rt, final Timestamp timeOfUpgrade )
//	{
//		if ((user == null)
//		    || (userStruct == null)
//		    || (userStruct.getLastRetrieved() == null)) {
//			LOG.error("parameter passed in is null. user="
//			    + user
//			    + ", user struct="
//			    + userStruct
//			    + ", userStruct's last retrieve time="
//			    + userStruct.getLastRetrieved());
//			return false;
//		}
//		if (!userStruct.isComplete()) {
//			resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_NOT_BUILT_YET);
//			LOG.error("user struct is not complete yet");
//			return false;
//		}
//		if (null == nextLevelStruct) {
//			resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_AT_MAX_LEVEL_ALREADY);
//			LOG.error("user struct at max level already. struct is "
//			    + currentStruct);
//			return false;
//		}
//		if (timeOfUpgrade.getTime() < userStruct.getLastRetrieved()
//		    .getTime()) {
//			resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_NOT_BUILT_YET);
//			LOG.error("the upgrade time "
//			    + timeOfUpgrade
//			    + " is before the last time the building was retrieved:"
//			    + userStruct.getLastRetrieved());
//			return false;
//		}
//		// see if the user can upgrade it
//		if (user.getId() != userStruct.getUserId()) {
//			resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_NOT_USERS_STRUCT);
//			LOG.error("user struct belongs to someone else with id "
//			    + userStruct.getUserId());
//			return false;
//		}
//
//		final int userGems = user.getGems();
//		if ((gemsSpent > 0)
//		    && (userGems < gemsSpent)) {
//			LOG.error("user has "
//			    + userGems
//			    + " gems; trying to spend "
//			    + gemsSpent
//			    + " and "
//			    + resourceChange
//			    + " "
//			    + rt
//			    + " to upgrade to structure="
//			    + nextLevelStruct);
//			resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_NOT_ENOUGH_GEMS);
//			return false;
//		}
//		if (ResourceType.CASH.equals(rt)) {
//			if (user.getCash() < resourceChange) {
//				resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_NOT_ENOUGH_CASH);
//				LOG.error("user doesn't have enough cash, has "
//				    + user.getCash()
//				    + ", needs "
//				    + resourceChange);
//				return false;
//			}
//		} else if (ResourceType.OIL.equals(rt)) {
//			if (user.getOil() < resourceChange) {
//				resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_NOT_ENOUGH_OIL);
//				LOG.error("user doesn't have enough gems, has "
//				    + user.getGems()
//				    + ", needs "
//				    + resourceChange);
//				return false;
//			}
//		}
//
//		/*
//		 * //TODO: only make one user struct retrieve call
//		 * List<StructureForUser> userStructs =
//		 * RetrieveUtils.userStructRetrieveUtils
//		 * ().getUserStructsForUser(user.getId()); if (userStructs != null) {
//		 * for (StructureForUser us : userStructs) { if (!us.isComplete() &&
//		 * us.getUpgradeStartTime() != null) {
//		 * resBuilder.setStatus(UpgradeNormStructureStatus
//		 * .FAIL_ANOTHER_STRUCT_STILL_UPGRADING);
//		 * LOG.error("another struct is still upgrading: user struct=" + us);
//		 * return false; } } }
//		 */
//
//		resBuilder.setStatus(UpgradeNormStructureStatus.SUCCESS);
//		return true;
//	}
//
//	private void writeChangesToDB( final User user, final StructureForUser userStruct,
//	    final Structure upgradedStruct, final int gemsSpent, final int resourceChange,
//	    final ResourceType rt, final Timestamp timeOfUpgrade, final Map<String, Integer> money )
//	{
//
//		final int newStructId = upgradedStruct.getId();
//		// upgrade the user's struct
//		if (!UpdateUtils.get()
//		    .updateBeginUpgradingUserStruct(userStruct.getId(), newStructId, timeOfUpgrade)) {
//			LOG.error("problem with changing time of upgrade to "
//			    + timeOfUpgrade
//			    + " and marking as incomplete, the user struct "
//			    + userStruct);
//		}
//
//		// charge the user
//		int cashChange = 0;
//		int oilChange = 0;
//		final int gemChange = -1
//		    * gemsSpent;
//
//		if (ResourceType.CASH.equals(rt)) {
//			cashChange = resourceChange;
//		} else if (ResourceType.OIL.equals(rt)) {
//			oilChange = resourceChange;
//		}
//
//		final int num = user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
//		if (1 != num) {
//			LOG.error("problem with updating user currency. gemChange="
//			    + gemChange
//			    + " cashChange="
//			    + cashChange
//			    + "\t oilChange="
//			    + oilChange
//			    + "\t numRowsUpdated="
//			    + num);
//		} else {// things went ok
//			if (0 != gemChange) {
//				money.put(MiscMethods.gems, gemChange);
//			}
//			if (0 != cashChange) {
//				money.put(MiscMethods.cash, cashChange);
//			}
//			if (0 != oilChange) {
//				money.put(MiscMethods.oil, oilChange);
//			}
//		}
//
//	}
//
//	private void writeToUserCurrencyHistory( final User aUser,
//	    final StructureForUser userStruct, final Structure curStruct,
//	    final Structure upgradedStruct, final Timestamp timeOfUpgrade,
//	    final Map<String, Integer> money, final int previousCash, final int previousOil,
//	    final int previousGems )
//	{
//
//		final String userUuid = aUser.getId();
//		final String userStructUuid = userStruct.getId();
//		final int prevStructId = curStruct.getId();
//		final int prevLevel = curStruct.getLevel();
//		final StringBuilder structDetailsSb = new StringBuilder();
//		structDetailsSb.append("uStructId:");
//		structDetailsSb.append(userStructUuid);
//		structDetailsSb.append(" preStructId:");
//		structDetailsSb.append(prevStructId);
//		structDetailsSb.append(" prevLevel:");
//		structDetailsSb.append(prevLevel);
//		final String structDetails = structDetailsSb.toString();
//
//		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
//		final String reasonForChange = ControllerConstants.UCHRFC__UPGRADE_NORM_STRUCT;
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> details = new HashMap<String, String>();
//		final String gems = MiscMethods.gems;
//		final String oil = MiscMethods.oil;
//		final String cash = MiscMethods.cash;
//
//		previousCurrencies.put(cash, previousCash);
//		previousCurrencies.put(oil, previousOil);
//		previousCurrencies.put(gems, previousGems);
//
//		currentCurrencies.put(cash, aUser.getCash());
//		currentCurrencies.put(oil, aUser.getOil());
//		currentCurrencies.put(gems, aUser.getGems());
//
//		reasonsForChanges.put(cash, reasonForChange);
//		reasonsForChanges.put(oil, reasonForChange);
//		reasonsForChanges.put(gems, reasonForChange);
//
//		details.put(cash, structDetails);
//		details.put(oil, structDetails);
//		details.put(gems, structDetails);
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, timeOfUpgrade, money,
//		    previousCurrencies, currentCurrencies, reasonsForChanges, details);
//	}
//}
