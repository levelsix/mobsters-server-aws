package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LockBoxEvent extends BasePersistentObject{
	
	private static final long serialVersionUID = -368747979562792778L;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	@Column(name = "lock_box_image_name")
	private String lockBoxImageName;
	@Column(name = "event_name")
	private String eventName;
	@Column(name = "prize_equip_id")
	private int prizeEquipId;
	@Column(name = "description_string")
	private String descriptionString;
	@Column(name = "description_image_name")
	private String descriptionImageName;
	@Column(name = "tag_image_name")
	private String tagImageName;  
	public LockBoxEvent(){}
  public LockBoxEvent(int id, Date startDate, Date endDate,
      String lockBoxImageName, String eventName, int prizeEquipId,
      String descriptionString, String descriptionImageName,
      String tagImageName) {
    super();
    this.startDate = startDate;
    this.endDate = endDate;
    this.lockBoxImageName = lockBoxImageName;
    this.eventName = eventName;
    this.prizeEquipId = prizeEquipId;
    this.descriptionImageName = descriptionImageName;
    this.descriptionString = descriptionString;
    this.tagImageName = tagImageName;
  }

  public String getDescriptionString() {
    return descriptionString;
  }

  public void setDescriptionString(String descriptionString) {
    this.descriptionString = descriptionString;
  }

  public String getDescriptionImageName() {
    return descriptionImageName;
  }

  public void setDescriptionImageName(String descriptionImageName) {
    this.descriptionImageName = descriptionImageName;
  }

  public String getTagImageName() {
    return tagImageName;
  }

  public void setTagImageName(String tagImageName) {
    this.tagImageName = tagImageName;
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

  public String getLockBoxImageName() {
    return lockBoxImageName;
  }

  public void setLockBoxImageName(String lockBoxImageName) {
    this.lockBoxImageName = lockBoxImageName;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public int getPrizeEquipId() {
    return prizeEquipId;
  }

  public void setPrizeEquipId(int prizeEquipId) {
    this.prizeEquipId = prizeEquipId;
  }

  @Override
  public String toString() {
    return "LockBoxEvent [id=" + id + ", startDate=" + startDate + ", endDate="
        + endDate + ", lockBoxImageName=" + lockBoxImageName + ", eventName="
        + eventName + ", prizeEquipId=" + prizeEquipId + ", descriptionString="
        + descriptionString + ", descriptionImageName=" + descriptionImageName
        + ", tagImageName=" + tagImageName + "]";
  }
}
