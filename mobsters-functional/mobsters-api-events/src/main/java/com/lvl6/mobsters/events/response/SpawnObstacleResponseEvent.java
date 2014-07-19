package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class SpawnObstacleResponseEvent extends NormalResponseEvent {

  // private final SpawnObstacleResponseProto spawnObstacleResponseProto;
  private final ByteString b; 

  public SpawnObstacleResponseEvent(
	final String playerId, final int tag, final SpawnObstacleResponseProto.Builder protoBuilder
  )
  {
    super(playerId, EventProtocolResponse.S_SPAWN_OBSTACLE_EVENT, tag);
    b = protoBuilder.build()
      .toByteString();
  }
  
  @Override
  public int write(final ByteBuffer bb) 
  {
    b.copyTo(bb);
    return b.size();
  }
}
