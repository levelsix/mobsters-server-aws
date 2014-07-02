package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;

public interface MonsterEvolvingForUserRepository extends BaseDynamoRepository<MonsterEvolvingForUser>
{

	List<MonsterEvolvingForUser> findByUserId( String userId );

}