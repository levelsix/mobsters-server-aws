package com.lvl6.mobsters.domain.game.model

import com.lvl6.mobsters.domain.game.api.IUserResource
import com.lvl6.mobsters.domain.game.model.IRepoRegistry
import com.lvl6.mobsters.domain.game.model.IUserResourceInternal
import com.lvl6.mobsters.dynamo.User

//@ExtractInterfaces(
//	api="com.lvl6.mobsters.domain.game.api.IUserResource",
//	internal="com.lvl6.mobsters.domain.game.internal.IUserResourceInternal"
//) 
class UserResourceImpl 
implements IUserResource, IUserResourceInternal 
{ 
	private var Player connectedPlayer
	private val IRepoRegistry repoRegistry
	private val String userUuid
		
	new(IRepoRegistry repoRegistry, String userUuid) {
		this.connectedPlayer = null
		this.repoRegistry = repoRegistry
		this.userUuid = userUuid
	}
	
	override Player connect() {
		if (connectedPlayer == null) {
			val User aUser = repoRegistry.userRepo.load(userUuid)
			connectedPlayer = new Player(this.repoRegistry, this, aUser)
			// publishUserConnected(this.userUuid, TimeUtils::createNow())
		} else {
			// TODO: Log connect when already connected
		}
		
		return connectedPlayer
	}
	
	override Player isConnected() 
	{
		return this.connectedPlayer
	}
		
	override void disconnect() {
		// TODO: Log disconnect if already disconnected
		this.connectedPlayer = null		
	}
	
	
	override String getUserUuid() {
		return userUuid
	}
}