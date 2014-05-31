package com.lvl6.mobsters.websockets;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


@Component
public class SessionMap extends ConcurrentHashMap<String, WebSocketSession> {

	private static final long serialVersionUID = 1L;
	
	

}
