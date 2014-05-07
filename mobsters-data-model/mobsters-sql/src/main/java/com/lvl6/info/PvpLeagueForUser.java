package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PvpLeagueForUser extends BasePersistentObject{	

	@Column(name = "final")
	private static final long serialVersionUID = 7323773679114371729L;	

	@Column(name = "user_id")
	private int userId;
	@Column(name = "pvp_league_id")
	private int pvpLeagueId;
	@Column(name = "rank")
	private int rank;
	@Column(name = "elo")
	private int elo;
	@Column(name = "shield_end_time")
	private Date shieldEndTime;	//try to prevent multiple people attacking one person. When user is attacked, he gains
	//a temporary shield that prevents him from being selected in pvp

	@Column(name = "in_battle_shield_end_time")
	private Date inBattleShieldEndTime;
	@Column(name = "attacks_won")
	private int attacksWon;
	@Column(name = "defenses_won")
	private int defensesWon;
	@Column(name = "attacks_lost")
	private int attacksLost;
	@Column(name = "defenses_lost")
	private int defensesLost;
	@Column(name = "last_battle_notification_time")
	private Date lastBattleNotificationTime;	
	public PvpLeagueForUser() {
		super();
	}

	public PvpLeagueForUser(PvpUser pu) {
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
	}
	
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
	
	public PvpLeagueForUser(int userId, int pvpLeagueId, int rank, int elo,
			Date shieldEndTime, Date inBattleShieldEndTime, int attacksWon,
			int defensesWon, int attacksLost, int defensesLost,
			Date lastBattleNotificationTime) {
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
	public int getBattlesWon() {
		int battlesWon = getAttacksWon() + getDefensesWon();
		return battlesWon;
	}
	
	public int getBattlesLost() {
		int battlesLost = getAttacksLost() + getDefensesLost();
		return battlesLost;
	}

	public int getNumBattles() {
		int numBattles = getBattlesWon() + getBattlesLost();
		return numBattles;
	}
	//end covenience methods--------------------------------------------------------

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPvpLeagueId() {
		return pvpLeagueId;
	}

	public void setPvpLeagueId(int pvpLeagueId) {
		this.pvpLeagueId = pvpLeagueId;
	}

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

	public Date getShieldEndTime() {
		return shieldEndTime;
	}

	public void setShieldEndTime(Date shieldEndTime) {
		this.shieldEndTime = shieldEndTime;
	}

	public Date getInBattleShieldEndTime() {
		return inBattleShieldEndTime;
	}

	public void setInBattleShieldEndTime(Date inBattleShieldEndTime) {
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

	public Date getLastBattleNotificationTime() {
		return lastBattleNotificationTime;
	}

	public void setLastBattleNotificationTime(Date lastBattleNotificationTime) {
		this.lastBattleNotificationTime = lastBattleNotificationTime;
	}

	@Override
	public String toString() {
		return "PvpLeagueForUser [userId=" + userId + ", pvpLeagueId="
				+ pvpLeagueId + ", rank=" + rank + ", elo=" + elo
				+ ", shieldEndTime=" + shieldEndTime
				+ ", inBattleShieldEndTime=" + inBattleShieldEndTime
				+ ", attacksWon=" + attacksWon + ", defensesWon=" + defensesWon
				+ ", attacksLost=" + attacksLost + ", defensesLost="
				+ defensesLost + ", lastBattleNotificationTime="
				+ lastBattleNotificationTime + "]";
	}

}
