package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TaskForUserCompleted extends BasePersistentObject{

	@Column(name = "final")
	private static final long serialVersionUID = 1056343725813049903L;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "task_id")
	private int taskId;
	@Column(name = "time_of_entry")
	private Date timeOfEntry;	
	public TaskForUserCompleted(){}
	public TaskForUserCompleted(int userId, int taskId, Date timeOfEntry) {
		super();
		this.userId = userId;
		this.taskId = taskId;
		this.timeOfEntry = timeOfEntry;
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

	public Date getTimeOfEntry() {
		return timeOfEntry;
	}

	public void setTimeOfEntry(Date timeOfEntry) {
		this.timeOfEntry = timeOfEntry;
	}

	@Override
	public String toString() {
		return "TaskForUserCompleted [userId=" + userId + ", taskId=" + taskId
				+ ", timeOfEntry=" + timeOfEntry + "]";
	}

}
