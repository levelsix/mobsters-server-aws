package com.lvl6.mobsters.utility.indexing.by_int 
import com.google.common.base.Preconditions 
import com.lvl6.mobsters.utility.indexing.by_object.AbstractObjComparable 
import com.lvl6.mobsters.utility.indexing.by_object.MutableObjKey 
/** 
 * Immutable IDComparable sub-type that encapsulates an ID of the required type and the object
 * corresponding to that key.
 * Intended for convenience re-use as a HashMap/HashKey key value for objects that either cannot
 * extend IDComparable or chose not to in order to satisfy an intentional design constraint.
 * Suited for serving as a hashing key because immutability prevents instances from losing their
 * identity after committing to their place in a hashing collection.
 * See {@link MutableObjKey} for a more garbage-friendly class in case your use case
 * is lookup up values from a IDComparable-hashed collection rather than constructing such a 
 * collection.
 * @author jheinnic
 */
final class IntKeyWrapper<V> extends AbstractIntComparable {
  val int key
  val V value

  new( V value)
  {
    Preconditions::checkNotNull(value)
    this.key=0
    this.value=value
  }
  /** 
 * Create a new ImmutableIDComparableLookupKey and set its initial life-long key matching value 
 * to the input argument, which cannot be null.
 * @param key
 * @throws NullPointerException if initialValue is null.
 * @see setOrderingId() For the method to set the current value to something else post-
 * construction, typically to prepare for a new search with the same lookup key instance.
 */
  new(  int key,   V value){
    Preconditions::checkNotNull(value)
    this.key=key
    this.value=value
  }
  /** 
 * Template method defined by IDComparable that enables any instance of IDComparable<T> to be
 * used as a lookup and/or hashing key for any other object that extends IDComparable<K> where
 * K and T are mutually Comparable (which typically means they are the same Class for all
 * practical purposes).
 * @see AbstractObjComparable#compareTo(AbstractObjComparable)
 */
  final override getOrderingInt(){
    this.key
  }
  final def getWrappedValue(){
    this.value
  }
}

