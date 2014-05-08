package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StructureResourceGenerator extends BasePersistentObject{
/**
	 * 
	 */

	
	private static final long serialVersionUID = -2371172975086740032L;
	//
	//	private static final long serialVersionUID = -2371172975086740032L;
	@Column(name = "struct_id")
	private int structId;
	@Column(name = "resource_type_generated")
	private String resourceTypeGenerated;	//at the moment, some amount per hour

	@Column(name = "production_rate")
	private float productionRate;
	@Column(name = "capacity")
	private int capacity;	
	public StructureResourceGenerator(){}
	public StructureResourceGenerator(int structId, String resourceTypeGenerated,
			float productionRate, int capacity) {
		super();
		this.structId = structId;
		this.resourceTypeGenerated = resourceTypeGenerated;
		this.productionRate = productionRate;
		this.capacity = capacity;
	}

	public int getStructId() {
		return structId;
	}

	public void setStructId(int structId) {
		this.structId = structId;
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
		return "ResourceGenerator [structId=" + structId
				+ ", resourceTypeGenerated=" + resourceTypeGenerated
				+ ", productionRate=" + productionRate + ", capacity=" + capacity + "]";
	}
	
}
