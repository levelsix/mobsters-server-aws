package com.lvl6.mobsters.dynamo;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName = "UserDataRarelyAccessed")
public class UserDataRarelyAccessed {

    private String userId;

    private Long version;

    private String udidForHistory;

    private Date lastLogin;

    private Date lastLogout;

    private String deviceToken;

    private Date createTime;

    private boolean fbIdSetOnUserCreate;

    private String gameCenterId;

    private Date lastObstacleSpawnTime; // TODO: consider moving to User

    private Date lastMiniJobGeneratedTime; // TODO: consider moving to User
    
    private int avatarMonsterId; //hash/primary key in monster table 

    public UserDataRarelyAccessed() {}

    public UserDataRarelyAccessed(
        String userId,
        String udidForHistory,
        Date lastLogin,
        Date lastLogout,
        String deviceToken,
        Date createTime,
        boolean fbIdSetOnUserCreate,
        String gameCenterId,
        Date lastObstacleSpawnTime,
        Date lastMiniJobGeneratedTime,
        int avatarMonsterId )
    {
        super();
        this.userId = userId;
        this.udidForHistory = udidForHistory;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
        this.deviceToken = deviceToken;
        this.createTime = createTime;
        this.fbIdSetOnUserCreate = fbIdSetOnUserCreate;
        this.gameCenterId = gameCenterId;
        this.lastObstacleSpawnTime = lastObstacleSpawnTime;
        this.lastMiniJobGeneratedTime = lastMiniJobGeneratedTime;
        this.avatarMonsterId = avatarMonsterId;
    }

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId( String userId ) {
        this.userId = userId;
    }

    @DynamoDBVersionAttribute
    public Long getVersion() {
        return version;
    }

    public void setVersion( Long version ) {
        this.version = version;
    }

    public String getUdidForHistory() {
        return udidForHistory;
    }

    public void setUdidForHistory( String udidForHistory ) {
        this.udidForHistory = udidForHistory;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin( Date lastLogin ) {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout( Date lastLogout ) {
        this.lastLogout = lastLogout;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken( String deviceToken ) {
        this.deviceToken = deviceToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime( Date createTime ) {
        this.createTime = createTime;
    }

    public boolean isFbIdSetOnUserCreate() {
        return fbIdSetOnUserCreate;
    }

    public void setFbIdSetOnUserCreate( boolean fbIdSetOnUserCreate ) {
        this.fbIdSetOnUserCreate = fbIdSetOnUserCreate;
    }

    public String getGameCenterId() {
        return gameCenterId;
    }

    public void setGameCenterId( String gameCenterId ) {
        this.gameCenterId = gameCenterId;
    }

    public Date getLastObstacleSpawnTime() {
        return lastObstacleSpawnTime;
    }

    public void setLastObstacleSpawnTime( Date lastObstacleSpawnTime ) {
        this.lastObstacleSpawnTime = lastObstacleSpawnTime;
    }

    public Date getLastMiniJobGeneratedTime() {
        return lastMiniJobGeneratedTime;
    }

    public void setLastMiniJobGeneratedTime( Date lastMiniJobGeneratedTime ) {
        this.lastMiniJobGeneratedTime = lastMiniJobGeneratedTime;
    }

    public int getAvatarMonsterId()
	{
		return avatarMonsterId;
	}

	public void setAvatarMonsterId( int avatarMonsterId )
	{
		this.avatarMonsterId = avatarMonsterId;
	}

	@Override
	public String toString()
	{
		return "UserDataRarelyAccessed [userId="
			+ userId
			+ ", udidForHistory="
			+ udidForHistory
			+ ", lastLogin="
			+ lastLogin
			+ ", lastLogout="
			+ lastLogout
			+ ", deviceToken="
			+ deviceToken
			+ ", createTime="
			+ createTime
			+ ", fbIdSetOnUserCreate="
			+ fbIdSetOnUserCreate
			+ ", gameCenterId="
			+ gameCenterId
			+ ", lastObstacleSpawnTime="
			+ lastObstacleSpawnTime
			+ ", lastMiniJobGeneratedTime="
			+ lastMiniJobGeneratedTime
			+ ", avatarMonsterId="
			+ avatarMonsterId
			+ "]";
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDataRarelyAccessed other = (UserDataRarelyAccessed) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}