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
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="Structure")
@Table(name="structure")
@Proxy(lazy=true, proxyClass=IStructure.class)
public class Structure extends BaseIntPersistentObject implements IStructure{

	
	private static final long serialVersionUID = 5524500535596533823L;
	
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
	
	@OneToOne(fetch=FetchType.EAGER, targetEntity=Structure.class, optional=true)
	@JoinColumn(
		name = "predecessor_struct_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure predecessorStruct;
	
	@OneToOne(fetch=FetchType.EAGER, targetEntity=Structure.class, optional=true)
	@JoinColumn(
		name = "successor_struct_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure successorStruct;
	
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
		orphanRemoval=true,
		targetEntity=StructureHospital.class)
	private List<IStructureHospital> hospitals;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true,
		targetEntity=StructureLab.class)
	private List<IStructureLab> labs;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true,
		targetEntity=StructureResidence.class)
	private List<IStructureResidence> residences;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true,
		targetEntity=StructureResourceGenerator.class)
	private List<IStructureResourceGenerator> resourceGenerators;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true,
		targetEntity=StructureResourceStorage.class)
	private List<IStructureResourceStorage> resourceStorages;

	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="struct", 
		orphanRemoval=true,
		targetEntity=StructureTownHall.class)
	private List<IStructureTownHall> townHalls;
	
	
	public Structure(){}
	public Structure(int id, String name, int level, String structType,
			String buildResourceType, int buildCost, int minutesToBuild,
			int requiredTownHallLvl, int width, int height,
			IStructure predecessorStruct, IStructure  successorStruct, String imgName,
			float imgVerticalPixelOffset, float imgHorizontalPixelOffset,
			String description, String shortDescription, String shadowImgName,
			float shadowVerticalOffset, float shadowHorizontalOffset,
			float shadowScale, List<IStructureHospital> hospitals,
			List<IStructureLab> labs, List<IStructureResidence> residences,
			List<IStructureResourceGenerator> resourceGenerators,
			List<IStructureResourceStorage> resourceStorages,
			List<IStructureTownHall> townHalls) {
		
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



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getLevel()
	 */
	@Override
	public int getLevel() {
		return level;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setLevel(int)
	 */
	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getStructType()
	 */
	@Override
	public String getStructType() {
		return structType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setStructType(java.lang.String)
	 */
	@Override
	public void setStructType(String structType) {
		this.structType = structType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getBuildResourceType()
	 */
	@Override
	public String getBuildResourceType() {
		return buildResourceType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setBuildResourceType(java.lang.String)
	 */
	@Override
	public void setBuildResourceType(String buildResourceType) {
		this.buildResourceType = buildResourceType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getBuildCost()
	 */
	@Override
	public int getBuildCost() {
		return buildCost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setBuildCost(int)
	 */
	@Override
	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getMinutesToBuild()
	 */
	@Override
	public int getMinutesToBuild() {
		return minutesToBuild;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setMinutesToBuild(int)
	 */
	@Override
	public void setMinutesToBuild(int minutesToBuild) {
		this.minutesToBuild = minutesToBuild;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getRequiredTownHallLvl()
	 */
	@Override
	public int getRequiredTownHallLvl() {
		return requiredTownHallLvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setRequiredTownHallLvl(int)
	 */
	@Override
	public void setRequiredTownHallLvl(int requiredTownHallLvl) {
		this.requiredTownHallLvl = requiredTownHallLvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getPredecessorStruct()
	 */
	@Override
	public IStructure getPredecessorStruct()
	{
		return predecessorStruct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setPredecessorStruct(com.lvl6.mobsters.info.IStructure)
	 */
	@Override
	public void setPredecessorStruct( IStructure predecessorStruct )
	{
		this.predecessorStruct = predecessorStruct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getSuccessorStruct()
	 */
	@Override
	public IStructure getSuccessorStruct()
	{
		return successorStruct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setSuccessorStruct(com.lvl6.mobsters.info.IStructure)
	 */
	@Override
	public void setSuccessorStruct( IStructure successorStruct )
	{
		this.successorStruct = successorStruct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getImgName()
	 */
	@Override
	public String getImgName() {
		return imgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setImgName(java.lang.String)
	 */
	@Override
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getImgVerticalPixelOffset()
	 */
	@Override
	public float getImgVerticalPixelOffset() {
		return imgVerticalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setImgVerticalPixelOffset(float)
	 */
	@Override
	public void setImgVerticalPixelOffset(float imgVerticalPixelOffset) {
		this.imgVerticalPixelOffset = imgVerticalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getImgHorizontalPixelOffset()
	 */
	@Override
	public float getImgHorizontalPixelOffset() {
		return imgHorizontalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setImgHorizontalPixelOffset(float)
	 */
	@Override
	public void setImgHorizontalPixelOffset(float imgHorizontalPixelOffset) {
		this.imgHorizontalPixelOffset = imgHorizontalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setShortDescription(java.lang.String)
	 */
	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getShadowImgName()
	 */
	@Override
	public String getShadowImgName() {
		return shadowImgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setShadowImgName(java.lang.String)
	 */
	@Override
	public void setShadowImgName(String shadowImgName) {
		this.shadowImgName = shadowImgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getShadowVerticalOffset()
	 */
	@Override
	public float getShadowVerticalOffset() {
		return shadowVerticalOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setShadowVerticalOffset(float)
	 */
	@Override
	public void setShadowVerticalOffset(float shadowVerticalOffset) {
		this.shadowVerticalOffset = shadowVerticalOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getShadowHorizontalOffset()
	 */
	@Override
	public float getShadowHorizontalOffset() {
		return shadowHorizontalOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setShadowHorizontalOffset(float)
	 */
	@Override
	public void setShadowHorizontalOffset(float shadowHorizontalOffset) {
		this.shadowHorizontalOffset = shadowHorizontalOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getShadowScale()
	 */
	@Override
	public float getShadowScale() {
		return shadowScale;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setShadowScale(float)
	 */
	@Override
	public void setShadowScale(float shadowScale) {
		this.shadowScale = shadowScale;
	}
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getHospitals()
	 */
	@Override
	public List<IStructureHospital> getHospitals()
	{
		return hospitals;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setHospitals(java.util.List)
	 */
	@Override
	public void setHospitals( List<IStructureHospital> hospitals )
	{
		this.hospitals = hospitals;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getLabs()
	 */
	@Override
	public List<IStructureLab> getLabs()
	{
		return labs;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setLabs(java.util.List)
	 */
	@Override
	public void setLabs( List<IStructureLab> labs )
	{
		this.labs = labs;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getResidences()
	 */
	@Override
	public List<IStructureResidence> getResidences()
	{
		return residences;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setResidences(java.util.List)
	 */
	@Override
	public void setResidences( List<IStructureResidence> residences )
	{
		this.residences = residences;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getResourceGenerators()
	 */
	@Override
	public List<IStructureResourceGenerator> getResourceGenerators()
	{
		return resourceGenerators;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setResourceGenerators(java.util.List)
	 */
	@Override
	public void setResourceGenerators( List<IStructureResourceGenerator> resourceGenerators )
	{
		this.resourceGenerators = resourceGenerators;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getResourceStorages()
	 */
	@Override
	public List<IStructureResourceStorage> getResourceStorages()
	{
		return resourceStorages;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setResourceStorages(java.util.List)
	 */
	@Override
	public void setResourceStorages( List<IStructureResourceStorage> resourceStorages )
	{
		this.resourceStorages = resourceStorages;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#getTownHalls()
	 */
	@Override
	public List<IStructureTownHall> getTownHalls()
	{
		return townHalls;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructure#setTownHalls(java.util.List)
	 */
	@Override
	public void setTownHalls( List<IStructureTownHall> townHalls )
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
