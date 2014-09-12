package com.lvl6.mobsters.websocket.adapters;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;

/**
 * Messaging client interface that uses the MessagingTemplate encapsulating the to-Broker channel of a
 * Spring STOMP relay over WebSocket component.
 * 
 * TODO: I think this is more aptly considered to be a Gateway than an Adapter, in Spring terms.  Consider
 *       renaming and re-wiring for clarity and a more suitable implementation-oriented feature set.
 *       
 * @author jheinnic
 *
 */
public class LegacyGameSrvrClient {
    private final SimpMessagingTemplate brokerMessageTemplate;
    
    private static final int HEADER_SIZE = 12;
    private static final Pattern DESTINATION_PATTERN = 
    	Pattern.compile("^(/app)?/gameserver/(\\d+)$");

    LegacyGameSrvrClient( SimpMessagingTemplate brokerMessageTemplate )
    {
    	this.brokerMessageTemplate = brokerMessageTemplate;
    }
    
    public void sendRequest( Message<com.google.protobuf.Message> request )
    {
    	final com.google.protobuf.Message payload = request.getPayload();
    	final byte[] payloadBytes = payload.toByteArray();
    	final int payloadSize = payloadBytes.length;
    	
		final StompHeaderAccessor headers =
			StompHeaderAccessor.wrap(request);
		final MobstersHeaderAccessor mobsterHeaders =
			MobstersHeaderAccessor.wrap(headers);

		final int tag = mobsterHeaders.getSequenceTag();

		final Matcher m = 
			DESTINATION_PATTERN.matcher(
				headers.getDestination());
		final int reqIndex =
			Integer.parseInt(
				m.group(2));
		
		final byte[] legacyPayload = new byte[HEADER_SIZE + payloadSize];
		final ByteBuffer output = ByteBuffer.wrap(legacyPayload);

		output.putInt(reqIndex);
		output.putInt(tag);
		output.putInt(payloadSize);
		output.put(payloadBytes);

		brokerMessageTemplate.convertAndSend("/amq-queue/clientMessages", legacyPayload);
    }
}
