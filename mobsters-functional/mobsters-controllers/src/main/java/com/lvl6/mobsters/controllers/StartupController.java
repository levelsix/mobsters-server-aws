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

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.controllers.utils.ConfigurationDataUtil;
import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;
import com.lvl6.mobsters.dynamo.User;
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
import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.info.BaseIntPersistentObject;
import com.lvl6.mobsters.info.BoosterPack;
import com.lvl6.mobsters.info.EventPersistent;
import com.lvl6.mobsters.info.Item;
import com.lvl6.mobsters.info.Monster;
import com.lvl6.mobsters.info.MonsterBattleDialogue;
import com.lvl6.mobsters.info.Obstacle;
import com.lvl6.mobsters.info.PvpLeague;
import com.lvl6.mobsters.info.StaticUserLevelInfo;
import com.lvl6.mobsters.info.StructureHospital;
import com.lvl6.mobsters.info.StructureLab;
import com.lvl6.mobsters.info.StructureMiniJob;
import com.lvl6.mobsters.info.StructureResidence;
import com.lvl6.mobsters.info.StructureResourceGenerator;
import com.lvl6.mobsters.info.StructureResourceStorage;
import com.lvl6.mobsters.info.StructureTownHall;
import com.lvl6.mobsters.info.Task;
import com.lvl6.mobsters.info.repository.AchievementRepository;
import com.lvl6.mobsters.info.repository.BoosterPackRepository;
import com.lvl6.mobsters.info.repository.EventPersistentRepository;
import com.lvl6.mobsters.info.repository.ItemRepository;
import com.lvl6.mobsters.info.repository.MonsterBattleDialogueRepository;
import com.lvl6.mobsters.info.repository.MonsterRepository;
import com.lvl6.mobsters.info.repository.ObstacleRepository;
import com.lvl6.mobsters.info.repository.PvpLeagueRepository;
import com.lvl6.mobsters.info.repository.QuestRepository;
import com.lvl6.mobsters.info.repository.StaticUserLevelInfoRepository;
import com.lvl6.mobsters.info.repository.StructureHospitalRepository;
import com.lvl6.mobsters.info.repository.StructureLabRepository;
import com.lvl6.mobsters.info.repository.StructureMiniJobRepository;
import com.lvl6.mobsters.info.repository.StructureResidenceRepository;
import com.lvl6.mobsters.info.repository.StructureResourceGeneratorRepository;
import com.lvl6.mobsters.info.repository.StructureResourceStorageRepository;
import com.lvl6.mobsters.info.repository.StructureTownHallRepository;
import com.lvl6.mobsters.info.repository.TaskRepository;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.FullUserQuestProto;
import com.lvl6.mobsters.noneventproto.NoneventStaticDataProto.StaticDataProto;
import com.lvl6.mobsters.noneventproto.utils.NoneventAchievementProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventBoosterPackProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventClanProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventEventPersistentProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventMonsterProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventPvpProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventQuestProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventStructureProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventTaskProtoSerializer;
import com.lvl6.mobsters.noneventproto.utils.NoneventUserProtoSerializer;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.clan.ClanService;
import com.lvl6.mobsters.services.monster.MonsterService;
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
	protected NoneventUserProtoSerializer noneventUserProtoSerializer;

	@Autowired
	protected QuestService questService;
	
	@Autowired
	protected QuestRepository questRepository;
	
	@Autowired
	protected NoneventQuestProtoSerializer noneventQuestProtoSerializer;
	
	@Autowired
	protected ClanService clanService;
	
	@Autowired
	protected NoneventClanProtoSerializer noneventClanProtoSerializer;
	
	@Autowired
	protected MonsterService monsterService;
	
	@Autowired
	protected TaskRepository taskRepository;
	
	@Autowired
	protected NoneventTaskProtoSerializer noneventTaskProtoSerializer;
	
	@Autowired
	protected MonsterRepository monsterRepository;
	
	@Autowired
	protected NoneventMonsterProtoSerializer noneventMonsterProtoSerializer;
	
	@Autowired
	protected StaticUserLevelInfoRepository staticUserLevelInfoRepository;
	
	@Autowired
	protected BoosterPackRepository boosterPackRepository;
	
	@Autowired
	protected NoneventBoosterPackProtoSerializer noneventBoosterPackProtoSerializer;
	
	@Autowired
	protected NoneventStructureProtoSerializer noneventStructureProtoSerializer;
	
	@Autowired
	protected StructureResourceGeneratorRepository structureResourceGeneratorRepository;
	
	@Autowired
	protected StructureResourceStorageRepository structureResourceStorageRepository;
	
	@Autowired
	protected StructureHospitalRepository structureHospitalRepository;
	
	@Autowired
	protected StructureResidenceRepository structureResidenceRepository;
	
	@Autowired
	protected StructureTownHallRepository structureTownHallRepository;
	
	@Autowired
	protected StructureLabRepository structureLabRepository;
	
	@Autowired
	protected StructureMiniJobRepository structureMiniJobRepository;
	
	@Autowired
	protected EventPersistentRepository eventPersistentRepository;
	
	@Autowired
	protected NoneventEventPersistentProtoSerializer eventPersistentProtoSerializer;
	
	@Autowired
	protected MonsterBattleDialogueRepository monsterBattleDialogueRepository;
	
	@Autowired
	protected ItemRepository itemRepository;
	
	@Autowired
	protected ObstacleRepository obstacleRepository;
	
	@Autowired
	protected PvpLeagueRepository pvpLeagueRepository;
	
	@Autowired
	protected NoneventPvpProtoSerializer noneventPvpProtoSerializer;
	
	@Autowired
	protected AchievementRepository achievementRepository;
	
	@Autowired
	protected NoneventAchievementProtoSerializer noneventAchievementProtoSerializer;
	
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
				user = userService.getUserCredentialByFacebookIdOrUdid(fbId, udid);
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
		setUserClanInfos(resBuilder, userId);
