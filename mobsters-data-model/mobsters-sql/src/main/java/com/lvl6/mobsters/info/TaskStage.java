package com.lvl6.mobsters.info;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="TaskStage")
@Table(name="task_stage")
@Proxy(lazy=true, proxyClass=ITaskStage.class)
public class TaskStage extends BaseIntPersistentObject implements ITaskStage{

	private static final long serialVersionUID = -4763468164533816643L;
	
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Quest.class)
	@JoinColumn(
		name = "task_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ITask task;

	@Column(name = "stage_num")
	private int stageNum;	
	
	@OneToMany(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.LAZY,
		mappedBy="stage", 
		orphanRemoval=true,
		targetEntity=TaskStageMonster.class)
	private List<ITaskStageMonster> stageMonsters;

	public TaskStage() {
		this.stageMonsters = new ArrayList<ITaskStageMonster>(3);
	}
	
	public TaskStage(
		int id, ITask task, int stageNum, List<ITaskStageMonster> stageMonsters)
	{
		super(id);
		this.task = task;
		this.stageNum = stageNum;
		if (stageMonsters == null) {
			this.stageMonsters = new ArrayList<ITaskStageMonster>(3);
		} else {
			this.stageMonsters = stageMonsters;
		}
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITaskStage#getTaskId()
	 */
	@Override
	public ITask getTask() {
		return task;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITaskStage#setTaskId(int)
	 */
	@Override
	public void setTask(ITask task) {
		this.task = task;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITaskStage#getStageNum()
	 */
	@Override
	public int getStageNum() {
		return stageNum;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITaskStage#setStageNum(int)
	 */
	@Override
	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}
	
	@Override
	public List<ITaskStageMonster> getStageMonsters() {
		return this.stageMonsters;
	}

	@Override
	public String toString() {
		return "TaskStage [id=" + id + ", taskId=" + task.getId() + ", stageNum="
				+ stageNum + "]";
	}

}
