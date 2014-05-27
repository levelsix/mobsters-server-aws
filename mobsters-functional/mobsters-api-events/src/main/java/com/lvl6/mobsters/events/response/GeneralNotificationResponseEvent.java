package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventChatProto.GeneralNotificationResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class GeneralNotificationResponseEvent extends NormalResponseEvent {

  private GeneralNotificationResponseProto generalNotificationResponseProto;

  //The input argument is not used.
  public GeneralNotificationResponseEvent(String playerId){
    super(playerId); 
    eventType = EventProtocolResponse.S_GENERAL_NOTIFICATION_EVENT;
  }

  @Override
  public int write(ByteBuffer bb) {
    ByteString b = generalNotificationResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setGeneralNotificationResponseProto(
      GeneralNotificationResponseProto generalNotificationResponseProto) {
    this.generalNotificationResponseProto = generalNotificationResponseProto;
  }

  public GeneralNotificationResponseProto getGeneralNotificationResponseProto() {
    return generalNotificationResponseProto;
  }
}
