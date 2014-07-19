//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser
//import com.lvl6.mobsters.dynamo.ClanEventPersistentUserReward
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterResponseProto
//import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterResponseProto.AttackClanRaidMonsterStatus
//import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventClanProto.AwardClanRaidStageRewardResponseProto
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.AttackClanRaidMonsterRequestEvent
//import com.lvl6.mobsters.events.response.AttackClanRaidMonsterResponseEvent
//import com.lvl6.mobsters.events.response.AwardClanRaidStageRewardResponseEvent
//import com.lvl6.mobsters.info.ClanRaidStage
//import com.lvl6.mobsters.info.ClanRaidStageMonster
//import com.lvl6.mobsters.info.ClanRaidStageReward
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventClanInfoProto
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserCurrentMonsterTeamProto
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
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
//class AttackClanRaidMonsterController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(AttackClanRaidMonsterController))
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
//		return new AttackClanRaidMonsterRequestEvent()
//	}
//
//	override getEventType()
//	{
//		return EventProtocolRequest.C_ATTACK_CLAN_RAID_MONSTER_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as AttackClanRaidMonsterRequestEvent)).
//			attackClanRaidMonsterRequestProto
//		LOG.info('reqProto=')
//		LOG.info(reqProto + '')
//		val sender = reqProto.sender
//		val userUuid = sender.userUuid
//		val mcp = sender.clan
//		var clanId = 0
//		val eventDetails = reqProto.eventDetails
//		val curDate = new Date(reqProto.clientTime)
//		val curTime = new Timestamp(curDate.time)
//		val damageDealt = reqProto.damageDealt
//		val monsterHealthProtos = reqProto.monsterHealthsList
//		val userMonsterIdToExpectedHealth = new HashMap<Long, Integer>()
//		MonsterStuffUtils::getUserMonsterUuids(monsterHealthProtos,
//			userMonsterIdToExpectedHealth)
//		val userMonsterThatAttacked = reqProto.userMonsterThatAttacked
//		val userMonsterTeam = reqProto.userMonsterTeam
//		val resBuilder = AttackClanRaidMonsterResponseProto::newBuilder
//		resBuilder.status = AttackClanRaidMonsterStatus.FAIL_OTHER
//		resBuilder.sender = sender
//		resBuilder.userMonsterThatAttacked = userMonsterThatAttacked
//		resBuilder.dmgDealt = damageDealt
//		var ClanEventPersistentForClan clanEvent = null
//		val userIdToCepfu = new HashMap<Integer, ClanEventPersistentForUser>()
//		val clanEventList = new ArrayList<ClanEventPersistentForClan>()
//		var lockedClan = false
//		if ((null !== mcp) && mcp.clanId)
//		{
//			clanId = mcp.clanId
//			lockedClan = locker.lockClan(clanId)
//		}
//		try
//		{
//			val legitRequest = checkLegitRequest(resBuilder, lockedClan, sender, userUuid,
//				clanId, eventDetails, curDate, clanEventList)
//			val allRewards = new ArrayList<ClanEventPersistentUserReward>()
//			if (legitRequest)
//			{
//				LOG.info('legitRequest')
//				if (!clanEventList.empty)
//				{
//					clanEvent = clanEventList.get(0)
//				}
//				val clanEventClientSent = ClanStuffUtils::
//					createClanEventPersistentForClan(eventDetails)
//				writeChangesToDB(resBuilder, clanId, userUuid, damageDealt, curTime, clanEvent,
//					clanEventClientSent, userMonsterTeam, userMonsterIdToExpectedHealth,
//					userIdToCepfu, allRewards)
//			}
//			setClanEventClanDetails(resBuilder, clanEventList)
//			setClanEventUserDetails(resBuilder, userIdToCepfu)
//			val resEvent = new AttackClanRaidMonsterResponseEvent(userUuid)
//			resEvent.tag = event.tag
//			resEvent.attackClanRaidMonsterResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in AttackClanRaidMonsterController.processRequestEvent', e)
//			}
//			if (AttackClanRaidMonsterStatus::SUCCESS == resBuilder.status ||
//				AttackClanRaidMonsterStatus::SUCCESS_MONSTER_JUST_DIED == resBuilder.status)
//			{
//				server.writeClanEvent(resEvent, clanId)
//				if (!allRewards.empty)
//				{
//					setClanEventRewards(allRewards, eventDetails)
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in AttackClanRaidMonster processEvent', e)
//			try
//			{
//				resBuilder.status = AttackClanRaidMonsterStatus.FAIL_OTHER
//				val resEvent = new AttackClanRaidMonsterResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.attackClanRaidMonsterResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in AttackClanRaidMonsterController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in AttackClanRaidMonster processEvent', e)
//			}
//		}
//		finally
//		{
//			if ((null !== mcp) && mcp.clanId && lockedClan)
//			{
//				locker.unlockClan(clanId)
//			}
//		}
//	}
//
//	private def checkLegitRequest(Builder resBuilder, boolean lockedClan, MinimumUserProto mup,
//		String userUuid, int clanId, PersistentClanEventClanInfoProto eventDetails,
//		Date curDate, List<ClanEventPersistentForClan> clanEventList)
//	{
//		if (!lockedClan)
//		{
//			LOG.error("couldn't obtain clan lock")
//			return false;
//		}
//		val uc = RetrieveUtils::userClanRetrieveUtils.getSpecificUserClan(userUuid, clanId)
//		if (null === uc)
//		{
//			resBuilder.status = AttackClanRaidMonsterStatus.FAIL_USER_NOT_IN_CLAN
//			LOG.error('not in clan. user=' + mup)
//			return false;
//		}
//		if (null === eventDetails)
//		{
//			LOG.error('no PersistentClanEventClanInfoProto set by client.')
//			return false;
//		}
//		val raidStartedByClan = ClanEventPersistentForClanRetrieveUtils::
//			getPersistentEventForClanId(clanId)
//		val eventClientSent = ClanStuffUtils::createClanEventPersistentForClan(eventDetails)
//		if ((null !== raidStartedByClan) && raidStartedByClan == eventClientSent)
//		{
//			clanEventList.add(raidStartedByClan)
//			resBuilder.status = AttackClanRaidMonsterStatus.SUCCESS
//			LOG.info(
//				'since data client sent matches up with db info, allowing attack, clanEvent=' +
//					raidStartedByClan)
//			return true;
//		}
//		return true
//	}
//
//	private def writeChangesToDB(Builder resBuilder, int clanId, String userUuid,
//		int damageDealt, Timestamp curTime, ClanEventPersistentForClan clanEvent,
//		ClanEventPersistentForClan clanEventClientSent, UserCurrentMonsterTeamProto ucmtp,
//		Map<Long, Integer> userMonsterIdToExpectedHealth,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu,
//		List<ClanEventPersistentUserReward> allRewards)
//	throws Exception {
//		LOG.info('clanEventInDb=' + clanEvent)
//		LOG.info('clanEventClientSent=' + clanEventClientSent)
//		if ((null !== clanEvent) && clanEvent == clanEventClientSent &&
//			(null !== clanEvent.stageStartTime) && (null !== clanEvent.stageMonsterStartTime))
//		{
//			updateClanRaid(resBuilder, userUuid, clanId, damageDealt, curTime, clanEvent,
//				clanEventClientSent, ucmtp, userIdToCepfu, allRewards)
//		}
//		else if ((null !== clanEvent) && clanEvent == clanEventClientSent &&
//			(null === clanEvent.stageStartTime))
//		{
//			LOG.warn(
//				'possibly remnants of old requests to attack clan raid stage monster. ' +
//					' raidInDb=' + clanEvent + '	 clanEventClientSent=' + clanEventClientSent)
//			resBuilder.status = AttackClanRaidMonsterStatus.FAIL_MONSTER_ALREADY_DEAD
//		}
//		else if (null === clanEvent)
//		{
//			LOG.warn(
//				'possibly remnants of old requests to attack last clan raid stage monster.' +
//					' raidInDb=' + clanEvent + '	 clanEventClientSent=' + clanEventClientSent)
//			resBuilder.status = AttackClanRaidMonsterStatus.FAIL_NO_STAGE_RAID_IN_PROGRESS
//		}
//		val numUpdated = UpdateUtils::get.updateUserMonstersHealth(userMonsterIdToExpectedHealth)
//		LOG.info('num monster healths updated:' + numUpdated)
//		return true
//	}
//
//	private def updateClanRaid(Builder resBuilder, String userUuid, int clanId, int dmgDealt,
//		Timestamp curTime, ClanEventPersistentForClan clanEvent,
//		ClanEventPersistentForClan clanEventClientSent, UserCurrentMonsterTeamProto ucmtp,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu,
//		List<ClanEventPersistentUserReward> allRewards)
//	throws Exception {
//		LOG.info('updating clan raid')
//		val curCrId = clanEvent.crId
//		val curCrsId = clanEvent.crsId
//		val curCrsmId = clanEvent.crsmId
//		val newDmgList = new ArrayList<Integer>()
//		var newDmg = dmgDealt
//		val monsterDied = checkMonsterDead(clanId, curCrsId, curCrsmId, dmgDealt, userIdToCepfu,
//			newDmgList)
//		newDmg = newDmgList.get(0)
//		LOG.info('actual dmg dealt=' + newDmg)
//		if (monsterDied)
//		{
//			getAllClanUserDmgInfo(userUuid, clanId, newDmg, userIdToCepfu)
//		}
//		val nextStage = ClanRaidStageRetrieveUtils::
//			getNextStageForClanRaidStageId(curCrsId, curCrId)
//		val curStageNextCrsm = ClanRaidStageMonsterRetrieveUtils::
//			getNextMonsterForClanRaidStageMonsterId(curCrsmId, curCrsId)
//		if ((null === nextStage) && monsterDied)
//		{
//			LOG.info('user killed the monster and ended the raid!')
//			recordClanRaidVictory(clanId, clanEvent, curTime, userIdToCepfu)
//			val rewards = awardRewards(curCrsId, clanEventClientSent.stageStartTime, curTime,
//				clanEventClientSent.clanEventPersistentId, userIdToCepfu)
//			allRewards.addAll(rewards)
//		}
//		else if ((null === curStageNextCrsm) && monsterDied)
//		{
//			LOG.info('user killed the monster and ended the stage!')
//			recordClanRaidStageVictory(clanId, curCrsId, nextStage, curTime, clanEvent,
//				userIdToCepfu)
//			val rewards = awardRewards(curCrsId, clanEventClientSent.stageStartTime, curTime,
//				clanEventClientSent.clanEventPersistentId, userIdToCepfu)
//			allRewards.addAll(rewards)
//		}
//		else if (monsterDied)
//		{
//			LOG.info('user killed the monster!')
//			recordClanRaidStageMonsterVictory(userUuid, clanId, curCrsId, curTime, newDmg,
//				curStageNextCrsm, clanEvent, userIdToCepfu)
//		}
//		else if (!monsterDied)
//		{
//			LOG.info('user did not deal killing blow.')
//			val numUpdated = UpdateUtils::get.
//				updateClanEventPersistentForUserCrsmDmgDone(userUuid, newDmg, curCrsId,
//					curCrsmId)
//			LOG.info('rows updated when user attacked monster. num=' + numUpdated)
//			val cepfu = ClanEventPersistentForUserRetrieveUtils::
//				getPersistentEventUserInfoForUserUuidClanId(userUuid, clanId)
//			val pceuip = CreateInfoProtoUtils::
//				createPersistentClanEventUserInfoProto(cepfu, null, ucmtp.currentTeamList)
//			resBuilder.addClanUsersDetails(pceuip)
//			val replaceCrsmDmg = false
//			clanEventUtil.updateClanIdCrsmDmg(clanId, newDmg, replaceCrsmDmg)
//		}
//		if (monsterDied && (0 !== newDmg))
//		{
//			resBuilder.status = AttackClanRaidMonsterStatus.SUCCESS_MONSTER_JUST_DIED
//		}
//		else if (monsterDied && (0 === newDmg))
//		{
//			LOG.error(
//				'not really error since will continue processing. should not be here. ' +
//					'what has been processed same as this user killing last monster in the raid')
//			resBuilder.status = AttackClanRaidMonsterStatus.FAIL_MONSTER_ALREADY_DEAD
//		}
//		else if (!monsterDied)
//		{
//			resBuilder.status = AttackClanRaidMonsterStatus.SUCCESS
//		}
//		return resBuilder.dmgDealt = newDmg
//	}
//
//	private def checkMonsterDead(int clanId, int crsId, int crsmId, int dmgDealt,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu, List<Integer> newDmgList)
//	{
//		val crsm = ClanRaidStageMonsterRetrieveUtils::
//			getClanRaidStageMonsterForClanRaidStageMonsterId(crsmId)
//		val dmgSoFar = getCrsmDmgSoFar(clanId, crsId, crsmId, userIdToCepfu)
//		val crsmHp = crsm.monsterHp
//		LOG.info('dmgSoFar=' + dmgSoFar)
//		LOG.info("monster's health=" + crsmHp)
//		LOG.info('dmgDealt=' + dmgDealt)
//		var newDmgDealt = dmgDealt
//		var monsterDied = false
//		if ((dmgSoFar + dmgDealt) >= crsmHp)
//		{
//			LOG.info(
//				'monster just died! dmgSoFar=' + dmgSoFar + '	 dmgDealt=' + dmgDealt +
//					'	 monster=' + crsm)
//			monsterDied = true
//			newDmgDealt = Math::min(dmgDealt, crsmHp - dmgSoFar)
//			newDmgDealt = Math::max(0, newDmgDealt)
//		}
//		if (dmgSoFar >= crsmHp)
//		{
//			LOG.error(
//				"(won't error out) client sent an attack and server didn't update the" +
//					' ClanEventPersistentForClan when the monster was just killed by a previous.' +
//					' AttackClanRaidMonster event')
//			newDmgDealt = 0
//		}
//		newDmgList.add(newDmgDealt)
//		LOG.info('newDmg=' + newDmgDealt)
//		LOG.info('monsterDied=' + monsterDied)
//		LOG.info('newUserIdToCepfu=' + userIdToCepfu)
//		return monsterDied
//	}
//
//	private def getCrsmDmgSoFar(int clanId, int crsId, int crsmId,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	{
//		var dmgSoFar = clanEventUtil.getCrsmDmgForClanId(clanId)
//		if (0 === dmgSoFar)
//		{
//			val newUserIdToCepfu = ClanEventPersistentForUserRetrieveUtils::
//				getPersistentEventUserInfoForClanId(clanId)
//			setCrsCrsmId(crsId, crsmId, newUserIdToCepfu)
//			dmgSoFar = sumDamageDoneToMonster(newUserIdToCepfu)
//			userIdToCepfu.putAll(newUserIdToCepfu)
//		}
//		return dmgSoFar
//	}
//
//	private def setCrsCrsmId(int crsId, int crsmId,
//		Map<Integer, ClanEventPersistentForUser> newUserIdToCepfu)
//	{
//		for (cepfu : newUserIdToCepfu.values)
//		{
//			val cepfuCrsId = cepfu.crsId
//			if (0 === cepfuCrsId)
//			{
//				cepfu.crsId = crsId
//			}
//			val cepfuCrsmId = cepfu.crsmId
//			if (0 === cepfuCrsmId)
//			{
//				cepfu.crsmId = crsmId
//			}
//		}
//	}
//
//	private def sumDamageDoneToMonster(Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	{
//		var dmgTotal = 0
//		LOG.info('printing the users who attacked in this raid')
//		for (cepfu : userIdToCepfu.values)
//		{
//			LOG.info('cepfu=' + cepfu)
//			dmgTotal += cepfu.crsmDmgDone
//		}
//		return dmgTotal
//	}
//
//	private def getAllClanUserDmgInfo(String userUuid, int clanId, int newDmg,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	{
//		if ((null === userIdToCepfu) || userIdToCepfu.empty)
//		{
//			val newUserIdToCepfu = ClanEventPersistentForUserRetrieveUtils::
//				getPersistentEventUserInfoForClanId(clanId)
//			userIdToCepfu.putAll(newUserIdToCepfu)
//		}
//		val cepfu = userIdToCepfu.get(userUuid)
//		val newCrsmDmgDone = cepfu.crsmDmgDone + newDmg
//		cepfu.crsmDmgDone = newCrsmDmgDone
//	}
//
//	private def recordClanRaidVictory(int clanId, ClanEventPersistentForClan clanEvent,
//		Timestamp now, Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	{
//		val clanEventId = clanEvent.clanEventPersistentId
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
//		val won = true
//		var numInserted = InsertUtils::get.
//			insertIntoClanEventPersistentForClanHistory(clanId, now, clanEventId, crId, crsId,
//				stageStartTime, crsmId, stageMonsterStartTime, won)
//		LOG.info(
//			'rows inserted into clan raid info for clan history (should be 1): ' + numInserted)
//		DeleteUtils::get.deleteClanEventPersistentForClan(clanId)
//		if ((null !== userIdToCepfu) && !userIdToCepfu.empty)
//		{
//			numInserted = InsertUtils::get.insertIntoCepfuRaidHistory(clanEventId, now,
//				userIdToCepfu)
//			LOG.info(
//				'rows inserted into clan raid info for user history (should be ' +
//					userIdToCepfu.size + '): ' + numInserted)
//			val userIdList = new ArrayList<Integer>(userIdToCepfu.keySet)
//			DeleteUtils::get.deleteClanEventPersistentForUsers(userIdList)
//			val stageHp = ClanRaidStageRetrieveUtils::getClanRaidStageHealthForCrsId(crsId)
//			numInserted = InsertUtils::get.
//				insertIntoCepfuRaidStageHistory(clanEventId, stageStartTime, now, stageHp,
//					userIdToCepfu)
//			LOG.info(
//				'clan event persistent for user raid stage history, numInserted=' + numInserted)
//		}
//		return clanEventUtil.deleteCrsmDmgForClanId(clanId)
//	}
//
//	private def recordClanRaidStageVictory(int clanId, int curCrsId, ClanRaidStage nextStage,
//		Timestamp curTime, ClanEventPersistentForClan cepfc,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	throws Exception {
//		val eventId = cepfc.clanEventPersistentId
//		val stageStartTime = new Timestamp(cepfc.stageStartTime.time)
//		val nextCrsId = nextStage.id
//		val nextCrsFirstCrsm = ClanRaidStageMonsterRetrieveUtils::
//			getFirstMonsterForClanRaidStage(nextCrsId)
//		if (null === nextCrsFirstCrsm)
//		{
//			throw new Exception('WTF!!!! clan raid stage has no monsters! >:( crs=' + nextStage);
//		}
//		val nextCrsCrsmId = nextCrsFirstCrsm.id
//		var numUpdated = UpdateUtils::get.
//			updateClanEventPersistentForClanGoToNextStage(clanId, nextCrsId, nextCrsCrsmId)
//		LOG.info(
//			'clan just cleared stage! nextStage=' + nextStage + '	 curEventInfo' + cepfc +
//				'	 numUpdated=' + numUpdated)
//		numUpdated = UpdateUtils::get.
//			updateClanEventPersistentForUserGoToNextStage(nextCrsId, nextCrsCrsmId,
//				userIdToCepfu)
//		LOG.info('rows updated when clan cleared stage. num=' + numUpdated)
//		cepfc.crsId = nextCrsId
//		cepfc.stageStartTime = null
//		cepfc.crsmId = nextCrsCrsmId
//		cepfc.stageMonsterStartTime = null
//		val newCrsmHp = 0
//		val replaceCrsmDmg = true
//		clanEventUtil.updateClanIdCrsmDmg(clanId, newCrsmHp, replaceCrsmDmg)
//		val stageHp = ClanRaidStageRetrieveUtils::getClanRaidStageHealthForCrsId(curCrsId)
//		val numInserted = InsertUtils::get.
//			insertIntoCepfuRaidStageHistory(eventId, stageStartTime, curTime, stageHp,
//				userIdToCepfu)
//		LOG.info('clan event persistent for user raid stage history, numInserted=' + numInserted)
//	}
//
//	private def recordClanRaidStageMonsterVictory(String userUuid, int clanId, int crsId,
//		Timestamp curTime, int newDmg, ClanRaidStageMonster nextCrsm,
//		ClanEventPersistentForClan cepfc,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	{
//		val nextCrsmId = nextCrsm.id
//		var numUpdated = UpdateUtils::get.
//			updateClanEventPersistentForClanGoToNextMonster(clanId, nextCrsmId, curTime)
//		LOG.info('rows updated when clan killed monster. num=' + numUpdated)
//		numUpdated = UpdateUtils::get.
//			updateClanEventPersistentForUsersGoToNextMonster(crsId, nextCrsmId, userIdToCepfu)
//		LOG.info('rows updated when user killed monster. num=' + numUpdated)
//		cepfc.crsmId = nextCrsmId
//		cepfc.stageMonsterStartTime = curTime
//		val newCrsmHp = 0
//		val replaceCrsmDmg = true
//		return clanEventUtil.updateClanIdCrsmDmg(clanId, newCrsmHp, replaceCrsmDmg)
//	}
//
//	private def awardRewards(int crsId, Date crsStartDate, Date crsEndDate, int clanEventId,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	{
//		val stageHp = ClanRaidStageRetrieveUtils::getClanRaidStageHealthForCrsId(crsId)
//		val crsStartTime = new Timestamp(crsStartDate.time)
//		val crsEndTime = new Timestamp(crsEndDate.time)
//		val allRewards = new ArrayList<ClanEventPersistentUserReward>()
//		val rewardUuidsToRewards = ClanRaidStageRewardRetrieveUtils::
//			getClanRaidStageRewardsForClanRaidStageId(crsId)
//		for (userUuid : userIdToCepfu.keySet)
//		{
//			val cepfu = userIdToCepfu.get(userUuid)
//			generateAllRewardsForUser(crsId, crsStartDate, crsEndDate, clanEventId, cepfu,
//				stageHp, rewardUuidsToRewards, allRewards)
//		}
//		val ids = InsertUtils::get.insertIntoCepUserReward(crsStartTime, crsId, crsEndTime,
//			clanEventId, allRewards)
//		LOG.info('num clan event user rewards inserted:' + ids.size)
//		for (i : 0 ..< ids.size)
//		{
//			val id = ids.get(i)
//			val reward = allRewards.get(i)
//			reward.id = id
//		}
//		return allRewards
//	}
//
//	private def generateAllRewardsForUser(int crsId, Date crsStartDate, Date crsEndDate,
//		int clanEventId, ClanEventPersistentForUser cepfu, int stageHp,
//		Map<Integer, ClanRaidStageReward> rewardUuidsToRewards,
//		List<ClanEventPersistentUserReward> allRewards)
//	{
//		for (reward : rewardUuidsToRewards.values)
//		{
//			val someRewards = generateSomeRewardsForUser(crsId, crsStartDate, crsEndDate,
//				clanEventId, cepfu, stageHp, reward)
//			allRewards.addAll(someRewards)
//		}
//	}
//
//	private def generateSomeRewardsForUser(int crsId, Date crsStartDate, Date crsEndDate,
//		int clanEventId, ClanEventPersistentForUser cepfu, int stageHp,
//		ClanRaidStageReward reward)
//	{
//		val userRewards = new ArrayList<ClanEventPersistentUserReward>()
//		val userUuid = cepfu.userUuid
//		val userCrsDmg = cepfu.crsDmgDone + cepfu.crsmDmgDone
//		val userCrsContribution = (userCrsDmg) / (stageHp)
//		var staticDataId = 0
//		val userOilReward = ((((reward.oilDrop as float)) * userCrsContribution) as int)
//		createClanEventPersistentUserReward(MiscMethods::OIL, userOilReward, staticDataId, crsId,
//			crsStartDate, crsEndDate, clanEventId, userUuid, userRewards)
//		val userCashReward = ((((reward.cashDrop as float)) * userCrsContribution) as int)
//		createClanEventPersistentUserReward(MiscMethods::CASH, userCashReward, staticDataId,
//			crsId, crsStartDate, crsEndDate, clanEventId, userUuid, userRewards)
//		val monsterId = reward.monsterId
//		if (0 >= monsterId)
//		{
//			return userRewards;
//		}
//		val monsterDropRate = userCrsContribution *
//			((reward.expectedMonsterRewardQuantity as float))
//		val rand = reward.rand
//		if (rand.nextFloat < monsterDropRate)
//		{
//			staticDataId = monsterId
//			val quantity = 1
//			createClanEventPersistentUserReward(MiscMethods::MONSTER, staticDataId, quantity,
//				crsId, crsStartDate, crsEndDate, clanEventId, userUuid, userRewards)
//		}
//		return userRewards
//	}
//
//	private def createClanEventPersistentUserReward(String resourceType, int staticDataId,
//		int quantity, int crsId, Date crsStartDate, Date crsEndDate, int clanEventId,
//		String userUuid, List<ClanEventPersistentUserReward> userRewards)
//	{
//		if (quantity <= 0)
//		{
//			return;
//		}
//		val cepur = new ClanEventPersistentUserReward(0, userUuid, crsStartDate, crsId,
//			crsEndDate, resourceType, staticDataId, quantity, clanEventId, null)
//		userRewards.add(cepur)
//	}
//
//	private def setClanEventClanDetails(Builder resBuilder,
//		List<ClanEventPersistentForClan> clanEventList)
//	{
//		if (!clanEventList.empty)
//		{
//			val cepfc = clanEventList.get(0)
//			if (null !== cepfc)
//			{
//				val updatedEventDetails = CreateInfoProtoUtils::
//					createPersistentClanEventClanInfoProto(cepfc)
//				return resBuilder.eventDetails = updatedEventDetails
//			}
//		}
//	}
//
//	private def setClanEventUserDetails(Builder resBuilder,
//		Map<Integer, ClanEventPersistentForUser> userIdToCepfu)
//	{
//		if (!userIdToCepfu.empty)
//		{
//			val userMonsterUuids = MonsterStuffUtils::
//				getUserMonsterUuidsInClanRaid(userIdToCepfu)
//			val idsToUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
//				getSpecificUserMonsters(userMonsterUuids)
//			for (cepfu : userIdToCepfu.values)
//			{
//				val pceuip = CreateInfoProtoUtils::
//					createPersistentClanEventUserInfoProto(cepfu, idsToUserMonsters, null)
//				resBuilder.addClanUsersDetails(pceuip)
//			}
//		}
//	}
//
//	private def setClanEventRewards(List<ClanEventPersistentUserReward> allRewards,
//		PersistentClanEventClanInfoProto eventDetails)
//	{
//		if (null === allRewards)
//		{
//			return;
//		}
//		val clanId = eventDetails.clanId
//		val crsId = eventDetails.clanRaidStageId
//		val resBuilder = AwardClanRaidStageRewardResponseProto::newBuilder
//		resBuilder.crsId = crsId
//		for (reward : allRewards)
//		{
//			val rewardProto = CreateInfoProtoUtils::
//				createPersistentClanEventUserRewardProto(reward)
//			resBuilder.addAllRewards(rewardProto)
//		}
//		val resEvent = new AwardClanRaidStageRewardResponseEvent(clanId)
//		resEvent.tag = 0
//		resEvent.awardClanRaidStageRewardResponseProto = resBuilder.build
//		server.writeClanEvent(resEvent, clanId)
//	}
//
//	def getTimeUtils()
//	{
//		return timeUtils
//	}
//
//	def setTimeUtils(TimeUtils timeUtils)
//	{
//		return this.timeUtils = timeUtils
//	}
//
//	def getClanEventUtil()
//	{
//		return clanEventUtil
//	}
//
//	def setClanEventUtil(ClanEventUtil clanEventUtil)
//	{
//		return this.clanEventUtil = clanEventUtil
//	}
//}
