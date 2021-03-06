package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class PurchaseNormStructureRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private PurchaseNormStructureRequestProto purchaseNormStructureRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      purchaseNormStructureRequestProto = PurchaseNormStructureRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = purchaseNormStructureRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("purchase norm structure request exception", e);
    }
  }

  public PurchaseNormStructureRequestProto getPurchaseNormStructureRequestProto() {
    return purchaseNormStructureRequestProto;
  }
}
