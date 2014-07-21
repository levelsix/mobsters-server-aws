//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.ClanForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberResponseProto
//import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberResponseProto.PromoteDemoteClanMemberStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.PromoteDemoteClanMemberRequestEvent
//import com.lvl6.mobsters.events.response.PromoteDemoteClanMemberResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
//import com.lvl6.mobsters.server.EventController
//import java.util.ArrayList
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class PromoteDemoteClanMemberController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(PromoteDemoteClanMemberController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 4
//	}
//
//	override createRequestEvent()
//	{
//		new PromoteDemoteClanMemberRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_PROMOTE_DEMOTE_CLAN_MEMBER_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as PromoteDemoteClanMemberRequestEvent)).
//			promoteDemoteClanMemberRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val victimId = reqProto.victimUuid
//		val newUserClanStatus = reqProto.userClanStatus
//		val userUuids = new ArrayList<Integer>()
//		userUuids.add(userUuid)
//		userUuids.add(victimId)
//		val resBuilder = PromoteDemoteClanMemberResponseProto::newBuilder
//		resBuilder.status = PromoteDemoteClanMemberStatus.FAIL_OTHER
//		resBuilder.sender = senderProto
//		resBuilder.userClanStatus = newUserClanStatus
//		var clanId = 0
//		if (senderProto.clan && (null !== senderProto.clan))
//		{
//			clanId = senderProto.clan.clanId
//		}
//		var lockedClan = false
//		if (0 !== clanId)
//		{
//			lockedClan = locker.lockClan(clanId)
//		}
//		try
//		{
//			val users = RetrieveUtils::userRetrieveUtils.getUsersByUuids(userUuids)
//			val userClans = RetrieveUtils::userClanRetrieveUtils.
//				getUserClanForUsers(clanId, userUuids)
//			val legitRequest = checkLegitRequest(resBuilder, lockedClan, userUuid, victimId,
//				newUserClanStatus, users, userClans)
//			var success = false
//			if (legitRequest)
//			{
//				val victim = users.get(victimId)
//				val oldInfo = userClans.get(victimId)
//				try
//				{
//					val ucs = UserClanStatus::valueOf(oldInfo.status)
//					resBuilder.prevUserClanStatus = ucs
//				}
//				catch (Exception e)
//				{
//					LOG.error('incorrect user clan status. userClan=' + oldInfo)
//				}
//				success = writeChangesToDB(victim, victimId, clanId, oldInfo, newUserClanStatus)
//			}
//			val resEvent = new PromoteDemoteClanMemberResponseEvent(userUuid)
//			resEvent.tag = event.tag
//			if (!success)
//			{
//				resEvent.promoteDemoteClanMemberResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in PromoteDemoteClanMemberController.processRequestEvent',
//						e)
//				}
//			}
//			else
//			{
//				resBuilder.status = PromoteDemoteClanMemberStatus.SUCCESS
//				val victim = users.get(victimId)
//				val mup = CreateInfoProtoUtils::createMinimumUserProtoFromUser(victim)
//				resBuilder.victim = mup
//				resEvent.promoteDemoteClanMemberResponseProto = resBuilder.build
//				server.writeClanEvent(resEvent, clanId)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in PromoteDemoteClanMember processEvent', e)
//			try
//			{
//				resBuilder.status = PromoteDemoteClanMemberStatus.FAIL_OTHER
//				val resEvent = new PromoteDemoteClanMemberResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.promoteDemoteClanMemberResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in PromoteDemoteClanMemberController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in PromoteDemoteClanMember processEvent', e)
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
//	private def checkLegitRequest(Builder resBuilder, boolean lockedClan, String userUuid,
//		int victimId, UserClanStatus newUserClanStatus, Map<Integer, User> userUuidsToUsers,
//		Map<Integer, ClanForUser> userUuidsToUserClans)
//	{
//		if (!lockedClan)
//		{
//			LOG.error("couldn't obtain clan lock")
//			return false;
//		}
//		if ((null === userUuidsToUsers) || (userUuidsToUsers.size !== 2) ||
//			(null === userUuidsToUserClans) || (userUuidsToUserClans.size !== 2))
//		{
//			LOG.error(
//				'user or userClan objects do not total 2. users=' + userUuidsToUsers +
//					'	 userUuidsToUserClans=' + userUuidsToUserClans)
//			return false;
//		}
//		if (!userUuidsToUserClans.containsKey(userUuid) ||
//			!userUuidsToUsers.containsKey(userUuid))
//		{
//			LOG.error(
//				'user promoting or demoting not in clan or db. userUuid=' + userUuid +
//					'	 userUuidsToUserClans=' + userUuidsToUserClans + '	 userUuidsToUsers=' +
//					userUuidsToUsers)
//			resBuilder.status = PromoteDemoteClanMemberStatus.FAIL_NOT_IN_CLAN
//			return false;
//		}
//		if (!userUuidsToUserClans.containsKey(victimId) ||
//			!userUuidsToUsers.containsKey(victimId))
//		{
//			LOG.error(
//				'user to be promoted or demoted not in clan or db. victim=' + victimId +
//					'	 userUuidsToUserClans=' + userUuidsToUserClans + '	 userUuidsToUsers=' +
//					userUuidsToUsers)
//			resBuilder.status = PromoteDemoteClanMemberStatus.FAIL_NOT_IN_CLAN
//			return false;
//		}
//		val promoterDemoter = userUuidsToUserClans.get(userUuid)
//		val victim = userUuidsToUserClans.get(victimId)
//		val first = UserClanStatus::valueOf(promoterDemoter.status)
//		val second = UserClanStatus::valueOf(victim.status)
//		if (UserClanStatus::CAPTAIN == first ||
//			!ClanStuffUtils::firstUserClanStatusAboveSecond(first, second))
//		{
//			LOG.error(
//				'user not authorized to promote or demote otherUser. clanStatus of user=' +
//					first + '	 clanStatus of other user=' + second)
//			resBuilder.status = PromoteDemoteClanMemberStatus.FAIL_NOT_AUTHORIZED
//			return false;
//		}
//		if (!ClanStuffUtils::firstUserClanStatusAboveSecond(first, newUserClanStatus))
//		{
//			LOG.error(
//				'user not authorized to promote or demote otherUser. clanStatus of user=' +
//					first + '	 clanStatus of other user=' + second +
//					'	 newClanStatus of other user=' + newUserClanStatus)
//			resBuilder.status = PromoteDemoteClanMemberStatus.FAIL_NOT_AUTHORIZED
//			return false;
//		}
//		if (UserClanStatus::REQUESTING == second)
//		{
//			LOG.error(
//				"user can't promote, demote a non-clan member. UserClan for user=" +
//					promoterDemoter + '	 UserClan for victim=' + victim + '	 users=' +
//					userUuidsToUsers)
//			resBuilder.status = PromoteDemoteClanMemberStatus.FAIL_NOT_AUTHORIZED
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDB(User victim, int victimId, int clanId, ClanForUser oldInfo,
//		UserClanStatus newUserClanStatus)
//	{
//		if (!UpdateUtils::get.updateUserClanStatus(victimId, clanId, newUserClanStatus))
//		{
//			LOG.error(
//				'problem with updating user clan status for user=' + victim + '	 oldInfo=' +
//					oldInfo + '	 newUserClanStatus=' + newUserClanStatus)
//			return false;
//		}
//		true
//	}
//}
