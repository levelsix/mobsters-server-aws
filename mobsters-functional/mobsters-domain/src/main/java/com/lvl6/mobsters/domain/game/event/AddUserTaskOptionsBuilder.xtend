package com.lvl6.mobsters.domain.game.event

import com.google.common.collect.ImmutableList

class AddUserTaskOptionsBuilder implements ModelEventFactoryImpl.IAddUserTaskOptionsBuilder {
	val ImmutableList.Builder<AddUserTaskStage> stageBuilder =
		ImmutableList.<AddUserTaskStage>builder()
	var String taskUuid
		
	override addStage(
		String userTaskUuid, int stageNum, 
		int taskStageMonsterId, String monsterType, 
		int expGained, int cashGained, int oilGained, 
		int droppedItemId, boolean monsterPieceDropped
	) {
		stageBuilder.add(
			new AddUserTaskStage(
				null /* Temporary hack while this is changing a little more */,
				userTaskUuid, stageNum, taskStageMonsterId,
				monsterType, expGained, cashGained, oilGained,
				droppedItemId, monsterPieceDropped
			)
		)
		
		return this
	}
	
	override taskUuid(String taskUuid) {
		this.taskUuid = taskUuid
		
		return this
	}
	
	// Only a ROOT builder can take options on its build() method.  Nested builders will HAVE to take them in a 
	// constructor, so to generalize, perhaps root builders should accept them via constructor as well?  
	//
	// No...  Event roots will have other differences, so best to let this be just another one of them.
	def AddTask build(String userUuid, int taskId) {
		// val ArrayList<AddUserTaskStageBuilder>: 
		// For a nested collection with optional per-element values, stageBuilder will be a mutable list of concrete 
		// builders.  In that case, the ImmutableList is acquired by:   ImmutableList.copyOf(stageList.map[it.build()])
		
		return new AddTask(userUuid, taskId, taskUuid, stageBuilder.build());
	}
}