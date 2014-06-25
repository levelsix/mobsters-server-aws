package com.lvl6.mobsters.info;

public interface IBoosterItem extends IBaseIntPersistentObject
{
	public IBoosterPack getBoosterPack();

	public void setBoosterPack( IBoosterPack boosterPack );

	public Monster getMonster();

	public void setMonster( Monster monster );

	public int getNumPieces();

	public void setNumPieces( int numPieces );

	public boolean isComplete();

	public void setComplete( boolean isComplete );

	public boolean isSpecial();

	public void setSpecial( boolean isSpecial );

	public int getGemReward();

	public void setGemReward( int gemReward );

	public int getCashReward();

	public void setCashReward( int cashReward );

	public float getChanceToAppear();

	public void setChanceToAppear( float chanceToAppear );
}