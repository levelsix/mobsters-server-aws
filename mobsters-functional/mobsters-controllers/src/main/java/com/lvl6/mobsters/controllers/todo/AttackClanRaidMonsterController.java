package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.TimeUtils;
import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan;
import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser;
import com.lvl6.mobsters.dynamo.ClanEventPersistentUserReward;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterResponseProto.AttackClanRaidMonsterStatus;
import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventClanProto.AwardClanRaidStageRewardResponseProto;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.AttackClanRaidMonsterRequestEvent;
import com.lvl6.mobsters.events.response.AttackClanRaidMonsterResponseEvent;
import com.lvl6.mobsters.events.response.AwardClanRaidStageRewardResponseEvent;
import com.lvl6.mobsters.info.ClanRaidStage;
import com.lvl6.mobsters.info.ClanRaidStageMonster;
import com.lvl6.mobsters.info.ClanRaidStageReward;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventClanInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventUserInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.PersistentClanEventUserRewardProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserCurrentMonsterTeamProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class AttackClanRaidMonsterController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(AttackClanRaidMonsterController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected TimeUtils timeUtils;

	@Autowired
	protected ClanEventUtil clanEventUtil;

	public AttackClanRaidMonsterController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new AttackClanRaidMonsterRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_ATTACK_CLAN_RAID_MONSTER_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final AttackClanRaidMonsterRequestProto reqProto =
		    ((AttackClanRaidMonsterRequestEvent) event).getAttackClanRaidMonsterRequestProto();
		LOG.info("reqProto=");
		LOG.info(reqProto
		    + "");

		final MinimumUserProto sender = reqProto.getSender();
		final String userUuid = sender.getUserUuid();
		final MinimumClanProto mcp = sender.getClan();
		int clanId = 0;

		final PersistentClanEventClanInfoProto eventDetails = reqProto.getEventDetails();
		final Date curDate = new Date(reqProto.getClientTime());
		final Timestamp curTime = new Timestamp(curDate.getTime());
		final int damageDealt = reqProto.getDamageDealt(); // remember take min
														   // of
		// this with monsters's
		// remaining hp

		// extract the new healths for the monster(s)
		final List<UserMonsterCurrentHealthProto> monsterHealthProtos =
		    reqProto.getMonsterHealthsList();
		final Map<Long, Integer> userMonsterIdToExpectedHealth = new HashMap<Long, Integer>();
		MonsterStuffUtils.getUserMonsterUuids(monsterHealthProtos,
		    userMonsterIdToExpectedHealth);

		final FullUserMonsterProto userMonsterThatAttacked =
		    reqProto.getUserMonsterThatAttacked();
		final UserCurrentMonsterTeamProto userMonsterTeam = reqProto.getUserMonsterTeam();

		final AttackClanRaidMonsterResponseProto.Builder resBuilder =
		    AttackClanRaidMonsterResponseProto.newBuilder();
		resBuilder.setStatus(AttackClanRaidMonsterStatus.FAIL_OTHER);
		resBuilder.setSender(sender);
		resBuilder.setUserMonsterThatAttacked(userMonsterThatAttacked);
		resBuilder.setDmgDealt(damageDealt);

		// OUTLINE:
		// get the clan lock; get the clan raid object for the clan;
		// for the first ever (initial attack) in the raid, stage and
		// stageMonster start time
		// are already set.
		// When user kills curMonster, the crsmId changes to the next monster
		// and
		// the stageMonster start time changes to when curMonster was killed.
		// When user kills curMonster and go to the next stage, the stage and
		// stageMonster
		// StartTime and crsmId is set to nothing, crsId changes to the next
		// stage,

		ClanEventPersistentForClan clanEvent = null;
		final Map<Integer, ClanEventPersistentForUser> userIdToCepfu =
		    new HashMap<Integer, ClanEventPersistentForUser>();
		// boolean errorless = true;
		// barring error or request failure (but not attacking dead monster),
		// will always be set
		final List<ClanEventPersistentForClan> clanEventList =
		    new ArrayList<ClanEventPersistentForClan>();

		boolean lockedClan = false;
		if ((null != mcp)
		    && mcp.hasClanId()) {
			clanId = mcp.getClanId();
			lockedClan = getLocker().lockClan(clanId);
		}
		try {
			// so as to prevent another db read call to get the same information

			final boolean legitRequest =
			    checkLegitRequest(resBuilder, lockedClan, sender, userUuid, clanId,
			        eventDetails, curDate, clanEventList);

			// boolean success = false;
			final List<ClanEventPersistentUserReward> allRewards =
			    new ArrayList<ClanEventPersistentUserReward>();
			if (legitRequest) {
				LOG.info("legitRequest");

				if (!clanEventList.isEmpty()) {
					clanEvent = clanEventList.get(0);
				}
				final ClanEventPersistentForClan clanEventClientSent =
				    ClanStuffUtils.createClanEventPersistentForClan(eventDetails);

				// clanevent MIGHT BE MODIFIED (this will always be sent to the
				// client)
				writeChangesToDB(resBuilder, clanId, userUuid, damageDealt, curTime, clanEvent,
				    clanEventClientSent, userMonsterTeam, userMonsterIdToExpectedHealth,
				    userIdToCepfu, allRewards);
			}

			setClanEventClanDetails(resBuilder, clanEventList);
			setClanEventUserDetails(resBuilder, userIdToCepfu);

			final AttackClanRaidMonsterResponseEvent resEvent =
			    new AttackClanRaidMonsterResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setAttackClanRaidMonsterResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in AttackClanRaidMonsterController.processRequestEvent", e);
			}

			// tell whole clan on a successful attack
			if (AttackClanRaidMonsterStatus.SUCCESS.equals(resBuilder.getStatus())
			    || AttackClanRaidMonsterStatus.SUCCESS_MONSTER_JUST_DIED.equals(resBuilder.getStatus())) {
				server.writeClanEvent(resEvent, clanId);

				if (!allRewards.isEmpty()) {
					setClanEventRewards(allRewards, eventDetails);
				}
			}

		} catch (final Exception e) {
			LOG.error("exception in AttackClanRaidMonster processEvent", e);
			// errorless = false;
			try {
				resBuilder.setStatus(AttackClanRaidMonsterStatus.FAIL_OTHER);
				final AttackClanRaidMonsterResponseEvent resEvent =
				    new AttackClanRaidMonsterResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setAttackClanRaidMonsterResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in AttackClanRaidMonsterController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in AttackClanRaidMonster processEvent", e);
			}
		} finally {

			if ((null != mcp)
			    && mcp.hasClanId()
			    && lockedClan) {
				getLocker().unlockClan(clanId);
			}

		}

		// //not necessary, can just delete this part (purpose is to record in
		// detail, a user's
		// //contribution to a clan raid) in particular the damage a user has
		// done to a monster
		// //once the monster is dead
		// try {
		// ClanEventPersistentForClan clanEventClientSent =
		// clanEventList.get(1);
		// if (errorless && null != clanEventClientSent &&
		// !userIdToCepfu.isEmpty() &&
		// AttackClanRaidMonsterStatus.SUCCESS_MONSTER_JUST_DIED.equals(resBuilder.getStatus()))
		// {
		// int numInserted =
		// InsertUtils.get().insertIntoClanEventPersistentForUserHistoryDetail(
		// curTime, userIdToCepfu, clanEventClientSent);
		// LOG.info("num raid detail inserted = " + numInserted +
		// "\t should be " +
		// userIdToCepfu.size());
		// }
		// } catch (Exception e) {
		// LOG.warn("could not record more details about clan raid", e);
		// }

	}

	// want to update user monster healths even if the monster is dead
	private boolean checkLegitRequest( final Builder resBuilder, final boolean lockedClan,
	    final MinimumUserProto mup, final String userUuid, final int clanId,
	    final PersistentClanEventClanInfoProto eventDetails, final Date curDate,
	    final List<ClanEventPersistentForClan> clanEventList )
	{

		if (!lockedClan) {
			LOG.error("couldn't obtain clan lock");
			return false;
		}

		// check if user is in clan
		final ClanForUser uc = RetrieveUtils.userClanRetrieveUtils()
		    .getSpecificUserClan(userUuid, clanId);
		if (null == uc) {
			resBuilder.setStatus(AttackClanRaidMonsterStatus.FAIL_USER_NOT_IN_CLAN);
			LOG.error("not in clan. user="
			    + mup);
			return false;
		}

		if (null == eventDetails) {
			LOG.error("no PersistentClanEventClanInfoProto set by client.");
			return false;
		}

		// now check if clan already started the event
		final ClanEventPersistentForClan raidStartedByClan =
		    ClanEventPersistentForClanRetrieveUtils.getPersistentEventForClanId(clanId);

		final ClanEventPersistentForClan eventClientSent =
		    ClanStuffUtils.createClanEventPersistentForClan(eventDetails);

		// still want to deduct user's monsters' healths
		// if (null == raidStartedByClan) {
		//
		// }

		if ((null != raidStartedByClan)
		    && raidStartedByClan.equals(eventClientSent)) {// &&
			// null != raidStartedByClan.getStageStartTime()) {
			// stageStartTime won't be null in eventClientSent (this would mean
			// stage has not
			// started, so user can't attack, so this event should not have been
			// sent)

			clanEventList.add(raidStartedByClan);

			resBuilder.setStatus(AttackClanRaidMonsterStatus.SUCCESS);
			LOG.info("since data client sent matches up with db info, allowing attack, clanEvent="
			    + raidStartedByClan);
			return true;
		}

		// still want to deduct user's monsters' healths
		// if (null != raidStartedByClan && null ==
		// raidStartedByClan.getStageStartTime()) {
		// resBuilder.setStatus(AttackClanRaidMonsterStatus.FAIL_NO_STAGE_RAID_IN_PROGRESS);
		// LOG.warn("possibly remnants of old requests to attack clan raid stage monster. "
		// +
		// " raidInDb=" + raidStartedByClan + "\t eventDetailsClientSent=" +
		// eventDetails);
		// return false;
		// }

		return true;
	}

	private boolean writeChangesToDB( final Builder resBuilder, final int clanId,
	    final String userUuid, final int damageDealt, final Timestamp curTime,
	    final ClanEventPersistentForClan clanEvent,
	    final ClanEventPersistentForClan clanEventClientSent,
	    final UserCurrentMonsterTeamProto ucmtp,
	    final Map<Long, Integer> userMonsterIdToExpectedHealth,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu,
	    final List<ClanEventPersistentUserReward> allRewards ) throws Exception
	{

		LOG.info("clanEventInDb="
		    + clanEvent);
		LOG.info("clanEventClientSent="
		    + clanEventClientSent);

		if ((null != clanEvent)
		    && clanEvent.equals(clanEventClientSent)
		    && (null != clanEvent.getStageStartTime())
		    && (null != clanEvent.getStageMonsterStartTime())) {
			// this user might have just dealt the killing blow
			// clanEvent might be modified if the user dealt the killing blow
			updateClanRaid(resBuilder, userUuid, clanId, damageDealt, curTime, clanEvent,
			    clanEventClientSent, ucmtp, userIdToCepfu, allRewards);

		} else if ((null != clanEvent)
		    && clanEvent.equals(clanEventClientSent)
		    && (null == clanEvent.getStageStartTime())) {
			// in db the clan event info does not have a start time, meaning no
			// one began the
			// raid stage yet (probably because a user killed the last stage
			// monster, recently)
			LOG.warn("possibly remnants of old requests to attack clan raid stage monster. "
			    + " raidInDb="
			    + clanEvent
			    + "\t clanEventClientSent="
			    + clanEventClientSent);
			resBuilder.setStatus(AttackClanRaidMonsterStatus.FAIL_MONSTER_ALREADY_DEAD);

		} else if (null == clanEvent) {
			LOG.warn("possibly remnants of old requests to attack last clan raid stage monster."
			    + " raidInDb="
			    + clanEvent
			    + "\t clanEventClientSent="
			    + clanEventClientSent);
			resBuilder.setStatus(AttackClanRaidMonsterStatus.FAIL_NO_STAGE_RAID_IN_PROGRESS);
		}

		// update user's monsters' healths, don't know if it should be blindly
		// done though...
		final int numUpdated = UpdateUtils.get()
		    .updateUserMonstersHealth(userMonsterIdToExpectedHealth);
		LOG.info("num monster healths updated:"
		    + numUpdated);

		return true;
	}

	// Updates tables: clan_event_persistent_for clan/user
	// for the first ever, initial attack, in the raid, stage and stageMonster
	// start time
	// are set.
	// When user kills curMonster, the crsmId changes to the next monster and
	// the stageMonster start time changes to when curMonster was killed.
	// When user kills curMonster and is the last in the stage, the stage
	// monster startTime
	// and stage startTime are set to nothing, crsId changes to the next stage
	// and
	// crsmId changes to the first monster in the stage
	// When user kills curMonster, is the last in the, and no more stages,
	// the clan raid is over, delete everything and send it to history.

	// userIdToCepfu will hold all clan users' clan raid info, if
	// checkIfMonsterDied = true
	// give this info to caller

	// ClanEventPersistentForClan clanEvent MIGHT BE UPDATED
	// Map<Integer, ClanEventPersistentForUser> userIdToCepfu will be populated
	// if the
	// monster died
	private void updateClanRaid( final Builder resBuilder, final String userUuid,
	    final int clanId, final int dmgDealt, final Timestamp curTime,
	    final ClanEventPersistentForClan clanEvent,
	    final ClanEventPersistentForClan clanEventClientSent,
	    final UserCurrentMonsterTeamProto ucmtp,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu,
	    final List<ClanEventPersistentUserReward> allRewards ) throws Exception
	{
		LOG.info("updating clan raid");

		final int curCrId = clanEvent.getCrId();
		final int curCrsId = clanEvent.getCrsId();
		final int curCrsmId = clanEvent.getCrsmId();

		final List<Integer> newDmgList = new ArrayList<Integer>();
		int newDmg = dmgDealt;
		final boolean monsterDied =
		    checkMonsterDead(clanId, curCrsId, curCrsmId, dmgDealt, userIdToCepfu, newDmgList);
		newDmg = newDmgList.get(0);
		LOG.info("actual dmg dealt="
		    + newDmg);

		if (monsterDied) {
			// if monsterDied then get all the clan users' damages, also update
			// cur user's crsmDmg
			getAllClanUserDmgInfo(userUuid, clanId, newDmg, userIdToCepfu);
		}

		final ClanRaidStage nextStage =
		    ClanRaidStageRetrieveUtils.getNextStageForClanRaidStageId(curCrsId, curCrId);
		final ClanRaidStageMonster curStageNextCrsm =
		    ClanRaidStageMonsterRetrieveUtils.getNextMonsterForClanRaidStageMonsterId(
		        curCrsmId, curCrsId);

		if ((null == nextStage)
		    && monsterDied) {
			LOG.info("user killed the monster and ended the raid!");
			// this user just killed the last clan raid monster: this means
			// put all the clan users' clan raid info into the history table
			recordClanRaidVictory(clanId, clanEvent, curTime, userIdToCepfu);

			// TODO: GIVE OUT THE REWARDS AFTER EVERY STAGE THAT HAS JUST ENDED
			final List<ClanEventPersistentUserReward> rewards =
			    awardRewards(curCrsId, clanEventClientSent.getStageStartTime(), curTime,
			        clanEventClientSent.getClanEventPersistentId(), userIdToCepfu);
			allRewards.addAll(rewards);

		} else if ((null == curStageNextCrsm)
		    && monsterDied) {
			// this user killed last monster in stage, go to the next one
			// clanEvent will be modified
			LOG.info("user killed the monster and ended the stage!");
			recordClanRaidStageVictory(clanId, curCrsId, nextStage, curTime, clanEvent,
			    userIdToCepfu);

			// TODO: GIVE OUT THE REWARDS AFTER EVERY STAGE THAT HAS JUST ENDED
			final List<ClanEventPersistentUserReward> rewards =
			    awardRewards(curCrsId, clanEventClientSent.getStageStartTime(), curTime,
			        clanEventClientSent.getClanEventPersistentId(), userIdToCepfu);
			allRewards.addAll(rewards);

		} else if (monsterDied) {
			LOG.info("user killed the monster!");
			// this user killed a monster, continue with the next one
			// clanEvent will be modified
			recordClanRaidStageMonsterVictory(userUuid, clanId, curCrsId, curTime, newDmg,
			    curStageNextCrsm, clanEvent, userIdToCepfu);

		} else if (!monsterDied) {
			LOG.info("user did not deal killing blow.");
			final int numUpdated =
			    UpdateUtils.get()
			        .updateClanEventPersistentForUserCrsmDmgDone(userUuid, newDmg, curCrsId,
			            curCrsmId);
			LOG.info("rows updated when user attacked monster. num="
			    + numUpdated);
			final ClanEventPersistentForUser cepfu =
			    ClanEventPersistentForUserRetrieveUtils.getPersistentEventUserInfoForUserUuidClanId(
			        userUuid, clanId);

			// want to send to everyone in clan this user's clan event
			// information
			final PersistentClanEventUserInfoProto pceuip =
			    CreateInfoProtoUtils.createPersistentClanEventUserInfoProto(cepfu, null,
			        ucmtp.getCurrentTeamList());
			resBuilder.addClanUsersDetails(pceuip);

			// update the damage for this raid monster for the clan in
			// hazelcast, add the dmg
			// since it keeps track of how much dmg the clan has done so far
			final boolean replaceCrsmDmg = false;
			getClanEventUtil().updateClanIdCrsmDmg(clanId, newDmg, replaceCrsmDmg);
		}

		if (monsterDied
		    && (0 != newDmg)) {
			resBuilder.setStatus(AttackClanRaidMonsterStatus.SUCCESS_MONSTER_JUST_DIED);
		} else if (monsterDied
		    && (0 == newDmg)) {
			LOG.error("not really error since will continue processing. should not be here. "
			    + "what has been processed same as this user killing last monster in the raid");
			resBuilder.setStatus(AttackClanRaidMonsterStatus.FAIL_MONSTER_ALREADY_DEAD);
		} else if (!monsterDied) {
			resBuilder.setStatus(AttackClanRaidMonsterStatus.SUCCESS);
		}
		resBuilder.setDmgDealt(newDmg);
	}

	// update damage this user dealt accordingly
	private boolean checkMonsterDead( final int clanId, final int crsId, final int crsmId,
	    final int dmgDealt, final Map<Integer, ClanEventPersistentForUser> userIdToCepfu,
	    final List<Integer> newDmgList )
	{
		final ClanRaidStageMonster crsm =
		    ClanRaidStageMonsterRetrieveUtils.getClanRaidStageMonsterForClanRaidStageMonsterId(crsmId);

		// userIdToCepfu might actually be populated
		final int dmgSoFar = getCrsmDmgSoFar(clanId, crsId, crsmId, userIdToCepfu);
		final int crsmHp = crsm.getMonsterHp();

		LOG.info("dmgSoFar="
		    + dmgSoFar);
		LOG.info("monster's health="
		    + crsmHp);
		LOG.info("dmgDealt="
		    + dmgDealt);

		// default values if monster isn't dead
		int newDmgDealt = dmgDealt;
		boolean monsterDied = false;

		if ((dmgSoFar + dmgDealt) >= crsmHp) {
			LOG.info("monster just died! dmgSoFar="
			    + dmgSoFar
			    + "\t dmgDealt="
			    + dmgDealt
			    + "\t monster="
			    + crsm);

			monsterDied = true;
			// need to deal dmg equal to how much hp was left
			newDmgDealt = Math.min(dmgDealt, crsmHp
			    - dmgSoFar);
			newDmgDealt = Math.max(0, newDmgDealt); // in case (crsmHp -
			                                        // dmgSoFar) is negative
		}
		if (dmgSoFar >= crsmHp) {
			// should never go in here if everything went right
			LOG.error("(won't error out) client sent an attack and server didn't update the"
			    + " ClanEventPersistentForClan when the monster was just killed by a previous."
			    + " AttackClanRaidMonster event");
			// treating it like normal
			newDmgDealt = 0;
		}

		// give caller return values
		newDmgList.add(newDmgDealt);

		LOG.info("newDmg="
		    + newDmgDealt);
		LOG.info("monsterDied="
		    + monsterDied);
		LOG.info("newUserIdToCepfu="
		    + userIdToCepfu);
		return monsterDied;
	}

	// get clan's crsmDmg from Hazelcast first if somehow fails or is 0 then,
	// actually query db for clan users' clan raid info, check if monster is
	// dead,
	// might actually populate userIdToCepfu
	private int getCrsmDmgSoFar( final int clanId, final int crsId, final int crsmId,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu )
	{
		// get the clan raid information for all the clan users
		// shouldn't be null (per the retrieveUtils)

		int dmgSoFar = getClanEventUtil().getCrsmDmgForClanId(clanId);

		if (0 == dmgSoFar) {
			final Map<Integer, ClanEventPersistentForUser> newUserIdToCepfu =
			    ClanEventPersistentForUserRetrieveUtils.getPersistentEventUserInfoForClanId(clanId);

			// history purposes and so entries in tables don't look weird (crsId
			// and crsmId=0)
			setCrsCrsmId(crsId, crsmId, newUserIdToCepfu);

			dmgSoFar = sumDamageDoneToMonster(newUserIdToCepfu);

			userIdToCepfu.putAll(newUserIdToCepfu);
		}

		return dmgSoFar;
	}

	private void setCrsCrsmId( final int crsId, final int crsmId,
	    final Map<Integer, ClanEventPersistentForUser> newUserIdToCepfu )
	{

		for (final ClanEventPersistentForUser cepfu : newUserIdToCepfu.values()) {
			final int cepfuCrsId = cepfu.getCrsId();
			if (0 == cepfuCrsId) {
				cepfu.setCrsId(crsId);
			}

			final int cepfuCrsmId = cepfu.getCrsmId();
			if (0 == cepfuCrsmId) {
				cepfu.setCrsmId(crsmId);
			}
		}
	}

	private int sumDamageDoneToMonster(
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu )
	{
		int dmgTotal = 0;
		LOG.info("printing the users who attacked in this raid");
		for (final ClanEventPersistentForUser cepfu : userIdToCepfu.values()) {
			LOG.info("cepfu="
			    + cepfu);
			dmgTotal += cepfu.getCrsmDmgDone();
		}

		return dmgTotal;
	}

	private void getAllClanUserDmgInfo( final String userUuid, final int clanId,
	    final int newDmg, final Map<Integer, ClanEventPersistentForUser> userIdToCepfu )
	{

		// since monster died, get all clan users' clan raid info, in order to
		// send
		// to the client
		if ((null == userIdToCepfu)
		    || userIdToCepfu.isEmpty()) {
			// more often than not, don't have it so get it
			// only case where would have it is ClanEventUtil.java (Hazelcast)
			// doesn't
			final Map<Integer, ClanEventPersistentForUser> newUserIdToCepfu =
			    ClanEventPersistentForUserRetrieveUtils.getPersistentEventUserInfoForClanId(clanId);
			userIdToCepfu.putAll(newUserIdToCepfu);
		}

		// update the user's crsmDmg
		final ClanEventPersistentForUser cepfu = userIdToCepfu.get(userUuid);
		final int newCrsmDmgDone = cepfu.getCrsmDmgDone()
		    + newDmg;
		cepfu.setCrsmDmgDone(newCrsmDmgDone);
	}

	private void recordClanRaidVictory( final int clanId,
	    final ClanEventPersistentForClan clanEvent, final Timestamp now,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu )
	{
		final int clanEventId = clanEvent.getClanEventPersistentId();
		final int crId = clanEvent.getCrId();
		final int crsId = clanEvent.getCrsId();
		Timestamp stageStartTime = null;
		if (null != clanEvent.getStageStartTime()) {
			stageStartTime = new Timestamp(clanEvent.getStageStartTime()
			    .getTime());
		}
		final int crsmId = clanEvent.getCrsmId();
		Timestamp stageMonsterStartTime = null;
		if (null != clanEvent.getStageMonsterStartTime()) {
			stageMonsterStartTime = new Timestamp(clanEvent.getStageMonsterStartTime()
			    .getTime());
		}
		final boolean won = true;

		// record whatever is in the ClanEventPersistentForClan
		int numInserted =
		    InsertUtils.get()
		        .insertIntoClanEventPersistentForClanHistory(clanId, now, clanEventId, crId,
		            crsId, stageStartTime, crsmId, stageMonsterStartTime, won);

		// clan_event_persistent_for_clan_history
		LOG.info("rows inserted into clan raid info for clan history (should be 1): "
		    + numInserted);

		// delete clan info for clan raid
		DeleteUtils.get()
		    .deleteClanEventPersistentForClan(clanId);

		if ((null != userIdToCepfu)
		    && !userIdToCepfu.isEmpty()) { // should always go in here
			numInserted = InsertUtils.get()
			    .insertIntoCepfuRaidHistory(clanEventId, now, userIdToCepfu);
			// clan_event_persistent_for_user_history
			LOG.info("rows inserted into clan raid info for user history (should be "
			    + userIdToCepfu.size()
			    + "): "
			    + numInserted);

			// delete clan user info for clan raid
			final List<Integer> userIdList = new ArrayList<Integer>(userIdToCepfu.keySet());
			DeleteUtils.get()
			    .deleteClanEventPersistentForUsers(userIdList);

			// record to the clan raid stage user history
			final int stageHp =
			    ClanRaidStageRetrieveUtils.getClanRaidStageHealthForCrsId(crsId);
			numInserted =
			    InsertUtils.get()
			        .insertIntoCepfuRaidStageHistory(clanEventId, stageStartTime, now, stageHp,
			            userIdToCepfu);
			LOG.info("clan event persistent for user raid stage history, numInserted="
			    + numInserted);
		}

		// delete the crsmDmg for this clan
		getClanEventUtil().deleteCrsmDmgForClanId(clanId);
	}

	// last monster in stage. this means:
	// 1) for ClanEventPersistentForClan, crsId goes to the next one,
	// stageStartTime
	// goes null, crsmId goes to the first monster in the crs, and
	// stageMonsterStartTime
	// goes null
	// 2) for ClanEventPersistentForClan, push crsmDmg to crsDmg, update crs and
	// crsm ids
	private void recordClanRaidStageVictory( final int clanId, final int curCrsId,
	    final ClanRaidStage nextStage, final Timestamp curTime,
	    final ClanEventPersistentForClan cepfc,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu ) throws Exception
	{
		final int eventId = cepfc.getClanEventPersistentId();
		final Timestamp stageStartTime = new Timestamp(cepfc.getStageStartTime()
		    .getTime());

		final int nextCrsId = nextStage.getId();
		final ClanRaidStageMonster nextCrsFirstCrsm =
		    ClanRaidStageMonsterRetrieveUtils.getFirstMonsterForClanRaidStage(nextCrsId);

		if (null == nextCrsFirstCrsm) {
			// WTF???
			throw new Exception("WTF!!!! clan raid stage has no monsters! >:( crs="
			    + nextStage);
		}

		final int nextCrsCrsmId = nextCrsFirstCrsm.getId();
		int numUpdated = UpdateUtils.get()
		    .updateClanEventPersistentForClanGoToNextStage(clanId, nextCrsId, nextCrsCrsmId);
		LOG.info("clan just cleared stage! nextStage="
		    + nextStage
		    + "\t curEventInfo"
		    + cepfc
		    + "\t numUpdated="
		    + numUpdated);
		// need to update clan raid info for all the clan users

		numUpdated =
		    UpdateUtils.get()
		        .updateClanEventPersistentForUserGoToNextStage(nextCrsId, nextCrsCrsmId,
		            userIdToCepfu);

		LOG.info("rows updated when clan cleared stage. num="
		    + numUpdated);

		// need to update ClanEventPersistentForClan cepfc since need to update
		// client
		cepfc.setCrsId(nextCrsId);
		cepfc.setStageStartTime(null);
		cepfc.setCrsmId(nextCrsCrsmId);
		cepfc.setStageMonsterStartTime(null);

		// since just killed monster, another one takes its place. replace the
		// amount
		// in hazelcast with 0 which represents how much damage the clan has
		// done so far
		final int newCrsmHp = 0;// nextCrsFirstCrsm.getMonsterHp();
		final boolean replaceCrsmDmg = true;
		getClanEventUtil().updateClanIdCrsmDmg(clanId, newCrsmHp, replaceCrsmDmg);

		// record to the clan raid stage user history, since stage ended
		final int stageHp = ClanRaidStageRetrieveUtils.getClanRaidStageHealthForCrsId(curCrsId);
		final int numInserted =
		    InsertUtils.get()
		        .insertIntoCepfuRaidStageHistory(eventId, stageStartTime, curTime, stageHp,
		            userIdToCepfu);
		LOG.info("clan event persistent for user raid stage history, numInserted="
		    + numInserted);
	}

	// user killed monster. this means:
	// 1) for clanEventPersistentForClan, crsmId goes to the next one, and stage
	// monser start time changes to now
	// 2) for clanEventpersistentForUser, for all clan members update push their
	// crsmDmg
	// to crsDmg and update the crsmId
	private void recordClanRaidStageMonsterVictory( final String userUuid, final int clanId,
	    final int crsId, final Timestamp curTime, final int newDmg,
	    final ClanRaidStageMonster nextCrsm, final ClanEventPersistentForClan cepfc,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu )
	{

		final int nextCrsmId = nextCrsm.getId();

		int numUpdated = UpdateUtils.get()
		    .updateClanEventPersistentForClanGoToNextMonster(clanId, nextCrsmId, curTime);
		LOG.info("rows updated when clan killed monster. num="
		    + numUpdated);

		numUpdated = UpdateUtils.get()
		    .updateClanEventPersistentForUsersGoToNextMonster(crsId, nextCrsmId, userIdToCepfu);

		LOG.info("rows updated when user killed monster. num="
		    + numUpdated);

		// need to update ClanEventPersistentForClan cepfc since need to update
		// client
		cepfc.setCrsmId(nextCrsmId);
		cepfc.setStageMonsterStartTime(curTime);

		// since just killed monster, another one takes its place. replace the
		// amount
		// in hazelcast with 0 which represents how much damage the clan has
		// done so far
		final int newCrsmHp = 0;// nextCrsm.getMonsterHp();
		final boolean replaceCrsmDmg = true;
		getClanEventUtil().updateClanIdCrsmDmg(clanId, newCrsmHp, replaceCrsmDmg);
	}

	// for currency rewards, calculate the number user gets, then take a
	// fraction of it
	// for monster reward, calculate user contribution in stage (crsmDmg +
	// crsDmg)
	// divided it by stageHp, multiply it by the reward's
	// monsterDropRateMultiplier
	// generate random number and if below computed value, user gets monster
	private List<ClanEventPersistentUserReward> awardRewards( final int crsId,
	    final Date crsStartDate, final Date crsEndDate, final int clanEventId,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu )
	{
		final int stageHp = ClanRaidStageRetrieveUtils.getClanRaidStageHealthForCrsId(crsId);
		final Timestamp crsStartTime = new Timestamp(crsStartDate.getTime());
		final Timestamp crsEndTime = new Timestamp(crsEndDate.getTime());

		final List<ClanEventPersistentUserReward> allRewards =
		    new ArrayList<ClanEventPersistentUserReward>();

		final Map<Integer, ClanRaidStageReward> rewardUuidsToRewards =
		    ClanRaidStageRewardRetrieveUtils.getClanRaidStageRewardsForClanRaidStageId(crsId);

		// for each user generate the rewards he gets based on his contribution
		for (final Integer userUuid : userIdToCepfu.keySet()) {
			final ClanEventPersistentForUser cepfu = userIdToCepfu.get(userUuid);

			generateAllRewardsForUser(crsId, crsStartDate, crsEndDate, clanEventId, cepfu,
			    stageHp, rewardUuidsToRewards, allRewards);
		}

		final List<Integer> ids = InsertUtils.get()
		    .insertIntoCepUserReward(crsStartTime, crsId, crsEndTime, clanEventId, allRewards);
		LOG.info("num clan event user rewards inserted:"
		    + ids.size());

		// set the ids because these rewards are going to be written back to the
		// client
		for (int i = 0; i < ids.size(); i++) {
			final int id = ids.get(i);
			final ClanEventPersistentUserReward reward = allRewards.get(i);
			reward.setId(id);
		}

		return allRewards;
	}

	private void generateAllRewardsForUser( final int crsId, final Date crsStartDate,
	    final Date crsEndDate, final int clanEventId, final ClanEventPersistentForUser cepfu,
	    final int stageHp, final Map<Integer, ClanRaidStageReward> rewardUuidsToRewards,
	    final List<ClanEventPersistentUserReward> allRewards )
	{

		// for each reward, see what this user gets
		for (final ClanRaidStageReward reward : rewardUuidsToRewards.values()) {

			final List<ClanEventPersistentUserReward> someRewards =
			    generateSomeRewardsForUser(crsId, crsStartDate, crsEndDate, clanEventId, cepfu,
			        stageHp, reward);

			// NOTE: IF WANT TO CUT DOWN ON REWARDS WRITTEN, CAN AGGREGATE
			// someRewards LIST
			// BEFORE ADDING IT ALL INTO allRewards
			allRewards.addAll(someRewards);
		}
	}

	private List<ClanEventPersistentUserReward> generateSomeRewardsForUser( final int crsId,
	    final Date crsStartDate, final Date crsEndDate, final int clanEventId,
	    final ClanEventPersistentForUser cepfu, final int stageHp,
	    final ClanRaidStageReward reward )
	{
		final List<ClanEventPersistentUserReward> userRewards =
		    new ArrayList<ClanEventPersistentUserReward>();

		final String userUuid = cepfu.getUserUuid();
		final int userCrsDmg = cepfu.getCrsDmgDone()
		    + cepfu.getCrsmDmgDone();
		final float userCrsContribution = (userCrsDmg)
		    / (stageHp);

		int staticDataId = 0;
		// TODO: FIGURE OUT IF CURRENCY CALCULATION IS OK (right now just
		// truncating the value)
		// create the userOilReward maybe
		final int userOilReward = (int) (((float) reward.getOilDrop()) * userCrsContribution);
		createClanEventPersistentUserReward(MiscMethods.OIL, userOilReward, staticDataId,
		    crsId, crsStartDate, crsEndDate, clanEventId, userUuid, userRewards);

		// create the userOilReward maybe
		final int userCashReward = (int) (((float) reward.getCashDrop()) * userCrsContribution);
		createClanEventPersistentUserReward(MiscMethods.CASH, userCashReward, staticDataId,
		    crsId, crsStartDate, crsEndDate, clanEventId, userUuid, userRewards);

		final int monsterId = reward.getMonsterId();
		if (0 >= monsterId) {
			// not a monster reward
			return userRewards;
		}

		// compute monster reward
		final float monsterDropRate = userCrsContribution
		    * ((float) reward.getExpectedMonsterRewardQuantity());
		final Random rand = reward.getRand();
		if (rand.nextFloat() < monsterDropRate) {
			// user gets the monster reward
			staticDataId = monsterId;
			final int quantity = 1;
			createClanEventPersistentUserReward(MiscMethods.MONSTER, staticDataId, quantity,
			    crsId, crsStartDate, crsEndDate, clanEventId, userUuid, userRewards);
		}

		return userRewards;
	}

	// goal is to make a method that knows when not to create a reward so caller
	// doesn't
	// have to
	private void createClanEventPersistentUserReward( final String resourceType,
	    final int staticDataId, final int quantity, final int crsId, final Date crsStartDate,
	    final Date crsEndDate, final int clanEventId, final String userUuid,
	    final List<ClanEventPersistentUserReward> userRewards )
	{

		if (quantity <= 0) {
			return;
		}

		final ClanEventPersistentUserReward cepur =
		    new ClanEventPersistentUserReward(0, userUuid, crsStartDate, crsId, crsEndDate,
		        resourceType, staticDataId, quantity, clanEventId, null);

		userRewards.add(cepur);
	}

	private void setClanEventClanDetails( final Builder resBuilder,
	    final List<ClanEventPersistentForClan> clanEventList )
	{
		if (!clanEventList.isEmpty()) {
			final ClanEventPersistentForClan cepfc = clanEventList.get(0);
			if (null != cepfc) {
				final PersistentClanEventClanInfoProto updatedEventDetails =
				    CreateInfoProtoUtils.createPersistentClanEventClanInfoProto(cepfc);
				resBuilder.setEventDetails(updatedEventDetails);
			}
		}
	}

	private void setClanEventUserDetails( final Builder resBuilder,
	    final Map<Integer, ClanEventPersistentForUser> userIdToCepfu )
	{
		if (!userIdToCepfu.isEmpty()) {
			// whenever server has this information send it to the clients
			final List<Long> userMonsterUuids =
			    MonsterStuffUtils.getUserMonsterUuidsInClanRaid(userIdToCepfu);

			final Map<Long, MonsterForUser> idsToUserMonsters =
			    RetrieveUtils.monsterForUserRetrieveUtils()
			        .getSpecificUserMonsters(userMonsterUuids);

			for (final ClanEventPersistentForUser cepfu : userIdToCepfu.values()) {
				final PersistentClanEventUserInfoProto pceuip =
				    CreateInfoProtoUtils.createPersistentClanEventUserInfoProto(cepfu,
				        idsToUserMonsters, null);
				resBuilder.addClanUsersDetails(pceuip);
			}
		}
	}

	// write to the whole clan the rewards for all the users when the stage ends
	// and
	// there are rewards
	private void setClanEventRewards( final List<ClanEventPersistentUserReward> allRewards,
	    final PersistentClanEventClanInfoProto eventDetails )
	{
		if (null == allRewards) {
			return;
		}

		final int clanId = eventDetails.getClanId();
		final int crsId = eventDetails.getClanRaidStageId();

		final AwardClanRaidStageRewardResponseProto.Builder resBuilder =
		    AwardClanRaidStageRewardResponseProto.newBuilder();
		resBuilder.setCrsId(crsId);

		for (final ClanEventPersistentUserReward reward : allRewards) {
			final PersistentClanEventUserRewardProto rewardProto =
			    CreateInfoProtoUtils.createPersistentClanEventUserRewardProto(reward);
			resBuilder.addAllRewards(rewardProto);
		}

		final AwardClanRaidStageRewardResponseEvent resEvent =
		    new AwardClanRaidStageRewardResponseEvent(clanId);
		resEvent.setTag(0);
		resEvent.setAwardClanRaidStageRewardResponseProto(resBuilder.build());

		server.writeClanEvent(resEvent, clanId);
	}

	public TimeUtils getTimeUtils()
	{
		return timeUtils;
	}

	public void setTimeUtils( final TimeUtils timeUtils )
	{
		this.timeUtils = timeUtils;
	}

	public ClanEventUtil getClanEventUtil()
	{
		return clanEventUtil;
	}

	public void setClanEventUtil( final ClanEventUtil clanEventUtil )
	{
		this.clanEventUtil = clanEventUtil;
	}

}
