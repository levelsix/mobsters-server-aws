package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * The persistent class for the monster_for_user database table.
 * 
 */
@Entity

	private static final long serialVersionUID = 8761455335098287924L;
	
	@Column(name = "user_id")
	private int userId;
	@Column(name = "monster_id")
	private int monsterId;
	@Column(name = "current_exp")
	private int currentExp;
	@Column(name = "current_lvl")
	private int currentLvl;
	@Column(name = "current_health")
@Table(name="monster_for_user")
public class MonsterForUser extends BasePersistentObject {
	private int currentHealth;
	@Column(name = "num_pieces")
	private int numPieces;
	@Column(name = "is_complete")
	private boolean isComplete;
	@Column(name = "combine_start_time")
	private Date combineStartTime;
	@Column(name = "team_slot_num")
	private int teamSlotNum;
	@Column(name = "source_of_pieces")
	private String sourceOfPieces;  
	public MonsterForUser(){}
	public MonsterForUser(int userId, int monsterId, int currentExp,
			int currentLvl, int currentHealth, int numPieces, boolean isComplete,
			Date combineStartTime, int teamSlotNum, String sourceOfPieces) {
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



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public int getCurrentExp() {
		return currentExp;
	}

	public void setCurrentExp(int currentExp) {
		this.currentExp = currentExp;
	}

	public int getCurrentLvl() {
		return currentLvl;
	}

	public void setCurrentLvl(int currentLvl) {
		this.currentLvl = currentLvl;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getNumPieces() {
		return numPieces;
	}

	public void setNumPieces(int numPieces) {
		this.numPieces = numPieces;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public Date getCombineStartTime() {
		return combineStartTime;
	}

	public void setCombineStartTime(Date combineStartTime) {
		this.combineStartTime = combineStartTime;
	}

	public int getTeamSlotNum() {
		return teamSlotNum;
	}

	public void setTeamSlotNum(int teamSlotNum) {
		this.teamSlotNum = teamSlotNum;
	}

	public String getSourceOfPieces() {
		return sourceOfPieces;
	}

	public void setSourceOfPieces(String sourceOfPieces) {
		this.sourceOfPieces = sourceOfPieces;
	}

	@Override
	public String toString() {
		return "MonsterForUser [id=" + id + ", userId=" + userId + ", monsterId="
				+ monsterId + ", currentExp=" + currentExp + ", currentLvl="
				+ currentLvl + ", currentHealth=" + currentHealth + ", numPieces="
				+ numPieces + ", isComplete=" + isComplete + ", combineStartTime="
				+ combineStartTime + ", teamSlotNum=" + teamSlotNum
				+ ", sourceOfPieces=" + sourceOfPieces + "]";
	}
  
}