package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.EventPersistent;
public class EventPersistentRepository extends BaseDynamoRepository<EventPersistent>{
	public EventPersistentRepository(){
		super(EventPersistent.class);
	}

}