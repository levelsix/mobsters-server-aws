package com.lvl6.mobsters.binaryproto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.BinaryMessage;

import com.google.common.base.Preconditions;
import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;
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
public abstract class MobstersDecoder {
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
		// final ByteOrder originalByteOrder = buf.order();
		
		// Existing code base is only applying an explicitly set byte order to what it reads from the header.  Original byte
		// order is therefore read before the following overrides it so it can be restored before next message bytes are read.
		buf.order(
			getHeaderByteOrder()
		);
		final int reqTypeIdx  = buf.getInt();
		final int sequenceTag = buf.getInt();
		final int decodedPayloadSize = buf.getInt();
		buf.order(MobstersCodec.PAYLOAD_BYTE_ORDER);
		
		return
			encapsulateAsMessage(
				buf, decodedPayloadSize + MobstersCodec.HEADER_SIZE, decodedPayloadSize, reqTypeIdx, sequenceTag);
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

		// final ByteOrder originalByteOrder = buf.order();
		buf.order(
			getHeaderByteOrder()
		);
		final int reqTypeIdx  = buf.getInt();
		
		// TODO: Temporary--remove these after observing log output (I hope)
		final int altSequenceTag = buf.getInt();
		// final int decodedPayloadSize = buf.getInt();
		
		buf.position(MobstersCodec.HEADER_SIZE);
		buf.order(MobstersCodec.PAYLOAD_BYTE_ORDER);

		// NOTE: Intentional truncation--we do not expect to receive values that overflow an int!
		// final int sequenceTag = 
		// 	(int) delivery.getEnvelope().getDeliveryTag();
		final int encodedPayloadSize = 
			(int) delivery.getProperties().getBodySize();
		
		/*
		LOG.info(
			"decode(Delivery) found request type as {} from envelope", reqTypeIdx);
		LOG.info(
			"decode(Delivery) found sequence tag as {} from envelope and {} from binary header",
			sequenceTag, altSequenceTag);
		LOG.info(
			"decode(Delivery) found payload size as {} from envelope and {} from binary header",
			encodedPayloadSize - 12, decodedPayloadSize);
		*/
		
		return
			encapsulateAsMessage(
				buf, encodedPayloadSize, encodedPayloadSize - MobstersCodec.HEADER_SIZE, reqTypeIdx, altSequenceTag);
	}

	private Message<byte[]> encapsulateAsMessage(
		final ByteBuffer buf, 
		final int encodedPayloadSize,
		final int decodedMessageSize,
		final int reqTypeIdx, 
		final int sequenceTag
	) throws IOException {
		Preconditions.checkArgument((encodedPayloadSize - decodedMessageSize) == 12);
		
		final int availableBytes = buf.remaining();
		if (encodedPayloadSize < availableBytes) {
			// TODO: To support a server NIO buffer smaller than largest message size Mobsters supports, we could instantiate this 
			//       class once per WebSocketSession and use it to hold onto a copy of the bytes from each call to this method 
			//       until it has enough content for a complete message payload.
			LOG.warn(
				"There aren't enough bytes for the next complete payload in current frame's buffer!  encodedPayloadSize={}, availableBytes={}",
				encodedPayloadSize, availableBytes);
		} else if (decodedMessageSize > availableBytes) {
			if (decodedMessageSize > MobstersCodec.MAX_PAYLOAD_SIZE) {
				throw new IOException(
					String.format(
						"Next message's encoded payload exceeds maximum allowed size!  encodedPayloadSize=%d, availableBytes=%d, maxEncodedPayloadSize=%d",
						encodedPayloadSize, availableBytes, MobstersCodec.MAX_MESSAGE_SIZE));
			} else {
				// TODO: What we really need to do in case of this scenario is copy the bytes into an storage location of our own
				//       and remember the next place where we need to write into that array when the remaining bytes arrive.
				throw new IOException(
					String.format(
						"There are bytes from the next message frame in this frame's buffer!  encodedPayloadSize=%d, availableBytes=%d",
						encodedPayloadSize, availableBytes));
			}
		} else {
			LOG.debug(
				"Decoding a message payload of {} bytes left after removing 12 header bytes from the {} bytes recently read.",
				decodedMessageSize, encodedPayloadSize);
		}
		
		final byte[] payload = new byte[decodedMessageSize];
		buf.get(payload, 0, decodedMessageSize);
		
		final HashMap<String,List<String>> headersMap = 
			new HashMap<String,List<String>>(8);
		headersMap.put(
			MobstersHeaderAccessor.MOBSTERS_CONTENT_LENGTH_HEADER,
			Collections.singletonList(
				Integer.toString(decodedMessageSize)));
		headersMap.put(
			MobstersHeaderAccessor.MOBSTERS_REQUEST_TYPE_INDEX_HEADER,
			Collections.singletonList(
				Integer.toString(reqTypeIdx)));
		headersMap.put(
			MobstersHeaderAccessor.MOBSTERS_TAG_HEADER,
			Collections.singletonList(
				Integer.toString(sequenceTag)));
		headersMap.put(
			StompHeaderAccessor.STOMP_CONTENT_LENGTH_HEADER,
			Collections.singletonList(
				Integer.toString(decodedMessageSize)));
		headersMap.put(
			StompHeaderAccessor.STOMP_CONTENT_TYPE_HEADER,
			Collections.singletonList(
				RequestProtobufConverter.PROTO_BUF_MIME_TYPE.toString()));

		final StompHeaderAccessor mobstHeaders = 
			StompHeaderAccessor.create(StompCommand.SEND, headersMap);
		
		return
			MessageBuilder.<byte[]>withPayload(payload)
				.setHeaders(mobstHeaders)
				.build();
	}
	
	protected abstract ByteOrder getHeaderByteOrder();
	
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
	
	public static class MobstersRequestDecoder extends MobstersDecoder {

		@Override
		protected ByteOrder getHeaderByteOrder() {
			return MobstersCodec.REQUEST_HEADER_BYTE_ORDER;
		}
		
	}
	
	public static class MobstersResponseDecoder extends MobstersDecoder {

		@Override
		protected ByteOrder getHeaderByteOrder() {
			return MobstersCodec.RESPONSE_HEADER_BYTE_ORDER;
		}		
	}
}
