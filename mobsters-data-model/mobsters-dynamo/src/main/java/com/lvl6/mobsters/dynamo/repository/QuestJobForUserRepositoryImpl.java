package com.lvl6.mobsters.dynamo.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lvl6.mobsters.dynamo.QuestJobForUser;

@Component
public class QuestJobForUserRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<QuestJobForUser, Integer>
	implements
		QuestJobForUserRepository
{
	private static final Logger log = LoggerFactory
		.getLogger(QuestForUserRepositoryImpl.class);
	
	public QuestJobForUserRepositoryImpl()
	{
		super(QuestJobForUser.class, "questJobId", Integer.class);
	}

	@Override
	public List<QuestJobForUser> findByUserIdAndQuestIdIn(
		String userId,
		Collection<Integer> questIds )
	{
		final List<AttributeValue> questIdz = new ArrayList<>();
		final QuestJobForUser hashKey = new QuestJobForUser();
		hashKey.setUserId(userId);
		for (final Integer quest : questIds) {
			questIdz.add(new AttributeValue().withN(quest.toString()));
		}

		final DynamoDBQueryExpression<QuestJobForUser> query =
			new DynamoDBQueryExpression<QuestJobForUser>()
				//.withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withQueryFilterEntry(
					"questId",
					new Condition().withComparisonOperator(
						ComparisonOperator.IN).withAttributeValueList(
							questIdz)
				)
				.withConsistentRead(true);
		QuestJobForUserRepositoryImpl.log.info(
			"Query: {}",
			query);
		final PaginatedQueryList<QuestJobForUser> questJobsForUser = query(query);
		questJobsForUser.loadAllResults();
		return questJobsForUser;
	}

}