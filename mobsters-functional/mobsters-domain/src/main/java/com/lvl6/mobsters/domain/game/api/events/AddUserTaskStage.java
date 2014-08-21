package com.lvl6.mobsters.domain.game.api.events;


@SuppressWarnings("all")
final class AddUserTaskStage {
	private final String userTaskUuid;

	private final int stageNum;

	private final int taskStageMonsterId;

	private final String monsterType;

	private final int expGained;

	private final int cashGained;

	private final int oilGained;

	private final int droppedItemId;

	private final boolean monsterPieceDropped;

	AddUserTaskStage(
		final String userTaskUuid,
		final int stageNum,
		final int taskStageMonsterId,
		final String monsterType,
		final int expGained,
		final int cashGained,
		final int oilGained,
		final int droppedItemId,
		final boolean monsterPieceDropped)
	{
		this.userTaskUuid = userTaskUuid;
		this.stageNum = stageNum;
		this.taskStageMonsterId = taskStageMonsterId;
		this.monsterType = monsterType;
		this.expGained = expGained;
		this.cashGained = cashGained;
		this.oilGained = oilGained;
		this.droppedItemId = droppedItemId;
		this.monsterPieceDropped = monsterPieceDropped;

	}

	public String getUserTaskUuid()
	{
		return this.userTaskUuid;
	}

	public int getStageNum()
	{
		return this.stageNum;
	}

	public int getTaskStageMonsterId()
	{
		return this.taskStageMonsterId;
	}

	public String getMonsterType()
	{
		return this.monsterType;
	}

	public int getExpGained()
	{
		return this.expGained;
	}

	public int getCashGained()
	{
		return this.cashGained;
	}

	public int getOilGained()
	{
		return this.oilGained;
	}

	public int getDroppedItemId()
	{
		return this.droppedItemId;
	}

	public boolean getMonsterPieceDropped()
	{
		return this.monsterPieceDropped;
	}
}
