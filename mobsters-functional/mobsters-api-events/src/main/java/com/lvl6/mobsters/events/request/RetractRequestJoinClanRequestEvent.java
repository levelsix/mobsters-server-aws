package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class RetractRequestJoinClanRequestEvent extends RequestEvent {
	
	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  private RetractRequestJoinClanRequestProto retractRequestJoinClanRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      retractRequestJoinClanRequestProto = RetractRequestJoinClanRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = retractRequestJoinClanRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("retract request join clan request exception", e);
    }
  }

  public RetractRequestJoinClanRequestProto getRetractRequestJoinClanRequestProto() {
    return retractRequestJoinClanRequestProto;
  }
}
