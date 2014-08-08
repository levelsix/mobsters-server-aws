package com.lvl6.mobsters.utility.indexing.by_int;

import com.google.common.base.Preconditions;
import com.lvl6.mobsters.utility.indexing.by_object.AbstractObjComparable;

/**
 * Mutable IDComparable subtype that encapsulates only an ID of the required type.
 * 
 * Intended for batch lookup operations, where the mutability of the id flag enables the object's reuse
 * without undue strain on garbage collection.  Suited for looking up instances of any concrete 
 * IDComparable subtype, including those with more sophisticated implementations (e.g. Static 
 * Adapter Context Attachments), provided their IDComparable<K> key type is identical.  
 * 
 * See {@link StaticStructureContext} and {@link StructureExtensionLib#doInitExtension()}, and
 * {@link StructureExtensionLib#getStructure(com.lvl6.mobsters.dynamo.StructureForUser)} for
 * an illustration of its use in practice.
 * 
 * NOTE: This class, by nature of its by-design mutability, is NOT suitable for use as the Key 
 * object of a HashMap or HashSet collection!  For those use cases with objects that cannot inherit
 * from IDComparable, look to {@link ImmutableIDComparableHashKey}.
 * 
 * @author jheinnic
 *
 */
public final class MutableIntKey<T extends Comparable<? super T>> extends AbstractObjComparable<T> {
	private T id;
	
	public MutableIntKey() {
		// id begins with a default value.
	}
	
	/**
	 * Create a new MutableIDComparableLookupKey and set its initial id value to the input argument,
	 * which cannot be null.
	 * 
	 * @param initialValue
	 * @throws NullPointerException if initialValue is null.
	 * @see setOrderingId() For the method to set the current value to something else post-
	 * construction, typically to prepare for a new search with the same lookup key instance.
	 */
    public MutableIntKey(T initialValue) {
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
		return this.id;
	}
	
	/**
	 * Change the lookup key value.  Must not be null, else a NullPointerException will occur.
	 * 
	 * @param id
	 * @throws NullPointerException if initialValue is null.
	 */
	public void setOrderingId(T id) {
		Preconditions.checkNotNull(id);
		this.id = id;
	}
}
