package com.lvl6.mobsters.info;

public interface IStructureTownHall extends IBaseIntPersistentObject
{

	public IStructure getStruct();

	public void setStruct( IStructure struct );

	public int getNumResourceOneGenerators();

	public void setNumResourceOneGenerators( int numResourceOneGenerators );

	public int getNumResourceOneStorages();

	public void setNumResourceOneStorages( int numResourceOneStorages );

	public int getNumResourceTwoGenerators();

	public void setNumResourceTwoGenerators( int numResourceTwoGenerators );

	public int getNumResourceTwoStorages();

	public void setNumResourceTwoStorages( int numResourceTwoStorages );

	public int getNumHospitals();

	public void setNumHospitals( int numHospitals );

	public int getNumResidences();

	public void setNumResidences( int numResidences );

	public int getNumMonsterSlots();

	public void setNumMonsterSlots( int numMonsterSlots );

	public int getNumLabs();

	public void setNumLabs( int numLabs );

	public int getPvpQueueCashCost();

	public void setPvpQueueCashCost( int pvpQueueCashCost );

	public int getResourceCapacity();

	public void setResourceCapacity( int resourceCapacity );

}