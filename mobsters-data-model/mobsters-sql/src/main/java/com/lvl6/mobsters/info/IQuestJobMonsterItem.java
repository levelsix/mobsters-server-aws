package com.lvl6.mobsters.info;

public interface IQuestJobMonsterItem extends IBaseIntPersistentObject
{

	public int getQuestJobId();

	public void setQuestJobId( int questJobId );

	public int getMonsterId();

	public void setMonsterId( int monsterId );

	public int getItemId();

	public void setItemId( int itemId );

	public float getItemDropRate();

	public void setItemDropRate( float itemDropRate );

}