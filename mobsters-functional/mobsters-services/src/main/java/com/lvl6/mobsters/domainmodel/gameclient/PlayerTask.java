package com.lvl6.mobsters.domainmodel.gameclient;

import com.lvl6.mobsters.info.ITask;


public interface PlayerTask {
	String getTaskForUserId();
	
	ITask getTaskMeta();

	boolean completeOngoingTask();

	boolean cancelOngoingTask();
	
	void saveOngoingTask();
}
