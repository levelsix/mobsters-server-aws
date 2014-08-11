package com.lvl6.mobsters.domainmodel.gameimpl;

import com.lvl6.mobsters.domainmodel.gameclient.ClientEventListener;
import com.lvl6.mobsters.domainmodel.gameclient.event.ClientGameEvent;
import com.lvl6.mobsters.domainmodel.gameserver.ServerPlayer;
import com.lvl6.mobsters.domainmodel.gameserver.event.ServerGameEvent;


public interface ServerUserResource {
	public String getUserUuid();

	public ServerPlayer connect();
	
	public ServerPlayer isConnected();

	public void disconnect();
	
	public void registerListener(AbstractSemanticObject listener);
	
	public void deregisterListener(AbstractSemanticObject listener);

	public void registerListener(ClientEventListener listener);
	
	public void deregisterListener(ClientEventListener listener);
	
	public void publish(ClientGameEvent event);
	
	public void publish(ServerGameEvent event);
	
	// TODO: This should really be a facade so a IRepoRegistry reference cannot survive close.
	public IRepoRegistry getRepoRegistry();
}