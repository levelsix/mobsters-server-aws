package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
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
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class ExpansionWaitCompleteController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(ExpansionWaitCompleteController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public ExpansionWaitCompleteController()
	{
		numAllocatedThreads = 1;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new ExpansionWaitCompleteRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_EXPANSION_WAIT_COMPLETE_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final ExpansionWaitCompleteRequestProto reqProto =
		    ((ExpansionWaitCompleteRequestEvent) event).getExpansionWaitCompleteRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final Timestamp clientTime = new Timestamp(reqProto.getCurTime());
		final int xPosition = reqProto.getXPosition();
		final int yPosition = reqProto.getYPosition();
		final boolean speedUp = reqProto.getSpeedUp();
		final int gemCostToSpeedup = reqProto.getGemCostToSpeedup();

		final ExpansionWaitCompleteResponseProto.Builder resBuilder =
		    ExpansionWaitCompleteResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(ExpansionWaitCompleteStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			final List<ExpansionPurchaseForUser> epfuList =
			    ExpansionPurchaseForUserRetrieveUtils.getUserCityExpansionDatasForUserUuid(userUuid);
			final ExpansionPurchaseForUser epfu =
			    selectSpecificExpansion(xPosition, yPosition, epfuList);

			final boolean legitExpansionComplete =
			    checkLegitExpansionComplete(user, resBuilder, epfuList, epfu, clientTime,
			        speedUp, gemCostToSpeedup);
			int previousGems = 0;

			boolean success = false;
			final Map<String, Integer> money = new HashMap<String, Integer>();
			if (legitExpansionComplete) {
				previousGems = user.getGems();
				success =
				    writeChangesToDB(user, epfu, speedUp, money, clientTime, gemCostToSpeedup);
			}

			if (success) {
				final UserCityExpansionDataProto ucedp =
				    CreateInfoProtoUtils.createUserCityExpansionDataProtoFromUserCityExpansionData(epfu);
				resBuilder.setUcedp(ucedp);
			}

			final ExpansionWaitCompleteResponseEvent resEvent =
			    new ExpansionWaitCompleteResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setExpansionWaitCompleteResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in ExpansionWaitCompleteController.processRequestEvent", e);
			}

			if (success) {
				writeToUserCurrencyHistory(userUuid, user, xPosition, yPosition, clientTime,
				    money, previousGems);
				final UserCityExpansionDataProto ucedp =
				    CreateInfoProtoUtils.createUserCityExpansionDataProtoFromUserCityExpansionData(epfu);
				resBuilder.setUcedp(ucedp);
			}

		} catch (final Exception e) {
			LOG.error("exception in ExpansionWaitCompleteController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(ExpansionWaitCompleteStatus.FAIL_OTHER);
				final ExpansionWaitCompleteResponseEvent resEvent =
				    new ExpansionWaitCompleteResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setExpansionWaitCompleteResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in ExpansionWaitCompleteController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in ExpansionWaitCompleteController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private ExpansionPurchaseForUser selectSpecificExpansion( final int xPosition,
	    final int yPosition, final List<ExpansionPurchaseForUser> epfuList )
	{

		// if there aren't any expansion purchases return null;
		if ((null == epfuList)
		    || epfuList.isEmpty()) {
			return null;
		}

		// go through each of the user's expansion purchases get the one with
		// the
		// corresponding x and y coordinates
		for (final ExpansionPurchaseForUser epfu : epfuList) {
			final int x = epfu.getxPosition();
			final int y = epfu.getyPosition();

			if ((x == xPosition)
			    && (y == yPosition)) {
				return epfu;
			}
		}

		return null;
	}

	private boolean checkLegitExpansionComplete( final User user, final Builder resBuilder,
	    final List<ExpansionPurchaseForUser> epfuList,
	    final ExpansionPurchaseForUser userCityExpansionData, final Timestamp clientTime,
	    final boolean speedUp, final int gemCostToSpeedup )
	{
		final int nthExpansion = epfuList.size();

		if (userCityExpansionData == null) {
			resBuilder.setStatus(ExpansionWaitCompleteStatus.FAIL_WAS_NOT_EXPANDING);
			LOG.error("unexpected error: user has no expansion pending");
			return false;
		}
		final long curTimeMillis = clientTime.getTime();

		final ExpansionCost ec =
		    ExpansionCostRetrieveUtils.getCityExpansionCostById(nthExpansion);
		final int numMinutes = ec.getNumMinutesToExpand();

		// check if user has waited long enough to complete expansion (sans
		// using gems)
		final long expandStartMillis = userCityExpansionData.getExpandStartTime()
		    .getTime();
		final long millisForExpansion = 60000 * numMinutes;
		final long expandEndMillis = expandStartMillis
		    + millisForExpansion;
		if (!speedUp
		    && (expandEndMillis > curTimeMillis)) {
			resBuilder.setStatus(ExpansionWaitCompleteStatus.FAIL_NOT_DONE_YET);
			LOG.error("client error: time is out of sync. Client incorrectly thinks"
			    + " that the expansion is done. userCityExpansionData="
			    + userCityExpansionData);
			return false;
		}

		if (speedUp
		    && (user.getGems() < gemCostToSpeedup)) {
			resBuilder.setStatus(ExpansionWaitCompleteStatus.FAIL_INSUFFICIENT_GEMS);
			LOG.error("user error: user does not have enough gems to speed up expansion."
			    + " userCityExpansionData="
			    + userCityExpansionData
			    + "cost="
			    + gemCostToSpeedup);
			return false;
		}

		resBuilder.setStatus(ExpansionWaitCompleteStatus.SUCCESS);
		return true;
	}

	private boolean writeChangesToDB( final User user, final ExpansionPurchaseForUser epfu,
	    final boolean speedup, final Map<String, Integer> money, final Timestamp clientTime,
	    final int gemCost )
	{
		if (speedup) {
			final int gemChange = -1
			    * gemCost;
			if (!user.updateRelativeGemsNaive(gemChange)) {
				LOG.error("problem updating user gems. gemChange="
				    + gemChange);
				return false;
			} else {
				// everything went ok
				money.put(MiscMethods.gems, gemChange);
			}
		}

		final int xPosition = epfu.getxPosition();
		final int yPosition = epfu.getyPosition();
		if (!UpdateUtils.get()
		    .updateUserCityExpansionData(user.getId(), xPosition, yPosition, false, clientTime)) {
			LOG.error("problem with resolving expansion. expansion="
			    + epfu
			    + "\t speedup="
			    + speedup
			    + "\t user="
			    + user);
			return false;
		}
		return true;
	}

	private void writeToUserCurrencyHistory( final String userUuid, final User aUser,
	    final int xPosition, final int yPosition, final Timestamp date,
	    final Map<String, Integer> currencyChange, final int previousGems )
	{
		if (currencyChange.isEmpty()) {
			return;
		}
		final String gems = MiscMethods.gems;

		final String reason = ControllerConstants.UCHRFC__EXPANSION_WAIT_COMPLETE;
		final StringBuilder detailsSb = new StringBuilder();
		detailsSb.append("xPos=");
		detailsSb.append(xPosition);
		detailsSb.append(", yPos=");
		detailsSb.append(yPosition);
		final String details = detailsSb.toString();

		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();

		// if (currencyChange.containsKey(gems)) {
		previousCurrency.put(gems, previousGems);
		currentCurrency.put(gems, aUser.getGems());
		reasonsForChanges.put(gems, reason);
		detailsMap.put(gems, details);
		// }

		MiscMethods.writeToUserCurrencyOneUser(userUuid, date, currencyChange,
		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);

	}

}
