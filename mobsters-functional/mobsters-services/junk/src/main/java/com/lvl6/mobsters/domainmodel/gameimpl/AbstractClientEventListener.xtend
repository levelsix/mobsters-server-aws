package com.lvl6.mobsters.domainmodel.gameimpl

import com.google.common.base.Preconditions
import com.lvl6.mobsters.domainmodel.gameclient.ClientEventListener
import com.lvl6.mobsters.domainmodel.gameclient.UserResource
import org.slf4j.Logger

public abstract class AbstractClientEventListener implements ClientEventListener {
	// private static final Logger LOG = LoggerFactory.getLogger(AbstractClientEventListener);
	
	private val Logger concreteLogger
	private var UserResource eventManager
	
	protected new(Logger concreteLogger)
	{ 
		Preconditions.checkNotNull(concreteLogger)
		
		this.concreteLogger = concreteLogger
		this.eventManager = null
	}
	
	final override beginConversation(String name) {
    	concreteLogger.info("Game server client listener is being notified about beginning of conversation named %s", name)
		handleBeginConversation(name)
    	concreteLogger.debug("Game server client listener has been notified about beginning of conversation named %s", name)
	}
	
	protected abstract def void handleBeginConversation(String name)
    
    
    final override endConversation(String name) {
    	concreteLogger.debug("Game server client listener is about to be notified about end of conversation named %s", name)
    	handleEndConversation(name)
    	concreteLogger.info("Game server client listener has been notified about end of conversation named %s", name)
    }
    
    protected abstract def void handleEndConversation(String name)
	
	
	package final def void connect(UserResource eventManager)
	{
		this.eventManager = eventManager;
		concreteLogger.info("Game server client listener is connected")
	}
	
	protected final def void disconnect() 
	{
		if (eventManager != null) {
		    eventManager.deregisterListener(this);
		    eventManager = null
		    concreteLogger.info("Game server client listener has disconnected")
		} else {
			concreteLogger.warn("Game server client listener is already disconnected")
		}
	}
}