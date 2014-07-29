package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventUserProto.UpdateClientUserResponseProto;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class UpdateClientUserResponseEvent extends NormalResponseEvent{

  private final UpdateClientUserResponseProto updateClientUserResponseProto;
  
  // By doing all state assignment in the event's construction, we gain the ability to pass the event object to another thread
  // without having to consider using a "synchronized" or "volatile" memory barrier to ensure visibility.  The ability to
  // size and schedule I/O processing thread pools independently from request computation thread pools  
  public UpdateClientUserResponseEvent(String playerId, int tag, UpdateClientUserResponseProto.Builder responseProtoBuilder) {
	    super(playerId, EventProtocolResponse.S_UPDATE_CLIENT_USER_EVENT, tag);
	    this.updateClientUserResponseProto = responseProtoBuilder.build();
	  }
	  
  public UpdateClientUserResponseEvent(String playerId, UpdateClientUserResponseProto.Builder responseProtoBuilder) {
	    super(playerId, EventProtocolResponse.S_UPDATE_CLIENT_USER_EVENT);
	    this.updateClientUserResponseProto = responseProtoBuilder.build();
	  }
	  
  /** 
   * write the event to the given ByteBuffer
   * 
   * note we are using 1.4 ByteBuffers for both client and server
   * depending on the deployment you may need to support older java
   * versions on the client and use old-style socket input/output streams
   */
  public int write(ByteBuffer buff) {
    ByteString b = updateClientUserResponseProto.toByteString();
    b.copyTo(buff);
    return b.size();
  }
}
