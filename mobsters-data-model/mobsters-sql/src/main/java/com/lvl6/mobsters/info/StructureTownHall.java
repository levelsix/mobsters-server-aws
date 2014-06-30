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

@Entity(name="StructureTownHall")
@Table(name="structure_town_hall")
@Proxy(lazy=true, proxyClass=IStructureTownHall.class)
public class StructureTownHall extends BaseIntPersistentObject implements IStructureTownHall{

	
	private static final long serialVersionUID = 1342783029881923550L;
	
	@OneToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure struct;
	
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
	public StructureTownHall(IStructure struct, int numResourceOneGenerators,
			int numResourceOneStorages, int numResourceTwoGenerators,
			int numResourceTwoStorages, int numHospitals, int numResidences,
			int numMonsterSlots, int numLabs, int pvpQueueCashCost,
			int resourceCapacity) {
		super();
		this.struct = struct;
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


	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getStruct()
	 */
	@Override
	public IStructure getStruct()
	{
		return struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setStruct(com.lvl6.mobsters.info.Structure)
	 */
	@Override
	public void setStruct( IStructure struct )
	{
		this.struct = struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumResourceOneGenerators()
	 */
	@Override
	public int getNumResourceOneGenerators() {
		return numResourceOneGenerators;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumResourceOneGenerators(int)
	 */
	@Override
	public void setNumResourceOneGenerators(int numResourceOneGenerators) {
		this.numResourceOneGenerators = numResourceOneGenerators;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumResourceOneStorages()
	 */
	@Override
	public int getNumResourceOneStorages() {
		return numResourceOneStorages;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumResourceOneStorages(int)
	 */
	@Override
	public void setNumResourceOneStorages(int numResourceOneStorages) {
		this.numResourceOneStorages = numResourceOneStorages;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumResourceTwoGenerators()
	 */
	@Override
	public int getNumResourceTwoGenerators() {
		return numResourceTwoGenerators;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumResourceTwoGenerators(int)
	 */
	@Override
	public void setNumResourceTwoGenerators(int numResourceTwoGenerators) {
		this.numResourceTwoGenerators = numResourceTwoGenerators;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumResourceTwoStorages()
	 */
	@Override
	public int getNumResourceTwoStorages() {
		return numResourceTwoStorages;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumResourceTwoStorages(int)
	 */
	@Override
	public void setNumResourceTwoStorages(int numResourceTwoStorages) {
		this.numResourceTwoStorages = numResourceTwoStorages;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumHospitals()
	 */
	@Override
	public int getNumHospitals() {
		return numHospitals;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumHospitals(int)
	 */
	@Override
	public void setNumHospitals(int numHospitals) {
		this.numHospitals = numHospitals;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumResidences()
	 */
	@Override
	public int getNumResidences() {
		return numResidences;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumResidences(int)
	 */
	@Override
	public void setNumResidences(int numResidences) {
		this.numResidences = numResidences;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumMonsterSlots()
	 */
	@Override
	public int getNumMonsterSlots() {
		return numMonsterSlots;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumMonsterSlots(int)
	 */
	@Override
	public void setNumMonsterSlots(int numMonsterSlots) {
		this.numMonsterSlots = numMonsterSlots;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getNumLabs()
	 */
	@Override
	public int getNumLabs() {
		return numLabs;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setNumLabs(int)
	 */
	@Override
	public void setNumLabs(int numLabs) {
		this.numLabs = numLabs;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getPvpQueueCashCost()
	 */
	@Override
	public int getPvpQueueCashCost() {
		return pvpQueueCashCost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setPvpQueueCashCost(int)
	 */
	@Override
	public void setPvpQueueCashCost(int pvpQueueCashCost) {
		this.pvpQueueCashCost = pvpQueueCashCost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#getResourceCapacity()
	 */
	@Override
	public int getResourceCapacity() {
		return resourceCapacity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureTownHall#setResourceCapacity(int)
	 */
	@Override
	public void setResourceCapacity(int resourceCapacity) {
		this.resourceCapacity = resourceCapacity;
	}

	@Override
	public String toString() {
		return "StructureTownHall [structId=" + struct
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
