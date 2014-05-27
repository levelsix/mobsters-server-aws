package com.lvl6.mobsters.dynamo.repository;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.lvl6.mobsters.dynamo.UserCredential;
@Component public class UserCredentialRepository extends BaseDynamoRepository<UserCredential>{
	public UserCredentialRepository(){
		super(UserCredential.class);
		isActive = true;//for unit test
	}
	
	private static final Logger log = LoggerFactory.getLogger(UserCredentialRepository.class);
	
	
	public List<UserCredential> getUserCredentialByFacebook(String facebookId){
		UserCredential key = new UserCredential();
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
	
	
	public List<UserCredential> getUserCredentialByUdid(String udid){
		UserCredential key = new UserCredential();
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
	protected void createTable() {
		try {
		log.info("Creating Dynamo table {}", getTableName());
		ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("userId").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("udid").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("facebookId").withAttributeType("S"));
		
		ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
		ks.add(new KeySchemaElement().withAttributeName("userId").withKeyType(KeyType.HASH));
		
		  
		CreateTableRequest request = new CreateTableRequest()
		    .withTableName(getTableName())
		    .withAttributeDefinitions(attributeDefinitions)
		    .withKeySchema(ks)
		    .withProvisionedThroughput(provisionedThroughput);
			List<GlobalSecondaryIndex> globalIndexes = getGlobalIndexes();
			if(globalIndexes != null && !globalIndexes.isEmpty()) {
				request.withGlobalSecondaryIndexes(globalIndexes);
			}
			List<LocalSecondaryIndex> localIndexes = getLocalIndexes();
			if(localIndexes != null && !localIndexes.isEmpty()) {
				request.withLocalSecondaryIndexes(localIndexes);
			}
		    log.info("Creating table: {}", request);
		CreateTableResult result = client.createTable(request);
		log.info("Create table result: {}", result);
		}catch(Throwable e) {
			log.error("Error creating Dynamo table {}", getTableName(), e);
			throw e;
		}
	}
	
	
	
	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes(){
		List<GlobalSecondaryIndex> indexes = new ArrayList<>();
		//udid
		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("udid", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex()
			.withIndexName("udidGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL))
			.withProvisionedThroughput(provisionedThroughput));
		
		//facebooktId
		indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("facebookId", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex()
			.withIndexName("facebookIdGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL))
			.withProvisionedThroughput(provisionedThroughput));
		
		return indexes;
	}
}