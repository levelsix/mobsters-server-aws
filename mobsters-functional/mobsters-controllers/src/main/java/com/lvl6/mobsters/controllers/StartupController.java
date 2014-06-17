package com.lvl6.mobsters.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupRequestProto;
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto;
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.StartupStatus;
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.UpdateStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.StartupRequestEvent;
import com.lvl6.mobsters.events.response.StartupResponseEvent;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.FullUserQuestProto;
import com.lvl6.mobsters.noneventproto.utils.NoneventQuestProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventUserProtoSerializer;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.quest.QuestService;
import com.lvl6.mobsters.services.user.UserService;
import com.lvl6.properties.Globals;
import com.lvl6.properties.MDCKeys;

@Component
public class StartupController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(StartupController.class);

	@Autowired
	protected UserService userService;

	@Autowired
	protected QuestService questService;
	
	@Autowired
	protected NoneventQuestProtoSerializer noneventQuestProtoSerializer;
	
	@Autowired
	protected NoneventUserProtoSerializer noneventUserProtoSerializer;
	
	/*
	 * @Autowired protected EventWriter eventWriter;
	 */

	public StartupController()
	{}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new StartupRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_STARTUP_EVENT;
	}

	@Override
	protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
	{
		final StartupRequestProto reqProto =
			((StartupRequestEvent) event).getStartupRequestProto();
		LOG.info("Prcessing Startup request event");

		final String udid = reqProto.getUdid();
		String fbId = reqProto.getFbId();
		boolean freshRestart = reqProto.getIsFreshRestart();

		// the player might be a new player with no user_id yet
		String userId = null;
		setMDCProperties(udid, userId); // cassandra version had this

		double tempClientVersionNum = reqProto.getVersionNum() * 10;
		// TODO: Figure out the right version number to use
		double tempLatestVersionNum = Globals.VERSION_NUMBER() * 10;

		UpdateStatus updateStatus;
		// Check version number
		if ((int) tempClientVersionNum < (int) tempLatestVersionNum
			&& tempClientVersionNum > 12.5) {
			updateStatus = UpdateStatus.MAJOR_UPDATE;
			LOG.info("player has been notified of forced update");
		} else if (tempClientVersionNum < tempLatestVersionNum) {
			updateStatus = UpdateStatus.MINOR_UPDATE;
		} else {
			updateStatus = UpdateStatus.NO_UPDATE;
		}

		// prepare to send response back to client
		StartupResponseProto.Builder resBuilder = StartupResponseProto.newBuilder();
		resBuilder.setUpdateStatus(updateStatus);
		resBuilder.setAppStoreURL(Globals.APP_STORE_URL());
		resBuilder.setReviewPageURL(Globals.REVIEW_PAGE_URL());
		resBuilder.setReviewPageConfirmationMessage(Globals.REVIEW_PAGE_CONFIRMATION_MESSAGE);

		StartupResponseEvent resEvent = new StartupResponseEvent(udid);
		resEvent.setTag(event.getTag());

		// Don't fill in other fields if it is a major update
		StartupStatus startupStatus = StartupStatus.USER_NOT_IN_DB;
		Date now = new Date();
		UserCredential user = null;

		try {
			if (!UpdateStatus.MAJOR_UPDATE.equals(updateStatus)) {
				user = getUserService().getUserCredentialByFacebookIdOrUdid(fbId, udid);
				if (null != user) {
					userId = user.getUserId();
					startupStatus = StartupStatus.USER_IN_DB;
					LOG.info("No major update... getting user info");
					loginExistingUser(resBuilder, user, userId);
				} else {
					LOG.info("no user id: tutorial(?) player with udid "
						+ udid);
				}

				resBuilder.setStartupStatus(startupStatus);
//				setConstants(resBuilder, startupStatus);
			}
			//startup time

		} catch (Exception e) {
			LOG.error("exception in StartupController processEvent when calling userService", e);
			resBuilder.setStartupStatus(StartupStatus.USER_NOT_IN_DB);
			
		}

		resBuilder.setServerTimeMillis((new Date()).getTime());
		resEvent.setStartupResponseProto(resBuilder.build());

		// write to client
		LOG.info("Writing event: "
			+ resEvent);
		try {
			eventWriter.writeEvent(resEvent);
		} catch (Exception e) {
			LOG.error("fatal exception in StartupController processRequestEvent", e);
		}

	}

	// copy pasted from aoc's MiscMethods.java
	// commenting it out, 1) don't know how to properly get ip, 2) just cause don't know what
	// these are used for or how they are used
	public void purgeMDCProperties()
	{
		MDC.remove(MDCKeys.UDID);
		MDC.remove(MDCKeys.PLAYER_ID);
		// MDC.remove(MDCKeys.IP);
	}

	public void setMDCProperties( String udid, String playerId )
	{// , String ip) {
		purgeMDCProperties();
		if (udid != null)
			MDC.put(MDCKeys.UDID, udid);
		// if (ip != null) MDC.put(MDCKeys.IP, ip);
		if (playerId != null)
			MDC.put(MDCKeys.PLAYER_ID.toString(), playerId);
	}
	
	private void loginExistingUser(Builder resBuilder, UserCredential uc, String userId) {
		// TODO: Account for forcelogout
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();

		LOG.info("No major update... getting user info");
		setInProgressAndAvailableQuests(resBuilder, userId);
//		LOG.info("{}ms at setInProgressAndAvailableQuests", stopWatch.getTime());
//		setUserClanInfos(resBuilder, userId);
//		LOG.info("{}ms at setUserClanInfos", stopWatch.getTime());
//		setNotifications(resBuilder, user);
//		LOG.info("{}ms at setNotifications", stopWatch.getTime());
//		setNoticesToPlayers(resBuilder);
//		LOG.info("{}ms at setNoticesToPlayers", stopWatch.getTime());
//		setGroupChatMessages(resBuilder, user);
//		LOG.info("{}ms at groupChatMessages", stopWatch.getTime());
//		setPrivateChatPosts(resBuilder, user, userId);
//		LOG.info("{}ms at privateChatPosts", stopWatch.getTime());
//		setUserMonsterStuff(resBuilder, userId);
//		LOG.info("{}ms at setUserMonsterStuff", stopWatch.getTime());
//		setBoosterPurchases(resBuilder);
//		LOG.info("{}ms at boosterPurchases", stopWatch.getTime());
//		setFacebookAndExtraSlotsStuff(resBuilder, user, userId);
//		LOG.info("{}ms at facebookAndExtraSlotsStuff", stopWatch.getTime());
//		setTaskStuff(resBuilder, userId);
//		LOG.info("{}ms at task stuff", stopWatch.getTime());
//		setAllStaticData(resBuilder, userId, true);
//		LOG.info("{}ms at static data", stopWatch.getTime());
//		setEventStuff(resBuilder, userId);
//		LOG.info("{}ms at eventStuff", stopWatch.getTime());
		//if server sees that the user is in a pvp battle, decrement user's elo
//		PvpLeagueForUser plfu = pvpBattleStuff(resBuilder, user,
//			userId, freshRestart, now);
//		LOG.info("{}ms at pvpBattleStuff", stopWatch.getTime());
//		pvpBattleHistoryStuff(resBuilder, user, userId);
//		LOG.info("{}ms at pvpBattleHistoryStuff", stopWatch.getTime());
//		setClanRaidStuff(resBuilder, user, userId, now);
//		LOG.info("{}ms at clanRaidStuff", stopWatch.getTime());
//		setAchievementStuff(resBuilder, userId);
//		LOG.info("{}ms at achivementStuff", stopWatch.getTime());
//		setMiniJob(resBuilder, userId);
//		LOG.info("{}ms at miniJobStuff", stopWatch.getTime());
	}
	
	private void setInProgressAndAvailableQuests(Builder resBuilder, String userId) {
		// get all inProgressAndRedeemedUserQuests which is all userQuests
		List<QuestForUser> allUserQuests = questService.findByUserId(userId);
		
		List<QuestForUser> inProgressQuests = new ArrayList<QuestForUser>();
		List<Integer> inProgressQuestIds = new ArrayList<Integer>();
		List<Integer> redeemedQuestIds = new ArrayList<Integer>();

		for (QuestForUser uq : allUserQuests) {

			if (!uq.isRedeemed()) {
				//unredeemed quest section, could be complete or not
				inProgressQuests.add(uq);
				inProgressQuestIds.add(uq.getQuestId());
			} else {
				redeemedQuestIds.add(uq.getQuestId());
			}
		}
		
		// get the QuestJobForUser for ONLY the inProgressQuests
		Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs = questService
			.findByUserIdAndQuestIdIn(userId, inProgressQuestIds);
		
		Map<Integer, Quest> questIdToQuests = null;//QuestRetrieveUtils.getQuestIdsToQuests();

		//generate the user quests
		List<FullUserQuestProto> currentUserQuests = noneventQuestProtoSerializer
			.createFullUserQuestDataLarges(inProgressQuests, questIdToQuests,
				questIdToUserQuestJobs);
		resBuilder.addAllUserQuests(currentUserQuests);

		//generate the redeemed quest ids
		resBuilder.addAllRedeemedQuestIds(redeemedQuestIds);
	}
	
	private void setUserClanInfos() {
		
	}
	

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService( UserService userService )
	{
		this.userService = userService;
	}

	public QuestService getQuestService()
	{
		return questService;
	}

	public void setQuestService( QuestService questService )
	{
		this.questService = questService;
	}

	public NoneventUserProtoSerializer getNoneventUserProtoSerializer()
	{
		return noneventUserProtoSerializer;
	}

	public void setNoneventUserProtoSerializer(
		NoneventUserProtoSerializer noneventUserProtoSerializer )
	{
		this.noneventUserProtoSerializer = noneventUserProtoSerializer;
	}

	public NoneventQuestProtoSerializer getNoneventQuestProtoSerializer()
	{
		return noneventQuestProtoSerializer;
	}

	public void setNoneventQuestProtoSerializer(
		NoneventQuestProtoSerializer noneventQuestProtoSerializer )
	{
		this.noneventQuestProtoSerializer = noneventQuestProtoSerializer;
	}

}
