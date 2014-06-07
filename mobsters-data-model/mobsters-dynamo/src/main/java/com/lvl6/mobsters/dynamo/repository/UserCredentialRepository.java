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
public class UserCredentialRepository extends BaseDynamoRepository<UserCredential>
{
	public UserCredentialRepository()
	{
		super(
			UserCredential.class);
		isActive = true;// for unit test
	}

	private static final Logger log = LoggerFactory.getLogger(UserCredentialRepository.class);

	public List<UserCredential> getUserCredentialByFacebook( final String facebookId )
	{
		final UserCredential key = new UserCredential();
		key.setFacebookId(facebookId);
		DynamoDBQueryExpression<UserCredential> query = new DynamoDBQueryExpression<UserCredential>()
				.withIndexName("facebookIdGlobalIndex")
				.withHashKeyValues(key)
				.withConsistentRead(false);
		//log.info("Query: {}", query);
		PaginatedQueryList<UserCredential> users = mapper.query(UserCredential.class, query);
		users.loadAllResults();
		return users;
	}

	public List<UserCredential> getUserCredentialByUdid( final String udid )
	{
		final UserCredential key = new UserCredential();
		key.setUdid(udid);
		DynamoDBQueryExpression<UserCredential> query = new DynamoDBQueryExpression<UserCredential>()
				.withIndexName("udidGlobalIndex")
				.withHashKeyValues(key)
				.withConsistentRead(false);
		//log.info("Query: {}", query);
		PaginatedQueryList<UserCredential> users = mapper.query(UserCredential.class, query);
		users.loadAllResults();
		return users;
	}

	@Override
	public void deleteAll()
	{
		super.deleteAll();
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes()
	{
		final List<GlobalSecondaryIndex> indexes = new ArrayList<>();
		// udid
		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement(
			"udid",
			KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex().withIndexName(
			"udidGlobalIndex").withKeySchema(
			indexKeySchema).withProjection(
			new Projection().withProjectionType(ProjectionType.ALL)).withProvisionedThroughput(
			provisionedThroughput));

		// facebooktId
		indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement(
			"facebookId",
			KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex().withIndexName(
			"facebookIdGlobalIndex").withKeySchema(
			indexKeySchema).withProjection(
			new Projection().withProjectionType(ProjectionType.ALL)).withProvisionedThroughput(
			provisionedThroughput));

		return indexes;
	}
}
