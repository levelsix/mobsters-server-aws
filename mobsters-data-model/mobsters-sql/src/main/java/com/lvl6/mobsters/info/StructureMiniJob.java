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

@Entity(name="StructureMiniJob")
@Table(name="structure_mini_job")
@Proxy(lazy=true, proxyClass=IStructureMiniJob.class)
public class StructureMiniJob extends BaseIntPersistentObject implements IStructureMiniJob {

	private static final long serialVersionUID = -7186606098304818215L;
	

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IStructure struct;
	
	@Column(name = "generated_job_limit")
	private int generatedJobLimit;
	
	@Column(name = "hours_between_job_generation")
	private int hoursBetweenJobGeneration;
	
	public StructureMiniJob(){}
	public StructureMiniJob(IStructure struct, int generatedJobLimit, int hoursBetweenJobGeneration) {
		super();
		this.struct = struct;
		this.generatedJobLimit = generatedJobLimit;
		this.hoursBetweenJobGeneration = hoursBetweenJobGeneration;
	}
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureMiniJob#getStruct()
	 */
	@Override
	public IStructure getStruct()
	{
		return struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureMiniJob#setStruct(com.lvl6.mobsters.info.IStructure)
	 */
	@Override
	public void setStruct( IStructure struct )
	{
		this.struct = struct;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureMiniJob#getGeneratedJobLimit()
	 */
	@Override
	public int getGeneratedJobLimit()
	{
		return generatedJobLimit;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureMiniJob#setGeneratedJobLimit(int)
	 */
	@Override
	public void setGeneratedJobLimit( int generatedJobLimit )
	{
		this.generatedJobLimit = generatedJobLimit;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureMiniJob#getHoursBetweenJobGeneration()
	 */
	@Override
	public int getHoursBetweenJobGeneration()
	{
		return hoursBetweenJobGeneration;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStructureMiniJob#setHoursBetweenJobGeneration(int)
	 */
	@Override
	public void setHoursBetweenJobGeneration( int hoursBetweenJobGeneration )
	{
		this.hoursBetweenJobGeneration = hoursBetweenJobGeneration;
	}
	@Override
	public String toString()
	{
		return "StructureMiniJob [struct="
			+ struct
			+ ", generatedJobLimit="
			+ generatedJobLimit
			+ ", hoursBetweenJobGeneration="
			+ hoursBetweenJobGeneration
			+ "]";
	}
	
}
