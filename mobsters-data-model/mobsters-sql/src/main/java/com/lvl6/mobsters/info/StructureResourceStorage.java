package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StructureResourceStorage extends BaseIntPersistentObject{
/**
	 * 
	 */

	
	private static final long serialVersionUID = -1614890686257355530L;
	//
//	private static final long serialVersionUID = 5021640371198924904L;
	@Column(name = "struct_id")
	private int structId;
	@Column(name = "resource_type_stored")
	private String resourceTypeStored;
	@Column(name = "capacity")
	private int capacity;	
	public StructureResourceStorage(){}
	public StructureResourceStorage(int structId, String resourceTypeStored, int capacity) {
		super();
		this.structId = structId;
		this.resourceTypeStored = resourceTypeStored;
		this.capacity = capacity;
	}

	public int getStructId() {
		return structId;
	}

	public void setStructId(int structId) {
		this.structId = structId;
	}

	public String getResourceTypeStored() {
		return resourceTypeStored;
	}

	public void setResourceTypeStored(String resourceTypeStored) {
		this.resourceTypeStored = resourceTypeStored;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "ResourceStorage [structId=" + structId + ", resourceTypeStored="
				+ resourceTypeStored + ", capacity=" + capacity + "]";
	}
}
