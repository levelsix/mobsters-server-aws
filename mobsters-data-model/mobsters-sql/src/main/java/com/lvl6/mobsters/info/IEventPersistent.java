package com.lvl6.mobsters.info;

public interface IEventPersistent extends IBaseIntPersistentObject
{

	public String getDayOfWeek();

	public void setDayOfWeek( String dayOfWeek );

	public int getStartHour();

	public void setStartHour( int startHour );

	public int getEventDurationMinutes();

	public void setEventDurationMinutes( int eventDurationMinutes );

	public ITask getTask();

	public void setTask( ITask task );

	public int getCooldownMinutes();

	public void setCooldownMinutes( int cooldownMinutes );

	public String getEventType();

	public void setEventType( String eventType );

	public String getMonsterElement();

	public void setMonsterElement( String monsterElement );

}