package com.lvl6.mobsters.websockets;

import java.nio.ByteBuffer;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class GameEventEndpoint extends Endpoint {

	
	
	@Override
	public void onOpen(final Session session, EndpointConfig config) {
		session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

			@Override
			public void onMessage(ByteBuffer bytes) {
				
			}
		});
	}

	
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		// TODO Auto-generated method stub
		super.onClose(session, closeReason);
	}

	
	@Override
	public void onError(Session session, Throwable thr) {
		// TODO Auto-generated method stub
		super.onError(session, thr);
	}
	
	

}
