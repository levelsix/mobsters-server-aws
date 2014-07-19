//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.MiniJobForUser;
//import com.lvl6.mobsters.dynamo.MonsterForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobRequestProto;
//import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto;
//import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto.CompleteMiniJobStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.CompleteMiniJobRequestEvent;
//import com.lvl6.mobsters.events.response.CompleteMiniJobResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//public class CompleteMiniJobController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(CompleteMiniJobController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Autowired
//	protected MonsterForUserRetrieveUtils monsterForUserRetrieveUtils;
//
//	@Autowired
//	protected MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil;
//
//	public CompleteMiniJobController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new CompleteMiniJobRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_COMPLETE_MINI_JOB_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final CompleteMiniJobRequestProto reqProto =
//		    ((CompleteMiniJobRequestEvent) event).getCompleteMiniJobRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final Timestamp clientTime = new Timestamp(reqProto.getClientTime());
//		final long userMiniJobId = reqProto.getUserMiniJobUuid();
//
//		final boolean isSpeedUp = reqProto.getIsSpeedUp();
//		final int gemCost = reqProto.getGemCost();
//		final List<UserMonsterCurrentHealthProto> umchpList = reqProto.getUmchpList();
//
//		final CompleteMiniJobResponseProto.Builder resBuilder =
//		    CompleteMiniJobResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(CompleteMiniJobStatus.FAIL_OTHER);
//
//		svcTxManager.beginTransaction();
//		try {
//			// retrieve whatever is necessary from the db
//			// TODO: consider only retrieving user if the request is valid
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(senderProto.getUserUuid());
//			int previousGems = 0;
//
//			final Map<Long, Integer> userMonsterIdToExpectedHealth =
//			    new HashMap<Long, Integer>();
//
//			final boolean legit =
//			    checkLegit(resBuilder, userUuid, user, userMiniJobId, isSpeedUp, gemCost,
//			        umchpList, userMonsterIdToExpectedHealth);
//
//			boolean success = false;
//			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
//
//			if (legit) {
//				previousGems = user.getGems();
//				success =
//				    writeChangesToDB(userUuid, user, userMiniJobId, isSpeedUp, gemCost,
//				        clientTime, userMonsterIdToExpectedHealth, currencyChange);
//			}
//
//			if (success) {
//				resBuilder.setStatus(CompleteMiniJobStatus.SUCCESS);
//			}
//
//			final CompleteMiniJobResponseEvent resEvent =
//			    new CompleteMiniJobResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setCompleteMiniJobResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in CompleteMiniJobController.processRequestEvent", e);
//			}
//
//			if (success) {
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in CompleteMiniJobController.processRequestEvent", e);
//				}
//
//				writeToUserCurrencyHistory(user, userMiniJobId, currencyChange, clientTime,
//				    previousGems);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in CompleteMiniJobController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(CompleteMiniJobStatus.FAIL_OTHER);
//				final CompleteMiniJobResponseEvent resEvent =
//				    new CompleteMiniJobResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setCompleteMiniJobResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in CompleteMiniJobController.processRequestEvent", e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in CompleteMiniJobController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegit( final Builder resBuilder, final String userUuid,
//	    final User user, final long userMiniJobId, final boolean isSpeedUp, final int gemCost,
//	    final List<UserMonsterCurrentHealthProto> umchpList,
//	    final Map<Long, Integer> userMonsterIdToExpectedHealth )
//	{
//
//		// sanity check
//		if (umchpList.isEmpty()
//		    || (0 == userMiniJobId)) {
//			LOG.error("invalid userMonsterUuids (monsters need to be damaged)"
//			    + " or userMiniJobId. userMonsters="
//			    + umchpList
//			    + "\t userMiniJobId="
//			    + userMiniJobId);
//			return false;
//		}
//
//		final List<Long> userMonsterUuids =
//		    MonsterStuffUtils.getUserMonsterUuids(umchpList, userMonsterIdToExpectedHealth);
//		final Map<Long, MonsterForUser> mfuUuidsToUserMonsters =
//		    getMonsterForUserRetrieveUtils().getSpecificOrAllUserMonstersForUser(userUuid,
//		        userMonsterUuids);
//
//		// keep only valid userMonsterUuids another sanity check
//		if (userMonsterUuids.size() != mfuUuidsToUserMonsters.size()) {
//			LOG.warn("some userMonsterUuids client sent are invalid."
//			    + " Keeping valid ones. userMonsterUuids="
//			    + userMonsterUuids
//			    + " mfuUuidsToUserMonsters="
//			    + mfuUuidsToUserMonsters);
//
//			final Set<Long> existing = mfuUuidsToUserMonsters.keySet();
//			MonsterStuffUtils.retainValidMonsterUuids(existing, userMonsterUuids);
//		}
//
//		if (userMonsterUuids.isEmpty()) {
//			LOG.error("no valid user monster ids sent by client");
//			return false;
//		}
//
//		final Collection<Long> userMiniJobUuids = Collections.singleton(userMiniJobId);
//		final Map<Long, MiniJobForUser> idToUserMiniJob =
//		    getMiniJobForUserRetrieveUtil().getSpecificOrAllIdToMiniJobForUser(userUuid,
//		        userMiniJobUuids);
//
//		if (idToUserMiniJob.isEmpty()) {
//			LOG.error("no UserMiniJob exists with id="
//			    + userMiniJobId);
//			resBuilder.setStatus(CompleteMiniJobStatus.FAIL_NO_MINI_JOB_EXISTS);
//			return false;
//		}
//
//		if (isSpeedUp
//		    && !hasEnoughGems(resBuilder, user, gemCost)) {
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean hasEnoughGems( final Builder resBuilder, final User u, final int gemsSpent )
//	{
//		final int userGems = u.getGems();
//		// if user's aggregate gems is < cost, don't allow transaction
//		if (userGems < gemsSpent) {
//			LOG.error("user error: user does not have enough gems. userGems="
//			    + userGems
//			    + "\t gemsSpent="
//			    + gemsSpent);
//			resBuilder.setStatus(CompleteMiniJobStatus.FAIL_INSUFFICIENT_GEMS);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean writeChangesToDB( final String userUuid, final User user,
//	    final long userMiniJobId, final boolean isSpeedUp, final int gemCost,
//	    final Timestamp clientTime, final Map<Long, Integer> userMonsterIdToExpectedHealth,
//	    final Map<String, Integer> currencyChange )
//	{
//
//		LOG.info("updating user's monsters' healths");
//		int numUpdated = UpdateUtils.get()
//		    .updateUserMonstersHealth(userMonsterIdToExpectedHealth);
//		LOG.info("numUpdated="
//		    + numUpdated);
//
//		// number updated is based on INSERT ... ON DUPLICATE KEY UPDATE
//		// so returns 2 if one row was updated, 1 if inserted
//		if (numUpdated > (2 * userMonsterIdToExpectedHealth.size())) {
//			LOG.warn("unexpected error: more than user monsters were"
//			    + " updated. actual numUpdated="
//			    + numUpdated
//			    + "expected: userMonsterIdToExpectedHealth="
//			    + userMonsterIdToExpectedHealth);
//		}
//
//		// update user currency
//		final int gemsChange = -1
//		    * Math.abs(gemCost);
//		final int cashChange = 0;
//		final int oilChange = 0;
//
//		if (isSpeedUp
//		    && !updateUser(user, gemsChange, cashChange, oilChange)) {
//			LOG.error("unexpected error: could not decrement user gems by "
//			    + gemsChange
//			    + ", cash by "
//			    + cashChange
//			    + ", and oil by "
//			    + oilChange);
//			return false;
//		} else {
//			if (0 != gemsChange) {
//				currencyChange.put(MiscMethods.gems, gemsChange);
//			}
//		}
//
//		// update complete time for MiniJobForUser
//		numUpdated = UpdateUtils.get()
//		    .updateMiniJobForUserCompleteTime(userUuid, userMiniJobId, clientTime);
//
//		LOG.info("writeChangesToDB() numUpdated="
//		    + numUpdated);
//
//		return true;
//	}
//
//	private boolean updateUser( final User u, final int gemsChange, final int cashChange,
//	    final int oilChange )
//	{
//		final int numChange =
//		    u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemsChange);
//
//		if (numChange <= 0) {
//			LOG.error("unexpected error: problem with updating user gems,"
//			    + " cash, and oil. gemChange="
//			    + gemsChange
//			    + ", cash= "
//			    + cashChange
//			    + ", oil="
//			    + oilChange
//			    + " user="
//			    + u);
//			return false;
//		}
//		return true;
//	}
//
//	private void writeToUserCurrencyHistory( final User aUser, final long userMiniJobId,
//	    final Map<String, Integer> currencyChange, final Timestamp curTime,
//	    final int previousGems )
//	{
//		final String userUuid = aUser.getId();
//		final String reason = ControllerConstants.UCHRFC__SPED_UP_COMPLETE_MINI_JOB;
//		final StringBuilder detailsSb = new StringBuilder();
//		detailsSb.append("userMiniJobId=");
//		detailsSb.append(userMiniJobId);
//
//		final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> detailsMap = new HashMap<String, String>();
//		final String gems = MiscMethods.gems;
//
//		previousCurrency.put(gems, previousGems);
//		currentCurrency.put(gems, aUser.getGems());
//		reasonsForChanges.put(gems, reason);
//		detailsMap.put(gems, detailsSb.toString());
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);
//
//	}
//
//	public MonsterForUserRetrieveUtils getMonsterForUserRetrieveUtils()
//	{
//		return monsterForUserRetrieveUtils;
//	}
//
//	public void setMonsterForUserRetrieveUtils(
//	    final MonsterForUserRetrieveUtils monsterForUserRetrieveUtils )
//	{
//		this.monsterForUserRetrieveUtils = monsterForUserRetrieveUtils;
//	}
//
//	public MiniJobForUserRetrieveUtil getMiniJobForUserRetrieveUtil()
//	{
//		return miniJobForUserRetrieveUtil;
//	}
//
//	public void setMiniJobForUserRetrieveUtil(
//	    final MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil )
//	{
//		this.miniJobForUserRetrieveUtil = miniJobForUserRetrieveUtil;
//	}
//
//}
