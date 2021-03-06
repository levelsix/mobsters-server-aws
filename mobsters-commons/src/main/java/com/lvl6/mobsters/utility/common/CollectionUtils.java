package com.lvl6.mobsters.utility.common;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CollectionUtils {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CollectionUtils.class);
	
	private CollectionUtils() {
		throw new AssertionError("CollectionUtils is a static utility class");
	}

	/**
	 * Return true if <code>col</code> is null, is empty, or is non-empty with
	 * any null value contents.  Otherwise, return false.
	 * 
	 * This method could have equivalently been named isEmptyOrIsOrHasNull().
	 * 
	 * @param col
	 * @return
	 */
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

    
    /**
     * This is returns true iff <code> is not null and is not empty.
     * 
     * @param col
     * @return
     * @see #isEmptyOrNull(Collection) This method's return values are the inverse of those
     * returned by isEmptyOrNull()
     */
    public static boolean hasStorage(final Collection<?> col) {
    	return (col != null) && (! col.isEmpty());
    }
        
    /**
     * This is returns true iff <code> is either:
     * <ul>
     *   <li>null</li>
     *   <li>not-null and empty</li>
     * </ul>
     * 
     * @param col
     * @return
     * @see #hasStorage(Collection) This method's return values are the inverse of those
     * returned by hasStorage()
     */
    public static boolean isEmptyOrNull(final Collection<?> col) {
    	return (col == null) || col.isEmpty();
    }
}
