package com.lvl6.mobsters.domain.game.event;


public interface IEventPublisher {
	public void publish(IGameEvent event);
}