package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MonsterHealingForUser extends BasePersistentObject{

	
	private static final long serialVersionUID = -2979320446074707533L;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "monster_for_user_id")
	private long monsterForUserId;
	@Column(name = "queued_time")
	private Date queuedTime;
	//@Column(name = "user_struct_hospital_id")
//	private int userStructHospitalId;
	@Column(name = "health_progress")
	private float healthProgress;
	@Column(name = "priority")
	private int priority;  
	public MonsterHealingForUser(){}
	public MonsterHealingForUser(int userId, long monsterForUserId,
			Date queuedTime, float healthProgress, int priority) {
		super();
		this.userId = userId;
		this.monsterForUserId = monsterForUserId;
		this.queuedTime = queuedTime;
		this.healthProgress = healthProgress;
		this.priority = priority;
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

	public Date getQueuedTime() {
		return queuedTime;
	}

	public void setQueuedTime(Date queuedTime) {
		this.queuedTime = queuedTime;
	}

	public float getHealthProgress() {
		return healthProgress;
	}

	public void setHealthProgress(float healthProgress) {
		this.healthProgress = healthProgress;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "MonsterHealingForUser [userId=" + userId + ", monsterForUserId="
				+ monsterForUserId + ", queuedTime=" + queuedTime + ", healthProgress="
				+ healthProgress + ", priority=" + priority + "]";
	}
  
}
