//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser
//import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser
//import com.lvl6.mobsters.dynamo.MonsterForUser
//import com.lvl6.mobsters.dynamo.MonsterHealingForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementResponseProto
//import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementResponseProto.SubmitMonsterEnhancementStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.SubmitMonsterEnhancementRequestEvent
//import com.lvl6.mobsters.events.response.SubmitMonsterEnhancementResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserEnhancementItemProto
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Collections
//import java.util.Date
//import java.util.HashMap
//import java.util.HashSet
//import java.util.Map
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class SubmitMonsterEnhancementController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(SubmitMonsterEnhancementController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//
//	new()
//	{
//		numAllocatedThreads = 3
//	}
//
//	override createRequestEvent()
//	{
//		new SubmitMonsterEnhancementRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_SUBMIT_MONSTER_ENHANCEMENT_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as SubmitMonsterEnhancementRequestEvent)).
//			submitMonsterEnhancementRequestProto
//		val senderResourcesProto = reqProto.sender
//		val senderProto = senderResourcesProto.minUserProto
//		val ueipDelete = reqProto.ueipDeleteList
//		val ueipUpdated = reqProto.ueipUpdateList
//		val ueipNew = reqProto.ueipNewList
//		val userUuid = senderProto.userUuid
//		val gemsSpent = reqProto.gemsSpent
//		val oilChange = reqProto.oilChange
//		val clientTime = new Timestamp((new Date()).time)
//		val maxOil = senderResourcesProto.maxOil
//		val deleteMap = MonsterStuffUtils::convertIntoUserMonsterIdToUeipProtoMap(ueipDelete)
//		val updateMap = MonsterStuffUtils::convertIntoUserMonsterIdToUeipProtoMap(ueipUpdated)
//		val newMap = MonsterStuffUtils::convertIntoUserMonsterIdToUeipProtoMap(ueipNew)
//		val resBuilder = SubmitMonsterEnhancementResponseProto::newBuilder
//		resBuilder.sender = senderResourcesProto
//		resBuilder.status = SubmitMonsterEnhancementStatus.FAIL_OTHER
//		locker.lockPlayer(userUuid, class.simpleName)
//		try
//		{
//			var previousOil = 0
//			var previousGems = 0
//			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
//			val alreadyEnhancing = MonsterEnhancingForUserRetrieveUtils::
//				getMonstersForUser(userUuid)
//			val alreadyHealing = MonsterHealingForUserRetrieveUtils::
//				getMonstersForUser(userUuid)
//			val evolution = MonsterEvolvingForUserRetrieveUtils::getEvolutionForUser(userUuid)
//			val newUuids = new HashSet<Long>()
//			newUuids.addAll(newMap.keySet)
//			val existingUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
//				getSpecificOrAllUserMonstersForUser(userUuid, newUuids)
//			val legitMonster = checkLegit(resBuilder, aUser, userUuid, existingUserMonsters,
//				alreadyEnhancing, alreadyHealing, deleteMap, updateMap, newMap, evolution,
//				gemsSpent, oilChange)
//			var successful = false
//			val money = new HashMap<String, Integer>()
//			if (legitMonster)
//			{
//				previousOil = aUser.oil
//				previousGems = aUser.gems
//				successful = writeChangesToDB(aUser, userUuid, gemsSpent, oilChange, deleteMap,
//					updateMap, newMap, money, maxOil)
//			}
//			if (successful)
//			{
//				resBuilder.status = SubmitMonsterEnhancementStatus.SUCCESS
//			}
//			val resEvent = new SubmitMonsterEnhancementResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.submitMonsterEnhancementResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in SubmitMonsterEnhancementController.processRequestEvent',
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
//						'fatal exception in SubmitMonsterEnhancementController.processRequestEvent',
//						e)
//				}
//				writeToUserCurrencyHistory(aUser, clientTime, money, previousOil, previousGems,
//					deleteMap, updateMap, newMap)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in EnhanceMonster processEvent', e)
//		}
//		finally
//		{
//			locker.unlockPlayer(userUuid, class.simpleName)
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, User u, String userUuid,
//		Map<Long, MonsterForUser> existingUserMonsters,
//		Map<Long, MonsterEnhancingForUser> alreadyEnhancing,
//		Map<Long, MonsterHealingForUser> alreadyHealing,
//		Map<Long, UserEnhancementItemProto> deleteMap,
//		Map<Long, UserEnhancementItemProto> updateMap,
//		Map<Long, UserEnhancementItemProto> newMap, MonsterEvolvingForUser evolution,
//		int gemsSpent, int oilChange)
//	{
//		if (null === u)
//		{
//			LOG.error(
//				'unexpected error: user is null. user=' + u + '	 deleteMap=' + deleteMap +
//					'	 updateMap=' + updateMap + '	 newMap=' + newMap)
//			return false;
//		}
//		var keepThingsInDomain = true
//		var keepThingsNotInDomain = false
//		val alreadyEnhancingUuids = alreadyEnhancing.keySet
//		if ((null !== deleteMap) && !deleteMap.empty)
//		{
//			MonsterStuffUtils::retainValidMonsters(alreadyEnhancingUuids, deleteMap,
//				keepThingsInDomain, keepThingsNotInDomain)
//		}
//		if ((null !== updateMap) && !updateMap.empty)
//		{
//			MonsterStuffUtils::retainValidMonsters(alreadyEnhancingUuids, updateMap,
//				keepThingsInDomain, keepThingsNotInDomain)
//		}
//		if ((null !== newMap) && !newMap.empty)
//		{
//			val existingUuids = existingUserMonsters.keySet
//			MonsterStuffUtils::retainValidMonsters(existingUuids, newMap, keepThingsInDomain,
//				keepThingsNotInDomain)
//			keepThingsInDomain = false
//			keepThingsNotInDomain = true
//			val alreadyHealingUuids = alreadyHealing.keySet
//			MonsterStuffUtils::retainValidMonsters(alreadyHealingUuids, newMap,
//				keepThingsInDomain, keepThingsNotInDomain)
//			val idsInEvolutions = MonsterStuffUtils::
//				getUserMonsterUuidsUsedInEvolution(evolution, null)
//			MonsterStuffUtils::retainValidMonsters(idsInEvolutions, newMap, keepThingsInDomain,
//				keepThingsNotInDomain)
//		}
//		if (!hasEnoughGems(resBuilder, u, gemsSpent, oilChange, deleteMap, updateMap, newMap))
//		{
//			return false;
//		}
//		if (!hasEnoughOil(resBuilder, u, gemsSpent, oilChange, deleteMap, updateMap, newMap))
//		{
//			return false;
//		}
//		true
//	}
//
//	private def hasEnoughGems(Builder resBuilder, User u, int gemsSpent, int oilChange,
//		Map<Long, UserEnhancementItemProto> deleteMap,
//		Map<Long, UserEnhancementItemProto> updateMap,
//		Map<Long, UserEnhancementItemProto> newMap)
//	{
//		val userGems = u.gems
//		if (userGems < gemsSpent)
//		{
//			LOG.error(
//				'user error: user does not have enough gems. userGems=' + userGems +
//					'	 gemsSpent=' + gemsSpent + '	 deleteMap=' + deleteMap + '	 newMap=' +
//					newMap + '	 updateMap=' + updateMap + '	 cashChange=' + oilChange +
//					'	 user=' + u)
//			resBuilder.status = SubmitMonsterEnhancementStatus.FAIL_INSUFFICIENT_GEMS
//			return false;
//		}
//		true
//	}
//
//	private def hasEnoughOil(Builder resBuilder, User u, int oilChange, int gemsSpent,
//		Map<Long, UserEnhancementItemProto> deleteMap,
//		Map<Long, UserEnhancementItemProto> updateMap,
//		Map<Long, UserEnhancementItemProto> newMap)
//	{
//		val userOil = u.oil
//		val cost = -1 * oilChange
//		if ((0 === gemsSpent) && (userOil < cost))
//		{
//			LOG.error(
//				'user error: user does not have enough oil. userOil=' + userOil + '	 cost=' +
//					cost + '	 deleteMap=' + deleteMap + '	 newMap=' + newMap + '	 updateMap=' +
//					updateMap + '	 user=' + u)
//			resBuilder.status = SubmitMonsterEnhancementStatus.FAIL_INSUFFICIENT_OIL
//			return false;
//		}
//		true
//	}
//
//	private def writeChangesToDB(User user, int uId, int gemsSpent, int oilChange,
//		Map<Long, UserEnhancementItemProto> protoDeleteMap,
//		Map<Long, UserEnhancementItemProto> protoUpdateMap,
//		Map<Long, UserEnhancementItemProto> protoNewMap, Map<String, Integer> money,
//		int maxOil)
//	{
//		val cashChange = 0
//		val gemChange = -1 * gemsSpent
//		if (oilChange > 0)
//		{
//			val curOil = Math::min(user.oil, maxOil)
//			val maxOilUserCanGain = maxOil - curOil
//			oilChange = Math::min(oilChange, maxOilUserCanGain)
//		}
//		if ((0 !== oilChange) || (0 !== gemChange))
//		{
//			val numChange = user.updateRelativeCashAndOilAndGems(cashChange, oilChange,
//				gemChange)
//			if (1 !== numChange)
//			{
//				LOG.warn(
//					'problem with updating user stats: gemChange=' + gemChange + ', oilChange=' +
//						oilChange + ', user is ' + user +
//						'	 perhaps base monster deleted 	 protoDeleteMap=' + protoDeleteMap)
//			}
//			else
//			{
//				if (0 !== oilChange)
//				{
//					money.put(MiscMethods::oil, oilChange)
//				}
//				if (0 !== gemsSpent)
//				{
//					money.put(MiscMethods::gems, gemChange)
//				}
//			}
//		}
//		var num = 0
//		if (!protoDeleteMap.empty)
//		{
//			val deleteUuids = new ArrayList<Long>(protoDeleteMap.keySet)
//			num = DeleteUtils::get.deleteMonsterEnhancingForUser(uId, deleteUuids)
//			LOG.info(
//				'deleted monster enhancing rows. numDeleted=' + num + '	 protoDeleteMap=' +
//					protoDeleteMap)
//		}
//		val updateMap = MonsterStuffUtils::convertToMonsterEnhancingForUser(uId, protoUpdateMap)
//		LOG.info('updateMap=' + updateMap)
//		val newMap = MonsterStuffUtils::convertToMonsterEnhancingForUser(uId, protoNewMap)
//		LOG.info('newMap=' + newMap)
//		val updateAndNew = new ArrayList<MonsterEnhancingForUser>()
//		updateAndNew.addAll(updateMap)
//		updateAndNew.addAll(newMap)
//		if ((null !== updateAndNew) && !updateAndNew.empty)
//		{
//			num = UpdateUtils::get.updateUserMonsterEnhancing(uId, updateAndNew)
//			LOG.info('updated monster enhancing rows. numUpdated/inserted=' + num)
//		}
//		if ((null !== protoNewMap) && !protoNewMap.empty)
//		{
//			val size = protoNewMap.size
//			val userMonsterIdList = new ArrayList<Long>(protoNewMap.keySet)
//			val teamSlotNumList = Collections::nCopies(size, 0)
//			num = UpdateUtils::get.
//				updateNullifyUserMonstersTeamSlotNum(userMonsterIdList, teamSlotNumList)
//			LOG.info('updated user monster rows. numUpdated=' + num)
//		}
//		true
//	}
//
//	def writeToUserCurrencyHistory(User aUser, Timestamp date,
//		Map<String, Integer> currencyChange, int previousOil, int previousGems,
//		Map<Long, UserEnhancementItemProto> protoDeleteMap,
//		Map<Long, UserEnhancementItemProto> protoUpdateMap,
//		Map<Long, UserEnhancementItemProto> protoNewMap)
//	{
//		val detailsSb = new StringBuilder()
//		if ((null !== protoDeleteMap) && !protoDeleteMap.empty)
//		{
//			detailsSb.append('deleteUuids=')
//			for (ueip : protoDeleteMap.values)
//			{
//				val id = ueip.userMonsterId
//				detailsSb.append(id)
//				detailsSb.append(' ')
//			}
//		}
//		if ((null !== protoUpdateMap) && !protoUpdateMap.empty)
//		{
//			detailsSb.append('updateUuids=')
//			for (ueip : protoUpdateMap.values)
//			{
//				val id = ueip.userMonsterId
//				detailsSb.append(id)
//				detailsSb.append(' ')
//			}
//		}
//		if ((null !== protoNewMap) && !protoNewMap.empty)
//		{
//			detailsSb.append('newUuids=')
//			for (ueip : protoNewMap.values)
//			{
//				val id = ueip.userMonsterId
//				detailsSb.append(id)
//				detailsSb.append(' ')
//			}
//		}
//		val userUuid = aUser.id
//		val previousCurrency = new HashMap<String, Integer>()
//		val currentCurrency = new HashMap<String, Integer>()
//		val reasonsForChanges = new HashMap<String, String>()
//		val detailsMap = new HashMap<String, String>()
//		val reason = ControllerConstants.UCHRFC__ENHANCING
//		val oil = MiscMethods::oil
//		val gems = MiscMethods::gems
//		previousCurrency.put(oil, previousOil)
//		previousCurrency.put(gems, previousGems)
//		currentCurrency.put(oil, aUser.oil)
//		currentCurrency.put(gems, aUser.gems)
//		reasonsForChanges.put(oil, reason)
//		reasonsForChanges.put(gems, reason)
//		detailsMap.put(oil, detailsSb.toString)
//		detailsMap.put(gems, detailsSb.toString)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, date, currencyChange, previousCurrency,
//			currentCurrency, reasonsForChanges, detailsMap)
//	}
//}
