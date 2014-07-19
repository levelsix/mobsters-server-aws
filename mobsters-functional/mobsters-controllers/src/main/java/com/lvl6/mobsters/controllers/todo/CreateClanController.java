//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.management.Notification;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.Clan;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanRequestProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto.CreateClanStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.CreateClanRequestEvent;
//import com.lvl6.mobsters.events.response.CreateClanResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class CreateClanController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(CreateClanController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public CreateClanController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new CreateClanRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_CREATE_CLAN_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final CreateClanRequestProto reqProto =
//		    ((CreateClanRequestEvent) event).getCreateClanRequestProto();
//		LOG.info("reqProto="
//		    + reqProto);
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final String clanName = reqProto.getName();
//		final String tag = reqProto.getTag();
//		final boolean requestToJoinRequired = reqProto.getRequestToJoinClanRequired();
//		final String description = reqProto.getDescription();
//		final int clanIconId = reqProto.getClanIconUuid();
//		final int gemsSpent = reqProto.getGemsSpent();
//		final int cashChange = reqProto.getCashChange();
//
//		final CreateClanResponseProto.Builder resBuilder = CreateClanResponseProto.newBuilder();
//		resBuilder.setStatus(CreateClanStatus.FAIL_OTHER);
//		resBuilder.setSender(senderProto);
//
//		svcTxManager.beginTransaction();
//		try {
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(senderProto.getUserUuid());
//			final Timestamp createTime = new Timestamp(new Date().getTime());
//
//			final boolean legitCreate =
//			    checkLegitCreate(resBuilder, user, clanName, tag, gemsSpent, cashChange);
//
//			boolean success = false;
//			final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
//			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
//			final Clan createdClan = new Clan();
//			if (legitCreate) {
//				previousCurrency.put(MiscMethods.gems, user.getGems());
//				previousCurrency.put(MiscMethods.cash, user.getCash());
//				success =
//				    writeChangesToDB(user, clanName, tag, requestToJoinRequired, description,
//				        clanIconId, createTime, createdClan, gemsSpent, cashChange,
//				        currencyChange);
//			}
//
//			if (success) {
//				resBuilder.setClanInfo(CreateInfoProtoUtils.createMinimumClanProtoFromClan(createdClan));
//				resBuilder.setStatus(CreateClanStatus.SUCCESS);
//			}
//
//			final CreateClanResponseEvent resEvent =
//			    new CreateClanResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setCreateClanResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in CreateClanController.processRequestEvent", e);
//			}
//
//			if (success) {
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error("fatal exception in CreateClanController.processRequestEvent", e);
//				}
//
//				sendGeneralNotification(user.getName(), clanName);
//
//				writeToUserCurrencyHistory(user, createdClan, createTime, currencyChange,
//				    previousCurrency);
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in CreateClan processEvent", e);
//			try {
//				resBuilder.setStatus(CreateClanStatus.FAIL_OTHER);
//				final CreateClanResponseEvent resEvent = new CreateClanResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setCreateClanResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error("fatal exception in CreateClanController.processRequestEvent", e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in CreateClan processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegitCreate( final Builder resBuilder, final User user,
//	    final String clanName, final String tag, final int gemsSpent, final int cashChange )
//	{
//		if ((user == null)
//		    || (clanName == null)
//		    || (clanName.length() <= 0)
//		    || (tag == null)
//		    || (tag.length() <= 0)) {
//			resBuilder.setStatus(CreateClanStatus.FAIL_OTHER);
//			LOG.error("user is null");
//			return false;
//		}
//		// if (user.getCash() <
//		// ControllerConstants.CREATE_CLAN__COIN_PRICE_TO_CREATE_CLAN) {
//		// resBuilder.setStatus(CreateClanStatus.FAIL_NOT_ENOUGH_CASH);
//		// LOG.error("user only has " + user.getCash() + ", needs " +
//		// ControllerConstants.CREATE_CLAN__COIN_PRICE_TO_CREATE_CLAN);
//		// return false;
//		// }
//		if (clanName.length() > ControllerConstants.CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_NAME) {
//			resBuilder.setStatus(CreateClanStatus.FAIL_OTHER);
//			LOG.error("clan name "
//			    + clanName
//			    + " is more than "
//			    + ControllerConstants.CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_NAME
//			    + " characters");
//			return false;
//		}
//
//		if (tag.length() > ControllerConstants.CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_TAG) {
//			resBuilder.setStatus(CreateClanStatus.FAIL_INVALID_TAG_LENGTH);
//			LOG.error("clan tag "
//			    + tag
//			    + " is more than "
//			    + ControllerConstants.CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_TAG
//			    + " characters");
//			return false;
//		}
//
//		if (user.getClanId() > 0) {
//			resBuilder.setStatus(CreateClanStatus.FAIL_ALREADY_IN_CLAN);
//			LOG.error("user already in clan with id "
//			    + user.getClanId());
//			return false;
//		}
//		final Clan clan = ClanRetrieveUtils.getClanWithNameOrTag(clanName, tag);
//		if (clan != null) {
//			if (clan.getName()
//			    .equalsIgnoreCase(clanName)) {
//				resBuilder.setStatus(CreateClanStatus.FAIL_NAME_TAKEN);
//				LOG.error("clan name already taken with name "
//				    + clanName);
//				return false;
//			}
//			if (clan.getTag()
//			    .equalsIgnoreCase(tag)) {
//				resBuilder.setStatus(CreateClanStatus.FAIL_TAG_TAKEN);
//				LOG.error("clan tag already taken with tag "
//				    + tag);
//				return false;
//			}
//		}
//
//		// CHECK MONEY
//		if (0 == gemsSpent) {
//			if (!hasEnoughCash(resBuilder, user, cashChange)) {
//				return false;
//			}
//		}
//
//		if (!hasEnoughGems(resBuilder, user, gemsSpent)) {
//			return false;
//		}
//
//		resBuilder.setStatus(CreateClanStatus.SUCCESS);
//		return true;
//	}
//
//	private boolean hasEnoughCash( final Builder resBuilder, final User u, final int cashSpent )
//	{
//		final int userCash = u.getCash();
//		// if user's aggregate cash is < cost, don't allow transaction
//		if (userCash < cashSpent) {
//			LOG.error("user error: user does not have enough cash. userCash="
//			    + userCash
//			    + "\t cashSpent="
//			    + cashSpent);
//			resBuilder.setStatus(CreateClanStatus.FAIL_INSUFFICIENT_FUNDS);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean hasEnoughGems( final Builder resBuilder, final User u, final int gemsSpent )
//	{
//		final int userGems = u.getGems();
//		// if user's aggregate gems is < cost, don't allow transaction
//		if (userGems < gemsSpent) {
//			LOG.error("user error: user does not have enough gems. userGems="
//			    + userGems
//			    + "\t gemsSpent="
//			    + gemsSpent);
//			resBuilder.setStatus(CreateClanStatus.FAIL_INSUFFICIENT_FUNDS);
//			return false;
//		}
//
//		return true;
//	}
//
//	private void sendGeneralNotification( final String userName, final String clanName )
//	{
//		final Notification createClanNotification = new Notification();
//		createClanNotification.setAsClanCreated(userName, clanName);
//
//		MiscMethods.writeGlobalNotification(createClanNotification, server);
//	}
//
//	private boolean writeChangesToDB( final User user, final String name, final String tag,
//	    final boolean requestToJoinRequired, String description, final int clanIconId,
//	    final Timestamp createTime, final Clan createdClan, final int gemsSpent,
//	    int cashChange, final Map<String, Integer> money )
//	{
//
//		// just in case user doesn't input one, set default description
//		if ((null == description)
//		    || description.isEmpty()) {
//			description = "Welcome to "
//			    + name
//			    + "!";
//		}
//
//		final int clanId = InsertUtils.get()
//		    .insertClan(name, createTime, description, tag, requestToJoinRequired, clanIconId);
//		if (clanId <= 0) {
//			return false;
//		} else {
//			setClan(createdClan, clanId, name, createTime, description, tag,
//			    requestToJoinRequired, clanIconId);
//			LOG.info("clan="
//			    + createdClan);
//		}
//
//		final int gemChange = -1
//		    * Math.abs(gemsSpent);
//		cashChange = -1
//		    * Math.abs(cashChange);
//		if (!user.updateGemsCashClan(gemChange, cashChange, clanId)) {
//			LOG.error("problem with decreasing user gems, cash for creating clan. gemChange="
//			    + gemChange
//			    + "\t cashChange="
//			    + cashChange);
//		} else {
//			if (0 != gemsSpent) {
//				money.put(MiscMethods.gems, gemsSpent);
//			}
//			if (0 != cashChange) {
//				money.put(MiscMethods.cash, cashChange);
//			}
//		}
//
//		if (!InsertUtils.get()
//		    .insertUserClan(user.getId(), clanId, UserClanStatus.LEADER.name(), createTime)) {
//			LOG.error("problem with inserting user clan data for user "
//			    + user
//			    + ", and clan id "
//			    + clanId);
//		}
//		DeleteUtils.get()
//		    .deleteUserClansForUserExceptSpecificClan(user.getId(), clanId);
//
//		return true;
//	}
//
//	private void setClan( final Clan createdClan, final int clanId, final String name,
//	    final Timestamp createTime, final String description, final String tag,
//	    final boolean requestToJoinRequired, final int clanIconId )
//	{
//		createdClan.setId(clanId);
//		createdClan.setName(name);
//		createdClan.setCreateTime(createTime);
//		createdClan.setDescription(description);
//		createdClan.setTag(tag);
//		createdClan.setRequestToJoinRequired(requestToJoinRequired);
//		createdClan.setClanIconId(clanIconId);
//	}
//
//	private void writeToUserCurrencyHistory( final User aUser, final Clan clan,
//	    final Timestamp createTime, final Map<String, Integer> currencyChange,
//	    final Map<String, Integer> previousCurrency )
//	{
//		if (currencyChange.isEmpty()) {
//			return;
//		}
//
//		final String reason = ControllerConstants.UCHRFC__CREATE_CLAN;
//		final StringBuilder detailsSb = new StringBuilder();
//		detailsSb.append("clanId=");
//		detailsSb.append(clan.getId());
//		detailsSb.append(" clanName=");
//		detailsSb.append(clan.getName());
//		final String details = detailsSb.toString();
//
//		final String userUuid = aUser.getId();
//		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//		final String gems = MiscMethods.gems;
//		final String cash = MiscMethods.cash;
//
//		currentCurrency.put(gems, aUser.getGems());
//		currentCurrency.put(cash, aUser.getCash());
//		reasonsForChanges.put(gems, reason);
//		reasonsForChanges.put(cash, reason);
//		detailsMap.put(gems, details);
//		detailsMap.put(cash, details);
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, createTime, currencyChange,
//		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
//
//	}
//
//}
