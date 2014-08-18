package com.lvl6.mobsters.event;


public interface IEventProducer<L extends IEventListener<?>>
{
	public void registerListenerSync(L listener);

	public void registerListenerAsync(L listener);

	public void deregisterListener(L listener);
}
