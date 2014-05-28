package com.lvl6.mobsters.dynamo;
import java.util.HashSet;
import java.util.Set;

//user can have multiple of these (different clanIds)
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="ClanEventPersistentForUser")
public class ClanEventPersistentForUser {



	private String id;
	private Long version;

	
	private int userId;
	private int clanId;
	private int crId;
	private int crDmgDone;
	private int crsId;
	private int crsDmgDone;
	private int crsmId;//primary key in clan raid stage monster
	private int crsmDmgDone;
	private long userMonsterIdOne;
	private long userMonsterIdTwo;
	private long userMonsterIdThree;	
	public ClanEventPersistentForUser(){}
	public ClanEventPersistentForUser(int userId, int clanId, int crId,
			int crDmgDone, int crsId, int crsDmgDone, int crsmId, int crsmDmgDone,
			long userMonsterIdOne, long userMonsterIdTwo, long userMonsterIdThree) {
		super();
		this.userId = userId;
		this.clanId = clanId;
		this.crId = crId;
		this.crDmgDone = crDmgDone;
		this.crsId = crsId;
		this.crsDmgDone = crsDmgDone;
		this.crsmId = crsmId;
		this.crsmDmgDone = crsmDmgDone;
		this.userMonsterIdOne = userMonsterIdOne;
		this.userMonsterIdTwo = userMonsterIdTwo;
		this.userMonsterIdThree = userMonsterIdThree;
	}
	
	//convenience methods
	public Set<Long> getUserMonsterIds() {
		Set<Long> userMonsterIds = new HashSet<Long>();
		
		if (userMonsterIdOne > 0) {
			userMonsterIds.add(userMonsterIdOne);
		}
		if (userMonsterIdTwo > 0) {
			userMonsterIds.add(userMonsterIdTwo);
		}
		if (userMonsterIdThree > 0) {
			userMonsterIds.add(userMonsterIdThree);
		}
		
		return userMonsterIds;
	}
	//---------------------------


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

	public int getCrId() {
		return crId;
	}

	public void setCrId(int crId) {
		this.crId = crId;
	}

	public int getCrDmgDone() {
		return crDmgDone;
	}

	public void setCrDmgDone(int crDmgDone) {
		this.crDmgDone = crDmgDone;
	}

	public int getCrsId() {
		return crsId;
	}

	public void setCrsId(int crsId) {
		this.crsId = crsId;
	}

	public int getCrsDmgDone() {
		return crsDmgDone;
	}

	public void setCrsDmgDone(int crsDmgDone) {
		this.crsDmgDone = crsDmgDone;
	}

	public int getCrsmId() {
		return crsmId;
	}

	public void setCrsmId(int crsmId) {
		this.crsmId = crsmId;
	}

	public int getCrsmDmgDone() {
		return crsmDmgDone;
	}

	public void setCrsmDmgDone(int crsmDmgDone) {
		this.crsmDmgDone = crsmDmgDone;
	}

	public long getUserMonsterIdOne() {
		return userMonsterIdOne;
	}

	public void setUserMonsterIdOne(int userMonsterIdOne) {
		this.userMonsterIdOne = userMonsterIdOne;
	}

	public long getUserMonsterIdTwo() {
		return userMonsterIdTwo;
	}

	public void setUserMonsterIdTwo(int userMonsterIdTwo) {
		this.userMonsterIdTwo = userMonsterIdTwo;
	}

	public long getUserMonsterIdThree() {
		return userMonsterIdThree;
	}

	public void setUserMonsterIdThree(int userMonsterIdThree) {
		this.userMonsterIdThree = userMonsterIdThree;
	}

	@Override
	public String toString() {
		return "ClanEventPersistentForUser [userId=" + userId + ", clanId="
				+ clanId + ", crId=" + crId + ", crDmgDone=" + crDmgDone + ", crsId="
				+ crsId + ", crsDmgDone=" + crsDmgDone + ", crsmId=" + crsmId
				+ ", crsmDmgDone=" + crsmDmgDone + ", userMonsterIdOne="
				+ userMonsterIdOne + ", userMonsterIdTwo=" + userMonsterIdTwo
				+ ", userMonsterIdThree=" + userMonsterIdThree + "]";
	}
  
 	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ClanEventPersistentForUser other = (ClanEventPersistentForUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}