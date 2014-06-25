package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StructureResourceStorage extends BaseIntPersistentObject{


	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Structure struct;
	
	@Column(name = "resource_type_stored")
	private String resourceTypeStored;
	@Column(name = "capacity")
	private int capacity;	
	
	public StructureResourceStorage(){}
	public StructureResourceStorage(Structure struct, String resourceTypeStored, int capacity) {
		super();
		this.struct = struct;
		this.resourceTypeStored = resourceTypeStored;
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
		return "ResourceStorage [structId=" + struct + ", resourceTypeStored="
				+ resourceTypeStored + ", capacity=" + capacity + "]";
	}
}
