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
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyRequestProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto.UpdateUserCurrencyStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.UpdateUserCurrencyRequestEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateUserCurrencyResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class UpdateUserCurrencyController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(UpdateUserCurrencyController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public UpdateUserCurrencyController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new UpdateUserCurrencyRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_UPDATE_USER_CURRENCY_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final UpdateUserCurrencyRequestProto reqProto =
//		    ((UpdateUserCurrencyRequestEvent) event).getUpdateUserCurrencyRequestProto();
//
//		// get values sent from the client (the request proto)
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//
//		// both positive numbers, server will change to negative
//		final int cashSpent = reqProto.getCashSpent();
//		final int oilSpent = reqProto.getOilSpent();
//		final int gemsSpent = reqProto.getGemsSpent();
//
//		final String reason = reqProto.getReason();
//		final String details = reqProto.getDetails();
//		final Timestamp clientTime = new Timestamp(reqProto.getClientTime());
//
//		// set some values to send to the client (the response proto)
//		final UpdateUserCurrencyResponseProto.Builder resBuilder =
//		    UpdateUserCurrencyResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(UpdateUserCurrencyStatus.FAIL_OTHER); // default
//
//		svcTxManager.beginTransaction();
//		try {
//			final User aUser = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			int previousGems = 0;
//			int previousCash = 0;
//			int previousOil = 0;
//
//			final boolean legit =
//			    checkLegit(resBuilder, aUser, userUuid, cashSpent, oilSpent, gemsSpent);
//
//			boolean successful = false;
//			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
//			if (legit) {
//				previousGems = aUser.getGems();
//				previousCash = aUser.getCash();
//				previousOil = aUser.getOil();
//
//				successful =
//				    writeChangesToDb(aUser, userUuid, cashSpent, oilSpent, gemsSpent,
//				        clientTime, currencyChange);
//			}
//			if (successful) {
//				resBuilder.setStatus(UpdateUserCurrencyStatus.SUCCESS);
//			}
//
//			final UpdateUserCurrencyResponseEvent resEvent =
//			    new UpdateUserCurrencyResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setUpdateUserCurrencyResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in UpdateUserCurrencyController.processRequestEvent", e);
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
//					    "fatal exception in UpdateUserCurrencyController.processRequestEvent",
//					    e);
//				}
//
//				writeToUserCurrencyHistory(aUser, currencyChange, clientTime, previousGems,
//				    previousCash, previousOil, reason, details);
//
//			}
//
//			// cheat code, reset user account
//			if ((1234 == cashSpent)
//			    && (1234 == oilSpent)
//			    && (1234 == gemsSpent)) {
//				LOG.info("resetting user "
//				    + aUser);
//				aUser.updateResetAccount();
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in UpdateUserCurrencyController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(UpdateUserCurrencyStatus.FAIL_OTHER);
//				final UpdateUserCurrencyResponseEvent resEvent =
//				    new UpdateUserCurrencyResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setUpdateUserCurrencyResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in UpdateUserCurrencyController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in UpdateUserCurrencyController processEvent", e);
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
//	    final int cashSpent, final int oilSpent, final int gemsSpent )
//	{
//		if (null == u) {
//			LOG.error("unexpected error: user is null. user="
//			    + u);
//			return false;
//		}
//
//		if ((cashSpent != Math.abs(cashSpent))
//		    || (oilSpent != Math.abs(oilSpent))
//		    || (gemsSpent != Math.abs(gemsSpent))) {
//			LOG.error("client sent a negative value! all should be positive :(  cashSpent="
//			    + cashSpent
//			    + "\t oilSpent="
//			    + oilSpent
//			    + "\t gemsSpent="
//			    + gemsSpent);
//			if (u.isAdmin()) {
//				LOG.info("it's alright. User is admin.");
//			} else {
//				return false;
//			}
//		}
//
//		// CHECK MONEY
//		if (!hasEnoughCash(resBuilder, u, cashSpent)) {
//			if (u.isAdmin()) {
//				LOG.info("it's alright. User is admin.");
//			} else {
//				return false;
//			}
//		}
//
//		if (!hasEnoughOil(resBuilder, u, oilSpent)) {
//			if (u.isAdmin()) {
//				LOG.info("it's alright. User is admin.");
//			} else {
//				return false;
//			}
//		}
//
//		if (!hasEnoughGems(resBuilder, u, gemsSpent)) {
//			if (u.isAdmin()) {
//				LOG.info("it's alright. User is admin.");
//			} else {
//				return false;
//			}
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
//			resBuilder.setStatus(UpdateUserCurrencyStatus.FAIL_INSUFFICIENT_CASH);
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
//			resBuilder.setStatus(UpdateUserCurrencyStatus.FAIL_INSUFFICIENT_OIL);
//			return false;
//		}
//
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
//			resBuilder.setStatus(UpdateUserCurrencyStatus.FAIL_INSUFFICIENT_GEMS);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean writeChangesToDb( final User u, final int uId, final int cashSpent,
//	    final int oilSpent, final int gemsSpent, final Timestamp clientTime,
//	    final Map<String, Integer> currencyChange )
//	{
//
//		// update user currency
//		int gemsChange = -1
//		    * Math.abs(gemsSpent);
//		int cashChange = -1
//		    * Math.abs(cashSpent);
//		int oilChange = -1
//		    * Math.abs(oilSpent);
//
//		// if user is admin then allow any change
//		if (u.isAdmin()) {
//			gemsChange = gemsSpent;
//			cashChange = cashSpent;
//			oilChange = oilSpent;
//		}
//
//		if (!updateUser(u, gemsChange, cashChange, oilChange)) {
//			LOG.error("unexpected error: could not decrement user's gems by "
//			    + gemsChange
//			    + ", cash by "
//			    + cashChange
//			    + ", and oil by "
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
//	private void writeToUserCurrencyHistory( final User aUser,
//	    final Map<String, Integer> currencyChange, final Timestamp curTime,
//	    final int previousGems, final int previousCash, final int previousOil,
//	    final String reason, final String details )
//	{
//		final String userUuid = aUser.getId();
//
//		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//		final String gems = MiscMethods.gems;
//		final String cash = MiscMethods.cash;
//		final String oil = MiscMethods.oil;
//
//		previousCurrency.put(gems, previousGems);
//		previousCurrency.put(cash, previousCash);
//		previousCurrency.put(oil, previousOil);
//		currentCurrency.put(gems, aUser.getGems());
//		currentCurrency.put(cash, aUser.getCash());
//		currentCurrency.put(oil, aUser.getOil());
//		reasonsForChanges.put(gems, reason);
//		reasonsForChanges.put(cash, reason);
//		reasonsForChanges.put(oil, reason);
//		detailsMap.put(gems, details);
//		detailsMap.put(cash, details);
//		detailsMap.put(oil, details);
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
//
//	}
//}
