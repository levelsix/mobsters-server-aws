package com.lvl6.info;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MonsterForPvp extends BasePersistentObject{

	
	private static final long serialVersionUID = 4013127890023301706L;	

	@Column(name = "monster_id")
	private int monsterId;
	@Column(name = "monster_lvl")
	private int monsterLvl;
	@Column(name = "elo")
	private int elo;
	@Column(name = "min_cash_reward")
	private int minCashReward;
	@Column(name = "max_cash_reward")
	private int maxCashReward;
	@Column(name = "min_oil_reward")
	private int minOilReward;
	@Column(name = "max_oil_reward")
	private int maxOilReward;	
	
	//not part of the table, just for convenience

	@Column(name = "rand")
	private Random rand;	
	public MonsterForPvp(){}
	public MonsterForPvp(int id, int monsterId, int monsterLvl, int elo,
			int minCashReward, int maxCashReward, int minOilReward, int maxOilReward) {
		super();
		this.monsterId = monsterId;
		this.monsterLvl = monsterLvl;
		this.elo = elo;
		this.minCashReward = minCashReward;
		this.maxCashReward = maxCashReward;
		this.minOilReward = minOilReward;
		this.maxOilReward = maxOilReward;
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
    
    int minMaxDiff = getMaxCashReward() - getMinCashReward();
    int randCash = rand.nextInt(minMaxDiff + 1); 

    //number generated in [0, max-min] range, but need to transform
    //back to original range [min, max]. so add min. [0+min, max-min+min]
    return randCash + getMinCashReward();
  }
  
  public int getOilDrop() {
    //example goal: [min,max]=[5, 10], transform range to start at 0.
    //[min-min, max-min] = [0,max-min] = [0,10-5] = [0,5]
    //this means there are (10-5)+1 possible numbers
    
    int minMaxDiff = getMaxOilReward() - getMinOilReward();
    int randCash = rand.nextInt(minMaxDiff + 1); 

    //number generated in [0, max-min] range, but need to transform
    //back to original range [min, max]. so add min. [0+min, max-min+min]
    return randCash + getMinOilReward();
  }

  //end covenience methods--------------------------------------------------------




	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public int getMonsterLvl() {
		return monsterLvl;
	}

	public void setMonsterLvl(int monsterLvl) {
		this.monsterLvl = monsterLvl;
	}

	public int getElo() {
		return elo;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}

	public int getMinCashReward() {
		return minCashReward;
	}

	public void setMinCashReward(int minCashReward) {
		this.minCashReward = minCashReward;
	}

	public int getMaxCashReward() {
		return maxCashReward;
	}

	public void setMaxCashReward(int maxCashReward) {
		this.maxCashReward = maxCashReward;
	}

	public int getMinOilReward() {
		return minOilReward;
	}

	public void setMinOilReward(int minOilReward) {
		this.minOilReward = minOilReward;
	}

	public int getMaxOilReward() {
		return maxOilReward;
	}

	public void setMaxOilReward(int maxOilReward) {
		this.maxOilReward = maxOilReward;
	}

	@Override
	public String toString() {
		return "MonsterForPvp [id=" + id + ", monsterId=" + monsterId
				+ ", monsterLvl=" + monsterLvl + ", elo=" + elo + ", minCashReward="
				+ minCashReward + ", maxCashReward=" + maxCashReward
				+ ", minOilReward=" + minOilReward + ", maxOilReward=" + maxOilReward
				+ "]";
	}

}
