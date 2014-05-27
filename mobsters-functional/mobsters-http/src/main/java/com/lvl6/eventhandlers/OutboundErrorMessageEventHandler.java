package com.lvl6.eventhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;

import com.lvl6.mobsters.cache.PlayerMapsCacheManager;
import com.lvl6.mobsters.events.NormalResponseEvent;

public class OutboundErrorMessageEventHandler implements MessageHandler {

	
	private static final Logger log = LoggerFactory.getLogger(OutboundErrorMessageEventHandler.class);
	
	@Autowired
	protected PlayerMapsCacheManager playerMaps;

	


	@Resource(name="messagesForDisconnectedPlayers")
	protected Map<String, List<Message<?>>> messagesForDisconnectedPlayers;

	
	
	public Map<String, List<Message<?>>> getMessagesForDisconnectedPlayers() {
		return messagesForDisconnectedPlayers;
	}



	public void setMessagesForDisconnectedPlayers(
			Map<String, List<Message<?>>> messagesForDisconnectedPlayers) {
		this.messagesForDisconnectedPlayers = messagesForDisconnectedPlayers;
	}



	@Override
	public void handleMessage(Message<?> failedMessage) throws MessagingException {
		log.info("Handling failed message");
		if(failedMessage.getHeaders().containsKey("playerId")) {
			NormalResponseEvent ev = (NormalResponseEvent) failedMessage.getPayload();
			String user = ev.getPlayerId();
			log.info("Failed to send message to user "+user+"... saving in case they reconnect ");
			List<Message<?>> playerPendingMessages;
			if(messagesForDisconnectedPlayers.containsKey(user)) {
				playerPendingMessages = messagesForDisconnectedPlayers.get(user);
			}else {
				playerPendingMessages = new ArrayList<Message<?>>();
				messagesForDisconnectedPlayers.put(user, playerPendingMessages);
			}
			playerPendingMessages.add(failedMessage);
			/*if(playersByPlayerId.containsKey(user)) {
				playersByPlayerId.remove(user);
			}*/
		}else {
			log.warn("Message that failed to send had no playerId header");
			log.warn(failedMessage.getHeaders().toString());
		}
	}



	public PlayerMapsCacheManager getPlayerMaps() {
		return playerMaps;
	}



	public void setPlayerMaps(PlayerMapsCacheManager playerMaps) {
		this.playerMaps = playerMaps;
	}

}
