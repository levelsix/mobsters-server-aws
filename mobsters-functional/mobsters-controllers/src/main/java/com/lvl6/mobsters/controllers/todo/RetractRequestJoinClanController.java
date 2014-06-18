package com.lvl6.mobsters.controllers.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanResponseProto.RetractRequestJoinClanStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.RetractRequestJoinClanRequestEvent;
import com.lvl6.mobsters.events.response.RetractRequestJoinClanResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class RetractRequestJoinClanController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(RetractRequestJoinClanController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public RetractRequestJoinClanController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RetractRequestJoinClanRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_RETRACT_REQUEST_JOIN_CLAN_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final RetractRequestJoinClanRequestProto reqProto =
		    ((RetractRequestJoinClanRequestEvent) event).getRetractRequestJoinClanRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int clanId = reqProto.getClanUuid();

		final RetractRequestJoinClanResponseProto.Builder resBuilder =
		    RetractRequestJoinClanResponseProto.newBuilder();
		resBuilder.setStatus(RetractRequestJoinClanStatus.FAIL_OTHER);
		resBuilder.setSender(senderProto);
		resBuilder.setClanId(clanId);

		boolean lockedClan = false;
		if (0 != clanId) {
			lockedClan = getLocker().lockClan(clanId);
		}
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			final Clan clan = ClanRetrieveUtils.getClanWithId(clanId);

			final boolean legitRetract = checkLegitRequest(resBuilder, lockedClan, user, clan);

			boolean success = false;
			if (legitRetract) {
				success = writeChangesToDB(user, clanId);
			}

			if (success) {
				resBuilder.setStatus(RetractRequestJoinClanStatus.SUCCESS);
			}

			final RetractRequestJoinClanResponseEvent resEvent =
			    new RetractRequestJoinClanResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setRetractRequestJoinClanResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in RetractRequestJoinClanController.processRequestEvent",
				    e);
			}

			if (success) {
				server.writeClanEvent(resEvent, clan.getId());
			}

		} catch (final Exception e) {
			LOG.error("exception in RetractRequestJoinClan processEvent", e);
			try {
				resBuilder.setStatus(RetractRequestJoinClanStatus.FAIL_OTHER);
				final RetractRequestJoinClanResponseEvent resEvent =
				    new RetractRequestJoinClanResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setRetractRequestJoinClanResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in RetractRequestJoinClanController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in RetractRequestJoinClan processEvent", e);
			}
		} finally {
			if ((0 != clanId)
			    && lockedClan) {
				getLocker().unlockClan(clanId);
			}
		}
	}

	private boolean checkLegitRequest( final Builder resBuilder, final boolean lockedClan,
	    final User user, final Clan clan )
	{

		if (!lockedClan) {
			LOG.error("couldn't obtain clan lock");
			return false;
		}
		if ((user == null)
		    || (clan == null)) {
			resBuilder.setStatus(RetractRequestJoinClanStatus.FAIL_OTHER);
			LOG.error("user is "
			    + user
			    + ", clan is "
			    + clan);
			return false;
		}
		if (user.getClanId() > 0) {
			resBuilder.setStatus(RetractRequestJoinClanStatus.FAIL_ALREADY_IN_CLAN);
			LOG.error("user is already in clan with id "
			    + user.getClanId());
			return false;
		}
		final ClanForUser uc = RetrieveUtils.userClanRetrieveUtils()
		    .getSpecificUserClan(user.getId(), clan.getId());
		if ((uc == null)
		    || !UserClanStatus.REQUESTING.name()
		        .equals(uc.getStatus())) {
			resBuilder.setStatus(RetractRequestJoinClanStatus.FAIL_DID_NOT_REQUEST);
			LOG.error("user clan request has not been filed");
			return false;
		}
		return true;
	}

	private boolean writeChangesToDB( final User user, final int clanId )
	{
		if (!DeleteUtils.get()
		    .deleteUserClan(user.getId(), clanId)) {
			LOG.error("problem with deleting user clan data for user "
			    + user
			    + ", and clan id "
			    + clanId);
			return false;
		}
		return true;
	}

}
