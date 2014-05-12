package com.lvl6.dynamo;
import java.util.Date;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="ExpansionPurchaseForUser")
public class ExpansionPurchaseForUser {



	private String id;
	private Long version;

	
	private int userId;
	private int xPosition;
	private int yPosition;
	private boolean isExpanding;
	private Date expandStartTime; // refers to last time the user clicks the  // upgrade button, not when the last upgrade
  // was complete
  
	public ExpansionPurchaseForUser(){}
	public ExpansionPurchaseForUser(int userId, int xPosition, int yPosition,
			boolean isExpanding, Date expandStartTime) {
		super();
		this.userId = userId;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.isExpanding = isExpanding;
		this.expandStartTime = expandStartTime;
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

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public boolean isExpanding() {
		return isExpanding;
	}

	public void setExpanding(boolean isExpanding) {
		this.isExpanding = isExpanding;
	}

	public Date getExpandStartTime() {
		return expandStartTime;
	}

	public void setExpandStartTime(Date expandStartTime) {
		this.expandStartTime = expandStartTime;
	}

	@Override
	public String toString() {
		return "ExpansionPurchaseForUser [userId=" + userId + ", xPosition="
				+ xPosition + ", yPosition=" + yPosition + ", isExpanding="
				+ isExpanding + ", expandStartTime=" + expandStartTime + "]";
	}

}
