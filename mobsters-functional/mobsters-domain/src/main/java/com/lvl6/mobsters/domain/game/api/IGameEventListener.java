package com.lvl6.mobsters.domain.game.api;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.lvl6.mobsters.domain.game.api.events.IGameEvent;
import com.lvl6.mobsters.event.IEventListener;

/**
 * Primarily a marker interface for classes with event handler methods for events emitted 
 * during game-related state-changes.
 * 
 * Events can be found in com.lvl6.mobsters.domain.game.event.  To subscribe to an event,
 * implement a method that recieves it as a single argument and add the @Subscribe annotation
 * to that method.  To receieve all events, accept Object your annotated method's single
 * argument.
 * 
 * Events can be subscribed to either synchronously or asynchronously at runtime and should
 * ideally be registered before a game logic workflow begins.  In the first case, the event 
 * poster's thread is used to invoke handler methods in sequence.  In the latter case, an
 * asynchronous Executor thread pool is used instead.
 * 
 * For more details and feature documentation, see {@link EventBus} and {@link AsyncEventBus}
 */
public interface IGameEventListener extends IEventListener<IGameEvent> {
	/**
	 * Service layer interactions are called "Conversations" in this API Javadoc.  The
	 * {@link beginConversation(String) }  and {@link endConversation(String)} methods are
	 * called to mark the boundaries between and identifies over service methods, allowing the
	 * events between each begin/end pair to be attributed to the conversation named by each
	 * pair.
	 */
	public void beginConversation(String name);
	
	public void endConversation(String name);
}
