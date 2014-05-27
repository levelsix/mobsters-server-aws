package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class AcceptAndRejectFbInviteForSlotsRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private AcceptAndRejectFbInviteForSlotsRequestProto acceptAndRejectFbInviteForSlotsRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      acceptAndRejectFbInviteForSlotsRequestProto = AcceptAndRejectFbInviteForSlotsRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = acceptAndRejectFbInviteForSlotsRequestProto.getSender().getMinUserProto().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("accept and reject fb invite for slots request exception", e);
    }
  }

  public AcceptAndRejectFbInviteForSlotsRequestProto getAcceptAndRejectFbInviteForSlotsRequestProto() {
    return acceptAndRejectFbInviteForSlotsRequestProto;
  }
}
