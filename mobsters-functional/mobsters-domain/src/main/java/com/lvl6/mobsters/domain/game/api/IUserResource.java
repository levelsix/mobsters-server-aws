package com.lvl6.mobsters.domain.game.api;

public interface IUserResource {
  public IPlayer connect();
  
  public IPlayer isConnected();
  
  public void disconnect();
  
  public String getUserUuid();
}
