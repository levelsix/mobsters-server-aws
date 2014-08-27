package com.lvl6.mobsters.server;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.IAction;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.GameEvent;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.utility.common.TimeUtils;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersException;
import com.lvl6.properties.Globals;

@Component
public abstract class EventController{
	private static Logger log = LoggerFactory.getLogger(EventController.class);

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
	 * TBD Factory method for acquiring response builders where cross-cutting
	 * reply envelope properties can be set without knowledge of the specific
	 * response payload type.
	 * 
	 * Primarily to be used to set status code information in common exception
	 * handling.
	 * 
	 * NOTE: DO NOT DO THIS! Instead, use an annotation to facilitate reflective
	 *       access to the appropriate Protobuf builder and, if necessary, its
	 *       setStatus( ) method as well as the enum to name match against.
	 */
	// public abstract Object createResponseBuilder();
	
	public final EventsToDispatch handleEvent(GameEvent event) {
		final EventsToDispatch cre = new EventsToDispatch();
		// Exception handling is addressed by processEvent itself.
		// try {
		processEvent(event, cre);
		// }
		return cre;
	}
	
	protected void processEvent(GameEvent event, EventsToDispatch eventWriter) {
		final RequestEvent reqEvent = (RequestEvent) event;
		
		//TODO: fix this
		/*MiscMethods
				.setMDCProperties(
						null,
						reqEvent.getPlayerId(),
						MiscMethods.getIPOfPlayer(server,
								reqEvent.getPlayerId(), null));*/
		log.info("Received event: {}", event.getClass().getSimpleName());

		final long endTime;
		final long startTime = TimeUtils.nanoTime();
		try {
			processRequestEvent(reqEvent, eventWriter);
		} catch (Lvl6MobstersException e) {
			// TODO: 
			log.error("Error handling game event: {}", event, e);
		} catch (Throwable e) {
			log.error("Error handling game event: {}", event, e);
		} finally {
			endTime = TimeUtils.nanoTime();
		}
		double numSeconds = (endTime - startTime) / 1000000;

		log.info("Finished processing event: {}, took ~{}ms", event.getClass().getSimpleName(), numSeconds);

		if (numSeconds / 1000 > Globals.NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING) {
			log.warn("Event {} took over {} seconds", event.getClass().getSimpleName(),  Globals.NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING);
		}

		//MiscMethods.purgeMDCProperties();
	}

	/**
	 * Subclass-reusable subroutine for invoking an IAction's self-defined syntax validity checks and processing
	 * the return set to throw a derived InvalidArgumentsException in case any inputs fail syntax validation.
	 * 
	 * @param svcAction
	 */
	protected void checkSyntaxValidity(final IAction svcAction) {
		final Set<ConstraintViolation<IAction>> syntaxErrors =
			svcAction.verifySyntax();
		if ((syntaxErrors != null) && (! syntaxErrors.isEmpty())) {
			final int errCnt = syntaxErrors.size();
			if (errCnt > 1) {
				final ArrayList<String> msgList = new ArrayList<String>(errCnt);
				for (final ConstraintViolation<IAction> violation : syntaxErrors) {
					msgList.add(
						String.format(
							"%s: %s", 
							violation.getPropertyPath()
								.toString(), 
							violation.getMessage()
						)
					);
				}
				
				throw new IllegalArgumentException(
					String.format(
						"Syntax check failed with a total of %d faults: %s",
						errCnt, msgList.toString()
					)
				);
			} else {
				throw new IllegalArgumentException(
					String.format(
						"Syntax check failed on a fault: %s", 
						syntaxErrors.iterator()
							.next()
					)
				);
			}
		}
	}

	/**
	 * subclasses must implement to provide their Event type
	 */
	public abstract EventProtocolRequest getEventType();

	/**
	 * subclasses must implement to do their processing
	 * 
	 * @throws Exception
	 * @throws Lvl6Exception
	 */
	@Async
	protected abstract void processRequestEvent(RequestEvent event, EventsToDispatch eventWriter) throws Exception;

	protected int numAllocatedThreads = 0;

	public int getNumAllocatedThreads() {
		return numAllocatedThreads;
	}

}
