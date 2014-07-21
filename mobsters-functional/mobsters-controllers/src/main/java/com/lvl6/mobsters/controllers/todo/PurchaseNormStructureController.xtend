//package com.lvl6.mobsters.controllers.todo
//
//import com.amazonaws.services.ec2.model.ResourceType
//import com.lvl6.mobsters.dynamo.CoordinatePair
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto
//import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto.PurchaseNormStructureStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.PurchaseNormStructureRequestEvent
//import com.lvl6.mobsters.events.response.PurchaseNormStructureResponseEvent
//import com.lvl6.mobsters.info.Structure
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.HashMap
//import java.util.List
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class PurchaseNormStructureController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(PurchaseNormStructureController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var InsertUtil insertUtils
//
//	def setInsertUtils(InsertUtil insertUtils)
//	{
//		this.insertUtils = insertUtils
//	}
//
//	new()
//	{
//		numAllocatedThreads = 3
//	}
//
//	override createRequestEvent()
//	{
//		new PurchaseNormStructureRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_PURCHASE_NORM_STRUCTURE_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as PurchaseNormStructureRequestEvent)).
//			purchaseNormStructureRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val structId = reqProto.structUuid
//		val cp = new CoordinatePair(reqProto.structCoordinates.x, reqProto.structCoordinates.y)
//		val timeOfPurchase = new Timestamp(reqProto.timeOfPurchase)
//		val gemsSpent = reqProto.gemsSpent
//		val resourceChange = reqProto.resourceChange
//		val resourceType = reqProto.resourceType
//		val resBuilder = PurchaseNormStructureResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = PurchaseNormStructureStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			LOG.info('user=' + user)
//			val struct = StructureRetrieveUtils::getStructForStructId(structId)
//			var previousGems = 0
//			var previousOil = 0
//			var previousCash = 0
//			var uStructId = 0
//			val legitPurchaseNorm = checkLegitPurchaseNorm(resBuilder, struct, user,
//				timeOfPurchase, gemsSpent, resourceChange, resourceType)
//			var success = false
//			val uStructIdList = new ArrayList<Integer>()
//			val money = new HashMap<String, Integer>()
//			if (legitPurchaseNorm)
//			{
//				previousGems = user.gems
//				previousOil = user.oil
//				previousCash = user.cash
//				success = writeChangesToDB(user, structId, cp, timeOfPurchase, gemsSpent,
//					resourceChange, resourceType, uStructIdList, money)
//			}
//			if (success)
//			{
//				resBuilder.status = PurchaseNormStructureStatus.SUCCESS
//				uStructId = uStructIdList.get(0)
//				resBuilder.userStructId = uStructId
//			}
//			val resEvent = new PurchaseNormStructureResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.purchaseNormStructureResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in PurchaseNormStructureController.processRequestEvent', e)
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
//					LOG.error(
//						'fatal exception in PurchaseNormStructureController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(user, structId, uStructId, timeOfPurchase, money,
//					previousGems, previousOil, previousCash)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in PurchaseNormStructure processEvent', e)
//			try
//			{
//				resBuilder.status = PurchaseNormStructureStatus.FAIL_OTHER
//				val resEvent = new PurchaseNormStructureResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.purchaseNormStructureResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in PurchaseNormStructureController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in PurchaseNormStructure processEvent', e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegitPurchaseNorm(Builder resBuilder, Structure prospective, User user,
//		Timestamp timeOfPurchase, int gemsSpent, int resourceChange, ResourceType resourceType)
//	{
//		if ((user === null) || (prospective === null) || (timeOfPurchase === null))
//		{
//			LOG.error(
//				'parameter passed in is null. user=' + user + ', struct=' + prospective +
//					', timeOfPurchase=' + timeOfPurchase)
//			return false;
//		}
//		val structResourceType = ResourceType::valueOf(prospective.buildResourceType)
//		if (resourceType !== structResourceType)
//		{
//			LOG.error(
//				'client is specifying unexpected resource type. actual=' + resourceType +
//					'	 expected=' + structResourceType + '	 structure=' + prospective)
//			return false;
//		}
//		val userGems = user.gems
//		if (gemsSpent > 0)
//		{
//			if (userGems < gemsSpent)
//			{
//				LOG.error(
//					'user has ' + userGems + ' gems; trying to spend ' + gemsSpent + ' and ' +
//						resourceChange + ' ' + resourceType + ' to buy structure=' + prospective)
//				resBuilder.status = PurchaseNormStructureStatus.FAIL_INSUFFICIENT_GEMS
//				return false;
//			}
//			else
//			{
//				return true;
//			}
//		}
//		val requiredResourceAmount = -1 * resourceChange
//		if (resourceType === ResourceType::CASH)
//		{
//			val userResource = user.cash
//			if (userResource < requiredResourceAmount)
//			{
//				LOG.error(
//					'not enough cash to buy structure. cash=' + userResource + '	 cost=' +
//						requiredResourceAmount + '	 structure=' + prospective)
//				resBuilder.status = PurchaseNormStructureStatus.FAIL_INSUFFICIENT_CASH
//				return false;
//			}
//		}
//		else if (resourceType === ResourceType::OIL)
//		{
//			val userResource = user.oil
//			if (userResource < requiredResourceAmount)
//			{
//				LOG.error(
//					'not enough oil to buy structure. oil=' + userResource + '	 cost=' +
//						requiredResourceAmount + '	 structure=' + prospective)
//				resBuilder.status = PurchaseNormStructureStatus.FAIL_INSUFFICIENT_OIL
//				return false;
//			}
//		}
//		else
//		{
//			LOG.error('unknown resource type: ' + resourceType + '	 structure=' + prospective)
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDB(User user, int structId, CoordinatePair cp,
//		Timestamp purchaseTime, int gemsSpent, int resourceChange, ResourceType resourceType,
//		List<Integer> uStructId, Map<String, Integer> money)
//	{
//		val userUuid = user.id
//		val Timestamp lastRetrievedTime = null
//		val isComplete = false
//		val userStructId = insertUtils.insertUserStruct(userUuid, structId, cp, purchaseTime,
//			lastRetrievedTime, isComplete)
//		if (userStructId <= 0)
//		{
//			LOG.error(
//				'problem with giving struct ' + structId + ' at ' + purchaseTime + ' on ' + cp)
//			return false;
//		}
//		val gemChange = -1 * gemsSpent
//		var cashChange = 0
//		var oilChange = 0
//		if (resourceType === ResourceType::CASH)
//		{
//			cashChange = resourceChange
//		}
//		else if (resourceType === ResourceType::OIL)
//		{
//			oilChange = resourceChange
//		}
//		if ((0 === gemChange) && (0 === cashChange) && (0 === oilChange))
//		{
//			LOG.error(
//				'gemChange=' + gemChange + ' cashChange=' + cashChange + ' oilChange=' +
//					oilChange + '	 Not purchasing norm struct.')
//			return false;
//		}
//		val num = user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange)
//		if (1 !== num)
//		{
//			LOG.error(
//				'problem with updating user currency. gemChange=' + gemChange + ' cashChange=' +
//					cashChange + '	 numRowsUpdated=' + num)
//			return false;
//		}
//		else
//		{
//			if (0 !== gemChange)
//			{
//				money.put(MiscMethods::gems, gemChange)
//			}
//			if (0 !== cashChange)
//			{
//				money.put(MiscMethods::cash, cashChange)
//			}
//			if (0 !== oilChange)
//			{
//				money.put(MiscMethods::oil, oilChange)
//			}
//		}
//		uStructId.add(userStructId)
//		true
//	}
//
//	private def writeToUserCurrencyHistory(User u, int structId, int uStructId, Timestamp date,
//		Map<String, Integer> money, int previousGems, int previousOil, int previousCash)
//	{
//		val userUuid = u.id
//		val previousCurencyMap = new HashMap<String, Integer>()
//		val currentCurrencyMap = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val details = new HashMap<String, String>()
//		val gems = MiscMethods::gems
//		val cash = MiscMethods::cash
//		val oil = MiscMethods::oil
//		val reasonForChange = ControllerConstants.UCHRFC__PURCHASE_NORM_STRUCT
//		val detailSb = new StringBuilder()
//		detailSb.append('structId=')
//		detailSb.append(structId)
//		detailSb.append(' uStructId=')
//		detailSb.append(uStructId)
//		val detail = detailSb.toString
//		previousCurencyMap.put(gems, previousGems)
//		previousCurencyMap.put(cash, previousCash)
//		previousCurencyMap.put(oil, previousOil)
//		currentCurrencyMap.put(gems, u.gems)
//		currentCurrencyMap.put(cash, u.cash)
//		currentCurrencyMap.put(oil, u.oil)
//		reasonsForChanges.put(gems, reasonForChange)
//		reasonsForChanges.put(cash, reasonForChange)
//		reasonsForChanges.put(oil, reasonForChange)
//		details.put(gems, detail)
//		details.put(cash, detail)
//		details.put(oil, detail)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, date, money, previousCurencyMap,
//			currentCurrencyMap, reasonsForChanges, details)
//	}
//}
