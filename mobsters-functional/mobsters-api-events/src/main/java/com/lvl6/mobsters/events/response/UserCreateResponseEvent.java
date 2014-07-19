package com.lvl6.mobsters.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto;
import com.lvl6.mobsters.events.PreDatabaseResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class UserCreateResponseEvent extends PreDatabaseResponseEvent{

  private final UserCreateResponseProto userCreateResponseProto;
  
  // By doing all state assignment in the event's construction, we gain the ability to pass the event object to another thread
  // without having to consider using a "synchronized" or "volatile" memory barrier to ensure visibility.  The ability to
  // size and schedule I/O processing thread pools independently from request computation thread pools  
  public UserCreateResponseEvent(final String udid, final int tag, UserCreateResponseProto.Builder responseProtoBuilder)
  {
    super(udid, EventProtocolResponse.S_USER_CREATE_EVENT, tag);
    this.userCreateResponseProto = responseProtoBuilder.build();
  }
  
  /** 
   * write the event to the given ByteBuffer
   * 
   * note we are using 1.4 ByteBuffers for both client and server
   * depending on the deployment you may need to support older java
   * versions on the client and use old-style socket input/output streams
   */
  public int write(ByteBuffer buff) {
    ByteString b = getUserCreateResponseProto().toByteString();
    b.copyTo(buff);
    return b.size();
  }

	public UserCreateResponseProto getUserCreateResponseProto()
	{
		return userCreateResponseProto;
	}

//  public void setUserCreateResponseProto(UserCreateResponseProto UserCreateResponseProto) {
//    this.userCreateResponseProto = UserCreateResponseProto;
//  }
}
