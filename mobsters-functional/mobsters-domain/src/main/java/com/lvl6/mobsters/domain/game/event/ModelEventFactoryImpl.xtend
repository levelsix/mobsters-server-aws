package com.lvl6.mobsters.domain.game.event

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import com.lvl6.mobsters.utility.lambda.Director
import com.lvl6.mobsters.utility.xtend.lib.EventFactory
import javax.annotation.PostConstruct
import javax.annotation.Resource
import org.springframework.core.task.AsyncListenableTaskExecutor
import org.springframework.stereotype.Service

@EventFactory
@Service
package class ModelEventFactoryImpl
implements IModelEventRegistry {
	private val EventBus syncEventBus = new EventBus()

	@Resource(name="asyncEventPool")
	private var AsyncListenableTaskExecutor asyncThreadPool = null
	private var AsyncEventBus asyncEventBus = null
	
	@PostConstruct
	def void initAsyncBus()
	{
		asyncEventBus = new AsyncEventBus(asyncThreadPool)
	}
		
	// @ExposeTo( api=true, internal=true )
	override void registerListenerSync(IEventListener listener) {
		syncEventBus.register(listener)
		return
	}
	
	// @ExposeTo( api=true, internal=true )
	override void registerListenerAsync(IEventListener listener) {
		asyncEventBus.register(listener)		
		return
	}
	
	// @ExposeTo( api=true, internal=true )
	override void deregisterListener(IEventListener listener) {
		syncEventBus.unregister(listener)
		asyncEventBus.unregister(listener)
		
		return
	}
		
	// @ExposeTo( api=false, internal=true )
	private def void doPublish(IGameEvent event) {
		asyncEventBus.post(event);
		syncEventBus.post(event);
		
		return
	}
	
	def void publishAddUserTaskStage(
		String userUuid /* Temporary placeholder while this is changing a little more */,
		String userTaskUuid, int stageNum, 
		int taskStageMonsterId, String monsterType, 
		int expGained, int cashGained, int oilGained, 
		int droppedItemId, boolean monsterPieceDropped)
	{ }
	
	def void publishBeginAddUserTask(String userUuid, int taskId)
	{ }
	
	def void publishFinishAddUserTask(String taskUuid, String userUuid, int taskId)
	{ }

	interface IAddUserTaskOptionsBuilder {
		def ModelEventFactoryImpl.IAddUserTaskOptionsBuilder addStage(
			String userTaskUuid, int stageNum, 
			int taskStageMonsterId, String monsterType, 
			int expGained, int cashGained, int oilGained, 
			int droppedItemId, boolean monsterPieceDropped)		
		
		def ModelEventFactoryImpl.IAddUserTaskOptionsBuilder taskUuid(String taskUuid)
	}
	
	def void testAddUserTask(String userUuid, int taskId, Director<ModelEventFactoryImpl.IAddUserTaskOptionsBuilder> director)
	{
		val AddUserTaskOptionsBuilder bldr = new AddUserTaskOptionsBuilder
		director.apply(bldr)
		
		doPublish(bldr.build(userUuid, taskId));
	}
}