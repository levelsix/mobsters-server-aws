package com.lvl6.mobsters.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.lvl6.mobsters.services.task.TaskService.AddStageGenerateUserTaskListener;
import com.lvl6.mobsters.services.task.TaskService.GenerateUserTaskListener;
import com.lvl6.mobsters.utility.common.TimeUtils;
import com.lvl6.mobsters.utility.lambda.Director;

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

		// if not set, then go select monsters at random
		boolean forceEnemyElem = reqProto.getForceEnemyElem();
		Element elem = reqProto.getElem();
		
		// Verify syntax validity of service call parameters.
		ICallableAction<GenerateUserTaskListener> svcAction = 
			taskService.generateUserTaskStages(
				senderProto.getUserUuid(), 
				TimeUtils.createDateFromTime(reqProto.getClientTime()), 
				reqProto.getTaskId(),
				reqProto.getIsEvent(),
				reqProto.getPersistentEventId(),
				reqProto.getGemsSpent(),
				reqProto.getQuestIdsList(),
				elem != null ? elem.name() : "",
				reqProto.getForceEnemyElem(),
				reqProto.hasAlreadyCompletedMiniTutorialTask()
			);
		checkSyntaxValidity(svcAction);

		// Prepare to receive response values for sending to the client (via response proto),
		// then call the service.
		final GenerateUserTaskListenerImpl responseListener = 
			new GenerateUserTaskListenerImpl(senderProto, event.getTag(), taskId);
		svcAction.execute(responseListener);
		
		// If no exception was thrown, result was a success.  Write response to client
		final BeginDungeonResponseEvent responseEvent = responseListener.build();
		LOG.info("Writing event: %s", responseEvent);
		eventWriter.writeEvent(responseEvent);
	}
	
	static class GenerateUserTaskListenerImpl implements GenerateUserTaskListener {
		private final BeginDungeonResponseProto.Builder resBuilder;
		private final String userUuid;
		private final int tag;
		
		GenerateUserTaskListenerImpl(
			final MinimumUserProto senderProto, final int tag, final int taskId ) 
		{
			resBuilder = BeginDungeonResponseProto.newBuilder();
			resBuilder.setSender(senderProto);
			resBuilder.setTaskId(taskId);
			resBuilder.setStatus(BeginDungeonStatus.SUCCESS); //default

			userUuid = senderProto.getUserUuid();
			this.tag = tag;
		}
		
		BeginDungeonResponseEvent build() {
			return new BeginDungeonResponseEvent(userUuid, tag, resBuilder);
		}

		@Override
		public GenerateUserTaskListener beginUserTask(String userUuid, int taskId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GenerateUserTaskListener addUserTaskStage(int stageNum,
				Director<AddStageGenerateUserTaskListener> optionsDirector) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GenerateUserTaskListener endUserTask(String userUuid,
				int taskId, String userTaskUuid) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static class AddStageGenerateUserTaskListenerImpl implements AddStageGenerateUserTaskListener {
		public AddStageGenerateUserTaskListener onAddStageMonster(
			int stageNum, int monsterId, 
			int monsterLevel, float dmgMulti,
			int expGiven, int cashGiven, int oilGiven, 
			int droppedItemId, boolean puzzlePieceGiven
		) {
			/*TaskStageProto.newBuilder()
				.addStageMonsters(
					TaskStageMonsterProto.newBuilder()
						.setDmgMultiplier(1.0f)
						.setCashReward(cashGiven)
						.setExpReward(expGiven)
						.setItemId(droppedItemId)
						.setLevel(monsterLevel)
						.setMonsterId(monsterId)
						.setMonsterType(MonsterType.REGULAR)
				)*/
			return this;
		}
	}

	/******************************************************************************************/
	
	/* BEGIN Dependency Injection *************************************************************/
	
	void setTaskService( TaskService taskService )
	{
		this.taskService = taskService;
	}
}
