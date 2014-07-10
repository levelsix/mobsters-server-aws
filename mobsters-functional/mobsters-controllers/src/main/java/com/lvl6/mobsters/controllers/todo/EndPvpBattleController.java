package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.lvl6.mobsters.dynamo.PvpBattleForUser;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleRequestProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleResponseProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleResponseProto.EndPvpBattleStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.EndPvpBattleRequestEvent;
import com.lvl6.mobsters.events.response.EndPvpBattleResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class EndPvpBattleController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(EndPvpBattleController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected HazelcastPvpUtil hazelcastPvpUtil;

	@Autowired
	protected TimeUtils timeUtils;

	@Autowired
	protected PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil;

	public EndPvpBattleController()
	{
		numAllocatedThreads = 7;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new EndPvpBattleRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_END_PVP_BATTLE_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final EndPvpBattleRequestProto reqProto =
		    ((EndPvpBattleRequestEvent) event).getEndPvpBattleRequestProto();
		LOG.info("reqProto="
		    + reqProto);

		// get values sent from the client (the request proto)
		final MinimumUserProtoWithMaxResources senderProtoMaxResources = reqProto.getSender();
		final MinimumUserProto senderProto = senderProtoMaxResources.getMinUserProto();
		final int attackerId = senderProto.getUserUuid();
		final int defenderId = reqProto.getDefenderUuid();
		final boolean attackerAttacked = reqProto.getUserAttacked();
		final boolean attackerWon = reqProto.getUserWon();
		int oilStolen = reqProto.getOilChange(); // non negative
		int cashStolen = reqProto.getCashChange(); // non negative

		if (!attackerWon
		    && (oilStolen != 0)) {
			LOG.error("client should set oilStolen to be 0 since attacker lost!"
			    + "\t client sent oilStolen="
			    + oilStolen);
			oilStolen = 0;
		}

		if (!attackerWon
		    && (cashStolen != 0)) {
			LOG.error("client should set cashStolen to be 0 since attacker lost!"
			    + "\t client sent cashStolen="
			    + cashStolen);
			cashStolen = 0;
		}

		final int attackerMaxOil = senderProtoMaxResources.getMaxOil();
		final int attackerMaxCash = senderProtoMaxResources.getMaxCash();

		final Timestamp curTime = new Timestamp(reqProto.getClientTime());
		final Date curDate = new Date(curTime.getTime());

		// set some values to send to the client (the response proto)
		final EndPvpBattleResponseProto.Builder resBuilder =
		    EndPvpBattleResponseProto.newBuilder();
		resBuilder.setSender(senderProtoMaxResources);
		resBuilder.setStatus(EndPvpBattleStatus.FAIL_OTHER); // default
		final EndPvpBattleResponseEvent resEvent = new EndPvpBattleResponseEvent(attackerId);
		resEvent.setTag(event.getTag());

		final List<Integer> userUuids = new ArrayList<Integer>();
		userUuids.add(attackerId);
		userUuids.add(defenderId); // doesn't matter if fake i.e. enemyUserId=0

		// NEED TO LOCK BOTH PLAYERS, well need to lock defender because
		// defender can be online,
		// Lock attacker because someone might be attacking him while attacker
		// is attacking defender?
		if (0 != defenderId) {
			getLocker().lockPlayers(defenderId, attackerId, this.getClass()
			    .getSimpleName());
			LOG.info("locked defender and attacker");
		} else {
			// ONLY ATTACKER IF DEFENDER IS FAKE
			getLocker().lockPlayer(attackerId, this.getClass()
			    .getSimpleName());
			LOG.info("locked attacker");
		}

		try {
			// get whatever from db
			final Map<Integer, User> users = RetrieveUtils.userRetrieveUtils()
			    .getUsersByUuids(userUuids);
			final User attacker = users.get(attackerId);
			final User defender = users.get(defenderId);
			final PvpBattleForUser pvpBattleInfo =
			    PvpBattleForUserRetrieveUtils.getPvpBattleForUserForAttacker(attackerId);
			LOG.info("pvpBattleInfo="
			    + pvpBattleInfo);

			final Map<Integer, PvpLeagueForUser> plfuMap =
			    getPvpLeagueForUserRetrieveUtil().getUserPvpLeagueForUsers(userUuids);
			LOG.info("plfuMap="
			    + plfuMap);
			// these objects will be updated if not null
			PvpLeagueForUser attackerPlfu = null;
			PvpLeagueForUser defenderPlfu = null;

			if (plfuMap.containsKey(attackerId)) {
				attackerPlfu = plfuMap.get(attackerId);
			}
			// could be fake user so could be null
			if (plfuMap.containsKey(defenderId)) {
				defenderPlfu = plfuMap.get(defenderId);
			}

			final boolean legit =
			    checkLegit(resBuilder, attacker, defender, pvpBattleInfo, curDate);

			final Map<Integer, Map<String, Integer>> changeMap =
			    new HashMap<Integer, Map<String, Integer>>();
			final Map<Integer, Map<String, Integer>> previousCurrencyMap =
			    new HashMap<Integer, Map<String, Integer>>();
			boolean successful = false;
			if (legit) {
				// it is possible that the defender has a shield, most likely
				// via
				// buying it, and less likely locks didn't work, regardless, the
				// user can have a shield
				successful =
				    writeChangesToDb(attacker, attackerId, attackerPlfu, defender, defenderId,
				        defenderPlfu, pvpBattleInfo, oilStolen, cashStolen, curTime, curDate,
				        attackerAttacked, attackerWon, attackerMaxOil, attackerMaxCash,
				        changeMap, previousCurrencyMap);
			}

			if (successful) {
				resBuilder.setStatus(EndPvpBattleStatus.SUCCESS);
			}

			// respond to the attacker
			resEvent.setEndPvpBattleResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in EndPvpBattleController.processRequestEvent", e);
			}

			if (successful) {
				// respond to the defender
				final EndPvpBattleResponseEvent resEventDefender =
				    new EndPvpBattleResponseEvent(defenderId);
				resEvent.setTag(0);
				resEventDefender.setEndPvpBattleResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEventDefender);
				try {
					eventWriter.writeEvent(resEventDefender);
				} catch (final Throwable e) {
					LOG.error("fatal exception in EndPvpBattleController.processRequestEvent",
					    e);
				}

				// regardless of whether the attacker won, his elo will change
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(
				        attacker, attackerPlfu);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error("fatal exception in EndPvpBattleController.processRequestEvent",
					    e);
				}

				// defender's elo and resources changed only if attacker won,
				// and defender is real
				if (attackerWon
				    && (null != defender)) {
					final UpdateClientUserResponseEvent resEventUpdateDefender =
					    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(
					        defender, defenderPlfu);
					resEventUpdate.setTag(event.getTag());
					// write to client
					LOG.info("Writing event: "
					    + resEventUpdateDefender);
					try {
						eventWriter.writeEvent(resEventUpdateDefender);
					} catch (final Throwable e) {
						LOG.error(
						    "fatal exception in EndPvpBattleController.processRequestEvent", e);
					}
				}
				// TRACK CURRENCY HISTORY
				writeToUserCurrencyHistory(attackerId, attacker, defenderId, defender,
				    attackerWon, curTime, changeMap, previousCurrencyMap);
			}

		} catch (final Exception e) {
			LOG.error("exception in EndPvpBattleController processEvent", e);
			// don't let the client hang
			try {
				resEvent.setEndPvpBattleResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in EndPvpBattleController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in EndPvpBattleController processEvent", e);
			}

		} finally {
			if (0 != defenderId) {
				getLocker().unlockPlayers(defenderId, attackerId, this.getClass()
				    .getSimpleName());
				LOG.info("unlocked defender and attacker");
			} else {
				getLocker().unlockPlayer(attackerId, this.getClass()
				    .getSimpleName());
				LOG.info("unlocked attacker");
			}
		}
	}

	/*
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value.
	 */
	private boolean checkLegit( final Builder resBuilder, final User attacker,
	    final User defender, final PvpBattleForUser pvpInfo, final Date curDate )
	{

		if (null == pvpInfo) {
			LOG.error("unexpected error: no battle exists for the attacker. attacker="
			    + attacker
			    + "\t defender="
			    + defender);
			return false;
		}

		// if the battle has finished one hour after the battle started, don't
		// count it
		final long nowMillis = curDate.getTime();
		final long battleEndTime = pvpInfo.getBattleStartTime()
		    .getTime()
		    + ControllerConstants.PVP__MAX_BATTLE_DURATION_MILLIS;
		if (nowMillis > battleEndTime) {
			resBuilder.setStatus(EndPvpBattleStatus.FAIL_BATTLE_TOOK_TOO_LONG);
			LOG.error("the client took too long to finish a battle. pvpInfo="
			    + pvpInfo
			    + "\t now="
			    + curDate);
			return false;
		}

		// if (0 == defenderUserId || !userAttacked) {
		// //if fake user, or user didn't attack just allow this to happen
		// return true;
		// }

		return true;
	}

	// the attackerPlfu and the defenderPlfu will be modified
	private boolean writeChangesToDb( final User attacker, final int attackerId,
	    final PvpLeagueForUser attackerPlfu, final User defender, final int defenderId,
	    final PvpLeagueForUser defenderPlfu, final PvpBattleForUser pvpBattleInfo,
	    final int oilStolen, final int cashStolen, final Timestamp clientTime,
	    final Date clientDate, final boolean attackerAttacked, final boolean attackerWon,
	    final int attackerMaxOil, final int attackerMaxCash,
	    final Map<Integer, Map<String, Integer>> changeMap,
	    final Map<Integer, Map<String, Integer>> previousCurrencyMap )
	{

		final boolean cancelled = !attackerAttacked;

		if (cancelled) {
			LOG.info("battle cancelled");
			// this means that the only thing that changes is defenderOpu's
			// inBattleShieldEndTime
			// just change it so its not in the future
			processCancellation(attacker, attackerId, attackerPlfu, defender, defenderId,
			    defenderPlfu, pvpBattleInfo, clientTime, attackerWon, cancelled);

		} else {
			// user attacked so either he won or lost
			final int previousCash = attacker.getCash();
			final int previousOil = attacker.getOil();

			// TODO: WHEN MAX ELO IS FIGURED OUT, MAKE SURE ELO DOESN'T GO ABOVE
			// THAT
			// these elo change lists are populated by getEloChanges(...)
			final List<Integer> attackerEloChangeList = new ArrayList<Integer>();
			final List<Integer> defenderEloChangeList = new ArrayList<Integer>();
			getEloChanges(attacker, attackerPlfu, defender, defenderPlfu, attackerWon,
			    pvpBattleInfo, attackerEloChangeList, defenderEloChangeList);

			final int attackerEloChange = attackerEloChangeList.get(0); // already
																		// pos
			// or neg
			final int defenderEloChange = defenderEloChangeList.get(0); // already
																		// pos
			// or neg
			final int attackerCashChange =
			    calculateMaxCashChange(attacker, attackerMaxCash, cashStolen, attackerWon);
			final int attackerOilChange =
			    calculateMaxOilChange(attacker, attackerMaxOil, oilStolen, attackerWon);

			final PvpLeagueForUser attackerPrevPlfu = new PvpLeagueForUser(attackerPlfu);
			// attackerPlfu will be updated
			updateAttacker(attacker, attackerId, attackerPlfu, attackerCashChange,
			    attackerOilChange, attackerEloChange, attackerWon);

			// need to take into account if defender (online and) spent some
			// cash/oil before
			// pvp battle ends, defender will not gain resources by winning
			// made lists so it can hold the values and be shared among methods
			final List<Integer> defenderOilChangeList = new ArrayList<Integer>();
			final List<Integer> defenderCashChangeList = new ArrayList<Integer>();
			final List<Boolean> displayToDefenderList = new ArrayList<Boolean>();

			// could have attacked a fake person
			PvpLeagueForUser defenderPrevPlfu = null;
			if (null != defenderPlfu) {
				defenderPrevPlfu = new PvpLeagueForUser(defenderPlfu);
			}
			// defender could be fake user, in which case no change is made to
			// defender
			// no change could still be made if the defender is already under
			// attack by another
			// and this attacker is the second guy attacking the defender
			updateDefender(attacker, defender, defenderId, defenderPlfu, pvpBattleInfo,
			    defenderEloChange, cashStolen, oilStolen, clientDate, attackerWon,
			    defenderEloChangeList, defenderOilChangeList, defenderCashChangeList,
			    displayToDefenderList, changeMap, previousCurrencyMap);

			writePvpBattleHistoryNotcancelled(attackerId, attackerPrevPlfu, attackerPlfu,
			    defenderId, defenderPrevPlfu, defenderPlfu, pvpBattleInfo, clientTime,
			    attackerWon, attackerCashChange, attackerOilChange, attackerEloChange,
			    defenderEloChange, defenderOilChangeList, defenderCashChangeList,
			    displayToDefenderList, cancelled);

			// user currency stuff
			final Map<String, Integer> attackerChangeMap = new HashMap<String, Integer>();
			attackerChangeMap.put(MiscMethods.cash, attackerCashChange);
			attackerChangeMap.put(MiscMethods.oil, attackerOilChange);
			changeMap.put(attackerId, attackerChangeMap);

			final Map<String, Integer> attackerPreviousCurrency =
			    new HashMap<String, Integer>();
			attackerPreviousCurrency.put(MiscMethods.cash, previousCash);
			attackerPreviousCurrency.put(MiscMethods.oil, previousOil);
			previousCurrencyMap.put(attackerId, attackerPreviousCurrency);
		}

		LOG.info("deleting from PvpBattleForUser");
		// need to delete PvpBattleForUser
		final int numDeleted = DeleteUtils.get()
		    .deletePvpBattleForUser(attackerId);
		LOG.info("numDeleted (should be 1): "
		    + numDeleted);

		return true;
	}

	private void processCancellation( final User attacker, final int attackerId,
	    final PvpLeagueForUser attackerPlfu, final User defender, final int defenderId,
	    final PvpLeagueForUser defenderPlfu, final PvpBattleForUser pvpBattleInfo,
	    final Timestamp clientTime, final boolean attackerWon, final boolean cancelled )
	{

		if ((null != defender)
		    && (defenderPlfu.getInBattleShieldEndTime()
		        .getTime() > clientTime.getTime())) {
			// since real player and "battle end time" is after now, change it
			// so defender can be attackable again
			defenderPlfu.setInBattleShieldEndTime(defenderPlfu.getShieldEndTime());
			final PvpUser defenderOpu = new PvpUser(defenderPlfu);
			getHazelcastPvpUtil().replacePvpUser(defenderOpu, defenderId);
			LOG.info("changed battleEndTime to shieldEndTime. defenderPvpUser="
			    + defenderOpu);
		}
		final int attackerEloBefore = attackerPlfu.getElo();
		final int attackerPrevLeague = attackerPlfu.getPvpLeagueId();
		final int attackerPrevRank = attackerPlfu.getRank();

		int defenderEloBefore = 0;
		int defenderPrevLeague = 0;
		int defenderPrevRank = 0;
		if (null != defenderPlfu) {
			defenderEloBefore = defenderPlfu.getElo();
			defenderPrevLeague = defenderPlfu.getPvpLeagueId();
			defenderPrevRank = defenderPlfu.getRank();
		}

		// since battle cancelled, nothing should have changed
		LOG.info("writing to pvp history that user cancelled battle");
		writePvpBattleHistory(attackerId, attackerEloBefore, defenderId, defenderEloBefore,
		    attackerPrevLeague, attackerPrevLeague, defenderPrevLeague, defenderPrevLeague,
		    attackerPrevRank, attackerPrevRank, defenderPrevRank, defenderPrevRank, clientTime,
		    pvpBattleInfo, 0, 0, 0, 0, 0, 0, attackerWon, cancelled, false, false);
	}

	// cashChange is non negative number,
	// returns a signed number representing oilChange
	private int calculateMaxCashChange( final User user, final int maxCash,
	    final int cashChange, final boolean userWon )
	{
		if (null == user) {
			LOG.info("calculateMaxCashChange user is null! cashChange=0");
			// this is for fake user
			return 0;
		}
		// if user somehow has more than max cash, first treat user as having
		// max cash,
		// figure out the amount he gains and then subtract, the extra cash he
		// had
		final int userCash = user.getCash();
		final int amountOverMax =
		    calculateAmountOverMaxResource(user, userCash, maxCash, MiscMethods.cash);
		LOG.info("calculateMaxCashChange amount over max="
		    + amountOverMax);

		if (userWon) {
			LOG.info("calculateMaxCashChange userWon!. user="
			    + user);
			final int curCash = Math.min(user.getCash(), maxCash); // in case
																   // user's
			// cash is more
			// than maxOil.
			LOG.info("calculateMaxCashChange curCash="
			    + curCash);
			final int maxCashUserCanGain = maxCash
			    - curCash;
			LOG.info("calculateMaxCashChange  maxCashUserCanGain="
			    + maxCashUserCanGain);
			final int maxCashChange = Math.min(cashChange, maxCashUserCanGain);
			LOG.info("calculateMaxCashChange maxCashChange="
			    + maxCashChange);

			// IF USER IS ABOVE maxCash, need to drag him down to maxCash
			final int actualCashChange = maxCashChange
			    - amountOverMax;
			LOG.info("calculateMaxCashChange  actualCashChange="
			    + actualCashChange);
			return actualCashChange;

		} else {
			LOG.info("calculateMaxCashChange userLost!. user="
			    + user);

			final int maxCashUserCanLose = Math.min(user.getCash(), maxCash);
			// always non negative number
			final int maxCashChange = Math.min(cashChange, maxCashUserCanLose);

			final int actualCashChange = -1
			    * (amountOverMax + maxCashChange);
			LOG.info("calculateMaxCashChange  actualCashChange="
			    + actualCashChange);
			return actualCashChange;
		}
	}

	// oilChange is positive number,
	// returns a signed number representing oilChange
	private int calculateMaxOilChange( final User user, final int maxOil, final int oilChange,
	    final boolean userWon )
	{
		if (null == user) {
			LOG.info("calculateMaxOilChange user is null! oilChange=0");
			// this is for fake user
			return 0;
		}

		// if user somehow has more than max oil, first treat user as having max
		// oil,
		// figure out the amount he gains and then subtract, the extra oil he
		// had
		final int userOil = user.getOil();
		final int amountOverMax =
		    calculateAmountOverMaxResource(user, userOil, maxOil, MiscMethods.oil);
		LOG.info("calculateMaxOilChange amount over max="
		    + amountOverMax);

		if (userWon) {
			LOG.info("calculateMaxOilChange userWon!. user="
			    + user);
			final int curOil = Math.min(user.getOil(), maxOil); // in case
																// user's oil
			// is more than
			// maxOil.
			LOG.info("calculateAmountOverMaxOil curOil="
			    + curOil);
			final int maxOilUserCanGain = maxOil
			    - curOil;
			LOG.info("calculateAmountOverMaxOil  maxOilUserCanGain="
			    + maxOilUserCanGain);
			final int maxOilChange = Math.min(oilChange, maxOilUserCanGain);
			LOG.info("calculateAmountOverMaxOil maxOilChange="
			    + maxOilChange);

			// IF USER IS ABOVE maxOil, need to drag him down to maxOil
			final int actualOilChange = maxOilChange
			    - amountOverMax;
			LOG.info("calculateAmountOverMaxOil  actualOilChange="
			    + actualOilChange);
			return actualOilChange;

		} else {
			LOG.info("calculateAmountOverMaxOil userLost!. user="
			    + user);
			final int maxOilUserCanLose = Math.min(user.getOil(), maxOil);
			// always a nonnegative number
			final int maxOilChange = Math.min(oilChange, maxOilUserCanLose);

			final int actualOilChange = -1
			    * (amountOverMax + maxOilChange);
			LOG.info("calculateAmountOverMaxOil  actualOilChange="
			    + actualOilChange);
			return actualOilChange;
		}
	}

	private int calculateAmountOverMaxResource( final User u, final int userResource,
	    final int maxResource, final String resource )
	{
		LOG.info("calculateAmountOverMaxResource resource="
		    + resource);
		int resourceLoss = 0;
		if (userResource > maxResource) {
			// if (u.isAdmin()) {
			// LOG.info("alright for user to have more than maxResource." +
			// " user is admin. user=" + u + "\t maxResource=" + maxResource);
			// } else {
			LOG.info("wtf!!!!! user has more than max cash! user="
			    + u
			    + "\t cutting him down to maxResource="
			    + maxResource);
			resourceLoss = userResource
			    - maxResource;
			// }
		}
		return resourceLoss;
	}

	// calculate how much elo changes for the attacker and defender
	// based on whether the attacker won, capping min elo at 0 for attacker &
	// defender
	private void getEloChanges( final User attacker, final PvpLeagueForUser attackerPlfu,
	    final User defender, final PvpLeagueForUser defenderPlfu, final boolean attackerWon,
	    final PvpBattleForUser pvpBattleInfo, final List<Integer> attackerEloChangeList,
	    final List<Integer> defenderEloChangeList )
	{
		// temp variables
		int attackerEloChange = 0;
		int defenderEloChange = 0;

		if (attackerWon) {
			LOG.info("getEloChanges attacker won.");

			attackerEloChange = pvpBattleInfo.getAttackerWinEloChange(); // positive
			                                                             // value
			defenderEloChange = pvpBattleInfo.getDefenderLoseEloChange(); // negative
			                                                              // value

			// don't cap fake player's elo
			if ((null != defender)
			    && (pvpBattleInfo.getDefenderId() > 0)) {
				LOG.info("getEloChanges attacker fought real player. battleInfo="
				    + pvpBattleInfo);

				// make sure defender's elo doesn't go below 0
				defenderEloChange = capPlayerMinimumElo(defenderPlfu, defenderEloChange);
			} else {
				LOG.info("getEloChanges attacker fought fake player. battleInfo="
				    + pvpBattleInfo);
			}

			LOG.info("getEloChanges attackerEloChange="
			    + attackerEloChange);
			LOG.info("getEloChanges defenderEloChange="
			    + defenderEloChange);

		} else {
			LOG.info("getEloChanges attacker lost.");
			attackerEloChange = pvpBattleInfo.getAttackerLoseEloChange(); // negative
			                                                              // value
			defenderEloChange = pvpBattleInfo.getDefenderWinEloChange(); // positive
			                                                             // value

			// make sure attacker's elo doesn't go below 0
			attackerEloChange = capPlayerMinimumElo(attackerPlfu, attackerEloChange);

		}

		attackerEloChangeList.add(attackerEloChange);
		defenderEloChangeList.add(defenderEloChange);

	}

	private int capPlayerMinimumElo( final PvpLeagueForUser playerPlfu, int playerEloChange )
	{
		final int playerElo = playerPlfu.getElo();
		LOG.info("capPlayerMinimumElo plfu="
		    + playerPlfu
		    + "\t eloChange"
		    + playerEloChange);
		if ((playerElo + playerEloChange) < 0) {
			LOG.info("capPlayerMinimumElo player loses more elo than has atm. playerElo="
			    + playerElo
			    + "\t playerEloChange="
			    + playerEloChange);
			playerEloChange = -1
			    * playerElo;
		}

		LOG.info("capPlayerMinimumElo updated playerEloChange="
		    + playerEloChange);
		return playerEloChange;
	}

	private void updateAttacker( final User attacker, final int attackerId,
	    final PvpLeagueForUser attackerPlfu, final int attackerCashChange,
	    final int attackerOilChange, final int attackerEloChange, final boolean attackerWon )
	{

		// update attacker's cash, oil, elo
		if ((0 != attackerOilChange)
		    || (0 != attackerEloChange)) {
			LOG.info("attacker before currency update: "
			    + attacker);
			final int numUpdated =
			    attacker.updateRelativeCashAndOilAndGems(attackerCashChange, attackerOilChange,
			        0);
			LOG.info("attacker after currency update: "
			    + attacker);
			LOG.info("num updated when changing attacker's currency="
			    + numUpdated);
		}

		LOG.info("attacker PvpLeagueForUser before battle outcome:"
		    + attackerPlfu);
		final int prevElo = attackerPlfu.getElo();
		final int attackerPrevLeague = attackerPlfu.getPvpLeagueId();
		int attacksWon = attackerPlfu.getAttacksWon();
		int attacksLost = attackerPlfu.getAttacksLost();

		int attacksWonDelta = 0;
		final int defensesWonDelta = 0;
		int attacksLostDelta = 0;
		final int defensesLostDelta = 0;

		if (attackerWon) {
			attacksWonDelta = 1;
			attacksWon += attacksWonDelta;
		} else {
			attacksLostDelta = 1;
			attacksLost += attacksLostDelta;
		}

		final int curElo = prevElo
		    + attackerEloChange;
		final int attackerCurLeague =
		    PvpLeagueRetrieveUtils.getLeagueIdForElo(curElo, false, attackerPrevLeague);
		final int attackerCurRank =
		    PvpLeagueRetrieveUtils.getRankForElo(curElo, attackerCurLeague);

		// don't update his shields
		final int numUpdated =
		    UpdateUtils.get()
		        .updatePvpLeagueForUser(attackerId, attackerCurLeague, attackerCurRank,
		            attackerEloChange, null, null, attacksWonDelta, defensesWonDelta,
		            attacksLostDelta, defensesLostDelta);
		LOG.info("num updated when changing attacker's elo="
		    + numUpdated);

		// modify object to return back to user
		attackerPlfu.setElo(curElo);
		attackerPlfu.setPvpLeagueId(attackerCurLeague);
		attackerPlfu.setRank(attackerCurRank);
		attackerPlfu.setAttacksWon(attacksWon);
		attackerPlfu.setAttacksLost(attacksLost);

		// update hazelcast's object
		final PvpUser attackerPu = new PvpUser(attackerPlfu);
		getHazelcastPvpUtil().replacePvpUser(attackerPu, attackerId);
		LOG.info("attacker PvpLeagueForUser after battle outcome:"
		    + attackerPlfu);
	}

	// the elo, oil, cash, display lists are return values
	private void updateDefender( final User attacker, final User defender,
	    final int defenderId, final PvpLeagueForUser defenderPlfu,
	    final PvpBattleForUser pvpBattleInfo, int defenderEloChange, final int oilStolen,
	    final int cashStolen, final Date clientDate, final boolean attackerWon,
	    final List<Integer> defenderEloChangeList, final List<Integer> defenderOilChangeList,
	    final List<Integer> defenderCashChangeList, final List<Boolean> displayToDefenderList,
	    final Map<Integer, Map<String, Integer>> changeMap,
	    final Map<Integer, Map<String, Integer>> previousCurrencyMap )
	{
		if (null == defender) {
			LOG.info("attacker attacked fake defender. attacker="
			    + attacker);
			defenderEloChangeList.clear();
			defenderEloChangeList.add(0);
			defenderOilChangeList.add(0);
			defenderCashChangeList.add(0);
			displayToDefenderList.add(false);
			return;
		}
		// processing real user data
		final int previousCash = defender.getCash();
		final int previousOil = defender.getOil();

		final boolean defenderWon = !attackerWon;
		int defenderCashChange =
		    calculateMaxCashChange(defender, defender.getCash(), cashStolen, defenderWon);
		int defenderOilChange =
		    calculateMaxOilChange(defender, defender.getOil(), oilStolen, defenderWon);
		boolean displayToDefender = true;

		// if DEFENDER HAS SHIELD THEN DEFENDER SHOULD NOT BE PENALIZED, and
		// the history for this battle should have the display_to_defender set
		// to false;
		final Date shieldEndTime = defenderPlfu.getShieldEndTime();
		if (getTimeUtils().isFirstEarlierThanSecond(clientDate, shieldEndTime)) {
			LOG.warn("some how attacker attacked a defender with a shield!! pvpBattleInfo="
			    + pvpBattleInfo
			    + "\t attacker="
			    + attacker
			    + "\t defender="
			    + defender);
			defenderCashChange = 0;
			defenderOilChange = 0;
			defenderEloChange = 0;
			displayToDefender = false;

		} else {
			LOG.info("penalizing/rewarding for losing/winning. defenderWon="
			    + defenderWon);
			updateUnshieldedDefender(attacker, defenderId, defender, defenderPlfu,
			    shieldEndTime, pvpBattleInfo, clientDate, attackerWon, defenderEloChange,
			    defenderCashChange, defenderOilChange);
		}

		defenderEloChangeList.add(defenderEloChange);
		defenderCashChangeList.add(defenderCashChange);
		defenderOilChangeList.add(defenderOilChange);
		displayToDefenderList.add(displayToDefender);

		// user currency stuff
		final Map<String, Integer> defenderChangeMap = new HashMap<String, Integer>();
		defenderChangeMap.put(MiscMethods.cash, defenderCashChange);
		defenderChangeMap.put(MiscMethods.oil, defenderOilChange);
		changeMap.put(defenderId, defenderChangeMap);

		final Map<String, Integer> defenderPreviousCurrency = new HashMap<String, Integer>();
		defenderPreviousCurrency.put(MiscMethods.cash, previousCash);
		defenderPreviousCurrency.put(MiscMethods.oil, previousOil);
		previousCurrencyMap.put(defenderId, defenderPreviousCurrency);
	}

	private void updateUnshieldedDefender( final User attacker, final int defenderId,
	    final User defender, final PvpLeagueForUser defenderPlfu, Date defenderShieldEndTime,
	    final PvpBattleForUser pvpBattleInfo, final Date clientDate, final boolean attackerWon,
	    final int defenderEloChange, final int defenderCashChange, final int defenderOilChange )
	{
		// NO ONE ELSE ATTACKING DEFENDER. update defender's cash, oil, elo and
		// shields
		LOG.info("attacker attacked unshielded defender. attacker="
		    + attacker
		    + "\t defender="
		    + defender
		    + "\t battleInfo="
		    + pvpBattleInfo);

		// old info before battle
		final int prevElo = defenderPlfu.getElo();
		final int prevPvpLeague = defenderPlfu.getPvpLeagueId();
		int defensesLost = defenderPlfu.getDefensesLost();
		int defensesWon = defenderPlfu.getDefensesWon();

		final int attacksWonDelta = 0;
		int defensesWonDelta = 0;
		final int attacksLostDelta = 0;
		int defensesLostDelta = 0;

		// if attacker won then defender money would need to be updated
		if (attackerWon) {
			LOG.info("updateUnshieldedDefender  defender before currency update:"
			    + defender);
			final int numUpdated =
			    defender.updateRelativeCashAndOilAndGems(defenderCashChange, defenderOilChange,
			        0);
			LOG.info("updateUnshieldedDefender num updated when changing defender's"
			    + " currency="
			    + numUpdated);
			LOG.info("updateUnshieldedDefender  defender after currency update:"
			    + defender);

			final int hoursAddend = ControllerConstants.PVP__LOST_BATTLE_SHIELD_DURATION_HOURS;
			defenderShieldEndTime = getTimeUtils().createDateAddHours(clientDate, hoursAddend);
			defensesLostDelta = 1;
			defensesLost += defensesLostDelta;

		} else {
			LOG.info("updateUnshieldedDefender defender won!");
			defensesWonDelta = 1;
			defensesWon += defensesWonDelta;
		}
		LOG.info("updateUnshieldedDefender defender PvpLeagueForUser before battle outcome:"
		    + defenderPlfu);

		// regardless if user won or lost, this is shield time
		final Date inBattleShieldEndTime = defenderShieldEndTime;

		final int curElo = prevElo
		    + defenderEloChange;
		final int curPvpLeague =
		    PvpLeagueRetrieveUtils.getLeagueIdForElo(curElo, false, prevPvpLeague);
		final int curRank = PvpLeagueRetrieveUtils.getRankForElo(curElo, curPvpLeague);

		// update pvp stuff: elo most likely changed, shields might have if
		// attackerWon
		final Timestamp shieldEndTimestamp = new Timestamp(defenderShieldEndTime.getTime());
		final Timestamp inBattleTimestamp = new Timestamp(inBattleShieldEndTime.getTime());
		final int numUpdated =
		    UpdateUtils.get()
		        .updatePvpLeagueForUser(defenderId, curPvpLeague, curRank, defenderEloChange,
		            shieldEndTimestamp, inBattleTimestamp, attacksWonDelta, defensesWonDelta,
		            attacksLostDelta, defensesLostDelta);

		LOG.info("num updated when changing defender's elo="
		    + numUpdated);

		// modify object to return back to user
		defenderPlfu.setShieldEndTime(defenderShieldEndTime);
		defenderPlfu.setInBattleShieldEndTime(inBattleShieldEndTime);

		defenderPlfu.setElo(curElo);
		defenderPlfu.setPvpLeagueId(curPvpLeague);
		defenderPlfu.setRank(curRank);
		defenderPlfu.setDefensesLost(defensesLost);
		defenderPlfu.setDefensesWon(defensesWon);

		// update hazelcast's object
		final PvpUser defenderPu = new PvpUser(defenderPlfu);
		getHazelcastPvpUtil().replacePvpUser(defenderPu, defenderId);
		LOG.info("defender PvpLeagueForUser after battle outcome:"
		    + defenderPlfu);
	}

	// new method created so as to reduce clutter in calling method
	private void writePvpBattleHistory( final int attackerId, final int attackerEloBefore,
	    final int defenderId, final int defenderEloBefore, final int attackerPrevLeague,
	    final int attackerCurLeague, final int defenderPrevLeague, final int defenderCurLeague,
	    final int attackerPrevRank, final int attackerCurRank, final int defenderPrevRank,
	    final int defenderCurRank, final Timestamp endTime,
	    final PvpBattleForUser pvpBattleInfo, final int attackerEloChange,
	    final int defenderEloChange, final int attackerOilChange, final int defenderOilChange,
	    final int attackerCashChange, final int defenderCashChange, final boolean attackerWon,
	    final boolean cancelled, final boolean gotRevenge, final boolean displayToDefender )
	{

		final Date startDate = pvpBattleInfo.getBattleStartTime();
		final Timestamp battleStartTime = new Timestamp(startDate.getTime());
		final int numInserted =
		    InsertUtils.get()
		        .insertIntoPvpBattleHistory(attackerId, defenderId, endTime, battleStartTime,
		            attackerEloChange, attackerEloBefore, defenderEloChange, defenderEloBefore,
		            attackerPrevLeague, attackerCurLeague, defenderPrevLeague,
		            defenderCurLeague, attackerPrevRank, attackerCurRank, defenderPrevRank,
		            defenderCurRank, attackerOilChange, defenderOilChange, attackerCashChange,
		            defenderCashChange, attackerWon, cancelled, gotRevenge, displayToDefender);
		LOG.info("num inserted into history="
		    + numInserted);
	}

	// new method created so as to reduce clutter in calling method
	private void writePvpBattleHistoryNotcancelled( final int attackerId,
	    final PvpLeagueForUser attackerPrevPlfu, final PvpLeagueForUser attackerPlfu,
	    final int defenderId, final PvpLeagueForUser defenderPrevPlfu,
	    final PvpLeagueForUser defenderPlfu, final PvpBattleForUser pvpBattleInfo,
	    final Timestamp clientTime, final boolean attackerWon, final int attackerCashChange,
	    final int attackerOilChange, final int attackerEloChange, final int defenderEloChange,
	    final List<Integer> defenderOilChangeList, final List<Integer> defenderCashChangeList,
	    final List<Boolean> displayToDefenderList, final boolean cancelled )
	{

		final int attackerEloBefore = attackerPrevPlfu.getElo();
		final int attackerPrevLeague = attackerPrevPlfu.getPvpLeagueId();
		final int attackerPrevRank = attackerPrevPlfu.getRank();
		final int attackerCurLeague = attackerPlfu.getPvpLeagueId();
		final int attackerCurRank = attackerPlfu.getRank();

		int defenderEloBefore = 0;
		int defenderPrevLeague = 0;
		int defenderPrevRank = 0;
		int defenderCurLeague = 0;
		int defenderCurRank = 0;

		// user could have fought a fake person
		if (null != defenderPrevPlfu) {
			defenderEloBefore = defenderPrevPlfu.getElo();
			defenderPrevLeague = defenderPrevPlfu.getPvpLeagueId();
			defenderPrevRank = defenderPrevPlfu.getRank();

			defenderCurLeague = defenderPlfu.getPvpLeagueId();
			defenderCurRank = defenderPlfu.getRank();
		}

		final int defenderOilChange = defenderOilChangeList.get(0);
		final int defenderCashChange = defenderCashChangeList.get(0);
		final boolean displayToDefender = displayToDefenderList.get(0);

		LOG.info("writing to pvp history that user finished battle");
		LOG.info("attackerEloChange="
		    + attackerEloChange
		    + "\t defenderEloChange="
		    + defenderEloChange
		    + "\t attackerOilChange="
		    + attackerOilChange
		    + "\t defenderOilChange="
		    + defenderOilChange
		    + "\t attackerCashChange="
		    + attackerCashChange
		    + "\t defenderCashChange="
		    + defenderCashChange);
		writePvpBattleHistory(attackerId, attackerEloBefore, defenderId, defenderEloBefore,
		    attackerPrevLeague, attackerCurLeague, defenderPrevLeague, defenderCurLeague,
		    attackerPrevRank, attackerCurRank, defenderPrevRank, defenderCurRank, clientTime,
		    pvpBattleInfo, attackerEloChange, defenderEloChange, attackerOilChange,
		    defenderOilChange, attackerCashChange, defenderCashChange, attackerWon, cancelled,
		    false, displayToDefender);
	}

	private void writeToUserCurrencyHistory( final int attackerId, final User attacker,
	    final int defenderId, final User defender, final boolean attackerWon,
	    final Timestamp curTime, final Map<Integer, Map<String, Integer>> changeMap,
	    final Map<Integer, Map<String, Integer>> previousCurrencyMap )
	{

		final Map<Integer, Map<String, Integer>> currentCurrencyMap =
		    new HashMap<Integer, Map<String, Integer>>();
		final Map<Integer, Map<String, String>> changeReasonsMap =
		    new HashMap<Integer, Map<String, String>>();
		final Map<Integer, Map<String, String>> detailsMap =
		    new HashMap<Integer, Map<String, String>>();
		final String reasonForChange = ControllerConstants.UCHRFC__PVP_BATTLE;
		final String oil = MiscMethods.oil;
		final String cash = MiscMethods.cash;

		// reasons
		final Map<String, String> reasonMap = new HashMap<String, String>();
		reasonMap.put(cash, reasonForChange);
		reasonMap.put(oil, reasonForChange);
		changeReasonsMap.put(attackerId, reasonMap);
		changeReasonsMap.put(defenderId, reasonMap);

		// attacker stuff
		// current currency stuff
		final int attackerCash = attacker.getCash();
		final int attackerOil = attacker.getOil();
		final Map<String, Integer> attackerCurrency = new HashMap<String, Integer>();
		attackerCurrency.put(cash, attackerCash);
		attackerCurrency.put(oil, attackerOil);
		// aggregate currency
		currentCurrencyMap.put(attackerId, attackerCurrency);

		// details
		final StringBuilder attackerDetailsSb = new StringBuilder();
		if (attackerWon) {
			attackerDetailsSb.append("beat ");
		} else {
			attackerDetailsSb.append("lost to ");
		}
		attackerDetailsSb.append(defenderId);
		final String attackerDetails = attackerDetailsSb.toString();
		final Map<String, String> attackerDetailsMap = new HashMap<String, String>();
		attackerDetailsMap.put(cash, attackerDetails);
		attackerDetailsMap.put(oil, attackerDetails);
		// aggregate details
		detailsMap.put(attackerId, attackerDetailsMap);

		// defender stuff
		if (null != defender) {
			// current currency stuff
			final int defenderCash = defender.getCash();
			final int defenderOil = defender.getOil();
			final Map<String, Integer> defenderCurrency = new HashMap<String, Integer>();
			defenderCurrency.put(cash, defenderCash);
			defenderCurrency.put(oil, defenderOil);
			// aggregate currency
			currentCurrencyMap.put(defenderId, defenderCurrency);

			// details
			final StringBuilder defenderDetailsSb = new StringBuilder();
			if (attackerWon) {
				defenderDetailsSb.append("lost to ");
			} else {
				defenderDetailsSb.append("beat ");
			}
			defenderDetailsSb.append(attackerId);
			final String defenderDetails = defenderDetailsSb.toString();
			final Map<String, String> defenderDetailsMap = new HashMap<String, String>();
			defenderDetailsMap.put(cash, defenderDetails);
			defenderDetailsMap.put(oil, defenderDetails);
			// aggregate details
			detailsMap.put(defenderId, defenderDetailsMap);

		}

		final List<Integer> userUuids = new ArrayList<Integer>();
		userUuids.add(attackerId);
		userUuids.add(defenderId);
		MiscMethods.writeToUserCurrencyUsers(userUuids, curTime, changeMap,
		    previousCurrencyMap, currentCurrencyMap, changeReasonsMap, detailsMap);

	}

	public HazelcastPvpUtil getHazelcastPvpUtil()
	{
		return hazelcastPvpUtil;
	}

	public void setHazelcastPvpUtil( final HazelcastPvpUtil hazelcastPvpUtil )
	{
		this.hazelcastPvpUtil = hazelcastPvpUtil;
	}

	public TimeUtils getTimeUtils()
	{
		return timeUtils;
	}

	public void setTimeUtils( final TimeUtils timeUtils )
	{
		this.timeUtils = timeUtils;
	}

	public PvpLeagueForUserRetrieveUtil getPvpLeagueForUserRetrieveUtil()
	{
		return pvpLeagueForUserRetrieveUtil;
	}

	public void setPvpLeagueForUserRetrieveUtil(
	    final PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil )
	{
		this.pvpLeagueForUserRetrieveUtil = pvpLeagueForUserRetrieveUtil;
	}
}
