package com.lvl6.mobsters.dynamo.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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
import com.google.common.collect.ImmutableMap;
import com.lvl6.mobsters.dynamo.setup.DataRepositoryTxManager;

public abstract class BaseDynamoRepositoryImpl<T> implements BaseDynamoRepository<T>, DynamoRepositorySetup
{
	private static final Logger LOG = LoggerFactory.getLogger(BaseDynamoRepositoryImpl.class);

	@Autowired
	private DynamoDBMapper mapper;

	@Autowired
	private DataRepositoryTxManager repoTxManager;
	
	private volatile ProvisionedThroughput provisionedThroughput;
	
	private volatile String tableName;
	
	protected final Class<T> clazz;

	protected BaseDynamoRepositoryImpl( final Class<T> clazz )
	{
		super();
		this.clazz = clazz;
		this.provisionedThroughput =
			new ProvisionedThroughput()
				.withReadCapacityUnits(1L)
			    .withWriteCapacityUnits(1L);
		this.tableName = getBaseTableName();
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#save(T)
	 */
	@Override
	public final void save( final T obj )
	{
		final Transaction t1 = getActiveTransaction();
		if (t1 != null) {
			t1.save(obj);
		} else {
			mapper.save(obj);
		}
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#saveAll(java.lang.Iterable)
	 */
	@Override
	public final void saveEach( final Iterable<T> objs )
	{
		final Transaction t1 = getActiveTransaction();
		if (t1 != null) {
			// DynamoDB transaction library has no bulk operations...
			for (final T nextObj : objs) {
				t1.save(nextObj);
			}
		} else {
			// batchSave() and batchDelete() are problematic insofar as they do
			// not even check optimistic lock versions!
			// mapper.batchSave(objs);
			for (final T nextObj : objs) {
				mapper.save(nextObj);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#delete(T)
	 */
	@Override
	public void delete( final T item )
	{
		final Transaction t1 = getActiveTransaction();
		if (t1 != null) {
			t1.delete(item);
		} else {
			mapper.delete(item);
		}
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#deleteAll(java.lang.Iterable)
	 */
	public final void deleteEach( final Iterable<T> objs )
	{
		final Transaction t1 = getActiveTransaction();
		if (t1 != null) {
			// DynamoDB transaction library has no bulk operations...
			for (final T nextObj : objs) {
				t1.delete(nextObj);
			}
		} else {
			// batchSave() and batchDelete() are problematic insofar as they do
			// not check optimistic lock versions!
			for (final T nextObj : objs) {
				mapper.delete(nextObj);
			}
		}
	}

	/**
	 * Run the argument scan. Beware this is not a transaction protected
	 * read--it has no isolation guarantees and can potentially return results
	 * that will later get rolled back and is susceptible both non-repeatable
	 * and phantom reads.
	 * 
	 * @param scan
	 * @return
	 */
	protected final List<T> scan( final DynamoDBScanExpression scan )
	{
		return mapper.scan(clazz, scan);
	}

	protected final PaginatedQueryList<T> query( final DynamoDBQueryExpression<T> query )
	{
		return mapper.query(clazz, query);
	}

	protected final Transaction getActiveTransaction() {
		return repoTxManager.getActiveTransaction();
	}

	protected final String getBoolean( final boolean bool )
	{
		return bool ? "1" : "0";
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#createTable()
	 */
	@Override
	public final void createTable()
	{
		final String tableName = getTableName();
		try {
			LOG.info("Creating Dynamo table {}", tableName);
			final ArrayList<AttributeDefinition> ads = new ArrayList<AttributeDefinition>();
			final ArrayList<KeySchemaElement> kse = new ArrayList<KeySchemaElement>();
			getAttributeDefinitions(ads, kse);
			final CreateTableRequest request =
			    new CreateTableRequest()
					.withTableName(tableName)
			        .withAttributeDefinitions(ads)
			        .withKeySchema(kse)
			        .withProvisionedThroughput(provisionedThroughput);
			
			final List<GlobalSecondaryIndex> globalIndices = getGlobalIndexes();
			final List<LocalSecondaryIndex> localIndices = getLocalIndexes();
			
			if ((globalIndices != null)
			    && !globalIndices.isEmpty()) {
				for( final GlobalSecondaryIndex nextGsi : globalIndices ) {
					// TODO: It is unlikely to be correct that every table's index has the same throughput
					//       requirements as the table itself.
					nextGsi.withProvisionedThroughput(provisionedThroughput);
				}
				request.withGlobalSecondaryIndexes(globalIndices);
			}
			if ((localIndices != null)
			    && !localIndices.isEmpty()) {
				request.withLocalSecondaryIndexes(localIndices);
			}
			if( LOG.isInfoEnabled() ) {
				LOG.info("Issuing Dynamo create table request: {}", request.toString());
			}
			repoTxManager
				.getClient()
			    .createTable(request);
		} catch (final Throwable e) {
			LOG.error("Error creating Dynamo table {}", tableName, e);
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#updateTable()
	 */
	@Override
	public final void updateTable()
	{
		try {
			final UpdateTableRequest updateTableRequest =
			    new UpdateTableRequest()
					.withTableName(
						getTableName())
			        .withProvisionedThroughput(provisionedThroughput);
			repoTxManager
				.getClient()
			    .updateTable(updateTableRequest);
		} catch (final Throwable e) {
			LOG.error("Error updating Dynamo table {}", getTableName(), e);
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#checkTable()
	 */
	@Override
	public final void checkTable()
	{
		final String tableName = getTableName();
		try {
			final DescribeTableResult result = repoTxManager
				.getClient()
			    .describeTable(tableName);
			if ((result != null)
			    && (result.getTable()
			        .getCreationDateTime() != null)) {
				LOG.info(
					"Dynamo table {} status: {}", 
					tableName, 
					result
						.getTable()
						.getTableStatus()
				);
				final ProvisionedThroughputDescription prov = result
					.getTable()
				    .getProvisionedThroughput();
				if (prov
					.getReadCapacityUnits()
					.equals(
						provisionedThroughput.getReadCapacityUnits())
				&& prov
					.getWriteCapacityUnits()
				    .equals(
				    	provisionedThroughput.getWriteCapacityUnits())) {
					LOG.info("Dynamo table {} provisioned as expected", getTableName());
				} else {
					updateTable();
				}
			} else {
				createTable();
			}
		} catch (final ResourceNotFoundException re) {
			createTable();
		} catch (final Throwable e) {
			LOG.error("Error checking Dynamo table {}", tableName, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Destroys all table content and re-creates it as an empty table.
	 * 
	 * This method is sufficiently dangerous that it is not automatically
	 * public. To expose it, a concrete subclass must override it with a public
	 * modifier or call it from some other public API method.
	 */
	public final void emptyTable()
	{
		dropTable();
		createTable();
	}

	/**
	 * Deletes table without recreating it.
	 * 
	 * This method is sufficiently dangerous that it is not automatically
	 * public. To expose it, a concrete subclass must override it with a public
	 * modifier or call it from some other public API method.
	 */
	public final void dropTable()
	{
		repoTxManager
			.getClient()
		    .deleteTable(
		    	getTableName());
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#getTableName()
	 */
	@Override
	public final String getTableName()
	{
		return tableName;
	}

	protected List<GlobalSecondaryIndex> getGlobalIndexes()
	{
		return Collections.emptyList();
	}

	protected List<LocalSecondaryIndex> getLocalIndexes()
	{
		return Collections.emptyList();
	}

	private static final ImmutableMap<Class<?>, String> CLASS_TO_ATTR_TYPE;
	static {
		try {
			CLASS_TO_ATTR_TYPE = ImmutableMap.<Class<?>, String> builder()
			    .put(String.class, "S")
			    .put(Date.class, "S")
			    .put(Calendar.class, "S")
			    .put(Boolean.class, "N")
			    .put(Boolean.TYPE, "N")
			    .put(Integer.class, "N")
			    .put(Integer.class.getMethod("intValue")
			        .getReturnType(), "N")
			    .put(Long.class, "N")
			    .put(Integer.class.getMethod("longValue")
			        .getReturnType(), "N")
			    .put(Double.class, "N")
			    .put(Integer.class.getMethod("doubleValue")
			        .getReturnType(), "N")
			    .put(Float.class, "N")
			    .put(Integer.class.getMethod("floatValue")
			        .getReturnType(), "N")
			    .put(BigDecimal.class, "N")
			    .put(BigInteger.class, "N")
			    .put(Byte.class, "B")
			    .put(Integer.class.getMethod("byteValue")
			        .getReturnType(), "B")
			    .put(ByteBuffer.class, "B")
			    .build();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	private void getAttributeDefinitions( final List<AttributeDefinition> ads,
	    final List<KeySchemaElement> kse )
	{
		KeySchemaElement rkse = null;
		for (final Field nextField : clazz.getDeclaredFields()) {
			if ((nextField.isAnnotationPresent(DynamoDBHashKey.class)
			    || nextField.isAnnotationPresent(DynamoDBRangeKey.class)
			    || nextField.isAnnotationPresent(DynamoDBIndexHashKey.class) || nextField.isAnnotationPresent(DynamoDBIndexRangeKey.class))
			    && (((!nextField.getGenericType()
			        .equals(Set.class)) && CLASS_TO_ATTR_TYPE.containsKey(nextField.getType())) || ((nextField.getGenericType().equals(Set.class)) && CLASS_TO_ATTR_TYPE.containsKey(nextField.getType()
			        .getTypeParameters()[0])))) {
				ads.add(new AttributeDefinition(nextField.getName(),
				    getAttrType(nextField.getType())));
				if (nextField.isAnnotationPresent(DynamoDBHashKey.class)) {
					kse.add(new KeySchemaElement(nextField.getName(), KeyType.HASH));
				}
				if (nextField.isAnnotationPresent(DynamoDBRangeKey.class)) {
					rkse = new KeySchemaElement(nextField.getName(), KeyType.RANGE);
				}
			}
		}

		for (final Method nextMethod : clazz.getMethods()) {
			String attrName = nextMethod.getName();
			if ((attrName.startsWith("get")
			    || attrName.startsWith("is") || attrName.startsWith("has"))
			    && (nextMethod.getParameterTypes().length == 0)
			    && (nextMethod.isAnnotationPresent(DynamoDBHashKey.class)
			        || nextMethod.isAnnotationPresent(DynamoDBRangeKey.class)
			        || nextMethod.isAnnotationPresent(DynamoDBIndexHashKey.class) || nextMethod.isAnnotationPresent(DynamoDBIndexRangeKey.class))
			    && (((!nextMethod.getGenericReturnType()
			        .equals(Set.class)) && CLASS_TO_ATTR_TYPE.containsKey(nextMethod.getReturnType())) || (nextMethod.getGenericReturnType()
			        .equals(Set.class)
			        && (nextMethod.getReturnType()
			            .getTypeParameters().length > 0) && CLASS_TO_ATTR_TYPE.containsKey(nextMethod.getReturnType()
			        .getTypeParameters()[0])))) {
				if (attrName.startsWith("is")) {
					attrName = attrName.substring(2);
				} else {
					attrName = attrName.substring(3);
				}
				attrName = attrName.substring(0, 1)
				    .toLowerCase()
				    + attrName.substring(1);
				ads.add(new AttributeDefinition(attrName,
				    getAttrType(nextMethod.getReturnType())));
				if (nextMethod.isAnnotationPresent(DynamoDBHashKey.class)) {
					kse.add(new KeySchemaElement(attrName, KeyType.HASH));
				}
				if (nextMethod.isAnnotationPresent(DynamoDBRangeKey.class)) {
					rkse = new KeySchemaElement(attrName, KeyType.RANGE);
				}
			}
		}

		// Append the range key if one was found.
		if (rkse != null) {
			kse.add(rkse);
		}
	}

	private String getAttrType( final Class<?> attrClass )
	{
		final TypeVariable<?>[] typeParameters = attrClass.getTypeParameters();
		final String retVal;

		// The only parameterized type that can reach this code is Set<?>. All
		// others are unparameterized types used as keys in CLASS_TO_ATTR_TYPE.
		if (typeParameters.length == 0) {
			retVal = CLASS_TO_ATTR_TYPE.get(attrClass);
		} else {
			retVal = "S"
			    + CLASS_TO_ATTR_TYPE.get(typeParameters[0]);
		}

		return retVal;
	}

	private String getBaseTableName() {
		final String retVal;
		final DynamoDBTable tableAnnotation = clazz.getAnnotation(DynamoDBTable.class);
		if (
			(tableAnnotation != null)
		    && StringUtils.hasText(
		    	tableAnnotation.tableName())) {
			retVal = tableAnnotation.tableName();
		} else {
			retVal = clazz.getSimpleName();
		}
		
		return retVal;
	}

	@Autowired
	public final void setProvisioning( final DynamoProvisioning provisioning )
	{
		this.provisionedThroughput =
			new ProvisionedThroughput(
				provisioning.getReads(),
			   	provisioning.getWrites());
	}

	protected void setMapper( final DynamoDBMapper mapper )
	{
		this.mapper = mapper;
	}

	@Autowired
	public final void setMapperConfig( final DynamoDBMapperConfig mapperConfig )
	{
		final TableNameOverride nameOverride = mapperConfig.getTableNameOverride();
		if (
			(nameOverride != null)
		    && StringUtils.hasText(
		    	nameOverride.getTableNamePrefix())) {
			tableName = 
				nameOverride.getTableNamePrefix()
			    + getBaseTableName();
		}

	}

	protected void setRepoTxManager(DataRepositoryTxManager repoTxManager) {
		this.repoTxManager = repoTxManager;
	}
}
