package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StructureResourceGenerator extends BaseIntPersistentObject{



	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Structure struct;
	
	@Column(name = "resource_type_generated")
	private String resourceTypeGenerated;	//at the moment, some amount per hour

	@Column(name = "production_rate")
	private float productionRate;
	@Column(name = "capacity")
	private int capacity;	
	
	public StructureResourceGenerator(){}
	public StructureResourceGenerator(Structure struct, String resourceTypeGenerated,
			float productionRate, int capacity) {
		super();
		this.struct = struct;
		this.resourceTypeGenerated = resourceTypeGenerated;
		this.productionRate = productionRate;
		this.capacity = capacity;
	}


	public Structure getStruct()
	{
		return struct;
	}
	public void setStruct( Structure struct )
	{
		this.struct = struct;
	}
	public String getResourceTypeGenerated() {
		return resourceTypeGenerated;
	}

	public void setResourceTypeGenerated(String resourceTypeGenerated) {
		this.resourceTypeGenerated = resourceTypeGenerated;
	}

	public float getProductionRate() {
		return productionRate;
	}

	public void setProductionRate(float productionRate) {
		this.productionRate = productionRate;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "ResourceGenerator [structId=" + struct
				+ ", resourceTypeGenerated=" + resourceTypeGenerated
				+ ", productionRate=" + productionRate + ", capacity=" + capacity + "]";
	}
	
}
