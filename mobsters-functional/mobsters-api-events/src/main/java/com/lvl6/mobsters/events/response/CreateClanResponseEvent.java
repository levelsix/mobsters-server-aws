package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class CreateClanResponseEvent extends NormalResponseEvent {

  private CreateClanResponseProto createClanResponseProto;
  
  public CreateClanResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_CREATE_CLAN_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = createClanResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setCreateClanResponseProto(CreateClanResponseProto createClanResponseProto) {
    this.createClanResponseProto = createClanResponseProto;
  }

}
