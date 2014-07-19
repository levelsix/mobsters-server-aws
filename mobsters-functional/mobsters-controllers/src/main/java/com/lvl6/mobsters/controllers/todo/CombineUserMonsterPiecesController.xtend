package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto.CombineUserMonsterPiecesStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.CombineUserMonsterPiecesRequestEvent
import com.lvl6.mobsters.events.response.CombineUserMonsterPiecesResponseEvent
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
class CombineUserMonsterPiecesController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(CombineUserMonsterPiecesController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new CombineUserMonsterPiecesRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_COMBINE_USER_MONSTER_PIECES_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as CombineUserMonsterPiecesRequestEvent)).
			combineUserMonsterPiecesRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		var userMonsterUuids = reqProto.userMonsterUuidsList
		userMonsterUuids = new ArrayList<Long>(userMonsterUuids)
		val gemCost = reqProto.gemCost
		val curDate = new Date()
		val curTime = new Timestamp(curDate.time)
		val resBuilder = CombineUserMonsterPiecesResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = CombineUserMonsterPiecesStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			var previousGems = 0
			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			val idsToUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
				getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids)
			val legit = checkLegit(resBuilder, userUuid, aUser, userMonsterUuids,
				idsToUserMonsters, gemCost)
			var successful = false
			val money = new HashMap<String, Integer>()
			if (legit)
			{
				previousGems = aUser.gems
				successful = writeChangesToDb(aUser, userMonsterUuids, gemCost, money)
			}
			if (successful)
			{
				resBuilder.status = CombineUserMonsterPiecesStatus.SUCCESS
			}
			val resEvent = new CombineUserMonsterPiecesResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.combineUserMonsterPiecesResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in CombineUserMonsterPiecesController.processRequestEvent',
					e)
			}
			if (successful && (gemCost > 0))
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
					LOG.error(
						'fatal exception in CombineUserMonsterPiecesController.processRequestEvent',
						e)
				}
				writeToUserCurrencyHistory(aUser, money, curTime, previousGems, userMonsterUuids)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in CombineUserMonsterPiecesController processEvent', e)
			try
			{
				resBuilder.status = CombineUserMonsterPiecesStatus.FAIL_OTHER
				val resEvent = new CombineUserMonsterPiecesResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.combineUserMonsterPiecesResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in CombineUserMonsterPiecesController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in CombineUserMonsterPiecesController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegit(Builder resBuilder, String userUuid, User u,
		List<Long> userMonsterUuids, Map<Long, MonsterForUser> idsToUserMonsters, int gemCost)
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
		val wholeUserMonsterUuids = MonsterStuffUtils::
			getWholeButNotCombinedUserMonsters(idsToUserMonsters)
		if (wholeUserMonsterUuids.size !== userMonsterUuids.size)
		{
			LOG.warn(
				'client trying to combine already combined or incomplete monsters.' +
					' clientSentUuids=' + userMonsterUuids + '	 wholeButIncompleteMonsterUuids=' +
					wholeUserMonsterUuids + '	 idsToUserMonsters=' + idsToUserMonsters +
					'	 Will continue processing')
			userMonsterUuids.clear
			userMonsterUuids.addAll(wholeUserMonsterUuids)
		}
		if (userMonsterUuids.empty)
		{
			resBuilder.status = CombineUserMonsterPiecesStatus.FAIL_OTHER
			LOG.error("the user didn't send any userMonsters to complete!.")
			return false;
		}
		if ((gemCost > 0) && (userMonsterUuids.size > 1))
		{
			LOG.error(
				'user speeding up combining pieces for multiple monsters can only ' +
					'speed up one monster. gemCost=' + gemCost + '	 userMonsterUuids=' +
					userMonsterUuids)
			resBuilder.status = CombineUserMonsterPiecesStatus::
				FAIL_MORE_THAN_ONE_MONSTER_FOR_SPEEDUP
			return false;
		}
		val userGems = u.gems
		if (userGems < gemCost)
		{
			LOG.error(
				"user doesn't have enough gems to speed up combining. userGems=" + userGems +
					'	 gemCost=' + gemCost + '	 userMonsterUuids=' + userMonsterUuids)
			resBuilder.status = CombineUserMonsterPiecesStatus.FAIL_INSUFFUCIENT_GEMS
			return false;
		}
		true
	}

	private def writeChangesToDb(User aUser, List<Long> userMonsterUuids, int gemCost,
		Map<String, Integer> money)
	{
		if (gemCost > 0)
		{
			val gemChange = -1 * gemCost
			if (!aUser.updateRelativeGemsNaive(gemChange))
			{
				LOG.error(
					'problem with updating user gems for speedup. gemChange=' + gemChange +
						'	 userMonsterUuids=' + userMonsterUuids)
				return false;
			}
			else
			{
				money.put(MiscMethods::gems, gemChange)
			}
		}
		val num = UpdateUtils::get.updateCompleteUserMonster(userMonsterUuids)
		if (num !== userMonsterUuids.size)
		{
			LOG.error(
				'problem with updating user monster is_complete. numUpdated=' + num +
					'	 userMonsterUuids=' + userMonsterUuids)
		}
		true
	}

	private def writeToUserCurrencyHistory(User aUser, Map<String, Integer> money,
		Timestamp curTime, int previousGems, List<Long> userMonsterUuids)
	{
		if ((null === money) || money.empty)
		{
			return;
		}
		val userUuid = aUser.id
		val gems = MiscMethods::gems
		val reasonForChange = ControllerConstants.UCHRFC__SPED_UP_COMBINING_MONSTER
		val previousCurrencies = new HashMap<String, Integer>()
		val currentCurrencies = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsList = new HashMap<String, String>()
		previousCurrencies.put(gems, previousGems)
		currentCurrencies.put(gems, aUser.gems)
		reasonsForChanges.put(gems, reasonForChange)
		detailsList.put(gems, 'userMonsterUuids=' + userMonsterUuids)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, money, previousCurrencies,
			currentCurrencies, reasonsForChanges, detailsList)
	}
}
