//package com.lvl6.mobsters.controllers.todo;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.hazelcast.core.IList;
//import com.kabam.apiclient.KabamApi;
//import com.kabam.apiclient.MobileNaidResponse;
//import com.kabam.apiclient.ResponseCode;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.StartupRequestEvent;
//import com.lvl6.mobsters.events.response.ForceLogoutResponseEvent;
//import com.lvl6.mobsters.events.response.StartupResponseEvent;
//import com.lvl6.mobsters.dynamo.AchievementForUser;
//import com.lvl6.mobsters.dynamo.BoosterItem;
//import com.lvl6.mobsters.dynamo.CepfuRaidStageHistory;
//import com.lvl6.mobsters.dynamo.Clan;
//import com.lvl6.mobsters.dynamo.ClanChatPost;
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan;
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser;
//import com.lvl6.mobsters.dynamo.ClanEventPersistentUserReward;
//import com.lvl6.mobsters.dynamo.EventPersistentForUser;
//import com.lvl6.mobsters.dynamo.MiniJobForUser;
//import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
//import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
//import com.lvl6.mobsters.dynamo.MonsterForUser;
//import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
//import com.lvl6.mobsters.dynamo.PrivateChatPost;
//import com.lvl6.mobsters.dynamo.PvpBattleForUser;
//import com.lvl6.mobsters.dynamo.PvpBattleHistory;
//import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
//import com.lvl6.mobsters.dynamo.Quest;
//import com.lvl6.mobsters.dynamo.QuestForUser;
//import com.lvl6.mobsters.dynamo.QuestJobForUser;
//import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
//import com.lvl6.mobsters.dynamo.TaskStageForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.UserClan;
//import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot;
//import com.lvl6.leaderboards.LeaderBoardUtil;
//import com.lvl6.misc.MiscMethods;
//import com.lvl6.properties.ControllerConstants;
//import com.lvl6.properties.Globals;
//import com.lvl6.properties.KabamProperties;
//import com.lvl6.mobsters.noneventproto.NoneventAchievementStuffProto.UserAchievementProto;
//import com.lvl6.mobsters.noneventproto.NoneventPvpProto.PvpHistoryProto;
//import com.lvl6.mobsters.noneventproto.NoneventHallStuffProto.RareBoosterPurchaseProto;
//import com.lvl6.mobsters.noneventproto.NoneventChatProto.GroupChatMessageProto;
//import com.lvl6.mobsters.noneventproto.NoneventChatProto.PrivateChatPostProto;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventClanInfoProto;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventRaidStageHistoryProto;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventUserInfoProto;
//import com.lvl6.mobsters.eventproto.EventStartupProto.ForceLogoutResponseProto;
//import com.lvl6.mobsters.eventproto.EventStartupProto.StartupRequestProto;
//import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto;
//import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.StartupStatus;
//import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.TutorialConstants;
//import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.UpdateStatus;
//import com.lvl6.mobsters.noneventproto.NoneventMiniJobConfigProto.UserMiniJobProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserMonsterProto.FullUserMonsterProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserMonsterProto.UserEnhancementItemProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserMonsterProto.UserEnhancementProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserMonsterProto.UserMonsterEvolutionProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserMonsterProto.UserMonsterHealingProto;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventQuestProto.FullUserQuestProto;
//import com.lvl6.mobsters.noneventproto.NoneventStaticDataStuffProto.StaticDataProto;
//import com.lvl6.mobsters.noneventproto.NoneventTaskProto.MinimumUserTaskProto;
//import com.lvl6.mobsters.noneventproto.NoneventTaskProto.TaskStageProto;
//import com.lvl6.mobsters.noneventproto.NoneventTaskProto.UserPersistentEventProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.FullUserProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithFacebookId;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.UserFacebookInviteForSlotProto;
//import com.lvl6.pvp.HazelcastPvpUtil;
//import com.lvl6.pvp.PvpUser;
//import com.lvl6.retrieveutils.AchievementForUserRetrieveUtil;
//import com.lvl6.retrieveutils.CepfuRaidStageHistoryRetrieveUtils;
//import com.lvl6.retrieveutils.ClanChatPostRetrieveUtils;
//import com.lvl6.retrieveutils.ClanEventPersistentForClanRetrieveUtils;
//import com.lvl6.retrieveutils.ClanEventPersistentForUserRetrieveUtils;
//import com.lvl6.retrieveutils.ClanEventPersistentUserRewardRetrieveUtils;
//import com.lvl6.retrieveutils.ClanRetrieveUtils;
//import com.lvl6.retrieveutils.EventPersistentForUserRetrieveUtils;
//import com.lvl6.retrieveutils.FirstTimeUsersRetrieveUtils;
//import com.lvl6.retrieveutils.IAPHistoryRetrieveUtils;
//import com.lvl6.retrieveutils.LoginHistoryRetrieveUtils;
//import com.lvl6.retrieveutils.MiniJobForUserRetrieveUtil;
//import com.lvl6.retrieveutils.MonsterEnhancingForUserRetrieveUtils;
//import com.lvl6.retrieveutils.MonsterEvolvingForUserRetrieveUtils;
//import com.lvl6.retrieveutils.MonsterHealingForUserRetrieveUtils;
//import com.lvl6.retrieveutils.PrivateChatPostRetrieveUtils;
//import com.lvl6.retrieveutils.PvpBattleForUserRetrieveUtils;
//import com.lvl6.retrieveutils.PvpBattleHistoryRetrieveUtil;
//import com.lvl6.retrieveutils.PvpLeagueForUserRetrieveUtil;
//import com.lvl6.retrieveutils.QuestJobForUserRetrieveUtil;
//import com.lvl6.retrieveutils.TaskForUserCompletedRetrieveUtils;
//import com.lvl6.retrieveutils.TaskForUserOngoingRetrieveUtils;
//import com.lvl6.retrieveutils.TaskStageForUserRetrieveUtils;
//import com.lvl6.retrieveutils.UserFacebookInviteForSlotRetrieveUtils;
//import com.lvl6.retrieveutils.rarechange.PvpLeagueRetrieveUtils;
//import com.lvl6.retrieveutils.rarechange.QuestRetrieveUtils;
//import com.lvl6.retrieveutils.rarechange.StartupStuffRetrieveUtils;
//import com.lvl6.server.GameServer;
//import com.lvl6.server.Locker;
//import com.lvl6.server.controller.utils.MonsterStuffUtils;
//import com.lvl6.server.controller.utils.TimeUtils;
//import com.lvl6.spring.AppContext;
//import com.lvl6.utils.CreateInfoProtoUtils;
//import com.lvl6.utils.RetrieveUtils;
//import com.lvl6.utils.utilmethods.DeleteUtils;
//import com.lvl6.utils.utilmethods.InsertUtils;
//import com.lvl6.utils.utilmethods.UpdateUtils;
//
//@Component
//@DependsOn("gameServer")
//public class StartupController extends EventController {
////  private static String nameRulesFile = "namerulesElven.txt";
////  private static int syllablesInName1 = 2;
////  private static int syllablesInName2 = 3;
//
//	private static Logger LOG = LoggerFactory.getLogger(StartupController.class);
//  }.getClass().getEnclosingClass());
//
//  private static final class GroupChatComparator implements Comparator<GroupChatMessageProto> {
//	  @Override
//	  public int compare(final GroupChatMessageProto o1, final GroupChatMessageProto o2) {
//		  if (o1.getTimeOfChat() < o2.getTimeOfChat()) {
//			  return -1;
//		  } else if (o1.getTimeOfChat() > o2.getTimeOfChat()) {
//			  return 1;
//		  } else {
//			  return 0;
//		  }
//	  }
//  }
//  @Resource(name = "goodEquipsRecievedFromBoosterPacks")
//  protected IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks;
//  public IList<RareBoosterPurchaseProto> getGoodEquipsRecievedFromBoosterPacks() {
//    return goodEquipsRecievedFromBoosterPacks;
//  }
//  public void setGoodEquipsRecievedFromBoosterPacks(
//      final IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks) {
//    this.goodEquipsRecievedFromBoosterPacks = goodEquipsRecievedFromBoosterPacks;
//  }
//
//  @Resource(name = "globalChat")
//  protected IList<GroupChatMessageProto> chatMessages;
//  public IList<GroupChatMessageProto> getChatMessages() {
//    return chatMessages;
//  }
//  public void setChatMessages(final IList<GroupChatMessageProto> chatMessages) {
//    this.chatMessages = chatMessages;
//  }
//  
//  @Autowired
// protected DataServiceTxManager svcTxManager;
//
// @Autowired
//  protected HazelcastPvpUtil hazelcastPvpUtil;
//
//  @Autowired
//  protected Locker locker;
//
//  @Autowired
//  protected TimeUtils timeUtils;
//
//  @Autowired
//  protected Globals globals;
//  
//  @Autowired
//  protected QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil;
//  
//  @Autowired
//  protected PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil;
//
//  @Autowired
//  protected PvpBattleHistoryRetrieveUtil pvpBattleHistoryRetrieveUtil;
//
//  @Autowired
//  protected AchievementForUserRetrieveUtil achievementForUserRetrieveUtil; 
//  
//  @Autowired
//  protected MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil;
//
//  public StartupController() {
//	  numAllocatedThreads = 3;
//  }
//
//  @Override
//  public RequestEvent createRequestEvent() {
//    return new StartupRequestEvent();
//  }
//
//  @Override
//  public EventProtocolRequest getEventType() {
//    return EventProtocolRequest.C_STARTUP_EVENT;
//  }
//
//  @Override
//  protected void processRequestEvent( final RequestEvent event, final EventsToDispatch eventWriter ) throws Exception {
//    final StartupRequestProto reqProto = ((StartupRequestEvent) event).getStartupRequestProto();
//    LOG.info("Processing startup request event");
//    UpdateStatus updateStatus;
//    final String udid = reqProto.getUdid();
//    final String apsalarId = reqProto.hasApsalarId() ? reqProto.getApsalarUuid() : null;
//    final String fbId = reqProto.getFbUuid();
//    final boolean freshRestart = reqProto.getIsFreshRestart();
//
//    MiscMethods.setMDCProperties(udid, null, MiscMethods.getIPOfPlayer(server, null, udid));
//
//    final double tempClientVersionNum = reqProto.getVersionNum() * 10;
//    final double tempLatestVersionNum = GameServer.clientVersionNumber * 10;
//
//    // Check version number
//    if (((int) tempClientVersionNum < (int) tempLatestVersionNum) && (tempClientVersionNum > 12.5)) {
//      updateStatus = UpdateStatus.MAJOR_UPDATE;
//      LOG.info("player has been notified of forced update");
//    } else if (tempClientVersionNum < tempLatestVersionNum) {
//      updateStatus = UpdateStatus.MINOR_UPDATE;
//    } else {
//      updateStatus = UpdateStatus.NO_UPDATE;
//    }
//    
//    final StartupResponseProto.Builder resBuilder = StartupResponseProto.newBuilder();
//    resBuilder.setUpdateStatus(updateStatus);
//    resBuilder.setAppStoreURL(Globals.APP_STORE_URL());
//    resBuilder.setReviewPageURL(Globals.REVIEW_PAGE_URL());
//    resBuilder.setReviewPageConfirmationMessage(Globals.REVIEW_PAGE_CONFIRMATION_MESSAGE);
//
//    User user = null;
//
//    // Don't fill in other fields if it is a major update
//    StartupStatus startupStatus = StartupStatus.USER_NOT_IN_DB;
//
//    Date nowDate = new Date();
//    nowDate = getTimeUtils().createDateTruncateMillis(nowDate);
//    final Timestamp now = new Timestamp(nowDate.getTime());
//    final boolean isLogin = true;
//
//    final int newNumConsecutiveDaysLoggedIn = 0;
//
//    try {
//			if (updateStatus != UpdateStatus.MAJOR_UPDATE) {
//				final List<User> users = RetrieveUtils.userRetrieveUtils().getUserByUDIDorFbId(udid, fbId);
//			  user = selectUser(users, udid, fbId);//RetrieveUtils.userRetrieveUtils().getUserByUDID(udid);
//			  if (user != null) {
//			  	final String userUuid = user.getId();
//			  	//if can't lock player, exception will be thrown
//			    svcTxManager.beginTransaction();
//			    try {
//			    	//force other devices on this account to logout
//			      final ForceLogoutResponseProto.Builder logoutResponse = ForceLogoutResponseProto.newBuilder();
//			      //login value before it is overwritten with current time
//			      logoutResponse.setPreviousLoginTime(user.getLastLogin().getTime());
//			      logoutResponse.setUdid(udid);
//			      final ForceLogoutResponseEvent logoutEvent = new ForceLogoutResponseEvent(userUuid);
//			      logoutEvent.setForceLogoutResponseProto(logoutResponse.build());
//			      //to take care of two devices using the same udid amqp queue (not very common)
//			      //only if a device is already on and then another one comes on and somehow
//			      //switches to the existing user account, no fbId though
//			      getEventWriter().processPreDBResponseEvent(logoutEvent, udid);
//			      //to take care of one device already logged on (lot more common than above)
//			      getEventWriter().handleEvent(logoutEvent);
//			      //to take care of both the above, but when user is logged in via facebook id
//			      if ((null != fbId) && !fbId.isEmpty()) {
//			      	getEventWriter().processPreDBFacebookEvent(logoutEvent, fbId);
//			      }
//			      
//			    	
//			      startupStatus = StartupStatus.USER_IN_DB;
//			      LOG.info("No major update... getting user info");
////          newNumConsecutiveDaysLoggedIn = setDailyBonusInfo(resBuilder, user, now);
//			      setInProgressAndAvailableQuests(resBuilder, userUuid);
//			      setUserClanInfos(resBuilder, userUuid);
//			      setNotifications(resBuilder, user);
//			      setNoticesToPlayers(resBuilder);
//			      setGroupChatMessages(resBuilder, user);
//			      setPrivateChatPosts(resBuilder, user, userUuid);
//			      setUserMonsterStuff(resBuilder, userUuid);
//			      setBoosterPurchases(resBuilder);
//			      setFacebookAndExtraSlotsStuff(resBuilder, user, userUuid);
//			      setTaskStuff(resBuilder, userUuid);
//			      setAllStaticData(resBuilder, userUuid, true);
//			      setEventStuff(resBuilder, userUuid);
//			      //if server sees that the user is in a pvp battle, decrement user's elo
//			      final PvpLeagueForUser plfu = pvpBattleStuff(resBuilder, user,
//			    		  userUuid, freshRestart, now); 
//			      pvpBattleHistoryStuff(resBuilder, user, userUuid);
//			      setClanRaidStuff(resBuilder, user, userUuid, now);
//			      setAchievementStuff(resBuilder, userUuid);
//			      setMiniJob(resBuilder, userUuid);
//			      
//			      setWhetherPlayerCompletedInAppPurchase(resBuilder, user);
//			      setUnhandledForgeAttempts(resBuilder, user);
//			      setLockBoxEvents(resBuilder, user);
////          setLeaderboardEventStuff(resBuilder);
//			      setAllies(resBuilder, user);
////          setAllBosses(resBuilder, user.getType());
//
//			      //OVERWRITE THE LASTLOGINTIME TO THE CURRENT TIME
//			      //log.info("before last login change, user=" + user);
//			      user.setLastLogin(nowDate);
//			      //log.info("after last login change, user=" + user);
//			      
//			      final FullUserProto fup = CreateInfoProtoUtils.createFullUserProtoFromUser(
//			    		  user, plfu);
//			      //log.info("fup=" + fup);
//			      resBuilder.setSender(fup);
//
//			      final boolean isNewUser = false;
//			      InsertUtils.get().insertIntoLoginHistory(udid, user.getId(), now, isLogin, isNewUser);
//			    } catch (final Exception e) {
//			      LOG.error("exception in StartupController processEvent", e);
//			    } finally {
//			      // server.unlockClanTowersTable();
//			      getLocker().unlockPlayer(user.getId(), this.getClass().getSimpleName());
//			    }
//			  } else {
//			    LOG.info("tutorial player with udid " + udid);
//			    
//			    final TutorialConstants tc = MiscMethods.createTutorialConstantsProto();
//			    resBuilder.setTutorialConstants(tc);
//			    setAllStaticData(resBuilder, 0, false);
//
//			    final boolean userLoggedIn = LoginHistoryRetrieveUtils.userLoggedInByUDID(udid);
//			    final int numOldAccounts = RetrieveUtils.userRetrieveUtils().numAccountsForUDID(udid);
//			    final boolean alreadyInFirstTimeUsers = FirstTimeUsersRetrieveUtils.userExistsWithUDID(udid);
//			    boolean isFirstTimeUser = false;
//			    // LOG.info("userLoggedIn=" + userLoggedIn + ", numOldAccounts="
//			    // + numOldAccounts
//			    // + ", alreadyInFirstTimeUsers=" + alreadyInFirstTimeUsers);
//			    if (!userLoggedIn && (0 >= numOldAccounts) && !alreadyInFirstTimeUsers) {
//			      isFirstTimeUser = true;
//			    }
//			    
//			    LOG.info("\n userLoggedIn=" + userLoggedIn + "\t numOldAccounts=" +
//			    		numOldAccounts + "\t alreadyInFirstTimeUsers=" +
//			    		alreadyInFirstTimeUsers + "\t isFirstTimeUser=" + isFirstTimeUser);
//
//			    if (isFirstTimeUser) {
//			      LOG.info("new player with udid " + udid);
//			      InsertUtils.get().insertIntoFirstTimeUsers(udid, null,
//			          reqProto.getMacAddress(), reqProto.getAdvertiserUuid(), now);
//			    }
//			    
//			    if (Globals.OFFERCHART_ENABLED() && isFirstTimeUser) {
//			      sendOfferChartInstall(now, reqProto.getAdvertiserUuid());
//			    }
//
//			    final boolean goingThroughTutorial = true;
//			    InsertUtils.get().insertIntoLoginHistory(udid, 0, now, isLogin, goingThroughTutorial);
//			  }
//			  resBuilder.setStartupStatus(startupStatus);
//			  setConstants(resBuilder, startupStatus);
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in StartupController processEvent", e);
//      //don't let the client hang
//      try {
//    	  resBuilder.setStartupStatus(StartupStatus.SERVER_IN_MAINTENANCE); //DO NOT allow user to play
//    	  final StartupResponseEvent resEvent = new StartupResponseEvent(udid);
//    	  resEvent.setTag(event.getTag());
//    	  resEvent.setStartupResponseProto(resBuilder.build());
//    	  getEventWriter().processPreDBResponseEvent(resEvent, udid);
//      } catch (final Exception e2) {
//    	  LOG.error("exception2 in UpdateUserCurrencyController processEvent", e);
//      }
//		}
//
//    if (Globals.KABAM_ENABLED()) {
//      final String naid = retrieveKabamNaid(user, udid, reqProto.getMacAddress(),
//          reqProto.getAdvertiserUuid());
//      resBuilder.setKabamNaid(naid);
//    }
//    
//    //startup time
//    resBuilder.setServerTimeMillis((new Date()).getTime());
//    final StartupResponseProto resProto = resBuilder.build();
//    final StartupResponseEvent resEvent = new StartupResponseEvent(udid);
//    resEvent.setTag(event.getTag());
//    resEvent.setStartupResponseProto(resProto);
//
//    // LOG.info("Sending struct");
//    // sendAllStructs(udid, user);
//
//    LOG.debug("Writing event response: " + resEvent);
//    server.writePreDBEvent(resEvent, udid);
//    LOG.debug("Wrote response event: " + resEvent);
//    // for things that client doesn't need
//    LOG.debug("After response tasks");
//
//    // if app is not in force tutorial execute this function,
//    // regardless of whether the user is new or restarting from an account
//    // reset
//    //UPDATE USER's LAST LOGIN
//    updateLeaderboard(apsalarId, user, now, newNumConsecutiveDaysLoggedIn);
//    //log.info("user after change user login via db. user=" + user);
//  }
//
//  //priority of user returned is 
//  //user with specified fbId
//  //user with specified udid
//  //null
//  private User selectUser(final List<User> users, final String udid, final String fbId) {
//  	final int numUsers = users.size();
//  	if (numUsers > 2) {
//  		LOG.error("(not really error) there are more than 2 users with the same udid and fbId. udid=" +
//  				udid + " fbId=" + fbId + " users=" + users);
//  	}
//  	if (1 == numUsers) {
//  		return users.get(0);
//  	}
//  	
//  	User udidUser = null;
//  	
//  	for (final User u : users) {
//  		final String userFbId = u.getFacebookId();
//  		final String userUdid = u.getUdid();
//  		
//  		if ((fbId != null) && fbId.equals(userFbId)) {
//  			return u;
//  		} else if ((null == udidUser) && (udid != null) && udid.equals(userUdid)) {
//  			//so this is the first user with specified udid, don't change reference
//  			//to this user once set
//  			udidUser = u;
//  		}
//  	}
//  	
//  	//didn't find user with specified fbId
//  	return udidUser;
//  }
//
//  private void setInProgressAndAvailableQuests(final Builder resBuilder, final String userUuid) {
//  	  final List<QuestForUser> inProgressAndRedeemedUserQuests = RetrieveUtils.questForUserRetrieveUtils()
//  	      .getUserQuestsForUser(userUuid);
////  	  LOG.info("user quests: " + inProgressAndRedeemedUserQuests);
//  	  
//  	  final List<QuestForUser> inProgressQuests = new ArrayList<QuestForUser>();
//  	  final Set<Integer> questUuids = new HashSet<Integer>();
//  	  final List<Integer> redeemedQuestUuids = new ArrayList<Integer>();
//  	  
//  	  final Map<Integer, Quest> questIdToQuests = QuestRetrieveUtils.getQuestUuidsToQuests();
//  	  for (final QuestForUser uq : inProgressAndRedeemedUserQuests) {
//  	  	
//  	    if (!uq.isRedeemed()) {
//  	    	//unredeemed quest section
//  	    	inProgressQuests.add(uq);
//  	    } else {
//  	    	final int questId = uq.getQuestId();
//  	    	redeemedQuestUuids.add(questId);
//  	    }
//  	  }
//  	  
//  	  //get the QuestJobForUser for ONLY the inProgressQuests
//  	  final Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs =
//  			  getQuestJobForUserRetrieveUtil()
//  			  .getSpecificOrAllQuestIdToQuestJobsForUserUuid(userUuid, questUuids);
//  	  
//  	  //generate the user quests
//  	  final List<FullUserQuestProto> currentUserQuests = CreateInfoProtoUtils
//  	  		.createFullUserQuestDataLarges(inProgressQuests, questIdToQuests,
//  	  			questIdToUserQuestJobs);
//  	  resBuilder.addAllUserQuests(currentUserQuests);
//  	  
//  	  //generate the redeemed quest ids
//  	  resBuilder.addAllRedeemedQuestUuids(redeemedQuestUuids);
//  }
//  
//  private void setUserClanInfos(final StartupResponseProto.Builder resBuilder, final String userUuid) {
//    final List<UserClan> userClans = RetrieveUtils.userClanRetrieveUtils().getUserClansRelatedToUser(
//        userUuid);
//    for (final UserClan uc : userClans) {
//      resBuilder.addUserClanInfo(CreateInfoProtoUtils.createFullUserClanProtoFromUserClan(uc));
//    }
//  }
//
//  private void setNotifications(final Builder resBuilder, final User user) {
////    List<Integer> userUuids = new ArrayList<Integer>();
//
////    Timestamp earliestBattleNotificationTimeToRetrieve = new Timestamp(new Date().getTime()
////        - ControllerConstants.STARTUP__HOURS_OF_BATTLE_NOTIFICATIONS_TO_SEND * 3600000);
////
////    List<ClanChatPost> clanChatPosts = null;
////    if (user.getClanId() > 0) {
////      clanChatPosts = ClanChatPostRetrieveUtils.getMostRecentClanChatPostsForClan(
////          ControllerConstants.RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP, user.getClanId());
////      for (ClanChatPost p : clanChatPosts) {
////        userUuids.add(p.getPosterId());
////      }
////    }
////
////    Map<Integer, User> usersByUuids = null;
////    if (userUuids.size() > 0) {
////      usersByUuids = RetrieveUtils.userRetrieveUtils().getUsersByUuids(userUuids);
////    }
////
////    if (clanChatPosts != null && clanChatPosts.size() > 0) {
////
////    }
//  }
//  
//  private void setNoticesToPlayers(final Builder resBuilder) {
//  	final List<String> notices = StartupStuffRetrieveUtils.getAllActiveAlerts();
//  	if (null != notices) {
//  	  for (final String notice : notices) {
//  	    resBuilder.addNoticesToPlayers(notice);
//  	  }
//  	}
//
//  }
//  
//  private void setGroupChatMessages(final StartupResponseProto.Builder resBuilder, final User user) {
//  	final Iterator<GroupChatMessageProto> it = chatMessages.iterator();
//  	final List<GroupChatMessageProto> globalChats = new ArrayList<GroupChatMessageProto>();
//  	while (it.hasNext()) {
//  		globalChats.add(it.next());
//  	}
//  	/*
//  	  Comparator<GroupChatMessageProto> c = new Comparator<GroupChatMessageProto>() {
//  	    @Override
//  	    public int compare(GroupChatMessageProto o1, GroupChatMessageProto o2) {
//  	      if (o1.getTimeOfChat() < o2.getTimeOfChat()) {
//  	        return -1;
//  	      } else if (o1.getTimeOfChat() > o2.getTimeOfChat()) {
//  	        return 1;
//  	      } else {
//  	        return 0;
//  	      }
//  	    }
//  	  };*/
//  	Collections.sort(globalChats, new GroupChatComparator());
//  	// Need to add them in reverse order
//  	for (int i = 0; i < globalChats.size(); i++) {
//  		resBuilder.addGlobalChats(globalChats.get(i));
//  	}
//
//  	if (user.getClanId() <= 0) {
//  		return;
//  	}
//  	final int limit = ControllerConstants.RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP;
//  	final List<ClanChatPost> activeClanChatPosts = ClanChatPostRetrieveUtils
//  			.getMostRecentClanChatPostsForClan(limit , user.getClanId());
//
//  	if ((null == activeClanChatPosts) || activeClanChatPosts.isEmpty()) {
//  		return;
//  	}  		
//  	final List<Integer> userUuids = new ArrayList<Integer>();
//  	for (final ClanChatPost p : activeClanChatPosts) {
//  		userUuids.add(p.getPosterId());
//  	}
//  	//!!!!!!!!!!!RETRIEVE BUNCH OF USERS REQUEST
//  	Map<Integer, User> usersByUuids = null;
//  	if (userUuids.size() > 0) {
//  		usersByUuids = RetrieveUtils.userRetrieveUtils().getUsersByUuids(userUuids);
//  		for (int i = activeClanChatPosts.size() - 1; i >= 0; i--) {
//  			final ClanChatPost pwp = activeClanChatPosts.get(i);
//  			resBuilder.addClanChats(CreateInfoProtoUtils
//  					.createGroupChatMessageProtoFromClanChatPost(pwp,
//  							usersByUuids.get(pwp.getPosterId())));
//  		}
//  	}
//  }
//
//  private void setPrivateChatPosts(final Builder resBuilder, final User aUser, final String userUuid) {
//    boolean isRecipient = true;
//    Map<Integer, Integer> userIdsToPrivateChatPostUuids = null;
//    final Map<Integer, PrivateChatPost> postUuidsToPrivateChatPosts = new HashMap<Integer, PrivateChatPost>();
//    Map<Integer, User> userUuidsToUsers = null;
//    Map<Integer, Set<Integer>> clanUuidsToUserIdSet = null;
//    Map<Integer, Clan> clanUuidsToClans = null;
//    final List<Integer> clanlessUserUuids = new ArrayList<Integer>();
//    final List<Integer> clanIdList = new ArrayList<Integer>();
//    final List<Integer> privateChatPostUuids = new ArrayList<Integer>();
//
//    //get all the most recent posts sent to this user
//    final Map<Integer, PrivateChatPost> postsUserReceived = 
//        PrivateChatPostRetrieveUtils.getMostRecentPrivateChatPostsByOrToUser(
//            userUuid, isRecipient, ControllerConstants.STARTUP__MAX_PRIVATE_CHAT_POSTS_RECEIVED);
//
//    //get all the most recent posts this user sent
//    isRecipient = false;
//    final Map<Integer, PrivateChatPost> postsUserSent = 
//        PrivateChatPostRetrieveUtils.getMostRecentPrivateChatPostsByOrToUser(
//            userUuid, isRecipient, ControllerConstants.STARTUP__MAX_PRIVATE_CHAT_POSTS_SENT);
//
//    if (((null == postsUserReceived) || postsUserReceived.isEmpty()) &&
//        ((null == postsUserSent) || postsUserSent.isEmpty()) ) {
//      LOG.info("user has no private chats. aUser=" + aUser);
//      return;
//    }
//
//    //link other users with private chat posts and combine all the posts
//    //linking is done to select only the latest post between the duple (userUuid, otherUserId)
//    userIdsToPrivateChatPostUuids = aggregateOtherUserUuidsAndPrivateChatPost(postsUserReceived, postsUserSent, postUuidsToPrivateChatPosts);
//
//    if ((null != userIdsToPrivateChatPostUuids) && !userIdsToPrivateChatPostUuids.isEmpty()) {
//      //retrieve all users
//      final List<Integer> userIdList = new ArrayList<Integer>();
//      userIdList.addAll(userIdsToPrivateChatPostUuids.keySet());
//      userIdList.add(userUuid); //userIdsToPrivateChatPostUuids contains userUuids other than 'this' userUuid
//      //!!!!!!!!!!!RETRIEVE BUNCH OF USERS REQUEST
//      userUuidsToUsers = RetrieveUtils.userRetrieveUtils().getUsersByUuids(userIdList);
//    } else {
//      //user did not send any nor received any private chat posts
//      LOG.error("(not really error) aggregating private chat post ids returned nothing, noob user?");
//      return;
//    }
//    if ((null == userUuidsToUsers) || userUuidsToUsers.isEmpty() ||
//        (userUuidsToUsers.size() == 1)) {
//      LOG.error("unexpected error: perhaps user talked to himself. postsUserReceved="
//          + postsUserReceived + ", postsUserSent=" + postsUserSent + ", aUser=" + aUser);
//      return;
//    }
//
//    //get all the clans for the users (a map: clanId->set(userUuid))
//    //put the clanless users in the second argument: userUuidsToClanlessUsers
//    clanUuidsToUserIdSet = determineClanUuidsToUserIdSet(userUuidsToUsers, clanlessUserUuids);
//    if ((null != clanUuidsToUserIdSet) && !clanUuidsToUserIdSet.isEmpty()) {
//      clanIdList.addAll(clanUuidsToUserIdSet.keySet());
//      //retrieve all clans for the users
//      clanUuidsToClans = ClanRetrieveUtils.getClansByUuids(clanIdList);
//    }
//
//
//    //create the protoList
//    privateChatPostUuids.addAll(userIdsToPrivateChatPostUuids.values());
//    final List<PrivateChatPostProto> pcppList = CreateInfoProtoUtils.createPrivateChatPostProtoList(
//        clanUuidsToClans, clanUuidsToUserIdSet, userUuidsToUsers, clanlessUserUuids, privateChatPostUuids,
//        postUuidsToPrivateChatPosts);
//
//    resBuilder.addAllPcpp(pcppList);
//  }
//  
//  private Map<Integer, Integer> aggregateOtherUserUuidsAndPrivateChatPost(
//      final Map<Integer, PrivateChatPost> postsUserReceived, final Map<Integer, PrivateChatPost> postsUserSent,
//      final Map<Integer, PrivateChatPost> postUuidsToPrivateChatPosts) {
//    final Map<Integer, Integer> userIdsToPrivateChatPostUuids = new HashMap<Integer, Integer>();
//
//    //go through the posts specific user received
//    if ((null != postsUserReceived) && !postsUserReceived.isEmpty()) {
//      for (final int pcpId : postsUserReceived.keySet()) {
//        final PrivateChatPost postUserReceived = postsUserReceived.get(pcpId);
//        final int senderId = postUserReceived.getPosterId();
//
//        //record that the other user and specific user chatted
//        userIdsToPrivateChatPostUuids.put(senderId, pcpId);
//      }
//      //combine all the posts together
//      postUuidsToPrivateChatPosts.putAll(postsUserReceived);
//    }
//
//    if ((null != postsUserSent) && !postsUserSent.isEmpty()) {
//      //go through the posts user sent
//      for (final int pcpId: postsUserSent.keySet()) {
//        final PrivateChatPost postUserSent = postsUserSent.get(pcpId);
//        final int recipientId = postUserSent.getRecipientId();
//
//        //determine the latest post between other recipientId and specific user
//        if (!userIdsToPrivateChatPostUuids.containsKey(recipientId)) {
//          //didn't see this user id yet, record it
//          userIdsToPrivateChatPostUuids.put(recipientId, pcpId);
//
//        } else {
//          //recipientId sent something to specific user, choose the latest one
//          final int postIdUserReceived = userIdsToPrivateChatPostUuids.get(recipientId);
//          //postsUserReceived can't be null here
//          final PrivateChatPost postUserReceived = postsUserReceived.get(postIdUserReceived);
//
//          final Date newDate = postUserSent.getTimeOfPost();
//          final Date existingDate = postUserReceived.getTimeOfPost();
//          if (newDate.getTime() > existingDate.getTime()) {
//            //since postUserSent's time is later, choose this post for recipientId
//            userIdsToPrivateChatPostUuids.put(recipientId, pcpId);
//          }
//        }
//      }
//
//      //combine all the posts together
//      postUuidsToPrivateChatPosts.putAll(postsUserSent);
//    }
//    return userIdsToPrivateChatPostUuids;
//  }
//  
//  private Map<Integer, Set<Integer>> determineClanUuidsToUserIdSet(final Map<Integer, User> userUuidsToUsers,
//      final List<Integer> clanlessUserUserUuids) {
//    final Map<Integer, Set<Integer>> clanUuidsToUserIdSet = new HashMap<Integer, Set<Integer>>();
//    if ((null == userUuidsToUsers)  || userUuidsToUsers.isEmpty()) {
//      return clanUuidsToUserIdSet;
//    }
//    //go through users and lump them by clan id
//    for (final String userUuid : userUuidsToUsers.keySet()) {
//      final User u = userUuidsToUsers.get(userUuid);
//      final int clanId = u.getClanId();
//      if (ControllerConstants.NOT_SET == clanId) {
//        clanlessUserUserUuids.add(userUuid);
//        continue;	      
//      }
//
//      if (clanUuidsToUserIdSet.containsKey(clanId)) {
//        //clan id exists, add userUuid in with others
//        final Set<Integer> userIdSet = clanUuidsToUserIdSet.get(clanId);
//        userIdSet.add(userUuid);
//      } else {
//        //clan id doesn't exist, create new grouping of userUuids
//        final Set<Integer> userIdSet = new HashSet<Integer>();
//        userIdSet.add(userUuid);
//
//        clanUuidsToUserIdSet.put(clanId, userIdSet);
//      }
//    }
//    return clanUuidsToUserIdSet;
//  }
//
//
//  private void setUserMonsterStuff(final Builder resBuilder, final String userUuid) {
//    final List<MonsterForUser> userMonsters= RetrieveUtils.monsterForUserRetrieveUtils()
//        .getMonstersForUser(userUuid);
//    
//    if ((null != userMonsters) && !userMonsters.isEmpty()) {
//    	for (final MonsterForUser mfu : userMonsters) {
//    		final FullUserMonsterProto fump = CreateInfoProtoUtils.createFullUserMonsterProtoFromUserMonster(mfu);
//    		resBuilder.addUsersMonsters(fump);
//    	}
//    }
//    
//    //monsters in healing
//    final Map<Long, MonsterHealingForUser> userMonstersHealing = MonsterHealingForUserRetrieveUtils
//    		.getMonstersForUser(userUuid);
//    if ((null != userMonstersHealing) && !userMonstersHealing.isEmpty()) {
//
//    	final Collection<MonsterHealingForUser> healingMonsters = userMonstersHealing.values();
//    	for (final MonsterHealingForUser mhfu : healingMonsters) {
//    		final UserMonsterHealingProto umhp = CreateInfoProtoUtils.createUserMonsterHealingProtoFromObj(mhfu);
//    		resBuilder.addMonstersHealing(umhp);
//    	}
//    }
//    
//    //enhancing monsters
//    final Map<Long, MonsterEnhancingForUser> userMonstersEnhancing = MonsterEnhancingForUserRetrieveUtils
//    		.getMonstersForUser(userUuid);
//    if ((null != userMonstersEnhancing) && !userMonstersEnhancing.isEmpty()) {
//    	//find the monster that is being enhanced
//    	final Collection<MonsterEnhancingForUser> enhancingMonsters = userMonstersEnhancing.values();
//    	UserEnhancementItemProto baseMonster = null;
//    	
//    	final List<UserEnhancementItemProto> feeders = new ArrayList<UserEnhancementItemProto>();
//    	for (final MonsterEnhancingForUser mefu : enhancingMonsters) {
//    		final UserEnhancementItemProto ueip = CreateInfoProtoUtils.createUserEnhancementItemProtoFromObj(mefu);
//    		
//    		//TODO: if user has no monsters with null start time
//    		//(if user has all monsters with a start time), or if user has more than one
//    		//monster with a null start time
//    		//STORE THEM AND DELETE THEM OR SOMETHING
//    		
//    		//search for the monster that is being enhanced, the one with null start time
//    		final Date startTime = mefu.getExpectedStartTime();
//    		if(null == startTime) {
//    			//found him
//    			baseMonster = ueip;
//    		} else {
//    			//just a feeder, add him to the list
//    			feeders.add(ueip);
//    		}
//    	}
//    	
//    	final UserEnhancementProto uep = CreateInfoProtoUtils.createUserEnhancementProtoFromObj(
//    			userUuid, baseMonster, feeders);
//    	
//    	resBuilder.setEnhancements(uep);
//    }
//    
//    //evolving monsters
//    final Map<Long, MonsterEvolvingForUser> userMonsterEvolving = MonsterEvolvingForUserRetrieveUtils
//    		.getCatalystUuidsToEvolutionsForUser(userUuid);
//    if ((null != userMonsterEvolving) && !userMonsterEvolving.isEmpty()) {
//    	
//    	for (final MonsterEvolvingForUser mefu : userMonsterEvolving.values()) {
//    		final UserMonsterEvolutionProto uep = CreateInfoProtoUtils
//    				.createUserEvolutionProtoFromEvolution(mefu);
//    		
//    		//TODO: NOTE THAT IF MORE THAN ONE EVOLUTION IS ALLLOWED AT A TIME, THIS METHOD
//    		//CALL NEEDS TO CHANGE
//    		resBuilder.setEvolution(uep);
//    	}
//    }
//    
//    
//  }
//
//  private void setBoosterPurchases(final StartupResponseProto.Builder resBuilder) {
//    final Iterator<RareBoosterPurchaseProto> it = goodEquipsRecievedFromBoosterPacks.iterator();
//    final List<RareBoosterPurchaseProto> boosterPurchases = new ArrayList<RareBoosterPurchaseProto>();
//    while (it.hasNext()) {
//      boosterPurchases.add(it.next());
//    }
//
//    final Comparator<RareBoosterPurchaseProto> c = new Comparator<RareBoosterPurchaseProto>() {
//      @Override
//      public int compare(final RareBoosterPurchaseProto o1, final RareBoosterPurchaseProto o2) {
//        if (o1.getTimeOfPurchase() < o2.getTimeOfPurchase()) {
//          return -1;
//        } else if (o1.getTimeOfPurchase() > o2.getTimeOfPurchase()) {
//          return 1;
//        } else {
//          return 0;
//        }
//      }
//    };
//    Collections.sort(boosterPurchases, c);
//    // Need to add them in reverse order
//    for (int i = 0; i < boosterPurchases.size(); i++) {
//      resBuilder.addRareBoosterPurchases(boosterPurchases.get(i));
//    }
//  }  
//  
//  private void setFacebookAndExtraSlotsStuff(final Builder resBuilder, final User thisUser, final String userUuid) {
//  	//gather up data so as to make only one user retrieval query
//  	
//  	//get the invites where this user is the recipient, get unaccepted, hence, unredeemed invites
//  	Map<Integer, UserFacebookInviteForSlot> idsToInvitesToMe = new HashMap<Integer, UserFacebookInviteForSlot>();
//  	final String fbId = thisUser.getFacebookId();
//  	final List<Integer> specificInviteUuids = null;
//  	final boolean filterByAccepted = true;
//  	boolean isAccepted = false;
//  	final boolean filterByRedeemed = false;
//  	final boolean isRedeemed = false; //doesn't matter
//  	//base case where user does not have facebook id
//  	if ((null != fbId) && !fbId.isEmpty()) {
//  		idsToInvitesToMe = UserFacebookInviteForSlotRetrieveUtils
//  				.getSpecificOrAllInvitesForRecipient(fbId, specificInviteUuids, filterByAccepted,
//  						isAccepted, filterByRedeemed, isRedeemed);
//  	}
//  	
//  	//get the invites where this user is the inviter, get accepted, unredeemed does not matter 
//  	isAccepted = true;
//  	final Map<Integer, UserFacebookInviteForSlot> idsToInvitesFromMe = 
//  			UserFacebookInviteForSlotRetrieveUtils.getSpecificOrAllInvitesForInviter(
//  					userUuid, specificInviteUuids, filterByAccepted, isAccepted, filterByRedeemed, isRedeemed);
//  	
//  	final List<String> recipientFacebookIds = getRecipientFbUuids(idsToInvitesFromMe);
//  	
//  	//to make it easier later on, get the inviter ids for these invites and
//  	//map inviter id to an invite
//  	final Map<Integer, UserFacebookInviteForSlot> inviterUuidsToInvites =
//  			new HashMap<Integer, UserFacebookInviteForSlot>();
//  	//inviterUuidsToInvites will be populated by getInviterUuids(...)
//  	final List<Integer> inviterUserUuids = getInviterUuids(idsToInvitesToMe, inviterUuidsToInvites);
//  	
//  	
//  	//base case where user never did any invites
//  	if (((null == recipientFacebookIds) || recipientFacebookUuids.isEmpty()) &&
//  			((null == inviterUserUuids) || inviterUserUuids.isEmpty())) {
//  		//no facebook stuff
//  		return;
//  	}
//  	
//  	//!!!!!!!!!!!RETRIEVE BUNCH OF USERS REQUEST
//  	//GET THE USERS
//  	final Map<Integer, User> idsToUsers = RetrieveUtils.userRetrieveUtils()
//  			.getUsersForFacebookIdsOrUserUuids(recipientFacebookIds, inviterUserUuids);
//  	final List<User> recipients = new ArrayList<User>();
//  	final List<User> inviters = new ArrayList<User>();
//  	separateUsersIntoRecipientsAndInviters(idsToUsers, recipientFacebookIds,
//  			inviterUserUuids, recipients, inviters);
//  	
//  	
//  	//send all the invites where this user is the one being invited
//  	for (final Integer inviterId : inviterUserUuids) {
//  		final User inviter = idsToUsers.get(inviterId);
//  		final MinimumUserProtoWithFacebookId inviterProto = null;
//  		final UserFacebookInviteForSlot invite = inviterUuidsToInvites.get(inviterId);
//  		final UserFacebookInviteForSlotProto inviteProto = CreateInfoProtoUtils
//  				.createUserFacebookInviteForSlotProtoFromInvite(invite, inviter, inviterProto);
//  		
//  		resBuilder.addInvitesToMeForSlots(inviteProto);
//  	}
//  	
//  	//send all the invites where this user is the one inviting
//  	final MinimumUserProtoWithFacebookId thisUserProto = CreateInfoProtoUtils
//  			.createMinimumUserProtoWithFacebookIdFromUser(thisUser);
//  	for (final UserFacebookInviteForSlot invite : idsToInvitesFromMe.values()) {
//  		final UserFacebookInviteForSlotProto inviteProto = CreateInfoProtoUtils
//  				.createUserFacebookInviteForSlotProtoFromInvite(invite, thisUser, thisUserProto);
//  		resBuilder.addInvitesFromMeForSlots(inviteProto);
//  	}
//  }
//  
//  private List<String> getRecipientFbUuids(final Map<Integer, UserFacebookInviteForSlot> idsToInvitesFromMe) {
//  	final List<String> fbUuids = new ArrayList<String>();
//  	for (final UserFacebookInviteForSlot invite : idsToInvitesFromMe.values()) {
//  		final String fbId = invite.getRecipientFacebookId();
//  		fbUuids.add(fbId);
//  	}
//  	return fbUuids;
//  }
//  
//  //inviterUuidsToInvites will be populated
//  private List<Integer> getInviterUuids(final Map<Integer, UserFacebookInviteForSlot> idsToInvites,
//  		final Map<Integer, UserFacebookInviteForSlot> inviterUuidsToInvites) {
//  	
//  	final List<Integer> inviterUuids = new ArrayList<Integer>(); 
//  	for (final UserFacebookInviteForSlot invite : idsToInvites.values()) {
//  		final String userUuid = invite.getInviterUserId();
//  		inviterUuids.add(userUuid);
//  		
//  		inviterUuidsToInvites.put(userUuid, invite);
//  	}
//  	return inviterUuids;
//  }
//  
//  //given map of userUuids to users, list of recipient facebook ids and list of inviter
//  //user ids, separate the map of users into recipient and inviter
//  private void separateUsersIntoRecipientsAndInviters(final Map<Integer, User> idsToUsers,
//  		final List<String> recipientFacebookIds, final List<Integer> inviterUserUuids,
//  		final List<User> recipients, final List<User> inviters) {
//  	
//  	final Set<String> recipientFacebookIdsSet = new HashSet<String>(recipientFacebookUuids);
//  	
//  	//set the recipients
//  	for (final Integer userUuid : idsToUsers.keySet()) {
//  		final User u = idsToUsers.get(userUuid);
//  		final String facebookId = u.getFacebookId();
//  		
//  		if ((null != facebookId) && recipientFacebookIdsSet.contains(facebookId)) {
//  			//this is a recipient
//  			recipients.add(u);
//  		}
//  	}
//  	
//  	//set the inviters
//  	for (final Integer inviterId : inviterUserUuids) {
//  		if (idsToUsers.containsKey(inviterId)) {
//  			final User u = idsToUsers.get(inviterId);
//  			inviters.add(u);
//  		}
//  	}
//  	
//  }
//  
//  
//  private void setTaskStuff(final Builder resBuilder, final String userUuid) {
//  	final List<Integer> taskUuids = TaskForUserCompletedRetrieveUtils
//  			.getAllTaskUuidsForUser(userUuid);
//  	resBuilder.addAllCompletedTaskUuids(taskUuids);
//  	
//  	final TaskForUserOngoing aTaskForUser = TaskForUserOngoingRetrieveUtils
//  			.getUserTaskForUserUuid(userUuid);
//    if(null != aTaskForUser) {
//      LOG.warn("user has incompleted task userTask=" + aTaskForUser);
//      setOngoingTask(resBuilder, userUuid, aTaskForUser);
//    }
//  }
//  
//  private void setOngoingTask(final Builder resBuilder, final String userUuid,
//		  final TaskForUserOngoing aTaskForUser) {
//	  try {
//		  final MinimumUserTaskProto mutp = CreateInfoProtoUtils.createMinimumUserTaskProto(
//				  userUuid, aTaskForUser);
//		  resBuilder.setCurTask(mutp);
//
//		  //create protos for stages
//		  final long userTaskId = aTaskForUser.getId();
//		  final List<TaskStageForUser> taskStages = TaskStageForUserRetrieveUtils
//				  .getTaskStagesForUserWithTaskForUserUuid(userTaskId);
//		  
//		  //group taskStageForUsers by stage nums because more than one
//		  //taskStageForUser with the same stage num means this stage
//		  //has more than one monster
//		  final Map<Integer, List<TaskStageForUser>> stageNumToTsfu =
//				  new HashMap<Integer, List<TaskStageForUser>>();
//		  for (final TaskStageForUser tsfu : taskStages) {
//			  final int stageNum = tsfu.getStageNum();
//			  
//			  if (!stageNumToTsfu.containsKey(stageNum)) {
//				  final List<TaskStageForUser> a = new ArrayList<TaskStageForUser>(); 
//				  stageNumToTsfu.put(stageNum, a);
//			  }
//			  
//			  final List<TaskStageForUser> tsfuList = stageNumToTsfu.get(stageNum);
//			  tsfuList.add(tsfu);
//		  }
//		  
//		  //now that we have grouped all the monsters in their corresponding
//		  //task stages, protofy them
//		  final int taskId = aTaskForUser.getTaskId();
//		  for (final Integer stageNum : stageNumToTsfu.keySet()) {
//			  final List<TaskStageForUser> monsters = stageNumToTsfu.get(stageNum);
//			  
//			  final TaskStageProto tsp = CreateInfoProtoUtils.createTaskStageProto(
//					  taskId, stageNum, monsters);
//			  resBuilder.addCurTaskStages(tsp);
//		  }
//		  
//		  
//	  } catch (final Exception e) {
//		  LOG.error("could not create existing user task, letting it get" +
//		  		" deleted when user starts another task.", e);
//	  }
//  }
//  
//  private void setAllStaticData(final Builder resBuilder, final String userUuid, final boolean userIdSet) {
//  	final StaticDataProto sdp = MiscMethods.getAllStaticData(userUuid, userIdSet);
//  	
//  	resBuilder.setStaticDataStuffProto(sdp);
//  }
//  
//  private void setEventStuff(final Builder resBuilder, final String userUuid) {
//  	final List<EventPersistentForUser> events = EventPersistentForUserRetrieveUtils
//  			.getUserPersistentEventForUserUuid(userUuid);
//  	
//  	for (final EventPersistentForUser epfu : events) {
//  		final UserPersistentEventProto upep = CreateInfoProtoUtils.createUserPersistentEventProto(epfu);
//  		resBuilder.addUserEvents(upep);
//  	}
//  	
//  }
//  
//  private PvpLeagueForUser pvpBattleStuff(final Builder resBuilder, final User user, final String userUuid,
//		  final boolean isFreshRestart, final Timestamp battleEndTime) {
//	  
////	  PvpLeagueForUser plfu = setPvpLeagueInfo(resBuilder, userUuid);
//	  //TODO: should I be doing this?
//	  final PvpLeagueForUser plfu = getPvpLeagueForUserRetrieveUtil()
//			  .getUserPvpLeagueForId(userUuid);
//	  
//	  final PvpUser pu = new PvpUser(plfu);
//	  getHazelcastPvpUtil().replacePvpUser(pu, userUuid);
//	  
//  	if (!isFreshRestart) {
//  		LOG.info("not fresh restart, so not deleting pvp battle stuff");
//  		return plfu;
//  	}
//  	
//  	//if bool isFreshRestart is true, then deduct user's elo by amount specified in
//  	//the table (pvp_battle_for_user), since user auto loses
//  	final PvpBattleForUser battle = PvpBattleForUserRetrieveUtils
//  			.getPvpBattleForUserForAttacker(userUuid);
//  	
//  	if (null == battle) {
//  		return plfu;
//  	}
//  	final Timestamp battleStartTime = new Timestamp(battle.getBattleStartTime().getTime());
//  	//capping max elo attacker loses
//  	int eloAttackerLoses = battle.getAttackerLoseEloChange();
//  	if ((plfu.getElo() + eloAttackerLoses) < 0) {
//  		eloAttackerLoses = -1 * plfu.getElo();
//  	}
//
//  	final int defenderId = battle.getDefenderId();
//  	final int eloDefenderWins = battle.getDefenderLoseEloChange();
//  	
//  	//user has unfinished battle, reward defender and penalize attacker
//  	penalizeUserForLeavingGameWhileInPvp(userUuid, user, plfu, defenderId,
//  			eloAttackerLoses, eloDefenderWins, battleEndTime, battleStartTime, battle);
//  	return plfu;
//  }
//  
//  private void penalizeUserForLeavingGameWhileInPvp(final String userUuid, final User user, 
//		  final PvpLeagueForUser attackerPlfu, final int defenderId,
//		  final int eloAttackerLoses, final int eloDefenderWins, final Timestamp battleEndTime,
//		  final Timestamp battleStartTime, final PvpBattleForUser battle) {
//	  //NOTE: this lock ordering might result in a temp deadlock
//	  //doesn't reeeally matter if can't penalize defender...
//
//	  //only lock real users
//	  if (0 != defenderId) {
//		  getLocker().lockPlayer(defenderId, this.getClass().getSimpleName());
//	  }
//	  try {
//		  final int attackerEloBefore = attackerPlfu.getElo();
//		  int defenderEloBefore = 0;
//		  final int attackerPrevLeague = attackerPlfu.getPvpLeagueId();
//		  int attackerCurLeague = 0;
//		  int defenderPrevLeague = 0;
//		  int defenderCurLeague = 0;
//		  final int attackerPrevRank = attackerPlfu.getRank();
//		  int attackerCurRank = 0;
//		  int defenderPrevRank = 0;
//		  int defenderCurRank = 0;
//				  
//		  //update hazelcast map and ready arguments for pvp battle history
//		  final int attackerCurElo = attackerPlfu.getElo() + eloAttackerLoses;
//		  attackerCurLeague = PvpLeagueRetrieveUtils.getLeagueIdForElo(
//				  attackerCurElo, false, attackerPrevLeague);
//		  attackerCurRank = PvpLeagueRetrieveUtils.getRankForElo(
//				  attackerCurElo, attackerCurLeague);
//		  
//		  final int attacksLost = attackerPlfu.getAttacksLost() + 1;
//		  
//		  //update attacker
//		  //don't update his shields, just elo
//		  int numUpdated = UpdateUtils.get().updatePvpLeagueForUser(userUuid,
//				  attackerCurLeague, attackerCurRank, eloAttackerLoses,
//				  null, null, 0, 0, 1, 0);
//		  
//		  LOG.info("num updated when changing attacker's elo because of reset=" + numUpdated);
//		  attackerPlfu.setElo(attackerCurElo);
//		  attackerPlfu.setPvpLeagueId(attackerCurLeague);
//		  attackerPlfu.setRank(attackerCurRank);
//		  attackerPlfu.setAttacksLost(attacksLost);
//		  final PvpUser attackerPu = new PvpUser(attackerPlfu);
//		  getHazelcastPvpUtil().replacePvpUser(attackerPu, userUuid);
//
//		  //update defender if real, TODO: might need to cap defenderElo
//		  if (0 != defenderId) {
//			  final PvpLeagueForUser defenderPlfu = getPvpLeagueForUserRetrieveUtil()
//					  .getUserPvpLeagueForId(defenderId);
//			  
//			  defenderEloBefore = defenderPlfu.getElo();
//			  defenderPrevLeague = defenderPlfu.getPvpLeagueId();
//			  defenderPrevRank = defenderPlfu.getRank(); 
//			  //update hazelcast map and ready arguments for pvp battle history
//			  final int defenderCurElo = defenderEloBefore + eloDefenderWins;
//			  defenderCurLeague = PvpLeagueRetrieveUtils.getLeagueIdForElo(
//					  defenderCurElo, false, defenderPrevLeague);
//			  defenderCurRank = PvpLeagueRetrieveUtils.getRankForElo(
//					  defenderCurElo, defenderCurLeague);
//			  
//			  final int defensesWon = defenderPlfu.getDefensesWon() + 1;
//
//			  numUpdated = UpdateUtils.get().updatePvpLeagueForUser(defenderId,
//					  defenderCurLeague, defenderCurRank, eloDefenderWins,
//					  null, null, 0, 1, 0, 0);
//			  LOG.info("num updated when changing defender's elo because of reset=" + numUpdated);
//
//			  
//			  defenderPlfu.setElo(defenderCurElo);
//			  defenderPlfu.setPvpLeagueId(defenderCurLeague);
//			  defenderPlfu.setRank(defenderCurRank);
//			  defenderPlfu.setDefensesWon(defensesWon);
//			  final PvpUser defenderPu = new PvpUser(defenderPlfu);
//			  getHazelcastPvpUtil().replacePvpUser(defenderPu, defenderId);
//		  }
//		  
//		  final boolean attackerWon = false;
//		  final boolean cancelled = false;
//		  final boolean defenderGotRevenge = false;
//		  final boolean displayToDefender = true;
//		  final int numInserted = InsertUtils.get().insertIntoPvpBattleHistory(userUuid,
//				  defenderId, battleEndTime, battleStartTime, eloAttackerLoses,
//				  attackerEloBefore, eloDefenderWins, defenderEloBefore,
//				  attackerPrevLeague, attackerCurLeague, defenderPrevLeague,
//				  defenderCurLeague, attackerPrevRank, attackerCurRank,
//				  defenderPrevRank, defenderCurRank, 0, 0, 0, 0, attackerWon,
//				  cancelled, defenderGotRevenge, displayToDefender);
//		  
//		  LOG.info("numInserted into battle history=" + numInserted);
//		  //delete that this battle occurred
//		  final int numDeleted = DeleteUtils.get().deletePvpBattleForUser(userUuid);
//		  LOG.info("successfully penalized, rewarded attacker and defender respectively. battle= " +
//				  battle + "\t numDeleted=" + numDeleted);
//
//	  } catch (final Exception e){
//		  LOG.error("tried to penalize, reward attacker and defender respectively. battle=" +
//				  battle, e);
//	  } finally {
//		  if (0 != defenderId) {
//			  getLocker().unlockPlayer(defenderId, this.getClass().getSimpleName());
//		  }
//	  }
//  }
//  
//  private void pvpBattleHistoryStuff(final Builder resBuilder, final User user, final String userUuid) {
//  	final int n = ControllerConstants.PVP_HISTORY__NUM_RECENT_BATTLES;
//  	
//  	//NOTE: AN ATTACKER MIGHT SHOW UP MORE THAN ONCE DUE TO REVENGE
//  	final List<PvpBattleHistory> historyList = getPvpBattleHistoryRetrieveUtil()
//  			.getRecentNBattlesForDefenderId(userUuid, n);
//  	LOG.info("historyList=" + historyList);
//  	
//  	final Set<Integer> attackerUuids = getPvpBattleHistoryRetrieveUtil()
//  			.getAttackerUuidsFromHistory(historyList);
//  	LOG.info("attackerUuids=" + attackerUuids);
//  	
//  	if ((null == attackerUuids) || attackerUuids.isEmpty()) {
//  		LOG.info("no valid 10 pvp battles for user. ");
//  		return;
//  	}
//  	//!!!!!!!!!!!RETRIEVE BUNCH OF USERS REQUEST
//  	final Map<Integer, User> idsToAttackers = RetrieveUtils.userRetrieveUtils()
//  			.getUsersByUuids(attackerUuids);
//  	LOG.info("idsToAttackers=" + idsToAttackers);
//
//  	final List<Integer> attackerUuidsList = new ArrayList<Integer>(idsToAttackers.keySet());
//  	final Map<Integer, List<MonsterForUser>> attackerIdToCurTeam = selectMonstersForUsers(
//  			attackerUuidsList);
//  	LOG.info("history monster teams=" + attackerIdToCurTeam);
//
//  	final Map<Integer, Integer> attackerUuidsToProspectiveCashWinnings = MiscMethods
//  			.calculateCashRewardFromPvpUsers(idsToAttackers);
//  	final Map<Integer, Integer> attackerUuidsToProspectiveOilWinnings = MiscMethods
//  			.calculateOilRewardFromPvpUsers(idsToAttackers);
//
//  	final List<PvpHistoryProto> historyProtoList = CreateInfoProtoUtils
//  			.createPvpHistoryProto(historyList, idsToAttackers, attackerIdToCurTeam,
//  					attackerUuidsToProspectiveCashWinnings, attackerUuidsToProspectiveOilWinnings);
//
////  	LOG.info("historyProtoList=" + historyProtoList);
//  	resBuilder.addAllRecentNBattles(historyProtoList);
//  }
//  
//  //SOOOOOO DISGUSTING.............ALL THIS FUCKING CODE. SO GROSS.
//  //COPIED FROM QueueUpController;
//  //given users, get the 3 monsters for each user
//	private Map<Integer, List<MonsterForUser>> selectMonstersForUsers(
//			final List<Integer> userIdList) {
//		
//		//return value
//		final Map<Integer, List<MonsterForUser>> userUuidsToUserMonsters =
//				new HashMap<Integer, List<MonsterForUser>>();
//		
//		//for all these users, get all their complete monsters
//		final Map<Integer, Map<Long, MonsterForUser>> userIdsToMfuUuidsToMonsters = RetrieveUtils
//				.monsterForUserRetrieveUtils().getCompleteMonstersForUser(userIdList);
//		
//		
//		for (int index = 0; index < userIdList.size(); index++) {
//			//extract a user's monsters
//			final int defenderId = userIdList.get(index);
//			final Map<Long, MonsterForUser> mfuUuidsToMonsters = userIdsToMfuUuidsToMonsters.get(defenderId);
//			
//			if ((null == mfuUuidsToMonsters) || mfuUuidsToMonsters.isEmpty()) {
//				LOG.error("WTF!!!!!!!! user has no monsters!!!!! userUuid=" + defenderId +
//						"\t will move on to next guy.");
//				continue;
//			}
//			//try to select at most 3 monsters for this user
//			final List<MonsterForUser> defenderMonsters = selectMonstersForUser(mfuUuidsToMonsters);
//			
//			//if the user still doesn't have 3 monsters, then too bad
//			userUuidsToUserMonsters.put(defenderId, defenderMonsters);
//		}
//		
//		return userUuidsToUserMonsters;
//	}
//	private List<MonsterForUser> selectMonstersForUser(final Map<Long, MonsterForUser> mfuUuidsToMonsters) {
//
//		//get all the monsters the user has on a team (at the moment, max is 3)
//		final List<MonsterForUser> defenderMonsters = getEquippedMonsters(mfuUuidsToMonsters);
//
//		//so users can have no monsters equipped, so just choose one fucking monster for him
//		if (defenderMonsters.isEmpty()) {
//			final List<MonsterForUser> randMonsters = new ArrayList<MonsterForUser>(
//					mfuUuidsToMonsters.values());
//			defenderMonsters.add(randMonsters.get(0));
//		}
//		
//		return defenderMonsters;
//	}
//	private List<MonsterForUser> getEquippedMonsters(final Map<Long, MonsterForUser> userMonsters) {
//		final List<MonsterForUser> equipped = new ArrayList<MonsterForUser>();
//		
//		for (final MonsterForUser mfu : userMonsters.values()) {
//			if (mfu.getTeamSlotNum() <= 0) {
//				//only want equipped monsters
//				continue;
//			}
//			equipped.add(mfu);
//			
//		}
//		return equipped;
//	}
//
//	
//  
//  
//  private void setClanRaidStuff(final Builder resBuilder, final User user, final String userUuid, final Timestamp now) {
//  	final Date nowDate = new Date(now.getTime());
//  	final int clanId = user.getClanId();
//  	
//  	if (clanId <= 0) {
//  		return;
//  	}
//  	//get the clan raid information for the clan
//  	final ClanEventPersistentForClan cepfc = ClanEventPersistentForClanRetrieveUtils
//  			.getPersistentEventForClanId(clanId);
//  	
//  	if (null == cepfc) {
//  		LOG.info("no clan raid stuff existing for clan=" + clanId + "\t user=" + user);
//  		return;
//  	}
//  	
//  	final PersistentClanEventClanInfoProto pcecip = CreateInfoProtoUtils
//  			.createPersistentClanEventClanInfoProto(cepfc);
//  	resBuilder.setCurRaidClanInfo(pcecip);
//  	
//  	//get the clan raid information for all the clan users
//  	//shouldn't be null (per the retrieveUtils)
//  	final Map<Integer, ClanEventPersistentForUser> userIdToCepfu = ClanEventPersistentForUserRetrieveUtils
//  			.getPersistentEventUserInfoForClanId(clanId);
//  	LOG.info("the users involved in clan raid:" + userIdToCepfu);
//  	
//  	if ((null == userIdToCepfu) || userIdToCepfu.isEmpty()) {
//  		LOG.info("no users involved in clan raid. clanRaid=" + cepfc);
//  		return;
//  	}
//  	
//  	final List<Long> userMonsterUuids = MonsterStuffUtils.getUserMonsterUuidsInClanRaid(userIdToCepfu);
//  	
//  	//TODO: when retrieving clan info, and user's current teams, maybe query for 
//  	//these monsters as well
//  	final Map<Long, MonsterForUser> idsToUserMonsters = RetrieveUtils.monsterForUserRetrieveUtils()
//  			.getSpecificUserMonsters(userMonsterUuids);
//  	
//  	for (final ClanEventPersistentForUser cepfu : userIdToCepfu.values()) {
//  		final PersistentClanEventUserInfoProto pceuip = CreateInfoProtoUtils
//  				.createPersistentClanEventUserInfoProto(cepfu, idsToUserMonsters, null);
//  		resBuilder.addCurRaidClanUserInfo(pceuip);
//  	}
//  	
//  	setClanRaidHistoryStuff(resBuilder, userUuid, nowDate);
//  	
//  }
//  
//  private void setClanRaidHistoryStuff(final Builder resBuilder, final String userUuid, final Date nowDate) {
//   	
//  	//the raid stage and reward history for past 7 days
//  	final int nDays = ControllerConstants.CLAN_EVENT_PERSISTENT__NUM_DAYS_FOR_RAID_STAGE_HISTORY; 
//  	final Map<Date, CepfuRaidStageHistory> timesToRaidStageHistory =
//  			CepfuRaidStageHistoryRetrieveUtils.getRaidStageHistoryForPastNDaysForUserUuid(
//  					userUuid, nDays, nowDate, timeUtils);
//  	
//  	final Map<Date, List<ClanEventPersistentUserReward>> timesToUserRewards =
//  			ClanEventPersistentUserRewardRetrieveUtils.getCepUserRewardForPastNDaysForUserUuid(
//  					userUuid, nDays, nowDate, timeUtils);
//  	
//  	//possible for ClanRaidStageHistory to have no rewards if clan didn't beat stage
//  	for (final Date aDate : timesToRaidStageHistory.keySet()) {
//  		final CepfuRaidStageHistory cepfursh = timesToRaidStageHistory.get(aDate);
//  		List<ClanEventPersistentUserReward> rewards = null; 
//  		
//  		if (timesToUserRewards.containsKey(aDate)) {
//  			rewards = timesToUserRewards.get(aDate);
//  		}
//  		
//  		final PersistentClanEventRaidStageHistoryProto stageProto =
//  				CreateInfoProtoUtils.createPersistentClanEventRaidStageHistoryProto(cepfursh, rewards);
//  		
//  		resBuilder.addRaidStageHistory(stageProto);
//  	}
//  }
//  
//  private void setAchievementStuff(final Builder resBuilder, final String userUuid) {
//	  final Map<Integer, AchievementForUser> achievementIdToUserAchievements =
//			  getAchievementForUserRetrieveUtil()
//			  .getSpecificOrAllAchievementIdToAchievementForUserUuid(
//					  userUuid, null);
//	  
//	  for (final AchievementForUser afu : achievementIdToUserAchievements.values()) {
//		  final UserAchievementProto uap = CreateInfoProtoUtils
//				  .createUserAchievementProto(afu);
//		  resBuilder.addUserAchievements(uap);
//	  }
//  }
//  
//  private void setMiniJob(final Builder resBuilder, final String userUuid) {
//	  final Map<Long, MiniJobForUser> miniJobIdToUserMiniJobs =
//			  getMiniJobForUserRetrieveUtil()
//			  .getSpecificOrAllIdToMiniJobForUser(userUuid, null);
//	  
//	  if (miniJobIdToUserMiniJobs.isEmpty()) {
//		  return;
//	  }
//	  
//	  final List<MiniJobForUser> mjfuList = new ArrayList<MiniJobForUser>(
//			  miniJobIdToUserMiniJobs.values());
//	  final List<UserMiniJobProto> umjpList = CreateInfoProtoUtils
//			  .createUserMiniJobProtos(mjfuList, null);
//	  
//	  resBuilder.addAllUserMiniJobProtos(umjpList);
//  }
//
//  
//  
//  
//  
//  
//  
//  
//  
//  
//  
//  
//  
//  
//  
////  private void setAllBosses(Builder resBuilder, UserType type) {
////    Map<Integer, Monster> bossUuidsToBosses = 
////        MonsterRetrieveUtils.getBossUuidsToBosses();
////
////    for (Monster b : bossUuidsToBosses.values()) {
////      FullBossProto fbp =
////          CreateInfoProtoUtils.createFullBossProtoFromBoss(type, b);
////      resBuilder.addBosses(fbp);
////    }
////  }
//
//  // retrieve's the active leaderboard event prizes and rewards for the events
////  private void setLeaderboardEventStuff(StartupResponseProto.Builder resBuilder) {
////    resBuilder.addAllLeaderboardEvents(MiscMethods.currentTournamentEventProtos());
////  }
//
//  private void sendOfferChartInstall(final Date installTime, final String advertiserId) {
//    final String clientId = "15";
//    final String appId = "648221050";
//    final String geo = "N/A";
//    final String installTimeStr = ""+installTime.getTime();
//    final String devicePlatform = "iphone";
//    final String deviceType = "iphone";
//
//    final String urlString = "http://offerchart.com/mobileapp/api/send_install_ping?" +
//        "client_id="+clientId +
//        "&app_id="+appId +
//        "&device_id="+advertiserId +
//        "&device_type="+deviceType +
//        "&geo="+geo +
//        "&install_time="+installTimeStr +
//        "&device_platform="+devicePlatform;
//
//    LOG.info("Sending offerchart request:\n"+urlString);
//    final DefaultHttpClient httpclient = new DefaultHttpClient();
//    final HttpGet httpGet = new HttpGet(urlString);
//
//    try {
//      final HttpResponse response1 = httpclient.execute(httpGet);
//      final BufferedReader rd = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
//      String responseString = "";
//      String line;
//      while ((line = rd.readLine()) != null) {
//        responseString += line;
//      }
//      LOG.info("Received response: " + responseString);
//    } catch (final Exception e) {
//      LOG.error("failed to make offer chart call", e);
//    }
//  }
//
//  private String retrieveKabamNaid(final User user, final String openUdid, final String mac, final String advertiserId) {
//    String host;
//    final int port = 443;
//    int clientId;
//    String secret;
//    if (Globals.IS_SANDBOX()) {
//      host = KabamProperties.SANDBOX_API_URL;
//      clientId = KabamProperties.SANDBOX_CLIENT_ID;
//      secret = KabamProperties.SANDBOX_SECRET;
//    } else {
//      host = KabamProperties.PRODUCTION_API_URL;
//      clientId = KabamProperties.PRODUCTION_CLIENT_ID;
//      secret = KabamProperties.PRODUCTION_SECRET;
//    }
//
//    final KabamApi kabamApi = new KabamApi(host, port, secret);
//    final String userUuid = openUdid;
//    final String platform = "iphone";
//
//    final String biParams = "{\"open_udid\":\"" + userUuid + "\",\"mac\":\"" + mac
//        + "\",\"mac_hash\":\"" + DigestUtils.md5Hex(mac) + "\",\"advertiser_id\":\"" + advertiserId
//        + "\"}";
//
//    MobileNaidResponse naidResponse;
//    try {
//      naidResponse = kabamApi.mobileGetNaid(userUuid, clientId, platform, biParams,
//          new Date().getTime() / 1000);
//    } catch (final Exception e) {
//      e.printStackTrace();
//      return "";
//    }
//
//    if (naidResponse.getReturnCode() == ResponseCode.Success) {
//      if (user != null) {
//        user.updateSetKabamNaid(naidResponse.getNaid());
//      }
//      LOG.info("Successfully got kabam naid.");
//      return naidResponse.getNaid()+"";
//    } else {
//      LOG.error("Error retrieving kabam naid: " + naidResponse.getReturnCode());
//    }
//    return "";
//  }
//
//  private void setLockBoxEvents(final StartupResponseProto.Builder resBuilder, final User user) {
////    resBuilder.addAllLockBoxEvents(MiscMethods.currentLockBoxEvents());
////    Map<Integer, UserLockBoxEvent> map = UserLockBoxEventRetrieveUtils
////        .getLockBoxEventUuidsToLockBoxEventsForUser(user.getId());
////    for (LockBoxEventProto p : resBuilder.getLockBoxEventsList()) {
////      UserLockBoxEvent e = map.get(p.getLockBoxEventId());
////      if (e != null) {
////        resBuilder.addUserLockBoxEvents(CreateInfoProtoUtils.createUserLockBoxEventProto(e,
////            user.getType()));
////      }
////    }
//  }
//
//  protected void updateLeaderboard(final String apsalarId, final User user, final Timestamp now,
//      final int newNumConsecutiveDaysLoggedIn) {
//    if (user != null) {
//      LOG.info("Updating leaderboard for user " + user.getId());
//      syncApsalaridLastloginConsecutivedaysloggedinResetBadges(user, apsalarId, now,
//          newNumConsecutiveDaysLoggedIn);
//      final LeaderBoardUtil leaderboard = AppContext.getApplicationContext().getBean(LeaderBoardUtil.class);
//      //null PvpLeagueFromUser means will pull from hazelcast instead
//      leaderboard.updateLeaderboardForUser(user, null);
//    }
//  }
//
//
//  private void setUnhandledForgeAttempts(final Builder resBuilder, final User user) {
////    Map<Integer, BlacksmithAttempt> blacksmithIdToBlacksmithAttempt = 
////        UnhandledBlacksmithAttemptRetrieveUtils.getUnhandledBlacksmithAttemptsForUser(user.getId());
////    int numEquipsBeingForged = blacksmithIdToBlacksmithAttempt.size();
////    int numEquipsUserCanForge = ControllerConstants.FORGE_DEFAULT_NUMBER_OF_FORGE_SLOTS
////        + user.getNumAdditionalForgeSlots();
////
////    if (blacksmithIdToBlacksmithAttempt != null && numEquipsBeingForged <= numEquipsUserCanForge) {
////      for (BlacksmithAttempt ba : blacksmithIdToBlacksmithAttempt.values()) {
////
////        int baId = ba.getId();
////        resBuilder.addUnhandledForgeAttempt(
////            CreateInfoProtoUtils.createUnhandledBlacksmithAttemptProtoFromBlacksmithAttempt(
////                blacksmithIdToBlacksmithAttempt.get(baId))
////            );
////
////        int equipId = ba.getEquipId();
////        Equipment e = EquipmentRetrieveUtils.getEquipmentUuidsToEquipment().get(equipId);
////        resBuilder.addForgeAttemptEquip(
////            CreateInfoProtoUtils.createFullEquipProtoFromEquip(e)
////            );
////      }
////    }
////
////    if (blacksmithIdToBlacksmithAttempt != null && numEquipsBeingForged > numEquipsUserCanForge) {
////      LOG.error("user has too many blacksmith attempts, should only have " + numEquipsUserCanForge + 
////          ". blacksmith attempts = " + blacksmithIdToBlacksmithAttempt);
////    }
//  }
//
//  private void setWhetherPlayerCompletedInAppPurchase(final Builder resBuilder, final User user) {
//    final boolean hasPurchased = IAPHistoryRetrieveUtils.checkIfUserHasPurchased(user.getId());
//    resBuilder.setPlayerHasBoughtInAppPurchase(hasPurchased);
//  }
//
//  // returns the total number of consecutive days the user logged in,
//  // awards user if user logged in for an additional consecutive day
////  private int setDailyBonusInfo(Builder resBuilder, User user, Timestamp now) {
////    // will keep track of total consecutive days user has logged in, just
////    // for funzies
////    List<Integer> numConsecDaysList = new ArrayList<Integer>();
////    int totalConsecutiveDaysPlayed = 1;
////    List<Boolean> rewardUserList = new ArrayList<Boolean>();
////    boolean rewardUser = false;
////
////    int consecutiveDaysPlayed = determineCurrentConsecutiveDay(user, now, numConsecDaysList,
////        rewardUserList);
////    if (!numConsecDaysList.isEmpty()) {
////      totalConsecutiveDaysPlayed = numConsecDaysList.get(0);
////      rewardUser = rewardUserList.get(0);
////    }
////
////    DailyBonusReward rewardForUser = determineRewardForUser(user);
////    // function does nothing if null reward was returned from
////    // determineRewardForUser
////    Map<String, Integer> currentDayReward = selectRewardFromDailyBonusReward(rewardForUser,
////        consecutiveDaysPlayed);
////
////    List<Integer> equipIdRewardedList = new ArrayList<Integer>();
////    // function does nothing if previous function returned null, or
////    // either updates user's money or "purchases" booster pack for user
////    boolean successful = writeDailyBonusRewardToDB(user, currentDayReward, rewardUser, now,
////        equipIdRewardedList);
////    if (successful) {
////      int equipIdRewarded = equipIdRewardedList.get(0);
////      writeToUserDailyBonusRewardHistory(user, currentDayReward, consecutiveDaysPlayed, now,
////          equipIdRewarded);
////    }
////    setDailyBonusStuff(resBuilder, user, rewardUser, rewardForUser);
////    return totalConsecutiveDaysPlayed;
////  }
//
//  // totalConsecutiveDaysList will contain one element the actual number of
//  // consecutive
//  // days the user has logged into our game, not really necessary to keep
//  // track...
////  private int determineCurrentConsecutiveDay(User user, Timestamp now,
////      List<Integer> totalConsecutiveDaysList, List<Boolean> rewardUserList) {
////    // SETTING STUFF UP
////    String userUuid = user.getId();
////    UserDailyBonusRewardHistory lastReward = UserDailyBonusRewardHistoryRetrieveUtils
////        .getLastDailyRewardAwardedForUserUuid(userUuid);
////    Date nowDate = new Date(now.getTime());
////    long nowDateMillis = nowDate.getTime();
////
////    if (null == lastReward) {
////      LOG.info("user has never received a daily bonus reward. Setting consecutive days played to 1.");
////      totalConsecutiveDaysList.add(1);
////      rewardUserList.add(true);
////      return 1;
////    }
////    // let days = consecutive day amount corresponding to the reward user
////    // was given
////    // if reward was more than one day ago (in past), return 1
////    // else if user was rewarded yesterday return the (1 + days)
////    // else reward was today return days
////    int nthConsecutiveDay = lastReward.getNthConsecutiveDay();
////    Date dateLastAwarded = lastReward.getDateAwarded();
////    long dateLastMillis = dateLastAwarded.getTime();
////    boolean awardedInThePast = nowDateMillis > dateLastMillis;
////
////    int dayDiff = MiscMethods.dateDifferenceInDays(dateLastAwarded, nowDate);
////    // LOG.info("dateLastAwarded=" + dateLastAwarded + ", nowDate=" +
////    // nowDate + ", day difference=" + dayDiff);
////    if (1 < dayDiff && awardedInThePast) {
////      // LOG.info("user broke their logging in streak. previous daily bonus reward: "
////      // + lastReward
////      // + ", now=" + now);
////      // been a while since user last logged in
////      totalConsecutiveDaysList.add(1);
////      rewardUserList.add(true);
////      return 1;
////    } else if (1 == dayDiff && awardedInThePast) {
////      // LOG.info("awarding user. previous daily bonus reward: " +
////      // lastReward + ", now=" + now);
////      // user logged in yesterday
////      totalConsecutiveDaysList.add(user.getNumConsecutiveDaysPlayed() + 1);
////      rewardUserList.add(true);
////      return nthConsecutiveDay % ControllerConstants.STARTUP__DAILY_BONUS_MAX_CONSECUTIVE_DAYS + 1;
////    } else {
////      // either user logged in today or user tried faking time, but who
////      // cares...
////      totalConsecutiveDaysList.add(user.getNumConsecutiveDaysPlayed());
////      rewardUserList.add(false);
////      // LOG.info("user already collected his daily reward. previous daily bonus reward: "
////      // + lastReward + ", now=" + now);
////      return nthConsecutiveDay;
////    }
////  }
//
////  private DailyBonusReward determineRewardForUser(User aUser) {
////    Map<Integer, DailyBonusReward> allDailyRewards = DailyBonusRewardRetrieveUtils
////        .getDailyBonusRewardUuidsToDailyBonusRewards();
////    // sanity check, exit if it fails
////    if (null == allDailyRewards || allDailyRewards.isEmpty()) {
////      LOG.error("unexpected error: There are no daily bonus rewards set up in the daily_bonus_reward table");
////      return null;
////    }
////
////    int level = aUser.getLevel();
////    // determine daily bonus reward for this user's level, exit if there it
////    // doesn't exist
////    DailyBonusReward reward = selectDailyBonusRewardForLevel(allDailyRewards, level);
////    if (null == reward) {
////      LOG.error("unexpected error: no daily bonus rewards available for level=" + level);
////    }
////    return reward;
////  }
//
////  private DailyBonusReward selectDailyBonusRewardForLevel(Map<Integer, DailyBonusReward> allRewards,
////      int userLevel) {
////    DailyBonusReward returnValue = null;
////    for (int id : allRewards.keySet()) {
////      DailyBonusReward dbr = allRewards.get(id);
////      int minLevel = dbr.getMinLevel();
////      int maxLevel = dbr.getMaxLevel();
////      if (minLevel <= userLevel && userLevel <= maxLevel) {
////        // we found the reward to return
////        returnValue = dbr;
////        break;
////      }
////    }
////    return returnValue;
////  }
////
////  private Map<String, Integer> selectRewardFromDailyBonusReward(DailyBonusReward rewardForUser,
////      int numConsecutiveDaysPlayed) {
////    if (null == rewardForUser) {
////      return null;
////    }
////    if (5 < numConsecutiveDaysPlayed || 0 >= numConsecutiveDaysPlayed) {
////      LOG.error("unexpected error: number of consecutive days played is not in the range [1,5]. "
////          + "numConsecutiveDaysPlayed=" + numConsecutiveDaysPlayed);
////      return null;
////    }
////    Map<String, Integer> reward = getCurrentDailyReward(rewardForUser, numConsecutiveDaysPlayed);
////    return reward;
////  }
//
//  // sets the rewards the user gets/ will get in the daily bonus info builder
////  private Map<String, Integer> getCurrentDailyReward(DailyBonusReward reward, int numConsecutiveDaysPlayed) {
////    Map<String, Integer> returnValue = new HashMap<String, Integer>();
////    String key = "";
////    int value = ControllerConstants.NOT_SET;
////
////    String silver = MiscMethods.silver;
////    String gold = MiscMethods.gold;
////    String boosterPackIdString = MiscMethods.boosterPackId;
////
////    // mimicking fall through in switch statement, setting reward user just
////    // got
////    // today and will get in future logins in 5 consecutive day spans
////    if (5 == numConsecutiveDaysPlayed) {
////      // can't set reward in the builder; currently have booster pack id
////      // need equip id
////      key = boosterPackIdString;
////      List<Integer> boosterPackUuids = reward.getDayFiveBoosterPackUuids();
////      value = MiscMethods.getRandomIntFromList(boosterPackUuids);
////    }
////    if (4 == numConsecutiveDaysPlayed) {
////      key = silver;
////      value = reward.getDayFourCoins();
////    }
////    if (3 == numConsecutiveDaysPlayed) {
////      key = gold;
////      value = reward.getDayThreeDiamonds();
////    }
////    if (2 == numConsecutiveDaysPlayed) {
////      key = silver;
////      value = reward.getDayTwoCoins();
////    }
////    if (1 == numConsecutiveDaysPlayed) {
////      key = silver;
////      value = reward.getDayOneCoins();
////    }
////    returnValue.put(key, value);
////    return returnValue;
////  }
//
//  // returns the equip id user "purchased" by logging in
//  // mimics purchase booster pack controller except the argument checking and
//  // dealing with money
//  private int purchaseBoosterPack(final int boosterPackId, final User aUser, final int numBoosterItemsUserWants, final Timestamp now) {
//    final int equipId = ControllerConstants.NOT_SET;
////    try {
////      // local vars
////      String userUuid = aUser.getId();
////      BoosterPack aPack = BoosterPackRetrieveUtils.getBoosterPackForBoosterPackId(boosterPackId);
////      Map<Integer, BoosterItem> boosterItemUuidsToBoosterItems = BoosterItemRetrieveUtils
////          .getBoosterItemUuidsToBoosterItemsForBoosterPackId(boosterPackId);
////      Map<Integer, Integer> boosterItemUuidsToNumCollected = UserBoosterItemRetrieveUtils
////          .getBoosterItemUuidsToQuantityForUser(userUuid);
////      Map<Integer, Integer> newBoosterItemUuidsToNumCollected = new HashMap<Integer, Integer>();
////      List<BoosterItem> itemsUserReceives = new ArrayList<BoosterItem>();
////      List<Boolean> collectedBeforeReset = new ArrayList<Boolean>();
////      List<Long> userEquipUuids = new ArrayList<Long>();
////
////      // actually selecting equips
////      boolean resetOccurred = MiscMethods.getAllBoosterItemsForUser(boosterItemUuidsToBoosterItems,
////          boosterItemUuidsToNumCollected, numBoosterItemsUserWants, aUser, aPack, itemsUserReceives,
////          collectedBeforeReset);
////      newBoosterItemUuidsToNumCollected = new HashMap<Integer, Integer>(boosterItemUuidsToNumCollected);
////      boolean successful = writeBoosterStuffToDB(aUser, boosterItemUuidsToNumCollected,
////          newBoosterItemUuidsToNumCollected, itemsUserReceives, collectedBeforeReset,
////          resetOccurred, now, userEquipUuids);
////      if (successful) {
////        //exclude from daily limit check in PurchaseBoosterPackController
////        boolean excludeFromLimitCheck = true;
////        MiscMethods.writeToUserBoosterPackHistoryOneUser(userUuid, boosterPackId,
////            numBoosterItemsUserWants, now, itemsUserReceives, excludeFromLimitCheck,
////            userEquipUuids);
////        equipId = getEquipId(numBoosterItemsUserWants, itemsUserReceives);
////      }
////
////    } catch (Exception e) {
////      LOG.error("unexpected error: ", e);
////    }
//    return equipId;
//  }
//
////  private int getEquipId(int numBoosterItemsUserWants, List<BoosterItem> itemsUserReceives) {
////    if (1 != numBoosterItemsUserWants) {
////      LOG.error("unexpected error: trying to buy more than one equip from booster pack. boosterItems="
////          + MiscMethods.shallowListToString(itemsUserReceives));
////      return ControllerConstants.NOT_SET;
////    }
////    BoosterItem bi = itemsUserReceives.get(0);
////    return bi.getEquipId();
////  }
//
//  private boolean writeBoosterStuffToDB(final User aUser, final Map<Integer, Integer> boosterItemUuidsToNumCollected,
//      final Map<Integer, Integer> newBoosterItemUuidsToNumCollected, final List<BoosterItem> itemsUserReceives,
//      final List<Boolean> collectedBeforeReset, final boolean resetOccurred, final Timestamp now,
//      final List<Long> userEquipUuidsForHistoryTable) {
////    String userUuid = aUser.getId();
////    List<Long> userEquipUuids = MiscMethods.insertNewUserEquips(userUuid,
////        itemsUserReceives, now, ControllerConstants.UER__DAILY_BONUS_REWARD);
////    if (null == userEquipUuids || userEquipUuids.isEmpty() || userEquipUuids.size() != itemsUserReceives.size()) {
////      LOG.error("unexpected error: failed to insert equip for user. boosteritems="
////          + MiscMethods.shallowListToString(itemsUserReceives) + "\t userEquipUuids="+ userEquipUuids);
////      return false;
////    }
////
////    if (!MiscMethods.updateUserBoosterItems(itemsUserReceives, collectedBeforeReset,
////        boosterItemUuidsToNumCollected, newBoosterItemUuidsToNumCollected, userUuid, resetOccurred)) {
////      // failed to update user_booster_items
////      LOG.error("unexpected error: failed to update user_booster_items for userUuid: " + userUuid
////          + " attempting to delete equips given: " + MiscMethods.shallowListToString(userEquipUuids));
////      DeleteUtils.get().deleteUserEquips(userEquipUuids);
////      return false;
////    }
////    userEquipUuidsForHistoryTable.addAll(userEquipUuids);
//    return true;
//  }
//
//  private boolean writeDailyBonusRewardToDB(final User aUser, final Map<String, Integer> currentDayReward,
//      final boolean giveToUser, final Timestamp now, final List<Integer> equipIdRewardedList) {
//    int equipId = ControllerConstants.NOT_SET;
//    if (!giveToUser || (null == currentDayReward) || (0 == currentDayReward.size())) {
//      return false;
//    }
//    String key = "";
//    int value = ControllerConstants.NOT_SET;
//    // sanity check, should only be one reward: gold, silver, equipId
//    if (1 == currentDayReward.size()) {
//      final String[] keys = new String[1];
//      currentDayReward.keySet().toArray(keys);
//      key = keys[0];
//      value = currentDayReward.get(key);
//    } else {
//      LOG.error("unexpected error: current day's reward for a user is more than one. rewards="
//          + currentDayReward);
//      return false;
//    }
//
//    final int previousSilver = aUser.getCash();
//    final int previousGold = aUser.getGems();
//    if (key.equals(MiscMethods.boosterPackId)) {
//      // since user got a booster pack id as reward, need to "buy it" for
//      // him
//      final int numBoosterItemsUserWants = 1;
//      // calling this will already give the user an equip
//      equipId = purchaseBoosterPack(value, aUser, numBoosterItemsUserWants, now);
//      if (ControllerConstants.NOT_SET == equipId) {
//        LOG.error("unexpected error: failed to 'buy' booster pack for user. packId=" + value
//            + ", user=" + aUser);
//        return false;
//      }
//    }
//    if (key.equals(MiscMethods.cash)) {
//      if (!aUser.updateRelativeCashNaive(value)) {
//        LOG.error("unexpected error: could not give silver bonus of " + value + " to user " + aUser);
//        return false;
//      } else {// gave user silver
//        writeToUserCurrencyHistory(aUser, key, previousSilver, currentDayReward);
//      }
//    }
//    if (key.equals(MiscMethods.gems)) {
//      if (!aUser.updateRelativeGemsNaive(value)) {
//        LOG.error("unexpected error: could not give silver bonus of " + value + " to user " + aUser);
//        return false;
//      } else {// gave user gold
//        writeToUserCurrencyHistory(aUser, key, previousGold, currentDayReward);
//      }
//    }
//    equipIdRewardedList.add(equipId);
//    return true;
//  }
//
//  private void writeToUserDailyBonusRewardHistory(final User aUser, final Map<String, Integer> rewardForUser,
//      final int nthConsecutiveDay, final Timestamp now, final int equipIdRewarded) {
////    String userUuid = aUser.getId();
////    int currencyRewarded = ControllerConstants.NOT_SET;
////    boolean isCoins = false;
////    int boosterPackIdRewarded = ControllerConstants.NOT_SET;
////
////    String boosterPackId = MiscMethods.boosterPackId;
////    String silver = MiscMethods.silver;
////    String gold = MiscMethods.gold;
////    if (rewardForUser.containsKey(boosterPackId)) {
////      boosterPackIdRewarded = rewardForUser.get(boosterPackId);
////    }
////    if (rewardForUser.containsKey(silver)) {
////      currencyRewarded = rewardForUser.get(silver);
////      isCoins = true;
////    }
////    if (rewardForUser.containsKey(gold)) {
////      currencyRewarded = rewardForUser.get(gold);
////    }
////    int numInserted = InsertUtils.get().insertIntoUserDailyRewardHistory(userUuid, currencyRewarded,
////        isCoins, boosterPackIdRewarded, equipIdRewarded, nthConsecutiveDay, now);
////    if (1 != numInserted) {
////      LOG.error("unexpected error: could not record that user got a reward for this day: " + now);
////    }
//  }
//
////  private void setDailyBonusStuff(Builder resBuilder, User aUser, boolean rewardUser,
////      DailyBonusReward rewardForUser) {
////    // LOG.info("rewardUser=" + rewardUser + "rewardForUser=" +
////    // rewardForUser + "user=" + aUser);
////
////    String userUuid = aUser.getId();
////    // there should be a reward inserted if things saved sans a hitch
////    UserDailyBonusRewardHistory udbrh = UserDailyBonusRewardHistoryRetrieveUtils
////        .getLastDailyRewardAwardedForUserUuid(userUuid);
////
////    if (null == udbrh || null == rewardForUser) {
////      LOG.error("unexpected error: no daily bonus reward history exists for user=" + aUser);
////      return;
////    }
////    int consecutiveDaysPlayed = udbrh.getNthConsecutiveDay();
////
////    DailyBonusInfo.Builder dbib = DailyBonusInfo.newBuilder();
////    if (5 == consecutiveDaysPlayed) {
////      // user just got an equip
////      int boosterPackId = udbrh.getBoosterPackIdRewarded();
////      BoosterPack bp = BoosterPackRetrieveUtils.getBoosterPackForBoosterPackId(boosterPackId);
////      Map<Integer, BoosterItem> biMap = BoosterItemRetrieveUtils
////          .getBoosterItemUuidsToBoosterItemsForBoosterPackId(boosterPackId);
////      Collection<BoosterItem> biList = biMap.values();
////      BoosterPackProto aBoosterPackProto = CreateInfoProtoUtils.createBoosterPackProto(bp, biList);
////      dbib.setBoosterPack(aBoosterPackProto);
////
////      // LOG.info("setting 5th consecutive day reward");
////      int equipId = udbrh.getEquipIdRewarded();
////      dbib.setEquipId(equipId);
////    }
////    if (4 >= consecutiveDaysPlayed) {
////      // LOG.info("setting 4th consecutive day reward");
////      dbib.setDayFourCoins(rewardForUser.getDayFourCoins());
////    }
////    if (3 >= consecutiveDaysPlayed) {
////      // LOG.info("setting 3rd consecutive day reward");
////      dbib.setDayThreeDiamonds(rewardForUser.getDayThreeDiamonds());
////    }
////    if (2 >= consecutiveDaysPlayed) {
////      // LOG.info("setting 2nd consecutive day reward");
////      dbib.setDayTwoCoins(rewardForUser.getDayTwoCoins());
////    }
////    if (1 == consecutiveDaysPlayed) {
////      // LOG.info("setting first consecutive day reward");
////      dbib.setDayOneCoins(rewardForUser.getDayOneCoins());
////    }
////    // LOG.info("nth consecutive day=" + consecutiveDaysPlayed);
////    Date dateAwarded = udbrh.getDateAwarded();
////    long dateAwardedMillis = dateAwarded.getTime();
////    dbib.setTimeAwarded(dateAwardedMillis);
////    dbib.setNumConsecutiveDaysPlayed(consecutiveDaysPlayed);
////    resBuilder.setDailyBonusInfo(dbib.build());
////  }
//
//  private void syncApsalaridLastloginConsecutivedaysloggedinResetBadges(final User user, String apsalarId,
//      final Timestamp loginTime, final int newNumConsecutiveDaysLoggedIn) {
//    if ((user.getApsalarId() != null) && (apsalarId == null)) {
//      apsalarId = user.getApsalarId();
//    }
//    if (!user.updateAbsoluteApsalaridLastloginBadgesNumConsecutiveDaysLoggedIn(apsalarId, loginTime, 0,
//        newNumConsecutiveDaysLoggedIn)) {
//      LOG.error("problem with updating apsalar id to " + apsalarId + ", last login to " + loginTime
//          + ", and badge count to 0 for " + user + " and newNumConsecutiveDaysLoggedIn is "
//          + newNumConsecutiveDaysLoggedIn);
//    }
//    if (!InsertUtils.get().insertLastLoginLastLogoutToUserSessions(user.getId(), loginTime, null)) {
//      LOG.error("problem with inserting last login time for user " + user + ", loginTime=" + loginTime);
//    }
//
//    if (user.getNumBadges() != 0) {
//      if (user.getDeviceToken() != null) {
//        /*
//         * handled locally?
//         */
//        // ApnsServiceBuilder builder =
//        // APNS.newService().withCert(APNSProperties.PATH_TO_CERT,
//        // APNSProperties.CERT_PASSWORD);
//        // if (Globals.IS_SANDBOX()) {
//        // builder.withSandboxDestination();
//        // }
//        // ApnsService service = builder.build();
//        // service.push(newDeviceToken,
//        // APNS.newPayload().badge(0).build());
//        // service.stop();
//      }
//    }
//  }
//
//  private void setAllies(final Builder resBuilder, final User user) {
////    boolean realPlayersOnly = false;
////    boolean fakePlayersOnly = false;
////    boolean offlinePlayersOnly = false; //does not include fake players
////    boolean inactiveShield = false;
////    List<Integer> forbiddenUserUuids = new ArrayList<Integer>();
////    forbiddenUserUuids.add(user.getId());
////
////    List<User> allies = RetrieveUtils.userRetrieveUtils().getUsers(
////        ControllerConstants.STARTUP__APPROX_NUM_ALLIES_TO_SEND, user.getLevel(),
////        user.getId(), false, realPlayersOnly,
////        fakePlayersOnly, offlinePlayersOnly,
////        inactiveShield, forbiddenUserUuids);
////    if (allies != null && allies.size() > 0) {
////      for (User ally : allies) {
////        resBuilder.addAllies(CreateInfoProtoUtils.createMinimumUserProtoWithLevelFromUser(ally));
////      }
////    }
//  }
//
//
//  private void setConstants(final Builder startupBuilder, final StartupStatus startupStatus) {
//    startupBuilder.setStartupConstants(MiscMethods.createStartupConstantsProto(globals));
//    if (startupStatus == StartupStatus.USER_NOT_IN_DB) {
//      setTutorialConstants(startupBuilder);
//    }
//  }
//
//  private void setTutorialConstants(final Builder resBuilder) {
////    Map<Integer, Equipment> equipmentUuidsToEquipment = EquipmentRetrieveUtils.getEquipmentUuidsToEquipment();
////
////    UserType aGoodType = UserType.GOOD_ARCHER;
////    UserType aBadType = UserType.BAD_ARCHER;
////
////    Task task = TaskRetrieveUtils.getTaskForTaskId(ControllerConstants.TUTORIAL__FIRST_TASK_ID);
//////    task.setPotentialLootEquipUuids(new ArrayList<Integer>());
////    
////    FullTaskProto ftpGood = CreateInfoProtoUtils.createFullTaskProtoFromTask(aGoodType, task);
////    FullTaskProto ftpBad = CreateInfoProtoUtils.createFullTaskProtoFromTask(aBadType, task);
////
////    task = TaskRetrieveUtils.getTaskForTaskId(ControllerConstants.TUTORIAL__FAKE_QUEST_TASK_ID);
//////    task.setPotentialLootEquipUuids(new ArrayList<Integer>());
////    FullTaskProto questFtpGood = CreateInfoProtoUtils.createFullTaskProtoFromTask(aGoodType, task);
////    FullTaskProto questFtpBad = CreateInfoProtoUtils.createFullTaskProtoFromTask(aBadType, task);
////
////    Dialogue goodAcceptDialogue = MiscMethods
////        .createDialogue(ControllerConstants.TUTORIAL__FAKE_QUEST_GOOD_ACCEPT_DIALOGUE);
////    Dialogue badAcceptDialogue = MiscMethods
////        .createDialogue(ControllerConstants.TUTORIAL__FAKE_QUEST_BAD_ACCEPT_DIALOGUE);
////
////    FullTutorialQuestProto tqbp = FullTutorialQuestProto
////        .newBuilder()
////        .setGoodName(ControllerConstants.TUTORIAL__FAKE_QUEST_GOOD_NAME)
////        .setBadName(ControllerConstants.TUTORIAL__FAKE_QUEST_BAD_NAME)
////        .setGoodDescription(ControllerConstants.TUTORIAL__FAKE_QUEST_GOOD_DESCRIPTION)
////        .setBadDescription(ControllerConstants.TUTORIAL__FAKE_QUEST_BAD_DESCRIPTION)
////        .setGoodDoneResponse(ControllerConstants.TUTORIAL__FAKE_QUEST_GOOD_DONE_RESPONSE)
////        .setBadDoneResponse(ControllerConstants.TUTORIAL__FAKE_QUEST_BAD_DONE_RESPONSE)
////        .setGoodAcceptDialogue(
////            CreateInfoProtoUtils.createDialogueProtoFromDialogue(goodAcceptDialogue))
////            .setBadAcceptDialogue(CreateInfoProtoUtils.createDialogueProtoFromDialogue(badAcceptDialogue))
////            .setAssetNumWithinCity(ControllerConstants.TUTORIAL__FAKE_QUEST_ASSET_NUM_WITHIN_CITY)
////            .setCoinsGained(ControllerConstants.TUTORIAL__FAKE_QUEST_COINS_GAINED)
////            .setExpGained(ControllerConstants.TUTORIAL__FAKE_QUEST_EXP_GAINED)
////            .setTaskGood(questFtpGood)
////            .setTaskBad(questFtpBad)
//////            .setTaskCompleteCoinGain(MiscMethods.calculateCoinsGainedFromTutorialTask(task))
////            .setEquipReward(
////                CreateInfoProtoUtils.createFullEquipProtoFromEquip(equipmentUuidsToEquipment
////                    .get(ControllerConstants.TUTORIAL__FAKE_QUEST_AMULET_LOOT_EQUIP_ID))).build();
////
////    PlayerWallPost pwp = new PlayerWallPost(-1,
////        ControllerConstants.USER_CREATE__ID_OF_POSTER_OF_FIRST_WALL, -1, new Date(),
////        ControllerConstants.USER_CREATE__FIRST_WALL_POST_TEXT);
////    User poster = RetrieveUtils.userRetrieveUtils().getUserById(
////        ControllerConstants.USER_CREATE__ID_OF_POSTER_OF_FIRST_WALL);
////
////    String name = "";
////    int syllablesInName = (Math.random() < .5) ? syllablesInName1 : syllablesInName2;
////    name = nameGeneratorElven.compose(syllablesInName);
////
////
////    TutorialConstants.Builder builder = TutorialConstants
////    		.newBuilder();
////    builder.setInitEnergy(ControllerConstants.TUTORIAL__INIT_ENERGY);
////    builder.setInitStamina(ControllerConstants.TUTORIAL__INIT_STAMINA)
////    .setInitHealth(ControllerConstants.TUTORIAL__INIT_HEALTH);
////    builder.setStructToBuild(ControllerConstants.TUTORIAL__FIRST_STRUCT_TO_BUILD);
////    builder.setDiamondCostToInstabuildFirstStruct(ControllerConstants.TUTORIAL__DIAMOND_COST_TO_INSTABUILD_FIRST_STRUCT);
////    builder.setArcherInitAttack(ControllerConstants.TUTORIAL__ARCHER_INIT_ATTACK);
////    builder.setArcherInitDefense(ControllerConstants.TUTORIAL__ARCHER_INIT_DEFENSE);
////    FullEquipProto fep = CreateInfoProtoUtils.createFullEquipProtoFromEquip(
////    		equipmentUuidsToEquipment.get(ControllerConstants.TUTORIAL__ARCHER_INIT_WEAPON_ID));
////    builder.setArcherInitWeapon(fep);
////    builder.setArcherInitArmor(
////    		CreateInfoProtoUtils.createFullEquipProtoFromEquip(equipmentUuidsToEquipment
////    				.get(ControllerConstants.TUTORIAL__ARCHER_INIT_ARMOR_ID)));
////    builder.setMageInitAttack(ControllerConstants.TUTORIAL__MAGE_INIT_ATTACK);
////    builder.setMageInitDefense(ControllerConstants.TUTORIAL__MAGE_INIT_DEFENSE);
////    builder.setMageInitWeapon(
////    		CreateInfoProtoUtils.createFullEquipProtoFromEquip(equipmentUuidsToEquipment
////    				.get(ControllerConstants.TUTORIAL__MAGE_INIT_WEAPON_ID)));
////    builder.setMageInitArmor(
////    		CreateInfoProtoUtils.createFullEquipProtoFromEquip(equipmentUuidsToEquipment
////    				.get(ControllerConstants.TUTORIAL__MAGE_INIT_ARMOR_ID)));
////    builder.setWarriorInitAttack(ControllerConstants.TUTORIAL__WARRIOR_INIT_ATTACK);
////    builder.setWarriorInitDefense(ControllerConstants.TUTORIAL__WARRIOR_INIT_DEFENSE);
////    builder.setWarriorInitWeapon(
////    		CreateInfoProtoUtils.createFullEquipProtoFromEquip(equipmentUuidsToEquipment
////    				.get(ControllerConstants.TUTORIAL__WARRIOR_INIT_WEAPON_ID)));
////    builder.setWarriorInitArmor(
////    		CreateInfoProtoUtils.createFullEquipProtoFromEquip(equipmentUuidsToEquipment
////    				.get(ControllerConstants.TUTORIAL__WARRIOR_INIT_ARMOR_ID)));
////    builder.setTutorialQuest(tqbp);
////    builder.setMinNameLength(ControllerConstants.USER_CREATE__MIN_NAME_LENGTH);
////    builder.setTutorialQuest(tqbp);
////    builder.setMaxNameLength(ControllerConstants.USER_CREATE__MAX_NAME_LENGTH);
////    builder.setCoinRewardForBeingReferred(
////    		ControllerConstants.USER_CREATE__COIN_REWARD_FOR_BEING_REFERRED);
////    builder.setInitDiamonds(Globals.INITIAL_DIAMONDS());
////    builder.setInitCoins(ControllerConstants.TUTORIAL__INIT_COINS);
////    builder.setFirstBattleCoinGain(ControllerConstants.TUTORIAL__FIRST_BATTLE_COIN_GAIN);
////    builder.setFirstBattleExpGain(ControllerConstants.TUTORIAL__FIRST_BATTLE_EXP_GAIN);
////    builder.setFirstTaskGood(ftpGood);
////    builder.setFirstTaskBad(ftpBad);
////    builder.setExpRequiredForLevelTwo(
////    		LevelsRequiredExperienceRetrieveUtils.getLevelsToRequiredExperienceForLevels().get(2));
////    builder.setExpRequiredForLevelThree(
////    		LevelsRequiredExperienceRetrieveUtils.getLevelsToRequiredExperienceForLevels().get(3));
////    builder.setFirstWallPost(
////    		CreateInfoProtoUtils.createPlayerWallPostProtoFromPlayerWallPost(pwp, poster));
////    builder.setDefaultName(name);
////    builder.setCostToSpeedUpForge(ControllerConstants.TUTORIAL__COST_TO_SPEED_UP_FORGE);
////
////
////    List<NeutralCityElement> neutralCityElements = NeutralCityElementsRetrieveUtils
////    		.getNeutralCityElementsForCity(ControllerConstants.TUTORIAL__FIRST_NEUTRAL_CITY_ID);
////    if (neutralCityElements != null) {
////    	for (NeutralCityElement nce : neutralCityElements) {
////        builder.addFirstCityElementsForGood(CreateInfoProtoUtils
////            .createNeutralCityElementProtoFromNeutralCityElement(nce, aGoodType));
////        builder.addFirstCityElementsForBad(CreateInfoProtoUtils
////            .createNeutralCityElementProtoFromNeutralCityElement(nce, aBadType));
////      }
////    }
////
////    Map<Integer, Structure> structUuidsToStructs = StructureRetrieveUtils.getStructUuidsToStructs();
////    for (Structure struct : structUuidsToStructs.values()) {
////      if (struct != null) {
////        FullStructureProto fsp = CreateInfoProtoUtils.createFullStructureProtoFromStructure(struct);
////        builder.addCarpenterStructs(fsp);
////        if (struct.getMinLevel() == 2) {
////          builder.addNewlyAvailableStructsAfterLevelup(CreateInfoProtoUtils
////              .createFullStructureProtoFromStructure(struct));
////        }
////      }
////    }
////
////    List<City> availCities = MiscMethods
////        .getCitiesAvailableForUserLevel(ControllerConstants.USER_CREATE__START_LEVEL);
////    for (City city : availCities) {
////      if (city.getMinLevel() == ControllerConstants.USER_CREATE__START_LEVEL) {
////        builder.addCitiesNewlyAvailableToUserAfterLevelup(CreateInfoProtoUtils
////            .createFullCityProtoFromCity(city));
////      }
////    }
////
////    Map<Integer, Equipment> equipIdToEquips = EquipmentRetrieveUtils.getEquipmentUuidsToEquipment();
////    if (equipIdToEquips != null) {
////      for (Equipment e : equipIdToEquips.values()) {
////        if (e != null && e.getMinLevel() == ControllerConstants.USER_CREATE__START_LEVEL
////            && (e.getRarity() == Rarity.EPIC || e.getRarity() == Rarity.LEGENDARY)) {
////          builder.addNewlyEquippableEpicsAndLegendariesForAllClassesAfterLevelup(CreateInfoProtoUtils
////              .createFullEquipProtoFromEquip(e));
////        }
////      }
////    }
////    resBuilder.setTutorialConstants(builder.build());
//  }
//
//  //TODO: FIX THIS
//  public void writeToUserCurrencyHistory(final User aUser, final String goldSilver, final int previousMoney,
//      final Map<String, Integer> goldSilverChange) {
//    //String cash = MiscMethods.cash;
//    //String gems = MiscMethods.gems;
//
////    Timestamp date = new Timestamp((new Date()).getTime());
////    Map<String, Integer> previousGoldSilver = new HashMap<String, Integer>();
////    Map<String, String> reasonsForChanges = new HashMap<String, String>();
////    String reasonForChange = ControllerConstants.UCHRFC__STARTUP_DAILY_BONUS;
////
////    if (goldSilver.equals(cash)) {
////      previousGoldSilver.put(cash, previousMoney);
////      reasonsForChanges.put(cash, reasonForChange);
////    } else {
////      previousGoldSilver.put(gems, previousMoney);
////      reasonsForChanges.put(gems, reasonForChange);
////    }
////
////    MiscMethods.writeToUserCurrencyOneUserGemsAndOrCash(aUser, date, goldSilverChange,
////        previousGoldSilver, reasonsForChanges);
//  }
//
//  
//  //for the setter dependency injection or something****************************************************************
//  public HazelcastPvpUtil getHazelcastPvpUtil() {
//	  return hazelcastPvpUtil;
//  }
//  public void setHazelcastPvpUtil(final HazelcastPvpUtil hazelcastPvpUtil) {
//	  this.hazelcastPvpUtil = hazelcastPvpUtil;
//  }
//  
//  public Locker getLocker() {
//	  return locker;
//  }
//  public void setLocker(final Locker locker) {
//	  this.locker = locker;
//  }  
//  
//  public TimeUtils getTimeUtils() {
//	  return timeUtils;
//  }
//  public void setTimeUtils(final TimeUtils timeUtils) {
//	  this.timeUtils = timeUtils;
//  }
//  
//  public Globals getGlobals() {
//	  return globals;
//  }
//  public void setGlobals(final Globals globals) {
//	  this.globals = globals;
//  }
//
//  public QuestJobForUserRetrieveUtil getQuestJobForUserRetrieveUtil() {
//	  return questJobForUserRetrieveUtil;
//  }
//  public void setQuestJobForUserRetrieveUtil(
//		  final QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil) {
//	  this.questJobForUserRetrieveUtil = questJobForUserRetrieveUtil;
//  }
//  public PvpLeagueForUserRetrieveUtil getPvpLeagueForUserRetrieveUtil() {
//	  return pvpLeagueForUserRetrieveUtil;
//  }
//  public void setPvpLeagueForUserRetrieveUtil(
//		  final PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil) {
//	  this.pvpLeagueForUserRetrieveUtil = pvpLeagueForUserRetrieveUtil;
//  }
//  
//  public PvpBattleHistoryRetrieveUtil getPvpBattleHistoryRetrieveUtil() {
//	  return pvpBattleHistoryRetrieveUtil;
//  }
//  public void setPvpBattleHistoryRetrieveUtil(
//		  final PvpBattleHistoryRetrieveUtil pvpBattleHistoryRetrieveUtil) {
//	  this.pvpBattleHistoryRetrieveUtil = pvpBattleHistoryRetrieveUtil;
//  }
//  public AchievementForUserRetrieveUtil getAchievementForUserRetrieveUtil() {
//	  return achievementForUserRetrieveUtil;
//  }
//  public void setAchievementForUserRetrieveUtil(
//		  final AchievementForUserRetrieveUtil achievementForUserRetrieveUtil) {
//	  this.achievementForUserRetrieveUtil = achievementForUserRetrieveUtil;
//  }
//  public MiniJobForUserRetrieveUtil getMiniJobForUserRetrieveUtil() {
//	  return miniJobForUserRetrieveUtil;
//  }
//  public void setMiniJobForUserRetrieveUtil(
//		  final MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil) {
//	  this.miniJobForUserRetrieveUtil = miniJobForUserRetrieveUtil;
//  }  
//  
//}
