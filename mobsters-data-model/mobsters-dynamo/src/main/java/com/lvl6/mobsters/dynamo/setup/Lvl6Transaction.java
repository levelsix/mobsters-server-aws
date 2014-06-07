package com.lvl6.mobsters.dynamo.setup;

import java.util.concurrent.Callable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.TransactionDynamoDBFacade;

/**
 * This subtype exist to provide missing implementations of the DynamoDbMapper class' two load(Class &
 * Key) methods that were left unimplemented in the transaction library.
 *
 * @author jheinnic
 *
 */
public class Lvl6Transaction extends Transaction
{

    private final Lvl6TxManager lvl6TxManager;

    public Lvl6Transaction(
        final String string,
        final Lvl6TxManager txManager,
        final boolean insert )
    {
        super(
            string,
            txManager,
            insert);
        lvl6TxManager = txManager;
    }

    @Override
    public <T> T load( final T keyObject )
    {
        return super.load(keyObject);
    }

    /**
     * Loads an object with the hash key given, using the default configuration.
     *
     * @see DynamoDBMapper#load(Class, Object)
     */
    public <T extends Object> T load( final Class<T> clazz, final Object hashKey )
    {
        return doWithMapper(new Callable<T>() {
            @Override
            public T call() throws Exception
            {
                return lvl6TxManager.getClientMapper()
                    .load(
                        clazz,
                        hashKey);
            }
        });
    }

    /**
     * Loads an object with a hash and range key.
     *
     * @see DynamoDBMapper#load(Class, Object, Object)
     */
    public <T extends Object> T load(
        final Class<T> clazz,
        final Object hashKey,
        final Object rangeKey )
    {
        return doWithMapper(new Callable<T>() {
            @Override
            public T call() throws Exception
            {
                return lvl6TxManager.getClientMapper()
                    .load(
                        clazz,
                        hashKey,
                        rangeKey);
            }
        });
    }

    private <T> T doWithMapper( final Callable<T> callable )
    {
        try {
            lvl6TxManager.getFacadeProxy()
                .setBackend(
                    new TransactionDynamoDBFacade(
                        this,
                        lvl6TxManager));
            return callable.call();
        } catch (final RuntimeException e) {
            // have to do this here in order to avoid having to declare a checked exception type
            throw e;
        } catch (final Exception e) {
            // none of the callers of this method need to throw a checked exception
            throw new RuntimeException(
                e);
        } finally {
            lvl6TxManager.getFacadeProxy()
                .setBackend(
                    null);
        }
    }

}
