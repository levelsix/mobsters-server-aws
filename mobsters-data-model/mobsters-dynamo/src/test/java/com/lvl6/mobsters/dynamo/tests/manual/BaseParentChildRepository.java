package com.lvl6.mobsters.dynamo.tests.manual;

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
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
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
import com.google.common.collect.ImmutableMap;
import com.lvl6.mobsters.dynamo.setup.Lvl6TxManager;

/**
 * Implemenation-less base class defining the contract of a DynamoDB abstract
 * repository that addresses a pair of a tables with a recurring pattern of
 * traits in how their contents are related that can be summarized as a
 * one-to-many parent to dependent children relation. P, the parent table's
 * in-memory representation class, is a fragment of a first-class object that
 * defines a single collection property that exerts containment over its
 * contents. C, the child table's in-memory class, is the third class entity
 * type that P's collection exists to contain. When transfered to a real
 * implementation, this classes methods will not be abstract. Rather they will
 * be a generalized version of the variant from this manual test program that is
 * selected as "best". A scenario this experiment does not explore is how a
 * Parent/Child repository base identifies the getter for its target collection,
 * particularly if there is desire for a parent fragment to have more than one
 * kind of containment collection. Afterthought notes: A first-class object is
 * characterized by the fact that its lifecycle is independent of any other data
 * model object's lifecycle. Its existence is not tied in any way to the
 * existence of some other parent object. A first-class fragment is associated
 * with the first class object whose identity (but not type) it shares. It is
 * globally unique, but its existence is dependent on the first class object its
 * a "part" of. An object fragment has the same key value(s) as some other
 * object, but a different type. An object can have any number of fragments in a
 * data-model. By definition of, no two fragment would share an object type more
 * specific than "Object". Each fragment can be interpreted as a vertical
 * decomposition of a big hypothetical object whose properties were the union of
 * a root object and all of its fragments. Terms like "first class",
 * "third-class", and "fragment" exist solely for the sake of expressing
 * patterns in object lifecycle and linkability that concisely capture the
 * requirements a modeler needs to allocate key attribute types and the required
 * set of repository operations. To illustrate this point, consider a data model
 * that includes "Employee", "Brain", and "KeyCard" as three types. Employee has
 * a unary link to one Brain and a unary link to one KeyCard, both of which are
 * used to exert a kind of ownership. By labeling Brain as a "fragment" type and
 * KeyCard as being a "third-class" type we have a rich set of information to
 * guide our ID choices. KeyCard and Brain are similar insofar as they are only
 * created to be attached to an Employee and are owned by the same Employee for
 * their entire existence. We also recognize they're dissimilar insofar as there
 * is no explicit application constructor for Brain--Brain creation is an
 * implicit side effect of creating an Employee. KeyCard, on the other hand, can
 * be created and attached to an Employee any time during an Employee's
 * lifecycle. Furthermore, while Employee's containment of a KeyCard could be
 * modeled as either mandatory or optional, the only optional for an
 * Employee-Brain association is mandatory containment. Neither KeyCard nor
 * Brain requires its own unique identifier, but KeyCard will at least require a
 * scoped discriminator if Employee-to-KeyCard is N-ary and not unary. If
 * KeyCard had been described as second-class, instead of third-class, we would
 * know there is a use case for transferring a KeyCard from one Employee to
 * another and that there is a possibility that other entities have simple
 * reference associations to the KeyCard that must be preserved. This is the
 * information we need to justify giving KeyCard a UUID of its own. When keyCard
 * is third class, the only way to move it between Employees is by changing its
 * identity. Whether or not the KeyCard is freed and reallocated or merely
 * modified, changing its identity is conceptually equivalent to destroying and
 * recreating it because no references to it based on the old identifier value
 * (the UUID of its parent and its discriminator index, if any) will be
 * effectively severed.
 * 
 * @author John
 * @param <P>
 *            The entity type of a parent object fragment where containment is
 *            exerted.
 * @param <C>
 *            The entity type of the contained third class object type.
 * @param <D>
 *            The type of the discriminator attribute used to distinguish a
 *            parent's children from one another. (This will disappear from the
 *            template signature once the implementation pattern is chosen--this
 *            is just necessary to test implementations that need to make
 *            different decisions about this type.
 */
