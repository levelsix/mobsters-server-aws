package com.lvl6.eventdispatcher;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.cache.PlayerMapsCacheManager;
import com.lvl6.mobsters.events.BroadcastResponseEvent;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.events.PreDatabaseResponseEvent;
import com.lvl6.mobsters.websockets.SessionMap;


@Component
@Qualifier("Root")
public class ClientEventDispatcherImpl 
	implements ClientEventDispatcher 
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ClientEventDispatcherImpl.class);
	
	@Autowired
	protected PlayerMapsCacheManager playerMaps;

	@Autowired
	protected SessionMap sessionMap;
	
	
	@Autowired
	@Qualifier("AMQP")
	protected ClientEventDispatcher amqpEventDispatcher;
	
	@Autowired
	@Qualifier("WebSocket")
	protected ClientEventDispatcher webSocketEventDispatcher;
	
	
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

	public ClientEventDispatcher getAmqpEventDispatcher() {
		return amqpEventDispatcher;
	}

	public void setAmqpEventDispatcher(ClientEventDispatcher amqpEventDispatcher) {
		this.amqpEventDispatcher = amqpEventDispatcher;
	}

	public ClientEventDispatcher getWebSocketEventDispatcher() {
		return webSocketEventDispatcher;
	}

	public void setWebSocketEventDispatcher(ClientEventDispatcher webSocketEventDispatcher) {
		this.webSocketEventDispatcher = webSocketEventDispatcher;
	}

}
