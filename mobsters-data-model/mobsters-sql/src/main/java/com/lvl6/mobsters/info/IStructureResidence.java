package com.lvl6.mobsters.info;

public interface IStructureResidence extends IBaseIntPersistentObject
{

	public IStructure getStruct();

	public void setStruct( IStructure struct );

	public int getNumMonsterSlots();

	public void setNumMonsterSlots( int numMonsterSlots );

	public int getNumBonusMonsterSlots();

	public void setNumBonusMonsterSlots( int numBonusMonsterSlots );

	public int getNumGemsRequired();

	public void setNumGemsRequired( int numGemsRequired );

	public int getNumAcceptedFbInvites();

	public void setNumAcceptedFbInvites( int numAcceptedFbInvites );

	public String getOccupationName();

	public void setOccupationName( String occupationName );

}