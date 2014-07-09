package com.lvl6.mobsters.dynamo.repository.filter;

public interface Director<T> {
    public void apply( T builder );
}
