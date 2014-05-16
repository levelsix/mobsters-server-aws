package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class BeginDungeonResponseEvent extends NormalResponseEvent {

  private BeginDungeonResponseProto beginDungeonResponseProto;
  
  public BeginDungeonResponseEvent(int playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_BEGIN_DUNGEON_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = beginDungeonResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setBeginDungeonResponseProto(BeginDungeonResponseProto beginDungeonResponseProto) {
    this.beginDungeonResponseProto = beginDungeonResponseProto;
  }

  public BeginDungeonResponseProto getBeginDungeonResponseProto() {   //because APNS required
    return beginDungeonResponseProto;
  }
  
}
