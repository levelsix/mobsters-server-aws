package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MonsterLevelInfo extends BasePersistentObject{

	
	private static final long serialVersionUID = 5516157948001134283L;	

	@Column(name = "monster_id")
	private int monsterId;
	@Column(name = "level")
	private int level;
	@Column(name = "hp")
	private int hp;
	@Column(name = "cur_lvl_required_exp")
	private int curLvlRequiredExp;
	@Column(name = "feeder_exp")
	private int feederExp;
	@Column(name = "fire_dmg")
	private int fireDmg;
	@Column(name = "grass_dmg")
	private int grassDmg;
	@Column(name = "water_dmg")
	private int waterDmg;
	@Column(name = "lightning_dmg")
	private int lightningDmg;
	@Column(name = "darkness_dmg")
	private int darknessDmg;
	@Column(name = "rock_dmg")
	private int rockDmg;
	@Column(name = "speed")
	private int speed;
	@Column(name = "hp_exponent_base")
	private float hpExponentBase;
	@Column(name = "dmg_exponent_base")
	private float dmgExponentBase;
	@Column(name = "exp_lvl_divisor")
	private float expLvlDivisor;
	@Column(name = "exp_lvl_exponent")
	private float expLvlExponent;	
	public MonsterLevelInfo(){}
	public MonsterLevelInfo(int monsterId, int level, int hp,
			int curLvlRequiredExp, int feederExp, int fireDmg, int grassDmg,
			int waterDmg, int lightningDmg, int darknessDmg, int rockDmg,
			int speed, float hpExponentBase, float dmgExponentBase,
			float expLvlDivisor, float expLvlExponent) {
		super();
		this.monsterId = monsterId;
		this.level = level;
		this.hp = hp;
		this.curLvlRequiredExp = curLvlRequiredExp;
		this.feederExp = feederExp;
		this.fireDmg = fireDmg;
		this.grassDmg = grassDmg;
		this.waterDmg = waterDmg;
		this.lightningDmg = lightningDmg;
		this.darknessDmg = darknessDmg;
		this.rockDmg = rockDmg;
		this.speed = speed;
		this.hpExponentBase = hpExponentBase;
		this.dmgExponentBase = dmgExponentBase;
		this.expLvlDivisor = expLvlDivisor;
		this.expLvlExponent = expLvlExponent;
	}

	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getCurLvlRequiredExp() {
		return curLvlRequiredExp;
	}

	public void setCurLvlRequiredExp(int curLvlRequiredExp) {
		this.curLvlRequiredExp = curLvlRequiredExp;
	}

	public int getFeederExp() {
		return feederExp;
	}

	public void setFeederExp(int feederExp) {
		this.feederExp = feederExp;
	}

	public int getFireDmg() {
		return fireDmg;
	}

	public void setFireDmg(int fireDmg) {
		this.fireDmg = fireDmg;
	}

	public int getGrassDmg() {
		return grassDmg;
	}

	public void setGrassDmg(int grassDmg) {
		this.grassDmg = grassDmg;
	}

	public int getWaterDmg() {
		return waterDmg;
	}

	public void setWaterDmg(int waterDmg) {
		this.waterDmg = waterDmg;
	}

	public int getLightningDmg() {
		return lightningDmg;
	}

	public void setLightningDmg(int lightningDmg) {
		this.lightningDmg = lightningDmg;
	}

	public int getDarknessDmg() {
		return darknessDmg;
	}

	public void setDarknessDmg(int darknessDmg) {
		this.darknessDmg = darknessDmg;
	}

	public int getRockDmg() {
		return rockDmg;
	}

	public void setRockDmg(int rockDmg) {
		this.rockDmg = rockDmg;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public float getHpExponentBase() {
		return hpExponentBase;
	}

	public void setHpExponentBase(float hpExponentBase) {
		this.hpExponentBase = hpExponentBase;
	}

	public float getDmgExponentBase() {
		return dmgExponentBase;
	}

	public void setDmgExponentBase(float dmgExponentBase) {
		this.dmgExponentBase = dmgExponentBase;
	}

	public float getExpLvlDivisor() {
		return expLvlDivisor;
	}

	public void setExpLvlDivisor(float expLvlDivisor) {
		this.expLvlDivisor = expLvlDivisor;
	}

	public float getExpLvlExponent() {
		return expLvlExponent;
	}

	public void setExpLvlExponent(float expLvlExponent) {
		this.expLvlExponent = expLvlExponent;
	}

	@Override
	public String toString() {
		return "MonsterLevelInfo [monsterId=" + monsterId + ", level=" + level
				+ ", hp=" + hp + ", curLvlRequiredExp=" + curLvlRequiredExp
				+ ", feederExp=" + feederExp + ", fireDmg=" + fireDmg
				+ ", grassDmg=" + grassDmg + ", waterDmg=" + waterDmg
				+ ", lightningDmg=" + lightningDmg + ", darknessDmg="
				+ darknessDmg + ", rockDmg=" + rockDmg + ", speed=" + speed
				+ ", hpExponentBase=" + hpExponentBase + ", dmgExponentBase="
				+ dmgExponentBase + ", expLvlDivisor=" + expLvlDivisor
				+ ", expLvlExponent=" + expLvlExponent + "]";
	}

}
