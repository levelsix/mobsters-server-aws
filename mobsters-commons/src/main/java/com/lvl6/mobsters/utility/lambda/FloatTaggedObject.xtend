package com.lvl6.mobsters.utility.lambda

/** 
 * Utility class that pairs an object reference with a floating point counter
 * value.
 * 
 * @author jheinnic
 * @param<T> The type of object to tag.
 */
public class FloatTaggedObject<T> extends FloatCounter
{
	val T obj

	new(T obj)
	{
		super();
		this.obj = obj
	}

	new(T obj, float value)
	{
		super(value);
		this.obj = obj
	}
	
	new(T obj, (T)=>float valueLambda )
	{
		super(valueLambda.apply(obj))
		this.obj = obj
	}

	def T getObject()
	{
		return obj
	}
}
