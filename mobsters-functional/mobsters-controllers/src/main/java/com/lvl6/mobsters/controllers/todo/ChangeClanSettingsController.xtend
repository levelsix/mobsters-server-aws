//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.Clan
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto
//import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto.ChangeClanSettingsStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.ChangeClanSettingsRequestEvent
//import com.lvl6.mobsters.events.response.ChangeClanSettingsResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
//import com.lvl6.mobsters.server.ControllerConstants
//import com.lvl6.mobsters.server.EventController
//import java.util.ArrayList
//import java.util.Collections
//import java.util.HashSet
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class ChangeClanSettingsController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(ChangeClanSettingsController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var ClanIconRetrieveUtils clanIconRetrieveUtils
//
//	new()
//	{
//		numAllocatedThreads = 4
//	}
//
//	override createRequestEvent()
//	{
//		new ChangeClanSettingsRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_CHANGE_CLAN_SETTINGS_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as ChangeClanSettingsRequestEvent)).
//			changeClanSettingsRequestProto
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val isChangeDescription = reqProto.isChangeDescription
//		val description = reqProto.descriptionNow
//		val isChangeJoinType = reqProto.isChangeJoinType
//		val requestToJoinRequired = reqProto.requestToJoinRequired
//		val isChangeIcon = reqProto.isChangeIcon
//		val iconId = reqProto.iconUuid
//		val resBuilder = ChangeClanSettingsResponseProto::newBuilder
//		resBuilder.status = ChangeClanSettingsStatus.FAIL_OTHER
//		resBuilder.sender = senderProto
//		var clanId = 0
//		if (senderProto.clan && (null !== senderProto.clan))
//		{
//			clanId = senderProto.clan.clanId
//		}
//		var lockedClan = false
//		if (0 !== clanId)
//		{
//			lockedClan = locker.lockClan(clanId)
//		}
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			val clan = ClanRetrieveUtils::getClanWithId(user.clanId)
//			val legitChange = checkLegitChange(resBuilder, lockedClan, userUuid, user, clanId,
//				clan)
//			if (legitChange)
//			{
//				writeChangesToDB(resBuilder, clanId, clan, isChangeDescription, description,
//					isChangeJoinType, requestToJoinRequired, isChangeIcon, iconId)
//				setResponseBuilderStuff(resBuilder, clanId, clan)
//			}
//			val resEvent = new ChangeClanSettingsResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.changeClanSettingsResponseProto = resBuilder.build
//			if (ChangeClanSettingsStatus::SUCCESS != resBuilder.status)
//			{
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in ChangeClanSettingsController.processRequestEvent', e)
//				}
//			}
//			else
//			{
//				server.writeClanEvent(resEvent, clan.id)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in ChangeClanSettings processEvent', e)
//			try
//			{
//				resBuilder.status = ChangeClanSettingsStatus.FAIL_OTHER
//				val resEvent = new ChangeClanSettingsResponseEvent(userUuid)
//				resEvent.tag = event.tag
//				resEvent.changeClanSettingsResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error(
//						'fatal exception in ChangeClanSettingsController.processRequestEvent', e)
//				}
//			}
//			catch (Exception e2)
//			{
//				LOG.error('exception2 in ChangeClanSettings processEvent', e)
//			}
//		}
//		finally
//		{
//			if ((0 !== clanId) && lockedClan)
//			{
//				locker.unlockClan(clanId)
//			}
//		}
//	}
//
//	private def checkLegitChange(Builder resBuilder, boolean lockedClan, String userUuid,
//		User user, int clanId, Clan clan)
//	{
//		if (!lockedClan)
//		{
//			LOG.error("couldn't obtain clan lock")
//			return false;
//		}
//		if ((user === null) || (clan === null))
//		{
//			resBuilder.status = ChangeClanSettingsStatus.FAIL_OTHER
//			LOG.error(
//				'userUuid is ' + userUuid + ', user is ' + user + '	 clanId is ' + clanId +
//					', clan is ' + clan)
//			return false;
//		}
//		if (user.clanId <= 0)
//		{
//			resBuilder.status = ChangeClanSettingsStatus.FAIL_NOT_IN_CLAN
//			LOG.error('user not in clan')
//			return false;
//		}
//		val statuses = new ArrayList<String>()
//		statuses.add(UserClanStatus.LEADER.name)
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
//		val userUuids = RetrieveUtils::userClanRetrieveUtils.
//			getUserUuidsWithStatuses(clanId, statuses)
//		val uniqUserUuids = new HashSet<Integer>()
//		if ((null !== userUuids) && !userUuids.empty)
//		{
//			uniqUserUuids.addAll(userUuids)
//		}
//		if (!uniqUserUuids.contains(userUuid))
//		{
//			resBuilder.status = ChangeClanSettingsStatus.FAIL_NOT_AUTHORIZED
//			LOG.error("clan member can't change clan description member=" + user)
//			return false;
//		}
//		resBuilder.status = ChangeClanSettingsStatus.SUCCESS
//		true
//	}
//
//	private def writeChangesToDB(Builder resBuilder, int clanId, Clan clan,
//		boolean isChangeDescription, String description, boolean isChangeJoinType,
//		boolean requestToJoinRequired, boolean isChangeIcon, int iconId)
//	{
//		if (isChangeDescription)
//		{
//			if (description.length >
//				ControllerConstants::CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_DESCRIPTION)
//			{
//				resBuilder.status = ChangeClanSettingsStatus.FAIL_OTHER
//				LOG.warn(
//					'description is ' + description + ', and length of that is ' +
//						description.length + ', max size is ' +
//						ControllerConstants::CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_DESCRIPTION)
//			}
//			else
//			{
//				clan.description = description
//			}
//		}
//		if (isChangeJoinType)
//		{
//			clan.requestToJoinRequired = requestToJoinRequired
//		}
//		if (isChangeIcon)
//		{
//			val ci = ClanIconRetrieveUtils::getClanIconForId(iconId)
//			if (null === ci)
//			{
//				resBuilder.status = ChangeClanSettingsStatus.FAIL_OTHER
//				LOG.warn('no clan icon with id=' + iconId)
//			}
//			else
//			{
//				clan.clanIconId = iconId
//			}
//		}
//		val numUpdated = UpdateUtils::get.updateClan(clanId, isChangeDescription, description,
//			isChangeJoinType, requestToJoinRequired, isChangeIcon, iconId)
//		LOG.info('numUpdated (should be 1)=' + numUpdated)
//	}
//
//	private def setResponseBuilderStuff(Builder resBuilder, int clanId, Clan clan)
//	{
//		val clanIdList = Collections::singletonList(clanId)
//		val statuses = new ArrayList<String>()
//		statuses.add(UserClanStatus.LEADER.name)
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name)
//		statuses.add(UserClanStatus.CAPTAIN.name)
//		statuses.add(UserClanStatus.MEMBER.name)
//		val clanIdToSize = RetrieveUtils::userClanRetrieveUtils.
//			getClanSizeForClanUuidsAndStatuses(clanIdList, statuses)
//		resBuilder.minClan = CreateInfoProtoUtils::createMinimumClanProtoFromClan(clan)
//		val size = clanIdToSize.get(clanId)
//		resBuilder.fullClan = CreateInfoProtoUtils::createFullClanProtoWithClanSize(clan, size)
//	}
//
//	def getClanIconRetrieveUtils()
//	{
//		clanIconRetrieveUtils
//	}
//
//	def setClanIconRetrieveUtils(ClanIconRetrieveUtils clanIconRetrieveUtils)
//	{
//		this.clanIconRetrieveUtils = clanIconRetrieveUtils
//	}
//}
