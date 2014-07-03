package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;
@Component public abstract class EventPersistentForUserRepository extends BaseDynamoItemRepositoryImpl<EventPersistentForUser>{
	public EventPersistentForUserRepository(){
		super(EventPersistentForUser.class);
	}

	List<EventPersistentForUser> findByUserId( String userId );

}