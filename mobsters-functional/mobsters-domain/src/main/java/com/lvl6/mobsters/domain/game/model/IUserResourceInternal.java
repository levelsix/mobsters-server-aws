package com.lvl6.mobsters.domain.game.model;

interface IUserResourceInternal {
	public String getUserUuid();

	public IPlayerInternal connect();
	
	public IPlayerInternal isConnected();

	public void disconnect();
}