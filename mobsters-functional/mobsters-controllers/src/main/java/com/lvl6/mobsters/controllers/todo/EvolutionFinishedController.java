//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
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
//import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
//import com.lvl6.mobsters.dynamo.MonsterForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedRequestProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedResponseProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedResponseProto.EvolutionFinishedStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.EvolutionFinishedRequestEvent;
//import com.lvl6.mobsters.events.response.EvolutionFinishedResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.info.Monster;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class EvolutionFinishedController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(EvolutionFinishedController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public EvolutionFinishedController()
//	{
//		numAllocatedThreads = 3;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new EvolutionFinishedRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_EVOLUTION_FINISHED_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final EvolutionFinishedRequestProto reqProto =
//		    ((EvolutionFinishedRequestEvent) event).getEvolutionFinishedRequestProto();
//
//		LOG.info("reqProto="
//		    + reqProto);
//
//		// get data client sent
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		// (positive number, server will convert it to negative)
//		final int gemsSpent = reqProto.getGemsSpent();
//		final Date now = new Date();
//
//		// set some values to send to the client (the response proto)
//		final EvolutionFinishedResponseProto.Builder resBuilder =
//		    EvolutionFinishedResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(EvolutionFinishedStatus.FAIL_OTHER);
//
//		getLocker().lockPlayer(senderProto.getUserUuid(), getClass().getSimpleName());
//		try {
//			int previousGems = 0;
//			// get whatever we need from the database
//			final User aUser = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(userUuid);
//			final MonsterEvolvingForUser evolution =
//			    MonsterEvolvingForUserRetrieveUtils.getEvolutionForUser(userUuid);
//
//			// retrieve all the monsters used in evolution
//			final Map<Long, MonsterForUser> existingUserMonsters =
//			    getMonstersUsedInEvolution(userUuid, evolution);
//
//			LOG.info("evolution="
//			    + evolution);
//			LOG.info("existingUserMonsters="
//			    + existingUserMonsters);
//
//			final boolean legitMonster =
//			    checkLegit(resBuilder, aUser, userUuid, evolution, existingUserMonsters,
//			        gemsSpent);
//
//			boolean successful = false;
//			final Map<String, Integer> money = new HashMap<String, Integer>();
//			final List<MonsterForUser> evolvedUserMonster = new ArrayList<MonsterForUser>();
//			if (legitMonster) {
//				previousGems = aUser.getGems();
//				successful =
//				    writeChangesToDB(aUser, userUuid, now, gemsSpent, evolution, money,
//				        existingUserMonsters, evolvedUserMonster);
//			}
//
//			if (successful) {
//				final MonsterForUser evolvedMfu = evolvedUserMonster.get(0);
//				final FullUserMonsterProto fump =
//				    CreateInfoProtoUtils.createFullUserMonsterProtoFromUserMonster(evolvedMfu);
//				resBuilder.setEvolvedMonster(fump);
//				resBuilder.setStatus(EvolutionFinishedStatus.SUCCESS);
//			}
//
//			final EvolutionFinishedResponseEvent resEvent =
//			    new EvolutionFinishedResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setEvolutionFinishedResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in EvolutionFinishedController.processRequestEvent",
//				    e);
//			}
//
//			if (successful) {
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in EvolutionFinishedController.processRequestEvent", e);
//				}
//
//				writeToUserCurrencyHistory(aUser, now, money, previousGems, evolution,
//				    evolvedUserMonster);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in EnhanceMonster processEvent", e);
//		} finally {
//			getLocker().unlockPlayer(senderProto.getUserUuid(), getClass().getSimpleName());
//		}
//	}
//
//	private Map<Long, MonsterForUser> getMonstersUsedInEvolution( final String userUuid,
//	    final MonsterEvolvingForUser evolution )
//	{
//		Map<Long, MonsterForUser> existingUserMonsters = new HashMap<Long, MonsterForUser>();
//		// just in case evolution is null, but most likely not.
//		// retrieve all the monsters used in evolution, so they can be deleted
//		if (null != evolution) {
//			final Set<Long> newUuids = new HashSet<Long>();
//			final long catalystUserMonsterId = evolution.getCatalystMonsterForUserUuid();
//			final long userMonsterIdOne = evolution.getMonsterForUserUuidOne();
//			final long userMonsterIdTwo = evolution.getMonsterForUserUuidTwo();
//			newUuids.add(catalystUserMonsterId);
//			newUuids.add(userMonsterIdOne);
//			newUuids.add(userMonsterIdTwo);
//			existingUserMonsters = RetrieveUtils.monsterForUserRetrieveUtils()
//			    .getSpecificOrAllUserMonstersForUser(userUuid, newUuids);
//		}
//
//		return existingUserMonsters;
//	}
//
//	private boolean checkLegit( final Builder resBuilder, final User u, final String userUuid,
//	    final MonsterEvolvingForUser evolution,
//	    final Map<Long, MonsterForUser> existingUserMonsters, final int gemsSpent )
//	{
//		if ((null == u)
//		    || (null == evolution)
//		    || (null == existingUserMonsters)
//		    || existingUserMonsters.isEmpty()) {
//			LOG.error("unexpected error: user, evolution, or existingMonsters is null. user="
//			    + u
//			    + ",\t evolution="
//			    + evolution
//			    + "\t existingMonsters="
//			    + existingUserMonsters);
//			return false;
//		}
//
//		// check to make sure these monsters still exist
//		final long catalystUserMonsterId = evolution.getCatalystMonsterForUserUuid();
//		final long one = evolution.getMonsterForUserUuidOne();
//		final long two = evolution.getMonsterForUserUuidTwo();
//		if (!existingUserMonsters.containsKey(catalystUserMonsterId)
//		    || !existingUserMonsters.containsKey(one)
//		    || !existingUserMonsters.containsKey(two)) {
//			LOG.error("one of the monsters in an evolution is missing. evolution="
//			    + evolution
//			    + "\t existingUserMonsters="
//			    + existingUserMonsters);
//			resBuilder.setStatus(EvolutionFinishedStatus.FAIL_OTHER);
//			return false;
//		}
//
//		// CHECK MONEY
//		if (!hasEnoughGems(resBuilder, u, gemsSpent, evolution)) {
//			return false;
//		}
//
//		return true;
//	}
//
//	// if gem cost is 0 and user gems is 0, then 0 !< 0 so no error issued
//	private boolean hasEnoughGems( final Builder resBuilder, final User u, final int gemsSpent,
//	    final MonsterEvolvingForUser evolution )
//	{
//		final int userGems = u.getGems();
//		// if user's aggregate gems is < cost, don't allow transaction
//		if (userGems < gemsSpent) {
//			LOG.error("user error: user does not have enough gems. userGems="
//			    + userGems
//			    + "\t gemsSpent="
//			    + gemsSpent
//			    + "\t evolution="
//			    + evolution);
//			resBuilder.setStatus(EvolutionFinishedStatus.FAIL_INSUFFICIENT_GEMS);
//			return false;
//		}
//		return true;
//	}
//
//	// List<MonsterForUser> userMonsters will be populated with the evolved
//	// monster
//	private boolean writeChangesToDB( final User user, final int uId, final Date now,
//	    final int gemsSpent, final MonsterEvolvingForUser mefu,
//	    final Map<String, Integer> money, final Map<Long, MonsterForUser> idsToUserMonsters,
//	    final List<MonsterForUser> userMonsters )
//	{
//
//		// CHARGE THE USER IF HE SPED UP THE EVOLUTION
//		if (0 != gemsSpent) {
//			final int oilChange = 0;
//			final int cashChange = 0;
//			final int gemChange = -1
//			    * gemsSpent;
//			final int numChange =
//			    user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
//			if (1 != numChange) {
//				LOG.warn("problem with updating user stats: gemChange="
//				    + gemChange
//				    + ", cashChange="
//				    + oilChange
//				    + ", user is "
//				    + user);
//			} else {
//				// everything went well
//				if (0 != oilChange) {
//					money.put(MiscMethods.oil, oilChange);
//				}
//				if (0 != gemsSpent) {
//					money.put(MiscMethods.gems, gemChange);
//				}
//			}
//		}
//		// delete the monsters used in the evolution
//		final List<Long> userMonsterUuids = new ArrayList<Long>();
//		final long catalystUserMonsterId = mefu.getCatalystMonsterForUserUuid();
//		userMonsterUuids.add(catalystUserMonsterId);
//		final long uMonsterIdOne = mefu.getMonsterForUserUuidOne();
//		userMonsterUuids.add(uMonsterIdOne);
//		final long uMonsterIdTwo = mefu.getMonsterForUserUuidTwo();
//		userMonsterUuids.add(uMonsterIdTwo);
//		int num = DeleteUtils.get()
//		    .deleteMonstersForUser(userMonsterUuids);
//		LOG.info("num monsterForUser deleted: "
//		    + num);
//
//		// delete the evolution
//		num =
//		    DeleteUtils.get()
//		        .deleteMonsterEvolvingForUser(catalystUserMonsterId, uMonsterIdOne,
//		            uMonsterIdTwo, uId);
//		LOG.info("num evolutions deleted: "
//		    + num);
//
//		// insert the COMPLETE evolved monster into monster for user
//		// get evolved version of monster
//		final MonsterForUser evolvedUserMonster =
//		    createEvolvedMonster(uId, now, uMonsterIdOne, idsToUserMonsters);
//		userMonsters.add(evolvedUserMonster);
//
//		final String sourceOfPieces =
//		    createSourceOfPieces(catalystUserMonsterId, uMonsterIdOne, uMonsterIdTwo);
//		final List<Long> evovlvedMfuId = InsertUtils.get()
//		    .insertIntoMonsterForUserReturnUuids(uId, userMonsters, sourceOfPieces, now);
//
//		// set the evolved monster for user's id
//		if ((null != evovlvedMfuId)
//		    && !evovlvedMfuId.isEmpty()) {
//			final long mfuId = evovlvedMfuId.get(0);
//			evolvedUserMonster.setId(mfuId);
//		}
//
//		LOG.info("evolvedUserMonster="
//		    + evolvedUserMonster);
//		LOG.info("userMonsters="
//		    + userMonsters);
//
//		return true;
//	}
//
//	private MonsterForUser createEvolvedMonster( final int uId, final Date now,
//	    final long uMonsterIdOne, final Map<Long, MonsterForUser> idsToUserMonsters )
//	{
//		final MonsterForUser unevolvedMonster = idsToUserMonsters.get(uMonsterIdOne);
//		final int monsterId = unevolvedMonster.getMonsterId();
//		final Monster evolvedMonster = MonsterRetrieveUtils.getEvolvedFormForMonster(monsterId);
//		final int numPieces = evolvedMonster.getNumPuzzlePieces();
//		final boolean isComplete = true;
//		final MonsterForUser mfu =
//		    MonsterStuffUtils.createNewUserMonster(uId, numPieces, evolvedMonster, now,
//		        isComplete);
//
//		return mfu;
//	}
//
//	private String createSourceOfPieces( final long catalystUserMonsterId,
//	    final long uMonsterIdOne, final long uMonsterIdTwo )
//	{
//		final StringBuilder sourceOfPiecesSb = new StringBuilder();
//		sourceOfPiecesSb.append("evolved from (catalystId,idOne,idTwo): (");
//		sourceOfPiecesSb.append(catalystUserMonsterId);
//		sourceOfPiecesSb.append(",");
//		sourceOfPiecesSb.append(uMonsterIdOne);
//		sourceOfPiecesSb.append(",");
//		sourceOfPiecesSb.append(uMonsterIdTwo);
//		sourceOfPiecesSb.append(")");
//
//		return sourceOfPiecesSb.toString();
//	}
//
//	public void writeToUserCurrencyHistory( final User aUser, final Date now,
//	    final Map<String, Integer> moneyChange, final int previousGems,
//	    final MonsterEvolvingForUser evolution,
//	    final List<MonsterForUser> evolvedUserMonsterList )
//	{
//		if (moneyChange.isEmpty()) {
//			return;
//		}
//		final String gems = MiscMethods.gems;
//
//		final Timestamp date = new Timestamp((now.getTime()));
//		final long catalystUserMonsterId = evolution.getCatalystMonsterForUserUuid();
//		final long one = evolution.getMonsterForUserUuidOne();
//		final long two = evolution.getMonsterForUserUuidTwo();
//		final MonsterForUser evolved = evolvedUserMonsterList.get(0);
//		final long evolvedId = evolved.getId();
//
//		final String reasonForChange = ControllerConstants.UCHRFC__SPED_UP_EVOLUTION;
//		final StringBuilder detailSb = new StringBuilder();
//		detailSb.append("(catalystId, userMonsterId, userMonsterId, evolvedMonsterId)");
//		// maybe shouldn't keep track...oh well, more info hopefully is better
//		// than none
//		detailSb.append("(");
//		detailSb.append(catalystUserMonsterId);
//		detailSb.append(",");
//		detailSb.append(one);
//		detailSb.append(",");
//		detailSb.append(two);
//		detailSb.append(",");
//		detailSb.append(evolvedId);
//		detailSb.append(")");
//
//		final String userUuid = aUser.getId();
//		final Map<String, Integer> previousCurrencyMap = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrencyMap = new HashMap<String, Integer>();
//		final Map<String, String> changeReasonsMap = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//
//		previousCurrencyMap.put(gems, previousGems);
//		currentCurrencyMap.put(gems, aUser.getGems());
//		changeReasonsMap.put(gems, reasonForChange);
//		detailsMap.put(gems, detailSb.toString());
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, date, moneyChange,
//		    previousCurrencyMap, currentCurrencyMap, changeReasonsMap, detailsMap);
//	}
//
//}
