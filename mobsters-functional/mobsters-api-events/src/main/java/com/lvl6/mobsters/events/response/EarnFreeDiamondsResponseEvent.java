package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.EarnFreeDiamondsResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class EarnFreeDiamondsResponseEvent extends NormalResponseEvent {

  private EarnFreeDiamondsResponseProto earnFreeDiamondsResponseProto;
  
  public EarnFreeDiamondsResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_EARN_FREE_DIAMONDS_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = earnFreeDiamondsResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setEarnFreeDiamondsResponseProto(EarnFreeDiamondsResponseProto earnFreeDiamondsResponseProto) {
    this.earnFreeDiamondsResponseProto = earnFreeDiamondsResponseProto;
  }

}
