package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class MoveOrRotateNormStructureResponseEvent extends NormalResponseEvent {

  private MoveOrRotateNormStructureResponseProto moveOrRotateNormStructureResponseProto;
  
  public MoveOrRotateNormStructureResponseEvent(int playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = moveOrRotateNormStructureResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setMoveOrRotateNormStructureResponseProto(MoveOrRotateNormStructureResponseProto moveOrRotateNormStructureResponseProto) {
    this.moveOrRotateNormStructureResponseProto = moveOrRotateNormStructureResponseProto;
  }

}
