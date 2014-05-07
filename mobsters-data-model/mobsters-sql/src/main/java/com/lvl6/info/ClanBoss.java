package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanBoss extends BasePersistentObject{

	@Column(name = "final")
	private static final long serialVersionUID = 4262433171624845065L;
	@Column(name = "name")
	private String name;
	@Column(name = "hp")
	private int hp;
	@Column(name = "energy_cost")
	private int energyCost;
	@Column(name = "num_minutes_to_kill")
	private int numMinutesToKill;
	@Column(name = "num_minutes_to_respawn")
	private int numMinutesToRespawn;
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
	public ClanBoss(){}
  public ClanBoss(int id, String name, int hp, int energyCost,
      int numMinutesToKill, int numMinutesToRespawn, int numRunesOne,
      int numRunesTwo, int numRunesThree, int numRunesFour, int numRunesFive) {
    super();
    this.name = name;
    this.hp = hp;
    this.energyCost = energyCost;
    this.numMinutesToKill = numMinutesToKill;
    this.numMinutesToRespawn = numMinutesToRespawn;
    this.numRunesOne = numRunesOne;
    this.numRunesTwo = numRunesTwo;
    this.numRunesThree = numRunesThree;
    this.numRunesFour = numRunesFour;
    this.numRunesFive = numRunesFive;
  }



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public int getEnergyCost() {
    return energyCost;
  }

  public void setEnergyCost(int energyCost) {
    this.energyCost = energyCost;
  }

  public int getNumMinutesToKill() {
    return numMinutesToKill;
  }

  public void setNumMinutesToKill(int numMinutesToKill) {
    this.numMinutesToKill = numMinutesToKill;
  }

  public int getNumMinutesToRespawn() {
    return numMinutesToRespawn;
  }

  public void setNumMinutesToRespawn(int numMinutesToRespawn) {
    this.numMinutesToRespawn = numMinutesToRespawn;
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
    return "ClanBoss [id=" + id + ", name=" + name + ", hp=" + hp
        + ", energyCost=" + energyCost + ", numMinutesToKill="
        + numMinutesToKill + ", numMinutesToRespawn=" + numMinutesToRespawn
        + ", numRunesOne=" + numRunesOne + ", numRunesTwo=" + numRunesTwo
        + ", numRunesThree=" + numRunesThree + ", numRunesFour=" + numRunesFour
        + ", numRunesFive=" + numRunesFive + "]";
  }
  
}
