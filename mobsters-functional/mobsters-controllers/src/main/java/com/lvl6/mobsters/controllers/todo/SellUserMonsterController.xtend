package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto.SellUserMonsterStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.SellUserMonsterRequestEvent
import com.lvl6.mobsters.events.response.SellUserMonsterResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class SellUserMonsterController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(SellUserMonsterController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new SellUserMonsterRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_SELL_USER_MONSTER_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as SellUserMonsterRequestEvent)).sellUserMonsterRequestProto
		val senderResourcesProto = reqProto.sender
		val senderProto = senderResourcesProto.minUserProto
		val userUuid = senderProto.userUuid
		val userMonsters = reqProto.salesList
		val userMonsterUuidsToCashAmounts = MonsterStuffUtils::
			convertToMonsterForUserIdToCashAmount(userMonsters)
		val userMonsterUuidsSet = userMonsterUuidsToCashAmounts.keySet
		val userMonsterUuids = new ArrayList<Long>(userMonsterUuidsSet)
		val deleteDate = new Date()
		val deleteTime = new Timestamp(deleteDate.time)
		val maxCash = senderResourcesProto.maxCash
		val resBuilder = SellUserMonsterResponseProto::newBuilder
		resBuilder.sender = senderResourcesProto
		resBuilder.status = SellUserMonsterStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			var previousCash = 0
			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			val idsToUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
				getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids)
			val legit = checkLegit(resBuilder, userUuid, aUser, userMonsterUuids,
				idsToUserMonsters)
			val currencyChange = new HashMap<String, Integer>()
			var successful = false
			if (legit)
			{
				previousCash = aUser.cash
				successful = writeChangesToDb(aUser, userMonsterUuids,
					userMonsterUuidsToCashAmounts, maxCash, currencyChange)
			}
			if (successful)
			{
				resBuilder.status = SellUserMonsterStatus.SUCCESS
			}
			val resEvent = new SellUserMonsterResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.sellUserMonsterResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in SellUserMonsterController.processRequestEvent', e)
			}
			if (successful)
			{
				val resEventUpdate = MiscMethods::
					createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser, null)
				resEventUpdate.tag = event.tag
				LOG.info('Writing event: ' + resEventUpdate)
				try
				{
					eventWriter.writeEvent(resEventUpdate)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in SellUserMonsterController.processRequestEvent',
						e)
				}
				writeChangesToHistory(userUuid, userMonsterUuids, userMonsterUuidsToCashAmounts,
					idsToUserMonsters, deleteDate)
				writeToUserCurrencyHistory(userUuid, aUser, previousCash, deleteTime,
					userMonsterUuidsToCashAmounts, userMonsterUuids, currencyChange)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in SellUserMonsterController processEvent', e)
			try
			{
				resBuilder.status = SellUserMonsterStatus.FAIL_OTHER
				val resEvent = new SellUserMonsterResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.sellUserMonsterResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in SellUserMonsterController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in SellUserMonsterController processEvent', e)
			}
		}
		finally
		{
			locker.unlockPlayer(senderProto.userUuid, this.class.simpleName)
		}
	}

	private def checkLegit(Builder resBuilder, String userUuid, User u,
		List<Long> userMonsterUuids, Map<Long, MonsterForUser> idsToUserMonsters)
	{
		if (null === u)
		{
			LOG.error('user is null. no user exists with id=' + userUuid + '')
			return false;
		}
		if ((null === userMonsterUuids) || userMonsterUuids.empty || idsToUserMonsters.empty)
		{
			LOG.error(
				'no user monsters exist. userMonsterUuids=' + userMonsterUuids +
					'	 idsToUserMonsters=' + idsToUserMonsters)
			return false;
		}
		if (userMonsterUuids.size !== idsToUserMonsters.size)
		{
			LOG.warn(
				'not all monster_for_user_ids exist. userMonsterUuids=' + userMonsterUuids +
					'	 idsToUserMonsters=' + idsToUserMonsters + '	. Will continue processing')
			userMonsterUuids.clear
			userMonsterUuids.addAll(idsToUserMonsters.keySet)
		}
		resBuilder.status = SellUserMonsterStatus.SUCCESS
		true
	}

	private def writeChangesToDb(User aUser, List<Long> userMonsterUuids,
		Map<Long, Integer> userMonsterUuidsToCashAmounts, int maxCash,
		Map<String, Integer> currencyChange)
	{
		val success = true
		var sum = MiscMethods::sumMapValues(userMonsterUuidsToCashAmounts)
		val curCash = Math::min(aUser.cash, maxCash)
		val maxCashUserCanGain = maxCash - curCash
		sum = Math::min(sum, maxCashUserCanGain)
		if (0 !== sum)
		{
			if (!aUser.updateRelativeCashNaive(sum))
			{
				LOG.error(
					'error updating user coins by ' + sum + ' not deleting ' +
						'userMonstersUuidsToCashAmounts=' + userMonsterUuidsToCashAmounts)
				return false;
			}
			else
			{
				currencyChange.put(MiscMethods::cash, sum)
			}
		}
		if ((null !== userMonsterUuids) && !userMonsterUuids.empty)
		{
			val num = DeleteUtils::get.deleteMonstersForUser(userMonsterUuids)
			LOG.info('num user monsters deleted: ' + num + '	 ids deleted: ' + userMonsterUuids)
		}
		success
	}

	private def writeChangesToHistory(String userUuid, List<Long> userMonsterUuids,
		Map<Long, Integer> userMonsterUuidsToCashAmounts,
		Map<Long, MonsterForUser> idsToUserMonsters, Date deleteDate)
	{
		if ((null === userMonsterUuids) || userMonsterUuids.empty)
		{
			return;
		}
		val delReason = ControllerConstants.MFUDR__SELL
		val deleteReasons = new ArrayList<String>()
		val userMonstersList = new ArrayList<MonsterForUser>()
		for (i : 0 ..< userMonsterUuids.size)
		{
			val userMonsterId = userMonsterUuids.get(i)
			val amount = userMonsterUuidsToCashAmounts.get(userMonsterId)
			val mfu = idsToUserMonsters.get(userMonsterId)
			userMonstersList.add(mfu)
			deleteReasons.add(delReason + amount)
		}
	}

	def writeToUserCurrencyHistory(String userUuid, User aUser, int previousCash,
		Timestamp aDate, Map<Long, Integer> userMonsterUuidsToCashAmounts,
		List<Long> userMonsterUuids, Map<String, Integer> currencyChange)
	{
		if (currencyChange.empty)
		{
			return;
		}
		val detailsSb = new StringBuilder()
		for (umId : userMonsterUuidsToCashAmounts.keySet)
		{
			val cash = userMonsterUuidsToCashAmounts.get(umId)
			detailsSb.append('mfuId=')
			detailsSb.append(umId)
			detailsSb.append(', cash=')
			detailsSb.append(cash)
		}
		val previousCurrency = new HashMap<String, Integer>()
		val currentCurrency = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		val reason = ControllerConstants.UCHRFC__SOLD_USER_MONSTERS
		val cash = MiscMethods::cash
		previousCurrency.put(cash, previousCash)
		currentCurrency.put(cash, aUser.cash)
		reasonsForChanges.put(cash, reason)
		detailsMap.put(cash, detailsSb.toString)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, aDate, currencyChange,
			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
	}
}
