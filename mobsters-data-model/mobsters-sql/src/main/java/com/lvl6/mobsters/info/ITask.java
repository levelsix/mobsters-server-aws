package com.lvl6.mobsters.info;

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

	public abstract void setGroundImgPrefix( String groundImgPrefix );

	public abstract String getGroundImgPrefix();

}
