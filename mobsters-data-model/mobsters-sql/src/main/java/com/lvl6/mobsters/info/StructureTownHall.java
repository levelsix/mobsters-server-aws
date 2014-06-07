package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StructureTownHall extends BaseIntPersistentObject{

	
	private static final long serialVersionUID = 1342783029881923550L;
	@Column(name = "struct_id")
	private int structId;
	@Column(name = "num_resource_one_generators")
	private int numResourceOneGenerators;
	@Column(name = "num_resource_one_storages")
	private int numResourceOneStorages;
	@Column(name = "num_resource_two_generators")
	private int numResourceTwoGenerators;
	@Column(name = "num_resource_two_storages")
	private int numResourceTwoStorages;
	@Column(name = "num_hospitals")
	private int numHospitals;
	@Column(name = "num_residences")
	private int numResidences;
	@Column(name = "num_monster_slots")
	private int numMonsterSlots;
	@Column(name = "num_labs")
	private int numLabs;
	@Column(name = "pvp_queue_cash_cost")
	private int pvpQueueCashCost;
	@Column(name = "resource_capacity")
	private int resourceCapacity;	
	public StructureTownHall(){}
	public StructureTownHall(int structId, int numResourceOneGenerators,
			int numResourceOneStorages, int numResourceTwoGenerators,
			int numResourceTwoStorages, int numHospitals, int numResidences,
			int numMonsterSlots, int numLabs, int pvpQueueCashCost,
			int resourceCapacity) {
		super();
		this.structId = structId;
		this.numResourceOneGenerators = numResourceOneGenerators;
		this.numResourceOneStorages = numResourceOneStorages;
		this.numResourceTwoGenerators = numResourceTwoGenerators;
		this.numResourceTwoStorages = numResourceTwoStorages;
		this.numHospitals = numHospitals;
		this.numResidences = numResidences;
		this.numMonsterSlots = numMonsterSlots;
		this.numLabs = numLabs;
		this.pvpQueueCashCost = pvpQueueCashCost;
		this.resourceCapacity = resourceCapacity;
	}

	public int getStructId() {
		return structId;
	}

	public void setStructId(int structId) {
		this.structId = structId;
	}

	public int getNumResourceOneGenerators() {
		return numResourceOneGenerators;
	}

	public void setNumResourceOneGenerators(int numResourceOneGenerators) {
		this.numResourceOneGenerators = numResourceOneGenerators;
	}

	public int getNumResourceOneStorages() {
		return numResourceOneStorages;
	}

	public void setNumResourceOneStorages(int numResourceOneStorages) {
		this.numResourceOneStorages = numResourceOneStorages;
	}

	public int getNumResourceTwoGenerators() {
		return numResourceTwoGenerators;
	}

	public void setNumResourceTwoGenerators(int numResourceTwoGenerators) {
		this.numResourceTwoGenerators = numResourceTwoGenerators;
	}

	public int getNumResourceTwoStorages() {
		return numResourceTwoStorages;
	}

	public void setNumResourceTwoStorages(int numResourceTwoStorages) {
		this.numResourceTwoStorages = numResourceTwoStorages;
	}

	public int getNumHospitals() {
		return numHospitals;
	}

	public void setNumHospitals(int numHospitals) {
		this.numHospitals = numHospitals;
	}

	public int getNumResidences() {
		return numResidences;
	}

	public void setNumResidences(int numResidences) {
		this.numResidences = numResidences;
	}

	public int getNumMonsterSlots() {
		return numMonsterSlots;
	}

	public void setNumMonsterSlots(int numMonsterSlots) {
		this.numMonsterSlots = numMonsterSlots;
	}

	public int getNumLabs() {
		return numLabs;
	}

	public void setNumLabs(int numLabs) {
		this.numLabs = numLabs;
	}

	public int getPvpQueueCashCost() {
		return pvpQueueCashCost;
	}

	public void setPvpQueueCashCost(int pvpQueueCashCost) {
		this.pvpQueueCashCost = pvpQueueCashCost;
	}

	public int getResourceCapacity() {
		return resourceCapacity;
	}

	public void setResourceCapacity(int resourceCapacity) {
		this.resourceCapacity = resourceCapacity;
	}

	@Override
	public String toString() {
		return "StructureTownHall [structId=" + structId
				+ ", numResourceOneGenerators=" + numResourceOneGenerators
				+ ", numResourceOneStorages=" + numResourceOneStorages
				+ ", numResourceTwoGenerators=" + numResourceTwoGenerators
				+ ", numResourceTwoStorages=" + numResourceTwoStorages
				+ ", numHospitals=" + numHospitals + ", numResidences=" + numResidences
				+ ", numMonsterSlots=" + numMonsterSlots + ", numLabs=" + numLabs
				+ ", pvpQueueCashCost=" + pvpQueueCashCost + ", resourceCapacity="
				+ resourceCapacity + "]";
	}
	
}
