package com.lvl6.mobsters.info;

public interface IMonsterForPvp extends IBaseIntPersistentObject
{

	public IMonster getMonster();

	public void setMonster( IMonster monster );

	public int getMonsterLvl();

	public void setMonsterLvl( int monsterLvl );

	public int getElo();

	public void setElo( int elo );

	public int getMinCashReward();

	public void setMinCashReward( int minCashReward );

	public int getMaxCashReward();

	public void setMaxCashReward( int maxCashReward );

	public int getMinOilReward();

	public void setMinOilReward( int minOilReward );

	public int getMaxOilReward();

	public void setMaxOilReward( int maxOilReward );

}