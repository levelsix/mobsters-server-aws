package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.AchievementForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemRequestProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto.AchievementRedeemStatus;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto.Builder;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.AchievementRedeemRequestEvent;
import com.lvl6.mobsters.events.response.AchievementRedeemResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class AchievementRedeemController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(AchievementRedeemController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected AchievementForUserRetrieveUtil achievementForUserRetrieveUtil;

	public AchievementRedeemController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new AchievementRedeemRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_ACHIEVEMENT_REDEEM_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final AchievementRedeemRequestProto reqProto =
		    ((AchievementRedeemRequestEvent) event).getAchievementRedeemRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int achievementId = reqProto.getAchievementUuid();
		final Date currentDate = new Date();
		final Timestamp now = new Timestamp(currentDate.getTime());

		final AchievementRedeemResponseProto.Builder resBuilder =
		    AchievementRedeemResponseProto.newBuilder();
		resBuilder.setStatus(AchievementRedeemStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			// retrieve whatever is necessary from the db
			// TODO: consider only retrieving user if the request is valid
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			int previousGems = 0;

			final Map<Integer, AchievementForUser> achievementIdToUserAchievement =
			    getAchievementForUserRetrieveUtil().getSpecificOrAllAchievementIdToAchievementForUserUuid(
			        userUuid, Collections.singleton(achievementId));

			final boolean legitRedeem =
			    checkLegitRedeem(resBuilder, userUuid, achievementId,
			        achievementIdToUserAchievement);

			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
			boolean success = false;
			if (legitRedeem) {
				previousGems = user.getGems();

				success = writeChangesToDB(userUuid, user, achievementId, now, currencyChange);
			}

			if (success) {
				resBuilder.setStatus(AchievementRedeemStatus.SUCCESS);
			}

			final AchievementRedeemResponseEvent resEvent =
			    new AchievementRedeemResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setAchievementRedeemResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in AchievementRedeemController.processRequestEvent",
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
					    "fatal exception in AchievementRedeemController.processRequestEvent", e);
				}

				final Map<String, Integer> previousCurrency =
				    Collections.singletonMap(MiscMethods.gems, previousGems);
				writeToUserCurrencyHistory(user, userUuid, achievementId, currencyChange,
				    previousCurrency, now);
			}

		} catch (final Exception e) {
			LOG.error("exception in AchievementRedeem processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(AchievementRedeemStatus.FAIL_OTHER);
				final AchievementRedeemResponseEvent resEvent =
				    new AchievementRedeemResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setAchievementRedeemResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in AchievementRedeemController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in AchievementRedeem processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegitRedeem( final Builder resBuilder, final String userUuid,
	    final int achievementId,
	    final Map<Integer, AchievementForUser> achievementIdToUserAchievement )
	{
		if ((null == achievementIdToUserAchievement)
		    || achievementIdToUserAchievement.isEmpty()
		    || !achievementIdToUserAchievement.containsKey(achievementId)) {
			resBuilder.setStatus(AchievementRedeemStatus.FAIL_OTHER);
			LOG.error("userAchievement does not exist. id="
			    + achievementId
			    + "userAchievement="
			    + achievementIdToUserAchievement);
			return false;
		}

		final AchievementForUser userAchievement =
		    achievementIdToUserAchievement.get(achievementId);
		if (!userAchievement.isComplete()) {
			resBuilder.setStatus(AchievementRedeemStatus.FAIL_NOT_COMPLETE);
			LOG.error("userAchievement is not complete");
			return false;
		}

		if (userAchievement.isRedeemed()) {
			resBuilder.setStatus(AchievementRedeemStatus.FAIL_ALREADY_REDEEMED);
			LOG.error("userAchievement is already redeemed: "
			    + userAchievement);
			return false;
		}

		return true;
	}

	private boolean writeChangesToDB( final String userUuid, final User user,
	    final int achievementId, final Timestamp redeemTime,
	    final Map<String, Integer> currencyChange )
	{
		final int numUpdated =
		    UpdateUtils.get()
		        .updateRedeemAchievementForUser(userUuid,
		            Collections.singletonList(achievementId), redeemTime);
		LOG.info("user achievements redeemed. numUpdated="
		    + numUpdated
		    + "\t achievementId="
		    + achievementId);

		final Achievement achievement =
		    AchievementRetrieveUtils.getAchievementForAchievementId(achievementId);
		final int gemReward = achievement.getGemReward();
		final int gemsGained = Math.max(0, gemReward);

		if (0 == gemsGained) {
			LOG.warn("redeeming achievement does not give gem reward: "
			    + achievement);
		}

		if (!user.updateRelativeGemsCashOilExperienceNaive(gemsGained, 0, 0, 0)) {
			LOG.error("problem with giving user "
			    + gemsGained
			    + " gems");
		} else {
			// things worked
			if (0 != gemsGained) {
				currencyChange.put(MiscMethods.gems, gemsGained);
			}
		}
		return true;
	}

	public void writeToUserCurrencyHistory( final User aUser, final String userUuid,
	    final int achievementId, final Map<String, Integer> currencyChange,
	    final Map<String, Integer> previousCurrency, final Timestamp curTime )
	{

		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();
		final String gems = MiscMethods.gems;

		final String reason = ControllerConstants.UCHRFC__QUEST_REDEEM;
		final StringBuilder detailsSb = new StringBuilder();
		detailsSb.append("achievement redeemed=");
		detailsSb.append(achievementId);
		final String details = detailsSb.toString();

		currentCurrency.put(gems, aUser.getGems());
		reasonsForChanges.put(gems, reason);
		detailsMap.put(gems, details);

		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
	}

	public AchievementForUserRetrieveUtil getAchievementForUserRetrieveUtil()
	{
		return achievementForUserRetrieveUtil;
	}

	public void setAchievementForUserRetrieveUtil(
	    final AchievementForUserRetrieveUtil achievementForUserRetrieveUtil )
	{
		this.achievementForUserRetrieveUtil = achievementForUserRetrieveUtil;
	}

}
