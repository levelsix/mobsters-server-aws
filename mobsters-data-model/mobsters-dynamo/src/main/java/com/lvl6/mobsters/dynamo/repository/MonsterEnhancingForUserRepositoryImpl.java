package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
@Component public class MonsterEnhancingForUserRepositoryImpl extends BaseDynamoRepositoryImpl<MonsterEnhancingForUser>
	implements
		MonsterEnhancingForUserRepository
{
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterEnhancingForUserRepositoryImpl.class);

	
	public MonsterEnhancingForUserRepositoryImpl(){
		super(MonsterEnhancingForUser.class);
		isActive = true;// for unit test
	}
	
	@Override
	public List<MonsterEnhancingForUser> findByUserId( String userId )
	{
		final MonsterEnhancingForUser hashKey = new MonsterEnhancingForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<MonsterEnhancingForUser> query =
			new DynamoDBQueryExpression<MonsterEnhancingForUser>()
			// .withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<MonsterEnhancingForUser> monstersEnhancing = query(query);
		monstersEnhancing.loadAllResults();
		return monstersEnhancing;
	}

}