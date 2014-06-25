package com.lvl6.mobsters.info;

public interface IStructureResourceGenerator extends IBaseIntPersistentObject
{

	public IStructure getStruct();

	public void setStruct( IStructure struct );

	public String getResourceTypeGenerated();

	public void setResourceTypeGenerated( String resourceTypeGenerated );

	public float getProductionRate();

	public void setProductionRate( float productionRate );

	public int getCapacity();

	public void setCapacity( int capacity );

}