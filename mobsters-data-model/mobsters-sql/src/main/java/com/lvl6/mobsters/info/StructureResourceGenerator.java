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

@Entity(name="StructureResourceGenerator")
@Table(name="structure_resource_generator")
@Proxy(lazy=true, proxyClass=IStructureResourceGenerator.class)
public class StructureResourceGenerator extends BaseIntPersistentObject implements IStructureResourceGenerator{

	private static final long serialVersionUID = 6894348581947398084L;
	

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure struct;
	
	@Column(name = "resource_type_generated")
	private String resourceTypeGenerated;	//at the moment, some amount per hour

	@Column(name = "production_rate")
	private float productionRate;
	@Column(name = "capacity")
	private int capacity;	
	
	public StructureResourceGenerator(){}
	public StructureResourceGenerator(IStructure struct, String resourceTypeGenerated,
			float productionRate, int capacity) {
		super();
		this.struct = struct;
		this.resourceTypeGenerated = resourceTypeGenerated;
		this.productionRate = productionRate;
		this.capacity = capacity;
	}


	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#getStruct()
	 */
	@Override
	public IStructure getStruct()
	{
		return struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#setStruct(com.lvl6.mobsters.info.Structure)
	 */
	@Override
	public void setStruct( IStructure struct )
	{
		this.struct = struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#getResourceTypeGenerated()
	 */
	@Override
	public String getResourceTypeGenerated() {
		return resourceTypeGenerated;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#setResourceTypeGenerated(java.lang.String)
	 */
	@Override
	public void setResourceTypeGenerated(String resourceTypeGenerated) {
		this.resourceTypeGenerated = resourceTypeGenerated;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#getProductionRate()
	 */
	@Override
	public float getProductionRate() {
		return productionRate;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#setProductionRate(float)
	 */
	@Override
	public void setProductionRate(float productionRate) {
		this.productionRate = productionRate;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#getCapacity()
	 */
	@Override
	public int getCapacity() {
		return capacity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureResourceGenerator#setCapacity(int)
	 */
	@Override
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
