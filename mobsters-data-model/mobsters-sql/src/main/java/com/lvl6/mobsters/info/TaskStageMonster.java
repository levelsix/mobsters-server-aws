package com.lvl6.mobsters.info;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TaskStageMonster extends BaseIntPersistentObject{

	private static final long serialVersionUID = -8786008429159573146L;

	@ManyToOne(fetch=FetchType.LAZY, targetEntity=TaskStage.class)
	@JoinColumn(
		name = "stage_id",
		nullable = false,
		foreignKey = @ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private TaskStage stage;
	
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=Monster.class)
	@JoinColumn(
		name = "monster_id",
		nullable = false,
		foreignKey = @ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Monster monster;
	
	@Column(name = "monster_type")
	private String monsterType;
	@Column(name = "exp_reward")
	private int expReward;
	@Column(name = "min_cash_drop")
	private int minCashDrop;
	@Column(name = "max_cash_drop")
	private int maxCashDrop;
	@Column(name = "min_oil_drop")
	private int minOilDrop;
	@Column(name = "max_oil_drop")
	private int maxOilDrop;
	@Column(name = "puzzle_piece_drop_rate")
	private float puzzlePieceDropRate;
	@Column(name = "level")
	private int level;
	@Column(name = "chance_to_appear")
	private float chanceToAppear;  

	private Random rand = new Random();
	
	public TaskStageMonster(){}
	public TaskStageMonster(int id, TaskStage stage, Monster monster, String monsterType,
		int expReward, int minCashDrop, int maxCashDrop, int minOilDrop,
		int maxOilDrop, float puzzlePieceDropRate, int level,
		float chanceToAppear) {
	super(id);
	this.stage= stage;
	this.monster = monster;
	this.monsterType = monsterType;
	this.expReward = expReward;
	this.minCashDrop = minCashDrop;
	this.maxCashDrop = maxCashDrop;
	this.minOilDrop = minOilDrop;
	this.maxOilDrop = maxOilDrop;
	this.puzzlePieceDropRate = puzzlePieceDropRate;
	this.level = level;
	this.chanceToAppear = chanceToAppear;
	}

//covenience methods--------------------------------------------------------
  public Random getRand() {
    return rand;
  }

  public void setRand(Random rand) {
    this.rand = rand;
  }
  
  public int getCashDrop() {
    //example goal: [min,max]=[5, 10], transform range to start at 0.
    //[min-min, max-min] = [0,max-min] = [0,10-5] = [0,5]
    //this means there are (10-5)+1 possible numbers
    
    int minMaxDiff = getMaxCashDrop() - getMinCashDrop();
    int randCash = rand.nextInt(minMaxDiff + 1); 

    //number generated in [0, max-min] range, but need to transform
    //back to original range [min, max]. so add min. [0+min, max-min+min]
    return randCash + getMinCashDrop();
  }
  
  public int getOilDrop() {
    int minMaxDiff = getMaxOilDrop() - getMinOilDrop();
    int randOil = rand.nextInt(minMaxDiff + 1); 

    //number generated in [0, max-min] range, but need to transform
    //back to original range [min, max]. so add min. [0+min, max-min+min]
    return randOil + getMinOilDrop();
  }  

  public boolean didPuzzlePieceDrop() {
    float randFloat = getRand().nextFloat();
    
    if (randFloat < getPuzzlePieceDropRate()) {
      return true;
    } else {
      return false;
    }
  }
  //end covenience methods--------------------------------------------------------


  public TaskStage getStage()
  {
	  return stage;
  }
  public void setStage( TaskStage stage )
  {
	  this.stage = stage;
  }
  public Monster getMonster()
  {
	  return monster;
  }
  public void setMonster( Monster monster )
  {
	  this.monster = monster;
  }

  public String getMonsterType() {
	  return monsterType;
  }

  public void setMonsterType(String monsterType) {
	  this.monsterType = monsterType;
  }

  public int getExpReward() {
	  return expReward;
  }

  public void setExpReward(int expReward) {
	  this.expReward = expReward;
  }

  public int getMinCashDrop() {
	  return minCashDrop;
  }

  public void setMinCashDrop(int minCashDrop) {
	  this.minCashDrop = minCashDrop;
  }

  public int getMaxCashDrop() {
	  return maxCashDrop;
  }

  public void setMaxCashDrop(int maxCashDrop) {
	  this.maxCashDrop = maxCashDrop;
  }

  public int getMinOilDrop() {
	  return minOilDrop;
  }

  public void setMinOilDrop(int minOilDrop) {
	  this.minOilDrop = minOilDrop;
  }

  public int getMaxOilDrop() {
	  return maxOilDrop;
  }

  public void setMaxOilDrop(int maxOilDrop) {
	  this.maxOilDrop = maxOilDrop;
  }

  public float getPuzzlePieceDropRate() {
	  return puzzlePieceDropRate;
  }

  public void setPuzzlePieceDropRate(float puzzlePieceDropRate) {
	  this.puzzlePieceDropRate = puzzlePieceDropRate;
  }

  public int getLevel() {
	  return level;
  }

  public void setLevel(int level) {
	  this.level = level;
  }

  public float getChanceToAppear() {
	  return chanceToAppear;
  }

  public void setChanceToAppear(float chanceToAppear) {
	  this.chanceToAppear = chanceToAppear;
  }

  @Override
  public String toString() {
	  return "TaskStageMonster [id=" + id + ", stageId=" + stage
			  + ", monsterId=" + monster + ", monsterType=" + monsterType
			  + ", expReward=" + expReward + ", minCashDrop=" + minCashDrop
			  + ", maxCashDrop=" + maxCashDrop + ", minOilDrop=" + minOilDrop
			  + ", maxOilDrop=" + maxOilDrop + ", puzzlePieceDropRate="
			  + puzzlePieceDropRate + ", level=" + level + ", chanceToAppear="
			  + chanceToAppear + "]";
  }

}
