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

	public void setBoardHeight( int boardHeight );

	public int getBoardHeight();

	public void setBoardWidth( int boardWidth );

	public int getBoardWidth();

	public void setGroundImgPrefix( String groundImgPrefix );

	public String getGroundImgPrefix();
	
	public List<ITaskMapElement> getTaskMapElements();

	public List<ITaskStage> getTaskStages();
}
