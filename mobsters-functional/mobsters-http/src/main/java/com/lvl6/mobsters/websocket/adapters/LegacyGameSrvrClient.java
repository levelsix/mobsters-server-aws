package com.lvl6.mobsters.websocket.adapters;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompConversionException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;

public class LegacyGameSrvrClient {
    private final SimpMessagingTemplate brokerMessageTemplate;
    
    private static final int HEADER_SIZE = 12;
    private static final Pattern DESTINATION_PATTERN = 
    	Pattern.compile("^/app/gameserver/(\\d+)$");

    LegacyGameSrvrClient( SimpMessagingTemplate brokerMessageTemplate )
    {
    	this.brokerMessageTemplate = brokerMessageTemplate;
    }
    
    public void sendRequest( Message<com.google.protobuf.Message> request )
    {
    	final com.google.protobuf.Message payload =
    		request.getPayload();
    	final byte[] payloadBytes = 
    		payload.toByteArray();
    	final int payloadSize = 
    		payloadBytes.length;
    	
    	try {
			final StompHeaderAccessor headers =
				StompHeaderAccessor.wrap(request);
			final MobstersHeaderAccessor mobsterHeaders =
				MobstersHeaderAccessor.wrap(headers);
			final int tag = 
				mobsterHeaders.getSequenceTag();
			final Matcher m = 
				DESTINATION_PATTERN.matcher(
					headers.getDestination());
			final int reqIndex =
				Integer.parseInt(
					m.group(1));
			
			final ByteArrayOutputStream baos =
				new ByteArrayOutputStream(HEADER_SIZE + payloadSize);
			final DataOutputStream output =
				new DataOutputStream(baos);

			output.write(reqIndex);
			output.write(tag);
			output.write(payloadSize);
			output.write(payloadBytes);

			brokerMessageTemplate.convertAndSend("/queue/clientMessages", payloadBytes);
		} catch (IOException e) {
			throw new StompConversionException("Failed to encode STOMP frame",  e);
		}
    }
}
