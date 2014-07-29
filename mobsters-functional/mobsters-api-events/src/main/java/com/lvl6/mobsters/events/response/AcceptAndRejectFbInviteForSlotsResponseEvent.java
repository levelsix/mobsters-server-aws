package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class AcceptAndRejectFbInviteForSlotsResponseEvent extends NormalResponseEvent {

  private final AcceptAndRejectFbInviteForSlotsResponseProto acceptAndRejectFbInviteForSlotsResponseProto;
  
  public AcceptAndRejectFbInviteForSlotsResponseEvent(String playerId, AcceptAndRejectFbInviteForSlotsResponseProto.Builder protoBuilder){
    super(playerId, EventProtocolResponse.S_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT);
    this.acceptAndRejectFbInviteForSlotsResponseProto = protoBuilder.build();
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = acceptAndRejectFbInviteForSlotsResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }
}
