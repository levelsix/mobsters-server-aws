package com.lvl6.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class WebSocketService {
	private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);
	
	
	protected static ConcurrentHashMap<String, Session> websocketSessions;
	
	public void addSession(String userId, Session session) {
		log.info("Adding session for user {}", userId);
		websocketSessions.put(userId, session);
	}
	
	public void removeSession(String userId) {
		log.info("Removing session for user {}", userId);
	}
	
	public void sendMessageToUser(String userId, ByteBuffer bytes) {
		Session session = websocketSessions.get(userId);
		if(session != null) {
			if(session.isOpen()) {
				session.getAsyncRemote().sendBinary(bytes);
			}else {
				//TODO: handle closed sessions
			}
		}else {
			log.error("Session for user {} not found", userId);
		}
	}
	
	
	public void sendMessageToUsers(Collection<String> userIds, ByteBuffer bytes) {
		for(String userId : userIds) {
			sendMessageToUser(userId, bytes);
		}
	}
	
	
	public void pingAllOpenSessions() {
		Long time = System.currentTimeMillis();
		for(Session session : websocketSessions.values()) {
			try {
				ByteBuffer buff = ByteBuffer.allocate(Long.SIZE);
				buff.putLong(time);
				session.getAsyncRemote().sendPing(buff);
			} catch (IllegalArgumentException e) {
				log.error("Message to large for ping", e);
			} catch (IOException e) {
				log.error("Ping failed", e);
			}
		}
	}
	
}
