package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class ApproveOrRejectRequestToJoinClanResponseEvent extends NormalResponseEvent {

  private ApproveOrRejectRequestToJoinClanResponseProto approveOrRejectRequestToJoinClanResponseProto;
  
  public ApproveOrRejectRequestToJoinClanResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = approveOrRejectRequestToJoinClanResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setApproveOrRejectRequestToJoinClanResponseProto(ApproveOrRejectRequestToJoinClanResponseProto approveOrRejectRequestToJoinClanResponseProto) {
    this.approveOrRejectRequestToJoinClanResponseProto = approveOrRejectRequestToJoinClanResponseProto;
  }

}
