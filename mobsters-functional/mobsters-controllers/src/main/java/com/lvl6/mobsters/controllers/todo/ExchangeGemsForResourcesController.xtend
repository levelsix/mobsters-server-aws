//package com.lvl6.mobsters.controllers.todo
//
//import com.amazonaws.services.ec2.model.ResourceType
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto.ExchangeGemsForResourcesStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.ExchangeGemsForResourcesRequestEvent
//import com.lvl6.mobsters.events.response.ExchangeGemsForResourcesResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.ControllerConstants
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
//class ExchangeGemsForResourcesController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(ExchangeGemsForResourcesController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 1
//	}
//
//	override createRequestEvent()
//	{
//		new ExchangeGemsForResourcesRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_EXCHANGE_GEMS_FOR_RESOURCES_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as ExchangeGemsForResourcesRequestEvent)).
//			exchangeGemsForResourcesRequestProto
//		val senderResourcesProto = reqProto.sender
//		val senderProto = senderResourcesProto.minUserProto
//		val numGems = reqProto.numGems
//		val numResources = reqProto.numResources
//		val resourceType = reqProto.resourceType
//		val curTime = new Timestamp(reqProto.clientTime)
//		val maxCash = senderResourcesProto.maxCash
//		val maxOil = senderResourcesProto.maxOil
//		val resBuilder = ExchangeGemsForResourcesResponseProto::newBuilder
//		resBuilder.sender = senderResourcesProto
//		resBuilder.status = ExchangeGemsForResourcesStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			val legit = checkLegit(resBuilder, user, numGems, resourceType, numGems)
//			var successful = false
//			val currencyChange = new HashMap<String, Integer>()
//			val previousCurrency = new HashMap<String, Integer>()
//			if (legit)
//			{
//				previousCurrency.put(MiscMethods::cash, user.cash)
//				previousCurrency.put(MiscMethods::oil, user.oil)
//				previousCurrency.put(MiscMethods::gems, user.gems)
//				successful = writeChangesToDb(user, numGems, resourceType, numResources, maxCash,
//					maxOil, currencyChange)
//			}
//			if (successful)
//			{
//				resBuilder.status = ExchangeGemsForResourcesStatus.SUCCESS
//			}
//			val resProto = resBuilder.build
//			val resEvent = new ExchangeGemsForResourcesResponseEvent(senderProto.userUuid)
//			resEvent.exchangeGemsForResourcesResponseProto = resProto
//			resEvent.tag = event.tag
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in ExchangeGemsForResourcesController.processRequestEvent',
//					e)
//			}
//			if (successful)
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
//					LOG.error(
//						'fatal exception in ExchangeGemsForResourcesController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(user, previousCurrency, currencyChange, curTime,
//					resourceType, numResources, numGems)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in ExchangeGemsForResourcesController processEvent', e)
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, User aUser, int numGems,
//		ResourceType resourceType, int numResources)
//	{
//		if ((null === aUser) || (null === resourceType) || (0 === numGems))
//		{
//			LOG.error(
//				'user or resourceType is null, or numGems is 0. user=' + aUser +
//					'	 resourceType=' + resourceType + '	 numGems=' + numGems)
//			return false;
//		}
//		val userGems = aUser.gems
//		if (userGems < numGems)
//		{
//			LOG.error(
//				'user does not have enough gems to exchange for resource.' + ' userGems=' +
//					userGems + '	 resourceType=' + resourceType + '	 numResources=' +
//					numResources)
//			resBuilder.status = ExchangeGemsForResourcesStatus.FAIL_INSUFFICIENT_GEMS
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDb(User user, int numGems, ResourceType resourceType,
//		int numResources, int maxCash, int maxOil, Map<String, Integer> currencyChange)
//	{
//		var success = true
//		LOG.info('exchanging ' + numGems + ' gems for ' + numResources + ' ' + resourceType.name)
//		var cashChange = 0
//		var oilChange = 0
//		val gemChange = -1 * numGems
//		if (ResourceType::CASH === resourceType)
//		{
//			cashChange = numResources
//			if (numResources > 0)
//			{
//				val curCash = Math::min(user.cash, maxCash)
//				val maxCashUserCanGain = maxCash - curCash
//				cashChange = Math::min(numResources, maxCashUserCanGain)
//			}
//		}
//		else if (ResourceType::OIL === resourceType)
//		{
//			oilChange = numResources
//			if (numResources > 0)
//			{
//				val curOil = Math::min(user.oil, maxOil)
//				val maxOilUserCanGain = maxOil - curOil
//				oilChange = Math::min(numResources, maxOilUserCanGain)
//			}
//		}
//		if ((0 === oilChange) && (0 === cashChange))
//		{
//			LOG.error(
//				'oil and cash (user exchanged) for gems are both 0. oilChange=' + oilChange +
//					'	 cashChange=' + cashChange + '	 gemChange=' + gemChange + '	 maxOil=' +
//					maxOil + '	 maxCash=' + maxCash)
//			return false;
//		}
//		LOG.info('user before: ' + user)
//		val numUpdated = user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange)
//		if ((2 !== numUpdated) && (1 !== numUpdated))
//		{
//			LOG.error("did not increase user's " + resourceType + ' by ' + numResources)
//			success = false
//		}
//		else
//		{
//			if (0 !== cashChange)
//			{
//				currencyChange.put(MiscMethods::cash, cashChange)
//			}
//			if (0 !== oilChange)
//			{
//				currencyChange.put(MiscMethods::oil, oilChange)
//			}
//			if (0 !== gemChange)
//			{
//				currencyChange.put(MiscMethods::gems, gemChange)
//			}
//		}
//		LOG.info('user after: ' + user)
//		success
//	}
//
//	private def writeToUserCurrencyHistory(User aUser, Map<String, Integer> previousCurrency,
//		Map<String, Integer> currencyChange, Timestamp curTime, ResourceType resourceType,
//		int numResources, int numGems)
//	{
//		if (currencyChange.empty)
//		{
//			return;
//		}
//		val cash = MiscMethods::cash
//		val oil = MiscMethods::oil
//		val gems = MiscMethods::gems
//		val reasonForChange = ControllerConstants.UCHRFC__CURRENCY_EXCHANGE
//		val detailsSb = new StringBuilder()
//		detailsSb.append(' exchanged ')
//		detailsSb.append(numGems)
//		detailsSb.append(' gems for ')
//		detailsSb.append(numResources)
//		detailsSb.append(' ')
//		detailsSb.append(resourceType.name)
//		val userUuid = aUser.id
//		val currentCurrencies = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val details = new HashMap<String, String>()
//		currentCurrencies.put(cash, aUser.cash)
//		currentCurrencies.put(oil, aUser.oil)
//		currentCurrencies.put(gems, aUser.gems)
//		reasonsForChanges.put(cash, reasonForChange)
//		reasonsForChanges.put(oil, reasonForChange)
//		reasonsForChanges.put(gems, reasonForChange)
//		details.put(cash, detailsSb.toString)
//		details.put(oil, detailsSb.toString)
//		details.put(gems, detailsSb.toString)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//			previousCurrency, currentCurrencies, reasonsForChanges, details)
//	}
//}
