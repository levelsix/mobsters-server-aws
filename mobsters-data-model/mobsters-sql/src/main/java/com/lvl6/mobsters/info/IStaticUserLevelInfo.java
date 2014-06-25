package com.lvl6.mobsters.info;

public interface IStaticUserLevelInfo extends IBaseIntPersistentObject
{

	public int getLvl();

	public void setLvl( int lvl );

	public int getRequiredExp();

	public void setRequiredExp( int requiredExp );

}