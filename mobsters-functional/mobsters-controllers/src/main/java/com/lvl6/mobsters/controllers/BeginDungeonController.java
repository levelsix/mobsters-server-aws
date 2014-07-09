
// package com.lvl6.mobsters.controllers;
//
// import java.sql.Timestamp;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
// import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
// import com.lvl6.mobsters.dynamo.User;
// import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonRequestProto;
// import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto;
// import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto.Builder;
// import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto.BeginDungeonStatus;
// import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto;
// import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto.SetGameCenterIdStatus;
// import com.lvl6.mobsters.events.ControllerResponseEvents;
// import com.lvl6.mobsters.events.RequestEvent;
// import com.lvl6.mobsters.events.request.BeginDungeonRequestEvent;
// import com.lvl6.mobsters.events.response.SetGameCenterIdResponseEvent;
// import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
// import com.lvl6.mobsters.info.Task;
// import com.lvl6.mobsters.info.TaskStage;
// import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
// import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Element;
// import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
// import com.lvl6.mobsters.server.EventController;
// import com.lvl6.mobsters.services.taskforuserongoing.TaskForUserOngoingService;
// import com.lvl6.mobsters.services.common.TimeUtils;
// import com.lvl6.mobsters.services.user.UserService;
//
// @Component
// public class BeginDungeonController extends EventController {
//
// private static Logger log = LoggerFactory.getLogger(new Object() {
// }.getClass().getEnclosingClass());
//
// @Autowired
// protected UserService userService;
//
// @Autowired
// protected TaskForUserOngoingService taskForUserOngoingService;
//
// /* @Autowired
// protected EventWriter eventWriter;*/
//
// public BeginDungeonController() {
// }
//
// @Override
// public RequestEvent createRequestEvent() {
// return new BeginDungeonRequestEvent();
// }
//
// @Override
// public EventProtocolRequest getEventType() {
// return EventProtocolRequest.C_BEGIN_DUNGEON_EVENT;
// }
//
// @Override
// protected void processRequestEvent(RequestEvent event, ControllerResponseEvents eventWriter) throws
// Exception {
// BeginDungeonRequestProto reqProto = ((BeginDungeonRequestEvent) event)
// .getBeginDungeonRequestProto();
//
// MinimumUserProto senderProto = reqProto.getSender();
// String userId = senderProto.getUserUuid();
//        final Date clientTime = 
//        	TimeUtils.createDateFromTime(
//        		reqProto.getClientTime());
// int taskId = reqProto.getTaskId();
//
// //if is event, start the cool down timer in event_persistent_for_user
// boolean isEvent = reqProto.getIsEvent();
// int eventId = reqProto.getPersistentEventId();
// int gemsSpent = reqProto.getGemsSpent();
//
// //active quests a user has, this is to allow monsters to drop something
// //other than a piece of themselves (quest_monster_item)
// List<Integer> questIds = reqProto.getQuestIdsList();
//
// //used for element tutorial, client sets what enemy monster element should appear
// //and only that one guy should appear (quest tasks should have only one stage in db)
// Element elem = reqProto.getElem();
// // if not set, then go select monsters at random
// boolean forceEnemyElem = reqProto.getForceEnemyElem();
//
// //set some values to send to the client (the response proto)
// BeginDungeonResponseProto.Builder resBuilder = BeginDungeonResponseProto.newBuilder();
// resBuilder.setSender(senderProto);
// resBuilder.setTaskId(taskId);
// resBuilder.setStatus(BeginDungeonStatus.FAIL_OTHER); //default
//
// try {
// //get whatever is needed from the db
// User aUser = getUserService().getUserByUserId(userId);
// Task aTask = null; //TODO: POPULATE WITH CALL TO SERVICE CLASS
// Map<Integer, TaskStage> tsMap = null; //TODO: CALL SERVICE CLASS
//
// boolean legit = checkLegit(resBuilder, aUser, userId, aTask,
// taskId, tsMap, curTime, isEvent, eventId, gemsSpent);
//
// user.setGameCenterId(gameCenterId);
// getUserService().save(user);
// resBuilder.setStatus(SetGameCenterIdStatus.SUCCESS);
// //resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
//
// SetGameCenterIdResponseProto resProto = resBuilder.build();
// SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(senderProto.getUserUuid());
// resEvent.setSetGameCenterIdResponseProto(resProto);
// eventWriter.writeEvent(resEvent);
//
// // game center id might have changed
// // null PvpLeagueFromUser means will pull from hazelcast instead
//
//
// // TODO: Make a service for this in mobsters-services
// UpdateClientUserResponseEvent resEventUpdate = null;//
// MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null);
// resEventUpdate.setTag(event.getTag());
// eventWriter.writeEvent(resEventUpdate);
//
// } catch(ConditionalCheckFailedException e) {
// //TODO: version was probably out of date... meaning some other thread updated this item after you
// loaded it but before you save it
// //handle this case
// //need to reread and do some more logic, basically call this method again...
//
// }
// catch (Exception e) {
// log.error("exception in SetGameCenterIdController processEvent", e);
// // don't let the client hang
// try {
// eventWriter.clearResponses();
// resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
// SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(userId);
// resEvent.setTag(event.getTag());
// resEvent.setSetGameCenterIdResponseProto(resBuilder.build());
// eventWriter.writeEvent(resEvent);
// } catch (Exception e2) {
// log.error("exception2 in SetGameCenterIdController processEvent", e);
// }
// }
// }
//
// private boolean checkLegit(Builder resBuilder, User u, String userId, Task aTask,
// int taskId, Date clientDate, Map<Integer, TaskStage> tsMap, boolean isEvent,
// int eventId, int gemsSpent) {
// if (null == u || null == aTask) {
// log.error("unexpected error: user or task is null. user=" + u + "\t task="+ aTask);
// return false;
// }
// if (null == tsMap) {
// log.error("unexpected error: task has no taskStages. task=" + aTask);
// return false;
// }
//
// TaskForUserOngoing existing = getTaskForUserOngoingService()
// .getAllUserTaskForUser(userId);
// if (null != existing) {
// log.warn("(will continue processing, but) user has existing task when" +
// " beginning another. No task should exist. user=" + u + "\t task=" +
// aTask + "\t userTask=" + existing);
//
// //DELETE TASK AND PUT IT INTO TASK HISTORY
// //could put all this logic into TaskForUserOngoingService, but making it
// //so that other classes can use it.
// UUID userTaskId = existing.getId();
// boolean userWon = false;
// boolean cancelled = true;
// getTaskForUserOngoingService().deleteExistingUserTask(userTaskId, clientDate,
// userWon, cancelled, existing);
// //DELETE FROM TASK STAGE FOR USER AND PUT IT INTO TASK STAGE HISTORY,
// //don't want to find out what monster pieces the user could have gotten
// boolean getMonsterPieces = false;
// Map<Integer, Integer> monsterIdToNumPieces = null;
// getTaskStageForUserService().deleteExistingTaskStagesForUserTaskId(userTaskId,
// getMonsterPieces, monsterIdToNumPieces);
// }
//
// //TODO: if event, maybe somehow check if user has enough gems to reset event
// //right now just relying on client
// // if (isEvent) {
// // if (eventId > 0) {
// //
// // } else {
// // log.error("isEvent set to true but eventId not positive " + "\t eventId=" +
// // eventId + "\t gemsSpent=" + gemsSpent);
// // return false;
// // }
// // }
//
// resBuilder.setStatus(BeginDungeonStatus.SUCCESS);
// return true;
// }
//
//
//
//
// public UserService getUserService() {
// return userService;
// }
//
// public void setUserService(
// UserService userService) {
// this.userService = userService;
// }
//
// public TaskForUserOngoingService getTaskForUserOngoingService() {
// return taskForUserOngoingService;
// }
//
// public void setTaskForUserOngoingService(
// TaskForUserOngoingService taskForUserOngoingService) {
// this.taskForUserOngoingService = taskForUserOngoingService;
// }
//
// }
