package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MonsterEvolvingForUser extends BasePersistentObject{

	
	private static final long serialVersionUID = -8663041420598844646L;
	@Column(name = "catalyst_monster_for_user_id")
	private long catalystMonsterForUserId;
	@Column(name = "monster_for_user_id_one")
	private long monsterForUserIdOne;
	@Column(name = "monster_for_user_id_two")
	private long monsterForUserIdTwo;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "start_time")
	private Date startTime;
	public MonsterEvolvingForUser(){}
  public MonsterEvolvingForUser(long catalystMonsterForUserId,
			long monsterForUserIdOne, long monsterForUserIdTwo, int userId,
			Date startTime) {
		super();
		this.catalystMonsterForUserId = catalystMonsterForUserId;
		this.monsterForUserIdOne = monsterForUserIdOne;
		this.monsterForUserIdTwo = monsterForUserIdTwo;
		this.userId = userId;
		this.startTime = startTime;
	}

	public long getCatalystMonsterForUserId() {
		return catalystMonsterForUserId;
	}

	public void setCatalystMonsterForUserId(long catalystMonsterForUserId) {
		this.catalystMonsterForUserId = catalystMonsterForUserId;
	}

	public long getMonsterForUserIdOne() {
		return monsterForUserIdOne;
	}

	public void setMonsterForUserIdOne(long monsterForUserIdOne) {
		this.monsterForUserIdOne = monsterForUserIdOne;
	}

	public long getMonsterForUserIdTwo() {
		return monsterForUserIdTwo;
	}

	public void setMonsterForUserIdTwo(long monsterForUserIdTwo) {
		this.monsterForUserIdTwo = monsterForUserIdTwo;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "MonsterEvolvingForUser [catalystMonsterForUserId="
				+ catalystMonsterForUserId + ", monsterForUserIdOne="
				+ monsterForUserIdOne + ", monsterForUserIdTwo=" + monsterForUserIdTwo
				+ ", userId=" + userId + ", startTime=" + startTime + "]";
	}
  
}
