package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class InviteFbFriendsForSlotsResponseEvent extends NormalResponseEvent {

  private InviteFbFriendsForSlotsResponseProto inviteFbFriendsForSlotsResponseProto;
  
  public InviteFbFriendsForSlotsResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = inviteFbFriendsForSlotsResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setInviteFbFriendsForSlotsResponseProto(InviteFbFriendsForSlotsResponseProto inviteFbFriendsForSlotsResponseProto) {
    this.inviteFbFriendsForSlotsResponseProto = inviteFbFriendsForSlotsResponseProto;
  }

  public InviteFbFriendsForSlotsResponseProto getInviteFbFriendsForSlotsResponseProto() {   //because APNS required
    return inviteFbFriendsForSlotsResponseProto;
  }
  
}
