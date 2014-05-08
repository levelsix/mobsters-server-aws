package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MonsterReward extends BasePersistentObject{

	
	private static final long serialVersionUID = 2695141690452902009L;
	@Column(name = "monster_id")
	private int monsterId;
	@Column(name = "equip_id")
	private int equipId;
	@Column(name = "drop_rate")
	private float dropRate;	

	public MonsterReward(){}
  public MonsterReward(int id, int monsterId, int equipId, float dropRate) {
    super();
    this.monsterId = monsterId;
    this.equipId = equipId;
    this.dropRate = dropRate;
  }






  public int getMonsterId() {
    return monsterId;
  }


  public void setMonsterId(int monsterId) {
    this.monsterId = monsterId;
  }


  public int getEquipId() {
    return equipId;
  }


  public void setEquipId(int equipId) {
    this.equipId = equipId;
  }


  public float getDropRate() {
    return dropRate;
  }


  public void setDropRate(float dropRate) {
    this.dropRate = dropRate;
  }


  @Override
  public String toString() {
    return "MonsterReward [id=" + id + ", monsterId=" + monsterId
        + ", equipId=" + equipId + ", dropRate=" + dropRate + "]";
  }

}
