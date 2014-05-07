package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TaskForUserOngoing extends BasePersistentObject{	

	@Column(name = "final")
	private static final long serialVersionUID = 8030638296503679697L;	

	@Column(name = "id")
	private long id;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "task_id")
	private int taskId;	public int expGained;
	public int cashGained;
	public int oilGained;
	public int numRevives;

	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "task_stage_id")
	private int taskStageId;	
	public TaskForUserOngoing(){}
	public TaskForUserOngoing(long id, int userId, int taskId, int expGained,
			int cashGained, int oilGained, int numRevives, Date startDate,
			int taskStageId) {
		super();
		this.userId = userId;
		this.taskId = taskId;
		this.expGained = expGained;
		this.cashGained = cashGained;
		this.oilGained = oilGained;
		this.numRevives = numRevives;
		this.startDate = startDate;
		this.taskStageId = taskStageId;
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getExpGained() {
		return expGained;
	}

	public void setExpGained(int expGained) {
		this.expGained = expGained;
	}

	public int getCashGained() {
		return cashGained;
	}

	public void setCashGained(int cashGained) {
		this.cashGained = cashGained;
	}

	public int getOilGained() {
		return oilGained;
	}

	public void setOilGained(int oilGained) {
		this.oilGained = oilGained;
	}

	public int getNumRevives() {
		return numRevives;
	}

	public void setNumRevives(int numRevives) {
		this.numRevives = numRevives;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getTaskStageId() {
		return taskStageId;
	}

	public void setTaskStageId(int taskStageId) {
		this.taskStageId = taskStageId;
	}

	@Override
	public String toString() {
		return "TaskForUserOngoing [id=" + id + ", userId=" + userId
				+ ", taskId=" + taskId + ", expGained=" + expGained
				+ ", cashGained=" + cashGained + ", oilGained=" + oilGained
				+ ", numRevives=" + numRevives + ", startDate=" + startDate
				+ ", taskStageId=" + taskStageId + "]";
	}

}
