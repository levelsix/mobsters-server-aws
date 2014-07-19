package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.Clan
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanResponseProto
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanResponseProto.LeaveClanStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.LeaveClanRequestEvent
import com.lvl6.mobsters.events.response.LeaveClanResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
import com.lvl6.mobsters.server.EventController
import java.util.ArrayList
import java.util.Collections
import java.util.List
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class LeaveClanController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(LeaveClanController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new LeaveClanRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_LEAVE_CLAN_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as LeaveClanRequestEvent)).leaveClanRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val resBuilder = LeaveClanResponseProto::newBuilder
		resBuilder.status = LeaveClanStatus.FAIL_OTHER
		resBuilder.sender = senderProto
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
			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
			val clan = ClanRetrieveUtils::getClanWithId(clanId)
			val clanOwnerIdList = new ArrayList<Integer>()
			val legitLeave = checkLegitLeave(resBuilder, lockedClan, user, clan, clanOwnerIdList)
			var success = false
			if (legitLeave)
			{
				val clanOwnerId = clanOwnerIdList.get(0)
				success = writeChangesToDB(user, clan, clanOwnerId)
			}
			val resEvent = new LeaveClanResponseEvent(userUuid)
			resEvent.tag = event.tag
			if (!success)
			{
				resEvent.leaveClanResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in LeaveClanController.processRequestEvent', e)
				}
			}
			else
			{
				resBuilder.status = LeaveClanStatus.SUCCESS
				resEvent.leaveClanResponseProto = resBuilder.build
				server.writeClanEvent(resEvent, clanId)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in LeaveClan processEvent', e)
			try
			{
				resBuilder.status = LeaveClanStatus.FAIL_OTHER
				val resEvent = new LeaveClanResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.leaveClanResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in LeaveClanController.processRequestEvent', e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in LeaveClan processEvent', e)
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

	private def checkLegitLeave(Builder resBuilder, boolean lockedClan, User user, Clan clan,
		List<Integer> clanOwnerIdList)
	{
		if (!lockedClan)
		{
			LOG.error("couldn't obtain clan lock")
			return false;
		}
		if ((user === null) || (clan === null))
		{
			LOG.error('user is null')
			return false;
		}
		if (user.clanId !== clan.id)
		{
			resBuilder.status = LeaveClanStatus.FAIL_NOT_IN_CLAN
			LOG.error("user's clan id is " + user.clanId + ', clan id is ' + clan.id)
			return false;
		}
		val clanId = user.clanId
		val statuses = new ArrayList<String>()
		statuses.add(UserClanStatus.LEADER.name)
		val userUuids = RetrieveUtils::userClanRetrieveUtils.
			getUserUuidsWithStatuses(clanId, statuses)
		var clanOwnerId = 0
		if ((null !== userUuids) && !userUuids.empty)
		{
			clanOwnerId = userUuids.get(0)
		}
		if (clanOwnerId === user.id)
		{
			val clanIdList = Collections::singletonList(clanId)
			statuses.add(UserClanStatus.JUNIOR_LEADER.name)
			statuses.add(UserClanStatus.CAPTAIN.name)
			statuses.add(UserClanStatus.MEMBER.name)
			val clanIdToSize = RetrieveUtils::userClanRetrieveUtils.
				getClanSizeForClanUuidsAndStatuses(clanIdList, statuses)
			val userClanMembersInClan = clanIdToSize.get(clanId)
			if (userClanMembersInClan > 1)
			{
				resBuilder.status = LeaveClanStatus.FAIL_OWNER_OF_CLAN_WITH_OTHERS_STILL_IN
				LOG.error(
					"user is owner and he's not alone in clan, can't leave without switching ownership. user clan members are " +
						userClanMembersInClan)
				return false;
			}
		}
		clanOwnerIdList.add(clanOwnerId)
		true
	}

	private def writeChangesToDB(User user, Clan clan, int clanOwnerId)
	{
		val userUuid = user.id
		val clanId = clan.id
		if (userUuid === clanOwnerId)
		{
			val userUuids = RetrieveUtils::userClanRetrieveUtils.
				getUserUuidsRelatedToClan(clanId)
			deleteClan(clan, userUuids, user)
		}
		else
		{
			if (!DeleteUtils::get.deleteUserClan(userUuid, clanId))
			{
				LOG.error('problem with deleting user clan for ' + user + ' and clan ' + clan)
			}
			if (!user.updateRelativeCoinsAbsoluteClan(0, null))
			{
				LOG.error('problem with making clanid for user null')
			}
		}
		true
	}

	private def deleteClan(Clan clan, List<Integer> userUuids, User user)
	{
		if (!user.updateRelativeCoinsAbsoluteClan(0, null))
		{
			LOG.error('problem with marking clan id null for users with ids in ' + userUuids)
		}
		else
		{
			if (!DeleteUtils::get.deleteUserClanDataRelatedToClanId(clan.id, userUuids.size))
			{
				LOG.error('problem with deleting user clan data for clan with id ' + clan.id)
			}
			else
			{
				if (!DeleteUtils::get.deleteClanWithClanId(clan.id))
				{
					LOG.error('problem with deleting clan with id ' + clan.id)
				}
			}
		}
	}
}
