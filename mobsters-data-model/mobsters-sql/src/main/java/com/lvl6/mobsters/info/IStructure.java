package com.lvl6.mobsters.info;

public interface IStructure extends IBaseIntPersistentObject
{

	public String getName();

	public void setName( String name );

	public int getLevel();

	public void setLevel( int level );

	public String getStructType();

	public void setStructType( String structType );

	public String getBuildResourceType();

	public void setBuildResourceType( String buildResourceType );

	public int getBuildCost();

	public void setBuildCost( int buildCost );

	public int getMinutesToBuild();

	public void setMinutesToBuild( int minutesToBuild );

	public int getRequiredTownHallLvl();

	public void setRequiredTownHallLvl( int requiredTownHallLvl );

	public int getWidth();

	public void setWidth( int width );

	public int getHeight();

	public void setHeight( int height );

	public IStructure getPredecessorStruct();

	public void setPredecessorStruct( IStructure predecessorStruct );

	public IStructure getSuccessorStruct();

	public void setSuccessorStruct( IStructure successorStruct );

	public String getImgName();

	public void setImgName( String imgName );

	public float getImgVerticalPixelOffset();

	public void setImgVerticalPixelOffset( float imgVerticalPixelOffset );

	public float getImgHorizontalPixelOffset();

	public void setImgHorizontalPixelOffset( float imgHorizontalPixelOffset );

	public String getDescription();

	public void setDescription( String description );

	public String getShortDescription();

	public void setShortDescription( String shortDescription );

	public String getShadowImgName();

	public void setShadowImgName( String shadowImgName );

	public float getShadowVerticalOffset();

	public void setShadowVerticalOffset( float shadowVerticalOffset );

	public float getShadowHorizontalOffset();

	public void setShadowHorizontalOffset( float shadowHorizontalOffset );

	public float getShadowScale();

	public void setShadowScale( float shadowScale );

	public IStructureHospital getHospital();

	public void setHospital( IStructureHospital hospital );

	public IStructureLab getLab();

	public void setLab( IStructureLab lab );

	public IStructureResidence getResidence();

	public void setResidence( IStructureResidence residence );

	public IStructureResourceGenerator getResourceGenerator();

	public void setResourceGenerator( IStructureResourceGenerator resourceGenerator );

	public IStructureResourceStorage getResourceStorage();

	public void setResourceStorage( IStructureResourceStorage resourceStorage );

	public IStructureTownHall getTownHall();

	public void setTownHall( IStructureTownHall townHall );

}