package com.lvl6.mobsters.dynamo.setup;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.transactions.ThreadLocalDynamoDBFacade;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.Transaction.IsolationLevel;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManager;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManagerDynamoDBFacade;

/**
 * As in Spring-Data repositories, Lvl6 DynamoDB repositories should not have be aware as to whether or
 * not they are running at the beginning, middle, or end of a unit of transactional work. Repositories
 * are building blocks that offer CRUD services contextually rooted at a single model class, and a unit
 * of transactional work can span both multiple methods in a single Repository as well as multiple
 * methods across multiple Repositories. Data Service components create a layer above Data Repositories
 * that coordinate series of calls across one or more Data Repository components that each represent a
 * unit of transactional work.
 *
 * This component serves as a bridge between a universe of N Data Services and M Data Repositories. It is
 * auto-wired into the abstract base class of each base repository and is meant to be auto-wired to each
 * Data Service. It provides different services to these two classes of consumers:
 *
 * For Data Services: -- Methods for explicitly beginning, committing, and rolling back a Thread-scoped
 * Transaction -- DataService control the Transaction life-cycle, but do not use or refer to Transaction
 * objects directly. -- Spring-Data provides the @Transactional annotation and an AOP bean post-processor
 * in order to weave life cycle management around the entry (begin transaction), exit (commit
 * transaction), and exception handling stages of calls into any method annotated as @Transactional.
 * We're not yet that sophisticated, hence must provide a component and make explicit calls with similar
 * semantics.
 *
 * For Data Repositories: -- The ability to test for the existence of an active transaction, enabling the
 * BaseDynamoRepository class to provide inherited support methods that branch to a suitable
 * implementation based on the presence or absence of an active transaction. -- It becomes the data
 * service consumer's responsibility to use repository classes outside of a transaction with an awareness
 * that DynamoDb operations are not inherently ACID compliant without use of the Transaction library.
 * Natively, DynamoDB ensures only Consistency and Durability, omitting both Atomicity and Isolation.
 *
 * @author jheinnic
 *
 */
