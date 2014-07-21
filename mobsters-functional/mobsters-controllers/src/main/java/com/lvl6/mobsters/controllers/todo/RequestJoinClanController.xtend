//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.Clan
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanResponseProto
//import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanResponseProto.RequestJoinClanStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.RequestJoinClanRequestEvent
//import com.lvl6.mobsters.events.response.RequestJoinClanResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Collections
//import java.util.Date
//import java.util.List
//import javax.management.Notification
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class RequestJoinClanController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(RequestJoinClanController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil
//
//	new()
//	{
//		numAllocatedThreads = 4
//	}
//
//	override createRequestEvent()
//	{
//		new RequestJoinClanRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_REQUEST_JOIN_CLAN_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as RequestJoinClanRequestEvent)).requestJoinClanRequestProto
//		val senderProto = reqProto.sender
//		val clanId = reqProto.clanUuid
//		val userUuid = senderProto.userUuid
//		val resBuilder = RequestJoinClanResponseProto::newBuilder
//		resBuilder.status = RequestJoinClanStatus.FAIL_OTHER
//		resBuilder.sender = senderProto
//		resBuilder.clanId = clanId
//		var lockedClan = false
//		if (0 !== clanId)
//		{
//			lockedClan = locker.lockClan(clanId)
//		}
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
//			val clan = ClanRetrieveUtils::getClanWithId(clanId)
//			var requestToJoinRequired = false
//			val clanSizeList = new ArrayList<Integer>()
//			val legitRequest = checkLegitRequest(resBuilder, lockedClan, user, clan,
//				clanSizeList)
//			var successful = false
//			if (legitRequest)
//			{
//				requestToJoinRequired = clan.requestToJoinRequired
//				val battlesWon = pvpLeagueForUserRetrieveUtil.getPvpBattlesWonForUser(userUuid)
//				if (requestToJoinRequired)
//				{
//					val mupfc = CreateInfoProtoUtils::
//						createMinimumUserProtoForClans(user, UserClanStatus::REQUESTING, 0F,
//							battlesWon)
//					resBuilder.requester = mupfc
//				}
//				else
//				{
//					val mupfc = CreateInfoProtoUtils::
//						createMinimumUserProtoForClans(user, UserClanStatus::MEMBER, 0F,
//							battlesWon)
//					resBuilder.requester = mupfc
//				}
//				successful = writeChangesToDB(resBuilder, user, clan)
//			}
//			if (successful)
//			{
//				setResponseBuilderStuff(resBuilder, clan, clanSizeList)
//				sendClanRaidStuff(resBuilder, clan, user)
//			}
//			val resEvent = new RequestJoinClanResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.requestJoinClanResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in RequestJoinClanController.processRequestEvent', e)
//			}
//			if (successful)
//			{
//				val userUuids = new ArrayList<Integer>()
//				userUuids.add(userUuid)
//				val userIdToTeam = RetrieveUtils::monsterForUserRetrieveUtils.
//					getUserUuidsToMonsterTeamForUserUuids(userUuids)
//				val curTeamProto = CreateInfoProtoUtils::
//					createUserCurrentMonsterTeamProto(userUuid, userIdToTeam.get(userUuid))
//				resBuilder.requesterMonsters = curTeamProto
//				resBuilder.clearEventDetails
//				resBuilder.clearClanUsersDetails
//				resEvent.requestJoinClanResponseProto = resBuilder.build
//				server.writeClanEvent(resEvent, clan.id)
//				val resEventUpdate = MiscMethods::
//					createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null)
//				resEventUpdate.tag = event.tag
//				LOG.info('Writing event: ' + resEventUpdate)
//				try
//				{
//					eventWriter.writeEvent(resEventUpdate)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in RequestJoinClanController.processRequestEvent',
//						e)
//				}
//				notifyClan(user, clan, requestToJoinRequired)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in RequestJoinClan processEvent', e)
//			try
//			{
//				resBuilder.status = RequestJoinClanStatus.FAIL_OTHER
//				val resEvent = new RequestJoinClanResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.requestJoinClanResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in RequestJoinClanController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in RequestJoinClan processEvent', e)
//			}
//		}
//		finally
//		{
//			if (0 !== clanId)
//			{
//				locker.unlockClan(clanId)
//			}
//		}
//	}
//
//	private def checkLegitRequest(Builder resBuilder, boolean lockedClan, User user, Clan clan,
//		List<Integer> clanSizeList)
//	{
//		if (!lockedClan)
//		{
//			LOG.error("couldn't obtain clan lock")
//			return false;
//		}
//		if ((user === null) || (clan === null))
//		{
//			resBuilder.status = RequestJoinClanStatus.FAIL_OTHER
//			LOG.error('user is ' + user + ', clan is ' + clan)
//			return false;
//		}
//		var clanId = user.clanId
//		if (clanId > 0)
//		{
//			resBuilder.status = RequestJoinClanStatus.FAIL_ALREADY_IN_CLAN
//			LOG.error('user is already in clan with id ' + clanId)
//			return false;
//		}
//		clanId = clan.id
//		val uc = RetrieveUtils::userClanRetrieveUtils.getSpecificUserClan(user.id, clanId)
//		if (uc !== null)
//		{
//			resBuilder.status = RequestJoinClanStatus.FAIL_REQUEST_ALREADY_FILED
//			LOG.error('user clan already exists for this: ' + uc)
//			return false;
//		}
//		if ((ControllerConstants::CLAN__ALLIANCE_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT === clanId) ||
//			(ControllerConstants::CLAN__LEGION_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT === clanId))
//		{
//			return true;
//		}
//		if (clan.requestToJoinRequired)
//		{
//			return true;
//		}
//		val clanIdList = Collections::singletonList(clanId)
//		val statuses = new ArrayList<String>()
//		statuses.add(UserClanStatus.LEADER.name)
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
//		statuses.add(UserClanStatus.CAPTAIN.name)
//		statuses.add(UserClanStatus.MEMBER.name)
//		val clanIdToSize = RetrieveUtils::userClanRetrieveUtils.
//			getClanSizeForClanUuidsAndStatuses(clanIdList, statuses)
//		val size = clanIdToSize.get(clanId)
//		val maxSize = ControllerConstants.CLAN__MAX_NUM_MEMBERS
//		if (size >= maxSize)
//		{
//			resBuilder.status = RequestJoinClanStatus.FAIL_CLAN_IS_FULL
//			LOG.warn(
//				'user error: trying to join full clan with id ' + clanId + ' cur size=' +
//					maxSize)
//			return false;
//		}
//		clanSizeList.add(size)
//		true
//	}
//
//	private def writeChangesToDB(Builder resBuilder, User user, Clan clan)
//	{
//		val requestToJoinRequired = clan.requestToJoinRequired
//		val userUuid = user.id
//		val clanId = clan.id
//		var String userClanStatus
//		if (requestToJoinRequired)
//		{
//			userClanStatus = UserClanStatus.REQUESTING.name
//			resBuilder.status = RequestJoinClanStatus.SUCCESS_REQUEST
//		}
//		else
//		{
//			userClanStatus = UserClanStatus.MEMBER.name
//			resBuilder.status = RequestJoinClanStatus.SUCCESS_JOIN
//		}
//		if (!InsertUtils::get.insertUserClan(userUuid, clanId, userClanStatus,
//			new Timestamp(new Date().time)))
//		{
//			LOG.error(
//				'unexpected error: problem with inserting user clan data for user ' + user +
//					', and clan id ' + clanId)
//			resBuilder.status = RequestJoinClanStatus.FAIL_OTHER
//			return false;
//		}
//		var deleteUserClanInserted = false
//		if (!requestToJoinRequired)
//		{
//			if (!user.updateRelativeCoinsAbsoluteClan(0, clanId))
//			{
//				LOG.error(
//					'unexpected error: could not change clan id for requester ' + user + ' to ' +
//						clanId + '. Deleting user clan that was just created.')
//				deleteUserClanInserted = true
//			}
//			else
//			{
//				DeleteUtils::get.deleteUserClansForUserExceptSpecificClan(userUuid, clanId)
//			}
//		}
//		var successful = true
//		if (deleteUserClanInserted)
//		{
//			if (!DeleteUtils::get.deleteUserClan(userUuid, clanId))
//			{
//				LOG.error('unexpected error: could not delete user clan inserted.')
//			}
//			resBuilder.status = RequestJoinClanStatus.FAIL_OTHER
//			successful = false
//		}
//		successful
//	}
//
//	private def notifyClan(User aUser, Clan aClan, boolean requestToJoinRequired)
//	{
//		val clanId = aUser.clanId
//		val statuses = Collections::singletonList(UserClanStatus.LEADER.name)
//		val userUuids = RetrieveUtils::userClanRetrieveUtils.
//			getUserUuidsWithStatuses(clanId, statuses)
//		var clanOwnerId = 0
//		if ((null !== userUuids) && !userUuids.empty)
//		{
//			clanOwnerId = userUuids.get(0)
//		}
//		val level = aUser.level
//		val requester = aUser.name
//		val aNote = new Notification()
//		if (requestToJoinRequired)
//		{
//			aNote.setAsUserRequestedToJoinClan(level, requester)
//		}
//		else
//		{
//			aNote.setAsUserJoinedClan(level, requester)
//		}
//		MiscMethods::writeNotificationToUser(aNote, server, clanOwnerId)
//	}
//
//	private def setResponseBuilderStuff(Builder resBuilder, Clan clan,
//		List<Integer> clanSizeList)
//	{
//		resBuilder.minClan = CreateInfoProtoUtils::createMinimumClanProtoFromClan(clan)
//		val size = clanSizeList.get(0)
//		resBuilder.fullClan = CreateInfoProtoUtils::createFullClanProtoWithClanSize(clan, size)
//	}
//
//	private def sendClanRaidStuff(Builder resBuilder, Clan clan, User user)
//	{
//		if (RequestJoinClanStatus::SUCCESS_JOIN != resBuilder.status)
//		{
//			return;
//		}
//		val clanId = clan.id
//		val cepfc = ClanEventPersistentForClanRetrieveUtils::getPersistentEventForClanId(clanId)
//		if (null !== cepfc)
//		{
//			val updatedEventDetails = CreateInfoProtoUtils::
//				createPersistentClanEventClanInfoProto(cepfc)
//			resBuilder.eventDetails = updatedEventDetails
//		}
//		val userIdToCepfu = ClanEventPersistentForUserRetrieveUtils::
//			getPersistentEventUserInfoForClanId(clanId)
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
