package com.lvl6.mobsters.dynamo.repository;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;


@Component
public class MonsterEnhancingForUserRepositoryImpl
	extends BaseDynamoCollectionRepositoryImpl<MonsterEnhancingForUser, String>
	implements MonsterEnhancingForUserRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterEnhancingForUserRepositoryImpl.class);

	public MonsterEnhancingForUserRepositoryImpl()
	{
		super(MonsterEnhancingForUser.class, "monsterForUserId", String.class);
	}

	@Override
	public List<MonsterEnhancingForUser> findByUserId(String userId) 
	{
		return loadAll(userId);
	}
}