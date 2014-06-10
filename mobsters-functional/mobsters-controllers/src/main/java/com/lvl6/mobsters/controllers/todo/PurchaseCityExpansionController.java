package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

/*
 * NOT READY/BEING USED YET
 */

@Component
@DependsOn("gameServer")
public class PurchaseCityExpansionController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(PurchaseCityExpansionController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected LeaderBoardUtil leaderboard;

	public LeaderBoardUtil getLeaderboard()
	{
		return leaderboard;
	}

	public void setLeaderboard( final LeaderBoardUtil leaderboard )
	{
		this.leaderboard = leaderboard;
	}

	public PurchaseCityExpansionController()
	{
		numAllocatedThreads = 1;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new PurchaseCityExpansionRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_PURCHASE_CITY_EXPANSION_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final PurchaseCityExpansionRequestProto reqProto =
		    ((PurchaseCityExpansionRequestEvent) event).getPurchaseCityExpansionRequestProto();

		// variables client sent
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		// in relation to center square (the origin 0,0)
		final int xPosition = reqProto.getXPosition();
		final int yPosition = reqProto.getYPosition();
		final Timestamp timeOfPurchase = new Timestamp(reqProto.getTimeOfPurchase());

		final PurchaseCityExpansionResponseProto.Builder resBuilder =
		    PurchaseCityExpansionResponseProto.newBuilder();
		resBuilder.setSender(senderProto);

		// so someone doesn't steal user's silver during transaction
		svcTxManager.beginTransaction();
		try {

			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			final List<ExpansionPurchaseForUser> userCityExpansionDataList =
			    ExpansionPurchaseForUserRetrieveUtils.getUserCityExpansionDatasForUserUuid(userUuid);
			// used to calculate cost for buying expansion
			int numExpansions = 0;
			if (null != userCityExpansionDataList) {
				numExpansions = userCityExpansionDataList.size();
			}

			final List<Integer> cityExpansionCostList = new ArrayList<Integer>();
			final boolean legitExpansion =
			    checkLegitExpansion(resBuilder, timeOfPurchase, user,
			        userCityExpansionDataList, numExpansions, cityExpansionCostList);

			// write to the client
			final PurchaseCityExpansionResponseEvent resEvent =
			    new PurchaseCityExpansionResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setPurchaseCityExpansionResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in PurchaseCityExpansionController.processRequestEvent", e);
			}

			if (legitExpansion) {
				// update database tables
				final int previousCash = user.getCash();
				final int cost = cityExpansionCostList.get(0);

				final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
				writeChangesToDB(user, timeOfPurchase, xPosition, yPosition, true,
				    numExpansions, cost, currencyChange);

				// modified user object, need to update the client to reflect
				// this
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
				        null);
				final ExpansionPurchaseForUser uced =
				    ExpansionPurchaseForUserRetrieveUtils.getSpecificUserCityExpansionDataForUserUuidAndPosition(
				        user.getId(), xPosition, yPosition);
				resBuilder.setUcedp(CreateInfoProtoUtils.createUserCityExpansionDataProtoFromUserCityExpansionData(uced));
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in PurchaseCityExpansionController.processRequestEvent",
					    e);
				}

				writeToUserCurrencyHistory(user, timeOfPurchase, xPosition, yPosition,
				    previousCash, currencyChange);
			}
		} catch (final Exception e) {
			LOG.error("exception in PurchaseCityExpansion processEvent", e);
			try {
				resBuilder.setStatus(PurchaseCityExpansionStatus.OTHER_FAIL);
				final PurchaseCityExpansionResponseEvent resEvent =
				    new PurchaseCityExpansionResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setPurchaseCityExpansionResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in PurchaseCityExpansionController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in BeginDungeonController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private void writeChangesToDB( final User user, final Timestamp timeOfPurchase,
	    final int xPosition, final int yPosition, final boolean isExpanding,
	    final int numOfExpansions, final int cost, final Map<String, Integer> currencyChange )
	{

		final int cashChange = cost
		    * -1;
		if (!user.updateRelativeCashNaive(cashChange)) {
			LOG.error("problem with updating cash purchasing a city expansion");

		} else {
			// everything went ok
			if (!InsertUtils.get()
			    .insertUserCityExpansionData(user.getId(), timeOfPurchase, xPosition,
			        yPosition, isExpanding)) {
				LOG.error("problem with updating user expansion info after purchase");
			}
			currencyChange.put(MiscMethods.cash, cashChange);
		}
	}

	private boolean checkLegitExpansion( final Builder resBuilder,
	    final Timestamp timeOfPurchase, final User user,
	    final List<ExpansionPurchaseForUser> userCityExpansionDataList,
	    final int numOfExpansions, final List<Integer> costList )
	{

		// if (!MiscMethods.checkClientTimeAroundApproximateNow(timeOfPurchase))
		// {
		// resBuilder.setStatus(PurchaseCityExpansionStatus.CLIENT_TOO_APART_FROM_SERVER_TIME);
		// return false;
		// }

		boolean isExpanding = false;
		// loop through each expansion and see if any expansions are still
		// expanding
		if (userCityExpansionDataList != null) {
			for (final ExpansionPurchaseForUser uced : userCityExpansionDataList) {
				if (uced.isExpanding()) {
					isExpanding = true;
					break;
				}
			}
		}

		// user cannot buy another expansion while an expansion is still being
		// constructed
		if (isExpanding) {
			LOG.error("user has a current expansion going on still");
			resBuilder.setStatus(PurchaseCityExpansionStatus.ALREADY_EXPANDING);
			return false;
		}

		// see if user has enough to buy next expansion
		final int cost = calculateExpansionCost(numOfExpansions + 1);
		if (user.getCash() < cost) {
			resBuilder.setStatus(PurchaseCityExpansionStatus.NOT_ENOUGH_COINS);
			return false;
		}

		costList.add(cost);
		resBuilder.setStatus(PurchaseCityExpansionStatus.SUCCESS);
		return true;
	}

	private int calculateExpansionCost( final int numOfExpansions )
	{
		final ExpansionCost cec =
		    ExpansionCostRetrieveUtils.getCityExpansionCostById(numOfExpansions);
		// log.info("cec=" + cec);
		// log.info("all expansion stuff" +
		// CityExpansionCostRetrieveUtils.getAllExpansionNumsToCosts());
		return cec.getExpansionCostCash();
	}

	public void writeToUserCurrencyHistory( final User aUser, final Timestamp date,
	    final int xPosition, final int yPosition, final int previousCash,
	    final Map<String, Integer> changeMap )
	{

		final StringBuilder detailSb = new StringBuilder();
		detailSb.append("Expanding xPosition: ");
		detailSb.append(xPosition);
		detailSb.append(", yPosition: ");
		detailSb.append(yPosition);

		final String userUuid = aUser.getId();
		final Map<String, Integer> previousCurencyMap = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencyMap = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> details = new HashMap<String, String>();
		final String cash = MiscMethods.cash;
		final String reasonForChange = ControllerConstants.UCHRFC__PURCHASE_CITY_EXPANSION;

		previousCurencyMap.put(cash, previousCash);
		currentCurrencyMap.put(cash, aUser.getCash());
		reasonsForChanges.put(cash, reasonForChange);
		details.put(cash, detailSb.toString());

		MiscMethods.writeToUserCurrencyOneUser(userUuid, date, changeMap, previousCurencyMap,
		    currentCurrencyMap, reasonsForChanges, details);
	}

}
