package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StructureHospital extends BasePersistentObject{
/**
	 * 
	 */

	
	private static final long serialVersionUID = -3569030137246330339L;
	//
	//	private static final long serialVersionUID = -3569030137246330339L;
	@Column(name = "struct_id")
	private int structId;
	@Column(name = "queue_size")
	private int queueSize;	float healthPerSecond;
	
	public StructureHospital(){}
	public StructureHospital(int structId, int queueSize, float healthPerSecond) {
		super();
		this.structId = structId;
		this.queueSize = queueSize;
		this.healthPerSecond = healthPerSecond;
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
	public float getHealthPerSecond() {
		return healthPerSecond;
	}
	public void setHealthPerSecond(float healthPerSecond) {
		this.healthPerSecond = healthPerSecond;
	}
	
	@Override
	public String toString() {
		return "StructureHospital [structId=" + structId + ", queueSize="
				+ queueSize + ", healthPerSecond=" + healthPerSecond + "]";
	}
}
