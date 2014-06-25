package com.lvl6.mobsters.info;

public interface IMiniJob extends IBaseIntPersistentObject
{

	public String getName();

	public IStructure getRequiredStruct();

	public void setRequiredStruct( IStructure requiredStruct );

	public void setName( String name );

	public int getCashReward();

	public void setCashReward( int cashReward );

	public int getOilReward();

	public void setOilReward( int oilReward );

	public int getGemReward();

	public void setGemReward( int gemReward );

	public IMonster getMonsterReward();

	public void setMonsterReward( IMonster monsterReward );

	public String getQuality();

	public void setQuality( String quality );

	public int getMaxNumMonstersAllowed();

	public void setMaxNumMonstersAllowed( int maxNumMonstersAllowed );

	public float getChanceToAppear();

	public void setChanceToAppear( float chanceToAppear );

	public int getHpRequired();

	public void setHpRequired( int hpRequired );

	public int getAtkRequired();

	public void setAtkRequired( int atkRequired );

	public int getMinDmgDealt();

	public void setMinDmgDealt( int minDmgDealt );

	public int getMaxDmgDealt();

	public void setMaxDmgDealt( int maxDmgDealt );

	public int getDurationMinMinutes();

	public void setDurationMinMinutes( int durationMinMinutes );

	public int getDurationMaxMinutes();

	public void setDurationMaxMinutes( int durationMaxMinutes );

}