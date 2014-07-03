package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
@Component public class MonsterEvolvingForUserRepositoryImpl extends BaseDynamoRepositoryImpl<MonsterEvolvingForUser>
	implements 
		MonsterEvolvingForUserRepository
{
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterEvolvingForUserRepositoryImpl.class);

	public MonsterEvolvingForUserRepositoryImpl(){
		super(MonsterEvolvingForUser.class);
		isActive = true;// for unit test
	}
 
	@Override
	public List<MonsterEvolvingForUser> findByUserId( String userId )
	{
		final MonsterEvolvingForUser hashKey = new MonsterEvolvingForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<MonsterEvolvingForUser> query =
			new DynamoDBQueryExpression<MonsterEvolvingForUser>()
			// .withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<MonsterEvolvingForUser> monstersEvolving = query(query);
		monstersEvolving.loadAllResults();
		return monstersEvolving;
	}
}