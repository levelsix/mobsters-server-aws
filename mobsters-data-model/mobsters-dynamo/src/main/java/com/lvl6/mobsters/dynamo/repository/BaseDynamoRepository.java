package com.lvl6.mobsters.dynamo.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputDescription;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.UpdateTableRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateTableResult;

abstract public class BaseDynamoRepository<T> {
	
	
	private static final Logger log = LoggerFactory.getLogger(BaseDynamoRepository.class);
	
	protected boolean isActive = false;
	
	@Autowired
	protected AmazonDynamoDBClient client;
	
	@Autowired
	protected DynamoProvisioning provisioning = new DynamoProvisioning();
	
	
	@Autowired
	protected DynamoDBMapper mapper;
	
	@Autowired
	protected DynamoDBMapperConfig mapperConfig;
	
	protected Class<T> clss;
	
	
	public BaseDynamoRepository(Class<T> clss) {
		super();
		this.clss = clss;
	}

	
	public void save(T obj) {
		mapper.save(obj);
	}
	
	public T load(String id) {
		return mapper.load(clss, id);
	}
	
	
	
	protected void createTable() {
		try {
		log.info("Creating Dynamo table {}", getTableName());
		ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType("S"));
		        
		ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
		ks.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH));
		  
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
		    .withReadCapacityUnits(provisioning.getReads())
		    .withWriteCapacityUnits(provisioning.getWrites());
		        
		CreateTableRequest request = new CreateTableRequest()
		    .withTableName(getTableName())
		    .withAttributeDefinitions(attributeDefinitions)
		    .withKeySchema(ks)
		    .withProvisionedThroughput(provisionedThroughput);
			if(getGlobalIndexes() != null && !getGlobalIndexes().isEmpty()) {
				request.withGlobalSecondaryIndexes(getGlobalIndexes());
			}
			if(getLocalIndexes() != null && !getLocalIndexes().isEmpty()) {
				request.withLocalSecondaryIndexes(getLocalIndexes());
			}
		    
		CreateTableResult result = client.createTable(request);
		}catch(Throwable e) {
			log.error("Error creating Dynamo table {}", getTableName(), e);
			throw e;
		}
	}
	
	protected void updateTable() {
		try {
	        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
	        .withReadCapacityUnits(provisioning.getReads())
	        .withWriteCapacityUnits(provisioning.getWrites());

	        UpdateTableRequest updateTableRequest = new UpdateTableRequest()
	            .withTableName(getTableName())
	            .withProvisionedThroughput(provisionedThroughput);
	        
	        UpdateTableResult result = client.updateTable(updateTableRequest);
			
		}catch(Throwable e) {
			log.error("Error creating Dynamo table {}", getTableName(), e);
			throw e;
		}
	}
	
	public void checkTable() {
		if(!isActive){
			return;
		}
		try {
		DescribeTableResult result = client.describeTable(getTableName());
		if(result != null && result.getTable().getCreationDateTime() != null) {
			log.info("Dynamo table {} status: {}", getTableName(), result.getTable().getTableStatus());
			ProvisionedThroughputDescription prov = result.getTable().getProvisionedThroughput();
			if(prov.getReadCapacityUnits().equals(provisioning.getReads())&&prov.getWriteCapacityUnits().equals(provisioning.getWrites())) {
				log.info("Dynamo table {}", getTableName());
			}else {
				updateTable();
			}
		}else {
			createTable();
		}
		}catch(ResourceNotFoundException re) {
			createTable();
		}catch(Throwable e) {
			log.error("Error checking Dynamo table {}", getTableName(), e);
			throw e;
		}
	}
	
	protected String getTableName() {
		return mapperConfig.getTableNameOverride().getTableNamePrefix()+clss.getSimpleName();
	}
	
	
	public List<GlobalSecondaryIndex> getGlobalIndexes(){
		return new ArrayList<>();
	}
	
	public List<LocalSecondaryIndex> getLocalIndexes(){
		return new ArrayList<>();
	}
	
	public AmazonDynamoDBClient getClient() {
		return client;
	}

	public void setClient(AmazonDynamoDBClient client) {
		this.client = client;
	}

	public DynamoProvisioning getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(DynamoProvisioning provisioning) {
		this.provisioning = provisioning;
	}


	public DynamoDBMapper getMapper() {
		return mapper;
	}


	public void setMapper(DynamoDBMapper mapper) {
		this.mapper = mapper;
	}


	public DynamoDBMapperConfig getMapperConfig() {
		return mapperConfig;
	}


	public void setMapperConfig(DynamoDBMapperConfig mapperConfig) {
		this.mapperConfig = mapperConfig;
	}

}
