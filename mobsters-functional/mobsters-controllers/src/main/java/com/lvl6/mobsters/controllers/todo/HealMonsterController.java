package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventMonsterProto.HealMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.HealMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.HealMonsterResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMonsterProto.HealMonsterResponseProto.HealMonsterStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.HealMonsterRequestEvent;
import com.lvl6.mobsters.events.response.HealMonsterResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterHealingProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class HealMonsterController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(HealMonsterController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public HealMonsterController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new HealMonsterRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_HEAL_MONSTER_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final HealMonsterRequestProto reqProto =
		    ((HealMonsterRequestEvent) event).getHealMonsterRequestProto();
		LOG.info("reqProto="
		    + reqProto);

		// get values sent from the client (the request proto)
		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
		final String userUuid = senderProto.getUserUuid();
		final List<UserMonsterHealingProto> umhDelete = reqProto.getUmhDeleteList();
		final List<UserMonsterHealingProto> umhUpdate = reqProto.getUmhUpdateList();
		final List<UserMonsterHealingProto> umhNew = reqProto.getUmhNewList();
		// positive means refund, negative means charge user
		final int cashChange = reqProto.getCashChange();
		final int gemCostForHealing = reqProto.getGemCostForHealing();

		final boolean isSpeedup = reqProto.getIsSpeedup();
		final int gemsForSpeedup = reqProto.getGemsForSpeedup();
		final List<UserMonsterCurrentHealthProto> umchpList = reqProto.getUmchpList();
		Map<Long, Integer> userMonsterIdToExpectedHealth = new HashMap<Long, Integer>();
		final List<Long> userMonsterUuids =
		    MonsterStuffUtils.getUserMonsterUuids(umchpList, userMonsterIdToExpectedHealth);

		final int gemCost = gemCostForHealing
		    + gemsForSpeedup;// reqProto.getTotalGemCost();
		final Timestamp curTime = new Timestamp((new Date()).getTime());
		final int maxCash = senderResourcesProto.getMaxCash();

		final Map<Long, UserMonsterHealingProto> deleteMap =
		    MonsterStuffUtils.convertIntoUserMonsterIdToUmhpProtoMap(umhDelete);
		final Map<Long, UserMonsterHealingProto> updateMap =
		    MonsterStuffUtils.convertIntoUserMonsterIdToUmhpProtoMap(umhUpdate);
		final Map<Long, UserMonsterHealingProto> newMap =
		    MonsterStuffUtils.convertIntoUserMonsterIdToUmhpProtoMap(umhNew);

		LOG.info("umchpList="
		    + umchpList
		    + "\t userMonsterIdToExpectedHealth"
		    + userMonsterIdToExpectedHealth
		    + "\t userMonsterUuids="
		    + userMonsterUuids);

		// set some values to send to the client (the response proto)
		final HealMonsterResponseProto.Builder resBuilder =
		    HealMonsterResponseProto.newBuilder();
		resBuilder.setSender(senderResourcesProto);
		resBuilder.setStatus(HealMonsterStatus.FAIL_OTHER); // default

		svcTxManager.beginTransaction();
		try {
			int previousCash = 0;
			int previousGems = 0;
			// get whatever we need from the database
			final User aUser = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			final Map<Long, MonsterHealingForUser> alreadyHealing =
			    MonsterHealingForUserRetrieveUtils.getMonstersForUser(userUuid);
			final Map<Long, MonsterEnhancingForUser> alreadyEnhancing =
			    MonsterEnhancingForUserRetrieveUtils.getMonstersForUser(userUuid);
			final MonsterEvolvingForUser evolution =
			    MonsterEvolvingForUserRetrieveUtils.getEvolutionForUser(userUuid);

			// retrieve only the new monsters that will be healed
			Map<Long, MonsterForUser> existingUserMonsters =
			    new HashMap<Long, MonsterForUser>();
			if ((null != newMap)
			    && !newMap.isEmpty()) {
				final Set<Long> newUuids = new HashSet<Long>();
				newUuids.addAll(newMap.keySet());
				existingUserMonsters = RetrieveUtils.monsterForUserRetrieveUtils()
				    .getSpecificOrAllUserMonstersForUser(userUuid, newUuids);
			}

			final boolean legit =
			    checkLegit(resBuilder, aUser, userUuid, cashChange, gemCost,
			        existingUserMonsters, alreadyHealing, alreadyEnhancing, deleteMap,
			        updateMap, newMap, userMonsterUuids, evolution);

			boolean successful = false;
			// first two maps are for different heal monster and speed up heal
			// monster
			final Map<String, Integer> money = new HashMap<String, Integer>();
			final Map<String, Integer> moneyForHealSpeedup = new HashMap<String, Integer>();
			// this map is both combined
			final Map<String, Integer> changeMap = new HashMap<String, Integer>();
			if (legit) {
				previousCash = aUser.getCash();
				previousGems = aUser.getGems();

				// from HealMonsterWaitTimeCompleteController
				userMonsterIdToExpectedHealth =
				    getValidEntries(userMonsterUuids, userMonsterIdToExpectedHealth);
				successful =
				    writeChangesToDb(aUser, userUuid, cashChange, gemCost, deleteMap,
				        updateMap, newMap, gemCostForHealing, money, userMonsterUuids,
				        userMonsterIdToExpectedHealth, isSpeedup, gemsForSpeedup,
				        moneyForHealSpeedup, changeMap, maxCash);
			}

			if (successful) {
				resBuilder.setStatus(HealMonsterStatus.SUCCESS);
			}

			final HealMonsterResponseEvent resEvent = new HealMonsterResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setHealMonsterResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in HealMonsterController.processRequestEvent", e);
			}

			if (successful) {
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error("fatal exception in HealMonsterController.processRequestEvent", e);
				}
				// TODO: WRITE TO monster healing HISTORY
				writeToUserCurrencyHistory(aUser, changeMap, money, curTime, previousCash,
				    previousGems, deleteMap, updateMap, newMap, moneyForHealSpeedup);
			}
		} catch (final Exception e) {
			LOG.error("exception in HealMonsterController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(HealMonsterStatus.FAIL_OTHER);
				final HealMonsterResponseEvent resEvent =
				    new HealMonsterResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setHealMonsterResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in HealMonsterController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in HealMonsterController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	/*
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value. delete, update, new maps MIGHT BE
	 * MODIFIED.
	 * 
	 * from HealMonsterWaitTimeComplete controller logic
	 * 
	 * @healedUp MIGHT ALSO BE MODIFIED.
	 * 
	 * For the most part, will always return success. Why? (Will return fail if
	 * user does not have enough funds.) Answer: For the map
	 * 
	 * delete - The monsters to be removed from healing will only be the ones
	 * the user already has in healing. update - Same logic as above. new - Same
	 * as above.
	 * 
	 * Ex. If user wants to delete a monster, 'A', that isn't healing, along
	 * with some monsters already healing, 'B', i.e. wants to delete (A, B),
	 * then only the valid monster(s), 'B', will be deleted. Same logic with
	 * update and new.
	 */
	private boolean checkLegit( final Builder resBuilder, final User u, final String userUuid,
	    final int cashChange, final int gemCost,
	    final Map<Long, MonsterForUser> existingUserMonsters,
	    final Map<Long, MonsterHealingForUser> alreadyHealing,
	    final Map<Long, MonsterEnhancingForUser> alreadyEnhancing,
	    final Map<Long, UserMonsterHealingProto> deleteMap,
	    final Map<Long, UserMonsterHealingProto> updateMap,
	    final Map<Long, UserMonsterHealingProto> newMap, final List<Long> healedUp,
	    final MonsterEvolvingForUser evolution )
	{
		if (null == u) {
			LOG.error("unexpected error: user is null. user="
			    + u
			    + "\t deleteMap="
			    + deleteMap
			    + "\t updateMap="
			    + updateMap
			    + "\t newMap="
			    + newMap);
			return false;
		}

		// CHECK MONEY
		final int userGems = u.getGems();
		if (gemCost > userGems) {
			LOG.error("user error: user does not have enough gems. userGems="
			    + userGems
			    + "\t gemCost="
			    + gemCost
			    + "\t user="
			    + u);
			resBuilder.setStatus(HealMonsterStatus.FAIL_INSUFFICIENT_FUNDS);
			return false;
		}

		// scenario can be user has insufficient cash but has enough
		// gems to cover the difference
		final int userCash = u.getCash();
		final int cashCost = -1
		    * cashChange;
		if ((gemCost == 0)
		    && (cashCost > userCash)) {
			// user doesn't have enough cash and is not paying gems.

			LOG.error("user error: user has too little cash and not using gems. userCash="
			    + userCash
			    + "\t cashCost="
			    + cashCost
			    + "\t user="
			    + u);
			resBuilder.setStatus(HealMonsterStatus.FAIL_INSUFFICIENT_FUNDS);
			return false;
		}
		// if user has insufficient cash but gems is nonzero, take it on full
		// faith
		// client calculated things correctly

		// retain only the userMonsters, the client sent, that are in healing
		boolean keepThingsInDomain = true;
		boolean keepThingsNotInDomain = false;
		final Set<Long> alreadyHealingUuids = alreadyHealing.keySet();
		MonsterStuffUtils.retainValidMonsters(alreadyHealingUuids, deleteMap,
		    keepThingsInDomain, keepThingsNotInDomain);
		MonsterStuffUtils.retainValidMonsters(alreadyHealingUuids, updateMap,
		    keepThingsInDomain, keepThingsNotInDomain);

		// retain only the userMonsters, the client sent, that are in the db
		final Set<Long> existingUuids = existingUserMonsters.keySet();
		MonsterStuffUtils.retainValidMonsters(existingUuids, newMap, keepThingsInDomain,
		    keepThingsNotInDomain);

		// retain only the userMonsters, the client sent, that are not in
		// enhancing
		keepThingsInDomain = false;
		keepThingsNotInDomain = true;
		final Set<Long> alreadyEnhancingUuids = alreadyEnhancing.keySet();
		MonsterStuffUtils.retainValidMonsters(alreadyEnhancingUuids, newMap,
		    keepThingsInDomain, keepThingsNotInDomain);

		// retain only the userMonsters, the client sent, that are not in
		// evolutions
		final Set<Long> idsInEvolutions =
		    MonsterStuffUtils.getUserMonsterUuidsUsedInEvolution(evolution, null);
		MonsterStuffUtils.retainValidMonsters(idsInEvolutions, newMap, keepThingsInDomain,
		    keepThingsNotInDomain);

		// FROM HealMonsterWaitTimeComplete CONTROLLER
		// modify healedUp to contain only those that exist
		MonsterStuffUtils.retainValidMonsterUuids(alreadyHealingUuids, healedUp);

		return true;
	}

	// only the entries in the map that have their key in validUuids will be
	// kept
	// idsToValues contains keys that are in validUuids and some that aren't
	private Map<Long, Integer> getValidEntries( final List<Long> validUuids,
	    final Map<Long, Integer> idsToValues )
	{

		final Map<Long, Integer> returnMap = new HashMap<Long, Integer>();

		for (final long id : validUuids) {
			final int value = idsToValues.get(id);
			returnMap.put(id, value);
		}
		return returnMap;
	}

	private boolean writeChangesToDb( final User u, final int uId, int cashChange,
	    final int gemCost, final Map<Long, UserMonsterHealingProto> protoDeleteMap,
	    final Map<Long, UserMonsterHealingProto> protoUpdateMap,
	    final Map<Long, UserMonsterHealingProto> protoNewMap, final int gemCostForHealing,
	    final Map<String, Integer> moneyForHealing, final List<Long> userMonsterUuids,
	    final Map<Long, Integer> userMonsterUuidsToHealths, final boolean isSpeedup,
	    final int gemsForSpeedup, final Map<String, Integer> moneyForSpeedup,
	    final Map<String, Integer> changeMap, final int maxCash )
	{

		LOG.info("cashChange="
		    + cashChange);
		LOG.info("gemCost="
		    + gemCost);
		LOG.info("deleteMap="
		    + protoDeleteMap);
		LOG.info("updateMap="
		    + protoUpdateMap);
		LOG.info("newMap="
		    + protoNewMap);
		LOG.info("gemCostForHealing="
		    + gemCostForHealing);
		LOG.info("isSpeedup="
		    + isSpeedup);
		LOG.info("gemsForSpedup"
		    + gemsForSpeedup);

		final int oilChange = 0;
		final int gemChange = -1
		    * gemCost;

		// if user is getting cash back, make sure it doesn't exceed his limit
		if (cashChange > 0) {
			final int curCash = Math.min(u.getCash(), maxCash); // in case
																// user's cash
			// is more than
			// maxCash.
			LOG.info("curCash="
			    + curCash);
			final int maxCashUserCanGain = maxCash
			    - curCash;
			LOG.info("maxCashUserCanGain="
			    + maxCashUserCanGain);
			cashChange = Math.min(cashChange, maxCashUserCanGain);
		}

		// if checks are here because the changes are 0 if the
		// HealMonsterWaitTimeComplete
		// feature part of this controller is being processed or if user reached
		// max resources
		if ((0 != cashChange)
		    || (0 != gemChange)) {

			// CHARGE THE USER
			LOG.info("user before funds change. u="
			    + u
			    + "\t cashChange="
			    + cashChange
			    + "\t oilChange="
			    + oilChange
			    + "\t gemChange="
			    + gemChange);
			final int num = u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
			LOG.info("user after funds change. u="
			    + u);
			if (num != 1) {
				LOG.error("problem with updating user's funds. cashChange="
				    + cashChange
				    + ", gemCost="
				    + gemCost
				    + ", user="
				    + u
				    + "\t numUpdated="
				    + num);
				return false;
			} else {
				// things went ok
				if (0 != cashChange) {
					moneyForHealing.put(MiscMethods.cash, cashChange);
					changeMap.put(MiscMethods.cash, cashChange);
				}
				if (0 != gemCostForHealing) {
					moneyForHealing.put(MiscMethods.gems, gemCostForHealing);
				}
				if (0 != gemsForSpeedup) {
					moneyForSpeedup.put(MiscMethods.gems, gemsForSpeedup);
				}
				if (0 != gemCost) {
					changeMap.put(MiscMethods.gems, gemChange);
				}

			}
		}

		// delete the selected monsters from the healing table, if there are
		// any to delete
		if (!protoDeleteMap.isEmpty()) {
			final List<Long> deleteUuids = new ArrayList<Long>(protoDeleteMap.keySet());
			final int num = DeleteUtils.get()
			    .deleteMonsterHealingForUser(uId, deleteUuids);
			LOG.info("deleted monster healing rows. numDeleted="
			    + num
			    + "\t protoDeleteMap="
			    + protoDeleteMap);
		}

		// convert protos to java counterparts
		final List<MonsterHealingForUser> updateList =
		    MonsterStuffUtils.convertToMonsterHealingForUser(uId, protoUpdateMap);
		final List<MonsterHealingForUser> newList =
		    MonsterStuffUtils.convertToMonsterHealingForUser(uId, protoNewMap);

		final List<MonsterHealingForUser> updateAndNew = new ArrayList<MonsterHealingForUser>();
		updateAndNew.addAll(updateList);
		updateAndNew.addAll(newList);

		LOG.info("updated and new monsters for healing: "
		    + updateAndNew);

		// client could have deleted one item from two item queue, or added at
		// least one item
		if (!updateAndNew.isEmpty()) {
			// update and insert the new monsters
			final int num = UpdateUtils.get()
			    .updateUserMonsterHealing(uId, updateAndNew);
			LOG.info("updated monster healing rows. numUpdated/inserted="
			    + num);
		}

		// don't unequip the monsters
		// //for the new monsters, ensure that the monsters are "unequipped"
		// if (!protoNewMap.isEmpty()) {
		// //for the new monsters, set the teamSlotNum to 0
		// int size = protoNewMap.size();
		// List<Long> userMonsterIdList = new
		// ArrayList<Long>(protoNewMap.keySet());
		// List<Integer> teamSlotNumList = Collections.nCopies(size, 0);
		// num =
		// UpdateUtils.get().updateNullifyUserMonstersTeamSlotNum(userMonsterIdList,
		// teamSlotNumList);
		// LOG.info("updated user monster rows. numUpdated=" + num);
		// }

		// LOGIC FROM HealMonsterWaitTimeCompleteController
		if ((null != userMonsterUuidsToHealths)
		    && !userMonsterUuidsToHealths.isEmpty()) {
			// HEAL THE MONSTER
			final int num = UpdateUtils.get()
			    .updateUserMonstersHealth(userMonsterUuidsToHealths);
			LOG.info("num updated="
			    + num);
		}
		// should always execute, but who knows...
		if ((null != userMonsterUuids)
		    && !userMonsterUuids.isEmpty()) {
			// delete the selected monsters from the healing table
			final int num = DeleteUtils.get()
			    .deleteMonsterHealingForUser(uId, userMonsterUuids);
			LOG.info("deleted monster healing rows. numDeleted="
			    + num
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
		}
		return true;
	}

	private void writeToUserCurrencyHistory( final User aUser,
	    final Map<String, Integer> changeMap, final Map<String, Integer> moneyForHealing,
	    final Timestamp curTime, final int previousCash, final int previousGems,
	    final Map<Long, UserMonsterHealingProto> deleteMap,
	    final Map<Long, UserMonsterHealingProto> updateMap,
	    final Map<Long, UserMonsterHealingProto> newMap,
	    final Map<String, Integer> moneyForHealSpeedup )
	{

		final String userUuid = aUser.getId();
		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> details = new HashMap<String, String>();

		final StringBuilder reasonForChange = new StringBuilder();
		reasonForChange.append(ControllerConstants.UCHRFC__HEAL_MONSTER_OR_SPED_UP_HEALING);
		final String gems = MiscMethods.gems;
		final String cash = MiscMethods.cash;

		final StringBuilder detailSb = new StringBuilder();
		if (!moneyForHealing.isEmpty()) {
			detailSb.append("heal monster info: ");
		}
		if (moneyForHealing.containsKey(gems)) {
			detailSb.append("gemChange=");
			detailSb.append(moneyForHealing.get(gems));
			detailSb.append(" ");
		}
		if (moneyForHealing.containsKey(cash)) {
			detailSb.append("cashChange=");
			detailSb.append(moneyForHealing.get(cash));
			detailSb.append(" ");
		}
		// could just individually add in the ids or something else, but eh,
		// lazy
		// not really necessary to record ids, but maybe more info is better
		if ((null != deleteMap)
		    && !deleteMap.isEmpty()) {
			detailSb.append("deleted=");
			detailSb.append(deleteMap.keySet());
			detailSb.append(" ");
		}
		if ((null != updateMap)
		    && !updateMap.isEmpty()) {
			detailSb.append("updated=");
			detailSb.append(updateMap.keySet());
			detailSb.append(" ");
		}
		if ((null != newMap)
		    && !newMap.isEmpty()) {
			detailSb.append("new=");
			detailSb.append(newMap.keySet());
			detailSb.append(" ");
		}

		if (!moneyForHealSpeedup.isEmpty()) {
			detailSb.append("sped up healing info: ");
		}
		if (moneyForHealSpeedup.containsKey(gems)) {
			detailSb.append("gemChange= ");
			detailSb.append(moneyForHealSpeedup.get(gems));
		}

		previousCurrencies.put(gems, previousGems);
		previousCurrencies.put(cash, previousCash);
		currentCurrencies.put(gems, aUser.getGems());
		currentCurrencies.put(cash, aUser.getCash());
		reasonsForChanges.put(gems, reasonForChange.toString());
		reasonsForChanges.put(cash, reasonForChange.toString());
		details.put(gems, detailSb.toString());
		details.put(cash, detailSb.toString());

		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, changeMap,
		    previousCurrencies, currentCurrencies, reasonsForChanges, details);

	}

}
