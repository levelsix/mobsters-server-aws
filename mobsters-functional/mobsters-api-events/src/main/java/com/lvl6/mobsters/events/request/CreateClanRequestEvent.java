package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class CreateClanRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private CreateClanRequestProto createClanRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      createClanRequestProto = CreateClanRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = createClanRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("create clan request exception", e);
    }
  }

  public CreateClanRequestProto getCreateClanRequestProto() {
    return createClanRequestProto;
  }
}