public abstract class BaseParentChildRepository<P, C>
{
	private static final Logger LOG = LoggerFactory.getLogger(BaseParentChildRepository.class);

	@Autowired
	protected Lvl6TxManager repoTxManager;

	@Autowired
	protected DynamoDBMapper mapper;

	@Autowired
	protected DynamoDBMapperConfig mapperConfig;

	ProvisionedThroughput provisionedThroughput =
	    new ProvisionedThroughput().withReadCapacityUnits(1l)
	        .withWriteCapacityUnits(1l);

	protected final Class<P> pClass;

	protected final Class<C> cClass;

	public BaseParentChildRepository( final Class<P> pClass, final Class<C> cClass )
	{
		this.pClass = pClass;
		this.cClass = cClass;
	}

	public abstract void saveParent( P obj );

	public abstract P loadParent( String hashAttr );

	// public abstract P loadParent( String hashAttr, String rangeAttr );

	public abstract void deleteParent( P obj );

	public abstract void saveChild( String parentHashKey, C child );

	public abstract void saveChildren( String parentHashKey, Iterable<C> children );

	public abstract C loadChild( String parentHashKey, String childRangeKey );

	public abstract List<C> loadChildren( String parentHashKey, Iterable<String> childRangeKeys );

	public abstract List<C> loadAllChildren( String parentHashKey );

	/**
	 * As with Spring-Data and Hibernate, "load" implies a method that returns a
	 * fully populated object, but get methods return unloaded proxies that only
	 * have their @Id field populated. For our use case, a collection of key
	 * attribute values has the same information as a list of objects, each with
	 * just the key property set. Functionally, while loadAll() first loads the
	 * Id collection and then uses it to load each collection member, getAll()
	 * just loads the Id collection for return.
	 * 
	 * @param * @return
	 */
	public abstract List<String> getAllChildren( String parentHashKey );

	public abstract void deleteChild( String parentHashKey, C child );

	public abstract void deleteChildren( String parentHashKey, Iterable<C> children );

	/**
	 * Delete all children of the identified parent and returns them in a list
	 * with their state at time of deletion.
	 */
	public abstract List<C> deleteAllChildren( String parentHashKey );

/**
	 * Delete all children of the identified parent and returns a List of their
	 * child Id values. The similarities and differences between {@link
	 * 
	 * @link #deleteAllChildren(String)} and {@link #removeAllChildren(String)}
	 *       closely mirror those between {@link #loadAllChildren(String)} and
	 *       {@link #getAllChildren(String)}.
	 * 
	 * @param @param parentId
	 * @return
	 */
	public abstract List<String> removeAllChildren( String parentHashKey );

	// TODO: Is there any justification for paginated deletes from a parent
	// scope?

	//
	// Non-transactional methods
	//

	protected PaginatedQueryList<C> childQuery( final DynamoDBQueryExpression<C> query )
	{
		return mapper.query(cClass, query);
	}

	protected PaginatedScanList<C> childScan( final DynamoDBScanExpression scan )
	{
		return mapper.scan(cClass, scan);
	}

	//
	// DDL methods
	//
	public void createTables()
	{
		createTable(pClass);
		createTable(cClass);
	}

