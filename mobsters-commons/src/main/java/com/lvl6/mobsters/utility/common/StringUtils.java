package com.lvl6.mobsters.utility.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(StringUtils.class);
	
	private StringUtils() {
		throw new AssertionError("StringUtils is a static utility class");
	}

    public static boolean hasContent(final String str) {
    	return (str != null) && (! str.isEmpty());
    }
    
    public static boolean isEmpty(final String str) {
    	return (str != null) && (str.isEmpty());
    }
    
    public static boolean isNull(final String str) {
    	return str == null;
    }
}
