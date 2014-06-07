package com.lvl6.mobsters.dynamo.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputDescription;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.UpdateTableRequest;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.Transaction.IsolationLevel;
import com.google.common.collect.ImmutableMap;
import com.lvl6.mobsters.dynamo.setup.DataRepositoryTxManager;
import com.lvl6.mobsters.dynamo.setup.Lvl6Transaction;

abstract public class BaseDynamoRepository<T>
{

	private static final Logger log = LoggerFactory.getLogger(BaseDynamoRepository.class);

	protected boolean isActive = true;

	@Autowired
	private DynamoProvisioning provisioning = new DynamoProvisioning();

	@Autowired
	private DynamoDBMapper mapper;

	@Autowired
	private DynamoDBMapperConfig mapperConfig;

	@Autowired
	private DataRepositoryTxManager repoTxManager;
	protected Class<T> clss;
	
	// @Autowired
	// private Lvl6TxManager ourTxManager;
	
	ProvisionedThroughput provisionedThroughput= new ProvisionedThroughput()
    .withReadCapacityUnits(1l)
    .withWriteCapacityUnits(1l);
	
	
	
	public BaseDynamoRepository(Class<T> clss) {
		super();
		this.clss = clss;
	}

	
	public void save(T obj) {
		//Transaction t1 = null; //ourTxManager.getActiveTransaction();
		//if (t1 != null) {
		if (false) {
			// TODO: Do transactional putItem
		} else {
			mapper.save(obj);
		}
	}
	
	public void saveAll(Iterable<T> objs) {
		//Transaction t1 = null; //ourTxManager.getActiveTransaction();
		//if (t1 != null) {
		if (false) {
			// DynamoDB transaction library has no bulk operations...
			for (final T nextObj : objs ) {
				// TODO: Do transactional putItem
			}
		} else {
			mapper.batchSave(objs);
		}
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
		    
		}catch(Throwable e) {
			log.error("Error creating Dynamo table {}", getTableName(), e);
			repoTxManager.getClient().createTable(
				request);
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
	        
			
		}catch(Throwable e) {
			log.error("Error creating Dynamo table {}", getTableName(), e);
			repoTxManager.getClient().updateTable(
				updateTableRequest);
			throw e;
		}
	}
	
	public void checkTable() {
		if(!isActive){
			return;
		}
		try {
			final DescribeTableResult result = repoTxManager.getClient().describeTable(
				tableName);
			if ((result != null) && (result.getTable().getCreationDateTime() != null)) {
				BaseDynamoRepository.log.info(
					"Dynamo table {} status: {}",
					tableName,
					result.getTable().getTableStatus());
				final ProvisionedThroughputDescription prov =
					result.getTable().getProvisionedThroughput();
				if (prov.getReadCapacityUnits().equals(
					provisioning.getReads()) && prov.getWriteCapacityUnits().equals(
					provisioning.getWrites())) {
					BaseDynamoRepository.log.info(
						"Dynamo table {}",
						tableName);
				} else {
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
	
	protected String getBoolean(boolean bool) {
		return bool ? "1" : "0";
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

	}

	public DynamoProvisioning getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(DynamoProvisioning provisioning) {
		this.provisioning = provisioning;
		 provisionedThroughput = new ProvisionedThroughput()
		    .withReadCapacityUnits(provisioning.getReads())
		    .withWriteCapacityUnits(provisioning.getWrites());
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
