package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.TimeUtils;
import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan;
import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsResponseProto.RecordClanRaidStatsStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.RecordClanRaidStatsRequestEvent;
import com.lvl6.mobsters.events.response.RecordClanRaidStatsResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class RecordClanRaidStatsController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(RecordClanRaidStatsController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected HazelcastPvpUtil hazelcastPvpUtil;

	@Autowired
	protected TimeUtils timeUtils;

	public RecordClanRaidStatsController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RecordClanRaidStatsRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_RECORD_CLAN_RAID_STATS_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final RecordClanRaidStatsRequestProto reqProto =
		    ((RecordClanRaidStatsRequestEvent) event).getRecordClanRaidStatsRequestProto();
		final long startTime = System.nanoTime();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int clanId = reqProto.getClanUuid(); // should probably set it to
												   // the
		// senderProto.clanId
		final Timestamp now = new Timestamp(reqProto.getClientTime());

		final RecordClanRaidStatsResponseProto.Builder resBuilder =
		    RecordClanRaidStatsResponseProto.newBuilder();
		resBuilder.setStatus(RecordClanRaidStatsStatus.FAIL_OTHER);
		resBuilder.setSender(senderProto);

		final long endTimeAfterLock;
		final long endTimeAfterCheckLegitRequest;
		final long endTimeAfterWriteChangesToDb;
		final long endTimeAfterWriteEvent;
		final long endTimeAfterWriteClanEvent;
		final long endTimeAfterUnlock;
		// OUTLINE:
		// get the clan lock; get the clan raid object for the clan;
		// If does exist, record it and then delete it; same for clan user
		// stuff.
		// Else, do nothing.

		// will hold stuff to be stored to history (but not necessary to do so)
		ClanEventPersistentForClan clanEvent = null;
		final Map<Integer, ClanEventPersistentForUser> userIdToClanUserInfo =
		    new HashMap<Integer, ClanEventPersistentForUser>();
		boolean lockedClan = true;
		if (0 != clanId) {
			lockedClan = getLocker().lockClan(clanId);
		}
		try {
			endTimeAfterLock = System.nanoTime();
			// User user =
			// RetrieveUtils.userRetrieveUtils().getUserById(userUuid);
			clanEvent =
			    ClanEventPersistentForClanRetrieveUtils.getPersistentEventForClanId(clanId);
			final boolean legitRequest =
			    checkLegitRequest(resBuilder, lockedClan, senderProto, userUuid, clanId,
			        clanEvent);

			endTimeAfterCheckLegitRequest = System.nanoTime();

			final RecordClanRaidStatsResponseEvent resEvent =
			    new RecordClanRaidStatsResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setRecordClanRaidStatsResponseProto(resBuilder.build());

			boolean success = false;
			if (legitRequest) {
				success =
				    writeChangesToDB(userUuid, clanId, now, clanEvent, userIdToClanUserInfo);
			}

			endTimeAfterWriteChangesToDb = System.nanoTime();
			if (success) {
			}
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in RecordClanRaidStatsController.processRequestEvent", e);
			}
			endTimeAfterWriteEvent = System.nanoTime();

			if (legitRequest) {
				// only write to the user if the request was valid
				server.writeClanEvent(resEvent, clanId);
			}
			endTimeAfterWriteClanEvent = System.nanoTime();

			LOG.info("startTime to afterLock took ~{}ms",
			    (endTimeAfterLock - startTime) / 1000000);
			LOG.info("afterLock to afterCheckLegitRequest took ~{}ms",
			    (endTimeAfterCheckLegitRequest - endTimeAfterLock) / 1000000);
			LOG.info("afterCheckLegitRequest to endTimeAfterWriteChangesToDb took ~{}ms",
			    (endTimeAfterWriteChangesToDb - endTimeAfterCheckLegitRequest) / 1000000);
			LOG.info("endTimeAfterWriteChangesToDb to endTimeAfterWriteEvent took ~{}ms",
			    (endTimeAfterWriteEvent - endTimeAfterWriteChangesToDb) / 1000000);
			LOG.info("endTimeAfterWriteEvent to endTimeAfterWriteClanEvent took ~{}ms",
			    (endTimeAfterWriteClanEvent - endTimeAfterWriteEvent) / 1000000);

		} catch (final Exception e) {
			// errorless = false;
			try {
				resBuilder.setStatus(RecordClanRaidStatsStatus.FAIL_OTHER);
				final RecordClanRaidStatsResponseEvent resEvent =
				    new RecordClanRaidStatsResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setRecordClanRaidStatsResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in RecordClanRaidStatsController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception in RecordClanRaidStats processEvent", e);
			}
		} finally {

			if ((0 != clanId)
			    && lockedClan) {
				getLocker().unlockClan(clanId);
			}
			endTimeAfterUnlock = System.nanoTime();
			LOG.info("startTime to endTimeAfterUnlock took ~{}ms",
			    (startTime - endTimeAfterUnlock) / 1000000);
		}

		// //not necessary, can just delete this part (purpose is to record in
		// detail, a user's
		// //contribution to a clan raid) on the clan raid stage monster level
		// try {
		// if (errorless && null != clanEvent &&
		// !userIdToClanUserInfo.isEmpty()) {
		// int numInserted =
		// InsertUtils.get().insertIntoClanEventPersistentForUserHistoryDetail(
		// now, userIdToClanUserInfo, clanEvent);
		// LOG.error("num raid detail inserted = " + numInserted +
		// "\t should be " +
		// userIdToClanUserInfo.size());
		// }
		// } catch (Exception e) {
		// LOG.warn("could not record more details about clan raid", e);
		// }
	}

	private boolean checkLegitRequest( final Builder resBuilder, final boolean lockedClan,
	    final MinimumUserProto mupfc, final String userUuid, final int clanId,
	    final ClanEventPersistentForClan clanEvent )
	{
		if (!lockedClan) {
			LOG.error("couldn't obtain clan lock");
			return false;
		}
		if ((0 == clanId)
		    || (0 == userUuid)
		    || (null == clanEvent)) {
			LOG.error("not in clan. user is "
			    + mupfc
			    + "\t or clanId invalid id="
			    + clanId
			    + "\t or clanEvent is null clanEvent="
			    + clanEvent);
			return false;
		}

		resBuilder.setStatus(RecordClanRaidStatsStatus.SUCCESS);
		return true;
	}

	private boolean writeChangesToDB( final String userUuid, final int clanId,
	    final Timestamp now, final ClanEventPersistentForClan clanEvent,
	    final Map<Integer, ClanEventPersistentForUser> userIdToClanUserInfo )
	{
		final int clanEventPersistentId = clanEvent.getClanEventPersistentId();
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
		final boolean won = false;

		// record whatever is in the ClanEventPersistentForClan
		int numInserted =
		    InsertUtils.get()
		        .insertIntoClanEventPersistentForClanHistory(clanId, now,
		            clanEventPersistentId, crId, crsId, stageStartTime, crsmId,
		            stageMonsterStartTime, won);

		// clan_event_persistent_for_clan_history
		LOG.info("rows inserted into clan raid info for clan history (should be 1): "
		    + numInserted);
		// get all the clan raid info for the users, and then delete them
		final Map<Integer, ClanEventPersistentForUser> clanUserInfo =
		    ClanEventPersistentForUserRetrieveUtils.getPersistentEventUserInfoForClanId(clanId);

		// delete clan info for clan raid
		DeleteUtils.get()
		    .deleteClanEventPersistentForClan(clanId);

		if ((null != clanUserInfo)
		    && !clanUserInfo.isEmpty()) {
			numInserted = InsertUtils.get()
			    .insertIntoCepfuRaidHistory(clanEventPersistentId, now, clanUserInfo);
			// clan_event_persistent_for_user_history
			LOG.info("rows inserted into clan raid info for user history (should be "
			    + clanUserInfo.size()
			    + "): "
			    + numInserted);

			// delete clan user info for clan raid
			final List<Integer> userIdList = new ArrayList<Integer>(clanUserInfo.keySet());
			DeleteUtils.get()
			    .deleteClanEventPersistentForUsers(userIdList);

			userIdToClanUserInfo.putAll(clanUserInfo);
		}
		return true;
	}

	protected HazelcastPvpUtil getHazelcastPvpUtil()
	{
		return hazelcastPvpUtil;
	}

	protected void setHazelcastPvpUtil( final HazelcastPvpUtil hazelcastPvpUtil )
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

}
