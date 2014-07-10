package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto.FinishNormStructWaittimeStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.FinishNormStructWaittimeWithDiamondsRequestEvent;
import com.lvl6.mobsters.events.response.FinishNormStructWaittimeWithDiamondsResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.info.Structure;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class FinishNormStructWaittimeWithDiamondsController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(FinishNormStructWaittimeWithDiamondsController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public FinishNormStructWaittimeWithDiamondsController()
	{
		numAllocatedThreads = 2;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new FinishNormStructWaittimeWithDiamondsRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{

		final FinishNormStructWaittimeWithDiamondsRequestProto reqProto =
		    ((FinishNormStructWaittimeWithDiamondsRequestEvent) event).getFinishNormStructWaittimeWithDiamondsRequestProto();
		LOG.info("reqProto="
		    + reqProto);

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int userStructId = reqProto.getUserStructUuid();
		// userstruct's lastRetrieved will start with this date
		final Timestamp timeOfSpeedup = new Timestamp(reqProto.getTimeOfSpeedup());
		final int gemCostToSpeedup = reqProto.getGemCostToSpeedup();

		final FinishNormStructWaittimeWithDiamondsResponseProto.Builder resBuilder =
		    FinishNormStructWaittimeWithDiamondsResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(FinishNormStructWaittimeStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			LOG.info("user="
			    + user);
			int previousGems = 0;
			final StructureForUser userStruct = RetrieveUtils.userStructRetrieveUtils()
			    .getSpecificUserStruct(userStructId);
			Structure struct = null;
			Structure formerStruct = null;

			if (userStruct != null) {
				final int structId = userStruct.getStructId();
				struct = StructureRetrieveUtils.getStructForStructId(structId);
				formerStruct = StructureRetrieveUtils.getPredecessorStructForStructId(structId);
			}

			final boolean legitSpeedup =
			    checkLegitSpeedup(resBuilder, user, userStruct, timeOfSpeedup, struct,
			        gemCostToSpeedup);

			boolean success = false;
			final Map<String, Integer> money = new HashMap<String, Integer>();
			if (legitSpeedup) {
				previousGems = user.getGems();
				success =
				    writeChangesToDB(user, userStruct, timeOfSpeedup, struct, gemCostToSpeedup,
				        money);
			}
			if (success) {
				resBuilder.setStatus(FinishNormStructWaittimeStatus.SUCCESS);
			}

			final FinishNormStructWaittimeWithDiamondsResponseEvent resEvent =
			    new FinishNormStructWaittimeWithDiamondsResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setFinishNormStructWaittimeWithDiamondsResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in FinishNormStructWaittimeWithDiamondsController.processRequestEvent",
				    e);
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
					    "fatal exception in FinishNormStructWaittimeWithDiamondsController.processRequestEvent",
					    e);
				}
				writeToUserCurrencyHistory(user, userStruct, formerStruct, timeOfSpeedup,
				    money, previousGems);
			}
		} catch (final Exception e) {
			LOG.error(
			    "exception in FinishNormStructWaittimeWithDiamondsController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(FinishNormStructWaittimeStatus.FAIL_OTHER);
				final FinishNormStructWaittimeWithDiamondsResponseEvent resEvent =
				    new FinishNormStructWaittimeWithDiamondsResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setFinishNormStructWaittimeWithDiamondsResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in FinishNormStructWaittimeWithDiamondsController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error(
				    "exception2 in FinishNormStructWaittimeWithDiamondsController processEvent",
				    e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegitSpeedup( final Builder resBuilder, final User user,
	    final StructureForUser userStruct, final Timestamp timeOfSpeedup,
	    final Structure struct, final int gemCostToSpeedup )
	{
		if ((user == null)
		    || (userStruct == null)
		    || (struct == null)
		    || (userStruct.getUserUuid() != user.getId())
		    || userStruct.isComplete()) {
			resBuilder.setStatus(FinishNormStructWaittimeStatus.FAIL_OTHER);
			LOG.error("something passed in is null. user="
			    + user
			    + ", struct="
			    + struct
			    + ", struct owner's id="
			    + userStruct.getUserUuid()
			    + "\t or user struct is complete. userStruct="
			    + userStruct);
			return false;
		}

		if (user.getGems() < gemCostToSpeedup) {
			resBuilder.setStatus(FinishNormStructWaittimeStatus.FAIL_NOT_ENOUGH_GEMS);
			LOG.error("user doesn't have enough diamonds. has "
			    + user.getGems()
			    + ", needs "
			    + gemCostToSpeedup);
			return false;
		}

		return true;
	}

	private boolean writeChangesToDB( final User user, final StructureForUser userStruct,
	    final Timestamp timeOfSpeedup, final Structure struct, final int gemCost,
	    final Map<String, Integer> money )
	{

		final int gemChange = -1
		    * gemCost;
		// update user gems
		if (!user.updateRelativeGemsNaive(gemChange)) {
			LOG.error("problem with using diamonds to finish norm struct build. userStruct="
			    + userStruct
			    + "\t struct="
			    + struct
			    + "\t gemCost="
			    + gemChange);
			return false;
		} else {
			if (0 != gemChange) {
				money.put(MiscMethods.gems, gemChange);
			}
		}

		// the last retrieved time has a value of timeOfSpeedup

		// update structure for user to reflect it is complete
		if (!UpdateUtils.get()
		    .updateSpeedupUpgradingUserStruct(userStruct.getId(), timeOfSpeedup)) {
			LOG.error("problem with completing norm struct build time. userStruct="
			    + userStruct
			    + "\t struct="
			    + struct
			    + "\t gemCost="
			    + gemChange);
			return false;
		}

		return true;
	}

	public void writeToUserCurrencyHistory( final User aUser,
	    final StructureForUser userStruct, final Structure formerStruct,
	    final Timestamp timeOfPurchase, final Map<String, Integer> money, final int previousGems )
	{
		if (money.isEmpty()) {
			return;
		}
		final int userStructId = userStruct.getId();
		final int structId = userStruct.getStructId();
		final StringBuilder structDetails = new StringBuilder(); // + structId;
		if (null == formerStruct) {
			// no previous guy so user speeding up building first building
			structDetails.append("construction ");
		} else {
			structDetails.append("upgrade ");
		}
		structDetails.append("uStructId: ");
		structDetails.append(userStructId);
		structDetails.append(" structId: ");
		structDetails.append(structId);

		if (null != formerStruct) {
			final int prevStructId = formerStruct.getId();
			final int prevLevel = formerStruct.getLevel();
			structDetails.append(" prevStructId: ");
			structDetails.append(prevStructId);
			structDetails.append(" prevLevel: ");
			structDetails.append(prevLevel);
		}

		final String userUuid = aUser.getId();
		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> details = new HashMap<String, String>();
		final String reasonForChange = ControllerConstants.UCHRFC__SPED_UP_NORM_STRUCT;
		final String gems = MiscMethods.gems;

		previousCurrencies.put(gems, previousGems);
		currentCurrencies.put(gems, aUser.getGems());
		reasonsForChanges.put(gems, reasonForChange);
		final String detail = structDetails.toString();
		details.put(gems, detail);

		MiscMethods.writeToUserCurrencyOneUser(userUuid, timeOfPurchase, money,
		    previousCurrencies, currentCurrencies, reasonsForChanges, details);
	}

}
