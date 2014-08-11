package com.lvl6.mobsters.domainmodel.gameimpl

import com.google.common.base.Preconditions
import com.lvl6.mobsters.domainmodel.gameclient.event.ClientGameEvent
import com.lvl6.mobsters.domainmodel.gameserver.event.ServerGameEvent

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
	
	protected def void publish(ClientGameEvent event)
	{
		resourceServices.publish(event)
	}
	
	protected def void publish(ServerGameEvent event)
	{
		resourceServices.publish(event)
	}
	
	protected def IRepoRegistry getRepoRegistry() {
		return resourceServices.getRepoRegistry()
	}
	
	
	protected def String getUserUuid() {
		return resourceServices.getUserUuid()
	}
}