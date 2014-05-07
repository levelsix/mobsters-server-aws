package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BlacksmithAttempt extends BasePersistentObject{  

	@Column(name = "final")
	private static final long serialVersionUID = 4280332245815139564L;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "equip_id")
	private int equipId;
	@Column(name = "goal_level")
	private int goalLevel;
	@Column(name = "guaranteed")
	private boolean guaranteed;
	@Column(name = "start_time")
	private Date startTime;
	@Column(name = "diamond_guarantee_cost")
	private int diamondGuaranteeCost;
	@Column(name = "time_of_speedup")
	private Date timeOfSpeedup;
	@Column(name = "attempt_complete")
	private boolean attemptComplete;
	@Column(name = "equip_one_enhancement_percent")
	private int equipOneEnhancementPercent;
	@Column(name = "equip_two_enhancement_percent")
	private int equipTwoEnhancementPercent;
	@Column(name = "forge_slot_number")
	private int forgeSlotNumber;
	public BlacksmithAttempt(){}
  public BlacksmithAttempt(int id, int userId, int equipId, int goalLevel,
      boolean guaranteed, Date startTime, int diamondGuaranteeCost, 
      Date timeOfSpeedup, boolean attemptComplete,
      int equipOneEnhancementPercent, int equipTwoEnhancementPercent,
      int forgeSlotNumber) {
    this.userId = userId;
    this.equipId = equipId;
    this.goalLevel = goalLevel;
    this.guaranteed = guaranteed;
    this.startTime = startTime;
    this.diamondGuaranteeCost = diamondGuaranteeCost;
    this.timeOfSpeedup = timeOfSpeedup;
    this.attemptComplete = attemptComplete;
    this.equipOneEnhancementPercent = equipOneEnhancementPercent;
    this.equipTwoEnhancementPercent = equipTwoEnhancementPercent;
    this.forgeSlotNumber = forgeSlotNumber;
  }


  public int getUserId() {
    return userId;
  }

  public int getEquipId() {
    return equipId;
  }

  public int getGoalLevel() {
    return goalLevel;
  }

  public boolean isGuaranteed() {
    return guaranteed;
  }

  public Date getStartTime() {
    return startTime;
  }

  public int getDiamondGuaranteeCost() {
    return diamondGuaranteeCost;
  }

  public Date getTimeOfSpeedup() {
    return timeOfSpeedup;
  }

  public boolean isAttemptComplete() {
    return attemptComplete;
  }

  public int getEquipOneEnhancementPercent() {
    return equipOneEnhancementPercent;
  }

  public void setEquipOneEnhancementPercent(int equipOneEnhancementPercent) {
    this.equipOneEnhancementPercent = equipOneEnhancementPercent;
  }

  public int getEquipTwoEnhancementPercent() {
    return equipTwoEnhancementPercent;
  }

  public void setEquipTwoEnhancementPercent(int equipTwoEnhancementPercent) {
    this.equipTwoEnhancementPercent = equipTwoEnhancementPercent;
  }

  public int getForgeSlotNumber() {
    return forgeSlotNumber;
  }

  public void setForgeSlotNumber(int forgeSlotNumber) {
    this.forgeSlotNumber = forgeSlotNumber;
  }

  @Override
  public String toString() {
    return "BlacksmithAttempt [id=" + id + ", userId=" + userId + ", equipId="
        + equipId + ", goalLevel=" + goalLevel + ", guaranteed=" + guaranteed
        + ", startTime=" + startTime + ", diamondGuaranteeCost="
        + diamondGuaranteeCost + ", timeOfSpeedup=" + timeOfSpeedup
        + ", attemptComplete=" + attemptComplete
        + ", equipOneEnhancementPercent=" + equipOneEnhancementPercent
        + ", equipTwoEnhancementPercent=" + equipTwoEnhancementPercent
        + ", forgeSlotNumber=" + forgeSlotNumber + "]";
  }

}
