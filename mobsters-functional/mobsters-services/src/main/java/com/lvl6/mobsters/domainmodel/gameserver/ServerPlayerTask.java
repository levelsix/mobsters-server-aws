package com.lvl6.mobsters.domainmodel.gameserver;

import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.info.ITask;


public interface ServerPlayerTask {
	// String getUuid();
	
	String getTaskForUserId();
	
	ITask getTaskMeta();
	
	boolean cancelOngoingTask();
	
	boolean completeOngoingTask();
	
	void saveOngoingTask();
	
	// Server Unwrap Methods -- TODO: These should not be required!
	//
	TaskForUserOngoing getTaskForUserOngoing();
		
	TaskForUserCompleted getTaskForUserCompleted();
}
