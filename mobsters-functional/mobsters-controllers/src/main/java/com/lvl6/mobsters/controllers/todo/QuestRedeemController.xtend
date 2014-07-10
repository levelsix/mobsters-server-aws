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

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemRequestProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemResponseProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemResponseProto.QuestRedeemStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.QuestRedeemRequestEvent;
import com.lvl6.mobsters.events.response.QuestRedeemResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class QuestRedeemController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(QuestRedeemController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public QuestRedeemController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new QuestRedeemRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_QUEST_REDEEM_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final QuestRedeemRequestProto reqProto =
		    ((QuestRedeemRequestEvent) event).getQuestRedeemRequestProto();

		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
		final String userUuid = senderProto.getUserUuid();
		final int questId = reqProto.getQuestUuid();
		final Date currentDate = new Date();
		final Timestamp now = new Timestamp(currentDate.getTime());
		final int maxCash = senderResourcesProto.getMaxCash();
		final int maxOil = senderResourcesProto.getMaxOil();

		final QuestRedeemResponseProto.Builder resBuilder =
		    QuestRedeemResponseProto.newBuilder();
		resBuilder.setSender(senderResourcesProto);
		resBuilder.setStatus(QuestRedeemStatus.FAIL_OTHER);
		resBuilder.setQuestId(questId);

		svcTxManager.beginTransaction();
		try {
			// retrieve whatever is necessary from the db

			final QuestForUser userQuest = RetrieveUtils.questForUserRetrieveUtils()
			    .getSpecificUnredeemedUserQuest(userUuid, questId);
			final Quest quest = QuestRetrieveUtils.getQuestForQuestId(questId);
			boolean legitRedeem = checkLegitRedeem(resBuilder, userQuest, quest);

			if (legitRedeem) {

				// calculate the available quests for this user
				setAvailableQuests(userUuid, questId, resBuilder);

				// give user the monster reward, if any, and send this to the
				// client
				legitRedeem =
				    awardMonsterReward(resBuilder, userUuid, quest, questId, currentDate);
			}

			final QuestRedeemResponseEvent resEvent =
			    new QuestRedeemResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setQuestRedeemResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in QuestRedeemController.processRequestEvent", e);
			}

			if (legitRedeem) {
				final User user = RetrieveUtils.userRetrieveUtils()
				    .getUserById(senderProto.getUserUuid());

				final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
				final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
				writeChangesToDB(userQuest, quest, user, senderProto, maxCash, maxOil,
				    previousCurrency, currencyChange);
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
					LOG.error("fatal exception in QuestRedeemController.processRequestEvent", e);
				}

				writeToUserCurrencyHistory(user, userUuid, questId, currencyChange,
				    previousCurrency, now);
			}
		} catch (final Exception e) {
			LOG.error("exception in QuestRedeem processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(QuestRedeemStatus.FAIL_OTHER);
				final QuestRedeemResponseEvent resEvent =
				    new QuestRedeemResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setQuestRedeemResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in QuestRedeemController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in QuestRedeem processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegitRedeem( final Builder resBuilder, final QuestForUser userQuest,
	    final Quest quest )
	{
		if ((userQuest == null)
		    || userQuest.isRedeemed()) {
			resBuilder.setStatus(QuestRedeemStatus.FAIL_OTHER);
			LOG.error("user quest is null or redeemed already. userQuest="
			    + userQuest);
			return false;
		}
		if (!userQuest.isComplete()) {
			resBuilder.setStatus(QuestRedeemStatus.FAIL_NOT_COMPLETE);
			LOG.error("user quest is not complete");
			return false;
		}
		resBuilder.setStatus(QuestRedeemStatus.SUCCESS);
		return true;
	}

	private void setAvailableQuests( final String userUuid, final int questId,
	    final Builder resBuilder )
	{
		final List<QuestForUser> inProgressAndRedeemedQuestForUsers =
		    RetrieveUtils.questForUserRetrieveUtils()
		        .getUserQuestsForUser(userUuid);
		final List<Integer> inProgressQuestUuids = new ArrayList<Integer>();
		final List<Integer> redeemedQuestUuids = new ArrayList<Integer>();

		if (inProgressAndRedeemedQuestForUsers != null) {
			// group things into redeemed and unredeemed
			for (final QuestForUser uq : inProgressAndRedeemedQuestForUsers) {
				if (uq.isRedeemed()
				    || (uq.getQuestId() == questId)) {
					redeemedQuestUuids.add(uq.getQuestId());
				} else {
					inProgressQuestUuids.add(uq.getQuestId());
				}
			}
			final List<Integer> availableQuestUuids =
			    QuestUtils.getAvailableQuestsForUser(redeemedQuestUuids, inProgressQuestUuids);

			// from the available quests, create protos out of the quests that
			// had
			// the quest user just redeemed as a prerequisite
			final Map<Integer, Quest> questUuidsToQuests =
			    QuestRetrieveUtils.getQuestUuidsToQuests();
			for (final Integer availableQuestId : availableQuestUuids) {
				final Quest q = questUuidsToQuests.get(availableQuestId);
				if (q.getQuestsRequiredForThis()
				    .contains(questId)) {
					resBuilder.addNewlyAvailableQuests(CreateInfoProtoUtils.createFullQuestProtoFromQuest(q));
				}
			}
		}
	}

	private boolean awardMonsterReward( final Builder resBuilder, final String userUuid,
	    final Quest quest, final int questId, final Date combineStartDate )
	{
		boolean legitRedeem = true;

		final int monsterIdReward = quest.getMonsterIdReward();
		if (monsterIdReward > 0) {
			// WHEN GIVING USER A MONSTER, CALL
			// MonsterStuffUtils.updateUserMonsters(...)
			final Map<Integer, Integer> monsterIdToNumPieces = new HashMap<Integer, Integer>();
			monsterIdToNumPieces.put(monsterIdReward, 1);

			final String mfusop = ControllerConstants.MFUSOP__QUEST
			    + questId;
			final List<FullUserMonsterProto> reward =
			    MonsterStuffUtils.updateUserMonsters(userUuid, monsterIdToNumPieces, mfusop,
			        combineStartDate);

			if (reward.isEmpty()) {
				resBuilder.setStatus(QuestRedeemStatus.FAIL_OTHER);
				LOG.error("problem with giving user 1 monster after completing the quest, monsterId="
				    + monsterIdReward
				    + ", quest= "
				    + quest);
				legitRedeem = false;
			} else {
				final FullUserMonsterProto fump = reward.get(0);
				resBuilder.setFump(fump);
			}
		}

		return legitRedeem;
	}

	private void writeChangesToDB( final QuestForUser userQuest, final Quest quest,
	    final User user, final MinimumUserProto senderProto, final int maxCash,
	    final int maxOil, final Map<String, Integer> previousCurrency,
	    final Map<String, Integer> money )
	{
		if (!UpdateUtils.get()
		    .updateRedeemQuestForUser(userQuest.getUserUuid(), userQuest.getQuestId())) {
			LOG.error("problem with marking user quest as redeemed. questId="
			    + userQuest.getQuestId());
		}

		previousCurrency.put(MiscMethods.gems, user.getGems());
		previousCurrency.put(MiscMethods.cash, user.getCash());
		previousCurrency.put(MiscMethods.oil, user.getOil());

		int cashGain = Math.max(0, quest.getCashReward());
		int oilGain = Math.max(0, quest.getOilReward());
		final int gemsGained = Math.max(0, quest.getGemReward());
		final int expGained = Math.max(0, quest.getExpReward());

		final int curCash = Math.min(user.getCash(), maxCash); // in case user's
															   // cash
		// is more than maxCash
		final int maxCashUserCanGain = maxCash
		    - curCash; // this is the max cash the user can gain
		cashGain = Math.min(maxCashUserCanGain, cashGain);

		final int curOil = Math.max(user.getOil(), maxOil);
		final int maxOilUserCanGain = maxOil
		    - curOil;
		oilGain = Math.min(maxOilUserCanGain, oilGain);

		if ((0 == gemsGained)
		    && (0 == cashGain)
		    && (0 == expGained)
		    && (0 == oilGain)) {
			LOG.info("user does not get any gems, cash, or exp from redeeming quest="
			    + quest
			    + " because user is maxed out on resources, and quest doesn't given exp nor gems.");
			return;
		}

		if (!user.updateRelativeGemsCashOilExperienceNaive(gemsGained, cashGain, oilGain,
		    expGained)) {
			LOG.error("problem with giving user "
			    + gemsGained
			    + " diamonds, "
			    + cashGain
			    + " cash, "
			    + expGained
			    + " exp, "
			    + oilGain
			    + " oilGain");
		} else {
			// things worked
			if (0 != gemsGained) {
				money.put(MiscMethods.gems, gemsGained);
			}
			if (0 != cashGain) {
				money.put(MiscMethods.cash, cashGain);
			}
			if (0 != oilGain) {
				money.put(MiscMethods.oil, oilGain);
			}
		}
	}

	public void writeToUserCurrencyHistory( final User aUser, final String userUuid,
	    final int questId, final Map<String, Integer> currencyChange,
	    final Map<String, Integer> previousCurrency, final Timestamp curTime )
	{

		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();
		final String gems = MiscMethods.gems;
		final String cash = MiscMethods.cash;
		final String oil = MiscMethods.oil;

		final String reason = ControllerConstants.UCHRFC__QUEST_REDEEM;
		final StringBuilder detailsSb = new StringBuilder();
		detailsSb.append("quest redeemed=");
		detailsSb.append(questId);
		final String details = detailsSb.toString();

		currentCurrency.put(gems, aUser.getGems());
		currentCurrency.put(cash, aUser.getCash());
		currentCurrency.put(oil, aUser.getOil());
		reasonsForChanges.put(gems, reason);
		reasonsForChanges.put(cash, reason);
		reasonsForChanges.put(oil, reason);
		detailsMap.put(gems, details);
		detailsMap.put(cash, details);
		detailsMap.put(oil, details);

		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
	}

}
