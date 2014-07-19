//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.SetAvatarMonsterRequestEvent
//import com.lvl6.mobsters.events.response.SetAvatarMonsterResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.EventController
//import org.slf4j.LoggerFactory
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class SetAvatarMonsterController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(SetAvatarMonsterController))
//
//	new()
//	{
//		numAllocatedThreads = 1
//	}
//
//	override createRequestEvent()
//	{
//		new SetAvatarMonsterRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_SET_AVATAR_MONSTER_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as SetAvatarMonsterRequestEvent)).setAvatarMonsterRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val monsterId = reqProto.monsterUuid
//		val resBuilder = SetAvatarMonsterResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			val legit = monsterId > 0
//			var successful = false
//			if (legit)
//			{
//				successful = writeChangesToDb(user, monsterId)
//			}
//			else
//			{
//				LOG.error("can't unset avatarMonsterId")
//			}
//			if (successful)
//			{
//				resBuilder.status = SetAvatarMonsterStatus.SUCCESS
//			}
//			else
//			{
//				resBuilder.status = SetAvatarMonsterStatus.FAIL_OTHER
//			}
//			val resProto = resBuilder.build
//			val resEvent = new SetAvatarMonsterResponseEvent(senderProto.userUuid)
//			resEvent.setAvatarMonsterResponseProto = resProto
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in SetAvatarMonsterController.processRequestEvent', e)
//			}
//			if (successful)
//			{
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
//					LOG.error(
//						'fatal exception in SetAvatarMonsterController.processRequestEvent', e)
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in SetAvatarMonsterController processEvent', e)
//			try
//			{
//				resBuilder.status = SetAvatarMonsterStatus.FAIL_OTHER
//				val resEvent = new SetAvatarMonsterResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.setAvatarMonsterResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in SetAvatarMonsterController.processRequestEvent', e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in SetAvatarMonsterController processEvent', e)
//			}
//		}
//		finally
//		{
//		}
//	}
//
//	private def writeChangesToDb(User user, int avatarMonsterId)
//	{
//		try
//		{
//			if (!user.updateAvatarMonsterId(avatarMonsterId))
//			{
//				LOG.error("problem with setting user's avatarMonsterId to " + avatarMonsterId)
//				return false;
//			}
//			return true;
//		}
//		catch (Exception e)
//		{
//			LOG.error(
//				'problem with updating user avatar monster id. user=' + user +
//					'	 avatarMonsterId=' + avatarMonsterId)
//		}
//		false
//	}
//}
