package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto
import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto.ApproveOrRejectRequestToJoinClanStatus
import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto.Builder
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.ApproveOrRejectRequestToJoinClanRequestEvent
import com.lvl6.mobsters.events.response.ApproveOrRejectRequestToJoinClanResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.util.ArrayList
import java.util.Collections
import java.util.HashSet
import java.util.List
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class ApproveOrRejectRequestToJoinClanController extends EventController
{
	static val Logger LOG = LoggerFactory::getLogger(
		typeof(ApproveOrRejectRequestToJoinClanController))

	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		return new ApproveOrRejectRequestToJoinClanRequestEvent()
	}

	override getEventType()
	{
		return EventProtocolRequest.C_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as ApproveOrRejectRequestToJoinClanRequestEvent)).
			approveOrRejectRequestToJoinClanRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val requesterId = reqProto.requesterUuid
		val accept = reqProto.accept
		val resBuilder = ApproveOrRejectRequestToJoinClanResponseProto::newBuilder
		resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.FAIL_OTHER
		resBuilder.sender = senderProto
		resBuilder.accept = accept
		var clanId = 0
		if (senderProto.clan && (null !== senderProto.clan))
		{
			clanId = senderProto.clan.clanId
		}
		var lockedClan = false
		if (0 !== clanId)
		{
			lockedClan = locker.lockClan(clanId)
		}
		try
		{
			val user = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			val requester = RetrieveUtils::userRetrieveUtils.getUserById(requesterId)
			val clanSizeList = new ArrayList<Integer>()
			val legitDecision = checkLegitDecision(resBuilder, lockedClan, user, requester,
				accept, clanSizeList)
			var success = false
			if (legitDecision)
			{
				success = writeChangesToDB(user, requester, accept)
			}
			if (success)
			{
				resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.SUCCESS
				setResponseBuilderStuff(resBuilder, clanId, clanSizeList)
				val requestMup = CreateInfoProtoUtils::createMinimumUserProtoFromUser(requester)
				resBuilder.requester = requestMup
			}
			val resEvent = new ApproveOrRejectRequestToJoinClanResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.approveOrRejectRequestToJoinClanResponseProto = resBuilder.build
			if (!success)
			{
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in ApproveOrRejectRequestToJoinClanController.processRequestEvent',
						e)
				}
			}
			else
			{
				server.writeClanEvent(resEvent, clanId)
				val resEvent2 = new ApproveOrRejectRequestToJoinClanResponseEvent(requesterId)
				resEvent2.approveOrRejectRequestToJoinClanResponseProto = resBuilder.build
				server.writeAPNSNotificationOrEvent(resEvent2)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in ApproveOrRejectRequestToJoinClan processEvent', e)
			try
			{
				resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.FAIL_OTHER
				val resEvent = new ApproveOrRejectRequestToJoinClanResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.approveOrRejectRequestToJoinClanResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in ApproveOrRejectRequestToJoinClanController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in ApproveOrRejectRequestToJoinClan processEvent', e)
			}
		}
		finally
		{
			if ((0 !== clanId) && lockedClan)
			{
				locker.unlockClan(clanId)
			}
		}
	}

	private def checkLegitDecision(Builder resBuilder, boolean lockedClan, User user,
		User requester, boolean accept, List<Integer> clanSizeList)
	{
		if (!lockedClan)
		{
			LOG.error('could not get lock for clan.')
			return false;
		}
		if ((user === null) || (requester === null))
		{
			resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.FAIL_OTHER
			LOG.error('user is ' + user + ', requester is ' + requester)
			return false;
		}
		val clanId = user.clanId
		val statuses = new ArrayList<String>()
		statuses.add(UserClanStatus.LEADER.name)
		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
		val userUuids = RetrieveUtils::userClanRetrieveUtils.
			getUserUuidsWithStatuses(clanId, statuses)
		val uniqUserUuids = new HashSet<Integer>()
		if ((null !== userUuids) && !userUuids.empty)
		{
			uniqUserUuids.addAll(userUuids)
		}
		val userUuid = user.id
		if (!uniqUserUuids.contains(userUuid))
		{
			resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.FAIL_NOT_AUTHORIZED
			LOG.error(
				"clan member can't approve clan join request. member=" + user + '	 requester=' +
					requester)
			return false;
		}
		if (0 < requester.clanId)
		{
			resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.FAIL_ALREADY_IN_A_CLAN
			LOG.error('trying to accept a user that is already in a clan')
			return false;
		}
		val uc = RetrieveUtils::userClanRetrieveUtils.getSpecificUserClan(requester.id, clanId)
		if ((uc === null) || UserClanStatus.REQUESTING.name != uc.status)
		{
			resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.FAIL_NOT_A_REQUESTER
			LOG.error('requester has not requested for clan with id ' + user.clanId)
			return false;
		}
		if ((ControllerConstants.CLAN__ALLIANCE_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT === clanId) ||
			(ControllerConstants.CLAN__LEGION_CLAN_ID_THAT_IS_EXCEPTION_TO_LIMIT === clanId))
		{
			return true;
		}
		val clanIdList = Collections::singletonList(clanId)
		statuses.add(UserClanStatus.CAPTAIN.name)
		statuses.add(UserClanStatus.MEMBER.name)
		val clanIdToSize = RetrieveUtils::userClanRetrieveUtils.
			getClanSizeForClanUuidsAndStatuses(clanIdList, statuses)
		val size = clanIdToSize.get(clanId)
		val maxSize = ControllerConstants.CLAN__MAX_NUM_MEMBERS
		if ((size >= maxSize) && accept)
		{
			resBuilder.status = ApproveOrRejectRequestToJoinClanStatus.FAIL_CLAN_IS_FULL
			LOG.warn(
				'user error: trying to add user into already full clan with id ' + user.clanId)
			return false;
		}
		clanSizeList.add(size)
		return true
	}

	private def writeChangesToDB(User user, User requester, boolean accept)
	{
		if (accept)
		{
			if (!requester.updateRelativeCoinsAbsoluteClan(0, user.clanId))
			{
				LOG.error(
					'problem with change requester ' + requester + ' clan id to ' + user.clanId)
				return false;
			}
			if (!UpdateUtils::get.updateUserClanStatus(requester.id, user.clanId,
				UserClanStatus.MEMBER))
			{
				LOG.error(
					'problem with updating user clan status to member for requester ' +
						requester + ' and clan id ' + user.clanId)
				return false;
			}
			DeleteUtils::get.deleteUserClansForUserExceptSpecificClan(requester.id, user.clanId)
			return true;
		}
		else
		{
			if (!DeleteUtils::get.deleteUserClan(requester.id, user.clanId))
			{
				LOG.error(
					'problem with deleting user clan info for requester with id ' + requester.id +
						' and clan id ' + user.clanId)
				return false;
			}
			return true;
		}
	}

	private def setResponseBuilderStuff(Builder resBuilder, int clanId,
		List<Integer> clanSizeList)
	{
		val clan = ClanRetrieveUtils::getClanWithId(clanId)
		resBuilder.minClan = CreateInfoProtoUtils::createMinimumClanProtoFromClan(clan)
		val size = clanSizeList.get(0)
		return resBuilder.fullClan = CreateInfoProtoUtils::
			createFullClanProtoWithClanSize(clan, size)
	}
}
