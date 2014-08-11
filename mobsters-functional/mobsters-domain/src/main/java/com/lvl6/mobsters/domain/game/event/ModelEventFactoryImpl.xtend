package com.lvl6.mobsters.domain.game.event

import com.lvl6.mobsters.utility.xtend.lib.EventFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@EventFactory
@Component
package class ModelEventFactoryImpl {
	@Property
	@Autowired
	var IEventPublisher eventPublisher
	
	def void publishAddUserTaskStage(
		String userTaskUuid, int stageNum, 
		int taskStageMonsterId, String monsterType, 
		int expGained, int cashGained, int oilGained, 
		int droppedItemId, boolean monsterPieceDropped)
	{ }
	
	def void publishBeginAddUserTask(String userUuid, int taskId)
	{ }
	
	def void publishFinishAddUserTask(String taskUuid, String userUuid, int taskId)
	{ }
}