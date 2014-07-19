package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;

public interface EventPersistentForUserRepository extends BaseDynamoRepository<EventPersistentForUser>
{

	List<EventPersistentForUser> findByUserId( String userId );

}