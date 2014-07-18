package com.lvl6.mobsters.info;

public interface IQuestJob extends IBaseIntPersistentObject
{

	public IQuest getQuest();

	public void setQuest( IQuest quest );

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

	public abstract void setTask( ITask task );

	public abstract ITask getTask();

}