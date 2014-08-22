package com.lvl6.mobsters.utility.indexing.by_int

import java.util.HashMap
import java.util.Set

abstract class IntKeyIndex<T> {
	protected val HashMap<AbstractIntComparable, T> structureContextLookup

	package new() {
		this.structureContextLookup = new HashMap<AbstractIntComparable, T>()
	}
	
	public static def <T> IntKeyIndex<T> createIndex((T)=>int valueToInt) {
		return new LambdaIntKeyIndex<T>(valueToInt)
	}
	
	public static def <T extends AbstractIntComparable> IntKeyIndex<T> createIndex() {
		return new SelfIntKeyIndex<T>()
	}

	def T get(int key) {
		return structureContextLookup.get(Integer.valueOf(key))
	}

	def T get(AbstractIntComparable key) {
		return structureContextLookup.get(key)
	}

	def T get(Integer key) {
		return structureContextLookup.get(key)
	}

    // TODO: Need to provide immutable access to the full set without copying on every request!
	def Set<T> values() {
		return structureContextLookup.values().toSet
	}

	abstract def T put(T metadata)
	
}
    
package final class LambdaIntKeyIndex<T> extends IntKeyIndex<T> 
{
	private val (T)=>int valueToInt

	package new((T)=>int valueToInt) {
		super()
		this.valueToInt = valueToInt;
	}

	override T put(T metadata) {
		return structureContextLookup.put(
			new ImmutableIntKey(
				valueToInt.apply(metadata)
			), metadata
		)
	}
}
    
package final class SelfIntKeyIndex<T extends AbstractIntComparable> extends IntKeyIndex<T>
{
	package new() {
		super()
	}

	override T put(T metadata)
	{
		return structureContextLookup.put(metadata, metadata);
	}
}