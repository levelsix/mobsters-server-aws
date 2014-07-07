package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
@Component public class MonsterHealingForUserRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<MonsterHealingForUser, String>
	implements
		MonsterHealingForUserRepository
{
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterHealingForUserRepositoryImpl.class);

	
	public MonsterHealingForUserRepositoryImpl(){
		super(MonsterHealingForUser.class, "monsterForUserId", String.class);
	}

	@Override
	public List<MonsterHealingForUser> findByUserId( String userId )
	{
		return loadAll(userId);
	}

}