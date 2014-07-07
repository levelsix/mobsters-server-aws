package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;
public interface EventPersistentForUserRepository extends BaseDynamoCollectionRepository<EventPersistentForUser, Integer>{
	List<EventPersistentForUser> findByUserId( String userId );
}