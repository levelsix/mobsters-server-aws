package com.lvl6.mobsters.websockets;

import java.security.Principal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.google.common.base.Preconditions;
import com.lvl6.mobsters.websockets.MobstersPlayerPrincipal.UserIdentityType;
import com.mysql.jdbc.StringUtils;

@Component
public class MobstersHandshakeHandler extends DefaultHandshakeHandler {
	private static final Logger LOG = 
		LoggerFactory.getLogger(MobstersHandshakeHandler.class);
	
	// TODO for security purposes
	public boolean isValidOrigin(ServerHttpRequest request) {
		return super.isValidOrigin(request);
	}

	private static final Pattern PROTOCOL_PATTERN = 
		Pattern.compile("^mobsters\\.(userid|udid)\\.(.*)$");
	
	/**
	 * A method that can be used to associate a user with the WebSocket session
	 * in the process of being established. The default implementation calls
	 * {@link org.springframework.http.server.ServerHttpRequest#getPrincipal()}
	 * <p>2
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
		String principalName = null;
		UserIdentityType idType = null;
		WebSocketHttpHeaders headers =
			new WebSocketHttpHeaders(
				request.getHeaders());
		
		for (final String nextProto : headers.getSecWebSocketProtocol()) {
			Matcher m = PROTOCOL_PATTERN.matcher(nextProto);
			if (m.matches()) {
				final String idTypeStr = m.group(1);
				principalName = m.group(2);
				if (idTypeStr.equals("userid")) {
					idType = UserIdentityType.USER_ID;
				} else {
					idType = UserIdentityType.DEVICE_UDID;
				}
				
			    // setSupportedProtocols("v10.stomp", "v11.stomp", "v12.stomp");
				break;
			}
		}

		LOG.info(
			String.format("WebSocketSession owner type: <%s>, and name: <%s>", idType, principalName));
		
		Preconditions.checkNotNull(idType);
		Preconditions.checkNotNull(principalName);
		Preconditions.checkArgument(
			! StringUtils.isEmptyOrWhitespaceOnly(principalName));
		
		attributes.put(
				MobstersHeaderAccessor.MOBSTERS_PLAYER_TYPE_HEADER, idType);
		attributes.put(
				MobstersHeaderAccessor.MOBSTERS_PLAYER_ID_HEADER, principalName);
		
		return new MobstersPlayerPrincipal(principalName, idType);
	}	
}
