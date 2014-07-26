package com.lvl6.mobsters.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonRequestProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto.BeginDungeonStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.BeginDungeonRequestEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Element;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.task.TaskService;

@Component
public class BeginDungeonController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(BeginDungeonController.class);

	@Autowired
	protected TaskService taskService;
	
	/*
	 * @Autowired protected EventWriter eventWriter;
	 */

	public BeginDungeonController()
	{}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new BeginDungeonRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_BEGIN_DUNGEON_EVENT;
	}

	@Override
	protected void processRequestEvent(RequestEvent event, EventsToDispatch eventWriter) throws
	Exception {
		BeginDungeonRequestProto reqProto = ((BeginDungeonRequestEvent) event)
			.getBeginDungeonRequestProto();
		LOG.info("reqProto=" + reqProto);

	    //get values sent from the client (the request proto)
		MinimumUserProto senderProto = reqProto.getSender();
		String userId = senderProto.getUserUuid();
		Date curTime = new Date(reqProto.getClientTime());
		int taskId = reqProto.getTaskId();

		//if is event, start the cool down timer in event_persistent_for_user
		boolean isEvent = reqProto.getIsEvent();
		int eventId = reqProto.getPersistentEventId();
		int gemsSpent = reqProto.getGemsSpent();

		//active quests a user has, this is to allow monsters to drop something
		//other than a piece of themselves (quest_monster_item)
		List<Integer> questIds = reqProto.getQuestIdsList();

		//used for element tutorial, client sets what enemy monster element should appear
		//and only that one guy should appear (quest tasks should have only one stage in db)
		Element elem = reqProto.getElem();
		// if not set, then go select monsters at random
		boolean forceEnemyElem = reqProto.getForceEnemyElem();

		//set some values to send to the client (the response proto)
		BeginDungeonResponseProto.Builder resBuilder = BeginDungeonResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setTaskId(taskId);
		resBuilder.setStatus(BeginDungeonStatus.SUCCESS); //default

//			TreeMap<Integer, List<TaskStageForUser>> orderedStages = beginTaskService
//				.generateUserTaskStages(userId, curTime, taskId, isEvent,
//					eventId, gemsSpent, questIds, elem.name(), forceEnemyElem);
	}

	public void setBeginTaskService( TaskService taskService )
	{
		this.taskService = taskService;
	}
	
}
