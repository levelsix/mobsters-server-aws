package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class FinishNormStructWaittimeWithDiamondsRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private FinishNormStructWaittimeWithDiamondsRequestProto finishNormStructWaittimeWithDiamondsRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      finishNormStructWaittimeWithDiamondsRequestProto = FinishNormStructWaittimeWithDiamondsRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = finishNormStructWaittimeWithDiamondsRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("finish norm struct wait time with diamonds request exception", e);
    }
  }

  public FinishNormStructWaittimeWithDiamondsRequestProto getFinishNormStructWaittimeWithDiamondsRequestProto() {
    return finishNormStructWaittimeWithDiamondsRequestProto;
  }
}
