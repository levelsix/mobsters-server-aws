package com.lvl6.mobsters.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.GameEvent;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.properties.Globals;

@Component
public abstract class EventController{


	

	private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass().getEnclosingClass());

	/**
	 * GameController subclasses should implement initController in order to do
	 * any initialization they require.
	 */
	protected void initController() {
	}

	/**
	 * factory method for fetching GameEvent objects
	 */
	public abstract RequestEvent createRequestEvent();

	/**
	 * subclasses must implement to do their processing
	 * 
	 * @throws Exception
	 */

	public EventsToDispatch handleEvent(GameEvent event) {
		try {
			EventsToDispatch cre = new EventsToDispatch();
			processEvent(event, cre);
			return cre;
		} catch (Exception e) {
			log.error("Error handling game event: {}", event, e);
		}
		return new EventsToDispatch();
	}
	
	protected void processEvent(GameEvent event, EventsToDispatch eventWriter) throws Exception {
		final RequestEvent reqEvent = (RequestEvent) event;
		
		//TODO: fix this
		/*MiscMethods
				.setMDCProperties(
						null,
						reqEvent.getPlayerId(),
						MiscMethods.getIPOfPlayer(server,
								reqEvent.getPlayerId(), null));*/
		log.info("Received event: {}", event.getClass().getSimpleName());

		final long startTime = System.nanoTime();
		final long endTime;
		try {
			processRequestEvent(reqEvent, eventWriter);
		} catch (Exception e) {
			throw e;
		} finally {
			endTime = System.nanoTime();
		}
		double numSeconds = (endTime - startTime) / 1000000;

		log.info("Finished processing event: {}, took ~{}ms", event.getClass().getSimpleName(), numSeconds);

		if (numSeconds / 1000 > Globals.NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING) {
			log.warn("Event {} took over {} seconds", event.getClass().getSimpleName(),  Globals.NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING);
		}

		//MiscMethods.purgeMDCProperties();
	}


	/**
	 * subclasses must implement to provide their Event type
	 */
	public abstract EventProtocolRequest getEventType();

	@Async
	protected abstract void processRequestEvent(RequestEvent event, EventsToDispatch eventWriter) throws Exception;

	protected int numAllocatedThreads = 0;

	public int getNumAllocatedThreads() {
		return numAllocatedThreads;
	}

}
