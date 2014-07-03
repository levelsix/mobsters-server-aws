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

@Entity(name="Task")
@Table(name="task")
@Proxy(lazy=true, proxyClass=ITask.class)
public class Task extends BaseIntPersistentObject implements ITask{

	private static final long serialVersionUID = -8221278285986682349L;
	

	@Column(name = "good_name")
	private String goodName;

	@Column(name = "description")
	private String description;

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Task.class, optional=true)
	@JoinColumn(
		name = "prerequisite_task_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ITask prerequisiteTask;

	@OneToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(
		name = "prerequisite_quest_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Quest prerequisiteQuest;	

	public Task() {
		super();
	}


	public Task(final int id, final String goodName, final String description,
			final ITask prerequisiteTask, final Quest prerequisiteQuest) {
		super(id);
		this.goodName = goodName;
		this.description = description;
		this.prerequisiteTask = prerequisiteTask;
		this.prerequisiteQuest = prerequisiteQuest;
	}
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getGoodName()
	 */
	@Override
	public String getGoodName() {
		return goodName;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setGoodName(java.lang.String)
	 */
	@Override
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getPrerequisiteTask()
	 */
	@Override
	public ITask getPrerequisiteTask() {
		return prerequisiteTask;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setPrerequisiteTask(com.lvl6.mobsters.info.ITask)
	 */
	@Override
	public void setPrerequisiteTask(ITask prerequisiteTask) {
		this.prerequisiteTask = prerequisiteTask;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getPrerequisiteQuest()
	 */
	@Override
	public Quest getPrerequisiteQuest() {
		return prerequisiteQuest;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setPrerequisiteQuest(com.lvl6.mobsters.info.Quest)
	 */
	@Override
	public void setPrerequisiteQuest(Quest prerequisiteQuest) {
		this.prerequisiteQuest = prerequisiteQuest;
	}


	@Override
	public String toString()
	{
		return "Task [goodName="
			+ goodName
			+ ", description="
			+ description
			+ ", prerequisiteTask="
			+ prerequisiteTask
			+ ", prerequisiteQuest="
			+ prerequisiteQuest
			+ "]";
	}

}
