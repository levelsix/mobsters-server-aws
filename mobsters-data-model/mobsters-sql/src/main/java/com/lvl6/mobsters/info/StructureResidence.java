package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="StructureResidence")
@Table(name="structure_residence")
@Proxy(lazy=true, proxyClass=IStructureResidence.class)
public class StructureResidence extends BaseIntPersistentObject implements IStructureResidence{

	private static final long serialVersionUID = 2004953796398792669L;
	

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure struct;
	
	//how many monster slots this residence gives the user (absolute number)
	//does not depend on previous lower level structures
	@Column(name = "num_monster_slots")
	private int numMonsterSlots;	
	
	//additional slots if user buys some gems or invites friends
	@Column(name = "num_bonus_monster_slots")
	private int numBonusMonsterSlots;	
	
	//number of gems it costs to buy all numBonusMonsterSlots
	@Column(name = "num_gems_required")
	private int numGemsRequired;	
	
	//number of accepted fb invites to get all numBonusMonsterSlots
	@Column(name = "num_accepted_fb_invites")
	private int numAcceptedFbInvites;	

	@Column(name = "occupation_name")
	private String occupationName;	
	
	@Column(name = "img_suffix")
	private String imgSuffix;
	
	public StructureResidence(){}
	public StructureResidence(IStructure struct, int numMonsterSlots,
			int numBonusMonsterSlots, int numGemsRequired, int numAcceptedFbInvites,
			String occupationName, String imgSuffix) {
		super();
		this.struct = struct;
		this.numMonsterSlots = numMonsterSlots;
		this.numBonusMonsterSlots = numBonusMonsterSlots;
		this.numGemsRequired = numGemsRequired;
		this.numAcceptedFbInvites = numAcceptedFbInvites;
		this.occupationName = occupationName;
		this.imgSuffix = imgSuffix;
	}


	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#getStruct()
	 */
	@Override
	public IStructure getStruct()
	{
		return struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#setStruct(com.lvl6.mobsters.info.Structure)
	 */
	@Override
	public void setStruct( IStructure struct )
	{
		this.struct = struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#getNumMonsterSlots()
	 */
	@Override
	public int getNumMonsterSlots() {
		return numMonsterSlots;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#setNumMonsterSlots(int)
	 */
	@Override
	public void setNumMonsterSlots(int numMonsterSlots) {
		this.numMonsterSlots = numMonsterSlots;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#getNumBonusMonsterSlots()
	 */
	@Override
	public int getNumBonusMonsterSlots() {
		return numBonusMonsterSlots;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#setNumBonusMonsterSlots(int)
	 */
	@Override
	public void setNumBonusMonsterSlots(int numBonusMonsterSlots) {
		this.numBonusMonsterSlots = numBonusMonsterSlots;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#getNumGemsRequired()
	 */
	@Override
	public int getNumGemsRequired() {
		return numGemsRequired;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#setNumGemsRequired(int)
	 */
	@Override
	public void setNumGemsRequired(int numGemsRequired) {
		this.numGemsRequired = numGemsRequired;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#getNumAcceptedFbInvites()
	 */
	@Override
	public int getNumAcceptedFbInvites() {
		return numAcceptedFbInvites;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#setNumAcceptedFbInvites(int)
	 */
	@Override
	public void setNumAcceptedFbInvites(int numAcceptedFbInvites) {
		this.numAcceptedFbInvites = numAcceptedFbInvites;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#getOccupationName()
	 */
	@Override
	public String getOccupationName() {
		return occupationName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResidence#setOccupationName(java.lang.String)
	 */
	@Override
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}

	@Override
	public String getImgSuffix()
	{
		return imgSuffix;
	}
	@Override
	public void setImgSuffix( String imgSuffix )
	{
		this.imgSuffix = imgSuffix;
	}
	
	@Override
	public String toString()
	{
		return "StructureResidence [struct="
			+ struct
			+ ", numMonsterSlots="
			+ numMonsterSlots
			+ ", numBonusMonsterSlots="
			+ numBonusMonsterSlots
			+ ", numGemsRequired="
			+ numGemsRequired
			+ ", numAcceptedFbInvites="
			+ numAcceptedFbInvites
			+ ", occupationName="
			+ occupationName
			+ ", imgSuffix="
			+ imgSuffix
			+ "]";
	}
	
}
