//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.common.utils.TimeUtils;
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan;
//import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser;
//import com.lvl6.mobsters.dynamo.ClanForUser;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidRequestProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto.BeginClanRaidStatus;
//import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto.Builder;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.BeginClanRaidRequestEvent;
//import com.lvl6.mobsters.events.response.BeginClanRaidResponseEvent;
//import com.lvl6.mobsters.info.ClanEventPersistent;
//import com.lvl6.mobsters.info.ClanRaidStage;
//import com.lvl6.mobsters.info.ClanRaidStageMonster;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventClanInfoProto;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventUserInfoProto;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class BeginClanRaidController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(BeginClanRaidController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Autowired
//	protected TimeUtils timeUtils;
//
//	@Autowired
//	protected ClanEventUtil clanEventUtil;
//
//	public BeginClanRaidController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new BeginClanRaidRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_BEGIN_CLAN_RAID_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final BeginClanRaidRequestProto reqProto =
//		    ((BeginClanRaidRequestEvent) event).getBeginClanRaidRequestProto();
//
//		LOG.info("reqProto="
//		    + reqProto);
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final MinimumClanProto mcp = senderProto.getClan();
//		int clanId = mcp.getClanId();
//		final int clanEventPersistentId = reqProto.getClanEventUuid();
//
//		final Date curDate = new Date(reqProto.getCurTime());
//		final Timestamp curTime = new Timestamp(curDate.getTime());
//		final int clanRaidId = reqProto.getRaidUuid(); // not really needed
//
//		final boolean setMonsterTeamForRaid = reqProto.getSetMonsterTeamForRaid();
//		final List<FullUserMonsterProto> userMonsters = reqProto.getUserMonstersList();
//		final List<Long> userMonsterUuids = MonsterStuffUtils.getUserMonsterUuids(userMonsters);
//		final boolean isFirstStage = reqProto.getIsFirstStage();
//
//		final BeginClanRaidResponseProto.Builder resBuilder =
//		    BeginClanRaidResponseProto.newBuilder();
//		resBuilder.setStatus(BeginClanRaidStatus.FAIL_OTHER);
//		resBuilder.setSender(senderProto);
//
//		// OUTLINE:
//		// get the clan lock; get the clan raid object for the clan;
//		// If doesn't exist, create it. If does exist, check to see if the raids
//		// are different.
//		// If different, replace it with a new one. Else, do nothing.
//
//		// ONLY GET CLAN LOCK IF TRYING TO BEGIN A RAID
//		boolean lockedClan = false;
//		if ((null != mcp)
//		    && mcp.hasClanId()) {
//			clanId = mcp.getClanId();
//			if ((0 != clanId)
//			    && !setMonsterTeamForRaid) {
//				lockedClan = getLocker().lockClan(clanId);
//				LOG.info("locking clanId="
//				    + clanId);
//			}
//		}
//		try {
//			// User user =
//			// RetrieveUtils.userRetrieveUtils().getUserById(userUuid);
//			final ClanForUser uc = RetrieveUtils.userClanRetrieveUtils()
//			    .getSpecificUserClan(userUuid, clanId);
//			final boolean legitRequest =
//			    checkLegitRequest(resBuilder, lockedClan, senderProto, userUuid, clanId, uc,
//			        clanEventPersistentId, clanRaidId, curDate, curTime, setMonsterTeamForRaid,
//			        userMonsterUuids, isFirstStage);
//
//			final List<ClanEventPersistentForClan> clanInfoList =
//			    new ArrayList<ClanEventPersistentForClan>();
//			boolean success = false;
//			if (legitRequest) {
//				LOG.info("recording in the db that the clan began a clan raid or setting monsters."
//				    + " or starting a stage. isFirstStage="
//				    + isFirstStage);
//				success =
//				    writeChangesToDB(userUuid, clanId, clanEventPersistentId, clanRaidId,
//				        curTime, setMonsterTeamForRaid, userMonsterUuids, isFirstStage,
//				        clanInfoList);
//			}
//
//			if (success) {
//				if (!setMonsterTeamForRaid) {
//					final ClanEventPersistentForClan cepfc = clanInfoList.get(0);
//					final PersistentClanEventClanInfoProto eventDetails =
//					    CreateInfoProtoUtils.createPersistentClanEventClanInfoProto(cepfc);
//					resBuilder.setEventDetails(eventDetails);
//				}
//				if (setMonsterTeamForRaid) {
//					setClanAndUserDetails(resBuilder, userUuid, clanId, clanRaidId,
//					    userMonsterUuids, userMonsters);
//				}
//				resBuilder.setStatus(BeginClanRaidStatus.SUCCESS);
//				LOG.info("BEGIN CLAN RAID EVENT SUCCESS!!!!!!!");
//			}
//
//			final BeginClanRaidResponseEvent resEvent =
//			    new BeginClanRaidResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setBeginClanRaidResponseProto(resBuilder.build());
//			LOG.info("resBuilder="
//			    + resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in BeginClanRaidController.processRequestEvent", e);
//			}
//
//			if (success) {
//				// only write to the user if the request was valid
//				server.writeClanEvent(resEvent, clanId);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in BeginClanRaid processEvent", e);
//			try {
//				resBuilder.setStatus(BeginClanRaidStatus.FAIL_OTHER);
//				final BeginClanRaidResponseEvent resEvent =
//				    new BeginClanRaidResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setBeginClanRaidResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error("fatal exception in BeginClanRaidController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in BeginClanRaid processEvent", e);
//			}
//		} finally {
//
//			// ONLY RELEASE CLAN LOCK IF TRYING TO BEGIN A RAID
//			if ((null != mcp)
//			    && mcp.hasClanId()) {
//				if ((0 != clanId)
//				    && !setMonsterTeamForRaid
//				    && lockedClan) {
//					getLocker().unlockClan(clanId);
//					LOG.info("unlocked clanId="
//					    + clanId);
//				}
//			}
//
//		}
//	}
//
//	private boolean checkLegitRequest( final Builder resBuilder, final boolean lockedClan,
//	    final MinimumUserProto mupfc, final String userUuid, final int clanId,
//	    final ClanForUser uc, final int clanEventId, final int clanRaidId, final Date curDate,
//	    final Timestamp curTime, final boolean setMonsterTeamForRaid,
//	    final List<Long> userMonsterUuids, final boolean isFirstStage )
//	{
//
//		if (!lockedClan) {
//			LOG.error("couldn't obtain clan lock");
//			return false;
//		}
//		if ((0 == clanId)
//		    || (0 == clanRaidId)
//		    || (null == uc)) {
//			LOG.error("not in clan. user is "
//			    + mupfc
//			    + "\t or clanRaidId invalid id="
//			    + clanRaidId
//			    + "\t or no user clan exists. uc="
//			    + uc);
//			return false;
//		}
//
//		// user can only start raid if an event exists for it, check if event
//		// exists,
//		// clan raid events CAN overlap
//		final Map<Integer, ClanEventPersistent> clanEventIdToEvent =
//		    ClanEventPersistentRetrieveUtils.getActiveClanEventUuidsToEvents(curDate, timeUtils);
//		if (!clanEventIdToEvent.containsKey(clanEventId)) {
//			resBuilder.setStatus(BeginClanRaidStatus.FAIL_NO_ACTIVE_CLAN_RAID);
//			LOG.error("no active clan event with id="
//			    + clanEventId
//			    + "\t user="
//			    + mupfc);
//			return false;
//		}
//
//		// give the event id back to the caller
//		final ClanEventPersistent cep = clanEventIdToEvent.get(clanEventId);
//		final int eventRaidId = cep.getClanRaidId();
//		if (clanRaidId != eventRaidId) {
//			resBuilder.setStatus(BeginClanRaidStatus.FAIL_NO_ACTIVE_CLAN_RAID);
//			LOG.error("no active clan event with raidId="
//			    + clanRaidId
//			    + "\t event="
//			    + cep
//			    + "\t user="
//			    + mupfc);
//			return false;
//		}
//
//		// only check if user can start raid if he is not setting his monsters
//		// check if the clan already has existing raid information
//		if (!setMonsterTeamForRaid) {
//			final Set<Integer> authorizedUsers = getAuthorizedUsers(clanId);
//			if (!authorizedUsers.contains(userUuid)) {
//				resBuilder.setStatus(BeginClanRaidStatus.FAIL_NOT_AUTHORIZED);
//				LOG.error("user can't start raid. user="
//				    + mupfc);
//				return false;
//			}
//			// user is authorized to start clan raid
//
//			if (!validClanInfo(resBuilder, clanId, clanEventId, clanRaidId, curDate, curTime,
//			    isFirstStage)) {
//				return false;
//			}
//		}
//
//		// Don't think any checks need to be made
//		// (user needs to equip user monsters before beginning raid; checks are
//		// done there)
//		if (setMonsterTeamForRaid
//		    && ((null == userMonsterUuids) || userMonsterUuids.isEmpty())) {
//			resBuilder.setStatus(BeginClanRaidStatus.FAIL_NO_MONSTERS_SENT);
//			LOG.error("client did not send any monster ids to set for clan raid.");
//			return false;
//		}
//
//		return true;
//	}
//
//	// get all the members in a clan
//	private Set<Integer> getAuthorizedUsers( final int clanId )
//	{
//		final Set<Integer> authorizedUsers = new HashSet<Integer>();
//		final List<String> statuses = new ArrayList<String>();
//		statuses.add(UserClanStatus.LEADER.name());
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
//		statuses.add(UserClanStatus.CAPTAIN.name());
//		final List<Integer> userUuids = RetrieveUtils.userClanRetrieveUtils()
//		    .getUserUuidsWithStatuses(clanId, statuses);
//
//		if ((null != userUuids)
//		    && !userUuids.isEmpty()) {
//			authorizedUsers.addAll(userUuids);
//		}
//
//		return authorizedUsers;
//	}
//
//	private boolean validClanInfo( final Builder resBuilder, final int clanId,
//	    final int clanEventId, final int clanRaidId, final Date curDate, final Timestamp now,
//	    final boolean isFirstStage )
//	{
//		// check if clan already started the event
//		final ClanEventPersistentForClan raidStartedByClan =
//		    ClanEventPersistentForClanRetrieveUtils.getPersistentEventForClanId(clanId);
//
//		if ((null == raidStartedByClan)
//		    && isFirstStage) {
//			return true;
//		} else if ((null == raidStartedByClan)
//		    && !isFirstStage) {
//			LOG.error("clan has not started a raid/event (nothing in clan_event_persistent_for_clan)"
//			    + " but client claims clan started one. clanId="
//			    + clanId
//			    + "\t clanEventId="
//			    + clanEventId
//			    + "\t clanRaidId="
//			    + clanRaidId
//			    + "\t isFirstStage="
//			    + isFirstStage);
//			return false;
//		}
//
//		// clan raid/event entry exists for clan
//		final int ceId = raidStartedByClan.getClanEventPersistentId();
//		final int crId = raidStartedByClan.getCrId();
//
//		if (((ceId != clanEventId) || (crId != clanRaidId))
//		    || ((null != raidStartedByClan) && isFirstStage)) {
//			LOG.warn("possibly encountered clan raid data that should have been pushed to"
//			    + " history. pushing now. clanEvent="
//			    + raidStartedByClan
//			    + "\t clanEventId="
//			    + clanEventId
//			    + "\t clanRaidId="
//			    + clanRaidId
//			    + "\t isFirstStage="
//			    + isFirstStage);
//			// record this (raidStartedByClan) in history along with all the
//			// clan users' stuff
//			// but should I be doing this
//			pushCurrentClanEventDataToHistory(clanId, now, raidStartedByClan);
//			return true;
//		}
//
//		// entered case where null != raidStartedByClan && !isFirstStage
//		// verified clanEventId and clan raid id are consistent, now time to
//		// verify
//		// the clan raid stage
//
//		// the clan raid stage start time should be null/not set
//		if (null != raidStartedByClan.getStageStartTime()) {
//			LOG.warn("the clan raid stage start time is not null when beginning clan raid."
//			    + " clanEvent="
//			    + raidStartedByClan);
//			// let the testers/users notify us that something is wrong, because
//			// I don't know how to resolve this issue
//			return false;
//		}
//
//		// maybe clan started event last week and didn't push the clan related
//		// information on the raid to the history table when event ended.
//		// TODO: if so, do it now and do it for the clan users' stuff as well
//		// Date raidStartedByClanDate = raidStartedByClan.getStageStartTime();
//		// int dayOfMonthRaidBegan =
//		// timeUtils.getDayOfMonthPst(raidStartedByClanDate);
//		// int dayOfMonthNow = timeUtils.getDayOfMonthPst(curDate);
//
//		LOG.info("valid clan info, can begin raid.");
//		return true;
//	}
//
//	// copy pasted from RecordClanRaidStatsController.writeChangesToD
//	private void pushCurrentClanEventDataToHistory( final int clanId, final Timestamp now,
//	    final ClanEventPersistentForClan clanEvent )
//	{
//		final int clanEventPersistentId = clanEvent.getClanEventPersistentId();
//		final int crId = clanEvent.getCrId();
//		final int crsId = clanEvent.getCrsId();
//		Timestamp stageStartTime = null;
//		if (null != clanEvent.getStageStartTime()) {
//			stageStartTime = new Timestamp(clanEvent.getStageStartTime()
//			    .getTime());
//		}
//		final int crsmId = clanEvent.getCrsmId();
//		Timestamp stageMonsterStartTime = null;
//		if (null != clanEvent.getStageMonsterStartTime()) {
//			stageMonsterStartTime = new Timestamp(clanEvent.getStageMonsterStartTime()
//			    .getTime());
//		}
//		final boolean won = false;
//
//		// record whatever is in the ClanEventPersistentForClan
//		int numInserted =
//		    InsertUtils.get()
//		        .insertIntoClanEventPersistentForClanHistory(clanId, now,
//		            clanEventPersistentId, crId, crsId, stageStartTime, crsmId,
//		            stageMonsterStartTime, won);
//
//		LOG.info("rows inserted into clan raid info for clan (should be 1): "
//		    + numInserted);
//		// get all the clan raid info for the users, and then delete them
//		final Map<Integer, ClanEventPersistentForUser> clanUserInfo =
//		    ClanEventPersistentForUserRetrieveUtils.getPersistentEventUserInfoForClanId(clanId);
//
//		// delete clan info for clan raid
//		DeleteUtils.get()
//		    .deleteClanEventPersistentForClan(clanId);
//
//		if ((null != clanUserInfo)
//		    && !clanUserInfo.isEmpty()) {
//			// record whatever is in the ClanEventPersistentForUser
//			numInserted = InsertUtils.get()
//			    .insertIntoCepfuRaidHistory(clanEventPersistentId, now, clanUserInfo);
//			LOG.info("rows inserted into clan raid info for user (should be "
//			    + clanUserInfo.size()
//			    + "): "
//			    + numInserted);
//
//			// delete clan user info for clan raid
//			final List<Integer> userIdList = new ArrayList<Integer>(clanUserInfo.keySet());
//			DeleteUtils.get()
//			    .deleteClanEventPersistentForUsers(userIdList);
//
//		}
//	}
//
//	private boolean writeChangesToDB( final String userUuid, final int clanId,
//	    final int clanEventPersistentId, final int clanRaidId, final Timestamp curTime,
//	    final boolean setMonsterTeamForRaid, final List<Long> userMonsterUuids,
//	    final boolean isFirstStage, final List<ClanEventPersistentForClan> clanInfo )
//	{
//
//		if (setMonsterTeamForRaid) {
//			final int numInserted =
//			    InsertUtils.get()
//			        .insertIntoUpdateMonstersClanEventPersistentForUser(userUuid, clanId,
//			            clanRaidId, userMonsterUuids);
//
//			LOG.info("num rows inserted into clan raid info for user table: "
//			    + numInserted);
//
//		} else if (isFirstStage) {
//			final ClanRaidStage crs =
//			    ClanRaidStageRetrieveUtils.getFirstStageForClanRaid(clanRaidId);
//			final int clanRaidStageId = crs.getId();
//
//			final Map<Integer, ClanRaidStageMonster> stageIdToMonster =
//			    ClanRaidStageMonsterRetrieveUtils.getClanRaidStageMonstersForClanRaidStageId(clanRaidStageId);
//			final ClanRaidStageMonster crsm = stageIdToMonster.get(clanRaidStageId);
//			final int crsmId = crsm.getId();
//
//			// once user begins a raid he auto begins a stage and auto begins
//			// the first monster
//			final int numInserted =
//			    InsertUtils.get()
//			        .insertIntoClanEventPersistentForClan(clanId, clanEventPersistentId,
//			            clanRaidId, clanRaidStageId, curTime, crsmId, curTime);
//
//			LOG.info("num rows inserted into clan raid info for clan table: "
//			    + numInserted);
//
//			final ClanEventPersistentForClan cepfc =
//			    new ClanEventPersistentForClan(clanId, clanEventPersistentId, clanRaidId,
//			        clanRaidStageId, curTime, crsmId, curTime);
//			clanInfo.add(cepfc);
//
//			// since beginning the first stage of a clan raide stage, zero out
//			// the damage
//			// for this raid for the clan in hazelcast
//			getClanEventUtil().updateClanIdCrsmDmg(clanId, 0, true);
//
//		} else {
//			LOG.info("starting another clan raid stage!!!!!!!!!");
//			final int numUpdated = UpdateUtils.get()
//			    .updateClanEventPersistentForClanStageStartTime(clanId, curTime);
//			LOG.info("num rows updated in clan raid info for clan table: "
//			    + numUpdated);
//
//			final ClanEventPersistentForClan cepfc =
//			    ClanEventPersistentForClanRetrieveUtils.getPersistentEventForClanId(clanId);
//			clanInfo.add(cepfc);
//		}
//
//		return true;
//	}
//
//	private void setClanAndUserDetails( final Builder resBuilder, final String userUuid,
//	    final int clanId, final int clanRaidId, final List<Long> userMonsterUuids,
//	    final List<FullUserMonsterProto> userMonsters )
//	{
//		// crsId and crsmId is not needed in PersistentClanEventUserInfoProto
//		// should already be in the db table
//		// ClanEventPersistentForClan cepfc =
//		// ClanEventPersistentForClanRetrieveUtils
//		// .getPersistentEventForClanId(clanId);
//
//		long userMonsterIdOne = 0;
//		long userMonsterIdTwo = 0;
//		long userMonsterIdThree = 0;
//
//		if (userMonsterUuids.size() >= 1) {
//			userMonsterIdOne = userMonsterUuids.get(0);
//		}
//		if (userMonsterUuids.size() >= 2) {
//			userMonsterIdTwo = userMonsterUuids.get(1);
//		}
//		if (userMonsterUuids.size() >= 3) {
//			userMonsterIdThree = userMonsterUuids.get(2);
//		}
//
//		final ClanEventPersistentForUser cepfu =
//		    new ClanEventPersistentForUser(userUuid, clanId, clanRaidId, 0, 0, 0, 0, 0,
//		        userMonsterIdOne, userMonsterIdTwo, userMonsterIdThree);
//		final PersistentClanEventUserInfoProto userDetails =
//		    CreateInfoProtoUtils.createPersistentClanEventUserInfoProto(cepfu, null,
//		        userMonsters);
//		resBuilder.setUserDetails(userDetails);
//	}
//
//	public TimeUtils getTimeUtils()
//	{
//		return timeUtils;
//	}
//
//	public void setTimeUtils( final TimeUtils timeUtils )
//	{
//		this.timeUtils = timeUtils;
//	}
//
//	public ClanEventUtil getClanEventUtil()
//	{
//		return clanEventUtil;
//	}
//
//	public void setClanEventUtil( final ClanEventUtil clanEventUtil )
//	{
//		this.clanEventUtil = clanEventUtil;
//	}
//
//}
