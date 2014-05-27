package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamRequestProto;
import com.lvl6.mobsters.events.RequestEvent;


public class RemoveMonsterFromBattleTeamRequestEvent extends RequestEvent {
	
	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

  private RemoveMonsterFromBattleTeamRequestProto removeMonsterFromBattleTeamRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try {
      removeMonsterFromBattleTeamRequestProto = RemoveMonsterFromBattleTeamRequestProto.parseFrom(ByteString.copyFrom(buff));
      playerId = removeMonsterFromBattleTeamRequestProto.getSender().getUserUuid();
    } catch (InvalidProtocolBufferException e) {
      log.error("remove monster from battle team request exception", e);
    }
  }

  public RemoveMonsterFromBattleTeamRequestProto getRemoveMonsterFromBattleTeamRequestProto() {
    return removeMonsterFromBattleTeamRequestProto;
  }
}
