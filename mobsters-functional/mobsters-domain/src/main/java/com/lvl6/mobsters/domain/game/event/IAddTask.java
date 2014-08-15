package com.lvl6.mobsters.domain.game.event;

import com.google.common.collect.ImmutableList;

public interface IAddTask extends IGameEvent {
	public String getTaskUuid();

	public int getTaskId();

	public ImmutableList<AddUserTaskStage> getStageList();
}