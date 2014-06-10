package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.TimeUtils;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto.UserCreateStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.UserCreateRequestEvent;
import com.lvl6.mobsters.events.response.UserCreateResponseEvent;
import com.lvl6.mobsters.info.ConnectedPlayer;
import com.lvl6.mobsters.info.Monster;
import com.lvl6.mobsters.info.MonsterLevelInfo;
import com.lvl6.mobsters.info.PvpLeague;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.CoordinateProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TutorialStructProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class UserCreateController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(UserCreateController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected LeaderBoardUtil leaderboard;

	@Autowired
	protected InsertUtil insertUtils;

	@Autowired
	protected TimeUtils timeUtils;

	@Autowired
	protected StructureStuffUtil structureStuffUtil;

	public UserCreateController()
	{
		numAllocatedThreads = 3;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new UserCreateRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_USER_CREATE_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final UserCreateRequestProto reqProto =
		    ((UserCreateRequestEvent) event).getUserCreateRequestProto();
		final String udid = reqProto.getUdid();
		final String name = reqProto.getName();
		final String deviceToken = reqProto.getDeviceToken();
		final String fbId = reqProto.getFacebookId();
		final Timestamp createTime = new Timestamp((new Date()).getTime());
		final List<TutorialStructProto> structsJustBuilt = reqProto.getStructsJustBuiltList();
		final String facebookId = reqProto.getFacebookId();

		// in case user tries hacking, don't let the amount go over tutorial
		// default values
		final int cash = Math.min(reqProto.getCash(), ControllerConstants.TUTORIAL__INIT_CASH);
		final int oil = Math.min(reqProto.getOil(), ControllerConstants.TUTORIAL__INIT_OIL);
		final int gems = Math.min(reqProto.getGems(), ControllerConstants.TUTORIAL__INIT_GEMS);

		final UserCreateResponseProto.Builder resBuilder = UserCreateResponseProto.newBuilder();
		resBuilder.setStatus(UserCreateStatus.FAIL_OTHER);

		boolean gotLock = true;
		if ((null != fbId)
		    && !fbId.isEmpty()) {
			// this is to prevent one user on two devices creating account with
			// one fbId
			gotLock = getLocker().lockFbId(fbId);
		}
		try {
			final boolean legitUserCreate =
			    checkLegitUserCreate(gotLock, resBuilder, udid, facebookId, name);

			String userUuid = ControllerConstants.NOT_SET;

			if (legitUserCreate) {
				// String newReferCode = grabNewReferCode();
				userUuid =
				    writeChangeToDb(resBuilder, name, udid, cash, oil, gems, deviceToken,
				        createTime, facebookId);
			}

			final UserCreateResponseProto resProto = resBuilder.build();
			final UserCreateResponseEvent resEvent = new UserCreateResponseEvent(udid);
			resEvent.setTag(event.getTag());
			resEvent.setUserCreateResponseProto(resProto);
			LOG.info("Writing event: "
			    + resEvent);
			server.writePreDBEvent(resEvent, udid);

			if (userUuid > 0) {
				// recording that player is online I guess
				final ConnectedPlayer player = server.getPlayerByUdId(udid);
				player.setPlayerId(userUuid);
				server.getPlayersByPlayerId()
				    .put(userUuid, player);
				server.getPlayersPreDatabaseByUDID()
				    .remove(udid);
			}

			if (legitUserCreate
			    && (userUuid > 0)) {
				/* server.lockPlayer(userUuid, this.getClass().getSimpleName()); */
				try {
					// TAKE INTO ACCOUNT THE PROPERTIES SENT IN BY CLIENT
					LOG.info("writing user structs");
					writeStructs(userUuid, createTime, structsJustBuilt);
					writeObstacles(userUuid);
					LOG.info("writing tasks");
					writeTaskCompleted(userUuid, createTime);
					writeMonsters(userUuid, createTime, facebookId);
					writePvpStuff(userUuid, createTime);
					// LeaderBoardUtil leaderboard =
					// AppContext.getApplicationContext().getBean(LeaderBoardUtil.class);
					// leaderboard.updateLeaderboardForUser(user);

					// CURRENCY CHANGE HISTORY
					writeToUserCurrencyHistory(userUuid, cash, oil, gems, createTime);

				} catch (final Exception e) {
					LOG.error("exception in UserCreateController processEvent", e);
				} /*
				 * finally { server.unlockPlayer(userUuid,
				 * this.getClass().getSimpleName()); }
				 */
			}
		} catch (final Exception e) {
			LOG.error("exception in UserCreateController processEvent", e);
			try {
				resBuilder.setStatus(UserCreateStatus.FAIL_OTHER);
				final UserCreateResponseEvent resEvent = new UserCreateResponseEvent(udid);
				resEvent.setTag(event.getTag());
				resEvent.setUserCreateResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in UserCreateController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in UserCreateController processEvent", e);
			}
		} finally {
			if (gotLock) {
				getLocker().unlockFbId(fbId);
			}
		}
	}

	private boolean checkLegitUserCreate( final boolean gotLock, final Builder resBuilder,
	    final String udid, final String facebookId, final String name )
	{
		final List<User> users = RetrieveUtils.userRetrieveUtils()
		    .getUserByUDIDorFbId(udid, facebookId);

		if (!gotLock) {
			LOG.error("did not get fb lock. fbId="
			    + facebookId
			    + ", udid="
			    + udid
			    + ", name="
			    + name);
			return false;
		}

		if ((null == users)
		    || users.isEmpty()) {
			return true;
		}

		if ((null != facebookId)
		    && !facebookId.isEmpty()
		    && facebookIdExists(facebookId, users)) {
			// check if facebookId is tied to an account
			resBuilder.setStatus(UserCreateStatus.FAIL_USER_WITH_FACEBOOK_ID_EXISTS);
			LOG.error("user with facebookId "
			    + facebookId
			    + " already exists. users="
			    + users);
			return false;
		}
		if ((null != udid)
		    && !udid.isEmpty()
		    && udidExists(udid, users)) {
			// udid shouldn't be empty
			// check if udid is tied to an account
			resBuilder.setStatus(UserCreateStatus.FAIL_USER_WITH_UDID_ALREADY_EXISTS);
			LOG.error("user with udid "
			    + udid
			    + " already exists");
			return false;
		}

		return true;
	}

	private boolean facebookIdExists( final String facebookId, final List<User> users )
	{
		for (final User u : users) {
			final String userFacebookId = u.getFacebookId();

			if ((null != userFacebookId)
			    && userFacebookId.equals(facebookId)) {
				return true;
			}
		}
		return false;
	}

	private boolean udidExists( final String udid, final List<User> users )
	{
		for (final User u : users) {
			final String userUdid = u.getUdid();

			if ((null != userUdid)
			    && userUdid.equals(udid)) {
				return true;
			}
		}
		return false;
	}

	private int writeChangeToDb( final Builder resBuilder, final String name,
	    final String udid, final int cash, final int oil, final int gems,
	    final String deviceToken, final Timestamp createTime, final String facebookId )
	{
		// TODO: FIX THESE NUMBERS
		final int lvl = ControllerConstants.USER_CREATE__START_LEVEL;
		final int playerExp = 10;
		final int avatarMonsterId = ControllerConstants.TUTORIAL__STARTING_MONSTER_ID;

		final String userUuid =
		    insertUtils.insertUser(name, udid, lvl, playerExp, cash, oil, gems, false,
		        deviceToken, createTime, facebookId, avatarMonsterId);

		if (userUuid > 0) {
			/* server.lockPlayer(userUuid, this.getClass().getSimpleName()); *//*
																				 * try
																				 * {
																				 * user
																				 * =
																				 * RetrieveUtils
																				 * .
																				 * userRetrieveUtils
																				 * (
																				 * )
																				 * .
																				 * getUserById
																				 * (
																				 * userUuid
																				 * )
																				 * ;
																				 * FullUserProto
																				 * userProto
																				 * =
																				 * CreateInfoProtoUtils
																				 * .
																				 * createFullUserProtoFromUser
																				 * (
																				 * user
																				 * )
																				 * ;
																				 * resBuilder
																				 * .
																				 * setSender
																				 * (
																				 * userProto
																				 * )
																				 * ;
																				 * }
																				 * catch
																				 * (
																				 * Exception
																				 * e
																				 * )
																				 * {
																				 * LOG
																				 * .
																				 * error
																				 * (
																				 * "exception in UserCreateController processEvent"
																				 * ,
																				 * e
																				 * )
																				 * ;
																				 * }
																				 *//*
																					 * finally
																					 * {
																					 * server
																					 * .
																					 * unlockPlayer
																					 * (
																					 * userUuid
																					 * ,
																					 * this
																					 * .
																					 * getClass
																					 * (
																					 * )
																					 * .
																					 * getSimpleName
																					 * (
																					 * )
																					 * )
																					 * ;
																					 * }
																					 */
		} else {
			resBuilder.setStatus(UserCreateStatus.FAIL_OTHER);
			LOG.error("problem with trying to create user. udid="
			    + udid
			    + ", name="
			    + name
			    + ", deviceToken="
			    + deviceToken
			    + ", playerExp="
			    + playerExp
			    + ", cash="
			    + cash
			    + ", oil="
			    + oil
			    + ", gems="
			    + gems);
		}

		LOG.info("created new userUuid="
		    + userUuid);
		resBuilder.setStatus(UserCreateStatus.SUCCESS);
		return userUuid;
	}

	// THE VALUES AND STRUCTS TO GIVE THE USER
	private void writeStructs( final String userUuid, final Timestamp purchaseTime,
	    final List<TutorialStructProto> structsJustBuilt )
	{
		final Date purchaseDate = new Date(purchaseTime.getTime());
		final Date purchaseDateOneWeekAgo = getTimeUtils().createDateAddDays(purchaseDate, -7);
		final Timestamp purchaseTimeOneWeekAgo =
		    new Timestamp(purchaseDateOneWeekAgo.getTime());

		final int[] buildingUuids = ControllerConstants.TUTORIAL__EXISTING_BUILDING_IDS;
		final float[] xPositions = ControllerConstants.TUTORIAL__EXISTING_BUILDING_X_POS;
		final float[] yPositions = ControllerConstants.TUTORIAL__EXISTING_BUILDING_Y_POS;
		LOG.info("giving user buildings");
		final int quantity = buildingUuids.length;
		final List<Integer> userIdList =
		    new ArrayList<Integer>(Collections.nCopies(quantity, userUuid));
		final List<Integer> structIdList = new ArrayList<Integer>();
		final List<Float> xCoordList = new ArrayList<Float>();
		final List<Float> yCoordList = new ArrayList<Float>();
		final List<Timestamp> purchaseTimeList =
		    new ArrayList<Timestamp>(Collections.nCopies(quantity, purchaseTime));
		final List<Timestamp> retrievedTimeList =
		    new ArrayList<Timestamp>(Collections.nCopies(quantity, purchaseTimeOneWeekAgo));
		final List<Boolean> isComplete =
		    new ArrayList<Boolean>(Collections.nCopies(quantity, true));

		for (int i = 0; i < buildingUuids.length; i++) {
			final int structId = buildingUuids[i];
			final float xCoord = xPositions[i];
			final float yCoord = yPositions[i];

			structIdList.add(structId);
			xCoordList.add(xCoord);
			yCoordList.add(yCoord);
		}

		// giving user the buildings he just created
		for (final TutorialStructProto tsp : structsJustBuilt) {
			final int structId = tsp.getStructId();
			final CoordinateProto cp = tsp.getCoordinate();
			final float xCoord = cp.getX();
			final float yCoord = cp.getY();

			userIdList.add(userUuid);
			structIdList.add(structId);
			xCoordList.add(xCoord);
			yCoordList.add(yCoord);
			purchaseTimeList.add(purchaseTime);
			retrievedTimeList.add(purchaseTime);
			isComplete.add(true);
		}

		LOG.info("userIdList="
		    + userIdList);
		LOG.info("structIdList="
		    + structIdList);
		LOG.info("xCoordList="
		    + xCoordList);
		LOG.info("yCoordList="
		    + yCoordList);
		LOG.info("purchaseTimeList="
		    + purchaseTimeList);
		LOG.info("retrievedTimeList="
		    + retrievedTimeList);
		LOG.info("isComplete="
		    + isComplete);

		final int numInserted =
		    InsertUtils.get()
		        .insertUserStructs(userIdList, structIdList, xCoordList, yCoordList,
		            purchaseTimeList, retrievedTimeList, isComplete);
		LOG.info("num buildings given to user: "
		    + numInserted);
	}

	private void writeObstacles( final String userUuid )
	{
		final List<ObstacleForUser> ofuList =
		    getStructureStuffUtil().createTutorialObstacleForUser(userUuid);
		LOG.info("inserting tutorial obstacles into obstacle_for_user");
		final List<Integer> ofuIdList = InsertUtils.get()
		    .insertIntoObstaclesForUserGetUuids(userUuid, ofuList);

		LOG.info("tutorial ofuIdList="
		    + ofuIdList);
	}

	private void writeTaskCompleted( final String userUuid, final Timestamp createTime )
	{
		final List<Integer> taskIdList = new ArrayList<Integer>();

		final int cityId = ControllerConstants.TUTORIAL__CITY_ONE_ID;
		final int assetIdOne =
		    ControllerConstants.TUTORIAL__CITY_ONE_ASSET_NUM_FOR_FIRST_DUNGEON;
		final int taskIdOne = TaskRetrieveUtils.getTaskIdForCityElement(cityId, assetIdOne);
		taskIdList.add(taskIdOne);

		final int assetIdTwo =
		    ControllerConstants.TUTORIAL__CITY_ONE_ASSET_NUM_FOR_SECOND_DUNGEON;
		final int taskIdTwo = TaskRetrieveUtils.getTaskIdForCityElement(cityId, assetIdTwo);
		taskIdList.add(taskIdTwo);

		LOG.info("taskIdList: "
		    + taskIdList);

		final int size = taskIdList.size();
		final List<Integer> userIdList = Collections.nCopies(size, userUuid);
		final List<Timestamp> createTimeList = Collections.nCopies(size, createTime);

		final int numInserted = InsertUtils.get()
		    .insertIntoTaskForUserCompleted(userIdList, taskIdList, createTimeList);
		LOG.info("num tasks inserted:"
		    + numInserted
		    + ", should be "
		    + size);
	}

	private void writeMonsters( final String userUuid, final Timestamp createTime,
	    final String fbId )
	{
		final String sourceOfPieces = ControllerConstants.MFUSOP__USER_CREATE;
		final Date createDate = new Date(createTime.getTime());
		final Date combineStartDate = getTimeUtils().createDateAddDays(createDate, -7);

		final List<Integer> monsterUuids = new ArrayList<Integer>();
		monsterUuids.add(ControllerConstants.TUTORIAL__STARTING_MONSTER_ID);

		if ((null != fbId)
		    && !fbId.isEmpty()) {
			LOG.info("awarding facebook zucker mucker burger.");
			monsterUuids.add(ControllerConstants.TUTORIAL__MARK_Z_MONSTER_ID);
		}

		final List<MonsterForUser> userMonsters = new ArrayList<MonsterForUser>();
		for (int i = 0; i < monsterUuids.size(); i++) {
			final int monsterId = monsterUuids.get(i);
			final int teamSlotNum = i + 1;

			final Monster monzter = MonsterRetrieveUtils.getMonsterForMonsterId(monsterId);
			final Map<Integer, MonsterLevelInfo> info =
			    MonsterLevelInfoRetrieveUtils.getMonsterLevelInfoForMonsterId(monsterId);

			final List<Integer> lvls = new ArrayList<Integer>(info.keySet());
			Collections.sort(lvls);
			final int firstOne = lvls.get(0);
			final MonsterLevelInfo mli = info.get(firstOne);

			final MonsterForUser mfu =
			    new MonsterForUser(0, userUuid, monsterId,
			        0,// mli.getCurLvlRequiredExp(),
			        mli.getLevel(), mli.getHp(), monzter.getNumPuzzlePieces(), true,
			        combineStartDate, teamSlotNum, sourceOfPieces);
			userMonsters.add(mfu);
		}
		final List<Long> ids =
		    InsertUtils.get()
		        .insertIntoMonsterForUserReturnUuids(userUuid, userMonsters, sourceOfPieces,
		            combineStartDate);

		LOG.info("monsters inserted for userUuid="
		    + userUuid
		    + ", ids="
		    + ids);
	}

	private void writePvpStuff( final String userUuid, final Timestamp createTime )
	{
		final int elo = ControllerConstants.PVP__INITIAL_ELO;
		int pvpLeagueId = ControllerConstants.PVP__INITIAL_LEAGUE_ID;
		final List<PvpLeague> pvpLeagueList = PvpLeagueRetrieveUtils.getLeaguesForElo(elo);
		if (pvpLeagueList.size() > 1) {
			LOG.error("there are multiple leagues for initial elo: "
			    + elo
			    + "\t leagues="
			    + pvpLeagueList
			    + "\t choosing first one.");
		} else if (pvpLeagueList.isEmpty()) {
			LOG.error("no pvp league id for elo: "
			    + elo);
		} else { // size is one
			pvpLeagueId = pvpLeagueList.get(0)
			    .getId();
		}

		final int rank = PvpLeagueRetrieveUtils.getRankForElo(elo, pvpLeagueId);

		final Date createDate = new Date(createTime.getTime());
		final Date shieldEndDate =
		    getTimeUtils().createDateAddDays(createDate,
		        ControllerConstants.PVP__SHIELD_DURATION_DAYS);
		final Timestamp shieldEndTime = new Timestamp(shieldEndDate.getTime());

		final int numInsertedIntoPvp =
		    InsertUtils.get()
		        .insertPvpLeagueForUser(userUuid, pvpLeagueId, rank, elo, shieldEndTime,
		            shieldEndTime);
		LOG.info("numInsertedIntoPvp="
		    + numInsertedIntoPvp);

		// maybe should populate hazelcast with the pvp user now...
		/*
		 * PvpUser plfu = new PvpUser(); plfu.setPvpLeagueId(pvpLeagueId);
		 * plfu.setRank(rank); plfu.setUserUuid(Integer.toString(userUuid));
		 * plfu.setElo(elo); plfu.setShieldEndTime(shieldEndDate);
		 * plfu.setInBattleEndTime(shieldEndDate); plfu.setAttacksWon(0);
		 * plfu.setDefensesWon(0); plfu.setAttacksLost(0);
		 * plfu.setDefensesLost(0);
		 */
	}

	// private String grabNewReferCode() {
	// String newReferCode =
	// AvailableReferralCodeRetrieveUtils.getAvailableReferralCode();
	// if (newReferCode != null && newReferCode.length() > 0) {
	// while (!DeleteUtils.get().deleteAvailableReferralCode(newReferCode)) {
	// newReferCode =
	// AvailableReferralCodeRetrieveUtils.getAvailableReferralCode();
	// }
	// } else {
	// LOG.error("no refer codes left");
	// }
	// return newReferCode;
	// }

	private void rewardReferrer( final User referrer, final User user )
	{
		if (!referrer.isFake()) {
			getLocker().lockPlayer(referrer.getId(), this.getClass()
			    .getSimpleName());
			try {
				final int previousSilver = referrer.getCash();

				final int coinsGivenToReferrer =
				    MiscMethods.calculateCoinsGivenToReferrer(referrer);
				if (!referrer.updateRelativeCoinsNumreferrals(coinsGivenToReferrer, 1)) {
					LOG.error("problem with rewarding the referrer "
					    + referrer
					    + " with this many coins: "
					    + coinsGivenToReferrer);
				} else {
					if (!insertUtils.insertReferral(referrer.getId(), user.getId(),
					    coinsGivenToReferrer)) {
						LOG.error("problem with inserting referral into db. referrer is "
						    + referrer.getId()
						    + ", user="
						    + user.getId()
						    + ", coins given to referrer="
						    + coinsGivenToReferrer);
					}
					final ReferralCodeUsedResponseEvent resEvent =
					    new ReferralCodeUsedResponseEvent(referrer.getId());
					final ReferralCodeUsedResponseProto resProto =
					    ReferralCodeUsedResponseProto.newBuilder()
					        .setSender(
					            CreateInfoProtoUtils.createMinimumUserProtoFromUser(referrer))
					        .setReferredPlayer(
					            CreateInfoProtoUtils.createMinimumUserProtoFromUser(user))
					        .setCoinsGivenToReferrer(coinsGivenToReferrer)
					        .build();
					resEvent.setReferralCodeUsedResponseProto(resProto);
					server.writeAPNSNotificationOrEvent(resEvent);

					writeToUserCurrencyHistoryTwo(referrer, coinsGivenToReferrer,
					    previousSilver);
				}
			} catch (final Exception e) {
				LOG.error("exception in UserCreateController processEvent", e);
			} finally {
				getLocker().unlockPlayer(referrer.getId(), this.getClass()
				    .getSimpleName());
			}
		}
	}

	private void writeToUserCurrencyHistory( final String userUuid, final int cash,
	    final int oil, final int gems, final Timestamp createTime )
	{
		final String reasonForChange = ControllerConstants.UCHRFC__USER_CREATED;

		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();
		final String gemsStr = MiscMethods.gems;
		final String cashStr = MiscMethods.cash;
		final String oilStr = MiscMethods.oil;

		previousCurrency.put(gemsStr, 0);
		previousCurrency.put(cashStr, 0);
		previousCurrency.put(oilStr, 0);
		currentCurrency.put(gemsStr, gems);
		currentCurrency.put(cashStr, cash);
		currentCurrency.put(oilStr, oil);
		reasonsForChanges.put(gemsStr, reasonForChange);
		reasonsForChanges.put(cashStr, reasonForChange);
		reasonsForChanges.put(oilStr, reasonForChange);
		detailsMap.put(gemsStr, "");
		detailsMap.put(cashStr, "");
		detailsMap.put(oilStr, "");

		MiscMethods.writeToUserCurrencyOneUser(userUuid, createTime, currentCurrency,
		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);

	}

	// TODO: FIX THIS
	public void writeToUserCurrencyHistoryTwo( final User aUser, final int coinChange,
	    final int previousSilver )
	{
		// Timestamp date = new Timestamp((new Date()).getTime());
		//
		// Map<String, Integer> goldSilverChange = new HashMap<String,
		// Integer>();
		// Map<String, Integer> previousGoldSilver = new HashMap<String,
		// Integer>();
		// Map<String, String> reasonsForChanges = new HashMap<String,
		// String>();
		// String silver = MiscMethods.cash;
		// String reasonForChange =
		// ControllerConstants.UCHRFC__USER_CREATE_REFERRED_A_USER;
		//
		// goldSilverChange.put(silver, coinChange);
		// previousGoldSilver.put(silver, previousSilver);
		// reasonsForChanges.put(silver, reasonForChange);
		//
		// MiscMethods.writeToUserCurrencyOneUserGemsAndOrCash(aUser, date,
		// goldSilverChange,
		// previousGoldSilver, reasonsForChanges);
	}

	public LeaderBoardUtil getLeaderboard()
	{
		return leaderboard;
	}

	public void setLeaderboard( final LeaderBoardUtil leaderboard )
	{
		this.leaderboard = leaderboard;
	}

	public void setInsertUtils( final InsertUtil insertUtils )
	{
		this.insertUtils = insertUtils;
	}

	public TimeUtils getTimeUtils()
	{
		return timeUtils;
	}

	public void setTimeUtils( final TimeUtils timeUtils )
	{
		this.timeUtils = timeUtils;
	}

	public StructureStuffUtil getStructureStuffUtil()
	{
		return structureStuffUtil;
	}

	public void setStructureStuffUtil( final StructureStuffUtil structureStuffUtil )
	{
		this.structureStuffUtil = structureStuffUtil;
	}
}
