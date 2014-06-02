package com.lvl6.mobsters.events;

import java.util.List;

public abstract class BroadcastResponseEvent extends ResponseEvent {

	protected List<String> recipients;

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

}
