package com.lvl6.mobsters.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.lvl6.eventhandlers.GameEventHandler;

public class GameWebsocket extends BinaryWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(GameWebsocket.class);

	
	@Autowired
	protected GameEventHandler gameEventHandler;
	
	@Autowired
	protected SessionMap sessionsMap;
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		if(sessionsMap.contains(session)) {
			String userId = (String) session.getAttributes().get(SessionAttributes.userId);
			String uuid = (String) session.getAttributes().get(SessionAttributes.uuid);
			if(userId != null && !userId.equals("")) {
				session.getAttributes().remove(userId);
			}
			if(uuid != null && !uuid.equals("")) {
				session.getAttributes().remove(uuid);
			}
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		log.info("Session established");
	}
	
	

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		super.handleBinaryMessage(session, message);
		getGameEventHandler().handleEvent(message.getPayload(), session);
	}

	
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
		log.error("Transport error", exception);
	}

	
	
	public GameEventHandler getGameEventHandler() {
		return gameEventHandler;
	}

	public void setGameEventHandler(GameEventHandler gameEventHandler) {
		this.gameEventHandler = gameEventHandler;
	}

	public SessionMap getSessionsMap() {
		return sessionsMap;
	}

	public void setSessionsMap(SessionMap sessionsMap) {
		this.sessionsMap = sessionsMap;
	}

}
