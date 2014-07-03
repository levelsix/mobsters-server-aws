package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
@Component public class MonsterHealingForUserRepositoryImpl extends BaseDynamoRepositoryImpl<MonsterHealingForUser>
	implements
		MonsterHealingForUserRepository
{
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterHealingForUserRepositoryImpl.class);

	
	public MonsterHealingForUserRepositoryImpl(){
		super(MonsterHealingForUser.class);
		isActive = true;// for unit test
	}

	@Override
	public List<MonsterHealingForUser> findByUserId( String userId )
	{
		final MonsterHealingForUser hashKey = new MonsterHealingForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<MonsterHealingForUser> query =
			new DynamoDBQueryExpression<MonsterHealingForUser>()
			// .withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<MonsterHealingForUser> monstersHealing = query(query);
		monstersHealing.loadAllResults();
		return monstersHealing;
	}

}