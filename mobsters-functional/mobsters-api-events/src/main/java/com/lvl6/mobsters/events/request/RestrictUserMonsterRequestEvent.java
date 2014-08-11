package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RestrictUserMonsterRequestProto;
import com.lvl6.mobsters.events.RequestEvent;


public class RestrictUserMonsterRequestEvent extends RequestEvent {
	
	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  private RestrictUserMonsterRequestProto restrictUserMonsterRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      restrictUserMonsterRequestProto = RestrictUserMonsterRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = restrictUserMonsterRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("remove monster from battle team request exception", e);
    }
  }

  public RestrictUserMonsterRequestProto getRestrictUserMonsterRequestProto() {
    return restrictUserMonsterRequestProto;
  }
}
