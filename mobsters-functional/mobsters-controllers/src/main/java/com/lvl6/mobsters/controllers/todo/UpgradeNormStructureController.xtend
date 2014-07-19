//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.StructureForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto
//import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto.UpgradeNormStructureStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.UpgradeNormStructureRequestEvent
//import com.lvl6.mobsters.events.response.UpgradeNormStructureResponseEvent
//import com.lvl6.mobsters.info.Structure
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.ResourceType
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
//class UpgradeNormStructureController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(UpgradeNormStructureController))
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
//		new UpgradeNormStructureRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_UPGRADE_NORM_STRUCTURE_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as UpgradeNormStructureRequestEvent)).
//			upgradeNormStructureRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val userStructId = reqProto.userStructUuid
//		val timeOfUpgrade = new Timestamp(reqProto.timeOfUpgrade)
//		val gemsSpent = reqProto.gemsSpent
//		val resourceChange = reqProto.resourceChange
//		val rt = reqProto.resourceType
//		val resBuilder = UpgradeNormStructureResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = UpgradeNormStructureStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
//			var Structure currentStruct = null
//			var Structure nextLevelStruct = null
//			val userStruct = RetrieveUtils::userStructRetrieveUtils.
//				getSpecificUserStruct(userStructId)
//			if (userStruct !== null)
//			{
//				currentStruct = StructureRetrieveUtils::getStructForStructId(userStruct.structId)
//				nextLevelStruct = StructureRetrieveUtils::
//					getUpgradedStructForStructId(userStruct.structId)
//			}
//			var previousCash = 0
//			var previousOil = 0
//			var previousGems = 0
//			val legitUpgrade = checkLegitUpgrade(resBuilder, user, userStruct, currentStruct,
//				nextLevelStruct, gemsSpent, resourceChange, rt, timeOfUpgrade)
//			val resEvent = new UpgradeNormStructureResponseEvent(userUuid)
//			resEvent.tag = event.tag
//			resEvent.upgradeNormStructureResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in UpgradeNormStructureController.processRequestEvent', e)
//			}
//			if (legitUpgrade)
//			{
//				previousCash = user.cash
//				previousOil = user.oil
//				previousGems = user.gems
//				val money = new HashMap<String, Integer>()
//				writeChangesToDB(user, userStruct, nextLevelStruct, gemsSpent, resourceChange,
//					rt, timeOfUpgrade, money)
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
//						'fatal exception in UpgradeNormStructureController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(user, userStruct, currentStruct, nextLevelStruct,
//					timeOfUpgrade, money, previousCash, previousOil, previousGems)
//			}
//		}
//		catch (Throwable e)
//		{
//			LOG.error('exception in UpgradeNormStructure processEvent', e)
//			try
//			{
//				resBuilder.status = UpgradeNormStructureStatus.FAIL_OTHER
//				val resEvent = new UpgradeNormStructureResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.upgradeNormStructureResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in UpgradeNormStructureController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Throwable e2)
//			{
//				LOG.error('exception2 in UpgradeNormStructure processEvent', e2)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegitUpgrade(Builder resBuilder, User user, StructureForUser userStruct,
//		Structure currentStruct, Structure nextLevelStruct, int gemsSpent, int resourceChange,
//		ResourceType rt, Timestamp timeOfUpgrade)
//	{
//		if ((user === null) || (userStruct === null) || (userStruct.lastRetrieved === null))
//		{
//			LOG.error(
//				'parameter passed in is null. user=' + user + ', user struct=' + userStruct +
//					", userStruct's last retrieve time=" + userStruct.lastRetrieved)
//			return false;
//		}
//		if (!userStruct.complete)
//		{
//			resBuilder.status = UpgradeNormStructureStatus.FAIL_NOT_BUILT_YET
//			LOG.error('user struct is not complete yet')
//			return false;
//		}
//		if (null === nextLevelStruct)
//		{
//			resBuilder.status = UpgradeNormStructureStatus.FAIL_AT_MAX_LEVEL_ALREADY
//			LOG.error('user struct at max level already. struct is ' + currentStruct)
//			return false;
//		}
//		if (timeOfUpgrade.time < userStruct.lastRetrieved.time)
//		{
//			resBuilder.status = UpgradeNormStructureStatus.FAIL_NOT_BUILT_YET
//			LOG.error(
//				'the upgrade time ' + timeOfUpgrade +
//					' is before the last time the building was retrieved:' +
//					userStruct.lastRetrieved)
//			return false;
//		}
//		if (user.id !== userStruct.userId)
//		{
//			resBuilder.status = UpgradeNormStructureStatus.FAIL_NOT_USERS_STRUCT
//			LOG.error('user struct belongs to someone else with id ' + userStruct.userId)
//			return false;
//		}
//		val userGems = user.gems
//		if ((gemsSpent > 0) && (userGems < gemsSpent))
//		{
//			LOG.error(
//				'user has ' + userGems + ' gems; trying to spend ' + gemsSpent + ' and ' +
//					resourceChange + ' ' + rt + ' to upgrade to structure=' + nextLevelStruct)
//			resBuilder.status = UpgradeNormStructureStatus.FAIL_NOT_ENOUGH_GEMS
//			return false;
//		}
//		if (ResourceType::CASH == rt)
//		{
//			if (user.cash < resourceChange)
//			{
//				resBuilder.status = UpgradeNormStructureStatus.FAIL_NOT_ENOUGH_CASH
//				LOG.error(
//					"user doesn't have enough cash, has " + user.cash + ', needs ' +
//						resourceChange)
//				return false;
//			}
//		}
//		else if (ResourceType::OIL == rt)
//		{
//			if (user.oil < resourceChange)
//			{
//				resBuilder.status = UpgradeNormStructureStatus.FAIL_NOT_ENOUGH_OIL
//				LOG.error(
//					"user doesn't have enough gems, has " + user.gems + ', needs ' +
//						resourceChange)
//				return false;
//			}
//		}
//		resBuilder.status = UpgradeNormStructureStatus.SUCCESS
//		true
//	}
//
//	private def writeChangesToDB(User user, StructureForUser userStruct,
//		Structure upgradedStruct, int gemsSpent, int resourceChange, ResourceType rt,
//		Timestamp timeOfUpgrade, Map<String, Integer> money)
//	{
//		val newStructId = upgradedStruct.id
//		if (!UpdateUtils::get.updateBeginUpgradingUserStruct(userStruct.id, newStructId,
//			timeOfUpgrade))
//		{
//			LOG.error(
//				'problem with changing time of upgrade to ' + timeOfUpgrade +
//					' and marking as incomplete, the user struct ' + userStruct)
//		}
//		var cashChange = 0
//		var oilChange = 0
//		val gemChange = -1 * gemsSpent
//		if (ResourceType::CASH == rt)
//		{
//			cashChange = resourceChange
//		}
//		else if (ResourceType::OIL == rt)
//		{
//			oilChange = resourceChange
//		}
//		val num = user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange)
//		if (1 !== num)
//		{
//			LOG.error(
//				'problem with updating user currency. gemChange=' + gemChange + ' cashChange=' +
//					cashChange + '	 oilChange=' + oilChange + '	 numRowsUpdated=' + num)
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
//	}
//
//	private def writeToUserCurrencyHistory(User aUser, StructureForUser userStruct,
//		Structure curStruct, Structure upgradedStruct, Timestamp timeOfUpgrade,
//		Map<String, Integer> money, int previousCash, int previousOil, int previousGems)
//	{
//		val userUuid = aUser.id
//		val userStructUuid = userStruct.id
//		val prevStructId = curStruct.id
//		val prevLevel = curStruct.level
//		val structDetailsSb = new StringBuilder()
//		structDetailsSb.append('uStructId:')
//		structDetailsSb.append(userStructUuid)
//		structDetailsSb.append(' preStructId:')
//		structDetailsSb.append(prevStructId)
//		structDetailsSb.append(' prevLevel:')
//		structDetailsSb.append(prevLevel)
//		val structDetails = structDetailsSb.toString
//		val previousCurrencies = new HashMap<String, Integer>()
//		val currentCurrencies = new HashMap<String, Integer>()
//		val reasonForChange = ControllerConstants.UCHRFC__UPGRADE_NORM_STRUCT
//		val reasonsForChanges = new HashMap<String, String>()
//		val details = new HashMap<String, String>()
//		val gems = MiscMethods::gems
//		val oil = MiscMethods::oil
//		val cash = MiscMethods::cash
//		previousCurrencies.put(cash, previousCash)
//		previousCurrencies.put(oil, previousOil)
//		previousCurrencies.put(gems, previousGems)
//		currentCurrencies.put(cash, aUser.cash)
//		currentCurrencies.put(oil, aUser.oil)
//		currentCurrencies.put(gems, aUser.gems)
//		reasonsForChanges.put(cash, reasonForChange)
//		reasonsForChanges.put(oil, reasonForChange)
//		reasonsForChanges.put(gems, reasonForChange)
//		details.put(cash, structDetails)
//		details.put(oil, structDetails)
//		details.put(gems, structDetails)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, timeOfUpgrade, money,
//			previousCurrencies, currentCurrencies, reasonsForChanges, details)
//	}
//}
