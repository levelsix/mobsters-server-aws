package com.lvl6.mobsters.dynamo.setup;

import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.Transaction.IsolationLevel;

public interface DataRepositoryTxManager
{
    /**
     * Returns the current thread's active DynamoDB transaction, if one exists, otherwise returns null.
     * 
     * @return Returns the current thread's active DynamoDB transaction, if one exists, otherwise returns
     *         null.
     */
    Transaction getActiveTransaction();

    <T> T load( Class<T> clazz, Object hashKey, IsolationLevel isolationLevel );

    <T> T load(
        Class<T> clazz,
        Object hashKey,
        boolean isStronglyConsistent,
        IsolationLevel isolationLevel );

    <T> T load( Class<T> clazz, Object hashKey, Object rangeKey, IsolationLevel isolationLevel );

    <T> T load(
        Class<T> clazz,
        Object hashKey,
        Object rangeKey,
        boolean isStronglyConsistent,
        IsolationLevel isolationLevel );

    <T> T load( T item, IsolationLevel isolationLevel );

    <T> T load( T item, boolean isStronglyConsistent, IsolationLevel isolationLevel );
}
