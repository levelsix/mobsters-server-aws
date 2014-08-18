package com.lvl6.mobsters.domain.game.model;

import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.info.ITask;


interface IPlayerTaskInternal {
	// String getUuid();
	
	String getTaskForUserId();
	
	ITask getTaskMeta();
	
	void cancelOngoingTask();
	
	void completeOngoingTask();
	
	void saveOngoingTask();
	
	// Server Unwrap Methods -- TODO: These should not be required!
	//
	TaskForUserOngoing getTaskForUserOngoing();
		
	TaskForUserCompleted getTaskForUserCompleted();
}
