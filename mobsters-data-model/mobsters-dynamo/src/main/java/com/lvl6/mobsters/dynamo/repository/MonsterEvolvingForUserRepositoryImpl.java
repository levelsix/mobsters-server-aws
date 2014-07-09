package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
@Component public class MonsterEvolvingForUserRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<MonsterEvolvingForUser, String>
	implements 
		MonsterEvolvingForUserRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterEvolvingForUserRepositoryImpl.class);

	public MonsterEvolvingForUserRepositoryImpl(){
		super(MonsterEvolvingForUser.class, "catalystMonsterForUserId", String.class);
	}
 
	@Override
	public List<MonsterEvolvingForUser> findByUserId( String userId )
	{
		return loadAll(userId);
	}
}