package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class EnhancementWaitTimeCompleteRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private EnhancementWaitTimeCompleteRequestProto enhancementWaitTimeCompleteRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
    	enhancementWaitTimeCompleteRequestProto = EnhancementWaitTimeCompleteRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = enhancementWaitTimeCompleteRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("enhancement wait time complete request exception", e);
    }
  }

  public EnhancementWaitTimeCompleteRequestProto getEnhancementWaitTimeCompleteRequestProto() {
    return enhancementWaitTimeCompleteRequestProto;
  }
}
