//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.StructureForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto
//import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto.FinishNormStructWaittimeStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.FinishNormStructWaittimeWithDiamondsRequestEvent
//import com.lvl6.mobsters.events.response.FinishNormStructWaittimeWithDiamondsResponseEvent
//import com.lvl6.mobsters.info.Structure
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
//class FinishNormStructWaittimeWithDiamondsController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(
//		typeof(FinishNormStructWaittimeWithDiamondsController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 2
//	}
//
//	override createRequestEvent()
//	{
//		new FinishNormStructWaittimeWithDiamondsRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as FinishNormStructWaittimeWithDiamondsRequestEvent)).
//			finishNormStructWaittimeWithDiamondsRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val userStructId = reqProto.userStructUuid
//		val timeOfSpeedup = new Timestamp(reqProto.timeOfSpeedup)
//		val gemCostToSpeedup = reqProto.gemCostToSpeedup
//		val resBuilder = FinishNormStructWaittimeWithDiamondsResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = FinishNormStructWaittimeStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			LOG.info('user=' + user)
//			var previousGems = 0
//			val userStruct = RetrieveUtils::userStructRetrieveUtils.
//				getSpecificUserStruct(userStructId)
//			var Structure struct = null
//			var Structure formerStruct = null
//			if (userStruct !== null)
//			{
//				val structId = userStruct.structId
//				struct = StructureRetrieveUtils::getStructForStructId(structId)
//				formerStruct = StructureRetrieveUtils::getPredecessorStructForStructId(structId)
//			}
//			val legitSpeedup = checkLegitSpeedup(resBuilder, user, userStruct, timeOfSpeedup,
//				struct, gemCostToSpeedup)
//			var success = false
//			val money = new HashMap<String, Integer>()
//			if (legitSpeedup)
//			{
//				previousGems = user.gems
//				success = writeChangesToDB(user, userStruct, timeOfSpeedup, struct,
//					gemCostToSpeedup, money)
//			}
//			if (success)
//			{
//				resBuilder.status = FinishNormStructWaittimeStatus.SUCCESS
//			}
//			val resEvent = new FinishNormStructWaittimeWithDiamondsResponseEvent(
//				senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.finishNormStructWaittimeWithDiamondsResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in FinishNormStructWaittimeWithDiamondsController.processRequestEvent',
//					e)
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
//						'fatal exception in FinishNormStructWaittimeWithDiamondsController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(user, userStruct, formerStruct, timeOfSpeedup, money,
//					previousGems)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in FinishNormStructWaittimeWithDiamondsController processEvent',
//				e)
//			try
//			{
//				resBuilder.status = FinishNormStructWaittimeStatus.FAIL_OTHER
//				val resEvent = new FinishNormStructWaittimeWithDiamondsResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.finishNormStructWaittimeWithDiamondsResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in FinishNormStructWaittimeWithDiamondsController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error(
//					'exception2 in FinishNormStructWaittimeWithDiamondsController processEvent',
//					e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegitSpeedup(Builder resBuilder, User user, StructureForUser userStruct,
//		Timestamp timeOfSpeedup, Structure struct, int gemCostToSpeedup)
//	{
//		if ((user === null) || (userStruct === null) || (struct === null) ||
//			(userStruct.userUuid !== user.id) || userStruct.complete)
//		{
//			resBuilder.status = FinishNormStructWaittimeStatus.FAIL_OTHER
//			LOG.error(
//				'something passed in is null. user=' + user + ', struct=' + struct +
//					", struct owner's id=" + userStruct.userUuid +
//					'	 or user struct is complete. userStruct=' + userStruct)
//			return false;
//		}
//		if (user.gems < gemCostToSpeedup)
//		{
//			resBuilder.status = FinishNormStructWaittimeStatus.FAIL_NOT_ENOUGH_GEMS
//			LOG.error(
//				"user doesn't have enough diamonds. has " + user.gems + ', needs ' +
//					gemCostToSpeedup)
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDB(User user, StructureForUser userStruct,
//		Timestamp timeOfSpeedup, Structure struct, int gemCost, Map<String, Integer> money)
//	{
//		val gemChange = -1 * gemCost
//		if (!user.updateRelativeGemsNaive(gemChange))
//		{
//			LOG.error(
//				'problem with using diamonds to finish norm struct build. userStruct=' +
//					userStruct + '	 struct=' + struct + '	 gemCost=' + gemChange)
//			return false;
//		}
//		else
//		{
//			if (0 !== gemChange)
//			{
//				money.put(MiscMethods::gems, gemChange)
//			}
//		}
//		if (!UpdateUtils::get.updateSpeedupUpgradingUserStruct(userStruct.id, timeOfSpeedup))
//		{
//			LOG.error(
//				'problem with completing norm struct build time. userStruct=' + userStruct +
//					'	 struct=' + struct + '	 gemCost=' + gemChange)
//			return false;
//		}
//		true
//	}
//
//	def writeToUserCurrencyHistory(User aUser, StructureForUser userStruct,
//		Structure formerStruct, Timestamp timeOfPurchase, Map<String, Integer> money,
//		int previousGems)
//	{
//		if (money.empty)
//		{
//			return;
//		}
//		val userStructId = userStruct.id
//		val structId = userStruct.structId
//		val structDetails = new StringBuilder()
//		if (null === formerStruct)
//		{
//			structDetails.append('construction ')
//		}
//		else
//		{
//			structDetails.append('upgrade ')
//		}
//		structDetails.append('uStructId: ')
//		structDetails.append(userStructId)
//		structDetails.append(' structId: ')
//		structDetails.append(structId)
//		if (null !== formerStruct)
//		{
//			val prevStructId = formerStruct.id
//			val prevLevel = formerStruct.level
//			structDetails.append(' prevStructId: ')
//			structDetails.append(prevStructId)
//			structDetails.append(' prevLevel: ')
//			structDetails.append(prevLevel)
//		}
//		val userUuid = aUser.id
//		val previousCurrencies = new HashMap<String, Integer>()
//		val currentCurrencies = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val details = new HashMap<String, String>()
//		val reasonForChange = ControllerConstants.UCHRFC__SPED_UP_NORM_STRUCT
//		val gems = MiscMethods::gems
//		previousCurrencies.put(gems, previousGems)
//		currentCurrencies.put(gems, aUser.gems)
//		reasonsForChanges.put(gems, reasonForChange)
//		val detail = structDetails.toString
//		details.put(gems, detail)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, timeOfPurchase, money,
//			previousCurrencies, currentCurrencies, reasonsForChanges, details)
//	}
//}
