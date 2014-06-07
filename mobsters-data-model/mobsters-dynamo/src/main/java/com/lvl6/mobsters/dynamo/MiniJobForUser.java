package com.lvl6.mobsters.dynamo;

import java.util.Date;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName = "MiniJobForUser")
public class MiniJobForUser {

    // private String id;
    private String userId;

    private Long version;
    
    private String miniJobForUserId;

    private int miniJobId;

    private int baseDmgReceived;

    private int durationMinutes;

    private Date timeStarted;

    //same as userMonsterIdStr below
    private Set<String> userMonsterIds;
    
    //same as userMonsterIds above
//    private String userMonsterIdStr;

    private Date timeCompleted;

    public MiniJobForUser() {
        super();
    }
    
    public MiniJobForUser(
        String userId,
        int miniJobId,
        int baseDmgReceived,
        int durationMinutes,
        Date timeStarted,
        Set<String> userMonsterIds,
        Date timeCompleted )
    {
        super();
        this.userId = userId;
        this.miniJobId = miniJobId;
        this.baseDmgReceived = baseDmgReceived;
        this.durationMinutes = durationMinutes;
        this.timeStarted = timeStarted;
        this.userMonsterIds = userMonsterIds;
        this.timeCompleted = timeCompleted;
    }



    /*
     * @DynamoDBHashKey(attributeName = "id")
     * @DynamoDBAutoGeneratedKey public String getId(){return id;} public void setId(String id){this.id =
     * id;}
     */

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

    @DynamoDBRangeKey(attributeName = "miniJobForUserId")
    @DynamoDBAutoGeneratedKey
    public String getMiniJobForUserId()
    {
        return miniJobForUserId;
    }

    public void setMiniJobForUserId( String miniJobForUserId )
    {
        this.miniJobForUserId = miniJobForUserId;
    }

    public int getMiniJobId()
    {
        return miniJobId;
    }

    public void setMiniJobId( int miniJobId )
    {
        this.miniJobId = miniJobId;
    }

    public int getBaseDmgReceived()
    {
        return baseDmgReceived;
    }

    public void setBaseDmgReceived( int baseDmgReceived )
    {
        this.baseDmgReceived = baseDmgReceived;
    }

    public int getDurationMinutes()
    {
        return durationMinutes;
    }

    public void setDurationMinutes( int durationMinutes )
    {
        this.durationMinutes = durationMinutes;
    }

    public Date getTimeStarted()
    {
        return timeStarted;
    }

    public void setTimeStarted( Date timeStarted )
    {
        this.timeStarted = timeStarted;
    }

    public Set<String> getUserMonsterIds()
    {
        return userMonsterIds;
    }

    public void setUserMonsterIds( Set<String> userMonsterIds )
    {
        this.userMonsterIds = userMonsterIds;
    }

    public Date getTimeCompleted()
    {
        return timeCompleted;
    }

    public void setTimeCompleted( Date timeCompleted )
    {
        this.timeCompleted = timeCompleted;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result =
            prime * result + ((miniJobForUserId == null) ? 0 : miniJobForUserId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MiniJobForUser other = (MiniJobForUser) obj;
        if (miniJobForUserId == null) {
            if (other.miniJobForUserId != null)
                return false;
        } else if (!miniJobForUserId.equals(other.miniJobForUserId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "MiniJobForUser [userId=" +
            userId +
            ", version=" +
            version +
            ", miniJobForUserId=" +
            miniJobForUserId +
            ", miniJobId=" +
            miniJobId +
            ", baseDmgReceived=" +
            baseDmgReceived +
            ", durationMinutes=" +
            durationMinutes +
            ", timeStarted=" +
            timeStarted +
            ", userMonsterIds=" +
            userMonsterIds +
            ", timeCompleted=" +
            timeCompleted +
            "]";
    }

}