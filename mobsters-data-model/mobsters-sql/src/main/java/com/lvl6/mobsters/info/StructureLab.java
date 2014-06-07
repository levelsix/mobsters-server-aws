package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StructureLab extends BaseIntPersistentObject{

	
	private static final long serialVersionUID = -1240983293629757080L;
	@Column(name = "struct_id")
	private int structId;
	@Column(name = "queue_size")
	private int queueSize;	float pointsPerSecond;
	
	public StructureLab(){}
	public StructureLab(int structId, int queueSize, float pointsPerSecond) {
		super();
		this.structId = structId;
		this.queueSize = queueSize;
		this.pointsPerSecond = pointsPerSecond;
	}

	public int getStructId() {
		return structId;
	}

	public void setStructId(int structId) {
		this.structId = structId;
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
