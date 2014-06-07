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

	ProvisionedThroughput provisionedThroughput =
		new ProvisionedThroughput().withReadCapacityUnits(
			1l).withWriteCapacityUnits(
			1l);

	public BaseDynamoRepository( final Class<T> clss )
	{
		super();
		this.clss = clss;
	}

	public final void save( final T obj )
	{
		final Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.save(obj);
		} else {
			mapper.save(obj);
		}
	}

	public final void save( final Iterable<T> objs )
	{
		final Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			// DynamoDB transaction library has no bulk operations...
			for (final T nextObj : objs) {
				t1.save(nextObj);
			}
		} else {
			// batchSave() and batchDelete() are problematic insofar as they do not even check optimistic
			// lock versions!
			// mapper.batchSave(objs);
			for (final T nextObj : objs) {
				mapper.save(nextObj);
			}
		}
	}

	public final T load( final String hashKey )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		final T retVal;
		if (t1 != null) {
			retVal = t1.load(
				clss,
				hashKey);
		} else {
			retVal = repoTxManager.load(
				clss,
				hashKey,
				IsolationLevel.COMMITTED);
		}

		return retVal;
	}

	public final T load( final String hashKey, final String rangeKey )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		final T retVal;
		if (t1 != null) {
			retVal = t1.load(
				clss,
				hashKey,
				rangeKey);
		} else {
			retVal = repoTxManager.load(
				clss,
				hashKey,
				rangeKey,
				IsolationLevel.COMMITTED);
		}

		return retVal;
	}

	public void delete( final T item )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.delete(item);
		} else {
			mapper.delete(item);
		}
	}

	public final void delete( final Iterable<T> objs )
	{
		final Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			// DynamoDB transaction library has no bulk operations...
			for (final T nextObj : objs) {
				t1.delete(nextObj);
			}
		} else {
			// batchSave() and batchDelete() are problematic insofar as they do not even check optimistic
			// lock versions!
			// mapper.batchSave(objs);
			for (final T nextObj : objs) {
				mapper.delete(nextObj);
			}
		}
	}

	/**
	 * Destroys all table content and re-creates it as an empty table.
	 *
	 * This method is sufficiently dangerous that it is not automatically public. To expose it, a
	 * concrete subclass must override it with a public modifier or call it from some other public API
	 * method.
	 */
	protected void deleteAll()
	{
		repoTxManager.getClient().deleteTable(
			getTableName());
		createTable();
	}

	/**
	 * Run the argument scan. Beware this is not a transaction protected read--it has no isolation
	 * guarantees and can potentially return results that will later get rolled back and is susceptible
	 * both non-repeatable and phantom reads.
	 *
	 * @param scan
	 * @return
	 */
	protected final List<T> scan( final DynamoDBScanExpression scan )
	{
		return mapper.scan(
			clss,
			scan);
	}

	protected final PaginatedQueryList<T> query( final DynamoDBQueryExpression<T> query )
	{
		return mapper.query(
			clss,
			query);
	}

	public final void createTable()
	{
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

	public final void updateTable()
	{
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

	public final void checkTable()
	{
		if (!isActive) { return; }
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
