package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class EventPersistent extends BasePersistentObject{	

	@Column(name = "final")
	private static final long serialVersionUID = 1511718617355339151L;
	@Column(name = "day_of_week")
	private String dayOfWeek;
	@Column(name = "start_hour")
	private int startHour;
	@Column(name = "event_duration_minutes")
	private int eventDurationMinutes;
	@Column(name = "task_id")
	private int taskId;
	@Column(name = "cooldown_minutes")
	private int cooldownMinutes;
	@Column(name = "event_type")
	private String eventType;
	@Column(name = "monster_element")
	private String monsterElement;	
	public EventPersistent(){}
	public EventPersistent(int id, String dayOfWeek, int startHour,
			int eventDurationMinutes, int taskId, int cooldownMinutes,
			String eventType, String monsterElement) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.startHour = startHour;
		this.eventDurationMinutes = eventDurationMinutes;
		this.taskId = taskId;
		this.cooldownMinutes = cooldownMinutes;
		this.eventType = eventType;
		this.monsterElement = monsterElement;
	}



	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getEventDurationMinutes() {
		return eventDurationMinutes;
	}

	public void setEventDurationMinutes(int eventDurationMinutes) {
		this.eventDurationMinutes = eventDurationMinutes;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getCooldownMinutes() {
		return cooldownMinutes;
	}

	public void setCooldownMinutes(int cooldownMinutes) {
		this.cooldownMinutes = cooldownMinutes;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getMonsterElement() {
		return monsterElement;
	}

	public void setMonsterElement(String monsterElement) {
		this.monsterElement = monsterElement;
	}

	@Override
	public String toString() {
		return "EventPersistent [id=" + id + ", dayOfWeek=" + dayOfWeek
				+ ", startHour=" + startHour + ", eventDurationMinutes="
				+ eventDurationMinutes + ", taskId=" + taskId + ", cooldownMinutes="
				+ cooldownMinutes + ", eventType=" + eventType + ", monsterElement="
				+ monsterElement + "]";
	}
	
}
