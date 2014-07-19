package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser
import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedResponseProto.EvolutionFinishedStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.EvolutionFinishedRequestEvent
import com.lvl6.mobsters.events.response.EvolutionFinishedResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class EvolutionFinishedController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(EvolutionFinishedController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 3
	}

	override createRequestEvent()
	{
		new EvolutionFinishedRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_EVOLUTION_FINISHED_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as EvolutionFinishedRequestEvent)).evolutionFinishedRequestProto
		LOG.info('reqProto=' + reqProto)
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val gemsSpent = reqProto.gemsSpent
		val now = new Date()
		val resBuilder = EvolutionFinishedResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = EvolutionFinishedStatus.FAIL_OTHER
		locker.lockPlayer(senderProto.userUuid, class.simpleName)
		try
		{
			var previousGems = 0
			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			val evolution = MonsterEvolvingForUserRetrieveUtils::getEvolutionForUser(userUuid)
			val existingUserMonsters = getMonstersUsedInEvolution(userUuid, evolution)
			LOG.info('evolution=' + evolution)
			LOG.info('existingUserMonsters=' + existingUserMonsters)
			val legitMonster = checkLegit(resBuilder, aUser, userUuid, evolution,
				existingUserMonsters, gemsSpent)
			var successful = false
			val money = new HashMap<String, Integer>()
			val evolvedUserMonster = new ArrayList<MonsterForUser>()
			if (legitMonster)
			{
				previousGems = aUser.gems
				successful = writeChangesToDB(aUser, userUuid, now, gemsSpent, evolution, money,
					existingUserMonsters, evolvedUserMonster)
			}
			if (successful)
			{
				val evolvedMfu = evolvedUserMonster.get(0)
				val fump = CreateInfoProtoUtils::
					createFullUserMonsterProtoFromUserMonster(evolvedMfu)
				resBuilder.evolvedMonster = fump
				resBuilder.status = EvolutionFinishedStatus.SUCCESS
			}
			val resEvent = new EvolutionFinishedResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.evolutionFinishedResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in EvolutionFinishedController.processRequestEvent',
					e)
			}
			if (successful)
			{
				val resEventUpdate = MiscMethods::
					createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser, null)
				resEventUpdate.tag = event.tag
				LOG.info('Writing event: ' + resEventUpdate)
				try
				{
					eventWriter.writeEvent(resEventUpdate)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in EvolutionFinishedController.processRequestEvent', e)
				}
				writeToUserCurrencyHistory(aUser, now, money, previousGems, evolution,
					evolvedUserMonster)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in EnhanceMonster processEvent', e)
		}
		finally
		{
			locker.unlockPlayer(senderProto.userUuid, class.simpleName)
		}
	}

	private def getMonstersUsedInEvolution(String userUuid, MonsterEvolvingForUser evolution)
	{
		var existingUserMonsters = new HashMap<Long, MonsterForUser>()
		if (null !== evolution)
		{
			val newUuids = new HashSet<Long>()
			val catalystUserMonsterId = evolution.catalystMonsterForUserUuid
			val userMonsterIdOne = evolution.monsterForUserUuidOne
			val userMonsterIdTwo = evolution.monsterForUserUuidTwo
			newUuids.add(catalystUserMonsterId)
			newUuids.add(userMonsterIdOne)
			newUuids.add(userMonsterIdTwo)
			existingUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
				getSpecificOrAllUserMonstersForUser(userUuid, newUuids)
		}
		existingUserMonsters
	}

	private def checkLegit(Builder resBuilder, User u, String userUuid,
		MonsterEvolvingForUser evolution, Map<Long, MonsterForUser> existingUserMonsters,
		int gemsSpent)
	{
		if ((null === u) || (null === evolution) || (null === existingUserMonsters) ||
			existingUserMonsters.empty)
		{
			LOG.error(
				'unexpected error: user, evolution, or existingMonsters is null. user=' + u +
					',	 evolution=' + evolution + '	 existingMonsters=' + existingUserMonsters)
			return false;
		}
		val catalystUserMonsterId = evolution.catalystMonsterForUserUuid
		val one = evolution.monsterForUserUuidOne
		val two = evolution.monsterForUserUuidTwo
		if (!existingUserMonsters.containsKey(catalystUserMonsterId) ||
			!existingUserMonsters.containsKey(one) || !existingUserMonsters.containsKey(two))
		{
			LOG.error(
				'one of the monsters in an evolution is missing. evolution=' + evolution +
					'	 existingUserMonsters=' + existingUserMonsters)
			resBuilder.status = EvolutionFinishedStatus.FAIL_OTHER
			return false;
		}
		if (!hasEnoughGems(resBuilder, u, gemsSpent, evolution))
		{
			return false;
		}
		true
	}

	private def hasEnoughGems(Builder resBuilder, User u, int gemsSpent,
		MonsterEvolvingForUser evolution)
	{
		val userGems = u.gems
		if (userGems < gemsSpent)
		{
			LOG.error(
				'user error: user does not have enough gems. userGems=' + userGems +
					'	 gemsSpent=' + gemsSpent + '	 evolution=' + evolution)
			resBuilder.status = EvolutionFinishedStatus.FAIL_INSUFFICIENT_GEMS
			return false;
		}
		true
	}

	private def writeChangesToDB(User user, int uId, Date now, int gemsSpent,
		MonsterEvolvingForUser mefu, Map<String, Integer> money,
		Map<Long, MonsterForUser> idsToUserMonsters, List<MonsterForUser> userMonsters)
	{
		if (0 !== gemsSpent)
		{
			val oilChange = 0
			val cashChange = 0
			val gemChange = -1 * gemsSpent
			val numChange = user.updateRelativeCashAndOilAndGems(cashChange, oilChange,
				gemChange)
			if (1 !== numChange)
			{
				LOG.warn(
					'problem with updating user stats: gemChange=' + gemChange + ', cashChange=' +
						oilChange + ', user is ' + user)
			}
			else
			{
				if (0 !== oilChange)
				{
					money.put(MiscMethods::oil, oilChange)
				}
				if (0 !== gemsSpent)
				{
					money.put(MiscMethods::gems, gemChange)
				}
			}
		}
		val userMonsterUuids = new ArrayList<Long>()
		val catalystUserMonsterId = mefu.catalystMonsterForUserUuid
		userMonsterUuids.add(catalystUserMonsterId)
		val uMonsterIdOne = mefu.monsterForUserUuidOne
		userMonsterUuids.add(uMonsterIdOne)
		val uMonsterIdTwo = mefu.monsterForUserUuidTwo
		userMonsterUuids.add(uMonsterIdTwo)
		var num = DeleteUtils::get.deleteMonstersForUser(userMonsterUuids)
		LOG.info('num monsterForUser deleted: ' + num)
		num = DeleteUtils::get.deleteMonsterEvolvingForUser(catalystUserMonsterId, uMonsterIdOne,
			uMonsterIdTwo, uId)
		LOG.info('num evolutions deleted: ' + num)
		val evolvedUserMonster = createEvolvedMonster(uId, now, uMonsterIdOne, idsToUserMonsters)
		userMonsters.add(evolvedUserMonster)
		val sourceOfPieces = createSourceOfPieces(catalystUserMonsterId, uMonsterIdOne,
			uMonsterIdTwo)
		val evovlvedMfuId = InsertUtils::get.
			insertIntoMonsterForUserReturnUuids(uId, userMonsters, sourceOfPieces, now)
		if ((null !== evovlvedMfuId) && !evovlvedMfuId.empty)
		{
			val mfuId = evovlvedMfuId.get(0)
			evolvedUserMonster.id = mfuId
		}
		LOG.info('evolvedUserMonster=' + evolvedUserMonster)
		LOG.info('userMonsters=' + userMonsters)
		true
	}

	private def createEvolvedMonster(int uId, Date now, long uMonsterIdOne,
		Map<Long, MonsterForUser> idsToUserMonsters)
	{
		val unevolvedMonster = idsToUserMonsters.get(uMonsterIdOne)
		val monsterId = unevolvedMonster.monsterId
		val evolvedMonster = MonsterRetrieveUtils::getEvolvedFormForMonster(monsterId)
		val numPieces = evolvedMonster.numPuzzlePieces
		val isComplete = true
		val mfu = MonsterStuffUtils::createNewUserMonster(uId, numPieces, evolvedMonster, now,
			isComplete)
		mfu
	}

	private def createSourceOfPieces(long catalystUserMonsterId, long uMonsterIdOne,
		long uMonsterIdTwo)
	{
		val sourceOfPiecesSb = new StringBuilder()
		sourceOfPiecesSb.append('evolved from (catalystId,idOne,idTwo): (')
		sourceOfPiecesSb.append(catalystUserMonsterId)
		sourceOfPiecesSb.append(',')
		sourceOfPiecesSb.append(uMonsterIdOne)
		sourceOfPiecesSb.append(',')
		sourceOfPiecesSb.append(uMonsterIdTwo)
		sourceOfPiecesSb.append(')')
		sourceOfPiecesSb.toString
	}

	def writeToUserCurrencyHistory(User aUser, Date now, Map<String, Integer> moneyChange,
		int previousGems, MonsterEvolvingForUser evolution,
		List<MonsterForUser> evolvedUserMonsterList)
	{
		if (moneyChange.empty)
		{
			return;
		}
		val gems = MiscMethods::gems
		val date = new Timestamp((now.time))
		val catalystUserMonsterId = evolution.catalystMonsterForUserUuid
		val one = evolution.monsterForUserUuidOne
		val two = evolution.monsterForUserUuidTwo
		val evolved = evolvedUserMonsterList.get(0)
		val evolvedId = evolved.id
		val reasonForChange = ControllerConstants.UCHRFC__SPED_UP_EVOLUTION
		val detailSb = new StringBuilder()
		detailSb.append('(catalystId, userMonsterId, userMonsterId, evolvedMonsterId)')
		detailSb.append('(')
		detailSb.append(catalystUserMonsterId)
		detailSb.append(',')
		detailSb.append(one)
		detailSb.append(',')
		detailSb.append(two)
		detailSb.append(',')
		detailSb.append(evolvedId)
		detailSb.append(')')
		val userUuid = aUser.id
		val previousCurrencyMap = new HashMap<String, Integer>()
		val currentCurrencyMap = new HashMap<String, Integer>()
		val changeReasonsMap = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		previousCurrencyMap.put(gems, previousGems)
		currentCurrencyMap.put(gems, aUser.gems)
		changeReasonsMap.put(gems, reasonForChange)
		detailsMap.put(gems, detailSb.toString)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, date, moneyChange, previousCurrencyMap,
			currentCurrencyMap, changeReasonsMap, detailsMap)
	}
}
