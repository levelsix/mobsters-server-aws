package com.lvl6.mobsters.info;

import java.util.List;

public interface ITask extends IBaseIntPersistentObject
{

	public String getName();

	public void setName( String name );

	public String getDescription();

	public void setDescription( String description );

	public ITask getPrerequisiteTask();

	public void setPrerequisiteTask( ITask prerequisiteTask );

	public abstract void setBoardHeight( int boardHeight );

	public abstract int getBoardHeight();

	public abstract void setBoardWidth( int boardWidth );

	public abstract int getBoardWidth();

	List<ITaskStage> getTaskStages();

}
