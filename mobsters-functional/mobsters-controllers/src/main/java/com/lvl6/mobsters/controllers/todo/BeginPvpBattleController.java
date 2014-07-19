//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.common.utils.TimeUtils;
//import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleRequestProto;
//import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleResponseProto;
//import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleResponseProto.BeginPvpBattleStatus;
//import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleResponseProto.Builder;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.BeginPvpBattleRequestEvent;
//import com.lvl6.mobsters.events.response.BeginPvpBattleResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventPvpProto.PvpProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class BeginPvpBattleController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(BeginPvpBattleController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Autowired
//	protected HazelcastPvpUtil hazelcastPvpUtil;
//
//	@Autowired
//	protected TimeUtils timeUtils;
//
//	@Autowired
//	protected PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil;
//
//	public BeginPvpBattleController()
//	{
//		numAllocatedThreads = 7;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new BeginPvpBattleRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_BEGIN_PVP_BATTLE_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final BeginPvpBattleRequestProto reqProto =
//		    ((BeginPvpBattleRequestEvent) event).getBeginPvpBattleRequestProto();
//		LOG.info("reqProto="
//		    + reqProto);
//
//		// get values sent from the client (the request proto)
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final int senderElo = reqProto.getSenderElo();
//		final int attackerId = senderProto.getUserUuid();
//		final Timestamp curTime = new Timestamp(reqProto.getAttackStartTime());
//		final Date curDate = new Date(curTime.getTime());
//		final PvpProto enemyProto = reqProto.getEnemy();
//		final int enemyUserId = enemyProto.getDefender()
//		    .getMinUserProto()
//		    .getUserUuid();
//
//		final boolean exactingRevenge = reqProto.getExactingRevenge();
//		Timestamp previousBattleEndTime = null;
//		if (exactingRevenge) {
//			// the battle that allowed sender to start this revenge battle
//			// where sender was the defender and enemy was the attacker
//			previousBattleEndTime = new Timestamp(reqProto.getPreviousBattleEndTime());
//		}
//
//		// set some values to send to the client (the response proto)
//		final BeginPvpBattleResponseProto.Builder resBuilder =
//		    BeginPvpBattleResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(BeginPvpBattleStatus.FAIL_OTHER); // default
//		final BeginPvpBattleResponseEvent resEvent =
//		    new BeginPvpBattleResponseEvent(attackerId);
//		resEvent.setTag(event.getTag());
//
//		// lock the user that client is going to attack, in order to prevent
//		// others from
//		// attacking same guy, only lock a real user
//		if (0 != enemyUserId) {
//			getLocker().lockPlayer(enemyUserId, this.getClass()
//			    .getSimpleName());
//		}
//		try {
//			final User attacker = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(attackerId);
//			PvpLeagueForUser enemyPlfu = null;
//			if (0 != enemyUserId) {
//				enemyPlfu =
//				    getPvpLeagueForUserRetrieveUtil().getUserPvpLeagueForId(enemyUserId);
//			}
//			// PvpUser enemyPu = getHazelcastPvpUtil().getPvpUser(enemyUserId);
//			final boolean legit =
//			    checkLegit(resBuilder, enemyPlfu, enemyUserId, enemyProto, curDate);
//
//			boolean successful = false;
//			if (legit) {
//				// since enemy exists, update the enemy's inBattleShieldEndTime
//				// in
//				// hazelcast and db
//				// record that the attacker is attacking the enemy
//				// calculateEloChange() will populate attackerEloChange and
//				// defenderEloChange
//				// the first values in both lists will be when attacker wins
//				// second values will be when attacker loses
//				final List<Integer> attackerEloChange = new ArrayList<Integer>();
//				final List<Integer> defenderEloChange = new ArrayList<Integer>();
//				calculateEloChange(senderElo, enemyProto, attackerEloChange, defenderEloChange);
//				// enemyProto could be a pastVersion of the current version of
//				// enemy if
//				// revenging
//
//				// if user exacting revenge update the history to say he can't
//				// exact revenge anymore
//				// also turn off sender's shield if it's on (in the case of
//				// revenge)
//				successful =
//				    writeChangesToDb(attacker, attackerId, enemyUserId, enemyPlfu,
//				        attackerEloChange, defenderEloChange, curTime, exactingRevenge,
//				        previousBattleEndTime);
//			}
//
//			if (successful) {
//				resBuilder.setStatus(BeginPvpBattleStatus.SUCCESS);
//			}
//
//			resEvent.setBeginPvpBattleResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in BeginPvpBattleController.processRequestEvent", e);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in BeginPvpBattleController processEvent", e);
//			// don't let the client hang
//			try {
//				resEvent.setBeginPvpBattleResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in BeginPvpBattleController.processRequestEvent", e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in BeginPvpBattleController processEvent", e);
//			}
//
//		} finally {
//			if (0 != enemyUserId) {
//				// only unlock if real user
//				svcTxManager.commit();
//			}
//		}
//	}
//
//	/*
//	 * Return true if user request is valid; false otherwise and set the builder
//	 * status to the appropriate value.
//	 */
//	private boolean checkLegit( final Builder resBuilder, final PvpLeagueForUser enemyPlfu,
//	    final int enemyUserId, final PvpProto enemyProto, final Date curDate )
//	{
//
//		if (0 == enemyUserId) {
//			// if fake user, just allow this to happen
//			return true;
//		}
//		if (null == enemyPlfu) {
//			LOG.error("unexpected error: enemy is null. enemyUserId="
//			    + enemyUserId
//			    + "\t enemyProto client sent="
//			    + enemyProto);
//			return false;
//		}
//
//		// check the shield times just to make sure this user is still
//		// attackable
//		// his shield end times should be in the past
//		final Date shieldEndTime = enemyPlfu.getShieldEndTime();
//		final Date inBattleEndTime = enemyPlfu.getInBattleShieldEndTime();
//
//		if ((shieldEndTime.getTime() > curDate.getTime())
//		    || (inBattleEndTime.getTime() > curDate.getTime())) {
//			// this is possible if another attacker got to this person first
//			resBuilder.setStatus(BeginPvpBattleStatus.FAIL_ENEMY_UNAVAILABLE);
//			LOG.warn("The user this client wants to attack has already been atttacked"
//			    + " or is being attacked. pvpUser="
//			    + enemyPlfu
//			    + "\t curDate"
//			    + curDate);
//			return false;
//		}
//
//		return true;
//	}
//
//	// fills up the lists attackerEloChange, defenderEloChange
//	private void calculateEloChange( final int attackerElo, final PvpProto defenderProto,
//	    final List<Integer> attackerEloChange, final List<Integer> defenderEloChange )
//	{
//
//		// TODO: calculate the actual values! And account for fake users!
//		// case where attacker wins
//		final int attackerWinEloChange = attackerElo + 10;
//		final int defenderElo = 4;
//		final int defenderLoseEloChange = Math.min(0, defenderElo - 10);
//
//		// case where attacker loses
//		final int attackerLoseEloChange = Math.min(0, attackerElo - 10);
//		final int defenderWinEloChange = defenderElo + 10;
//
//		// values are ordered by attacker win then attacker loses
//		attackerEloChange.add(attackerWinEloChange);
//		attackerEloChange.add(attackerLoseEloChange);
//
//		defenderEloChange.add(defenderLoseEloChange);
//		defenderEloChange.add(defenderWinEloChange);
//	}
//
//	// the first values in both elo change lists will be when attacker wins
//	// and second values will be when attacker loses
//	private boolean writeChangesToDb( final User attacker, final int attackerId,
//	    final int enemyId, final PvpLeagueForUser enemy, final List<Integer> attackerEloChange,
//	    final List<Integer> defenderEloChange, final Timestamp clientTime,
//	    final boolean exactingRevenge, final Timestamp previousBattleEndTime )
//	{
//
//		// record that attacker is attacking defender
//		final int attackerWinEloChange = attackerEloChange.get(0);
//		final int defenderLoseEloChange = defenderEloChange.get(0);
//		final int attackerLoseEloChange = attackerEloChange.get(1);
//		final int defenderWinEloChange = defenderEloChange.get(1);
//
//		LOG.info("inserting into PvpBattleForUser");
//		final int numInserted =
//		    InsertUtils.get()
//		        .insertUpdatePvpBattleForUser(attackerId, enemyId, attackerWinEloChange,
//		            defenderLoseEloChange, attackerLoseEloChange, defenderWinEloChange,
//		            clientTime);
//
//		LOG.info("numInserted (should be 1): "
//		    + numInserted);
//
//		// ACCOUNTING FOR FAKE DEFENDERS!
//		if (0 != enemyId) {
//			// assume that the longest a battle can go for is one hour from now
//			// so other users can't attack this person (who is under attack atm)
//			// for one hour
//			final long nowMillis = clientTime.getTime();
//			final Date newInBattleEndTime = new Date(nowMillis
//			    + ControllerConstants.PVP__MAX_BATTLE_DURATION_MILLIS);
//			enemy.setInBattleShieldEndTime(newInBattleEndTime);
//
//			// replace hazelcast object
//			final PvpUser nuEnemyPu = new PvpUser(enemy);
//			getHazelcastPvpUtil().replacePvpUser(nuEnemyPu, enemyId);
//
//			// as well as update db
//			final Timestamp nuInBattleEndTime = new Timestamp(newInBattleEndTime.getTime());
//			LOG.info("now="
//			    + clientTime);
//			LOG.info("should be one hour later, battleEndTime="
//			    + nuInBattleEndTime);
//			final int numUpdated = UpdateUtils.get()
//			    .updatePvpLeagueForUserShields(enemyId, null, nuInBattleEndTime);
//			LOG.info("(defender shield) num updated="
//			    + numUpdated);
//
//			// turn off attacker's shield if it's on, attacker can't revenge
//			// fake person
//			exactRevenge(attacker, attackerId, enemyId, clientTime, previousBattleEndTime,
//			    exactingRevenge);
//		}
//
//		return true;
//	}
//
//	private void exactRevenge( final User attacker, final int attackerId, final int defenderId,
//	    final Timestamp clientTime, final Timestamp prevBattleEndTime,
//	    final boolean exactingRevenge )
//	{
//		if (!exactingRevenge) {
//			LOG.info("not exacting revenge");
//			return;
//		}
//		if (null == prevBattleEndTime) {
//			LOG.info("not exacting revenge, prevBattleEndTime is null");
//		}
//		LOG.info("exacting revenge");
//		// need to switch the ids, because when exacting revenge roles are
//		// reversed
//		// when viewing from the battle that started this revenge battle
//		final int historyAttackerId = defenderId;
//		final int historyDefenderId = attackerId;
//		int numUpdated =
//		    UpdateUtils.get()
//		        .updatePvpBattleHistoryExactRevenge(historyAttackerId, historyDefenderId,
//		            prevBattleEndTime);
//		LOG.info("recorded that user exacted revenge. numUpdated (should be 1)="
//		    + numUpdated);
//
//		final PvpLeagueForUser attackerPlfu =
//		    getPvpLeagueForUserRetrieveUtil().getUserPvpLeagueForId(attackerId);
//
//		// if user has a shield up (defined as Time(now) < Time(shieldEnds)),
//		// change
//		// shield end time to login
//		// TODO: this is the same logic in QueueUpController
//		final Date curShieldEndTime = attackerPlfu.getShieldEndTime();
//		final Date nowDate = new Date(clientTime.getTime());
//		if (getTimeUtils().isFirstEarlierThanSecond(nowDate, curShieldEndTime)) {
//			LOG.info("user shield end time is now being reset since he's attacking with a shield");
//			LOG.info("1cur pvpuser="
//			    + getHazelcastPvpUtil().getPvpUser(attackerId));
//			final Date login = attacker.getLastLogin();
//			final Timestamp loginTime = new Timestamp(login.getTime());
//
//			numUpdated = UpdateUtils.get()
//			    .updatePvpLeagueForUserShields(attackerId, null, loginTime);
//			LOG.info("(defender shield) num updated="
//			    + numUpdated);
//
//			final PvpUser attackerOpu = new PvpUser(attackerPlfu);
//			attackerOpu.setShieldEndTime(login);
//			attackerOpu.setInBattleEndTime(login);
//			getHazelcastPvpUtil().replacePvpUser(attackerOpu, attackerId);
//			LOG.info("2cur pvpuser="
//			    + getHazelcastPvpUtil().getPvpUser(attackerId));
//			LOG.info("(should be same as 2cur pvpUser) 3cur pvpuser="
//			    + attackerOpu);
//		}
//	}
//
//	public HazelcastPvpUtil getHazelcastPvpUtil()
//	{
//		return hazelcastPvpUtil;
//	}
//
//	public void setHazelcastPvpUtil( final HazelcastPvpUtil hazelcastPvpUtil )
//	{
//		this.hazelcastPvpUtil = hazelcastPvpUtil;
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
//	public PvpLeagueForUserRetrieveUtil getPvpLeagueForUserRetrieveUtil()
//	{
//		return pvpLeagueForUserRetrieveUtil;
//	}
//
//	public void setPvpLeagueForUserRetrieveUtil(
//	    final PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil )
//	{
//		this.pvpLeagueForUserRetrieveUtil = pvpLeagueForUserRetrieveUtil;
//	}
//
//}
