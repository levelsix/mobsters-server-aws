package com.lvl6.mobsters.utility.indexing.by_int

import java.util.HashMap

class IntKeyIndex<T> {
	val HashMap<AbstractIntComparable, IntKeyWrapper<T>> structureContextLookup

	new((T)=>int valueToKey) {
		this.structureContextLookup = new HashMap<AbstractIntComparable, IntKeyWrapper<T>>()















	}

	def get(int key) {
		get(new ImmutableIntKey(key))
	}

	def get(AbstractIntComparable key) {
		val wrapper = structureContextLookup.get(key)
		var T retVal = null
		if (wrapper !== null) {
			retVal = wrapper.wrappedValue
		}
		retVal
	}

	def put(T metadata) {
		var wrapper = new IntKeyWrapper<T>(metadata)
		structureContextLookup.put(wrapper, wrapper)
	}
}
