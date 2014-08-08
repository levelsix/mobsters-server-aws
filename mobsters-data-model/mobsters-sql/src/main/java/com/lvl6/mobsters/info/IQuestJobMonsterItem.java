package com.lvl6.mobsters.info;

public interface IQuestJobMonsterItem extends IBaseIntPersistentObject
{
	public IQuestJob getQuestJob();

	public void setQuestJob( IQuestJob questJob );

	public IMonster getMonster();

	public void setMonster( IMonster monster );

	public IItem getItem();

	public void setItem( IItem item );

	public float getItemDropRate();

	public void setItemDropRate( float itemDropRate );
}