package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BoosterDisplayItem extends BaseIntPersistentObject{	

	
	private static final long serialVersionUID = 5409974253563638721L;	

	@Column(name = "booster_pack_id")
	private int boosterPackId;
	@Column(name = "is_monster")
	private boolean isMonster;
	@Column(name = "is_complete")
	private boolean isComplete;
	@Column(name = "monster_quality")
	private String monsterQuality;
	@Column(name = "gem_reward")
	private int gemReward;
	@Column(name = "quantity")
	private int quantity;  
	public BoosterDisplayItem(){}
	public BoosterDisplayItem(int id, int boosterPackId, boolean isMonster,
			boolean isComplete, String monsterQuality, int gemReward,
			int quantity) {
		super(id);
		this.boosterPackId = boosterPackId;
		this.isMonster = isMonster;
		this.isComplete = isComplete;
		this.monsterQuality = monsterQuality;
		this.gemReward = gemReward;
		this.quantity = quantity;
	}



	public int getBoosterPackId() {
		return boosterPackId;
	}

	public void setBoosterPackId(int boosterPackId) {
		this.boosterPackId = boosterPackId;
	}

	public boolean isMonster() {
		return isMonster;
	}

	public void setMonster(boolean isMonster) {
		this.isMonster = isMonster;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getMonsterQuality() {
		return monsterQuality;
	}

	public void setMonsterQuality(String monsterQuality) {
		this.monsterQuality = monsterQuality;
	}

	public int getGemReward() {
		return gemReward;
	}

	public void setGemReward(int gemReward) {
		this.gemReward = gemReward;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "BoosterDisplayItem [id=" + id + ", boosterPackId=" + boosterPackId
				+ ", isMonster=" + isMonster + ", isComplete=" + isComplete
				+ ", monsterQuality=" + monsterQuality + ", gemReward=" + gemReward
				+ ", quantity=" + quantity + "]";
	}
	
}
