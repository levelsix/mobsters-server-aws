package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.QuestForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemResponseProto
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemResponseProto.QuestRedeemStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.QuestRedeemRequestEvent
import com.lvl6.mobsters.events.response.QuestRedeemResponseEvent
import com.lvl6.mobsters.info.Quest
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class QuestRedeemController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(QuestRedeemController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new QuestRedeemRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_QUEST_REDEEM_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as QuestRedeemRequestEvent)).questRedeemRequestProto
		val senderResourcesProto = reqProto.sender
		val senderProto = senderResourcesProto.minUserProto
		val userUuid = senderProto.userUuid
		val questId = reqProto.questUuid
		val currentDate = new Date()
		val now = new Timestamp(currentDate.time)
		val maxCash = senderResourcesProto.maxCash
		val maxOil = senderResourcesProto.maxOil
		val resBuilder = QuestRedeemResponseProto::newBuilder
		resBuilder.sender = senderResourcesProto
		resBuilder.status = QuestRedeemStatus.FAIL_OTHER
		resBuilder.questId = questId
		svcTxManager.beginTransaction
		try
		{
			val userQuest = RetrieveUtils::questForUserRetrieveUtils.
				getSpecificUnredeemedUserQuest(userUuid, questId)
			val quest = QuestRetrieveUtils::getQuestForQuestId(questId)
			var legitRedeem = checkLegitRedeem(resBuilder, userQuest, quest)
			if (legitRedeem)
			{
				setAvailableQuests(userUuid, questId, resBuilder)
				legitRedeem = awardMonsterReward(resBuilder, userUuid, quest, questId,
					currentDate)
			}
			val resEvent = new QuestRedeemResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.questRedeemResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in QuestRedeemController.processRequestEvent', e)
			}
			if (legitRedeem)
			{
				val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
				val previousCurrency = new HashMap<String, Integer>()
				val currencyChange = new HashMap<String, Integer>()
				writeChangesToDB(userQuest, quest, user, senderProto, maxCash, maxOil,
					previousCurrency, currencyChange)
				val resEventUpdate = MiscMethods::
					createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null)
				resEventUpdate.tag = event.tag
				LOG.info('Writing event: ' + resEventUpdate)
				try
				{
					eventWriter.writeEvent(resEventUpdate)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in QuestRedeemController.processRequestEvent', e)
				}
				writeToUserCurrencyHistory(user, userUuid, questId, currencyChange,
					previousCurrency, now)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in QuestRedeem processEvent', e)
			try
			{
				resBuilder.status = QuestRedeemStatus.FAIL_OTHER
				val resEvent = new QuestRedeemResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.questRedeemResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in QuestRedeemController.processRequestEvent', e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in QuestRedeem processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegitRedeem(Builder resBuilder, QuestForUser userQuest, Quest quest)
	{
		if ((userQuest === null) || userQuest.redeemed)
		{
			resBuilder.status = QuestRedeemStatus.FAIL_OTHER
			LOG.error('user quest is null or redeemed already. userQuest=' + userQuest)
			return false;
		}
		if (!userQuest.complete)
		{
			resBuilder.status = QuestRedeemStatus.FAIL_NOT_COMPLETE
			LOG.error('user quest is not complete')
			return false;
		}
		resBuilder.status = QuestRedeemStatus.SUCCESS
		true
	}

	private def setAvailableQuests(String userUuid, int questId, Builder resBuilder)
	{
		val inProgressAndRedeemedQuestForUsers = RetrieveUtils::questForUserRetrieveUtils.
			getUserQuestsForUser(userUuid)
		val inProgressQuestUuids = new ArrayList<Integer>()
		val redeemedQuestUuids = new ArrayList<Integer>()
		if (inProgressAndRedeemedQuestForUsers !== null)
		{
			for (uq : inProgressAndRedeemedQuestForUsers)
			{
				if (uq.redeemed || (uq.questId === questId))
				{
					redeemedQuestUuids.add(uq.questId)
				}
				else
				{
					inProgressQuestUuids.add(uq.questId)
				}
			}
			val availableQuestUuids = QuestUtils::
				getAvailableQuestsForUser(redeemedQuestUuids, inProgressQuestUuids)
			val questUuidsToQuests = QuestRetrieveUtils::questUuidsToQuests
			for (availableQuestId : availableQuestUuids)
			{
				val q = questUuidsToQuests.get(availableQuestId)
				if (q.questsRequiredForThis.contains(questId))
				{
					resBuilder.addNewlyAvailableQuests(
						CreateInfoProtoUtils::createFullQuestProtoFromQuest(q))
				}
			}
		}
	}

	private def awardMonsterReward(Builder resBuilder, String userUuid, Quest quest,
		int questId, Date combineStartDate)
	{
		var legitRedeem = true
		val monsterIdReward = quest.monsterIdReward
		if (monsterIdReward > 0)
		{
			val monsterIdToNumPieces = new HashMap<Integer, Integer>()
			monsterIdToNumPieces.put(monsterIdReward, 1)
			val mfusop = ControllerConstants::MFUSOP__QUEST + questId
			val reward = MonsterStuffUtils::updateUserMonsters(userUuid, monsterIdToNumPieces,
				mfusop, combineStartDate)
			if (reward.empty)
			{
				resBuilder.status = QuestRedeemStatus.FAIL_OTHER
				LOG.error(
					'problem with giving user 1 monster after completing the quest, monsterId=' +
						monsterIdReward + ', quest= ' + quest)
				legitRedeem = false
			}
			else
			{
				val fump = reward.get(0)
				resBuilder.fump = fump
			}
		}
		legitRedeem
	}

	private def writeChangesToDB(QuestForUser userQuest, Quest quest, User user,
		MinimumUserProto senderProto, int maxCash, int maxOil,
		Map<String, Integer> previousCurrency, Map<String, Integer> money)
	{
		if (!UpdateUtils::get.updateRedeemQuestForUser(userQuest.userUuid, userQuest.questId))
		{
			LOG.error(
				'problem with marking user quest as redeemed. questId=' + userQuest.questId)
		}
		previousCurrency.put(MiscMethods::gems, user.gems)
		previousCurrency.put(MiscMethods::cash, user.cash)
		previousCurrency.put(MiscMethods::oil, user.oil)
		var cashGain = Math::max(0, quest.cashReward)
		var oilGain = Math::max(0, quest.oilReward)
		val gemsGained = Math::max(0, quest.gemReward)
		val expGained = Math::max(0, quest.expReward)
		val curCash = Math::min(user.cash, maxCash)
		val maxCashUserCanGain = maxCash - curCash
		cashGain = Math::min(maxCashUserCanGain, cashGain)
		val curOil = Math::max(user.oil, maxOil)
		val maxOilUserCanGain = maxOil - curOil
		oilGain = Math::min(maxOilUserCanGain, oilGain)
		if ((0 === gemsGained) && (0 === cashGain) && (0 === expGained) && (0 === oilGain))
		{
			LOG.info(
				'user does not get any gems, cash, or exp from redeeming quest=' + quest +
					" because user is maxed out on resources, and quest doesn't given exp nor gems.")
			return;
		}
		if (!user.
			updateRelativeGemsCashOilExperienceNaive(gemsGained, cashGain, oilGain, expGained))
		{
			LOG.error(
				'problem with giving user ' + gemsGained + ' diamonds, ' + cashGain + ' cash, ' +
					expGained + ' exp, ' + oilGain + ' oilGain')
		}
		else
		{
			if (0 !== gemsGained)
			{
				money.put(MiscMethods::gems, gemsGained)
			}
			if (0 !== cashGain)
			{
				money.put(MiscMethods::cash, cashGain)
			}
			if (0 !== oilGain)
			{
				money.put(MiscMethods::oil, oilGain)
			}
		}
	}

	def writeToUserCurrencyHistory(User aUser, String userUuid, int questId,
		Map<String, Integer> currencyChange, Map<String, Integer> previousCurrency,
		Timestamp curTime)
	{
		val currentCurrency = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		val gems = MiscMethods::gems
		val cash = MiscMethods::cash
		val oil = MiscMethods::oil
		val reason = ControllerConstants.UCHRFC__QUEST_REDEEM
		val detailsSb = new StringBuilder()
		detailsSb.append('quest redeemed=')
		detailsSb.append(questId)
		val details = detailsSb.toString
		currentCurrency.put(gems, aUser.gems)
		currentCurrency.put(cash, aUser.cash)
		currentCurrency.put(oil, aUser.oil)
		reasonsForChanges.put(gems, reason)
		reasonsForChanges.put(cash, reason)
		reasonsForChanges.put(oil, reason)
		detailsMap.put(gems, details)
		detailsMap.put(cash, details)
		detailsMap.put(oil, details)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
	}
}
