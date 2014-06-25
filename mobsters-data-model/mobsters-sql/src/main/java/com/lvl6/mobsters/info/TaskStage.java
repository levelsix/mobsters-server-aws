package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="TaskStage")
@Table(name="task_stage")
@Proxy(lazy=true, proxyClass=ITaskStage.class)
public class TaskStage extends BaseIntPersistentObject implements ITaskStage{

	private static final long serialVersionUID = -4763468164533816643L;
	
	@Column(name = "task_id")
	private int taskId;
	@Column(name = "stage_num")
	private int stageNum;	
	public TaskStage(){}
	public TaskStage(int id, int taskId, int stageNum) {
		super(id);
		this.taskId = taskId;
		this.stageNum = stageNum;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITaskStage#getTaskId()
	 */
	@Override
	public int getTaskId() {
		return taskId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITaskStage#setTaskId(int)
	 */
	@Override
	public void setTaskId(int taskId) {
		this.taskId = taskId;
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
	public String toString() {
		return "TaskStage [id=" + id + ", taskId=" + taskId + ", stageNum="
				+ stageNum + "]";
	}

}
