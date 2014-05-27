package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class CombineUserMonsterPiecesResponseEvent extends NormalResponseEvent {

  private CombineUserMonsterPiecesResponseProto combineMonsterPiecesResponseProto;
  
  public CombineUserMonsterPiecesResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_COMBINE_USER_MONSTER_PIECES_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = combineMonsterPiecesResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setCombineUserMonsterPiecesResponseProto(CombineUserMonsterPiecesResponseProto combineMonsterPiecesResponseProto) {
    this.combineMonsterPiecesResponseProto = combineMonsterPiecesResponseProto;
  }

  public CombineUserMonsterPiecesResponseProto getCombineUserMonsterPiecesResponseProto() {   //because APNS required
    return combineMonsterPiecesResponseProto;
  }
  
}
