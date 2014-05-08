package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TaskStageForUser extends BasePersistentObject{

	
	private static final long serialVersionUID = -8159939137604127481L;	


	@Column(name = "user_task_id")
	private long userTaskId;
	@Column(name = "stage_num")
	private int stageNum;
	@Column(name = "task_stage_monster_id")
	private int taskStageMonsterId; //not task stage monster monster id
	@Column(name = "monster_type")
	private String monsterType;
	@Column(name = "exp_gained")
	private int expGained;
	@Column(name = "cash_gained")
	private int cashGained;
	@Column(name = "oil_gained")
	private int oilGained;
	@Column(name = "monster_piece_dropped")
	private boolean monsterPieceDropped;
	@Column(name = "item_id_dropped")
	private int itemIdDropped;	//maybe should specify the (enhancement) level of monster dropped
	
	public TaskStageForUser(){}
	public TaskStageForUser(long userTaskId, int stageNum,
			int taskStageMonsterId, String monsterType, int expGained,
			int cashGained, int oilGained, boolean monsterPieceDropped,
			int itemIdDropped) {
		super();
		this.userTaskId = userTaskId;
		this.stageNum = stageNum;
		this.taskStageMonsterId = taskStageMonsterId;
		this.monsterType = monsterType;
		this.expGained = expGained;
		this.cashGained = cashGained;
		this.oilGained = oilGained;
		this.monsterPieceDropped = monsterPieceDropped;
		this.itemIdDropped = itemIdDropped;
	}



	public long getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(long userTaskId) {
		this.userTaskId = userTaskId;
	}

	public int getStageNum() {
		return stageNum;
	}

	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}

	public int getTaskStageMonsterId() {
		return taskStageMonsterId;
	}

	public void setTaskStageMonsterId(int taskStageMonsterId) {
		this.taskStageMonsterId = taskStageMonsterId;
	}

	public String getMonsterType() {
		return monsterType;
	}

	public void setMonsterType(String monsterType) {
		this.monsterType = monsterType;
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

	public boolean isMonsterPieceDropped() {
		return monsterPieceDropped;
	}

	public void setMonsterPieceDropped(boolean monsterPieceDropped) {
		this.monsterPieceDropped = monsterPieceDropped;
	}

	public int getItemIdDropped() {
		return itemIdDropped;
	}

	public void setItemIdDropped(int itemIdDropped) {
		this.itemIdDropped = itemIdDropped;
	}

	@Override
	public String toString() {
		return "TaskStageForUser [id=" + id + ", userTaskId=" + userTaskId
				+ ", stageNum=" + stageNum + ", taskStageMonsterId="
				+ taskStageMonsterId + ", monsterType=" + monsterType + ", expGained="
				+ expGained + ", cashGained=" + cashGained + ", oilGained=" + oilGained
				+ ", monsterPieceDropped=" + monsterPieceDropped + ", itemIdDropped="
				+ itemIdDropped + "]";
	}
	
}
