package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class BeginClanRaidResponseEvent extends NormalResponseEvent {

  private BeginClanRaidResponseProto beginClanRaidResponseProto;
  
  public BeginClanRaidResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_BEGIN_CLAN_RAID_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = beginClanRaidResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setBeginClanRaidResponseProto(BeginClanRaidResponseProto beginClanRaidResponseProto) {
    this.beginClanRaidResponseProto = beginClanRaidResponseProto;
  }

}
