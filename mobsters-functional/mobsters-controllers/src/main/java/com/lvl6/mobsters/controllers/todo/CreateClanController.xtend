package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.Clan
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto
import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto.CreateClanStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.CreateClanRequestEvent
import com.lvl6.mobsters.events.response.CreateClanResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.Date
import java.util.HashMap
import java.util.Map
import javax.management.Notification
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class CreateClanController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(CreateClanController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new CreateClanRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_CREATE_CLAN_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as CreateClanRequestEvent)).createClanRequestProto
		LOG.info('reqProto=' + reqProto)
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val clanName = reqProto.name
		val tag = reqProto.tag
		val requestToJoinRequired = reqProto.requestToJoinClanRequired
		val description = reqProto.description
		val clanIconId = reqProto.clanIconUuid
		val gemsSpent = reqProto.gemsSpent
		val cashChange = reqProto.cashChange
		val resBuilder = CreateClanResponseProto::newBuilder
		resBuilder.status = CreateClanStatus.FAIL_OTHER
		resBuilder.sender = senderProto
		svcTxManager.beginTransaction
		try
		{
			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
			val createTime = new Timestamp(new Date().time)
			val legitCreate = checkLegitCreate(resBuilder, user, clanName, tag, gemsSpent,
				cashChange)
			var success = false
			val previousCurrency = new HashMap<String, Integer>()
			val currencyChange = new HashMap<String, Integer>()
			val createdClan = new Clan()
			if (legitCreate)
			{
				previousCurrency.put(MiscMethods::gems, user.gems)
				previousCurrency.put(MiscMethods::cash, user.cash)
				success = writeChangesToDB(user, clanName, tag, requestToJoinRequired,
					description, clanIconId, createTime, createdClan, gemsSpent, cashChange,
					currencyChange)
			}
			if (success)
			{
				resBuilder.clanInfo = CreateInfoProtoUtils::
					createMinimumClanProtoFromClan(createdClan)
				resBuilder.status = CreateClanStatus.SUCCESS
			}
			val resEvent = new CreateClanResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.createClanResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in CreateClanController.processRequestEvent', e)
			}
			if (success)
			{
				val resEventUpdate = MiscMethods::
					createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null)
				resEventUpdate.tag = event.tag
				LOG.info('Writing event: ' + resEventUpdate)
				try
				{
					eventWriter.writeEvent(resEventUpdate)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in CreateClanController.processRequestEvent', e)
				}
				sendGeneralNotification(user.name, clanName)
				writeToUserCurrencyHistory(user, createdClan, createTime, currencyChange,
					previousCurrency)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in CreateClan processEvent', e)
			try
			{
				resBuilder.status = CreateClanStatus.FAIL_OTHER
				val resEvent = new CreateClanResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.createClanResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in CreateClanController.processRequestEvent', e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in CreateClan processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegitCreate(Builder resBuilder, User user, String clanName, String tag,
		int gemsSpent, int cashChange)
	{
		if ((user === null) || (clanName === null) || (clanName.length <= 0) || (tag === null) ||
			(tag.length <= 0))
		{
			resBuilder.status = CreateClanStatus.FAIL_OTHER
			LOG.error('user is null')
			return false;
		}
		if (clanName.length > ControllerConstants::CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_NAME)
		{
			resBuilder.status = CreateClanStatus.FAIL_OTHER
			LOG.error(
				'clan name ' + clanName + ' is more than ' +
					ControllerConstants::CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_NAME +
					' characters')
			return false;
		}
		if (tag.length > ControllerConstants::CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_TAG)
		{
			resBuilder.status = CreateClanStatus.FAIL_INVALID_TAG_LENGTH
			LOG.error(
				'clan tag ' + tag + ' is more than ' +
					ControllerConstants::CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_TAG +
					' characters')
			return false;
		}
		if (user.clanId > 0)
		{
			resBuilder.status = CreateClanStatus.FAIL_ALREADY_IN_CLAN
			LOG.error('user already in clan with id ' + user.clanId)
			return false;
		}
		val clan = ClanRetrieveUtils::getClanWithNameOrTag(clanName, tag)
		if (clan !== null)
		{
			if (clan.name.equalsIgnoreCase(clanName))
			{
				resBuilder.status = CreateClanStatus.FAIL_NAME_TAKEN
				LOG.error('clan name already taken with name ' + clanName)
				return false;
			}
			if (clan.tag.equalsIgnoreCase(tag))
			{
				resBuilder.status = CreateClanStatus.FAIL_TAG_TAKEN
				LOG.error('clan tag already taken with tag ' + tag)
				return false;
			}
		}
		if (0 === gemsSpent)
		{
			if (!hasEnoughCash(resBuilder, user, cashChange))
			{
				return false;
			}
		}
		if (!hasEnoughGems(resBuilder, user, gemsSpent))
		{
			return false;
		}
		resBuilder.status = CreateClanStatus.SUCCESS
		true
	}

	private def hasEnoughCash(Builder resBuilder, User u, int cashSpent)
	{
		val userCash = u.cash
		if (userCash < cashSpent)
		{
			LOG.error(
				'user error: user does not have enough cash. userCash=' + userCash +
					'	 cashSpent=' + cashSpent)
			resBuilder.status = CreateClanStatus.FAIL_INSUFFICIENT_FUNDS
			return false;
		}
		true
	}

	private def hasEnoughGems(Builder resBuilder, User u, int gemsSpent)
	{
		val userGems = u.gems
		if (userGems < gemsSpent)
		{
			LOG.error(
				'user error: user does not have enough gems. userGems=' + userGems +
					'	 gemsSpent=' + gemsSpent)
			resBuilder.status = CreateClanStatus.FAIL_INSUFFICIENT_FUNDS
			return false;
		}
		true
	}

	private def sendGeneralNotification(String userName, String clanName)
	{
		val createClanNotification = new Notification()
		createClanNotification.setAsClanCreated(userName, clanName)
		MiscMethods::writeGlobalNotification(createClanNotification, server)
	}

	private def writeChangesToDB(User user, String name, String tag,
		boolean requestToJoinRequired, String description, int clanIconId,
		Timestamp createTime, Clan createdClan, int gemsSpent, int cashChange,
		Map<String, Integer> money)
	{
		if ((null === description) || description.empty)
		{
			description = 'Welcome to ' + name + '!'
		}
		val clanId = InsertUtils::get.insertClan(name, createTime, description, tag,
			requestToJoinRequired, clanIconId)
		if (clanId <= 0)
		{
			return false;
		}
		else
		{
			setClan(createdClan, clanId, name, createTime, description, tag,
				requestToJoinRequired, clanIconId)
			LOG.info('clan=' + createdClan)
		}
		val gemChange = -1 * Math::abs(gemsSpent)
		cashChange = -1 * Math::abs(cashChange)
		if (!user.updateGemsCashClan(gemChange, cashChange, clanId))
		{
			LOG.error(
				'problem with decreasing user gems, cash for creating clan. gemChange=' +
					gemChange + '	 cashChange=' + cashChange)
		}
		else
		{
			if (0 !== gemsSpent)
			{
				money.put(MiscMethods::gems, gemsSpent)
			}
			if (0 !== cashChange)
			{
				money.put(MiscMethods::cash, cashChange)
			}
		}
		if (!InsertUtils::get.insertUserClan(user.id, clanId, UserClanStatus.LEADER.name,
			createTime))
		{
			LOG.error(
				'problem with inserting user clan data for user ' + user + ', and clan id ' +
					clanId)
		}
		DeleteUtils::get.deleteUserClansForUserExceptSpecificClan(user.id, clanId)
		true
	}

	private def setClan(Clan createdClan, int clanId, String name, Timestamp createTime,
		String description, String tag, boolean requestToJoinRequired, int clanIconId)
	{
		createdClan.id = clanId
		createdClan.name = name
		createdClan.createTime = createTime
		createdClan.description = description
		createdClan.tag = tag
		createdClan.requestToJoinRequired = requestToJoinRequired
		createdClan.clanIconId = clanIconId
	}

	private def writeToUserCurrencyHistory(User aUser, Clan clan, Timestamp createTime,
		Map<String, Integer> currencyChange, Map<String, Integer> previousCurrency)
	{
		if (currencyChange.empty)
		{
			return;
		}
		val reason = ControllerConstants.UCHRFC__CREATE_CLAN
		val detailsSb = new StringBuilder()
		detailsSb.append('clanId=')
		detailsSb.append(clan.id)
		detailsSb.append(' clanName=')
		detailsSb.append(clan.name)
		val details = detailsSb.toString
		val userUuid = aUser.id
		val currentCurrency = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		val gems = MiscMethods::gems
		val cash = MiscMethods::cash
		currentCurrency.put(gems, aUser.gems)
		currentCurrency.put(cash, aUser.cash)
		reasonsForChanges.put(gems, reason)
		reasonsForChanges.put(cash, reason)
		detailsMap.put(gems, details)
		detailsMap.put(cash, details)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, createTime, currencyChange,
			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
	}
}
