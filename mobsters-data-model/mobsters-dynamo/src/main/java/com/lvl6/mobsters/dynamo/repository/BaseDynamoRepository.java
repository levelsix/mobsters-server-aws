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
	
    ProvisionedThroughput provisionedThroughput =
        new ProvisionedThroughput().withReadCapacityUnits(1l).withWriteCapacityUnits(1l);

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
			for (final T nextObj : objs ) {
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
	
    // public Map<String, List<Object>> loadAll( List<KeyPair> hashAndRangeKeyPairs ) {
    // Map<Class<T>, List<KeyPair>> clazzToHashAndRangeKeyPairs =
    // new HashMap<Class<T>, List<KeyPair>>();
    // clazzToHashAndRangeKeyPairs.put(clss, hashAndRangeKeyPairs);
    // // mapper.batchLoad(clazzToHashAndRangeKeyPairs); // doesn't compile
    //
    // Map<Class<?>, List<KeyPair>> foo = new HashMap<Class<?>, List<KeyPair>>();
    // foo.putAll(clazzToHashAndRangeKeyPairs);
    //
    // // caller will do conversion to T manually
    // return mapper.batchLoad(foo); // compiles
    // }
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
		final String tableName = getTableName();
		try {
			BaseDynamoRepository.log.info(
				"Creating Dynamo table {}",
				tableName);
			final ArrayList<AttributeDefinition> ads = new ArrayList<AttributeDefinition>();
			final ArrayList<KeySchemaElement> kse = new ArrayList<KeySchemaElement>();
			getAttributeDefinitions(
				ads,
				kse);
			final ProvisionedThroughput provisionedThroughput =
				new ProvisionedThroughput().withReadCapacityUnits(
					provisioning.getReads()).withWriteCapacityUnits(
					provisioning.getWrites());
			final CreateTableRequest request = new CreateTableRequest().withTableName(
				tableName).withAttributeDefinitions(
				ads).withKeySchema(
				kse).withProvisionedThroughput(
                    provisionedThroughput);
			if ((getGlobalIndexes() != null) && !getGlobalIndexes().isEmpty()) {
				request.withGlobalSecondaryIndexes(getGlobalIndexes());
			}
			if ((getLocalIndexes() != null) && !getLocalIndexes().isEmpty()) {
				request.withLocalSecondaryIndexes(getLocalIndexes());
			}
			repoTxManager.getClient().createTable(
				request);
		} catch (final Throwable e) {
			BaseDynamoRepository.log.error(
				"Error creating Dynamo table {}",
				tableName,
				e);
			throw e;
		}
	}
	
	public final void updateTable()
	{
		try {
			final ProvisionedThroughput provisionedThroughput =
				new ProvisionedThroughput().withReadCapacityUnits(
					provisioning.getReads()).withWriteCapacityUnits(
                    provisioning.getWrites());

			final UpdateTableRequest updateTableRequest =
				new UpdateTableRequest().withTableName(
					getTableName()).withProvisionedThroughput(
                    provisionedThroughput);
	        
			repoTxManager.getClient().updateTable(
				updateTableRequest);
		} catch (final Throwable e) {
			BaseDynamoRepository.log.error(
				"Error creating Dynamo table {}",
				getTableName(),
				e);
			throw e;
		}
	}
	
	public final void checkTable()
	{
		if (!isActive) { return; }

		final String tableName = getTableName();
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
                if (prov.getReadCapacityUnits().equals(provisioning.getReads())
                    && prov.getWriteCapacityUnits().equals(provisioning.getWrites()))
                {
                    log.info("Dynamo table {}", getTableName());
			}else {
				updateTable();
			}
		}else {
			createTable();
		}
		} catch (final ResourceNotFoundException re) {
			createTable();
		} catch (final Throwable e) {
			BaseDynamoRepository.log.error(
				"Error checking Dynamo table {}",
				tableName,
				e);
			throw e;
		}
	}
	
	protected final String getBoolean( final boolean bool )
	{
		return bool ? "1" : "0";
	}
	
	public final String getTableName()
	{
		String tableName;
		final DynamoDBTable tableAnnotation = clss.getAnnotation(DynamoDBTable.class);
		if ((tableAnnotation != null) && StringUtils.hasText(tableAnnotation.tableName())) {
			tableName = tableAnnotation.tableName();
		} else {
			tableName = clss.getSimpleName();
	}
	
		final TableNameOverride nameOverride = mapperConfig.getTableNameOverride();
		if ((nameOverride != null) && StringUtils.hasText(nameOverride.getTableNamePrefix())) {
			tableName = nameOverride.getTableNamePrefix() + tableName;
		}

		return tableName;
	}
	public List<GlobalSecondaryIndex> getGlobalIndexes()
	{
		return new ArrayList<>();
	}
	
	public List<LocalSecondaryIndex> getLocalIndexes()
	{
		return new ArrayList<>();
	}
	
	public DynamoProvisioning getProvisioning()
	{
		return provisioning;
	}

	public void setProvisioning( final DynamoProvisioning provisioning )
	{
		this.provisioning = provisioning;
		provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(
			provisioning.getReads()).withWriteCapacityUnits(
			provisioning.getWrites());
	}

	public DynamoDBMapper getMapper()
	{
		return mapper;
	}

	public void setProvisioning(DynamoProvisioning provisioning) {
		this.provisioning = provisioning;
		 provisionedThroughput = new ProvisionedThroughput()
		    .withReadCapacityUnits(provisioning.getReads())
		    .withWriteCapacityUnits(provisioning.getWrites());
    public void setProvisioning( DynamoProvisioning provisioning ) {
        this.provisioning = provisioning;
        provisionedThroughput =
            new ProvisionedThroughput().withReadCapacityUnits(provisioning.getReads()).withWriteCapacityUnits(
                provisioning.getWrites());
	public void setMapper( final DynamoDBMapper mapper )
	{
		this.mapper = mapper;
	}

	public DynamoDBMapperConfig getMapperConfig()
	{
    }

    public void setMapper( DynamoDBMapper mapper ) {
        this.mapper = mapper;
    }

    public DynamoDBMapperConfig getMapperConfig() {
		return mapperConfig;
	}

	public void setMapperConfig( final DynamoDBMapperConfig mapperConfig )
	{
		this.mapperConfig = mapperConfig;
	private static final ImmutableMap<Class<?>, String> CLASS_TO_ATTR_TYPE;
	static {
		try {
			CLASS_TO_ATTR_TYPE = ImmutableMap.<Class<?>, String> builder().put(
				String.class,
				"S").put(
				Date.class,
				"S").put(
				Calendar.class,
				"S").put(
				Boolean.class,
				"N").put(
				Boolean.TYPE,
				"N").put(
				Integer.class,
				"N").put(
				Integer.class.getMethod(
					"intValue").getReturnType(),
				"N").put(
				Long.class,
				"N").put(
				Integer.class.getMethod(
					"longValue").getReturnType(),
				"N").put(
				Double.class,
				"N").put(
				Integer.class.getMethod(
					"doubleValue").getReturnType(),
				"N").put(
				Float.class,
				"N").put(
				Integer.class.getMethod(
					"floatValue").getReturnType(),
				"N").put(
				BigDecimal.class,
				"N").put(
				BigInteger.class,
				"N").put(
				Byte.class,
				"B").put(
				Integer.class.getMethod(
					"byteValue").getReturnType(),
				"B").put(
				ByteBuffer.class,
				"B").build();
		} catch (
			NoSuchMethodException |
			SecurityException e) {
			throw new RuntimeException(
				e);
		}
	}

	private void getAttributeDefinitions(
		final List<AttributeDefinition> ads,
		final List<KeySchemaElement> kse )
	{
		for (final Field nextField : clss.getDeclaredFields()) {
			if ((nextField.isAnnotationPresent(DynamoDBAttribute.class) ||
				nextField.isAnnotationPresent(DynamoDBHashKey.class) ||
				nextField.isAnnotationPresent(DynamoDBRangeKey.class) ||
				// nextField.isAnnotationPresent(DynamoDBVersionAttribute.class) ||
				nextField.isAnnotationPresent(DynamoDBIndexHashKey.class) || nextField.isAnnotationPresent(DynamoDBIndexRangeKey.class)) &&
				(((!nextField.getGenericType().equals(
					Set.class)) && BaseDynamoRepository.CLASS_TO_ATTR_TYPE.containsKey(nextField.getType())) || ((nextField.getGenericType().equals(Set.class)) && BaseDynamoRepository.CLASS_TO_ATTR_TYPE.containsKey(nextField.getType().getTypeParameters()[0])))) {
				ads.add(new AttributeDefinition(
					nextField.getName(),
					getAttrType(nextField.getType())));
				if (nextField.isAnnotationPresent(DynamoDBHashKey.class)) {
					kse.add(new KeySchemaElement(
						nextField.getName(),
						KeyType.HASH));
				}
				if (nextField.isAnnotationPresent(DynamoDBRangeKey.class)) {
					kse.add(new KeySchemaElement(
						nextField.getName(),
						KeyType.RANGE));
				}
			}
		}

		for (final Method nextMethod : clss.getMethods()) {
			String attrName = nextMethod.getName();
			if ((attrName.startsWith("get") || attrName.startsWith("is") || attrName.startsWith("has")) &&
				(nextMethod.getParameterTypes().length == 0) &&
				(nextMethod.isAnnotationPresent(DynamoDBAttribute.class) ||
					nextMethod.isAnnotationPresent(DynamoDBHashKey.class) ||
					nextMethod.isAnnotationPresent(DynamoDBRangeKey.class) ||
					// nextMethod.isAnnotationPresent(DynamoDBVersionAttribute.class) ||
					nextMethod.isAnnotationPresent(DynamoDBIndexHashKey.class) || nextMethod.isAnnotationPresent(DynamoDBIndexRangeKey.class)) &&
				(((!nextMethod.getGenericReturnType().equals(
					Set.class)) && BaseDynamoRepository.CLASS_TO_ATTR_TYPE.containsKey(nextMethod.getReturnType())) || (nextMethod.getGenericReturnType().equals(
					Set.class) &&
					(nextMethod.getReturnType().getTypeParameters().length > 0) && BaseDynamoRepository.CLASS_TO_ATTR_TYPE.containsKey(nextMethod.getReturnType().getTypeParameters()[0])))) {
				if (attrName.startsWith("is")) {
					attrName = attrName.substring(2);
				} else {
					attrName = attrName.substring(3);
				}
				attrName = attrName.substring(
					0,
					1).toLowerCase() + attrName.substring(1);
				ads.add(new AttributeDefinition(
					attrName,
					getAttrType(nextMethod.getReturnType())));
				if (nextMethod.isAnnotationPresent(DynamoDBHashKey.class)) {
					kse.add(new KeySchemaElement(
						attrName,
						KeyType.HASH));
				}
				if (nextMethod.isAnnotationPresent(DynamoDBRangeKey.class)) {
					kse.add(new KeySchemaElement(
						attrName,
						KeyType.RANGE));
				}
			}
		}
	}

	private String getAttrType( final Class<?> attrClass )
	{
		final TypeVariable<?>[] typeParameters = attrClass.getTypeParameters();
		final String retVal;

		// The only parameterized type that can reach this code is Set<?>. All others are unparameterized
		// types used as keys in CLASS_TO_ATTR_TYPE.
		if (typeParameters.length == 0) {
			retVal = BaseDynamoRepository.CLASS_TO_ATTR_TYPE.get(attrClass);
		} else {
			retVal = "S" + BaseDynamoRepository.CLASS_TO_ATTR_TYPE.get(typeParameters[0]);
	}

		return retVal;
	}
}
