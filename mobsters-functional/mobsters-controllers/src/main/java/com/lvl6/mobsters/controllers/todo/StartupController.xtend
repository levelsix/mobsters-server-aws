package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.ClanEventPersistentUserReward
import com.lvl6.mobsters.dynamo.MiniJobForUser
import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.QuestForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventStartupProto.ForceLogoutResponseProto
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.StartupStatus
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.UpdateStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.StartupRequestEvent
import com.lvl6.mobsters.events.response.ForceLogoutResponseEvent
import com.lvl6.mobsters.events.response.StartupResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventChatProto.GroupChatMessageProto
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.achievement.AchievementForUserRetrieveUtil
import com.lvl6.mobsters.services.common.TimeUtils
import com.lvl6.properties.Globals
import com.lvl6.properties.KabamProperties
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Comparator
import java.util.Date
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import javax.annotation.Resource
import org.apache.commons.codec.digest.DigestUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class StartupController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(StartupController))
}

private static final class GroupChatComparator implements Comparator<GroupChatMessageProto>
{
	override compare(GroupChatMessageProto o1, GroupChatMessageProto o2)
	{
		if (o1.timeOfChat < o2.timeOfChat)
		{
			return -1;
		}
		else if (o1.timeOfChat > o2.timeOfChat)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	@Resource(name='goodEquipsRecievedFromBoosterPacks')
	protected var IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks

	def getGoodEquipsRecievedFromBoosterPacks()
	{
		goodEquipsRecievedFromBoosterPacks
	}

	def setGoodEquipsRecievedFromBoosterPacks(
		IList<RareBoosterPurchaseProto> goodEquipsRecievedFromBoosterPacks)
	{
		this.goodEquipsRecievedFromBoosterPacks = goodEquipsRecievedFromBoosterPacks
	}

	@Resource(name='globalChat')
	protected var IList<GroupChatMessageProto> chatMessages

	def getChatMessages()
	{
		chatMessages
	}

	def setChatMessages(IList<GroupChatMessageProto> chatMessages)
	{
		this.chatMessages = chatMessages
	}

	@Autowired
	protected var DataServiceTxManager svcTxManager
	@Autowired
	protected var HazelcastPvpUtil hazelcastPvpUtil
	@Autowired
	protected var Locker locker
	@Autowired
	protected var TimeUtils timeUtils
	@Autowired
	protected var Globals globals
	@Autowired
	protected var QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil
	@Autowired
	protected var PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil
	@Autowired
	protected var PvpBattleHistoryRetrieveUtil pvpBattleHistoryRetrieveUtil
	@Autowired
	protected var AchievementForUserRetrieveUtil achievementForUserRetrieveUtil
	@Autowired
	protected var MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil

	def StartupController()
	{
		numAllocatedThreads = 3
	}

	override createRequestEvent()
	{
		new StartupRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_STARTUP_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as StartupRequestEvent)).startupRequestProto
		LOG.info('Processing startup request event')
		var UpdateStatus updateStatus
		val udid = reqProto.udid
		val apsalarId = if (reqProto.apsalarId) reqProto.apsalarUuid else null
		val fbId = reqProto.fbUuid
		val freshRestart = reqProto.isFreshRestart
		MiscMethods::setMDCProperties(udid, null, MiscMethods::getIPOfPlayer(server, null, udid))
		val tempClientVersionNum = reqProto.versionNum * 10
		val tempLatestVersionNum = GameServer::clientVersionNumber * 10
		if (((tempClientVersionNum as int) < (tempLatestVersionNum as int)) &&
			(tempClientVersionNum > 12.5))
		{
			updateStatus = UpdateStatus.MAJOR_UPDATE
			LOG.info('player has been notified of forced update')
		}
		else if (tempClientVersionNum < tempLatestVersionNum)
		{
			updateStatus = UpdateStatus.MINOR_UPDATE
		}
		else
		{
			updateStatus = UpdateStatus.NO_UPDATE
		}
		val resBuilder = StartupResponseProto::newBuilder
		resBuilder.updateStatus = updateStatus
		resBuilder.appStoreURL = Globals.APP_STORE_URL
		resBuilder.reviewPageURL = Globals.REVIEW_PAGE_URL
		resBuilder.reviewPageConfirmationMessage = Globals.REVIEW_PAGE_CONFIRMATION_MESSAGE
		var User user = null
		var startupStatus = StartupStatus.USER_NOT_IN_DB
		var nowDate = new Date()
		nowDate = timeUtils.createDateTruncateMillis(nowDate)
		val now = new Timestamp(nowDate.time)
		val isLogin = true
		val newNumConsecutiveDaysLoggedIn = 0
		try
		{
			if (updateStatus !== UpdateStatus::MAJOR_UPDATE)
			{
				val users = RetrieveUtils::userRetrieveUtils.getUserByUDIDorFbId(udid, fbId)
				user = selectUser(users, udid, fbId)
				if (user !== null)
				{
					val userUuid = user.id
					svcTxManager.beginTransaction
					try
					{
						val logoutResponse = ForceLogoutResponseProto::newBuilder
						logoutResponse.previousLoginTime = user.lastLogin.time
						logoutResponse.udid = udid
						val logoutEvent = new ForceLogoutResponseEvent(userUuid)
						logoutEvent.forceLogoutResponseProto = logoutResponse.build
						eventWriter.processPreDBResponseEvent(logoutEvent, udid)
						eventWriter.handleEvent(logoutEvent)
						if ((null !== fbId) && !fbId.empty)
						{
							eventWriter.processPreDBFacebookEvent(logoutEvent, fbId)
						}
						startupStatus = StartupStatus.USER_IN_DB
						LOG.info('No major update... getting user info')
						setInProgressAndAvailableQuests(resBuilder, userUuid)
						setUserClanInfos(resBuilder, userUuid)
						setNotifications(resBuilder, user)
						noticesToPlayers = resBuilder
						setGroupChatMessages(resBuilder, user)
						setPrivateChatPosts(resBuilder, user, userUuid)
						setUserMonsterStuff(resBuilder, userUuid)
						boosterPurchases = resBuilder
						setFacebookAndExtraSlotsStuff(resBuilder, user, userUuid)
						setTaskStuff(resBuilder, userUuid)
						setAllStaticData(resBuilder, userUuid, true)
						setEventStuff(resBuilder, userUuid)
						val plfu = pvpBattleStuff(resBuilder, user, userUuid, freshRestart, now)
						pvpBattleHistoryStuff(resBuilder, user, userUuid)
						setClanRaidStuff(resBuilder, user, userUuid, now)
						setAchievementStuff(resBuilder, userUuid)
						setMiniJob(resBuilder, userUuid)
						setWhetherPlayerCompletedInAppPurchase(resBuilder, user)
						setUnhandledForgeAttempts(resBuilder, user)
						setLockBoxEvents(resBuilder, user)
						setAllies(resBuilder, user)
						user.lastLogin = nowDate
						val fup = CreateInfoProtoUtils::createFullUserProtoFromUser(user, plfu)
						resBuilder.sender = fup
						val isNewUser = false
						InsertUtils::get.insertIntoLoginHistory(udid, user.id, now, isLogin,
							isNewUser)
					}
					catch (Exception e)
					{
						LOG.error('exception in StartupController processEvent', e)
					}
					finally
					{
						locker.unlockPlayer(user.id, this.class.simpleName)
					}
				}
				else
				{
					LOG.info('tutorial player with udid ' + udid)
					val tc = MiscMethods::createTutorialConstantsProto
					resBuilder.tutorialConstants = tc
					setAllStaticData(resBuilder, 0, false)
					val userLoggedIn = LoginHistoryRetrieveUtils::userLoggedInByUDID(udid)
					val numOldAccounts = RetrieveUtils::userRetrieveUtils.
						numAccountsForUDID(udid)
					val alreadyInFirstTimeUsers = FirstTimeUsersRetrieveUtils::
						userExistsWithUDID(udid)
					var isFirstTimeUser = false
					if (!userLoggedIn && (0 >= numOldAccounts) && !alreadyInFirstTimeUsers)
					{
						isFirstTimeUser = true
					}
					LOG.info(
						'
 userLoggedIn=' + userLoggedIn + '	 numOldAccounts=' + numOldAccounts +
							'	 alreadyInFirstTimeUsers=' + alreadyInFirstTimeUsers +
							'	 isFirstTimeUser=' + isFirstTimeUser)
					if (isFirstTimeUser)
					{
						LOG.info('new player with udid ' + udid)
						InsertUtils::get.insertIntoFirstTimeUsers(udid, null,
							reqProto.macAddress, reqProto.advertiserUuid, now)
					}
					if (Globals::OFFERCHART_ENABLED && isFirstTimeUser)
					{
						sendOfferChartInstall(now, reqProto.advertiserUuid)
					}
					val goingThroughTutorial = true
					InsertUtils::get.insertIntoLoginHistory(udid, 0, now, isLogin,
						goingThroughTutorial)
				}
				resBuilder.startupStatus = startupStatus
				setConstants(resBuilder, startupStatus)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in StartupController processEvent', e)
			try
			{
				resBuilder.startupStatus = StartupStatus.SERVER_IN_MAINTENANCE
				val resEvent = new StartupResponseEvent(udid)
				resEvent.tag = event.tag
				resEvent.startupResponseProto = resBuilder.build
				eventWriter.processPreDBResponseEvent(resEvent, udid)
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in UpdateUserCurrencyController processEvent', e)
			}
		}
		if (Globals::KABAM_ENABLED)
		{
			val naid = retrieveKabamNaid(user, udid, reqProto.macAddress,
				reqProto.advertiserUuid)
			resBuilder.kabamNaid = naid
		}
		resBuilder.serverTimeMillis = (new Date()).time
		val resProto = resBuilder.build
		val resEvent = new StartupResponseEvent(udid)
		resEvent.tag = event.tag
		resEvent.startupResponseProto = resProto
		LOG.debug('Writing event response: ' + resEvent)
		server.writePreDBEvent(resEvent, udid)
		LOG.debug('Wrote response event: ' + resEvent)
		LOG.debug('After response tasks')
		updateLeaderboard(apsalarId, user, now, newNumConsecutiveDaysLoggedIn)
	}

