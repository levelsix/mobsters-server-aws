package com.lvl6.mobsters.event;

public interface IEventPublisher<T extends IEvent> {
	public void publish(T event);
}