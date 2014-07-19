package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.ClanForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto
import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto.TransferClanOwnershipStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.TransferClanOwnershipRequestEvent
import com.lvl6.mobsters.events.response.TransferClanOwnershipResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
import com.lvl6.mobsters.server.EventController
import java.util.ArrayList
import java.util.Collections
import java.util.List
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class TransferClanOwnershipController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(TransferClanOwnershipController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 2
	}

	override createRequestEvent()
	{
		new TransferClanOwnershipRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_TRANSFER_CLAN_OWNERSHIP
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as TransferClanOwnershipRequestEvent)).
			transferClanOwnershipRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val newClanOwnerId = reqProto.clanOwnerUuidNew
		val userUuids = new ArrayList<Integer>()
		userUuids.add(userUuid)
		userUuids.add(newClanOwnerId)
		val resBuilder = TransferClanOwnershipResponseProto::newBuilder
		resBuilder.status = TransferClanOwnershipStatus.FAIL_OTHER
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
			val users = RetrieveUtils::userRetrieveUtils.getUsersByUuids(userUuids)
			val userClans = RetrieveUtils::userClanRetrieveUtils.
				getUserClanForUsers(clanId, userUuids)
			val user = users.get(userUuid)
			val newClanOwner = users.get(newClanOwnerId)
			val legitTransfer = checkLegitTransfer(resBuilder, lockedClan, userUuid, user,
				newClanOwnerId, newClanOwner, userClans)
			if (legitTransfer)
			{
				val statuses = new ArrayList<UserClanStatus>()
				statuses.add(UserClanStatus::JUNIOR_LEADER)
				statuses.add(UserClanStatus::LEADER)
				writeChangesToDB(clanId, userUuids, statuses)
				setResponseBuilderStuff(resBuilder, clanId, newClanOwner)
			}
			if (!legitTransfer)
			{
				val resEvent = new TransferClanOwnershipResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.transferClanOwnershipResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in TransferClanOwnershipController.processRequestEvent',
						e)
				}
			}
			if (legitTransfer)
			{
				val resEvent = new TransferClanOwnershipResponseEvent(senderProto.userUuid)
				resEvent.tag = event.tag
				resEvent.transferClanOwnershipResponseProto = resBuilder.build
				server.writeClanEvent(resEvent, clanId)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in TransferClanOwnership processEvent', e)
			try
			{
				resBuilder.status = TransferClanOwnershipStatus.FAIL_OTHER
				val resEvent = new TransferClanOwnershipResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.transferClanOwnershipResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in TransferClanOwnershipController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in TransferClanOwnership processEvent', e)
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

	private def checkLegitTransfer(Builder resBuilder, boolean lockedClan, String userUuid,
		User user, int newClanOwnerId, User newClanOwner, Map<Integer, ClanForUser> userClans)
	{
		if (!lockedClan)
		{
			LOG.error("couldn't obtain clan lock")
			return false;
		}
		if ((user === null) || (newClanOwner === null))
		{
			resBuilder.status = TransferClanOwnershipStatus.FAIL_OTHER
			LOG.error('user is ' + user + ', new clan owner is ' + newClanOwner)
			return false;
		}
		if (user.clanId <= 0)
		{
			resBuilder.status = TransferClanOwnershipStatus.FAIL_NOT_AUTHORIZED
			LOG.error('user not in clan. user=' + user)
			return false;
		}
		if (newClanOwner.clanId !== user.clanId)
		{
			resBuilder.status = TransferClanOwnershipStatus.FAIL_NEW_OWNER_NOT_IN_CLAN
			LOG.error(
				'new owner not in same clan as user. new owner= ' + newClanOwner + ', user is ' +
					user)
			return false;
		}
		if (!userClans.containsKey(userUuid) || !userClans.containsKey(newClanOwnerId))
		{
			LOG.error(
				'a UserClan does not exist userUuid=' + userUuid + ', newClanOwner=' +
					newClanOwnerId + '	 userClans=' + userClans)
		}
		val userClan = userClans.get(user.id)
		if (UserClanStatus::LEADER != userClan.status)
		{
			resBuilder.status = TransferClanOwnershipStatus.FAIL_NOT_AUTHORIZED
			LOG.error('user is ' + user + ", and user isn't owner. user is:" + userClan)
			return false;
		}
		resBuilder.status = TransferClanOwnershipStatus.SUCCESS
		true
	}

	private def writeChangesToDB(int clanId, List<Integer> userIdList,
		List<UserClanStatus> statuses)
	{
		val numUpdated = UpdateUtils::get.updateUserClanStatuses(clanId, userIdList, statuses)
		LOG.info(
			'num clan_for_user updated=' + numUpdated + ' userIdList=' + userIdList +
				' statuses=' + statuses)
	}

	private def setResponseBuilderStuff(Builder resBuilder, int clanId, User newClanOwner)
	{
		val clan = ClanRetrieveUtils::getClanWithId(clanId)
		val clanIdList = Collections::singletonList(clanId)
		val statuses = new ArrayList<String>()
		statuses.add(UserClanStatus.LEADER.name)
		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
		statuses.add(UserClanStatus.CAPTAIN.name)
		statuses.add(UserClanStatus.MEMBER.name)
		val clanIdToSize = RetrieveUtils::userClanRetrieveUtils.
			getClanSizeForClanUuidsAndStatuses(clanIdList, statuses)
		resBuilder.minClan = CreateInfoProtoUtils::createMinimumClanProtoFromClan(clan)
		val size = clanIdToSize.get(clanId)
		resBuilder.fullClan = CreateInfoProtoUtils::createFullClanProtoWithClanSize(clan, size)
		val mup = CreateInfoProtoUtils::createMinimumUserProtoFromUser(newClanOwner)
		resBuilder.clanOwnerNew = mup
	}
}
