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
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.lvl6.mobsters.dynamo.QuestForUser;

@Component
public class QuestForUserRepositoryImpl extends BaseDynamoRepositoryImpl<QuestForUser>
	implements
		QuestForUserRepository
{

	private static final Logger log = LoggerFactory
			.getLogger(QuestForUserRepositoryImpl.class);
	public QuestForUserRepositoryImpl()
	{
		super(
			QuestForUser.class);
		isActive = true;
	}

	@Override
	public List<QuestForUser> findByUserIdAndIsCompleteAndQuestIdIn(
		final String userId,
		final boolean isComplete,
		final Collection<Integer> questIds )
	{
		final List<AttributeValue> questIdz = new ArrayList<>();
		final QuestForUser hashKey = new QuestForUser();
		hashKey.setUserId(userId);
		for (final Integer quest : questIds) {
			questIdz.add(new AttributeValue().withN(quest.toString()));
		}

		final DynamoDBQueryExpression<QuestForUser> query =
			new DynamoDBQueryExpression<QuestForUser>()
				//.withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withQueryFilterEntry(
						"isComplete",
						new Condition().withComparisonOperator(
								ComparisonOperator.NE).withAttributeValueList(
								new AttributeValue()
										.withN(getBoolean(isComplete))))
				.withQueryFilterEntry(
						"questId",
						new Condition().withComparisonOperator(
								ComparisonOperator.IN).withAttributeValueList(
								questIdz)).withConsistentRead(true);
		QuestForUserRepositoryImpl.log.info(
			"Query: {}",
			query);
		final PaginatedQueryList<QuestForUser> questsForUser = query(query);
		questsForUser.loadAllResults();
		return questsForUser;
	}

	@Override
	public List<LocalSecondaryIndex> getLocalIndexes()
	{
		/*
		//isCompleteIndex
		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		indexKeySchema.add(new KeySchemaElement("questId", KeyType.RANGE));
		indexes.add(new LocalSecondaryIndex()
			.withIndexName("questIdIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));
		//isRedeemedIndex
		indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		indexKeySchema.add(new KeySchemaElement("isRedeemed", KeyType.RANGE));
		indexes.add(new LocalSecondaryIndex()
			.withIndexName("isRedeemedIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));
		//isCompleteIndex
				indexKeySchema = new ArrayList<KeySchemaElement>();
				indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
				indexKeySchema.add(new KeySchemaElement("isComplete", KeyType.RANGE));
				indexes.add(new LocalSecondaryIndex()
					.withIndexName("isRedeemedIndex")
					.withKeySchema(indexKeySchema)
					.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));
		 */
		return null;
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes(){
/*		List<GlobalSecondaryIndex> indexes = new ArrayList<>();
		//userId
		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex()
			.withIndexName("userIdGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL))
			.withProvisionedThroughput(provisionedThroughput));*/

		//questId
/*		indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("questId", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex()
			.withIndexName("questIdGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL))
			.withProvisionedThroughput(provisionedThroughput));
		*/
		return null;
	}

}