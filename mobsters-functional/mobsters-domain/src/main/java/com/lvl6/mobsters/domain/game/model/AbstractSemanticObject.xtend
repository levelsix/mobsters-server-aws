package com.lvl6.mobsters.domain.game.model

import com.google.common.base.Preconditions
import com.lvl6.mobsters.domain.game.api.events.IGameEvent

abstract class AbstractSemanticObject {
	private val UserResourceImpl resourceService
	private val IRepoRegistry repoRegistryService
	private val AbstractSemanticObject container
	
	protected new(
		IRepoRegistry repoRegistryService,
		UserResourceImpl resourceService 
	)
	{
		Preconditions.checkNotNull(repoRegistryService)
		Preconditions.checkNotNull(resourceService)
		
		this.repoRegistryService = repoRegistryService
		this.resourceService = resourceService
		this.container = this
	}
	
	protected new(AbstractSemanticObject parent) {
		Preconditions.checkNotNull(parent)
		this.repoRegistryService = parent.repoRegistryService
		this.resourceService = parent.resourceService
		this.container = parent
	}
	
	protected def IRepoRegistry getRepoRegistry() {
		return repoRegistryService
	}

	protected def String getUserUuid() {
		return resourceService.getUserUuid()
	}
	
	protected def void publish(IGameEvent evt) {
		repoRegistryService.getGameEventMediator().publish(evt)
	}
}