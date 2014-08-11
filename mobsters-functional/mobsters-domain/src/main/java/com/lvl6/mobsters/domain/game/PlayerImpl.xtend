package com.lvl6.mobsters.domain.game

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList
import com.lvl6.mobsters.domain.game.api.IPlayer
import com.lvl6.mobsters.domain.gameserver.IPlayerInternal
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.exception.Lvl6MobstersStatusCode
import com.lvl6.mobsters.info.IQuestJob
import com.lvl6.mobsters.info.ITask
import com.lvl6.mobsters.utility.indexing.by_int.IntKeyIndex
import java.util.List
import java.util.concurrent.Callable
import org.slf4j.Logger

import static com.lvl6.mobsters.exception.Lvl6MobstersConditions.*

class SemanticPlayer 
	extends AbstractSemanticObject 
	implements IPlayer, IPlayerInternal
{
	// @SemanticPassthrough(idProperty="id", clientInterface=Player, serverInterface=ServerPlayer)
	var User user

	var IntKeyIndex<SemanticPlayerTask> completedPlayerTasks

	var List<SemanticPlayerTask> ongoingPlayerTasks

	new(ServerUserResource resourceContainer, User user) {
		super(resourceContainer)
		this.user = user;
		this.completedPlayerTasks = null
		this.ongoingPlayerTasks = null
	}
	
	override List<SemanticPlayerTask> getOngoingPlayerTasks() {
		if (this.ongoingPlayerTasks == null ) {
			loadOngoingTasks();
		}
		
		return ImmutableList.copyOf(this.ongoingPlayerTasks)
	}
	
	override SemanticPlayerTask getOngoingPlayerTask(ITask taskMeta) {
		if (this.ongoingPlayerTasks == null ) {
			loadOngoingTasks();
		}
		
		return this.ongoingPlayerTasks.findFirst[return it.taskMeta === taskMeta]		
	}
	
	override SemanticPlayerTask beginTask(
		ITask taskMeta, Iterable<IQuestJob> questJobs, String elementName, boolean mayGeneratePieces
	) {
		Preconditions.checkNotNull(taskMeta)

		if (ongoingPlayerTasks == null) {
			loadOngoingTasks()
		}
		// Require the caller explicitly cancel any previous task before proceeding.
		Preconditions.checkState(
			ongoingPlayerTasks.empty,
			"User %s cannot begin task %s.  Another task is already in progress.", 
			getUuid(), taskMeta)

		// Make this the caller's responsibility
		// ongoingPlayerTasks.forEach[it.cancelOngoingTask()]
		ongoingPlayerTasks.clear
		
		var SemanticPlayerTask newSemanticTask = 
			new SemanticPlayerTask(this, taskMeta, questJobs, elementName, mayGeneratePieces)
		ongoingPlayerTasks.add(newSemanticTask)
		
		return newSemanticTask
	}

	override hasCompleted(ITask aTask) {
		if (completedPlayerTasks == null) {
			loadCompletedTasks()
		}
		
		return completedPlayerTasks.get(aTask.id) != null
	}

	override SemanticPlayer checkCanSpendGems(
		int gemsToSpend, Logger log, Callable<String> spendPurposeLambda) 
	{
		lvl6Precondition(
			this.canSpendGems(gemsToSpend),
			Lvl6MobstersStatusCode::FAIL_INSUFFICIENT_GEMS,
			log,
			[|
				return String.format(
					"user has %d gems; trying to spend %d as part of cost to %s",
					user.gems, gemsToSpend, spendPurposeLambda.call)]
		)

		return this
	}

	override SemanticPlayer checkCanSpendGems(int gemsToSpend, Logger log) {
		lvl6Precondition(
			this.canSpendGems(gemsToSpend),
			Lvl6MobstersStatusCode::FAIL_INSUFFICIENT_GEMS,
			log,
			"user has %d gems; trying to spend %d",
			user.gems,
			gemsToSpend
		)

		return this
	}

	override SemanticPlayer checkCanSpendCash(
		int cashToSpend,
		Logger log,
		Callable<String> spendPurposeLambda
	) {
		lvl6Precondition(
			this.canSpendCash(cashToSpend),
			Lvl6MobstersStatusCode::FAIL_INSUFFICIENT_CASH,
			log,
			[|
				return String.format(
					"user has %d cash; trying to spend %d as part of cost to %s",
					user.cash, cashToSpend, spendPurposeLambda.call())]
		)

		return this
	}

	override SemanticPlayer checkCanSpendCash(int cashToSpend, Logger log) {
		lvl6Precondition(
			this.canSpendCash(cashToSpend),
			Lvl6MobstersStatusCode::FAIL_INSUFFICIENT_CASH,
			log,
			"user has %d gems; trying to spend %d",
			user.cash,
			cashToSpend
		)

		return this
	}

	override SemanticPlayer checkCanSpendOil(
		int oilToSpend,
		Logger log,
		Callable<String> spendPurposeLambda
	) {
		lvl6Precondition(
			this.canSpendOil(oilToSpend),
			Lvl6MobstersStatusCode::FAIL_INSUFFICIENT_OIL,
			log,
			[|
				return String.format(
					"user has %d oil; trying to spend %d as part of cost %s", user.oil,
					oilToSpend, spendPurposeLambda.call())]
		)

		return this
	}

	override SemanticPlayer checkCanSpendOil(int oilToSpend, Logger log) {
		lvl6Precondition(
			this.canSpendOil(oilToSpend),
			Lvl6MobstersStatusCode::FAIL_INSUFFICIENT_OIL,
			log,
			"user has %d oil; trying to spend %d",
			user.oil,
			oilToSpend
		)

		return this
	}

	override boolean canSpendGems(int gemsToSpend) {
		Preconditions.checkArgument(gemsToSpend >= 0)

		return (user.gems >= gemsToSpend)
	}

	override boolean canSpendCash(int cashToSpend) {
		Preconditions.checkArgument(cashToSpend >= 0)

		return (user.cash >= cashToSpend)
	}

	override boolean canSpendOil(int oilToSpend) {
		Preconditions.checkArgument(oilToSpend >= 0)

		return (user.oil >= oilToSpend)
	}

	override SemanticPlayer spendGems(int gemsToSpend, Logger log) {
		this.checkCanSpendGems(gemsToSpend, log)
		user.gems = user.gems - gemsToSpend
		return this
	}

	override spendGems(int gemsToSpend) {
		var retVal = false
		if (this.canSpendGems(gemsToSpend)) {
			user.gems = user.gems - gemsToSpend
			retVal = true
		}

		return retVal
	}

	override SemanticPlayer spendCash(int cashToSpend, Logger log) {
		this.checkCanSpendCash(cashToSpend, log)
		user.cash = user.cash - cashToSpend
		return this
	}

	override spendCash(int cashToSpend) {
		var retVal = false
		if (this.canSpendCash(cashToSpend)) {
			user.cash = user.cash - cashToSpend
			retVal = true
		}

		return retVal
	}

	override SemanticPlayer spendOil(int oilToSpend, Logger log) {
		this.checkCanSpendOil(oilToSpend, log)
		user.oil = user.oil - oilToSpend
		return this
	}

	override spendOil(int oilToSpend) {
		var retVal = false
		if (this.canSpendOil(oilToSpend)) {
			user.oil = user.oil - oilToSpend
			retVal = true
		}

		return retVal

	}
	
	private def void loadOngoingTasks() {
		this.ongoingPlayerTasks = this.repoRegistry.tfuOngoingRepo
			.findByUserId(getUserUuid())
			.map[return new SemanticPlayerTask(this, it, null)]
			.toList

		return
	}

	private def void loadCompletedTasks() {
		this.completedPlayerTasks = new IntKeyIndex<SemanticPlayerTask> [return it.taskMeta.id]
		this.tfuCompleteRepo
			.findByUserId(getUserUuid())
			.forEach[
				this.completedPlayerTasks.put(
					new SemanticPlayerTask(this, null, it))]            

		return
	}

	//
	// Semantic Pass Through
	//
	
	override getUuid()
	{
		return user.id
	}
	
	override getGems()
	{
		return user.gems
	}
}
