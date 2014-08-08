package com.lvl6.mobsters.services.task;

import java.util.Date;
import java.util.List;

import com.lvl6.mobsters.common.utils.ICallableAction;
import com.lvl6.mobsters.dynamo.EventPersistentForUser;
import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.TaskStageForUser;
import com.lvl6.mobsters.utility.lambda.Director;

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

	public ICallableAction<GenerateUserTaskListener> generateUserTaskStages(
		String userId, Date curTime, int taskId, boolean isEvent, int eventId, int gemsSpent, 
		List<Integer> questIds, String elementName, boolean forceEnemyElem, 
		boolean alreadyCompletedMiniTutorialTask);

	public interface GenerateUserTaskListener // extends BeginAddUserTaskListener, AddUserTaskStageListener, FinishAddUserTaskListener
	{
		GenerateUserTaskListener beginUserTask(String userUuid, int taskId);
		
		GenerateUserTaskListener addUserTaskStage(
			int stageNum, 
			Director<AddStageGenerateUserTaskListener> optionsDirector
		);
		
		GenerateUserTaskListener endUserTask( String userUuid, int taskId, String userTaskUuid);
	}

	// TODO: This belongs in the event package and is merely reused by the impl from there.
	public interface AddStageGenerateUserTaskListener {
		AddStageGenerateUserTaskListener onAddStageMonster(
			int stageNum, int monsterId, 
			int monsterLevel, float dmgMulti,
			int expGiven, int cashGiven, int oilGiven, 
			int droppedItemId, boolean puzzlePieceGiven
		);

		AddStageGenerateUserTaskListener onAddStageMiniBoss(
			int stageNum, int monsterId, 
			int monsterLevel, float dmgMulti,
			int expGiven, int cashGiven, int oilGiven, 
			int droppedItemId, boolean puzzlePieceGiven
		);

		AddStageGenerateUserTaskListener onAddStageBosss(
			int stageNum, int monsterId, 
			int monsterLevel, float dmgMulti,
			int expGiven, int cashGiven, int oilGiven, 
			int droppedItemId, boolean puzzlePieceGiven
		);
	}

    /* END OF INTERFACE ********************************************************/    
}
