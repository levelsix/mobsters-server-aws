package com.lvl6.mobsters.info;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="MonsterLevelInfo")
@Table(name="monster_level_info")
@Cacheable(true)
@Proxy(lazy=true, proxyClass=IMonsterLevelInfo.class)
public class MonsterLevelInfo implements IMonsterLevelInfo{
	/**
	 */
	private static final long serialVersionUID = -7133780689684424323L;

	@EmbeddedId
	private MonsterLevelInfoPK id;

	@MapsId("monster_id")
	@ManyToOne(
			fetch = FetchType.LAZY, 
			targetEntity = Monster.class, 
			optional = false, 
			cascade = CascadeType.REFRESH
		)
		@JoinColumn(
			name = "monster_id", 
			referencedColumnName = "id", 
			nullable = false, 
			foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), 
			insertable = true, 
			updatable = false, 
			unique = false
		)
	private IMonster monster;
	
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
	@Column(name = "sell_amount")
	private int sellAmount;
	
	public MonsterLevelInfo(){}
	public MonsterLevelInfo(IMonster monster, int level, int hp,
			int curLvlRequiredExp, int feederExp, int fireDmg, int grassDmg,
			int waterDmg, int lightningDmg, int darknessDmg, int rockDmg,
			int speed, float hpExponentBase, float dmgExponentBase,
			float expLvlDivisor, float expLvlExponent, int sellAmount) {
		super();
		this.id = new MonsterLevelInfoPK();
		this.id.monsterId = monster.getId();
		this.id.level = level;
		this.monster = monster;
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
		this.sellAmount = sellAmount;
	}

	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getLevel()
	 */
	@Override
	public MonsterLevelInfoPK getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setLevel(int)
	 */
	@Override
	public void setId(MonsterLevelInfoPK id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getMonster()
	 */
	@Override
	public IMonster getMonster()
	{
		return this.monster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setMonster(com.lvl6.mobsters.info.Monster)
	 */
	@Override
	public void setMonster( IMonster monster )
	{
		this.monster = monster;
	}
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getLevel()
	 */
	@Override
	public int getLevel() {
		return id.getLevel();
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setLevel(int)
	 */
	@Override
	public void setLevel(int level) {
		this.id.setLevel(level);
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getHp()
	 */
	@Override
	public int getHp() {
		return hp;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setHp(int)
	 */
	@Override
	public void setHp(int hp) {
		this.hp = hp;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getCurLvlRequiredExp()
	 */
	@Override
	public int getCurLvlRequiredExp() {
		return curLvlRequiredExp;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setCurLvlRequiredExp(int)
	 */
	@Override
	public void setCurLvlRequiredExp(int curLvlRequiredExp) {
		this.curLvlRequiredExp = curLvlRequiredExp;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getFeederExp()
	 */
	@Override
	public int getFeederExp() {
		return feederExp;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setFeederExp(int)
	 */
	@Override
	public void setFeederExp(int feederExp) {
		this.feederExp = feederExp;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getFireDmg()
	 */
	@Override
	public int getFireDmg() {
		return fireDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setFireDmg(int)
	 */
	@Override
	public void setFireDmg(int fireDmg) {
		this.fireDmg = fireDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getGrassDmg()
	 */
	@Override
	public int getGrassDmg() {
		return grassDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setGrassDmg(int)
	 */
	@Override
	public void setGrassDmg(int grassDmg) {
		this.grassDmg = grassDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getWaterDmg()
	 */
	@Override
	public int getWaterDmg() {
		return waterDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setWaterDmg(int)
	 */
	@Override
	public void setWaterDmg(int waterDmg) {
		this.waterDmg = waterDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getLightningDmg()
	 */
	@Override
	public int getLightningDmg() {
		return lightningDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setLightningDmg(int)
	 */
	@Override
	public void setLightningDmg(int lightningDmg) {
		this.lightningDmg = lightningDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getDarknessDmg()
	 */
	@Override
	public int getDarknessDmg() {
		return darknessDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setDarknessDmg(int)
	 */
	@Override
	public void setDarknessDmg(int darknessDmg) {
		this.darknessDmg = darknessDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getRockDmg()
	 */
	@Override
	public int getRockDmg() {
		return rockDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setRockDmg(int)
	 */
	@Override
	public void setRockDmg(int rockDmg) {
		this.rockDmg = rockDmg;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getSpeed()
	 */
	@Override
	public int getSpeed() {
		return speed;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setSpeed(int)
	 */
	@Override
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getHpExponentBase()
	 */
	@Override
	public float getHpExponentBase() {
		return hpExponentBase;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setHpExponentBase(float)
	 */
	@Override
	public void setHpExponentBase(float hpExponentBase) {
		this.hpExponentBase = hpExponentBase;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getDmgExponentBase()
	 */
	@Override
	public float getDmgExponentBase() {
		return dmgExponentBase;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setDmgExponentBase(float)
	 */
	@Override
	public void setDmgExponentBase(float dmgExponentBase) {
		this.dmgExponentBase = dmgExponentBase;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getExpLvlDivisor()
	 */
	@Override
	public float getExpLvlDivisor() {
		return expLvlDivisor;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setExpLvlDivisor(float)
	 */
	@Override
	public void setExpLvlDivisor(float expLvlDivisor) {
		this.expLvlDivisor = expLvlDivisor;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#getExpLvlExponent()
	 */
	@Override
	public float getExpLvlExponent() {
		return expLvlExponent;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterLevelInfo#setExpLvlExponent(float)
	 */
	@Override
	public void setExpLvlExponent(float expLvlExponent) {
		this.expLvlExponent = expLvlExponent;
	}

	@Override
	public int getSellAmount()
	{
		return sellAmount;
	}
	@Override
	public void setSellAmount( int sellAmount )
	{
		this.sellAmount = sellAmount;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonsterLevelInfo other = (MonsterLevelInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MonsterLevelInfo [monsterId=" + monster.getId() 
				+ ", level=" + id.getLevel()
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
