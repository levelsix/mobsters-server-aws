package com.lvl6.mobsters.events;

import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public abstract class PreDatabaseResponseEvent extends ResponseEvent{
  protected final String udid;
  
  protected PreDatabaseResponseEvent(final String udid, final EventProtocolResponse eventType) {
	  super(eventType);
	  this.udid = udid;
  }
  
  protected PreDatabaseResponseEvent(final String udid, final EventProtocolResponse eventType, final int tag) {
	super(eventType, tag);
    this.udid = udid;
  }

  public String getUdid() {
    return udid;
  }
}
