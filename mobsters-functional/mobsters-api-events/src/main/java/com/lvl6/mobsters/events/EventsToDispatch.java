package com.lvl6.mobsters.events;

import java.util.ArrayList;
import java.util.List;

public class EventsToDispatch {
	protected List<NormalResponseEvent> responsesToSingleUser = new ArrayList<>();
	protected List<PreDatabaseResponseEvent> predbResponseEvents = new ArrayList<>();
	protected List<BroadcastResponseEvent>  broadcastResponseEvents = new ArrayList<>();
	
	public void writeEvent(NormalResponseEvent ev) {
		getResponsesToSingleUser().add(ev);
	}
	
	public void writeEvent(PreDatabaseResponseEvent ev) {
		getPredbResponseEvents().add(ev);
	}
	
	public void writeEvent(BroadcastResponseEvent ev) {
		getBroadcastResponseEvents().add(ev);
	}
	
	
	public void clearResponses() {
		responsesToSingleUser.clear();
		predbResponseEvents.clear();
		broadcastResponseEvents.clear();
	}

	
	
	

	public List<NormalResponseEvent> getResponsesToSingleUser() {
		return responsesToSingleUser;
	}


	public void setResponsesToSingleUser(List<NormalResponseEvent> responsesToSingleUser) {
		this.responsesToSingleUser = responsesToSingleUser;
	}

	public List<PreDatabaseResponseEvent> getPredbResponseEvents() {
		return predbResponseEvents;
	}

	public void setPredbResponseEvents(List<PreDatabaseResponseEvent> predbResponseEvents) {
		this.predbResponseEvents = predbResponseEvents;
	}

	public List<BroadcastResponseEvent> getBroadcastResponseEvents() {
		return broadcastResponseEvents;
	}

	public void setBroadcastResponseEvents(List<BroadcastResponseEvent> broadcastResponseEvents) {
		this.broadcastResponseEvents = broadcastResponseEvents;
	}
}
