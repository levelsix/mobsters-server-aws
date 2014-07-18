package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser
import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteResponseProto.EnhancementWaitTimeCompleteStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.EnhancementWaitTimeCompleteRequestEvent
import com.lvl6.mobsters.events.response.EnhancementWaitTimeCompleteResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentExpProto
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
class EnhancementWaitTimeCompleteController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(EnhancementWaitTimeCompleteController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new EnhancementWaitTimeCompleteRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_ENHANCEMENT_WAIT_TIME_COMPLETE_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as EnhancementWaitTimeCompleteRequestEvent)).
			enhancementWaitTimeCompleteRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val isSpeedUp = reqProto.isSpeedup
		val gemsForSpeedUp = reqProto.gemsForSpeedup
		val umcep = reqProto.umcep
		var userMonsterUuidsThatFinished = reqProto.userMonsterUuidsList
		userMonsterUuidsThatFinished = new ArrayList<Long>(userMonsterUuidsThatFinished)
		val curTime = new Timestamp((new Date()).time)
		val resBuilder = EnhancementWaitTimeCompleteResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = EnhancementWaitTimeCompleteStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			var previousGems = 0
			val userMonsterUuids = new ArrayList<Long>()
			userMonsterUuids.add(umcep.userMonsterId)
			userMonsterUuids.addAll(userMonsterUuidsThatFinished)
			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			val inEnhancing = MonsterEnhancingForUserRetrieveUtils::getMonstersForUser(userUuid)
			val idsToUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
				getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids)
			val legit = checkLegit(resBuilder, aUser, userUuid, idsToUserMonsters, inEnhancing,
				umcep, userMonsterUuidsThatFinished, isSpeedUp, gemsForSpeedUp)
			val money = new HashMap<String, Integer>()
			var successful = false
			if (legit)
			{
				previousGems = aUser.gems
				successful = writeChangesToDb(aUser, userUuid, curTime, inEnhancing, umcep,
					userMonsterUuidsThatFinished, isSpeedUp, gemsForSpeedUp, money)
			}
			if (successful)
			{
				responseBuilder = resBuilder
			}
			val resEvent = new EnhancementWaitTimeCompleteResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.enhancementWaitTimeCompleteResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in EnhancementWaitTimeCompleteController.processRequestEvent',
					e)
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
					LOG.error(
						'fatal exception in EnhancementWaitTimeCompleteController.processRequestEvent',
						e)
				}
				writeChangesToHistory(userUuid, inEnhancing, userMonsterUuidsThatFinished)
				writeToUserCurrencyHistory(aUser, curTime, umcep.userMonsterId, money,
					previousGems)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in EnhancementWaitTimeCompleteController processEvent', e)
			try
			{
				resBuilder.status = EnhancementWaitTimeCompleteStatus.FAIL_OTHER
				val resEvent = new EnhancementWaitTimeCompleteResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.enhancementWaitTimeCompleteResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in EnhancementWaitTimeCompleteController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in EnhancementWaitTimeCompleteController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	/** 
 * Return true if user request is valid; false otherwise and set the builder
 * status to the appropriate value.
 * Will return fail if user does not have enough funds. For the most part,
 * will always return success. Why? Answer: For @healedUp, the monsters the
 * client thinks completed healing, only existing/valid ids will be taken
 * off the healing queue.
 * Ex. Queue is (a,b,c,d). a is the base monster, b,c,d are the feeders. If
 * user says monster (b, e) finished enhancing a, only the valid monsters
 * (b) will be removed from the queue, leaving (a,c,d)
 * @param resBuilder
 * @param u
 * @param userUuid
 * @param idsToUserMonsters- the monsters the user has
 * @param inEnhancing- the monsters that are in the enhancing queue
 * @param umcep- the base monster that is updated from using up some of the
 * feeders
 * @param usedUpUserMonsterUuids- userMonsterUuids the user thinks has finished being enhanced
 * @param speedUUp
 * @param gemsForSpeedUp
 * @return
 */
	private def checkLegit(Builder resBuilder, User u, String userUuid,
		Map<Long, MonsterForUser> idsToUserMonsters,
		Map<Long, MonsterEnhancingForUser> inEnhancing, UserMonsterCurrentExpProto umcep,
		List<Long> usedUpMonsterUuids, boolean speedup, int gemsForSpeedup)
	{
		if ((null === u) || (null === umcep) || usedUpMonsterUuids.empty)
		{
			LOG.error(
				'unexpected error: user or idList is null. user=' + u + '	 umcep=' + umcep +
					'usedUpMonsterUuids=' + usedUpMonsterUuids + '	 speedup=' + speedup +
					'	 gemsForSpeedup=' + gemsForSpeedup)
			return false;
		}
		LOG.info('inEnhancing=' + inEnhancing)
		val userMonsterIdBeingEnhanced = umcep.userMonsterId
		val inEnhancingUuids = inEnhancing.keySet
		MonsterStuffUtils::retainValidMonsterUuids(inEnhancingUuids, usedUpMonsterUuids)
		if (!inEnhancingUuids.contains(userMonsterIdBeingEnhanced))
		{
			LOG.error(
				'client did not send updated base monster specifying what new exp and lvl are')
			return false;
		}
		if (!idsToUserMonsters.containsKey(userMonsterIdBeingEnhanced))
		{
			LOG.error(
				"monster being enhanced doesn't exist!. userMonsterIdBeingEnhanced=" +
					userMonsterIdBeingEnhanced + '	 deleteUuids=' + usedUpMonsterUuids +
					'	 inEnhancing=' + inEnhancing + '	 gemsForSpeedup=' + gemsForSpeedup +
					'	 speedup=' + speedup)
			return false;
		}
		val existingUuids = idsToUserMonsters.keySet
		MonsterStuffUtils::retainValidMonsterUuids(existingUuids, usedUpMonsterUuids)
		if (speedup)
		{
			val userGems = u.gems
			if (userGems < gemsForSpeedup)
			{
				LOG.error(
					'user does not have enough gems to speed up enhancing.userGems=' + userGems +
						'	 cost=' + gemsForSpeedup + '	 umcep=' + umcep + '	 inEnhancing=' +
						inEnhancing + '	 deleteUuids=' + usedUpMonsterUuids)
				resBuilder.status = EnhancementWaitTimeCompleteStatus.FAIL_INSUFFICIENT_FUNDS
				return false;
			}
		}
		resBuilder.status = EnhancementWaitTimeCompleteStatus.SUCCESS
		true
	}

	private def writeChangesToDb(User u, int uId, Timestamp clientTime,
		Map<Long, MonsterEnhancingForUser> inEnhancing, UserMonsterCurrentExpProto umcep,
		List<Long> userMonsterUuids, boolean isSpeedup, int gemsForSpeedup,
		Map<String, Integer> money)
	{
		if (isSpeedup)
		{
			val gemCost = -1 * gemsForSpeedup
			if (!u.updateRelativeGemsNaive(gemCost))
			{
				LOG.error(
					'problem with updating user gems. gemsForSpeedup=' + gemsForSpeedup +
						', clientTime=' + clientTime + ', baseMonster' + umcep + ', clientTime=' +
						clientTime + ', userMonsterUuidsToDelete=' + userMonsterUuids +
						', user=' + u)
				return false;
			}
			else
			{
				if (0 !== gemCost)
				{
					money.put(MiscMethods::gems, gemCost)
				}
			}
		}
		val userMonsterIdBeingEnhanced = umcep.userMonsterId
		val newExp = umcep.expectedExperience
		val newLvl = umcep.expectedLevel
		val num = UpdateUtils::get.updateUserMonsterExpAndLvl(userMonsterIdBeingEnhanced, newExp,
			newLvl)
		LOG.info('num updated=' + num)
		true
	}

	private def setResponseBuilder(Builder resBuilder)
	{
	}

	private def writeChangesToHistory(int uId, Map<Long, MonsterEnhancingForUser> inEnhancing,
		List<Long> userMonsterUuids)
	{
		var num = DeleteUtils::get.deleteMonsterEnhancingForUser(uId, userMonsterUuids)
		LOG.info(
			'deleted monster healing rows. numDeleted=' + num + '	 userMonsterUuids=' +
				userMonsterUuids + '	 inEnhancing=' + inEnhancing)
		num = DeleteUtils::get.deleteMonstersForUser(userMonsterUuids)
		LOG.info(
			'defeated monster_for_user rows. numDeleted=' + num + '	 inEnhancing=' + inEnhancing)
	}

	private def writeToUserCurrencyHistory(User aUser, Timestamp curTime, long userMonsterId,
		Map<String, Integer> money, int previousGems)
	{
		if (money.empty)
		{
			return;
		}
		val gems = MiscMethods::gems
		val reasonForChange = ControllerConstants.UCHRFC__SPED_UP_ENHANCING
		val userUuid = aUser.id
		val previousCurrencies = new HashMap<String, Integer>()
		val currentCurrencies = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		previousCurrencies.put(gems, previousGems)
		currentCurrencies.put(gems, aUser.gems)
		reasonsForChanges.put(gems, reasonForChange)
		detailsMap.put(gems, ' userMonsterId=' + userMonsterId)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, money, previousCurrencies,
			currentCurrencies, reasonsForChanges, detailsMap)
	}
}
