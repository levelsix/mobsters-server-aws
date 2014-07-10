package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterResponseProto.EvolveMonsterStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.EvolveMonsterRequestEvent;
import com.lvl6.mobsters.events.response.EvolveMonsterResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterEvolutionProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class EvolveMonsterController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(EvolveMonsterController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public EvolveMonsterController()
	{
		numAllocatedThreads = 3;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new EvolveMonsterRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_EVOLVE_MONSTER_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final EvolveMonsterRequestProto reqProto =
		    ((EvolveMonsterRequestEvent) event).getEvolveMonsterRequestProto();

		LOG.info("reqProto="
		    + reqProto);

		// get data client sent
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();

		final UserMonsterEvolutionProto uep = reqProto.getEvolution();
		final int gemsSpent = reqProto.getGemsSpent();
		// positive means refund, negative means charge user
		final int oilChange = reqProto.getOilChange();

		long catalystUserMonsterId = 0;
		List<Long> evolvingUserMonsterUuids = new ArrayList<Long>();
		Timestamp clientTime = null;

		if ((null != uep)
		    && reqProto.hasEvolution()) {
			LOG.info("uep is not null");
			catalystUserMonsterId = uep.getCatalystUserMonsterId();
			evolvingUserMonsterUuids = new ArrayList<Long>(uep.getUserMonsterUuidsList());
			clientTime = new Timestamp(uep.getStartTime());
		}

		// set some values to send to the client (the response proto)
		final EvolveMonsterResponseProto.Builder resBuilder =
		    EvolveMonsterResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(EvolveMonsterStatus.FAIL_OTHER);

		getLocker().lockPlayer(senderProto.getUserUuid(), getClass().getSimpleName());
		try {
			int previousOil = 0;
			int previousGems = 0;
			// get whatever we need from the database
			final User aUser = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			final Map<Long, MonsterEnhancingForUser> alreadyEnhancing =
			    MonsterEnhancingForUserRetrieveUtils.getMonstersForUser(userUuid);

			final Map<Long, MonsterHealingForUser> alreadyHealing =
			    MonsterHealingForUserRetrieveUtils.getMonstersForUser(userUuid);

			final Map<Long, MonsterEvolvingForUser> alreadyEvolving =
			    MonsterEvolvingForUserRetrieveUtils.getCatalystUuidsToEvolutionsForUser(userUuid);

			// retrieve all the new monsters
			Map<Long, MonsterForUser> existingUserMonsters =
			    new HashMap<Long, MonsterForUser>();

			// just in case uep is null, but most likely not. retrieve all the
			// monsters used
			// in evolution, just to make sure they exist
			if ((null != uep)
			    && reqProto.hasEvolution()) {
				final Set<Long> newUuids = new HashSet<Long>();
				newUuids.add(catalystUserMonsterId);
				newUuids.addAll(evolvingUserMonsterUuids);
				existingUserMonsters = RetrieveUtils.monsterForUserRetrieveUtils()
				    .getSpecificOrAllUserMonstersForUser(userUuid, newUuids);
				LOG.info("retrieved user monsters. existingUserMonsters="
				    + existingUserMonsters);
			}
			final boolean legitMonster =
			    checkLegit(resBuilder, aUser, userUuid, existingUserMonsters, alreadyEnhancing,
			        alreadyHealing, alreadyEvolving, catalystUserMonsterId,
			        evolvingUserMonsterUuids, gemsSpent, oilChange);

			boolean successful = false;
			final Map<String, Integer> money = new HashMap<String, Integer>();

			if (legitMonster) {
				previousOil = aUser.getOil();
				previousGems = aUser.getGems();
				successful =
				    writeChangesToDB(aUser, userUuid, gemsSpent, oilChange,
				        catalystUserMonsterId, evolvingUserMonsterUuids, clientTime, money);
			}

			if (successful) {
				resBuilder.setStatus(EvolveMonsterStatus.SUCCESS);
			}

			final EvolveMonsterResponseEvent resEvent =
			    new EvolveMonsterResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setEvolveMonsterResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in EvolveMonsterController.processRequestEvent", e);
			}

			if (successful) {
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error("fatal exception in EvolveMonsterController.processRequestEvent",
					    e);
				}

				writeToUserCurrencyHistory(aUser, clientTime, money, previousOil, previousGems,
				    catalystUserMonsterId, evolvingUserMonsterUuids);
			}

		} catch (final Exception e) {
			LOG.error("exception in EnhanceMonster processEvent", e);
		} finally {
			getLocker().unlockPlayer(senderProto.getUserUuid(), getClass().getSimpleName());
		}
	}

	private boolean checkLegit( final Builder resBuilder, final User u, final String userUuid,
	    final Map<Long, MonsterForUser> existingUserMonsters,
	    final Map<Long, MonsterEnhancingForUser> alreadyEnhancing,
	    final Map<Long, MonsterHealingForUser> alreadyHealing,
	    final Map<Long, MonsterEvolvingForUser> alreadyEvolving,
	    final long catalystUserMonsterId, final List<Long> userMonsterUuids,
	    final int gemsSpent, final int oilChange )
	{
		if (null == u) {
			LOG.error("unexpected error: user is null. user="
			    + u
			    + "\t catalystUserMonsterId="
			    + catalystUserMonsterId
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
			return false;
		}

		// at the moment only 3 are required to evolve a monster
		if ((null == existingUserMonsters)
		    || existingUserMonsters.isEmpty()
		    || (3 != existingUserMonsters.size())) {
			LOG.error("user trying to user nonexistent monster in evolution. existing="
			    + existingUserMonsters
			    + "\t catalyst="
			    + catalystUserMonsterId
			    + "\t others="
			    + userMonsterUuids);
			resBuilder.setStatus(EvolveMonsterStatus.FAIL_NONEXISTENT_MONSTERS);
			return false;
		}

		// at the moment only one evolution is allowed going on at any one time
		if ((null != alreadyEvolving)
		    && !alreadyEvolving.isEmpty()) {
			LOG.error("user already evolving monsters. monsters="
			    + alreadyEvolving);
			resBuilder.setStatus(EvolveMonsterStatus.FAIL_MAX_NUM_EVOLUTIONS_REACHED);
			return false;
		}

		// don't allow this transaction through because at least one of these
		// monsters is
		// used in enhancing or is being healed
		if (((null != alreadyEnhancing) && !alreadyEnhancing.isEmpty())
		    || ((null != alreadyHealing) && !alreadyHealing.isEmpty())) {
			LOG.error("the monsters provided are in healing or enhancing. enhancing="
			    + alreadyEnhancing
			    + "\t healing="
			    + alreadyHealing
			    + "\t catalyst="
			    + catalystUserMonsterId
			    + "\t others="
			    + userMonsterUuids);
			return false;
		}

		// CHECK MONEY
		if (!hasEnoughGems(resBuilder, u, gemsSpent, oilChange, catalystUserMonsterId,
		    userMonsterUuids)) {
			return false;
		}

		if (!hasEnoughOil(resBuilder, u, gemsSpent, oilChange, catalystUserMonsterId,
		    userMonsterUuids)) {
			return false;
		}

		if ((0 == gemsSpent)
		    && (0 == oilChange)) {
			LOG.error("gemsSpent="
			    + gemsSpent
			    + "\t oilChange="
			    + oilChange
			    + "\t Not evolving.");
			return false;
		}

		return true;
	}

	// if gem cost is 0 and user gems is 0, then 0 !< 0 so no error issued
	private boolean hasEnoughGems( final Builder resBuilder, final User u, final int gemsSpent,
	    final int oilChange, final long catalyst, final List<Long> userMonsterUuids )
	{
		final int userGems = u.getGems();
		// if user's aggregate gems is < cost, don't allow transaction
		if (userGems < gemsSpent) {
			LOG.error("user error: user does not have enough gems. userGems="
			    + userGems
			    + "\t gemsSpent="
			    + gemsSpent
			    + "\t oilChange="
			    + oilChange
			    + "\t catalyst="
			    + catalyst
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
			resBuilder.setStatus(EvolveMonsterStatus.FAIL_INSUFFICIENT_GEMS);
			return false;
		}
		return true;
	}

	private boolean hasEnoughOil( final Builder resBuilder, final User u, final int gemsSpent,
	    final int oilChange, final long catalyst, final List<Long> userMonsterUuids )
	{
		final int userOil = u.getOil();
		// positive 'cashChange' means refund, negative means charge user
		final int cost = -1
		    * oilChange;

		// if user not spending gems check if user has enough oil
		if ((0 == gemsSpent)
		    && (userOil < cost)) {
			LOG.error("user error: user does not have enough cash. cost="
			    + cost
			    + "\t oilChange="
			    + oilChange
			    + "\t catalyst="
			    + catalyst
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
			resBuilder.setStatus(EvolveMonsterStatus.FAIL_INSUFFICIENT_RESOURCES);
			return false;
		}
		return true;
	}

	private boolean writeChangesToDB( final User user, final int uId, final int gemsSpent,
	    final int oilChange, final long catalystUserMonsterId,
	    final List<Long> userMonsterUuids, final Timestamp clientTime,
	    final Map<String, Integer> money )
	{

		// CHARGE THE USER
		final int cashChange = 0;
		final int gemChange = -1
		    * gemsSpent;

		final int numChange =
		    user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
		if (1 != numChange) {
			LOG.error("problem with updating user stats: gemChange="
			    + gemChange
			    + ", cashChange="
			    + oilChange
			    + ", user is "
			    + user);
			return false;

		} else {
			// everything went well
			if (0 != oilChange) {
				money.put(MiscMethods.oil, oilChange);
			}
			if (0 != gemsSpent) {
				money.put(MiscMethods.gems, gemChange);
			}
		}

		// insert into monster_evolving_for_user table
		final int numInserted =
		    InsertUtils.get()
		        .insertIntoMonsterEvolvingForUser(uId, catalystUserMonsterId, userMonsterUuids,
		            clientTime);

		LOG.info("for monster_evolving table, numInserted="
		    + numInserted);

		return true;
	}

	public void writeToUserCurrencyHistory( final User aUser, final Timestamp date,
	    final Map<String, Integer> moneyChange, final int previousOil, final int previousGems,
	    final long catalystUserMonsterId, final List<Long> userMonsterUuids )
	{
		if (moneyChange.isEmpty()) {
			return;
		}
		final String oil = MiscMethods.oil;
		final String gems = MiscMethods.gems;

		String reasonForChange = ControllerConstants.UCHRFC__EVOLVING;
		final StringBuilder detailSb = new StringBuilder();
		detailSb.append("(catalystId, userMonsterId1, userMonsterId2) ");
		if (moneyChange.containsKey(gems)) {
			reasonForChange = ControllerConstants.UCHRFC__SPED_UP_EVOLUTION;
		}
		// maybe shouldn't keep track...oh well, more info hopefully is better
		// than none
		detailSb.append("(");
		detailSb.append(catalystUserMonsterId);
		detailSb.append(",");
		final long one = userMonsterUuids.get(0);
		detailSb.append(one);
		final long two = userMonsterUuids.get(1);
		detailSb.append(two);
		detailSb.append(")");

		final String userUuid = aUser.getId();
		final Map<String, Integer> previousCurrencyMap = new HashMap<String, Integer>();
		final Map<String, Integer> currentCurrencyMap = new HashMap<String, Integer>();
		final Map<String, String> changeReasonsMap = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();

		previousCurrencyMap.put(oil, previousOil);
		previousCurrencyMap.put(gems, previousGems);
		currentCurrencyMap.put(oil, aUser.getOil());
		currentCurrencyMap.put(gems, aUser.getGems());
		changeReasonsMap.put(oil, reasonForChange);
		changeReasonsMap.put(gems, reasonForChange);
		detailsMap.put(oil, detailSb.toString());
		detailsMap.put(gems, detailSb.toString());

		MiscMethods.writeToUserCurrencyOneUser(userUuid, date, moneyChange,
		    previousCurrencyMap, currentCurrencyMap, changeReasonsMap, detailsMap);
	}

}
