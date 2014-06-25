package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="EventPersistent")
@Table(name="event_persistent")
@Proxy(lazy=true, proxyClass=IEventPersistent.class)
public class EventPersistent extends BaseIntPersistentObject implements IEventPersistent{	

	private static final long serialVersionUID = 2138800000113125882L;
	
	@Column(name = "day_of_week")
	private String dayOfWeek;
	@Column(name = "start_hour")
	private int startHour;
	@Column(name = "event_duration_minutes")
	private int eventDurationMinutes;
	
	@OneToOne(fetch=FetchType.LAZY, targetEntity=Task.class)
	@JoinColumn(
		name = "task_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ITask task;
	
	@Column(name = "cooldown_minutes")
	private int cooldownMinutes;
	@Column(name = "event_type")
	private String eventType;
	@Column(name = "monster_element")
	private String monsterElement;	
	
	public EventPersistent(){}
	public EventPersistent(int id, String dayOfWeek, int startHour,
			int eventDurationMinutes, ITask task, int cooldownMinutes,
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



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#getDayOfWeek()
	 */
	@Override
	public String getDayOfWeek() {
		return dayOfWeek;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#setDayOfWeek(java.lang.String)
	 */
	@Override
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#getStartHour()
	 */
	@Override
	public int getStartHour() {
		return startHour;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#setStartHour(int)
	 */
	@Override
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#getEventDurationMinutes()
	 */
	@Override
	public int getEventDurationMinutes() {
		return eventDurationMinutes;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#setEventDurationMinutes(int)
	 */
	@Override
	public void setEventDurationMinutes(int eventDurationMinutes) {
		this.eventDurationMinutes = eventDurationMinutes;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#getTask()
	 */
	@Override
	public ITask getTask()
	{
		return task;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#setTask(com.lvl6.mobsters.info.ITask)
	 */
	@Override
	public void setTask( ITask task )
	{
		this.task = task;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#getCooldownMinutes()
	 */
	@Override
	public int getCooldownMinutes() {
		return cooldownMinutes;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#setCooldownMinutes(int)
	 */
	@Override
	public void setCooldownMinutes(int cooldownMinutes) {
		this.cooldownMinutes = cooldownMinutes;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#getEventType()
	 */
	@Override
	public String getEventType() {
		return eventType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#setEventType(java.lang.String)
	 */
	@Override
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#getMonsterElement()
	 */
	@Override
	public String getMonsterElement() {
		return monsterElement;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IEventPersistent#setMonsterElement(java.lang.String)
	 */
	@Override
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
