package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventUserProto.SetAvatarMonsterRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class SetAvatarMonsterRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private SetAvatarMonsterRequestProto setAvatarMonsterRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      setAvatarMonsterRequestProto = SetAvatarMonsterRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = setAvatarMonsterRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("enable apns request exception", e);
    }
  }

  public SetAvatarMonsterRequestProto getSetAvatarMonsterRequestProto() {
    return setAvatarMonsterRequestProto;
  }
}
