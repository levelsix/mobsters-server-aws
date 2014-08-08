package com.lvl6.mobsters.utility.indexing.by_object

import com.google.common.base.Preconditions
import java.util.HashMap

class ObjKeyIndex<K extends Comparable<K>, V>
{
	val HashMap<AbstractObjComparable<K>, ObjKeyWrapper<K, V>> lookupMap
	val (V)=>K valueToKey

	new( (V)=>K valueToKey ) {
		Preconditions::checkNotNull(valueToKey)
		lookupMap = new HashMap<AbstractObjComparable<K>, ObjKeyWrapper<K, V>>()
		this.valueToKey = valueToKey
	}
	
	def V get(K key)
	{
		Preconditions::checkNotNull(key)
		return get(key)
	}

	def V get(AbstractObjComparable<K> key)
	{
		Preconditions::checkNotNull(key)
		val wrapper = lookupMap.get(key)
		
		var V retVal = null
		if (wrapper !== null) {
			retVal = wrapper.wrappedValue
		}
		return retVal
	}

	def void put(V value)
	{
		Preconditions::checkNotNull(value)
		val K key = valueToKey.apply(value)
		Preconditions::checkNotNull(key)
		
		var wrapper = new ObjKeyWrapper<K, V>(value, valueToKey)
		lookupMap.put(wrapper, wrapper)
	}
}
