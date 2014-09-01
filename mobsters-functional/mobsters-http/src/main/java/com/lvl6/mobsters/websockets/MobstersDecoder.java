package com.lvl6.mobsters.websockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.BinaryMessage;

/**
 * Decodes one or more STOMP frames contained in a {@link ByteBuffer}.
 *
 * <p>An attempt is made to read all complete STOMP frames from the buffer, which
 * could be zero, one, or more. If there is any left-over content, i.e. an incomplete
 * STOMP frame, at the end the buffer is reset to point to the beginning of the
 * partial content. The caller is then responsible for dealing with that
 * incomplete content by buffering until there is more input available.
 *
 * @author Andy Wilkinson
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class MobstersDecoder {
	private final static Logger LOG = LoggerFactory.getLogger(MobstersDecoder.class);

	/**
	 * Decode a single Mobsters RequestMessage from an input ByteBuffer and returns
	 * it.  If the ByteBuffer does not contain a complete message, it instead throws
	 * an IOException.
	 *
	 * @param buffer The buffer to decode a Mobsters RequestMessage
	 *
	 * @return the decoded messages or an empty list
	 * @throws IOException 
	 */
	public Message<byte[]> decode(BinaryMessage msg) throws IOException {
		final ByteBuffer buf = msg.getPayload();
		final int availableBytes = buf.remaining();
		final ByteOrder originalByteOrder = buf.order();
		
		// Existing code base is only applying an explicitly set byte order to what it reads from the header.  Original byte
		// order is therefore read before the following overrides it so it can be restored before next message bytes are read.
		buf.order(MobstersCodec.HEADER_BYTE_ORDER);
		buf.getLong();
		final int payloadSize = buf.getInt();
		final int messageSize = payloadSize + 12;

		if (messageSize < availableBytes) {
			// TODO: To support a server NIO buffer smaller than largest message size Mobsters supports, we could instantiate this 
			//       class once per WebSocketSession and use it to hold onto a copy of the bytes from each call to this method 
			//       until it has enough content for a complete message payload.
			LOG.warn(
				String.format(
					"There are bytes from the next message frame in this frame's buffer!  messageSize=%d, availableBytes=%d",
					messageSize, availableBytes));
		} else if (messageSize > availableBytes) {
			if (payloadSize > MobstersCodec.MAX_PAYLOAD_SIZE) {
				throw new IOException(
					String.format(
						"Next message's payload exceeds maximum allowed size!  messageSize=%d, availableBytes=%d, maximumMessageSize=%d",
						messageSize, availableBytes, MobstersCodec.MAX_MESSAGE_SIZE));
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
					"Decoding a message payload of %d bytes from the %d bytes that were available for reading.",
					payloadSize, availableBytes));
		}
		
		final byte[] payload = new byte[payloadSize];
		buf.order(originalByteOrder);
		buf.get(payload, 0, payloadSize);

		return
			MessageBuilder.<byte[]>withPayload(payload)
			.build();
	}
}
