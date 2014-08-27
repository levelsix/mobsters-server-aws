package com.lvl6.mobsters.websockets;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class MobstersHandshakeHandler extends DefaultHandshakeHandler {
	/**
	 * A method that can be used to associate a user with the WebSocket session
	 * in the process of being established. The default implementation calls
	 * {@link org.springframework.http.server.ServerHttpRequest#getPrincipal()}
	 * <p>
	 * Sub-classes can provide custom logic for associating a user with a session,
	 * for example for assigning a name to anonymous users (i.e. not fully
	 * authenticated).
	 *
	 * @param request the handshake request
	 * @param wsHandler the WebSocket handler that will handle messages
	 * @param attributes handshake attributes to pass to the WebSocket session
	 *
	 * @return the user for the WebSocket session or {@code null}
	 */
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {

		return request.getPrincipal();
	}

}
