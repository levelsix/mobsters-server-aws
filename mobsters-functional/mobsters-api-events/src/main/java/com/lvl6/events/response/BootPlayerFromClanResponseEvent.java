package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class BootPlayerFromClanResponseEvent extends NormalResponseEvent {

  private BootPlayerFromClanResponseProto bootPlayerFromClanResponseProto;
  
  public BootPlayerFromClanResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_BOOT_PLAYER_FROM_CLAN_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = bootPlayerFromClanResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setBootPlayerFromClanResponseProto(BootPlayerFromClanResponseProto bootPlayerFromClanResponseProto) {
    this.bootPlayerFromClanResponseProto = bootPlayerFromClanResponseProto;
  }

}
