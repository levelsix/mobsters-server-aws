//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdResponseProto
//import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdResponseProto.SetFacebookIdStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.SetFacebookIdRequestEvent
//import com.lvl6.mobsters.events.response.SetFacebookIdResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.EventController
//import java.util.ArrayList
//import java.util.List
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class SetFacebookIdController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(SetFacebookIdController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 1
//	}
//
//	override createRequestEvent()
//	{
//		new SetFacebookIdRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_SET_FACEBOOK_ID_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as SetFacebookIdRequestEvent)).setFacebookIdRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		var fbId = reqProto.fbUuid
//		val isUserCreate = reqProto.isUserCreate
//		if ((null !== fbId) && fbId.empty)
//		{
//			fbId = null
//		}
//		var List<String> facebookUuids = null
//		if (null !== fbId)
//		{
//			facebookUuids = new ArrayList<String>()
//			facebookUuids.add(fbId)
//		}
//		val userUuids = new ArrayList<Integer>()
//		userUuids.add(userUuid)
//		val resBuilder = SetFacebookIdResponseProto::newBuilder
//		resBuilder.status = SetFacebookIdStatus.FAIL_OTHER
//		resBuilder.sender = senderProto
//		svcTxManager.beginTransaction
//		try
//		{
//			val userMap = RetrieveUtils::userRetrieveUtils.
//				getUsersForFacebookIdsOrUserUuids(facebookUuids, userUuids)
//			val user = userMap.get(userUuid)
//			var legit = checkLegitRequest(resBuilder, user, fbId, userMap)
//			if (legit)
//			{
//				legit = writeChangesToDb(user, fbId, isUserCreate)
//			}
//			if (legit)
//			{
//				resBuilder.status = SetFacebookIdStatus.SUCCESS
//			}
//			val resProto = resBuilder.build
//			val resEvent = new SetFacebookIdResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.setFacebookIdResponseProto = resProto
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in SetFacebookIdController.processRequestEvent', e)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in SetFacebookIdController processEvent', e)
//			try
//			{
//				resBuilder.status = SetFacebookIdStatus.FAIL_OTHER
//				val resEvent = new SetFacebookIdResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.setFacebookIdResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in SetFacebookIdController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in SetFacebookIdController processEvent', e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegitRequest(Builder resBuilder, User user, String newFbId,
//		Map<Integer, User> userMap)
//	{
//		if ((newFbId === null) || newFbId.empty || (user === null))
//		{
//			LOG.error("fbId not set or user is null. fbId='" + newFbId + "'	 user=" + user)
//			return false;
//		}
//		val existingFbId = user.facebookId
//		val existingFbIdSet = (existingFbId !== null) && !existingFbId.empty
//		if (existingFbIdSet)
//		{
//			LOG.error(
//				"fbId already set for user. existingFbId='" + existingFbId + "'	 user=" + user +
//					'	 newFbId=' + newFbId)
//			resBuilder.status = SetFacebookIdStatus.FAIL_USER_FB_ID_ALREADY_SET
//			return false;
//		}
//		if (userMap.size > 1)
//		{
//			LOG.error("fbId already taken. fbId='" + newFbId + "'	 usersInDb=" + userMap)
//			resBuilder.status = SetFacebookIdStatus.FAIL_FB_ID_EXISTS
//			for (u : userMap.values)
//			{
//				if (newFbId != u.facebookId)
//				{
//					continue;
//				}
//				val existingProto = CreateInfoProtoUtils::createMinimumUserProtoFromUser(u)
//				resBuilder.existing = existingProto
//        break;
//			}
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDb(User user, String fbId, boolean isUserCreate)
//	{
//		if (!user.updateSetFacebookId(fbId, isUserCreate))
//		{
//			LOG.error("problem with setting user's facebook id to " + fbId)
//			return false;
//		}
//		true
//	}
//}
