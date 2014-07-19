//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.PvpBattleForUser
//import com.lvl6.mobsters.dynamo.PvpLeagueForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleResponseProto
//import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleResponseProto.EndPvpBattleStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.EndPvpBattleRequestEvent
//import com.lvl6.mobsters.events.response.EndPvpBattleResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import com.lvl6.mobsters.services.common.TimeUtils
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Date
//import java.util.HashMap
//import java.util.List
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class EndPvpBattleController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(EndPvpBattleController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var HazelcastPvpUtil hazelcastPvpUtil
//	@Autowired
//	protected var TimeUtils timeUtils
//	@Autowired
//	protected var PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil
//
//	new()
//	{
//		numAllocatedThreads = 7
//	}
//
//	override createRequestEvent()
//	{
//		new EndPvpBattleRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_END_PVP_BATTLE_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as EndPvpBattleRequestEvent)).endPvpBattleRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProtoMaxResources = reqProto.sender
//		val senderProto = senderProtoMaxResources.minUserProto
//		val attackerId = senderProto.userUuid
//		val defenderId = reqProto.defenderUuid
//		val attackerAttacked = reqProto.userAttacked
//		val attackerWon = reqProto.userWon
//		var oilStolen = reqProto.oilChange
//		var cashStolen = reqProto.cashChange
//		if (!attackerWon && (oilStolen !== 0))
//		{
//			LOG.error(
//				'client should set oilStolen to be 0 since attacker lost!' +
//					'	 client sent oilStolen=' + oilStolen)
//			oilStolen = 0
//		}
//		if (!attackerWon && (cashStolen !== 0))
//		{
//			LOG.error(
//				'client should set cashStolen to be 0 since attacker lost!' +
//					'	 client sent cashStolen=' + cashStolen)
//			cashStolen = 0
//		}
//		val attackerMaxOil = senderProtoMaxResources.maxOil
//		val attackerMaxCash = senderProtoMaxResources.maxCash
//		val curTime = new Timestamp(reqProto.clientTime)
//		val curDate = new Date(curTime.time)
//		val resBuilder = EndPvpBattleResponseProto::newBuilder
//		resBuilder.sender = senderProtoMaxResources
//		resBuilder.status = EndPvpBattleStatus.FAIL_OTHER
//		val resEvent = new EndPvpBattleResponseEvent(attackerId)
//		resEvent.tag = event.tag
//		val userUuids = new ArrayList<Integer>()
//		userUuids.add(attackerId)
//		userUuids.add(defenderId)
//		if (0 !== defenderId)
//		{
//			locker.lockPlayers(defenderId, attackerId, this.class.simpleName)
//			LOG.info('locked defender and attacker')
//		}
//		else
//		{
//			locker.lockPlayer(attackerId, this.class.simpleName)
//			LOG.info('locked attacker')
//		}
//		try
//		{
//			val users = RetrieveUtils::userRetrieveUtils.getUsersByUuids(userUuids)
//			val attacker = users.get(attackerId)
//			val defender = users.get(defenderId)
//			val pvpBattleInfo = PvpBattleForUserRetrieveUtils::
//				getPvpBattleForUserForAttacker(attackerId)
//			LOG.info('pvpBattleInfo=' + pvpBattleInfo)
//			val plfuMap = pvpLeagueForUserRetrieveUtil.getUserPvpLeagueForUsers(userUuids)
//			LOG.info('plfuMap=' + plfuMap)
//			var PvpLeagueForUser attackerPlfu = null
//			var PvpLeagueForUser defenderPlfu = null
//			if (plfuMap.containsKey(attackerId))
//			{
//				attackerPlfu = plfuMap.get(attackerId)
//			}
//			if (plfuMap.containsKey(defenderId))
//			{
//				defenderPlfu = plfuMap.get(defenderId)
//			}
//			val legit = checkLegit(resBuilder, attacker, defender, pvpBattleInfo, curDate)
//			val changeMap = new HashMap<Integer, Map<String, Integer>>()
//			val previousCurrencyMap = new HashMap<Integer, Map<String, Integer>>()
//			var successful = false
//			if (legit)
//			{
//				successful = writeChangesToDb(attacker, attackerId, attackerPlfu, defender,
//					defenderId, defenderPlfu, pvpBattleInfo, oilStolen, cashStolen, curTime,
//					curDate, attackerAttacked, attackerWon, attackerMaxOil, attackerMaxCash,
//					changeMap, previousCurrencyMap)
//			}
//			if (successful)
//			{
//				resBuilder.status = EndPvpBattleStatus.SUCCESS
//			}
//			resEvent.endPvpBattleResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in EndPvpBattleController.processRequestEvent', e)
//			}
//			if (successful)
//			{
//				val resEventDefender = new EndPvpBattleResponseEvent(defenderId)
//				resEvent.tag = 0
//				resEventDefender.endPvpBattleResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEventDefender)
//				try
//				{
//					eventWriter.writeEvent(resEventDefender)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in EndPvpBattleController.processRequestEvent', e)
//				}
//				val resEventUpdate = MiscMethods::
//					createUpdateClientUserResponseEventAndUpdateLeaderboard(attacker,
//						attackerPlfu)
//				resEventUpdate.tag = event.tag
//				LOG.info('Writing event: ' + resEventUpdate)
//				try
//				{
//					eventWriter.writeEvent(resEventUpdate)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in EndPvpBattleController.processRequestEvent', e)
//				}
//				if (attackerWon && (null !== defender))
//				{
//					val resEventUpdateDefender = MiscMethods::
//						createUpdateClientUserResponseEventAndUpdateLeaderboard(defender,
//							defenderPlfu)
//					resEventUpdate.tag = event.tag
//					LOG.info('Writing event: ' + resEventUpdateDefender)
//					try
//					{
//						eventWriter.writeEvent(resEventUpdateDefender)
//					}
//					catch (Throwable e)
//					{
//						LOG.error(
//							'fatal exception in EndPvpBattleController.processRequestEvent', e)
//					}
//				}
//				writeToUserCurrencyHistory(attackerId, attacker, defenderId, defender,
//					attackerWon, curTime, changeMap, previousCurrencyMap)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in EndPvpBattleController processEvent', e)
//			try
//			{
//				resEvent.endPvpBattleResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in EndPvpBattleController.processRequestEvent', e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in EndPvpBattleController processEvent', e)
//			}
//		}
//		finally
//		{
//			if (0 !== defenderId)
//			{
//				locker.unlockPlayers(defenderId, attackerId, this.class.simpleName)
//				LOG.info('unlocked defender and attacker')
//			}
//			else
//			{
//				locker.unlockPlayer(attackerId, this.class.simpleName)
//				LOG.info('unlocked attacker')
//			}
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, User attacker, User defender,
//		PvpBattleForUser pvpInfo, Date curDate)
//	{
//		if (null === pvpInfo)
//		{
//			LOG.error(
//				'unexpected error: no battle exists for the attacker. attacker=' + attacker +
//					'	 defender=' + defender)
//			return false;
//		}
//		val nowMillis = curDate.time
//		val battleEndTime = pvpInfo.battleStartTime.time +
//			ControllerConstants.PVP__MAX_BATTLE_DURATION_MILLIS
//		if (nowMillis > battleEndTime)
//		{
//			resBuilder.status = EndPvpBattleStatus.FAIL_BATTLE_TOOK_TOO_LONG
//			LOG.error(
//				'the client took too long to finish a battle. pvpInfo=' + pvpInfo + '	 now=' +
//					curDate)
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDb(User attacker, int attackerId, PvpLeagueForUser attackerPlfu,
//		User defender, int defenderId, PvpLeagueForUser defenderPlfu,
//		PvpBattleForUser pvpBattleInfo, int oilStolen, int cashStolen, Timestamp clientTime,
//		Date clientDate, boolean attackerAttacked, boolean attackerWon, int attackerMaxOil,
//		int attackerMaxCash, Map<Integer, Map<String, Integer>> changeMap,
//		Map<Integer, Map<String, Integer>> previousCurrencyMap)
//	{
//		val cancelled = !attackerAttacked
//		if (cancelled)
//		{
//			LOG.info('battle cancelled')
//			processCancellation(attacker, attackerId, attackerPlfu, defender, defenderId,
//				defenderPlfu, pvpBattleInfo, clientTime, attackerWon, cancelled)
//		}
//		else
//		{
//			val previousCash = attacker.cash
//			val previousOil = attacker.oil
//			val attackerEloChangeList = new ArrayList<Integer>()
//			val defenderEloChangeList = new ArrayList<Integer>()
//			getEloChanges(attacker, attackerPlfu, defender, defenderPlfu, attackerWon,
//				pvpBattleInfo, attackerEloChangeList, defenderEloChangeList)
//			val attackerEloChange = attackerEloChangeList.get(0)
//			val defenderEloChange = defenderEloChangeList.get(0)
//			val attackerCashChange = calculateMaxCashChange(attacker, attackerMaxCash,
//				cashStolen, attackerWon)
//			val attackerOilChange = calculateMaxOilChange(attacker, attackerMaxOil, oilStolen,
//				attackerWon)
//			val attackerPrevPlfu = new PvpLeagueForUser(attackerPlfu)
//			updateAttacker(attacker, attackerId, attackerPlfu, attackerCashChange,
//				attackerOilChange, attackerEloChange, attackerWon)
//			val defenderOilChangeList = new ArrayList<Integer>()
//			val defenderCashChangeList = new ArrayList<Integer>()
//			val displayToDefenderList = new ArrayList<Boolean>()
//			var PvpLeagueForUser defenderPrevPlfu = null
//			if (null !== defenderPlfu)
//			{
//				defenderPrevPlfu = new PvpLeagueForUser(defenderPlfu)
//			}
//			updateDefender(attacker, defender, defenderId, defenderPlfu, pvpBattleInfo,
//				defenderEloChange, cashStolen, oilStolen, clientDate, attackerWon,
//				defenderEloChangeList, defenderOilChangeList, defenderCashChangeList,
//				displayToDefenderList, changeMap, previousCurrencyMap)
//			writePvpBattleHistoryNotcancelled(attackerId, attackerPrevPlfu, attackerPlfu,
//				defenderId, defenderPrevPlfu, defenderPlfu, pvpBattleInfo, clientTime,
//				attackerWon, attackerCashChange, attackerOilChange, attackerEloChange,
//				defenderEloChange, defenderOilChangeList, defenderCashChangeList,
//				displayToDefenderList, cancelled)
//			val attackerChangeMap = new HashMap<String, Integer>()
//			attackerChangeMap.put(MiscMethods::cash, attackerCashChange)
//			attackerChangeMap.put(MiscMethods::oil, attackerOilChange)
//			changeMap.put(attackerId, attackerChangeMap)
//			val attackerPreviousCurrency = new HashMap<String, Integer>()
//			attackerPreviousCurrency.put(MiscMethods::cash, previousCash)
//			attackerPreviousCurrency.put(MiscMethods::oil, previousOil)
//			previousCurrencyMap.put(attackerId, attackerPreviousCurrency)
//		}
//		LOG.info('deleting from PvpBattleForUser')
//		val numDeleted = DeleteUtils::get.deletePvpBattleForUser(attackerId)
//		LOG.info('numDeleted (should be 1): ' + numDeleted)
//		true
//	}
//
//	private def processCancellation(User attacker, int attackerId,
//		PvpLeagueForUser attackerPlfu, User defender, int defenderId,
//		PvpLeagueForUser defenderPlfu, PvpBattleForUser pvpBattleInfo, Timestamp clientTime,
//		boolean attackerWon, boolean cancelled)
//	{
//		if ((null !== defender) && (defenderPlfu.inBattleShieldEndTime.time > clientTime.time))
//		{
//			defenderPlfu.inBattleShieldEndTime = defenderPlfu.shieldEndTime
//			val defenderOpu = new PvpUser(defenderPlfu)
//			hazelcastPvpUtil.replacePvpUser(defenderOpu, defenderId)
//			LOG.info('changed battleEndTime to shieldEndTime. defenderPvpUser=' + defenderOpu)
//		}
//		val attackerEloBefore = attackerPlfu.elo
//		val attackerPrevLeague = attackerPlfu.pvpLeagueId
//		val attackerPrevRank = attackerPlfu.rank
//		var defenderEloBefore = 0
//		var defenderPrevLeague = 0
//		var defenderPrevRank = 0
//		if (null !== defenderPlfu)
//		{
//			defenderEloBefore = defenderPlfu.elo
//			defenderPrevLeague = defenderPlfu.pvpLeagueId
//			defenderPrevRank = defenderPlfu.rank
//		}
//		LOG.info('writing to pvp history that user cancelled battle')
//		writePvpBattleHistory(attackerId, attackerEloBefore, defenderId, defenderEloBefore,
//			attackerPrevLeague, attackerPrevLeague, defenderPrevLeague, defenderPrevLeague,
//			attackerPrevRank, attackerPrevRank, defenderPrevRank, defenderPrevRank, clientTime,
//			pvpBattleInfo, 0, 0, 0, 0, 0, 0, attackerWon, cancelled, false, false)
//	}
//
//	private def calculateMaxCashChange(User user, int maxCash, int cashChange, boolean userWon)
//	{
//		if (null === user)
//		{
//			LOG.info('calculateMaxCashChange user is null! cashChange=0')
//			return 0;
//		}
//		val userCash = user.cash
//		val amountOverMax = calculateAmountOverMaxResource(user, userCash, maxCash,
//			MiscMethods::cash)
//		LOG.info('calculateMaxCashChange amount over max=' + amountOverMax)
//		if (userWon)
//		{
//			LOG.info('calculateMaxCashChange userWon!. user=' + user)
//			val curCash = Math::min(user.cash, maxCash)
//			LOG.info('calculateMaxCashChange curCash=' + curCash)
//			val maxCashUserCanGain = maxCash - curCash
//			LOG.info('calculateMaxCashChange  maxCashUserCanGain=' + maxCashUserCanGain)
//			val maxCashChange = Math::min(cashChange, maxCashUserCanGain)
//			LOG.info('calculateMaxCashChange maxCashChange=' + maxCashChange)
//			val actualCashChange = maxCashChange - amountOverMax
//			LOG.info('calculateMaxCashChange  actualCashChange=' + actualCashChange)
//			return actualCashChange;
//		}
//		else
//		{
//			LOG.info('calculateMaxCashChange userLost!. user=' + user)
//			val maxCashUserCanLose = Math::min(user.cash, maxCash)
//			val maxCashChange = Math::min(cashChange, maxCashUserCanLose)
//			val actualCashChange = -1 * (amountOverMax + maxCashChange)
//			LOG.info('calculateMaxCashChange  actualCashChange=' + actualCashChange)
//			return actualCashChange;
//		}
//	}
//
//	private def calculateMaxOilChange(User user, int maxOil, int oilChange, boolean userWon)
//	{
//		if (null === user)
//		{
//			LOG.info('calculateMaxOilChange user is null! oilChange=0')
//			return 0;
//		}
//		val userOil = user.oil
//		val amountOverMax = calculateAmountOverMaxResource(user, userOil, maxOil,
//			MiscMethods::oil)
//		LOG.info('calculateMaxOilChange amount over max=' + amountOverMax)
//		if (userWon)
//		{
//			LOG.info('calculateMaxOilChange userWon!. user=' + user)
//			val curOil = Math::min(user.oil, maxOil)
//			LOG.info('calculateAmountOverMaxOil curOil=' + curOil)
//			val maxOilUserCanGain = maxOil - curOil
//			LOG.info('calculateAmountOverMaxOil  maxOilUserCanGain=' + maxOilUserCanGain)
//			val maxOilChange = Math::min(oilChange, maxOilUserCanGain)
//			LOG.info('calculateAmountOverMaxOil maxOilChange=' + maxOilChange)
//			val actualOilChange = maxOilChange - amountOverMax
//			LOG.info('calculateAmountOverMaxOil  actualOilChange=' + actualOilChange)
//			return actualOilChange;
//		}
//		else
//		{
//			LOG.info('calculateAmountOverMaxOil userLost!. user=' + user)
//			val maxOilUserCanLose = Math::min(user.oil, maxOil)
//			val maxOilChange = Math::min(oilChange, maxOilUserCanLose)
//			val actualOilChange = -1 * (amountOverMax + maxOilChange)
//			LOG.info('calculateAmountOverMaxOil  actualOilChange=' + actualOilChange)
//			return actualOilChange;
//		}
//	}
//
//	private def calculateAmountOverMaxResource(User u, int userResource, int maxResource,
//		String resource)
//	{
//		LOG.info('calculateAmountOverMaxResource resource=' + resource)
//		var resourceLoss = 0
//		if (userResource > maxResource)
//		{
//			LOG.info(
//				'wtf!!!!! user has more than max cash! user=' + u +
//					'	 cutting him down to maxResource=' + maxResource)
//			resourceLoss = userResource - maxResource
//		}
//		resourceLoss
//	}
//
//	private def getEloChanges(User attacker, PvpLeagueForUser attackerPlfu, User defender,
//		PvpLeagueForUser defenderPlfu, boolean attackerWon, PvpBattleForUser pvpBattleInfo,
//		List<Integer> attackerEloChangeList, List<Integer> defenderEloChangeList)
//	{
//		var attackerEloChange = 0
//		var defenderEloChange = 0
//		if (attackerWon)
//		{
//			LOG.info('getEloChanges attacker won.')
//			attackerEloChange = pvpBattleInfo.attackerWinEloChange
//			defenderEloChange = pvpBattleInfo.defenderLoseEloChange
//			if ((null !== defender) && (pvpBattleInfo.defenderId > 0))
//			{
//				LOG.info(
//					'getEloChanges attacker fought real player. battleInfo=' + pvpBattleInfo)
//				defenderEloChange = capPlayerMinimumElo(defenderPlfu, defenderEloChange)
//			}
//			else
//			{
//				LOG.info(
//					'getEloChanges attacker fought fake player. battleInfo=' + pvpBattleInfo)
//			}
//			LOG.info('getEloChanges attackerEloChange=' + attackerEloChange)
//			LOG.info('getEloChanges defenderEloChange=' + defenderEloChange)
//		}
//		else
//		{
//			LOG.info('getEloChanges attacker lost.')
//			attackerEloChange = pvpBattleInfo.attackerLoseEloChange
//			defenderEloChange = pvpBattleInfo.defenderWinEloChange
//			attackerEloChange = capPlayerMinimumElo(attackerPlfu, attackerEloChange)
//		}
//		attackerEloChangeList.add(attackerEloChange)
//		defenderEloChangeList.add(defenderEloChange)
//	}
//
//	private def capPlayerMinimumElo(PvpLeagueForUser playerPlfu, int playerEloChange)
//	{
//		val playerElo = playerPlfu.elo
//		LOG.info('capPlayerMinimumElo plfu=' + playerPlfu + '	 eloChange' + playerEloChange)
//		if ((playerElo + playerEloChange) < 0)
//		{
//			LOG.info(
//				'capPlayerMinimumElo player loses more elo than has atm. playerElo=' + playerElo +
//					'	 playerEloChange=' + playerEloChange)
//			playerEloChange = -1 * playerElo
//		}
//		LOG.info('capPlayerMinimumElo updated playerEloChange=' + playerEloChange)
//		playerEloChange
//	}
//
//	private def updateAttacker(User attacker, int attackerId, PvpLeagueForUser attackerPlfu,
//		int attackerCashChange, int attackerOilChange, int attackerEloChange,
//		boolean attackerWon)
//	{
//		if ((0 !== attackerOilChange) || (0 !== attackerEloChange))
//		{
//			LOG.info('attacker before currency update: ' + attacker)
//			val numUpdated = attacker.
//				updateRelativeCashAndOilAndGems(attackerCashChange, attackerOilChange, 0)
//			LOG.info('attacker after currency update: ' + attacker)
//			LOG.info("num updated when changing attacker's currency=" + numUpdated)
//		}
//		LOG.info('attacker PvpLeagueForUser before battle outcome:' + attackerPlfu)
//		val prevElo = attackerPlfu.elo
//		val attackerPrevLeague = attackerPlfu.pvpLeagueId
//		var attacksWon = attackerPlfu.attacksWon
//		var attacksLost = attackerPlfu.attacksLost
//		var attacksWonDelta = 0
//		val defensesWonDelta = 0
//		var attacksLostDelta = 0
//		val defensesLostDelta = 0
//		if (attackerWon)
//		{
//			attacksWonDelta = 1
//			attacksWon += attacksWonDelta
//		}
//		else
//		{
//			attacksLostDelta = 1
//			attacksLost += attacksLostDelta
//		}
//		val curElo = prevElo + attackerEloChange
//		val attackerCurLeague = PvpLeagueRetrieveUtils::getLeagueIdForElo(curElo, false,
//			attackerPrevLeague)
//		val attackerCurRank = PvpLeagueRetrieveUtils::getRankForElo(curElo, attackerCurLeague)
//		val numUpdated = UpdateUtils::get.updatePvpLeagueForUser(attackerId, attackerCurLeague,
//			attackerCurRank, attackerEloChange, null, null, attacksWonDelta, defensesWonDelta,
//			attacksLostDelta, defensesLostDelta)
//		LOG.info("num updated when changing attacker's elo=" + numUpdated)
//		attackerPlfu.elo = curElo
//		attackerPlfu.pvpLeagueId = attackerCurLeague
//		attackerPlfu.rank = attackerCurRank
//		attackerPlfu.attacksWon = attacksWon
//		attackerPlfu.attacksLost = attacksLost
//		val attackerPu = new PvpUser(attackerPlfu)
//		hazelcastPvpUtil.replacePvpUser(attackerPu, attackerId)
//		LOG.info('attacker PvpLeagueForUser after battle outcome:' + attackerPlfu)
//	}
//
//	private def updateDefender(User attacker, User defender, int defenderId,
//		PvpLeagueForUser defenderPlfu, PvpBattleForUser pvpBattleInfo, int defenderEloChange,
//		int oilStolen, int cashStolen, Date clientDate, boolean attackerWon,
//		List<Integer> defenderEloChangeList, List<Integer> defenderOilChangeList,
//		List<Integer> defenderCashChangeList, List<Boolean> displayToDefenderList,
//		Map<Integer, Map<String, Integer>> changeMap,
//		Map<Integer, Map<String, Integer>> previousCurrencyMap)
//	{
//		if (null === defender)
//		{
//			LOG.info('attacker attacked fake defender. attacker=' + attacker)
//			defenderEloChangeList.clear
//			defenderEloChangeList.add(0)
//			defenderOilChangeList.add(0)
//			defenderCashChangeList.add(0)
//			displayToDefenderList.add(false)
//			return;
//		}
//		val previousCash = defender.cash
//		val previousOil = defender.oil
//		val defenderWon = !attackerWon
//		var defenderCashChange = calculateMaxCashChange(defender, defender.cash, cashStolen,
//			defenderWon)
//		var defenderOilChange = calculateMaxOilChange(defender, defender.oil, oilStolen,
//			defenderWon)
//		var displayToDefender = true
//		val shieldEndTime = defenderPlfu.shieldEndTime
//		if (timeUtils.isFirstEarlierThanSecond(clientDate, shieldEndTime))
//		{
//			LOG.warn(
//				'some how attacker attacked a defender with a shield!! pvpBattleInfo=' +
//					pvpBattleInfo + '	 attacker=' + attacker + '	 defender=' + defender)
//			defenderCashChange = 0
//			defenderOilChange = 0
//			defenderEloChange = 0
//			displayToDefender = false
//		}
//		else
//		{
//			LOG.info('penalizing/rewarding for losing/winning. defenderWon=' + defenderWon)
//			updateUnshieldedDefender(attacker, defenderId, defender, defenderPlfu, shieldEndTime,
//				pvpBattleInfo, clientDate, attackerWon, defenderEloChange, defenderCashChange,
//				defenderOilChange)
//		}
//		defenderEloChangeList.add(defenderEloChange)
//		defenderCashChangeList.add(defenderCashChange)
//		defenderOilChangeList.add(defenderOilChange)
//		displayToDefenderList.add(displayToDefender)
//		val defenderChangeMap = new HashMap<String, Integer>()
//		defenderChangeMap.put(MiscMethods::cash, defenderCashChange)
//		defenderChangeMap.put(MiscMethods::oil, defenderOilChange)
//		changeMap.put(defenderId, defenderChangeMap)
//		val defenderPreviousCurrency = new HashMap<String, Integer>()
//		defenderPreviousCurrency.put(MiscMethods::cash, previousCash)
//		defenderPreviousCurrency.put(MiscMethods::oil, previousOil)
//		previousCurrencyMap.put(defenderId, defenderPreviousCurrency)
//	}
//
//	private def updateUnshieldedDefender(User attacker, int defenderId, User defender,
//		PvpLeagueForUser defenderPlfu, Date defenderShieldEndTime,
//		PvpBattleForUser pvpBattleInfo, Date clientDate, boolean attackerWon,
//		int defenderEloChange, int defenderCashChange, int defenderOilChange)
//	{
//		LOG.info(
//			'attacker attacked unshielded defender. attacker=' + attacker + '	 defender=' +
//				defender + '	 battleInfo=' + pvpBattleInfo)
//		val prevElo = defenderPlfu.elo
//		val prevPvpLeague = defenderPlfu.pvpLeagueId
//		var defensesLost = defenderPlfu.defensesLost
//		var defensesWon = defenderPlfu.defensesWon
//		val attacksWonDelta = 0
//		var defensesWonDelta = 0
//		val attacksLostDelta = 0
//		var defensesLostDelta = 0
//		if (attackerWon)
//		{
//			LOG.info('updateUnshieldedDefender  defender before currency update:' + defender)
//			val numUpdated = defender.
//				updateRelativeCashAndOilAndGems(defenderCashChange, defenderOilChange, 0)
//			LOG.info(
//				"updateUnshieldedDefender num updated when changing defender's" + ' currency=' +
//					numUpdated)
//			LOG.info('updateUnshieldedDefender  defender after currency update:' + defender)
//			val hoursAddend = ControllerConstants.PVP__LOST_BATTLE_SHIELD_DURATION_HOURS
//			defenderShieldEndTime = timeUtils.createDateAddHours(clientDate, hoursAddend)
//			defensesLostDelta = 1
//			defensesLost += defensesLostDelta
//		}
//		else
//		{
//			LOG.info('updateUnshieldedDefender defender won!')
//			defensesWonDelta = 1
//			defensesWon += defensesWonDelta
//		}
//		LOG.info(
//			'updateUnshieldedDefender defender PvpLeagueForUser before battle outcome:' +
//				defenderPlfu)
//		val inBattleShieldEndTime = defenderShieldEndTime
//		val curElo = prevElo + defenderEloChange
//		val curPvpLeague = PvpLeagueRetrieveUtils::getLeagueIdForElo(curElo, false,
//			prevPvpLeague)
//		val curRank = PvpLeagueRetrieveUtils::getRankForElo(curElo, curPvpLeague)
//		val shieldEndTimestamp = new Timestamp(defenderShieldEndTime.time)
//		val inBattleTimestamp = new Timestamp(inBattleShieldEndTime.time)
//		val numUpdated = UpdateUtils::get.updatePvpLeagueForUser(defenderId, curPvpLeague,
//			curRank, defenderEloChange, shieldEndTimestamp, inBattleTimestamp, attacksWonDelta,
//			defensesWonDelta, attacksLostDelta, defensesLostDelta)
//		LOG.info("num updated when changing defender's elo=" + numUpdated)
//		defenderPlfu.shieldEndTime = defenderShieldEndTime
//		defenderPlfu.inBattleShieldEndTime = inBattleShieldEndTime
//		defenderPlfu.elo = curElo
//		defenderPlfu.pvpLeagueId = curPvpLeague
//		defenderPlfu.rank = curRank
//		defenderPlfu.defensesLost = defensesLost
//		defenderPlfu.defensesWon = defensesWon
//		val defenderPu = new PvpUser(defenderPlfu)
//		hazelcastPvpUtil.replacePvpUser(defenderPu, defenderId)
//		LOG.info('defender PvpLeagueForUser after battle outcome:' + defenderPlfu)
//	}
//
//	private def writePvpBattleHistory(int attackerId, int attackerEloBefore, int defenderId,
//		int defenderEloBefore, int attackerPrevLeague, int attackerCurLeague,
//		int defenderPrevLeague, int defenderCurLeague, int attackerPrevRank,
//		int attackerCurRank, int defenderPrevRank, int defenderCurRank, Timestamp endTime,
//		PvpBattleForUser pvpBattleInfo, int attackerEloChange, int defenderEloChange,
//		int attackerOilChange, int defenderOilChange, int attackerCashChange,
//		int defenderCashChange, boolean attackerWon, boolean cancelled, boolean gotRevenge,
//		boolean displayToDefender)
//	{
//		val startDate = pvpBattleInfo.battleStartTime
//		val battleStartTime = new Timestamp(startDate.time)
//		val numInserted = InsertUtils::get.insertIntoPvpBattleHistory(attackerId, defenderId,
//			endTime, battleStartTime, attackerEloChange, attackerEloBefore, defenderEloChange,
//			defenderEloBefore, attackerPrevLeague, attackerCurLeague, defenderPrevLeague,
//			defenderCurLeague, attackerPrevRank, attackerCurRank, defenderPrevRank,
//			defenderCurRank, attackerOilChange, defenderOilChange, attackerCashChange,
//			defenderCashChange, attackerWon, cancelled, gotRevenge, displayToDefender)
//		LOG.info('num inserted into history=' + numInserted)
//	}
//
//	private def writePvpBattleHistoryNotcancelled(int attackerId,
//		PvpLeagueForUser attackerPrevPlfu, PvpLeagueForUser attackerPlfu, int defenderId,
//		PvpLeagueForUser defenderPrevPlfu, PvpLeagueForUser defenderPlfu,
//		PvpBattleForUser pvpBattleInfo, Timestamp clientTime, boolean attackerWon,
//		int attackerCashChange, int attackerOilChange, int attackerEloChange,
//		int defenderEloChange, List<Integer> defenderOilChangeList,
//		List<Integer> defenderCashChangeList, List<Boolean> displayToDefenderList,
//		boolean cancelled)
//	{
//		val attackerEloBefore = attackerPrevPlfu.elo
//		val attackerPrevLeague = attackerPrevPlfu.pvpLeagueId
//		val attackerPrevRank = attackerPrevPlfu.rank
//		val attackerCurLeague = attackerPlfu.pvpLeagueId
//		val attackerCurRank = attackerPlfu.rank
//		var defenderEloBefore = 0
//		var defenderPrevLeague = 0
//		var defenderPrevRank = 0
//		var defenderCurLeague = 0
//		var defenderCurRank = 0
//		if (null !== defenderPrevPlfu)
//		{
//			defenderEloBefore = defenderPrevPlfu.elo
//			defenderPrevLeague = defenderPrevPlfu.pvpLeagueId
//			defenderPrevRank = defenderPrevPlfu.rank
//			defenderCurLeague = defenderPlfu.pvpLeagueId
//			defenderCurRank = defenderPlfu.rank
//		}
//		val defenderOilChange = defenderOilChangeList.get(0)
//		val defenderCashChange = defenderCashChangeList.get(0)
//		val displayToDefender = displayToDefenderList.get(0)
//		LOG.info('writing to pvp history that user finished battle')
//		LOG.info(
//			'attackerEloChange=' + attackerEloChange + '	 defenderEloChange=' +
//				defenderEloChange + '	 attackerOilChange=' + attackerOilChange +
//				'	 defenderOilChange=' + defenderOilChange + '	 attackerCashChange=' +
//				attackerCashChange + '	 defenderCashChange=' + defenderCashChange)
//		writePvpBattleHistory(attackerId, attackerEloBefore, defenderId, defenderEloBefore,
//			attackerPrevLeague, attackerCurLeague, defenderPrevLeague, defenderCurLeague,
//			attackerPrevRank, attackerCurRank, defenderPrevRank, defenderCurRank, clientTime,
//			pvpBattleInfo, attackerEloChange, defenderEloChange, attackerOilChange,
//			defenderOilChange, attackerCashChange, defenderCashChange, attackerWon, cancelled,
//			false, displayToDefender)
//	}
//
//	private def writeToUserCurrencyHistory(int attackerId, User attacker, int defenderId,
//		User defender, boolean attackerWon, Timestamp curTime,
//		Map<Integer, Map<String, Integer>> changeMap,
//		Map<Integer, Map<String, Integer>> previousCurrencyMap)
//	{
//		val currentCurrencyMap = new HashMap<Integer, Map<String, Integer>>()
//		val changeReasonsMap = new HashMap<Integer, Map<String, String>>()
//		val detailsMap = new HashMap<Integer, Map<String, String>>()
//		val reasonForChange = ControllerConstants.UCHRFC__PVP_BATTLE
//		val oil = MiscMethods::oil
//		val cash = MiscMethods::cash
//		val reasonMap = new HashMap<String, String>()
//		reasonMap.put(cash, reasonForChange)
//		reasonMap.put(oil, reasonForChange)
//		changeReasonsMap.put(attackerId, reasonMap)
//		changeReasonsMap.put(defenderId, reasonMap)
//		val attackerCash = attacker.cash
//		val attackerOil = attacker.oil
//		val attackerCurrency = new HashMap<String, Integer>()
//		attackerCurrency.put(cash, attackerCash)
//		attackerCurrency.put(oil, attackerOil)
//		currentCurrencyMap.put(attackerId, attackerCurrency)
//		val attackerDetailsSb = new StringBuilder()
//		if (attackerWon)
//		{
//			attackerDetailsSb.append('beat ')
//		}
//		else
//		{
//			attackerDetailsSb.append('lost to ')
//		}
//		attackerDetailsSb.append(defenderId)
//		val attackerDetails = attackerDetailsSb.toString
//		val attackerDetailsMap = new HashMap<String, String>()
//		attackerDetailsMap.put(cash, attackerDetails)
//		attackerDetailsMap.put(oil, attackerDetails)
//		detailsMap.put(attackerId, attackerDetailsMap)
//		if (null !== defender)
//		{
//			val defenderCash = defender.cash
//			val defenderOil = defender.oil
//			val defenderCurrency = new HashMap<String, Integer>()
//			defenderCurrency.put(cash, defenderCash)
//			defenderCurrency.put(oil, defenderOil)
//			currentCurrencyMap.put(defenderId, defenderCurrency)
//			val defenderDetailsSb = new StringBuilder()
//			if (attackerWon)
//			{
//				defenderDetailsSb.append('lost to ')
//			}
//			else
//			{
//				defenderDetailsSb.append('beat ')
//			}
//			defenderDetailsSb.append(attackerId)
//			val defenderDetails = defenderDetailsSb.toString
//			val defenderDetailsMap = new HashMap<String, String>()
//			defenderDetailsMap.put(cash, defenderDetails)
//			defenderDetailsMap.put(oil, defenderDetails)
//			detailsMap.put(defenderId, defenderDetailsMap)
//		}
//		val userUuids = new ArrayList<Integer>()
//		userUuids.add(attackerId)
//		userUuids.add(defenderId)
//		MiscMethods::writeToUserCurrencyUsers(userUuids, curTime, changeMap, previousCurrencyMap,
//			currentCurrencyMap, changeReasonsMap, detailsMap)
//	}
//
//	def getHazelcastPvpUtil()
//	{
//		hazelcastPvpUtil
//	}
//
//	def setHazelcastPvpUtil(HazelcastPvpUtil hazelcastPvpUtil)
//	{
//		this.hazelcastPvpUtil = hazelcastPvpUtil
//	}
//
//	def getTimeUtils()
//	{
//		timeUtils
//	}
//
//	def setTimeUtils(TimeUtils timeUtils)
//	{
//		this.timeUtils = timeUtils
//	}
//
//	def getPvpLeagueForUserRetrieveUtil()
//	{
//		pvpLeagueForUserRetrieveUtil
//	}
//
//	def setPvpLeagueForUserRetrieveUtil(
//		PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil)
//	{
//		this.pvpLeagueForUserRetrieveUtil = pvpLeagueForUserRetrieveUtil
//	}
//}
