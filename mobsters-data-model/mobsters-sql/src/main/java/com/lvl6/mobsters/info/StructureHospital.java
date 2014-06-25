package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StructureHospital extends BaseIntPersistentObject{

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Structure struct;
	
	@Column(name = "queue_size")
	private int queueSize;	float healthPerSecond;
	
	public StructureHospital(){}
	public StructureHospital(Structure struct, int queueSize, float healthPerSecond) {
		super();
		this.struct = struct;
		this.queueSize = queueSize;
		this.healthPerSecond = healthPerSecond;
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
	public float getHealthPerSecond() {
		return healthPerSecond;
	}
	public void setHealthPerSecond(float healthPerSecond) {
		this.healthPerSecond = healthPerSecond;
	}
	
	@Override
	public String toString() {
		return "StructureHospital [structId=" + struct + ", queueSize="
				+ queueSize + ", healthPerSecond=" + healthPerSecond + "]";
	}
}
