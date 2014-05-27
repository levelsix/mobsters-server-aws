package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class RetrieveClanInfoRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private RetrieveClanInfoRequestProto retrieveClanInfoRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      retrieveClanInfoRequestProto = RetrieveClanInfoRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = retrieveClanInfoRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("retrieve clan info request exception", e);
    }
  }

  public RetrieveClanInfoRequestProto getRetrieveClanInfoRequestProto() {
    return retrieveClanInfoRequestProto;
  }
}
