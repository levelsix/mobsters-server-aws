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

@Entity(name="StructureResourceStorage")
@Table(name="structure_resource_storage")
@Proxy(lazy=true, proxyClass=IStructureResourceStorage.class)
public class StructureResourceStorage extends BaseIntPersistentObject implements IStructureResourceStorage{

	private static final long serialVersionUID = 155994504717867502L;
	

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure struct;
	
	@Column(name = "resource_type_stored")
	private String resourceTypeStored;
	@Column(name = "capacity")
	private int capacity;	
	
	public StructureResourceStorage(){}
	public StructureResourceStorage(IStructure struct, String resourceTypeStored, int capacity) {
		super();
		this.struct = struct;
		this.resourceTypeStored = resourceTypeStored;
		this.capacity = capacity;
	}


	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceStorage#getStruct()
	 */
	@Override
	public IStructure getStruct()
	{
		return struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceStorage#setStruct(com.lvl6.mobsters.info.Structure)
	 */
	@Override
	public void setStruct( IStructure struct )
	{
		this.struct = struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceStorage#getResourceTypeStored()
	 */
	@Override
	public String getResourceTypeStored() {
		return resourceTypeStored;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceStorage#setResourceTypeStored(java.lang.String)
	 */
	@Override
	public void setResourceTypeStored(String resourceTypeStored) {
		this.resourceTypeStored = resourceTypeStored;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceStorage#getCapacity()
	 */
	@Override
	public int getCapacity() {
		return capacity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceStorage#setCapacity(int)
	 */
	@Override
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "ResourceStorage [structId=" + struct + ", resourceTypeStored="
				+ resourceTypeStored + ", capacity=" + capacity + "]";
	}
}