	private def selectUser(List<User> users, String udid, String fbId)
	{
		val numUsers = users.size
		if (numUsers > 2)
		{
			LOG.error(
				'(not really error) there are more than 2 users with the same udid and fbId. udid=' +
					udid + ' fbId=' + fbId + ' users=' + users)
		}
		if (1 === numUsers)
		{
			return users.get(0);
		}
		var User udidUser = null
		for (u : users)
		{
			val userFbId = u.facebookId
			val userUdid = u.udid
			if ((fbId !== null) && fbId == userFbId)
			{
				return u;
			}
			else if ((null === udidUser) && (udid !== null) && udid == userUdid)
			{
				udidUser = u
			}
		}
		udidUser
	}

	private def setInProgressAndAvailableQuests(Builder resBuilder, String userUuid)
	{
		val inProgressAndRedeemedUserQuests = RetrieveUtils::questForUserRetrieveUtils.
			getUserQuestsForUser(userUuid)
		val inProgressQuests = new ArrayList<QuestForUser>()
		val questUuids = new HashSet<Integer>()
		val redeemedQuestUuids = new ArrayList<Integer>()
		val questIdToQuests = QuestRetrieveUtils::questUuidsToQuests
		for (uq : inProgressAndRedeemedUserQuests)
		{
			if (!uq.redeemed)
			{
				inProgressQuests.add(uq)
			}
			else
			{
				val questId = uq.questId
				redeemedQuestUuids.add(questId)
			}
		}
		val questIdToUserQuestJobs = questJobForUserRetrieveUtil.
			getSpecificOrAllQuestIdToQuestJobsForUserUuid(userUuid, questUuids)
		val currentUserQuests = CreateInfoProtoUtils::
			createFullUserQuestDataLarges(inProgressQuests, questIdToQuests,
				questIdToUserQuestJobs)
		resBuilder.addAllUserQuests(currentUserQuests)
		resBuilder.addAllRedeemedQuestUuids(redeemedQuestUuids)
	}

