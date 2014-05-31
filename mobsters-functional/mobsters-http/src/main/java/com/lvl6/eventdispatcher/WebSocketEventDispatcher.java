package com.lvl6.eventdispatcher;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import com.lvl6.mobsters.cache.PlayerMapsCacheManager;
import com.lvl6.mobsters.events.BroadcastResponseEvent;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.events.PreDatabaseResponseEvent;
import com.lvl6.mobsters.events.ResponseEvent;
import com.lvl6.mobsters.utils.NIOUtils;
import com.lvl6.mobsters.websockets.SessionMap;

public class WebSocketEventDispatcher {

	
	private static final Logger log = LoggerFactory.getLogger(WebSocketEventDispatcher.class);
	
	@Autowired
	protected PlayerMapsCacheManager playerMaps;

	@Autowired
	protected SessionMap sessionMap;

	
	public void dispatchEvents(EventsToDispatch events) {
		dispatchNormalEvents(events.getResponsesToSingleUser());
		dispatchPreDatabaseEvents(events.getPredbResponseEvents());
		dispatchBroadcastEvents(events.getBroadcastResponseEvents());
	}

	
	protected void dispatchNormalEvents(List<NormalResponseEvent> events) {
		for(NormalResponseEvent ev : events) {
			dispatchNormalEvent(ev);
		}
	}


	protected void dispatchNormalEvent(NormalResponseEvent ev) {
		WebSocketSession sess = sessionMap.get(ev.getPlayerId());
		if(sess != null && sess.isOpen()) {
			if(sess.isOpen()) {
				sendMessage(ev, sess);
			}
		}else {
			
		}
	}



	protected void dispatchPreDatabaseEvents(List<PreDatabaseResponseEvent> events) {
		for(PreDatabaseResponseEvent ev : events) {
			dispatchPreDatabaseEvent(ev);
		}
	}

	
	protected void dispatchPreDatabaseEvent(PreDatabaseResponseEvent ev) {
		WebSocketSession sess = sessionMap.get(ev.getUdid());
		if(sess != null && sess.isOpen()){
			if(sess.isOpen()) {
				sendMessage(ev, sess);
			}
		}else {
			
		}
	}

	
	
	protected void dispatchBroadcastEvents(List<BroadcastResponseEvent> events) {
		for(BroadcastResponseEvent ev : events) {
			dispatchBroadcastEvent(ev);
		}
	}
	
	protected void dispatchBroadcastEvent(BroadcastResponseEvent ev) {
		for(String recipient : ev.getRecipients()) {
			WebSocketSession sess = sessionMap.get(recipient);
			if(sess != null && sess.isOpen()) {
				if(sess.isOpen()) {
					sendMessage(ev, sess);
				}
			}else {
				
			}	
		}
	}
	
	
	protected void sendMessage(ResponseEvent ev, WebSocketSession sess) {
		BinaryMessage msg = new BinaryMessage(NIOUtils.getByteArray(ev));
		try {
			sess.sendMessage(msg);
		} catch (IOException e) {
			log.error("Error sending message", e);
		}
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

}
