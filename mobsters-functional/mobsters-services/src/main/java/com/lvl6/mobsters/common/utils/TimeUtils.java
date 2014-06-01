package com.lvl6.mobsters.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TimeUtils {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TimeUtils.class);
	
	private TimeUtils() {
		throw new AssertionError("TimeUtils is a static utility class");
	}

    public static Date createNow() {
    	return new Date();
    }
    
    public static Calendar createToday() {
    	return new GregorianCalendar();
    }
    
    public static long currentTimeMillis() {
    	return System.currentTimeMillis();
    }
}
