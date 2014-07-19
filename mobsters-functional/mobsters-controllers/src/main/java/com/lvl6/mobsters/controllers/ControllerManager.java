package com.lvl6.mobsters.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.server.EventController;


@Component
public class ControllerManager {
	private static final Logger log = LoggerFactory.getLogger(ControllerManager.class);
	
	@Autowired
	protected List<EventController> eventControllerList;

	public void setEventControllerList(final List<EventController> eventControllerList) {
		this.eventControllerList = eventControllerList;
	}

	// protected Hashtable<EventProtocolRequest, EventController> eventControllers;
	private final AtomicReference<EventControllerMap> eventControllers =
		new AtomicReference<EventControllerMap> (
			new EventControllerMap()
		);
	
	// To leverage the Java memory model's visibility guarantee on immutable objects, all state changes required
	// to configure them _must_ happen during a constructor invocation.  Therefore, in order to have the 
	// lock-free visibility guarantee involving an immutable HashMap, that HashMap must be encapsulated within a
	// class that does a one-time population loop from within its constructor.  This immutable wrapper class facilitates
	// reducing the cost of looking up an EventController from three lock acquire/release cycles on a single 
	// synchronized mutex down to one volatile read in the common case where the map has already been built.
	//
	// TODO: Evaluate whether eventControllerList can manifest additional entries after the first time it is
	//       used to build a lookup map.  If it cannot, and threads perform multiple EventControllerMap lookups in 
	//       their lifetime, then it is even possible to do away with the volatile read cost by using ThreadLocal
	//       to capture the map after its is first looked up.  If the map can change at runtime after its initial
	//       build, then preventing future thread-safe atomic get() calls is not viable.
	
	//NOTE: This map is only is only written to once during spring initialization. After that it is read only. Making it atomic was unnecessary.
	private static final class EventControllerMap {
		private final HashMap<EventProtocolRequest, EventController> eventControllerMap;
		
		EventControllerMap() {
			this.eventControllerMap = new HashMap<EventProtocolRequest, EventController>(0);
		}
		
		EventControllerMap(final List<EventController> eventControllerList) {
			this.eventControllerMap = new HashMap<EventProtocolRequest, EventController>(eventControllerList.size() * 2);
			for (EventController ec : eventControllerList) {
				this.eventControllerMap.put(ec.getEventType(), ec);
			}
		}

	    EventController getEventControllerByEventType(EventProtocolRequest eventType) {
			final EventController ec = eventControllerMap.get(eventType);
			if (ec == null) {
				log.error("no eventcontroller for eventType: " + eventType);
				throw new RuntimeException("EventController of type: " + eventType + " not found");
			}
			return ec;
	    }
	    
	    int size() {
	    	return this.eventControllerMap.size();
	    }
	}
	
	public EventController getEventControllerByEventType(final EventProtocolRequest eventType) {
		if (eventType == null) {
			throw new RuntimeException("EventProtocolRequest (eventType) is null");
		}

		EventControllerMap eventControllerMap = eventControllers.get();
		while (eventControllerList.size() > eventControllerMap.size()) {
			eventControllerMap = loadEventControllers(eventControllerMap);
		}
		
		return eventControllerMap.getEventControllerByEventType(eventType);
	}

	/**
	 * Dynamically loads GameControllers
	 */
	protected EventControllerMap loadEventControllers(final EventControllerMap previousMap) {
		log.info("Building next controllerType-->eventController map");

		// Build an immutable next contribution for the controller map.
		final EventControllerMap nextMap = new EventControllerMap(eventControllerList);
		final EventControllerMap retVal;

		// Attempt to replace the previous immutable map with the one just constructed.  Return it if successful, otherwise
		// return what the thread that beat this one injected instead.
		if (this.eventControllers.compareAndSet(previousMap, nextMap)) {
			// Our update "took".  Return the newly constructed nextMap.
			retVal = nextMap;
		} else {
			// Another thread updated the static reference first.  Get what it set and discard nextMap.
			retVal = this.eventControllers.get();
		}
		
		return retVal;
	}
	
}
