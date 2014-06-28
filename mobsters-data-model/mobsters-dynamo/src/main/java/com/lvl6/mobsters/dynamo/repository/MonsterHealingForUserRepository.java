package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;

public interface MonsterHealingForUserRepository extends BaseDynamoRepository<MonsterHealingForUser>
{

	List<MonsterHealingForUser> findByUserId( String userId );

}