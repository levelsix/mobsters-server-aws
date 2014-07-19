package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.MonsterHealingForUser;

public interface MonsterHealingForUserRepository extends BaseDynamoCollectionRepository<MonsterHealingForUser, String>
{
	public List<MonsterHealingForUser> findByUserId( String userId );
}
