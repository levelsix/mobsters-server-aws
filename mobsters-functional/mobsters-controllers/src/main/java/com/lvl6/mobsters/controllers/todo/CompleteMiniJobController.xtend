//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto
//import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto.CompleteMiniJobStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.CompleteMiniJobRequestEvent
//import com.lvl6.mobsters.events.response.CompleteMiniJobResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.Collections
//import java.util.HashMap
//import java.util.List
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Component
//
//@Component
//class CompleteMiniJobController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(CompleteMiniJobController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var MonsterForUserRetrieveUtils monsterForUserRetrieveUtils
//	@Autowired
//	protected var MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil
//
//	new()
//	{
//		numAllocatedThreads = 4
//	}
//
//	override createRequestEvent()
//	{
//		new CompleteMiniJobRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_COMPLETE_MINI_JOB_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as CompleteMiniJobRequestEvent)).completeMiniJobRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val clientTime = new Timestamp(reqProto.clientTime)
//		val userMiniJobId = reqProto.userMiniJobUuid
//		val isSpeedUp = reqProto.isSpeedUp
//		val gemCost = reqProto.gemCost
//		val umchpList = reqProto.umchpList
//		val resBuilder = CompleteMiniJobResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = CompleteMiniJobStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			var previousGems = 0
//			val userMonsterIdToExpectedHealth = new HashMap<Long, Integer>()
//			val legit = checkLegit(resBuilder, userUuid, user, userMiniJobId, isSpeedUp, gemCost,
//				umchpList, userMonsterIdToExpectedHealth)
//			var success = false
//			val currencyChange = new HashMap<String, Integer>()
//			if (legit)
//			{
//				previousGems = user.gems
//				success = writeChangesToDB(userUuid, user, userMiniJobId, isSpeedUp, gemCost,
//					clientTime, userMonsterIdToExpectedHealth, currencyChange)
//			}
//			if (success)
//			{
//				resBuilder.status = CompleteMiniJobStatus.SUCCESS
//			}
//			val resEvent = new CompleteMiniJobResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.completeMiniJobResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in CompleteMiniJobController.processRequestEvent', e)
//			}
//			if (success)
//			{
//				val resEventUpdate = MiscMethods::
//					createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null)
//				resEventUpdate.tag = event.tag
//				LOG.info('Writing event: ' + resEventUpdate)
//				try
//				{
//					eventWriter.writeEvent(resEventUpdate)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in CompleteMiniJobController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(user, userMiniJobId, currencyChange, clientTime,
//					previousGems)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in CompleteMiniJobController processEvent', e)
//			try
//			{
//				resBuilder.status = CompleteMiniJobStatus.FAIL_OTHER
//				val resEvent = new CompleteMiniJobResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.completeMiniJobResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in CompleteMiniJobController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in CompleteMiniJobController processEvent', e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, String userUuid, User user, long userMiniJobId,
//		boolean isSpeedUp, int gemCost, List<UserMonsterCurrentHealthProto> umchpList,
//		Map<Long, Integer> userMonsterIdToExpectedHealth)
//	{
//		if (umchpList.empty || (0 === userMiniJobId))
//		{
//			LOG.error(
//				'invalid userMonsterUuids (monsters need to be damaged)' +
//					' or userMiniJobId. userMonsters=' + umchpList + '	 userMiniJobId=' +
//					userMiniJobId)
//			return false;
//		}
//		val userMonsterUuids = MonsterStuffUtils::getUserMonsterUuids(umchpList,
//			userMonsterIdToExpectedHealth)
//		val mfuUuidsToUserMonsters = monsterForUserRetrieveUtils.
//			getSpecificOrAllUserMonstersForUser(userUuid, userMonsterUuids)
//		if (userMonsterUuids.size !== mfuUuidsToUserMonsters.size)
//		{
//			LOG.warn(
//				'some userMonsterUuids client sent are invalid.' +
//					' Keeping valid ones. userMonsterUuids=' + userMonsterUuids +
//					' mfuUuidsToUserMonsters=' + mfuUuidsToUserMonsters)
//			val existing = mfuUuidsToUserMonsters.keySet
//			MonsterStuffUtils::retainValidMonsterUuids(existing, userMonsterUuids)
//		}
//		if (userMonsterUuids.empty)
//		{
//			LOG.error('no valid user monster ids sent by client')
//			return false;
//		}
//		val userMiniJobUuids = Collections::singleton(userMiniJobId)
//		val idToUserMiniJob = miniJobForUserRetrieveUtil.
//			getSpecificOrAllIdToMiniJobForUser(userUuid, userMiniJobUuids)
//		if (idToUserMiniJob.empty)
//		{
//			LOG.error('no UserMiniJob exists with id=' + userMiniJobId)
//			resBuilder.status = CompleteMiniJobStatus.FAIL_NO_MINI_JOB_EXISTS
//			return false;
//		}
//		if (isSpeedUp && !hasEnoughGems(resBuilder, user, gemCost))
//		{
//			return false;
//		}
//		true
//	}
//
//	private def hasEnoughGems(Builder resBuilder, User u, int gemsSpent)
//	{
//		val userGems = u.gems
//		if (userGems < gemsSpent)
//		{
//			LOG.error(
//				'user error: user does not have enough gems. userGems=' + userGems +
//					'	 gemsSpent=' + gemsSpent)
//			resBuilder.status = CompleteMiniJobStatus.FAIL_INSUFFICIENT_GEMS
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDB(String userUuid, User user, long userMiniJobId,
//		boolean isSpeedUp, int gemCost, Timestamp clientTime,
//		Map<Long, Integer> userMonsterIdToExpectedHealth, Map<String, Integer> currencyChange)
//	{
//		LOG.info("updating user's monsters' healths")
//		var numUpdated = UpdateUtils::get.updateUserMonstersHealth(userMonsterIdToExpectedHealth)
//		LOG.info('numUpdated=' + numUpdated)
//		if (numUpdated > (2 * userMonsterIdToExpectedHealth.size))
//		{
//			LOG.warn(
//				'unexpected error: more than user monsters were' +
//					' updated. actual numUpdated=' + numUpdated +
//					'expected: userMonsterIdToExpectedHealth=' + userMonsterIdToExpectedHealth)
//		}
//		val gemsChange = -1 * Math::abs(gemCost)
//		val cashChange = 0
//		val oilChange = 0
//		if (isSpeedUp && !updateUser(user, gemsChange, cashChange, oilChange))
//		{
//			LOG.error(
//				'unexpected error: could not decrement user gems by ' + gemsChange +
//					', cash by ' + cashChange + ', and oil by ' + oilChange)
//			return false;
//		}
//		else
//		{
//			if (0 !== gemsChange)
//			{
//				currencyChange.put(MiscMethods::gems, gemsChange)
//			}
//		}
//		numUpdated = UpdateUtils::get.
//			updateMiniJobForUserCompleteTime(userUuid, userMiniJobId, clientTime)
//		LOG.info('writeChangesToDB() numUpdated=' + numUpdated)
//		true
//	}
//
//	private def updateUser(User u, int gemsChange, int cashChange, int oilChange)
//	{
//		val numChange = u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemsChange)
//		if (numChange <= 0)
//		{
//			LOG.error(
//				'unexpected error: problem with updating user gems,' +
//					' cash, and oil. gemChange=' + gemsChange + ', cash= ' + cashChange +
//					', oil=' + oilChange + ' user=' + u)
//			return false;
//		}
//		true
//	}
//
//	private def writeToUserCurrencyHistory(User aUser, long userMiniJobId,
//		Map<String, Integer> currencyChange, Timestamp curTime, int previousGems)
//	{
//		val userUuid = aUser.id
//		val reason = ControllerConstants.UCHRFC__SPED_UP_COMPLETE_MINI_JOB
//		val detailsSb = new StringBuilder()
//		detailsSb.append('userMiniJobId=')
//		detailsSb.append(userMiniJobId)
//		val previousCurrency = new HashMap<String, Integer>()
//		val currentCurrency = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val detailsMap = new HashMap<String, String>()
//		val gems = MiscMethods::gems
//		previousCurrency.put(gems, previousGems)
//		currentCurrency.put(gems, aUser.gems)
//		reasonsForChanges.put(gems, reason)
//		detailsMap.put(gems, detailsSb.toString)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
//	}
//
//	def getMonsterForUserRetrieveUtils()
//	{
//		monsterForUserRetrieveUtils
//	}
//
//	def setMonsterForUserRetrieveUtils(MonsterForUserRetrieveUtils monsterForUserRetrieveUtils)
//	{
//		this.monsterForUserRetrieveUtils = monsterForUserRetrieveUtils
//	}
//
//	def getMiniJobForUserRetrieveUtil()
//	{
//		miniJobForUserRetrieveUtil
//	}
//
//	def setMiniJobForUserRetrieveUtil(MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil)
//	{
//		this.miniJobForUserRetrieveUtil = miniJobForUserRetrieveUtil
//	}
//}
