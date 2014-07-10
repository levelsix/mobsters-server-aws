package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto.CombineUserMonsterPiecesStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.CombineUserMonsterPiecesRequestEvent;
import com.lvl6.mobsters.events.response.CombineUserMonsterPiecesResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class CombineUserMonsterPiecesController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(CombineUserMonsterPiecesController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public CombineUserMonsterPiecesController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new CombineUserMonsterPiecesRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_COMBINE_USER_MONSTER_PIECES_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final CombineUserMonsterPiecesRequestProto reqProto =
		    ((CombineUserMonsterPiecesRequestEvent) event).getCombineUserMonsterPiecesRequestProto();

		// get values sent from the client (the request proto)
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		List<Long> userMonsterUuids = reqProto.getUserMonsterUuidsList();
		userMonsterUuids = new ArrayList<Long>(userMonsterUuids);
		final int gemCost = reqProto.getGemCost();
		final Date curDate = new Date();
		final Timestamp curTime = new Timestamp(curDate.getTime());
		// LOG.info("reqProto=" + reqProto);

		// set some values to send to the client (the response proto)
		final CombineUserMonsterPiecesResponseProto.Builder resBuilder =
		    CombineUserMonsterPiecesResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_OTHER); // default

		svcTxManager.beginTransaction();
		try {
			int previousGems = 0;

			final User aUser = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			final Map<Long, MonsterForUser> idsToUserMonsters =
			    RetrieveUtils.monsterForUserRetrieveUtils()
			        .getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids);

			final boolean legit =
			    checkLegit(resBuilder, userUuid, aUser, userMonsterUuids, idsToUserMonsters,
			        gemCost);

			boolean successful = false;
			final Map<String, Integer> money = new HashMap<String, Integer>();
			if (legit) {
				previousGems = aUser.getGems();
				successful = writeChangesToDb(aUser, userMonsterUuids, gemCost, money);
			}

			if (successful) {
				resBuilder.setStatus(CombineUserMonsterPiecesStatus.SUCCESS);
			}

			final CombineUserMonsterPiecesResponseEvent resEvent =
			    new CombineUserMonsterPiecesResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setCombineUserMonsterPiecesResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in CombineUserMonsterPiecesController.processRequestEvent",
				    e);
			}

			if (successful
			    && (gemCost > 0)) {
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in CombineUserMonsterPiecesController.processRequestEvent",
					    e);
				}

				writeToUserCurrencyHistory(aUser, money, curTime, previousGems,
				    userMonsterUuids);
			}
		} catch (final Exception e) {
			LOG.error("exception in CombineUserMonsterPiecesController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_OTHER);
				final CombineUserMonsterPiecesResponseEvent resEvent =
				    new CombineUserMonsterPiecesResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setCombineUserMonsterPiecesResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in CombineUserMonsterPiecesController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in CombineUserMonsterPiecesController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	/*
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value. userMonsterUuids might be modified to
	 * contain only those user monsters that can be combined
	 * 
	 * Example. client gives ids (a, b, c, d). Let's say 'a' is already
	 * completed/combined, 'b' is missing a piece, 'c' doesn't exist and 'd' can
	 * be completed so "userMonsterUuids" will be modified to only contain 'd'
	 */
	private boolean checkLegit( final Builder resBuilder, final String userUuid, final User u,
	    final List<Long> userMonsterUuids, final Map<Long, MonsterForUser> idsToUserMonsters,
	    final int gemCost )
	{

		if (null == u) {
			LOG.error("user is null. no user exists with id="
			    + userUuid
			    + "");
			return false;
		}
		if ((null == userMonsterUuids)
		    || userMonsterUuids.isEmpty()
		    || idsToUserMonsters.isEmpty()) {
			LOG.error("no user monsters exist. userMonsterUuids="
			    + userMonsterUuids
			    + "\t idsToUserMonsters="
			    + idsToUserMonsters);
			return false;
		}

		// only complete the user monsters that exist
		if (userMonsterUuids.size() != idsToUserMonsters.size()) {
			LOG.warn("not all monster_for_user_ids exist. userMonsterUuids="
			    + userMonsterUuids
			    + "\t idsToUserMonsters="
			    + idsToUserMonsters
			    + "\t. Will continue processing");

			// retaining only the user monster ids that exist
			userMonsterUuids.clear();
			userMonsterUuids.addAll(idsToUserMonsters.keySet());
		}

		final List<Long> wholeUserMonsterUuids =
		    MonsterStuffUtils.getWholeButNotCombinedUserMonsters(idsToUserMonsters);
		if (wholeUserMonsterUuids.size() != userMonsterUuids.size()) {
			LOG.warn("client trying to combine already combined or incomplete monsters."
			    + " clientSentUuids="
			    + userMonsterUuids
			    + "\t wholeButIncompleteMonsterUuids="
			    + wholeUserMonsterUuids
			    + "\t idsToUserMonsters="
			    + idsToUserMonsters
			    + "\t Will continue processing");

			// retaining only user monsters that have all pieces but are
			// incomplete
			userMonsterUuids.clear();
			userMonsterUuids.addAll(wholeUserMonsterUuids);
		}

		if (userMonsterUuids.isEmpty()) {
			resBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_OTHER);
			LOG.error("the user didn't send any userMonsters to complete!.");
			return false;
		}

		if ((gemCost > 0)
		    && (userMonsterUuids.size() > 1)) {
			// user speeding up combining multiple monsters, can only speed up
			// one
			LOG.error("user speeding up combining pieces for multiple monsters can only "
			    + "speed up one monster. gemCost="
			    + gemCost
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
			resBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_MORE_THAN_ONE_MONSTER_FOR_SPEEDUP);
			return false;
		}

		// check user gems
		final int userGems = u.getGems();
		if (userGems < gemCost) {
			LOG.error("user doesn't have enough gems to speed up combining. userGems="
			    + userGems
			    + "\t gemCost="
			    + gemCost
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
			resBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_INSUFFUCIENT_GEMS);
			return false;
		}

		return true;
	}

	private boolean writeChangesToDb( final User aUser, final List<Long> userMonsterUuids,
	    final int gemCost, final Map<String, Integer> money )
	{

		// if user sped up stuff then charge him
		if (gemCost > 0) {
			final int gemChange = -1
			    * gemCost;
			if (!aUser.updateRelativeGemsNaive(gemChange)) {
				LOG.error("problem with updating user gems for speedup. gemChange="
				    + gemChange
				    + "\t userMonsterUuids="
				    + userMonsterUuids);
				return false;
			} else {
				money.put(MiscMethods.gems, gemChange);
			}
		}

		final int num = UpdateUtils.get()
		    .updateCompleteUserMonster(userMonsterUuids);

		if (num != userMonsterUuids.size()) {
			LOG.error("problem with updating user monster is_complete. numUpdated="
			    + num
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
		}
		return true;
	}

	private void writeToUserCurrencyHistory( final User aUser,
	    final Map<String, Integer> money, final Timestamp curTime, final int previousGems,
	    final List<Long> userMonsterUuids )
	{
		if ((null == money)
		    || money.isEmpty()) {
			return;
		}
		final String userUuid = aUser.getId();
		final String gems = MiscMethods.gems;
		final String reasonForChange = ControllerConstants.UCHRFC__SPED_UP_COMBINING_MONSTER;

		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsList = new HashMap<String, String>();

		previousCurrencies.put(gems, previousGems);
		currentCurrencies.put(gems, aUser.getGems());
		reasonsForChanges.put(gems, reasonForChange);
		detailsList.put(gems, "userMonsterUuids="
		    + userMonsterUuids);
		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, money, previousCurrencies,
		    currentCurrencies, reasonsForChanges, detailsList);

	}

}
