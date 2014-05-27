package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class ObstacleRemovalCompleteResponseEvent extends NormalResponseEvent {

  private ObstacleRemovalCompleteResponseProto obstacleRemovalCompleteResponseProto;
  
  public ObstacleRemovalCompleteResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_OBSTACLE_REMOVAL_COMPLETE_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = obstacleRemovalCompleteResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setObstacleRemovalCompleteResponseProto(ObstacleRemovalCompleteResponseProto obstacleRemovalCompleteResponseProto) {
    this.obstacleRemovalCompleteResponseProto = obstacleRemovalCompleteResponseProto;
  }

}
