package com.lvl6.mobsters.utility.probability

import com.google.common.base.Preconditions
import com.lvl6.mobsters.utility.lambda.FloatCounter
import com.lvl6.mobsters.utility.lambda.FloatTaggedObject
import java.util.List
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ProbabilityExtensionLib implements IRandomHelper
{
	@Autowired
	@Qualifier("concurrent")
	var IRandomHelper randomSource;
	
	public def <T> List<T> selectWithoutReplacement(
		Iterable<T> source, int quantity, (T)=>float relativeFrequency)
	{
		val int size = source.size()
		if (size == quantity) {
			return source.toList
		}
		Preconditions.checkArgument(
			size > quantity,
			String.format(
				"Cannot return <%d> elements without replacement from a source collection of only <%d> candidates: <%s>",
				quantity, size, source.toList.toString
			)
		)

		val FloatCounter sumOfProbabilities = new FloatCounter()
		val List<FloatTaggedObject<T>> freqSource =
			source.map[
				val float chanceToAppear = relativeFrequency.apply(it)
				sumOfProbabilities.add(chanceToAppear)
				return new FloatTaggedObject(it, chanceToAppear)
			].toList
		
	  	//sum up chance to appear, and need to normalize all the probabilities
	  	val FloatCounter randFloat = new FloatCounter();
	  	return (0..<quantity).map[
	  		randFloat.set(
	  			randomSource.nextFloat() * sumOfProbabilities.read()
	  		)
	  		
	  		val retVal = freqSource.findFirst[
		  		var retVal = false
		  		val nextProb = it.read
		  		if (randFloat.isLessThan(nextProb)) {
		  			retVal = true;
		  		} else {
		  			randFloat.subtract(nextProb)
		  		}
	  		
		  		return retVal
		  	]
		  	
		  	// Adjust probability of retVal before returning it so it cannot be
		  	// selected again (a.k.a. 'without replacement').
		  	sumOfProbabilities.subtract(retVal.read())
		  	retVal.set(0)
		  	
		  	return retVal.getObject()
		].toList		
	}
	
		
	public def <T> T selectFirstIndependentEvent(
		Iterable<T> events, (T)=>float eventProbability)
	{
		return 
			events.findFirst [
				var retVal = false
				
				val float dieRoll = randomSource.nextFloat()
				if (dieRoll < eventProbability.apply(it)) {
					retVal = true
				}
				
				return retVal
			]
	}
	
	/**
	 * Given a floating point representation of probability (a value ranging from 0.0 inclusive
	 * to 1.0 inclusive), return true or false with true having a probability of being returned
	 * equal to the input value.
	 */
	public def boolean testProbability( float eventProbability )
	{
		Preconditions.checkArgument(
			(eventProbability >= 0) && (eventProbability <= 1),
			"Probability values must range between 0 inclusive and 1 inclusive"
		)
		
		var boolean retVal
		switch eventProbability {
			case 0.0 : retVal = false
			case 1.0 : retVal = true 
			default  : retVal = randomSource.nextFloat() < eventProbability
		}
		
		return retVal
	}
	
	/**
	 * Return a value between a minimum and a maximum value (both inclusive).
	 * 
	 * Probability distribution curve is uniform--every possible value is equally likely.
	 * 
	 * @param minValue The lowest possible return value
	 * @param maxValue The highest possible return value
	 * @throws IllegalArgumentException if minValue > maxValue
	 */
	public def int rollValueInRange( int minValue, int maxValue )
	{
		Preconditions.checkArgument(
			minValue <= maxValue, 
			"Cannot randomize a value that is >= %s and <= %s because %s < %s",
			minValue, maxValue, maxValue, minValue)
			
		if (minValue == maxValue) {
			return minValue
		}
		
		//example goal: [min,max]=[5, 10], transform range to start at 0.
    	//[min-min, max-min] = [0,max-min] = [0,10-5] = [0,5]
    	//this means there are (10-5)+1 possible numbers
    	return minValue + randomSource.nextInt(1 + maxValue - minValue)
	override nextBoolean()
	{
		return randomSource.nextBoolean()
	}
	
	override nextBytes(byte[] bytes)
	{
		Preconditions.checkNotNull(bytes)
		randomSource.nextBytes(bytes)
	
		return
	}
	
	override nextDouble()
	{
		return randomSource.nextDouble()
	}
	
	override nextFloat()
	{
		return randomSource.nextFloat()
	}
	
	override nextGaussian()
	{
		return randomSource.nextGaussian()
	}
	
	override nextInt()
	{
		return randomSource.nextInt()
	}
	
	override nextInt(int n)
	{
		return randomSource.nextInt(n)
	}
	
	override nextLong()
	{
		return randomSource.nextLong()
	}
	
	def void setRandomSource( IRandomHelper randomSource )
	{
		this.randomSource = randomSource
	}
	
}