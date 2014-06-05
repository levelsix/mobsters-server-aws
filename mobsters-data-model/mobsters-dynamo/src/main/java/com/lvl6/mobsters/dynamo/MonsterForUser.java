package com.lvl6.mobsters.dynamo;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName = "MonsterForUser")
public class MonsterForUser {

    private String userId;

    private String monsterForUserId;

    private Long version;

    private int monsterId;

    private int currentExp;

    private int currentLvl;

    private int currentHealth;

    private int numPieces;

    private boolean isComplete;

    private Date combineStartTime;

    private int teamSlotNum;

    private String sourceOfPieces;

    public MonsterForUser() {}

    public MonsterForUser(
        String userId,
        int monsterId,
        int currentExp,
        int currentLvl,
        int currentHealth,
        int numPieces,
        boolean isComplete,
        Date combineStartTime,
        int teamSlotNum,
        String sourceOfPieces )
    {
        super();
        this.userId = userId;
        this.monsterId = monsterId;
        this.currentExp = currentExp;
        this.currentLvl = currentLvl;
        this.currentHealth = currentHealth;
        this.numPieces = numPieces;
        this.isComplete = isComplete;
        this.combineStartTime = combineStartTime;
        this.teamSlotNum = teamSlotNum;
        this.sourceOfPieces = sourceOfPieces;
    }

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId( String userId ) {
        this.userId = userId;
    }

    @DynamoDBRangeKey(attributeName = "monsterForUserId")
    @DynamoDBAutoGeneratedKey
    public String getMonsterForUserId() {
        return monsterForUserId;
    }

    public void setId( String monsterForUserId ) {
        this.monsterForUserId = monsterForUserId;
    }

    @DynamoDBVersionAttribute
    public Long getVersion() {
        return version;
    }

    public void setVersion( Long version ) {
        this.version = version;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId( int monsterId ) {
        this.monsterId = monsterId;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp( int currentExp ) {
        this.currentExp = currentExp;
    }

    public int getCurrentLvl() {
        return currentLvl;
    }

    public void setCurrentLvl( int currentLvl ) {
        this.currentLvl = currentLvl;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth( int currentHealth ) {
        this.currentHealth = currentHealth;
    }

    public int getNumPieces() {
        return numPieces;
    }

    public void setNumPieces( int numPieces ) {
        this.numPieces = numPieces;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete( boolean isComplete ) {
        this.isComplete = isComplete;
    }

    public Date getCombineStartTime() {
        return combineStartTime;
    }

    public void setCombineStartTime( Date combineStartTime ) {
        this.combineStartTime = combineStartTime;
    }

    public int getTeamSlotNum() {
        return teamSlotNum;
    }

    public void setTeamSlotNum( int teamSlotNum ) {
        this.teamSlotNum = teamSlotNum;
    }

    public String getSourceOfPieces() {
        return sourceOfPieces;
    }

    public void setSourceOfPieces( String sourceOfPieces ) {
        this.sourceOfPieces = sourceOfPieces;
    }

    @Override
    public String toString() {
        return "MonsterForUser [monsterForUserId=" + monsterForUserId + ", userId=" + userId
            + ", monsterId=" + monsterId + ", currentExp=" + currentExp + ", currentLvl="
            + currentLvl + ", currentHealth=" + currentHealth + ", numPieces=" + numPieces
            + ", isComplete=" + isComplete + ", combineStartTime=" + combineStartTime
            + ", teamSlotNum=" + teamSlotNum + ", sourceOfPieces=" + sourceOfPieces + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((monsterForUserId == null) ? 0 : monsterForUserId.hashCode());
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
        MonsterForUser other = (MonsterForUser) obj;
        if (monsterForUserId == null) {
            if (other.monsterForUserId != null)
                return false;
        } else if (!monsterForUserId.equals(other.monsterForUserId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}