package com.lvl6.mobsters.info;

import java.util.List;

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

	public List<IStructureHospital> getHospitals();

	public void setHospitals( List<IStructureHospital> hospitals );

	public List<IStructureLab> getLabs();

	public void setLabs( List<IStructureLab> labs );

	public List<IStructureResidence> getResidences();

	public void setResidences( List<IStructureResidence> residences );

	public List<IStructureResourceGenerator> getResourceGenerators();

	public void setResourceGenerators( List<IStructureResourceGenerator> resourceGenerators );

	public List<IStructureResourceStorage> getResourceStorages();

	public void setResourceStorages( List<IStructureResourceStorage> resourceStorages );

	public List<IStructureTownHall> getTownHalls();

	public void setTownHalls( List<IStructureTownHall> townHalls );

}