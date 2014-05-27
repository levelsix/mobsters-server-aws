package com.lvl6.mobsters.events;

import java.util.ArrayList;
import java.util.List;

public class ControllerResponseEvents {
	protected List<NormalResponseEvent> responsesToSingleUser = new ArrayList<NormalResponseEvent>();

	
	public void writeEvent(NormalResponseEvent ev) {
		getResponsesToSingleUser().add(ev);
	}
	
	public void clearResponses() {
		responsesToSingleUser.clear();
	}

	
	
	

	public List<NormalResponseEvent> getResponsesToSingleUser() {
		return responsesToSingleUser;
	}


	public void setResponsesToSingleUser(List<NormalResponseEvent> responsesToSingleUser) {
		this.responsesToSingleUser = responsesToSingleUser;
	}
}
