package com.lvl6.mobsters.dynamo;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="UserCredential")
public class UserCredential {


	@DynamoDBVersionAttribute
	private Long version;

	
	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBIndexHashKey(globalSecondaryIndexName="udidGlobalIndex")
	private String udid;
	
	@DynamoDBIndexHashKey(globalSecondaryIndexName="facebookIdGlobalIndex")
	private String facebookId;
	
	public UserCredential(){}
	public UserCredential(String userId, String udid, String facebookId) {
		super();
		this.userId = userId;
		this.udid = udid;
		this.facebookId = facebookId;
	}

	public String getUserId(){return userId;}
	
	public Long getVersion(){return version;}
	public void setVersion(Long version){this.version = version;}


	public void setUserId(String userId){this.userId = userId;}


	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	@Override
	public String toString() {
		return "UserCredential [userId=" + userId + ", udid=" + udid + ", facebookId="
				+ facebookId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCredential other = (UserCredential) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}