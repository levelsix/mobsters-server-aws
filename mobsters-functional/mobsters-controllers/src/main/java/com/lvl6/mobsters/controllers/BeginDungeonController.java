package com.lvl6.mobsters.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.AbstractAction;
import com.lvl6.mobsters.common.utils.IAction;
import com.lvl6.mobsters.common.utils.ICallableAction;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonRequestProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto.BeginDungeonStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.BeginDungeonRequestEvent;
import com.lvl6.mobsters.events.response.BeginDungeonResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Element;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.task.TaskService;
import com.lvl6.mobsters.services.task.TaskService.GenerateUserTaskStagesResponseBuilder;

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
//			TreeMap<Integer, List<TaskStageForUser>> orderedStages = beginTaskService
//				.generateUserTaskStages(userId, curTime, taskId, isEvent,
//					eventId, gemsSpent, questIds, elem.name(), forceEnemyElem);
		
		// Verify syntax validity of service call parameters.
		ICallableAction<GenerateUserTaskStagesResponseBuilder> svcAction = 
			taskService.generateUserTaskStages(
				userId, curTime, taskId, isEvent, eventId, gemsSpent, questIds, 
				(elem != null) ? elem.name() : "", forceEnemyElem
			);
		checkSyntaxValidity(svcAction);

		// Prepare to receive response values for sending to the client (via response proto),
		// then call the service.
		final GenerateUserTaskStagesResponseBuilderImpl responseEventBuilder = 
			new GenerateUserTaskStagesResponseBuilderImpl(senderProto, event.getTag(), taskId);
		svcAction.execute(responseEventBuilder);
		
		// If no exception was thrown, result was a success.  Write response to client
		final BeginDungeonResponseEvent responseEvent = responseEventBuilder.build();
		LOG.info("Writing event: %s", responseEvent);
		eventWriter.writeEvent(responseEvent);
	}
	
	static class GenerateUserTaskStagesResponseBuilderImpl implements GenerateUserTaskStagesResponseBuilder {
		private final BeginDungeonResponseProto.Builder resBuilder;
		private final String userUuid;
		private final int tag;
		
		GenerateUserTaskStagesResponseBuilderImpl(
			final MinimumUserProto senderProto, final int tag, final int taskId ) 
		{
			resBuilder = BeginDungeonResponseProto.newBuilder();
			resBuilder.setSender(senderProto);
			resBuilder.setTaskId(taskId);
			resBuilder.setStatus(BeginDungeonStatus.SUCCESS); //default

			userUuid = senderProto.getUserUuid();
			this.tag = tag;
		}
		
		public GenerateUserTaskStagesResponseBuilder taskId(int taskId) {
			resBuilder.setTaskId(taskId);
			return this;
		}
		
		BeginDungeonResponseEvent build() {
			return new BeginDungeonResponseEvent(userUuid, tag, resBuilder);
		}
	}

	/******************************************************************************************/
	
	/* BEGIN Dependency Injection *************************************************************/
	
	void setTaskService( TaskService taskService )
	{
		this.taskService = taskService;
	}
}
