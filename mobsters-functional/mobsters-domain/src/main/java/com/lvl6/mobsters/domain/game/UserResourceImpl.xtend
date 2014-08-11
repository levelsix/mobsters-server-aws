package com.lvl6.mobsters.domain.game

import com.google.common.base.Preconditions
import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import com.lvl6.mobsters.domain.game.event.EventListener
import com.lvl6.mobsters.domain.gameserver.IGameServerInternal
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.events.GameEvent
import com.lvl6.mobsters.semanticmodel.annotation.EventFactory
import com.lvl6.mobsters.semanticmodel.annotation.ExtractInterfaces
import java.util.Date
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@ExtractInterfaces(
	api="com.lvl6.mobsters.domain.game.api.IUserResource",
	internal="com.lvl6.mobsters.domain.game.internal.IUserResourceInternal"
) 
//	interfaceName="com.lvl6.mobsters.semanticmodel.framework.IEventFactory", methodMarker=typeof(EventFactory)
//)
class UserResourceImpl { 
	private val EventBus syncEventBus
	private val EventBus asyncEventBus
	private val Executor asyncThreadPool = Executors.newCachedThreadPool
	private val IGameServerInternal repoRegistry
	private val String userUuid
	
	private var SemanticPlayer connectedPlayer
	
	new(IGameServerInternal gameServer, String userUuid) {
		syncEventBus = new EventBus()
		asyncEventBus = new AsyncEventBus(asyncThreadPool)
		this.repoRegistry = gameServer
		this.connectedPlayer = null
		this.userUuid = userUuid
	}
	
	override SemanticPlayer connect() {
		if (connectedPlayer == null) {
			val User aUser = repoRegistry.userRepo.load(userUuid)
			connectedPlayer = new SemanticPlayer(this, aUser)
			publishUserConnected(this.userUuid, TimeUtils::createNow())
		} else {
			// TODO: Log connect when already connected
		}
		
		return connectedPlayer
	}
	
	override SemanticPlayer isConnected() 
	{
		return connectedPlayer
	}
		
	override void disconnect() {
		// TODO: Log disconnect if already disconnected
		this.connectedPlayer = null		
	}
	
	override String getUserUuid() {
		return userUuid
	}
	
	@EventFactory
	private def void publishUserConnected(String userUuid, Date dateTime)
	{ }
	
	override void registerListeneSync(EventListener listener) {
		Preconditions.checkState(connectedPlayer != null)

		syncEventBus.register(listener)
		
		return
	}
	
	override void registerListeneAsync(EventListener listener) {
		Preconditions.checkState(connectedPlayer != null)

		asyncEventBus.register(listener)
		
		return
	}
	
	override void deregisterListener(EventListener listener) {
		Preconditions.checkState(connectedPlayer != null)

		syncEventBus.unregister(listener)
		asyncEventBus.unregister(listener)
		
		return
	}
		
	override void publish(GameEvent event) {
		Preconditions.checkState(connectedPlayer != null)

		asyncEventBus.post(event);
		syncEventBus.post(event);
		
		return
	}
	
	override IGameServerInternal getGameServer() {
		return this.repoRegistry;
	}	
}