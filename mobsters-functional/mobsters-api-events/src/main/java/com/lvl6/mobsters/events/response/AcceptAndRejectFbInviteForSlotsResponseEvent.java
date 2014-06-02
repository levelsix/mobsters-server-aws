package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class AcceptAndRejectFbInviteForSlotsResponseEvent extends NormalResponseEvent {

  private AcceptAndRejectFbInviteForSlotsResponseProto acceptAndRejectFbInviteForSlotsResponseProto;
  
  public AcceptAndRejectFbInviteForSlotsResponseEvent(String playerId){
    super(playerId, EventProtocolResponse.S_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT);
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = acceptAndRejectFbInviteForSlotsResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setAcceptAndRejectFbInviteForSlotsResponseProto(AcceptAndRejectFbInviteForSlotsResponseProto acceptAndRejectFbInviteForSlotsResponseProto) {
    this.acceptAndRejectFbInviteForSlotsResponseProto = acceptAndRejectFbInviteForSlotsResponseProto;
  }

  public AcceptAndRejectFbInviteForSlotsResponseProto getAcceptAndRejectFbInviteForSlotsResponseProto() {   //because APNS required
    return acceptAndRejectFbInviteForSlotsResponseProto;
  }
  
}
