package com.lvl6.mobsters.services.common;

import org.slf4j.Logger;

/**
 * Static utility class offering hotspot-friendly implementations of utility
 * methods for conditionally throwing Lvl6Exceptions. The exceptions generated
 * by this class cover both Preconditions (Caller's fault--bad input data) and
 * Exceptional Conditions (Blameless failure, e.g. failing a die roll) as
 * defined here:
 * https://code.google.com/p/guava-libraries/wiki/ConditionalFailuresExplained
 * 
 * All recent hotspots (as of 2009) *really* like to have the natural code...
 * 
 * if (guardExpression) { throw new BadException(messageExpression); }
 * 
 * ...refactored so that messageExpression is moved to a separate
 * String-returning method.
 * 
 * if (guardExpression) { throw new BadException(badMsg(...)); }
 * 
 * The alternative natural refactorings into void or Exception-returning methods
 * are much slower. This is a big deal - we're talking factors of 2-8 in
 * microbenchmarks, not just 10-20%. (This is a hotspot optimizer bug, which
 * should be fixed, but that's a separate, big project).
 * 
 * The coding pattern above is heavily used in java.util, e.g. in ArrayList.
 * There is a RangeCheckMicroBenchmark in the JDK that was used to test this.
 * 
 * @see com.google.common.base.Preconditions For Google's more general purpose
 *      utility class upon which this class is based.
 */
public final class Lvl6MobstersConditions {
	private Lvl6MobstersConditions() 
	{ }

	public static void lvl6Precondition(
		boolean condition,
		Lvl6MobstersStatusCode failureCode, 
		String message) 
	{
		if (!condition) {
			throw new Lvl6MobstersException(
				failureCode,
				echoString(message));
		}
	}

	public static void lvl6Precondition(
		boolean condition, 
		Lvl6MobstersStatusCode failureCode, 
		Logger log, 
		String message) 
	{
		if (!condition) {
			throw new Lvl6MobstersException(
				failureCode,
				echoAndLog(log, message));
		}
	}

	public static void lvl6Precondition(
		boolean condition, 
		Lvl6MobstersStatusCode failureCode, 
		String messageTemplate, Object... tmplArgs) 
	{
		if (!condition) {
			throw new Lvl6MobstersException(
				failureCode, 
				String.format(messageTemplate, tmplArgs));
		}
	}

	public static void lvl6Precondition(
		boolean condition, 
		Lvl6MobstersStatusCode failureCode, 
		Logger log, 
		String msgTemplate, Object... tmplArgs)
	{
		if (!condition) {
			throw new Lvl6MobstersException(
				failureCode, 
				formatAndLog(log, msgTemplate, tmplArgs));
		}
	}

	public static void throwException(
		Lvl6MobstersStatusCode failureCode, 
		String message) 
	{
		if (true) {
			throw new Lvl6MobstersException(
				failureCode,
				echoString(message));
		}
	}

	public static void throwException(
		Lvl6MobstersStatusCode failureCode, 
		Logger log, 
		String message) 
	{
		if (true) {
			throw new Lvl6MobstersException(
				failureCode, 
				echoAndLog(log, message));
		}
	}

	public static void throwException(
		Lvl6MobstersStatusCode failureCode, 
		String msgTemplate, Object... tmplArgs) 
	{
		if (true) {
			throw new Lvl6MobstersException(
				failureCode, 
				String.format(msgTemplate, tmplArgs));
		}
	}

	public static void throwException(
		Lvl6MobstersStatusCode failureCode,
		Logger log, 
		String msgTemplate, Object... tmplArgs) 
	{
		if (true) {
			throw new Lvl6MobstersException(
				failureCode, 
				formatAndLog(log, msgTemplate, tmplArgs));
		}
	}

	private static String echoString(String message) 
	{
		return message;
	}
	
	private static String echoAndLog(Logger log, String message) 
	{
		log.error(message);
		return message;
	}

	private static String formatAndLog(
		Logger log, String messageTemplate, Object... tmplArgs) 
	{
		final String message = 
			String.format(messageTemplate, tmplArgs);
		log.error(message);
		
		return message;
	}
}
