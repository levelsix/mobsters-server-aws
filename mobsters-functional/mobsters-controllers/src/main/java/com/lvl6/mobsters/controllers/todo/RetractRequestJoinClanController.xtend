package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.Clan
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanResponseProto
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanResponseProto.RetractRequestJoinClanStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RetractRequestJoinClanRequestEvent
import com.lvl6.mobsters.events.response.RetractRequestJoinClanResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
import com.lvl6.mobsters.server.EventController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class RetractRequestJoinClanController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(RetractRequestJoinClanController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new RetractRequestJoinClanRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_RETRACT_REQUEST_JOIN_CLAN_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as RetractRequestJoinClanRequestEvent)).
			retractRequestJoinClanRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val clanId = reqProto.clanUuid
		val resBuilder = RetractRequestJoinClanResponseProto::newBuilder
		resBuilder.status = RetractRequestJoinClanStatus.FAIL_OTHER
		resBuilder.sender = senderProto
		resBuilder.clanId = clanId
		var lockedClan = false
		if (0 !== clanId)
		{
			lockedClan = locker.lockClan(clanId)
		}
		try
		{
			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
			val clan = ClanRetrieveUtils::getClanWithId(clanId)
			val legitRetract = checkLegitRequest(resBuilder, lockedClan, user, clan)
			var success = false
			if (legitRetract)
			{
				success = writeChangesToDB(user, clanId)
			}
			if (success)
			{
				resBuilder.status = RetractRequestJoinClanStatus.SUCCESS
			}
			val resEvent = new RetractRequestJoinClanResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.retractRequestJoinClanResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in RetractRequestJoinClanController.processRequestEvent', e)
			}
			if (success)
			{
				server.writeClanEvent(resEvent, clan.id)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in RetractRequestJoinClan processEvent', e)
			try
			{
				resBuilder.status = RetractRequestJoinClanStatus.FAIL_OTHER
				val resEvent = new RetractRequestJoinClanResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.retractRequestJoinClanResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in RetractRequestJoinClanController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in RetractRequestJoinClan processEvent', e)
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

	private def checkLegitRequest(Builder resBuilder, boolean lockedClan, User user, Clan clan)
	{
		if (!lockedClan)
		{
			LOG.error("couldn't obtain clan lock")
			return false;
		}
		if ((user === null) || (clan === null))
		{
			resBuilder.status = RetractRequestJoinClanStatus.FAIL_OTHER
			LOG.error('user is ' + user + ', clan is ' + clan)
			return false;
		}
		if (user.clanId > 0)
		{
			resBuilder.status = RetractRequestJoinClanStatus.FAIL_ALREADY_IN_CLAN
			LOG.error('user is already in clan with id ' + user.clanId)
			return false;
		}
		val uc = RetrieveUtils::userClanRetrieveUtils.getSpecificUserClan(user.id, clan.id)
		if ((uc === null) || UserClanStatus.REQUESTING.name != uc.status)
		{
			resBuilder.status = RetractRequestJoinClanStatus.FAIL_DID_NOT_REQUEST
			LOG.error('user clan request has not been filed')
			return false;
		}
		true
	}

	private def writeChangesToDB(User user, int clanId)
	{
		if (!DeleteUtils::get.deleteUserClan(user.id, clanId))
		{
			LOG.error(
				'problem with deleting user clan data for user ' + user + ', and clan id ' +
					clanId)
			return false;
		}
		true
	}
}
