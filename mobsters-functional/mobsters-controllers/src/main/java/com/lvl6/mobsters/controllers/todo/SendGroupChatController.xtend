//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventChatProto.ReceivedGroupChatResponseProto
//import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatResponseProto
//import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatResponseProto.SendGroupChatStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.SendGroupChatRequestEvent
//import com.lvl6.mobsters.events.response.SendGroupChatResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventChatProto.GroupChatMessageProto
//import com.lvl6.mobsters.noneventproto.NoneventChatProto.GroupChatScope
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.Date
//import javax.annotation.Resource
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class SendGroupChatController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(SendGroupChatController))
//	public static var CHAT_MESSAGES_MAX_SIZE = 50
//	@Resource(name='globalChat')
//	protected var IList<GroupChatMessageProto> chatMessages
//	@Resource
//	protected var EventWriter eventWriter
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
//		new SendGroupChatRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_SEND_GROUP_CHAT_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as SendGroupChatRequestEvent)).sendGroupChatRequestProto
//		val senderProto = reqProto.sender
//		val scope = reqProto.scope
//		val chatMessage = reqProto.chatMessage
//		val timeOfPost = new Timestamp(new Date().time)
//		val resBuilder = SendGroupChatResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = SendGroupChatStatus.OTHER_FAIL
//		val resEvent = new SendGroupChatResponseEvent(senderProto.userUuid)
//		resEvent.tag = event.tag
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			val legitSend = checkLegitSend(resBuilder, user, scope, chatMessage)
//			resEvent.sendGroupChatResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in SendGroupChatController.processRequestEvent', e)
//			}
//			if (legitSend)
//			{
//				LOG.info('Group chat message is legit... sending to group')
//				val censoredChatMessage = MiscMethods::censorUserInput(chatMessage)
//				writeChangesToDB(user, scope, censoredChatMessage, timeOfPost)
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
//					LOG.error('fatal exception in SendGroupChatController.processRequestEvent',
//						e)
//				}
//				val chatProto = ReceivedGroupChatResponseProto::newBuilder
//				chatProto.chatMessage = censoredChatMessage
//				val mupWithLvl = CreateInfoProtoUtils::
//					createMinimumUserProtoWithLevelFromUser(user)
//				chatProto.sender = mupWithLvl
//				chatProto.scope = scope
//				if (scope === GroupChatScope::GLOBAL)
//				{
//					chatProto.isAdmin = user.admin
//				}
//				sendChatMessage(senderProto.userUuid, chatProto, event.tag,
//					scope === GroupChatScope::CLAN, user.clanId, user.admin, timeOfPost.time,
//					user.level)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in SendGroupChat processEvent', e)
//			try
//			{
//				resBuilder.status = SendGroupChatStatus.OTHER_FAIL
//				resEvent.sendGroupChatResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in SendGroupChatController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in SendGroupChat processEvent', e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	protected def sendChatMessage(int senderId, ReceivedGroupChatResponseProto ::Builder chatProto
//,   int tag,   boolean isForClan,   int clanId,   boolean isAdmin,   long time,   int level){
//    val ce=new ReceivedGroupChatResponseEvent(senderId)
//    ce.receivedGroupChatResponseProto=chatProto.build
//    if (isForClan) {
//      LOG.info('Sending event to clan ' + clanId)
//      eventWriter.handleClanEvent(ce, clanId)
//    }
// else {
//      LOG.info('Sending global chat ')
//      chatMessages.add(0, CreateInfoProtoUtils::createGroupChatMessageProto(time, chatProto.sender, chatProto.chatMessage, isAdmin, 0))
//      try {
//        while (chatMessages.size > CHAT_MESSAGES_MAX_SIZE) {
//          chatMessages.remove(CHAT_MESSAGES_MAX_SIZE)
//        }
//      }
// catch (      Exception e) {
//        LOG.error('Error sending chat message', e)
//      }
//      eventWriter.processGlobalChatResponseEvent(ce)
//    }
//  }
//
//private def writeChangesToDB(  User user,   GroupChatScope scope,   String content,   Timestamp timeOfPost){
//    if (scope === GroupChatScope::CLAN) {
//      InsertUtils::get.insertClanChatPost(user.id, user.clanId, content, timeOfPost)
//    }
//  }
//
//private def checkLegitSend(  Builder resBuilder,   User user,   GroupChatScope scope,   String chatMessage){
//    if ((user === null) || (scope === null) || (chatMessage === null)|| (chatMessage.length === 0)) {
//      resBuilder.status=SendGroupChatStatus.OTHER_FAIL
//      LOG.error('user is ' + user + ', scope is '+ scope+ ', chatMessage='+ chatMessage)
//      return false;
//    }
//    if (chatMessage.length > ControllerConstants::SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING) {
//      resBuilder.status=SendGroupChatStatus.TOO_LONG
//      LOG.error('chat message is too long. allowed is ' + ControllerConstants::SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING + ', length is '+ chatMessage.length+ ', chatMessage is '+ chatMessage)
//      return false;
//    }
//    val banned=BannedUserRetrieveUtils::allBannedUsers
//    if (banned.contains(user.id)) {
//      resBuilder.status=SendGroupChatStatus.BANNED
//      LOG.warn('banned user tried to send a post. user=' + user)
//      return false;
//    }
//    resBuilder.status=SendGroupChatStatus.SUCCESS
//    true
//  }
//  def getChatMessages(){
//    chatMessages
//  }
//  def setChatMessages(  IList<GroupChatMessageProto> chatMessages){
//    this.chatMessages=chatMessages
//  }
//  override getEventWriter(){
//    eventWriter
//  }
//  def setEventWriter(  EventWriter eventWriter){
//    this.eventWriter=eventWriter
//  }
//}
