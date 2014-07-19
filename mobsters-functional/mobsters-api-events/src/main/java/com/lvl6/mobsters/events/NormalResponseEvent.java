package com.lvl6.mobsters.events;

import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public abstract class NormalResponseEvent extends ResponseEvent
{
  protected final String playerId;   //refers to whoever sent the event/triggered it

  public String getPlayerId() {
    return playerId;
  }
  
  // TEMPORARY band-aid until event type argument constructor is fully utilized.
  protected NormalResponseEvent(String playerId) {
	super();
    this.playerId = playerId;
  }
  
  protected NormalResponseEvent(String playerId, EventProtocolResponse eventType) {
	super(eventType);
    this.playerId = playerId;
  }

  protected NormalResponseEvent(String playerId, EventProtocolResponse eventType, int tag) {
	super(eventType, tag);
    this.playerId = playerId;
  }
}
