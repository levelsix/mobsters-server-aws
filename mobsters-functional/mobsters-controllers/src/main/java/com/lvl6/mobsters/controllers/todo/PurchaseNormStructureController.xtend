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

import com.lvl6.mobsters.dynamo.CoordinatePair;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto.PurchaseNormStructureStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.PurchaseNormStructureRequestEvent;
import com.lvl6.mobsters.events.response.PurchaseNormStructureResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.info.Structure;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class PurchaseNormStructureController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(PurchaseNormStructureController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected InsertUtil insertUtils;

	public void setInsertUtils( final InsertUtil insertUtils )
	{
		this.insertUtils = insertUtils;
	}

	public PurchaseNormStructureController()
	{
		numAllocatedThreads = 3;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new PurchaseNormStructureRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_PURCHASE_NORM_STRUCTURE_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final PurchaseNormStructureRequestProto reqProto =
		    ((PurchaseNormStructureRequestEvent) event).getPurchaseNormStructureRequestProto();
		LOG.info("reqProto="
		    + reqProto);

		// get stuff client sent
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int structId = reqProto.getStructUuid();
		final CoordinatePair cp = new CoordinatePair(reqProto.getStructCoordinates()
		    .getX(), reqProto.getStructCoordinates()
		    .getY());
		final Timestamp timeOfPurchase = new Timestamp(reqProto.getTimeOfPurchase());
		// positive value, need to convert to negative when updating user
		final int gemsSpent = reqProto.getGemsSpent();
		// positive means refund, negative means charge user
		final int resourceChange = reqProto.getResourceChange();
		final ResourceType resourceType = reqProto.getResourceType();

		// things to send to client
		final PurchaseNormStructureResponseProto.Builder resBuilder =
		    PurchaseNormStructureResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(PurchaseNormStructureStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			// get things from the db
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			LOG.info("user="
			    + user);
			final Structure struct = StructureRetrieveUtils.getStructForStructId(structId);

			// currency history purposes
			int previousGems = 0;
			int previousOil = 0;
			int previousCash = 0;
			int uStructId = 0;

			final boolean legitPurchaseNorm =
			    checkLegitPurchaseNorm(resBuilder, struct, user, timeOfPurchase, gemsSpent,
			        resourceChange, resourceType);

			boolean success = false;
			final List<Integer> uStructIdList = new ArrayList<Integer>();
			final Map<String, Integer> money = new HashMap<String, Integer>();
			if (legitPurchaseNorm) {
				previousGems = user.getGems();
				previousOil = user.getOil();
				previousCash = user.getCash();
				success =
				    writeChangesToDB(user, structId, cp, timeOfPurchase, gemsSpent,
				        resourceChange, resourceType, uStructIdList, money);
			}

			if (success) {
				resBuilder.setStatus(PurchaseNormStructureStatus.SUCCESS);
				uStructId = uStructIdList.get(0);
				resBuilder.setUserStructId(uStructId);
			}

			final PurchaseNormStructureResponseEvent resEvent =
			    new PurchaseNormStructureResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setPurchaseNormStructureResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in PurchaseNormStructureController.processRequestEvent", e);
			}

			if (success) {
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
					    "fatal exception in PurchaseNormStructureController.processRequestEvent",
					    e);
				}

				writeToUserCurrencyHistory(user, structId, uStructId, timeOfPurchase, money,
				    previousGems, previousOil, previousCash);
			}
		} catch (final Exception e) {
			LOG.error("exception in PurchaseNormStructure processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(PurchaseNormStructureStatus.FAIL_OTHER);
				final PurchaseNormStructureResponseEvent resEvent =
				    new PurchaseNormStructureResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setPurchaseNormStructureResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in PurchaseNormStructureController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in PurchaseNormStructure processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegitPurchaseNorm( final Builder resBuilder,
	    final Structure prospective, final User user, final Timestamp timeOfPurchase,
	    final int gemsSpent, final int resourceChange, final ResourceType resourceType )
	{
		if ((user == null)
		    || (prospective == null)
		    || (timeOfPurchase == null)) {
			LOG.error("parameter passed in is null. user="
			    + user
			    + ", struct="
			    + prospective
			    + ", timeOfPurchase="
			    + timeOfPurchase);
			return false;
		}
		final ResourceType structResourceType =
		    ResourceType.valueOf(prospective.getBuildResourceType());
		if (resourceType != structResourceType) {
			LOG.error("client is specifying unexpected resource type. actual="
			    + resourceType
			    + "\t expected="
			    + structResourceType
			    + "\t structure="
			    + prospective);
			return false;
		}

		// check if user has enough resources to build it
		final int userGems = user.getGems();
		// check if gems are spent
		if (gemsSpent > 0) {
			if (userGems < gemsSpent) {
				// doesn't have enough gems
				LOG.error("user has "
				    + userGems
				    + " gems; trying to spend "
				    + gemsSpent
				    + " and "
				    + resourceChange
				    + " "
				    + resourceType
				    + " to buy structure="
				    + prospective);
				resBuilder.setStatus(PurchaseNormStructureStatus.FAIL_INSUFFICIENT_GEMS);
				return false;
			} else {
				// has enough gems
				return true;
			}

		}

		final int requiredResourceAmount = -1
		    * resourceChange;
		if (resourceType == ResourceType.CASH) {
			final int userResource = user.getCash();
			if (userResource < requiredResourceAmount) {
				LOG.error("not enough cash to buy structure. cash="
				    + userResource
				    + "\t cost="
				    + requiredResourceAmount
				    + "\t structure="
				    + prospective);
				resBuilder.setStatus(PurchaseNormStructureStatus.FAIL_INSUFFICIENT_CASH);
				return false;
			}
		} else if (resourceType == ResourceType.OIL) {
			final int userResource = user.getOil();
			if (userResource < requiredResourceAmount) {
				LOG.error("not enough oil to buy structure. oil="
				    + userResource
				    + "\t cost="
				    + requiredResourceAmount
				    + "\t structure="
				    + prospective);
				resBuilder.setStatus(PurchaseNormStructureStatus.FAIL_INSUFFICIENT_OIL);
				return false;
			}
		} else {
			LOG.error("unknown resource type: "
			    + resourceType
			    + "\t structure="
			    + prospective);
			return false;
		}

		return true;
	}

	// uStructId will store the newly created user structure
	private boolean writeChangesToDB( final User user, final int structId,
	    final CoordinatePair cp, final Timestamp purchaseTime, final int gemsSpent,
	    final int resourceChange, final ResourceType resourceType,
	    final List<Integer> uStructId, final Map<String, Integer> money )
	{
		final String userUuid = user.getId();
		final Timestamp lastRetrievedTime = null;
		final boolean isComplete = false;

		final int userStructId =
		    insertUtils.insertUserStruct(userUuid, structId, cp, purchaseTime,
		        lastRetrievedTime, isComplete);
		if (userStructId <= 0) {
			LOG.error("problem with giving struct "
			    + structId
			    + " at "
			    + purchaseTime
			    + " on "
			    + cp);
			return false;
		}

		// TAKE AWAY THE CORRECT RESOURCE
		final int gemChange = -1
		    * gemsSpent;
		int cashChange = 0;
		int oilChange = 0;

		if (resourceType == ResourceType.CASH) {
			cashChange = resourceChange;
		} else if (resourceType == ResourceType.OIL) {
			oilChange = resourceChange;
		}

		if ((0 == gemChange)
		    && (0 == cashChange)
		    && (0 == oilChange)) {
			LOG.error("gemChange="
			    + gemChange
			    + " cashChange="
			    + cashChange
			    + " oilChange="
			    + oilChange
			    + "\t Not purchasing norm struct.");
			return false;
		}

		final int num = user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
		if (1 != num) {
			LOG.error("problem with updating user currency. gemChange="
			    + gemChange
			    + " cashChange="
			    + cashChange
			    + "\t numRowsUpdated="
			    + num);
			return false;
		} else {// things went ok
			if (0 != gemChange) {
				money.put(MiscMethods.gems, gemChange);
			}
			if (0 != cashChange) {
				money.put(MiscMethods.cash, cashChange);
			}
			if (0 != oilChange) {
				money.put(MiscMethods.oil, oilChange);
			}
		}

		uStructId.add(userStructId);
		return true;
	}

	private void writeToUserCurrencyHistory( final User u, final int structId,
	    final int uStructId, final Timestamp date, final Map<String, Integer> money,
	    final int previousGems, final int previousOil, final int previousCash )
	{
		final String userUuid = u.getId();
		final Map<String, Integer> previousCurencyMap = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencyMap = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> details = new HashMap<String, String>();
		final String gems = MiscMethods.gems;
		final String cash = MiscMethods.cash;
		final String oil = MiscMethods.oil;
		final String reasonForChange = ControllerConstants.UCHRFC__PURCHASE_NORM_STRUCT;
		final StringBuilder detailSb = new StringBuilder();
		detailSb.append("structId=");
		detailSb.append(structId);
		detailSb.append(" uStructId=");
		detailSb.append(uStructId);
		final String detail = detailSb.toString();

		previousCurencyMap.put(gems, previousGems);
		previousCurencyMap.put(cash, previousCash);
		previousCurencyMap.put(oil, previousOil);
		currentCurrencyMap.put(gems, u.getGems());
		currentCurrencyMap.put(cash, u.getCash());
		currentCurrencyMap.put(oil, u.getOil());
		reasonsForChanges.put(gems, reasonForChange);
		reasonsForChanges.put(cash, reasonForChange);
		reasonsForChanges.put(oil, reasonForChange);
		details.put(gems, detail);
		details.put(cash, detail);
		details.put(oil, detail);

		MiscMethods.writeToUserCurrencyOneUser(userUuid, date, money, previousCurencyMap,
		    currentCurrencyMap, reasonsForChanges, details);

	}

}
