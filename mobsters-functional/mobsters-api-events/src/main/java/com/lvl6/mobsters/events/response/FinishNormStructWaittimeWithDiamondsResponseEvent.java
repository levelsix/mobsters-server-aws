package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class FinishNormStructWaittimeWithDiamondsResponseEvent extends NormalResponseEvent{

  private FinishNormStructWaittimeWithDiamondsResponseProto finishNormStructWaittimeWithDiamondsResponseProto;
  
  public FinishNormStructWaittimeWithDiamondsResponseEvent(String playerId) {
    super(playerId);
    eventType = EventProtocolResponse.S_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT;
  }
  
  /** 
   * write the event to the given ByteBuffer
   * 
   * note we are using 1.4 ByteBuffers for both client and server
   * depending on the deployment you may need to support older java
   * versions on the client and use old-style socket input/output streams
   */
  public int write(ByteBuffer buff) {
    ByteString b = finishNormStructWaittimeWithDiamondsResponseProto.toByteString();
    b.copyTo(buff);
    return b.size();
  }

  public void setFinishNormStructWaittimeWithDiamondsResponseProto(
      FinishNormStructWaittimeWithDiamondsResponseProto finishNormStructWaittimeWithDiamondsResponseProto) {
    this.finishNormStructWaittimeWithDiamondsResponseProto = finishNormStructWaittimeWithDiamondsResponseProto;
  }
  
  
  
}
