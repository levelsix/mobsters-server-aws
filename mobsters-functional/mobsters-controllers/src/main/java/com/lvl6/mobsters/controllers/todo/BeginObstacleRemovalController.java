//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.ObstacleForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalRequestProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto.BeginObstacleRemovalStatus;
//import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto.Builder;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.BeginObstacleRemovalRequestEvent;
//import com.lvl6.mobsters.events.response.BeginObstacleRemovalResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//public class BeginObstacleRemovalController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(BeginObstacleRemovalController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Autowired
//	protected ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil;
//
//	public BeginObstacleRemovalController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new BeginObstacleRemovalRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_BEGIN_OBSTACLE_REMOVAL_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final BeginObstacleRemovalRequestProto reqProto =
//		    ((BeginObstacleRemovalRequestEvent) event).getBeginObstacleRemovalRequestProto();
//		LOG.info("reqProto="
//		    + reqProto);
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final Timestamp clientTime = new Timestamp(reqProto.getCurTime());
//		final int gemsSpent = reqProto.getGemsSpent();
//		final int resourceChange = reqProto.getResourceChange();
//		final ResourceType rt = reqProto.getResourceType();
//		final int userObstacleId = reqProto.getUserObstacleUuid();
//
//		final BeginObstacleRemovalResponseProto.Builder resBuilder =
//		    BeginObstacleRemovalResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(BeginObstacleRemovalStatus.FAIL_OTHER);
//
//		svcTxManager.beginTransaction();
//
//		try {
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(senderProto.getUserUuid());
//			final ObstacleForUser ofu =
//			    getObstacleForUserRetrieveUtil().getUserObstacleForId(userObstacleId);
//
//			final boolean legitComplete =
//			    checkLegit(resBuilder, userUuid, user, userObstacleId, ofu, gemsSpent,
//			        resourceChange, rt);
//
//			boolean success = false;
//			// make it easier to record currency history
//			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
//			final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
//			if (legitComplete) {
//				success =
//				    writeChangesToDB(user, userObstacleId, gemsSpent, resourceChange, rt,
//				        clientTime, currencyChange, previousCurrency);
//			}
//
//			final BeginObstacleRemovalResponseEvent resEvent =
//			    new BeginObstacleRemovalResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setBeginObstacleRemovalResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in BeginObstacleRemovalController.processRequestEvent", e);
//			}
//
//			if (success) {
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
//					    "fatal exception in BeginObstacleRemovalController.processRequestEvent",
//					    e);
//				}
//
//				writeToUserCurrencyHistory(userUuid, user, clientTime, currencyChange,
//				    previousCurrency, ofu, rt);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in BeginObstacleRemovalController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(BeginObstacleRemovalStatus.FAIL_OTHER);
//				final BeginObstacleRemovalResponseEvent resEvent =
//				    new BeginObstacleRemovalResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setBeginObstacleRemovalResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in BeginObstacleRemovalController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in BeginObstacleRemovalController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegit( final Builder resBuilder, final String userUuid,
//	    final User user, final int ofuId, final ObstacleForUser ofu, final int gemsSpent,
//	    final int resourceChange, final ResourceType rt )
//	{
//
//		if ((null == user)
//		    || (null == ofu)) {
//			resBuilder.setStatus(BeginObstacleRemovalStatus.FAIL_OTHER);
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
//		if (!hasEnoughGems(resBuilder, user, gemsSpent)) {
//			return false;
//		}
//
//		if (ResourceType.CASH.equals(rt)) {
//			if (!hasEnoughCash(resBuilder, user, resourceChange)) {
//				return false;
//			}
//		}
//
//		if (ResourceType.OIL.equals(rt)) {
//			if (!hasEnoughOil(resBuilder, user, resourceChange)) {
//				return false;
//			}
//		}
//
//		resBuilder.setStatus(BeginObstacleRemovalStatus.SUCCESS);
//		return true;
//	}
//
//	private boolean hasEnoughGems( final Builder resBuilder, final User u, final int gemsSpent )
//	{
//		final int userGems = u.getGems();
//		// if user's aggregate gems is < cost, don't allow transaction
//		if (userGems < gemsSpent) {
//			LOG.error("user error: user does not have enough gems. userGems="
//			    + userGems
//			    + "\t gemsSpent="
//			    + gemsSpent);
//			resBuilder.setStatus(BeginObstacleRemovalStatus.FAIL_INSUFFICIENT_GEMS);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean hasEnoughCash( final Builder resBuilder, final User u, final int cashSpent )
//	{
//		final int userCash = u.getCash();
//		// if user's aggregate cash is < cost, don't allow transaction
//		if (userCash < cashSpent) {
//			LOG.error("user error: user does not have enough cash. userCash="
//			    + userCash
//			    + "\t cashSpent="
//			    + cashSpent);
//			resBuilder.setStatus(BeginObstacleRemovalStatus.FAIL_INSUFFICIENT_RESOURCE);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean hasEnoughOil( final Builder resBuilder, final User u, final int oilSpent )
//	{
//		final int userOil = u.getOil();
//		// if user's aggregate oil is < cost, don't allow transaction
//		if (userOil < oilSpent) {
//			LOG.error("user error: user does not have enough oil. userOil="
//			    + userOil
//			    + "\t oilSpent="
//			    + oilSpent);
//			resBuilder.setStatus(BeginObstacleRemovalStatus.FAIL_INSUFFICIENT_RESOURCE);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean writeChangesToDB( final User user, final int ofuId, final int gemsSpent,
//	    final int resourceChange, final ResourceType rt, final Timestamp clientTime,
//	    final Map<String, Integer> currencyChange, final Map<String, Integer> previousCurrency )
//	{
//
//		// update user currency
//		final int gemsChange = -1
//		    * Math.abs(gemsSpent);
//		int cashChange = 0;
//		int oilChange = 0;
//
//		if (0 != gemsChange) {
//			previousCurrency.put(MiscMethods.gems, user.getGems());
//		}
//		if (ResourceType.CASH.equals(rt)) {
//			LOG.info("user spent cash.");
//			cashChange = resourceChange;
//			previousCurrency.put(MiscMethods.cash, user.getCash());
//		}
//		if (ResourceType.OIL.equals(rt)) {
//			LOG.info("user spent cash.");
//			oilChange = resourceChange;
//			previousCurrency.put(MiscMethods.oil, user.getOil());
//		}
//
//		if (!updateUser(user, gemsChange, cashChange, oilChange)) {
//			LOG.error("unexpected error: could not decrement user's gems by "
//			    + gemsChange
//			    + ", cash by "
//			    + cashChange
//			    + " or oil by "
//			    + oilChange);
//			return false;
//		} else {
//			if (0 != gemsChange) {
//				currencyChange.put(MiscMethods.gems, gemsChange);
//			}
//			if (0 != cashChange) {
//				currencyChange.put(MiscMethods.cash, cashChange);
//			}
//			if (0 != oilChange) {
//				currencyChange.put(MiscMethods.oil, oilChange);
//			}
//		}
//
//		final int numUpdated = UpdateUtils.get()
//		    .updateObstacleForUserRemovalTime(ofuId, clientTime);
//		LOG.info("(obstacles, should be 1) numUpdated="
//		    + numUpdated);
//		return true;
//	}
//
//	private boolean updateUser( final User u, final int gemsChange, final int cashChange,
//	    final int oilChange )
//	{
//		final int numChange =
//		    u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemsChange);
//
//		if (numChange <= 0) {
//			LOG.error("unexpected error: problem with updating user gems, cash, and oil. gemChange="
//			    + gemsChange
//			    + ", cash= "
//			    + cashChange
//			    + ", oil="
//			    + oilChange
//			    + " user="
//			    + u);
//			return false;
//		}
//		return true;
//	}
//
//	private void writeToUserCurrencyHistory( final String userUuid, final User user,
//	    final Timestamp curTime, final Map<String, Integer> currencyChange,
//	    final Map<String, Integer> previousCurrency, final ObstacleForUser ofu,
//	    final ResourceType rt )
//	{
//		if (currencyChange.isEmpty()) {
//			return;
//		}
//
//		final String reason = ControllerConstants.UCHRFC__REMOVE_OBSTACLE;
//		final StringBuilder detailsSb = new StringBuilder();
//		detailsSb.append("obstacleId=");
//		detailsSb.append(ofu.getObstacleId());
//		detailsSb.append(" x=");
//		detailsSb.append(ofu.getXcoord());
//		detailsSb.append(" y=");
//		detailsSb.append(ofu.getYcoord());
//		detailsSb.append(" resourceType=");
//		detailsSb.append(rt.name());
//		final String details = detailsSb.toString();
//
//		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//		final String gems = MiscMethods.gems;
//		final String cash = MiscMethods.cash;
//		final String oil = MiscMethods.oil;
//
//		if (currencyChange.containsKey(gems)) {
//			currentCurrency.put(gems, user.getGems());
//			reasonsForChanges.put(gems, reason);
//			detailsMap.put(gems, details);
//		}
//		if (currencyChange.containsKey(cash)) {
//			currentCurrency.put(cash, user.getCash());
//			reasonsForChanges.put(cash, reason);
//			detailsMap.put(cash, details);
//		}
//		if (currencyChange.containsKey(oil)) {
//			currentCurrency.put(oil, user.getOil());
//			reasonsForChanges.put(oil, reason);
//			detailsMap.put(oil, details);
//		}
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
