package com.lvl6.mobsters.utility.indexing.by_int

import java.util.HashMap

class IntKeyIndex<T> {
	val HashMap<ImmutableIntKey, T> structureContextLookup
	val (T)=>int valueToKey

	new((T)=>int valueToKey) {
		this.structureContextLookup = new HashMap<ImmutableIntKey, T>()
		this.valueToKey = valueToKey
	}

	def T get(int key) {
		return get(new ImmutableIntKey(key))
	}

	def T get(AbstractIntComparable key) {
		return structureContextLookup.get(key)
	}

	def T get(Integer key) {
		return structureContextLookup.get(key)
	}

	def T put(T metadata) {
		return structureContextLookup.put(
			new ImmutableIntKey(
				valueToKey.apply(metadata)
			), metadata
		)
	}
}
 