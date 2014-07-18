package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsRequestProto.FacebookInviteStructure
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto.InviteFbFriendsForSlotsStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.InviteFbFriendsForSlotsRequestEvent
import com.lvl6.mobsters.events.response.InviteFbFriendsForSlotsResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class InviteFbFriendsForSlotsController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(InviteFbFriendsForSlotsController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new InviteFbFriendsForSlotsRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as InviteFbFriendsForSlotsRequestEvent)).
			inviteFbFriendsForSlotsRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.minUserProto.userUuid
		val invites = reqProto.invitesList
		val fbIdsToUserStructUuids = new HashMap<String, Integer>()
		val fbUuidsToUserStructFbLvl = new HashMap<String, Integer>()
		val fbUuidsOfFriends = demultiplexFacebookInviteStructure(invites,
			fbIdsToUserStructUuids, fbUuidsToUserStructFbLvl)
		val curTime = new Timestamp((new Date()).time)
		val resBuilder = InviteFbFriendsForSlotsResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = InviteFbFriendsForSlotsStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val aUser = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			val List<Integer> specificUuids = null
			val filterByAccepted = false
			val isAccepted = false
			val filterByRedeemed = false
			val isRedeemed = false
			val idsToInvites = UserFacebookInviteForSlotRetrieveUtils::
				getSpecificOrAllInvitesForInviter(userUuid, specificUuids, filterByAccepted,
					isAccepted, filterByRedeemed, isRedeemed)
			val newFacebookIdsToInvite = new ArrayList<String>()
			val legit = checkLegit(resBuilder, userUuid, aUser, fbUuidsOfFriends, idsToInvites,
				newFacebookIdsToInvite)
			var successful = false
			val inviteUuids = new ArrayList<Integer>()
			if (legit)
			{
				successful = writeChangesToDb(aUser, newFacebookIdsToInvite, curTime,
					fbIdsToUserStructUuids, fbUuidsToUserStructFbLvl, inviteUuids)
			}
			if (successful)
			{
				val newUuidsToInvites = UserFacebookInviteForSlotRetrieveUtils::
					getInviteForId(inviteUuids)
				for (id : newUuidsToInvites.keySet)
				{
					val invite = newUuidsToInvites.get(id)
					val inviteProto = CreateInfoProtoUtils::
						createUserFacebookInviteForSlotProtoFromInvite(invite, aUser,
							senderProto)
					resBuilder.addInvitesNew(inviteProto)
				}
				resBuilder.status = InviteFbFriendsForSlotsStatus.SUCCESS
			}
			val resEvent = new InviteFbFriendsForSlotsResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.inviteFbFriendsForSlotsResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in InviteFbFriendsForSlotsController.processRequestEvent',
					e)
			}
			if (successful)
			{
				val recipientUserUuids = RetrieveUtils::userRetrieveUtils.
					getUserUuidsForFacebookIds(newFacebookUuidsToInvite)
				val responseProto = resBuilder.build
				for (recipientUserId : recipientUserUuids)
				{
					val newResEvent = new InviteFbFriendsForSlotsResponseEvent(recipientUserId)
					newResEvent.tag = 0
					newResEvent.inviteFbFriendsForSlotsResponseProto = responseProto
					LOG.info('Writing event: ' + newResEvent)
					try
					{
						eventWriter.writeEvent(newResEvent)
					}
					catch (Throwable e)
					{
						LOG.error(
							'fatal exception in InviteFbFriendsForSlotsController.processRequestEvent',
							e)
					}
				}
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in InviteFbFriendsForSlotsController processEvent', e)
			try
			{
				resBuilder.status = InviteFbFriendsForSlotsStatus.FAIL_OTHER
				val resEvent = new InviteFbFriendsForSlotsResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.inviteFbFriendsForSlotsResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in InviteFbFriendsForSlotsController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in InviteFbFriendsForSlotsController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def demultiplexFacebookInviteStructure(List<FacebookInviteStructure> invites,
		Map<String, Integer> fbIdsToUserStructUuids,
		Map<String, Integer> fbUuidsToUserStructFbLvl)
	{
		val retVal = new ArrayList<String>()
		for (fis : invites)
		{
			val fbId = fis.fbFriendId
			val userStructUuid = fis.userStructUuid
			val userStructFbLvl = fis.userStructFbLvl
			retVal.add(fbId)
			fbIdsToUserStructUuids.put(fbId, userStructId)
			fbUuidsToUserStructFbLvl.put(fbId, userStructFbLvl)
		}
		retVal
	}

	private def checkLegit(Builder resBuilder, String userUuid, User u,
		List<String> fbUuidsOfFriends, Map<Integer, UserFacebookInviteForSlot> idsToInvites,
		List<String> newFacebookIdsToInvite)
	{
		if (null === u)
		{
			LOG.error('user is null. no user exists with id=' + userUuid)
			return false;
		}
		val newFacebookIdsToInviteTemp = getNewInvites(fbUuidsOfFriends, idsToInvites)
		newFacebookIdsToInvite.addAll(newFacebookUuidsToInviteTemp)
		true
	}

	private def getNewInvites(List<String> fbUuidsOfFriends,
		Map<Integer, UserFacebookInviteForSlot> idsToInvites)
	{
		val inviteUuidsOfDuplicateInvites = new ArrayList<Integer>()
		val processedRecipientUuids = new HashSet<String>()
		for (inviteId : idsToInvites.keySet)
		{
			val invite = idsToInvites.get(inviteId)
			val recipientId = invite.recipientFacebookId
			if (processedRecipientUuids.contains(recipientId))
			{
				inviteUuidsOfDuplicateInvites.add(inviteId)
			}
			else
			{
				processedRecipientUuids.add(recipientId)
			}
		}
		if (!inviteUuidsOfDuplicateInvites.empty)
		{
			val num = DeleteUtils::get.
				deleteUserFacebookInvitesForSlots(inviteUuidsOfDuplicateInvites)
			LOG.warn('num duplicate invites deleted: ' + num)
		}
		val newFacebookIdsToInvite = new ArrayList<String>()
		for (prospectiveRecipientId : fbUuidsOfFriends)
		{
			if (!processedRecipientUuids.contains(prospectiveRecipientId))
			{
				newFacebookIdsToInvite.add(prospectiveRecipientId)
			}
		}
		newFacebookIdsToInvite
	}

	private def writeChangesToDb(User aUser, List<String> newFacebookIdsToInvite,
		Timestamp curTime, Map<String, Integer> fbIdsToUserStructUuids,
		Map<String, Integer> fbUuidsToUserStructsFbLvl, List<Integer> inviteUuids)
	{
		if (newFacebookIdsToInvite.empty)
		{
			return true;
		}
		val userUuid = aUser.id
		val inviteUuidsTemp = InsertUtils::get.
			insertIntoUserFbInviteForSlot(userUuid, newFacebookIdsToInvite, curTime,
				fbIdsToUserStructUuids, fbUuidsToUserStructsFbLvl)
		val numInserted = inviteUuidsTemp.size
		val expectedNum = newFacebookIdsToInvite.size
		if (numInserted !== expectedNum)
		{
			LOG.error(
				'problem with updating user monster inventory slots and diamonds.' +
					' num inserted: ' + numInserted + '	 should have been: ' + expectedNum)
		}
		LOG.info('num inserted: ' + numInserted + '	 ids=' + inviteUuidsTemp)
		inviteUuids.addAll(inviteUuidsTemp)
		true
	}
}
