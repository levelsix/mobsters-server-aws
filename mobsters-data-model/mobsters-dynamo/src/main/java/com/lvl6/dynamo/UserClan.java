package com.lvl6.dynamo;
import java.util.Date;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="UserClan")
public class UserClan {



	private String id;
	private Long version;

	

	private int userId;
	private int clanId;
	private String status;
	private Date requestTime;
	public UserClan(){}
	public UserClan(int userId, int clanId, String status, Date requestTime) {
		super();
		this.userId = userId;
		this.clanId = clanId;
		this.status = status;
		this.requestTime = requestTime;
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
	public int getClanId() {
		return clanId;
	}
	public void setClanId(int clanId) {
		this.clanId = clanId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	
	@Override
	public String toString() {
		return "UserClan [userId=" + userId + ", clanId=" + clanId
				+ ", status=" + status + ", requestTime=" + requestTime + "]";
	}

}
