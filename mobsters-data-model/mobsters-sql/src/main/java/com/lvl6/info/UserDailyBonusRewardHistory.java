package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserDailyBonusRewardHistory extends BasePersistentObject{  

	@Column(name = "user_id")
	private int userId;
	@Column(name = "currency_rewarded")
	private int currencyRewarded;
	@Column(name = "is_coins")
	private boolean isCoins;
	@Column(name = "booster_pack_id_rewarded")
	private int boosterPackIdRewarded;
	@Column(name = "equip_id_rewarded")
	private int equipIdRewarded;
	@Column(name = "nth_consecutive_day")
	private int nthConsecutiveDay;
	@Column(name = "date_awarded")
	private Date dateAwarded;  
	public UserDailyBonusRewardHistory(){}
  public UserDailyBonusRewardHistory(int id, int userId, int currencyRewarded,
      boolean isCoins, int boosterPackIdRewarded, int equipIdRewarded,
      int nthConsecutiveDay, Date dateAwarded) {
    super();
    this.userId = userId;
    this.currencyRewarded = currencyRewarded;
    this.isCoins = isCoins;
    this.boosterPackIdRewarded = boosterPackIdRewarded;
    this.equipIdRewarded = equipIdRewarded;
    this.nthConsecutiveDay = nthConsecutiveDay;
    this.dateAwarded = dateAwarded;
  }



  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getCurrencyRewarded() {
    return currencyRewarded;
  }

  public void setCurrencyRewarded(int currencyRewarded) {
    this.currencyRewarded = currencyRewarded;
  }

  public boolean isCoins() {
    return isCoins;
  }

  public void setCoins(boolean isCoins) {
    this.isCoins = isCoins;
  }

  public int getBoosterPackIdRewarded() {
    return boosterPackIdRewarded;
  }

  public void setBoosterPackIdRewarded(int boosterPackIdRewarded) {
    this.boosterPackIdRewarded = boosterPackIdRewarded;
  }

  public int getEquipIdRewarded() {
    return equipIdRewarded;
  }

  public void setEquipIdRewarded(int equipIdRewarded) {
    this.equipIdRewarded = equipIdRewarded;
  }

  public int getNthConsecutiveDay() {
    return nthConsecutiveDay;
  }

  public void setNthConsecutiveDay(int nthConsecutiveDay) {
    this.nthConsecutiveDay = nthConsecutiveDay;
  }

  public Date getDateAwarded() {
    return dateAwarded;
  }

  public void setDateAwarded(Date dateAwarded) {
    this.dateAwarded = dateAwarded;
  }

  @Override
  public String toString() {
    return "UserDailyBonusRewardHistory [id=" + id + ", userId=" + userId
        + ", currencyRewarded=" + currencyRewarded + ", isCoins=" + isCoins
        + ", boosterPackIdRewarded=" + boosterPackIdRewarded
        + ", equipIdRewarded=" + equipIdRewarded + ", nthConsecutiveDay="
        + nthConsecutiveDay + ", dateAwarded=" + dateAwarded + "]";
  }
  
}