public class Lvl6TxManager extends TransactionManager
    implements
        DataServiceTxManager,
        DataRepositoryTxManager
{
    private static final Logger LOG = LoggerFactory.getLogger(Lvl6TxManager.class);

    static final String TX_TABLE_NAME = "Transactions";

    static final String TX_IMAGES_TABLE_NAME = "TransactionImages";

    private static final long ROLLBACK_WINDOW = 0;

    private static final long DELETE_WINDOW = 0;

    private final ThreadLocal<Lvl6Transaction> threadActiveTx =
        new ThreadLocal<Lvl6Transaction>() {
            @Override
            protected Lvl6Transaction initialValue()
            {
                return null;
            }
        };

    public Lvl6TxManager(
        final AmazonDynamoDBClient client,
        final DynamoDBMapperConfig mapperConfig )
    {
        super(
            client,
            Lvl6TxManager.TX_TABLE_NAME,
            Lvl6TxManager.TX_IMAGES_TABLE_NAME,
            mapperConfig);
    }

    // @Autowired
    // private AmazonDynamoDBClient client;

    // @Autowired
    // private DynamoDBRequestFactory requestFactory;

    // private TransactionManager txManager;

    /**
     * @param isolationLevel
     *        The isolation level to use; this has the same meaning as for
     *        {@link TransactionManager#getItem(GetItemRequest, IsolationLevel)} .
     * @return An instance of the item class with all attributes populated from the table, or null if the
     *         item does not exist.
     */
    @Override
    public <T> T load( final T item, final IsolationLevel isolationLevel )
    {
        return this.load(item, isolationLevel);
    }

    /**
     * Loads an object with the hash key given outside a transaction, using the default configuration.
     *
     * @param isolationLevel
     *        The isolation level to use; this has the same meaning as for
     *        {@link TransactionManager#getItem(GetItemRequest, IsolationLevel)}
     * @return An instance of the item class with all attributes populated from the table, or null if the
     *         item does not exist.
     * @see DynamoDBMapper#load(Class, Object)
     */
    @Override
    public <T extends Object> T load(
        final Class<T> clazz,
        final Object hashKey,
        final IsolationLevel isolationLevel )
    {
        try {
            getFacadeProxy().setBackend(
                new TransactionManagerDynamoDBFacade(
                    this,
                    isolationLevel));
            return getClientMapper().load(
                clazz,
                hashKey);
        } finally {
            getFacadeProxy().setBackend(
                null);
        }
    }

    /**
     * Loads an object with a hash and range key outside a transaction, using the default configuration.
     *
     * @param isolationLevel
     *        The isolation level to use; this has the same meaning as for
     *        {@link TransactionManager#getItem(GetItemRequest, IsolationLevel)}
     * @return An instance of the item class with all attributes populated from the table, or null if the
     *         item does not exist.
     * @see DynamoDBMapper#load(Class, Object, Object)
     */
    @Override
    public <T extends Object> T load(
        final Class<T> clazz,
        final Object hashKey,
        final Object rangeKey,
        final IsolationLevel isolationLevel )
    {
        try {
            getFacadeProxy().setBackend(
                new TransactionManagerDynamoDBFacade(
                    this,
                    isolationLevel));
            return getClientMapper().load(
                clazz,
                hashKey,
                rangeKey);
        } finally {
            getFacadeProxy().setBackend(
                null);
        }
    }

    //
    // Session context style elements from DataRepositoryTxManager
    //

    @Override
    public Lvl6Transaction getActiveTransaction()
    {
        return threadActiveTx.get();
    }

    //
    // Overridden method from base DynamoDbTxManager class to ensure load() enhancements are also
    // available within a Transaction
    //

    @Override
    public Lvl6Transaction newTransaction()
    {
        final Lvl6Transaction transaction = new Lvl6Transaction(
            UUID.randomUUID()
            .toString(),
            this,
            true);
        Lvl6TxManager.LOG.info("Started transaction " + transaction.getId());
        return transaction;
    }

    //
    // Session context manager mode methods for DataServiceTxManager
    //

    @Override
    public void beginTransaction()
    {
        if (threadActiveTx.get() != null) { 
        	throw new IllegalStateException(); 
        }

        doBeginTransaction();
    }

    @Override
    public boolean requireTransaction()
    {
    	boolean retVal = false;
        if (threadActiveTx.get() == null) {
        	doBeginTransaction();
        	retVal = true;
        }
        
        return retVal;
    }
        
    private void doBeginTransaction() {
        final Lvl6Transaction newTx;
        try {
            newTx = newTransaction();
        } catch (final Throwable t) {
            Lvl6TxManager.LOG.error(
                "Failed to create new transaction!",
                t);
            throw new TransactionFailureException(
                t);
        }

        try {
            threadActiveTx.set(newTx);
        } catch (final Throwable t) {
            try {
                newTx.rollback();
            } catch (final Throwable t2) {
                LOG.error(
                    "Failed to retain new transaction with ThreadLocal, then failed to roll it back!  Original exception: ",
                    t);
                LOG.error(
                    "Failed to retain new transaction with ThreadLocal, then failed to roll it back!  Rollback exception: ",
                    t2);
            }
            throw new TransactionFailureException(t);
        }
    }

    @Override
    protected ThreadLocalDynamoDBFacade getFacadeProxy()
    {
        return super.getFacadeProxy();
    }

    @Override
    public void commit()
    {
        final Transaction activeTx = threadActiveTx.get();
        if (activeTx == null) { throw new IllegalStateException(); }

        try {
            activeTx.commit();
        } catch (final Throwable t) {
            try {
                activeTx.rollback();
            } catch (final Throwable t2) {
                //
            }
            throw new TransactionFailureException(
                t);
        }
        try {
            activeTx.commit();
        } catch (final Throwable t) {
            try {
                activeTx.rollback();
            } catch (final Throwable t2) {
                Lvl6TxManager.LOG.error(
                    "Could neither commit nor rollback transaction.  Dumping it!  Commit exception: ",
                    t);
                Lvl6TxManager.LOG.error(
                    "Could neither commit nor rollback transaction.  Dumping it!  Rollback exception: ",
                    t2);
                activeTx.sweep(
                    Lvl6TxManager.ROLLBACK_WINDOW,
                    Lvl6TxManager.DELETE_WINDOW);
            }
            throw new TransactionFailureException(
                t);
        }

        try {
            threadActiveTx.set(null);
        } catch (final Throwable t) {
            Lvl6TxManager.LOG.error(
                "Could not release ThreadLocal reference to committed transaction!  This thread may have issues with its next transaction...",
                t);
        }
    }

    @Override
    public void rollback()
    {
        final Transaction activeTx = threadActiveTx.get();
        if (activeTx == null) { throw new IllegalStateException(); }

        try {
            activeTx.rollback();
            threadActiveTx.set(null);
        } catch (final Throwable t) {
            throw new TransactionFailureException(
                t);
        }

    }

}
