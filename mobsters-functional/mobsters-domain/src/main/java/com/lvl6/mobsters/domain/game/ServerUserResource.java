package com.lvl6.mobsters.domain.game;

import com.lvl6.mobsters.domain.game.event.AbstractGameEvent;
import com.lvl6.mobsters.domain.game.event.EventListener;
import com.lvl6.mobsters.domain.gameserver.IPlayerInternal;


public interface ServerUserResource {
	public String getUserUuid();

	public IPlayerInternal connect();
	
	public IPlayerInternal isConnected();

	public void disconnect();
	
	public void registerSyncListener(EventListener listener);
	
	public void registerAsyncListener(EventListener listener);
	
	public void deregisterListener(EventListener listener);
	
	public void publish(AbstractGameEvent event);
	
	// TODO: This should really be a facade so a IRepoRegistry reference cannot survive close.
	// public IRepoRegistry getRepoRegistry();
}