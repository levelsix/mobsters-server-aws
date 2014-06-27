package com.lvl6.mobsters.info;

public interface IStructureMiniJob extends IBaseIntPersistentObject
{

	public IStructure getStruct();

	public void setStruct( IStructure struct );

	public int getGeneratedJobLimit();

	public void setGeneratedJobLimit( int generatedJobLimit );

	public int getHoursBetweenJobGeneration();

	public void setHoursBetweenJobGeneration( int hoursBetweenJobGeneration );

}