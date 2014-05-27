package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventUserProto.LogoutRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class LogoutRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private LogoutRequestProto logoutRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      logoutRequestProto = LogoutRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = logoutRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("logout request exception", e);
    }
  }

  public LogoutRequestProto getLogoutRequestProto() {
    return logoutRequestProto;
  }
}
