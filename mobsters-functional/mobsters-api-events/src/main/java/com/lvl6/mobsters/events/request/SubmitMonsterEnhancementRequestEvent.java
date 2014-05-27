package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class SubmitMonsterEnhancementRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  private SubmitMonsterEnhancementRequestProto submitMonsterEnhancementRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      submitMonsterEnhancementRequestProto = SubmitMonsterEnhancementRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = submitMonsterEnhancementRequestProto.getSender().getMinUserProto().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("submit monster enhancement request exception", e);
    }
  }

  public SubmitMonsterEnhancementRequestProto getSubmitMonsterEnhancementRequestProto() {
    return submitMonsterEnhancementRequestProto;
  }
}
