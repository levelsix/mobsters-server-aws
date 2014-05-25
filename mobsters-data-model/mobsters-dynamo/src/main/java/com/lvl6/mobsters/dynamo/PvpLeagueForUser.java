package com.lvl6.mobsters.dynamo;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="PvpLeagueForUser")
public class PvpLeagueForUser {


	@DynamoDBVersionAttribute
	private Long version;
	
	@DynamoDBHashKey(attributeName = "userId")
	private String userId;
	
	@DynamoDBRangeKey(attributeName="pvpLeagueId")
	private String pvpLeagueId;
	
	
	private int rank;
	private int elo;
	private long shieldEndTime;	//try to prevent multiple people attacking one person. When user is attacked, he gains
	//a temporary shield that prevents him from being selected in pvp

	private Long inBattleShieldEndTime;
	private int attacksWon;
	private int defensesWon;
	private int attacksLost;
	private int defensesLost;
	private Long lastBattleNotificationTime;	
	public PvpLeagueForUser() {
		super();
	}

/*	public PvpLeagueForUser(PvpUser pu) {
		super();
		this.userId = Integer.parseInt(pu.getUserId());
		this.pvpLeagueId = pu.getPvpLeagueId();
		this.rank = pu.getRank();
		this.elo = pu.getElo();
		this.shieldEndTime = pu.getShieldEndTime();
		this.inBattleShieldEndTime = pu.getInBattleEndTime();
		this.attacksWon = pu.getAttacksWon();
		this.defensesWon = pu.getDefensesWon();
		this.attacksLost = pu.getAttacksLost();
		this.defensesLost = pu.getDefensesLost();
	}*/
	
	public PvpLeagueForUser(PvpLeagueForUser plfu) {
		super();
		this.userId = plfu.getUserId();
		this.pvpLeagueId = plfu.getPvpLeagueId();
		this.rank = plfu.getRank();
		this.elo = plfu.getElo();
		this.shieldEndTime = plfu.getShieldEndTime();
		this.inBattleShieldEndTime = plfu.getInBattleShieldEndTime();
		this.attacksWon = plfu.getAttacksWon();
		this.defensesWon = plfu.getDefensesWon();
		this.attacksLost = plfu.getAttacksLost();
		this.defensesLost = plfu.getDefensesLost();
		this.lastBattleNotificationTime = plfu.getLastBattleNotificationTime();
	}
	
	public PvpLeagueForUser(String userId, String pvpLeagueId, int rank, int elo,
			Long shieldEndTime, Long inBattleShieldEndTime, int attacksWon,
			int defensesWon, int attacksLost, int defensesLost,
			Long lastBattleNotificationTime) {
		super();
		this.userId = userId;
		this.pvpLeagueId = pvpLeagueId;
		this.rank = rank;
		this.elo = elo;
		this.shieldEndTime = shieldEndTime;
		this.inBattleShieldEndTime = inBattleShieldEndTime;
		this.attacksWon = attacksWon;
		this.defensesWon = defensesWon;
		this.attacksLost = attacksLost;
		this.defensesLost = defensesLost;
		this.lastBattleNotificationTime = lastBattleNotificationTime;
	}


	//covenience methods------------------------------------------------------------


	
	public Long getVersion(){return version;}
	public void setVersion(Long version){this.version = version;}

	@DynamoDBIgnore
	public int getBattlesWon() {		
		int battlesWon = getAttacksWon() + getDefensesWon();
		return battlesWon;
	}
	
	@DynamoDBIgnore
	public int getBattlesLost() {
		int battlesLost = getAttacksLost() + getDefensesLost();
		return battlesLost;
	}

	@DynamoDBIgnore
	public int getNumBattles() {
		int numBattles = getBattlesWon() + getBattlesLost();
		return numBattles;
	}
	//end covenience methods--------------------------------------------------------

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getElo() {
		return elo;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}

	public long getShieldEndTime() {
		return shieldEndTime;
	}

	public void setShieldEndTime(long shieldEndTime) {
		this.shieldEndTime = shieldEndTime;
	}

	public Long getInBattleShieldEndTime() {
		return inBattleShieldEndTime;
	}

	public void setInBattleShieldEndTime(Long inBattleShieldEndTime) {
		this.inBattleShieldEndTime = inBattleShieldEndTime;
	}

	public int getAttacksWon() {
		return attacksWon;
	}

	public void setAttacksWon(int attacksWon) {
		this.attacksWon = attacksWon;
	}

	public int getDefensesWon() {
		return defensesWon;
	}

	public void setDefensesWon(int defensesWon) {
		this.defensesWon = defensesWon;
	}

	public int getAttacksLost() {
		return attacksLost;
	}

	public void setAttacksLost(int attacksLost) {
		this.attacksLost = attacksLost;
	}

	public int getDefensesLost() {
		return defensesLost;
	}

	public void setDefensesLost(int defensesLost) {
		this.defensesLost = defensesLost;
	}

	public Long getLastBattleNotificationTime() {
		return lastBattleNotificationTime;
	}

	public void setLastBattleNotificationTime(Long lastBattleNotificationTime) {
		this.lastBattleNotificationTime = lastBattleNotificationTime;
	}





	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPvpLeagueId() {
		return pvpLeagueId;
	}

	public void setPvpLeagueId(String pvpLeagueId) {
		this.pvpLeagueId = pvpLeagueId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pvpLeagueId == null) ? 0 : pvpLeagueId.hashCode());
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
		PvpLeagueForUser other = (PvpLeagueForUser) obj;
		if (pvpLeagueId == null) {
			if (other.pvpLeagueId != null)
				return false;
		} else if (!pvpLeagueId.equals(other.pvpLeagueId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PvpLeagueForUser [version=" + version + ", userId=" + userId + ", pvpLeagueId=" + pvpLeagueId
				+ ", rank=" + rank + ", elo=" + elo + ", shieldEndTime=" + shieldEndTime
				+ ", inBattleShieldEndTime=" + inBattleShieldEndTime + ", attacksWon=" + attacksWon
				+ ", defensesWon=" + defensesWon + ", attacksLost=" + attacksLost + ", defensesLost="
				+ defensesLost + ", lastBattleNotificationTime=" + lastBattleNotificationTime + "]";
	}

}