//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.PrivateChatPost
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.util.ArrayList
//import java.util.HashMap
//import java.util.List
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class RetrievePrivateChatPostsController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(RetrievePrivateChatPostsController))
//
//	new()
//	{
//		numAllocatedThreads = 5
//	}
//
//	override createRequestEvent()
//	{
//		new RetrievePrivateChatPostsRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_RETRIEVE_PRIVATE_CHAT_POST_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as RetrievePrivateChatPostsRequestEvent)).
//			retrievePrivateChatPostsRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val otherUserId = reqProto.otherUserUuid
//		val beforePrivateChatId = reqProto.beforePrivateChatUuid
//		val resBuilder = RetrievePrivateChatPostsResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		if (reqProto.beforePrivateChatId)
//		{
//			resBuilder.beforePrivateChatId = beforePrivateChatId
//		}
//		resBuilder.otherUserId = otherUserId
//		try
//		{
//			resBuilder.status = RetrievePrivateChatPostsStatus.SUCCESS
//			var List<PrivateChatPost> recentPrivateChatPosts
//			if (beforePrivateChatId > 0)
//			{
//				recentPrivateChatPosts = PrivateChatPostRetrieveUtils::
//					getPrivateChatPostsBetweenUsersBeforePostId(
//						ControllerConstants::RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP,
//						beforePrivateChatId, userUuid, otherUserId)
//			}
//			else
//			{
//				recentPrivateChatPosts = PrivateChatPostRetrieveUtils::
//					getPrivateChatPostsBetweenUsersBeforePostId(
//						ControllerConstants::RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP,
//						ControllerConstants::NOT_SET, userUuid, otherUserId)
//			}
//			if (recentPrivateChatPosts !== null)
//			{
//				if ((recentPrivateChatPosts !== null) && (recentPrivateChatPosts.size > 0))
//				{
//					val userUuids = new ArrayList<Integer>()
//					userUuids.add(userUuid)
//					userUuids.add(otherUserId)
//					var Map<Integer, User> usersByUuids = null
//					if (userUuids.size > 0)
//					{
//						usersByUuids = RetrieveUtils::userRetrieveUtils.
//							getUsersByUuids(userUuids)
//						val userUuidsToMups = generateUserUuidsToMupsWithLevel(usersByUuids,
//							userUuid, otherUserId)
//						for (pwp : recentPrivateChatPosts)
//						{
//							val posterId = pwp.posterId
//							val time = pwp.timeOfPost.time
//							val user = userUuidsToMups.get(posterId)
//							val content = pwp.content
//							val isAdmin = false
//							val gcmp = CreateInfoProtoUtils::
//								createGroupChatMessageProto(time, user, content, isAdmin, pwp.id)
//							resBuilder.addPosts(gcmp)
//						}
//					}
//				}
//			}
//			else
//			{
//				LOG.info(
//					'No private chat posts found for userUuid=' + userUuid + ' and otherUserId=' +
//						otherUserId)
//			}
//			val resProto = resBuilder.build
//			val resEvent = new RetrievePrivateChatPostsResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.retrievePrivateChatPostsResponseProto = resProto
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in RetrievePrivateChatPostsController.processRequestEvent',
//					e)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in RetrievePrivateChatPostsController processEvent', e)
//			try
//			{
//				resBuilder.status = RetrievePrivateChatPostsStatus.FAIL
//				val resEvent = new RetrievePrivateChatPostsResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.retrievePrivateChatPostsResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in RetrievePrivateChatPostsController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in RetrievePrivateChatPostsController processEvent', e)
//			}
//		}
//	}
//
//	private def generateUserUuidsToMupsWithLevel(Map<Integer, User> usersByUuids,
//		String userUuid, int otherUserId)
//	{
//		val userUuidsToMups = new HashMap<Integer, MinimumUserProtoWithLevel>()
//		val aUser = usersByUuids.get(userUuid)
//		val otherUser = usersByUuids.get(otherUserId)
//		val mup1 = CreateInfoProtoUtils::createMinimumUserProtoWithLevelFromUser(aUser)
//		userUuidsToMups.put(userUuid, mup1)
//		val mup2 = CreateInfoProtoUtils::createMinimumUserProtoWithLevelFromUser(otherUser)
//		userUuidsToMups.put(otherUserId, mup2)
//		userUuidsToMups
//	}
//}
