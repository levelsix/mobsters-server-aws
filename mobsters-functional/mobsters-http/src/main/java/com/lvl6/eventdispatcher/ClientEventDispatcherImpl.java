package com.lvl6.eventdispatcher;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.cache.PlayerMapsCacheManager;
import com.lvl6.mobsters.events.BroadcastResponseEvent;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.events.PreDatabaseResponseEvent;
import com.lvl6.mobsters.websockets.SessionMap;


@Component
public class ClientEventDispatcherImpl implements ClientEventDispatcher {

	
	private static final Logger log = LoggerFactory.getLogger(ClientEventDispatcherImpl.class);
	
	@Autowired
	protected PlayerMapsCacheManager playerMaps;

	@Autowired
	protected SessionMap sessionMap;
	
	
	@Autowired
	protected AmqpEventDispatcher amqpEventDispatcher;
	
	@Autowired
	protected WebSocketEventDispatcher webSocketEventDispatcher;
	
	
	
	
	
	@Override
	public void dispatchEvents(EventsToDispatch events) {
		dispatchNormalEvents(events.getResponsesToSingleUser());
		dispatchPreDatabaseEvents(events.getPredbResponseEvents());
		dispatchBroadcastEvents(events.getBroadcastResponseEvents());
	}

	@Override
	public void dispatchNormalEvents(List<NormalResponseEvent> events) {
		for (NormalResponseEvent normalResponseEvent : events) {
			dispatchNormalEvent(normalResponseEvent);
		}
	}

	@Override
	public void dispatchNormalEvent(NormalResponseEvent ev) {
		if(userOnThisServer(ev.getPlayerId())) {
			getWebSocketEventDispatcher().dispatchNormalEvent(ev);
		}else {
			getAmqpEventDispatcher().dispatchNormalEvent(ev);
		}
	}

	@Override
	public void dispatchPreDatabaseEvents(List<PreDatabaseResponseEvent> events) {
		for (PreDatabaseResponseEvent preDatabaseResponseEvent : events) {
			dispatchPreDatabaseEvent(preDatabaseResponseEvent);
		}
	}

	@Override
	public void dispatchPreDatabaseEvent(PreDatabaseResponseEvent ev) {
		if(userOnThisServer(ev.getUdid())) {
			getWebSocketEventDispatcher().dispatchPreDatabaseEvent(ev);
		}else {
			getAmqpEventDispatcher().dispatchPreDatabaseEvent(ev);
		}
	}

	@Override
	public void dispatchBroadcastEvents(List<BroadcastResponseEvent> events) {
		for (BroadcastResponseEvent broadcastResponseEvent : events) {
			dispatchBroadcastEvent(broadcastResponseEvent);
		}
	}

	@Override
	public void dispatchBroadcastEvent(BroadcastResponseEvent ev) {
		List<String> thisServer = new ArrayList<>();
		List<String> otherServer = new ArrayList<>();
		for(String user : ev.getRecipients()) {
			if(userOnThisServer(user)) {
				thisServer.add(user);
			}else {
				otherServer.add(user);
			}	
		}
		ev.setRecipients(thisServer);
		getWebSocketEventDispatcher().dispatchBroadcastEvent(ev);
		ev.setRecipients(otherServer);
		getAmqpEventDispatcher().dispatchBroadcastEvent(ev);
	}

	
	protected boolean userOnThisServer(String userId) {
		return getSessionMap().containsKey(userId);
	}
	
	
	public PlayerMapsCacheManager getPlayerMaps() {
		return playerMaps;
	}

	public void setPlayerMaps(PlayerMapsCacheManager playerMaps) {
		this.playerMaps = playerMaps;
	}

	public SessionMap getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(SessionMap sessionMap) {
		this.sessionMap = sessionMap;
	}

	public AmqpEventDispatcher getAmqpEventDispatcher() {
		return amqpEventDispatcher;
	}

	public void setAmqpEventDispatcher(AmqpEventDispatcher amqpEventDispatcher) {
		this.amqpEventDispatcher = amqpEventDispatcher;
	}

	public WebSocketEventDispatcher getWebSocketEventDispatcher() {
		return webSocketEventDispatcher;
	}

	public void setWebSocketEventDispatcher(WebSocketEventDispatcher webSocketEventDispatcher) {
		this.webSocketEventDispatcher = webSocketEventDispatcher;
	}

}
