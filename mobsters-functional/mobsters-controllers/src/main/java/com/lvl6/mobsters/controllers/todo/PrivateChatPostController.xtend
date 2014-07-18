package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.AdminChatPost
import com.lvl6.mobsters.dynamo.PrivateChatPost
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto.PrivateChatPostStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.PrivateChatPostRequestEvent
import com.lvl6.mobsters.events.response.PrivateChatPostResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Date
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class PrivateChatPostController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(PrivateChatPostController))
	@Autowired
	protected var DataServiceTxManager svcTxManager
	@Autowired
	protected var AdminChatUtil adminChatUtil
	@Autowired
	protected var InsertUtil insertUtils

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new PrivateChatPostRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_PRIVATE_CHAT_POST_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as PrivateChatPostRequestEvent)).privateChatPostRequestProto
		val senderProto = reqProto.sender
		val posterId = senderProto.userUuid
		val recipientId = reqProto.recipientUuid
		val content = if ((reqProto.content)) reqProto.content else ''
		val resBuilder = PrivateChatPostResponseProto::newBuilder
		resBuilder.sender = senderProto
		val userUuids = new ArrayList<Integer>()
		userUuids.add(posterId)
		userUuids.add(recipientId)
		try
		{
			val users = RetrieveUtils::userRetrieveUtils.getUsersByUuids(userUuids)
			var legitPost = checkLegitPost(resBuilder, posterId, recipientId, content, users)
			val resEvent = new PrivateChatPostResponseEvent(posterId)
			resEvent.tag = event.tag
			if (legitPost)
			{
				val timeOfPost = new Timestamp(new Date().time)
				val censoredContent = MiscMethods::censorUserInput(content)
				val privateChatPostId = insertUtils.
					insertIntoPrivateChatPosts(posterId, recipientId, censoredContent,
						timeOfPost)
				if (privateChatPostId <= 0)
				{
					legitPost = false
					resBuilder.status = PrivateChatPostStatus.OTHER_FAIL
					LOG.error(
						'problem with inserting private chat post into db. posterId=' + posterId +
							', recipientId=' + recipientId + ', content=' + content +
							', censoredContent=' + censoredContent + ', timeOfPost=' +
							timeOfPost)
				}
				else
				{
					if (recipientId === ControllerConstants::STARTUP__ADMIN_CHAT_USER_ID)
					{
						val acp = new AdminChatPost(privateChatPostId, posterId, recipientId,
							new Date(), censoredContent)
						acp.username = users.get(posterId).name
						adminChatUtil.sendAdminChatEmail(acp)
					}
					val pwp = new PrivateChatPost(privateChatPostId, posterId, recipientId,
						timeOfPost, censoredContent)
					val poster = users.get(posterId)
					val recipient = users.get(recipientId)
					val pcpp = CreateInfoProtoUtils::
						createPrivateChatPostProtoFromPrivateChatPost(pwp, poster, recipient)
					resBuilder.post = pcpp
					val resEvent2 = new PrivateChatPostResponseEvent(recipientId)
					resEvent2.privateChatPostResponseProto = resBuilder.build
					server.writeAPNSNotificationOrEvent(resEvent2)
				}
			}
			resEvent.privateChatPostResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in PrivateChatPostController.processRequestEvent', e)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in PrivateChatPostController processEvent', e)
			try
			{
				resBuilder.status = PrivateChatPostStatus.OTHER_FAIL
				val resEvent = new PrivateChatPostResponseEvent(posterId)
				resEvent.tag = event.tag
				resEvent.privateChatPostResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in PrivateChatPostController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in PrivateChatPostController processEvent', e)
			}
		}
	}

	private def checkLegitPost(Builder resBuilder, int posterId, int recipientId,
		String content, Map<Integer, User> users)
	{
		if (users === null)
		{
			resBuilder.status = PrivateChatPostStatus.OTHER_FAIL
			LOG.error('users are null- posterId=' + posterId + ', recipientId=' + recipientId)
			return false;
		}
		if ((users.size !== 2) && (posterId !== recipientId))
		{
			resBuilder.status = PrivateChatPostStatus.OTHER_FAIL
			LOG.error(
				'error retrieving one of the users. posterId=' + posterId + ', recipientId=' +
					recipientId)
			return false;
		}
		if ((users.size !== 1) && (posterId === recipientId))
		{
			resBuilder.status = PrivateChatPostStatus.OTHER_FAIL
			LOG.error(
				'error retrieving one of the users. posterId=' + posterId + ', recipientId=' +
					recipientId)
			return false;
		}
		if ((content === null) || (content.length === 0))
		{
			resBuilder.status = PrivateChatPostStatus.NO_CONTENT_SENT
			LOG.error(
				'no content when posterId ' + posterId + ' tries to post on wall with owner ' +
					recipientId)
			return false;
		}
		if (content.length >= ControllerConstants::SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING)
		{
			resBuilder.status = PrivateChatPostStatus.POST_TOO_LARGE
			LOG.error(
				'wall post is too long. content length is ' + content.length +
					', max post length=' +
					ControllerConstants::SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING +
					', posterId ' + posterId + ' tries to post on wall with owner ' +
					recipientId)
			return false;
		}
		val banned = BannedUserRetrieveUtils::allBannedUsers
		if ((null !== banned) && banned.contains(posterId))
		{
			resBuilder.status = PrivateChatPostStatus.BANNED
			LOG.warn('banned user tried to send a post. posterId=' + posterId)
			return false;
		}
		resBuilder.status = PrivateChatPostStatus.SUCCESS
		true
	}

	def getAdminChatUtil()
	{
		adminChatUtil
	}

	def setAdminChatUtil(AdminChatUtil adminChatUtil)
	{
		this.adminChatUtil = adminChatUtil
	}

	def setInsertUtils(InsertUtil insertUtils)
	{
		this.insertUtils = insertUtils
	}
}
