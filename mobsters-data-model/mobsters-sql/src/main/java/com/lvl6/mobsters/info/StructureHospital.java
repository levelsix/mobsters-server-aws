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

@Entity(name="StructureHospital")
@Table(name="structure_hospital")
@Proxy(lazy=true, proxyClass=IStructureHospital.class)
public class StructureHospital extends BaseIntPersistentObject implements IStructureHospital{

	private static final long serialVersionUID = 1217501402525066820L;
	

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure struct;
	
	@Column(name = "queue_size")
	private int queueSize;
	
	@Column(name = "health_per_second")
	float healthPerSecond;
	
	public StructureHospital(){}
	public StructureHospital(IStructure struct, int queueSize, float healthPerSecond) {
		super();
		this.struct = struct;
		this.queueSize = queueSize;
		this.healthPerSecond = healthPerSecond;
	}
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureHospital#getStruct()
	 */
	@Override
	public IStructure getStruct()
	{
		return struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureHospital#setStruct(com.lvl6.mobsters.info.Structure)
	 */
	@Override
	public void setStruct( IStructure struct )
	{
		this.struct = struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureHospital#getQueueSize()
	 */
	@Override
	public int getQueueSize() {
		return queueSize;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureHospital#setQueueSize(int)
	 */
	@Override
	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureHospital#getHealthPerSecond()
	 */
	@Override
	public float getHealthPerSecond() {
		return healthPerSecond;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureHospital#setHealthPerSecond(float)
	 */
	@Override
	public void setHealthPerSecond(float healthPerSecond) {
		this.healthPerSecond = healthPerSecond;
	}
	
	@Override
	public String toString() {
		return "StructureHospital [structId=" + struct + ", queueSize="
				+ queueSize + ", healthPerSecond=" + healthPerSecond + "]";
	}
}
