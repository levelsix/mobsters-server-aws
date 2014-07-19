//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto
//import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto.AcceptAndRejectFbInviteForSlotsStatus
//import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto.Builder
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.AcceptAndRejectFbInviteForSlotsRequestEvent
//import com.lvl6.mobsters.events.response.AcceptAndRejectFbInviteForSlotsResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithFacebookId
//import com.lvl6.mobsters.server.EventController
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Collection
//import java.util.Date
//import java.util.HashMap
//import java.util.List
//import java.util.Map
//import java.util.Set
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class AcceptAndRejectFbInvitesForSlotsController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(AcceptAndRejectFbInvitesForSlotsController))
//	
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
//		return new AcceptAndRejectFbInviteForSlotsRequestEvent()
//	}
//
//	override getEventType()
//	{
//		return EventProtocolRequest.C_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as AcceptAndRejectFbInviteForSlotsRequestEvent)).
//			acceptAndRejectFbInviteForSlotsRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProto = reqProto.sender
//		val sender = senderProto.minUserProto
//		val userUuid = sender.userUuid
//		val userFacebookId = senderProto.facebookId
//		var acceptedInviteUuids = reqProto.acceptedInviteUuidsList
//		if (null === acceptedInviteUuids)
//		{
//			acceptedInviteUuids = new ArrayList<String>()
//		}
//		else
//		{
//			acceptedInviteUuids = new ArrayList<String>(acceptedInviteUuids)
//		}
//		var rejectedInviteUuids = reqProto.rejectedInviteUuidsList
//		if (null === rejectedInviteUuids)
//		{
//			rejectedInviteUuids = new ArrayList<String>()
//		}
//		else
//		{
//			rejectedInviteUuids = new ArrayList<String>(rejectedInviteUuids)
//		}
//		val acceptTime = new Timestamp((new Date()).time)
//		val resBuilder = AcceptAndRejectFbInviteForSlotsResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = AcceptAndRejectFbInviteForSlotsStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val idsToInvitesInDb = new HashMap<Integer, UserFacebookInviteForSlot>()
//			val legit = checkLegit(resBuilder, userUuid, userFacebookId, acceptedInviteUuids,
//				rejectedInviteUuids, idsToInvitesInDb)
//			var successful = false
//			if (legit)
//			{
//				successful = writeChangesToDb(userUuid, userFacebookId, acceptedInviteUuids,
//					rejectedInviteUuids, idsToInvitesInDb, acceptTime)
//			}
//			if (successful)
//			{
//				val invites = idsToInvitesInDb.values
//				val userUuids = getInviterUuids(invites)
//				val idsToInviters = RetrieveUtils::userRetrieveUtils.getUsersByUuids(userUuids)
//				for (invite : invites)
//				{
//					invite.timeAccepted = acceptTime
//					val inviterUuid = invite.inviterUserUuid
//					val inviter = idsToInviters.get(inviterUuid)
//					val MinimumUserProtoWithFacebookId inviterProto = null
//					val inviteProto = CreateInfoProtoUtils::
//						createUserFacebookInviteForSlotProtoFromInvite(invite, inviter,
//							inviterProto)
//					resBuilder.addAcceptedInvites(inviteProto)
//				}
//				resBuilder.status = AcceptAndRejectFbInviteForSlotsStatus.SUCCESS
//			}
//			val resEvent = new AcceptAndRejectFbInviteForSlotsResponseEvent(userUuid)
//			resEvent.tag = event.tag
//			resEvent.acceptAndRejectFbInviteForSlotsResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error(
//					'fatal exception in AcceptAndRejectFbInvitesForSlotsController.processRequestEvent',
//					e)
//			}
//			if (successful)
//			{
//				val responseProto = resBuilder.build
//				for (inviteId : acceptedInviteUuids)
//				{
//					val invite = idsToInvitesInDb.get(inviteId)
//					val inviterUuid = invite.inviterUserUuid
//					val newResEvent = new AcceptAndRejectFbInviteForSlotsResponseEvent(inviterId)
//					newResEvent.tag = 0
//					newResEvent.acceptAndRejectFbInviteForSlotsResponseProto = responseProto
//					LOG.info('Writing event: ' + newResEvent)
//					try
//					{
//						eventWriter.writeEvent(newResEvent)
//					}
//					catch (Throwable e)
//					{
//						LOG.error(
//							'fatal exception in AcceptAndRejectFbInvitesForSlotsController.processRequestEvent',
//							e)
//					}
//				}
//			}
//		}
//		catch (Throwable e)
//		{
//			LOG.error('exception in AcceptAndRejectFbInviteForSlotsController processEvent', e)
//			try
//			{
//				resBuilder.status = AcceptAndRejectFbInviteForSlotsStatus.FAIL_OTHER
//				val resEvent = new AcceptAndRejectFbInviteForSlotsResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.acceptAndRejectFbInviteForSlotsResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e2)
//				{
//					LOG.error(
//						'fatal exception in AcceptAndRejectFbInvitesForSlotsController.processRequestEvent',
//						e2)
//				}
//			}
//			catch (Throwable e2)
//			{
//				LOG.error('exception2 in AcceptAndRejectFbInviteForSlotsController processEvent',
//					e)
//			}
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegit(Builder resBuilder, String userUuid, String userFacebookId,
//		List<String> acceptedInviteUuids, List<String> rejectedInviteUuids,
//		Map<String, UserFacebookInviteForSlot> idsToInvites)
//	{
//		if ((null === userFacebookId) || userFacebookId.empty)
//		{
//			LOG.error(
//				'facebookId is null. id=' + userFacebookId + '	 acceptedInvitesUuids=' +
//					acceptedInviteUuids + '	 rejectedInviteUuids=' + rejectedInviteUuids)
//			return false;
//		}
//		val inviteUuids = new ArrayList<String>(acceptedInviteUuids)
//		inviteUuids.addAll(rejectedInviteUuids)
//		val filterByAccepted = true
//		var isAccepted = false
//		val filterByRedeemed = true
//		val isRedeemed = false
//		val idsToInvitesInDb = UserFacebookInviteForSlotRetrieveUtils::
//			getSpecificOrAllInvitesForRecipient(userFacebookId, inviteUuids, filterByAccepted,
//				isAccepted, filterByRedeemed, isRedeemed)
//		val validUuids = idsToInvitesInDb.keySet
//		LOG.info('acceptedInviteUuids before filter: ' + acceptedInviteUuids)
//		acceptedInviteUuids.retainAll(validUuids)
//		LOG.info('acceptedInviteUuids after filter: ' + acceptedInviteUuids)
//		rejectedInviteUuids.retainAll(validUuids)
//		val acceptedInviterUuidsToInviteUuids = getInviterUserUuids(acceptedInviteUuids,
//			idsToInvitesInDb)
//		isAccepted = true
//		val redeemedInviterUuids = UserFacebookInviteForSlotRetrieveUtils::
//			getUniqueInviterUserUuidsForRequesterId(userFacebookId, filterByAccepted, isAccepted)
//		LOG.info('acceptedInviteUuids before inviter used check: ' + acceptedInviteUuids)
//		retainInvitesFromUnusedInviters(redeemedInviterUuids, acceptedInviterUuidsToInviteUuids,
//			acceptedInviteUuids, rejectedInviteUuids)
//		LOG.info('acceptedInviteUuids after inviter used check: ' + acceptedInviteUuids)
//		idsToInvites.putAll(idsToInvitesInDb)
//		return true
//	}	
//
//	private def retainIfInExistingUuids(Set<String> existingInts, List<String> someUuids)
//	{
//		val lastIndex = someUuids.size - 1
//		{
//			var index = lastIndex
//
//			while (index >= 0)
//			{
//				val someInt = someUuids.get(index)
//				if (!existingInts.contains(someInt))
//				{
//					someUuids.remove(index)
//				}
//				index--
//			}
//		}
//	}
//
//	private def getInviterUserUuids(List<String> inviteUuids,
//		Map<String, UserFacebookInviteForSlot> uuidsToInvites)
//	{
//		val inviterUserIdsToInviteUuids = new HashMap<String, String>()
//		for (inviteUuid : inviteUuids)
//		{
//			val invite = uuidsToInvites.get(inviteUuid)
//			val inviterUserUuid = invite.inviterUserUuid
//			inviterUserIdsToInviteUuids.put(inviterUserUuid, inviteUuid)
//		}
//		return inviterUserIdsToInviteUuids
//	}
//
//	private def retainInvitesFromUnusedInviters(Set<String> recordedInviterUuids,
//		Map<String, Integer> acceptedInviterUuidsToInviteUuids,
//		List<String> acceptedInviteUuids, List<String> rejectedInviteUuids)
//	{
//		val invalidInviteUuidsToUserUuids = new HashMap<String, String>()
//		for (potentialNewInviterUuid : acceptedInviterUuidsToInviteUuids.keySet)
//		{
//			if (recordedInviterUuids.contains(potentialNewInviterUuid))
//			{
//				val inviteUuid = acceptedInviterUuidsToInviteUuids.get(potentialNewInviterUuid)
//				invalidInviteUuidsToUserUuids.put(inviteUuid, potentialNewInviterUuid)
//			}
//		}
//		val invalidInviteUuids = invalidInviteUuidsToUserUuids.keySet
//		if (invalidInviteUuids.empty)
//		{
//			return;
//		}
//		LOG.warn(
//			'user tried accepting invites from users he has already accepted.' +
//				'invalidInviteIdsToUserUuids=' + invalidInviteUuidsToUserUuids)
//		LOG.warn('before: rejectedInviteUuids=' + acceptedInviteUuids)
//		LOG.warn('before: acceptedInviteUuids=' + acceptedInviteUuids)
//		val lastIndex = acceptedInviteUuids.size - 1
//		{
//			var index = lastIndex
//
//			while (index >= 0)
//			{
//				val acceptedInviteUuid = acceptedInviteUuids.get(index)
//				if (invalidInviteUuids.contains(acceptedInviteUuid))
//				{
//					acceptedInviteUuids.remove(index)
//					rejectedInviteUuids.add(acceptedInviteUuid)
//				}
//				index--
//			}
//		}
//		LOG.warn('after: acceptedInviteUuids=' + acceptedInviteUuids)
//		LOG.warn('after: rejectedInviteUuids=' + rejectedInviteUuids)
//	}
//
//	private def writeChangesToDb(String userUuid, String userFacebookId,
//		List<Integer> acceptedInviteUuids, List<Integer> rejectedInviteUuids,
//		Map<Integer, UserFacebookInviteForSlot> idsToInvitesInDb, Timestamp acceptTime)
//	{
//		LOG.info(
//			'idsToInvitesInDb=' + idsToInvitesInDb + '	 acceptedInviteUuids=' +
//				acceptedInviteUuids + '	 rejectedInviteUuids=' + rejectedInviteUuids)
//		if (!acceptedInviteUuids.empty)
//		{
//			val num = UpdateUtils::get.
//				updateUserFacebookInviteForSlotAcceptTime(userFacebookId, acceptedInviteUuids,
//					acceptTime)
//			LOG.info(
//				'							 num acceptedInviteUuids updated: ' + num + '	 invites=' +
//					acceptedInviteUuids)
//		}
//		if (!rejectedInviteUuids.empty)
//		{
//			val num = DeleteUtils::get.deleteUserFacebookInvitesForSlots(rejectedInviteUuids)
//			LOG.info(
//				'num rejectedInviteUuids deleted: ' + num + '	 invites=' + rejectedInviteUuids)
//		}
//		return true
//	}
//
//	private def getInviterUuids(Collection<UserFacebookInviteForSlot> invites)
//	{
//		val inviterUuids = new ArrayList<String>()
//		for (invite : invites)
//		{
//			val inviterUuid = invite.inviterUserUuid
//			inviterUuids.add(inviterUuid)
//		}
//		return inviterUuids
//	}
//}
