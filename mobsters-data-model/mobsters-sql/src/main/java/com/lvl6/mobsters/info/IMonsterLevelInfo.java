package com.lvl6.mobsters.info;

import java.io.Serializable;

public interface IMonsterLevelInfo extends Serializable
{
	public MonsterLevelInfoPK getId();
	
	public void setId(MonsterLevelInfoPK lvlInfo);
	
	public IMonster getMonster();

	public void setMonster( IMonster monster );

	public int getLevel();

	public void setLevel( int level );

	public int getHp();

	public void setHp( int hp );

	public int getCurLvlRequiredExp();

	public void setCurLvlRequiredExp( int curLvlRequiredExp );

	public int getFeederExp();

	public void setFeederExp( int feederExp );

	public int getFireDmg();

	public void setFireDmg( int fireDmg );

	public int getGrassDmg();

	public void setGrassDmg( int grassDmg );

	public int getWaterDmg();

	public void setWaterDmg( int waterDmg );

	public int getLightningDmg();

	public void setLightningDmg( int lightningDmg );

	public int getDarknessDmg();

	public void setDarknessDmg( int darknessDmg );

	public int getRockDmg();

	public void setRockDmg( int rockDmg );

	public int getSpeed();

	public void setSpeed( int speed );

	public float getHpExponentBase();

	public void setHpExponentBase( float hpExponentBase );

	public float getDmgExponentBase();

	public void setDmgExponentBase( float dmgExponentBase );

	public float getExpLvlDivisor();

	public void setExpLvlDivisor( float expLvlDivisor );

	public float getExpLvlExponent();

	public void setExpLvlExponent( float expLvlExponent );

	public abstract void setSellAmount( int sellAmount );

	public abstract int getSellAmount();

}