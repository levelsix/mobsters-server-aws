package com.lvl6.dynamo;
import java.util.Date;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="LockBoxEvent")
public class LockBoxEvent {



	private String id;
	private Long version;
	
	private Date startDate;
	private Date endDate;
	private String lockBoxImageName;
	private String eventName;
	private int prizeEquipId;
	private String descriptionString;
	private String descriptionImageName;
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


	@DynamoDBHashKey(attributeName = "id")
	@DynamoDBAutoGeneratedKey
	public String getId(){return id;}
	public void setId(String id){this.id = id;}


	@DynamoDBVersionAttribute
	public Long getVersion(){return version;}
	public void setVersion(Long version){this.version = version;}


  public String getDescriptionString() {    return descriptionString;
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
