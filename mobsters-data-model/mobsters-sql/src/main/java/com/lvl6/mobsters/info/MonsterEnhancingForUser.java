package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MonsterEnhancingForUser extends BasePersistentObject{

	
	private static final long serialVersionUID = 5115756006620782231L;	

	@Column(name = "user_id")
	private int userId;
	@Column(name = "monster_for_user_id")
	private long monsterForUserId;
	@Column(name = "expected_start_time")
	private Date expectedStartTime;
	//@Column(name = "queued_time")
//	private Date queuedTime;
	@Column(name = "enhancing_cost")
	private int enhancingCost;  
	public MonsterEnhancingForUser(){}
	public MonsterEnhancingForUser(int userId, long monsterForUserId,
			Date expectedStartTime, int enhancingCost) {
		super();
		this.userId = userId;
		this.monsterForUserId = monsterForUserId;
		this.expectedStartTime = expectedStartTime;
		this.enhancingCost = enhancingCost;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getMonsterForUserId() {
		return monsterForUserId;
	}

	public void setMonsterForUserId(long monsterForUserId) {
		this.monsterForUserId = monsterForUserId;
	}

	public Date getExpectedStartTime() {
		return expectedStartTime;
	}

	public void setExpectedStartTime(Date expectedStartTime) {
		this.expectedStartTime = expectedStartTime;
	}

	public int getEnhancingCost() {
		return enhancingCost;
	}

	public void setEnhancingCost(int enhancingCost) {
		this.enhancingCost = enhancingCost;
	}

	@Override
	public String toString() {
		return "MonsterEnhancingForUser [userId=" + userId + ", monsterForUserId="
				+ monsterForUserId + ", expectedStartTime=" + expectedStartTime
				+ ", enhancingCost=" + enhancingCost + "]";
	}
  
}
