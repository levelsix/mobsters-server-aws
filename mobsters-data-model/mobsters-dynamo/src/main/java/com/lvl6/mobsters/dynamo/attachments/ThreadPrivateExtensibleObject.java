package com.lvl6.mobsters.dynamo.attachments;

import com.google.common.base.Preconditions;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

/**
 * Concrete ExtensibleObject implementation suitable for use only with objects whose attachments are only ever accessed by a single thread
 * during their life-span.
 * 
 * @author jheinnic
 */
public class ThreadPrivateExtensibleObject extends AbstractExtensibleObject {
    private ClassToInstanceMap<Object> attachments;
    
    public ThreadPrivateExtensibleObject() {
    	attachments = MutableClassToInstanceMap.create();
    }

	@Override
	public <T> T getAttachment(final Class<T> attachmentClass) {
		Preconditions.checkNotNull(attachmentClass);
		final T retVal = attachments.getInstance(attachmentClass);
		return retVal;
	}

	@Override
	public <T> void putAttachment(final Class<T> attachmentClass, final T attachmentObject) {
		Preconditions.checkNotNull(attachmentClass);
		Preconditions.checkNotNull(attachmentObject);
		attachments.putInstance(attachmentClass, attachmentObject);
		return;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T clearAttachment(final Class<T> attachmentClass) {
		Preconditions.checkNotNull(attachmentClass);
		return ((T) attachments.remove(attachmentClass));
	}
	

	@Override
	protected ImmutableClassToInstanceMap<Object> getAttachmentMap() {
		return ImmutableClassToInstanceMap.copyOf(this.attachments);
	}

	@Override
	protected void copyAttachmentMap(ImmutableClassToInstanceMap<Object> source) {
		this.attachments.putAll(source);
	}
}
