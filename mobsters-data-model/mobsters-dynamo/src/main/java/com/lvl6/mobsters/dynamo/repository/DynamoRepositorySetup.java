package com.lvl6.mobsters.dynamo.repository;



public interface DynamoRepositorySetup {
	public void createTable();

	public void updateTable();

	public void checkTable();

	public void emptyTable();
	
	public void dropTable();
	
	public String getTableName();
}