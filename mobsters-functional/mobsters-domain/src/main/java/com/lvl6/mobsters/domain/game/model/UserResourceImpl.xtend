package com.lvl6.mobsters.domain.game.model

import com.lvl6.mobsters.domain.game.api.IUserResource
import com.lvl6.mobsters.domain.game.internal.IRepoRegistry
import com.lvl6.mobsters.domain.game.internal.IUserResourceInternal
import com.lvl6.mobsters.dynamo.User

//@ExtractInterfaces(
//	api="com.lvl6.mobsters.domain.game.api.IUserResource",
//	internal="com.lvl6.mobsters.domain.game.internal.IUserResourceInternal"
//) 
class UserResourceImpl 
implements IUserResource, IUserResourceInternal 
{ 
	private var PlayerImpl connectedPlayer
	private val IRepoRegistry repoRegistry
	private val String userUuid
		
	new(IRepoRegistry repoRegistry, String userUuid) {
		this.connectedPlayer = null
		this.repoRegistry = repoRegistry
		this.userUuid = userUuid
	}
	
	//@SplitReturn(
	//	api="com.lvl6.mobsters.domain.game.api.IPlayer",
	//	internal="com.lvl6.mobsters.domain.game.internal.IPlayerInternal"
	//)
	override PlayerImpl connect() {
		if (connectedPlayer == null) {
			val User aUser = repoRegistry.userRepo.load(userUuid)
			connectedPlayer = new PlayerImpl(this.repoRegistry, this, aUser)
			// publishUserConnected(this.userUuid, TimeUtils::createNow())
		} else {
			// TODO: Log connect when already connected
		}
		
		return connectedPlayer
	}
	
	//@SplitReturn(
	//	api="com.lvl6.mobsters.domain.game.api.IPlayer",
	//	internal="com.lvl6.mobsters.domain.game.internal.IPlayerInternal"
	//)
	override PlayerImpl isConnected() 
	{
		return connectedPlayer
	}
		
	// @ExposeTo( api=true, internal=true )
	override void disconnect() {
		// TODO: Log disconnect if already disconnected
		this.connectedPlayer = null		
	}
	
	
	// @ExposeTo( api=true, internal=true )
	override String getUserUuid() {
		return userUuid
	}
	
	// @EventFactory
	// private def void publishUserConnected(String userUuid, Date dateTime)
	// { }
	
	//@SplitReturn(
	//	api="com.lvl6.mobsters.domain.game.api.IGameServer",
	//	internal="com.lvl6.mobsters.domain.game.internal.IGameServerInternal"
	//)
	//override GameServerImpl getGameServer() {
	//	return this.gameServer;
	//}	
}