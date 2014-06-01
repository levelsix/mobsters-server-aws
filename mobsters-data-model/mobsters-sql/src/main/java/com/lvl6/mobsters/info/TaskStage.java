package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TaskStage extends BasePersistentObject{

	
	private static final long serialVersionUID = -4429232440756191289L;
	@Column(name = "task_id")
	private int taskId;
	@Column(name = "stage_num")
	private int stageNum;	
	public TaskStage(){}
	public TaskStage(String id, int taskId, int stageNum) {
		super(id);
		this.taskId = taskId;
		this.stageNum = stageNum;
	}



	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getStageNum() {
		return stageNum;
	}

	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}

	@Override
	public String toString() {
		return "TaskStage [id=" + id + ", taskId=" + taskId + ", stageNum="
				+ stageNum + "]";
	}

}
