package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

public interface BaseDynamoItemRepository<T> extends BaseDynamoRepository<T> 
{
	public T load( String hashKey );
	
	public List<T> loadEach(Iterable<String> hashKeyList);

	public void remove( String hashKey );

	public void removeEach( Iterable<String> hashKeyList );
}