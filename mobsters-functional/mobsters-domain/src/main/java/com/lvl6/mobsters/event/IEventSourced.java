package com.lvl6.mobsters.event;


public interface IEventSourced<E extends IEvent> {
    void apply(E evt);
}
