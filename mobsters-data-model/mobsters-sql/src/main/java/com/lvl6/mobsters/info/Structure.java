package com.lvl6.mobsters.info;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Structure extends BaseIntPersistentObject{

	
	@Column(name = "name")
	private String name;
	@Column(name = "level")
	private int level;
	@Column(name = "struct_type")
	private String structType;
	@Column(name = "build_resource_type")
	private String buildResourceType;
	@Column(name = "build_cost")
	private int buildCost;
	@Column(name = "minutes_to_build")
	private int minutesToBuild;
	@Column(name = "required_town_hall_lvl")
	private int requiredTownHallLvl;
	@Column(name = "width")
	private int width;
	@Column(name = "height")
	private int height;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(
		name = "predecessor_struct_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Structure predecessorStruct;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(
		name = "successor_struct_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Structure successorStruct;
	
	@Column(name = "img_name")
	private String imgName;
	@Column(name = "img_vertical_pixel_offset")
	private float imgVerticalPixelOffset;
	@Column(name = "img_horizontal_pixel_offset")
	private float imgHorizontalPixelOffset;
	@Column(name = "description")
	private String description;
	@Column(name = "short_description")
	private String shortDescription;
	@Column(name = "shadow_img_name")
	private String shadowImgName;
	@Column(name = "shadow_vertical_offset")
	private float shadowVerticalOffset;
	@Column(name = "shadow_horizontal_offset")
	private float shadowHorizontalOffset;
	@Column(name = "shadow_scale")
	private float shadowScale;	

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true)
	private List<StructureHospital> hospitals;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true)
	private List<StructureLab> labs;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true)
	private List<StructureResidence> residences;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true)
	private List<StructureResourceGenerator> resourceGenerators;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true)
	private List<StructureResourceStorage> resourceStorages;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true)
	private List<StructureTownHall> townHalls;
	
	
	public Structure(){}
	public Structure(int id, String name, int level, String structType,
			String buildResourceType, int buildCost, int minutesToBuild,
			int requiredTownHallLvl, int width, int height,
			Structure predecessorStruct, Structure  successorStruct, String imgName,
			float imgVerticalPixelOffset, float imgHorizontalPixelOffset,
			String description, String shortDescription, String shadowImgName,
			float shadowVerticalOffset, float shadowHorizontalOffset,
			float shadowScale, List<StructureHospital> hospitals,
			List<StructureLab> labs, List<StructureResidence> residences,
			List<StructureResourceGenerator> resourceGenerators,
			List<StructureResourceStorage> resourceStorages,
			List<StructureTownHall> townHalls) {
		
		super(id);
		this.name = name;
		this.level = level;
		this.structType = structType;
		this.buildResourceType = buildResourceType;
		this.buildCost = buildCost;
		this.minutesToBuild = minutesToBuild;
		this.requiredTownHallLvl = requiredTownHallLvl;
		this.width = width;
		this.height = height;
		this.predecessorStruct = predecessorStruct;
		this.successorStruct = successorStruct;
		this.imgName = imgName;
		this.imgVerticalPixelOffset = imgVerticalPixelOffset;
		this.imgHorizontalPixelOffset = imgHorizontalPixelOffset;
		this.description = description;
		this.shortDescription = shortDescription;
		this.shadowImgName = shadowImgName;
		this.shadowVerticalOffset = shadowVerticalOffset;
		this.shadowHorizontalOffset = shadowHorizontalOffset;
		this.shadowScale = shadowScale;
		this.hospitals = hospitals;
		this.labs = labs;
		this.residences = residences;
		this.resourceGenerators = resourceGenerators;
		this.resourceStorages = resourceStorages;
		this.townHalls = townHalls;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getStructType() {
		return structType;
	}

	public void setStructType(String structType) {
		this.structType = structType;
	}

	public String getBuildResourceType() {
		return buildResourceType;
	}

	public void setBuildResourceType(String buildResourceType) {
		this.buildResourceType = buildResourceType;
	}

	public int getBuildCost() {
		return buildCost;
	}

	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}

	public int getMinutesToBuild() {
		return minutesToBuild;
	}

	public void setMinutesToBuild(int minutesToBuild) {
		this.minutesToBuild = minutesToBuild;
	}

	public int getRequiredTownHallLvl() {
		return requiredTownHallLvl;
	}

	public void setRequiredTownHallLvl(int requiredTownHallLvl) {
		this.requiredTownHallLvl = requiredTownHallLvl;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Structure getPredecessorStruct()
	{
		return predecessorStruct;
	}
	public void setPredecessorStruct( Structure predecessorStruct )
	{
		this.predecessorStruct = predecessorStruct;
	}
	public Structure getSuccessorStruct()
	{
		return successorStruct;
	}
	public void setSuccessorStruct( Structure successorStruct )
	{
		this.successorStruct = successorStruct;
	}
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public float getImgVerticalPixelOffset() {
		return imgVerticalPixelOffset;
	}

	public void setImgVerticalPixelOffset(float imgVerticalPixelOffset) {
		this.imgVerticalPixelOffset = imgVerticalPixelOffset;
	}

	public float getImgHorizontalPixelOffset() {
		return imgHorizontalPixelOffset;
	}

	public void setImgHorizontalPixelOffset(float imgHorizontalPixelOffset) {
		this.imgHorizontalPixelOffset = imgHorizontalPixelOffset;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getShadowImgName() {
		return shadowImgName;
	}

	public void setShadowImgName(String shadowImgName) {
		this.shadowImgName = shadowImgName;
	}

	public float getShadowVerticalOffset() {
		return shadowVerticalOffset;
	}

	public void setShadowVerticalOffset(float shadowVerticalOffset) {
		this.shadowVerticalOffset = shadowVerticalOffset;
	}

	public float getShadowHorizontalOffset() {
		return shadowHorizontalOffset;
	}

	public void setShadowHorizontalOffset(float shadowHorizontalOffset) {
		this.shadowHorizontalOffset = shadowHorizontalOffset;
	}

	public float getShadowScale() {
		return shadowScale;
	}

	public void setShadowScale(float shadowScale) {
		this.shadowScale = shadowScale;
	}
	
	public List<StructureHospital> getHospitals()
	{
		return hospitals;
	}
	public void setHospitals( List<StructureHospital> hospitals )
	{
		this.hospitals = hospitals;
	}
	public List<StructureLab> getLabs()
	{
		return labs;
	}
	public void setLabs( List<StructureLab> labs )
	{
		this.labs = labs;
	}
	public List<StructureResidence> getResidences()
	{
		return residences;
	}
	public void setResidences( List<StructureResidence> residences )
	{
		this.residences = residences;
	}
	public List<StructureResourceGenerator> getResourceGenerators()
	{
		return resourceGenerators;
	}
	public void setResourceGenerators( List<StructureResourceGenerator> resourceGenerators )
	{
		this.resourceGenerators = resourceGenerators;
	}
	public List<StructureResourceStorage> getResourceStorages()
	{
		return resourceStorages;
	}
	public void setResourceStorages( List<StructureResourceStorage> resourceStorages )
	{
		this.resourceStorages = resourceStorages;
	}
	public List<StructureTownHall> getTownHalls()
	{
		return townHalls;
	}
	public void setTownHalls( List<StructureTownHall> townHalls )
	{
		this.townHalls = townHalls;
	}
	@Override
	public String toString() {
		return "Structure [id=" + id + ", name=" + name + ", level=" + level
				+ ", structType=" + structType + ", buildResourceType="
				+ buildResourceType + ", buildCost=" + buildCost
				+ ", minutesToBuild=" + minutesToBuild
				+ ", requiredTownHallLvl=" + requiredTownHallLvl + ", width="
				+ width + ", height=" + height + ", predecessorStructId="
				+ predecessorStruct + ", successorStructId="
				+ successorStruct + ", imgName=" + imgName
				+ ", imgVerticalPixelOffset=" + imgVerticalPixelOffset
				+ ", imgHorizontalPixelOffset=" + imgHorizontalPixelOffset
				+ ", description=" + description + ", shortDescription="
				+ shortDescription + ", shadowImgName=" + shadowImgName
				+ ", shadowVerticalOffset=" + shadowVerticalOffset
				+ ", shadowHorizontalOffset=" + shadowHorizontalOffset
				+ ", shadowScale=" + shadowScale + "]";
	}

}
