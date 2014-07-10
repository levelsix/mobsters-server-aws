package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.hazelcast.core.IList;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SendGroupChatRequestEvent;
import com.lvl6.mobsters.events.response.ReceivedGroupChatResponseEvent;
import com.lvl6.mobsters.events.response.SendGroupChatResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.misc.MiscMethods;
import com.lvl6.properties.ControllerConstants;
import com.lvl6.mobsters.noneventproto.NoneventChatProto.GroupChatMessageProto;
import com.lvl6.mobsters.noneventproto.NoneventChatProto.GroupChatScope;
import com.lvl6.mobsters.eventproto.EventChatProto.ReceivedGroupChatResponseProto;
import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatRequestProto;
import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatResponseProto;
import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatResponseProto.SendGroupChatStatus;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithLevel;
import com.lvl6.retrieveutils.rarechange.BannedUserRetrieveUtils;
import com.lvl6.server.EventWriter;
import com.lvl6.server.Locker;
import com.lvl6.utils.CreateInfoProtoUtils;
import com.lvl6.utils.RetrieveUtils;
import com.lvl6.utils.utilmethods.InsertUtils;

@Component
@DependsOn("gameServer")
public class SendGroupChatController extends EventController {

	private static Logger LOG = LoggerFactory.getLogger(SendGroupChatController.class);
  }.getClass().getEnclosingClass());

  public static int CHAT_MESSAGES_MAX_SIZE = 50;

  @Resource(name = "globalChat")
  protected IList<GroupChatMessageProto> chatMessages;

  @Resource
  protected EventWriter eventWriter;

  @Autowired
 protected DataServiceTxManager svcTxManager;

 public SendGroupChatController() {
    numAllocatedThreads = 4;
  }

  @Override
  public RequestEvent createRequestEvent() {
    return new SendGroupChatRequestEvent();
  }

  @Override
  public EventProtocolRequest getEventType() {
    return EventProtocolRequest.C_SEND_GROUP_CHAT_EVENT;
  }

  @Override
  protected void processRequestEvent( final RequestEvent event, final EventsToDispatch eventWriter ) throws Exception {
    final SendGroupChatRequestProto reqProto = ((SendGroupChatRequestEvent) event)
        .getSendGroupChatRequestProto();

    final MinimumUserProto senderProto = reqProto.getSender();
    final GroupChatScope scope = reqProto.getScope();
    final String chatMessage = reqProto.getChatMessage();
    final Timestamp timeOfPost = new Timestamp(new Date().getTime());

    final SendGroupChatResponseProto.Builder resBuilder = SendGroupChatResponseProto.newBuilder();
    resBuilder.setSender(senderProto);
    resBuilder.setStatus(SendGroupChatStatus.OTHER_FAIL);
    final SendGroupChatResponseEvent resEvent = new SendGroupChatResponseEvent(senderProto.getUserUuid());
    resEvent.setTag(event.getTag());

    svcTxManager.beginTransaction();
    try {
      final User user = RetrieveUtils.userRetrieveUtils().getUserById(senderProto.getUserUuid());

      final boolean legitSend = checkLegitSend(resBuilder, user, scope, chatMessage);

      resEvent.setSendGroupChatResponseProto(resBuilder.build());
      // write to client
      LOG.info("Writing event: " + resEvent);
      try {
          eventWriter.writeEvent(resEvent);
      } catch (final Throwable e) {
          LOG.error("fatal exception in SendGroupChatController.processRequestEvent", e);
      }

      if (legitSend) {
        LOG.info("Group chat message is legit... sending to group");
        final String censoredChatMessage = MiscMethods.censorUserInput(chatMessage);
        writeChangesToDB(user, scope, censoredChatMessage, timeOfPost);

        //null PvpLeagueFromUser means will pull from hazelcast instead
        final UpdateClientUserResponseEvent resEventUpdate = MiscMethods
            .createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null);
        resEventUpdate.setTag(event.getTag());
        // write to client
        LOG.info("Writing event: " + resEventUpdate);
        try {
            eventWriter.writeEvent(resEventUpdate);
        } catch (final Throwable e) {
            LOG.error("fatal exception in SendGroupChatController.processRequestEvent", e);
        }
        final ReceivedGroupChatResponseProto.Builder chatProto = ReceivedGroupChatResponseProto
            .newBuilder();
        chatProto.setChatMessage(censoredChatMessage);
        final MinimumUserProtoWithLevel mupWithLvl = CreateInfoProtoUtils
        		.createMinimumUserProtoWithLevelFromUser(user);
        chatProto.setSender(mupWithLvl);
        chatProto.setScope(scope);
        if (scope == GroupChatScope.GLOBAL) {
          chatProto.setIsAdmin(user.isAdmin());
        }
        sendChatMessage(senderProto.getUserUuid(), chatProto, event.getTag(),
            scope == GroupChatScope.CLAN, user.getClanId(), user.isAdmin(),
            timeOfPost.getTime(), user.getLevel());
        // send messages in background so sending player can unlock
        /*
         * executor.execute(new Runnable() {
         * 
         * @Override public void run() {
         * sendChatMessageToConnectedPlayers(chatProto, event.getTag(),
         * timeOfPost.getTime(), scope == GroupChatScope.CLAN,
         * user.getClanId(), user.isAdmin()); } });
         */
      }
    } catch (final Exception e) {
    	LOG.error("exception in SendGroupChat processEvent", e);
    	//don't let the client hang
    	try {
    		resBuilder.setStatus(SendGroupChatStatus.OTHER_FAIL);
    		resEvent.setSendGroupChatResponseProto(resBuilder.build());
    		// write to client
    		LOG.info("Writing event: " + resEvent);
    		try {
    		    eventWriter.writeEvent(resEvent);
    		} catch (final Throwable e) {
    		    LOG.error("fatal exception in SendGroupChatController.processRequestEvent", e);
    		}
    	} catch (final Exception e2) {
    		LOG.error("exception2 in SendGroupChat processEvent", e);
    	}
    } finally {
      svcTxManager.commit();
    }
  }

  protected void sendChatMessage(final int senderId, final ReceivedGroupChatResponseProto.Builder chatProto, final int tag,
      final boolean isForClan, final int clanId, final boolean isAdmin, final long time, final int level) {
    final ReceivedGroupChatResponseEvent ce = new ReceivedGroupChatResponseEvent(senderId);
    ce.setReceivedGroupChatResponseProto(chatProto.build());
    if (isForClan) {
      LOG.info("Sending event to clan "+ clanId);
      eventWriter.handleClanEvent(ce, clanId);
    } else {
      LOG.info("Sending global chat ");
      //add new message to front of list
      chatMessages.add(0, CreateInfoProtoUtils.createGroupChatMessageProto(time, chatProto.getSender(), chatProto.getChatMessage(), isAdmin, 0));
      //remove older messages
      try {
        while(chatMessages.size() > CHAT_MESSAGES_MAX_SIZE) {
          chatMessages.remove(CHAT_MESSAGES_MAX_SIZE);
        }
      } catch(final Exception e) {
        LOG.error("Error sending chat message", e);
      }
      eventWriter.processGlobalChatResponseEvent(ce);
    }
  }

  /*
   * protected void
   * sendChatMessageToConnectedPlayers(ReceivedGroupChatResponseProto.Builder
   * chatProto, int tag, long time, boolean forClan, int clanId, boolean
   * isAdmin) { Collection<ConnectedPlayer> players = new
   * ArrayList<ConnectedPlayer>(); if (forClan) { List<UserClan> clanMembers =
   * RetrieveUtils.userClanRetrieveUtils().getUserClanMembersInClan( clanId);
   * for (UserClan uc : clanMembers) { ConnectedPlayer cp =
   * playersByPlayerId.get(uc.getUserUuid()); if (cp != null) { players.add(cp);
   * } } } else { players = playersByPlayerId.values(); // add new message to
   * front of list chatMessages.add( 0,
   * CreateInfoProtoUtils.createGroupChatMessageProto(time,
   * chatProto.getSender(), chatProto.getChatMessage(), isAdmin)); // remove
   * older messages try { while (chatMessages.size() > CHAT_MESSAGES_MAX_SIZE)
   * { chatMessages.remove(CHAT_MESSAGES_MAX_SIZE); } } catch (Exception e) {
   * LOG.error(e); } } for (ConnectedPlayer player : players) {
   * LOG.info("Sending chat message to player: " + player.getPlayerId());
   * ReceivedGroupChatResponseEvent ce = new
   * ReceivedGroupChatResponseEvent(player.getPlayerId());
   * ce.setReceivedGroupChatResponseProto(chatProto.build()); ce.setTag(tag);
   * try { server.writeEvent(ce); } catch (Exception e) { LOG.error(e); } } }
   */

  private void writeChangesToDB(final User user, final GroupChatScope scope, final String content, final Timestamp timeOfPost) {
    // if (!user.updateRelativeNumGroupChatsRemainingAndDiamonds(-1, 0)) {
    // LOG.error("problem with decrementing a global chat");
    // }

    if (scope == GroupChatScope.CLAN) {
      InsertUtils.get().insertClanChatPost(user.getId(), user.getClanId(), content, timeOfPost);
    }
  }

  private boolean checkLegitSend(final Builder resBuilder, final User user, final GroupChatScope scope, final String chatMessage) {
    if ((user == null) || (scope == null) || (chatMessage == null) || (chatMessage.length() == 0)) {
      resBuilder.setStatus(SendGroupChatStatus.OTHER_FAIL);
      LOG.error("user is " + user + ", scope is " + scope + ", chatMessage=" + chatMessage);
      return false;
    }

    if (chatMessage.length() > ControllerConstants.SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING) {
      resBuilder.setStatus(SendGroupChatStatus.TOO_LONG);
      LOG.error("chat message is too long. allowed is "
          + ControllerConstants.SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING + ", length is "
          + chatMessage.length() + ", chatMessage is " + chatMessage);
      return false;
    }

    final Set<Integer> banned = BannedUserRetrieveUtils.getAllBannedUsers();
    if(banned.contains(user.getId())) {
      resBuilder.setStatus(SendGroupChatStatus.BANNED);
      LOG.warn("banned user tried to send a post. user=" + user);
      return false;
    }

    resBuilder.setStatus(SendGroupChatStatus.SUCCESS);
    return true;
  }
  

  public IList<GroupChatMessageProto> getChatMessages() {
    return chatMessages;
  }

  public void setChatMessages(final IList<GroupChatMessageProto> chatMessages) {
    this.chatMessages = chatMessages;
  }
  
  @Override
public EventWriter getEventWriter() {
	  return eventWriter;
  }
  
  public void setEventWriter(final EventWriter eventWriter) {
	  this.eventWriter = eventWriter;
  }

}
