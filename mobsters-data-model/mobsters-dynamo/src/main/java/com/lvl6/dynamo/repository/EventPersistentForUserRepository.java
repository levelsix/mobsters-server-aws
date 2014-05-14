package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.EventPersistentForUser;
@Component public class EventPersistentForUserRepository extends BaseDynamoRepository<EventPersistentForUser>{
	public EventPersistentForUserRepository(){
		super(EventPersistentForUser.class);
	}

}