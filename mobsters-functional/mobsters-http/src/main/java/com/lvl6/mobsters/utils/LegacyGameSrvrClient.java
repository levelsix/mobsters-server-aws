package com.lvl6.mobsters.utils;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.BinaryMessage;

import com.lvl6.mobsters.websockets.MobstersEncoder;

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
    private final MobstersEncoder encoder;
    
    private static final int HEADER_SIZE = 12;

    LegacyGameSrvrClient( SimpMessagingTemplate brokerMessageTemplate )
    {
    	this.brokerMessageTemplate = brokerMessageTemplate;
    	this.encoder = new MobstersEncoder();
    }
    
    /**
     * Encodes a 
     * @param request
     */
    public void sendRequest( final Message<byte[]> request )
    {
		final BinaryMessage legacyPayload = encoder.encode(request);

		brokerMessageTemplate.convertAndSend("/amq-queue/clientMessages", legacyPayload);
    }
}
