package com.lvl6.eventdispatcher;

import java.util.List;

import com.lvl6.mobsters.events.BroadcastResponseEvent;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.events.PreDatabaseResponseEvent;

public class AmqpEventDispatcher implements ClientEventDispatcher {

	
	
	
	
	
	@Override
	public void dispatchEvents(EventsToDispatch events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispatchNormalEvents(List<NormalResponseEvent> events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispatchNormalEvent(NormalResponseEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispatchPreDatabaseEvents(List<PreDatabaseResponseEvent> events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispatchPreDatabaseEvent(PreDatabaseResponseEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispatchBroadcastEvents(List<BroadcastResponseEvent> events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispatchBroadcastEvent(BroadcastResponseEvent ev) {
		// TODO Auto-generated method stub

	}

}
