package com.lvl6.mobsters.dynamo.attachments;

import com.google.common.collect.ImmutableClassToInstanceMap;

/**
 * An abstract base class for ExtnsibleObject implementations that are designed to support reuse as 
 * composition-friendly delegates.
 * 
 * Provides a non-consumer initialization method for bootstrapping a replacement concrete 
 * implementation to retain all registered attachments known by the instance being replaced.
 * 
 * @author jheinnic
 *
 */
public abstract class AbstractExtensibleObject implements ExtensibleObject {
    /**
     * Return an immutable copy of the source ExtensibleObject's known attachment registrations.
     */
	protected abstract ImmutableClassToInstanceMap<Object> getAttachmentMap();
    
	/**
	 * Absorb the information in an input immutable copy of the source's known attachment 
	 * registrations.  Callee is free to retain the input argument for its own re-use, but must
	 * respect the fact that it is an immutable Map by design and so may prefer to copy the
	 * content to its own mutable structure instead.
	 * 
	 * @param source
	 */
	protected abstract void copyAttachmentMap(ImmutableClassToInstanceMap<Object> source);
    
    /**
     * This method is public to expose enough internals to composition users to facilitate
     * a change-of-representation.  It is not a consumer method hence intentionally not part
     * 
     * @param predecessor
     */
    public void initializeWith(AbstractExtensibleObject predecessor) {
    	copyAttachmentMap(
    		predecessor.getAttachmentMap()
    	);
    }
}
