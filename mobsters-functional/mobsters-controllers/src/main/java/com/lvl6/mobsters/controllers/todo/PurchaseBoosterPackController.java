//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.MonsterForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackRequestProto;
//import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackResponseProto;
//import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackResponseProto.PurchaseBoosterPackStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.PurchaseBoosterPackRequestEvent;
//import com.lvl6.mobsters.events.response.PurchaseBoosterPackResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.info.BoosterItem;
//import com.lvl6.mobsters.info.BoosterPack;
//import com.lvl6.mobsters.info.Monster;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventBoosterPackProto.BoosterItemProto;
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class PurchaseBoosterPackController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(PurchaseBoosterPackController.class);
//
//	public static int BOOSTER_PURCHASES_MAX_SIZE = 50;
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Resource(name = "goodEquipsRecievedFromBoosterPacks")
//	protected IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks;
//
//	public PurchaseBoosterPackController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new PurchaseBoosterPackRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_PURCHASE_BOOSTER_PACK_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final PurchaseBoosterPackRequestProto reqProto =
//		    ((PurchaseBoosterPackRequestEvent) event).getPurchaseBoosterPackRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final int boosterPackId = reqProto.getBoosterPackUuid();
//		final Date now = new Date(reqProto.getClientTime());
//		final Timestamp nowTimestamp = new Timestamp(now.getTime());
//
//		// values to send to client
//		final PurchaseBoosterPackResponseProto.Builder resBuilder =
//		    PurchaseBoosterPackResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(PurchaseBoosterPackStatus.FAIL_OTHER);
//
//		svcTxManager.beginTransaction();
//		try {
//			final String userUuid = senderProto.getUserUuid();
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			int previousGems = 0;
//			final BoosterPack aPack =
//			    BoosterPackRetrieveUtils.getBoosterPackForBoosterPackId(boosterPackId);
//			final int gemPrice = aPack.getGemPrice();
//			final Map<Integer, BoosterItem> boosterItemUuidsToBoosterItems =
//			    BoosterItemRetrieveUtils.getBoosterItemUuidsToBoosterItemsForBoosterPackId(boosterPackId);
//
//			List<BoosterItem> itemsUserReceives = new ArrayList<BoosterItem>();
//			int gemReward = 0;
//
//			boolean legit =
//			    checkLegitPurchase(resBuilder, user, userUuid, now, aPack, boosterPackId,
//			        gemPrice, boosterItemUuidsToBoosterItems);
//
//			boolean successful = false;
//			if (legit) {
//				previousGems = user.getGems();
//
//				final int numBoosterItemsUserWants = 1;
//				LOG.info("determining the booster items the user receives.");
//				itemsUserReceives =
//				    determineBoosterItemsUserReceives(numBoosterItemsUserWants,
//				        boosterItemUuidsToBoosterItems);
//
//				legit = checkIfMonstersExist(itemsUserReceives);
//			}
//
//			if (legit) {
//				gemReward = determineGemReward(itemsUserReceives);
//				// set the FullUserMonsterProtos (in resBuilder) to send to the
//				// client
//				successful =
//				    writeChangesToDB(resBuilder, user, boosterPackId, itemsUserReceives,
//				        gemPrice, now, gemReward);
//			}
//
//			if (successful) {
//				// assume user only purchases 1 item. NEED TO LET CLIENT KNOW
//				// THE PRIZE
//				if ((null != itemsUserReceives)
//				    && !itemsUserReceives.isEmpty()) {
//					final BoosterItem bi = itemsUserReceives.get(0);
//					final BoosterItemProto bip =
//					    CreateInfoProtoUtils.createBoosterItemProto(bi);
//					resBuilder.setPrize(bip);
//				}
//			}
//
//			final PurchaseBoosterPackResponseProto resProto = resBuilder.build();
//			final PurchaseBoosterPackResponseEvent resEvent =
//			    new PurchaseBoosterPackResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setPurchaseBoosterPackResponseProto(resProto);
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in PurchaseBoosterPackController.processRequestEvent", e);
//			}
//
//			if (successful) {
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
//					    "fatal exception in PurchaseBoosterPackController.processRequestEvent",
//					    e);
//				}
//
//				writeToUserCurrencyHistory(user, boosterPackId, nowTimestamp, gemPrice,
//				    previousGems, itemsUserReceives, gemReward);
//
//				// just assume user can only buy one booster pack at a time
//				writeToBoosterPackPurchaseHistory(userUuid, boosterPackId, itemsUserReceives,
//				    resBuilder.getUpdatedOrNewList(), nowTimestamp);
//				sendBoosterPurchaseMessage(user, aPack, itemsUserReceives);
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in PurchaseBoosterPackController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(PurchaseBoosterPackStatus.FAIL_OTHER);
//				final PurchaseBoosterPackResponseEvent resEvent =
//				    new PurchaseBoosterPackResponseEvent(senderProto.getUserUuid());
//				resEvent.setTag(event.getTag());
//				resEvent.setPurchaseBoosterPackResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in PurchaseBoosterPackController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in SellUserMonsterController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private void sendBoosterPurchaseMessage( final User user, final BoosterPack aPack,
//	    final List<BoosterItem> itemsUserReceives )
//	{
//		// Map<Integer, Monster> equipMap =
//		// MonsterRetrieveUtils.getMonsterUuidsToMonster();
//		// Date d = new Date();
//		// for (BoosterItem bi : itemsUserReceives) {
//		// Monster eq = equipMap.get(bi.getEquipId());
//		// if (eq.getRarity().compareTo(Rarity.SUPERRARE) >= 0) {
//		// RareBoosterPurchaseProto r =
//		// CreateInfoProtoUtils.createRareBoosterPurchaseProto(aPack, user, eq,
//		// d);
//		//
//		// goodEquipsRecievedFromBoosterPacks.add(0, r);
//		// //remove older messages
//		// try {
//		// while(goodEquipsRecievedFromBoosterPacks.size() >
//		// BOOSTER_PURCHASES_MAX_SIZE) {
//		// goodEquipsRecievedFromBoosterPacks.remove(BOOSTER_PURCHASES_MAX_SIZE);
//		// }
//		// } catch(Exception e) {
//		// LOG.error("Error adding rare booster purchase.", e);
//		// }
//		//
//		// ReceivedRareBoosterPurchaseResponseProto p =
//		// ReceivedRareBoosterPurchaseResponseProto.newBuilder().setRareBoosterPurchase(r).build();
//		// ReceivedRareBoosterPurchaseResponseEvent e = new
//		// ReceivedRareBoosterPurchaseResponseEvent(user.getId());
//		// e.setReceivedRareBoosterPurchaseResponseProto(p);
//		// eventWriter.processGlobalChatResponseEvent(e);
//		// }
//		// }
//	}
//
//	private boolean checkLegitPurchase( final Builder resBuilder, final User aUser,
//	    final String userUuid, final Date now, final BoosterPack aPack,
//	    final int boosterPackId, final int gemPrice,
//	    final Map<Integer, BoosterItem> idsToBoosterItems )
//	{
//
//		if ((null == aUser)
//		    || (null == aPack)
//		    || (null == idsToBoosterItems)
//		    || idsToBoosterItems.isEmpty()) {
//			resBuilder.setStatus(PurchaseBoosterPackStatus.FAIL_OTHER);
//			LOG.error("no user for id: "
//			    + userUuid
//			    + ", or no BoosterPack for id: "
//			    + boosterPackId
//			    + ", or no booster items for BoosterPack id. items="
//			    + idsToBoosterItems);
//			return false;
//		}
//
//		final int userGems = aUser.getGems();
//		// check if user can afford to buy however many more user wants to buy
//		if (userGems < gemPrice) {
//			resBuilder.setStatus(PurchaseBoosterPackStatus.FAIL_INSUFFICIENT_GEMS);
//			return false; // resBuilder status set in called function
//		}
//
//		resBuilder.setStatus(PurchaseBoosterPackStatus.SUCCESS);
//		return true;
//	}
//
//	// private int getNumEquipsPurchasedToday(String userUuid, int
//	// boosterPackId,
//	// DateTime startOfDayInLA) {
//	// //get the time at the start of the day in UTC
//	// DateTimeZone utcTZ = DateTimeZone.UTC;
//	// DateTime startOfDayInLAInUtc = startOfDayInLA.withZone(utcTZ);
//	// Timestamp startTime = new
//	// Timestamp(startOfDayInLAInUtc.toDate().getTime());
//	//
//	// int numPurchased = UserBoosterPackRetrieveUtils
//	// .getNumPacksPurchasedAfterDateForUserAndPackId(userUuid, boosterPackId,
//	// startTime);
//	//
//	// return numPurchased;
//	// }
//
//	// no arguments are modified
//	private List<BoosterItem> determineBoosterItemsUserReceives(
//	    final int amountUserWantsToPurchase,
//	    final Map<Integer, BoosterItem> boosterItemUuidsToBoosterItemsForPackId )
//	{
//		// return value
//		final List<BoosterItem> itemsUserReceives = new ArrayList<BoosterItem>();
//
//		final Collection<BoosterItem> items = boosterItemUuidsToBoosterItemsForPackId.values();
//		final List<BoosterItem> itemsList = new ArrayList<BoosterItem>(items);
//		final float sumOfProbabilities =
//		    sumProbabilities(boosterItemUuidsToBoosterItemsForPackId.values());
//
//		// selecting items at random with replacement
//		for (int purchaseN = 0; purchaseN < amountUserWantsToPurchase; purchaseN++) {
//			final BoosterItem bi = selectBoosterItem(itemsList, sumOfProbabilities);
//			if (null == bi) {
//				continue;
//			}
//			itemsUserReceives.add(bi);
//		}
//
//		return itemsUserReceives;
//	}
//
//	private float sumProbabilities( final Collection<BoosterItem> boosterItems )
//	{
//		float sumOfProbabilities = 0.0f;
//		for (final BoosterItem bi : boosterItems) {
//			sumOfProbabilities += bi.getChanceToAppear();
//		}
//		return sumOfProbabilities;
//	}
//
//	private BoosterItem selectBoosterItem( final List<BoosterItem> itemsList,
//	    final float sumOfProbabilities )
//	{
//		final Random rand = new Random();
//		float unnormalizedProbabilitySoFar = 0f;
//		final float randFloat = rand.nextFloat();
//
//		LOG.info("selecting booster item. sumOfProbabilities="
//		    + sumOfProbabilities
//		    + "\t randFloat="
//		    + randFloat);
//
//		final int size = itemsList.size();
//		// for each item, normalize before seeing if it is selected
//		for (int i = 0; i < size; i++) {
//			final BoosterItem item = itemsList.get(i);
//
//			// normalize probability
//			unnormalizedProbabilitySoFar += item.getChanceToAppear();
//			final float normalizedProbabilitySoFar = unnormalizedProbabilitySoFar
//			    / sumOfProbabilities;
//
//			LOG.info("boosterItem="
//			    + item
//			    + "\t normalizedProbabilitySoFar="
//			    + normalizedProbabilitySoFar);
//
//			if (randFloat < normalizedProbabilitySoFar) {
//				// we have a winner! current boosterItem is what the user gets
//				return item;
//			}
//		}
//
//		LOG.error("maybe no boosterItems exist. boosterItems="
//		    + itemsList);
//		return null;
//	}
//
//	// purpose of this method is to discover if the booster items that contain
//	// monsters as rewards, if the monster ids are valid
//	private boolean checkIfMonstersExist( final List<BoosterItem> itemsUserReceives )
//	{
//		boolean monstersExist = true;
//
//		final Map<Integer, Monster> monsterUuidsToMonsters =
//		    MonsterRetrieveUtils.getMonsterUuidsToMonsters();
//		for (final BoosterItem bi : itemsUserReceives) {
//			final int monsterId = bi.getMonsterId();
//
//			if (0 == monsterId) {
//				// this booster item does not contain a monster reward
//				continue;
//			} else if (!monsterUuidsToMonsters.containsKey(monsterId)) {
//				LOG.error("This booster item contains nonexistent monsterId. item="
//				    + bi);
//				monstersExist = false;
//			}
//		}
//		return monstersExist;
//	}
//
//	private int determineGemReward( final List<BoosterItem> boosterItems )
//	{
//		int gemReward = 0;
//		for (final BoosterItem bi : boosterItems) {
//			gemReward += bi.getGemReward();
//		}
//
//		return gemReward;
//	}
//
//	private boolean writeChangesToDB( final Builder resBuilder, final User user,
//	    final int bPackId, final List<BoosterItem> itemsUserReceives, final int gemPrice,
//	    final Date now, final int gemReward )
//	{
//
//		// update user, user_monsters
//		final String userUuid = user.getId();
//		int currencyChange = -1
//		    * gemPrice; // should be negative
//		currencyChange += gemReward;
//
//		// update user's money
//		if (!user.updateRelativeGemsNaive(currencyChange)) {
//			LOG.error("could not change user's money. gemPrice="
//			    + gemPrice
//			    + "\t gemReward="
//			    + gemReward
//			    + "\t change="
//			    + currencyChange);
//			return false;
//		}
//
//		LOG.info("SPENT MONEY ON BOOSTER PACK: "
//		    + bPackId
//		    + "\t gemPrice="
//		    + gemPrice
//		    + "\t gemReward="
//		    + gemReward
//		    + "\t itemsUserReceives="
//		    + itemsUserReceives);
//
//		final Map<Integer, Integer> monsterIdToNumPieces = new HashMap<Integer, Integer>();
//		final List<MonsterForUser> completeUserMonsters = new ArrayList<MonsterForUser>();
//		// sop = source of pieces
//		final String mfusop =
//		    createUpdateUserMonsterArguments(userUuid, bPackId, itemsUserReceives,
//		        monsterIdToNumPieces, completeUserMonsters, now);
//
//		LOG.info("!!!!!!!!!mfusop="
//		    + mfusop);
//
//		// this is if the user bought a complete monster, STORE TO DB THE NEW
//		// MONSTERS
//		if (!completeUserMonsters.isEmpty()) {
//			final List<Long> monsterForUserUuids =
//			    InsertUtils.get()
//			        .insertIntoMonsterForUserReturnUuids(userUuid, completeUserMonsters,
//			            mfusop, now);
//			final List<FullUserMonsterProto> newOrUpdated =
//			    createFullUserMonsterProtos(monsterForUserUuids, completeUserMonsters);
//
//			LOG.info("YIIIIPEEEEE!. BOUGHT COMPLETE MONSTER(S)! monster(s)= newOrUpdated"
//			    + newOrUpdated
//			    + "\t bpackId="
//			    + bPackId);
//			// set the builder that will be sent to the client
//			resBuilder.addAllUpdatedOrNew(newOrUpdated);
//		}
//
//		// this is if the user did not buy a complete monster, UPDATE DB
//		if (!monsterIdToNumPieces.isEmpty()) {
//			// assume things just work while updating user monsters
//			final List<FullUserMonsterProto> newOrUpdated =
//			    MonsterStuffUtils.updateUserMonsters(userUuid, monsterIdToNumPieces, mfusop,
//			        now);
//
//			LOG.info("YIIIIPEEEEE!. BOUGHT INCOMPLETE MONSTER(S)! monster(s)= newOrUpdated"
//			    + newOrUpdated
//			    + "\t bpackId="
//			    + bPackId);
//			// set the builder that will be sent to the client
//			resBuilder.addAllUpdatedOrNew(newOrUpdated);
//		}
//
//		// if (monsterIdToNumPieces.isEmpty() && completeUserMonsters.isEmpty()
//		// &&
//		// gemReward <= 0) {
//		// LOG.warn("user didn't get any monsters or gems...boosterItemsUserReceives="
//		// + itemsUserReceives);
//		// resBuilder.setStatus(PurchaseBoosterPackStatus.FAIL_OTHER);
//		// return false;
//		// }
//
//		return true;
//	}
//
//	// monsterUuidsToNumPieces or completeUserMonsters will be populated
//	private String createUpdateUserMonsterArguments( final String userUuid,
//	    final int boosterPackId, final List<BoosterItem> boosterItems,
//	    final Map<Integer, Integer> monsterUuidsToNumPieces,
//	    final List<MonsterForUser> completeUserMonsters, final Date now )
//	{
//		final StringBuilder sb = new StringBuilder();
//		sb.append(ControllerConstants.MFUSOP__BOOSTER_PACK);
//		sb.append(" ");
//		sb.append(boosterPackId);
//		sb.append(" boosterItemUuids ");
//
//		final List<Integer> boosterItemUuids = new ArrayList<Integer>();
//		for (final BoosterItem item : boosterItems) {
//			final Integer id = item.getId();
//			final Integer monsterId = item.getMonsterId();
//			final Integer numPieces = item.getNumPieces();
//
//			// only keep track of the booster item ids that are a monster reward
//			if (monsterId <= 0) {
//				continue;
//			}
//			if (item.isComplete()) {
//				// create a "complete" user monster
//				final boolean isComplete = true;
//				final Monster monzter = MonsterRetrieveUtils.getMonsterForMonsterId(monsterId);
//				final MonsterForUser newUserMonster =
//				    MonsterStuffUtils.createNewUserMonster(userUuid, numPieces, monzter, now,
//				        isComplete);
//
//				// return this monster in the argument list
//				// completeUserMonsters, so caller
//				// can use it
//				completeUserMonsters.add(newUserMonster);
//
//			} else {
//				monsterUuidsToNumPieces.put(monsterId, numPieces);
//			}
//			boosterItemUuids.add(id);
//		}
//		if (!boosterItemUuids.isEmpty()) {
//			final String boosterItemUuidsStr = StringUtils.csvList(boosterItemUuids);
//			sb.append(boosterItemUuidsStr);
//		}
//
//		return sb.toString();
//	}
//
//	private List<FullUserMonsterProto> createFullUserMonsterProtos(
//	    final List<Long> userMonsterUuids, final List<MonsterForUser> mfuList )
//	{
//		final List<FullUserMonsterProto> protos = new ArrayList<FullUserMonsterProto>();
//
//		for (int i = 0; i < userMonsterUuids.size(); i++) {
//			final long mfuId = userMonsterUuids.get(i);
//			final MonsterForUser mfu = mfuList.get(i);
//			mfu.setId(mfuId);
//			final FullUserMonsterProto fump =
//			    CreateInfoProtoUtils.createFullUserMonsterProtoFromUserMonster(mfu);
//			protos.add(fump);
//		}
//
//		return protos;
//	}
//
//	private void writeToUserCurrencyHistory( final User aUser, final int packId,
//	    final Timestamp date, final int gemPrice, final int previousGems,
//	    final List<BoosterItem> items, final int gemReward )
//	{
//		final String userUuid = aUser.getId();
//		final List<Integer> itemUuids = new ArrayList<Integer>();
//		for (final BoosterItem item : items) {
//			final int id = item.getId();
//			itemUuids.add(id);
//		}
//
//		final StringBuilder detailSb = new StringBuilder();
//		if ((null != items)
//		    && !items.isEmpty()) {
//			detailSb.append(" bItemUuids=");
//			final String itemUuidsCsv = StringUtils.csvList(itemUuids);
//			detailSb.append(itemUuidsCsv);
//		}
//		if (gemReward > 0) {
//			detailSb.append(" gemPrice=");
//			detailSb.append(gemPrice);
//			detailSb.append(" gemReward=");
//			detailSb.append(gemReward);
//		}
//		final String gems = MiscMethods.gems;
//		final String reasonForChange = ControllerConstants.UCHRFC__PURHCASED_BOOSTER_PACK;
//
//		final Map<String, Integer> money = new HashMap<String, Integer>();
//		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> details = new HashMap<String, String>();
//
//		final int change = (-1 * gemPrice)
//		    + gemReward;
//		money.put(gems, change);
//		previousCurrencies.put(gems, previousGems);
//		currentCurrencies.put(gems, aUser.getGems());
//		reasonsForChanges.put(gems, reasonForChange);
//		details.put(gems, detailSb.toString());
//
//		LOG.info("DETAILS="
//		    + detailSb.toString());
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, date, money, previousCurrencies,
//		    currentCurrencies, reasonsForChanges, details);
//	}
//
//	private void writeToBoosterPackPurchaseHistory( final String userUuid,
//	    final int boosterPackId, final List<BoosterItem> itemsUserReceives,
//	    final List<FullUserMonsterProto> fumpList, final Timestamp timeOfPurchase )
//	{
//		// just assuming there is one Booster Item
//		if (itemsUserReceives.isEmpty()) {
//			return;
//		}
//		final BoosterItem bi = itemsUserReceives.get(0);
//
//		final List<Long> userMonsterUuids = MonsterStuffUtils.getUserMonsterUuids(fumpList);
//
//		final int num =
//		    InsertUtils.get()
//		        .insertIntoBoosterPackPurchaseHistory(userUuid, boosterPackId, timeOfPurchase,
//		            bi, userMonsterUuids);
//
//		LOG.info("wrote to booster pack history!!!! \t numInserted="
//		    + num
//		    + "\t boosterItem="
//		    + itemsUserReceives);
//	}
//
//	public IList<RareBoosterPurchaseProto> getGoodEquipsRecievedFromBoosterPacks()
//	{
//		return goodEquipsRecievedFromBoosterPacks;
//	}
//
//	public void setGoodEquipsRecievedFromBoosterPacks(
//	    final IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks )
//	{
//		this.goodEquipsRecievedFromBoosterPacks = goodEquipsRecievedFromBoosterPacks;
//	}
//
//}
