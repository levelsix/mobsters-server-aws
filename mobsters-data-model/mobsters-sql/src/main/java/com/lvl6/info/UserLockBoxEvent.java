package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserLockBoxEvent extends BasePersistentObject{
	@Column(name = "final")
	private static final long serialVersionUID = -2707386084442793864L;
	@Column(name = "lock_box_id")
	private int lockBoxId;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "num_lock_boxes")
	private int numLockBoxes;
	@Column(name = "num_times_completed")
	private int numTimesCompleted;
	@Column(name = "last_pick_time")
	private Date lastPickTime;
	@Column(name = "has_been_redeemed")
	private boolean hasBeenRedeemed;

	public UserLockBoxEvent(){}
  public UserLockBoxEvent(int lockBoxId, int userId, int numLockBoxes,
      int numTimesCompleted, Date lastPickTime, boolean hasBeenRedeemed) {
    super();
    this.lockBoxId = lockBoxId;
    this.userId = userId;
    this.numLockBoxes = numLockBoxes;
    this.numTimesCompleted = numTimesCompleted;
    this.lastPickTime = lastPickTime;
    this.hasBeenRedeemed = hasBeenRedeemed;
  }

  public int getLockBoxId() {
    return lockBoxId;
  }

  public void setLockBoxId(int lockBoxId) {
    this.lockBoxId = lockBoxId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getNumLockBoxes() {
    return numLockBoxes;
  }

  public void setNumLockBoxes(int numLockBoxes) {
    this.numLockBoxes = numLockBoxes;
  }

  public int getNumTimesCompleted() {
    return numTimesCompleted;
  }

  public void setNumTimesCompleted(int numTimesCompleted) {
    this.numTimesCompleted = numTimesCompleted;
  }

  public Date getLastPickTime() {
    return lastPickTime;
  }

  public void setLastPickTime(Date lastPickTime) {
    this.lastPickTime = lastPickTime;
  }
  
  public boolean isHasBeenRedeemed() {
    return hasBeenRedeemed;
  }

  public void setHasBeenRedeemed(boolean hasBeenRedeemed) {
    this.hasBeenRedeemed = hasBeenRedeemed;
  }

  @Override
  public String toString() {
    return "UserLockBoxEvent [lockBoxId=" + lockBoxId + ", userId=" + userId
        + ", numLockBoxes=" + numLockBoxes + ", numTimesCompleted="
        + numTimesCompleted + ", lastPickTime=" + lastPickTime
        + ", hasBeenRedeemed=" + hasBeenRedeemed + "]";
  }
  
}
