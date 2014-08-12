//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser
//import com.lvl6.mobsters.dynamo.ClanForUser
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto
//import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto.BeginClanRaidStatus
//import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto.Builder
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.BeginClanRaidRequestEvent
//import com.lvl6.mobsters.events.response.BeginClanRaidResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
//import com.lvl6.mobsters.server.EventController
//import com.lvl6.mobsters.utility.common.TimeUtils
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Date
//import java.util.HashSet
//import java.util.List
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class BeginClanRaidController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(BeginClanRaidController))
//	
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	
//	@Autowired
//	protected var TimeUtils timeUtils
//	
//	@Autowired
//	protected var ClanEventUtil clanEventUtil
//
//	new()
//	{
//		numAllocatedThreads = 4
//	}
//
//	override createRequestEvent()
//	{
//		return new BeginClanRaidRequestEvent()
//	}
//
//	override getEventType()
//	{
//		return EventProtocolRequest.C_BEGIN_CLAN_RAID_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as BeginClanRaidRequestEvent)).beginClanRaidRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val mcp = senderProto.clan
//		var clanId = mcp.clanId
//		val clanEventPersistentId = reqProto.clanEventUuid
//		val curDate = new Date(reqProto.curTime)
//		val curTime = new Timestamp(curDate.time)
//		val clanRaidId = reqProto.raidUuid
//		val setMonsterTeamForRaid = reqProto.setMonsterTeamForRaid
//		val userMonsters = reqProto.userMonstersList
//		val userMonsterUuids = MonsterStuffUtils::getUserMonsterUuids(userMonsters)
//		val isFirstStage = reqProto.isFirstStage
//		val resBuilder = BeginClanRaidResponseProto::newBuilder
//		resBuilder.status = BeginClanRaidStatus.FAIL_OTHER
//		resBuilder.sender = senderProto
//		var lockedClan = false
//		if ((null !== mcp) && mcp.clanId)
//		{
//			clanId = mcp.clanId
//			if ((0 !== clanId) && !setMonsterTeamForRaid)
//			{
//				lockedClan = locker.lockClan(clanId)
//				LOG.info('locking clanId=' + clanId)
//			}
//		}
//		try
//		{
//			val uc = RetrieveUtils::userClanRetrieveUtils.getSpecificUserClan(userUuid, clanId)
//			val legitRequest = checkLegitRequest(resBuilder, lockedClan, senderProto, userUuid,
//				clanId, uc, clanEventPersistentId, clanRaidId, curDate, curTime,
//				setMonsterTeamForRaid, userMonsterUuids, isFirstStage)
//			val clanInfoList = new ArrayList<ClanEventPersistentForClan>()
//			var success = false
//			if (legitRequest)
//			{
//				LOG.info(
//					'recording in the db that the clan began a clan raid or setting monsters.' +
//						' or starting a stage. isFirstStage=' + isFirstStage)
//				success = writeChangesToDB(userUuid, clanId, clanEventPersistentId, clanRaidId,
//					curTime, setMonsterTeamForRaid, userMonsterUuids, isFirstStage, clanInfoList)
//			}
//			if (success)
//			{
//				if (!setMonsterTeamForRaid)
//				{
//					val cepfc = clanInfoList.get(0)
//					val eventDetails = CreateInfoProtoUtils::
//						createPersistentClanEventClanInfoProto(cepfc)
//					resBuilder.eventDetails = eventDetails
//				}
//				if (setMonsterTeamForRaid)
//				{
//					setClanAndUserDetails(resBuilder, userUuid, clanId, clanRaidId,
//						userMonsterUuids, userMonsters)
//				}
//				resBuilder.status = BeginClanRaidStatus.SUCCESS
//				LOG.info('BEGIN CLAN RAID EVENT SUCCESS!!!!!!!')
//			}
//			val resEvent = new BeginClanRaidResponseEvent(userUuid)
//			resEvent.tag = event.tag
//			resEvent.beginClanRaidResponseProto = resBuilder.build
//			LOG.info('resBuilder=' + resBuilder.build)
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in BeginClanRaidController.processRequestEvent', e)
//			}
//			if (success)
//			{
//				server.writeClanEvent(resEvent, clanId)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in BeginClanRaid processEvent', e)
//			try
//			{
//				resBuilder.status = BeginClanRaidStatus.FAIL_OTHER
//				val resEvent = new BeginClanRaidResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.beginClanRaidResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in BeginClanRaidController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in BeginClanRaid processEvent', e)
//			}
//		}
//		finally
//		{
//			if ((null !== mcp) && mcp.clanId)
//			{
//				if ((0 !== clanId) && !setMonsterTeamForRaid && lockedClan)
//				{
//					locker.unlockClan(clanId)
//					LOG.info('unlocked clanId=' + clanId)
//				}
//			}
//		}
//	}
//
//	private def checkLegitRequest(Builder resBuilder, boolean lockedClan,
//		MinimumUserProto mupfc, String userUuid, int clanId, ClanForUser uc, int clanEventId,
//		int clanRaidId, Date curDate, Timestamp curTime, boolean setMonsterTeamForRaid,
//		List<Long> userMonsterUuids, boolean isFirstStage)
//	{
//		if (!lockedClan)
//		{
//			LOG.error("couldn't obtain clan lock")
//			return false;
//		}
//		if ((0 === clanId) || (0 === clanRaidId) || (null === uc))
//		{
//			LOG.error(
//				'not in clan. user is ' + mupfc + '	 or clanRaidId invalid id=' + clanRaidId +
//					'	 or no user clan exists. uc=' + uc)
//			return false;
//		}
//		val clanEventIdToEvent = ClanEventPersistentRetrieveUtils::
//			getActiveClanEventUuidsToEvents(curDate, timeUtils)
//		if (!clanEventIdToEvent.containsKey(clanEventId))
//		{
//			resBuilder.status = BeginClanRaidStatus.FAIL_NO_ACTIVE_CLAN_RAID
//			LOG.error('no active clan event with id=' + clanEventId + '	 user=' + mupfc)
//			return false;
//		}
//		val cep = clanEventIdToEvent.get(clanEventId)
//		val eventRaidId = cep.clanRaidId
//		if (clanRaidId !== eventRaidId)
//		{
//			resBuilder.status = BeginClanRaidStatus.FAIL_NO_ACTIVE_CLAN_RAID
//			LOG.error(
//				'no active clan event with raidId=' + clanRaidId + '	 event=' + cep + '	 user=' +
//					mupfc)
//			return false;
//		}
//		if (!setMonsterTeamForRaid)
//		{
//			val authorizedUsers = getAuthorizedUsers(clanId)
//			if (!authorizedUsers.contains(userUuid))
//			{
//				resBuilder.status = BeginClanRaidStatus.FAIL_NOT_AUTHORIZED
//				LOG.error("user can't start raid. user=" + mupfc)
//				return false;
//			}
//			if (!validClanInfo(resBuilder, clanId, clanEventId, clanRaidId, curDate, curTime,
//				isFirstStage))
//			{
//				return false;
//			}
//		}
//		if (setMonsterTeamForRaid && ((null === userMonsterUuids) || userMonsterUuids.empty))
//		{
//			resBuilder.status = BeginClanRaidStatus.FAIL_NO_MONSTERS_SENT
//			LOG.error('client did not send any monster ids to set for clan raid.')
//			return false;
//		}
//		return true
//	}
//
//	private def getAuthorizedUsers(int clanId)
//	{
//		val authorizedUsers = new HashSet<Integer>()
//		val statuses = new ArrayList<String>()
//		statuses.add(UserClanStatus.LEADER.name)
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
//		statuses.add(UserClanStatus.CAPTAIN.name)
//		val userUuids = RetrieveUtils::userClanRetrieveUtils.
//			getUserUuidsWithStatuses(clanId, statuses)
//		if ((null !== userUuids) && !userUuids.empty)
//		{
//			authorizedUsers.addAll(userUuids)
//		}
//		return authorizedUsers
//	}
//
//	private def validClanInfo(Builder resBuilder, int clanId, int clanEventId, int clanRaidId,
//		Date curDate, Timestamp now, boolean isFirstStage)
//	{
//		val raidStartedByClan = ClanEventPersistentForClanRetrieveUtils::
//			getPersistentEventForClanId(clanId)
//		if ((null === raidStartedByClan) && isFirstStage)
//		{
//			return true;
//		}
//		else if ((null === raidStartedByClan) && !isFirstStage)
//		{
//			LOG.error(
//				'clan has not started a raid/event (nothing in clan_event_persistent_for_clan)' +
//					' but client claims clan started one. clanId=' + clanId + '	 clanEventId=' +
//					clanEventId + '	 clanRaidId=' + clanRaidId + '	 isFirstStage=' +
//					isFirstStage)
//			return false;
//		}
//		val ceId = raidStartedByClan.clanEventPersistentId
//		val crId = raidStartedByClan.crId
//		if (((ceId !== clanEventId) || (crId !== clanRaidId)) ||
//			((null !== raidStartedByClan) && isFirstStage))
//		{
//			LOG.warn(
//				'possibly encountered clan raid data that should have been pushed to' +
//					' history. pushing now. clanEvent=' + raidStartedByClan + '	 clanEventId=' +
//					clanEventId + '	 clanRaidId=' + clanRaidId + '	 isFirstStage=' +
//					isFirstStage)
//			pushCurrentClanEventDataToHistory(clanId, now, raidStartedByClan)
//			return true;
//		}
//		if (null !== raidStartedByClan.stageStartTime)
//		{
//			LOG.warn(
//				'the clan raid stage start time is not null when beginning clan raid.' +
//					' clanEvent=' + raidStartedByClan)
//			return false;
//		}
//		LOG.info('valid clan info, can begin raid.')
//		return true
//	}
//
//	private def pushCurrentClanEventDataToHistory(int clanId, Timestamp now,
//		ClanEventPersistentForClan clanEvent)
//	{
//		val clanEventPersistentId = clanEvent.clanEventPersistentId
//		val crId = clanEvent.crId
//		val crsId = clanEvent.crsId
//		var Timestamp stageStartTime = null
//		if (null !== clanEvent.stageStartTime)
//		{
//			stageStartTime = new Timestamp(clanEvent.stageStartTime.time)
//		}
//		val crsmId = clanEvent.crsmId
//		var Timestamp stageMonsterStartTime = null
//		if (null !== clanEvent.stageMonsterStartTime)
//		{
//			stageMonsterStartTime = new Timestamp(clanEvent.stageMonsterStartTime.time)
//		}
//		val won = false
//		var numInserted = InsertUtils::get.
//			insertIntoClanEventPersistentForClanHistory(clanId, now, clanEventPersistentId, crId,
//				crsId, stageStartTime, crsmId, stageMonsterStartTime, won)
//		LOG.info('rows inserted into clan raid info for clan (should be 1): ' + numInserted)
//		val clanUserInfo = ClanEventPersistentForUserRetrieveUtils::
//			getPersistentEventUserInfoForClanId(clanId)
//		DeleteUtils::get.deleteClanEventPersistentForClan(clanId)
//		if ((null !== clanUserInfo) && !clanUserInfo.empty)
//		{
//			numInserted = InsertUtils::get.insertIntoCepfuRaidHistory(clanEventPersistentId, now,
//				clanUserInfo)
//			LOG.info(
//				'rows inserted into clan raid info for user (should be ' + clanUserInfo.size +
//					'): ' + numInserted)
//			val userIdList = new ArrayList<Integer>(clanUserInfo.keySet)
//			return DeleteUtils::get.deleteClanEventPersistentForUsers(userIdList)
//		}
//	}
//
//	private def writeChangesToDB(String userUuid, int clanId, int clanEventPersistentId,
//		int clanRaidId, Timestamp curTime, boolean setMonsterTeamForRaid,
//		List<Long> userMonsterUuids, boolean isFirstStage,
//		List<ClanEventPersistentForClan> clanInfo)
//	{
//		if (setMonsterTeamForRaid)
//		{
//			val numInserted = InsertUtils::get.
//				insertIntoUpdateMonstersClanEventPersistentForUser(userUuid, clanId, clanRaidId,
//					userMonsterUuids)
//			LOG.info('num rows inserted into clan raid info for user table: ' + numInserted)
//		}
//		else if (isFirstStage)
//		{
//			val crs = ClanRaidStageRetrieveUtils::getFirstStageForClanRaid(clanRaidId)
//			val clanRaidStageId = crs.id
//			val stageIdToMonster = ClanRaidStageMonsterRetrieveUtils::
//				getClanRaidStageMonstersForClanRaidStageId(clanRaidStageId)
//			val crsm = stageIdToMonster.get(clanRaidStageId)
//			val crsmId = crsm.id
//			val numInserted = InsertUtils::get.
//				insertIntoClanEventPersistentForClan(clanId, clanEventPersistentId, clanRaidId,
//					clanRaidStageId, curTime, crsmId, curTime)
//			LOG.info('num rows inserted into clan raid info for clan table: ' + numInserted)
//			val cepfc = new ClanEventPersistentForClan(clanId, clanEventPersistentId, clanRaidId,
//				clanRaidStageId, curTime, crsmId, curTime)
//			clanInfo.add(cepfc)
//			clanEventUtil.updateClanIdCrsmDmg(clanId, 0, true)
//		}
//		else
//		{
//			LOG.info('starting another clan raid stage!!!!!!!!!')
//			val numUpdated = UpdateUtils::get.
//				updateClanEventPersistentForClanStageStartTime(clanId, curTime)
//			LOG.info('num rows updated in clan raid info for clan table: ' + numUpdated)
//			val cepfc = ClanEventPersistentForClanRetrieveUtils::
//				getPersistentEventForClanId(clanId)
//			clanInfo.add(cepfc)
//		}
//		return true
//	}
//
//	private def setClanAndUserDetails(Builder resBuilder, String userUuid, int clanId,
//		int clanRaidId, List<Long> userMonsterUuids, List<FullUserMonsterProto> userMonsters)
//	{
//		var userMonsterIdOne = 0
//		var userMonsterIdTwo = 0
//		var userMonsterIdThree = 0
//		if (userMonsterUuids.size >= 1)
//		{
//			userMonsterIdOne = userMonsterUuids.get(0)
//		}
//		if (userMonsterUuids.size >= 2)
//		{
//			userMonsterIdTwo = userMonsterUuids.get(1)
//		}
//		if (userMonsterUuids.size >= 3)
//		{
//			userMonsterIdThree = userMonsterUuids.get(2)
//		}
//		val cepfu = new ClanEventPersistentForUser(userUuid, clanId, clanRaidId, 0, 0, 0, 0, 0,
//			userMonsterIdOne, userMonsterIdTwo, userMonsterIdThree)
//		val userDetails = CreateInfoProtoUtils::
//			createPersistentClanEventUserInfoProto(cepfu, null, userMonsters)
//		return resBuilder.userDetails = userDetails
//	}
//
//	def TimeUtils setTimeUtils(TimeUtils timeUtils)
//	{
//		return this.timeUtils = timeUtils
//	}
//
//	def ClanEventUtil setClanEventUtil(ClanEventUtil clanEventUtil)
//	{
//		return this.clanEventUtil = clanEventUtil
//	}
//}
