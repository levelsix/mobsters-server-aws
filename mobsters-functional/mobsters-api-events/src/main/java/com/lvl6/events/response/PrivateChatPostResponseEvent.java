package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class PrivateChatPostResponseEvent extends NormalResponseEvent {

  private PrivateChatPostResponseProto privateChatPostResponseProto;
  
  public PrivateChatPostResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_PRIVATE_CHAT_POST_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = privateChatPostResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setPrivateChatPostResponseProto(PrivateChatPostResponseProto privateChatPostResponseProto) {
    this.privateChatPostResponseProto = privateChatPostResponseProto;
  }

  public PrivateChatPostResponseProto getPrivateChatPostResponseProto() { //required for APNS
    return privateChatPostResponseProto;
  }
  
}
