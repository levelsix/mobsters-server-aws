package com.lvl6.mobsters.info;

import java.util.Random;

//the oil, cash, monster rewards can be set or not set; not mutually exclusive
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanRaidStageReward extends BaseIntPersistentObject{	

	
	private static final long serialVersionUID = -2525273287809556883L;
	@Column(name = "clan_raid_stage_id")
	private int clanRaidStageId;
	@Column(name = "min_oil_reward")
	private int minOilReward;
	@Column(name = "max_oil_reward")
	private int maxOilReward;
	@Column(name = "min_cash_reward")
	private int minCashReward;
	@Column(name = "max_cash_reward")
	private int maxCashReward;
	@Column(name = "monster_id")
	private int monsterId;
	@Column(name = "expected_monster_reward_quantity")
	private int expectedMonsterRewardQuantity;//also monster drop rate multiplier	
	
	//not part of the table, just for convenience
	private Random rand;	
	
	public ClanRaidStageReward(){}
	public ClanRaidStageReward(int id, int clanRaidStageId, int minOilReward,
			int maxOilReward, int minCashReward, int maxCashReward, int monsterId,
			int expectedMonsterRewardQuantity) {
		super(id);
		this.clanRaidStageId = clanRaidStageId;
		this.minOilReward = minOilReward;
		this.maxOilReward = maxOilReward;
		this.minCashReward = minCashReward;
		this.maxCashReward = maxCashReward;
		this.monsterId = monsterId;
		this.expectedMonsterRewardQuantity = expectedMonsterRewardQuantity;
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



	public int getClanRaidStageId() {
		return clanRaidStageId;
	}

	public void setClanRaidStageId(int clanRaidStageId) {
		this.clanRaidStageId = clanRaidStageId;
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

	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public int getExpectedMonsterRewardQuantity() {
		return expectedMonsterRewardQuantity;
	}

	public void setExpectedMonsterRewardQuantity(int expectedMonsterRewardQuantity) {
		this.expectedMonsterRewardQuantity = expectedMonsterRewardQuantity;
	}

	@Override
	public String toString() {
		return "ClanRaidStageReward [id=" + id + ", clanRaidStageId="
				+ clanRaidStageId + ", minOilReward=" + minOilReward
				+ ", maxOilReward=" + maxOilReward + ", minCashReward=" + minCashReward
				+ ", maxCashReward=" + maxCashReward + ", monsterId=" + monsterId
				+ ", expectedMonsterRewardQuantity=" + expectedMonsterRewardQuantity
				+ "]";
	}
	
}
