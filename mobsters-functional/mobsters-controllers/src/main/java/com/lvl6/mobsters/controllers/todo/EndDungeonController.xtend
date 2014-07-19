package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.TaskForUserOngoing
import com.lvl6.mobsters.dynamo.TaskStageForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonResponseProto
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonResponseProto.EndDungeonStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.EndDungeonRequestEvent
import com.lvl6.mobsters.events.response.EndDungeonResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class EndDungeonController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(EndDungeonController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new EndDungeonRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_END_DUNGEON_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as EndDungeonRequestEvent)).endDungeonRequestProto
		LOG.info('reqProto=' + reqProto)
		val senderResourcesProto = reqProto.sender
		val senderProto = senderResourcesProto.minUserProto
		val userUuid = senderProto.userUuid
		val userTaskId = reqProto.userTaskUuid
		val userWon = reqProto.userWon
		val currentDate = new Date(reqProto.clientTime)
		val curTime = new Timestamp(reqProto.clientTime)
		val firstTimeUserWonTask = reqProto.firstTimeUserWonTask
		val maxCash = senderResourcesProto.maxCash
		val maxOil = senderResourcesProto.maxOil
		val resBuilder = EndDungeonResponseProto::newBuilder
		resBuilder.sender = senderResourcesProto
		resBuilder.userWon = userWon
		resBuilder.status = EndDungeonStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			var previousCash = 0
			var previousOil = 0
			val ut = TaskForUserOngoingRetrieveUtils::getUserTaskForId(userTaskId)
			val legit = checkLegit(resBuilder, aUser, userUuid, userTaskId, ut)
			var successful = false
			val money = new HashMap<String, Integer>()
			if (legit)
			{
				previousCash = aUser.cash
				previousOil = aUser.oil
				successful = writeChangesToDb(aUser, userUuid, ut, userWon, curTime, money,
					maxCash, maxOil)
				resBuilder.taskId = ut.taskId
			}
			if (successful)
			{
				val taskForUserId = ut.id
				val tsfuList = TaskStageForUserRetrieveUtils::
					getTaskStagesForUserWithTaskForUserId(taskForUserUuid)
				val monsterIdToNumPieces = new HashMap<Integer, Integer>()
				recordStageHistory(tsfuList, monsterIdToNumPieces)
				if (userWon)
				{
					LOG.info('user won dungeon, awarding the monsters and items')
					val mfusopB = new StringBuilder()
					mfusopB.append(ControllerConstants::MFUSOP__END_DUNGEON)
					mfusopB.append(' ')
					mfusopB.append(taskForUserId)
					val mfusop = mfusopB.toString
					val newOrUpdated = MonsterStuffUtils::updateUserMonsters(userUuid,
						monsterIdToNumPieces, mfusop, currentDate)
					setResponseBuilder(resBuilder, newOrUpdated)
				}
			}
			val resEvent = new EndDungeonResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.endDungeonResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in EndDungeonController.processRequestEvent', e)
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
					LOG.error('fatal exception in EndDungeonController.processRequestEvent', e)
				}
				val taskId = ut.taskId
				writeToUserCurrencyHistory(aUser, curTime, userTaskId, taskId, previousCash,
					previousOil, money)
				writeToTaskForUserCompleted(userUuid, taskId, userWon, firstTimeUserWonTask,
					curTime)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in EndDungeonController processEvent', e)
			try
			{
				resBuilder.status = EndDungeonStatus.FAIL_OTHER
				val resEvent = new EndDungeonResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.endDungeonResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in EndDungeonController.processRequestEvent', e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in EndDungeonController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegit(Builder resBuilder, User u, String userUuid, long userTaskId,
		TaskForUserOngoing ut)
	{
		if (null === u)
		{
			LOG.error('unexpected error: user is null. user=' + u)
			return false;
		}
		if (null === ut)
		{
			LOG.error('unexpected error: no user task for id userTaskId=' + userTaskId)
			return false;
		}
		resBuilder.status = EndDungeonStatus.SUCCESS
		true
	}

	private def writeChangesToDb(User u, int uId, TaskForUserOngoing ut, boolean userWon,
		Timestamp clientTime, Map<String, Integer> money, int maxCash, int maxOil)
	{
		var cashGained = ut.cashGained
		val expGained = ut.expGained
		var oilGained = ut.oilGained
		val curCash = Math::min(u.cash, maxCash)
		val maxCashUserCanGain = maxCash - curCash
		cashGained = Math::min(cashGained, maxCashUserCanGain)
		val curOil = Math::min(u.oil, maxOil)
		val maxOilUserCanGain = maxOil - curOil
		oilGained = Math::min(oilGained, maxOilUserCanGain)
		LOG.info('user before currency change. ' + u)
		if (userWon)
		{
			LOG.info('user WON DUNGEON!!!!!!!!. ')
			if (!updateUser(u, expGained, cashGained, oilGained, clientTime))
			{
				return false;
			}
			else
			{
				if (0 !== cashGained)
				{
					money.put(MiscMethods::cash, cashGained)
				}
				if (0 !== oilGained)
				{
					money.put(MiscMethods::oil, oilGained)
				}
			}
		}
		LOG.info('user after currency change. ' + u)
		val utId = ut.id
		val tId = ut.taskId
		val numRevives = ut.numRevives
		val startDate = ut.startDate
		val startMillis = startDate.time
		val startTime = new Timestamp(startMillis)
		val cancelled = false
		val tsId = ut.taskStageId
		var num = InsertUtils::get.insertIntoTaskHistory(utId, uId, tId, expGained, cashGained,
			oilGained, numRevives, startTime, clientTime, userWon, cancelled, tsId)
		if (1 !== num)
		{
			LOG.error(
				'unexpected error: error when inserting into user_task_history. ' +
					'numInserted=' + num + ' Attempting to undo shi')
			updateUser(u, -1 * expGained, -1 * cashGained, -1 * oilGained, clientTime)
			return false;
		}
		num = DeleteUtils::get.deleteTaskForUserOngoingWithTaskForUserUuid(utId)
		LOG.info('num rows deleted from task_for_user table. num=' + num)
		true
	}

	private def updateUser(User u, int expGained, int cashGained, int oilGained,
		Timestamp clientTime)
	{
		val energyChange = 0
		if (!u.updateRelativeCashOilExpTasksCompleted(expGained, cashGained, oilGained, 1,
			clientTime))
		{
			LOG.error(
				'problem with updating user stats post-task. expGained=' + expGained +
					', cashGained=' + cashGained + ', oilGained=' + oilGained + ', increased' +
					' tasks completed by 1, energyChange=' + energyChange + ', clientTime=' +
					clientTime + ', user=' + u)
			return false;
		}
		true
	}

	private def recordStageHistory(List<TaskStageForUser> tsfuList,
		Map<Integer, Integer> monsterIdToNumPieces)
	{
		val taskStageMonsterIdToQuantity = new HashMap<Integer, Integer>()
		val userTaskStageId = new ArrayList<Long>()
		val userTaskId = new ArrayList<Long>()
		val stageNum = new ArrayList<Integer>()
		val taskStageMonsterIdList = new ArrayList<Integer>()
		val monsterTypes = new ArrayList<String>()
		val expGained = new ArrayList<Integer>()
		val cashGained = new ArrayList<Integer>()
		val oilGained = new ArrayList<Integer>()
		val monsterPieceDropped = new ArrayList<Boolean>()
		val itemIdDropped = new ArrayList<Integer>()
		for (i : 0 ..< tsfuList.size)
		{
			val tsfu = tsfuList.get(i)
			userTaskStageId.add(tsfu.id)
			userTaskId.add(tsfu.userTaskId)
			stageNum.add(tsfu.stageNum)
			val taskStageMonsterId = tsfu.taskStageMonsterId
			taskStageMonsterIdList.add(taskStageMonsterId)
			monsterTypes.add(tsfu.monsterType)
			expGained.add(tsfu.expGained)
			cashGained.add(tsfu.cashGained)
			oilGained.add(tsfu.oilGained)
			val dropped = tsfu.monsterPieceDropped
			monsterPieceDropped.add(dropped)
			itemIdDropped.add(tsfu.itemIdDropped)
			if (!dropped)
			{
				continue;
			}
			if (taskStageMonsterIdToQuantity.containsKey(taskStageMonsterId))
			{
				val quantity = 1 + taskStageMonsterIdToQuantity.get(taskStageMonsterId)
				taskStageMonsterIdToQuantity.put(taskStageMonsterId, quantity)
			}
			else
			{
				taskStageMonsterIdToQuantity.put(taskStageMonsterId, 1)
			}
		}
		var num = InsertUtils::get.insertIntoTaskStageHistory(userTaskStageId, userTaskId,
			stageNum, taskStageMonsterIdList, monsterTypes, expGained, cashGained, oilGained,
			monsterPieceDropped, itemIdDropped)
		LOG.info(
			'num task stage history rows inserted: num=' + num + 'taskStageForUser=' + tsfuList)
		num = DeleteUtils::get.deleteTaskStagesForUserWithUuids(userTaskStageId)
		LOG.info('num task stage for user rows deleted: num=' + num)
		val taskStageMonsterUuids = taskStageMonsterIdToQuantity.keySet
		val monstersThatDropped = TaskStageMonsterRetrieveUtils::
			getTaskStageMonstersForUuids(taskStageMonsterUuids)
		for (taskStageMonsterId : taskStageMonsterUuids)
		{
			val monsterThatDropped = monstersThatDropped.get(taskStageMonsterId)
			val monsterId = monsterThatDropped.monsterId
			val numPiecesDroppedForMonster = taskStageMonsterIdToQuantity.get(taskStageMonsterId)
			if (monsterIdToNumPieces.containsKey(monsterId))
			{
				val newAmount = numPiecesDroppedForMonster + monsterIdToNumPieces.get(monsterId)
				monsterIdToNumPieces.put(monsterId, newAmount)
			}
			else
			{
				monsterIdToNumPieces.put(monsterId, numPiecesDroppedForMonster)
			}
		}
	}

	private def setResponseBuilder(Builder resBuilder, List<FullUserMonsterProto> protos)
	{
		resBuilder.status = EndDungeonStatus.SUCCESS
		if (!protos.empty)
		{
			resBuilder.addAllUpdatedOrNew(protos)
		}
	}

	private def writeToUserCurrencyHistory(User aUser, Timestamp curTime, long userTaskId,
		int taskId, int previousCash, int previousOil, Map<String, Integer> money)
	{
		if (money.empty)
		{
			return;
		}
		val sb = new StringBuilder()
		sb.append('userTask=')
		sb.append(userTaskId)
		sb.append(' taskId=')
		sb.append(taskId)
		val cash = MiscMethods::cash
		val oil = MiscMethods::oil
		val reasonForChange = ControllerConstants.UCHRFC__END_TASK
		val userUuid = aUser.id
		val previousCurrencies = new HashMap<String, Integer>()
		val currentCurrencies = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		if (money.containsKey(cash))
		{
			previousCurrencies.put(cash, previousCash)
		}
		if (money.containsKey(oil))
		{
			previousCurrencies.put(oil, previousOil)
		}
		currentCurrencies.put(cash, aUser.cash)
		currentCurrencies.put(oil, aUser.oil)
		reasonsForChanges.put(cash, reasonForChange)
		reasonsForChanges.put(oil, reasonForChange)
		detailsMap.put(cash, sb.toString)
		detailsMap.put(oil, sb.toString)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, money, previousCurrencies,
			currentCurrencies, reasonsForChanges, detailsMap)
	}

	private def writeToTaskForUserCompleted(String userUuid, int taskId, boolean userWon,
		boolean firstTimeUserWonTask, Timestamp now)
	{
		if (userWon && firstTimeUserWonTask)
		{
			val numInserted = InsertUtils::get.
				insertIntoTaskForUserCompleted(userUuid, taskId, now)
			LOG.info('numInserted into task_for_user_completed: ' + numInserted)
		}
	}
}
