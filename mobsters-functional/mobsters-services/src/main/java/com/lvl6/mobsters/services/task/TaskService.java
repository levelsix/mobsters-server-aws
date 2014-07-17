package com.lvl6.mobsters.services.task;

import java.util.Date;
import java.util.List;

import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.dynamo.EventPersistentForUser;
import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.TaskStageForUser;

public interface TaskService {
	/* BEGIN NON-CRUD METHODS **************************************************/
	
	/* BEGIN READ ONLY METHODS *************************************************/
    
	/**
	 * TODO: This is not a valid service method!!  It leaks a Domain Object to the caller.
	 */
	public TaskForUserOngoing getUserTaskForUserId( String userId );

	/**
	 * TODO: This is not a valid service method!!  It leaks a Domain Object to the caller.
	 */
	public List<TaskForUserCompleted> getTaskCompletedForUser( String userId ); 
	
	/**
	 * TODO: This is not a valid service method!!  It leaks a Domain Object to the caller.
	 */
	public List<TaskStageForUser> getTaskStagesForUserWithTaskForUserId( String userTaskId );
	
	/**
	 * TODO: This is not a valid service method!!  It leaks a Domain Object to the caller.
	 */
	public List<EventPersistentForUser> getUserPersistentEventForUserId( String userId );
	
	/* BEGIN TRANSACTIONAL METHODS ********************************************/
    
    public void completeTasks( String userId, Director<CompleteTasksBuilder> listDirector);
    
    public interface CompleteTasksBuilder {
    	/**
    	 * Completes a task whose time-of-entry is presumed already known.
    	 * 
    	 * @param taskId
    	 */
    	CompleteTasksBuilder taskId(int taskId);

    	/**
    	 * Completes a task given an explicit time of entry.  Intended for tasks (such as tutorial) 
    	 * that are not retained as active if interrupted before completion.
    	 * 
    	 * @param taskId
    	 */
    	CompleteTasksBuilder taskId(int taskId, Date timeOfEntry);
    }
	
    /* END OF INTERFACE ********************************************************/    
}
