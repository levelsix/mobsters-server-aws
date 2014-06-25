package com.lvl6.mobsters.info;

public interface ITask extends IBaseIntPersistentObject
{

	public String getGoodName();

	public void setGoodName( String goodName );

	public String getDescription();

	public void setDescription( String description );

	public City getCity();

	public void setCity( City city );

	public int getAssetNumberWithinCity();

	public void setAssetNumberWithinCity( int assetNumberWithinCity );

	public ITask getPrerequisiteTask();

	public void setPrerequisiteTask( ITask prerequisiteTask );

	public Quest getPrerequisiteQuest();

	public void setPrerequisiteQuest( Quest prerequisiteQuest );

}