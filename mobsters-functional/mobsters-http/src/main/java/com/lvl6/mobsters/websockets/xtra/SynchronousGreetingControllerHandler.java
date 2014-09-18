package com.lvl6.mobsters.websockets.xtra;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.lvl6.mobsters.utils.MobstersPlayerPrincipal;
import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;

@Controller
public class SynchronousGreetingControllerHandler
{
	@Autowired
	SimpMessagingTemplate messageTemplate;

//	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(SynchronousGreetingControllerHandler.class);

    @MessageMapping("/hello2")
    public void greetingWithReplyTo(HelloMessage payload, Message<?> message) 
        throws Exception
    {
        	LOG.error("In corrected /hello handler");
        	Greeting payloadOut =
        		new Greeting(
        			String.format(
        				"Oh, hello %s!",
        				payload.getName()
        				.toUpperCase()
        			)
        		);
        	
        	MobstersHeaderAccessor headersIn =
        		MobstersHeaderAccessor.wrap(message);
        	
        	StompHeaderAccessor stompHeadersOut =
        		StompHeaderAccessor.create(StompCommand.SEND);
        	MobstersHeaderAccessor mobstersHeadersOut =
        		MobstersHeaderAccessor.wrap(stompHeadersOut);
        	
//        	stompHeadersOut.setDestination(
//        		headersIn.getReplyTo()
//        	);
        	mobstersHeadersOut.setReplyTo(
        		headersIn.getReplyTo()
        	);
        	stompHeadersOut.setSessionId(
        		headersIn.accessStompHeaders().getSessionId()
        	);
        	mobstersHeadersOut.setPlayer(
        		headersIn.getPlayer()
        	);
        	
        	messageTemplate.convertAndSend(
        		"/amq-queue/gameEvents", payloadOut, stompHeadersOut.toMap());
    }

    @MessageMapping("/hello")
    @SendToUser({ "/queue/freetings", "/exchange/gamemessages/freetings" })
    public Greeting greeting(HelloMessage payload, Message<?> message) 
    	throws Exception
    {
    	LOG.error("In corrected /hello handler");
    	Greeting payloadOut =
    		new Greeting(
    			String.format(
    				"Oh, hello %s!",
    				payload.getName()
    				.toUpperCase()
    			)
    		);
    	
    	MobstersHeaderAccessor headersIn =
    		MobstersHeaderAccessor.wrap(message);
    	
    	StompHeaderAccessor stompHeadersIn = headersIn.accessStompHeaders();
    	LOG.info(
    		String.format(
    			"Headers from request: %s", stompHeadersIn.toMap()));
    	LOG.info(
    		String.format(
    			"Headers from request alternatives:\nNative: %s",
    			stompHeadersIn.toNativeHeaderMap()));
    	
    	StompHeaderAccessor stompHeadersOut =
    		StompHeaderAccessor.create(StompCommand.SEND);
    	MobstersHeaderAccessor mobstersHeadersOut =
    		MobstersHeaderAccessor.wrap(stompHeadersOut);
    	
    	final MobstersPlayerPrincipal playerPrincipal = 
    		headersIn.getPlayer();
    	mobstersHeadersOut.setPlayer(playerPrincipal);
    	stompHeadersOut.setSessionId(
    		headersIn.accessStompHeaders().getSessionId()
    	);
    	
    	Map<String,Object> headersOut = stompHeadersOut.toMap();
    	LOG.info(
    		String.format("Headers for reply: %s", headersOut));
    	LOG.info(
    		String.format(
    			"Header alternatives:\nNative: %s\n0",
    			stompHeadersOut.toNativeHeaderMap()));
    	
    	messageTemplate.convertAndSend(
        	"/queue/greetings_" + playerPrincipal.getName(), payloadOut, headersOut);
    	messageTemplate.convertAndSend(
        	"/exchange/gamemessages/greetings_" + playerPrincipal.getName(), payloadOut, headersOut);
    	return payloadOut;
    }
}
