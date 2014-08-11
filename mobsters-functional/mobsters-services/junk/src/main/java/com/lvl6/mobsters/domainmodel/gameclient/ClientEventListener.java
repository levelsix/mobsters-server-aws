package com.lvl6.mobsters.domainmodel.gameclient;

public interface ClientEventListener {
	public void beginConversation(String name);
	
	public void endConversation(String name);
}
