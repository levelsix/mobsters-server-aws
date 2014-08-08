package com.lvl6.mobsters.domainmodel.gameimpl

import com.lvl6.mobsters.domainmodel.gameclient.PlayerTaskStage
import com.lvl6.mobsters.domainmodel.gameserver.ServerPlayerTaskStage
import com.lvl6.mobsters.dynamo.TaskStageForUser
import com.lvl6.mobsters.info.IQuestJob
import com.lvl6.mobsters.info.IQuestJobMonsterItem
import com.lvl6.mobsters.info.ITaskStage
import com.lvl6.mobsters.info.ITaskStageMonster
import com.lvl6.mobsters.semanticmodel.annotation.EventFactory
import com.lvl6.mobsters.semanticmodel.annotation.ListenerKind
import com.lvl6.mobsters.server.ControllerConstants
import java.util.List

class SemanticPlayerTaskStage 
	extends AbstractSemanticObject 
	implements PlayerTaskStage, ServerPlayerTaskStage {
	
	val ITaskStage stageMeta
	val TaskStageForUser playerTaskStage
	
	protected new(
		SemanticPlayerTask parent, 
		ITaskStage taskStageMeta,
		Iterable<IQuestJob> questJobs,
		String elementName,
		boolean mayGeneratePieces
	) {
		super(parent)
		this.stageMeta = taskStageMeta
		this.playerTaskStage = this.generateStage(questJobs, elementName, mayGeneratePieces)
	}
	
	protected new(
		SemanticPlayerTask parent, 
		TaskStageForUser playerTaskStage
	) {
		super(parent)
		
		// TODO: Provide a shorthand accessor for the query that follows.
		// var extension configExtensionLib = repoRegistry.configExtensionLib
		this.stageMeta = parent.taskMeta.taskStages.get(playerTaskStage.stageNum)

		this.playerTaskStage = playerTaskStage
	}
	
	private def TaskStageForUser generateStage(
		Iterable<IQuestJob> questJobs, String elementName, boolean mayGeneratePieces)
	{
		val extension configExtensionLib = repoRegistry.configExtensionLib
		val extension probabilityExtensionLib = repoRegistry.probabilityExtensionLib
		
		val List<ITaskStageMonster> taskStageMonsters = stageMeta.stageMonsters
			
		return new TaskStageForUser() => [
			it.stageNum = stageMeta.stageNum

			var ITaskStageMonster stageMonster = null
			if (elementName.nullOrEmpty) {
				//select one monster, at random. This is the ONE monster for this stage
				stageMonster = taskStageMonsters.
					selectWithoutReplacement(1)[it.chanceToAppear].get(0)
			} else {
				// select the element-matching monster.  This is the ONE monster for
				// this stage.
				stageMonster = taskStageMonsters.selectElementMonster(elementName)
			}
			it.taskStageMonsterId = stageMonster.id
			it.expGained = stageMonster.expReward
			it.cashGained =
				rollValueInRange(stageMonster.minCashDrop,
					stageMonster.maxCashDrop)
			it.oilGained =
				rollValueInRange(stageMonster.minOilDrop, stageMonster.maxOilDrop)

			var IQuestJobMonsterItem droppedItem = null
			if (stageMeta.task.id != ControllerConstants.
				MINI_TUTORIAL__GUARANTEED_MONSTER_DROP_TASK_ID) {
				droppedItem = stageMonster.monster.rollForDroppedItem(questJobs)
			}
			if (droppedItem != null) {
				it.itemIdDropped = droppedItem.item.id
				it.monsterPieceDropped = false
			} else {
				it.itemIdDropped = -1
				it.monsterPieceDropped = mayGeneratePieces
			}
			
			publishAddUserTaskStage(
				userUuid, stageMeta.stageNum, it.taskStageMonsterId, stageMonster.monsterType,
				it.expGained, it.cashGained, it.oilGained, it.itemIdDropped, it.monsterPieceDropped
			)
		]		
	}
	
	//
	// Game events
	//
		
	@EventFactory(targetListeners=ListenerKind.CLIENT)
	public def void publishAddUserTaskStage(
		String userTaskUuid, int stageNum, 
		int taskStageMonsterId, String monsterType, 
		int expGained, int cashGained, int oilGained, 
		int droppedItemId, boolean monsterPieceDropped ) 
	{
	}	
}