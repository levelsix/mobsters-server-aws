package com.lvl6.eventdispatcher;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventsFromOtherServersDispatcher  {

	
	@Autowired
	protected WebSocketEventDispatcher webSocketsEventDispatcher;
	

	protected void handleNormalEvent(Message msg){
		//Map<String, Object> headers = msg.getMessageProperties().getHeaders();
		String userId = msg.getMessageProperties().getUserId();
		webSocketsEventDispatcher.dispatchNormalEvent(userId, msg.getBody());
	}

	protected void handlePreDatabaseEvent(Message msg){
		String userId = msg.getMessageProperties().getUserId();
		webSocketsEventDispatcher.dispatchPreDatabaseEvent(userId, msg.getBody());
	}
	
	protected void handleBroadcastEvent(Message msg){
		String userId = msg.getMessageProperties().getUserId();
		webSocketsEventDispatcher.dispatchBroadcastEvent(userId, msg.getBody());
	}


	public WebSocketEventDispatcher getWebSocketsEventDispatcher() {
		return webSocketsEventDispatcher;
	}

	public void setWebSocketsEventDispatcher(WebSocketEventDispatcher webSocketsEventDispatcher) {
		this.webSocketsEventDispatcher = webSocketsEventDispatcher;
	}

}
