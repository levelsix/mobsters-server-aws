package com.lvl6.mobsters.controllers;

import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.server.EventController;


@Component
public class ControllerManager {

	
	private static final Logger log = LoggerFactory.getLogger(ControllerManager.class);
	
	@Autowired
	protected List<EventController> eventControllerList;

	public void setEventControllerList(List<EventController> eventControllerList) {
		this.eventControllerList = eventControllerList;
	}

	protected Hashtable<EventProtocolRequest, EventController> eventControllers;
	
	
	public EventController getEventControllerByEventType(EventProtocolRequest eventType) {
		if (eventType == null) {
			throw new RuntimeException("EventProtocolRequest (eventType) is null");
		}
		if (eventControllerList.size() > eventControllers.size()) {
			loadEventControllers();
		}
		if (eventControllers.containsKey(eventType)) {
			EventController ec = eventControllers.get(eventType);
			if (ec == null) {
				log.error("no eventcontroller for eventType: " + eventType);
				throw new RuntimeException("EventController of type: " + eventType + " not found");
			}
			return ec;
		}
		throw new RuntimeException("EventController of type: " + eventType + " not found");
	}

	/**
	 * Dynamically loads GameControllers
	 */
	protected void loadEventControllers() {
		log.info("Adding event controllers to eventControllers controllerType-->controller map");
		for (EventController ec : eventControllerList) {
			eventControllers.put(ec.getEventType(), ec);
		}
	}
	
}
