package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class PromoteDemoteClanMemberRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private PromoteDemoteClanMemberRequestProto promoteDemoteClanMemberRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      promoteDemoteClanMemberRequestProto = PromoteDemoteClanMemberRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = promoteDemoteClanMemberRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("boot player from clan request exception", e);
    }
  }

  public PromoteDemoteClanMemberRequestProto getPromoteDemoteClanMemberRequestProto() {
    return promoteDemoteClanMemberRequestProto;
  }
}
