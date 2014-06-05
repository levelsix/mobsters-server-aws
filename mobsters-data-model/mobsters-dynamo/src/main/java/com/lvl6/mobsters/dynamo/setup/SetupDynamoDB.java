package com.lvl6.mobsters.dynamo.setup;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManager;
import com.amazonaws.services.dynamodbv2.util.TableHelper;
import com.lvl6.mobsters.dynamo.repository.BaseDynamoRepository;

public class SetupDynamoDB implements ApplicationContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(SetupDynamoDB.class);

    protected ApplicationContext context;

    protected boolean checkTables = false;

    @Autowired
    AmazonDynamoDB dynamoClient;

    @PostConstruct
    public void setupDynamoDB() throws InterruptedException
    {
        checkDynamoTables();
        checkTxManagerTables();
    }

    public void checkDynamoTables()
    {
        @SuppressWarnings("rawtypes")
        final Map<String, BaseDynamoRepository> dynamoRepos =
            context.getBeansOfType(BaseDynamoRepository.class);
        SetupDynamoDB.LOG.info("Checking tables");
        for (final BaseDynamoRepository<?> repo : dynamoRepos.values()) {
            repo.checkTable();
        }
    }

    public void checkTxManagerTables() throws InterruptedException
    {
        // 1. Verify that the transaction table exists, or create it if it doesn't exist
        SetupDynamoDB.LOG.info("Verifying or creating table " + Lvl6TxManager.TX_TABLE_NAME);
        TransactionManager.verifyOrCreateTransactionTable(
            dynamoClient,
            Lvl6TxManager.TX_TABLE_NAME,
            1,
            1,
            null);

        // 2. Verify that the transaction item images table exists, or create it otherwise
        SetupDynamoDB.LOG.info("Verifying or creating table " +
            Lvl6TxManager.TX_IMAGES_TABLE_NAME);
        TransactionManager.verifyOrCreateTransactionImagesTable(
            dynamoClient,
            Lvl6TxManager.TX_IMAGES_TABLE_NAME,
            1,
            1,
            null);

        // 3. Wait for tables to be created
        final TableHelper tableHelper = new TableHelper(
            dynamoClient);
        SetupDynamoDB.LOG.info("Waiting for table to become ACTIVE: " +
            Lvl6TxManager.TX_TABLE_NAME);
        tableHelper.waitForTableActive(
            Lvl6TxManager.TX_TABLE_NAME,
            5 * 60L);
        SetupDynamoDB.LOG.info("Waiting for table to become ACTIVE: " +
            Lvl6TxManager.TX_IMAGES_TABLE_NAME);
        tableHelper.waitForTableActive(
            Lvl6TxManager.TX_IMAGES_TABLE_NAME,
            5 * 60L);

        /*
         * txManager = new TransactionManager( client, Lvl6TxManager.TX_TABLE_NAME,
         * Lvl6TxManager.TX_IMAGES_TABLE_NAME);
         */
    }

    @Override
    public void setApplicationContext( final ApplicationContext ctx ) throws BeansException
    {
        context = ctx;
    }

    public boolean isCheckTables()
    {
        return checkTables;
    }

    public void setCheckTables( final boolean checkTables )
    {
        this.checkTables = checkTables;
    }

}
