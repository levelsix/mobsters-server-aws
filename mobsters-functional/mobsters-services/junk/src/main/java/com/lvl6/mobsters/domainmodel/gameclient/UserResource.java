package com.lvl6.mobsters.domainmodel.gameclient;




public interface UserResource {
	public void registerListener(ClientEventListener listener);
	
	public void deregisterListener(ClientEventListener listener);
	
	public Player connect();
	
	public void disconnect();
	
	public Player isConnected();
	
	public String getUserUuid();
}
