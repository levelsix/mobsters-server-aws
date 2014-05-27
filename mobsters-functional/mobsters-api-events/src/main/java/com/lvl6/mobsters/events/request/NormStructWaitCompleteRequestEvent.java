package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class NormStructWaitCompleteRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private NormStructWaitCompleteRequestProto normStructWaitCompleteRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      normStructWaitCompleteRequestProto = NormStructWaitCompleteRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = normStructWaitCompleteRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("norm struct wait time complete request exception", e);
    }
  }

  public NormStructWaitCompleteRequestProto getNormStructWaitCompleteRequestProto() {
    return normStructWaitCompleteRequestProto;
  }
}
