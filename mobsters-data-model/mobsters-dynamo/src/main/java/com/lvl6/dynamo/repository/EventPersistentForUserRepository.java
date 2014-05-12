package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.EventPersistentForUser;
public class EventPersistentForUserRepository extends BaseDynamoRepository<EventPersistentForUser>{
	public EventPersistentForUserRepository(){
		super(EventPersistentForUser.class);
	}

}