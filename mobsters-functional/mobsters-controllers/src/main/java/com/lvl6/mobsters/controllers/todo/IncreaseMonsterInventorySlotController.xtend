//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.StructureForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotRequestProto.IncreaseSlotType
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto.IncreaseMonsterInventorySlotStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.IncreaseMonsterInventorySlotRequestEvent
//import com.lvl6.mobsters.events.response.IncreaseMonsterInventorySlotResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructureInfoProto.StructType
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Collections
//import java.util.Comparator
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
//class IncreaseMonsterInventorySlotController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(IncreaseMonsterInventorySlotController))
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
//		new IncreaseMonsterInventorySlotRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_INCREASE_MONSTER_INVENTORY_SLOT_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as IncreaseMonsterInventorySlotRequestEvent)).
//			increaseMonsterInventorySlotRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val increaseType = reqProto.increaseSlotType
//		val userStructId = reqProto.userStructUuid
//		val userFbInviteUuids = reqProto.userFbInviteForSlotUuidsList
//		val curTime = new Timestamp((new Date()).time)
//		val resBuilder = IncreaseMonsterInventorySlotResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = IncreaseMonsterInventorySlotStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			var previousGems = 0
//			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
//			val sfu = RetrieveUtils::userStructRetrieveUtils.getSpecificUserStruct(userStructId)
//			val idsToAcceptedInvites = new HashMap<Integer, UserFacebookInviteForSlot>()
//			val legit = checkLegit(resBuilder, userUuid, aUser, userStructId, sfu, increaseType,
//				userFbInviteUuids, idsToAcceptedInvites)
//			var gemCost = 0
//			var successful = false
//			val changeMap = new HashMap<String, Integer>()
//			if (legit)
//			{
//				previousGems = aUser.gems
//				gemCost = getGemPriceFromStruct(sfu)
//				successful = writeChangesToDb(aUser, sfu, increaseType, gemCost, curTime,
//					idsToAcceptedInvites, changeMap)
//			}
//			if (successful)
//			{
//				resBuilder.status = IncreaseMonsterInventorySlotStatus.SUCCESS
//			}
//			val resEvent = new IncreaseMonsterInventorySlotResponseEvent(userUuid)
//			resEvent.tag = event.tag
//			resEvent.increaseMonsterInventorySlotResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in IncreaseMonsterInventorySlotController.processRequestEvent',
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
//						'fatal exception in IncreaseMonsterInventorySlotController.processRequestEvent',
//						e)
//				}
//				if (increaseType === IncreaseSlotType::PURCHASE)
//				{
//					writeToUserCurrencyHistory(aUser, sfu, increaseType, curTime, changeMap,
//						previousGems)
//				}
//				deleteInvitesForSlotsAfterPurchase(userUuid, changeMap)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in IncreaseMonsterInventorySlotController processEvent', e)
//			try
//			{
//				resBuilder.status = IncreaseMonsterInventorySlotStatus.FAIL_OTHER
//				val resEvent = new IncreaseMonsterInventorySlotResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.increaseMonsterInventorySlotResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in IncreaseMonsterInventorySlotController.processRequestEvent',
//						e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in IncreaseMonsterInventorySlotController processEvent', e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, String userUuid, User u, int userStructId,
//		StructureForUser sfu, IncreaseSlotType aType, List<Integer> userFbInviteUuids,
//		Map<Integer, UserFacebookInviteForSlot> idsToAcceptedInvites)
//	{
//		if (null === u)
//		{
//			LOG.error('user is null. no user exists with id=' + userUuid)
//			return false;
//		}
//		if (null === sfu)
//		{
//			LOG.error("doesn't exist, user struct with id=" + userStructId)
//			return false;
//		}
//		if (IncreaseSlotType::REDEEM_FACEBOOK_INVITES === aType)
//		{
//			val idsToAcceptedTemp = getInvites(userUuid, userFbInviteUuids)
//			if ((null === idsToAcceptedTemp) || idsToAcceptedTemp.empty)
//			{
//				LOG.error('no invites exist with ids: ' + userFbInviteUuids)
//				return false;
//			}
//			val userStructIdFromInvites = getUserStructId(idsToAcceptedTemp)
//			if (userStructId !== userStructIdFromInvites)
//			{
//				resBuilder.status = IncreaseMonsterInventorySlotStatus::
//					FAIL_INCONSISTENT_INVITE_DATA
//				LOG.error(
//					"data across invites aren't consistent: user struct id/fb lvl. invites=" +
//						idsToAcceptedTemp + '	 expectedUserStructId=' + userStructId)
//				return false;
//			}
//			val structId = sfu.structId
//			val struct = StructureRetrieveUtils::getStructForStructId(structId)
//			val structLvl = struct.level
//			val nextUserStructFbInviteLvl = sfu.fbInviteStructLvl + 1
//			if (nextUserStructFbInviteLvl > structLvl)
//			{
//				resBuilder.status = IncreaseMonsterInventorySlotStatus::
//					FAIL_STRUCTURE_AT_MAX_FB_INVITE_LVL
//				LOG.error(
//					'user struct maxed fb invite lvl. userStruct=' + sfu + '	 struct=' + struct)
//				return false;
//			}
//			val minNumInvites = getMinNumInvitesFromStruct(sfu, structId,
//				nextUserStructFbInviteLvl)
//			val acceptedAmount = idsToAcceptedTemp.size
//			if (acceptedAmount < minNumInvites)
//			{
//				resBuilder.status = IncreaseMonsterInventorySlotStatus::
//					FAIL_INSUFFICIENT_FACEBOOK_INVITES
//				LOG.error(
//					"user doesn't meet num accepted facebook invites to increase slots. " +
//						'minRequired=' + minNumInvites + '	 has:' + acceptedAmount)
//				return false;
//			}
//			idsToAcceptedInvites.putAll(idsToAcceptedTemp)
//		}
//		else if (IncreaseSlotType::PURCHASE === aType)
//		{
//			val gemPrice = getGemPriceFromStruct(sfu)
//			val userGems = u.gems
//			if (userGems < gemPrice)
//			{
//				resBuilder.status = IncreaseMonsterInventorySlotStatus.FAIL_INSUFFICIENT_FUNDS
//				LOG.error(
//					'user does not have enough gems to buy more monster inventory slots. userGems=' +
//						userGems + '	 gemPrice=' + gemPrice)
//				return false;
//			}
//		}
//		else
//		{
//			return false;
//		}
//		true
//	}
//
//	private def getInvites(String userUuid, List<Integer> userFbInviteUuids)
//	{
//		val filterByAccepted = true
//		val isAccepted = true
//		val filterByRedeemed = true
//		val isRedeemed = false
//		val idsToAcceptedTemp = UserFacebookInviteForSlotRetrieveUtils::
//			getSpecificOrAllInvitesForInviter(userUuid, userFbInviteUuids, filterByAccepted,
//				isAccepted, filterByRedeemed, isRedeemed)
//		idsToAcceptedTemp
//	}
//
//	private def getUserStructId(Map<Integer, UserFacebookInviteForSlot> idsToAcceptedTemp)
//	{
//		var prevUserStructId = -1
//		var prevUserStructFbLvl = -1
//		for (invite : idsToAcceptedTemp.values)
//		{
//			val tempUserStructUuid = invite.userStructUuid
//			val tempUserStructFbLvl = invite.userStructFbLvl
//			if (-1 === prevUserStructId)
//			{
//				prevUserStructId = tempUserStructId
//				prevUserStructFbLvl = tempUserStructFbLvl
//			}
//			else if ((prevUserStructId !== tempUserStructId) ||
//				(prevUserStructFbLvl !== tempUserStructFbLvl))
//			{
//				return -1;
//			}
//		}
//		prevUserStructId
//	}
//
//	private def getMinNumInvitesFromStruct(StructureForUser sfu, int structId,
//		int userStructFbInviteLvl)
//	{
//		val structForFbInviteLvl = StructureRetrieveUtils::
//			getPredecessorStructForStructIdAndLvl(structId, userStructFbInviteLvl)
//		val structType = structForFbInviteLvl.structType
//		LOG.info('StructureForUser=' + sfu)
//		LOG.info('structId=' + structForFbInviteLvl)
//		LOG.info('userStructFbInviteLvl=' + userStructFbInviteLvl)
//		LOG.info('resulting structure for structId and level: ' + structForFbInviteLvl)
//		var minNumInvites = -1
//		if (StructType::valueOf(structType) === StructType::RESIDENCE)
//		{
//			val structIdForUserStructFbInviteLvl = structForFbInviteLvl.id
//			val residence = StructureResidenceRetrieveUtils::
//				getResidenceForStructId(structIdForUserStructFbInviteLvl)
//			minNumInvites = residence.numAcceptedFbInvites
//		}
//		else
//		{
//			LOG.error(
//				'invalid struct type for increasing monster slots. structType=' + structType)
//		}
//		LOG.info('getMinNumInvitesFromStruct returns minNumInvites=' + minNumInvites)
//		minNumInvites
//	}
//
//	private def getGemPriceFromStruct(StructureForUser sfu)
//	{
//		val structId = sfu.structId
//		val struct = StructureRetrieveUtils::getStructForStructId(structId)
//		val structType = struct.structType
//		var gemPrice = Integer.MAX_VALUE
//		if (StructType::valueOf(structType) === StructType::RESIDENCE)
//		{
//			val residence = StructureResidenceRetrieveUtils::getResidenceForStructId(structId)
//			gemPrice = residence.numGemsRequired
//		}
//		gemPrice
//	}
//
//	private def writeChangesToDb(User aUser, StructureForUser sfu,
//		IncreaseSlotType increaseType, int gemCost, Timestamp curTime,
//		Map<Integer, UserFacebookInviteForSlot> idsToAcceptedInvites,
//		Map<String, Integer> changeMap)
//	{
//		var success = false
//		if (IncreaseSlotType::REDEEM_FACEBOOK_INVITES === increaseType)
//		{
//			val structId = sfu.structId
//			val nextUserStructFbInviteLvl = sfu.fbInviteStructLvl + 1
//			val minNumInvites = getMinNumInvitesFromStruct(sfu, structId,
//				nextUserStructFbInviteLvl)
//			val inviteUuidsTheRest = new ArrayList<Integer>()
//			val nEarliestInvites = nEarliestInvites(idsToAcceptedInvites, minNumInvites,
//				inviteUuidsTheRest)
//			var num = UpdateUtils::get.
//				updateRedeemUserFacebookInviteForSlot(curTime, nEarliestInvites)
//			LOG.info('num saved: ' + num)
//			if (num !== minNumInvites)
//			{
//				LOG.error('expected updated: ' + minNumInvites + '	 actual updated: ' + num)
//				return false;
//			}
//			val numCurInvites = inviteUuidsTheRest.size
//			if (numCurInvites > 0)
//			{
//				LOG.info(
//					'num current invites: ' + numCurInvites + ' invitesToDelete= ' +
//						inviteUuidsTheRest)
//				num = DeleteUtils::get.deleteUserFacebookInvitesForSlots(inviteUuidsTheRest)
//				LOG.info('num deleted: ' + num)
//			}
//			success = true
//		}
//		if (IncreaseSlotType::PURCHASE === increaseType)
//		{
//			val cost = -1 * gemCost
//			success = aUser.updateRelativeGemsNaive(cost)
//			if (!success)
//			{
//				LOG.error('problem with updating user monster inventory slots and diamonds')
//				return false;
//			}
//			if (success && (0 !== cost))
//			{
//				changeMap.put(MiscMethods::gems, cost)
//			}
//		}
//		val userStructId = sfu.id
//		val fbInviteLevelChange = 1
//		if (!UpdateUtils::get.updateUserStructLevel(userStructId, fbInviteLevelChange))
//		{
//			LOG.error(
//				"(won't continue processing) couldn't update fbInviteLevel for user struct=" +
//					sfu)
//			return false;
//		}
//		success
//	}
//
//	private def nEarliestInvites(Map<Integer, UserFacebookInviteForSlot> idsToAcceptedInvites,
//		int n, List<Integer> inviteUuidsTheRest)
//	{
//		val earliestAcceptedInvites = new ArrayList<UserFacebookInviteForSlot>(
//			idsToAcceptedInvites.values)
//		orderUserFacebookAcceptedInvitesForSlots(earliestAcceptedInvites)
//		if (n < earliestAcceptedInvites.size)
//		{
//			val amount = earliestAcceptedInvites.size
//			for (invite : earliestAcceptedInvites.subList(n, amount))
//			{
//				val id = invite.id
//				inviteUuidsTheRest.add(id)
//			}
//			return earliestAcceptedInvites.subList(0, n);
//		}
//		else
//		{
//			return earliestAcceptedInvites;
//		}
//	}
//
//	private def orderUserFacebookAcceptedInvitesForSlots(
//		List<UserFacebookInviteForSlot> invites)
//	{
//		Collections::sort(
//			invites,
//			new Comparator<UserFacebookInviteForSlot>()
//			{
//				override compare(UserFacebookInviteForSlot lhs,
//					UserFacebookInviteForSlot rhs)
//				{
//					val lhsDate = lhs.timeAccepted
//					val rhsDate = rhs.timeAccepted
//					if ((null === lhsDate) && (null === rhsDate))
//					{
//						return 0;
//					}
//					else if (null === lhsDate)
//					{
//						return -1;
//					}
//					else if (null === rhsDate)
//					{
//						return 1;
//					}
//					else if (lhsDate.time < rhsDate.time)
//					{
//						return -1;
//					}
//					else if (lhsDate.time === rhsDate.time)
//					{
//						return 0;
//					}
//					else
//					{
//						return 1;
//					}
//				}
//			}
//		)
//	}
//
//	private def writeToUserCurrencyHistory(User aUser, StructureForUser sfu,
//		IncreaseSlotType increaseType, Timestamp curTime, Map<String, Integer> changeMap,
//		int previousGems)
//	{
//		if (changeMap.empty)
//		{
//			return;
//		}
//		val userUuid = aUser.id
//		val previousCurrencyMap = new HashMap<String, Integer>()
//		val currentCurrencyMap = new HashMap<String, Integer>()
//		val changeReasonsMap = new HashMap<String, String>()
//		val detailsMap = new HashMap<String, String>()
//		val gems = MiscMethods::gems
//		val reasonForChange = ControllerConstants.UCHRFC__INCREASE_MONSTER_INVENTORY
//		val sb = new StringBuilder()
//		sb.append('increaseType=')
//		sb.append(increaseType.name)
//		sb.append(' prevFbInviteStructLvl=')
//		sb.append(sfu.fbInviteStructLvl)
//		val details = sb.toString
//		previousCurrencyMap.put(gems, previousGems)
//		currentCurrencyMap.put(gems, aUser.gems)
//		changeReasonsMap.put(gems, reasonForChange)
//		detailsMap.put(gems, details)
//		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, changeMap,
//			previousCurrencyMap, currentCurrencyMap, changeReasonsMap, detailsMap)
//	}
//
//	private def deleteInvitesForSlotsAfterPurchase(String userUuid, Map<String, Integer> money)
//	{
//		if (money.empty)
//		{
//			return;
//		}
//		val num = DeleteUtils::get.deleteUnredeemedUserFacebookInvitesForUser(userUuid)
//		LOG.info(
//			'num invites deleted after buying slot. userUuid=' + userUuid + ' numDeleted=' + num)
//	}
//}
