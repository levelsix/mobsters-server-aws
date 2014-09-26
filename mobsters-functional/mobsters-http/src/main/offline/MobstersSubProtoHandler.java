package com.lvl6.mobsters.websockets;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.DestinationUserNameProvider;
import org.springframework.messaging.simp.user.UserSessionRegistry;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.SessionLimitExceededException;
import org.springframework.web.socket.messaging.SubProtocolHandler;

public class MobstersSubProtoHandler implements SubProtocolHandler, ApplicationEventPublisherAware
{
	private static final Logger LOG = LoggerFactory.getLogger(MobstersSubProtoHandler.class);

	private final MobstersCodec codec = new MobstersCodec();

	private ApplicationEventPublisher eventPublisher = null;

	@Autowired
	private UserSessionRegistry userSessionRegistry = null;
	
	/**
	 * 
	 * Handle incoming WebSocket messages from clients.
	 */
	public void handleMessageFromClient(
		WebSocketSession session,
		WebSocketMessage<?> webSocketMessage,
		MessageChannel outputChannel)
	{
		Assert.isInstanceOf(BinaryMessage.class,  webSocketMessage);

		Message<byte[]> message = null;
		try {
			message = codec.decode((BinaryMessage) webSocketMessage);
		} catch (Throwable e) {
			sendErrorMessage(session, e, CloseStatus.PROTOCOL_ERROR);
			return;
		}

		final SimpMessageHeaderAccessor headers =
			SimpMessageHeaderAccessor.wrap(message);
		headers.setSessionId(
			session.getId());
		headers.setSessionAttributes(
			session.getAttributes());
		headers.setUser(
			session.getPrincipal());

		/*
		 * TODO!
		if (this.eventPublisher != null && StompCommand.CONNECT.equals(headers.getCommand())) {
			publishEvent(new SessionConnectEvent(this, message));
		}
		*/

		outputChannel.send(message);
	}

	/**
	 * Handle STOMP messages going back out to WebSocket clients.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessageToClient(WebSocketSession session, Message<?> message) {
		if (message.getPayload() instanceof Throwable) {
			sendErrorMessage(session, (Throwable) message.getPayload(), CloseStatus.SERVER_ERROR);
		} else if (!(message.getPayload() instanceof byte[])) {
			LOG.error("Ignoring message, expected byte[] content: %s", message);
			return;
		}

		/*
		 * These will never trigger because we are not using Stomp headers in this variant
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECTED.equals(headers.getCommand())) {
			afterStompSessionConnected(headers, session);
		}
		
		if (StompCommand.MESSAGE.equals(headers.getCommand())) {
			if (headers.getSubscriptionId() == null) {
				LOG.error("Ignoring message, no subscriptionId header: %s", message);
				return;
			}
			String name = UserDestinationMessageHandler.SUBSCRIBE_DESTINATION;
			String origDestination = headers.getFirstNativeHeader(name);
			if (origDestination != null) {
				headers.setDestination(origDestination);
			}
		}
		 */

