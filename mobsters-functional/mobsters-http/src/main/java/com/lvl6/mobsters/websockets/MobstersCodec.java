package com.lvl6.mobsters.websockets;

import java.io.IOException;
import java.nio.ByteOrder;

import org.springframework.messaging.Message;
import org.springframework.web.socket.BinaryMessage;

/**
 * A paired combination of one decoder and one encoder.
 *
 * NOTE: This abstraction can break down if a decision is made to support a server
 * request buffer smaller than the largest possible message, as that situation requires
 * a Decoder per WebSocket channel, but can survive with only one Encoder for the entire
 * server.
 */
class MobstersCodec {
	static final int HEADER_SIZE = 12;
	static final int MAX_PAYLOAD_SIZE = 1024*1024;
	static final int MAX_MESSAGE_SIZE = HEADER_SIZE + MAX_PAYLOAD_SIZE;
	static final ByteOrder HEADER_BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    private final MobstersDecoder decoder = new MobstersDecoder();
    private final MobstersEncoder encoder = new MobstersEncoder();

    MobstersCodec()
    { }
    
    Message<byte[]> decode(BinaryMessage buf) throws IOException
    {
    	return decoder.decode(buf);
    }
    
    BinaryMessage encode(Message<byte[]> msg) throws IOException
    {
    	return encoder.encode(msg);
    }
}
