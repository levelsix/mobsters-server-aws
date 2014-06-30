package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.info.EventPersistent;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.PersistentEventProto;


public interface NoneventEventPersistentProtoSerializer
{

	public abstract PersistentEventProto createPersistentEventProtoFromEvent( EventPersistent event );

}
