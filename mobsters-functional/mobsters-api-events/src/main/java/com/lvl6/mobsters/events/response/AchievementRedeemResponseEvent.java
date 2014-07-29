package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class AchievementRedeemResponseEvent extends NormalResponseEvent {

  private final AchievementRedeemResponseProto achievementRedeemResponseProto;
  
  public AchievementRedeemResponseEvent(String playerId, int tag, AchievementRedeemResponseProto.Builder protoBuilder){
    super(playerId, EventProtocolResponse.S_ACHIEVEMENT_REDEEM_EVENT, tag);
    achievementRedeemResponseProto = protoBuilder.build();
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = achievementRedeemResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
}