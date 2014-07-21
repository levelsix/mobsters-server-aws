//package com.lvl6.mobsters.controllers.todo
//
//import com.amazonaws.services.ec2.model.ResourceType
//import com.lvl6.mobsters.dynamo.StructureForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureRequestProto.StructRetrieval
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto.RetrieveCurrencyFromNormStructureStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.RetrieveCurrencyFromNormStructureRequestEvent
//import com.lvl6.mobsters.events.response.RetrieveCurrencyFromNormStructureResponseEvent
//import com.lvl6.mobsters.info.StructureResourceGenerator
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Collection
//import java.util.Date
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
//class RetrieveCurrencyFromNormStructureController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(
//		typeof(RetrieveCurrencyFromNormStructureController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 14
//	}
//
//	override createRequestEvent()
//	{
//		new RetrieveCurrencyFromNormStructureRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_RETRIEVE_CURRENCY_FROM_NORM_STRUCTURE_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as RetrieveCurrencyFromNormStructureRequestEvent)).
//			retrieveCurrencyFromNormStructureRequestProto
//		val senderResourcesProto = reqProto.sender
//		val senderProto = senderResourcesProto.minUserProto
//		val userUuid = senderProto.userUuid
//		val structRetrievals = reqProto.structRetrievalsList
//		val curTime = new Timestamp((new Date()).time)
//		val maxCash = senderResourcesProto.maxCash
//		val maxOil = senderResourcesProto.maxOil
//		val userStructUuidsToTimesOfRetrieval = new HashMap<Integer, Timestamp>()
//		val userStructUuidsToAmountCollected = new HashMap<Integer, Integer>()
//		val duplicates = new ArrayList<Integer>()
//		getUuidsAndTimes(structRetrievals, duplicates, userStructUuidsToTimesOfRetrieval,
//			userStructUuidsToAmountCollected)
//		val resBuilder = RetrieveCurrencyFromNormStructureResponseProto::newBuilder
//		resBuilder.status = RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER
//		resBuilder.sender = senderResourcesProto
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			var previousCash = 0
//			var previousOil = 0
//			val userStructUuids = new ArrayList<Integer>(
//				userStructUuidsToTimesOfRetrieval.keySet)
//			val userStructUuidsToUserStructs = getUserStructUuidsToUserStructs(userUuid,
//				userStructUuids)
//			val userStructUuidsToGenerators = getUserStructUuidsToResourceGenerators(
//				userStructUuidsToUserStructs.values)
//			val resourcesGained = new HashMap<String, Integer>()
//			val legitRetrieval = checkLegitRetrieval(resBuilder, user, userStructUuids,
//				userStructUuidsToUserStructs, userStructUuidsToGenerators, duplicates,
//				userStructUuidsToTimesOfRetrieval, userStructUuidsToAmountCollected,
//				resourcesGained)
//			var cashGain = 0
//			var oilGain = 0
//			val currencyChange = new HashMap<String, Integer>()
//			var successful = false
//			if (legitRetrieval)
//			{
//				cashGain = resourcesGained.get(MiscMethods::cash)
//				previousCash = user.cash
//				oilGain = resourcesGained.get(MiscMethods::oil)
//				previousOil = user.oil
//				successful = writeChangesToDb(user, cashGain, oilGain,
//					userStructUuidsToUserStructs, userStructUuidsToTimesOfRetrieval,
//					userStructUuidsToAmountCollected, maxCash, maxOil, currencyChange)
//			}
//			if (successful)
//			{
//				resBuilder.status = RetrieveCurrencyFromNormStructureStatus.SUCCESS
//			}
//			val resEvent = new RetrieveCurrencyFromNormStructureResponseEvent(
//				senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.retrieveCurrencyFromNormStructureResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in RetrieveCurrencyFromNormStructureController.processRequestEvent',
//					e)
//			}
//			if (legitRetrieval)
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
//						'fatal exception in RetrieveCurrencyFromNormStructureController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(user, previousCash, previousOil, curTime,
//					userStructUuidsToUserStructs, userStructUuidsToGenerators,
//					userStructUuidsToTimesOfRetrieval, userStructUuidsToAmountCollected,
//					currencyChange)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in RetrieveCurrencyFromNormStructureController processEvent', e)
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def getUuidsAndTimes(List<StructRetrieval> srList, List<Integer> duplicates,
//		Map<Integer, Timestamp> structUuidsToTimesOfRetrieval,
//		Map<Integer, Integer> structUuidsToAmountCollected)
//	{
//		if (srList.empty)
//		{
//			LOG.error('RetrieveCurrencyFromNormStruct request did not send any user struct ids.')
//			return;
//		}
//		for (sr : srList)
//		{
//			val key = sr.userStructUuid
//			val value = new Timestamp(sr.timeOfRetrieval)
//			val amount = sr.amountCollected
//			if (structUuidsToTimesOfRetrieval.containsKey(key))
//			{
//				duplicates.add(key)
//			}
//			else
//			{
//				structUuidsToTimesOfRetrieval.put(key, value)
//				structUuidsToAmountCollected.put(key, amount)
//			}
//		}
//	}
//
//	private def getUserStructUuidsToUserStructs(String userUuid, List<Integer> userStructUuids)
//	{
//		val returnValue = new HashMap<Integer, StructureForUser>()
//		if ((null === userStructUuids) || userStructUuids.empty)
//		{
//			LOG.error('no user struct ids!')
//			return returnValue;
//		}
//		val userStructList = RetrieveUtils::userStructRetrieveUtils.
//			getSpecificOrAllUserStructsForUser(userUuid, userStructUuids)
//		for (us : userStructList)
//		{
//			if (null !== us)
//			{
//				returnValue.put(us.id, us)
//			}
//			else
//			{
//				LOG.error(
//					'could not retrieve one of the user structs. userStructUuids to retrieve=' +
//						userStructUuids + '. user structs retrieved=' + userStructList +
//						'. Continuing with processing.')
//			}
//		}
//		returnValue
//	}
//
//	private def getUserStructUuidsToResourceGenerators(
//		Collection<StructureForUser> userStructs)
//	{
//		val returnValue = new HashMap<Integer, StructureResourceGenerator>()
//		val structUuidsToStructs = StructureResourceGeneratorRetrieveUtils::
//			structUuidsToResourceGenerators
//		if ((null === userStructs) || userStructs.empty)
//		{
//			LOG.error('There are no user structs.')
//		}
//		for (us : userStructs)
//		{
//			val structId = us.structId
//			val userStructId = us.id
//			val s = structUuidsToStructs.get(structId)
//			if (null !== s)
//			{
//				returnValue.put(userStructId, s)
//			}
//			else
//			{
//				LOG.error(
//					'structure with id ' + structId +
//						' does not exist, therefore UserStruct is invalid:' + us)
//			}
//		}
//		returnValue
//	}
//
//	private def checkLegitRetrieval(Builder resBuilder, User user,
//		List<Integer> userStructUuids,
//		Map<Integer, StructureForUser> userStructUuidsToUserStructs,
//		Map<Integer, StructureResourceGenerator> userStructUuidsToGenerators,
//		List<Integer> duplicates, Map<Integer, Timestamp> userStructUuidsToTimesOfRetrieval,
//		Map<Integer, Integer> userStructUuidsToAmountCollected,
//		Map<String, Integer> resourcesGained)
//	{
//		val userUuid = user.id
//		if ((user === null) || userStructUuids.empty || userStructUuidsToUserStructs.empty ||
//			userStructUuidsToGenerators.empty || userStructUuidsToTimesOfRetrieval.empty)
//		{
//			LOG.error(
//				'user is null, or no struct ids, user structs, structures, or retrieval times . user=' +
//					user + '	 userStructUuids=' + userStructUuids +
//					'	 structUuidsToUserStructs=' + userStructUuidsToUserStructs +
//					'	 userStructUuidsToGenerators=' + userStructUuidsToGenerators +
//					'	 userStructUuidsToRetrievalTimes=' + userStructUuidsToTimesOfRetrieval)
//			return false;
//		}
//		if (!duplicates.empty)
//		{
//			resBuilder.status = RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER
//			LOG.warn('duplicate struct ids in request. ids=' + duplicates)
//		}
//		var cash = 0
//		var oil = 0
//		for (id : userStructUuids)
//		{
//			val userStruct = userStructUuidsToUserStructs.get(id)
//			val struct = userStructUuidsToGenerators.get(id)
//			if ((null === userStruct) || (userUuid !== userStruct.userUuid) ||
//				!userStruct.complete)
//			{
//				resBuilder.status = RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER
//				LOG.error(
//					'(will continue processing) struct owner is not user, or struct' +
//						' is not complete yet. userStruct=' + userStruct)
//				userStructUuidsToUserStructs.remove(id)
//				userStructUuidsToTimesOfRetrieval.remove(id)
//				userStructUuidsToAmountCollected.remove(id)
//        continue;
//			}
//			val type = struct.resourceTypeGenerated
//			val rt = ResourceType::valueOf(type)
//			if (ResourceType::CASH == rt)
//			{
//				cash += userStructUuidsToAmountCollected.get(id)
//			}
//			else if (ResourceType::OIL == rt)
//			{
//				oil += userStructUuidsToAmountCollected.get(id)
//			}
//			else
//			{
//				LOG.error('(will continue processing) unknown resource type: ' + rt)
//				userStructUuidsToUserStructs.remove(id)
//				userStructUuidsToTimesOfRetrieval.remove(id)
//				userStructUuidsToAmountCollected.remove(id)
//			}
//		}
//		resourcesGained.put(MiscMethods::cash, cash)
//		resourcesGained.put(MiscMethods::oil, oil)
//		true
//	}
//
//	private def writeChangesToDb(User user, int cashGain, int oilGain,
//		Map<Integer, StructureForUser> userStructUuidsToUserStructs,
//		Map<Integer, Timestamp> userStructUuidsToTimesOfRetrieval,
//		Map<Integer, Integer> userStructUuidsToAmountCollected, int maxCash, int maxOil,
//		Map<String, Integer> currencyChange)
//	{
//		val curCash = Math::min(user.cash, maxCash)
//		val maxCashUserCanGain = maxCash - curCash
//		cashGain = Math::min(maxCashUserCanGain, cashGain)
//		val curOil = Math::min(user.oil, maxOil)
//		val maxOilUserCanGain = maxOil - curOil
//		oilGain = Math::min(maxOilUserCanGain, oilGain)
//		if (!user.updateRelativeCoinsOilRetrievedFromStructs(cashGain, oilGain))
//		{
//			LOG.error(
//				'problem with updating user stats after retrieving ' + cashGain + ' cash' + '	' +
//					oilGain + ' oil.')
//			return false;
//		}
//		else
//		{
//			if (0 !== oilGain)
//			{
//				currencyChange.put(MiscMethods::oil, oilGain)
//			}
//			if (0 !== cashGain)
//			{
//				currencyChange.put(MiscMethods::cash, cashGain)
//			}
//		}
//		if (!UpdateUtils::get.updateUserStructsLastretrieved(userStructUuidsToTimesOfRetrieval,
//			userStructUuidsToUserStructs))
//		{
//			LOG.error(
//				'problem with updating user structs last retrieved for userStructUuids ' +
//					userStructUuidsToTimesOfRetrieval)
//			return false;
//		}
//		true
//	}
//
//	def writeToUserCurrencyHistory(User aUser, int previousCash, int previousOil,
//		Timestamp curTime, Map<Integer, StructureForUser> userStructUuidsToUserStructs,
//		Map<Integer, StructureResourceGenerator> userStructUuidsToGenerators,
//		Map<Integer, Timestamp> userStructUuidsToTimesOfRetrieval,
//		Map<Integer, Integer> userStructUuidsToAmountCollected,
//		Map<String, Integer> currencyChange)
//	{
//		val userUuid = aUser.id
//		val previousCurrencies = new HashMap<String, Integer>()
//		val currentCurrencies = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val details = new HashMap<String, String>()
//		val cash = MiscMethods::cash
//		val oil = MiscMethods::oil
//		val reasonForChange = ControllerConstants.UCHRFC__RETRIEVE_CURRENCY_FROM_NORM_STRUCT
//		val cashDetailSb = new StringBuilder()
//		cashDetailSb.append('(userStructId,time,amount)=')
//		val oilDetailSb = new StringBuilder()
//		oilDetailSb.append('(userStructId,time,amount)=')
//		for (id : userStructUuidsToAmountCollected.keySet)
//		{
//			val struct = userStructUuidsToGenerators.get(id)
//			val t = userStructUuidsToTimesOfRetrieval.get(id)
//			val amount = userStructUuidsToAmountCollected.get(id)
//			val type = struct.resourceTypeGenerated
//			val rt = ResourceType::valueOf(type)
//			if (ResourceType::CASH == rt)
//			{
//				cashDetailSb.append('(')
//				cashDetailSb.append(id)
//				cashDetailSb.append(',')
//				cashDetailSb.append(t)
//				cashDetailSb.append(',')
//				cashDetailSb.append(amount)
//				cashDetailSb.append(')')
//			}
//			else if (ResourceType::OIL == rt)
//			{
//				oilDetailSb.append('(')
//				oilDetailSb.append(id)
//				oilDetailSb.append(',')
//				oilDetailSb.append(t)
//				oilDetailSb.append(',')
//				oilDetailSb.append(amount)
//				oilDetailSb.append(')')
//			}
//		}
//		previousCurrencies.put(cash, previousCash)
//		previousCurrencies.put(oil, previousOil)
//		currentCurrencies.put(cash, aUser.cash)
//		currentCurrencies.put(oil, aUser.oil)
//		reasonsForChanges.put(cash, reasonForChange)
//		reasonsForChanges.put(oil, reasonForChange)
//		details.put(cash, cashDetailSb.toString)
//		details.put(oil, oilDetailSb.toString)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//			previousCurrencies, currentCurrencies, reasonsForChanges, details)
//	}
//}
