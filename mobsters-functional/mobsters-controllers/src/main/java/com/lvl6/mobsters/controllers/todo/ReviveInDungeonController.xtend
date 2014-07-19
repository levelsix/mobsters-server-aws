package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonResponseProto
import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonResponseProto.ReviveInDungeonStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.ReviveInDungeonRequestEvent
import com.lvl6.mobsters.events.response.ReviveInDungeonResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.HashMap
import java.util.List
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class ReviveInDungeonController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(ReviveInDungeonController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new ReviveInDungeonRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_REVIVE_IN_DUNGEON_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as ReviveInDungeonRequestEvent)).reviveInDungeonRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val userTaskId = reqProto.userTaskUuid
		val curTime = new Timestamp(reqProto.clientTime)
		val reviveMeProtoList = reqProto.reviveMeList
		val gemsSpent = reqProto.gemsSpent
		val resBuilder = ReviveInDungeonResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = ReviveInDungeonStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			var previousGems = 0
			val userMonsterIdToExpectedHealth = new HashMap<Long, Integer>()
			val legit = checkLegit(resBuilder, aUser, userUuid, gemsSpent, userTaskId,
				reviveMeProtoList, userMonsterIdToExpectedHealth)
			val currencyChange = new HashMap<String, Integer>()
			var successful = false
			if (legit)
			{
				previousGems = aUser.gems
				successful = writeChangesToDb(aUser, userUuid, userTaskId, gemsSpent, curTime,
					userMonsterIdToExpectedHealth, currencyChange)
			}
			if (successful)
			{
				resBuilder.status = ReviveInDungeonStatus.SUCCESS
			}
			val resEvent = new ReviveInDungeonResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.reviveInDungeonResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in ReviveInDungeonController.processRequestEvent', e)
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
					LOG.error('fatal exception in ReviveInDungeonController.processRequestEvent',
						e)
				}
				writeToUserCurrencyHistory(userUuid, aUser, userTaskId, curTime, previousGems,
					currencyChange)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in ReviveInDungeonController processEvent', e)
			try
			{
				resBuilder.status = ReviveInDungeonStatus.FAIL_OTHER
				val resEvent = new ReviveInDungeonResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.reviveInDungeonResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in ReviveInDungeonController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in ReviveInDungeonController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegit(Builder resBuilder, User u, String userUuid, int gemsSpent,
		long userTaskId, List<UserMonsterCurrentHealthProto> reviveMeProtoList,
		Map<Long, Integer> userMonsterIdToExpectedHealth)
	{
		if (null === u)
		{
			LOG.error('unexpected error: user is null. user=' + u)
			return false;
		}
		val userMonsterUuids = MonsterStuffUtils::getUserMonsterUuids(reviveMeProtoList,
			userMonsterIdToExpectedHealth)
		val userMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
			getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids)
		if ((null === userMonsters) || userMonsters.empty)
		{
			LOG.error("unexpected error: userMonsterUuids don't exist. ids=" + userMonsterUuids)
			return false;
		}
		if (userMonsters.size !== reviveMeProtoList.size)
		{
			LOG.error(
				'unexpected error: mismatch between user equips client sent and ' +
					'what is in the db. clientUserMonsterUuids=' + userMonsterUuids + '	 inDb=' +
					userMonsters + '	 continuing the processing')
		}
		val userDiamonds = u.gems
		val cost = gemsSpent
		if (cost > userDiamonds)
		{
			LOG.error(
				'user error: user does not have enough diamonds to revive. ' + 'cost=' + cost +
					'	 userDiamonds=' + userDiamonds)
			resBuilder.status = ReviveInDungeonStatus.FAIL_INSUFFICIENT_FUNDS
			return false;
		}
		resBuilder.status = ReviveInDungeonStatus.SUCCESS
		true
	}

	private def writeChangesToDb(User u, int uId, long userTaskId, int gemsSpent,
		Timestamp clientTime, Map<Long, Integer> userMonsterIdToExpectedHealth,
		Map<String, Integer> currencyChange)
	{
		val gemsChange = -1 * gemsSpent
		if (!updateUser(u, gemsChange))
		{
			LOG.error("unexpected error: could not decrement user's gold by " + gemsChange)
			return false;
		}
		else
		{
			if (0 !== gemsChange)
			{
				currencyChange.put(MiscMethods::gems, gemsSpent)
			}
		}
		val numRevivesDelta = 1
		var numUpdated = UpdateUtils::get.incrementUserTaskNumRevives(userTaskId,
			numRevivesDelta)
		if (1 !== numUpdated)
		{
			LOG.error(
				'unexpected error: user_task not updated correctly. Attempting ' +
					'to give back diamonds. userTaskId=' + userTaskId + '	 numUpdated=' +
					numUpdated + '	 user=' + u)
			if (!updateUser(u, -1 * gemsChange))
			{
				LOG.error(
					"unexpected error: could not change back user's gems by " +
						(-1 * gemsChange))
			}
			currencyChange.clear
			return false;
		}
		numUpdated = UpdateUtils::get.updateUserMonstersHealth(userMonsterIdToExpectedHealth)
		if (numUpdated >= userMonsterIdToExpectedHealth.size)
		{
			return true;
		}
		LOG.warn(
			'unexpected error: not all user equips were updated. ' + 'actual numUpdated=' +
				numUpdated + 'expected: ' + 'userMonsterIdToExpectedHealth=' +
				userMonsterIdToExpectedHealth)
		true
	}

	private def updateUser(User u, int diamondChange)
	{
		if (!u.updateRelativeGemsNaive(diamondChange))
		{
			LOG.error(
				'unexpected error: problem with updating user diamonds for reviving. diamondChange=' +
					diamondChange + 'user=' + u)
			return false;
		}
		true
	}

	private def writeToUserCurrencyHistory(String userUuid, User aUser, long userTaskId,
		Timestamp curTime, int previousGems, Map<String, Integer> currencyChange)
	{
		if (currencyChange.empty)
		{
			return;
		}
		val detailsSb = new StringBuilder()
		detailsSb.append('userTaskId=')
		detailsSb.append(userTaskId)
		val previousCurrency = new HashMap<String, Integer>()
		val currentCurrency = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		val reason = ControllerConstants.UCHRFC__REVIVE_IN_DUNGEON
		val gems = MiscMethods::gems
		previousCurrency.put(gems, previousGems)
		currentCurrency.put(gems, aUser.gems)
		reasonsForChanges.put(gems, reason)
		detailsMap.put(gems, detailsSb.toString)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
	}
}
