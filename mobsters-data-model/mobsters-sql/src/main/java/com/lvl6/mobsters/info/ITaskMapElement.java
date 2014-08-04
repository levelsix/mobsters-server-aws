package com.lvl6.mobsters.info;

public interface ITaskMapElement extends IBaseIntPersistentObject
{

	public abstract void setBossImgName( String bossImgName );

	public abstract String getBossImgName();

	public abstract void setBoss( boolean boss );

	public abstract boolean isBoss();

	public abstract void setElement( String element );

	public abstract String getElement();

	public abstract void setyPos( int yPos );

	public abstract int getyPos();

	public abstract void setxPos( int xPos );

	public abstract int getxPos();

	public abstract void setTask( ITask task );

	public abstract ITask getTask();

}