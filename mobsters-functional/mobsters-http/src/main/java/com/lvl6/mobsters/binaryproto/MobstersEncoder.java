package com.lvl6.mobsters.binaryproto;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompConversionException;
import org.springframework.web.socket.BinaryMessage;

import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;

/**
 * As websocket frame encoder for Mobsters Binary reply messages.
 *
 * @author John Heinnickel
 * @since 0.0.1-SNAPSHOT
 */
public final class MobstersEncoder 
{
	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(MobstersEncoder.class);

	/**
	 * Encodes the given STOMP {@code message} into a {@code byte[]}
	 * @param message the message to encode
	 * @return the encoded message
	 */
	public BinaryMessage encode(Message<byte[]> message) {
		// LOG.error("No error, all is well!\n");
		
		try {
			final MobstersHeaderAccessor headers =
				MobstersHeaderAccessor.wrap(message);
			final ByteArrayOutputStream baos =
				new ByteArrayOutputStream(
					MobstersCodec.HEADER_SIZE
					+ message.getPayload().length);
			final DataOutputStream output =
				new DataOutputStream(baos);

			output.write(headers.getRequestType());
			output.write(headers.getSequenceTag());
			output.write(headers.getContentLength());
			output.write(message.getPayload());

			return new BinaryMessage(
				baos.toByteArray());
		}
		catch (IOException e) {
			throw new StompConversionException("Failed to encode STOMP frame",  e);
		}
	}
}
