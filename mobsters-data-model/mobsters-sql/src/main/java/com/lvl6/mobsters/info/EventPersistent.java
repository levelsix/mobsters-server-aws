package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class EventPersistent extends BaseIntPersistentObject{	

	
	@Column(name = "day_of_week")
	private String dayOfWeek;
	@Column(name = "start_hour")
	private int startHour;
	@Column(name = "event_duration_minutes")
	private int eventDurationMinutes;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "task_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Task task;
	
	@Column(name = "cooldown_minutes")
	private int cooldownMinutes;
	@Column(name = "event_type")
	private String eventType;
	@Column(name = "monster_element")
	private String monsterElement;	
	
	public EventPersistent(){}
	public EventPersistent(int id, String dayOfWeek, int startHour,
			int eventDurationMinutes, Task task, int cooldownMinutes,
			String eventType, String monsterElement) {
		super(id);
		this.dayOfWeek = dayOfWeek;
		this.startHour = startHour;
		this.eventDurationMinutes = eventDurationMinutes;
		this.task = task;
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

	public Task getTask()
	{
		return task;
	}
	public void setTask( Task task )
	{
		this.task = task;
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
				+ eventDurationMinutes + ", taskId=" + task + ", cooldownMinutes="
				+ cooldownMinutes + ", eventType=" + eventType + ", monsterElement="
				+ monsterElement + "]";
	}
	
}