	private final void createTable( final Class<?> clss )
	{
		final String tableName = getTableName(clss);
		try {
			LOG.info("Creating Dynamo table {}", tableName);
			final ArrayList<AttributeDefinition> ads = new ArrayList<AttributeDefinition>();
			final ArrayList<KeySchemaElement> kse = new ArrayList<KeySchemaElement>();
			getAttributeDefinitions(clss, ads, kse);
			final CreateTableRequest request =
			    new CreateTableRequest().withTableName(tableName)
			        .withAttributeDefinitions(ads)
			        .withKeySchema(kse)
			        .withProvisionedThroughput(this.provisionedThroughput);
			if ((getGlobalIndexes() != null)
			    && !getGlobalIndexes().isEmpty()) {
				System.out.println("Adding global indices");
				request.withGlobalSecondaryIndexes(getGlobalIndexes());
			}
			if ((getLocalIndexes() != null)
			    && !getLocalIndexes().isEmpty()) {
				System.out.println("Adding local secondary indices");
				request.withLocalSecondaryIndexes(getLocalIndexes());
			}
			System.out.println(request.toString());
			this.repoTxManager.getClient()
			    .createTable(request);
		} catch (final Throwable e) {
			LOG.error("Error creating Dynamo table {}", tableName, e);
			throw e;
		}
	}

	public final void updateTables()
	{
		updateTable(pClass);
		updateTable(cClass);
	}

	private final void updateTable( final Class<?> clss )
	{
		final String tableName = getTableName(clss);
		try {
			final UpdateTableRequest updateTableRequest =
			    new UpdateTableRequest().withTableName(tableName)
			        .withProvisionedThroughput(this.provisionedThroughput);

			this.repoTxManager.getClient()
			    .updateTable(updateTableRequest);
		} catch (final Throwable e) {
			LOG.error("Error creating Dynamo table {}", tableName, e);
			throw e;
		}
	}

	public void checkTables()
	{
		checkTable(pClass);
		checkTable(cClass);
	}

	private final void checkTable( final Class<?> clss )
	{
		final String tableName = getTableName(clss);
		try {
			final DescribeTableResult result = this.repoTxManager.getClient()
			    .describeTable(tableName);
			if ((result != null)
			    && (result.getTable()
			        .getCreationDateTime() != null)) {
				LOG.info("Dynamo table {} status: {}", tableName, result.getTable()
				    .getTableStatus());
				final ProvisionedThroughputDescription prov = result.getTable()
				    .getProvisionedThroughput();
				if (prov.getReadCapacityUnits()
				    .equals(this.provisionedThroughput.getReadCapacityUnits())
				    && prov.getWriteCapacityUnits()
				        .equals(this.provisionedThroughput.getWriteCapacityUnits())) {
					LOG.info("Dynamo table {}", tableName);
				} else {
					updateTable(clss);
				}
			} else {
				createTable(clss);
			}
		} catch (final ResourceNotFoundException re) {
			createTable(clss);
		} catch (final Throwable e) {
			LOG.error("Error checking Dynamo table {}", tableName, e);
			throw e;
		}
	}

	/**
	 * Destroys all table content and re-creates it as an empty table. This
	 * method is sufficiently dangerous that it is not automatically public. To
	 * expose it, a concrete subclass must override it with a public modifier or
	 * call it from some other public API method.
	 */
	protected void emptyTables()
	{
		dropTables();
		createTables();
	}

	/**
	 * Deletes table without recreating it. This method is sufficiently
	 * dangerous that it is not automatically public. To expose it, a concrete
	 * subclass must override it with a public modifier or call it from some
	 * other public API method.
	 */
	protected void dropTables()
	{
		this.repoTxManager.getClient()
		    .deleteTable(getTableName(pClass));
		this.repoTxManager.getClient()
		    .deleteTable(getTableName(cClass));
	}

	private final String getTableName( final Class<?> clss )
	{
		String tableName;
		final DynamoDBTable tableAnnotation = clss.getAnnotation(DynamoDBTable.class);
		if ((tableAnnotation != null)
		    && StringUtils.hasText(tableAnnotation.tableName())) {
			tableName = tableAnnotation.tableName();
		} else {
			tableName = clss.getSimpleName();
		}

		final TableNameOverride nameOverride = this.mapperConfig.getTableNameOverride();
		if ((nameOverride != null)
		    && StringUtils.hasText(nameOverride.getTableNamePrefix())) {
			tableName = nameOverride.getTableNamePrefix()
			    + tableName;
		}

		return tableName;
	}

