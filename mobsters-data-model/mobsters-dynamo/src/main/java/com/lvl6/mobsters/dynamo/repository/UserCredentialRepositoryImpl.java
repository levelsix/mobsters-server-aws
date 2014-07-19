package com.lvl6.mobsters.dynamo.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.lvl6.mobsters.dynamo.UserCredential;

@Component
public class UserCredentialRepositoryImpl extends BaseDynamoItemRepositoryImpl<UserCredential>
	implements
		UserCredentialRepository
{
	public UserCredentialRepositoryImpl()
	{
		super(UserCredential.class);
	}

	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(UserCredentialRepositoryImpl.class);

	@Override
	public List<UserCredential> findByFacebookId( final String facebookId )
	{
		final UserCredential key = new UserCredential();
		key.setFacebookId(facebookId);
		final DynamoDBQueryExpression<UserCredential> query =
			new DynamoDBQueryExpression<UserCredential>().withIndexName("facebookIdGlobalIndex")
				.withHashKeyValues(key)
				.withConsistentRead(false);
		// log.info("Query: {}", query);
		final PaginatedQueryList<UserCredential> users = query(query);
		users.loadAllResults();
		return users;
	}

	@Override
	public List<UserCredential> findByUdid( final String udid )
	{
		final UserCredential key = new UserCredential();
		key.setUdid(udid);
		final DynamoDBQueryExpression<UserCredential> query =
			new DynamoDBQueryExpression<UserCredential>().withIndexName("udidGlobalIndex")
				.withHashKeyValues(key)
				.withConsistentRead(false);
		// log.info("Query: {}", query);
		final PaginatedQueryList<UserCredential> users = query(query);
		users.loadAllResults();
		return users;
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes()
	{
		// TODO Global indices seem to have throughput provisioning independent of their
		//      host table's provisioning, but we have not provided a facility to set it
		//      on an index-by-index level yet.
		
		final List<GlobalSecondaryIndex> indexes = new ArrayList<>();
		// udid
		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("udid", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex().withIndexName("udidGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));

		// facebooktId
		indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("facebookId", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex().withIndexName("facebookIdGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));

		return indexes;
	}

	@Override
	protected UserCredential getHashKeyObject(String hashKey) {
		UserCredential retVal = new UserCredential();
		retVal.setUserId(hashKey);
		return retVal;
	}
}
