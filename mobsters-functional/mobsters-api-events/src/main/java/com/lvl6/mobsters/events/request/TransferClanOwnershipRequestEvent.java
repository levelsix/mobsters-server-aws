package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipRequestProto;
import com.lvl6.mobsters.events.RequestEvent;

public class TransferClanOwnershipRequestEvent extends RequestEvent {

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  private TransferClanOwnershipRequestProto transferClanOwnershipRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      transferClanOwnershipRequestProto = TransferClanOwnershipRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = transferClanOwnershipRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("transfer clan ownership request exception", e);
    }
  }

  public TransferClanOwnershipRequestProto getTransferClanOwnershipRequestProto() {
    return transferClanOwnershipRequestProto;
  }
}
