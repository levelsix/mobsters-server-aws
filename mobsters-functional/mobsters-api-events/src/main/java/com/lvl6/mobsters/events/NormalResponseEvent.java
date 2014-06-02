package com.lvl6.mobsters.events;

import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public abstract class NormalResponseEvent extends ResponseEvent{
  protected String playerId;   //refers to whoever sent the event/triggered it

  public String getPlayerId() {
    return playerId;
  }
  
  // TEMPORARY band-aid until event type argument constructor is fully utilized.
  public NormalResponseEvent(String playerId) {
	super();
    this.playerId = playerId;
  }
  
  public NormalResponseEvent(String playerId, EventProtocolResponse eventType) {
	super(eventType);
    this.playerId = playerId;
  }

  public NormalResponseEvent(String playerId, EventProtocolResponse eventType, int tag) {
	super(eventType, tag);
    this.playerId = playerId;
  }
}
