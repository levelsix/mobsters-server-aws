//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.PvpLeagueForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleResponseProto
//import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleResponseProto.BeginPvpBattleStatus
//import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleResponseProto.Builder
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.BeginPvpBattleRequestEvent
//import com.lvl6.mobsters.events.response.BeginPvpBattleResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventPvpProto.PvpProto
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import com.lvl6.mobsters.services.common.TimeUtils
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Date
//import java.util.List
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class BeginPvpBattleController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(BeginPvpBattleController))
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
//		new BeginPvpBattleRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_BEGIN_PVP_BATTLE_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as BeginPvpBattleRequestEvent)).beginPvpBattleRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProto = reqProto.sender
//		val senderElo = reqProto.senderElo
//		val attackerId = senderProto.userUuid
//		val curTime = new Timestamp(reqProto.attackStartTime)
//		val curDate = new Date(curTime.time)
//		val enemyProto = reqProto.enemy
//		val enemyUserId = enemyProto.defender.minUserProto.userUuid
//		val exactingRevenge = reqProto.exactingRevenge
//		var Timestamp previousBattleEndTime = null
//		if (exactingRevenge)
//		{
//			previousBattleEndTime = new Timestamp(reqProto.previousBattleEndTime)
//		}
//		val resBuilder = BeginPvpBattleResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = BeginPvpBattleStatus.FAIL_OTHER
//		val resEvent = new BeginPvpBattleResponseEvent(attackerId)
//		resEvent.tag = event.tag
//		if (0 !== enemyUserId)
//		{
//			locker.lockPlayer(enemyUserId, this.class.simpleName)
//		}
//		try
//		{
//			val attacker = RetrieveUtils::userRetrieveUtils.getUserById(attackerId)
//			var PvpLeagueForUser enemyPlfu = null
//			if (0 !== enemyUserId)
//			{
//				enemyPlfu = pvpLeagueForUserRetrieveUtil.getUserPvpLeagueForId(enemyUserId)
//			}
//			val legit = checkLegit(resBuilder, enemyPlfu, enemyUserId, enemyProto, curDate)
//			var successful = false
//			if (legit)
//			{
//				val attackerEloChange = new ArrayList<Integer>()
//				val defenderEloChange = new ArrayList<Integer>()
//				calculateEloChange(senderElo, enemyProto, attackerEloChange, defenderEloChange)
//				successful = writeChangesToDb(attacker, attackerId, enemyUserId, enemyPlfu,
//					attackerEloChange, defenderEloChange, curTime, exactingRevenge,
//					previousBattleEndTime)
//			}
//			if (successful)
//			{
//				resBuilder.status = BeginPvpBattleStatus.SUCCESS
//			}
//			resEvent.beginPvpBattleResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in BeginPvpBattleController.processRequestEvent', e)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in BeginPvpBattleController processEvent', e)
//			try
//			{
//				resEvent.beginPvpBattleResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in BeginPvpBattleController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in BeginPvpBattleController processEvent', e)
//			}
//		}
//		finally
//		{
//			if (0 !== enemyUserId)
//			{
//				svcTxManager.commit
//			}
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, PvpLeagueForUser enemyPlfu, int enemyUserId,
//		PvpProto enemyProto, Date curDate)
//	{
//		if (0 === enemyUserId)
//		{
//			return true;
//		}
//		if (null === enemyPlfu)
//		{
//			LOG.error(
//				'unexpected error: enemy is null. enemyUserId=' + enemyUserId +
//					'	 enemyProto client sent=' + enemyProto)
//			return false;
//		}
//		val shieldEndTime = enemyPlfu.shieldEndTime
//		val inBattleEndTime = enemyPlfu.inBattleShieldEndTime
//		if ((shieldEndTime.time > curDate.time) || (inBattleEndTime.time > curDate.time))
//		{
//			resBuilder.status = BeginPvpBattleStatus.FAIL_ENEMY_UNAVAILABLE
//			LOG.warn(
//				'The user this client wants to attack has already been atttacked' +
//					' or is being attacked. pvpUser=' + enemyPlfu + '	 curDate' + curDate)
//			return false;
//		}
//		true
//	}
//
//	private def calculateEloChange(int attackerElo, PvpProto defenderProto,
//		List<Integer> attackerEloChange, List<Integer> defenderEloChange)
//	{
//		val attackerWinEloChange = attackerElo + 10
//		val defenderElo = 4
//		val defenderLoseEloChange = Math::min(0, defenderElo - 10)
//		val attackerLoseEloChange = Math::min(0, attackerElo - 10)
//		val defenderWinEloChange = defenderElo + 10
//		attackerEloChange.add(attackerWinEloChange)
//		attackerEloChange.add(attackerLoseEloChange)
//		defenderEloChange.add(defenderLoseEloChange)
//		defenderEloChange.add(defenderWinEloChange)
//	}
//
//	private def writeChangesToDb(User attacker, int attackerId, int enemyId,
//		PvpLeagueForUser enemy, List<Integer> attackerEloChange,
//		List<Integer> defenderEloChange, Timestamp clientTime, boolean exactingRevenge,
//		Timestamp previousBattleEndTime)
//	{
//		val attackerWinEloChange = attackerEloChange.get(0)
//		val defenderLoseEloChange = defenderEloChange.get(0)
//		val attackerLoseEloChange = attackerEloChange.get(1)
//		val defenderWinEloChange = defenderEloChange.get(1)
//		LOG.info('inserting into PvpBattleForUser')
//		val numInserted = InsertUtils::get.
//			insertUpdatePvpBattleForUser(attackerId, enemyId, attackerWinEloChange,
//				defenderLoseEloChange, attackerLoseEloChange, defenderWinEloChange, clientTime)
//		LOG.info('numInserted (should be 1): ' + numInserted)
//		if (0 !== enemyId)
//		{
//			val nowMillis = clientTime.time
//			val newInBattleEndTime = new Date(
//				nowMillis + ControllerConstants::PVP__MAX_BATTLE_DURATION_MILLIS)
//			enemy.inBattleShieldEndTime = newInBattleEndTime
//			val nuEnemyPu = new PvpUser(enemy)
//			hazelcastPvpUtil.replacePvpUser(nuEnemyPu, enemyId)
//			val nuInBattleEndTime = new Timestamp(newInBattleEndTime.time)
//			LOG.info('now=' + clientTime)
//			LOG.info('should be one hour later, battleEndTime=' + nuInBattleEndTime)
//			val numUpdated = UpdateUtils::get.
//				updatePvpLeagueForUserShields(enemyId, null, nuInBattleEndTime)
//			LOG.info('(defender shield) num updated=' + numUpdated)
//			exactRevenge(attacker, attackerId, enemyId, clientTime, previousBattleEndTime,
//				exactingRevenge)
//		}
//		true
//	}
//
//	private def exactRevenge(User attacker, int attackerId, int defenderId,
//		Timestamp clientTime, Timestamp prevBattleEndTime, boolean exactingRevenge)
//	{
//		if (!exactingRevenge)
//		{
//			LOG.info('not exacting revenge')
//			return;
//		}
//		if (null === prevBattleEndTime)
//		{
//			LOG.info('not exacting revenge, prevBattleEndTime is null')
//		}
//		LOG.info('exacting revenge')
//		val historyAttackerId = defenderId
//		val historyDefenderId = attackerId
//		var numUpdated = UpdateUtils::get.
//			updatePvpBattleHistoryExactRevenge(historyAttackerId, historyDefenderId,
//				prevBattleEndTime)
//		LOG.info('recorded that user exacted revenge. numUpdated (should be 1)=' + numUpdated)
//		val attackerPlfu = pvpLeagueForUserRetrieveUtil.getUserPvpLeagueForId(attackerId)
//		val curShieldEndTime = attackerPlfu.shieldEndTime
//		val nowDate = new Date(clientTime.time)
//		if (timeUtils.isFirstEarlierThanSecond(nowDate, curShieldEndTime))
//		{
//			LOG.info(
//				"user shield end time is now being reset since he's attacking with a shield")
//			LOG.info('1cur pvpuser=' + hazelcastPvpUtil.getPvpUser(attackerId))
//			val login = attacker.lastLogin
//			val loginTime = new Timestamp(login.time)
//			numUpdated = UpdateUtils::get.
//				updatePvpLeagueForUserShields(attackerId, null, loginTime)
//			LOG.info('(defender shield) num updated=' + numUpdated)
//			val attackerOpu = new PvpUser(attackerPlfu)
//			attackerOpu.shieldEndTime = login
//			attackerOpu.inBattleEndTime = login
//			hazelcastPvpUtil.replacePvpUser(attackerOpu, attackerId)
//			LOG.info('2cur pvpuser=' + hazelcastPvpUtil.getPvpUser(attackerId))
//			LOG.info('(should be same as 2cur pvpUser) 3cur pvpuser=' + attackerOpu)
//		}
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
