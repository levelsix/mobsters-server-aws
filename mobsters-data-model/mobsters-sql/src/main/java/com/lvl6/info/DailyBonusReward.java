package com.lvl6.info;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class DailyBonusReward extends BasePersistentObject{  

	@Column(name = "min_level")
	private int minLevel;
	@Column(name = "max_level")
	private int maxLevel;
	@Column(name = "day_one_coins")
	private int dayOneCoins;
	@Column(name = "day_two_coins")
	private int dayTwoCoins;
	@Column(name = "day_three_diamonds")
	private int dayThreeDiamonds;
	@Column(name = "day_four_coins")
	private int dayFourCoins;
	
	@ElementCollection
	@Column(name = "day_five_booster_pack_ids")
	private List<Integer> dayFiveBoosterPackIds;  
	public DailyBonusReward(){}
  public DailyBonusReward(int id, int minLevel, int maxLevel, int dayOneCoins,
      int dayTwoCoins, int dayThreeDiamonds, int dayFourCoins,
      List<Integer> dayFiveBoosterPackIds) {
    super();
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
    this.dayOneCoins = dayOneCoins;
    this.dayTwoCoins = dayTwoCoins;
    this.dayThreeDiamonds = dayThreeDiamonds;
    this.dayFourCoins = dayFourCoins;
    this.dayFiveBoosterPackIds = dayFiveBoosterPackIds;
  }



  public int getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(int minLevel) {
    this.minLevel = minLevel;
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(int maxLevel) {
    this.maxLevel = maxLevel;
  }

  public int getDayOneCoins() {
    return dayOneCoins;
  }

  public void setDayOneCoins(int dayOneCoins) {
    this.dayOneCoins = dayOneCoins;
  }

  public int getDayTwoCoins() {
    return dayTwoCoins;
  }

  public void setDayTwoCoins(int dayTwoCoins) {
    this.dayTwoCoins = dayTwoCoins;
  }

  public int getDayThreeDiamonds() {
    return dayThreeDiamonds;
  }

  public void setDayThreeDiamonds(int dayThreeDiamonds) {
    this.dayThreeDiamonds = dayThreeDiamonds;
  }

  public int getDayFourCoins() {
    return dayFourCoins;
  }

  public void setDayFourCoins(int dayFourCoins) {
    this.dayFourCoins = dayFourCoins;
  }

  public List<Integer> getDayFiveBoosterPackIds() {
    return dayFiveBoosterPackIds;
  }

  public void setDayFiveBoosterPackIds(List<Integer> dayFiveBoosterPackIds) {
    this.dayFiveBoosterPackIds = dayFiveBoosterPackIds;
  }

  @Override
  public String toString() {
    return "DailyBonusReward [id=" + id + ", minLevel=" + minLevel
        + ", maxLevel=" + maxLevel + ", dayOneCoins=" + dayOneCoins
        + ", dayTwoCoins=" + dayTwoCoins + ", dayThreeDiamonds="
        + dayThreeDiamonds + ", dayFourCoins=" + dayFourCoins
        + ", dayFiveBoosterPackIds=" + dayFiveBoosterPackIds + "]";
  }
  
}
