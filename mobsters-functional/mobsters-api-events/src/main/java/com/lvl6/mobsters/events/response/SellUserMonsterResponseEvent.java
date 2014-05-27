package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class SellUserMonsterResponseEvent extends NormalResponseEvent {

  private SellUserMonsterResponseProto sellUserMonsterResponseProto;
  
  public SellUserMonsterResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_SELL_USER_MONSTER_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = sellUserMonsterResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setSellUserMonsterResponseProto(SellUserMonsterResponseProto sellUserMonsterResponseProto) {
    this.sellUserMonsterResponseProto = sellUserMonsterResponseProto;
  }

  public SellUserMonsterResponseProto getSellUserMonsterResponseProto() {   //because APNS required
    return sellUserMonsterResponseProto;
  }
  
}
