package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TournamentEvent extends BasePersistentObject{
	
	private static final long serialVersionUID = -3130246899148578214L;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	@Column(name = "event_name")
	private String eventName;
	@Column(name = "rewards_given_out")
	private boolean rewardsGivenOut;  
	public TournamentEvent(){}
  public TournamentEvent(int id, Date startDate, Date endDate, String eventName, boolean rewardsGivenOut) {
    super();
    this.startDate = startDate;
    this.endDate = endDate;
    this.eventName = eventName;
    this.rewardsGivenOut = rewardsGivenOut;
  }

  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String getEventName() {
    return eventName;
  }
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public boolean isRewardsGivenOut() {
    return rewardsGivenOut;
  }

  public void setRewardsGivenOut(boolean rewardsGivenOut) {
    this.rewardsGivenOut = rewardsGivenOut;
  }

	@Override
	public String toString() {
		return "TournamentEvent [id=" + id + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", eventName=" + eventName
				+ ", rewardsGivenOut=" + rewardsGivenOut + "]";
	}

}
