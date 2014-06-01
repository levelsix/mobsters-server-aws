package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventMonsterProto.UpdateMonsterHealthRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class UpdateMonsterHealthRequestEvent extends RequestEvent {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateMonsterHealthRequestEvent.class);

  private UpdateMonsterHealthRequestProto updateMonsterHealthRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      updateMonsterHealthRequestProto = UpdateMonsterHealthRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = updateMonsterHealthRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      LOG.error("update monster health request exception", e);
    }
  }

  public UpdateMonsterHealthRequestProto getUpdateMonsterHealthRequestProto() {
    return updateMonsterHealthRequestProto;
  }
}

