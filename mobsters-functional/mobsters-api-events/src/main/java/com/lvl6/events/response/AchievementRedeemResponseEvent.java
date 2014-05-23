package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class AchievementRedeemResponseEvent extends NormalResponseEvent {

  private AchievementRedeemResponseProto achievementRedeemResponseProto;
  
  public AchievementRedeemResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_ACHIEVEMENT_REDEEM_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = achievementRedeemResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setAchievementRedeemResponseProto(AchievementRedeemResponseProto achievementRedeemResponseProto) {
    this.achievementRedeemResponseProto = achievementRedeemResponseProto;
  }

}