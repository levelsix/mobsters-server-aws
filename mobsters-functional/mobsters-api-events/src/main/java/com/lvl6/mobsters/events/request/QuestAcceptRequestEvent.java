package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class QuestAcceptRequestEvent extends RequestEvent {
	
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  private QuestAcceptRequestProto questAcceptRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      questAcceptRequestProto = QuestAcceptRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = questAcceptRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("quest accept request exception", e);
    }
  }

  public QuestAcceptRequestProto getQuestAcceptRequestProto() {
    return questAcceptRequestProto;
  }
}
