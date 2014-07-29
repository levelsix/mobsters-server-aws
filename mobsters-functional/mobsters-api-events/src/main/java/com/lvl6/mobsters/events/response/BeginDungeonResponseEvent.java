package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class BeginDungeonResponseEvent extends NormalResponseEvent {

  private final BeginDungeonResponseProto beginDungeonResponseProto;
  
  public BeginDungeonResponseEvent(String playerId, int tag, BeginDungeonResponseProto.Builder protoBuilder){
    super(playerId, EventProtocolResponse.S_BEGIN_DUNGEON_EVENT, tag);
    beginDungeonResponseProto = protoBuilder.build();
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = beginDungeonResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }  
}
