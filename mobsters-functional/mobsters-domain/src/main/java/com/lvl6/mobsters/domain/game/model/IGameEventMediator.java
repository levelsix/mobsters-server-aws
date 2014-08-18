package com.lvl6.mobsters.domain.game.model;

import com.lvl6.mobsters.domain.game.api.IGameEventListener;
import com.lvl6.mobsters.domain.game.api.events.IGameEvent;
import com.lvl6.mobsters.event.IEventMediator;

interface IGameEventMediator
	extends IEventMediator<IGameEventListener, IGameEvent>
{

}
