package com.lvl6.mobsters.dynamo.setup;


public interface DataServiceTxManager {
	/**
	 * Begin a new thread-scoped transaction if none is presently active.  If the current thread already has an active
	 * transaction, an IllegalStateException is thrown instead.
	 * 
	 * @return Returns if a new transaction was successfully opened and bound to the current thread.
	 * @throws IllegalStateException if an open transaction is already bound to the current thread.
	 * @throws TransactionFailureException if something unexpectedly prevents a transaction from being opened.
	 */
	public void beginTransaction();

	/**
	 * Begin a new thread-scoped transaction if none is presently active.  If the current thread already has an active
	 * transaction, nothing changes.
	 * 
	 * @return Returns if a new transaction was successfully opened and bound to the current thread.
	 * @throws IllegalStateException if an open transaction is already bound to the current thread.
	 * @throws TransactionFailureException if something unexpectedly prevents a transaction from being opened.
	 */
	public void requireTransaction();

	/**
	 * Ends an existing thread-scoped transaction if one is presently active.  If the current thread lacks an active
	 * transaction, an IllegalStateException is thrown instead.
	 * 
	 * @return Returns if an active transaction bound to the current thread was successfully committed.
	 * @throws IllegalStateException if no active transaction is bound to the current thread.
	 * @throws TransactionFailureException if something unexpectedly prevents the thread's active transaction from
	 *         being committed.
	 */
	public void commit();
	
	/**
	 * Begin a new thread-scoped transaction if none is presently active.  If the current thread already has an active
	 * transaction, an IllegalStateException is thrown instead.
	 * 
	 * @return Returns if an active transaction bound to the current thread was successfully rolled back.
	 * @throws IllegalStateException if no active transaction is bound to the current thread.
	 * @throws TransactionFailureException if something unexpectedly prevents the thread's active transaction from
	 *         being rolled back.
	 */
	public void rollback();
}
