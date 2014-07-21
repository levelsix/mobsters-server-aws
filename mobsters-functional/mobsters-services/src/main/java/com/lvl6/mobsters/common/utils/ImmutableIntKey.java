package com.lvl6.mobsters.common.utils;


/**
 * Immutable IDComparable sub-type that encapsulates only an ID of the required type.
 * 
 * Intended for convenience re-use as a HashMap/HashKey key value for objects that either cannot
 * extend IDComparable or chose not to in order to satisfy an intentional design constraint.
 * Suited for serving as a hashing key because immutability prevents instances from losing their
 * identity after committing to their place in a hashing collection.
 * 
 * See {@link MutableObjKey} for a more garbage-friendly class in case your use case
 * is lookup up values from a IDComparable-hashed collection rather than constructing such a 
 * collection.
 * 
 * @author jheinnic
 *
 */
public final class ImmutableIntKey extends AbstractIntComparable {
	private final int id;
	
	public ImmutableIntKey() 
	{
		this.id = 0;
	}
	
	/**
	 * Create a new ImmutableIDComparableLookupKey and set its initial life-long key matching value 
	 * to the input argument, which cannot be null.
	 * 
	 * @param initialValue
	 * @throws NullPointerException if initialValue is null.
	 * @see setOrderingId() For the method to set the current value to something else post-
	 * construction, typically to prepare for a new search with the same lookup key instance.
	 */
    public ImmutableIntKey(int initialValue) {
		this.id = initialValue;
    }

    /**
     * Template method defined by IDComparable that enables any instance of IDComparable<T> to be
     * used as a lookup and/or hashing key for any other object that extends IDComparable<K> where
     * K and T are mutually Comparable (which typically means they are the same Class for all
     * practical purposes).
     * 
     * @see AbstractObjComparable#compareTo(AbstractObjComparable)
     */
	@Override
	protected int getOrderingInt() {
		return this.id;
	}
}
