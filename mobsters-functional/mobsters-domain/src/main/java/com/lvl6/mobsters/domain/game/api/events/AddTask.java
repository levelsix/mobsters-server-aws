package com.lvl6.mobsters.domain.game.api.events;

import com.google.common.collect.ImmutableList;

final class AddTask implements IGameEvent {
	private final String taskUuid;

	private final String userUuid;

	private final int taskId;

	private final ImmutableList<AddUserTaskStage> stageList;

	AddTask(
		final String taskUuid,
		final int taskId,
		final String userUuid,
		ImmutableList<AddUserTaskStage> stageList
	) {
		this.taskUuid = taskUuid;
		this.userUuid = userUuid;
		this.taskId = taskId;
		this.stageList = stageList;
	}

	public String getTaskUuid()
	{
		return this.taskUuid;
	}

	public String getUserUuid()
	{
		return this.userUuid;
	}

	public int getTaskId()
	{
		return this.taskId;
	}

	public ImmutableList<AddUserTaskStage> getStageList()
	{
		return stageList;
	}
}
