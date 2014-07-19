package com.lvl6.mobsters.info;

public interface ITaskStageMonster extends IBaseIntPersistentObject
{

	public ITaskStage getStage();

	public void setStage( ITaskStage stage );

	public IMonster getMonster();

	public void setMonster( IMonster monster );

	public String getMonsterType();

	public void setMonsterType( String monsterType );

	public int getExpReward();

	public void setExpReward( int expReward );

	public int getMinCashDrop();

	public void setMinCashDrop( int minCashDrop );

	public int getMaxCashDrop();

	public void setMaxCashDrop( int maxCashDrop );

	public int getMinOilDrop();

	public void setMinOilDrop( int minOilDrop );

	public int getMaxOilDrop();

	public void setMaxOilDrop( int maxOilDrop );

	public float getPuzzlePieceDropRate();

	public void setPuzzlePieceDropRate( float puzzlePieceDropRate );

	public int getLevel();

	public void setLevel( int level );

	public float getChanceToAppear();

	public void setChanceToAppear( float chanceToAppear );

	public abstract void setDmgMultiplier( float dmgMultiplier );

	public abstract float getDmgMultiplier();

}