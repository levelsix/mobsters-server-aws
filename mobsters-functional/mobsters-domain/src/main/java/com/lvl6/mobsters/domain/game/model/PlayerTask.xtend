package com.lvl6.mobsters.domain.game.model

import com.google.common.base.Preconditions
import com.lvl6.mobsters.domain.game.api.IPlayerTask
import com.lvl6.mobsters.dynamo.TaskForUserCompleted
import com.lvl6.mobsters.dynamo.TaskForUserOngoing
import com.lvl6.mobsters.info.IQuestJob
import com.lvl6.mobsters.info.ITask
import com.lvl6.mobsters.info.xtension.ConfigExtensions
import java.util.List
import java.util.Set

class PlayerTask 
	extends AbstractSemanticObject 
	implements IPlayerTask, IPlayerTaskInternal
{
	private val TaskForUserOngoing ongoingTask
	
	private val TaskForUserCompleted completedTask
	
	private val ITask taskMeta
	
	private val List<PlayerTaskStage> playerTaskStages
	
	/**
	 * Constructor for "wrap pre-existing"
	 */
	new( Player parent, TaskForUserOngoing ongoingTask, TaskForUserCompleted completedTask )
	{
		super(parent)
		
		Preconditions.checkNotNull(parent)
		Preconditions.checkArgument( 
			((ongoingTask==null) && (completedTask != null)) 
			|| ((ongoingTask != null) && (completedTask == null)),
			"Tasks must be either ongoing or complete, not both"
		)

		val extension ConfigExtensions configExtensionLib = repoRegistry.configExtensionLib
		
		if (ongoingTask != null ) {
			this.taskMeta = ongoingTask.taskId.taskMeta
		} else {
			this.taskMeta = completedTask.taskId.taskMeta
		}
		
		this.ongoingTask = ongoingTask
		this.completedTask = completedTask
		this.playerTaskStages = null
	}
	
	/**
	 * Constructor for "create and wrap"
	 */
	new( Player parent, ITask taskMeta, Set<IQuestJob> questJobs, 
		String elementName, boolean mayGeneratePieces )
	{
		super(parent)
		
		Preconditions.checkNotNull(parent)
		Preconditions.checkNotNull(taskMeta) 
		Preconditions.checkNotNull(questJobs)
		// Element Name may be null--it indicates select randomly rather than force selection.
		
		this.taskMeta = taskMeta
		this.ongoingTask = new TaskForUserOngoing() => [
			it.userId = this.userUuid
			it.taskId = taskMeta.id
		]
		this.completedTask = null

		this.playerTaskStages = 
			//for each stage, calculate the monster(s) the user will face and reward(s) given if user wins
			// 1) select monster at random
			// 2) determine if monster drops an item or puzzle piece
			// 3) calculate currency changes
			// NOTE: quest monster items are dropped based on QUEST JOB ID, not quest id
			taskMeta.taskStages.map [ stageMeta |
				new PlayerTaskStage(this, stageMeta, questJobs, elementName, mayGeneratePieces)
			]
	}
	
	override boolean isOngoingTask() { 
		return (this.ongoingTask != null)
	}
	
	override void cancelOngoingTask() {
		Preconditions.checkState(
			ongoingTask != null, 
			"Cannot cancel a completed task.  taskId=%s", 
			Integer.toString(taskMeta.id)
		)
		
		throw new UnsupportedOperationException("TODO: Not implemented yet")
	}

	override void completeOngoingTask() {
		Preconditions.checkState(
			ongoingTask != null, 
			"Cannot cancel a completed task.  taskId=%s", 
			Integer.toString(taskMeta.id)
		)
		
		throw new UnsupportedOperationException("TODO: Not implemented yet")
	}

	override saveOngoingTask() {
		Preconditions.checkState(
			ongoingTask != null, 
			"Cannot cancel a completed task.  taskId=%s", 
			Integer.toString(taskMeta.id)
		)
		
		repoRegistry => [
			beginTransaction()
			tfuOngoingRepo.save(ongoingTask)
			taskStageForUserRepo.saveEach(
				playerTaskStages.map[it.taskStageForUser]
			)
			commitTransaction()
		]
		return
	}

	// Semantic Unwrapping Methods (Server API only!)
	//
	override TaskForUserOngoing getTaskForUserOngoing() {
		Preconditions.checkState(ongoingTask != null, "Unwrap method for an ongoing task was invoked on the SemanticTask for a completed task")
		return ongoingTask
	}
		
	override TaskForUserCompleted getTaskForUserCompleted() {
		Preconditions.checkState(completedTask != null, "Unwrap method for a completed task was invoked on the SemanticTask for an ongoing task")
		return completedTask
	}
	
	// Server events
	//
	
//	@EventFactory(targetListeners=ListenerKind.BOTH)
//	public def void publishBeginAddUserTask(
//		String userUuid, int taskId )
//	{
//	}
//	
//	@EventFactory(targetListeners=ListenerKind.BOTH)
//	public def void publishFinishAddUserTask(
//		String userUuid, int taskId, String userTaskUuid )
//	{
//	}
	
		
	// Semantic Passthrough Methods
	//	
	public override ITask getTaskMeta() {
		return taskMeta
	}
	
	public override String getTaskForUserId() {
		var String retVal = null;
		if (ongoingTask != null) {
			retVal = ongoingTask.taskForUserId
		} 
		
		// Apparently we discard the TaskForUserId when transfering ongoing tasks over to the completed table,
		// probably related to a choice to not store redundant passes through the same task peyond the first.
		
		return retVal
	}
}