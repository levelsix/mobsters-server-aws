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

@Entity(name="StructureLab")
@Table(name="structure_lab")
@Proxy(lazy=true, proxyClass=IStructureLab.class)
public class StructureLab extends BaseIntPersistentObject implements IStructureLab{

	private static final long serialVersionUID = -7843526067221565804L;
	

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure struct;
	
	@Column(name = "queue_size")
	private int queueSize;
	
	@Column(name = "points_per_second")
	private float pointsPerSecond;
	
	public StructureLab(){}
	public StructureLab(IStructure struct, int queueSize, float pointsPerSecond) {
		super();
		this.struct = struct;
		this.queueSize = queueSize;
		this.pointsPerSecond = pointsPerSecond;
	}


	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureLab#getStruct()
	 */
	@Override
	public IStructure getStruct()
	{
		return struct;
	}
	public void setStruct( IStructure struct )
	{
		this.struct = struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureLab#getQueueSize()
	 */
	@Override
	public int getQueueSize() {
		return queueSize;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureLab#setQueueSize(int)
	 */
	@Override
	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureLab#getPointsPerSecond()
	 */
	@Override
	public float getPointsPerSecond() {
		return pointsPerSecond;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureLab#setPointsPerSecond(float)
	 */
	@Override
	public void setPointsPerSecond(float pointsPerSecond) {
		this.pointsPerSecond = pointsPerSecond;
	}
	@Override
	public String toString()
	{
		return "StructureLab [struct="
			+ struct
			+ ", queueSize="
			+ queueSize
			+ ", pointsPerSecond="
			+ pointsPerSecond
			+ "]";
	}
	
}
