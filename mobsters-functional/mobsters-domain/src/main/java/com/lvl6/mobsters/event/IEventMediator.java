package com.lvl6.mobsters.event;


public interface IEventMediator <L extends IEventListener<E>, E extends IEvent>
	extends IEventProducer<L>, IEventPublisher<E> 
{

}
