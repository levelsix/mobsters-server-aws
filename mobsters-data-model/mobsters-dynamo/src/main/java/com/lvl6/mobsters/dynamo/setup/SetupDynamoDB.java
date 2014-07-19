package com.lvl6.mobsters.dynamo.setup;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManager;
import com.amazonaws.services.dynamodbv2.util.TableHelper;
import com.lvl6.mobsters.dynamo.repository.DynamoRepositorySetup;

@Component
public class SetupDynamoDB {
	private static final Logger LOG = LoggerFactory
			.getLogger(SetupDynamoDB.class);

	protected ApplicationContext context;

	protected boolean checkTables = false;

	@Autowired
	AmazonDynamoDB dynamoClient;
	
	@Autowired
	List<DynamoRepositorySetup> dynamoRepos;

	@PostConstruct
	public void setupDynamoDB() throws InterruptedException {
		checkDynamoTables();
		checkTxManagerTables();
	}

	public void checkDynamoTables() {
		SetupDynamoDB.LOG.info("Checking repository tables");
		for (final DynamoRepositorySetup repo : dynamoRepos) {
			repo.checkTable();
		}
	}

	public void checkTxManagerTables() throws InterruptedException {
		// 1. Verify that the transaction table exists, or create it if it
		// doesn't exist
		SetupDynamoDB.LOG.info("Verifying or creating table "
				+ Lvl6TxManager.TX_TABLE_NAME);
		TransactionManager.verifyOrCreateTransactionTable(dynamoClient,
				Lvl6TxManager.TX_TABLE_NAME, 1, 1, null);

		// 2. Verify that the transaction item images table exists, or create it
		// otherwise
		SetupDynamoDB.LOG.info("Verifying or creating table "
				+ Lvl6TxManager.TX_IMAGES_TABLE_NAME);
		TransactionManager.verifyOrCreateTransactionImagesTable(dynamoClient,
				Lvl6TxManager.TX_IMAGES_TABLE_NAME, 1, 1, null);

		// 3. Wait for tables to be created
		final TableHelper tableHelper = new TableHelper(dynamoClient);
		SetupDynamoDB.LOG.info("Waiting for table to become ACTIVE: "
				+ Lvl6TxManager.TX_TABLE_NAME);
		tableHelper.waitForTableActive(Lvl6TxManager.TX_TABLE_NAME, 5 * 60L);
		SetupDynamoDB.LOG.info("Waiting for table to become ACTIVE: "
				+ Lvl6TxManager.TX_IMAGES_TABLE_NAME);
		tableHelper.waitForTableActive(Lvl6TxManager.TX_IMAGES_TABLE_NAME,
				5 * 60L);
	}

	public boolean isCheckTables() {
		return checkTables;
	}

	public void setCheckTables(final boolean checkTables) {
		this.checkTables = checkTables;
	}
	
	public void setDynamoClient(final AmazonDynamoDB dynamoClient) {
		this.dynamoClient = dynamoClient;
	}
}
