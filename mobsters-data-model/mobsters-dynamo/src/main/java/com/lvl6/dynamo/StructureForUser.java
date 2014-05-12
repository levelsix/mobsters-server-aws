package com.lvl6.dynamo;
import java.util.Date;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="StructureForUser")
public class StructureForUser {



	private String id;
	private Long version;
	

	

	private int userId;
	private int structId;
	private Date lastRetrieved;
	private CoordinatePair coordinates;
//	private int level;
	private Date purchaseTime;
	private boolean isComplete;
	private String orientation;
	private int fbInviteStructLvl;	
	public StructureForUser(){}
	public StructureForUser(int id, int userId, int structId,
			Date lastRetrieved, CoordinatePair coordinates, Date purchaseTime,
			boolean isComplete, String orientation, int fbInviteStructLvl) {
		super();
		this.userId = userId;
		this.structId = structId;
		this.lastRetrieved = lastRetrieved;
		this.coordinates = coordinates;
		this.purchaseTime = purchaseTime;
		this.isComplete = isComplete;
		this.orientation = orientation;
		this.fbInviteStructLvl = fbInviteStructLvl;
	}




	@DynamoDBHashKey(attributeName = "id")
	@DynamoDBAutoGeneratedKey
	public String getId(){return id;}
	public void setId(String id){this.id = id;}


	@DynamoDBVersionAttribute
	public Long getVersion(){return version;}
	public void setVersion(Long version){this.version = version;}


	public int getUserId() {		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStructId() {
		return structId;
	}

	public void setStructId(int structId) {
		this.structId = structId;
	}

	public Date getLastRetrieved() {
		return lastRetrieved;
	}

	public void setLastRetrieved(Date lastRetrieved) {
		this.lastRetrieved = lastRetrieved;
	}

	public CoordinatePair getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(CoordinatePair coordinates) {
		this.coordinates = coordinates;
	}

	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public int getFbInviteStructLvl() {
		return fbInviteStructLvl;
	}

	public void setFbInviteStructLvl(int fbInviteStructLvl) {
		this.fbInviteStructLvl = fbInviteStructLvl;
	}

	@Override
	public String toString() {
		return "StructureForUser [id=" + id + ", userId=" + userId
				+ ", structId=" + structId + ", lastRetrieved=" + lastRetrieved
				+ ", coordinates=" + coordinates + ", purchaseTime="
				+ purchaseTime + ", isComplete=" + isComplete
				+ ", orientation=" + orientation + ", fbInviteStructLvl="
				+ fbInviteStructLvl + "]";
	}

}
