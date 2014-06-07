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
import com.lvl6.mobsters.dynamo.MiniJobForUser;

@Component
public class MiniJobForUserRepository extends
		BaseDynamoRepository<MiniJobForUser> {

	private static final Logger log = LoggerFactory
			.getLogger(MiniJobForUserRepository.class);

	public MiniJobForUserRepository() {
		super(MiniJobForUser.class);
		isActive = true;// for unit test
	}
	
	public List<MiniJobForUser> findByUserIdAndId(String userId,
        Collection<String> userMiniJobIds) {
        List<AttributeValue> userMiniJobIdz = new ArrayList<>();
        MiniJobForUser hashKey = new MiniJobForUser();
        hashKey.setUserId(userId);
        for (String userMiniJobId : userMiniJobIds) {
            userMiniJobIdz.add(new AttributeValue().withS(userMiniJobId.toString()));
        }

        DynamoDBQueryExpression<MiniJobForUser> query = new DynamoDBQueryExpression<MiniJobForUser>()
            // .withIndexName("userIdGlobalIndex")
            .withHashKeyValues(hashKey)
            .withQueryFilterEntry(
                "miniJobForUserId",
                new Condition().withComparisonOperator(
                    ComparisonOperator.IN).withAttributeValueList(
                        userMiniJobIdz)).withConsistentRead(true);

        log.info("Query: {}", query);
        PaginatedQueryList<MiniJobForUser> miniJobsForUser = mapper.query(
            MiniJobForUser.class, query);
        miniJobsForUser.loadAllResults();
        return miniJobsForUser;
    }

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

	@Override
	public List<LocalSecondaryIndex> getLocalIndexes() {
		return null;
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes() {
		return null;
	}

}