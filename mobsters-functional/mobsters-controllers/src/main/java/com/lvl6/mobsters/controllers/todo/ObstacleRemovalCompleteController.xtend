package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.ObstacleForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto.ObstacleRemovalCompleteStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.ObstacleRemovalCompleteRequestEvent
import com.lvl6.mobsters.events.response.ObstacleRemovalCompleteResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.HashMap
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class ObstacleRemovalCompleteController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(ObstacleRemovalCompleteController))
	@Autowired
	protected var DataServiceTxManager svcTxManager
	@Autowired
	protected var ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new ObstacleRemovalCompleteRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_OBSTACLE_REMOVAL_COMPLETE_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as ObstacleRemovalCompleteRequestEvent)).
			obstacleRemovalCompleteRequestProto
		LOG.info('reqProto=' + reqProto)
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val clientTime = new Timestamp(reqProto.curTime)
		val speedUp = reqProto.speedUp
		val gemCostToSpeedUp = reqProto.gemsSpent
		val userObstacleId = reqProto.userObstacleUuid
		val atMaxObstacles = reqProto.atMaxObstacles
		val resBuilder = ObstacleRemovalCompleteResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = ObstacleRemovalCompleteStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
			val ofu = obstacleForUserRetrieveUtil.getUserObstacleForId(userObstacleId)
			val legitExpansionComplete = checkLegit(resBuilder, userUuid, user, userObstacleId,
				ofu, speedUp, gemCostToSpeedUp)
			var previousGems = 0
			var success = false
			val money = new HashMap<String, Integer>()
			if (legitExpansionComplete)
			{
				previousGems = user.gems
				success = writeChangesToDB(user, userObstacleId, speedUp, gemCostToSpeedUp,
					clientTime, atMaxObstacles, money)
			}
			val resEvent = new ObstacleRemovalCompleteResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.obstacleRemovalCompleteResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in ObstacleRemovalCompleteController.processRequestEvent',
					e)
			}
			if (success && (speedUp || atMaxObstacles))
			{
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
					LOG.error(
						'fatal exception in ObstacleRemovalCompleteController.processRequestEvent',
						e)
				}
				writeToUserCurrencyHistory(userUuid, user, clientTime, money, previousGems, ofu)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in ObstacleRemovalCompleteController processEvent', e)
			try
			{
				resBuilder.status = ObstacleRemovalCompleteStatus.FAIL_OTHER
				val resEvent = new ObstacleRemovalCompleteResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.obstacleRemovalCompleteResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in ObstacleRemovalCompleteController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in ObstacleRemovalCompleteController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegit(Builder resBuilder, String userUuid, User user, int ofuId,
		ObstacleForUser ofu, boolean speedUp, int gemCostToSpeedup)
	{
		if ((null === user) || (null === ofu))
		{
			resBuilder.status = ObstacleRemovalCompleteStatus.FAIL_OTHER
			LOG.error(
				'unexpected error: user or obstacle for user is null. user=' + user +
					'	 userUuid=' + userUuid + '	 obstacleForUser=' + ofu + '	 ofuId=' + ofuId)
			return false;
		}
		if (speedUp && (user.gems < gemCostToSpeedup))
		{
			resBuilder.status = ObstacleRemovalCompleteStatus.FAIL_INSUFFICIENT_GEMS
			LOG.error(
				'user error: user does not have enough gems to speed up removal.' +
					'	 obstacleForUser=' + ofu + '	 cost=' + gemCostToSpeedup)
			return false;
		}
		resBuilder.status = ObstacleRemovalCompleteStatus.SUCCESS
		true
	}

	private def writeChangesToDB(User user, int ofuId, boolean speedUp, int gemCost,
		Timestamp clientTime, boolean atMaxObstacles, Map<String, Integer> money)
	{
		var gemChange = -1 * gemCost
		val obstaclesRemovedDelta = 1
		if (speedUp && atMaxObstacles)
		{
			LOG.info('isSpeedup and maxObstacles')
		}
		else if (speedUp)
		{
			LOG.info('isSpeedup')
			clientTime = null
		}
		else if (atMaxObstacles)
		{
			LOG.info('maxObstacles')
			gemChange = 0
		}
		else
		{
			gemChange = 0
			clientTime = null
			LOG.info('not isSpeedup and not maxObstacles')
		}
		if (!user.
			updateRelativeGemsObstacleTimeNumRemoved(gemChange, clientTime,
				obstaclesRemovedDelta))
		{
			LOG.error('problem updating user gems. gemChange=' + gemChange)
			return false;
		}
		else
		{
			if (0 !== gemChange)
			{
				money.put(MiscMethods::gems, gemChange)
			}
		}
		val numDeleted = DeleteUtils::get.deleteObstacleForUser(ofuId)
		LOG.info('(obstacles) numDeleted=' + numDeleted)
		true
	}

	private def writeToUserCurrencyHistory(String userUuid, User user, Timestamp curTime,
		Map<String, Integer> currencyChange, int previousGems, ObstacleForUser ofu)
	{
		val reason = ControllerConstants.UCHRFC__SPED_UP_REMOVE_OBSTACLE
		val detailsSb = new StringBuilder()
		detailsSb.append('obstacleId=')
		detailsSb.append(ofu.obstacleId)
		detailsSb.append(' x=')
		detailsSb.append(ofu.xcoord)
		detailsSb.append(' y=')
		detailsSb.append(ofu.ycoord)
		val details = detailsSb.toString
		val previousCurrency = new HashMap<String, Integer>()
		val currentCurrency = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		val gems = MiscMethods::gems
		previousCurrency.put(gems, previousGems)
		currentCurrency.put(gems, user.gems)
		reasonsForChanges.put(gems, reason)
		detailsMap.put(gems, details)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
	}

	def getObstacleForUserRetrieveUtil()
	{
		obstacleForUserRetrieveUtil
	}

	def setObstacleForUserRetrieveUtil(ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil)
	{
		this.obstacleForUserRetrieveUtil = obstacleForUserRetrieveUtil
	}
}
