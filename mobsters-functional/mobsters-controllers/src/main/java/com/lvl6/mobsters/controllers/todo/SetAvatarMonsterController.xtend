package com.lvl6.mobsters.controllers.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class SetAvatarMonsterController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(SetAvatarMonsterController.class);

	public SetAvatarMonsterController()
	{
		numAllocatedThreads = 1;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new SetAvatarMonsterRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_SET_AVATAR_MONSTER_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final SetAvatarMonsterRequestProto reqProto =
		    ((SetAvatarMonsterRequestEvent) event).getSetAvatarMonsterRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int monsterId = reqProto.getMonsterUuid();

		final SetAvatarMonsterResponseProto.Builder resBuilder =
		    SetAvatarMonsterResponseProto.newBuilder();
		resBuilder.setSender(senderProto);

		// server.lockPlayer(senderProto.getUserUuid(),
		// this.getClass().getSimpleName());
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());

			// boolean isDifferent =
			// checkIfNewTokenDifferent(user.getAvatarMonster(), gameCenterId);

			final boolean legit = monsterId > 0;

			boolean successful = false;
			if (legit) {
				successful = writeChangesToDb(user, monsterId);
			} else {
				LOG.error("can't unset avatarMonsterId");
			}

			if (successful) {
				resBuilder.setStatus(SetAvatarMonsterStatus.SUCCESS);
			} else {
				resBuilder.setStatus(SetAvatarMonsterStatus.FAIL_OTHER);
			}

			final SetAvatarMonsterResponseProto resProto = resBuilder.build();
			final SetAvatarMonsterResponseEvent resEvent =
			    new SetAvatarMonsterResponseEvent(senderProto.getUserUuid());
			resEvent.setSetAvatarMonsterResponseProto(resProto);
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in SetAvatarMonsterController.processRequestEvent",
				    e);
			}

			if (successful) {
				// game center id might have changed
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
					LOG.error(
					    "fatal exception in SetAvatarMonsterController.processRequestEvent", e);
				}
			}

		} catch (final Exception e) {
			LOG.error("exception in SetAvatarMonsterController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(SetAvatarMonsterStatus.FAIL_OTHER);
				final SetAvatarMonsterResponseEvent resEvent =
				    new SetAvatarMonsterResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setSetAvatarMonsterResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in SetAvatarMonsterController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in SetAvatarMonsterController processEvent", e);
			}
		} finally {
			// server.unlockPlayer(senderProto.getUserUuid(),
			// this.getClass().getSimpleName());
		}
	}

	private boolean writeChangesToDb( final User user, final int avatarMonsterId )
	{
		try {
			if (!user.updateAvatarMonsterId(avatarMonsterId)) {
				LOG.error("problem with setting user's avatarMonsterId to "
				    + avatarMonsterId);
				return false;
			}
			return true;
		} catch (final Exception e) {
			LOG.error("problem with updating user avatar monster id. user="
			    + user
			    + "\t avatarMonsterId="
			    + avatarMonsterId);
		}

		return false;
	}
}
