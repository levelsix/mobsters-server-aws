package com.lvl6.mobsters.event

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import javax.annotation.PostConstruct
import javax.annotation.Resource
import org.springframework.core.task.AsyncListenableTaskExecutor

// @EventFactory
public abstract class AbstractEventMediator<L extends IEventListener<E>, E extends IEvent>
	implements IEventMediator<L, E>
{
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
	override void registerListenerSync(L listener) {
		syncEventBus.register(listener)
		return
	}
	
	// @ExposeTo( api=true, internal=true )
	override void registerListenerAsync(L listener) {
		asyncEventBus.register(listener)		
		return
	}
	
	// @ExposeTo( api=true, internal=true )
	override void deregisterListener(L listener) {
		syncEventBus.unregister(listener)
		asyncEventBus.unregister(listener)
		
		return
	}
		
	override publish(E event) {
		asyncEventBus.post(event);
		syncEventBus.post(event);
		
		return
	}
}