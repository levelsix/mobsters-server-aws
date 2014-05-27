package com.lvl6.mobsters.events.request;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateRequestProto;
import com.lvl6.mobsters.events.PreDatabaseRequestEvent;

public class UserCreateRequestEvent extends PreDatabaseRequestEvent{

	private Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
	
  private UserCreateRequestProto userCreateRequestProto;
  
  /**
   * read the event from the given ByteBuffer to populate this event
   */
  public void read(ByteBuffer buff) {
    try { 
      userCreateRequestProto = UserCreateRequestProto.parseFrom(ByteString.copyFrom(buff));
      
      // Player id is "" since it won't be initialized yet. 
      playerId = "";
      
      udid = userCreateRequestProto.getUdid();
    } catch (InvalidProtocolBufferException e) {
      log.error("user create request exception", e);
    }
  }

  public UserCreateRequestProto getUserCreateRequestProto() {
    return userCreateRequestProto;
  }
  
  public void setUserCreateRequestProto(UserCreateRequestProto ucrp) {
  	this.userCreateRequestProto = ucrp;
  }
}