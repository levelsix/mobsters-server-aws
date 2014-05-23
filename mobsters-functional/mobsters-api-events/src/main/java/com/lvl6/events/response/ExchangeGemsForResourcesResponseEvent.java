package com.lvl6.events.response;

import java.nio.ByteBuffer;

import com.google.protobuf.ByteString;
import com.lvl6.events.NormalResponseEvent;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolResponse;

public class ExchangeGemsForResourcesResponseEvent extends NormalResponseEvent {

  private ExchangeGemsForResourcesResponseProto exchangeGemsForResourcesResponseProto;
  
  public ExchangeGemsForResourcesResponseEvent(String playerId){
    super(playerId);
    eventType = EventProtocolResponse.S_EXCHANGE_GEMS_FOR_RESOURCES_EVENT;
  }
  
  @Override
  public int write(ByteBuffer bb) {
    ByteString b = exchangeGemsForResourcesResponseProto.toByteString();
    b.copyTo(bb);
    return b.size();
  }

  public void setExchangeGemsForResourcesResponseProto(ExchangeGemsForResourcesResponseProto exchangeGemsForResourcesResponseProto) {
    this.exchangeGemsForResourcesResponseProto = exchangeGemsForResourcesResponseProto;
  }

}
