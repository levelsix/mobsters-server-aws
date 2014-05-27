package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventClanProto.AwardClanRaidStageRewardResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class AwardClanRaidStageRewardResponseEvent extends NormalResponseEvent {

  private AwardClanRaidStageRewardResponseProto awardClanRaidStageRewardResponseProto;
  
  public AwardClanRaidStageRewardResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_AWARD_CLAN_RAID_STAGE_REWARD_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = awardClanRaidStageRewardResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setAwardClanRaidStageRewardResponseProto(AwardClanRaidStageRewardResponseProto awardClanRaidStageRewardResponseProto) {
    this.awardClanRaidStageRewardResponseProto = awardClanRaidStageRewardResponseProto;
  }

}
