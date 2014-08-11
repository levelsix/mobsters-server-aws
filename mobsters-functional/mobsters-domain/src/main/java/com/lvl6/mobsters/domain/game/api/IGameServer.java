package com.lvl6.mobsters.domain.game.api;

import com.lvl6.mobsters.domain.game.event.IEventListener;

public interface IGameServer {
	public void registerListenerSync(IEventListener listener);

	public void registerListenerAsync(IEventListener listener);

	public void deregisterListener(IEventListener listener);

	public IUserResource getUserResourceFor(String userUuid);
}