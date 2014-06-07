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
import com.lvl6.mobsters.dynamo.AchievementForUser;

@Component
public class AchievementForUserRepository extends
		BaseDynamoRepository<AchievementForUser> {

	private static final Logger log = LoggerFactory
			.getLogger(AchievementForUserRepository.class);

	public AchievementForUserRepository() {
		super(AchievementForUser.class);
		isActive = true;// for unit test
	}

	public List<AchievementForUser> findByUserIdAndId(final String userId,
	    final Collection<Integer> achievementIds) {
	    final List<AttributeValue> achievementIdz = new ArrayList<>();
	    final AchievementForUser hashKey = new AchievementForUser();
	    hashKey.setUserId(userId);
	    for (final Integer achievementId : achievementIds) {
	        achievementIdz.add(new AttributeValue().withN(achievementId.toString()));
	    }

	    final DynamoDBQueryExpression<AchievementForUser> query = new DynamoDBQueryExpression<AchievementForUser>()
	        // .withIndexName("userIdGlobalIndex")
	        .withHashKeyValues(hashKey)
	        .withQueryFilterEntry(
	            "achievementId",
	            new Condition().withComparisonOperator(
	                ComparisonOperator.IN).withAttributeValueList(
	                    achievementIdz)).withConsistentRead(true);

	    AchievementForUserRepository.log.info("Query: {}", query);
	    final PaginatedQueryList<AchievementForUser> achievementsForUser = query(query);
	    achievementsForUser.loadAllResults();
	    return achievementsForUser;
	}

	/*
	@Override
	protected void createTable() {
		try {
			log.info("Creating Dynamo table {}", getTableName());
			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition()
					.withAttributeName("userId").withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition()
					.withAttributeName("achievementId").withAttributeType("N"));

			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
			ks.add(new KeySchemaElement().withAttributeName("userId")
					.withKeyType(KeyType.HASH));
			ks.add(new KeySchemaElement().withAttributeName("achievementId")
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
	*/

	@Override
	public List<LocalSecondaryIndex> getLocalIndexes() {
		return null;
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes() {
		return null;
	}

}