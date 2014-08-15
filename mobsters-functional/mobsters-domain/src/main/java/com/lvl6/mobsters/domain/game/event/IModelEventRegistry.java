package com.lvl6.mobsters.domain.game.event;


public interface IModelEventRegistry {
	public void registerListenerSync(IEventListener listener);

	public void registerListenerAsync(IEventListener listener);

	public void deregisterListener(IEventListener listener);
}
