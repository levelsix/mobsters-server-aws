package com.lvl6.mobsters.domain.game.api;

import com.lvl6.mobsters.info.ITask;


public interface IPlayerTask {
	String getTaskForUserId();
	
	ITask getTaskMeta();

	boolean completeOngoingTask();

	boolean cancelOngoingTask();
	
	void saveOngoingTask();
}
