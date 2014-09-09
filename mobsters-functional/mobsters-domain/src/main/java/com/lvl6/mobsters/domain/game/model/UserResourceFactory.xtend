package com.lvl6.mobsters.domain.game.model

import com.lvl6.mobsters.domain.config.ConfigExtensions
import com.lvl6.mobsters.domain.game.api.IUserResource
import com.lvl6.mobsters.domain.game.api.IUserResourceFactory
import com.lvl6.mobsters.dynamo.repository.AchievementForUserRepository
import com.lvl6.mobsters.dynamo.repository.EventPersistentForUserRepository
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
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.utility.indexing.by_object.ObjKeyIndex
import com.lvl6.mobsters.utility.probability.ProbabilityExtensionLib
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepositoryImpl

/**
 * A singleton facade class that serves several purposes that should probably get distributed to
 * a collection of singletons for a healthier separation of concerns:
 * 
 * <ol>
 *   <li>It plays the role of a ResourceSet by providing an entry point for the service layer to access
 *       a UserResource that can be opened/connected to gain access to the game state of a connected player</li>
 *   <li>It delegates interactions with observers and listeners to the EventBus components used to carve out
 *       a allow services to send structures responses back to the controller layer without either layer becoming
 *       dependent on or synchronous with the other.</li>
 *   <li>It acts as a registry for data layer artifacts that all semantic layer artifacts can access through a single
 *       stored reference, keeping data layer access from having excessive storage overhead.</li>
 * </ol>
 */
@Component("userResourceFactory")
class UserResourceFactory 
implements IUserResourceFactory, IRepoRegistry
{
	// TODO: Idle timeout purging and explicit close cleanup behaviors, some of which can already be
	//        found in Kelly's contributions.
	private val ObjKeyIndex<String, UserResourceImpl> resourceMap = 
		new ObjKeyIndex<String, UserResourceImpl>[it.getUserUuid()];
	
	@Autowired
	@Property
	UserRepository userRepo
	
	@Autowired
	@Property
	UserCredentialRepository userCredRepo
	
	@Autowired
	@Property
	UserDataRarelyAccessedRepositoryImpl udraRepo
	
//	@Autowired
//	@Property
//	LocationRepository locationRepo
	
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
	var IGameEventMediator gameEventMediator
	
	@Autowired
	@Property
	var DataServiceTxManager txManager;
	
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
	
	override beginTransaction() {
		txManager.beginTransaction()
	}
	
	override commitTransaction() {
		txManager.commit()
	}
	
	override rollbackTransaction() {
		txManager.rollback()
	}
	
}