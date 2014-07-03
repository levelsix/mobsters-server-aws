package com.lvl6.mobsters.dynamo.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.Transaction.IsolationLevel;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.lvl6.mobsters.dynamo.setup.DataRepositoryTxManager;

public abstract class BaseDynamoItemRepositoryImpl<T> extends BaseDynamoRepositoryImpl<T> implements BaseDynamoItemRepository<T>
{
	private static final Logger LOG = LoggerFactory.getLogger(BaseDynamoItemRepositoryImpl.class);

	@Autowired
	private DynamoDBMapper mapper;

	@Autowired
	private DataRepositoryTxManager repoTxManager;

	protected BaseDynamoItemRepositoryImpl( final Class<T> clazz )
	{
		super(clazz);
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#load(java.lang.String)
	 */
	@Override
	public final T load( final String hashKey )
	{
		final Transaction t1 = getActiveTransaction();
		final T retVal;
		if (t1 != null) {
			retVal = 
				t1.load(
					getHashKeyObject(hashKey));
		} else {
			// Non-locking committed reads only cost more than uncommitted reads if there actually
			// is a transaction conflict.  Under an assumption of single-agent-per-user-data-set,
			// transaction conflicts cannot occur, so asking for the safeguard of a committed read is
			// a safeguard at little-to-no additional cost.
			retVal = repoTxManager.load(clazz, hashKey, IsolationLevel.COMMITTED);
		}

		return retVal;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#load(java.lang.String)
	 */
	@Override
	public final List<T> loadEach( final Iterable<String> hashKeyList )
	{
		final Transaction t1 = getActiveTransaction();
		final List<T> retVal;
		if (t1 != null) {
			retVal = 
				FluentIterable
					.from(hashKeyList)
					.transform(new Function<String,T>() {
						@Override
						public T apply(String inputHashKey) {
							return getHashKeyObject(inputHashKey);
						}
					})
					.toImmutableList();
		} else {
			@SuppressWarnings("unchecked")
			final List<T> temp =
				(List<T>) mapper.batchLoad(
					FluentIterable
						.from(hashKeyList)
						.transform(new Function<String, Object>() {
							@Override
							public T apply(String inputHashKey) {
								return getHashKeyObject(inputHashKey);
							}
						})
						.toImmutableList()
					).get(
						getTableName()
					);
			retVal = temp;
		}

		return retVal;
	}

	// public Map<String, List<Object>> loadAll( List<KeyPair>
	// hashAndRangeKeyPairs ) {
	// Map<Class<T>, List<KeyPair>> clazzToHashAndRangeKeyPairs =
	// new HashMap<Class<T>, List<KeyPair>>();
	// clazzToHashAndRangeKeyPairs.put(clazz, hashAndRangeKeyPairs);
	// // mapper.batchLoad(clazzToHashAndRangeKeyPairs); // doesn't compile
	//
	// Map<Class<?>, List<KeyPair>> foo = new HashMap<Class<?>,
	// List<KeyPair>>();
	// foo.putAll(clazzToHashAndRangeKeyPairs);
	//
	// // caller will do conversion to T manually
	// return mapper.batchLoad(foo); // compiles
	// }

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#delete(T)
	 */
	@Override
	public void remove( final String hashKey )
	{
		final Transaction t1 = getActiveTransaction();
		if (t1 != null) {
			t1.delete(
				getHashKeyObject(hashKey));
		} else {
			mapper.delete(
				getHashKeyObject(hashKey));
		}
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository#deleteAll(java.lang.Iterable)
	 */
	public final void removeEach( final Iterable<String> hashKeyList )
	{
		final Transaction t1 = getActiveTransaction();
		if (t1 != null) {
			// DynamoDB transaction library has no bulk operations...
			for (final String hashKey : hashKeyList) {
				t1.delete(
					getHashKeyObject(hashKey));
			}
		} else {
			// batchSave() and batchDelete() are problematic insofar as they do
			// not check optimistic lock versions!
			for (final String hashKey : hashKeyList) {
				mapper.delete(
					getHashKeyObject(hashKey));
			}
		}
	}

	/**
	 * TODO: Evolve this to an abstract template method that does NOT hard-bake an assumption that all range
	 * keys must be userId.  There is enough information in the DynamoEntity annotations to be able to yield
	 * a suitable implementation at compile time and/or a reflective one at runtime.
	 * 
	 * @param hashKey
	 * @return
	 */
	protected abstract T getHashKeyObject(final String hashKey);
	/*{
		final T hashKeyObj;
		try {
			hashKeyObj = clazz.newInstance();
			final Method hashKeySetter = clazz.getMethod("setUserId", String.class);
			hashKeySetter.invoke(hashKey);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			LOG.error("Unexpected reflection error -- does " + clazz + " have a setUserId method?");
			throw new RuntimeException( e );
		}
		return hashKeyObj;
	}*/
	
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
