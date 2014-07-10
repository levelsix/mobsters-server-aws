package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.TimeUtils;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpRequestProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpResponseProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpResponseProto.QueueUpStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.QueueUpRequestEvent;
import com.lvl6.mobsters.events.response.QueueUpResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.info.MonsterForPvp;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventPvpProto.PvpProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class QueueUpController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(QueueUpController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected HazelcastPvpUtil hazelcastPvpUtil;

	@Autowired
	protected MonsterForPvpRetrieveUtils monsterForPvpRetrieveUtils;

	@Autowired
	protected TimeUtils timeUtils;

	@Autowired
	protected PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil;

	// @Autowired
	// protected PvpUtil pvpUtil;
	//
	// public PvpUtil getPvpUtil() {
	// return pvpUtil;
	// }
	//
	// public void setPvpUtil(PvpUtil pvpUtil) {
	// this.pvpUtil = pvpUtil;
	// }

	public QueueUpController()
	{
		numAllocatedThreads = 10;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new QueueUpRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_QUEUE_UP_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final QueueUpRequestProto reqProto =
		    ((QueueUpRequestEvent) event).getQueueUpRequestProto();

		LOG.info("reqProto="
		    + reqProto);

		final MinimumUserProto attackerProto = reqProto.getAttacker();
		final int attackerId = attackerProto.getUserUuid();
		final int attackerElo = reqProto.getAttackerElo();

		// positive means refund, negative means charge user; don't forsee being
		// positive
		// int gemsSpent = reqProto.getGemsSpent();
		// positive means refund, negative means charge user
		// int cashChange = reqProto.getCashChange();

		final List<Integer> seenUserUuids = reqProto.getSeenUserUuidsList();
		final Set<Integer> uniqSeenUserUuids = new HashSet<Integer>(seenUserUuids);
		// don't want the attacker to see himself
		uniqSeenUserUuids.add(attackerId);

		final Date clientDate = new Date(reqProto.getClientTime());
		final Timestamp clientTime = new Timestamp(clientDate.getTime());

		// set some values to send to the client
		final QueueUpResponseProto.Builder resBuilder = QueueUpResponseProto.newBuilder();
		resBuilder.setAttacker(attackerProto);
		resBuilder.setStatus(QueueUpStatus.FAIL_OTHER);

		try {
			final User attacker = RetrieveUtils.userRetrieveUtils()
			    .getUserById(attackerId);
			final PvpLeagueForUser plfu =
			    getPvpLeagueForUserRetrieveUtil().getUserPvpLeagueForId(attackerId);
			// check if user can search for a player to attack
			final boolean legitQueueUp = checkLegitQueueUp(resBuilder, attacker, clientDate);
			// gemsSpent, cashChange);

			boolean success = false;
			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
			if (legitQueueUp) {
				setProspectivePvpMatches(resBuilder, attacker, uniqSeenUserUuids, clientDate,
				    attackerElo);

				// update the user, and his shield
				success =
				    writeChangesToDB(attackerId, attacker, clientTime, plfu, currencyChange);
				// gemsSpent, cashChange, clientTime,
			}

			if (success) {
				resBuilder.setStatus(QueueUpStatus.SUCCESS);
			}

			// write event to the client
			final QueueUpResponseEvent resEvent = new QueueUpResponseEvent(attackerId);
			resEvent.setTag(event.getTag());
			resEvent.setQueueUpResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in QueueUpController.processRequestEvent", e);
			}

			if (success) {
				// UPDATE CLIENT
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(
				        attacker, plfu);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error("fatal exception in QueueUpController.processRequestEvent", e);
				}
				//
				// //TODO: tracking user currency change, among other things, if
				// charging
				//
			}

		} catch (final Exception e) {
			LOG.error("exception in QueueUp processEvent", e);
			resBuilder.setStatus(QueueUpStatus.FAIL_OTHER);
			final QueueUpResponseEvent resEvent = new QueueUpResponseEvent(attackerId);
			resEvent.setTag(event.getTag());
			resEvent.setQueueUpResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in QueueUpController.processRequestEvent", e);
			}
		}
	}

	private boolean checkLegitQueueUp( final Builder resBuilder, final User u,
	    final Date clientDate )
	{
		// int gemsSpent, int cashChange) {
		if (null == u) {
			resBuilder.setStatus(QueueUpStatus.FAIL_OTHER);
			LOG.error("problem with QueueUp- attacker is null. user is "
			    + u);
			return false;
		}

		// see if user has enough money to find a person to fight
		// CHECK MONEY
		// if (!hasEnoughGems(resBuilder, u, gemsSpent, cashChange)) {
		// return false;
		// }
		//
		// if (!hasEnoughCash(resBuilder, u, gemsSpent, cashChange)) {
		// return false;
		// }

		// User queuedOpponent = queuedOpponent(attacker, elo, seenUserUuids,
		// clientDate);
		//
		// queuedOpponentList.add(queuedOpponent);
		resBuilder.setStatus(QueueUpStatus.SUCCESS);
		return true;
	}

	// private boolean hasEnoughGems(Builder resBuilder, User u, int gemsSpent,
	// int cashChange) {
	// int userGems = u.getGems();
	// //if user's aggregate gems is < cost, don't allow transaction
	// if (userGems < gemsSpent) {
	// LOG.error("user error: user does not have enough gems. userGems=" +
	// userGems +
	// "\t gemsSpent=" + gemsSpent + "\t user=" + u);
	// resBuilder.setStatus(QueueUpStatus.FAIL_NOT_ENOUGH_GEMS);
	// return false;
	// }
	// return true;
	// }
	//
	// private boolean hasEnoughCash(Builder resBuilder, User u, int gemsSpent,
	// int cashChange) {
	// int userCash = u.getCash();
	// //positive 'cashChange' means refund, negative means charge user
	// int cost = -1 * cashChange;
	//
	// //if user not spending gems and is just spending cash, check if he has
	// enough
	// if (0 == gemsSpent && userCash < cost) {
	// LOG.error("user error: user does not have enough cash. userCash=" +
	// userCash +
	// "\t cost=" + cost + "\t user=" + u);
	// resBuilder.setStatus(QueueUpStatus.FAIL_NOT_ENOUGH_CASH);
	// return false;
	// }
	// return true;
	// }

	private void setProspectivePvpMatches( final Builder resBuilder, final User attacker,
	    final Set<Integer> uniqSeenUserUuids, final Date clientDate, final int attackerElo )
	{
		// so as to not recompute elo range
		// List<Integer> minEloList = new ArrayList<Integer>();
		// List<Integer> maxEloList = new ArrayList<Integer>();
		// getMinMaxElo(attackerElo, minEloList, maxEloList);
		//
		// int minElo = minEloList.get(0);
		// int maxElo = maxEloList.get(0);

		// just select people above and below attacker's elo
		final int minElo = Math.max(0, attackerElo
		    - ControllerConstants.PVP__ELO_RANGE_SUBTRAHEND);
		final int maxElo = attackerElo
		    + ControllerConstants.PVP__ELO_RANGE_ADDEND;

		// ids are for convenience
		final List<Integer> queuedOpponentUuidsList = new ArrayList<Integer>();
		// if want up to date info comment this out and query from db instead
		final Map<Integer, PvpUser> userIdToPvpUser = new HashMap<Integer, PvpUser>();

		// get the users that the attacker will fight
		final List<User> queuedOpponents =
		    getQueuedOpponents(attacker, attackerElo, minElo, maxElo, uniqSeenUserUuids,
		        clientDate, queuedOpponentUuidsList, userIdToPvpUser);

		int numWanted = ControllerConstants.PVP__MAX_QUEUE_SIZE;
		final List<PvpProto> pvpProtoList = new ArrayList<PvpProto>();

		if ((null == queuedOpponents)
		    || (queuedOpponents.size() < numWanted)) {
			numWanted = numWanted
			    - queuedOpponentUuidsList.size();

			// GENERATE THE FAKE DEFENDER AND MONSTERS, not enough enemies, get
			// fake ones
			LOG.info("no valid users for attacker="
			    + attacker);
			LOG.info("generating fake users.");
			final Set<MonsterForPvp> fakeMonsters =
			    getMonsterForPvpRetrieveUtils().retrievePvpMonsters(minElo, maxElo);

			// group monsters off by 3; limit the number of groups of 3
			// NOTE: this is assuming there are more than enough monsters...
			final List<List<MonsterForPvp>> fakeUserMonsters =
			    createFakeUserMonsters(fakeMonsters, numWanted);
			final List<PvpProto> pvpProtoListTemp =
			    createPvpProtosFromFakeUser(fakeUserMonsters);

			pvpProtoList.addAll(pvpProtoListTemp);
		}

		if ((null != queuedOpponents)
		    && !queuedOpponents.isEmpty()) {
			LOG.info("there are people to attack!");
			LOG.info("queuedOpponentUuidsList="
			    + queuedOpponentUuidsList);
			LOG.info("queuedOpponents:"
			    + queuedOpponents);

			/*
			 * Map<Integer, PvpLeagueForUser> userIdToPvpLeagueInfo =
			 * getPvpLeagueForUserRetrieveUtil()
			 * .getUserPvpLeagueForUsers(queuedOpponentUuidsList);
			 */

			// get the 3 monsters for each defender: ideally should be equipped,
			// but
			// will randomly select if user doesn't have 3 equipped
			final Map<Integer, List<MonsterForUser>> userIdToUserMonsters =
			    selectMonstersForUsers(queuedOpponentUuidsList);

			final Map<Integer, Integer> userIdToProspectiveCashReward =
			    new HashMap<Integer, Integer>();
			final Map<Integer, Integer> userIdToProspectiveOilReward =
			    new HashMap<Integer, Integer>();

			calculateCashOilRewards(queuedOpponents, userIdToProspectiveCashReward,
			    userIdToProspectiveOilReward);

			// create the protos for all this
			final List<PvpProto> pvpProtoListTemp =
			    CreateInfoProtoUtils.createPvpProtos(queuedOpponents, null, userIdToPvpUser,
			        userIdToUserMonsters, userIdToProspectiveCashReward,
			        userIdToProspectiveOilReward);

			// user should see real people before fake ones
			pvpProtoList.addAll(0, pvpProtoListTemp);
		}
		resBuilder.addAllDefenderInfoList(pvpProtoList);
		LOG.info("pvpProtoList="
		    + pvpProtoList);

	}

	/*
	 * //unused method. Purpose was user randomly gets users from an elo range
	 * //out of six possible elo ranges. private void getMinMaxElo(int
	 * attackerElo, List<Integer> minEloList, List<Integer> maxEloList) {
	 * 
	 * int firstEloBound = Math.max(0, attackerElo -
	 * ControllerConstants.PVP__ELO_DISTANCE_THREE); int secondEloBound =
	 * Math.max(0, attackerElo - ControllerConstants.PVP__ELO_DISTANCE_TWO); int
	 * thirdEloBound = Math.max(0, attackerElo -
	 * ControllerConstants.PVP__ELO_DISTANCE_ONE); int fourthEloBound =
	 * attackerElo + ControllerConstants.PVP__ELO_DISTANCE_ONE; int
	 * fifthEloBound = attackerElo + ControllerConstants.PVP__ELO_DISTANCE_TWO;
	 * int sixthEloBound = attackerElo +
	 * ControllerConstants.PVP__ELO_DISTANCE_THREE;
	 * 
	 * //get the min and max elo, initial values are dummy values int minElo =
	 * 0; int maxElo = attackerElo; Random rand = new Random();
	 * 
	 * float randFloat = rand.nextFloat(); if(randFloat <
	 * ControllerConstants.PVP__ELO_CATEGORY_ONE_PAIRING_CHANCE) {
	 * LOG.info("in first elo category"); minElo = firstEloBound; maxElo =
	 * secondEloBound;
	 * 
	 * } else if(randFloat <
	 * ControllerConstants.PVP__ELO_CATEGORY_TWO_PAIRING_CHANCE) {
	 * LOG.info("in second elo category"); minElo = secondEloBound; maxElo =
	 * thirdEloBound;
	 * 
	 * } else if(randFloat <
	 * ControllerConstants.PVP__ELO_CATEGORY_THREE_PAIRING_CHANCE) {
	 * LOG.info("in third elo category"); minElo = thirdEloBound; maxElo =
	 * attackerElo;
	 * 
	 * } else if(randFloat <
	 * ControllerConstants.PVP__ELO_CATEGORY_FOUR_PAIRING_CHANCE) {
	 * LOG.info("in fourth elo category");
	 * 
	 * minElo = attackerElo; maxElo = fourthEloBound;
	 * 
	 * } else if(randFloat <
	 * ControllerConstants.PVP__ELO_CATEGORY_FIVE_PAIRING_CHANCE) {
	 * LOG.info("in fifth elo category"); minElo = fourthEloBound; maxElo =
	 * fifthEloBound;
	 * 
	 * } else { LOG.info("in sixth elo category"); minElo = fifthEloBound;
	 * maxElo = sixthEloBound;
	 * 
	 * }
	 * 
	 * //this is to ensure that elos being searched for are not below 0
	 * LOG.info("minElo before maxing with 0: " + minElo);
	 * LOG.info("maxElo before maxing with 0: " + maxElo); minElo = Math.max(0,
	 * minElo); maxElo = Math.max(0, maxElo);
	 * LOG.info("minElo after maxing with 0: " + minElo);
	 * LOG.info("maxElo after maxing with 0: " + maxElo);
	 * 
	 * minEloList.add(minElo); maxEloList.add(maxElo); }
	 */

	// purpose of userIdList is to prevent another iteration through the return
	// list just
	// to extract the user ids
	private List<User> getQueuedOpponents( final User attacker, final int attackerElo,
	    final int minElo, final int maxElo, final Set<Integer> seenUserUuids,
	    final Date clientDate, final List<Integer> userIdList,
	    final Map<Integer, PvpUser> userIdToPvpUser )
	{
		// inefficient: 5 db calls
		// List<User> qList = getUserRetrieveUtils().retrieveCompleteQueueList(
		// attacker, elo, seenUserUuids, clientDate);
		// if(qList.isEmpty()) {
		// return null;
		// }
		// else {
		// Random rand = new Random();
		// User queuedOpponent = qList.get(rand.nextInt(qList.size()));
		// return queuedOpponent;
		// }
		// User defender = null;

		// jedis, redis stuff
		// //now having elo range, figure out the offset
		// //could make a range call and do stuff after but eh
		// int offset = 0;
		// int limit = ControllerConstants.PVP__NUM_ENEMIES_LIMIT;
		//
		// Set<Tuple> prospectiveDefenders = getPvpUtil().getEloTopN(minElo,
		// maxElo,
		// offset, limit);
		//
		// //go through them and select the one that has not been seen yet
		// for (Tuple t : prospectiveDefenders) {
		// String userUuid = Integer.valueOf(t.getElement());
		// int elo = (int) t.getScore();
		//
		// if (!seenUserUuids.contains(userUuid)) {
		// //we have a winner!
		// defender = RetrieveUtils.userRetrieveUtils().getUserById(userUuid);
		// }
		//
		//
		// }
		// use hazelcast distributed map to get the defenders, limit the amount
		final int numNeeded = ControllerConstants.PVP__MAX_QUEUE_SIZE;
		final Set<PvpUser> prospectiveDefenders =
		    getHazelcastPvpUtil().retrievePvpUsers(minElo, maxElo, clientDate, numNeeded,
		        seenUserUuids);

		final int numDefenders = prospectiveDefenders.size();
		LOG.info("users returned from hazelcast pvp util. users="
		    + prospectiveDefenders);

		// choose users either randomly or all of them
		selectUsers(numNeeded, numDefenders, prospectiveDefenders, userIdList, userIdToPvpUser);

		final List<User> selectedDefenders = new ArrayList<User>();
		if (!prospectiveDefenders.isEmpty()) {
			final Map<Integer, User> selectedDefendersMap = RetrieveUtils.userRetrieveUtils()
			    .getUsersByUuids(userIdList);
			selectedDefenders.addAll(selectedDefendersMap.values());
		}

		LOG.info("the lucky people who get to be attacked! defenders="
		    + selectedDefenders);
		return selectedDefenders;
	}

	private void selectUsers( final int numNeeded, final int numDefenders,
	    final Set<PvpUser> prospectiveDefenders, final List<Integer> userIdList,
	    final Map<Integer, PvpUser> userIdToPvpUser )
	{
		final Random rand = new Random();

		float numNeededSoFar = numNeeded;
		float numDefendersLeft = numDefenders;
		// go through them and select the one that has not been seen yet
		for (final PvpUser pvpUser : prospectiveDefenders) {
			// when prospectiveDefenders is out, loop breaks,
			// regardless of numNeededSoFar
			LOG.info("pvp opponents, numNeeded="
			    + numNeededSoFar);
			LOG.info("pvp opponents, numAvailable="
			    + numDefendersLeft);

			final String userUuid = Integer.valueOf(pvpUser.getUserUuid());

			if (userIdList.size() >= ControllerConstants.PVP__MAX_QUEUE_SIZE) {
				// don't want to send every eligible victim to user.
				LOG.info("reached queue length of "
				    + ControllerConstants.PVP__MAX_QUEUE_SIZE);
				break;
			}

			// if we whittle down the entire applicant pool to the minimum we
			// want
			// select all of them
			if (numNeededSoFar >= numDefendersLeft) {
				userIdList.add(userUuid);
				userIdToPvpUser.put(userUuid, pvpUser);
				numNeededSoFar -= 1;
				numDefendersLeft -= 1;
				continue;
			}

			// randomly pick people
			final float randFloat = rand.nextFloat();
			final float probabilityToBeSelected = numNeededSoFar
			    / numDefendersLeft;
			LOG.info("randFloat="
			    + randFloat);
			LOG.info("probabilityToBeSelected="
			    + probabilityToBeSelected);
			if (randFloat < probabilityToBeSelected) {
				// we have a winner!
				userIdList.add(userUuid);
				userIdToPvpUser.put(userUuid, pvpUser);
				numNeededSoFar -= 1;
			}
			numDefendersLeft -= 1;
		}

	}

	// given users, get the 3 monsters for each user
	private Map<Integer, List<MonsterForUser>> selectMonstersForUsers(
	    final List<Integer> userIdList )
	{

		// return value
		final Map<Integer, List<MonsterForUser>> userUuidsToUserMonsters =
		    new HashMap<Integer, List<MonsterForUser>>();

		// for all these users, get all their complete monsters
		final Map<Integer, Map<Long, MonsterForUser>> userIdsToMfuUuidsToMonsters =
		    RetrieveUtils.monsterForUserRetrieveUtils()
		        .getCompleteMonstersForUser(userIdList);

		for (int index = 0; index < userIdList.size(); index++) {
			// extract a user's monsters
			final int defenderId = userIdList.get(index);
			final Map<Long, MonsterForUser> mfuUuidsToMonsters =
			    userIdsToMfuUuidsToMonsters.get(defenderId);

			if ((null == mfuUuidsToMonsters)
			    || mfuUuidsToMonsters.isEmpty()) {
				LOG.error("WTF!!!!!!!! user has no monsters!!!!! userUuid="
				    + defenderId
				    + "\t will move on to next guy.");
				continue;
			}
			// try to select at most 3 monsters for this user
			final List<MonsterForUser> defenderMonsters =
			    selectMonstersForUser(mfuUuidsToMonsters);

			// if the user still doesn't have 3 monsters, then too bad
			userUuidsToUserMonsters.put(defenderId, defenderMonsters);
		}

		return userUuidsToUserMonsters;
	}

	private List<MonsterForUser> selectMonstersForUser(
	    final Map<Long, MonsterForUser> mfuUuidsToMonsters )
	{

		// get all the monsters the user has on a team (at the moment, max is 3)
		List<MonsterForUser> defenderMonsters = getEquippedMonsters(mfuUuidsToMonsters);

		if (defenderMonsters.size() < 3) {
			// need more monsters so select them randomly, fill up
			// "defenderMonsters" list
			getRandomMonsters(mfuUuidsToMonsters, defenderMonsters);
		}

		if (defenderMonsters.size() > 3) {
			// only get three monsters
			defenderMonsters = defenderMonsters.subList(0, 3);
		}

		return defenderMonsters;
	}

	private List<MonsterForUser> getEquippedMonsters(
	    final Map<Long, MonsterForUser> userMonsters )
	{
		final List<MonsterForUser> equipped = new ArrayList<MonsterForUser>();

		for (final MonsterForUser mfu : userMonsters.values()) {
			if (mfu.getTeamSlotNum() <= 0) {
				// only want equipped monsters
				continue;
			}
			equipped.add(mfu);

		}
		return equipped;
	}

	private void getRandomMonsters( final Map<Long, MonsterForUser> possibleMonsters,
	    final List<MonsterForUser> defenderMonsters )
	{

		final Map<Long, MonsterForUser> possibleMonstersTemp =
		    new HashMap<Long, MonsterForUser>(possibleMonsters);

		// remove the defender monsters from possibleMonstersTemp, since
		// defenderMonsters
		// were already selected from possibleMonsters
		for (final MonsterForUser m : defenderMonsters) {
			final long mfuId = m.getId();

			possibleMonstersTemp.remove(mfuId);
		}

		final int amountLeftOver = possibleMonstersTemp.size();
		int amountNeeded = 3 - defenderMonsters.size();

		if (amountLeftOver < amountNeeded) {
			defenderMonsters.addAll(possibleMonstersTemp.values());
			return;
		}

		// randomly select enough monsters to total 3
		final List<MonsterForUser> mfuList =
		    new ArrayList<MonsterForUser>(possibleMonstersTemp.values());
		final Random rand = new Random();

		// for each monster gen rand float, and if it "drops" select it
		for (int i = 0; i < mfuList.size(); i++) {

			// eg. need 2, have 3. If first one is not picked, then need 2, have
			// 2.
			final float probToBeChosen = amountNeeded
			    / (amountLeftOver - i);
			final float randFloat = rand.nextFloat();

			if (randFloat < probToBeChosen) {
				// we have a winner! select this monster
				final MonsterForUser mfu = mfuList.get(i);
				defenderMonsters.add(mfu);

				// need to decrement amount needed
				amountNeeded--;
			}

			// stop at three monsters, don't want to get more
			if (defenderMonsters.size() >= 3) {
				break;
			}
		}

	}

	// separate monsters into groups of three, limit the number of groups of
	// three
	private List<List<MonsterForPvp>> createFakeUserMonsters(
	    final Set<MonsterForPvp> fakeMonsters, final int numWanted )
	{
		final List<List<MonsterForPvp>> fakeUserMonsters = new ArrayList<List<MonsterForPvp>>();

		// this will contain a group of three monsters
		List<MonsterForPvp> tempFakeUser = new ArrayList<MonsterForPvp>();

		for (final MonsterForPvp mfp : fakeMonsters) {

			tempFakeUser.add(mfp);

			// limit how many monsters can go in a group
			if (tempFakeUser.size() >= 3) {
				fakeUserMonsters.add(tempFakeUser);
				// reset the group of three
				tempFakeUser = new ArrayList<MonsterForPvp>();

			}

			// if the num groups created are more than or equal to the limit of
			// num groups of
			// three, then exit
			if (fakeUserMonsters.size() >= numWanted) {
				break;
			}
		}

		return fakeUserMonsters;
	}

	private void calculateCashOilRewards( final List<User> queuedOpponents,
	    final Map<Integer, Integer> userIdToProspectiveCashReward,
	    final Map<Integer, Integer> userIdToProspectiveOilReward )
	{

		for (final User queuedOpponent : queuedOpponents) {
			final String userUuid = queuedOpponent.getId();

			final int cashReward = MiscMethods.calculateCashRewardFromPvpUser(queuedOpponent);
			final int oilReward = MiscMethods.calculateOilRewardFromPvpUser(queuedOpponent);

			userIdToProspectiveCashReward.put(userUuid, cashReward);
			userIdToProspectiveOilReward.put(userUuid, oilReward);
		}
	}

	private List<PvpProto> createPvpProtosFromFakeUser(
	    final List<List<MonsterForPvp>> fakeUserMonsters )
	{
		LOG.info("creating fake users for pvp!!!!");
		final List<PvpProto> ppList = new ArrayList<PvpProto>();

		for (final List<MonsterForPvp> mons : fakeUserMonsters) {
			final PvpProto user = createFakeUser(mons);
			ppList.add(user);
		}

		LOG.info("num fake users created: "
		    + ppList.size());
		return ppList;
	}

	// CREATES ONE FAKE USER FOR PVP
	private PvpProto createFakeUser( final List<MonsterForPvp> mfpList )
	{
		// to create the fake user, need userUuid=0, some random name, empty
		// clan
		// for lvl do something like (elo / 50)
		// for cur elo avg out the monsters elos

		final List<Integer> cashWinnings = new ArrayList<Integer>();
		final List<Integer> oilWinnings = new ArrayList<Integer>();
		final int avgElo = determineAvgEloAndCashOilReward(mfpList, cashWinnings, oilWinnings);

		final String userUuid = 0;
		final String randomName = getHazelcastPvpUtil().getRandomName();
		final int lvl = avgElo
		    / ControllerConstants.PVP__FAKE_USER_LVL_DIVISOR;

		final int prospectiveCashWinnings = cashWinnings.get(0);
		final int prospectiveOilWinnings = oilWinnings.get(0);

		LOG.info("fake user created: name="
		    + randomName
		    + "\t avgElo="
		    + avgElo
		    + "\t cash="
		    + prospectiveCashWinnings
		    + "\t oil="
		    + prospectiveOilWinnings
		    + "\t lvl="
		    + lvl);

		final PvpProto fakeUser =
		    CreateInfoProtoUtils.createFakePvpProto(userUuid, randomName, lvl, avgElo,
		        prospectiveCashWinnings, prospectiveOilWinnings, mfpList);
		return fakeUser;
	}

	// assumes the List<MonsterForPvp> mons is not empty
	private int determineAvgEloAndCashOilReward( final List<MonsterForPvp> mons,
	    final List<Integer> cashWinnings, final List<Integer> oilWinnings )
	{
		int avgElo = 0;
		int prospectiveCashWinnings = 0;
		int prospectiveOilWinnings = 0;

		for (final MonsterForPvp mon : mons) {
			avgElo += mon.getElo();

			prospectiveCashWinnings += mon.getCashDrop();
			prospectiveOilWinnings += mon.getOilDrop();
		}

		avgElo = avgElo
		    / mons.size();
		cashWinnings.add(prospectiveCashWinnings);
		oilWinnings.add(prospectiveOilWinnings);

		return avgElo;
	}

	// remove his shield if he has one, since he is going to attack some one
	private boolean writeChangesToDB( final int attackerId, final User attacker, // int
	    // gemsSpent,
	    // int
	    // cashChange,
	    final Timestamp queueTime, final PvpLeagueForUser plfu, final Map<String, Integer> money )
	{

		// //CHARGE THE USER
		// int oilChange = 0;
		// int gemChange = -1 * gemsSpent;
		//
		// int numChange = attacker.updateRelativeCashAndOilAndGems(cashChange,
		// oilChange, gemChange);
		// if (1 != numChange) {
		// LOG.error("problem with updating user stats: gemChange=" + gemChange
		// + ", cashChange=" + cashChange + ", user is " + attacker);
		// return false;
		// } else {
		// //everything went well
		// if (0 != oilChange) {
		// money.put(MiscMethods.cash, cashChange);
		// }
		// if (0 != gemsSpent) {
		// money.put(MiscMethods.gems, gemChange);
		// }
		// }
		// update the user who queued things

		// TODO: this is the same logic in BeginPvpBattleController
		// turn off user's shield if he has one active
		final Date curShieldEndTime = plfu.getShieldEndTime();
		final Date queueDate = new Date(queueTime.getTime());
		if (getTimeUtils().isFirstEarlierThanSecond(queueDate, curShieldEndTime)) {
			LOG.info("shield end time is now being reset since he's attacking with a shield");
			LOG.info("1cur pvpuser="
			    + getHazelcastPvpUtil().getPvpUser(attackerId));
			final Date login = attacker.getLastLogin();
			final Timestamp loginTime = new Timestamp(login.getTime());
			UpdateUtils.get()
			    .updatePvpLeagueForUserShields(attackerId, loginTime, loginTime);

			final PvpUser attackerOpu = new PvpUser(plfu);
			attackerOpu.setShieldEndTime(login);
			attackerOpu.setInBattleEndTime(login);
			getHazelcastPvpUtil().replacePvpUser(attackerOpu, attackerId);
			LOG.info("2cur pvpuser="
			    + getHazelcastPvpUtil().getPvpUser(attackerId));
			LOG.info("(should be same as 2cur pvpUser) 3cur pvpuser="
			    + attackerOpu);
		}

		return true;
	}

	public HazelcastPvpUtil getHazelcastPvpUtil()
	{
		return hazelcastPvpUtil;
	}

	public void setHazelcastPvpUtil( final HazelcastPvpUtil hazelcastPvpUtil )
	{
		this.hazelcastPvpUtil = hazelcastPvpUtil;
	}

	public MonsterForPvpRetrieveUtils getMonsterForPvpRetrieveUtils()
	{
		return monsterForPvpRetrieveUtils;
	}

	public void setMonsterForPvpRetrieveUtils(
	    final MonsterForPvpRetrieveUtils monsterForPvpRetrieveUtils )
	{
		this.monsterForPvpRetrieveUtils = monsterForPvpRetrieveUtils;
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
