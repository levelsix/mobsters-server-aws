//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.MonsterForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonRequestProto;
//import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonResponseProto;
//import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonResponseProto.ReviveInDungeonStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.ReviveInDungeonRequestEvent;
//import com.lvl6.mobsters.events.response.ReviveInDungeonResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class ReviveInDungeonController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(ReviveInDungeonController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public ReviveInDungeonController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new ReviveInDungeonRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_REVIVE_IN_DUNGEON_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final ReviveInDungeonRequestProto reqProto =
//		    ((ReviveInDungeonRequestEvent) event).getReviveInDungeonRequestProto();
//
//		// get values sent from the client (the request proto)
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final long userTaskId = reqProto.getUserTaskUuid();
//		final Timestamp curTime = new Timestamp(reqProto.getClientTime());
//		final List<UserMonsterCurrentHealthProto> reviveMeProtoList =
//		    reqProto.getReviveMeList();
//		// positive value, need to convert to negative when updating user
//		final int gemsSpent = reqProto.getGemsSpent();
//
//		// set some values to send to the client (the response proto)
//		final ReviveInDungeonResponseProto.Builder resBuilder =
//		    ReviveInDungeonResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(ReviveInDungeonStatus.FAIL_OTHER); // default
//
//		svcTxManager.beginTransaction();
//		try {
//			final User aUser = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			int previousGems = 0;
//
//			// will be populated by checkLegit(...);
//			final Map<Long, Integer> userMonsterIdToExpectedHealth =
//			    new HashMap<Long, Integer>();
//
//			// List<TaskForUserOngoing> userTaskList = new
//			// ArrayList<TaskForUserOngoing>();
//			final boolean legit =
//			    checkLegit(resBuilder, aUser, userUuid, gemsSpent, userTaskId,
//			        reviveMeProtoList, userMonsterIdToExpectedHealth);// ,
//			                                                          // userTaskList);
//
//			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
//			boolean successful = false;
//			if (legit) {
//				// TaskForUserOngoing ut = userTaskList.get(0);
//				previousGems = aUser.getGems();
//				successful =
//				    writeChangesToDb(aUser, userUuid, userTaskId, gemsSpent, curTime,
//				        userMonsterIdToExpectedHealth, currencyChange);
//			}
//			if (successful) {
//				resBuilder.setStatus(ReviveInDungeonStatus.SUCCESS);
//			}
//
//			final ReviveInDungeonResponseEvent resEvent =
//			    new ReviveInDungeonResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setReviveInDungeonResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in ReviveInDungeonController.processRequestEvent", e);
//			}
//
//			if (successful) {
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in ReviveInDungeonController.processRequestEvent", e);
//				}
//				writeToUserCurrencyHistory(userUuid, aUser, userTaskId, curTime, previousGems,
//				    currencyChange);
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in ReviveInDungeonController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(ReviveInDungeonStatus.FAIL_OTHER);
//				final ReviveInDungeonResponseEvent resEvent =
//				    new ReviveInDungeonResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setReviveInDungeonResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in ReviveInDungeonController.processRequestEvent", e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in ReviveInDungeonController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	/*
//	 * Return true if user request is valid; false otherwise and set the builder
//	 * status to the appropriate value.
//	 */
//	private boolean checkLegit( final Builder resBuilder, final User u, final String userUuid,
//	    final int gemsSpent, final long userTaskId,
//	    final List<UserMonsterCurrentHealthProto> reviveMeProtoList,
//	    final Map<Long, Integer> userMonsterIdToExpectedHealth )
//	{// , List<TaskForUserOngoing> userTaskList) {
//		if (null == u) {
//			LOG.error("unexpected error: user is null. user="
//			    + u);
//			return false;
//		}
//		//
//		// //make sure user task exists
//		// TaskForUserOngoing ut =
//		// TaskForUserOngoingRetrieveUtils.getUserTaskForId(userTaskId);
//		// if (null == ut) {
//		// LOG.error("unexpected error: no user task for id userTaskId=" +
//		// userTaskId);
//		// return false;
//		// }
//
//		// extract the ids so it's easier to get userMonsters from db
//		final List<Long> userMonsterUuids =
//		    MonsterStuffUtils.getUserMonsterUuids(reviveMeProtoList,
//		        userMonsterIdToExpectedHealth);
//		final Map<Long, MonsterForUser> userMonsters =
//		    RetrieveUtils.monsterForUserRetrieveUtils()
//		        .getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids);
//
//		if ((null == userMonsters)
//		    || userMonsters.isEmpty()) {
//			LOG.error("unexpected error: userMonsterUuids don't exist. ids="
//			    + userMonsterUuids);
//			return false;
//		}
//
//		// see if the user has the equips
//		if (userMonsters.size() != reviveMeProtoList.size()) {
//			LOG.error("unexpected error: mismatch between user equips client sent and "
//			    + "what is in the db. clientUserMonsterUuids="
//			    + userMonsterUuids
//			    + "\t inDb="
//			    + userMonsters
//			    + "\t continuing the processing");
//		}
//
//		// make sure user has enough diamonds/gold?
//		final int userDiamonds = u.getGems();
//		final int cost = gemsSpent;
//		if (cost > userDiamonds) {
//			LOG.error("user error: user does not have enough diamonds to revive. "
//			    + "cost="
//			    + cost
//			    + "\t userDiamonds="
//			    + userDiamonds);
//			resBuilder.setStatus(ReviveInDungeonStatus.FAIL_INSUFFICIENT_FUNDS);
//			return false;
//		}
//
//		// userTaskList.add(ut);
//		resBuilder.setStatus(ReviveInDungeonStatus.SUCCESS);
//		return true;
//	}
//
//	private boolean writeChangesToDb( final User u, final int uId, final long userTaskId,
//	    final int gemsSpent, final Timestamp clientTime,
//	    final Map<Long, Integer> userMonsterIdToExpectedHealth,
//	    final Map<String, Integer> currencyChange )
//	{
//
//		// update user diamonds
//		final int gemsChange = -1
//		    * gemsSpent;
//		if (!updateUser(u, gemsChange)) {
//			LOG.error("unexpected error: could not decrement user's gold by "
//			    + gemsChange);
//			return false;
//		} else {
//			if (0 != gemsChange) {
//				currencyChange.put(MiscMethods.gems, gemsSpent);
//			}
//		}
//
//		// update num revives for user task
//		final int numRevivesDelta = 1;
//		int numUpdated = UpdateUtils.get()
//		    .incrementUserTaskNumRevives(userTaskId, numRevivesDelta);
//		if (1 != numUpdated) {
//			LOG.error("unexpected error: user_task not updated correctly. Attempting "
//			    + "to give back diamonds. userTaskId="
//			    + userTaskId
//			    + "\t numUpdated="
//			    + numUpdated
//			    + "\t user="
//			    + u);
//			// undo gold charge
//			if (!updateUser(u, -1
//			    * gemsChange)) {
//				LOG.error("unexpected error: could not change back user's gems by "
//				    + (-1 * gemsChange));
//			}
//			currencyChange.clear();
//			return false;
//		}
//
//		// HEAL THE USER MONSTERS
//		// replace existing health for these user monsters with new values
//		numUpdated = UpdateUtils.get()
//		    .updateUserMonstersHealth(userMonsterIdToExpectedHealth);
//
//		if (numUpdated >= userMonsterIdToExpectedHealth.size()) {
//			return true;
//		}
//		LOG.warn("unexpected error: not all user equips were updated. "
//		    + "actual numUpdated="
//		    + numUpdated
//		    + "expected: "
//		    + "userMonsterIdToExpectedHealth="
//		    + userMonsterIdToExpectedHealth);
//
//		return true;
//	}
//
//	private boolean updateUser( final User u, final int diamondChange )
//	{
//		if (!u.updateRelativeGemsNaive(diamondChange)) {
//			LOG.error("unexpected error: problem with updating user diamonds for reviving. diamondChange="
//			    + diamondChange
//			    + "user="
//			    + u);
//			return false;
//		}
//		return true;
//	}
//
//	private void writeToUserCurrencyHistory( final String userUuid, final User aUser,
//	    final long userTaskId, final Timestamp curTime, final int previousGems,
//	    final Map<String, Integer> currencyChange )
//	{
//
//		if (currencyChange.isEmpty()) {
//			return;
//		}
//
//		final StringBuilder detailsSb = new StringBuilder();
//		detailsSb.append("userTaskId=");
//		detailsSb.append(userTaskId);
//
//		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//		final String reason = ControllerConstants.UCHRFC__REVIVE_IN_DUNGEON;
//		final String gems = MiscMethods.gems;
//
//		previousCurrency.put(gems, previousGems);
//		currentCurrency.put(gems, aUser.getGems());
//		reasonsForChanges.put(gems, reason);
//		detailsMap.put(gems, detailsSb.toString());
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
//
//	}
//
//}
