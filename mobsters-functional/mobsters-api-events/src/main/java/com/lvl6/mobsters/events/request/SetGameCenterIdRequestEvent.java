package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class SetGameCenterIdRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  private SetGameCenterIdRequestProto setGameCenterIdRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      setGameCenterIdRequestProto = SetGameCenterIdRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = setGameCenterIdRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("set facebook id request exception", e);
    }
  }

  public SetGameCenterIdRequestProto getSetGameCenterIdRequestProto() {
    return setGameCenterIdRequestProto;
  }
}
