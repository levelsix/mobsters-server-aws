package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LockBoxItem extends BasePersistentObject{/**
	 * 
	 */

	
	private static final long serialVersionUID = -8050037227928651857L;
	@Column(name = "lock_box_event_id")
	private int lockBoxEventId;
	@Column(name = "chance_to_unlock")
	private float chanceToUnlock;
	@Column(name = "name")
	private String name;
	@Column(name = "image_name")
	private String imageName;
	@Column(name = "redeem_for_num_booster_items")
	private int redeemForNumBoosterItems;
	@Column(name = "is_gold_booster_pack")
	private boolean isGoldBoosterPack;  
  
	public LockBoxItem(){}
  public LockBoxItem(int id, int lockBoxEventId, float chanceToUnlock,
			String name, String imageName, int redeemForNumBoosterItems,
			boolean isGoldBoosterPack) {
		super();
		this.lockBoxEventId = lockBoxEventId;
		this.chanceToUnlock = chanceToUnlock;
		this.name = name;
		this.imageName = imageName;
		this.redeemForNumBoosterItems = redeemForNumBoosterItems;
		this.isGoldBoosterPack = isGoldBoosterPack;
	}
	public String getImageName() {
    return imageName;
  }
  public void setImageName(String imageName) {
    this.imageName = imageName;
  }
  public int getLockBoxEventId() {
    return lockBoxEventId;
  }
  public void setLockBoxEventId(int lockBoxEventId) {
    this.lockBoxEventId = lockBoxEventId;
  }
  public float getChanceToUnlock() {
    return chanceToUnlock;
  }
  public void setChanceToUnlock(float chanceToUnlock) {
    this.chanceToUnlock = chanceToUnlock;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getRedeemForNumBoosterItems() {
    return redeemForNumBoosterItems;
  }
  public void setRedeemForNumBoosterItems(int redeemForNumBoosterItems) {
    this.redeemForNumBoosterItems = redeemForNumBoosterItems;
  }
  public boolean isGoldBoosterPack() {
    return isGoldBoosterPack;
  }
  public void setGoldBoosterPack(boolean isGoldBoosterPack) {
    this.isGoldBoosterPack = isGoldBoosterPack;
  }
	@Override
	public String toString() {
		return "LockBoxItem [id=" + id + ", lockBoxEventId=" + lockBoxEventId
				+ ", chanceToUnlock=" + chanceToUnlock + ", name=" + name
				+ ", imageName=" + imageName + ", redeemForNumBoosterItems="
				+ redeemForNumBoosterItems + ", isGoldBoosterPack=" + isGoldBoosterPack
				+ "]";
	}
  
}
