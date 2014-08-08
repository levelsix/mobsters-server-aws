package com.lvl6.mobsters.utility.attachment;

/**
 * An interface the indicates an object is able to store and return a "attachment" objects.  Attachments are keyed by their reflective
 * Class, such that there can only be exactly one attachment of a given type at any point in time.
 * 
 * Because there are three pre-built implementations provided for degrees of thread safety, consumers are encouraged to favor composition
 * and delegation over inheritance when using this interface and to manage the business logic governing which implementation to 
 * select by routing all new object construction through a factory class.
 * 
 * It must be noted that any thread safety provided by built-in concrete implementations is limited to assignment, retrieval, and 
 * replacement of attachments.  In particular, they provide no protection for access to the contents of individual Attachment
 * objects.
 * 
 * @author jheinnic
 *
 */
public interface ExtensibleObject {
	/**
	 * Given an attachment Class, locate and return the attachment object that matches that class (being assignable to the input
	 * class is not sufficient for a match here).   If there is currently no such attachment, null is returned instead.  However, 
	 * null is never a valid argument and will always result in a NullPointerException.
	 * 
	 * @param attachmentClass
	 * @return
	 */
    <T> T getAttachment(Class<T> attachmentClass);
    
    /**
     * Given an attachment class and a resource matching its type, assign the input argument object as that class's attachment instance
     * for the given class and return.
     * 
     * Neither the input class nor object may be null.  To clear the current attachment, if any, for an attachment class, use
     * {@link clearAttachment(Class<T>)} instead.
     * 
     * @param attachmentObject
     * @see #clearAttachment(Class<T>)
     */
    <T> void putAttachment(Class<T> attachmentClass, T attachmentObject);
    
    /**
     * Given and an attachment class, clear out the attachment object, if any, and return.
     * 
     * The input class may not be null.  Passing null will cause a NullPointerException to be thrown.
     * 
     * @param attachmentClass
     */
	<T> T clearAttachment(Class<T> attachmentClass);
}