	private def setUserClanInfos(StartupResponseProto ::Builder resBuilder
,   String userUuid){
    val userClans=RetrieveUtils::userClanRetrieveUtils.getUserClansRelatedToUser(userUuid)
    for (     uc : userClans) {
      resBuilder.addUserClanInfo(CreateInfoProtoUtils::createFullUserClanProtoFromUserClan(uc))
    }
  }

private def setNotifications(  Builder resBuilder,   User user){
  }

private def setNoticesToPlayers(  Builder resBuilder){
    val notices=StartupStuffRetrieveUtils::allActiveAlerts
    if (null !== notices) {
      for (       notice : notices) {
        resBuilder.addNoticesToPlayers(notice)
      }
    }
  }

private def setGroupChatMessages(  StartupResponseProto::Builder resBuilder,   User user){
    val it=chatMessages.iterator
    val globalChats=new ArrayList<GroupChatMessageProto>()
    while (it.next) {
      globalChats.add(it.next)
    }
    Collections::sort(globalChats, new GroupChatComparator())
    for (i: 0..<globalChats.size) {
      resBuilder.addGlobalChats(globalChats.get(i))
    }
    if (user.clanId <= 0) {
      return;
    }
    val limit=ControllerConstants.RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP
    val activeClanChatPosts=ClanChatPostRetrieveUtils::getMostRecentClanChatPostsForClan(limit, user.clanId)
    if ((null === activeClanChatPosts) || activeClanChatPosts.empty) {
      return;
    }
    val userUuids=new ArrayList<Integer>()
    for (     p : activeClanChatPosts) {
      userUuids.add(p.posterId)
    }
    var Map<Integer,User> usersByUuids=null
    if (userUuids.size > 0) {
      usersByUuids=RetrieveUtils::userRetrieveUtils.getUsersByUuids(userUuids)
{
                var i=activeClanChatPosts.size - 1

        while (i >= 0) {
          val pwp=activeClanChatPosts.get(i)
          resBuilder.addClanChats(CreateInfoProtoUtils::createGroupChatMessageProtoFromClanChatPost(pwp, usersByUuids.get(pwp.posterId)))
          i--
        }
      }
    }
  }

private def setPrivateChatPosts(  Builder resBuilder,   User aUser,   String userUuid){
    var isRecipient=true
    var Map<Integer,Integer> userIdsToPrivateChatPostUuids=null
    val postUuidsToPrivateChatPosts=new HashMap<Integer,PrivateChatPost>()
    var Map<Integer,User> userUuidsToUsers=null
    var Map<Integer,Set<Integer>> clanUuidsToUserIdSet=null
    var Map<Integer,Clan> clanUuidsToClans=null
    val clanlessUserUuids=new ArrayList<Integer>()
    val clanIdList=new ArrayList<Integer>()
    val privateChatPostUuids=new ArrayList<Integer>()
    val postsUserReceived=PrivateChatPostRetrieveUtils::getMostRecentPrivateChatPostsByOrToUser(userUuid, isRecipient, ControllerConstants::STARTUP__MAX_PRIVATE_CHAT_POSTS_RECEIVED)
    isRecipient=false
    val postsUserSent=PrivateChatPostRetrieveUtils::getMostRecentPrivateChatPostsByOrToUser(userUuid, isRecipient, ControllerConstants::STARTUP__MAX_PRIVATE_CHAT_POSTS_SENT)
    if (((null === postsUserReceived) || postsUserReceived.empty) && ((null === postsUserSent) || postsUserSent.empty)) {
      LOG.info('user has no private chats. aUser=' + aUser)
      return;
    }
    userIdsToPrivateChatPostUuids=aggregateOtherUserUuidsAndPrivateChatPost(postsUserReceived, postsUserSent, postUuidsToPrivateChatPosts)
    if ((null !== userIdsToPrivateChatPostUuids) && !userIdsToPrivateChatPostUuids.empty) {
      val userIdList=new ArrayList<Integer>()
      userIdList.addAll(userIdsToPrivateChatPostUuids.keySet)
      userIdList.add(userUuid)
      userUuidsToUsers=RetrieveUtils::userRetrieveUtils.getUsersByUuids(userIdList)
    }
 else {
      LOG.error('(not really error) aggregating private chat post ids returned nothing, noob user?')
      return;
    }
    if ((null === userUuidsToUsers) || userUuidsToUsers.empty || (userUuidsToUsers.size === 1)) {
      LOG.error('unexpected error: perhaps user talked to himself. postsUserReceved=' + postsUserReceived + ', postsUserSent='+ postsUserSent+ ', aUser='+ aUser)
      return;
    }
    clanUuidsToUserIdSet=determineClanUuidsToUserIdSet(userUuidsToUsers, clanlessUserUuids)
    if ((null !== clanUuidsToUserIdSet) && !clanUuidsToUserIdSet.empty) {
      clanIdList.addAll(clanUuidsToUserIdSet.keySet)
      clanUuidsToClans=ClanRetrieveUtils::getClansByUuids(clanIdList)
    }
    privateChatPostUuids.addAll(userIdsToPrivateChatPostUuids.values)
    val pcppList=CreateInfoProtoUtils::createPrivateChatPostProtoList(clanUuidsToClans, clanUuidsToUserIdSet, userUuidsToUsers, clanlessUserUuids, privateChatPostUuids, postUuidsToPrivateChatPosts)
    resBuilder.addAllPcpp(pcppList)
  }

private def aggregateOtherUserUuidsAndPrivateChatPost(  Map<Integer,PrivateChatPost> postsUserReceived,   Map<Integer,PrivateChatPost> postsUserSent,   Map<Integer,PrivateChatPost> postUuidsToPrivateChatPosts){
    val userIdsToPrivateChatPostUuids=new HashMap<Integer,Integer>()
    if ((null !== postsUserReceived) && !postsUserReceived.empty) {
      for (       pcpId : postsUserReceived.keySet) {
        val postUserReceived=postsUserReceived.get(pcpId)
        val senderId=postUserReceived.posterId
        userIdsToPrivateChatPostUuids.put(senderId, pcpId)
      }
      postUuidsToPrivateChatPosts.putAll(postsUserReceived)
    }
    if ((null !== postsUserSent) && !postsUserSent.empty) {
      for (       pcpId : postsUserSent.keySet) {
        val postUserSent=postsUserSent.get(pcpId)
        val recipientId=postUserSent.recipientId
        if (!userIdsToPrivateChatPostUuids.containsKey(recipientId)) {
          userIdsToPrivateChatPostUuids.put(recipientId, pcpId)
        }
 else {
          val postIdUserReceived=userIdsToPrivateChatPostUuids.get(recipientId)
          val postUserReceived=postsUserReceived.get(postIdUserReceived)
          val newDate=postUserSent.timeOfPost
          val existingDate=postUserReceived.timeOfPost
          if (newDate.time > existingDate.time) {
            userIdsToPrivateChatPostUuids.put(recipientId, pcpId)
          }
        }
      }
      postUuidsToPrivateChatPosts.putAll(postsUserSent)
    }
    userIdsToPrivateChatPostUuids
  }

private def determineClanUuidsToUserIdSet(  Map<Integer,User> userUuidsToUsers,   List<Integer> clanlessUserUserUuids){
    val clanUuidsToUserIdSet=new HashMap<Integer,Set<Integer>>()
    if ((null === userUuidsToUsers) || userUuidsToUsers.empty) {
      return clanUuidsToUserIdSet;
    }
    for (     userUuid : userUuidsToUsers.keySet) {
      val u=userUuidsToUsers.get(userUuid)
      val clanId=u.clanId
      if (ControllerConstants::NOT_SET === clanId) {
        clanlessUserUserUuids.add(userUuid)
        continue;
      }
      if (clanUuidsToUserIdSet.containsKey(clanId)) {
        val userIdSet=clanUuidsToUserIdSet.get(clanId)
        userIdSet.add(userUuid)
      }
 else {
        val userIdSet=new HashSet<Integer>()
        userIdSet.add(userUuid)
        clanUuidsToUserIdSet.put(clanId, userIdSet)
      }
    }
    clanUuidsToUserIdSet
  }

private def setUserMonsterStuff(  Builder resBuilder,   String userUuid){
    val userMonsters=RetrieveUtils::monsterForUserRetrieveUtils.getMonstersForUser(userUuid)
    if ((null !== userMonsters) && !userMonsters.empty) {
      for (       mfu : userMonsters) {
        val fump=CreateInfoProtoUtils::createFullUserMonsterProtoFromUserMonster(mfu)
        resBuilder.addUsersMonsters(fump)
      }
    }
    val userMonstersHealing=MonsterHealingForUserRetrieveUtils::getMonstersForUser(userUuid)
    if ((null !== userMonstersHealing) && !userMonstersHealing.empty) {
      val healingMonsters=userMonstersHealing.values
      for (       mhfu : healingMonsters) {
        val umhp=CreateInfoProtoUtils::createUserMonsterHealingProtoFromObj(mhfu)
        resBuilder.addMonstersHealing(umhp)
      }
    }
    val userMonstersEnhancing=MonsterEnhancingForUserRetrieveUtils::getMonstersForUser(userUuid)
    if ((null !== userMonstersEnhancing) && !userMonstersEnhancing.empty) {
      val enhancingMonsters=userMonstersEnhancing.values
      var UserEnhancementItemProto baseMonster=null
      val feeders=new ArrayList<UserEnhancementItemProto>()
      for (       mefu : enhancingMonsters) {
        val ueip=CreateInfoProtoUtils::createUserEnhancementItemProtoFromObj(mefu)
        val startTime=mefu.expectedStartTime
        if (null === startTime) {
          baseMonster=ueip
        }
 else {
          feeders.add(ueip)
        }
      }
      val uep=CreateInfoProtoUtils::createUserEnhancementProtoFromObj(userUuid, baseMonster, feeders)
      resBuilder.enhancements=uep
    }
    val userMonsterEvolving=MonsterEvolvingForUserRetrieveUtils::getCatalystUuidsToEvolutionsForUser(userUuid)
    if ((null !== userMonsterEvolving) && !userMonsterEvolving.empty) {
      for (       mefu : userMonsterEvolving.values) {
        val uep=CreateInfoProtoUtils::createUserEvolutionProtoFromEvolution(mefu)
        resBuilder.evolution=uep
      }
    }
  }

private def setBoosterPurchases(  StartupResponseProto::Builder resBuilder){
    val it=goodEquipsRecievedFromBoosterPacks.iterator
    val boosterPurchases=new ArrayList<RareBoosterPurchaseProto>()
    while (it.next) {
      boosterPurchases.add(it.next)
    }
    val c=new Comparator<RareBoosterPurchaseProto>(){
      override compare(      RareBoosterPurchaseProto o1,       RareBoosterPurchaseProto o2){
        if (o1.timeOfPurchase < o2.timeOfPurchase) {
          return -1;
        }
 else         if (o1.timeOfPurchase > o2.timeOfPurchase) {
          return 1;
        }
 else {
          return 0;
        }
      }
    }

    Collections::sort(boosterPurchases, c)
    for (i: 0..<boosterPurchases.size) {
      resBuilder.addRareBoosterPurchases(boosterPurchases.get(i))
    }
  }

private def setFacebookAndExtraSlotsStuff(  Builder resBuilder,   User thisUser,   String userUuid){
    var idsToInvitesToMe=new HashMap<Integer,UserFacebookInviteForSlot>()
    val fbId=thisUser.facebookId
    val List<Integer> specificInviteUuids=null
    val filterByAccepted=true
    var isAccepted=false
    val filterByRedeemed=false
    val isRedeemed=false
    if ((null !== fbId) && !fbId.empty) {
      idsToInvitesToMe=UserFacebookInviteForSlotRetrieveUtils::getSpecificOrAllInvitesForRecipient(fbId, specificInviteUuids, filterByAccepted, isAccepted, filterByRedeemed, isRedeemed)
    }
    isAccepted=true
    val idsToInvitesFromMe=UserFacebookInviteForSlotRetrieveUtils::getSpecificOrAllInvitesForInviter(userUuid, specificInviteUuids, filterByAccepted, isAccepted, filterByRedeemed, isRedeemed)
    val recipientFacebookIds=getRecipientFbUuids(idsToInvitesFromMe)
    val inviterUuidsToInvites=new HashMap<Integer,UserFacebookInviteForSlot>()
    val inviterUserUuids=getInviterUuids(idsToInvitesToMe, inviterUuidsToInvites)
    if (((null === recipientFacebookIds) || recipientFacebookUuids.empty) && ((null === inviterUserUuids) || inviterUserUuids.empty)) {
      return;
    }
    val idsToUsers=RetrieveUtils::userRetrieveUtils.getUsersForFacebookIdsOrUserUuids(recipientFacebookIds, inviterUserUuids)
    val recipients=new ArrayList<User>()
    val inviters=new ArrayList<User>()
    separateUsersIntoRecipientsAndInviters(idsToUsers, recipientFacebookIds, inviterUserUuids, recipients, inviters)
    for (     inviterId : inviterUserUuids) {
      val inviter=idsToUsers.get(inviterId)
      val MinimumUserProtoWithFacebookId inviterProto=null
      val invite=inviterUuidsToInvites.get(inviterId)
      val inviteProto=CreateInfoProtoUtils::createUserFacebookInviteForSlotProtoFromInvite(invite, inviter, inviterProto)
      resBuilder.addInvitesToMeForSlots(inviteProto)
    }
    val thisUserProto=CreateInfoProtoUtils::createMinimumUserProtoWithFacebookIdFromUser(thisUser)
    for (     invite : idsToInvitesFromMe.values) {
      val inviteProto=CreateInfoProtoUtils::createUserFacebookInviteForSlotProtoFromInvite(invite, thisUser, thisUserProto)
      resBuilder.addInvitesFromMeForSlots(inviteProto)
    }
  }

private def getRecipientFbUuids(  Map<Integer,UserFacebookInviteForSlot> idsToInvitesFromMe){
    val fbUuids=new ArrayList<String>()
    for (     invite : idsToInvitesFromMe.values) {
      val fbId=invite.recipientFacebookId
      fbUuids.add(fbId)
    }
    fbUuids
  }

private def getInviterUuids(  Map<Integer,UserFacebookInviteForSlot> idsToInvites,   Map<Integer,UserFacebookInviteForSlot> inviterUuidsToInvites){
    val inviterUuids=new ArrayList<Integer>()
    for (     invite : idsToInvites.values) {
      val userUuid=invite.inviterUserId
      inviterUuids.add(userUuid)
      inviterUuidsToInvites.put(userUuid, invite)
    }
    inviterUuids
  }

private def separateUsersIntoRecipientsAndInviters(  Map<Integer,User> idsToUsers,   List<String> recipientFacebookIds,   List<Integer> inviterUserUuids,   List<User> recipients,   List<User> inviters){
    val recipientFacebookIdsSet=new HashSet<String>(recipientFacebookUuids)
    for (     userUuid : idsToUsers.keySet) {
      val u=idsToUsers.get(userUuid)
      val facebookId=u.facebookId
      if ((null !== facebookId) && recipientFacebookIdsSet.contains(facebookId)) {
        recipients.add(u)
      }
    }
    for (     inviterId : inviterUserUuids) {
      if (idsToUsers.containsKey(inviterId)) {
        val u=idsToUsers.get(inviterId)
        inviters.add(u)
      }
    }
  }

private def setTaskStuff(  Builder resBuilder,   String userUuid){
    val taskUuids=TaskForUserCompletedRetrieveUtils::getAllTaskUuidsForUser(userUuid)
    resBuilder.addAllCompletedTaskUuids(taskUuids)
    val aTaskForUser=TaskForUserOngoingRetrieveUtils::getUserTaskForUserUuid(userUuid)
    if (null !== aTaskForUser) {
      LOG.warn('user has incompleted task userTask=' + aTaskForUser)
      setOngoingTask(resBuilder, userUuid, aTaskForUser)
    }
  }

private def setOngoingTask(  Builder resBuilder,   String userUuid,   TaskForUserOngoing aTaskForUser){
    try {
      val mutp=CreateInfoProtoUtils::createMinimumUserTaskProto(userUuid, aTaskForUser)
      resBuilder.curTask=mutp
      val userTaskId=aTaskForUser.id
      val taskStages=TaskStageForUserRetrieveUtils::getTaskStagesForUserWithTaskForUserUuid(userTaskId)
      val stageNumToTsfu=new HashMap<Integer,List<TaskStageForUser>>()
      for (       tsfu : taskStages) {
        val stageNum=tsfu.stageNum
        if (!stageNumToTsfu.containsKey(stageNum)) {
          val a=new ArrayList<TaskStageForUser>()
          stageNumToTsfu.put(stageNum, a)
        }
        val tsfuList=stageNumToTsfu.get(stageNum)
        tsfuList.add(tsfu)
      }
      val taskId=aTaskForUser.taskId
      for (       stageNum : stageNumToTsfu.keySet) {
        val monsters=stageNumToTsfu.get(stageNum)
        val tsp=CreateInfoProtoUtils::createTaskStageProto(taskId, stageNum, monsters)
        resBuilder.addCurTaskStages(tsp)
      }
    }
 catch (    Exception e) {
      LOG.error('could not create existing user task, letting it get' + ' deleted when user starts another task.', e)
    }
  }

private def setAllStaticData(  Builder resBuilder,   String userUuid,   boolean userIdSet){
    val sdp=MiscMethods::getAllStaticData(userUuid, userIdSet)
    resBuilder.staticDataStuffProto=sdp
  }

private def setEventStuff(  Builder resBuilder,   String userUuid){
    val events=EventPersistentForUserRetrieveUtils::getUserPersistentEventForUserUuid(userUuid)
    for (     epfu : events) {
      val upep=CreateInfoProtoUtils::createUserPersistentEventProto(epfu)
      resBuilder.addUserEvents(upep)
    }
  }

private def pvpBattleStuff(  Builder resBuilder,   User user,   String userUuid,   boolean isFreshRestart,   Timestamp battleEndTime){
    val plfu=pvpLeagueForUserRetrieveUtil.getUserPvpLeagueForId(userUuid)
    val pu=new PvpUser(plfu)
    hazelcastPvpUtil.replacePvpUser(pu, userUuid)
    if (!isFreshRestart) {
      LOG.info('not fresh restart, so not deleting pvp battle stuff')
      return plfu;
    }
    val battle=PvpBattleForUserRetrieveUtils::getPvpBattleForUserForAttacker(userUuid)
    if (null === battle) {
      return plfu;
    }
    val battleStartTime=new Timestamp(battle.battleStartTime.time)
    var eloAttackerLoses=battle.attackerLoseEloChange
    if ((plfu.elo + eloAttackerLoses) < 0) {
      eloAttackerLoses=-1 * plfu.elo
    }
    val defenderId=battle.defenderId
    val eloDefenderWins=battle.defenderLoseEloChange
    penalizeUserForLeavingGameWhileInPvp(userUuid, user, plfu, defenderId, eloAttackerLoses, eloDefenderWins, battleEndTime, battleStartTime, battle)
    plfu
  }

private def penalizeUserForLeavingGameWhileInPvp(  String userUuid,   User user,   PvpLeagueForUser attackerPlfu,   int defenderId,   int eloAttackerLoses,   int eloDefenderWins,   Timestamp battleEndTime,   Timestamp battleStartTime,   PvpBattleForUser battle){
    if (0 !== defenderId) {
      locker.lockPlayer(defenderId, this.

class .simpleName)
    }
    try
{
	val attackerEloBefore = attackerPlfu.elo
	var defenderEloBefore = 0
	val attackerPrevLeague = attackerPlfu.pvpLeagueId
	var attackerCurLeague = 0
	var defenderPrevLeague = 0
	var defenderCurLeague = 0
	val attackerPrevRank = attackerPlfu.rank
	var attackerCurRank = 0
	var defenderPrevRank = 0
	var defenderCurRank = 0
	val attackerCurElo = attackerPlfu.elo + eloAttackerLoses
	attackerCurLeague = PvpLeagueRetrieveUtils::getLeagueIdForElo(attackerCurElo, false,
		attackerPrevLeague)
	attackerCurRank = PvpLeagueRetrieveUtils::getRankForElo(attackerCurElo, attackerCurLeague)
	val attacksLost = attackerPlfu.attacksLost + 1
	var numUpdated = UpdateUtils::get.updatePvpLeagueForUser(userUuid, attackerCurLeague,
		attackerCurRank, eloAttackerLoses, null, null, 0, 0, 1, 0)
	LOG.info("num updated when changing attacker's elo because of reset=" + numUpdated
)
      attackerPlfu.elo=attackerCurElo
      attackerPlfu.pvpLeagueId=attackerCurLeague
      attackerPlfu.rank=attackerCurRank
      attackerPlfu.attacksLost=attacksLost
      val attackerPu=new PvpUser(attackerPlfu)
      hazelcastPvpUtil.replacePvpUser(attackerPu, userUuid)
      if (0 !== defenderId) {
        val defenderPlfu=pvpLeagueForUserRetrieveUtil.getUserPvpLeagueForId(defenderId)
        defenderEloBefore=defenderPlfu.elo
        defenderPrevLeague=defenderPlfu.pvpLeagueId
        defenderPrevRank=defenderPlfu.rank
        val defenderCurElo=defenderEloBefore + eloDefenderWins
        defenderCurLeague=PvpLeagueRetrieveUtils::getLeagueIdForElo(defenderCurElo, false, defenderPrevLeague)
        defenderCurRank=PvpLeagueRetrieveUtils::getRankForElo(defenderCurElo, defenderCurLeague)
        val defensesWon=defenderPlfu.defensesWon + 1
        numUpdated=UpdateUtils::get.updatePvpLeagueForUser(defenderId, defenderCurLeague, defenderCurRank, eloDefenderWins, null, null, 0, 1, 0, 0)
        LOG.info("num updated when changing defender's elo because of reset=" + numUpdated)
        defenderPlfu.elo=defenderCurElo
        defenderPlfu.pvpLeagueId=defenderCurLeague
        defenderPlfu.rank=defenderCurRank
        defenderPlfu.defensesWon=defensesWon
        val defenderPu=new PvpUser(defenderPlfu)
        hazelcastPvpUtil.replacePvpUser(defenderPu, defenderId)
      }
      val attackerWon=false
      val cancelled=false
      val defenderGotRevenge=false
      val displayToDefender=true
      val numInserted=InsertUtils::get.insertIntoPvpBattleHistory(userUuid, defenderId, battleEndTime, battleStartTime, eloAttackerLoses, attackerEloBefore, eloDefenderWins, defenderEloBefore, attackerPrevLeague, attackerCurLeague, defenderPrevLeague, defenderCurLeague, attackerPrevRank, attackerCurRank, defenderPrevRank, defenderCurRank, 0, 0, 0, 0, attackerWon, cancelled, defenderGotRevenge, displayToDefender)
      LOG.info('numInserted into battle history=' + numInserted)
      val numDeleted=DeleteUtils::get.deletePvpBattleForUser(userUuid)
      LOG.info('successfully penalized, rewarded attacker and defender respectively. battle= ' + battle + '	 numDeleted='+ numDeleted)
    }
 catch (    Exception e) {
      LOG.error('tried to penalize, reward attacker and defender respectively. battle=' + battle, e)
    }
 finally {
      if (0 !== defenderId) {
        locker.unlockPlayer(defenderId, this.

class .simpleName)
      }
    }
  }
  private def pvpBattleHistoryStuff(Builder resBuilder, User user, String userUuid)
{
	val n = ControllerConstants.PVP_HISTORY__NUM_RECENT_BATTLES
	val historyList = pvpBattleHistoryRetrieveUtil.getRecentNBattlesForDefenderId(userUuid, n)
	LOG.info('historyList=' + historyList)
	val attackerUuids = pvpBattleHistoryRetrieveUtil.getAttackerUuidsFromHistory(historyList)
	LOG.info('attackerUuids=' + attackerUuids)
	if ((null === attackerUuids) || attackerUuids.empty)
	{
		LOG.info('no valid 10 pvp battles for user. ')
		return;
	}
	val idsToAttackers = RetrieveUtils::userRetrieveUtils.getUsersByUuids(attackerUuids)
	LOG.info('idsToAttackers=' + idsToAttackers)
	val attackerUuidsList = new ArrayList<Integer>(idsToAttackers.keySet)
	val attackerIdToCurTeam = selectMonstersForUsers(attackerUuidsList)
	LOG.info('history monster teams=' + attackerIdToCurTeam)
	val attackerUuidsToProspectiveCashWinnings = MiscMethods::
		calculateCashRewardFromPvpUsers(idsToAttackers)
	val attackerUuidsToProspectiveOilWinnings = MiscMethods::
		calculateOilRewardFromPvpUsers(idsToAttackers)
	val historyProtoList = CreateInfoProtoUtils::createPvpHistoryProto(historyList,
		idsToAttackers, attackerIdToCurTeam, attackerUuidsToProspectiveCashWinnings,
		attackerUuidsToProspectiveOilWinnings)
	resBuilder.addAllRecentNBattles(historyProtoList)
}

private def selectMonstersForUsers(List<Integer> userIdList)
{
	val userUuidsToUserMonsters = new HashMap<Integer, List<MonsterForUser>>()
	val userIdsToMfuUuidsToMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
		getCompleteMonstersForUser(userIdList)
	for (index : 0 ..< userIdList.size)
	{
		val defenderId = userIdList.get(index)
		val mfuUuidsToMonsters = userIdsToMfuUuidsToMonsters.get(defenderId)
		if ((null === mfuUuidsToMonsters) || mfuUuidsToMonsters.empty)
		{
			LOG.error(
				'WTF!!!!!!!! user has no monsters!!!!! userUuid=' + defenderId +
					'	 will move on to next guy.')
        continue;
		}
		val defenderMonsters = selectMonstersForUser(mfuUuidsToMonsters)
		userUuidsToUserMonsters.put(defenderId, defenderMonsters)
	}
	userUuidsToUserMonsters
}

private def selectMonstersForUser(Map<Long, MonsterForUser> mfuUuidsToMonsters)
{
	val defenderMonsters = getEquippedMonsters(mfuUuidsToMonsters)
	if (defenderMonsters.empty)
	{
		val randMonsters = new ArrayList<MonsterForUser>(mfuUuidsToMonsters.values)
		defenderMonsters.add(randMonsters.get(0))
	}
	defenderMonsters
}

private def getEquippedMonsters(Map<Long, MonsterForUser> userMonsters)
{
	val equipped = new ArrayList<MonsterForUser>()
	for (mfu : userMonsters.values)
	{
		if (mfu.teamSlotNum <= 0)
		{
			continue;
		}
		equipped.add(mfu)
	}
	equipped
}

private def setClanRaidStuff(Builder resBuilder, User user, String userUuid, Timestamp now)
{
	val nowDate = new Date(now.time)
	val clanId = user.clanId
	if (clanId <= 0)
	{
		return;
	}
	val cepfc = ClanEventPersistentForClanRetrieveUtils::getPersistentEventForClanId(clanId)
	if (null === cepfc)
	{
		LOG.info('no clan raid stuff existing for clan=' + clanId + '	 user=' + user)
		return;
	}
	val pcecip = CreateInfoProtoUtils::createPersistentClanEventClanInfoProto(cepfc)
	resBuilder.curRaidClanInfo = pcecip
	val userIdToCepfu = ClanEventPersistentForUserRetrieveUtils::
		getPersistentEventUserInfoForClanId(clanId)
	LOG.info('the users involved in clan raid:' + userIdToCepfu)
	if ((null === userIdToCepfu) || userIdToCepfu.empty)
	{
		LOG.info('no users involved in clan raid. clanRaid=' + cepfc)
		return;
	}
	val userMonsterUuids = MonsterStuffUtils::getUserMonsterUuidsInClanRaid(userIdToCepfu)
	val idsToUserMonsters = RetrieveUtils::monsterForUserRetrieveUtils.
		getSpecificUserMonsters(userMonsterUuids)
	for (cepfu : userIdToCepfu.values)
	{
		val pceuip = CreateInfoProtoUtils::
			createPersistentClanEventUserInfoProto(cepfu, idsToUserMonsters, null)
		resBuilder.addCurRaidClanUserInfo(pceuip)
	}
	setClanRaidHistoryStuff(resBuilder, userUuid, nowDate)
}

private def setClanRaidHistoryStuff(Builder resBuilder, String userUuid, Date nowDate)
{
	val nDays = ControllerConstants.CLAN_EVENT_PERSISTENT__NUM_DAYS_FOR_RAID_STAGE_HISTORY
	val timesToRaidStageHistory = CepfuRaidStageHistoryRetrieveUtils::
		getRaidStageHistoryForPastNDaysForUserUuid(userUuid, nDays, nowDate, timeUtils)
	val timesToUserRewards = ClanEventPersistentUserRewardRetrieveUtils::
		getCepUserRewardForPastNDaysForUserUuid(userUuid, nDays, nowDate, timeUtils)
	for (aDate : timesToRaidStageHistory.keySet)
	{
		val cepfursh = timesToRaidStageHistory.get(aDate)
		var List<ClanEventPersistentUserReward> rewards = null
		if (timesToUserRewards.containsKey(aDate))
		{
			rewards = timesToUserRewards.get(aDate)
		}
		val stageProto = CreateInfoProtoUtils::
			createPersistentClanEventRaidStageHistoryProto(cepfursh, rewards)
		resBuilder.addRaidStageHistory(stageProto)
	}
}

private def setAchievementStuff(Builder resBuilder, String userUuid)
{
	val achievementIdToUserAchievements = achievementForUserRetrieveUtil.
		getSpecificOrAllAchievementIdToAchievementForUserUuid(userUuid, null)
	for (afu : achievementIdToUserAchievements.values)
	{
		val uap = CreateInfoProtoUtils::createUserAchievementProto(afu)
		resBuilder.addUserAchievements(uap)
	}
}

private def setMiniJob(Builder resBuilder, String userUuid)
{
	val miniJobIdToUserMiniJobs = miniJobForUserRetrieveUtil.
		getSpecificOrAllIdToMiniJobForUser(userUuid, null)
	if (miniJobIdToUserMiniJobs.empty)
	{
		return;
	}
	val mjfuList = new ArrayList<MiniJobForUser>(miniJobIdToUserMiniJobs.values)
	val umjpList = CreateInfoProtoUtils::createUserMiniJobProtos(mjfuList, null)
	resBuilder.addAllUserMiniJobProtos(umjpList)
}

private def sendOfferChartInstall(Date installTime, String advertiserId)
{
	val clientId = '15'
	val appId = '648221050'
	val geo = 'N/A'
	val installTimeStr = '' + installTime.time
	val devicePlatform = 'iphone'
	val deviceType = 'iphone'
	val urlString = 'http://offerchart.com/mobileapp/api/send_install_ping?' + 'client_id=' +
		clientId + '&app_id=' + appId + '&device_id=' + advertiserId + '&device_type=' +
		deviceType + '&geo=' + geo + '&install_time=' + installTimeStr + '&device_platform=' +
		devicePlatform
	LOG.info(
		'Sending offerchart request:
' + urlString)
	val httpclient = new DefaultHttpClient()
	val httpGet = new HttpGet(urlString)
	try
	{
		val response1 = httpclient.execute(httpGet)
		val rd = new BufferedReader(new InputStreamReader(response1.entity.content))
		var responseString = ''
		var String line
		while ((line = rd.readLine) !== null)
		{
			responseString += line
		}
		LOG.info('Received response: ' + responseString)
	}
	catch (Exception e)
	{
		LOG.error('failed to make offer chart call', e)
	}
}

private def retrieveKabamNaid(User user, String openUdid, String mac, String advertiserId)
{
	var String host
	val port = 443
	var int clientId
	var String secret
	if (Globals::IS_SANDBOX)
	{
		host = KabamProperties.SANDBOX_API_URL
		clientId = KabamProperties.SANDBOX_CLIENT_ID
		secret = KabamProperties.SANDBOX_SECRET
	}
	else
	{
		host = KabamProperties.PRODUCTION_API_URL
		clientId = KabamProperties.PRODUCTION_CLIENT_ID
		secret = KabamProperties.PRODUCTION_SECRET
	}
	val kabamApi = new KabamApi(host, port, secret)
	val userUuid = openUdid
	val platform = 'iphone'
	val biParams = '{"open_udid":"' + userUuid + '","mac":"' + mac + '","mac_hash":"' +
		DigestUtils::md5Hex(mac) + '","advertiser_id":"' + advertiserId + '"}'
	var MobileNaidResponse naidResponse
	try
	{
		naidResponse = kabamApi.mobileGetNaid(userUuid, clientId, platform, biParams,
			new Date().time / 1000)
	}
	catch (Exception e)
	{
		e.printStackTrace
		return '';
	}
	if (naidResponse.returnCode === ResponseCode::Success)
	{
		if (user !== null)
		{
			user.updateSetKabamNaid(naidResponse.naid)
		}
		LOG.info('Successfully got kabam naid.')
		return naidResponse.naid + '';
	}
	else
	{
		LOG.error('Error retrieving kabam naid: ' + naidResponse.returnCode)
	}
	''
}

private def setLockBoxEvents(StartupResponseProto ::Builder resBuilder
,   User user){
  }

protected def updateLeaderboard(  String apsalarId,   User user,   Timestamp now,   int newNumConsecutiveDaysLoggedIn){
    if (user !== null) {
      LOG.info('Updating leaderboard for user ' + user.id)
      syncApsalaridLastloginConsecutivedaysloggedinResetBadges(user, apsalarId, now, newNumConsecutiveDaysLoggedIn)
      val leaderboard=AppContext::applicationContext.getBean(typeof(LeaderBoardUtil))
      leaderboard.updateLeaderboardForUser(user, null)
    }
  }

private def setUnhandledForgeAttempts(  Builder resBuilder,   User user){
  }

private def setWhetherPlayerCompletedInAppPurchase(  Builder resBuilder,   User user){
    val hasPurchased=IAPHistoryRetrieveUtils::checkIfUserHasPurchased(user.id)
    resBuilder.playerHasBoughtInAppPurchase=hasPurchased
  }

private def purchaseBoosterPack(  int boosterPackId,   User aUser,   int numBoosterItemsUserWants,   Timestamp now){
    val equipId=ControllerConstants.NOT_SET
    equipId
  }

private def writeBoosterStuffToDB(  User aUser,   Map<Integer,Integer> boosterItemUuidsToNumCollected,   Map<Integer,Integer> newBoosterItemUuidsToNumCollected,   List<BoosterItem> itemsUserReceives,   List<Boolean> collectedBeforeReset,   boolean resetOccurred,   Timestamp now,   List<Long> userEquipUuidsForHistoryTable){
    true
  }

private def writeDailyBonusRewardToDB(  User aUser,   Map<String,Integer> currentDayReward,   boolean giveToUser,   Timestamp now,   List<Integer> equipIdRewardedList){
    var equipId=ControllerConstants.NOT_SET
    if (!giveToUser || (null === currentDayReward) || (0 === currentDayReward.size)) {
      return false;
    }
    var key=''
    var value=ControllerConstants.NOT_SET
    if (1 === currentDayReward.size) {
      val keys=new String[1]
      currentDayReward.keySet.toArray(keys)
      key=keys[0]
      value=currentDayReward.get(key)
    }
 else {
      LOG.error("unexpected error: current day's reward for a user is more than one. rewards=" + currentDayReward)
      return false;
    }
    val previousSilver=aUser.cash
    val previousGold=aUser.gems
    if (key == MiscMethods::boosterPackId) {
      val numBoosterItemsUserWants=1
      equipId=purchaseBoosterPack(value, aUser, numBoosterItemsUserWants, now)
      if (ControllerConstants::NOT_SET === equipId) {
        LOG.error("unexpected error: failed to 'buy' booster pack for user. packId=" + value + ', user='+ aUser)
        return false;
      }
    }
    if (key == MiscMethods::cash) {
      if (!aUser.updateRelativeCashNaive(value)) {
        LOG.error('unexpected error: could not give silver bonus of ' + value + ' to user '+ aUser)
        return false;
      }
 else {
        writeToUserCurrencyHistory(aUser, key, previousSilver, currentDayReward)
      }
    }
    if (key == MiscMethods::gems) {
      if (!aUser.updateRelativeGemsNaive(value)) {
        LOG.error('unexpected error: could not give silver bonus of ' + value + ' to user '+ aUser)
        return false;
      }
 else {
        writeToUserCurrencyHistory(aUser, key, previousGold, currentDayReward)
      }
    }
    equipIdRewardedList.add(equipId)
    true
  }

private def writeToUserDailyBonusRewardHistory(  User aUser,   Map<String,Integer> rewardForUser,   int nthConsecutiveDay,   Timestamp now,   int equipIdRewarded){
  }

private def syncApsalaridLastloginConsecutivedaysloggedinResetBadges(  User user,   String apsalarId,   Timestamp loginTime,   int newNumConsecutiveDaysLoggedIn){
    if ((user.apsalarId !== null) && (apsalarId === null)) {
      apsalarId=user.apsalarId
    }
    if (!user.updateAbsoluteApsalaridLastloginBadgesNumConsecutiveDaysLoggedIn(apsalarId, loginTime, 0, newNumConsecutiveDaysLoggedIn)) {
      LOG.error('problem with updating apsalar id to ' + apsalarId + ', last login to '+ loginTime+ ', and badge count to 0 for '+ user+ ' and newNumConsecutiveDaysLoggedIn is '+ newNumConsecutiveDaysLoggedIn)
    }
    if (!InsertUtils::get.insertLastLoginLastLogoutToUserSessions(user.id, loginTime, null)) {
      LOG.error('problem with inserting last login time for user ' + user + ', loginTime='+ loginTime)
    }
    if (user.numBadges !== 0) {
      if (user.deviceToken !== null) {
      }
    }
  }

private def setAllies(  Builder resBuilder,   User user){
  }

private def setConstants(  Builder startupBuilder,   StartupStatus startupStatus){
    startupBuilder.startupConstants=MiscMethods::createStartupConstantsProto(globals)
    if (startupStatus === StartupStatus::USER_NOT_IN_DB) {
      tutorialConstants=startupBuilder
    }
  }

private def setTutorialConstants(  Builder resBuilder){
  }
  def writeToUserCurrencyHistory(  User aUser,   String goldSilver,   int previousMoney,   Map<String,Integer> goldSilverChange){
  }
  def getHazelcastPvpUtil(){
    hazelcastPvpUtil
  }
  def setHazelcastPvpUtil(  HazelcastPvpUtil hazelcastPvpUtil){
    this.hazelcastPvpUtil=hazelcastPvpUtil
  }
  def getLocker(){
    locker
  }
  def setLocker(  Locker locker){
    this.locker=locker
  }
  def getTimeUtils(){
    timeUtils
  }
  def setTimeUtils(  TimeUtils timeUtils){
    this.timeUtils=timeUtils
  }
  def getGlobals(){
    globals
  }
  def setGlobals(  Globals globals){
    this.globals=globals
  }
  def getQuestJobForUserRetrieveUtil(){
    questJobForUserRetrieveUtil
  }
  def setQuestJobForUserRetrieveUtil(  QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil){
    this.questJobForUserRetrieveUtil=questJobForUserRetrieveUtil
  }
  def getPvpLeagueForUserRetrieveUtil(){
    pvpLeagueForUserRetrieveUtil
  }
  def setPvpLeagueForUserRetrieveUtil(  PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil){
    this.pvpLeagueForUserRetrieveUtil=pvpLeagueForUserRetrieveUtil
  }
  def getPvpBattleHistoryRetrieveUtil(){
    pvpBattleHistoryRetrieveUtil
  }
  def setPvpBattleHistoryRetrieveUtil(  PvpBattleHistoryRetrieveUtil pvpBattleHistoryRetrieveUtil){
    this.pvpBattleHistoryRetrieveUtil=pvpBattleHistoryRetrieveUtil
  }
  def getAchievementForUserRetrieveUtil(){
    achievementForUserRetrieveUtil
  }
  def setAchievementForUserRetrieveUtil(  AchievementForUserRetrieveUtil achievementForUserRetrieveUtil){
    this.achievementForUserRetrieveUtil=achievementForUserRetrieveUtil
  }
  def getMiniJobForUserRetrieveUtil(){
    miniJobForUserRetrieveUtil
  }
  def setMiniJobForUserRetrieveUtil(  MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil){
    this.miniJobForUserRetrieveUtil=miniJobForUserRetrieveUtil
  }
}
