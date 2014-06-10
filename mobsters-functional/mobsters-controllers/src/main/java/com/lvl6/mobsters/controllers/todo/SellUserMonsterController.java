package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SellUserMonsterRequestEvent;
import com.lvl6.mobsters.events.response.SellUserMonsterResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.misc.MiscMethods;
import com.lvl6.properties.ControllerConstants;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto.SellUserMonsterStatus;
import com.lvl6.mobsters.noneventproto.NoneventUserMonsterProto.MinimumUserMonsterSellProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.server.Locker;
import com.lvl6.server.controller.utils.MonsterStuffUtils;
import com.lvl6.utils.RetrieveUtils;
import com.lvl6.utils.utilmethods.DeleteUtils;

@Component
@DependsOn("gameServer")
public class SellUserMonsterController extends EventController {

	private static Logger LOG = LoggerFactory.getLogger(SellUserMonsterController.class);
	}.getClass().getEnclosingClass());

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public SellUserMonsterController() {
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent() {
		return new SellUserMonsterRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType() {
		return EventProtocolRequest.C_SELL_USER_MONSTER_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event, final EventsToDispatch eventWriter ) throws Exception {
		final SellUserMonsterRequestProto reqProto = ((SellUserMonsterRequestEvent) event)
				.getSellUserMonsterRequestProto();

		// get values sent from the client (the request proto)
		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
		final String userUuid = senderProto.getUserUuid();
		final List<MinimumUserMonsterSellProto> userMonsters = reqProto.getSalesList();
		final Map<Long, Integer> userMonsterUuidsToCashAmounts = MonsterStuffUtils
				.convertToMonsterForUserIdToCashAmount(userMonsters);
		final Set<Long> userMonsterUuidsSet = userMonsterUuidsToCashAmounts.keySet();
		final List<Long> userMonsterUuids = new ArrayList<Long>(userMonsterUuidsSet);
		final Date deleteDate = new Date();
		final Timestamp deleteTime = new Timestamp(deleteDate.getTime());
		
		final int maxCash = senderResourcesProto.getMaxCash();

		// set some values to send to the client (the response proto)
		final SellUserMonsterResponseProto.Builder resBuilder = SellUserMonsterResponseProto
				.newBuilder();
		resBuilder.setSender(senderResourcesProto);
		resBuilder.setStatus(SellUserMonsterStatus.FAIL_OTHER); // default

		svcTxManager.beginTransaction();
		try {
			int previousCash = 0;

			final User aUser = RetrieveUtils.userRetrieveUtils().getUserById(userUuid);
			final Map<Long, MonsterForUser> idsToUserMonsters = RetrieveUtils
					.monsterForUserRetrieveUtils().getSpecificOrAllUserMonstersForUser(userUuid,
							userMonsterUuids);

			final boolean legit = checkLegit(resBuilder, userUuid, aUser, userMonsterUuids,
					idsToUserMonsters);

			final Map<String, Integer> currencyChange =
					new HashMap<String, Integer>();
			boolean successful = false;
			if (legit) {
				previousCash = aUser.getCash();
				successful = writeChangesToDb(aUser, userMonsterUuids,
						userMonsterUuidsToCashAmounts, maxCash, currencyChange);
			}

			if (successful) {
				resBuilder.setStatus(SellUserMonsterStatus.SUCCESS);
			}

			final SellUserMonsterResponseEvent resEvent = new SellUserMonsterResponseEvent(
					userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setSellUserMonsterResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: " + resEvent);
			try {
			    eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
			    LOG.error("fatal exception in SellUserMonsterController.processRequestEvent", e);
			}


			if (successful) {
				//null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate = MiscMethods
						.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser, null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: " + resEventUpdate);
				try {
				    eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
				    LOG.error("fatal exception in SellUserMonsterController.processRequestEvent", e);
				}
				
				writeChangesToHistory(userUuid, userMonsterUuids,
						userMonsterUuidsToCashAmounts, idsToUserMonsters, deleteDate);
				// WRITE TO USER CURRENCY HISTORY
				writeToUserCurrencyHistory(userUuid, aUser, previousCash,
						deleteTime, userMonsterUuidsToCashAmounts,
						userMonsterUuids, currencyChange);
			}
		} catch (final Exception e) {
			LOG.error("exception in SellUserMonsterController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(SellUserMonsterStatus.FAIL_OTHER);
				final SellUserMonsterResponseEvent resEvent = new SellUserMonsterResponseEvent(
						userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setSellUserMonsterResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: " + resEvent);
				try {
				    eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
				    LOG.error("fatal exception in SellUserMonsterController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in SellUserMonsterController processEvent", e);
			}
		} finally {
			getLocker().unlockPlayer(senderProto.getUserUuid(), this.getClass()
					.getSimpleName());
		}
	}

	/*
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value. "userMonsterUuids" might be modified to
	 * contain only those user monsters that exist (and hence can be sold)
	 * 
	 * Example. client gives ids (a, b, c, d). Let's say 'a,' 'b,' and 'c' don't
	 * exist but 'd' does, so "userMonsterUuids" will be modified to only contain
	 * 'd'
	 */
	private boolean checkLegit(final Builder resBuilder, final String userUuid, final User u,
			final List<Long> userMonsterUuids, final Map<Long, MonsterForUser> idsToUserMonsters) {

		if (null == u) {
			LOG.error("user is null. no user exists with id=" + userUuid + "");
			return false;
		}
		if ((null == userMonsterUuids) || userMonsterUuids.isEmpty()
				|| idsToUserMonsters.isEmpty()) {
			LOG.error("no user monsters exist. userMonsterUuids=" + userMonsterUuids
					+ "\t idsToUserMonsters=" + idsToUserMonsters);
			return false;
		}

		// can only sell the user monsters that exist
		if (userMonsterUuids.size() != idsToUserMonsters.size()) {
			LOG.warn("not all monster_for_user_ids exist. userMonsterUuids="
					+ userMonsterUuids + "\t idsToUserMonsters=" + idsToUserMonsters
					+ "\t. Will continue processing");

			// retaining only the user monster ids that exist
			userMonsterUuids.clear();
			userMonsterUuids.addAll(idsToUserMonsters.keySet());
		}

		resBuilder.setStatus(SellUserMonsterStatus.SUCCESS);
		return true;
	}

	private boolean writeChangesToDb(final User aUser, final List<Long> userMonsterUuids,
			final Map<Long, Integer> userMonsterUuidsToCashAmounts, final int maxCash,
			final Map<String, Integer> currencyChange) {
		final boolean success = true;

		// sum up the monies and give it to the user
		int sum = MiscMethods.sumMapValues(userMonsterUuidsToCashAmounts);
		final int curCash = Math.min(aUser.getCash(), maxCash); //in case user's cash is more than maxCash
		final int maxCashUserCanGain = maxCash - curCash;
		sum = Math.min(sum, maxCashUserCanGain);
		
		//if user at max resources, user can still delete monster, but won't get any resources
		if (0 != sum) {
			if (!aUser.updateRelativeCashNaive(sum)) {
				LOG.error("error updating user coins by " + sum + " not deleting "
						+ "userMonstersUuidsToCashAmounts=" + userMonsterUuidsToCashAmounts);
				return false;
			} else {
				currencyChange.put(MiscMethods.cash, sum);
			}
		}

		// delete the user monsters;
		if ((null != userMonsterUuids) && !userMonsterUuids.isEmpty()) {
			final int num = DeleteUtils.get().deleteMonstersForUser(userMonsterUuids);
			LOG.info("num user monsters deleted: " + num + "\t ids deleted: "
					+ userMonsterUuids);
		}
		return success;
	}

	// FOR USER MONSTER HISTORY PURPOSES
	private void writeChangesToHistory(final String userUuid, final List<Long> userMonsterUuids,
			final Map<Long, Integer> userMonsterUuidsToCashAmounts,
			final Map<Long, MonsterForUser> idsToUserMonsters, final Date deleteDate) {

		if ((null == userMonsterUuids) || userMonsterUuids.isEmpty()) {
			return;
		}
		final String delReason = ControllerConstants.MFUDR__SELL;
		final List<String> deleteReasons = new ArrayList<String>();
		final List<MonsterForUser> userMonstersList = new ArrayList<MonsterForUser>();

		for (int i = 0; i < userMonsterUuids.size(); i++) {
			final long userMonsterId = userMonsterUuids.get(i);
			final int amount = userMonsterUuidsToCashAmounts.get(userMonsterId);
			final MonsterForUser mfu = idsToUserMonsters.get(userMonsterId);
			userMonstersList.add(mfu);
			deleteReasons.add(delReason + amount);
		}
		//TODO: FIX THIS RECORDING MONSTERS DELETED
//
//		int num = InsertUtils.get().insertIntoMonsterForUserDeleted(userUuid,
//				deleteReasons, userMonstersList, deleteDate);
//
//		LOG.info("user monsters that were deleted. userMonsterUuids="
//				+ userMonsterUuids + "\t idsToCashAmounts=" + userMonsterUuidsToCashAmounts
//				+ "\t idsToUserMonsters=" + idsToUserMonsters + "\t numDeleted=" + num);
	}

	// FOR CURRENCY HISTORY PURPOSES
	public void writeToUserCurrencyHistory(final String userUuid, final User aUser,
			final int previousCash, final Timestamp aDate,
			final Map<Long, Integer> userMonsterUuidsToCashAmounts,
			final List<Long> userMonsterUuids, final Map<String, Integer> currencyChange) {

		if (currencyChange.isEmpty()) {
			return;
		}
		
		// record the user monster ids that contributed to changing user's currency
		final StringBuilder detailsSb = new StringBuilder();
		for (final Long umId : userMonsterUuidsToCashAmounts.keySet()) {
			final Integer cash = userMonsterUuidsToCashAmounts.get(umId);
			detailsSb.append("mfuId=");
			detailsSb.append(umId);
			detailsSb.append(", cash=");
			detailsSb.append(cash);
		}
		
		// figure how much user gets. At the moment, if 0 then nothing is recorded
		// in db
		
		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();
		final String reason = ControllerConstants.UCHRFC__SOLD_USER_MONSTERS;
		final String cash = MiscMethods.cash;

		previousCurrency.put(cash, previousCash);
		currentCurrency.put(cash, aUser.getCash());
		reasonsForChanges.put(cash, reason);
		detailsMap.put(cash, detailsSb.toString());

		MiscMethods.writeToUserCurrencyOneUser(userUuid, aDate, currencyChange,
				previousCurrency, currentCurrency, reasonsForChanges,
				detailsMap);
	}

}
