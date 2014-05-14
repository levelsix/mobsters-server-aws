package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.EventPersistent;
@Component public class EventPersistentRepository extends BaseDynamoRepository<EventPersistent>{
	public EventPersistentRepository(){
		super(EventPersistent.class);
	}

}