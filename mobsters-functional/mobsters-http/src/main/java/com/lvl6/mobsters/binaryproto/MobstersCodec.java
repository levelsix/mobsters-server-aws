package com.lvl6.mobsters.binaryproto;

import java.io.IOException;
import java.nio.ByteOrder;

import org.springframework.messaging.Message;
import org.springframework.web.socket.BinaryMessage;

import com.lvl6.mobsters.binaryproto.MobstersDecoder.MobstersRequestDecoder;
import com.lvl6.mobsters.binaryproto.MobstersDecoder.MobstersResponseDecoder;
import com.lvl6.mobsters.binaryproto.MobstersEncoder.MobstersRequestEncoder;
import com.lvl6.mobsters.binaryproto.MobstersEncoder.MobstersResponseEncoder;

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

	static final ByteOrder REQUEST_HEADER_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
	static final ByteOrder RESPONSE_HEADER_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    static final ByteOrder PAYLOAD_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
    
    private final MobstersRequestDecoder requestDecoder = new MobstersRequestDecoder();
    private final MobstersResponseDecoder responseDecoder = new MobstersResponseDecoder();
    private final MobstersRequestEncoder requestEncoder = new MobstersRequestEncoder();
    private final MobstersResponseEncoder responseEncoder = new MobstersResponseEncoder();

    MobstersCodec()
    { }
    
    Message<byte[]> decodeRequest(BinaryMessage buf) throws IOException
    {
    	return requestDecoder.decode(buf);
    }
    
    BinaryMessage encodeRequest(Message<byte[]> msg) throws IOException
    {
    	return requestEncoder.encode(msg);
    }
    
    Message<byte[]> decodeResponse(BinaryMessage buf) throws IOException
    {
    	return responseDecoder.decode(buf);
    }
    
    BinaryMessage encodeResponse(Message<byte[]> msg) throws IOException
    {
    	return responseEncoder.encode(msg);
    }
}
