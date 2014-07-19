package com.lvl6.eventhandlers;

import java.io.FileNotFoundException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import com.lvl6.mobsters.cache.PlayerMapsCacheManager;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.PreDatabaseRequestEvent;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.info.ConnectedPlayer;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.server.ServerInstance;
import com.lvl6.mobsters.services.common.TimeUtils;
import com.lvl6.mobsters.websockets.SessionMap;

public class GameEventHandler extends AbstractGameEventHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GameEventHandler.class);

	@Autowired
	protected PlayerMapsCacheManager playerMaps;

	
	@Autowired
	protected ServerInstance server;
	
	
	@Autowired
	protected SessionMap sessionMap;
	
	/**
	 * pass off an event to the appropriate GameController based on the GameName
	 * of the event
	 * 
	 * @throws FileNotFoundException
	 */
	@Override
	protected void delegateEvent(byte[] bytes, RequestEvent event, EventProtocolRequest eventType, WebSocketSession session) {
		if (event != null && eventType.getNumber() < 0) {
			log.error("the event type is < 0");
			return;
		}
		EventController ec = getControllerManager().getEventControllerByEventType(eventType);
		if (ec == null) {
			log.error("No EventController for eventType: " + eventType);
			return;
		}
		updatePlayerToServerMaps(event, session);
		EventsToDispatch responseEvents = ec.handleEvent(event);
	}

	protected void updatePlayerToServerMaps(RequestEvent event, WebSocketSession session) {
		log.debug("Updating player to server maps for player: "	+ event.getPlayerId());
		ConnectedPlayer p = playerMaps.getPlayer(event.getPlayerId());
		if (p != null) {
			p.setLastMessageSentToServer(TimeUtils.createNow());
			p.setServerHostName(server.serverId());
			
			playerMaps.savePlayer(p);
			if(event.getPlayerId() != "") {
				sessionMap.put(event.getPlayerId(), session);
			}
		} else {
			addNewConnection(event, session);
		}
	}

	protected void addNewConnection(RequestEvent event, WebSocketSession session) {
		ConnectedPlayer newp = new ConnectedPlayer();
		newp.setServerHostName(server.serverId());
		String playerId = event.getPlayerId();
		if (playerId != "") {
			log.info("Player logged on: " + playerId);
			newp.setPlayerId(playerId);
			playerMaps.savePlayer(newp);
			sessionMap.put(playerId, session);
		} else {
			String udid = ((PreDatabaseRequestEvent) event).getUdid();
			newp.setUdid(udid);
			playerMaps.savePlayerPreDB(newp);
			sessionMap.put(udid, session);
			log.info("New player with UdId: " + newp.getUdid());
			
		}
	}
	
	
	

	public PlayerMapsCacheManager getPlayerMaps() {
		return playerMaps;
	}

	public void setPlayerMaps(PlayerMapsCacheManager playerMaps) {
		this.playerMaps = playerMaps;
	}

	public ServerInstance getServer() {
		return server;
	}

	public void setServer(ServerInstance server) {
		this.server = server;
	}

	public SessionMap getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(SessionMap sessionMap) {
		this.sessionMap = sessionMap;
	}

}
