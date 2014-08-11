package com.lvl6.mobsters.domainmodel.gameimpl

import com.google.common.base.Preconditions
import com.google.common.eventbus.EventBus
import com.lvl6.mobsters.domainmodel.gameclient.ClientEventListener
import com.lvl6.mobsters.domainmodel.gameclient.UserResource
import com.lvl6.mobsters.domainmodel.gameclient.event.ClientGameEvent
import com.lvl6.mobsters.domainmodel.gameserver.event.ServerGameEvent
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.semanticmodel.annotation.EventFactory
import com.lvl6.mobsters.semanticmodel.annotation.ListenerKind
import com.lvl6.mobsters.services.common.TimeUtils
import java.util.Date

//@ExtractInterface( 
//	interfaceName="com.lvl6.mobsters.semanticmodel.framework.IEventFactory", methodMarker=typeof(EventFactory)
//)
class UserResourceImpl implements UserResource, ServerUserResource {
	private val EventBus clientEventBus
	private val EventBus serverEventBus
	private val IRepoRegistry repoRegistry
	private val String userUuid
	
	private var SemanticPlayer connectedPlayer
	
	new(IRepoRegistry gameServer, String userUuid) {
		clientEventBus = new EventBus()
		serverEventBus = new EventBus()
		this.repoRegistry = gameServer
		this.connectedPlayer = null
		this.userUuid = userUuid
	}
	
	override SemanticPlayer connect() {
		if (connectedPlayer == null) {
			val User aUser = repoRegistry.userRepo.load(userUuid)
			connectedPlayer = new SemanticPlayer(this, aUser)
			publishUserConnected(this.userUuid, TimeUtils.createNow())
		} else {
			// TODO: Log connect when already connected
		}
		
		return connectedPlayer
	}
	
	override SemanticPlayer isConnected() 
	{
		return connectedPlayer
	}
		
	override disconnect() {
		// TODO: Log disconnect if already disconnected
		this.connectedPlayer = null		
	}
	
	override getUserUuid() {
		return userUuid
	}
	
	@EventFactory( targetListeners=ListenerKind.SERVER)
	def void publishUserConnected(String userUuid, Date dateTime)
	{ }
	
	public override registerListener(ClientEventListener listener) {
		Preconditions.checkState(connectedPlayer != null)

		clientEventBus.register(listener)
	}
	
	public override deregisterListener(ClientEventListener listener) {
		Preconditions.checkState(connectedPlayer != null)

		clientEventBus.unregister(listener)
	}
		
	public override publish(ClientGameEvent event) {
		Preconditions.checkState(connectedPlayer != null)

		clientEventBus.post(event);
	}
	
	public override registerListener(AbstractSemanticObject listener) {
		Preconditions.checkState(connectedPlayer != null)

		serverEventBus.register(listener)
	}
	
	public override deregisterListener(AbstractSemanticObject listener) {
		Preconditions.checkState(connectedPlayer != null)

		serverEventBus.unregister(listener)
	}
	
	public override publish(ServerGameEvent event) {
		Preconditions.checkState(connectedPlayer != null)

		serverEventBus.post(event);
	}
	
	override getRepoRegistry() {
		Preconditions.checkState(connectedPlayer != null)

		return repoRegistry;
	}	
}