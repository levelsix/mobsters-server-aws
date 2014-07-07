package com.lvl6.mobsters.dynamo.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.Transaction.IsolationLevel;
import com.lvl6.mobsters.dynamo.setup.DataRepositoryTxManager;

/**
 * Strategy class that exercises
 * 
 * @author John
 */
@Component
public abstract class BaseDynamoCollectionRepositoryImpl<T,R> extends BaseDynamoRepositoryImpl<T> implements BaseDynamoCollectionRepository<T,R>
{
		private static Logger LOG = LoggerFactory.getLogger(BaseDynamoCollectionRepositoryImpl.class);

		@Autowired
		private DynamoDBMapper mapper;

		@Autowired
		private DataRepositoryTxManager repoTxManager;

		private final String rangeIdPropName;

		private final Object rangeIdPropType;
		private final Method rangeKeySetter;
		private final Method hashKeySetter;

		public BaseDynamoCollectionRepositoryImpl(final Class<T> clazz, final String rangeIdPropName, final Class<R> rangeIdPropType)
		{
			super(clazz);
			
			// TODO: Look the range Id property up reflectively rather than requiring the repository
			//       subtype repeat the information already present in its model.
			this.rangeIdPropName = rangeIdPropName;
			this.rangeIdPropType = rangeIdPropType;
			
			try {
				hashKeySetter = this.clazz.getMethod("setUserId", String.class);
				rangeKeySetter = this.clazz.getMethod("set" + rangeIdPropName.substring(0, 1).toUpperCase() + rangeIdPropName.substring(1), rangeIdPropType);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalArgumentException(
					"Bad range id prop name ("
				+ (!StringUtils.isEmpty(rangeIdPropName) ? rangeIdPropName: "null") 
				+ ") or type (" 
				+ (rangeIdPropType != null ? rangeIdPropType : "null")
				+ ")", e );
			}
		}
		
		@Override
		public T load( final String hashKey, final R rangeKey )
		{
			final Transaction t1 = getActiveTransaction();
			T retVal;
			if (t1 != null) {
				retVal = t1.load(
					getHashKeyRangeKeyObject(hashKey, rangeKey));
			} else {
				// Non-locking committed reads only cost more than uncommitted reads if there actually
				// is a transaction conflict.  Under an assumption of single-agent-per-user-data-set,
				// transaction conflicts cannot occur, so asking for the safeguard of a committed read is
				// a safeguard at little-to-no additional cost.
				retVal = repoTxManager.load(clazz, hashKey, rangeKey, IsolationLevel.COMMITTED);
			}

			return retVal;
		}
		
		@Override
		public List<T> loadEach( final String hashKey,
		    final Iterable<R> rangeIdList )
		{
			final Transaction t1 = getActiveTransaction();
			final ArrayList<T> retVal = new ArrayList<>();
			if (t1 != null) {
				// Use the only API available that acquires read locks in a given active transaction.
				for (final R rangeKey : rangeIdList) {
					retVal.add(
						t1.load(getHashKeyRangeKeyObject(hashKey, rangeKey))
					);	
				}
			} else {
				return unsafeLoadEach(hashKey, rangeIdList);
			}

			return retVal;
		}

		/**
		 * With this implementation, there is no way to acquire a read lock in the
		 * current transaction.  The Query API is not offered and there is no way to
		 * identify the set of records to read without it since no such list is
		 * maintained in object state.
		 */
		@Override
		public List<T> loadAll( final String hashKey )
		{
			final Transaction t1 = getActiveTransaction();
			if (t1 != null) {
				throw new IllegalStateException("Cannot loadAll() transactionally.");
			}

			final T hashKeyObj = getHashKeyObject(hashKey);
			final DynamoDBQueryExpression<T> query =
			    new DynamoDBQueryExpression<T>()
			    	.withHashKeyValues(hashKeyObj)
			        .withConsistentRead(true);
			// LOG.info("Query: {}", query.toString());
			// final PaginatedQueryList<T> retVal = query(query);
			// retVal.loadAllResults();
			// return retVal;
			return query(query);
		}

