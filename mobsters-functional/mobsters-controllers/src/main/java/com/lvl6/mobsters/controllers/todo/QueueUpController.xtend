package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.PvpLeagueForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpResponseProto
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpResponseProto.QueueUpStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.QueueUpRequestEvent
import com.lvl6.mobsters.events.response.QueueUpResponseEvent
import com.lvl6.mobsters.info.MonsterForPvp
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventPvpProto.PvpProto
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.common.TimeUtils
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Random
import java.util.Set
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class QueueUpController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(QueueUpController))
	@Autowired
	protected var DataServiceTxManager svcTxManager
	@Autowired
	protected var HazelcastPvpUtil hazelcastPvpUtil
	@Autowired
	protected var MonsterForPvpRetrieveUtils monsterForPvpRetrieveUtils
	@Autowired
	protected var TimeUtils timeUtils
	@Autowired
	protected var PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil

	new()
	{
		numAllocatedThreads = 10
	}

	override createRequestEvent()
	{
		new QueueUpRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_QUEUE_UP_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as QueueUpRequestEvent)).queueUpRequestProto
		LOG.info('reqProto=' + reqProto)
		val attackerProto = reqProto.attacker
		val attackerId = attackerProto.userUuid
		val attackerElo = reqProto.attackerElo
		val seenUserUuids = reqProto.seenUserUuidsList
		val uniqSeenUserUuids = new HashSet<Integer>(seenUserUuids)
		uniqSeenUserUuids.add(attackerId)
		val clientDate = new Date(reqProto.clientTime)
		val clientTime = new Timestamp(clientDate.time)
		val resBuilder = QueueUpResponseProto::newBuilder
		resBuilder.attacker = attackerProto
		resBuilder.status = QueueUpStatus.FAIL_OTHER
		try
		{
			val attacker = RetrieveUtils::userRetrieveUtils.getUserById(attackerId)
			val plfu = pvpLeagueForUserRetrieveUtil.getUserPvpLeagueForId(attackerId)
			val legitQueueUp = checkLegitQueueUp(resBuilder, attacker, clientDate)
			var success = false
			val currencyChange = new HashMap<String, Integer>()
			if (legitQueueUp)
			{
				setProspectivePvpMatches(resBuilder, attacker, uniqSeenUserUuids, clientDate,
					attackerElo)
				success = writeChangesToDB(attackerId, attacker, clientTime, plfu,
					currencyChange)
			}
			if (success)
			{
				resBuilder.status = QueueUpStatus.SUCCESS
			}
			val resEvent = new QueueUpResponseEvent(attackerId)
			resEvent.tag = event.tag
			resEvent.queueUpResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in QueueUpController.processRequestEvent', e)
			}
			if (success)
			{
				val resEventUpdate = MiscMethods::
					createUpdateClientUserResponseEventAndUpdateLeaderboard(attacker, plfu)
				resEventUpdate.tag = event.tag
				LOG.info('Writing event: ' + resEventUpdate)
				try
				{
					eventWriter.writeEvent(resEventUpdate)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in QueueUpController.processRequestEvent', e)
				}
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in QueueUp processEvent', e)
			resBuilder.status = QueueUpStatus.FAIL_OTHER
			val resEvent = new QueueUpResponseEvent(attackerId)
			resEvent.tag = event.tag
			resEvent.queueUpResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in QueueUpController.processRequestEvent', e)
			}
		}
	}

	private def checkLegitQueueUp(Builder resBuilder, User u, Date clientDate)
	{
		if (null === u)
		{
			resBuilder.status = QueueUpStatus.FAIL_OTHER
			LOG.error('problem with QueueUp- attacker is null. user is ' + u)
			return false;
		}
		resBuilder.status = QueueUpStatus.SUCCESS
		true
	}

	private def setProspectivePvpMatches(Builder resBuilder, User attacker,
		Set<Integer> uniqSeenUserUuids, Date clientDate, int attackerElo)
	{
		val minElo = Math::max(0, attackerElo - ControllerConstants::PVP__ELO_RANGE_SUBTRAHEND)
		val maxElo = attackerElo + ControllerConstants.PVP__ELO_RANGE_ADDEND
		val queuedOpponentUuidsList = new ArrayList<Integer>()
		val userIdToPvpUser = new HashMap<Integer, PvpUser>()
		val queuedOpponents = getQueuedOpponents(attacker, attackerElo, minElo, maxElo,
			uniqSeenUserUuids, clientDate, queuedOpponentUuidsList, userIdToPvpUser)
		var numWanted = ControllerConstants.PVP__MAX_QUEUE_SIZE
		val pvpProtoList = new ArrayList<PvpProto>()
		if ((null === queuedOpponents) || (queuedOpponents.size < numWanted))
		{
			numWanted = numWanted - queuedOpponentUuidsList.size
			LOG.info('no valid users for attacker=' + attacker)
			LOG.info('generating fake users.')
			val fakeMonsters = monsterForPvpRetrieveUtils.retrievePvpMonsters(minElo, maxElo)
			val fakeUserMonsters = createFakeUserMonsters(fakeMonsters, numWanted)
			val pvpProtoListTemp = createPvpProtosFromFakeUser(fakeUserMonsters)
			pvpProtoList.addAll(pvpProtoListTemp)
		}
		if ((null !== queuedOpponents) && !queuedOpponents.empty)
		{
			LOG.info('there are people to attack!')
			LOG.info('queuedOpponentUuidsList=' + queuedOpponentUuidsList)
			LOG.info('queuedOpponents:' + queuedOpponents)
			val userIdToUserMonsters = selectMonstersForUsers(queuedOpponentUuidsList)
			val userIdToProspectiveCashReward = new HashMap<Integer, Integer>()
			val userIdToProspectiveOilReward = new HashMap<Integer, Integer>()
			calculateCashOilRewards(queuedOpponents, userIdToProspectiveCashReward,
				userIdToProspectiveOilReward)
			val pvpProtoListTemp = CreateInfoProtoUtils::createPvpProtos(queuedOpponents, null,
				userIdToPvpUser, userIdToUserMonsters, userIdToProspectiveCashReward,
				userIdToProspectiveOilReward)
			pvpProtoList.addAll(0, pvpProtoListTemp)
		}
		resBuilder.addAllDefenderInfoList(pvpProtoList)
		LOG.info('pvpProtoList=' + pvpProtoList)
	}

	private def getQueuedOpponents(User attacker, int attackerElo, int minElo, int maxElo,
		Set<Integer> seenUserUuids, Date clientDate, List<Integer> userIdList,
		Map<Integer, PvpUser> userIdToPvpUser)
	{
		val numNeeded = ControllerConstants.PVP__MAX_QUEUE_SIZE
		val prospectiveDefenders = hazelcastPvpUtil.retrievePvpUsers(minElo, maxElo, clientDate,
			numNeeded, seenUserUuids)
		val numDefenders = prospectiveDefenders.size
		LOG.info('users returned from hazelcast pvp util. users=' + prospectiveDefenders)
		selectUsers(numNeeded, numDefenders, prospectiveDefenders, userIdList, userIdToPvpUser)
		val selectedDefenders = new ArrayList<User>()
		if (!prospectiveDefenders.empty)
		{
			val selectedDefendersMap = RetrieveUtils::userRetrieveUtils.
				getUsersByUuids(userIdList)
			selectedDefenders.addAll(selectedDefendersMap.values)
		}
		LOG.info('the lucky people who get to be attacked! defenders=' + selectedDefenders)
		selectedDefenders
	}

	private def selectUsers(int numNeeded, int numDefenders, Set<PvpUser> prospectiveDefenders,
		List<Integer> userIdList, Map<Integer, PvpUser> userIdToPvpUser)
	{
		val rand = new Random()
		var numNeededSoFar = numNeeded
		var numDefendersLeft = numDefenders
		for (pvpUser : prospectiveDefenders)
		{
			LOG.info('pvp opponents, numNeeded=' + numNeededSoFar)
			LOG.info('pvp opponents, numAvailable=' + numDefendersLeft)
			val userUuid = Integer::valueOf(pvpUser.userUuid)
			if (userIdList.size >= ControllerConstants::PVP__MAX_QUEUE_SIZE)
			{
				LOG.info('reached queue length of ' + ControllerConstants::PVP__MAX_QUEUE_SIZE)
        break;
			}
			if (numNeededSoFar >= numDefendersLeft)
			{
				userIdList.add(userUuid)
				userIdToPvpUser.put(userUuid, pvpUser)
				numNeededSoFar -= 1
				numDefendersLeft -= 1
        continue;
			}
			val randFloat = rand.nextFloat
			val probabilityToBeSelected = numNeededSoFar / numDefendersLeft
			LOG.info('randFloat=' + randFloat)
			LOG.info('probabilityToBeSelected=' + probabilityToBeSelected)
			if (randFloat < probabilityToBeSelected)
			{
				userIdList.add(userUuid)
				userIdToPvpUser.put(userUuid, pvpUser)
				numNeededSoFar -= 1
			}
			numDefendersLeft -= 1
		}
	}

	private def selectMonstersForUsers(List<Integer> userIdList)
	{
		val userUuidsToUserMonsters = new HashMap<Integer, List<MonsterForUser>>()
		val userIdsToMfuUuidsToMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
			getCompleteMonstersForUser(userIdList)
		for (index : 0 ..< userIdList.size)
		{
			val defenderId = userIdList.get(index)
			val mfuUuidsToMonsters = userIdsToMfuUuidsToMonsters.get(defenderId)
			if ((null === mfuUuidsToMonsters) || mfuUuidsToMonsters.empty)
			{
				LOG.error(
					'WTF!!!!!!!! user has no monsters!!!!! userUuid=' + defenderId +
						'	 will move on to next guy.')
        continue;
			}
			val defenderMonsters = selectMonstersForUser(mfuUuidsToMonsters)
			userUuidsToUserMonsters.put(defenderId, defenderMonsters)
		}
		userUuidsToUserMonsters
	}

	private def selectMonstersForUser(Map<Long, MonsterForUser> mfuUuidsToMonsters)
	{
		var defenderMonsters = getEquippedMonsters(mfuUuidsToMonsters)
		if (defenderMonsters.size < 3)
		{
			getRandomMonsters(mfuUuidsToMonsters, defenderMonsters)
		}
		if (defenderMonsters.size > 3)
		{
			defenderMonsters = defenderMonsters.subList(0, 3)
		}
		defenderMonsters
	}

	private def getEquippedMonsters(Map<Long, MonsterForUser> userMonsters)
	{
		val equipped = new ArrayList<MonsterForUser>()
		for (mfu : userMonsters.values)
		{
			if (mfu.teamSlotNum <= 0)
			{
				continue;
			}
			equipped.add(mfu)
		}
		equipped
	}

	private def getRandomMonsters(Map<Long, MonsterForUser> possibleMonsters,
		List<MonsterForUser> defenderMonsters)
	{
		val possibleMonstersTemp = new HashMap<Long, MonsterForUser>(possibleMonsters)
		for (m : defenderMonsters)
		{
			val mfuId = m.id
			possibleMonstersTemp.remove(mfuId)
		}
		val amountLeftOver = possibleMonstersTemp.size
		var amountNeeded = 3 - defenderMonsters.size
		if (amountLeftOver < amountNeeded)
		{
			defenderMonsters.addAll(possibleMonstersTemp.values)
			return;
		}
		val mfuList = new ArrayList<MonsterForUser>(possibleMonstersTemp.values)
		val rand = new Random()
		for (i : 0 ..< mfuList.size)
		{
			val probToBeChosen = amountNeeded / (amountLeftOver - i)
			val randFloat = rand.nextFloat
			if (randFloat < probToBeChosen)
			{
				val mfu = mfuList.get(i)
				defenderMonsters.add(mfu)
				amountNeeded--
			}
			if (defenderMonsters.size >= 3)
			{
				break;
			}
		}
	}

	private def createFakeUserMonsters(Set<MonsterForPvp> fakeMonsters, int numWanted)
	{
		val fakeUserMonsters = new ArrayList<List<MonsterForPvp>>()
		var tempFakeUser = new ArrayList<MonsterForPvp>()
		for (mfp : fakeMonsters)
		{
			tempFakeUser.add(mfp)
			if (tempFakeUser.size >= 3)
			{
				fakeUserMonsters.add(tempFakeUser)
				tempFakeUser = new ArrayList<MonsterForPvp>()
			}
			if (fakeUserMonsters.size >= numWanted)
			{
				break;
			}
		}
		fakeUserMonsters
	}

	private def calculateCashOilRewards(List<User> queuedOpponents,
		Map<Integer, Integer> userIdToProspectiveCashReward,
		Map<Integer, Integer> userIdToProspectiveOilReward)
	{
		for (queuedOpponent : queuedOpponents)
		{
			val userUuid = queuedOpponent.id
			val cashReward = MiscMethods::calculateCashRewardFromPvpUser(queuedOpponent)
			val oilReward = MiscMethods::calculateOilRewardFromPvpUser(queuedOpponent)
			userIdToProspectiveCashReward.put(userUuid, cashReward)
			userIdToProspectiveOilReward.put(userUuid, oilReward)
		}
	}

	private def createPvpProtosFromFakeUser(List<List<MonsterForPvp>> fakeUserMonsters)
	{
		LOG.info('creating fake users for pvp!!!!')
		val ppList = new ArrayList<PvpProto>()
		for (mons : fakeUserMonsters)
		{
			val user = createFakeUser(mons)
			ppList.add(user)
		}
		LOG.info('num fake users created: ' + ppList.size)
		ppList
	}

	private def createFakeUser(List<MonsterForPvp> mfpList)
	{
		val cashWinnings = new ArrayList<Integer>()
		val oilWinnings = new ArrayList<Integer>()
		val avgElo = determineAvgEloAndCashOilReward(mfpList, cashWinnings, oilWinnings)
		val userUuid = 0
		val randomName = hazelcastPvpUtil.randomName
		val lvl = avgElo / ControllerConstants.PVP__FAKE_USER_LVL_DIVISOR
		val prospectiveCashWinnings = cashWinnings.get(0)
		val prospectiveOilWinnings = oilWinnings.get(0)
		LOG.info(
			'fake user created: name=' + randomName + '	 avgElo=' + avgElo + '	 cash=' +
				prospectiveCashWinnings + '	 oil=' + prospectiveOilWinnings + '	 lvl=' + lvl)
		val fakeUser = CreateInfoProtoUtils::createFakePvpProto(userUuid, randomName, lvl,
			avgElo, prospectiveCashWinnings, prospectiveOilWinnings, mfpList)
		fakeUser
	}

	private def determineAvgEloAndCashOilReward(List<MonsterForPvp> mons,
		List<Integer> cashWinnings, List<Integer> oilWinnings)
	{
		var avgElo = 0
		var prospectiveCashWinnings = 0
		var prospectiveOilWinnings = 0
		for (mon : mons)
		{
			avgElo += mon.elo
			prospectiveCashWinnings += mon.cashDrop
			prospectiveOilWinnings += mon.oilDrop
		}
		avgElo = avgElo / mons.size
		cashWinnings.add(prospectiveCashWinnings)
		oilWinnings.add(prospectiveOilWinnings)
		avgElo
	}

	private def writeChangesToDB(int attackerId, User attacker, Timestamp queueTime,
		PvpLeagueForUser plfu, Map<String, Integer> money)
	{
		val curShieldEndTime = plfu.shieldEndTime
		val queueDate = new Date(queueTime.time)
		if (timeUtils.isFirstEarlierThanSecond(queueDate, curShieldEndTime))
		{
			LOG.info("shield end time is now being reset since he's attacking with a shield")
			LOG.info('1cur pvpuser=' + hazelcastPvpUtil.getPvpUser(attackerId))
			val login = attacker.lastLogin
			val loginTime = new Timestamp(login.time)
			UpdateUtils::get.updatePvpLeagueForUserShields(attackerId, loginTime, loginTime)
			val attackerOpu = new PvpUser(plfu)
			attackerOpu.shieldEndTime = login
			attackerOpu.inBattleEndTime = login
			hazelcastPvpUtil.replacePvpUser(attackerOpu, attackerId)
			LOG.info('2cur pvpuser=' + hazelcastPvpUtil.getPvpUser(attackerId))
			LOG.info('(should be same as 2cur pvpUser) 3cur pvpuser=' + attackerOpu)
		}
		true
	}

	def getHazelcastPvpUtil()
	{
		hazelcastPvpUtil
	}

	def setHazelcastPvpUtil(HazelcastPvpUtil hazelcastPvpUtil)
	{
		this.hazelcastPvpUtil = hazelcastPvpUtil
	}

	def getMonsterForPvpRetrieveUtils()
	{
		monsterForPvpRetrieveUtils
	}

	def setMonsterForPvpRetrieveUtils(MonsterForPvpRetrieveUtils monsterForPvpRetrieveUtils)
	{
		this.monsterForPvpRetrieveUtils = monsterForPvpRetrieveUtils
	}

	def getTimeUtils()
	{
		timeUtils
	}

	def setTimeUtils(TimeUtils timeUtils)
	{
		this.timeUtils = timeUtils
	}

	def getPvpLeagueForUserRetrieveUtil()
	{
		pvpLeagueForUserRetrieveUtil
	}

	def setPvpLeagueForUserRetrieveUtil(
		PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil)
	{
		this.pvpLeagueForUserRetrieveUtil = pvpLeagueForUserRetrieveUtil
	}
}
