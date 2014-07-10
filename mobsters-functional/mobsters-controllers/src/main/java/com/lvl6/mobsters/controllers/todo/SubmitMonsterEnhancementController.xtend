package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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
import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementResponseProto.SubmitMonsterEnhancementStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SubmitMonsterEnhancementRequestEvent;
import com.lvl6.mobsters.events.response.SubmitMonsterEnhancementResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserEnhancementItemProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class SubmitMonsterEnhancementController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(SubmitMonsterEnhancementController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public SubmitMonsterEnhancementController()
	{
		numAllocatedThreads = 3;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new SubmitMonsterEnhancementRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_SUBMIT_MONSTER_ENHANCEMENT_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final SubmitMonsterEnhancementRequestProto reqProto =
		    ((SubmitMonsterEnhancementRequestEvent) event).getSubmitMonsterEnhancementRequestProto();

		// LOG.info("reqProto=" + reqProto);

		// get data client sent
		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
		final List<UserEnhancementItemProto> ueipDelete = reqProto.getUeipDeleteList();
		final List<UserEnhancementItemProto> ueipUpdated = reqProto.getUeipUpdateList();
		final List<UserEnhancementItemProto> ueipNew = reqProto.getUeipNewList();
		final String userUuid = senderProto.getUserUuid();
		// positive value, need to convert to negative when updating user
		final int gemsSpent = reqProto.getGemsSpent();
		// positive means refund, negative means charge user
		final int oilChange = reqProto.getOilChange();
		final Timestamp clientTime = new Timestamp((new Date()).getTime());
		final int maxOil = senderResourcesProto.getMaxOil();

		final Map<Long, UserEnhancementItemProto> deleteMap =
		    MonsterStuffUtils.convertIntoUserMonsterIdToUeipProtoMap(ueipDelete);
		final Map<Long, UserEnhancementItemProto> updateMap =
		    MonsterStuffUtils.convertIntoUserMonsterIdToUeipProtoMap(ueipUpdated);
		final Map<Long, UserEnhancementItemProto> newMap =
		    MonsterStuffUtils.convertIntoUserMonsterIdToUeipProtoMap(ueipNew);

		// set some values to send to the client (the response proto)
		final SubmitMonsterEnhancementResponseProto.Builder resBuilder =
		    SubmitMonsterEnhancementResponseProto.newBuilder();
		resBuilder.setSender(senderResourcesProto);
		resBuilder.setStatus(SubmitMonsterEnhancementStatus.FAIL_OTHER);

		getLocker().lockPlayer(userUuid, getClass().getSimpleName());
		try {
			int previousOil = 0;
			int previousGems = 0;
			// get whatever we need from the database
			final User aUser = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			final Map<Long, MonsterEnhancingForUser> alreadyEnhancing =
			    MonsterEnhancingForUserRetrieveUtils.getMonstersForUser(userUuid);
			final Map<Long, MonsterHealingForUser> alreadyHealing =
			    MonsterHealingForUserRetrieveUtils.getMonstersForUser(userUuid);
			final MonsterEvolvingForUser evolution =
			    MonsterEvolvingForUserRetrieveUtils.getEvolutionForUser(userUuid);

			// retrieve only the new monsters that will be used in enhancing
			final Set<Long> newUuids = new HashSet<Long>();
			newUuids.addAll(newMap.keySet());
			final Map<Long, MonsterForUser> existingUserMonsters =
			    RetrieveUtils.monsterForUserRetrieveUtils()
			        .getSpecificOrAllUserMonstersForUser(userUuid, newUuids);

			final boolean legitMonster =
			    checkLegit(resBuilder, aUser, userUuid, existingUserMonsters, alreadyEnhancing,
			        alreadyHealing, deleteMap, updateMap, newMap, evolution, gemsSpent,
			        oilChange);

			boolean successful = false;
			final Map<String, Integer> money = new HashMap<String, Integer>();

			if (legitMonster) {
				previousOil = aUser.getOil();
				previousGems = aUser.getGems();
				successful =
				    writeChangesToDB(aUser, userUuid, gemsSpent, oilChange, deleteMap,
				        updateMap, newMap, money, maxOil);
			}

			if (successful) {
				resBuilder.setStatus(SubmitMonsterEnhancementStatus.SUCCESS);
			}

			final SubmitMonsterEnhancementResponseEvent resEvent =
			    new SubmitMonsterEnhancementResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setSubmitMonsterEnhancementResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in SubmitMonsterEnhancementController.processRequestEvent",
				    e);
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
					LOG.error(
					    "fatal exception in SubmitMonsterEnhancementController.processRequestEvent",
					    e);
				}

				writeToUserCurrencyHistory(aUser, clientTime, money, previousOil, previousGems,
				    deleteMap, updateMap, newMap);
			}

		} catch (final Exception e) {
			LOG.error("exception in EnhanceMonster processEvent", e);
		} finally {
			getLocker().unlockPlayer(userUuid, getClass().getSimpleName());
		}
	}

	/*
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value. delete, update, new maps MIGHT BE
	 * MODIFIED.
	 * 
	 * For the most part, will always return success. Why? (Will return fail if
	 * user does not have enough funds.) Answer: For the map
	 * 
	 * delete - The monsters to be removed from enhancing will only be the ones
	 * the user already has in enhancing. update - Same logic as above. new -
	 * Same as above.
	 * 
	 * Ex. If user wants to delete a monster (A) that isn't enhancing, along
	 * with some monsters already enhancing (B), only the valid monsters (B)
	 * will be deleted. Same logic with update and new.
	 */
	private boolean checkLegit( final Builder resBuilder, final User u, final String userUuid,
	    final Map<Long, MonsterForUser> existingUserMonsters,
	    final Map<Long, MonsterEnhancingForUser> alreadyEnhancing,
	    final Map<Long, MonsterHealingForUser> alreadyHealing,
	    final Map<Long, UserEnhancementItemProto> deleteMap,
	    final Map<Long, UserEnhancementItemProto> updateMap,
	    final Map<Long, UserEnhancementItemProto> newMap,
	    final MonsterEvolvingForUser evolution, final int gemsSpent, final int oilChange )
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
		// NOTE: RETAIN CASES ONLY FILTER THINGS, AND NOT CAUSE THIS REQUEST TO
		// FAIL
		// retain only the userMonsters in deleteMap and updateMap that are in
		// enhancing
		boolean keepThingsInDomain = true;
		boolean keepThingsNotInDomain = false;
		final Set<Long> alreadyEnhancingUuids = alreadyEnhancing.keySet();
		if ((null != deleteMap)
		    && !deleteMap.isEmpty()) {
			MonsterStuffUtils.retainValidMonsters(alreadyEnhancingUuids, deleteMap,
			    keepThingsInDomain, keepThingsNotInDomain);
		}

		if ((null != updateMap)
		    && !updateMap.isEmpty()) {
			MonsterStuffUtils.retainValidMonsters(alreadyEnhancingUuids, updateMap,
			    keepThingsInDomain, keepThingsNotInDomain);
		}

		if ((null != newMap)
		    && !newMap.isEmpty()) {
			// retain only the userMonsters in newMap that are in the db
			final Set<Long> existingUuids = existingUserMonsters.keySet();
			MonsterStuffUtils.retainValidMonsters(existingUuids, newMap, keepThingsInDomain,
			    keepThingsNotInDomain);

			// retain only the userMonsters in newMap that are not in healing
			keepThingsInDomain = false;
			keepThingsNotInDomain = true;
			final Set<Long> alreadyHealingUuids = alreadyHealing.keySet();
			MonsterStuffUtils.retainValidMonsters(alreadyHealingUuids, newMap,
			    keepThingsInDomain, keepThingsNotInDomain);

			// retain only the userMonsters in newMap that are not in evolutions
			final Set<Long> idsInEvolutions =
			    MonsterStuffUtils.getUserMonsterUuidsUsedInEvolution(evolution, null);
			MonsterStuffUtils.retainValidMonsters(idsInEvolutions, newMap, keepThingsInDomain,
			    keepThingsNotInDomain);

		}

		// CHECK MONEY
		if (!hasEnoughGems(resBuilder, u, gemsSpent, oilChange, deleteMap, updateMap, newMap)) {
			return false;
		}

		if (!hasEnoughOil(resBuilder, u, gemsSpent, oilChange, deleteMap, updateMap, newMap)) {
			return false;
		}

		return true;
	}

	private boolean hasEnoughGems( final Builder resBuilder, final User u, final int gemsSpent,
	    final int oilChange, final Map<Long, UserEnhancementItemProto> deleteMap,
	    final Map<Long, UserEnhancementItemProto> updateMap,
	    final Map<Long, UserEnhancementItemProto> newMap )
	{
		final int userGems = u.getGems();
		// if user's aggregate gems is < cost, don't allow transaction
		if (userGems < gemsSpent) {
			LOG.error("user error: user does not have enough gems. userGems="
			    + userGems
			    + "\t gemsSpent="
			    + gemsSpent
			    + "\t deleteMap="
			    + deleteMap
			    + "\t newMap="
			    + newMap
			    + "\t updateMap="
			    + updateMap
			    + "\t cashChange="
			    + oilChange
			    + "\t user="
			    + u);
			resBuilder.setStatus(SubmitMonsterEnhancementStatus.FAIL_INSUFFICIENT_GEMS);
			return false;
		}
		return true;
	}

	private boolean hasEnoughOil( final Builder resBuilder, final User u, final int oilChange,
	    final int gemsSpent, final Map<Long, UserEnhancementItemProto> deleteMap,
	    final Map<Long, UserEnhancementItemProto> updateMap,
	    final Map<Long, UserEnhancementItemProto> newMap )
	{
		final int userOil = u.getOil();
		// positive 'cashChange' means refund, negative means charge user
		final int cost = -1
		    * oilChange;

		// if user not spending gems and is just spending cash, check if he has
		// enough
		if ((0 == gemsSpent)
		    && (userOil < cost)) {
			LOG.error("user error: user does not have enough oil. userOil="
			    + userOil
			    + "\t cost="
			    + cost
			    + "\t deleteMap="
			    + deleteMap
			    + "\t newMap="
			    + newMap
			    + "\t updateMap="
			    + updateMap
			    + "\t user="
			    + u);
			resBuilder.setStatus(SubmitMonsterEnhancementStatus.FAIL_INSUFFICIENT_OIL);
			return false;
		}
		return true;
	}

	private boolean writeChangesToDB( final User user, final int uId, final int gemsSpent,
	    int oilChange, final Map<Long, UserEnhancementItemProto> protoDeleteMap,
	    final Map<Long, UserEnhancementItemProto> protoUpdateMap,
	    final Map<Long, UserEnhancementItemProto> protoNewMap,
	    final Map<String, Integer> money, final int maxOil )
	{

		// CHARGE THE USER
		final int cashChange = 0;
		final int gemChange = -1
		    * gemsSpent;

		// if user is getting oil back, make sure it doesn't exceed his limit
		if (oilChange > 0) {
			final int curOil = Math.min(user.getOil(), maxOil); // in case
																// user's oil
			// is more than
			// maxOil.
			final int maxOilUserCanGain = maxOil
			    - curOil;
			oilChange = Math.min(oilChange, maxOilUserCanGain);
		}

		if ((0 != oilChange)
		    || (0 != gemChange)) {

			// LOG.info("oilChange=" + oilChange + "\t gemChange=" + gemChange);
			final int numChange =
			    user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
			if (1 != numChange) {
				LOG.warn("problem with updating user stats: gemChange="
				    + gemChange
				    + ", oilChange="
				    + oilChange
				    + ", user is "
				    + user
				    + "\t perhaps base monster deleted \t protoDeleteMap="
				    + protoDeleteMap);
			} else {
				// everything went well
				if (0 != oilChange) {
					money.put(MiscMethods.oil, oilChange);
				}
				if (0 != gemsSpent) {
					money.put(MiscMethods.gems, gemChange);
				}
			}
		}
		// LOG.info("deleteMap=" + protoDeleteMap);
		// LOG.info("updateMap=" + protoUpdateMap);
		// LOG.info("newMap=" + protoNewMap);

		int num = 0;
		// delete everything left in the map, if there are any
		if (!protoDeleteMap.isEmpty()) {
			final List<Long> deleteUuids = new ArrayList<Long>(protoDeleteMap.keySet());
			num = DeleteUtils.get()
			    .deleteMonsterEnhancingForUser(uId, deleteUuids);
			LOG.info("deleted monster enhancing rows. numDeleted="
			    + num
			    + "\t protoDeleteMap="
			    + protoDeleteMap);
		}

		// convert protos to java counterparts
		final List<MonsterEnhancingForUser> updateMap =
		    MonsterStuffUtils.convertToMonsterEnhancingForUser(uId, protoUpdateMap);
		LOG.info("updateMap="
		    + updateMap);

		final List<MonsterEnhancingForUser> newMap =
		    MonsterStuffUtils.convertToMonsterEnhancingForUser(uId, protoNewMap);
		LOG.info("newMap="
		    + newMap);

		final List<MonsterEnhancingForUser> updateAndNew =
		    new ArrayList<MonsterEnhancingForUser>();
		updateAndNew.addAll(updateMap);
		updateAndNew.addAll(newMap);
		// update everything in enhancing table that is in update and new map
		if ((null != updateAndNew)
		    && !updateAndNew.isEmpty()) {
			num = UpdateUtils.get()
			    .updateUserMonsterEnhancing(uId, updateAndNew);
			LOG.info("updated monster enhancing rows. numUpdated/inserted="
			    + num);
		}

		// for the new monsters, ensure that the monsters are "unequipped"
		if ((null != protoNewMap)
		    && !protoNewMap.isEmpty()) {
			// for the new monsters, set the teamSlotNum to 0
			final int size = protoNewMap.size();
			final List<Long> userMonsterIdList = new ArrayList<Long>(protoNewMap.keySet());
			final List<Integer> teamSlotNumList = Collections.nCopies(size, 0);
			num = UpdateUtils.get()
			    .updateNullifyUserMonstersTeamSlotNum(userMonsterIdList, teamSlotNumList);
			LOG.info("updated user monster rows. numUpdated="
			    + num);
		}

		return true;
	}

	public void writeToUserCurrencyHistory( final User aUser, final Timestamp date,
	    final Map<String, Integer> currencyChange, final int previousOil,
	    final int previousGems, final Map<Long, UserEnhancementItemProto> protoDeleteMap,
	    final Map<Long, UserEnhancementItemProto> protoUpdateMap,
	    final Map<Long, UserEnhancementItemProto> protoNewMap )
	{

		final StringBuilder detailsSb = new StringBuilder();

		// maybe shouldn't keep track...oh well, more info hopefully is better
		// than none
		if ((null != protoDeleteMap)
		    && !protoDeleteMap.isEmpty()) {
			detailsSb.append("deleteUuids=");
			for (final UserEnhancementItemProto ueip : protoDeleteMap.values()) {
				final long id = ueip.getUserMonsterId();
				detailsSb.append(id);
				detailsSb.append(" ");
			}
		}
		if ((null != protoUpdateMap)
		    && !protoUpdateMap.isEmpty()) {
			detailsSb.append("updateUuids=");
			for (final UserEnhancementItemProto ueip : protoUpdateMap.values()) {
				final long id = ueip.getUserMonsterId();
				detailsSb.append(id);
				detailsSb.append(" ");
			}
		}
		if ((null != protoNewMap)
		    && !protoNewMap.isEmpty()) {
			detailsSb.append("newUuids=");
			for (final UserEnhancementItemProto ueip : protoNewMap.values()) {
				final long id = ueip.getUserMonsterId();
				detailsSb.append(id);
				detailsSb.append(" ");
			}
		}

		final String userUuid = aUser.getId();
		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();
		final String reason = ControllerConstants.UCHRFC__ENHANCING;
		final String oil = MiscMethods.oil;
		final String gems = MiscMethods.gems;

		previousCurrency.put(oil, previousOil);
		previousCurrency.put(gems, previousGems);
		currentCurrency.put(oil, aUser.getOil());
		currentCurrency.put(gems, aUser.getGems());

		reasonsForChanges.put(oil, reason);
		reasonsForChanges.put(gems, reason);
		detailsMap.put(oil, detailsSb.toString());
		detailsMap.put(gems, detailsSb.toString());

		MiscMethods.writeToUserCurrencyOneUser(userUuid, date, currencyChange,
		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
	}

}
