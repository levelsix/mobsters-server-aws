package com.lvl6.mobsters.info;

public interface IStructureResourceStorage extends IBaseIntPersistentObject
{

	public IStructure getStruct();

	public void setStruct( IStructure struct );

	public String getResourceTypeStored();

	public void setResourceTypeStored( String resourceTypeStored );

	public int getCapacity();

	public void setCapacity( int capacity );

}