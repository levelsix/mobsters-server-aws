package com.lvl6.mobsters.dynamo.repository;



public interface BaseDynamoRepository<T> {
	public abstract void save(T obj);

	public abstract void saveEach(Iterable<T> itemList);
	
	public abstract void delete(T obj);

	public abstract void deleteEach(Iterable<T> itemList);
}