	public List<GlobalSecondaryIndex> getGlobalIndexes()
	{
		return Collections.emptyList();
	}

	public List<LocalSecondaryIndex> getLocalIndexes()
	{
		return Collections.emptyList();
	}

	public void setMapper( final DynamoDBMapper mapper )
	{
		this.mapper = mapper;
	}

	public void setMapperConfig( final DynamoDBMapperConfig mapperConfig )
	{
		this.mapperConfig = mapperConfig;
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

	private void getAttributeDefinitions( final Class<?> clss,
	    final ArrayList<AttributeDefinition> ads, final ArrayList<KeySchemaElement> kse )
	{
		final ArrayList<KeySchemaElement> rkse = new ArrayList<KeySchemaElement>(2);
		Class<?> nextFieldClass = clss;
		while (nextFieldClass != null) {
			for (final Field nextField : nextFieldClass.getDeclaredFields()) {
				if ((nextField.isAnnotationPresent(DynamoDBHashKey.class)
				    || nextField.isAnnotationPresent(DynamoDBRangeKey.class)
				    || nextField.isAnnotationPresent(DynamoDBIndexHashKey.class) || nextField.isAnnotationPresent(DynamoDBIndexRangeKey.class))
				    && (((!nextField.getGenericType()
				        .equals(Set.class)) && CLASS_TO_ATTR_TYPE.containsKey(nextField.getType())) || ((nextField.getGenericType().equals(Set.class)) && CLASS_TO_ATTR_TYPE.containsKey(nextField.getType()
				        .getTypeParameters()[0])))) {
					if (nextField.isAnnotationPresent(DynamoDBHashKey.class)) {
						ads.add(new AttributeDefinition(nextField.getName(),
						    getAttrType(nextField.getType())));
						kse.add(new KeySchemaElement(nextField.getName(), KeyType.HASH));
					} else if (nextField.isAnnotationPresent(DynamoDBRangeKey.class)) {
						ads.add(new AttributeDefinition(nextField.getName(),
						    getAttrType(nextField.getType())));
						rkse.add(new KeySchemaElement(nextField.getName(), KeyType.RANGE));
					}
				}
			}
			nextFieldClass = nextFieldClass.getSuperclass();
		}

		for (final Method nextMethod : clss.getMethods()) {
			String attrName = nextMethod.getName();
			if ((attrName.startsWith("get")
			    || attrName.startsWith("is") || nextMethod.isAnnotationPresent(DynamoDBAttribute.class))
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
				if (nextMethod.isAnnotationPresent(DynamoDBHashKey.class)) {
					ads.add(new AttributeDefinition(attrName,
					    getAttrType(nextMethod.getReturnType())));
					kse.add(new KeySchemaElement(attrName, KeyType.HASH));
				}
				if (nextMethod.isAnnotationPresent(DynamoDBRangeKey.class)) {
					ads.add(new AttributeDefinition(attrName,
					    getAttrType(nextMethod.getReturnType())));
					rkse.add(new KeySchemaElement(attrName, KeyType.RANGE));
				}
			}
		}

		// Make sure hash keys attributes precede range key attributes!
		kse.addAll(rkse);
	}

	private String getAttrType( final Class<?> attrClass )
	{
		final TypeVariable<?>[] typeParameters = attrClass.getTypeParameters();
		final String retVal;

		// The only parameterized type that can reach this code is Set<?>. All
		// others are unparameterized
		// types used as keys in CLASS_TO_ATTR_TYPE.
		if (typeParameters.length == 0) {
			retVal = CLASS_TO_ATTR_TYPE.get(attrClass);
		} else {
			retVal = "S"
			    + CLASS_TO_ATTR_TYPE.get(typeParameters[0]);
		}

		return retVal;
	}
}
