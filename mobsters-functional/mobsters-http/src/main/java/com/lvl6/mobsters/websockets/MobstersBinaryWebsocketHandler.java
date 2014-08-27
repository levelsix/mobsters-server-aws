package com.lvl6.mobsters.websockets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

public class MobstersBinaryWebSocketHandler extends BinaryWebSocketHandler {
	private static final Logger LOG = LoggerFactory.getLogger(MobstersBinaryWebSocketHandler.class);

	private static final int HEADER_SIZE = 12;
	private static final int MAX_PAYLOAD_SIZE = 1024*1024;
	private static final int MAX_MESSAGE_SIZE = HEADER_SIZE + MAX_PAYLOAD_SIZE;

	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		final int availableBytes = message.getPayloadLength();
		if (availableBytes >= MAX_MESSAGE_SIZE) {
			throw new IOException(
				String.format(
					"Next event exceeds maximum supported message length. availableBytes=%d, maximumMessageSize=%d",
					availableBytes, MAX_MESSAGE_SIZE));
		} else if (availableBytes <= 12) {
			// TODO: What we really need to do in case of this scenario is copy the bytes into an storage location of our own
			//       and remember the next place where we need to write into that array when the remaining bytes arrive.
			throw new IOException(
				String.format(
					"Next event cannot fit in the number of bytes available.  Header itself requires 12 bytes. availableBytes=%d",
					availableBytes));
		}
		
		ByteBuffer buf = message.getPayload();
		buf.order(getByteOrder());
		buf.getLong();
		final int payloadSize = buf.getInt();
		final int messageSize = payloadSize + 12;

		
		if (messageSize < availableBytes) {
			// TODO: What we really need to do in case of this scenario is copy the bytes into an storage location of our own
			//       and remember the next place where we need to write into that array when the remaining bytes arrive.
			LOG.warn(
				String.format(
					"There are bytes from the next message frame in this frame's buffer!  messageSize=%d, availableBytes=%d",
					messageSize, availableBytes));
		} else if (messageSize > availableBytes) {
			if (payloadSize > MAX_PAYLOAD_SIZE) {
				throw new IOException(
					String.format(
						"Next message's payload exceeds maximum allowed size!  messageSize=%d, availableBytes=%d, maximumMessageSize=%d",
						messageSize, availableBytes, MAX_MESSAGE_SIZE));
			} else {
				// TODO: What we really need to do in case of this scenario is copy the bytes into an storage location of our own
				//       and remember the next place where we need to write into that array when the remaining bytes arrive.
				throw new IOException(
					String.format(
						"Insufficient bytes to account for next message available!  messageSize=%d, availableBytes=%d",
						messageSize, availableBytes));
			}
		} else {
			LOG.debug(
				String.format(
					"Deserialized message payload of %d bytes from %d bytes available to read",
					payloadSize, availableBytes));
		}
		
		// Hopefully routing messages through the session is the right way to get them to the outbound
		// message channel.
		//
		// TODO: The handshake handler has the responsibility of packing attributes and whatknot on
		//       the Session object and I suspect we're expected to use those Attributes here to
		//       populate enough Headers on the message to enable the construction of downstream
		//       Messages that we'll be able to deliver if and when they find themselved routed
		//       back this way for delivery.
		//
		// NOTE: A Spring-provided facility for returning a message here is by delivering it to the
		//       message broker via a MessagingTemplate injected dependency.  That facility depends
		//       on a HandshakeHandler being used to populate the source message from which downstream
		//       messages are derived with a USER_HEADER entry linked to a Principal from the
		//       spring-security package.  If we can idenitify how we want to identify a user from the
		//       HTTP request context, we've got what we need...!
		byte[] messageBytes = new byte[payloadSize];
		buf.get(messageBytes, 0, payloadSize);
		session.sendMessage(
			new BinaryMessage(messageBytes));
	}

	protected static ByteOrder getByteOrder() {
		return ByteOrder.BIG_ENDIAN;
	}


	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
