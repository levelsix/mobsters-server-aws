package com.lvl6.mobsters.dynamo.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.lvl6.mobsters.dynamo.QuestForUser;

@Component
public class QuestForUserRepository extends BaseDynamoRepository<QuestForUser> {

	private static final Logger log = LoggerFactory
			.getLogger(QuestForUserRepository.class);

	public QuestForUserRepository() {
		super(QuestForUser.class);
		isActive = true;
	}

	public List<QuestForUser> getQuestsForUser(String userId,
			boolean isComplete, Collection<String> questIds) {
		List<AttributeValue> questIdz = new ArrayList<>();
		QuestForUser hashKey = new QuestForUser();
		hashKey.setUserId(userId);
		for (String quest : questIds) {
			questIdz.add(new AttributeValue().withS(quest));
		}

		DynamoDBQueryExpression<QuestForUser> query = new DynamoDBQueryExpression<QuestForUser>()
				// .withIndexName("userIdGlobalIndex")
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

		log.info("Query: {}", query);
		PaginatedQueryList<QuestForUser> questsForUser = mapper.query(
				QuestForUser.class, query);
		questsForUser.loadAllResults();
		return questsForUser;
	}

	@Override
	protected void createTable() {
		try {
			log.info("Creating Dynamo table {}", getTableName());
			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition()
					.withAttributeName("userId").withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition()
					.withAttributeName("questId").withAttributeType("N"));
			// attributeDefinitions.add(new
			// AttributeDefinition().withAttributeName("questId").withAttributeType("S"));

			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
			ks.add(new KeySchemaElement().withAttributeName("userId")
					.withKeyType(KeyType.HASH));
			ks.add(new KeySchemaElement().withAttributeName("questId")
					.withKeyType(KeyType.RANGE));

			CreateTableRequest request = new CreateTableRequest()
					.withTableName(getTableName())
					.withAttributeDefinitions(attributeDefinitions)
					.withKeySchema(ks)
					.withProvisionedThroughput(provisionedThroughput);
			List<GlobalSecondaryIndex> globalIndexes = getGlobalIndexes();
			if (globalIndexes != null && !globalIndexes.isEmpty()) {
				request.withGlobalSecondaryIndexes(globalIndexes);
			}
			List<LocalSecondaryIndex> localIndexes = getLocalIndexes();
			if (localIndexes != null && !localIndexes.isEmpty()) {
				request.withLocalSecondaryIndexes(localIndexes);
			}
			log.info("Creating table: {}", request);
			CreateTableResult result = client.createTable(request);
			log.info("Create table result: {}", result);
		} catch (Throwable e) {
			log.error("Error creating Dynamo table {}", getTableName(), e);
			throw e;
		}
	}

	@Override
	public List<LocalSecondaryIndex> getLocalIndexes() {
		/*
		 * List<LocalSecondaryIndex> indexes = new ArrayList<>();
		 * //isCompleteIndex ArrayList<KeySchemaElement> indexKeySchema = new
		 * ArrayList<KeySchemaElement>(); indexKeySchema.add(new
		 * KeySchemaElement("userId", KeyType.HASH)); indexKeySchema.add(new
		 * KeySchemaElement("questId", KeyType.RANGE)); indexes.add(new
		 * LocalSecondaryIndex() .withIndexName("questIdIndex")
		 * .withKeySchema(indexKeySchema) .withProjection(new
		 * Projection().withProjectionType(ProjectionType.ALL)));
		 * //isRedeemedIndex indexKeySchema = new ArrayList<KeySchemaElement>();
		 * indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		 * indexKeySchema.add(new KeySchemaElement("isRedeemed",
		 * KeyType.RANGE)); indexes.add(new LocalSecondaryIndex()
		 * .withIndexName("isRedeemedIndex") .withKeySchema(indexKeySchema)
		 * .withProjection(new
		 * Projection().withProjectionType(ProjectionType.ALL)));
		 * //isCompleteIndex indexKeySchema = new ArrayList<KeySchemaElement>();
		 * indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		 * indexKeySchema.add(new KeySchemaElement("isComplete",
		 * KeyType.RANGE)); indexes.add(new LocalSecondaryIndex()
		 * .withIndexName("isRedeemedIndex") .withKeySchema(indexKeySchema)
		 * .withProjection(new
		 * Projection().withProjectionType(ProjectionType.ALL)));
		 */
		return null;
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes() {
		/*
		 * List<GlobalSecondaryIndex> indexes = new ArrayList<>(); //userId
		 * ArrayList<KeySchemaElement> indexKeySchema = new
		 * ArrayList<KeySchemaElement>(); indexKeySchema.add(new
		 * KeySchemaElement("userId", KeyType.HASH)); indexes.add(new
		 * GlobalSecondaryIndex() .withIndexName("userIdGlobalIndex")
		 * .withKeySchema(indexKeySchema) .withProjection(new
		 * Projection().withProjectionType(ProjectionType.ALL))
		 * .withProvisionedThroughput(provisionedThroughput));
		 */

		// questId
		/*
		 * indexKeySchema = new ArrayList<KeySchemaElement>();
		 * indexKeySchema.add(new KeySchemaElement("questId", KeyType.HASH));
		 * indexes.add(new GlobalSecondaryIndex()
		 * .withIndexName("questIdGlobalIndex") .withKeySchema(indexKeySchema)
		 * .withProjection(new
		 * Projection().withProjectionType(ProjectionType.ALL))
		 * .withProvisionedThroughput(provisionedThroughput));
		 */
		return null;
	}

}