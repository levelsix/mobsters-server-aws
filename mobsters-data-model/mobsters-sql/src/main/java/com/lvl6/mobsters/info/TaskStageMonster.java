package com.lvl6.mobsters.info;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="TaskStageMonster")
@Table(name="task_stage_monster")
@Proxy(lazy=true, proxyClass=ITaskStageMonster.class)
public class TaskStageMonster extends BaseIntPersistentObject implements ITaskStageMonster{

	private static final long serialVersionUID = -6321506459007617514L;

	@ManyToOne(fetch=FetchType.EAGER, targetEntity=TaskStage.class)
	@JoinColumn(
		name = "stage_id",
		nullable = false,
		foreignKey = @ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ITaskStage stage;
	
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=Monster.class)
	@JoinColumn(
		name = "monster_id",
		nullable = false,
		foreignKey = @ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster monster;
	
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

	@Column(name = "dmg_multiplier")
	private float dmgMultiplier;
	
	private Random rand = new Random();
	
	public TaskStageMonster(){}
	public TaskStageMonster(int id, ITaskStage stage, IMonster monster, String monsterType,
		int expReward, int minCashDrop, int maxCashDrop, int minOilDrop,
		int maxOilDrop, float puzzlePieceDropRate, int level,
		float chanceToAppear, float dmgMultiplier) {
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
	this.dmgMultiplier = dmgMultiplier;
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


  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getStage()
 */
@Override
public ITaskStage getStage()
  {
	  return stage;
  }
  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setStage(com.lvl6.mobsters.info.TaskStage)
 */
@Override
public void setStage( ITaskStage stage )
  {
	  this.stage = stage;
  }
  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getMonster()
 */
@Override
public IMonster getMonster()
  {
	  return monster;
  }
  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setMonster(com.lvl6.mobsters.info.Monster)
 */
@Override
public void setMonster( IMonster monster )
  {
	  this.monster = monster;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getMonsterType()
 */
@Override
public String getMonsterType() {
	  return monsterType;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setMonsterType(java.lang.String)
 */
@Override
public void setMonsterType(String monsterType) {
	  this.monsterType = monsterType;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getExpReward()
 */
@Override
public int getExpReward() {
	  return expReward;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setExpReward(int)
 */
@Override
public void setExpReward(int expReward) {
	  this.expReward = expReward;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getMinCashDrop()
 */
@Override
public int getMinCashDrop() {
	  return minCashDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setMinCashDrop(int)
 */
@Override
public void setMinCashDrop(int minCashDrop) {
	  this.minCashDrop = minCashDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getMaxCashDrop()
 */
@Override
public int getMaxCashDrop() {
	  return maxCashDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setMaxCashDrop(int)
 */
@Override
public void setMaxCashDrop(int maxCashDrop) {
	  this.maxCashDrop = maxCashDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getMinOilDrop()
 */
@Override
public int getMinOilDrop() {
	  return minOilDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setMinOilDrop(int)
 */
@Override
public void setMinOilDrop(int minOilDrop) {
	  this.minOilDrop = minOilDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getMaxOilDrop()
 */
@Override
public int getMaxOilDrop() {
	  return maxOilDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setMaxOilDrop(int)
 */
@Override
public void setMaxOilDrop(int maxOilDrop) {
	  this.maxOilDrop = maxOilDrop;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getPuzzlePieceDropRate()
 */
@Override
public float getPuzzlePieceDropRate() {
	  return puzzlePieceDropRate;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setPuzzlePieceDropRate(float)
 */
@Override
public void setPuzzlePieceDropRate(float puzzlePieceDropRate) {
	  this.puzzlePieceDropRate = puzzlePieceDropRate;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getLevel()
 */
@Override
public int getLevel() {
	  return level;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setLevel(int)
 */
@Override
public void setLevel(int level) {
	  this.level = level;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#getChanceToAppear()
 */
@Override
public float getChanceToAppear() {
	  return chanceToAppear;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.info.ITaskStageMonster#setChanceToAppear(float)
 */
@Override
public void setChanceToAppear(float chanceToAppear) {
	  this.chanceToAppear = chanceToAppear;
  }

  @Override
public float getDmgMultiplier()
{
	return dmgMultiplier;
}
@Override
public void setDmgMultiplier( float dmgMultiplier )
{
	this.dmgMultiplier = dmgMultiplier;
}
@Override
public String toString()
{
	return "TaskStageMonster [stage="
		+ stage
		+ ", monster="
		+ monster
		+ ", monsterType="
		+ monsterType
		+ ", expReward="
		+ expReward
		+ ", minCashDrop="
		+ minCashDrop
		+ ", maxCashDrop="
		+ maxCashDrop
		+ ", minOilDrop="
		+ minOilDrop
		+ ", maxOilDrop="
		+ maxOilDrop
		+ ", puzzlePieceDropRate="
		+ puzzlePieceDropRate
		+ ", level="
		+ level
		+ ", chanceToAppear="
		+ chanceToAppear
		+ ", dmgMultiplier="
		+ dmgMultiplier
		+ "]";
}

}