		/**
		 * TODO: Evolve this to an abstract template method that does NOT hard-bake an assumption that all range
		 * keys must be userId.  There is enough information in the DynamoEntity annotations to be able to yield
		 * a suitable implementation at compile time and/or a reflective one at runtime.
		 * 
		 * @param hashKey
		 * @return
		 */
		private T getHashKeyObject(final String hashKey) {
			T hashKeyObj;
			try {
				hashKeyObj = clazz.newInstance();
				hashKeySetter.invoke(hashKeyObj, hashKey);
			} catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
				LOG.error("Unexpected reflection error -- does " + clazz + " have a setUserId method?");
				throw new RuntimeException( e );
			}
			return hashKeyObj;
		}

		/**
		 * TODO: Evolve this to an abstract template method that does NOT hard-bake an assumption that all range
		 * keys must be userId.  There is enough information in the DynamoEntity annotations to be able to yield
		 * a suitable implementation at compile time and/or a reflective one at runtime.  Similarly, the caller
		 * should not be required to repeat the rangeIdPropertyName when instantiating a concrete subtype of this
		 * class.
		 * 
		 * @param hashKey
		 * @return
		 */
		private T getHashKeyRangeKeyObject(final String hashKey, final R rangeId) {
			T hashKeyObj;
			try {
				hashKeyObj = clazz.newInstance();
				hashKeySetter.invoke(hashKeyObj, hashKey);
				rangeKeySetter.invoke(hashKeyObj, rangeId);
			} catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
				LOG.error("Unexpected reflection error -- does " + clazz + " have a setUserId method?");
				throw new RuntimeException( e );
			}
			
			return hashKeyObj;
		}

		/**
		 * A faster variant of loadEach() that uses a Query and therefore has no read isolation safety.
		 * 
		 * @param hashKey
		 * @param rangeIdList
		 * @return
		 */
		private List<T> unsafeLoadEach( String hashKey, Iterable<R> rangeIdList) {
			final List<AttributeValue> rangeIdAttrList = new ArrayList<>();
			final T hashKeyObj = getHashKeyObject(hashKey);
			// HACK HACK!  This does not cover all supported range ID types.  A proper solution will manifest
			// when we start generating a suitable base repository class by interpreting annotation at
			// compile time and replace this single runtime reflective implementation with the result.
			if (rangeIdPropType == String.class) {
				for (final R nextRangeId : rangeIdList) {
					rangeIdAttrList.add(new AttributeValue().withS(nextRangeId.toString()));
				}
			} else {
				for (final R nextRangeId : rangeIdList) {
					rangeIdAttrList.add(new AttributeValue().withN(nextRangeId.toString()));
				}
			}

			final DynamoDBQueryExpression<T> query =
				new DynamoDBQueryExpression<T>()
					.withHashKeyValues(hashKeyObj)
					.withQueryFilterEntry(rangeIdPropName,
						new Condition().withComparisonOperator(ComparisonOperator.IN)
							.withAttributeValueList(rangeIdAttrList))
					.withConsistentRead(true);

			LOG.info("Query: {}", query);
			final PaginatedQueryList<T> objectsForHashKey = query(query);
			objectsForHashKey.loadAllResults();
			return objectsForHashKey;
		}
//		private static Function<T, String> CHILD_TO_ID_FUNCTION =
//		    new Function<T, String>() {
//			    
//			    public String apply( final T input )
//			    {
//				    return input.getId();
//			    }
//		    };

		
//		public List<String> getAll( final String hashKey )
//		{
//			return convertToIds(loadAll(hashKey));
//		}
		
		@Override
		public void remove( final String hashKey, final R rangeKey )
		{
			final Transaction t1 = getActiveTransaction();
			final T obj =
				getHashKeyRangeKeyObject(hashKey, rangeKey);
			if (t1 != null) {
				t1.delete(obj);
			} else {
				mapper.delete(obj);
			}
		}
		
		@Override
		public void removeEach( final String hashKey, final Iterable<R> rangeKeyList )
		{
			final Transaction t1 = getActiveTransaction();
			if (t1 != null) {
				for (final R nextRangeKey : rangeKeyList) {
					t1.delete(
						getHashKeyRangeKeyObject(hashKey, nextRangeKey)
					);
				}
			} else {
				// batchSave() and batchDelete() are problematic insofar as they do
				// not check optimistic lock versions!
				for (final R nextRangeKey : rangeKeyList) {
					mapper.delete(
						getHashKeyRangeKeyObject(hashKey, nextRangeKey));
				}
			}
		}
		
		@Override
		public void removeAll( final String hashKey )
		{
			final T hashKeyObj = getHashKeyObject(hashKey);
			final DynamoDBQueryExpression<T> query =
			    new DynamoDBQueryExpression<T>()
			    	.withHashKeyValues(hashKeyObj)
			        .withConsistentRead(true);
			// LOG.info("Query: {}", query.toString());
			final PaginatedQueryList<T> objList = query(query);

			final Transaction t1 = getActiveTransaction();
			if (t1 != null) {
				for (final T obj : objList) {
					t1.delete(obj);
				}
			} else {
				// batchSave() and batchDelete() are problematic insofar as they do
				// not check optimistic lock versions!
				for (final T obj : objList) {
					mapper.delete(obj);
				}
			}
		}

		@Override
		public final void setMapper( final DynamoDBMapper mapper )
		{
			this.mapper = mapper;
			super.setMapper(mapper);
		}

		@Override
		public final void setRepoTxManager( final DataRepositoryTxManager repoTxManager )
		{
			this.repoTxManager = repoTxManager;
			super.setRepoTxManager(repoTxManager);
		}
	}
