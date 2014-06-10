package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobRequestProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto.RedeemMiniJobStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.RedeemMiniJobRequestEvent;
import com.lvl6.mobsters.events.response.RedeemMiniJobResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.info.MiniJob;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.server.EventController;

@Component
public class RedeemMiniJobController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(RedeemMiniJobController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected MonsterForUserRetrieveUtils monsterForUserRetrieveUtils;

	@Autowired
	protected MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil;

	public RedeemMiniJobController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RedeemMiniJobRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_REDEEM_MINI_JOB_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final RedeemMiniJobRequestProto reqProto =
		    ((RedeemMiniJobRequestEvent) event).getRedeemMiniJobRequestProto();
		LOG.info("reqProto="
		    + reqProto);

		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();

		final String userUuid = senderProto.getUserUuid();
		final Date now = new Date(reqProto.getClientTime());
		final Timestamp clientTime = new Timestamp(reqProto.getClientTime());
		final long userMiniJobId = reqProto.getUserMiniJobUuid();

		final RedeemMiniJobResponseProto.Builder resBuilder =
		    RedeemMiniJobResponseProto.newBuilder();
		resBuilder.setSender(senderResourcesProto);
		resBuilder.setStatus(RedeemMiniJobStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			// retrieve whatever is necessary from the db
			// TODO: consider only retrieving user if the request is valid
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			final List<MiniJobForUser> mjfuList = new ArrayList<MiniJobForUser>();

			final boolean legit =
			    checkLegit(resBuilder, userUuid, user, userMiniJobId, mjfuList);

			boolean success = false;
			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
			final Map<String, Integer> previousCurrency = new HashMap<String, Integer>();
			if (legit) {
				final MiniJobForUser mjfu = mjfuList.get(0);
				success =
				    writeChangesToDB(resBuilder, userUuid, user, userMiniJobId, mjfu, now,
				        clientTime, currencyChange, previousCurrency);
			}

			if (success) {
				resBuilder.setStatus(RedeemMiniJobStatus.SUCCESS);
			}

			final RedeemMiniJobResponseEvent resEvent =
			    new RedeemMiniJobResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setRedeemMiniJobResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in RedeemMiniJobController.processRequestEvent", e);
			}

			if (success) {
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error("fatal exception in RedeemMiniJobController.processRequestEvent",
					    e);
				}

				// TODO: track the MiniJobForUser history
				writeToUserCurrencyHistory(user, userMiniJobId, currencyChange, clientTime,
				    previousCurrency);
			}

		} catch (final Exception e) {
			LOG.error("exception in RedeemMiniJobController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(RedeemMiniJobStatus.FAIL_OTHER);
				final RedeemMiniJobResponseEvent resEvent =
				    new RedeemMiniJobResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setRedeemMiniJobResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in RedeemMiniJobController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in RedeemMiniJobController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegit( final Builder resBuilder, final String userUuid,
	    final User user, final long userMiniJobId, final List<MiniJobForUser> mjfuList )
	{

		final Collection<Long> userMiniJobUuids = Collections.singleton(userMiniJobId);
		final Map<Long, MiniJobForUser> idToUserMiniJob =
		    getMiniJobForUserRetrieveUtil().getSpecificOrAllIdToMiniJobForUser(userUuid,
		        userMiniJobUuids);

		if (idToUserMiniJob.isEmpty()) {
			LOG.error("no UserMiniJob exists with id="
			    + userMiniJobId);
			resBuilder.setStatus(RedeemMiniJobStatus.FAIL_NO_MINI_JOB_EXISTS);
			return false;
		}

		final MiniJobForUser mjfu = idToUserMiniJob.get(userMiniJobId);
		if (null == mjfu.getTimeCompleted()) {
			// sanity check
			LOG.error("MiniJobForUser incomplete: "
			    + mjfu);
			return false;
		}

		// sanity check
		final int miniJobId = mjfu.getMiniJobId();
		final MiniJob mj = MiniJobRetrieveUtils.getMiniJobForMiniJobId(miniJobId);
		if (null == mj) {
			LOG.error("no MiniJob exists with id="
			    + miniJobId
			    + "\t invalid MiniJobForUser="
			    + mjfu);
			resBuilder.setStatus(RedeemMiniJobStatus.FAIL_NO_MINI_JOB_EXISTS);
			return false;
		}

		mjfuList.add(mjfu);
		return true;
	}

	private boolean writeChangesToDB( final Builder resBuilder, final String userUuid,
	    final User user, final long userMiniJobId, final MiniJobForUser mjfu, final Date now,
	    final Timestamp clientTime, final Map<String, Integer> currencyChange,
	    final Map<String, Integer> previousCurrency )
	{
		final int miniJobId = mjfu.getMiniJobId();
		final MiniJob mj = MiniJobRetrieveUtils.getMiniJobForMiniJobId(miniJobId);

		final int prevGems = user.getGems();
		final int prevCash = user.getCash();
		final int prevOil = user.getOil();

		// update user currency
		final int gemsChange = mj.getGemReward();
		final int cashChange = mj.getCashReward();
		final int oilChange = mj.getOilReward();
		final int monsterIdReward = mj.getMonsterIdReward();

		if (!updateUser(user, gemsChange, cashChange, oilChange)) {
			LOG.error("unexpected error: could not decrement user gems by "
			    + gemsChange
			    + ", cash by "
			    + cashChange
			    + ", and oil by "
			    + oilChange);
			return false;
		} else {
			if (0 != gemsChange) {
				currencyChange.put(MiscMethods.gems, gemsChange);
				previousCurrency.put(MiscMethods.gems, prevGems);
			}
			if (0 != cashChange) {
				currencyChange.put(MiscMethods.cash, cashChange);
				previousCurrency.put(MiscMethods.cash, prevCash);
			}
			if (0 != oilChange) {
				currencyChange.put(MiscMethods.oil, oilChange);
				previousCurrency.put(MiscMethods.oil, prevOil);
			}
		}

		// give the user the monster if he got one
		if (0 != monsterIdReward) {
			final StringBuilder mfusopB = new StringBuilder();
			mfusopB.append(ControllerConstants.MFUSOP__MINI_JOB);
			mfusopB.append(" ");
			mfusopB.append(miniJobId);
			final String mfusop = mfusopB.toString();
			final Map<Integer, Integer> monsterIdToNumPieces = new HashMap<Integer, Integer>();
			monsterIdToNumPieces.put(monsterIdReward, 1);

			final List<FullUserMonsterProto> newOrUpdated =
			    MonsterStuffUtils.updateUserMonsters(userUuid, monsterIdToNumPieces, mfusop,
			        now);
			final FullUserMonsterProto fump = newOrUpdated.get(0);
			resBuilder.setFump(fump);
		}

		// delete the user mini job
		final int numDeleted = DeleteUtils.get()
		    .deleteMiniJobForUser(userMiniJobId);
		LOG.info("userMiniJob numDeleted="
		    + numDeleted);

		return true;
	}

	private boolean updateUser( final User u, final int gemsChange, final int cashChange,
	    final int oilChange )
	{
		final int numChange =
		    u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemsChange);

		if (numChange <= 0) {
			LOG.error("unexpected error: problem with updating user gems,"
			    + " cash, and oil. gemChange="
			    + gemsChange
			    + ", cash= "
			    + cashChange
			    + ", oil="
			    + oilChange
			    + " user="
			    + u);
			return false;
		}
		return true;
	}

	private void writeToUserCurrencyHistory( final User aUser, final long userMiniJobId,
	    final Map<String, Integer> currencyChange, final Timestamp curTime,
	    final Map<String, Integer> previousCurrency )
	{
		final String userUuid = aUser.getId();
		final String reason = ControllerConstants.UCHRFC__SPED_UP_COMPLETE_MINI_JOB;
		final StringBuilder detailsSb = new StringBuilder();
		detailsSb.append("userMiniJobId=");
		detailsSb.append(userMiniJobId);

		final Map<String, Integer> currentCurrency = new HashMap<String, Integer>();
		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
		final Map<String, String> detailsMap = new HashMap<String, String>();
		final String gems = MiscMethods.gems;

		currentCurrency.put(gems, aUser.getGems());
		reasonsForChanges.put(gems, reason);
		detailsMap.put(gems, detailsSb.toString());

		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
		    previousCurrency, currentCurrency, reasonsForChanges, detailsMap);

	}

	public MonsterForUserRetrieveUtils getMonsterForUserRetrieveUtils()
	{
		return monsterForUserRetrieveUtils;
	}

	public void setMonsterForUserRetrieveUtils(
	    final MonsterForUserRetrieveUtils monsterForUserRetrieveUtils )
	{
		this.monsterForUserRetrieveUtils = monsterForUserRetrieveUtils;
	}

	public MiniJobForUserRetrieveUtil getMiniJobForUserRetrieveUtil()
	{
		return miniJobForUserRetrieveUtil;
	}

	public void setMiniJobForUserRetrieveUtil(
	    final MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil )
	{
		this.miniJobForUserRetrieveUtil = miniJobForUserRetrieveUtil;
	}

}
