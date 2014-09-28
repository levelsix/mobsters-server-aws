package com.lvl6.mobsters.binaryproto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.web.socket.BinaryMessage;

import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;

/**
 * As websocket frame encoder for Mobsters Binary reply messages.
 *
 * @author John Heinnickel
 * @since 0.0.1-SNAPSHOT
 */
public abstract class MobstersEncoder 
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
		final MobstersHeaderAccessor headers =
			MobstersHeaderAccessor.wrap(message);
		final ByteBuffer buf = 
			ByteBuffer.allocateDirect(
				MobstersCodec.HEADER_SIZE
				+ message.getPayload().length);
		
		buf.order(
			getHeaderByteOrder()
		);
		buf.putInt(headers.getRequestType().getNumber());
		buf.putInt(headers.getSequenceTag());
		buf.putInt(headers.getContentLength());
		
		buf.order(MobstersCodec.PAYLOAD_BYTE_ORDER);
		buf.put(message.getPayload());

		return new BinaryMessage(
			buf.array());
	}
	
	protected abstract ByteOrder getHeaderByteOrder();
	
	public static class MobstersRequestEncoder 
		extends MobstersEncoder
	{
		protected ByteOrder getHeaderByteOrder() {
			return MobstersCodec.REQUEST_HEADER_BYTE_ORDER;
		}
	}
	
	public static class MobstersResponseEncoder 
	extends MobstersEncoder
	{
		protected ByteOrder getHeaderByteOrder() {
			return MobstersCodec.RESPONSE_HEADER_BYTE_ORDER;
		}
	}
}
