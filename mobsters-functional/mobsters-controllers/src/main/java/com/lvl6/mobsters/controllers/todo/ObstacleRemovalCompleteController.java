//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.ObstacleForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteRequestProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto.ObstacleRemovalCompleteStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.ObstacleRemovalCompleteRequestEvent;
//import com.lvl6.mobsters.events.response.ObstacleRemovalCompleteResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class ObstacleRemovalCompleteController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(ObstacleRemovalCompleteController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Autowired
//	protected ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil;
//
//	public ObstacleRemovalCompleteController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new ObstacleRemovalCompleteRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_OBSTACLE_REMOVAL_COMPLETE_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final ObstacleRemovalCompleteRequestProto reqProto =
//		    ((ObstacleRemovalCompleteRequestEvent) event).getObstacleRemovalCompleteRequestProto();
//
//		LOG.info("reqProto="
//		    + reqProto);
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final Timestamp clientTime = new Timestamp(reqProto.getCurTime());
//		final boolean speedUp = reqProto.getSpeedUp();
//		final int gemCostToSpeedUp = reqProto.getGemsSpent();
//		final int userObstacleId = reqProto.getUserObstacleUuid();
//		final boolean atMaxObstacles = reqProto.getAtMaxObstacles();
//
//		final ObstacleRemovalCompleteResponseProto.Builder resBuilder =
//		    ObstacleRemovalCompleteResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(ObstacleRemovalCompleteStatus.FAIL_OTHER);
//
//		svcTxManager.beginTransaction();
//		try {
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(senderProto.getUserUuid());
//			final ObstacleForUser ofu =
//			    getObstacleForUserRetrieveUtil().getUserObstacleForId(userObstacleId);
//
//			final boolean legitExpansionComplete =
//			    checkLegit(resBuilder, userUuid, user, userObstacleId, ofu, speedUp,
//			        gemCostToSpeedUp);
//			int previousGems = 0;
//
//			boolean success = false;
//			final Map<String, Integer> money = new HashMap<String, Integer>();
//			if (legitExpansionComplete) {
//				previousGems = user.getGems();
//				success =
//				    writeChangesToDB(user, userObstacleId, speedUp, gemCostToSpeedUp,
//				        clientTime, atMaxObstacles, money);
//			}
//
//			final ObstacleRemovalCompleteResponseEvent resEvent =
//			    new ObstacleRemovalCompleteResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setObstacleRemovalCompleteResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in ObstacleRemovalCompleteController.processRequestEvent",
//				    e);
//			}
//
//			if (success
//			    && (speedUp || atMaxObstacles)) {
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in ObstacleRemovalCompleteController.processRequestEvent",
//					    e);
//				}
//
//				writeToUserCurrencyHistory(userUuid, user, clientTime, money, previousGems, ofu);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in ObstacleRemovalCompleteController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(ObstacleRemovalCompleteStatus.FAIL_OTHER);
//				final ObstacleRemovalCompleteResponseEvent resEvent =
//				    new ObstacleRemovalCompleteResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setObstacleRemovalCompleteResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in ObstacleRemovalCompleteController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in ObstacleRemovalCompleteController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegit( final Builder resBuilder, final String userUuid,
//	    final User user, final int ofuId, final ObstacleForUser ofu, final boolean speedUp,
//	    final int gemCostToSpeedup )
//	{
//
//		if ((null == user)
//		    || (null == ofu)) {
//			resBuilder.setStatus(ObstacleRemovalCompleteStatus.FAIL_OTHER);
//			LOG.error("unexpected error: user or obstacle for user is null. user="
//			    + user
//			    + "\t userUuid="
//			    + userUuid
//			    + "\t obstacleForUser="
//			    + ofu
//			    + "\t ofuId="
//			    + ofuId);
//			return false;
//		}
//
//		if (speedUp
//		    && (user.getGems() < gemCostToSpeedup)) {
//			resBuilder.setStatus(ObstacleRemovalCompleteStatus.FAIL_INSUFFICIENT_GEMS);
//			LOG.error("user error: user does not have enough gems to speed up removal."
//			    + "\t obstacleForUser="
//			    + ofu
//			    + "\t cost="
//			    + gemCostToSpeedup);
//			return false;
//		}
//
//		resBuilder.setStatus(ObstacleRemovalCompleteStatus.SUCCESS);
//		return true;
//	}
//
//	private boolean writeChangesToDB( final User user, final int ofuId, final boolean speedUp,
//	    final int gemCost, Timestamp clientTime, final boolean atMaxObstacles,
//	    final Map<String, Integer> money )
//	{
//		int gemChange = -1
//		    * gemCost;
//		final int obstaclesRemovedDelta = 1;
//
//		if (speedUp
//		    && atMaxObstacles) {
//			LOG.info("isSpeedup and maxObstacles");
//		} else if (speedUp) {
//			LOG.info("isSpeedup");
//			clientTime = null;
//		} else if (atMaxObstacles) {
//			LOG.info("maxObstacles");
//			gemChange = 0;
//			// if user at max obstacles and removes one, a new obstacle
//			// should spawn in the amount of time it takes to spawn one, not
//			// right when user clears obstacle
//		} else {
//			gemChange = 0;
//			clientTime = null;
//			LOG.info("not isSpeedup and not maxObstacles");
//		}
//
//		if (!user.updateRelativeGemsObstacleTimeNumRemoved(gemChange, clientTime,
//		    obstaclesRemovedDelta)) {
//			LOG.error("problem updating user gems. gemChange="
//			    + gemChange);
//			return false;
//		} else {
//			// everything went ok
//			if (0 != gemChange) {
//				money.put(MiscMethods.gems, gemChange);
//			}
//		}
//
//		final int numDeleted = DeleteUtils.get()
//		    .deleteObstacleForUser(ofuId);
//		LOG.info("(obstacles) numDeleted="
//		    + numDeleted);
//		return true;
//	}
//
//	private void writeToUserCurrencyHistory( final String userUuid, final User user,
//	    final Timestamp curTime, final Map<String, Integer> currencyChange,
//	    final int previousGems, final ObstacleForUser ofu )
//	{
//		final String reason = ControllerConstants.UCHRFC__SPED_UP_REMOVE_OBSTACLE;
//		final StringBuilder detailsSb = new StringBuilder();
//		detailsSb.append("obstacleId=");
//		detailsSb.append(ofu.getObstacleId());
//		detailsSb.append(" x=");
//		detailsSb.append(ofu.getXcoord());
//		detailsSb.append(" y=");
//		detailsSb.append(ofu.getYcoord());
//		final String details = detailsSb.toString();
//
//		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//		final String gems = MiscMethods.gems;
//
//		previousCurrency.put(gems, previousGems);
//		currentCurrency.put(gems, user.getGems());
//		reasonsForChanges.put(gems, reason);
//		detailsMap.put(gems, details);
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
//
//	}
//
//	public ObstacleForUserRetrieveUtil getObstacleForUserRetrieveUtil()
//	{
//		return obstacleForUserRetrieveUtil;
//	}
//
//	public void setObstacleForUserRetrieveUtil(
//	    final ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil )
//	{
//		this.obstacleForUserRetrieveUtil = obstacleForUserRetrieveUtil;
//	}
//
//}
