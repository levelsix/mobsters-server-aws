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

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteResponseProto.EnhancementWaitTimeCompleteStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.EnhancementWaitTimeCompleteRequestEvent;
import com.lvl6.mobsters.events.response.EnhancementWaitTimeCompleteResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentExpProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class EnhancementWaitTimeCompleteController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(EnhancementWaitTimeCompleteController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public EnhancementWaitTimeCompleteController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new EnhancementWaitTimeCompleteRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_ENHANCEMENT_WAIT_TIME_COMPLETE_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final EnhancementWaitTimeCompleteRequestProto reqProto =
		    ((EnhancementWaitTimeCompleteRequestEvent) event).getEnhancementWaitTimeCompleteRequestProto();

		// get values sent from the client (the request proto)
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final boolean isSpeedUp = reqProto.getIsSpeedup();
		final int gemsForSpeedUp = reqProto.getGemsForSpeedup();
		final UserMonsterCurrentExpProto umcep = reqProto.getUmcep();
		// user monster ids that will be deleted from monster enhancing for user
		// table
		List<Long> userMonsterUuidsThatFinished = reqProto.getUserMonsterUuidsList();
		userMonsterUuidsThatFinished = new ArrayList<Long>(userMonsterUuidsThatFinished);
		final Timestamp curTime = new Timestamp((new Date()).getTime());

		// set some values to send to the client (the response proto)
		final EnhancementWaitTimeCompleteResponseProto.Builder resBuilder =
		    EnhancementWaitTimeCompleteResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(EnhancementWaitTimeCompleteStatus.FAIL_OTHER); // default

		svcTxManager.beginTransaction();
		try {
			int previousGems = 0;
			final List<Long> userMonsterUuids = new ArrayList<Long>();
			userMonsterUuids.add(umcep.getUserMonsterId()); // monster being
			                                                // enhanced
			userMonsterUuids.addAll(userMonsterUuidsThatFinished);

			// get whatever we need from the database
			final User aUser = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			final Map<Long, MonsterEnhancingForUser> inEnhancing =
			    MonsterEnhancingForUserRetrieveUtils.getMonstersForUser(userUuid);
			final Map<Long, MonsterForUser> idsToUserMonsters =
			    RetrieveUtils.monsterForUserRetrieveUtils()
			        .getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids);

			// do check to make sure one monster has a null start time
			final boolean legit =
			    checkLegit(resBuilder, aUser, userUuid, idsToUserMonsters, inEnhancing, umcep,
			        userMonsterUuidsThatFinished, isSpeedUp, gemsForSpeedUp);

			final Map<String, Integer> money = new HashMap<String, Integer>();
			boolean successful = false;
			if (legit) {
				previousGems = aUser.getGems();
				successful =
				    writeChangesToDb(aUser, userUuid, curTime, inEnhancing, umcep,
				        userMonsterUuidsThatFinished, isSpeedUp, gemsForSpeedUp, money);
			}
			if (successful) {
				setResponseBuilder(resBuilder);
			}

			final EnhancementWaitTimeCompleteResponseEvent resEvent =
			    new EnhancementWaitTimeCompleteResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setEnhancementWaitTimeCompleteResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in EnhancementWaitTimeCompleteController.processRequestEvent",
				    e);
			}

			if (successful) {
				// tell the client to update user because user's funds most
				// likely changed
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
					    "fatal exception in EnhancementWaitTimeCompleteController.processRequestEvent",
					    e);
				}

				writeChangesToHistory(userUuid, inEnhancing, userMonsterUuidsThatFinished);
				writeToUserCurrencyHistory(aUser, curTime, umcep.getUserMonsterId(), money,
				    previousGems);
			}
		} catch (final Exception e) {
			LOG.error("exception in EnhancementWaitTimeCompleteController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(EnhancementWaitTimeCompleteStatus.FAIL_OTHER);
				final EnhancementWaitTimeCompleteResponseEvent resEvent =
				    new EnhancementWaitTimeCompleteResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setEnhancementWaitTimeCompleteResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in EnhancementWaitTimeCompleteController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in EnhancementWaitTimeCompleteController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	/**
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value.
	 * 
	 * Will return fail if user does not have enough funds. For the most part,
	 * will always return success. Why? Answer: For @healedUp, the monsters the
	 * client thinks completed healing, only existing/valid ids will be taken
	 * off the healing queue.
	 * 
	 * Ex. Queue is (a,b,c,d). a is the base monster, b,c,d are the feeders. If
	 * user says monster (b, e) finished enhancing a, only the valid monsters
	 * (b) will be removed from the queue, leaving (a,c,d)
	 * 
	 * @param resBuilder
	 * @param u
	 * @param userUuid
	 * @param idsToUserMonsters
	 *            - the monsters the user has
	 * @param inEnhancing
	 *            - the monsters that are in the enhancing queue
	 * @param umcep
	 *            - the base monster that is updated from using up some of the
	 *            feeders
	 * @param usedUpUserMonsterUuids
	 *            - userMonsterUuids the user thinks has finished being enhanced
	 * @param speedUUp
	 * @param gemsForSpeedUp
	 * @return
	 */
	private boolean checkLegit( final Builder resBuilder, final User u, final String userUuid,
	    final Map<Long, MonsterForUser> idsToUserMonsters,
	    final Map<Long, MonsterEnhancingForUser> inEnhancing,
	    final UserMonsterCurrentExpProto umcep, final List<Long> usedUpMonsterUuids,
	    final boolean speedup, final int gemsForSpeedup )
	{

		if ((null == u)
		    || (null == umcep)
		    || usedUpMonsterUuids.isEmpty()) {
			LOG.error("unexpected error: user or idList is null. user="
			    + u
			    + "\t umcep="
			    + umcep
			    + "usedUpMonsterUuids="
			    + usedUpMonsterUuids
			    + "\t speedup="
			    + speedup
			    + "\t gemsForSpeedup="
			    + gemsForSpeedup);
			return false;
		}
		LOG.info("inEnhancing="
		    + inEnhancing);
		final long userMonsterIdBeingEnhanced = umcep.getUserMonsterId();

		// make sure that the user monster ids that will be deleted will only be
		// the ids that exist in enhancing table
		final Set<Long> inEnhancingUuids = inEnhancing.keySet();
		MonsterStuffUtils.retainValidMonsterUuids(inEnhancingUuids, usedUpMonsterUuids);

		// check to make sure the base monsterId is in enhancing
		if (!inEnhancingUuids.contains(userMonsterIdBeingEnhanced)) {
			LOG.error("client did not send updated base monster specifying what new exp and lvl are");
			return false;
		}

		/* NOT SURE IF THESE ARE NECESSARY, SO DOING IT ANYWAY */
		// check to make sure the monster being enhanced is part of the
		// user's monsters
		if (!idsToUserMonsters.containsKey(userMonsterIdBeingEnhanced)) {
			LOG.error("monster being enhanced doesn't exist!. userMonsterIdBeingEnhanced="
			    + userMonsterIdBeingEnhanced
			    + "\t deleteUuids="
			    + usedUpMonsterUuids
			    + "\t inEnhancing="
			    + inEnhancing
			    + "\t gemsForSpeedup="
			    + gemsForSpeedup
			    + "\t speedup="
			    + speedup);
			return false;
		}

		// retain only the valid monster for user ids that will be deleted
		final Set<Long> existingUuids = idsToUserMonsters.keySet();
		MonsterStuffUtils.retainValidMonsterUuids(existingUuids, usedUpMonsterUuids);

		// CHECK MONEY and CHECK SPEEDUP
		if (speedup) {
			final int userGems = u.getGems();

			if (userGems < gemsForSpeedup) {
				LOG.error("user does not have enough gems to speed up enhancing.userGems="
				    + userGems
				    + "\t cost="
				    + gemsForSpeedup
				    + "\t umcep="
				    + umcep
				    + "\t inEnhancing="
				    + inEnhancing
				    + "\t deleteUuids="
				    + usedUpMonsterUuids);
				resBuilder.setStatus(EnhancementWaitTimeCompleteStatus.FAIL_INSUFFICIENT_FUNDS);
				return false;
			}
		}

		resBuilder.setStatus(EnhancementWaitTimeCompleteStatus.SUCCESS);
		return true;
	}

	private boolean writeChangesToDb( final User u, final int uId, final Timestamp clientTime,
	    final Map<Long, MonsterEnhancingForUser> inEnhancing,
	    final UserMonsterCurrentExpProto umcep, final List<Long> userMonsterUuids,
	    final boolean isSpeedup, final int gemsForSpeedup, final Map<String, Integer> money )
	{

		if (isSpeedup) {
			// CHARGE THE USER
			final int gemCost = -1
			    * gemsForSpeedup;
			if (!u.updateRelativeGemsNaive(gemCost)) {
				LOG.error("problem with updating user gems. gemsForSpeedup="
				    + gemsForSpeedup
				    + ", clientTime="
				    + clientTime
				    + ", baseMonster"
				    + umcep
				    + ", clientTime="
				    + clientTime
				    + ", userMonsterUuidsToDelete="
				    + userMonsterUuids
				    + ", user="
				    + u);
				return false;
			} else {
				if (0 != gemCost) {
					money.put(MiscMethods.gems, gemCost);
				}
			}
		}

		final long userMonsterIdBeingEnhanced = umcep.getUserMonsterId();
		final int newExp = umcep.getExpectedExperience();
		final int newLvl = umcep.getExpectedLevel();

		// GIVE THE MONSTER EXP
		final int num = UpdateUtils.get()
		    .updateUserMonsterExpAndLvl(userMonsterIdBeingEnhanced, newExp, newLvl);
		LOG.info("num updated="
		    + num);

		return true;
	}

	private void setResponseBuilder( final Builder resBuilder )
	{
	}

	private void writeChangesToHistory( final int uId,
	    final Map<Long, MonsterEnhancingForUser> inEnhancing, final List<Long> userMonsterUuids )
	{

		// TODO: keep track of the userMonsters that are deleted

		// TODO: keep track of the monsters that were enhancing

		// delete the selected monsters from the enhancing table
		int num = DeleteUtils.get()
		    .deleteMonsterEnhancingForUser(uId, userMonsterUuids);
		LOG.info("deleted monster healing rows. numDeleted="
		    + num
		    + "\t userMonsterUuids="
		    + userMonsterUuids
		    + "\t inEnhancing="
		    + inEnhancing);

		// delete the userMonsterUuids from the monster_for_user table, but
		// don't delete
		// the monster user is enhancing
		num = DeleteUtils.get()
		    .deleteMonstersForUser(userMonsterUuids);
		LOG.info("defeated monster_for_user rows. numDeleted="
		    + num
		    + "\t inEnhancing="
		    + inEnhancing);

	}

	private void writeToUserCurrencyHistory( final User aUser, final Timestamp curTime,
	    final long userMonsterId, final Map<String, Integer> money, final int previousGems )
	{
		if (money.isEmpty()) {
			return;
		}
		final String gems = MiscMethods.gems;
		final String reasonForChange = ControllerConstants.UCHRFC__SPED_UP_ENHANCING;

		final String userUuid = aUser.getId();
		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();

		previousCurrencies.put(gems, previousGems);
		currentCurrencies.put(gems, aUser.getGems());
		reasonsForChanges.put(gems, reasonForChange);
		detailsMap.put(gems, " userMonsterId="
		    + userMonsterId);
		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, money, previousCurrencies,
		    currentCurrencies, reasonsForChanges, detailsMap);

	}

}
