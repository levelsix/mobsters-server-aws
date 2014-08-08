package com.lvl6.mobsters.utility.indexing.by_object

import com.google.common.base.Preconditions
import java.io.Serializable

/** 
 * Immutable IDComparable sub-type that encapsulates only an ID of the required type.
 * Intended for convenience re-use as a HashMap/HashKey key value for objects that either cannot
 * extend IDComparable or chose not to in order to satisfy an intentional design constraint.
 * Suited for serving as a hashing key because immutability prevents instances from losing their
 * identity after committing to their place in a hashing collection.
 * See {@link MutableObjKey} for a more garbage-friendly class in case your use case
 * is lookup up values from a IDComparable-hashed collection rather than constructing such a 
 * collection.
 * NOTE: Because of the lack of a no-argument constructor, this class is currently not suited for
 * being tagged as {@link Serializable}.  This can be corrected if need be--Java Serialization has 
 * back-door means of initializing immutable objects post-construction.
 * @author jheinnic
 */
final class ObjKeyWrapper<K extends Comparable<? super K>, V> extends AbstractObjComparable<K>
{
	val (V)=>K valueToKey
	val V value

	/** 
 * Create a new ImmutableIDComparableLookupKey and set its initial life-long key matching value 
 * to the input argument, which cannot be null.
 * @param key
 * @throws NullPointerException if initialValue is null.
 * @see setOrderingId() For the method to set the current value to something else post-
 * construction, typically to prepare for a new search with the same lookup key instance.
 */
	new(V value, (V)=>K valueToKey)
	{
		Preconditions::checkNotNull(valueToKey)
		Preconditions::checkNotNull(value)
		this.valueToKey = valueToKey
		this.value = value
	}

	/** 
	 * Template method defined by IDComparable that enables any instance of IDComparable<T> to be
	 * used as a lookup and/or hashing key for any other object that extends IDComparable<K> where
	 * K and T are mutually Comparable (which typically means they are the same Class for all
	 * practical purposes).
	 * @see AbstractObjComparable#compareTo(AbstractObjComparable)
	 */
	override K getOrderingObj()
	{
		return this.valueToKey.apply(this.value)
	}

	def V getWrappedValue()
	{
		return this.value
	}
}