		try {
			// message = MessageBuilder.withPayload(message.getPayload()).setHeaders(headers).build();

			/* TODO: Is this needed as part of a change of user authority?
			if (this.eventPublisher != null && StompCommand.CONNECTED.equals(headers.getCommand())) {
				publishEvent(new SessionConnectedEvent(this, (Message<byte[]>) message));
			}
			*/

			session.sendMessage(
				this.codec.encode((Message<byte[]>) message));
		}
		catch (SessionLimitExceededException ex) {
			// Bad session, just get out
			throw ex;
		}
		catch (Throwable ex) {
			sendErrorMessage(session, ex, CloseStatus.PROTOCOL_ERROR);
		}
		finally {
//			if (StompCommand.ERROR.equals(headers.getCommand())) {
//				try {
//					session.close(CloseStatus.PROTOCOL_ERROR);
//				}
//				catch (IOException ex) {
//					// Ignore
//				}
//			}
		}
	}


	@Override
	public List<String> getSupportedProtocols() {
		return Collections.singletonList("mobsters-ws10");
	}


	@Override
	public String resolveSessionId(Message<?> message) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
		return headers.getSessionId();
	}

	@Override
	public void afterSessionStarted(WebSocketSession session, MessageChannel outputChannel) {
		if (session.getBinaryMessageSizeLimit() < MobstersCodec.MAX_MESSAGE_SIZE) {
			session.setBinaryMessageSizeLimit(MobstersCodec.MAX_MESSAGE_SIZE);
		}
		
		// TODO: This is part of handling a large message that has been broken down to frames.
		// this.decoders.put(session.getId(), new BufferingStompDecoder(getMessageSizeLimit()));

		Principal principal = session.getPrincipal();
		if ((principal != null) && (this.userSessionRegistry != null)) {
			this.userSessionRegistry.registerSessionId(
				resolveNameForUserSessionRegistry(principal),
				session.getId());
		}
	}

	@Override
	public void afterSessionEnded(WebSocketSession session, CloseStatus closeStatus, MessageChannel brokerChannel) {
        // TODO: Part of accepting partial messages.
		// this.decoders.remove(session.getId());

		Principal principal = session.getPrincipal();
		if ((this.userSessionRegistry != null) && (principal != null)) {
			String userName = resolveNameForUserSessionRegistry(principal);
			this.userSessionRegistry.unregisterSessionId(userName, session.getId());
		}

		/* TODO: Is this needed as part of a change of user authority?
		if (this.eventPublisher != null) {
			publishEvent(new SessionDisconnectEvent(this, session.getId(), closeStatus));
		}
		 */

		LOG.debug("WebSocket session ended, sending DISCONNECT message to broker");
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		headers.setSessionId(session.getId());
		Message<?> message = MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build();

		brokerChannel.send(message);
	}

	private String resolveNameForUserSessionRegistry(final Principal principal) {
		String userName = principal.getName();
		if (principal instanceof DestinationUserNameProvider) {
			userName = ((DestinationUserNameProvider) principal).getDestinationUserName();
		}
		return userName;
	}
	
	// TODO: General purpose Exception handling goes here, but is not yet implemented.
	private final byte[] TEMPORARY_ERROR_BYTES_PLACEHOLDER = 
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 85, -16, 15, -86 };
	private final Message<byte[]> TEMPORARY_ERROR_MSG_PLACEHOLDER =
		MessageBuilder.withPayload(TEMPORARY_ERROR_BYTES_PLACEHOLDER)
		.build();

	protected void sendErrorMessage(WebSocketSession session, Throwable error, CloseStatus closeStatus) {
		// StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.ERROR);
		// headers.setMessage(error.getMessage());
		// Message<byte[]> message =
		// 	MessageBuilder.withPayload(new byte[0])
		// 	.setHeaders(headers).build();

		// TODO: Place a WireTap on the clientOutboundChannel to:
		//       Intercept the post-conversion Request Protobuf
		//       Use reflection to access its class name
		//       Replace "Request" with "Response" 
		//       Allocate a ResponseProto.Builder
		//       Find the Response class's StatusEnum
		//       Map the Lvl6MonstersStatusCode to the Response Class's enum
		//       Use the builder to get a Proto with the error status set
		//       Convert it to a byte array wrapped in a Message for encoding
		if (closeStatus == CloseStatus.PROTOCOL_ERROR) {
			LOG.error("A protocol error prevented a message from being processed.", error);
		} else {
			LOG.error("A server error prevented a message from being processed.", error);
		}
		
		try {
			final BinaryMessage msg =
				this.codec.encode(TEMPORARY_ERROR_MSG_PLACEHOLDER);
			session.sendMessage(msg);
		}
		catch (Throwable ex) {
			// ignore
			LOG.error(
				"Unexpected exception sending previous error message.  Message=%s\n%s",
				ex.getMessage(),
				Arrays.toString(ex.getStackTrace()));
		} finally {
			try {
				session.close(closeStatus);
			} catch (Throwable e2) {
				// Ignore
				LOG.error("Second exception thrown while closing session on error condition", e2);
			}
		}
	}

	@SuppressWarnings("unused")
	private void publishEvent(ApplicationEvent event) {
		try {
			this.eventPublisher.publishEvent(event);
		}
		catch (Throwable ex) {
			LOG.error("Error while publishing <" + event + '>', ex);
		}
	}

	@Override
	public void setApplicationEventPublisher(
		ApplicationEventPublisher applicationEventPublisher)
	{
		this.eventPublisher  = applicationEventPublisher;
	}

	void setUserSessionRegistry(UserSessionRegistry userSessionRegistry) {
		this.userSessionRegistry = userSessionRegistry;
	}
}
