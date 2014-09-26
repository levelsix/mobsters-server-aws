package com.lvl6.eventdispatcher;

import java.util.List;

import com.lvl6.mobsters.events.BroadcastResponseEvent;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.events.PreDatabaseResponseEvent;

public interface ClientEventDispatcher {

	public abstract void dispatchEvents(EventsToDispatch events);

	public abstract void dispatchNormalEvents(List<NormalResponseEvent> events);

	public abstract void dispatchNormalEvent(NormalResponseEvent ev);

	public abstract void dispatchPreDatabaseEvents(List<PreDatabaseResponseEvent> events);

	public abstract void dispatchPreDatabaseEvent(PreDatabaseResponseEvent ev);

	public abstract void dispatchBroadcastEvents(List<BroadcastResponseEvent> events);

	public abstract void dispatchBroadcastEvent(BroadcastResponseEvent ev);

}