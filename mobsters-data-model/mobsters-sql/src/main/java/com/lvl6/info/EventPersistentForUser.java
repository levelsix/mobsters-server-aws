package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class EventPersistentForUser extends BasePersistentObject{	

	@Column(name = "final")
	private static final long serialVersionUID = -5875488219039765364L;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "event_persistent_id")
	private int eventPersistentId;
	@Column(name = "time_of_entry")
	private Date timeOfEntry; // refers to time user started a daily event
	public EventPersistentForUser(){}
  public EventPersistentForUser(int userId, int eventPersistentId,
			Date timeOfEntry) {
		super();
		this.userId = userId;
		this.eventPersistentId = eventPersistentId;
		this.timeOfEntry = timeOfEntry;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getEventPersistentId() {
		return eventPersistentId;
	}

	public void setEventPersistentId(int eventPersistentId) {
		this.eventPersistentId = eventPersistentId;
	}

	public Date getTimeOfEntry() {
		return timeOfEntry;
	}

	public void setTimeOfEntry(Date timeOfEntry) {
		this.timeOfEntry = timeOfEntry;
	}

	@Override
	public String toString() {
		return "EventPersistentForUser [userId=" + userId + ", eventPersistentId="
				+ eventPersistentId + ", timeOfEntry=" + timeOfEntry + "]";
	}
 
}
