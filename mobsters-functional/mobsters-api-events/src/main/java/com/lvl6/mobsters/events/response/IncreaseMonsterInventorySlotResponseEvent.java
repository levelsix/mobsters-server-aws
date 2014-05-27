package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class IncreaseMonsterInventorySlotResponseEvent extends NormalResponseEvent {

  private IncreaseMonsterInventorySlotResponseProto increaseMonsterInventorySlotResponseProto;
  
  public IncreaseMonsterInventorySlotResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_INCREASE_MONSTER_INVENTORY_SLOT_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = increaseMonsterInventorySlotResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setIncreaseMonsterInventorySlotResponseProto(IncreaseMonsterInventorySlotResponseProto increaseMonsterInventorySlotResponseProto) {
    this.increaseMonsterInventorySlotResponseProto = increaseMonsterInventorySlotResponseProto;
  }

  public IncreaseMonsterInventorySlotResponseProto getIncreaseMonsterInventorySlotResponseProto() {   //because APNS required
    return increaseMonsterInventorySlotResponseProto;
  }
  
}
