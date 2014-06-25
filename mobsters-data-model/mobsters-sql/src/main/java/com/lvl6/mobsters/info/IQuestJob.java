package com.lvl6.mobsters.info;

public interface IQuestJob extends IBaseIntPersistentObject
{

	public Quest getQuest();

	public void setQuest( Quest quest );

	public String getQuestJobType();

	public void setQuestJobType( String questJobType );

	public String getDescription();

	public void setDescription( String description );

	public int getStaticDataId();

	public void setStaticDataId( int staticDataId );

	public int getQuantity();

	public void setQuantity( int quantity );

	public int getPriority();

	public void setPriority( int priority );

	public int getCityId();

	public void setCityId( int cityId );

	public int getCityAssetNum();

	public void setCityAssetNum( int cityAssetNum );

}