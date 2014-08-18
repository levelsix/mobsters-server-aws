package com.lvl6.mobsters.domain.game.api.events

import com.lvl6.mobsters.utility.lambda.Director

public interface IGameEventFactory
{
	def AddTask publishAddUserTask(String userUuid, int taskId, Director<IAddUserTaskOptionsBuilder> director)
}

public interface IAddUserTaskOptionsBuilder {
	def IAddUserTaskOptionsBuilder addStage(
		String userTaskUuid, int stageNum, 
		int taskStageMonsterId, String monsterType, 
		int expGained, int cashGained, int oilGained, 
		int droppedItemId, boolean monsterPieceDropped)		
	
	def IAddUserTaskOptionsBuilder taskUuid(String taskUuid)
}
