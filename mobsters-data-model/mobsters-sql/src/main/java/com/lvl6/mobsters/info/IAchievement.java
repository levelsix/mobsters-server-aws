package com.lvl6.mobsters.info;

public interface IAchievement extends IBaseIntPersistentObject 
{

	public String getAchievementName();

	public void setAchievementName( String achievementName );

	public String getDescription();

	public void setDescription( String description );

	public int getGemReward();

	public void setGemReward( int gemReward );

	public int getLvl();

	public void setLvl( int lvl );

	public String getAchievementType();

	public void setAchievementType( String achievementType );

	public String getResourceType();

	public void setResourceType( String resourceType );

	public String getMonsterElement();

	public void setMonsterElement( String monsterElement );

	public String getMonsterQuality();

	public void setMonsterQuality( String monsterQuality );

	public int getStaticDataId();

	public void setStaticDataId( int staticDataId );

	public int getQuantity();

	public void setQuantity( int quantity );

	public int getPriority();

	public void setPriority( int priority );

	public IAchievement getPrerequisiteAchievement();

	public void setPrerequisiteAchievement( IAchievement prerequisiteAchievement );

	public IAchievement getSuccessorAchievement();

	public void setSuccessorAchievement( IAchievement successorAchievement );

}