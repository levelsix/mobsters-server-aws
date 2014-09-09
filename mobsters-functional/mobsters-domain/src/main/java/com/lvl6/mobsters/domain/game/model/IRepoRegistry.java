package com.lvl6.mobsters.domain.game.model;

import com.lvl6.mobsters.dynamo.repository.AchievementForUserRepository;
import com.lvl6.mobsters.dynamo.repository.EventPersistentForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterEnhancingForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterEvolvingForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterHealingForUserRepository;
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository;
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository;
import com.lvl6.mobsters.dynamo.repository.QuestJobForUserRepository;
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository;
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepository;
import com.lvl6.mobsters.dynamo.repository.TaskForUserOngoingRepository;
import com.lvl6.mobsters.dynamo.repository.TaskStageForUserRepository;
import com.lvl6.mobsters.dynamo.repository.UserCredentialRepository;
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.domain.config.ConfigExtensions;
import com.lvl6.mobsters.utility.probability.ProbabilityExtensionLib;

interface IRepoRegistry {

	public UserRepository getUserRepo();

	public UserCredentialRepository getUserCredRepo();

	public UserDataRarelyAccessedRepository getUdraRepo();

	// public LocationRepository getLocationRepo();

	public EventPersistentForUserRepository getEventRepo();

	public TaskForUserOngoingRepository getTfuOngoingRepo();

	public TaskStageForUserRepository getTaskStageForUserRepo();

	public TaskForUserCompletedRepository getTfuCompleteRepo();

	public QuestForUserRepository getQfuRepo();

	public QuestJobForUserRepository getQjfuRepo();

	public StructureForUserRepository getSfuRepo();

	public ObstacleForUserRepository getOfuRepo();

	public MiniJobForUserRepository getMiniJobRepo();

	public MonsterForUserRepository getMfuRepo();

	public MonsterEnhancingForUserRepository getMonstEnhanceRepo();

	public MonsterEvolvingForUserRepository getMonstEvolvingRepo();

	public MonsterHealingForUserRepository getMonstHealingRepo();

	public AchievementForUserRepository getAchievementRepo();

	public void beginTransaction();
	
	public void commitTransaction();
	
	public void rollbackTransaction();
	
	public ConfigExtensions getConfigExtensionLib();

	public ProbabilityExtensionLib getProbabilityExtensionLib();

	public IGameEventMediator getGameEventMediator();
}