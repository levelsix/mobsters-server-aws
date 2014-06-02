package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanEventPersistent extends BasePersistentObject{	

	
	private static final long serialVersionUID = -3881447951245558541L;
	@Column(name = "day_of_week")
	private String dayOfWeek;
	@Column(name = "start_hour")
	private int startHour;
	@Column(name = "event_duration_minutes")
	private int eventDurationMinutes;
	@Column(name = "clan_raid_id")
	private int clanRaidId;	
	public ClanEventPersistent(){}
	public ClanEventPersistent(String id, String dayOfWeek, int startHour,
			int eventDurationMinutes, int clanRaidId) {
		super(id);
		this.dayOfWeek = dayOfWeek;
		this.startHour = startHour;
		this.eventDurationMinutes = eventDurationMinutes;
		this.clanRaidId = clanRaidId;
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

	public int getClanRaidId() {
		return clanRaidId;
	}

	public void setClanRaidId(int clanRaidId) {
		this.clanRaidId = clanRaidId;
	}

	@Override
	public String toString() {
		return "ClanEventPersistent [id=" + id + ", dayOfWeek=" + dayOfWeek
				+ ", startHour=" + startHour + ", eventDurationMinutes="
				+ eventDurationMinutes + ", clanRaidId=" + clanRaidId + "]";
	}
	
}
