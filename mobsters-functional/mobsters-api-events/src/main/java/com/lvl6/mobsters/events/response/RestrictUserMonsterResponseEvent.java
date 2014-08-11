package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RestrictUserMonsterResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class RestrictUserMonsterResponseEvent extends NormalResponseEvent {

  private RestrictUserMonsterResponseProto restrictUserMonsterResponseProto;
  
  public RestrictUserMonsterResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_RESTRICT_USER_MONSTER_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = restrictUserMonsterResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setRestrictUserMonsterResponseProto(RestrictUserMonsterResponseProto restrictUserMonsterResponseProto) {
    this.restrictUserMonsterResponseProto = restrictUserMonsterResponseProto;
  }

  public RestrictUserMonsterResponseProto getRestrictUserMonsterResponseProto() {   //because APNS required
    return restrictUserMonsterResponseProto;
  }
  
}
