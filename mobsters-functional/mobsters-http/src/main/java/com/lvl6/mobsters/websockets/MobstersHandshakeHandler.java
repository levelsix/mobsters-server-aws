package com.lvl6.mobsters.websockets;

import java.security.Principal;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.auth.BasicUserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.mysql.jdbc.StringUtils;

import reactor.util.UUIDUtils;

@Component
public class MobstersHandshakeHandler extends DefaultHandshakeHandler {
	// TODO for security purposes
	public boolean isValidOrigin(ServerHttpRequest request) {
		return true;
	}

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
	protected Principal determineUser(
		ServerHttpRequest request,
		WebSocketHandler wsHandler,
		Map<String, Object> attributes)
	{
		Principal principal = request.getPrincipal();
		if ((principal == null) || (StringUtils.isEmptyOrWhitespaceOnly(principal.getName()))) {
			// It should get attached to the message headers without our doing
			// anything and so should be find just returned here.  The headers
			// are supposedly immutable by this point.
		    principal =
		    	new BasicUserPrincipal(
		    		// time-based: UUIDUtils.create()
		    		UUIDUtils.random()
		    			.toString());
		}
		
		attributes.put(
			MobstersHeaderAccessor.MOBSTERS_USER_UUID_HEADER,
			principal.getName());
		
		return principal;
	}
	
    @PostConstruct
    void init() {
    	setSupportedProtocols("lvl6-mobsters");;
    }

}
