//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto
//import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto.BootPlayerFromClanStatus
//import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto.Builder
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.BootPlayerFromClanRequestEvent
//import com.lvl6.mobsters.events.response.BootPlayerFromClanResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
//import com.lvl6.mobsters.server.EventController
//import java.util.ArrayList
//import java.util.HashSet
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class BootPlayerFromClanController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(BootPlayerFromClanController))
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
//		new BootPlayerFromClanRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_BOOT_PLAYER_FROM_CLAN_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as BootPlayerFromClanRequestEvent)).
//			bootPlayerFromClanRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val playerToBootId = reqProto.playerToBoot
//		val userUuids = new ArrayList<Integer>()
//		userUuids.add(userUuid)
//		userUuids.add(playerToBootId)
//		val resBuilder = BootPlayerFromClanResponseProto::newBuilder
//		resBuilder.status = BootPlayerFromClanStatus.FAIL_OTHER
//		resBuilder.sender = senderProto
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
//			val user = users.get(userUuid)
//			val playerToBoot = users.get(playerToBootId)
//			val legitBoot = checkLegitBoot(resBuilder, lockedClan, user, playerToBoot)
//			var success = false
//			if (legitBoot)
//			{
//				success = writeChangesToDB(user, playerToBoot)
//			}
//			if (success)
//			{
//				val mup = CreateInfoProtoUtils::createMinimumUserProtoFromUser(playerToBoot)
//				resBuilder.playerToBoot = mup
//			}
//			val resEvent = new BootPlayerFromClanResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.bootPlayerFromClanResponseProto = resBuilder.build
//			if (success)
//			{
//				server.writeClanEvent(resEvent, clanId)
//			}
//			else
//			{
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in BootPlayerFromClanController.processRequestEvent', e)
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in BootPlayerFromClan processEvent', e)
//			try
//			{
//				resBuilder.status = BootPlayerFromClanStatus.FAIL_OTHER
//				val resEvent = new BootPlayerFromClanResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.bootPlayerFromClanResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in BootPlayerFromClanController.processRequestEvent', e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in BootPlayerFromClan processEvent', e)
//			}
//		}
//		finally
//		{
//			if ((0 !== clanId) && lockedClan)
//			{
//				locker.unlockClan(clanId)
//			}
//		}
//	}
//
//	private def checkLegitBoot(Builder resBuilder, boolean lockedClan, User user,
//		User playerToBoot)
//	{
//		if (!lockedClan)
//		{
//			LOG.error("couldn't obtain clan lock")
//			return false;
//		}
//		if ((user === null) || (playerToBoot === null))
//		{
//			resBuilder.status = BootPlayerFromClanStatus.FAIL_OTHER
//			LOG.error('user is ' + user + ', playerToBoot is ' + playerToBoot)
//			return false;
//		}
//		val clanId = user.clanId
//		val statuses = new ArrayList<String>()
//		statuses.add(UserClanStatus.LEADER.name)
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
//		val userUuids = RetrieveUtils::userClanRetrieveUtils.
//			getUserUuidsWithStatuses(clanId, statuses)
//		val uniqUserUuids = new HashSet<Integer>()
//		if ((null !== userUuids) && !userUuids.empty)
//		{
//			uniqUserUuids.addAll(userUuids)
//		}
//		val userUuid = user.id
//		if (!uniqUserUuids.contains(userUuid))
//		{
//			resBuilder.status = BootPlayerFromClanStatus.FAIL_NOT_AUTHORIZED
//			LOG.error("user can't boot player. user=" + user + '	 playerToBoot=' + playerToBoot)
//			return false;
//		}
//		if (playerToBoot.clanId !== user.clanId)
//		{
//			resBuilder.status = BootPlayerFromClanStatus.FAIL_BOOTED_NOT_IN_CLAN
//			LOG.error(
//				'playerToBoot is not in user clan. playerToBoot is in ' + playerToBoot.clanId)
//			return false;
//		}
//		resBuilder.status = BootPlayerFromClanStatus.SUCCESS
//		true
//	}
//
//	private def writeChangesToDB(User user, User playerToBoot)
//	{
//		if (!DeleteUtils::get.deleteUserClan(playerToBoot.id, playerToBoot.clanId))
//		{
//			LOG.error(
//				'problem with deleting user clan info for playerToBoot with id ' +
//					playerToBoot.id + ' and clan id ' + playerToBoot.clanId)
//		}
//		if (!playerToBoot.updateRelativeCoinsAbsoluteClan(0, null))
//		{
//			LOG.error('problem with change playerToBoot ' + playerToBoot + ' clan id to nothing')
//		}
//		true
//	}
//}
