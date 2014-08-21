package com.lvl6.mobsters.domain.game.api.events;

import com.lvl6.mobsters.event.IEvent;

public interface IGameEvent extends IEvent
{
    public String getUserUuid();
    
    // Coming next...
    // public GameModelEventType getEventType();
}