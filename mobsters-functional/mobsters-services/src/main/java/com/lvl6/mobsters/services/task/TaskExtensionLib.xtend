package com.lvl6.mobsters.services.task

import com.google.common.base.Preconditions
import com.lvl6.mobsters.domain.game.api.IPlayerTask
import com.lvl6.mobsters.dynamo.TaskForUserCompleted
import com.lvl6.mobsters.dynamo.TaskForUserOngoing
import com.lvl6.mobsters.dynamo.TaskStageForUser
import com.lvl6.mobsters.dynamo.repository.TaskStageForUserRepository
import com.lvl6.mobsters.info.Task
import java.util.ArrayList
import java.util.List
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.google.common.base.Preconditions.*

import static extension java.lang.String.format

// TODO: Place all Extension Libraries in a parallel package task
//       and use CheckStyle to detect attempts to use them outside the
//       Service layer.  Java package visibility is insufficient to
//       both package-per-feature and layer-scoped visibility control.
/**
 * Data Layer components may augment the domain objects to provide
 * business logic that each individual service component may access.
 * 
 * Sharing through the public interface is problematic because it
 * leads to redundant repository access due to the otherwise
 * desirable design constraint that it remain independent of the
 * domain objects that services rely onto implement its contract.
 * 
 * To attach the methods in this library to the TaskForUser
 * and ObstacleForUser domain objects.
 * 
 * 1)  Import this class in your compilation unit
 * 2)  In the Dependency injection section, declare an extension
 *     variable of this type:
 * 
 *     extension TaskExtensionLib structExtension
 * 
 * 3)  Invoke the methods as though they were operations of the
 *     TaskForUser and ObstacleForUser classes.  E.g,:
 * 
 *     var TaskForUser sfu = sfuRepo.load(...)
 *     sfu.moveTo( 20, 20 )
 * 
 * Extensions will not help with stateful extensions (e.g. navigation
 * to a Task from a TaskForUser), but there are other
 * ideas in the works for those use cases.
 */
@Component
class TaskExtensionLib {
	@Autowired
	TaskStageForUserRepository taskForUserStageRepo;

	public def String format(IPlayerTask task) {
		return "PlayerTask[%s(%d)]".format(task.taskForUserId, task.taskMeta.id)
	}

	public def String format(TaskForUserOngoing task) {
		return "TaskForUser[%s(%d)]".format(task.taskForUserId, task.taskId)
	}

	public def TaskForUserOngoing beginNewTask(Task task) {
		checkNotNull(task)
		val TaskForUserOngoing dataObject = new TaskForUserOngoing()
		dataObject.putAttachment(Task, task)
		return dataObject
	}

	public def List<TaskStageForUser> getUserTaskStages(TaskForUserOngoing tfu) {
		checkNotNull(tfu)
		var SemanticTaskForUser ctxt = tfu.derivedContext
		return ctxt.getUserTaskStages()
	}

	private def SemanticTaskForUser getDerivedContext(TaskForUserOngoing tfu) {
		var SemanticTaskForUser ctxt = tfu.getAttachment(SemanticTaskForUser)
		if (ctxt == null) {
			ctxt = new SemanticTaskForUser(tfu, taskForUserStageRepo)
			tfu.putAttachment(SemanticTaskForUser, ctxt)
		}
		return ctxt
	}
}

class SemanticTaskForUser {
	val TaskForUserOngoing tfuo
	val TaskForUserCompleted tfuc

	// This utility was built with an assumption that it was fair to submit new content
	// by letting users add it to a model-provided collection.  Unfortunately, that makes
	// observing changes to those collections difficult unless we write Decorators or 
	// alternate collection.  Unless and until then, try to backpedal from this style of
	// API in favor of read-only collections that represent a snapshot value result, not
	// a real-time and change-reactive authoritative abstraction
	var List<TaskStageForUser> stages
	val TaskStageForUserRepository stageRepo

	new(TaskForUserOngoing ongoing, TaskStageForUserRepository stageRepo) {
		this.tfuc = null
		this.tfuo = tfuo
		this.stages = null
		this.stageRepo = stageRepo
	}

	new(TaskForUserCompleted tfuc, TaskStageForUserRepository stageRepo) {
		this.tfuc = tfuc
		this.tfuo = null
		this.stages = null
		this.stageRepo = stageRepo
	}

	def List<TaskStageForUser> getUserTaskStages() {
		if (this.stages == null) {
			loadUserTaskStages()
		}

		return this.stages
	}

	def void addStage(TaskStageForUser stage) {
		if (this.stages == null) {
			loadUserTaskStages()
		}

		this.stages.add(stage)
	}

	private def void loadUserTaskStages() {
		Preconditions.checkState(tfuo != null, "Completed tasks have no record of stages")
		
		// If tfuo has an identity, then it has previously been written out to the
		// repository and so there may be stage objects stored there as well.
		// Search the repo for a collection if the identity is neither null nor
		// blank.  Otherwise, allocate a new collection.
		if (tfuo.taskForUserId.nullOrEmpty) {
			this.stages = new ArrayList<TaskStageForUser>(3)
		} else {
			this.stages = stageRepo.findByTaskForUserId(tfuo.taskForUserId)
		}
	}
}
