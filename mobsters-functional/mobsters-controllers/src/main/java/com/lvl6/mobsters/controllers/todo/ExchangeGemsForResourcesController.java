package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesRequestProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto.ExchangeGemsForResourcesStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.ExchangeGemsForResourcesRequestEvent;
import com.lvl6.mobsters.events.response.ExchangeGemsForResourcesResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class ExchangeGemsForResourcesController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(ExchangeGemsForResourcesController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public ExchangeGemsForResourcesController()
	{
		numAllocatedThreads = 1;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new ExchangeGemsForResourcesRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_EXCHANGE_GEMS_FOR_RESOURCES_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final ExchangeGemsForResourcesRequestProto reqProto =
		    ((ExchangeGemsForResourcesRequestEvent) event).getExchangeGemsForResourcesRequestProto();

		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
		final int numGems = reqProto.getNumGems();
		final int numResources = reqProto.getNumResources();
		final ResourceType resourceType = reqProto.getResourceType();
		final Timestamp curTime = new Timestamp(reqProto.getClientTime());
		final int maxCash = senderResourcesProto.getMaxCash();
		final int maxOil = senderResourcesProto.getMaxOil();

		final Builder resBuilder = ExchangeGemsForResourcesResponseProto.newBuilder();
		resBuilder.setSender(senderResourcesProto);
		resBuilder.setStatus(ExchangeGemsForResourcesStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());

			final boolean legit = checkLegit(resBuilder, user, numGems, resourceType, numGems);

			boolean successful = false;
			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
			final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
			if (legit) {
				previousCurrency.put(MiscMethods.cash, user.getCash());
				previousCurrency.put(MiscMethods.oil, user.getOil());
				previousCurrency.put(MiscMethods.gems, user.getGems());
				successful =
				    writeChangesToDb(user, numGems, resourceType, numResources, maxCash,
				        maxOil, currencyChange);
			}
			if (successful) {
				resBuilder.setStatus(ExchangeGemsForResourcesStatus.SUCCESS);
			}

			final ExchangeGemsForResourcesResponseProto resProto = resBuilder.build();
			final ExchangeGemsForResourcesResponseEvent resEvent =
			    new ExchangeGemsForResourcesResponseEvent(senderProto.getUserUuid());
			resEvent.setExchangeGemsForResourcesResponseProto(resProto);
			resEvent.setTag(event.getTag());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in ExchangeGemsForResourcesController.processRequestEvent",
				    e);
			}

			if (successful) {
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in ExchangeGemsForResourcesController.processRequestEvent",
					    e);
				}

				writeToUserCurrencyHistory(user, previousCurrency, currencyChange, curTime,
				    resourceType, numResources, numGems);
			}
		} catch (final Exception e) {
			LOG.error("exception in ExchangeGemsForResourcesController processEvent", e);
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegit( final Builder resBuilder, final User aUser, final int numGems,
	    final ResourceType resourceType, final int numResources )
	{
		if ((null == aUser)
		    || (null == resourceType)
		    || (0 == numGems)) {
			LOG.error("user or resourceType is null, or numGems is 0. user="
			    + aUser
			    + "\t resourceType="
			    + resourceType
			    + "\t numGems="
			    + numGems);
			return false;
		}

		final int userGems = aUser.getGems();

		if (userGems < numGems) {
			LOG.error("user does not have enough gems to exchange for resource."
			    + " userGems="
			    + userGems
			    + "\t resourceType="
			    + resourceType
			    + "\t numResources="
			    + numResources);
			resBuilder.setStatus(ExchangeGemsForResourcesStatus.FAIL_INSUFFICIENT_GEMS);
			return false;
		}

		return true;
	}

	private boolean writeChangesToDb( final User user, final int numGems,
	    final ResourceType resourceType, final int numResources, final int maxCash,
	    final int maxOil, final Map<String, Integer> currencyChange )
	{
		boolean success = true;
		LOG.info("exchanging "
		    + numGems
		    + " gems for "
		    + numResources
		    + " "
		    + resourceType.name());

		int cashChange = 0;
		int oilChange = 0;
		final int gemChange = -1
		    * numGems;

		if (ResourceType.CASH == resourceType) {
			cashChange = numResources;
			if (numResources > 0) {
				final int curCash = Math.min(user.getCash(), maxCash); // in
																	   // case
				// user's cash
				// is more than
				// maxCash.
				final int maxCashUserCanGain = maxCash
				    - curCash;
				cashChange = Math.min(numResources, maxCashUserCanGain);
			}
		} else if (ResourceType.OIL == resourceType) {
			oilChange = numResources;
			if (numResources > 0) {
				final int curOil = Math.min(user.getOil(), maxOil); // in case
																	// user's
				// oil is more
				// than maxOil.
				final int maxOilUserCanGain = maxOil
				    - curOil;
				oilChange = Math.min(numResources, maxOilUserCanGain);
			}
		}

		if ((0 == oilChange)
		    && (0 == cashChange)) {
			LOG.error("oil and cash (user exchanged) for gems are both 0. oilChange="
			    + oilChange
			    + "\t cashChange="
			    + cashChange
			    + "\t gemChange="
			    + gemChange
			    + "\t maxOil="
			    + maxOil
			    + "\t maxCash="
			    + maxCash);
			return false;
		}

		LOG.info("user before: "
		    + user);
		final int numUpdated =
		    user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
		if ((2 != numUpdated)
		    && (1 != numUpdated)) {
			LOG.error("did not increase user's "
			    + resourceType
			    + " by "
			    + numResources);
			success = false;
		} else {
			if (0 != cashChange) {
				currencyChange.put(MiscMethods.cash, cashChange);
			}
			if (0 != oilChange) {
				currencyChange.put(MiscMethods.oil, oilChange);
			}
			if (0 != gemChange) {
				currencyChange.put(MiscMethods.gems, gemChange);
			}
		}

		LOG.info("user after: "
		    + user);
		return success;
	}

	private void writeToUserCurrencyHistory( final User aUser,
	    final Map<String, Integer> previousCurrency, final Map<String, Integer> currencyChange,
	    final Timestamp curTime, final ResourceType resourceType, final int numResources,
	    final int numGems )
	{
		if (currencyChange.isEmpty()) {
			return;
		}
		final String cash = MiscMethods.cash;
		final String oil = MiscMethods.oil;
		final String gems = MiscMethods.gems;

		final String reasonForChange = ControllerConstants.UCHRFC__CURRENCY_EXCHANGE;
		final StringBuilder detailsSb = new StringBuilder();
		detailsSb.append(" exchanged ");
		detailsSb.append(numGems);
		detailsSb.append(" gems for ");
		detailsSb.append(numResources);
		detailsSb.append(" ");
		detailsSb.append(resourceType.name());

		final String userUuid = aUser.getId();
		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> details = new HashMap<String, String>();

		currentCurrencies.put(cash, aUser.getCash());
		currentCurrencies.put(oil, aUser.getOil());
		currentCurrencies.put(gems, aUser.getGems());
		reasonsForChanges.put(cash, reasonForChange);
		reasonsForChanges.put(oil, reasonForChange);
		reasonsForChanges.put(gems, reasonForChange);
		details.put(cash, detailsSb.toString());
		details.put(oil, detailsSb.toString());
		details.put(gems, detailsSb.toString());

		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
		    previousCurrency, currentCurrencies, reasonsForChanges, details);
	}

}
