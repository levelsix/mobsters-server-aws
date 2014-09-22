package com.lvl6.mobsters.domain.game.model;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.domain.game.api.IGameEventListener;
import com.lvl6.mobsters.domain.game.api.events.IGameEvent;
import com.lvl6.mobsters.event.AbstractEventMediator;

@Component("gameEventMediator")
class GameEventMediator
	extends AbstractEventMediator<IGameEvent, IGameEventListener>
	implements IGameEventMediator
{
}
