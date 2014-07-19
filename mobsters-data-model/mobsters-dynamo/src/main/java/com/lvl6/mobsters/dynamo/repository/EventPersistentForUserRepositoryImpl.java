package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.EventPersistentForUser;
@Component public class EventPersistentForUserRepositoryImpl extends BaseDynamoRepositoryImpl<EventPersistentForUser>
	implements
		EventPersistentForUserRepository
{
	public EventPersistentForUserRepositoryImpl(){
		super(EventPersistentForUser.class);
	}


	private static final Logger LOG =
		LoggerFactory.getLogger(EventPersistentForUserRepositoryImpl.class);

	
	@Override
	public List<EventPersistentForUser> findByUserId( String userId )
	{
		final EventPersistentForUser hashKey = new EventPersistentForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<EventPersistentForUser> query =
			new DynamoDBQueryExpression<EventPersistentForUser>()
			// .withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<EventPersistentForUser> eventPersistentForUser = query(query);
		eventPersistentForUser.loadAllResults();
		return eventPersistentForUser;
	}
}