package com.lvl6.mobsters.info;

public interface IStructureLab extends IBaseIntPersistentObject
{

	public IStructure getStruct();

	public int getQueueSize();

	public void setQueueSize( int queueSize );

	public float getPointsPerSecond();

	public void setPointsPerSecond( float pointsPerSecond );

}