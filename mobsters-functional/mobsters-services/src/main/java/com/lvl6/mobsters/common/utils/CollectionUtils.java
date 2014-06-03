package com.lvl6.mobsters.common.utils;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CollectionUtils {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CollectionUtils.class);
	
	private CollectionUtils() {
		throw new AssertionError("CollectionUtils is a static utility class");
	}

    public static boolean lacksSubstance(final Collection<?> col) {
    	boolean retVal = false;
    	if ((col != null) && (! col.isEmpty())) {
    		for (final Object nextItem : col) {
    			if (nextItem == null) {
    				retVal = true;
    				break;
    			}
    		}
    	} else {
    		retVal = true;
    	}
    	
    	return retVal;
    }

    public static boolean isOrHasNull(final Collection<?> col) {
    	boolean retVal;
    	if (col == null) {
    		retVal = true;
    	} else {
    		retVal = false;
    		for (final Object nextItem : col) {
    			if (nextItem == null) {
    				retVal = true;
    				break;
    			}
    		}
    	}
    	
    	return retVal;
    }

    public static boolean hasNull(final Collection<?> col) {
    	boolean retVal = false;
    	if ((col != null) && (! col.isEmpty())) {
    		for (final Object nextItem : col) {
    			if (nextItem == null) {
    				retVal = true;
    				break;
    			}
    		}
    	} 
    	
    	return retVal;
    }

    public static boolean hasItems(final Collection<?> col) {
    	boolean retVal = false;
    	if ((col != null) && (! col.isEmpty())) {
    		retVal = true;
    		for (final Object nextItem : col) {
    			if (nextItem == null) {
    				retVal = false;
    				break;
    			}
    		}
    	}
    	
    	return retVal;
    }

    public static boolean hasStorage(final Collection<?> col) {
    	return (col != null) && (! col.isEmpty());
    }
    
    public static boolean isEmpty(final Collection<?> col) {
    	return (col != null) && (col.isEmpty());
    }

	public static boolean lacksSubstance(Map<String, Integer> idToHealthMap) {
		// TODO Auto-generated method stub
		return false;
	}
}
