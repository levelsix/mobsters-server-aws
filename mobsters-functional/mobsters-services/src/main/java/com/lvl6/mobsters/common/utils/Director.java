package com.lvl6.mobsters.common.utils;

/**
 * Templated Director interface for use driving Builders from argument lambdas when applying the Gang of Four
 * "Builder" design pattern to service invocation.
 * 
 * @author jheinnic
 *
 * @param <T>
 */
public interface Director<T> {
    public void apply( T builder );
}
