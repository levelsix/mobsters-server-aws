package com.lvl6.mobsters.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.events.GameEvent;
import com.lvl6.mobsters.events.ResponseEvent;

public abstract class EventWriter {

	
	private static Logger log = LoggerFactory.getLogger(EventWriter.class);
	
	
	public EventWriter() {

	}

	public void writeEvent(GameEvent event) {
		try {
			processEvent(event);
		} catch (Throwable e) {
			log.error("Error handling event: {}", event, e);
		}
	}
	
	
	public abstract void processGlobalChatResponseEvent(ResponseEvent event);

	
	
	
	public void handleClanEvent(ResponseEvent event, int clanId) {
		try {
			processClanResponseEvent(event, clanId);
		} catch (Throwable e) {
			log.error("Error handling clan event: " + event, e);
		}
	}
	
	
	public abstract void processClanResponseEvent(ResponseEvent event, int clanId);
	

	
	
	protected abstract void processEvent(GameEvent event) throws Exception;
	
	public abstract void processPreDBResponseEvent(ResponseEvent event, String udid);
	
	public abstract void processPreDBFacebookEvent(ResponseEvent event, String fbId);
	//public abstract void sendAdminMessage(String message);
	
}