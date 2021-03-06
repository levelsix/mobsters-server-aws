package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class QuestProgressResponseEvent extends NormalResponseEvent {

  private QuestProgressResponseProto questProgressResponseProto;
  
  public QuestProgressResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_QUEST_PROGRESS_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = questProgressResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setQuestProgressResponseProto(QuestProgressResponseProto questProgressResponseProto) {
    this.questProgressResponseProto = questProgressResponseProto;
  }

}
