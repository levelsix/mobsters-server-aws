package com.lvl6.mobsters.services.user

import com.lvl6.mobsters.dynamo.TaskForUserOngoing
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.repository.TaskForUserOngoingRepository
import com.lvl6.mobsters.info.Task
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode
import com.lvl6.mobsters.utility.indexing.by_int.ImmutableIntKey
import com.lvl6.mobsters.utility.indexing.by_int.IntKeyIndex
import java.util.ArrayList
import java.util.List
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.google.common.base.Preconditions.*
import static com.lvl6.mobsters.services.common.Lvl6MobstersConditions.*

import static extension java.lang.String.*

@Component
class UserExtensionLib {

	@Autowired
	package TaskForUserOngoingRepository ongoingTaskRepo;

	// TODO: Exposing a mutable List may not be ideal here, since it implies a necessity
	//        to add a Decorator if the SemanticUser needs to be informed about changes
	//        to the list's contents.  That would lead to a more usable API, but more
	//        implementation effort.  Punt the issue for now until we know more.
	//
	// TODO: Contemplate the value of generalizing Protbuf construction as well as History
	//        logging as generalized Listeners that don't require the Services to twiddle
	//        anything explicitly when considering the above note.
	public def List<TaskForUserOngoing> getOngoingTaskList(User u) {
		checkNotNull(u)
		val SemanticUser ctxt = u.derivedContext
		return ctxt.ongoingTasks
	}

	// TODO: Consider migrating the task population logic out of TaskServiceImpl and into
	//        a SemanticTask initializer or TaskExtensionLib factory method.
	public def boolean beginNewTask(User u, TaskForUserOngoing newTask) {
		checkNotNull(u)
		checkNotNull(newTask)
		val SemanticUser ctxt = u.derivedContext
		return ctxt.beginNewTask(newTask)
	}

	def boolean hasCompletedTask(User u, Task t) {
		val SemanticUser ctxt = u.derivedContext
		return ctxt.hasCompletedTask(t)
	}

	def User checkCanSpendGems(
		User u,
		int gemsToSpend,
		Logger log,
		()=>String spendPurposeLambda
	) {
		lvl6Precondition(
			u.canSpendGems(gemsToSpend),
			Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS,
			log,
			[|
				return String.format(
					"user has %d gems; trying to spend %d as part of cost to %s",
					u.gems, gemsToSpend, spendPurposeLambda.apply())]
		)

		return u
	}

	def User checkCanSpendGems(User u, int gemsToSpend, Logger log) {
		lvl6Precondition(
			u.canSpendGems(gemsToSpend),
			Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS,
			log,
			"user has %d gems; trying to spend %d",
			u.gems,
			gemsToSpend
		)

		return u
	}

	def User checkCanSpendCash(
		User u,
		int cashToSpend,
		Logger log,
		()=>String spendPurposeLambda
	) {
		lvl6Precondition(
			u.canSpendCash(cashToSpend),
			Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_CASH,
			log,
			[|
				return String.format(
					"user has %d cash; trying to spend %d as part of cost to %s",
					u.cash, cashToSpend, spendPurposeLambda.apply())]
		)

		return u
	}

	def User checkCanSpendCash(User u, int cashToSpend, Logger log) {
		lvl6Precondition(
			u.canSpendCash(cashToSpend),
			Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_CASH,
			log,
			"user has %d gems; trying to spend %d",
			u.cash,
			cashToSpend
		)

		return u
	}

	def User checkCanSpendOil(
		User u,
		int oilToSpend,
		Logger log,
		()=>String spendPurposeLambda
	) {
		lvl6Precondition(
			u.canSpendOil(oilToSpend),
			Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_OIL,
			log,
			[|
				return String.format(
					"user has %d oil; trying to spend %d as part of cost %s", u.oil,
					oilToSpend, spendPurposeLambda.apply())]
		)

		return u
	}

	def User checkCanSpendOil(User u, int oilToSpend, Logger log) {
		lvl6Precondition(
			u.canSpendOil(oilToSpend),
			Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_OIL,
			log,
			"user has %d oil; trying to spend %d",
			u.oil,
			oilToSpend
		)

		return u
	}

	def boolean canSpendGems(User u, int gemsToSpend) {
		checkArgument(gemsToSpend >= 0)

		return (u.gems >= gemsToSpend)
	}

	def boolean canSpendCash(User u, int cashToSpend) {
		checkArgument(cashToSpend >= 0)

		return (u.cash >= cashToSpend)
	}

