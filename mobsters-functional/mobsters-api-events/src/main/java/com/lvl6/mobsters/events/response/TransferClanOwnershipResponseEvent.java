package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class TransferClanOwnershipResponseEvent extends NormalResponseEvent{

  private TransferClanOwnershipResponseProto transferClanOwnershipResponseProto;
  
  public TransferClanOwnershipResponseEvent(String playerId) {
    super(playerId);
    eventType = EventProtocolResponse.S_TRANSFER_CLAN_OWNERSHIP;
  }
  
  /** 
   * write the event to the given ByteBuffer
   * 
   * note we are using 1.4 ByteBuffers for both client and server
   * depending on the deployment you may need to support older java
   * versions on the client and use old-style socket input/output streams
   */
  public int write(ByteBuffer buff) {
    ByteString b = transferClanOwnershipResponseProto.toByteString();
    b.copyTo(buff);
    return b.size();
  }

  public void setTransferClanOwnershipResponseProto(TransferClanOwnershipResponseProto transferClanOwnershipResponseProto) {
    this.transferClanOwnershipResponseProto = transferClanOwnershipResponseProto;
  }
  
}
