package com.lvl6.mobsters.info;

public interface IStructureHospital extends IBaseIntPersistentObject
{

	public IStructure getStruct();

	public void setStruct( IStructure struct );

	public int getQueueSize();

	public void setQueueSize( int queueSize );

	public float getHealthPerSecond();

	public void setHealthPerSecond( float healthPerSecond );

}