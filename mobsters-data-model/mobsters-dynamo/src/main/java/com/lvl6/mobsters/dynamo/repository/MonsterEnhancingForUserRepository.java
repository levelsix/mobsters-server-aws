package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;

public interface MonsterEnhancingForUserRepository extends BaseDynamoRepository<MonsterEnhancingForUser>
{

	List<MonsterEnhancingForUser> findByUserId( String userId );

}