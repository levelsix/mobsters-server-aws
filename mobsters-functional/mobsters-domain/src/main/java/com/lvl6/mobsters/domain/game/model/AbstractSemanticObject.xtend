package com.lvl6.mobsters.domain.game.model

import com.google.common.base.Preconditions
import com.lvl6.mobsters.domain.game.internal.IRepoRegistry
import com.lvl6.mobsters.domain.game.internal.IUserResourceInternal
import com.lvl6.mobsters.events.GameEvent

abstract class AbstractSemanticObject {
	private val IUserResourceInternal resourceService
	private val IRepoRegistry repoRegistryService
	private val AbstractSemanticObject container
	
	protected new(
		IRepoRegistry repoRegistryService,
		IUserResourceInternal resourceService 
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
}