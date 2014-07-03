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
import com.lvl6.mobsters.dynamo.MiniJobForUser;

@Component
public class MiniJobForUserRepositoryImpl extends
		BaseDynamoRepositoryImpl<MiniJobForUser> implements MiniJobForUserRepository {

	private static final Logger log = LoggerFactory
			.getLogger(MiniJobForUserRepositoryImpl.class);

	public MiniJobForUserRepositoryImpl() {
		super(MiniJobForUser.class);
		isActive = true;// for unit test
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository#findByUserIdAndId(java.lang.String, java.util.Collection)
	 */
	@Override
	public List<MiniJobForUser> findByUserIdAndId(final String userId,
        final Collection<String> userMiniJobIds) {
        final List<AttributeValue> userMiniJobIdz = new ArrayList<>();
        final MiniJobForUser hashKey = new MiniJobForUser();
        hashKey.setUserId(userId);
        for (final String userMiniJobId : userMiniJobIds) {
            userMiniJobIdz.add(new AttributeValue().withS(userMiniJobId.toString()));
        }

        final DynamoDBQueryExpression<MiniJobForUser> query = new DynamoDBQueryExpression<MiniJobForUser>()
            // .withIndexName("userIdGlobalIndex")
            .withHashKeyValues(hashKey)
            .withQueryFilterEntry(
                "miniJobForUserId",
                new Condition().withComparisonOperator(
                    ComparisonOperator.IN).withAttributeValueList(
                        userMiniJobIdz)).withConsistentRead(true);

        MiniJobForUserRepositoryImpl.log.info("Query: {}", query);
        final PaginatedQueryList<MiniJobForUser> miniJobsForUser = query(query);
        miniJobsForUser.loadAllResults();
        return miniJobsForUser;
    }

	@Override
	public List<MiniJobForUser> findByUserId( final String userId ) {
		final MiniJobForUser hashKey = new MiniJobForUser();
        hashKey.setUserId(userId);
        
        final DynamoDBQueryExpression<MiniJobForUser> query = new DynamoDBQueryExpression<MiniJobForUser>()
            // .withIndexName("userIdGlobalIndex")
            .withHashKeyValues(hashKey)
            .withConsistentRead(true);

        MiniJobForUserRepositoryImpl.log.info("Query: {}", query);
        final PaginatedQueryList<MiniJobForUser> miniJobsForUser = query(query);
        miniJobsForUser.loadAllResults();
        return miniJobsForUser;
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
					.withAttributeName("miniJobForUserId").withAttributeType("S"));

			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
			ks.add(new KeySchemaElement().withAttributeName("userId")
					.withKeyType(KeyType.HASH));
			ks.add(new KeySchemaElement().withAttributeName("miniJobForUserId")
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
