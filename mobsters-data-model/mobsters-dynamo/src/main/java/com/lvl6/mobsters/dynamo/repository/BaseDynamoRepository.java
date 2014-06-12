package com.lvl6.mobsters.dynamo.repository;

public interface BaseDynamoRepository<T>
{
	public void save( T obj );

	public void saveAll( Iterable<T> objs );

	public T load( String hashKey );

	public T load( String hashKey, String rangeKey );

	public void delete( T item );

	public void deleteAll( Iterable<T> objs );

	public void createTable();

	public void updateTable();

	public void checkTable();

	public String getTableName();
}