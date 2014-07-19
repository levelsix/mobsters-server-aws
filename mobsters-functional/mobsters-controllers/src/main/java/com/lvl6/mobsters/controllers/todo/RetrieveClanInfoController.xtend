package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.CepfuRaidHistory
import com.lvl6.mobsters.dynamo.Clan
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoRequestProto.ClanInfoGrabType
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoResponseProto
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoResponseProto.RetrieveClanInfoStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RetrieveClanInfoRequestEvent
import com.lvl6.mobsters.events.response.RetrieveClanInfoResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserCurrentMonsterTeamProto
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.common.TimeUtils
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
class RetrieveClanInfoController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(RetrieveClanInfoController))
	@Autowired
	protected var DataServiceTxManager svcTxManager
	@Autowired
	protected var TimeUtils timeUtils
	@Autowired
	protected var HazelcastPvpUtil hazelcastPvpUtil

	new()
	{
		numAllocatedThreads = 8
	}

	override createRequestEvent()
	{
		new RetrieveClanInfoRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_RETRIEVE_CLAN_INFO_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as RetrieveClanInfoRequestEvent)).retrieveClanInfoRequestProto
		val senderProto = reqProto.sender
		val clanId = reqProto.clanUuid
		val clanName = reqProto.clanName
		val beforeClanId = reqProto.beforeThisClanUuid
		val grabType = reqProto.grabType
		val resBuilder = RetrieveClanInfoResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.isForBrowsingList = reqProto.isForBrowsingList
		resBuilder.isForSearch = false
		if (reqProto.clanName)
		{
			resBuilder.clanName = clanName
		}
		if (reqProto.clanId)
		{
			resBuilder.clanId = clanId
		}
		try
		{
			val legitCreate = checkLegitCreate(resBuilder, clanName, clanId)
			if (legitCreate)
			{
				if (reqProto.clanName || reqProto.clanId)
				{
					if ((grabType === ClanInfoGrabType::ALL) ||
						(grabType === ClanInfoGrabType::CLAN_INFO))
					{
						var List<Clan> clanList = null
						if (reqProto.clanName)
						{
							clanList = ClanRetrieveUtils::
								getClansWithSimilarNameOrTag(clanName, clanName)
							resBuilder.isForSearch = true
						}
						else if (reqProto.clanId)
						{
							val clan = ClanRetrieveUtils::getClanWithId(clanId)
							clanList = new ArrayList<Clan>()
							clanList.add(clan)
						}
						setClanProtosWithSize(resBuilder, clanList)
					}
					if ((grabType === ClanInfoGrabType::ALL) ||
						(grabType === ClanInfoGrabType::MEMBERS))
					{
						LOG.info('getUserClansRelatedToClan clanId=' + clanId)
						val userClans = RetrieveUtils::userClanRetrieveUtils.
							getUserClansRelatedToClan(clanId)
						LOG.info('user clans related to clanId:' + clanId + '	 ' + userClans)
						val userUuids = new HashSet<Integer>()
						for (uc : userClans)
						{
							userUuids.add(uc.userUuid)
						}
						val userIdList = new ArrayList<Integer>(userUuids)
						val usersMap = RetrieveUtils::userRetrieveUtils.
							getUsersByUuids(userIdList)
						val userUuidsToMonsterTeams = RetrieveUtils::monsterForUserRetrieveUtils.
							getUserUuidsToMonsterTeamForUserUuids(userIdList)
						val nDays = ControllerConstants::
							CLAN_EVENT_PERSISTENT__NUM_DAYS_FOR_RAID_HISTORY
						val timesToUserIdToRaidHistory = CepfuRaidHistoryRetrieveUtils::
							getRaidHistoryForPastNDaysForClan(clanId, nDays, new Date(),
								timeUtils)
						val userIdToClanRaidContribution = calculateRaidContribution(
							timesToUserIdToRaidHistory)
						for (uc : userClans)
						{
							val userUuid = uc.userUuid
							val u = usersMap.get(userUuid)
							var clanRaidContribution = 0F
							if (userIdToClanRaidContribution.containsKey(userUuid))
							{
								clanRaidContribution = userIdToClanRaidContribution.get(userUuid)
							}
							val battlesWon = getBattlesWonForUser(userUuid)
							val minUser = CreateInfoProtoUtils::
								createMinimumUserProtoForClans(u, uc.status,
									clanRaidContribution, battlesWon)
							resBuilder.addMembers(minUser)
							if (userUuidsToMonsterTeams.containsKey(userUuid))
							{
								val monsterTeam = userUuidsToMonsterTeams.get(userUuid)
								val proto = CreateInfoProtoUtils::
									createFullUserMonsterProtoList(monsterTeam)
								val teamForUser = UserCurrentMonsterTeamProto::newBuilder
								teamForUser.userUuid = userUuid
								teamForUser.addAllCurrentTeam(proto)
								resBuilder.addMonsterTeams(teamForUser.build)
							}
						}
					}
				}
				else
				{
					var List<Clan> clanList = null
					if (beforeClanId <= 0)
					{
						clanList = ClanRetrieveUtils::getMostRecentClans(
							ControllerConstants::RETRIEVE_CLANS__NUM_CLANS_CAP)
					}
					else
					{
						clanList = ClanRetrieveUtils::
							getMostRecentClansBeforeClanId(
								ControllerConstants::RETRIEVE_CLANS__NUM_CLANS_CAP, beforeClanId)
						resBuilder.beforeThisClanId = reqProto.beforeThisClanUuid
					}
					LOG.info('clanList=' + clanList)
					setClanProtosWithSize(resBuilder, clanList)
				}
			}
			val resEvent = new RetrieveClanInfoResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.retrieveClanInfoResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in RetrieveClanInfoController.processRequestEvent', e)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in RetrieveClanInfo processEvent', e)
		}
	}

	private def checkLegitCreate(Builder resBuilder, String clanName, int clanId)
	{
		if (((clanName === null) || (clanName.length !== 0)) && (clanId !== 0))
		{
			resBuilder.status = RetrieveClanInfoStatus.OTHER_FAIL
			LOG.error('clan name and clan id set')
			return false;
		}
		resBuilder.status = RetrieveClanInfoStatus.SUCCESS
		true
	}

	private def setClanProtosWithSize(Builder resBuilder, List<Clan> clanList)
	{
		if ((null === clanList) || clanList.empty)
		{
			return;
		}
		val clanUuids = ClanRetrieveUtils::getClanUuidsFromClans(clanList)
		val statuses = new ArrayList<String>()
		statuses.add(UserClanStatus.LEADER.name)
		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
		statuses.add(UserClanStatus.CAPTAIN.name)
		statuses.add(UserClanStatus.MEMBER.name)
		val clanUuidsToSizes = RetrieveUtils::userClanRetrieveUtils.
			getClanSizeForClanUuidsAndStatuses(clanUuids, statuses)
		for (c : clanList)
		{
			val clanId = c.id
			val size = clanUuidsToSizes.get(clanId)
			resBuilder.addClanInfo(
				CreateInfoProtoUtils::createFullClanProtoWithClanSize(c, size))
		}
	}

	private def calculateRaidContribution(
		Map<Date, Map<Integer, CepfuRaidHistory>> timesToUserIdToRaidHistory)
	{
		LOG.info('calculating clan raid contribution.')
		val userIdToContribution = new HashMap<Integer, Float>()
		val userIdToSumCrDmg = new HashMap<Integer, Integer>()
		var sumClanCrDmg = 0
		for (aDate : timesToUserIdToRaidHistory.keySet)
		{
			val userIdToRaidHistory = timesToUserIdToRaidHistory.get(aDate)
			sumClanCrDmg += getClanAndCrDmgs(userIdToRaidHistory, userIdToSumCrDmg)
		}
		for (userUuid : userIdToSumCrDmg.keySet)
		{
			val userCrDmg = userIdToSumCrDmg.get(userUuid)
			val contribution = ((userCrDmg as float) / (sumClanCrDmg as float))
			userIdToContribution.put(userUuid, contribution)
		}
		LOG.info(
			'total clan cr dmg=' + sumClanCrDmg + '	 userIdToContribution=' +
				userIdToContribution)
		userIdToContribution
	}

	private def getClanAndCrDmgs(Map<Integer, CepfuRaidHistory> userIdToRaidHistory,
		Map<Integer, Integer> userIdToSumCrDmg)
	{
		var clanCrDmg = 0
		for (userUuid : userIdToRaidHistory.keySet)
		{
			val raidHistory = userIdToRaidHistory.get(userUuid)
			clanCrDmg = raidHistory.clanCrDmg
			val userCrDmg = raidHistory.crDmgDone
			var userCrDmgSoFar = 0
			if (userIdToSumCrDmg.containsKey(userUuid))
			{
				userCrDmgSoFar = userIdToSumCrDmg.get(userUuid)
			}
			val totalUserCrDmg = userCrDmg + userCrDmgSoFar
			userIdToSumCrDmg.put(userUuid, totalUserCrDmg)
		}
		clanCrDmg
	}

	private def getBattlesWonForUser(String userUuid)
	{
		val pu = hazelcastPvpUtil.getPvpUser(userUuid)
		var battlesWon = 0
		if (null !== pu)
		{
			battlesWon = pu.battlesWon
		}
		battlesWon
	}

	def getTimeUtils()
	{
		timeUtils
	}

	def setTimeUtils(TimeUtils timeUtils)
	{
		this.timeUtils = timeUtils
	}

	def getHazelcastPvpUtil()
	{
		hazelcastPvpUtil
	}

	def setHazelcastPvpUtil(HazelcastPvpUtil hazelcastPvpUtil)
	{
		this.hazelcastPvpUtil = hazelcastPvpUtil
	}
}
