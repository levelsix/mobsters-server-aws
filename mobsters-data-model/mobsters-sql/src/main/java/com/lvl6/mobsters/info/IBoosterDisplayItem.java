package com.lvl6.mobsters.info;

public interface IBoosterDisplayItem extends IBaseIntPersistentObject
{

	public IBoosterPack getBoosterPack();

	public void setBoosterPack( IBoosterPack boosterPack );

	public boolean isMonster();

	public void setMonster( boolean isMonster );

	public boolean isComplete();

	public void setComplete( boolean isComplete );

	public String getMonsterQuality();

	public void setMonsterQuality( String monsterQuality );

	public int getGemReward();

	public void setGemReward( int gemReward );

	public int getQuantity();

	public void setQuantity( int quantity );

}