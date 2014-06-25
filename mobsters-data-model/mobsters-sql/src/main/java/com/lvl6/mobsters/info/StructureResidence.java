package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StructureResidence extends BaseIntPersistentObject{

	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Structure struct;
	
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
	
	public StructureResidence(){}
	public StructureResidence(Structure struct, int numMonsterSlots,
			int numBonusMonsterSlots, int numGemsRequired, int numAcceptedFbInvites,
			String occupationName) {
		super();
		this.struct = struct;
		this.numMonsterSlots = numMonsterSlots;
		this.numBonusMonsterSlots = numBonusMonsterSlots;
		this.numGemsRequired = numGemsRequired;
		this.numAcceptedFbInvites = numAcceptedFbInvites;
		this.occupationName = occupationName;
	}


	public Structure getStruct()
	{
		return struct;
	}
	public void setStruct( Structure struct )
	{
		this.struct = struct;
	}
	public int getNumMonsterSlots() {
		return numMonsterSlots;
	}

	public void setNumMonsterSlots(int numMonsterSlots) {
		this.numMonsterSlots = numMonsterSlots;
	}

	public int getNumBonusMonsterSlots() {
		return numBonusMonsterSlots;
	}

	public void setNumBonusMonsterSlots(int numBonusMonsterSlots) {
		this.numBonusMonsterSlots = numBonusMonsterSlots;
	}

	public int getNumGemsRequired() {
		return numGemsRequired;
	}

	public void setNumGemsRequired(int numGemsRequired) {
		this.numGemsRequired = numGemsRequired;
	}

	public int getNumAcceptedFbInvites() {
		return numAcceptedFbInvites;
	}

	public void setNumAcceptedFbInvites(int numAcceptedFbInvites) {
		this.numAcceptedFbInvites = numAcceptedFbInvites;
	}

	public String getOccupationName() {
		return occupationName;
	}

	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}

	@Override
	public String toString() {
		return "StructureResidence [structId=" + struct + ", numMonsterSlots="
				+ numMonsterSlots + ", numBonusMonsterSlots=" + numBonusMonsterSlots
				+ ", numGemsRequired=" + numGemsRequired + ", numAcceptedFbInvites="
				+ numAcceptedFbInvites + ", occupationName=" + occupationName + "]";
	}

}
