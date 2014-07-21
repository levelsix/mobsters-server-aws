//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.MonsterForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackResponseProto
//import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackResponseProto.PurchaseBoosterPackStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.PurchaseBoosterPackRequestEvent
//import com.lvl6.mobsters.events.response.PurchaseBoosterPackResponseEvent
//import com.lvl6.mobsters.info.BoosterItem
//import com.lvl6.mobsters.info.BoosterPack
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Collection
//import java.util.Date
//import java.util.HashMap
//import java.util.List
//import java.util.Map
//import java.util.Random
//import javax.annotation.Resource
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//import org.springframework.util.StringUtils
//
//@Component
//@DependsOn('gameServer')
//class PurchaseBoosterPackController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(PurchaseBoosterPackController))
//	public static var BOOSTER_PURCHASES_MAX_SIZE = 50
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Resource(name='goodEquipsRecievedFromBoosterPacks')
//	protected var IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks
//
//	new()
//	{
//		numAllocatedThreads = 4
//	}
//
//	override createRequestEvent()
//	{
//		new PurchaseBoosterPackRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_PURCHASE_BOOSTER_PACK_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as PurchaseBoosterPackRequestEvent)).
//			purchaseBoosterPackRequestProto
//		val senderProto = reqProto.sender
//		val boosterPackId = reqProto.boosterPackUuid
//		val now = new Date(reqProto.clientTime)
//		val nowTimestamp = new Timestamp(now.time)
//		val resBuilder = PurchaseBoosterPackResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = PurchaseBoosterPackStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val userUuid = senderProto.userUuid
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
//			var previousGems = 0
//			val aPack = BoosterPackRetrieveUtils::getBoosterPackForBoosterPackId(boosterPackId)
//			val gemPrice = aPack.gemPrice
//			val boosterItemUuidsToBoosterItems = BoosterItemRetrieveUtils::
//				getBoosterItemUuidsToBoosterItemsForBoosterPackId(boosterPackId)
//			var itemsUserReceives = new ArrayList<BoosterItem>()
//			var gemReward = 0
//			var legit = checkLegitPurchase(resBuilder, user, userUuid, now, aPack, boosterPackId,
//				gemPrice, boosterItemUuidsToBoosterItems)
//			var successful = false
//			if (legit)
//			{
//				previousGems = user.gems
//				val numBoosterItemsUserWants = 1
//				LOG.info('determining the booster items the user receives.')
//				itemsUserReceives = determineBoosterItemsUserReceives(numBoosterItemsUserWants,
//					boosterItemUuidsToBoosterItems)
//				legit = checkIfMonstersExist(itemsUserReceives)
//			}
//			if (legit)
//			{
//				gemReward = determineGemReward(itemsUserReceives)
//				successful = writeChangesToDB(resBuilder, user, boosterPackId, itemsUserReceives,
//					gemPrice, now, gemReward)
//			}
//			if (successful)
//			{
//				if ((null !== itemsUserReceives) && !itemsUserReceives.empty)
//				{
//					val bi = itemsUserReceives.get(0)
//					val bip = CreateInfoProtoUtils::createBoosterItemProto(bi)
//					resBuilder.prize = bip
//				}
//			}
//			val resProto = resBuilder.build
//			val resEvent = new PurchaseBoosterPackResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.purchaseBoosterPackResponseProto = resProto
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in PurchaseBoosterPackController.processRequestEvent',
//					e)
//			}
//			if (successful)
//			{
//				val resEventUpdate = MiscMethods::
//					createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null)
//				resEventUpdate.tag = event.tag
//				LOG.info('Writing event: ' + resEventUpdate)
//				try
//				{
//					eventWriter.writeEvent(resEventUpdate)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in PurchaseBoosterPackController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(user, boosterPackId, nowTimestamp, gemPrice,
//					previousGems, itemsUserReceives, gemReward)
//				writeToBoosterPackPurchaseHistory(userUuid, boosterPackId, itemsUserReceives,
//					resBuilder.updatedOrNewList, nowTimestamp)
//				sendBoosterPurchaseMessage(user, aPack, itemsUserReceives)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in PurchaseBoosterPackController processEvent', e)
//			try
//			{
//				resBuilder.status = PurchaseBoosterPackStatus.FAIL_OTHER
//				val resEvent = new PurchaseBoosterPackResponseEvent(senderProto.userUuid)
//				resEvent.tag = event.tag
//				resEvent.purchaseBoosterPackResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in PurchaseBoosterPackController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in SellUserMonsterController processEvent', e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def sendBoosterPurchaseMessage(User user, BoosterPack aPack,
//		List<BoosterItem> itemsUserReceives)
//	{
//	}
//
//	private def checkLegitPurchase(Builder resBuilder, User aUser, String userUuid, Date now,
//		BoosterPack aPack, int boosterPackId, int gemPrice,
//		Map<Integer, BoosterItem> idsToBoosterItems)
//	{
//		if ((null === aUser) || (null === aPack) || (null === idsToBoosterItems) ||
//			idsToBoosterItems.empty)
//		{
//			resBuilder.status = PurchaseBoosterPackStatus.FAIL_OTHER
//			LOG.error(
//				'no user for id: ' + userUuid + ', or no BoosterPack for id: ' + boosterPackId +
//					', or no booster items for BoosterPack id. items=' + idsToBoosterItems)
//			return false;
//		}
//		val userGems = aUser.gems
//		if (userGems < gemPrice)
//		{
//			resBuilder.status = PurchaseBoosterPackStatus.FAIL_INSUFFICIENT_GEMS
//			return false;
//		}
//		resBuilder.status = PurchaseBoosterPackStatus.SUCCESS
//		true
//	}
//
//	private def determineBoosterItemsUserReceives(int amountUserWantsToPurchase,
//		Map<Integer, BoosterItem> boosterItemUuidsToBoosterItemsForPackId)
//	{
//		val itemsUserReceives = new ArrayList<BoosterItem>()
//		val items = boosterItemUuidsToBoosterItemsForPackId.values
//		val itemsList = new ArrayList<BoosterItem>(items)
//		val sumOfProbabilities = sumProbabilities(boosterItemUuidsToBoosterItemsForPackId.values)
//		for (purchaseN : 0 ..< amountUserWantsToPurchase)
//		{
//			val bi = selectBoosterItem(itemsList, sumOfProbabilities)
//			if (null === bi)
//			{
//				continue;
//			}
//			itemsUserReceives.add(bi)
//		}
//		itemsUserReceives
//	}
//
//	private def sumProbabilities(Collection<BoosterItem> boosterItems)
//	{
//		var sumOfProbabilities = 0.0f
//		for (bi : boosterItems)
//		{
//			sumOfProbabilities += bi.chanceToAppear
//		}
//		sumOfProbabilities
//	}
//
//	private def selectBoosterItem(List<BoosterItem> itemsList, float sumOfProbabilities)
//	{
//		val rand = new Random()
//		var unnormalizedProbabilitySoFar = 0f
//		val randFloat = rand.nextFloat
//		LOG.info(
//			'selecting booster item. sumOfProbabilities=' + sumOfProbabilities + '	 randFloat=' +
//				randFloat)
//		val size = itemsList.size
//		for (i : 0 ..< size)
//		{
//			val item = itemsList.get(i)
//			unnormalizedProbabilitySoFar += item.chanceToAppear
//			val normalizedProbabilitySoFar = unnormalizedProbabilitySoFar / sumOfProbabilities
//			LOG.info(
//				'boosterItem=' + item + '	 normalizedProbabilitySoFar=' +
//					normalizedProbabilitySoFar)
//			if (randFloat < normalizedProbabilitySoFar)
//			{
//				return item;
//			}
//		}
//		LOG.error('maybe no boosterItems exist. boosterItems=' + itemsList)
//		null
//	}
//
//	private def checkIfMonstersExist(List<BoosterItem> itemsUserReceives)
//	{
//		var monstersExist = true
//		val monsterUuidsToMonsters = MonsterRetrieveUtils::monsterUuidsToMonsters
//		for (bi : itemsUserReceives)
//		{
//			val monsterId = bi.monsterId
//			if (0 === monsterId)
//			{
//				continue;
//			}
//			else if (!monsterUuidsToMonsters.containsKey(monsterId))
//			{
//				LOG.error('This booster item contains nonexistent monsterId. item=' + bi)
//				monstersExist = false
//			}
//		}
//		monstersExist
//	}
//
//	private def determineGemReward(List<BoosterItem> boosterItems)
//	{
//		var gemReward = 0
//		for (bi : boosterItems)
//		{
//			gemReward += bi.gemReward
//		}
//		gemReward
//	}
//
//	private def writeChangesToDB(Builder resBuilder, User user, int bPackId,
//		List<BoosterItem> itemsUserReceives, int gemPrice, Date now, int gemReward)
//	{
//		val userUuid = user.id
//		var currencyChange = -1 * gemPrice
//		currencyChange += gemReward
//		if (!user.updateRelativeGemsNaive(currencyChange))
//		{
//			LOG.error(
//				"could not change user's money. gemPrice=" + gemPrice + '	 gemReward=' +
//					gemReward + '	 change=' + currencyChange)
//			return false;
//		}
//		LOG.info(
//			'SPENT MONEY ON BOOSTER PACK: ' + bPackId + '	 gemPrice=' + gemPrice +
//				'	 gemReward=' + gemReward + '	 itemsUserReceives=' + itemsUserReceives)
//		val monsterIdToNumPieces = new HashMap<Integer, Integer>()
//		val completeUserMonsters = new ArrayList<MonsterForUser>()
//		val mfusop = createUpdateUserMonsterArguments(userUuid, bPackId, itemsUserReceives,
//			monsterIdToNumPieces, completeUserMonsters, now)
//		LOG.info('!!!!!!!!!mfusop=' + mfusop)
//		if (!completeUserMonsters.empty)
//		{
//			val monsterForUserUuids = InsertUtils::get.
//				insertIntoMonsterForUserReturnUuids(userUuid, completeUserMonsters, mfusop, now)
//			val newOrUpdated = createFullUserMonsterProtos(monsterForUserUuids,
//				completeUserMonsters)
//			LOG.info(
//				'YIIIIPEEEEE!. BOUGHT COMPLETE MONSTER(S)! monster(s)= newOrUpdated' +
//					newOrUpdated + '	 bpackId=' + bPackId)
//			resBuilder.addAllUpdatedOrNew(newOrUpdated)
//		}
//		if (!monsterIdToNumPieces.empty)
//		{
//			val newOrUpdated = MonsterStuffUtils::updateUserMonsters(userUuid,
//				monsterIdToNumPieces, mfusop, now)
//			LOG.info(
//				'YIIIIPEEEEE!. BOUGHT INCOMPLETE MONSTER(S)! monster(s)= newOrUpdated' +
//					newOrUpdated + '	 bpackId=' + bPackId)
//			resBuilder.addAllUpdatedOrNew(newOrUpdated)
//		}
//		true
//	}
//
//	private def createUpdateUserMonsterArguments(String userUuid, int boosterPackId,
//		List<BoosterItem> boosterItems, Map<Integer, Integer> monsterUuidsToNumPieces,
//		List<MonsterForUser> completeUserMonsters, Date now)
//	{
//		val sb = new StringBuilder()
//		sb.append(ControllerConstants::MFUSOP__BOOSTER_PACK)
//		sb.append(' ')
//		sb.append(boosterPackId)
//		sb.append(' boosterItemUuids ')
//		val boosterItemUuids = new ArrayList<Integer>()
//		for (item : boosterItems)
//		{
//			val id = item.id
//			val monsterId = item.monsterId
//			val numPieces = item.numPieces
//			if (monsterId <= 0)
//			{
//				continue;
//			}
//			if (item.complete)
//			{
//				val isComplete = true
//				val monzter = MonsterRetrieveUtils::getMonsterForMonsterId(monsterId)
//				val newUserMonster = MonsterStuffUtils::createNewUserMonster(userUuid, numPieces,
//					monzter, now, isComplete)
//				completeUserMonsters.add(newUserMonster)
//			}
//			else
//			{
//				monsterUuidsToNumPieces.put(monsterId, numPieces)
//			}
//			boosterItemUuids.add(id)
//		}
//		if (!boosterItemUuids.empty)
//		{
//			val boosterItemUuidsStr = StringUtils::csvList(boosterItemUuids)
//			sb.append(boosterItemUuidsStr)
//		}
//		sb.toString
//	}
//
//	private def createFullUserMonsterProtos(List<Long> userMonsterUuids,
//		List<MonsterForUser> mfuList)
//	{
//		val protos = new ArrayList<FullUserMonsterProto>()
//		for (i : 0 ..< userMonsterUuids.size)
//		{
//			val mfuId = userMonsterUuids.get(i)
//			val mfu = mfuList.get(i)
//			mfu.id = mfuId
//			val fump = CreateInfoProtoUtils::createFullUserMonsterProtoFromUserMonster(mfu)
//			protos.add(fump)
//		}
//		protos
//	}
//
//	private def writeToUserCurrencyHistory(User aUser, int packId, Timestamp date,
//		int gemPrice, int previousGems, List<BoosterItem> items, int gemReward)
//	{
//		val userUuid = aUser.id
//		val itemUuids = new ArrayList<Integer>()
//		for (item : items)
//		{
//			val id = item.id
//			itemUuids.add(id)
//		}
//		val detailSb = new StringBuilder()
//		if ((null !== items) && !items.empty)
//		{
//			detailSb.append(' bItemUuids=')
//			val itemUuidsCsv = StringUtils::csvList(itemUuids)
//			detailSb.append(itemUuidsCsv)
//		}
//		if (gemReward > 0)
//		{
//			detailSb.append(' gemPrice=')
//			detailSb.append(gemPrice)
//			detailSb.append(' gemReward=')
//			detailSb.append(gemReward)
//		}
//		val gems = MiscMethods::gems
//		val reasonForChange = ControllerConstants.UCHRFC__PURHCASED_BOOSTER_PACK
//		val money = new HashMap<String, Integer>()
//		val previousCurrencies = new HashMap<String, Integer>()
//		val currentCurrencies = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val details = new HashMap<String, String>()
//		val change = (-1 * gemPrice) + gemReward
//		money.put(gems, change)
//		previousCurrencies.put(gems, previousGems)
//		currentCurrencies.put(gems, aUser.gems)
//		reasonsForChanges.put(gems, reasonForChange)
//		details.put(gems, detailSb.toString)
//		LOG.info('DETAILS=' + detailSb.toString)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, date, money, previousCurrencies,
//			currentCurrencies, reasonsForChanges, details)
//	}
//
//	private def writeToBoosterPackPurchaseHistory(String userUuid, int boosterPackId,
//		List<BoosterItem> itemsUserReceives, List<FullUserMonsterProto> fumpList,
//		Timestamp timeOfPurchase)
//	{
//		if (itemsUserReceives.empty)
//		{
//			return;
//		}
//		val bi = itemsUserReceives.get(0)
//		val userMonsterUuids = MonsterStuffUtils::getUserMonsterUuids(fumpList)
//		val num = InsertUtils::get.
//			insertIntoBoosterPackPurchaseHistory(userUuid, boosterPackId, timeOfPurchase, bi,
//				userMonsterUuids)
//		LOG.info(
//			'wrote to booster pack history!!!! 	 numInserted=' + num + '	 boosterItem=' +
//				itemsUserReceives)
//	}
//
//	def getGoodEquipsRecievedFromBoosterPacks()
//	{
//		goodEquipsRecievedFromBoosterPacks
//	}
//
//	def setGoodEquipsRecievedFromBoosterPacks(
//		IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks)
//	{
//		this.goodEquipsRecievedFromBoosterPacks = goodEquipsRecievedFromBoosterPacks
//	}
//}
