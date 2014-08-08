package com.lvl6.mobsters.domainmodel.gameimpl

import com.lvl6.mobsters.domainmodel.gameclient.GameServer
import com.lvl6.mobsters.domainmodel.gameserver.ServerUserResourceFactory
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
import com.lvl6.mobsters.services.task.TaskExtensionLib
import com.lvl6.mobsters.utility.indexing.by_object.ObjKeyIndex
import com.lvl6.mobsters.utility.probability.ProbabilityExtensionLib
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserResourceFactoryImpl 
	implements GameServer, ServerUserResourceFactory, IRepoRegistry
{
	// TODO: Idle timeout purging and explicit close cleanup modes, some of which can already be
	//        found in Kelly's contributions.
	ObjKeyIndex<String, UserResourceImpl> resourceMap = 
		new ObjKeyIndex<String, UserResourceImpl>([it|return it.getUserUuid()]);

	@Autowired
	UserRepository userRepo
	
	@Autowired
	UserCredentialRepository userCredRepo
	
	@Autowired
	UserDataRarelyAccessedRepository udraRepo
	
	@Autowired
	LocationRepository locationRepo
	
	@Autowired
	EventPersistentForUserRepository eventRepo
	
	@Autowired
	TaskForUserOngoingRepository tfuOngoingRepo
	
	@Autowired
	TaskStageForUserRepository taskStageForUserRepo
	
	@Autowired
	TaskForUserCompletedRepository tfuCompleteRepo
	
	@Autowired
	QuestForUserRepository qfuRepo
	
	@Autowired
	QuestJobForUserRepository qjfuRepo
	
	@Autowired
	StructureForUserRepository sfuRepo
	
	@Autowired
	ObstacleForUserRepository ofuRepo
	
	@Autowired
	MiniJobForUserRepository miniJobRepo
	
	@Autowired
	MonsterForUserRepository mfuRepo
	
	@Autowired
	MonsterEnhancingForUserRepository monstEnhanceRepo
	
	@Autowired
	MonsterEvolvingForUserRepository monstEvolvingRepo
	
	@Autowired
	MonsterHealingForUserRepository monstHealingRepo
	
	@Autowired
	AchievementForUserRepository achievementRepo
	
	@Autowired
	var ConfigExtensions configExtensionLib
	
	@Autowired
	var ProbabilityExtensionLib probabilityExtensionLib
	
	@Autowired
	var TaskExtensionLib taskExtensionLib
	
	//
	// Provide all semantic objects and their canvas access to the repository objects
	//
	
	override UserRepository getUserRepo( )
	{
		return userRepo
	}
	
	override UserCredentialRepository getUserCredRepo( )
	{
		return userCredRepo
	}
	
	override UserDataRarelyAccessedRepository getUdraRepo( )
	{
		return udraRepo
	}
	
	override LocationRepository getLocationRepo( )
	{
		return locationRepo
	}

	override EventPersistentForUserRepository getEventRepo( )
	{
		return eventRepo
	}
	
	override TaskForUserOngoingRepository getTfuOngoingRepo( )
	{
		return tfuOngoingRepo
	}

	override TaskStageForUserRepository getTaskStageForUserRepo( )
	{
		return taskStageForUserRepo
	}
	
	override TaskForUserCompletedRepository getTfuCompleteRepo( )
	{
		return tfuCompleteRepo
	}
	
	override QuestForUserRepository getQfuRepo( )
	{
		return qfuRepo
	}
	
	override QuestJobForUserRepository getQjfuRepo( )
	{
		return qjfuRepo
	}

	override StructureForUserRepository getSfuRepo( )
	{
		return sfuRepo
	}
	
	override ObstacleForUserRepository getOfuRepo( )
	{
		return ofuRepo
	}
	
	override MiniJobForUserRepository getMiniJobRepo( )
	{
		return miniJobRepo
	}
	
	override MonsterForUserRepository getMfuRepo( )
	{
		return mfuRepo
	}
	
	override MonsterEnhancingForUserRepository getMonstEnhanceRepo( )
	{
		return monstEnhanceRepo
	}
	
	override MonsterEvolvingForUserRepository getMonstEvolvingRepo( )
	{
		return monstEvolvingRepo
	}
	
	override MonsterHealingForUserRepository getMonstHealingRepo( )
	{
		return monstHealingRepo
	}
	
	override AchievementForUserRepository getAchievementRepo( )
	{
		return achievementRepo
	}

	//
	// Extension Lib Getters
	//
		
	override ConfigExtensions getConfigExtensionLib( ) {
		return configExtensionLib
	}
	
	override ProbabilityExtensionLib getProbabilityExtensionLib( ) {
		return probabilityExtensionLib
	}
	
	override TaskExtensionLib getTaskExtensionLib( ) {
		return taskExtensionLib
	}
	
	//
	// Dependency Injection
	//
	
	package def void setUserRepo( UserRepository userRepo )
	{
		this.userRepo = userRepo
	}

	package def void setUserCredRepo( UserCredentialRepository userCredRepo )
	{
		this.userCredRepo = userCredRepo
	}

	package def void setUdraRepo( UserDataRarelyAccessedRepository udraRepo )
	{
		this.udraRepo = udraRepo
	}

	package def void setLocationRepo( LocationRepository locationRepo )
	{
		this.locationRepo = locationRepo
	}

	package def void setEventRepo( EventPersistentForUserRepository eventRepo )
	{
		this.eventRepo = eventRepo
	}
	
	package def void setTfuOngoingRepo( TaskForUserOngoingRepository tfuOngoingRepo )
	{
		this.tfuOngoingRepo = tfuOngoingRepo
	}
	
	package def void setTaskStageForUserRepo( TaskStageForUserRepository taskStageForUserRepo )
	{
		this.taskStageForUserRepo = taskStageForUserRepo
	}
	
	package def void setTfuCompleteRepo( TaskForUserCompletedRepository tfuCompleteRepo )
	{
		this.tfuCompleteRepo = tfuCompleteRepo
	}

	package def void setQfuRepo( QuestForUserRepository qfuRepo )
	{
		this.qfuRepo = qfuRepo
	}
	
	package def void setQjfuRepo( QuestJobForUserRepository qjfuRepo )
	{
		this.qjfuRepo = qjfuRepo
	}
	
	package def void setSfuRepo( StructureForUserRepository sfuRepo )
	{
		this.sfuRepo = sfuRepo
	}
	
	package def void setOfuRepo( ObstacleForUserRepository ofuRepo )
	{
		this.ofuRepo = ofuRepo
	}
	
	package def void setMiniJobRepo( MiniJobForUserRepository miniJobRepo )
	{
		this.miniJobRepo = miniJobRepo
	}

	package def void setMfuRepo( MonsterForUserRepository mfuRepo )
	{
		this.mfuRepo = mfuRepo
	}

	package def void setMonstEnhanceRepo( MonsterEnhancingForUserRepository monstEnhanceRepo )
	{
		this.monstEnhanceRepo = monstEnhanceRepo
	}

	package def void setMonstEvolvingRepo( MonsterEvolvingForUserRepository monstEvolvingRepo )
	{
		this.monstEvolvingRepo = monstEvolvingRepo
	}

	package def void setMonstHealingRepo( MonsterHealingForUserRepository monstHealingRepo )
	{
		this.monstHealingRepo = monstHealingRepo
	}
	
	package def void setAchievementRepo( AchievementForUserRepository achievementRepo )
	{
		this.achievementRepo = achievementRepo
	}
	
	package def void setConfigExtensionLib( ConfigExtensions configExtensionLib ) {
		this.configExtensionLib = configExtensionLib
	}
	
	package def void setProbabilityExtensionLib( ProbabilityExtensionLib probabilityExtensionLib ) {
		this.probabilityExtensionLib = probabilityExtensionLib
	}
	
	package def void setTaskExtensionLib( TaskExtensionLib taskExtensionLib ) {
		this.taskExtensionLib = taskExtensionLib
	}
	
//	package def void setQuestExtensionLib( QuestExtensionLib questExtensionLib ) {
//		this.questExtensionLib = questExtensionLib
//	}
	
	// A single lookup method shared by both client and server accessors b/c they each return 
	// one of the concrete class's two different interfaces, one for client, one for server.
	override UserResourceImpl getUserResourceFor(String userUuid) {
		var UserResourceImpl resource = resourceMap.get(userUuid)
		if (resource == null) {
			resource = new UserResourceImpl(this, userUuid)
			resourceMap.put(resource)
		}
		
		return resource;	
	}
	
}