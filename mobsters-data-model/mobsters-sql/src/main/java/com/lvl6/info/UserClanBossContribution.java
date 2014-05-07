package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserClanBossContribution extends BasePersistentObject{

	@Column(name = "final")
	private static final long serialVersionUID = 9153933930548195860L;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "clan_id")
	private int clanId;
	@Column(name = "boss_id")
	private int bossId;
	@Column(name = "total_damage_done")
	private int totalDamageDone;
	@Column(name = "total_energy_used")
	private int totalEnergyUsed;
	@Column(name = "num_runes_one")
	private int numRunesOne;
	@Column(name = "num_runes_two")
	private int numRunesTwo;
	@Column(name = "num_runes_three")
	private int numRunesThree;
	@Column(name = "num_runes_four")
	private int numRunesFour;
	@Column(name = "num_runes_five")
	private int numRunesFive;  
	public UserClanBossContribution(){}
  public UserClanBossContribution(int userId, int clanId, int bossId,
      int totalDamageDone, int totalEnergyUsed, int numRunesOne,
      int numRunesTwo, int numRunesThree, int numRunesFour, int numRunesFive) {
    super();
    this.userId = userId;
    this.clanId = clanId;
    this.bossId = bossId;
    this.totalDamageDone = totalDamageDone;
    this.totalEnergyUsed = totalEnergyUsed;
    this.numRunesOne = numRunesOne;
    this.numRunesTwo = numRunesTwo;
    this.numRunesThree = numRunesThree;
    this.numRunesFour = numRunesFour;
    this.numRunesFive = numRunesFive;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getClanId() {
    return clanId;
  }

  public void setClanId(int clanId) {
    this.clanId = clanId;
  }

  public int getBossId() {
    return bossId;
  }

  public void setBossId(int bossId) {
    this.bossId = bossId;
  }

  public int getTotalDamageDone() {
    return totalDamageDone;
  }

  public void setTotalDamageDone(int totalDamageDone) {
    this.totalDamageDone = totalDamageDone;
  }

  public int getTotalEnergyUsed() {
    return totalEnergyUsed;
  }

  public void setTotalEnergyUsed(int totalEnergyUsed) {
    this.totalEnergyUsed = totalEnergyUsed;
  }

  public int getNumRunesOne() {
    return numRunesOne;
  }

  public void setNumRunesOne(int numRunesOne) {
    this.numRunesOne = numRunesOne;
  }

  public int getNumRunesTwo() {
    return numRunesTwo;
  }

  public void setNumRunesTwo(int numRunesTwo) {
    this.numRunesTwo = numRunesTwo;
  }

  public int getNumRunesThree() {
    return numRunesThree;
  }

  public void setNumRunesThree(int numRunesThree) {
    this.numRunesThree = numRunesThree;
  }

  public int getNumRunesFour() {
    return numRunesFour;
  }

  public void setNumRunesFour(int numRunesFour) {
    this.numRunesFour = numRunesFour;
  }

  public int getNumRunesFive() {
    return numRunesFive;
  }

  public void setNumRunesFive(int numRunesFive) {
    this.numRunesFive = numRunesFive;
  }

  @Override
  public String toString() {
    return "UserClanBossContribution [userId=" + userId + ", clanId=" + clanId
        + ", bossId=" + bossId + ", totalDamageDone=" + totalDamageDone
        + ", totalEnergyUsed=" + totalEnergyUsed + ", numRunesOne="
        + numRunesOne + ", numRunesTwo=" + numRunesTwo + ", numRunesThree="
        + numRunesThree + ", numRunesFour=" + numRunesFour + ", numRunesFive="
        + numRunesFive + "]";
  }
  
}
