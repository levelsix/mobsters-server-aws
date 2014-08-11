package com.lvl6.mobsters.domain.game

import com.google.common.base.Preconditions
import com.lvl6.mobsters.domain.gameserver.IGameServerInternal
import com.lvl6.mobsters.events.GameEvent

abstract class AbstractSemanticObject {
	private val ServerUserResource resourceServices
	protected val AbstractSemanticObject container
	
	protected new(ServerUserResource resourceServices)
	{
		Preconditions.checkNotNull(resourceServices)
		this.resourceServices = resourceServices
		this.container = this
	}
	
	protected new(AbstractSemanticObject parent) {
		Preconditions.checkNotNull(parent)
		this.resourceServices = parent.resourceServices
		this.container = parent
	}
	
	protected def void publish(GameEvent event)
	{
		resourceServices.publish(event)
	}
	
	protected def IGameServerInternal getGameServer() {
		return resourceServices.gameServer
	}
	
	
	protected def String getUserUuid() {
		return resourceServices.getUserUuid()
	}
}