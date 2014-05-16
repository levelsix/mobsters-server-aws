package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventChatProto.RetrievePrivateChatPostsResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class RetrievePrivateChatPostsResponseEvent extends NormalResponseEvent {

  private RetrievePrivateChatPostsResponseProto retrievePrivateChatPostsResponseProto;
  
  public RetrievePrivateChatPostsResponseEvent(int playerId) {
    super(playerId);
    eventType = EventProtocolResponse.S_RETRIEVE_PRIVATE_CHAT_POST_EVENT;
  }

  /** 
   * write the event to the given ByteBuffer
   * 
   * note we are using 1.4 ByteBuffers for both client and server
   * depending on the deployment you may need to support older java
   * versions on the client and use old-style socket input/output streams
   */
  public int write(ByteBuffer buff) {
    ByteString b = retrievePrivateChatPostsResponseProto.toByteString();
    b.copyTo(buff);
    return b.size();
  }

  public void setRetrievePrivateChatPostsResponseProto(
      RetrievePrivateChatPostsResponseProto retrievePrivateChatPostsResponseProto) {
    this.retrievePrivateChatPostsResponseProto = retrievePrivateChatPostsResponseProto;
  }
  
}
