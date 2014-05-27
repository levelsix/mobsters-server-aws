package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class ChangeClanSettingsResponseEvent extends NormalResponseEvent {

  private ChangeClanSettingsResponseProto changeClanSettingsResponseProto;
  
  public ChangeClanSettingsResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_CHANGE_CLAN_SETTINGS_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = changeClanSettingsResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setChangeClanSettingsResponseProto(ChangeClanSettingsResponseProto changeClanSettingsResponseProto) {
    this.changeClanSettingsResponseProto = changeClanSettingsResponseProto;
  }

}