//		LOG.info("{}ms at setUserClanInfos", stopWatch.getTime());
//		setNotifications(resBuilder, user);
//		LOG.info("{}ms at setNotifications", stopWatch.getTime());
//		setNoticesToPlayers(resBuilder);
//		LOG.info("{}ms at setNoticesToPlayers", stopWatch.getTime());
//		setGroupChatMessages(resBuilder, user);
//		LOG.info("{}ms at groupChatMessages", stopWatch.getTime());
//		setPrivateChatPosts(resBuilder, user, userId);
//		LOG.info("{}ms at privateChatPosts", stopWatch.getTime());
		setUserMonsterStuff(resBuilder, userId);
//		LOG.info("{}ms at setUserMonsterStuff", stopWatch.getTime());
//		setFacebookAndExtraSlotsStuff(resBuilder, user, userId);
//		LOG.info("{}ms at facebookAndExtraSlotsStuff", stopWatch.getTime());
//		setTaskStuff(resBuilder, userId);
//		LOG.info("{}ms at task stuff", stopWatch.getTime());
		setAllStaticData(resBuilder, userId, true);
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
		
		Collection<?> configQuests = questRepository.findByIdIn(inProgressQuestIds);
		
		Map<Integer, BaseIntPersistentObject> questIdToQuests =
			ConfigurationDataUtil.mapifyConfigurationData(configQuests);

		//generate the user quests
		List<FullUserQuestProto> currentUserQuests = noneventQuestProtoSerializer
			.createFullUserQuestDataLarges(inProgressQuests, questIdToQuests,
				questIdToUserQuestJobs);
		resBuilder.addAllUserQuests(currentUserQuests);

		//send the redeemed quest ids
		resBuilder.addAllRedeemedQuestIds(redeemedQuestIds);
	}
	
	private void setUserClanInfos(Builder resBuilder, String userId) {
		List<ClanForUser> userClans = clanService.findByUserId(userId);
		
		for (ClanForUser cfu : userClans) {
			resBuilder.addUserClanInfo(
				noneventClanProtoSerializer.createFullUserClanProtoFromUserClan(cfu));
		}
	}
	
	/*private void setNoticesToPlayers(Builder resBuilder) {
		// TODO: Fill in the place holder
		List<String> notices = null;//StartupStuffRetrieveUtils.getAllActiveAlerts();
	  	if (null != notices) {
	  	  for (String notice : notices) {
	  	    resBuilder.addNoticesToPlayers(notice);
	  	  }
	  	}
	}*/
	
	private void setGroupChatMessages(Builder resBuilder, User user) {
		// TODO: Fill in
	}
	
	private void setUserMonsterStuff(Builder resBuilder, String userId) {
		// TODO: Fill in
		List<MonsterForUser> userMonsters= monsterService.getMonstersForUser(userId);
		
		if (!CollectionUtils.lacksSubstance(userMonsters)) {
			for (MonsterForUser mfu : userMonsters) {
				resBuilder.addUsersMonsters(
					noneventMonsterProtoSerializer.createFullUserMonsterProtoFromUserMonster(mfu));
			}
		}
		
		//monsters in healing
		List<MonsterHealingForUser> userMonstersHealing =
			monsterService.getMonstersInHealingForUser(userId);
		if (!CollectionUtils.lacksSubstance(userMonstersHealing)) {
			for (MonsterHealingForUser mhfu : userMonstersHealing) {
				resBuilder.addMonstersHealing(
					noneventMonsterProtoSerializer.createUserMonsterHealingProto(mhfu));
			}
		}
		
		//monsters in enhancing
	}
	
	private void setAllStaticData(Builder resBuilder, String userId, boolean userIdSet) {
		StaticDataProto.Builder sdpb = StaticDataProto.newBuilder();
		
		setTasks(sdpb);
		setMonsters(sdpb);
		setUserLevelInfo(sdpb);
		setBoosterPackInfo(sdpb);
		setStructures(sdpb);
		setEvents(sdpb);
		setMonsterDialogue(sdpb);
//	    setClanRaidStuff(sdpb);
	    setItems(sdpb);
	    setObstacleStuff(sdpb);
//	    setClanIconStuff(sdpb);
	    setPvpLeagueStuff(sdpb);
	    setAchievementStuff(sdpb);

		resBuilder.setStaticDataStuffProto(sdpb.build());
	}
	
	private void setTasks(StaticDataProto.Builder sdpb) {
		List<Task> tasks = taskRepository.findAll();
		for (Task aTask : tasks) {
			sdpb.addAllTasks(
				noneventTaskProtoSerializer.createFullTaskProtoFromTask(aTask));
		}
	}
	
	private void setMonsters(StaticDataProto.Builder sdpb) {
		List<Monster> monsterList = monsterRepository.findAll();
		for (Monster monster : monsterList) {
			sdpb.addAllMonsters(
				noneventMonsterProtoSerializer.createMonsterProto(monster));
		}
	}
	
	private void setUserLevelInfo(StaticDataProto.Builder sdpb) {
		List<StaticUserLevelInfo> levelInfoList = staticUserLevelInfoRepository.findAll();
		for (StaticUserLevelInfo levelInfo : levelInfoList) {
			sdpb.addSlip(
				noneventUserProtoSerializer.createStaticUserLevelInfoProto(levelInfo));
		}
	}
	
	private void setBoosterPackInfo(StaticDataProto.Builder sdpb) {
		List<BoosterPack> boosterPackList = boosterPackRepository.findAll();
		for (BoosterPack bp : boosterPackList) {
			sdpb.addBoosterPacks(
				noneventBoosterPackProtoSerializer.createBoosterPackProto(bp));
		}
	}
	
	private void setStructures(StaticDataProto.Builder sdpb) {
		
		setGenerators(sdpb);
		setStorages(sdpb);
		setHospitals(sdpb);
	    setResidences(sdpb);
	    setTownHalls(sdpb);
	    setLabs(sdpb);
	    setMiniJobCenters(sdpb);
	}
	
	private void setGenerators(StaticDataProto.Builder sdpb) {
		List<StructureResourceGenerator> resourceGenerators =
			structureResourceGeneratorRepository.findAll();
		for (StructureResourceGenerator srg : resourceGenerators) {
			sdpb.addAllGenerators(
				noneventStructureProtoSerializer.createResourceGeneratorProto(srg));
		}
	}
	
	private void setStorages(StaticDataProto.Builder sdpb) {
		List<StructureResourceStorage> resourceStorages =
			structureResourceStorageRepository.findAll();
		for (StructureResourceStorage srg : resourceStorages) {
			sdpb.addAllStorages(
				noneventStructureProtoSerializer.createResourceStorageProto(srg));
		}
	}

	private void setHospitals(StaticDataProto.Builder sdpb) {
		List<StructureHospital> hospitals = structureHospitalRepository.findAll();
		for (StructureHospital sh : hospitals) {
			sdpb.addAllHospitals(
				noneventStructureProtoSerializer.createHospitalProto(sh));
		}
	}

	private void setResidences(StaticDataProto.Builder sdpb) {
		List<StructureResidence> residences = structureResidenceRepository.findAll();
		for (StructureResidence sr : residences) {
			sdpb.addAllResidences(
				noneventStructureProtoSerializer.createResidenceProto(sr));
		}
	}

	private void setTownHalls(StaticDataProto.Builder sdpb) {
		List<StructureTownHall> townHalls = structureTownHallRepository.findAll();
		for (StructureTownHall sth : townHalls) {
			sdpb.addAllTownHalls(
				noneventStructureProtoSerializer.createTownHallProto(sth));
		}
	}

	private void setLabs(StaticDataProto.Builder sdpb) {
		List<StructureLab> labs = structureLabRepository.findAll();
		for (StructureLab sl : labs) {
			sdpb.addAllLabs(
				noneventStructureProtoSerializer.createLabProto(sl));
		}
	}

	private void setMiniJobCenters(StaticDataProto.Builder sdpb) {
		List<StructureMiniJob> miniJobCenters = structureMiniJobRepository.findAll();
		for (StructureMiniJob smj : miniJobCenters) {
			sdpb.addAllMiniJobCenters(
				noneventStructureProtoSerializer.createMiniJobCenterProto(smj));
		}
	}
	
	private void setEvents(StaticDataProto.Builder sdpb) {
		List<EventPersistent> persistentEvents = eventPersistentRepository.findAll();
		for (EventPersistent ep : persistentEvents) {
			sdpb.addPersistentEvents(
				eventPersistentProtoSerializer.createPersistentEventProtoFromEvent(ep));
		}
		
	}
	
	private void setMonsterDialogue(StaticDataProto.Builder sdpb) {
		List<MonsterBattleDialogue> dialogues = monsterBattleDialogueRepository.findAll();
		for (MonsterBattleDialogue mbd : dialogues) {
			sdpb.addMbds(
				noneventMonsterProtoSerializer.createMonsterBattleDialogueProto(mbd));
		}
	}
	
	private void setItems(StaticDataProto.Builder sdpb) {
		List<Item> items = itemRepository.findAll();
		
		for (Item i : items) {
			sdpb.addItems(
				noneventQuestProtoSerializer.createItemProtoFromItem(i));
		}
	}
	
	private void setObstacleStuff(StaticDataProto.Builder sdpb) {
		List<Obstacle> obstacles = obstacleRepository.findAll();
		
		for (Obstacle o : obstacles) {
			sdpb.addObstacles(
				noneventStructureProtoSerializer.createObstacleProto(o));
		}
	}
	
	private void setPvpLeagueStuff(StaticDataProto.Builder sdpb) {
		List<PvpLeague> pvpLeagues = pvpLeagueRepository.findAll();
		
		for (PvpLeague pl : pvpLeagues) {
			sdpb.addLeagues(
				noneventPvpProtoSerializer.createPvpLeagueProto(pl));
		}
	}
	
	private void setAchievementStuff(StaticDataProto.Builder sdpb) {
		List<Achievement> achievements = achievementRepository.findAll();
		for (Achievement a : achievements) {
			sdpb.addAchievements(
				noneventAchievementProtoSerializer.createAchievementProto(a));
		}
	}
	
	//TODO: Generate the getters and setters for the autowired properties 

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService( UserService userService )
	{
		this.userService = userService;
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

	public QuestService getQuestService()
	{
		return questService;
	}

	public void setQuestService( QuestService questService )
	{
		this.questService = questService;
	}

	public QuestRepository getQuestRepository()
	{
		return questRepository;
	}

	public void setQuestRepository( QuestRepository questRepository )
	{
		this.questRepository = questRepository;
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

	public ClanService getClanService()
	{
		return clanService;
	}

	public void setClanService( ClanService clanService )
	{
		this.clanService = clanService;
	}

	public NoneventClanProtoSerializer getNoneventClanProtoSerializer()
	{
		return noneventClanProtoSerializer;
	}

	public void setNoneventClanProtoSerializer(
		NoneventClanProtoSerializer noneventClanProtoSerializer )
	{
		this.noneventClanProtoSerializer = noneventClanProtoSerializer;
	}

	public MonsterService getMonsterService()
	{
		return monsterService;
	}

	public void setMonsterService( MonsterService monsterService )
	{
		this.monsterService = monsterService;
	}

	public TaskRepository getTaskRepository()
	{
		return taskRepository;
	}

	public void setTaskRepository( TaskRepository taskRepository )
	{
		this.taskRepository = taskRepository;
	}

	public NoneventTaskProtoSerializer getNoneventTaskProtoSerializer()
	{
		return noneventTaskProtoSerializer;
	}

	public void setNoneventTaskProtoSerializer(
		NoneventTaskProtoSerializer noneventTaskProtoSerializer )
	{
		this.noneventTaskProtoSerializer = noneventTaskProtoSerializer;
	}

	public MonsterRepository getMonsterRepository()
	{
		return monsterRepository;
	}

	public void setMonsterRepository( MonsterRepository monsterRepository )
	{
		this.monsterRepository = monsterRepository;
	}

	public NoneventMonsterProtoSerializer getNoneventMonsterProtoSerializer()
	{
		return noneventMonsterProtoSerializer;
	}

	public void setNoneventMonsterProtoSerializer(
		NoneventMonsterProtoSerializer noneventMonsterProtoSerializer )
	{
		this.noneventMonsterProtoSerializer = noneventMonsterProtoSerializer;
	}

	public StaticUserLevelInfoRepository getStaticUserLevelInfoRepository()
	{
		return staticUserLevelInfoRepository;
	}

	public void setStaticUserLevelInfoRepository(
		StaticUserLevelInfoRepository staticUserLevelInfoRepository )
	{
		this.staticUserLevelInfoRepository = staticUserLevelInfoRepository;
	}

	public BoosterPackRepository getBoosterPackRepository()
	{
		return boosterPackRepository;
	}

	public void setBoosterPackRepository( BoosterPackRepository boosterPackRepository )
	{
		this.boosterPackRepository = boosterPackRepository;
	}

	public NoneventBoosterPackProtoSerializer getNoneventBoosterPackProtoSerializer()
	{
		return noneventBoosterPackProtoSerializer;
	}

	public void setNoneventBoosterPackProtoSerializer(
		NoneventBoosterPackProtoSerializer noneventBoosterPackProtoSerializer )
	{
		this.noneventBoosterPackProtoSerializer = noneventBoosterPackProtoSerializer;
	}

	public NoneventStructureProtoSerializer getNoneventStructureProtoSerializer()
	{
		return noneventStructureProtoSerializer;
	}

	public void setNoneventStructureProtoSerializer(
		NoneventStructureProtoSerializer noneventStructureProtoSerializer )
	{
		this.noneventStructureProtoSerializer = noneventStructureProtoSerializer;
	}

	public StructureResourceGeneratorRepository getStructureResourceGeneratorRepository()
	{
		return structureResourceGeneratorRepository;
	}

	public void setStructureResourceGeneratorRepository(
		StructureResourceGeneratorRepository structureResourceGeneratorRepository )
	{
		this.structureResourceGeneratorRepository = structureResourceGeneratorRepository;
	}

	public StructureResourceStorageRepository getStructureResourceStorageRepository()
	{
		return structureResourceStorageRepository;
	}

	public void setStructureResourceStorageRepository(
		StructureResourceStorageRepository structureResourceStorageRepository )
	{
		this.structureResourceStorageRepository = structureResourceStorageRepository;
	}

	public StructureHospitalRepository getStructureHospitalRepository()
	{
		return structureHospitalRepository;
	}

	public void setStructureHospitalRepository(
		StructureHospitalRepository structureHospitalRepository )
	{
		this.structureHospitalRepository = structureHospitalRepository;
	}

	public StructureResidenceRepository getStructureResidenceRepository()
	{
		return structureResidenceRepository;
	}

	public void setStructureResidenceRepository(
		StructureResidenceRepository structureResidenceRepository )
	{
		this.structureResidenceRepository = structureResidenceRepository;
	}

	public StructureTownHallRepository getStructureTownHallRepository()
	{
		return structureTownHallRepository;
	}

	public void setStructureTownHallRepository(
		StructureTownHallRepository structureTownHallRepository )
	{
		this.structureTownHallRepository = structureTownHallRepository;
	}

	public StructureLabRepository getStructureLabRepository()
	{
		return structureLabRepository;
	}

	public void setStructureLabRepository( StructureLabRepository structureLabRepository )
	{
		this.structureLabRepository = structureLabRepository;
	}

	public StructureMiniJobRepository getStructureMiniJobRepository()
	{
		return structureMiniJobRepository;
	}

	public void setStructureMiniJobRepository( StructureMiniJobRepository structureMiniJobRepository )
	{
		this.structureMiniJobRepository = structureMiniJobRepository;
	}

	public EventPersistentRepository getEventPersistentRepository()
	{
		return eventPersistentRepository;
	}

	public void setEventPersistentRepository( EventPersistentRepository eventPersistentRepository )
	{
		this.eventPersistentRepository = eventPersistentRepository;
	}

	public NoneventEventPersistentProtoSerializer getEventPersistentProtoSerializer()
	{
		return eventPersistentProtoSerializer;
	}

	public void setEventPersistentProtoSerializer(
		NoneventEventPersistentProtoSerializer eventPersistentProtoSerializer )
	{
		this.eventPersistentProtoSerializer = eventPersistentProtoSerializer;
	}

	public MonsterBattleDialogueRepository getMonsterBattleDialogueRepository()
	{
		return monsterBattleDialogueRepository;
	}

	public void setMonsterBattleDialogueRepository(
		MonsterBattleDialogueRepository monsterBattleDialogueRepository )
	{
		this.monsterBattleDialogueRepository = monsterBattleDialogueRepository;
	}

	public ItemRepository getItemRepository()
	{
		return itemRepository;
	}

	public void setItemRepository( ItemRepository itemRepository )
	{
		this.itemRepository = itemRepository;
	}

	public ObstacleRepository getObstacleRepository()
	{
		return obstacleRepository;
	}

	public void setObstacleRepository( ObstacleRepository obstacleRepository )
	{
		this.obstacleRepository = obstacleRepository;
	}

	public PvpLeagueRepository getPvpLeagueRepository()
	{
		return pvpLeagueRepository;
	}

	public void setPvpLeagueRepository( PvpLeagueRepository pvpLeagueRepository )
	{
		this.pvpLeagueRepository = pvpLeagueRepository;
	}

	public NoneventPvpProtoSerializer getNoneventPvpProtoSerializer()
	{
		return noneventPvpProtoSerializer;
	}

	public void setNoneventPvpProtoSerializer( NoneventPvpProtoSerializer noneventPvpProtoSerializer )
	{
		this.noneventPvpProtoSerializer = noneventPvpProtoSerializer;
	}

	public AchievementRepository getAchievementRepository()
	{
		return achievementRepository;
	}

	public void setAchievementRepository( AchievementRepository achievementRepository )
	{
		this.achievementRepository = achievementRepository;
	}

	public NoneventAchievementProtoSerializer getNoneventAchievementProtoSerializer()
	{
		return noneventAchievementProtoSerializer;
	}

	public void setNoneventAchievementProtoSerializer(
		NoneventAchievementProtoSerializer noneventAchievementProtoSerializer )
	{
		this.noneventAchievementProtoSerializer = noneventAchievementProtoSerializer;
	}

}
