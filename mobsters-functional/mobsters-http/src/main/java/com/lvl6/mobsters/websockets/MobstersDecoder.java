package com.lvl6.mobsters.websockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.BinaryMessage;

import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * Decodes exactly one Mobsters Binary frame contained in a {@link ByteBuffer}.
 *
 * <p>An attempt is made to detect complete request message frames from the buffer, 
 * which could be zero, one, or more.  If there is any left-over content, i.e. an
 * incomplete Mobsters Binary request frame, or more than one request event available,
 * this decoder fails and closes the session.
 *
 * @author John Heinnickel
 * @since 0.0.1-SNAPSHOT
 */
public class MobstersDecoder {
	private static final Logger LOG = 
		LoggerFactory.getLogger(MobstersDecoder.class);

	/**
	 * Decode a single Mobsters RequestMessage from an input BinaryMessage and returns
	 * it.  If the BinaryMessage does not contain a complete message, this instead throws
	 * an IOException.
	 *
	 * @param msg The STOMP message with a binary payload to decode as a Mobsters 
	 *             request Message.
	 *
	 * @return the decoded messages or an empty list
	 * @throws IOException 
	 */
	public Message<byte[]> decode( final BinaryMessage msg ) throws IOException 
	{
		final ByteBuffer buf = msg.getPayload();
		final ByteOrder originalByteOrder = buf.order();
		
		// Existing code base is only applying an explicitly set byte order to what it reads from the header.  Original byte
		// order is therefore read before the following overrides it so it can be restored before next message bytes are read.
		buf.order(MobstersCodec.HEADER_BYTE_ORDER);
		final int reqTypeIdx  = buf.getInt();
		final int sequenceTag = buf.getInt();
		final int payloadSize = buf.getInt();
		buf.order(originalByteOrder);
		
		return
			encapsulateAsMessage(buf, payloadSize, reqTypeIdx, sequenceTag);
	}
	
	/**
	 * Decode a single Mobsters RequestMessage from an input AMQP Delivery and returns
	 * it.  If the Delivery does not contain a complete message, this instead throws
	 * an IOException.
	 *
	 * @param msg The AMQP Delivery for decoding to a Mobsters request Message.
	 *
	 * @return the decoded messages or an empty list
	 * @throws IOException 
	 */
	public Message<byte[]> decode( final Delivery delivery ) throws IOException
	{
		final byte[] binaryBody = delivery.getBody();
		final ByteBuffer buf = ByteBuffer.wrap(binaryBody);

		final ByteOrder originalByteOrder = buf.order();
		final int reqTypeIdx  = buf.getInt();
		
		// TODO: Temporary--remove these after observing log output (I hope)
		final int altSequenceTag = buf.getInt();
		final int altPayloadSize = buf.getInt();
		
		buf.position(MobstersCodec.HEADER_SIZE);
		buf.order(originalByteOrder);

		final long sequenceTag = delivery.getEnvelope().getDeliveryTag();
		final long payloadSize = delivery.getProperties().getBodySize();
		
		LOG.info(
			"decode(Delivery) found sequence tag as {} from envelope and {} from binary header",
			sequenceTag, altSequenceTag);
		LOG.info(
			"decode(Delivery) found payload size as {} from envelope and {} from binary header",
			payloadSize, altPayloadSize);
			
		return
			encapsulateAsMessage(buf, reqTypeIdx, payloadSize, sequenceTag);
	}

	private Message<byte[]> encapsulateAsMessage(
		final ByteBuffer buf, 
		final int decodedPayloadSize, 
		final long reqTypeIdx, 
		final long sequenceTag
	) throws IOException {
		final int availableBytes = buf.remaining();
		final int messageSize    = 
			decodedPayloadSize + MobstersCodec.HEADER_SIZE;
		
		if (messageSize < availableBytes) {
			// TODO: To support a server NIO buffer smaller than largest message size Mobsters supports, we could instantiate this 
			//       class once per WebSocketSession and use it to hold onto a copy of the bytes from each call to this method 
			//       until it has enough content for a complete message payload.
			LOG.warn(
				String.format(
					"There are bytes from the next message frame in this frame's buffer!  messageSize={}, availableBytes={}",
					messageSize, availableBytes));
		} else if (messageSize > availableBytes) {
			if (decodedPayloadSize > MobstersCodec.MAX_PAYLOAD_SIZE) {
				throw new IOException(
					String.format(
						"Next message's payload exceeds maximum allowed size!  messageSize={}, availableBytes={}, maximumMessageSize={}",
						messageSize, availableBytes, MobstersCodec.MAX_MESSAGE_SIZE));
			} else {
				// TODO: What we really need to do in case of this scenario is copy the bytes into an storage location of our own
				//       and remember the next place where we need to write into that array when the remaining bytes arrive.
				throw new IOException(
					String.format(
						"Insufficient bytes to account for next message available!  messageSize={}, availableBytes={}",
						messageSize, availableBytes));
			}
		} else {
			LOG.debug(
				String.format(
					"Decoding a message payload of {} bytes from the {} bytes that were available for reading.",
					decodedPayloadSize, availableBytes));
		}
		
		final byte[] payload = new byte[decodedPayloadSize];
		buf.get(payload, 0, decodedPayloadSize);

		return MessageBuilder.<byte[]>withPayload(payload)
		.setHeader(MobstersHeaderAccessor.MOBSTERS_REQUEST_TYPE_INDEX_HEADER, reqTypeIdx)
		.setHeader(MobstersHeaderAccessor.MOBSTERS_TAG_HEADER, sequenceTag)
		.setHeader("Content-Type", ProtoBufConverter.PROTO_BUF_MIME_TYPE)
		.setHeader("Content-Length", decodedPayloadSize)
		.build();
	}
	
	/*
	 * TODO: After switching off legacy game server, it may be possible to include the 
	 *       message type index in the destination path.  This will extract it from therein.
	 */
	private static final Pattern DESTINATION_PATTERN = 
		Pattern.compile("^(/app)?/gameserver/(\\d+)$");
	
	@SuppressWarnings("unused")
	private static int getRequestTypeFromDestination(
		StompHeaderAccessor headers
	) {
		return 
			Integer.parseInt(
				DESTINATION_PATTERN.matcher(
					headers.getDestination()
				).group(2));
	}
}
