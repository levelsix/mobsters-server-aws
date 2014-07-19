package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan
import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsResponseProto
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsResponseProto.RecordClanRaidStatsStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RecordClanRaidStatsRequestEvent
import com.lvl6.mobsters.events.response.RecordClanRaidStatsResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.common.TimeUtils
import java.sql.Timestamp
import java.util.ArrayList
import java.util.HashMap
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class RecordClanRaidStatsController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(RecordClanRaidStatsController))
	@Autowired
	protected var DataServiceTxManager svcTxManager
	@Autowired
	protected var HazelcastPvpUtil hazelcastPvpUtil
	@Autowired
	protected var TimeUtils timeUtils

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new RecordClanRaidStatsRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_RECORD_CLAN_RAID_STATS_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as RecordClanRaidStatsRequestEvent)).
			recordClanRaidStatsRequestProto
		val startTime = System::nanoTime
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val clanId = reqProto.clanUuid
		val now = new Timestamp(reqProto.clientTime)
		val resBuilder = RecordClanRaidStatsResponseProto::newBuilder
		resBuilder.status = RecordClanRaidStatsStatus.FAIL_OTHER
		resBuilder.sender = senderProto
		val long endTimeAfterLock
		val long endTimeAfterCheckLegitRequest
		val long endTimeAfterWriteChangesToDb
		val long endTimeAfterWriteEvent
		val long endTimeAfterWriteClanEvent
		val long endTimeAfterUnlock
		var ClanEventPersistentForClan clanEvent = null
		val userIdToClanUserInfo = new HashMap<Integer, ClanEventPersistentForUser>()
		var lockedClan = true
		if (0 !== clanId)
		{
			lockedClan = locker.lockClan(clanId)
		}
		try
		{
			endTimeAfterLock = System::nanoTime
			clanEvent = ClanEventPersistentForClanRetrieveUtils::
				getPersistentEventForClanId(clanId)
			val legitRequest = checkLegitRequest(resBuilder, lockedClan, senderProto, userUuid,
				clanId, clanEvent)
			endTimeAfterCheckLegitRequest = System::nanoTime
			val resEvent = new RecordClanRaidStatsResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.recordClanRaidStatsResponseProto = resBuilder.build
			var success = false
			if (legitRequest)
			{
				success = writeChangesToDB(userUuid, clanId, now, clanEvent,
					userIdToClanUserInfo)
			}
			endTimeAfterWriteChangesToDb = System::nanoTime
			if (success)
			{
			}
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in RecordClanRaidStatsController.processRequestEvent',
					e)
			}
			endTimeAfterWriteEvent = System::nanoTime
			if (legitRequest)
			{
				server.writeClanEvent(resEvent, clanId)
			}
			endTimeAfterWriteClanEvent = System::nanoTime
			LOG.info('startTime to afterLock took ~{}ms',
				(endTimeAfterLock - startTime) / 1000000)
			LOG.info('afterLock to afterCheckLegitRequest took ~{}ms',
				(endTimeAfterCheckLegitRequest - endTimeAfterLock) / 1000000)
			LOG.info('afterCheckLegitRequest to endTimeAfterWriteChangesToDb took ~{}ms',
				(endTimeAfterWriteChangesToDb - endTimeAfterCheckLegitRequest) / 1000000)
			LOG.info('endTimeAfterWriteChangesToDb to endTimeAfterWriteEvent took ~{}ms',
				(endTimeAfterWriteEvent - endTimeAfterWriteChangesToDb) / 1000000)
			LOG.info('endTimeAfterWriteEvent to endTimeAfterWriteClanEvent took ~{}ms',
				(endTimeAfterWriteClanEvent - endTimeAfterWriteEvent) / 1000000)
		}
		catch (Exception e)
		{
			try
			{
				resBuilder.status = RecordClanRaidStatsStatus.FAIL_OTHER
				val resEvent = new RecordClanRaidStatsResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.recordClanRaidStatsResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in RecordClanRaidStatsController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception in RecordClanRaidStats processEvent', e)
			}
		}
		finally
		{
			if ((0 !== clanId) && lockedClan)
			{
				locker.unlockClan(clanId)
			}
			endTimeAfterUnlock = System::nanoTime
			LOG.info('startTime to endTimeAfterUnlock took ~{}ms',
				(startTime - endTimeAfterUnlock) / 1000000)
		}
	}

	private def checkLegitRequest(Builder resBuilder, boolean lockedClan,
		MinimumUserProto mupfc, String userUuid, int clanId,
		ClanEventPersistentForClan clanEvent)
	{
		if (!lockedClan)
		{
			LOG.error("couldn't obtain clan lock")
			return false;
		}
		if ((0 === clanId) || (0 === userUuid) || (null === clanEvent))
		{
			LOG.error(
				'not in clan. user is ' + mupfc + '	 or clanId invalid id=' + clanId +
					'	 or clanEvent is null clanEvent=' + clanEvent)
			return false;
		}
		resBuilder.status = RecordClanRaidStatsStatus.SUCCESS
		true
	}

	private def writeChangesToDB(String userUuid, int clanId, Timestamp now,
		ClanEventPersistentForClan clanEvent,
		Map<Integer, ClanEventPersistentForUser> userIdToClanUserInfo)
	{
		val clanEventPersistentId = clanEvent.clanEventPersistentId
		val crId = clanEvent.crId
		val crsId = clanEvent.crsId
		var Timestamp stageStartTime = null
		if (null !== clanEvent.stageStartTime)
		{
			stageStartTime = new Timestamp(clanEvent.stageStartTime.time)
		}
		val crsmId = clanEvent.crsmId
		var Timestamp stageMonsterStartTime = null
		if (null !== clanEvent.stageMonsterStartTime)
		{
			stageMonsterStartTime = new Timestamp(clanEvent.stageMonsterStartTime.time)
		}
		val won = false
		var numInserted = InsertUtils::get.
			insertIntoClanEventPersistentForClanHistory(clanId, now, clanEventPersistentId, crId,
				crsId, stageStartTime, crsmId, stageMonsterStartTime, won)
		LOG.info(
			'rows inserted into clan raid info for clan history (should be 1): ' + numInserted)
		val clanUserInfo = ClanEventPersistentForUserRetrieveUtils::
			getPersistentEventUserInfoForClanId(clanId)
		DeleteUtils::get.deleteClanEventPersistentForClan(clanId)
		if ((null !== clanUserInfo) && !clanUserInfo.empty)
		{
			numInserted = InsertUtils::get.insertIntoCepfuRaidHistory(clanEventPersistentId, now,
				clanUserInfo)
			LOG.info(
				'rows inserted into clan raid info for user history (should be ' +
					clanUserInfo.size + '): ' + numInserted)
			val userIdList = new ArrayList<Integer>(clanUserInfo.keySet)
			DeleteUtils::get.deleteClanEventPersistentForUsers(userIdList)
			userIdToClanUserInfo.putAll(clanUserInfo)
		}
		true
	}

	protected def getHazelcastPvpUtil()
	{
		hazelcastPvpUtil
	}

	protected def setHazelcastPvpUtil(HazelcastPvpUtil hazelcastPvpUtil)
	{
		this.hazelcastPvpUtil = hazelcastPvpUtil
	}

	def getTimeUtils()
	{
		timeUtils
	}

	def setTimeUtils(TimeUtils timeUtils)
	{
		this.timeUtils = timeUtils
	}
}
