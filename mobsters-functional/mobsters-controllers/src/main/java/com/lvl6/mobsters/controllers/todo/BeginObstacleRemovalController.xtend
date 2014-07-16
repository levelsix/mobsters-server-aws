package com.lvl6.mobsters.controllers.todo

import com.amazonaws.services.ec2.model.ResourceType
import com.lvl6.mobsters.dynamo.ObstacleForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto.BeginObstacleRemovalStatus
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto.Builder
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.BeginObstacleRemovalRequestEvent
import com.lvl6.mobsters.events.response.BeginObstacleRemovalResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.HashMap
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BeginObstacleRemovalController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(BeginObstacleRemovalController))
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
		return new BeginObstacleRemovalRequestEvent()
	}

	override getEventType()
	{
		return EventProtocolRequest.C_BEGIN_OBSTACLE_REMOVAL_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as BeginObstacleRemovalRequestEvent)).
			beginObstacleRemovalRequestProto
		LOG.info('reqProto=' + reqProto)
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val clientTime = new Timestamp(reqProto.curTime)
		val gemsSpent = reqProto.gemsSpent
		val resourceChange = reqProto.resourceChange
		val rt = reqProto.resourceType
		val userObstacleId = reqProto.userObstacleUuid
		val resBuilder = BeginObstacleRemovalResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = BeginObstacleRemovalStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
			val ofu = obstacleForUserRetrieveUtil.getUserObstacleForId(userObstacleId)
			val legitComplete = checkLegit(resBuilder, userUuid, user, userObstacleId, ofu,
				gemsSpent, resourceChange, rt)
			var success = false
			val currencyChange = new HashMap<String, Integer>()
			val previousCurrency = new HashMap<String, Integer>()
			if (legitComplete)
			{
				success = writeChangesToDB(user, userObstacleId, gemsSpent, resourceChange, rt,
					clientTime, currencyChange, previousCurrency)
			}
			val resEvent = new BeginObstacleRemovalResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.beginObstacleRemovalResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in BeginObstacleRemovalController.processRequestEvent', e)
			}
			if (success)
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
						'fatal exception in BeginObstacleRemovalController.processRequestEvent',
						e)
				}
				writeToUserCurrencyHistory(userUuid, user, clientTime, currencyChange,
					previousCurrency, ofu, rt)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in BeginObstacleRemovalController processEvent', e)
			try
			{
				resBuilder.status = BeginObstacleRemovalStatus.FAIL_OTHER
				val resEvent = new BeginObstacleRemovalResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.beginObstacleRemovalResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in BeginObstacleRemovalController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in BeginObstacleRemovalController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegit(Builder resBuilder, String userUuid, User user, int ofuId,
		ObstacleForUser ofu, int gemsSpent, int resourceChange, ResourceType rt)
	{
		if ((null === user) || (null === ofu))
		{
			resBuilder.status = BeginObstacleRemovalStatus.FAIL_OTHER
			LOG.error(
				'unexpected error: user or obstacle for user is null. user=' + user +
					'	 userUuid=' + userUuid + '	 obstacleForUser=' + ofu + '	 ofuId=' + ofuId)
			return false;
		}
		if (!hasEnoughGems(resBuilder, user, gemsSpent))
		{
			return false;
		}
		if (ResourceType.CASH == rt)
		{
			if (!hasEnoughCash(resBuilder, user, resourceChange))
			{
				return false;
			}
		}
		if (ResourceType.OIL == rt)
		{
			if (!hasEnoughOil(resBuilder, user, resourceChange))
			{
				return false;
			}
		}
		resBuilder.status = BeginObstacleRemovalStatus.SUCCESS
		return true
	}

	private def hasEnoughGems(Builder resBuilder, User u, int gemsSpent)
	{
		val userGems = u.gems
		if (userGems < gemsSpent)
		{
			LOG.error(
				'user error: user does not have enough gems. userGems=' + userGems +
					'	 gemsSpent=' + gemsSpent)
			resBuilder.status = BeginObstacleRemovalStatus.FAIL_INSUFFICIENT_GEMS
			return false;
		}
		return true
	}

	private def hasEnoughCash(Builder resBuilder, User u, int cashSpent)
	{
		val userCash = u.cash
		if (userCash < cashSpent)
		{
			LOG.error(
				'user error: user does not have enough cash. userCash=' + userCash +
					'	 cashSpent=' + cashSpent)
			resBuilder.status = BeginObstacleRemovalStatus.FAIL_INSUFFICIENT_RESOURCE
			return false;
		}
		return true
	}

	private def hasEnoughOil(Builder resBuilder, User u, int oilSpent)
	{
		val userOil = u.oil
		if (userOil < oilSpent)
		{
			LOG.error(
				'user error: user does not have enough oil. userOil=' + userOil + '	 oilSpent=' +
					oilSpent)
			resBuilder.status = BeginObstacleRemovalStatus.FAIL_INSUFFICIENT_RESOURCE
			return false;
		}
		return true
	}

	private def writeChangesToDB(User user, int ofuId, int gemsSpent, int resourceChange,
		ResourceType rt, Timestamp clientTime, Map<String, Integer> currencyChange,
		Map<String, Integer> previousCurrency)
	{
		val gemsChange = -1 * Math::abs(gemsSpent)
		var cashChange = 0
		var oilChange = 0
		if (0 !== gemsChange)
		{
			previousCurrency.put(MiscMethods::gems, user.gems)
		}
		if (ResourceType.CASH == rt)
		{
			LOG.info('user spent cash.')
			cashChange = resourceChange
			previousCurrency.put(MiscMethods::cash, user.cash)
		}
		if (ResourceType.OIL == rt)
		{
			LOG.info('user spent cash.')
			oilChange = resourceChange
			previousCurrency.put(MiscMethods::oil, user.oil)
		}
		if (!updateUser(user, gemsChange, cashChange, oilChange))
		{
			LOG.error(
				"unexpected error: could not decrement user's gems by " + gemsChange +
					', cash by ' + cashChange + ' or oil by ' + oilChange)
			return false;
		}
		else
		{
			if (0 !== gemsChange)
			{
				currencyChange.put(MiscMethods::gems, gemsChange)
			}
			if (0 !== cashChange)
			{
				currencyChange.put(MiscMethods::cash, cashChange)
			}
			if (0 !== oilChange)
			{
				currencyChange.put(MiscMethods::oil, oilChange)
			}
		}
		val numUpdated = UpdateUtils::get.updateObstacleForUserRemovalTime(ofuId, clientTime)
		LOG.info('(obstacles, should be 1) numUpdated=' + numUpdated)
		return true
	}

	private def updateUser(User u, int gemsChange, int cashChange, int oilChange)
	{
		val numChange = u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemsChange)
		if (numChange <= 0)
		{
			LOG.error(
				'unexpected error: problem with updating user gems, cash, and oil. gemChange=' +
					gemsChange + ', cash= ' + cashChange + ', oil=' + oilChange + ' user=' + u)
			return false;
		}
		return true
	}

	private def writeToUserCurrencyHistory(String userUuid, User user, Timestamp curTime,
		Map<String, Integer> currencyChange, Map<String, Integer> previousCurrency,
		ObstacleForUser ofu, ResourceType rt)
	{
		if (currencyChange.empty)
		{
			return;
		}
		val reason = ControllerConstants.UCHRFC__REMOVE_OBSTACLE
		val detailsSb = new StringBuilder()
		detailsSb.append('obstacleId=')
		detailsSb.append(ofu.obstacleId)
		detailsSb.append(' x=')
		detailsSb.append(ofu.xcoord)
		detailsSb.append(' y=')
		detailsSb.append(ofu.ycoord)
		detailsSb.append(' resourceType=')
		detailsSb.append(rt.name)
		val details = detailsSb.toString
		val currentCurrency = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		val gems = MiscMethods::gems
		val cash = MiscMethods::cash
		val oil = MiscMethods::oil
		if (currencyChange.containsKey(gems))
		{
			currentCurrency.put(gems, user.gems)
			reasonsForChanges.put(gems, reason)
			detailsMap.put(gems, details)
		}
		if (currencyChange.containsKey(cash))
		{
			currentCurrency.put(cash, user.cash)
			reasonsForChanges.put(cash, reason)
			detailsMap.put(cash, details)
		}
		if (currencyChange.containsKey(oil))
		{
			currentCurrency.put(oil, user.oil)
			reasonsForChanges.put(oil, reason)
			detailsMap.put(oil, details)
		}
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
	}

	def ObstacleForUserRetrieveUtil getObstacleForUserRetrieveUtil()
	{
		return obstacleForUserRetrieveUtil
	}

	def ObstacleForUserRetrieveUtil setObstacleForUserRetrieveUtil(ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil)
	{
		return this.obstacleForUserRetrieveUtil = obstacleForUserRetrieveUtil
	}
}
