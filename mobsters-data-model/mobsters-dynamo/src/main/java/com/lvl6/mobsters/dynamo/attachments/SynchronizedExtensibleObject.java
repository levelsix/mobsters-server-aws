package com.lvl6.mobsters.dynamo.attachments;

import com.google.common.base.Preconditions;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

/**
 * Concrete ExtensibleObject implementation for use cases where an object's attachments are replaced by\
 * multiple threads with sufficient frequency to justify synchronization as opposed to atomic updates.
 * 
 * @author jheinnic
 */
public class SynchronizedExtensibleObject extends AbstractExtensibleObject {
    private ClassToInstanceMap<Object> attachments;
    
    public SynchronizedExtensibleObject() {
    	attachments = MutableClassToInstanceMap.create();
    }

	@Override
	public <T> T getAttachment(final Class<T> attachmentClass) {
		Preconditions.checkNotNull(attachmentClass);
		synchronized(attachments) {
			return attachments.getInstance(attachmentClass);
		}
	}

	@Override
	public <T> void putAttachment(final Class<T> attachmentClass, final T attachmentObject) {
		Preconditions.checkNotNull(attachmentClass);
		Preconditions.checkNotNull(attachmentObject);
		synchronized(attachments) {
			attachments.putInstance(attachmentClass, attachmentObject);
		}
		return;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T clearAttachment(final Class<T> attachmentClass) {
		Preconditions.checkNotNull(attachmentClass);
		synchronized(attachments) {
			return ((T) attachments.remove(attachmentClass));
		}
	}

	@Override
	protected ImmutableClassToInstanceMap<Object> getAttachmentMap() {
		synchronized(this.attachments) {
			return ImmutableClassToInstanceMap.copyOf(this.attachments);
		}
	}

	@Override
	protected void copyAttachmentMap(ImmutableClassToInstanceMap<Object> source) {
		synchronized(this.attachments) {
			this.attachments.putAll(source);
		}
	}
}
