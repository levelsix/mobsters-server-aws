package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.TaskStageForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonRequestProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonResponseProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonResponseProto.EndDungeonStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.EndDungeonRequestEvent;
import com.lvl6.mobsters.events.response.EndDungeonResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.info.TaskStageMonster;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class EndDungeonController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(EndDungeonController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public EndDungeonController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new EndDungeonRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_END_DUNGEON_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final EndDungeonRequestProto reqProto =
		    ((EndDungeonRequestEvent) event).getEndDungeonRequestProto();
		LOG.info("reqProto="
		    + reqProto);

		// get values sent from the client (the request proto)
		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
		final String userUuid = senderProto.getUserUuid();
		final long userTaskId = reqProto.getUserTaskUuid();
		final boolean userWon = reqProto.getUserWon();
		final Date currentDate = new Date(reqProto.getClientTime());
		final Timestamp curTime = new Timestamp(reqProto.getClientTime());
		final boolean firstTimeUserWonTask = reqProto.getFirstTimeUserWonTask();
		final int maxCash = senderResourcesProto.getMaxCash();
		final int maxOil = senderResourcesProto.getMaxOil();

		// set some values to send to the client (the response proto)
		final EndDungeonResponseProto.Builder resBuilder = EndDungeonResponseProto.newBuilder();
		resBuilder.setSender(senderResourcesProto);
		resBuilder.setUserWon(userWon);
		resBuilder.setStatus(EndDungeonStatus.FAIL_OTHER); // default

		svcTxManager.beginTransaction();
		try {
			final User aUser = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			int previousCash = 0;
			int previousOil = 0;

			final TaskForUserOngoing ut =
			    TaskForUserOngoingRetrieveUtils.getUserTaskForId(userTaskId);
			final boolean legit = checkLegit(resBuilder, aUser, userUuid, userTaskId, ut);

			boolean successful = false;
			final Map<String, Integer> money = new HashMap<String, Integer>();
			if (legit) {
				previousCash = aUser.getCash();
				previousOil = aUser.getOil();
				successful =
				    writeChangesToDb(aUser, userUuid, ut, userWon, curTime, money, maxCash,
				        maxOil);

				resBuilder.setTaskId(ut.getTaskId());
			}
			if (successful) {
				final long taskForUserId = ut.getId();
				// the things to delete and store into history
				final List<TaskStageForUser> tsfuList =
				    TaskStageForUserRetrieveUtils.getTaskStagesForUserWithTaskForUserId(taskForUserUuid);

				// delete from task_stage_for_user and put into
				// task_stage_history
				final Map<Integer, Integer> monsterIdToNumPieces =
				    new HashMap<Integer, Integer>();
				// TODO: record (items(?))
				// Map<Integer, Integer> itemIdToQuantity = new HashMap<Integer,
				// Integer>();
				recordStageHistory(tsfuList, monsterIdToNumPieces);

				if (userWon) {
					LOG.info("user won dungeon, awarding the monsters and items");
					// update user's monsters
					final StringBuilder mfusopB = new StringBuilder();
					mfusopB.append(ControllerConstants.MFUSOP__END_DUNGEON);
					mfusopB.append(" ");
					mfusopB.append(taskForUserId);
					final String mfusop = mfusopB.toString();
					final List<FullUserMonsterProto> newOrUpdated =
					    MonsterStuffUtils.updateUserMonsters(userUuid, monsterIdToNumPieces,
					        mfusop, currentDate);
					setResponseBuilder(resBuilder, newOrUpdated);

					// //MAYBE NEED TO SEND THESE TO THE CLIENT?
					// Map<Integer, ItemForUser> itemUuidsToItems =
					// updateUserItems(tsfuList, userUuid);

				}

			}

			final EndDungeonResponseEvent resEvent = new EndDungeonResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setEndDungeonResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in EndDungeonController.processRequestEvent", e);
			}

			if (successful) {
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error("fatal exception in EndDungeonController.processRequestEvent", e);
				}
				final int taskId = ut.getTaskId();
				writeToUserCurrencyHistory(aUser, curTime, userTaskId, taskId, previousCash,
				    previousOil, money);
				writeToTaskForUserCompleted(userUuid, taskId, userWon, firstTimeUserWonTask,
				    curTime);
			}

		} catch (final Exception e) {
			LOG.error("exception in EndDungeonController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(EndDungeonStatus.FAIL_OTHER);
				final EndDungeonResponseEvent resEvent = new EndDungeonResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setEndDungeonResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in EndDungeonController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in EndDungeonController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	/*
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value.
	 */
	private boolean checkLegit( final Builder resBuilder, final User u, final String userUuid,
	    final long userTaskId, final TaskForUserOngoing ut )
	{
		if (null == u) {
			LOG.error("unexpected error: user is null. user="
			    + u);
			return false;
		}

		if (null == ut) {
			LOG.error("unexpected error: no user task for id userTaskId="
			    + userTaskId);
			return false;
		}

		resBuilder.setStatus(EndDungeonStatus.SUCCESS);
		return true;
	}

	private boolean writeChangesToDb( final User u, final int uId, final TaskForUserOngoing ut,
	    final boolean userWon, final Timestamp clientTime, final Map<String, Integer> money,
	    final int maxCash, final int maxOil )
	{
		int cashGained = ut.getCashGained();
		final int expGained = ut.getExpGained();
		int oilGained = ut.getOilGained();

		final int curCash = Math.min(u.getCash(), maxCash); // in case user's
															// cash is
		// more than maxCash
		final int maxCashUserCanGain = maxCash
		    - curCash;
		cashGained = Math.min(cashGained, maxCashUserCanGain);

		final int curOil = Math.min(u.getOil(), maxOil);
		final int maxOilUserCanGain = maxOil
		    - curOil;
		oilGained = Math.min(oilGained, maxOilUserCanGain);

		LOG.info("user before currency change. "
		    + u);
		if (userWon) {
			LOG.info("user WON DUNGEON!!!!!!!!. ");
			// update user cash and experience
			if (!updateUser(u, expGained, cashGained, oilGained, clientTime)) {
				return false;
			} else {
				if (0 != cashGained) {
					money.put(MiscMethods.cash, cashGained);
				}
				if (0 != oilGained) {
					money.put(MiscMethods.oil, oilGained);
				}
			}
		}
		LOG.info("user after currency change. "
		    + u);
		// TODO: MOVE THIS INTO A UTIL METHOD FOR TASK
		// delete from user_task and insert it into user_task_history
		final long utId = ut.getId();
		final int tId = ut.getTaskId();
		final int numRevives = ut.getNumRevives();
		final Date startDate = ut.getStartDate();
		final long startMillis = startDate.getTime();
		final Timestamp startTime = new Timestamp(startMillis);
		final boolean cancelled = false;
		final int tsId = ut.getTaskStageId();
		int num =
		    InsertUtils.get()
		        .insertIntoTaskHistory(utId, uId, tId, expGained, cashGained, oilGained,
		            numRevives, startTime, clientTime, userWon, cancelled, tsId);
		if (1 != num) {
			LOG.error("unexpected error: error when inserting into user_task_history. "
			    + "numInserted="
			    + num
			    + " Attempting to undo shi");
			updateUser(u, -1
			    * expGained, -1
			    * cashGained, -1
			    * oilGained, clientTime);
			return false;
		}

		// DELETE FROM TASK_FOR_USER TABLE
		num = DeleteUtils.get()
		    .deleteTaskForUserOngoingWithTaskForUserUuid(utId);
		LOG.info("num rows deleted from task_for_user table. num="
		    + num);

		return true;
	}

	private boolean updateUser( final User u, final int expGained, final int cashGained,
	    final int oilGained, final Timestamp clientTime )
	{
		final int energyChange = 0;
		if (!u.updateRelativeCashOilExpTasksCompleted(expGained, cashGained, oilGained, 1,
		    clientTime)) {
			LOG.error("problem with updating user stats post-task. expGained="
			    + expGained
			    + ", cashGained="
			    + cashGained
			    + ", oilGained="
			    + oilGained
			    + ", increased"
			    + " tasks completed by 1, energyChange="
			    + energyChange
			    + ", clientTime="
			    + clientTime
			    + ", user="
			    + u);
			return false;
		}
		return true;
	}

	//
	private void recordStageHistory( final List<TaskStageForUser> tsfuList,
	    final Map<Integer, Integer> monsterIdToNumPieces )
	{
		// keep track of how many pieces dropped and by which task stage monster
		final Map<Integer, Integer> taskStageMonsterIdToQuantity =
		    new HashMap<Integer, Integer>();

		// collections to hold values to be saved to the db
		final List<Long> userTaskStageId = new ArrayList<Long>();
		final List<Long> userTaskId = new ArrayList<Long>();
		final List<Integer> stageNum = new ArrayList<Integer>();
		final List<Integer> taskStageMonsterIdList = new ArrayList<Integer>();
		final List<String> monsterTypes = new ArrayList<String>();
		final List<Integer> expGained = new ArrayList<Integer>();
		final List<Integer> cashGained = new ArrayList<Integer>();
		final List<Integer> oilGained = new ArrayList<Integer>();
		final List<Boolean> monsterPieceDropped = new ArrayList<Boolean>();
		final List<Integer> itemIdDropped = new ArrayList<Integer>();

		for (int i = 0; i < tsfuList.size(); i++) {
			final TaskStageForUser tsfu = tsfuList.get(i);
			userTaskStageId.add(tsfu.getId());
			userTaskId.add(tsfu.getUserTaskId());
			stageNum.add(tsfu.getStageNum());

			final int taskStageMonsterId = tsfu.getTaskStageMonsterId();
			taskStageMonsterIdList.add(taskStageMonsterId);
			monsterTypes.add(tsfu.getMonsterType());
			expGained.add(tsfu.getExpGained());
			cashGained.add(tsfu.getCashGained());
			oilGained.add(tsfu.getOilGained());
			final boolean dropped = tsfu.isMonsterPieceDropped();
			monsterPieceDropped.add(dropped);
			itemIdDropped.add(tsfu.getItemIdDropped());

			if (!dropped) {
				// not going to keep track of non dropped monster pieces
				continue;
			}

			// since monster piece dropped, update our current stats on monster
			// pieces
			// this was done under the assumption that one task stage could have
			// more than one task stage monster (otheriwse only the else case
			// would execute)
			if (taskStageMonsterIdToQuantity.containsKey(taskStageMonsterId)) {
				// saw this task stage monster id before, increment quantity
				final int quantity = 1 + taskStageMonsterIdToQuantity.get(taskStageMonsterId);
				taskStageMonsterIdToQuantity.put(taskStageMonsterId, quantity);

			} else {
				// haven't seen this task stage monster id yet, so start off at
				// 1
				taskStageMonsterIdToQuantity.put(taskStageMonsterId, 1);
			}
		}

		int num =
		    InsertUtils.get()
		        .insertIntoTaskStageHistory(userTaskStageId, userTaskId, stageNum,
		            taskStageMonsterIdList, monsterTypes, expGained, cashGained, oilGained,
		            monsterPieceDropped, itemIdDropped);
		LOG.info("num task stage history rows inserted: num="
		    + num
		    + "taskStageForUser="
		    + tsfuList);

		// DELETE FROM TASK_STAGE_FOR_USER
		num = DeleteUtils.get()
		    .deleteTaskStagesForUserWithUuids(userTaskStageId);
		LOG.info("num task stage for user rows deleted: num="
		    + num);

		// retrieve those task stage monsters. aggregate the quantities by
		// monster id
		// assume different task stage monsters can be the same monster
		final Collection<Integer> taskStageMonsterUuids = taskStageMonsterIdToQuantity.keySet();
		final Map<Integer, TaskStageMonster> monstersThatDropped =
		    TaskStageMonsterRetrieveUtils.getTaskStageMonstersForUuids(taskStageMonsterUuids);

		for (final int taskStageMonsterId : taskStageMonsterUuids) {
			final TaskStageMonster monsterThatDropped =
			    monstersThatDropped.get(taskStageMonsterId);
			final int monsterId = monsterThatDropped.getMonsterId();
			final int numPiecesDroppedForMonster =
			    taskStageMonsterIdToQuantity.get(taskStageMonsterId);

			// aggregate pieces based on monsterId, since assuming different
			// task
			// stage monsters can be the same monster
			if (monsterIdToNumPieces.containsKey(monsterId)) {
				final int newAmount = numPiecesDroppedForMonster
				    + monsterIdToNumPieces.get(monsterId);
				monsterIdToNumPieces.put(monsterId, newAmount);

			} else {
				// first time seeing this monster, store existing quantity
				monsterIdToNumPieces.put(monsterId, numPiecesDroppedForMonster);
			}
		}
	}

	private void setResponseBuilder( final Builder resBuilder,
	    final List<FullUserMonsterProto> protos )
	{
		resBuilder.setStatus(EndDungeonStatus.SUCCESS);

		if (!protos.isEmpty()) {
			resBuilder.addAllUpdatedOrNew(protos);
		}
	}

	// not using this method because how many items the user has is kept track
	// through
	// quest progress
	/*
	 * private Map<Integer, ItemForUser> updateUserItems(List<TaskStageForUser>
	 * tsfuList, String userUuid) { Map<Integer, Integer> itemUuidsToQuantities
	 * = getItemsDropped(tsfuList);
	 * 
	 * //retrieve these specific items for the user, so as to update them
	 * Collection<Integer> itemUuids = itemUuidsToQuantities.keySet();
	 * 
	 * Map<Integer, ItemForUser> itemIdToUserItem = ItemForUserRetrieveUtils
	 * .getSpecificOrAllUserItems(userUuid, itemUuids);
	 * LOG.info("items user won: " + itemUuidsToQuantities);
	 * LOG.info("existing items before modification: " + itemIdToUserItem);
	 * //update how many items the user has now
	 * 
	 * for (Integer itemId : itemUuids) { //get cur amount of items
	 * 
	 * //check base case if (!itemIdToUserItem.containsKey(itemId)) { //first
	 * time user is getting this item ItemForUser ifu = new
	 * ItemForUser(userUuid, itemId, 0); itemIdToUserItem.put(itemId, ifu); }
	 * 
	 * ItemForUser ifu = itemIdToUserItem.get(itemId); int curAmount =
	 * ifu.getQuantity();
	 * 
	 * //update it int delta = itemUuidsToQuantities.get(itemId); curAmount +=
	 * delta; ifu.setQuantity(curAmount); }
	 * 
	 * 
	 * int numUpdated = UpdateUtils.get().updateUserItems(userUuid,
	 * itemIdToUserItem); LOG.info("existing items after modification: " +
	 * itemIdToUserItem + "\t numUpdated=" + numUpdated);
	 * 
	 * 
	 * return itemIdToUserItem; }
	 * 
	 * //go through list of task stage for user, aggregate all the item ids with
	 * non zero //quantities private Map<Integer, Integer>
	 * getItemsDropped(List<TaskStageForUser> tsfuList) { Map<Integer, Integer>
	 * itemUuidsToQuantities = new HashMap<Integer, Integer>();
	 * 
	 * for (TaskStageForUser tsfu : tsfuList) { //if item dropped, add it in
	 * with the others int itemIdDropped = tsfu.getItemIdDropped();
	 * 
	 * if (itemIdDropped <= 0) { //item didn't drop continue; }
	 * 
	 * int quantity = 0;
	 * 
	 * //if item dropped before, get how much dropped if
	 * (itemUuidsToQuantities.containsKey(itemIdDropped)) { quantity =
	 * itemUuidsToQuantities.get(itemIdDropped); } //update quantity++;
	 * itemUuidsToQuantities.put(itemIdDropped, quantity); }
	 * 
	 * return itemUuidsToQuantities; }
	 */

	private void writeToUserCurrencyHistory( final User aUser, final Timestamp curTime,
	    final long userTaskId, final int taskId, final int previousCash, final int previousOil,
	    final Map<String, Integer> money )
	{
		if (money.isEmpty()) {
			return;
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("userTask=");
		sb.append(userTaskId);
		sb.append(" taskId=");
		sb.append(taskId);
		final String cash = MiscMethods.cash;
		final String oil = MiscMethods.oil;
		final String reasonForChange = ControllerConstants.UCHRFC__END_TASK;

		final String userUuid = aUser.getId();
		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();

		if (money.containsKey(cash)) {
			previousCurrencies.put(cash, previousCash);
		}
		if (money.containsKey(oil)) {
			previousCurrencies.put(oil, previousOil);
		}

		currentCurrencies.put(cash, aUser.getCash());
		currentCurrencies.put(oil, aUser.getOil());
		reasonsForChanges.put(cash, reasonForChange);
		reasonsForChanges.put(oil, reasonForChange);
		detailsMap.put(cash, sb.toString());
		detailsMap.put(oil, sb.toString());
		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, money, previousCurrencies,
		    currentCurrencies, reasonsForChanges, detailsMap);

	}

	private void writeToTaskForUserCompleted( final String userUuid, final int taskId,
	    final boolean userWon, final boolean firstTimeUserWonTask, final Timestamp now )
	{
		if (userWon
		    && firstTimeUserWonTask) {
			final int numInserted = InsertUtils.get()
			    .insertIntoTaskForUserCompleted(userUuid, taskId, now);

			LOG.info("numInserted into task_for_user_completed: "
			    + numInserted);
		}
	}

}
