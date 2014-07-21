package com.lvl6.mobsters.dynamo.attachments;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.Preconditions;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap.Builder;

/**
 * Concrete ExtensibleObject implementation suitable for use only with objects whose attachments are retrieved by multiple threads, but rarely 
 * changed (refering to instance replacement, not attachment mutability).
 * 
 * @author jheinnic
 */
public class AtomicExtensibleObject extends AbstractExtensibleObject {
    private AtomicReference<ImmutableClassToInstanceMap<Object>> attachmentsRef;
    
    public AtomicExtensibleObject() {
    	ImmutableClassToInstanceMap<Object> attachments = 
    		ImmutableClassToInstanceMap.builder().build();
    	attachmentsRef = new AtomicReference<ImmutableClassToInstanceMap<Object>>(attachments);
    }

	@Override
	public <T> T getAttachment(Class<T> attachmentClass) {
		Preconditions.checkNotNull(attachmentClass);

		ClassToInstanceMap<Object> attachments = attachmentsRef.get();
		return attachments.getInstance(attachmentClass);
	}

	@Override
	public <T> void putAttachment(final Class<T> attachmentClass, final T attachmentObject) {
		Preconditions.checkNotNull(attachmentClass);
		Preconditions.checkNotNull(attachmentObject);

		ImmutableClassToInstanceMap<Object> attachments = attachmentsRef.get();
		ImmutableClassToInstanceMap<Object> nextAttachments = 
			buildNextMapWith(attachmentClass, attachmentObject, attachments);
		while (attachments != nextAttachments) {
			if (attachmentsRef.compareAndSet(attachments, nextAttachments)) {
				attachments = nextAttachments;
			} else {
				attachments = attachmentsRef.get();
				nextAttachments =
					buildNextMapWith(attachmentClass, attachmentObject, attachments);
			}
		}
		
		return;
	}

	@Override
	public <T> T clearAttachment(final Class<T> attachmentClass) {
		Preconditions.checkNotNull(attachmentClass);

		ImmutableClassToInstanceMap<Object> attachments = attachmentsRef.get();
		ImmutableClassToInstanceMap<Object> nextAttachments = 
			buildNextMapWithout(attachmentClass, attachments);
		T retVal = null;
		while (attachments != nextAttachments) {			
			if (attachments.size() > nextAttachments.size()) {
				// The new map is smaller, so we did remove an element.  Attempt to
				// make an atomic update by replacing the map we read.
				if (attachmentsRef.compareAndSet(attachments, nextAttachments)) {
					// Success.  Prepare to break out of the loop and identify the
					// return value.
					attachments = nextAttachments;
					retVal = attachments.getInstance(attachmentClass);
				} else {
					// Failed.  Get the new hash and repeat the remove derivation to
					// prepare for another iteration.
					attachments = attachmentsRef.get();
					nextAttachments =
						buildNextMapWithout(attachmentClass, attachments);
				}
			} else {
				// Some other thread already removed the class for us or it wasn't
				// present in the first place.  Return as though from a no-op.
				nextAttachments = attachments;
			}
		}

		return retVal;
	}

	private <T> ImmutableClassToInstanceMap<Object> buildNextMapWith(
		Class<T> attachmentClass, T attachmentObject,
		ClassToInstanceMap<Object> attachments) 
	{
		return ImmutableClassToInstanceMap.builder()
			.putAll(attachments)
			.put(attachmentClass, attachmentObject)
			.build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> ImmutableClassToInstanceMap<Object> buildNextMapWithout(
		Class<T> attachmentClass, ClassToInstanceMap<Object> attachments) 
	{
		final Builder<Object> builder = ImmutableClassToInstanceMap.builder();
		for( final Map.Entry<? extends Class,Object> nextEntry : attachments.entrySet()) {
			if (attachmentClass != nextEntry.getKey()) {
				builder.put(nextEntry.getKey(), nextEntry.getValue());
			}
		}
		return builder.build();
	}

	@Override
	protected ImmutableClassToInstanceMap<Object> getAttachmentMap() {
		return attachmentsRef.get();
	}

	@Override
	protected void copyAttachmentMap(ImmutableClassToInstanceMap<Object> source) {
		// This method is only to be invoked as an instance is being assigned to replace
		// a prior implementation, so it should not have any prior attachments of its
		// own to preserve.
		attachmentsRef.set(source);
	}
}
