package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;
@Component 
public class EventPersistentForUserRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<EventPersistentForUser, Integer>
	implements
		EventPersistentForUserRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(EventPersistentForUserRepositoryImpl.class);
	
	public EventPersistentForUserRepositoryImpl(){
		super(EventPersistentForUser.class, "eventPersistentId", Integer.TYPE);
	}
	
	@Override
	public List<EventPersistentForUser> findByUserId( String userId )
	{
		return loadAll(userId);
	}
}