package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class UpgradeNormStructureResponseEvent extends NormalResponseEvent {

  private UpgradeNormStructureResponseProto upgradeNormStructureResponseProto;
  
  public UpgradeNormStructureResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_UPGRADE_NORM_STRUCTURE_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = upgradeNormStructureResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setUpgradeNormStructureResponseProto(UpgradeNormStructureResponseProto upgradeNormStructureResponseProto) {
    this.upgradeNormStructureResponseProto = upgradeNormStructureResponseProto;
  }

}
