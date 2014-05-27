package com.lvl6.eventhandlers;

import java.io.FileNotFoundException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.mobsters.cache.PlayerMapsCacheManager;
import com.lvl6.mobsters.events.PreDatabaseRequestEvent;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.info.ConnectedPlayer;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.server.ServerInstance;

public class GameEventHandler extends AbstractGameEventHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GameEventHandler.class);

	@Autowired
	protected PlayerMapsCacheManager playerMaps;

	
	@Autowired
	protected ServerInstance server;
	
	/**
	 * pass off an event to the appropriate GameController based on the GameName
	 * of the event
	 * 
	 * @throws FileNotFoundException
	 */
	@Override
	protected void delegateEvent(byte[] bytes, RequestEvent event, EventProtocolRequest eventType) {
		if (event != null && eventType.getNumber() < 0) {
			log.error("the event type is < 0");
			return;
		}
		EventController ec = getControllerManager().getEventControllerByEventType(eventType);
		if (ec == null) {
			log.error("No EventController for eventType: " + eventType);
			return;
		}
		updatePlayerToServerMaps(event);
		ec.handleEvent(event);
	}

	protected void updatePlayerToServerMaps(RequestEvent event) {
		log.debug("Updating player to server maps for player: "	+ event.getPlayerId());
		ConnectedPlayer p = playerMaps.getPlayer(event.getPlayerId());
		if (p != null) {
			p.setLastMessageSentToServer(new Date());
			p.setServerHostName(server.serverId());
			
			playerMaps.savePlayer(p);
		} else {
			addNewConnection(event);
		}
	}

	protected void addNewConnection(RequestEvent event) {
		ConnectedPlayer newp = new ConnectedPlayer();
		newp.setServerHostName(server.serverId());
		if (event.getPlayerId() != "") {
			log.info("Player logged on: " + event.getPlayerId());
			newp.setPlayerId(event.getPlayerId());
			playerMaps.savePlayer(newp);
		} else {
			newp.setUdid(((PreDatabaseRequestEvent) event).getUdid());
			playerMaps.savePlayerPreDatabase(newp);
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

}
