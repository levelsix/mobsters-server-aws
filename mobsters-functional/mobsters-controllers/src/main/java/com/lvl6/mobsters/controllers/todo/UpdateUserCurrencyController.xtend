//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto.UpdateUserCurrencyStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.UpdateUserCurrencyRequestEvent
//import com.lvl6.mobsters.events.response.UpdateUserCurrencyResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.HashMap
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class UpdateUserCurrencyController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(UpdateUserCurrencyController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 4
//	}
//
//	override createRequestEvent()
//	{
//		new UpdateUserCurrencyRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_UPDATE_USER_CURRENCY_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as UpdateUserCurrencyRequestEvent)).
//			updateUserCurrencyRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val cashSpent = reqProto.cashSpent
//		val oilSpent = reqProto.oilSpent
//		val gemsSpent = reqProto.gemsSpent
//		val reason = reqProto.reason
//		val details = reqProto.details
//		val clientTime = new Timestamp(reqProto.clientTime)
//		val resBuilder = UpdateUserCurrencyResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = UpdateUserCurrencyStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
//			var previousGems = 0
//			var previousCash = 0
//			var previousOil = 0
//			val legit = checkLegit(resBuilder, aUser, userUuid, cashSpent, oilSpent, gemsSpent)
//			var successful = false
//			val currencyChange = new HashMap<String, Integer>()
//			if (legit)
//			{
//				previousGems = aUser.gems
//				previousCash = aUser.cash
//				previousOil = aUser.oil
//				successful = writeChangesToDb(aUser, userUuid, cashSpent, oilSpent, gemsSpent,
//					clientTime, currencyChange)
//			}
//			if (successful)
//			{
//				resBuilder.status = UpdateUserCurrencyStatus.SUCCESS
//			}
//			val resEvent = new UpdateUserCurrencyResponseEvent(userUuid)
//			resEvent.tag = event.tag
//			resEvent.updateUserCurrencyResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in UpdateUserCurrencyController.processRequestEvent',
//					e)
//			}
//			if (successful)
//			{
//				val resEventUpdate = MiscMethods::
//					createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser, null)
//				resEventUpdate.tag = event.tag
//				LOG.info('Writing event: ' + resEventUpdate)
//				try
//				{
//					eventWriter.writeEvent(resEventUpdate)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in UpdateUserCurrencyController.processRequestEvent', e)
//				}
//				writeToUserCurrencyHistory(aUser, currencyChange, clientTime, previousGems,
//					previousCash, previousOil, reason, details)
//			}
//			if ((1234 === cashSpent) && (1234 === oilSpent) && (1234 === gemsSpent))
//			{
//				LOG.info('resetting user ' + aUser)
//				aUser.updateResetAccount
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in UpdateUserCurrencyController processEvent', e)
//			try
//			{
//				resBuilder.status = UpdateUserCurrencyStatus.FAIL_OTHER
//				val resEvent = new UpdateUserCurrencyResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.updateUserCurrencyResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in UpdateUserCurrencyController.processRequestEvent', e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in UpdateUserCurrencyController processEvent', e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, User u, String userUuid, int cashSpent,
//		int oilSpent, int gemsSpent)
//	{
//		if (null === u)
//		{
//			LOG.error('unexpected error: user is null. user=' + u)
//			return false;
//		}
//		if ((cashSpent !== Math::abs(cashSpent)) || (oilSpent !== Math::abs(oilSpent)) ||
//			(gemsSpent !== Math::abs(gemsSpent)))
//		{
//			LOG.error(
//				'client sent a negative value! all should be positive :(  cashSpent=' +
//					cashSpent + '	 oilSpent=' + oilSpent + '	 gemsSpent=' + gemsSpent)
//			if (u.admin)
//			{
//				LOG.info("it's alright. User is admin.")
//			}
//			else
//			{
//				return false;
//			}
//		}
//		if (!hasEnoughCash(resBuilder, u, cashSpent))
//		{
//			if (u.admin)
//			{
//				LOG.info("it's alright. User is admin.")
//			}
//			else
//			{
//				return false;
//			}
//		}
//		if (!hasEnoughOil(resBuilder, u, oilSpent))
//		{
//			if (u.admin)
//			{
//				LOG.info("it's alright. User is admin.")
//			}
//			else
//			{
//				return false;
//			}
//		}
//		if (!hasEnoughGems(resBuilder, u, gemsSpent))
//		{
//			if (u.admin)
//			{
//				LOG.info("it's alright. User is admin.")
//			}
//			else
//			{
//				return false;
//			}
//		}
//		true
//	}
//
//	private def hasEnoughCash(Builder resBuilder, User u, int cashSpent)
//	{
//		val userCash = u.cash
//		if (userCash < cashSpent)
//		{
//			LOG.error(
//				'user error: user does not have enough cash. userCash=' + userCash +
//					'	 cashSpent=' + cashSpent)
//			resBuilder.status = UpdateUserCurrencyStatus.FAIL_INSUFFICIENT_CASH
//			return false;
//		}
//		true
//	}
//
//	private def hasEnoughOil(Builder resBuilder, User u, int oilSpent)
//	{
//		val userOil = u.oil
//		if (userOil < oilSpent)
//		{
//			LOG.error(
//				'user error: user does not have enough oil. userOil=' + userOil + '	 oilSpent=' +
//					oilSpent)
//			resBuilder.status = UpdateUserCurrencyStatus.FAIL_INSUFFICIENT_OIL
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
//			resBuilder.status = UpdateUserCurrencyStatus.FAIL_INSUFFICIENT_GEMS
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDb(User u, int uId, int cashSpent, int oilSpent, int gemsSpent,
//		Timestamp clientTime, Map<String, Integer> currencyChange)
//	{
//		var gemsChange = -1 * Math::abs(gemsSpent)
//		var cashChange = -1 * Math::abs(cashSpent)
//		var oilChange = -1 * Math::abs(oilSpent)
//		if (u.admin)
//		{
//			gemsChange = gemsSpent
//			cashChange = cashSpent
//			oilChange = oilSpent
//		}
//		if (!updateUser(u, gemsChange, cashChange, oilChange))
//		{
//			LOG.error(
//				"unexpected error: could not decrement user's gems by " + gemsChange +
//					', cash by ' + cashChange + ', and oil by ' + oilChange)
//			return false;
//		}
//		else
//		{
//			if (0 !== gemsChange)
//			{
//				currencyChange.put(MiscMethods::gems, gemsChange)
//			}
//			if (0 !== cashChange)
//			{
//				currencyChange.put(MiscMethods::cash, cashChange)
//			}
//			if (0 !== oilChange)
//			{
//				currencyChange.put(MiscMethods::oil, oilChange)
//			}
//		}
//		true
//	}
//
//	private def updateUser(User u, int gemsChange, int cashChange, int oilChange)
//	{
//		val numChange = u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemsChange)
//		if (numChange <= 0)
//		{
//			LOG.error(
//				'unexpected error: problem with updating user gems, cash, and oil. gemChange=' +
//					gemsChange + ', cash= ' + cashChange + ', oil=' + oilChange + ' user=' + u)
//			return false;
//		}
//		true
//	}
//
//	private def writeToUserCurrencyHistory(User aUser, Map<String, Integer> currencyChange,
//		Timestamp curTime, int previousGems, int previousCash, int previousOil, String reason,
//		String details)
//	{
//		val userUuid = aUser.id
//		val previousCurrency = new HashMap<String, Integer>()
//		val currentCurrency = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val detailsMap = new HashMap<String, String>()
//		val gems = MiscMethods::gems
//		val cash = MiscMethods::cash
//		val oil = MiscMethods::oil
//		previousCurrency.put(gems, previousGems)
//		previousCurrency.put(cash, previousCash)
//		previousCurrency.put(oil, previousOil)
//		currentCurrency.put(gems, aUser.gems)
//		currentCurrency.put(cash, aUser.cash)
//		currentCurrency.put(oil, aUser.oil)
//		reasonsForChanges.put(gems, reason)
//		reasonsForChanges.put(cash, reason)
//		reasonsForChanges.put(oil, reason)
//		detailsMap.put(gems, details)
//		detailsMap.put(cash, details)
//		detailsMap.put(oil, details)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
//	}
//}
