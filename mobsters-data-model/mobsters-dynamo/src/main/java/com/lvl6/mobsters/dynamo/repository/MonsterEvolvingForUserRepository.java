package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;

public interface MonsterEvolvingForUserRepository extends BaseDynamoCollectionRepository<MonsterEvolvingForUser, String>
{
	List<MonsterEvolvingForUser> findByUserId( String userId );
}
