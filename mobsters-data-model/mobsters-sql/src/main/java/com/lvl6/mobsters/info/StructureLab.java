package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StructureLab extends BaseIntPersistentObject{

	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Structure struct;
	
	@Column(name = "queue_size")
	private int queueSize;	float pointsPerSecond;
	
	public StructureLab(){}
	public StructureLab(Structure struct, int queueSize, float pointsPerSecond) {
		super();
		this.struct = struct;
		this.queueSize = queueSize;
		this.pointsPerSecond = pointsPerSecond;
	}


	public Structure getStruct()
	{
		return struct;
	}
	public void setStruct( Structure struct )
	{
		this.struct = struct;
	}
	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public float getPointsPerSecond() {
		return pointsPerSecond;
	}

	public void setPointsPerSecond(float pointsPerSecond) {
		this.pointsPerSecond = pointsPerSecond;
	}
}
