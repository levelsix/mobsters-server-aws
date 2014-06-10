package com.lvl6.mobsters.controllers.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpResponseProto.LevelUpStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.LevelUpRequestEvent;
import com.lvl6.mobsters.events.response.LevelUpResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
public class LevelUpController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(LevelUpController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public LevelUpController()
	{
		numAllocatedThreads = 2;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new LevelUpRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_LEVEL_UP_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final LevelUpRequestProto reqProto =
		    ((LevelUpRequestEvent) event).getLevelUpRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int newLevel = reqProto.getNextLevel();

		final LevelUpResponseProto.Builder resBuilder = LevelUpResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(LevelUpStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);
			final boolean legitLevelUp = checkLegitLevelUp(resBuilder, user);

			boolean success = false;
			if (legitLevelUp) {
				success = writeChangesToDB(user, newLevel);
			}

			if (success) {
				resBuilder.setStatus(LevelUpStatus.SUCCESS);
			}

			final LevelUpResponseEvent resEvent =
			    new LevelUpResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			final LevelUpResponseProto resProto = resBuilder.build();
			resEvent.setLevelUpResponseProto(resProto);
			getEventWriter().handleEvent(resEvent);

			if (success) {
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
				        null);
				resEventUpdate.setTag(event.getTag());
				getEventWriter().handleEvent(resEventUpdate);
			}

		} catch (final Exception e) {
			LOG.error("exception in LevelUpController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(LevelUpStatus.FAIL_OTHER);
				final LevelUpResponseEvent resEvent = new LevelUpResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setLevelUpResponseProto(resBuilder.build());
				getEventWriter().handleEvent(resEvent);
			} catch (final Exception e2) {
				LOG.error("exception2 in LevelUpController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean writeChangesToDB( final User user, final int newLevel )
	{
		if (!user.updateLevel(newLevel, true)) {
			LOG.error("problem in changing the user's level");
			return false;
		}
		return true;
	}

	private boolean checkLegitLevelUp( final Builder resBuilder, final User user )
	{
		if (null == user) {
			LOG.error("user is null");
			return false;
		}
		return true;
	}

}
