//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser
//import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser
//import com.lvl6.mobsters.dynamo.MonsterForUser
//import com.lvl6.mobsters.dynamo.MonsterHealingForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterResponseProto
//import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterResponseProto.EvolveMonsterStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.EvolveMonsterRequestEvent
//import com.lvl6.mobsters.events.response.EvolveMonsterResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.HashMap
//import java.util.HashSet
//import java.util.List
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class EvolveMonsterController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(EvolveMonsterController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 3
//	}
//
//	override createRequestEvent()
//	{
//		new EvolveMonsterRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_EVOLVE_MONSTER_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as EvolveMonsterRequestEvent)).evolveMonsterRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val uep = reqProto.evolution
//		val gemsSpent = reqProto.gemsSpent
//		val oilChange = reqProto.oilChange
//		var catalystUserMonsterId = 0
//		var evolvingUserMonsterUuids = new ArrayList<Long>()
//		var Timestamp clientTime = null
//		if ((null !== uep) && reqProto.evolution)
//		{
//			LOG.info('uep is not null')
//			catalystUserMonsterId = uep.catalystUserMonsterId
//			evolvingUserMonsterUuids = new ArrayList<Long>(uep.userMonsterUuidsList)
//			clientTime = new Timestamp(uep.startTime)
//		}
//		val resBuilder = EvolveMonsterResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = EvolveMonsterStatus.FAIL_OTHER
//		locker.lockPlayer(senderProto.userUuid, class.simpleName)
//		try
//		{
//			var previousOil = 0
//			var previousGems = 0
//			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
//			val alreadyEnhancing = MonsterEnhancingForUserRetrieveUtils::
//				getMonstersForUser(userUuid)
//			val alreadyHealing = MonsterHealingForUserRetrieveUtils::
//				getMonstersForUser(userUuid)
//			val alreadyEvolving = MonsterEvolvingForUserRetrieveUtils::
//				getCatalystUuidsToEvolutionsForUser(userUuid)
//			var existingUserMonsters = new HashMap<Long, MonsterForUser>()
//			if ((null !== uep) && reqProto.evolution)
//			{
//				val newUuids = new HashSet<Long>()
//				newUuids.add(catalystUserMonsterId)
//				newUuids.addAll(evolvingUserMonsterUuids)
//				existingUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
//					getSpecificOrAllUserMonstersForUser(userUuid, newUuids)
//				LOG.info('retrieved user monsters. existingUserMonsters=' + existingUserMonsters)
//			}
//			val legitMonster = checkLegit(resBuilder, aUser, userUuid, existingUserMonsters,
//				alreadyEnhancing, alreadyHealing, alreadyEvolving, catalystUserMonsterId,
//				evolvingUserMonsterUuids, gemsSpent, oilChange)
//			var successful = false
//			val money = new HashMap<String, Integer>()
//			if (legitMonster)
//			{
//				previousOil = aUser.oil
//				previousGems = aUser.gems
//				successful = writeChangesToDB(aUser, userUuid, gemsSpent, oilChange,
//					catalystUserMonsterId, evolvingUserMonsterUuids, clientTime, money)
//			}
//			if (successful)
//			{
//				resBuilder.status = EvolveMonsterStatus.SUCCESS
//			}
//			val resEvent = new EvolveMonsterResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.evolveMonsterResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in EvolveMonsterController.processRequestEvent', e)
//			}
//			if (successful)
//			{
//				val resEventUpdate = MiscMethods::
//					createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser, null)
//				resEventUpdate.tag = event.tag
//				LOG.info('Writing event: ' + resEventUpdate)
//				try
//				{
//					eventWriter.writeEvent(resEventUpdate)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in EvolveMonsterController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(aUser, clientTime, money, previousOil, previousGems,
//					catalystUserMonsterId, evolvingUserMonsterUuids)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in EnhanceMonster processEvent', e)
//		}
//		finally
//		{
//			locker.unlockPlayer(senderProto.userUuid, class.simpleName)
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, User u, String userUuid,
//		Map<Long, MonsterForUser> existingUserMonsters,
//		Map<Long, MonsterEnhancingForUser> alreadyEnhancing,
//		Map<Long, MonsterHealingForUser> alreadyHealing,
//		Map<Long, MonsterEvolvingForUser> alreadyEvolving, long catalystUserMonsterId,
//		List<Long> userMonsterUuids, int gemsSpent, int oilChange)
//	{
//		if (null === u)
//		{
//			LOG.error(
//				'unexpected error: user is null. user=' + u + '	 catalystUserMonsterId=' +
//					catalystUserMonsterId + '	 userMonsterUuids=' + userMonsterUuids)
//			return false;
//		}
//		if ((null === existingUserMonsters) || existingUserMonsters.empty ||
//			(3 !== existingUserMonsters.size))
//		{
//			LOG.error(
//				'user trying to user nonexistent monster in evolution. existing=' +
//					existingUserMonsters + '	 catalyst=' + catalystUserMonsterId + '	 others=' +
//					userMonsterUuids)
//			resBuilder.status = EvolveMonsterStatus.FAIL_NONEXISTENT_MONSTERS
//			return false;
//		}
//		if ((null !== alreadyEvolving) && !alreadyEvolving.empty)
//		{
//			LOG.error('user already evolving monsters. monsters=' + alreadyEvolving)
//			resBuilder.status = EvolveMonsterStatus.FAIL_MAX_NUM_EVOLUTIONS_REACHED
//			return false;
//		}
//		if (((null !== alreadyEnhancing) && !alreadyEnhancing.empty) ||
//			((null !== alreadyHealing) && !alreadyHealing.empty))
//		{
//			LOG.error(
//				'the monsters provided are in healing or enhancing. enhancing=' +
//					alreadyEnhancing + '	 healing=' + alreadyHealing + '	 catalyst=' +
//					catalystUserMonsterId + '	 others=' + userMonsterUuids)
//			return false;
//		}
//		if (!hasEnoughGems(resBuilder, u, gemsSpent, oilChange, catalystUserMonsterId,
//			userMonsterUuids))
//		{
//			return false;
//		}
//		if (!hasEnoughOil(resBuilder, u, gemsSpent, oilChange, catalystUserMonsterId,
//			userMonsterUuids))
//		{
//			return false;
//		}
//		if ((0 === gemsSpent) && (0 === oilChange))
//		{
//			LOG.error('gemsSpent=' + gemsSpent + '	 oilChange=' + oilChange + '	 Not evolving.')
//			return false;
//		}
//		true
//	}
//
//	private def hasEnoughGems(Builder resBuilder, User u, int gemsSpent, int oilChange,
//		long catalyst, List<Long> userMonsterUuids)
//	{
//		val userGems = u.gems
//		if (userGems < gemsSpent)
//		{
//			LOG.error(
//				'user error: user does not have enough gems. userGems=' + userGems +
//					'	 gemsSpent=' + gemsSpent + '	 oilChange=' + oilChange + '	 catalyst=' +
//					catalyst + '	 userMonsterUuids=' + userMonsterUuids)
//			resBuilder.status = EvolveMonsterStatus.FAIL_INSUFFICIENT_GEMS
//			return false;
//		}
//		true
//	}
//
//	private def hasEnoughOil(Builder resBuilder, User u, int gemsSpent, int oilChange,
//		long catalyst, List<Long> userMonsterUuids)
//	{
//		val userOil = u.oil
//		val cost = -1 * oilChange
//		if ((0 === gemsSpent) && (userOil < cost))
//		{
//			LOG.error(
//				'user error: user does not have enough cash. cost=' + cost + '	 oilChange=' +
//					oilChange + '	 catalyst=' + catalyst + '	 userMonsterUuids=' +
//					userMonsterUuids)
//			resBuilder.status = EvolveMonsterStatus.FAIL_INSUFFICIENT_RESOURCES
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDB(User user, int uId, int gemsSpent, int oilChange,
//		long catalystUserMonsterId, List<Long> userMonsterUuids, Timestamp clientTime,
//		Map<String, Integer> money)
//	{
//		val cashChange = 0
//		val gemChange = -1 * gemsSpent
//		val numChange = user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange)
//		if (1 !== numChange)
//		{
//			LOG.error(
//				'problem with updating user stats: gemChange=' + gemChange + ', cashChange=' +
//					oilChange + ', user is ' + user)
//			return false;
//		}
//		else
//		{
//			if (0 !== oilChange)
//			{
//				money.put(MiscMethods::oil, oilChange)
//			}
//			if (0 !== gemsSpent)
//			{
//				money.put(MiscMethods::gems, gemChange)
//			}
//		}
//		val numInserted = InsertUtils::get.
//			insertIntoMonsterEvolvingForUser(uId, catalystUserMonsterId, userMonsterUuids,
//				clientTime)
//		LOG.info('for monster_evolving table, numInserted=' + numInserted)
//		true
//	}
//
//	def writeToUserCurrencyHistory(User aUser, Timestamp date,
//		Map<String, Integer> moneyChange, int previousOil, int previousGems,
//		long catalystUserMonsterId, List<Long> userMonsterUuids)
//	{
//		if (moneyChange.empty)
//		{
//			return;
//		}
//		val oil = MiscMethods::oil
//		val gems = MiscMethods::gems
//		var reasonForChange = ControllerConstants.UCHRFC__EVOLVING
//		val detailSb = new StringBuilder()
//		detailSb.append('(catalystId, userMonsterId1, userMonsterId2) ')
//		if (moneyChange.containsKey(gems))
//		{
//			reasonForChange = ControllerConstants.UCHRFC__SPED_UP_EVOLUTION
//		}
//		detailSb.append('(')
//		detailSb.append(catalystUserMonsterId)
//		detailSb.append(',')
//		val one = userMonsterUuids.get(0)
//		detailSb.append(one)
//		val two = userMonsterUuids.get(1)
//		detailSb.append(two)
//		detailSb.append(')')
//		val userUuid = aUser.id
//		val previousCurrencyMap = new HashMap<String, Integer>()
//		val currentCurrencyMap = new HashMap<String, Integer>()
//		val changeReasonsMap = new HashMap<String, String>()
//		val detailsMap = new HashMap<String, String>()
//		previousCurrencyMap.put(oil, previousOil)
//		previousCurrencyMap.put(gems, previousGems)
//		currentCurrencyMap.put(oil, aUser.oil)
//		currentCurrencyMap.put(gems, aUser.gems)
//		changeReasonsMap.put(oil, reasonForChange)
//		changeReasonsMap.put(gems, reasonForChange)
//		detailsMap.put(oil, detailSb.toString)
//		detailsMap.put(gems, detailSb.toString)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, date, moneyChange, previousCurrencyMap,
//			currentCurrencyMap, changeReasonsMap, detailsMap)
//	}
//}
