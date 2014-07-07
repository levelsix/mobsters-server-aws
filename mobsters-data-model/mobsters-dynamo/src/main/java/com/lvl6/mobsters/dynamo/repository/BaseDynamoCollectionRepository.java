package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

public interface BaseDynamoCollectionRepository<T, R> extends BaseDynamoRepository<T> {
	public T load(final String hashKey, final R rangeKey);
	
	public List<T> loadEach(final String hashKey, final Iterable<R> rangeKeyList);
	
	public List<T> loadAll(final String hashKey);
	
	public void remove(final String hashKey, final R rangeKey);
	
	public void removeEach(final String hashKey, final Iterable<R> rangeKeyList);
	
	public void removeAll(final String hashKey);
}
