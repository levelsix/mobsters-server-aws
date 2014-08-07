package com.lvl6.mobsters.common.utils;

import java.io.Serializable;

import com.google.common.base.Preconditions;

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
 * NOTE: Because of the lack of a no-argument constructor, this class is currently not suited for
 * being tagged as {@link Serializable}.  This can be corrected if need be--Java Serialization has 
 * back-door means of initializing immutable objects post-construction.
 * 
 * @author jheinnic
 *
 */
public final class ImmutableObjKey<T extends Comparable<? super T>> 
	extends AbstractObjComparable<T> {
	private final T id;
	
	/**
	 * Create a new ImmutableIDComparableLookupKey and set its initial life-long key matching value 
	 * to the input argument, which cannot be null.
	 * 
	 * @param initialValue
	 * @throws NullPointerException if initialValue is null.
	 * @see setOrderingId() For the method to set the current value to something else post-
	 * construction, typically to prepare for a new search with the same lookup key instance.
	 */
    public ImmutableObjKey(T initialValue) {
		Preconditions.checkNotNull(initialValue);
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
	protected T getOrderingObj() {
		// No null check because we're ignore Serializable as a non-requirement and
		// have a guard check against id ever being assigned null on construction.
		return this.id;
	}
}
