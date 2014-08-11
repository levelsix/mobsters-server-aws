package com.lvl6.mobsters.domain.game.api;

import com.lvl6.mobsters.info.ITask;


public interface IPlayerTask {
	String getTaskForUserId();
	
	ITask getTaskMeta();

	boolean isOngoingTask();
	
	void completeOngoingTask();

	void cancelOngoingTask();
	
	void saveOngoingTask();
}
