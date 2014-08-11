package com.lvl6.mobsters.domain.game.internal;

public interface IUserResourceInternal {
	public String getUserUuid();

	public IPlayerInternal connect();
	
	public IPlayerInternal isConnected();

	public void disconnect();
}