	def boolean canSpendOil(User u, int oilToSpend) {
		checkArgument(oilToSpend >= 0)

		return (u.oil >= oilToSpend)
	}

	def User spendGems(User u, int gemsToSpend, Logger log) {
		u.checkCanSpendGems(gemsToSpend, log)
		u.gems = u.gems - gemsToSpend
		return u
	}

	def boolean spendGems(User u, int gemsToSpend) {
		var retVal = false
		if (u.canSpendGems(gemsToSpend)) {
			u.gems = u.gems - gemsToSpend
			retVal = true
		}

		return retVal
	}

	def User spendCash(User u, int cashToSpend, Logger log) {
		u.checkCanSpendCash(cashToSpend, log)
		u.cash = u.cash - cashToSpend
		return u
	}

	def boolean spendCash(User u, int cashToSpend) {
		var retVal = false
		if (u.canSpendCash(cashToSpend)) {
			u.cash = u.cash - cashToSpend
			retVal = true
		}

		return retVal
	}

	def User spendOil(User u, int oilToSpend, Logger log) {
		u.checkCanSpendOil(oilToSpend, log)
		u.oil = u.oil - oilToSpend
		return u
	}

	def boolean spendOil(User u, int oilToSpend) {
		var retVal = false
		if (u.canSpendOil(oilToSpend)) {
			u.oil = u.oil - oilToSpend
			retVal = true
		}

		return retVal
	}

	private def SemanticUser getDerivedContext(User u) {
		var SemanticUser ctxt = u.getAttachment(SemanticUser)
		if (ctxt == null) {
			ctxt = new SemanticUser(this, u, u.id.nullOrEmpty)
			u.putAttachment(SemanticUser, ctxt)
		}

		return ctxt
	}
}

package class SemanticUser {
	private static val Logger LOG = LoggerFactory.getLogger(SemanticUser)

	private val UserExtensionLib parent
	private val User aUser

	// A "new" object here refers to a user that has been allocated, but not yet saved once, hence no UUID is present.
	private val boolean newObject

	// If previously loaded, these values will store a non-null value.  There is a notable difference
	// between finding a null here (no fetch attempted) and finding an empty collection (load attempted,
	// and no records found).
	private var List<TaskForUserOngoing> ongoingTasks
	private var IntKeyIndex<Task> completedIndex

	new(UserExtensionLib parent, User aUser, boolean newObject) {
		this.parent = parent
		this.aUser = aUser
		this.newObject = newObject
		this.ongoingTasks = null
		this.completedIndex = null
	}

	def List<TaskForUserOngoing> getOngoingTasks() {
		if (ongoingTasks == null) {
			loadOngoingTasks()
		}

		return ongoingTasks.clone()
	}

	def boolean beginNewTask(TaskForUserOngoing newTask) {
		if (ongoingTasks == null) {
			loadOngoingTasks()
		}

		return ongoingTasks.add(newTask)
	}

	def boolean hasCompletedTask(Task aTask) {
		if (completedIndex == null) {
			loadCompletedTasks()
		}

		val ImmutableIntKey taskKey = new ImmutableIntKey(aTask.getId())
		return completedIndex.get(taskKey) != null
	}

	private def loadOngoingTasks() {
		if (newObject) {
			ongoingTasks = new ArrayList<TaskForUserOngoing>(3)
		} else {
			ongoingTasks = parent.ongoingTaskRepo.findByUserId(aUser.id)
			if (ongoingTasks.nullOrEmpty) {
			} else if (ongoingTasks.size > 1) {
				LOG.warn(
					String.format(
						"Users should have at most one ongoing task, but user with id=%s has %d.  Tasks=%s",
						aUser.id, ongoingTasks.size(),
						ongoingTasks.map["%s(%d)".format(it.taskForUserId, it.taskId)]))
			}
		}
		
		return
	}

	private def void loadCompletedTasks() {
		if (newObject) {
			ongoingTasks = new ArrayList<TaskForUserOngoing>(3)
		} else {
			ongoingTasks = parent.ongoingTaskRepo.findByUserId(aUser.id)
			if (ongoingTasks.nullOrEmpty) {
			} else if (ongoingTasks.size > 1) {
				LOG.warn(
					String.format(
						"Users should have at most one ongoing task, but user with id=%s has %d.  Tasks=%s",
						aUser.id, ongoingTasks.size(),
						ongoingTasks.map["%s(%d)".format(it.taskForUserId, it.taskId)])
				)
			}
		}
		
		return
	}
}
