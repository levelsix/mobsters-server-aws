package com.lvl6.mobsters.domain.game.model

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import com.lvl6.mobsters.domain.game.api.IGameServer
import com.lvl6.mobsters.domain.game.api.IUserResource
import com.lvl6.mobsters.domain.game.event.IEventListener
import com.lvl6.mobsters.domain.game.event.IEventPublisher
import com.lvl6.mobsters.domain.game.event.IGameEvent
import com.lvl6.mobsters.domain.game.internal.IRepoRegistry
import com.lvl6.mobsters.dynamo.repository.AchievementForUserRepository
import com.lvl6.mobsters.dynamo.repository.EventPersistentForUserRepository
import com.lvl6.mobsters.dynamo.repository.LocationRepository
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository
import com.lvl6.mobsters.dynamo.repository.MonsterEnhancingForUserRepository
import com.lvl6.mobsters.dynamo.repository.MonsterEvolvingForUserRepository
import com.lvl6.mobsters.dynamo.repository.MonsterForUserRepository
import com.lvl6.mobsters.dynamo.repository.MonsterHealingForUserRepository
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository
import com.lvl6.mobsters.dynamo.repository.QuestJobForUserRepository
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepository
import com.lvl6.mobsters.dynamo.repository.TaskForUserOngoingRepository
import com.lvl6.mobsters.dynamo.repository.TaskStageForUserRepository
import com.lvl6.mobsters.dynamo.repository.UserCredentialRepository
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.info.xtension.ConfigExtensions
import com.lvl6.mobsters.utility.indexing.by_object.ObjKeyIndex
import com.lvl6.mobsters.utility.probability.ProbabilityExtensionLib
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
//@ExtractInterfaces(
//	api="com.lvl6.mobsters.domain.game.api.IGameServer",
//	internal="com.lvl6.mobsters.domain.game.internal.IGameServerInternal"
//)
class GameServerImpl 
implements IGameServer, IEventPublisher, IRepoRegistry
{
	// TODO: Idle timeout purging and explicit close cleanup behaviors, some of which can already be
	//        found in Kelly's contributions.
	private val ObjKeyIndex<String, UserResourceImpl> resourceMap = 
		new ObjKeyIndex<String, UserResourceImpl>[it.getUserUuid()];
	
	private val EventBus syncEventBus
	private val EventBus asyncEventBus
	private val ExecutorService asyncThreadPool = Executors.newCachedThreadPool
	
	@Autowired
	@Property
	UserRepository userRepo
	
	@Autowired
	@Property
	UserCredentialRepository userCredRepo
	
	@Autowired
	@Property
	UserDataRarelyAccessedRepository udraRepo
	
	@Autowired
	@Property
	LocationRepository locationRepo
	
	@Autowired
	@Property
	EventPersistentForUserRepository eventRepo
	
	@Autowired
	@Property
	TaskForUserOngoingRepository tfuOngoingRepo
	
	@Autowired
	@Property
	TaskStageForUserRepository taskStageForUserRepo
	
	@Autowired
	@Property
	TaskForUserCompletedRepository tfuCompleteRepo
	
	@Autowired
	@Property
	QuestForUserRepository qfuRepo
	
	@Autowired
	@Property
	QuestJobForUserRepository qjfuRepo
	
	@Autowired
	@Property
	StructureForUserRepository sfuRepo
	
	@Autowired
	@Property
	ObstacleForUserRepository ofuRepo
	
	@Autowired
	@Property
	MiniJobForUserRepository miniJobRepo
	
	@Autowired
	@Property
	MonsterForUserRepository mfuRepo
	
	@Autowired
	@Property
	MonsterEnhancingForUserRepository monstEnhanceRepo
	
	@Autowired
	@Property
	MonsterEvolvingForUserRepository monstEvolvingRepo
	
	@Autowired
	@Property
	MonsterHealingForUserRepository monstHealingRepo
	
	@Autowired
	@Property
	AchievementForUserRepository achievementRepo
	
	@Autowired
	@Property
	var ConfigExtensions configExtensionLib
	
	@Autowired
	@Property
	var ProbabilityExtensionLib probabilityExtensionLib
	
//	@Autowired
//	@Property
//	var TaskExtensionLib taskExtensionLib
	
	@Autowired
	@Property
	var IEventPublisher eventPublisher
	
	new() {
		syncEventBus = new EventBus()
		asyncEventBus = new AsyncEventBus(asyncThreadPool)
	}
		
	// @ExposeTo( api=true, internal=true )
	override void registerListenerSync(IEventListener listener) {
		syncEventBus.register(listener)
		return
	}
	
	// @ExposeTo( api=true, internal=true )
	override void registerListenerAsync(IEventListener listener) {
		asyncEventBus.register(listener)		
		return
	}
	
	// @ExposeTo( api=true, internal=true )
	override void deregisterListener(IEventListener listener) {
		syncEventBus.unregister(listener)
		asyncEventBus.unregister(listener)
		
		return
	}
		
	// @ExposeTo( api=false, internal=true )
	override void publish(IGameEvent event) {
		asyncEventBus.post(event);
		syncEventBus.post(event);
		
		return
	}
	
	//
	// Provide all semantic objects and their canvas access to the repository objects
	//
	
	// A single lookup method shared by both client and server accessors b/c they each return 
	// one of the concrete class's two different interfaces, one for client, one for server.
//	@SplitReturn(
//		api="com.lvl6.mobsters.domain.game.api.IUserResource",
//		internal="com.lvl6.mobsters.domain.game.internal.IUserResourceInternal")
	override IUserResource getUserResourceFor(String userUuid) {
		var UserResourceImpl resource = resourceMap.get(userUuid)
		if (resource == null) {
			resource = new UserResourceImpl(this, userUuid)
			resourceMap.put(resource)
		}
		
		return resource
	}
	
}