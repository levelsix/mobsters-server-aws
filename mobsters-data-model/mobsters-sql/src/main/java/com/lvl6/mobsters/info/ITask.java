package com.lvl6.mobsters.info;

public interface ITask extends IBaseIntPersistentObject
{

	public String getName();

	public void setName( String name );

	public String getDescription();

	public void setDescription( String description );

	public ITask getPrerequisiteTask();

	public void setPrerequisiteTask( ITask prerequisiteTask );

}
