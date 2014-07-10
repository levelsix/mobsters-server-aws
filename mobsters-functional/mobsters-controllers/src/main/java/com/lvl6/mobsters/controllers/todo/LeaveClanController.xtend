package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanResponseProto.LeaveClanStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.LeaveClanRequestEvent;
import com.lvl6.mobsters.events.response.LeaveClanResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class LeaveClanController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(LeaveClanController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public LeaveClanController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new LeaveClanRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_LEAVE_CLAN_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final LeaveClanRequestProto reqProto =
		    ((LeaveClanRequestEvent) event).getLeaveClanRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();

		final LeaveClanResponseProto.Builder resBuilder = LeaveClanResponseProto.newBuilder();
		resBuilder.setStatus(LeaveClanStatus.FAIL_OTHER);
		resBuilder.setSender(senderProto);

		int clanId = 0;
		if (senderProto.hasClan()
		    && (null != senderProto.getClan())) {
			clanId = senderProto.getClan()
			    .getClanId();
		}

		// maybe should get clan lock instead of locking person
		// but going to modify user, so lock user. however maybe locking is not
		// necessary
		boolean lockedClan = false;
		if (0 != clanId) {
			lockedClan = getLocker().lockClan(clanId);
		}/*
		 * else { server.lockPlayer(senderProto.getUserUuid(),
		 * this.getClass().getSimpleName()); }
		 */
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			final Clan clan = ClanRetrieveUtils.getClanWithId(clanId);

			final List<Integer> clanOwnerIdList = new ArrayList<Integer>();
			final boolean legitLeave =
			    checkLegitLeave(resBuilder, lockedClan, user, clan, clanOwnerIdList);

			boolean success = false;
			if (legitLeave) {
				final int clanOwnerId = clanOwnerIdList.get(0);
				success = writeChangesToDB(user, clan, clanOwnerId);
			}

			final LeaveClanResponseEvent resEvent = new LeaveClanResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			// only write to user if failed
			if (!success) {
				resEvent.setLeaveClanResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in LeaveClanController.processRequestEvent", e);
				}

			} else {
				// only write to clan if success
				resBuilder.setStatus(LeaveClanStatus.SUCCESS);
				resEvent.setLeaveClanResponseProto(resBuilder.build());
				server.writeClanEvent(resEvent, clanId);
				// this works for other clan members, but not for the person
				// who left (they see the message when they join a clan, reenter
				// clan house
				// notifyClan(user, clan);
			}
		} catch (final Exception e) {
			LOG.error("exception in LeaveClan processEvent", e);
			try {
				resBuilder.setStatus(LeaveClanStatus.FAIL_OTHER);
				final LeaveClanResponseEvent resEvent = new LeaveClanResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setLeaveClanResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error("fatal exception in LeaveClanController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in LeaveClan processEvent", e);
			}
		} finally {
			if ((0 != clanId)
			    && lockedClan) {
				getLocker().unlockClan(clanId);
			}/*
			 * else { server.unlockPlayer(senderProto.getUserUuid(),
			 * this.getClass().getSimpleName()); }
			 */
		}
	}

	private boolean checkLegitLeave( final Builder resBuilder, final boolean lockedClan,
	    final User user, final Clan clan, final List<Integer> clanOwnerIdList )
	{

		if (!lockedClan) {
			LOG.error("couldn't obtain clan lock");
			return false;
		}
		if ((user == null)
		    || (clan == null)) {
			LOG.error("user is null");
			return false;
		}
		if (user.getClanId() != clan.getId()) {
			resBuilder.setStatus(LeaveClanStatus.FAIL_NOT_IN_CLAN);
			LOG.error("user's clan id is "
			    + user.getClanId()
			    + ", clan id is "
			    + clan.getId());
			return false;
		}

		final int clanId = user.getClanId();
		final List<String> statuses = new ArrayList<String>();
		statuses.add(UserClanStatus.LEADER.name());
		final List<Integer> userUuids = RetrieveUtils.userClanRetrieveUtils()
		    .getUserUuidsWithStatuses(clanId, statuses);
		// should just be one id
		int clanOwnerId = 0;
		if ((null != userUuids)
		    && !userUuids.isEmpty()) {
			clanOwnerId = userUuids.get(0);
		}

		if (clanOwnerId == user.getId()) {
			final List<Integer> clanIdList = Collections.singletonList(clanId);
			// add in the other "in clan" statuses with the existing leader
			// status
			statuses.add(UserClanStatus.JUNIOR_LEADER.name());
			statuses.add(UserClanStatus.CAPTAIN.name());
			statuses.add(UserClanStatus.MEMBER.name());
			final Map<Integer, Integer> clanIdToSize = RetrieveUtils.userClanRetrieveUtils()
			    .getClanSizeForClanUuidsAndStatuses(clanIdList, statuses);
			final int userClanMembersInClan = clanIdToSize.get(clanId);
			if (userClanMembersInClan > 1) {
				resBuilder.setStatus(LeaveClanStatus.FAIL_OWNER_OF_CLAN_WITH_OTHERS_STILL_IN);
				LOG.error("user is owner and he's not alone in clan, can't leave without switching ownership. user clan members are "
				    + userClanMembersInClan);
				return false;
			}
		}

		clanOwnerIdList.add(clanOwnerId);
		return true;
	}

	private boolean writeChangesToDB( final User user, final Clan clan, final int clanOwnerId )
	{
		final String userUuid = user.getId();
		final int clanId = clan.getId();

		if (userUuid == clanOwnerId) {
			final List<Integer> userUuids = RetrieveUtils.userClanRetrieveUtils()
			    .getUserUuidsRelatedToClan(clanId);
			deleteClan(clan, userUuids, user);
		} else {
			if (!DeleteUtils.get()
			    .deleteUserClan(userUuid, clanId)) {
				LOG.error("problem with deleting user clan for "
				    + user
				    + " and clan "
				    + clan);
			}
			if (!user.updateRelativeCoinsAbsoluteClan(0, null)) {
				LOG.error("problem with making clanid for user null");
			}
		}
		return true;
	}

	private void deleteClan( final Clan clan, final List<Integer> userUuids, final User user )
	{
		if (!user.updateRelativeCoinsAbsoluteClan(0, null)) {
			LOG.error("problem with marking clan id null for users with ids in "
			    + userUuids);
		} else {
			if (!DeleteUtils.get()
			    .deleteUserClanDataRelatedToClanId(clan.getId(), userUuids.size())) {
				LOG.error("problem with deleting user clan data for clan with id "
				    + clan.getId());
			} else {
				if (!DeleteUtils.get()
				    .deleteClanWithClanId(clan.getId())) {
					LOG.error("problem with deleting clan with id "
					    + clan.getId());
				}
			}
		}
	}

	/*
	 * private void notifyClan(User aUser, Clan aClan) { int clanId =
	 * aClan.getId();
	 * 
	 * int level = aUser.getLevel(); String deserter = aUser.getName();
	 * Notification aNote = new Notification();
	 * 
	 * aNote.setAsUserLeftClan(level, deserter);
	 * MiscMethods.writeClanApnsNotification(aNote, server, clanId); }
	 */

}
