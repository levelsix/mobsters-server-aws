package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StructureResidence extends BasePersistentObject{

	
	private static final long serialVersionUID = 5657322728558293593L;
	@Column(name = "struct_id")
	private int structId;	//how many monster slots this residence gives the user (absolute number)
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
	public StructureResidence(int structId, int numMonsterSlots,
			int numBonusMonsterSlots, int numGemsRequired, int numAcceptedFbInvites,
			String occupationName) {
		super();
		this.structId = structId;
		this.numMonsterSlots = numMonsterSlots;
		this.numBonusMonsterSlots = numBonusMonsterSlots;
		this.numGemsRequired = numGemsRequired;
		this.numAcceptedFbInvites = numAcceptedFbInvites;
		this.occupationName = occupationName;
	}

	public int getStructId() {
		return structId;
	}
	
	public void setStructId(int structId) {
		this.structId = structId;
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
		return "StructureResidence [structId=" + structId + ", numMonsterSlots="
				+ numMonsterSlots + ", numBonusMonsterSlots=" + numBonusMonsterSlots
				+ ", numGemsRequired=" + numGemsRequired + ", numAcceptedFbInvites="
				+ numAcceptedFbInvites + ", occupationName=" + occupationName + "]";
	}

}
