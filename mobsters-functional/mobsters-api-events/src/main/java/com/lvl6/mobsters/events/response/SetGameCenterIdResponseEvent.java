package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class SetGameCenterIdResponseEvent extends NormalResponseEvent {

  private SetGameCenterIdResponseProto setGameCenterIdResponseProto;
  
  public SetGameCenterIdResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_SET_GAME_CENTER_ID_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = setGameCenterIdResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setSetGameCenterIdResponseProto(SetGameCenterIdResponseProto setGameCenterIdResponseProto) {
    this.setGameCenterIdResponseProto = setGameCenterIdResponseProto;
  }

}
