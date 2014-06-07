package com.lvl6.mobsters.dynamo.setup;

public class TransactionFailureException extends RuntimeException {

    private static final long serialVersionUID = 1759944532617418614L;

    public TransactionFailureException( final Throwable t ) {
        super(t);
    }
}
