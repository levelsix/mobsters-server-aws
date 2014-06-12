package com.lvl6.mobsters.controllers;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto.RemoveMonsterFromBattleTeamStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.RemoveMonsterFromBattleTeamRequestEvent;
import com.lvl6.mobsters.events.response.RemoveMonsterFromBattleTeamResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.monster.MonsterService;

@Component
public class RemoveMonsterFromBattleTeamController extends EventController
{

	private static Logger LOG =
		LoggerFactory.getLogger(RemoveMonsterFromBattleTeamController.class);

	@Autowired
	protected MonsterService monsterService;

	/*
	 * @Autowired protected EventWriter eventWriter;
	 */

	public RemoveMonsterFromBattleTeamController()
	{}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RemoveMonsterFromBattleTeamRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_REMOVE_MONSTER_FROM_BATTLE_TEAM_EVENT;
	}

	@Override
	protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
	{
		final RemoveMonsterFromBattleTeamRequestProto reqProto =
			((RemoveMonsterFromBattleTeamRequestEvent) event).getRemoveMonsterFromBattleTeamRequestProto();
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userIdString = senderProto.getUserUuid();
		final String userMonsterId = reqProto.getUserMonsterUuid();

		// prepare to send response back to client
		RemoveMonsterFromBattleTeamResponseProto.Builder responseBuilder =
			RemoveMonsterFromBattleTeamResponseProto.newBuilder();
		responseBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.FAIL_OTHER);
		responseBuilder.setSender(senderProto);
		RemoveMonsterFromBattleTeamResponseEvent resEvent =
			new RemoveMonsterFromBattleTeamResponseEvent(userIdString);
		resEvent.setTag(event.getTag());

		Set<String> monsterForUserIds = new HashSet<String>();
		if (StringUtils.hasText(userMonsterId)) {
			// Check values client sent for syntax errors. Call service only if
			// syntax checks out ok; prepare arguments for service
			responseBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.SUCCESS);
		}
		// call service if syntax is ok
		if (responseBuilder.getStatus() == RemoveMonsterFromBattleTeamStatus.SUCCESS) {
			try {
				monsterService.clearMonstersForUserTeamSlot(userIdString, monsterForUserIds);
			} catch (Exception e) {
				LOG.error(
					"exception in RemoveMonsterFromBattleTeamController processEvent when calling userService",
					e);
				responseBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.FAIL_OTHER);
			}
		}

		resEvent.setRemoveMonsterFromBattleTeamResponseProto(responseBuilder.build());

		// write to client
		LOG.info("Writing event: "
			+ resEvent);
		try {
			eventWriter.writeEvent(resEvent);
		} catch (Exception e) {
			LOG.error(
				"fatal exception in RemoveMonsterFromBattleTeamController processRequestEvent",
				e);
		}

		// TODO: FIGURE OUT IF THIS IS STILL NEEDED
		// game center id might have changed
		// null PvpLeagueFromUser means will pull from a cache instead
		// UpdateClientUserResponseEvent resEventUpdate =
		// CreateEventProtoUtil.createUpdateClientUserResponseEvent(null, null, user, null, null);
		// resEventUpdate.setTag(event.getTag());
		// eventWriter.writeEvent(resEventUpdate);
	}

	// private void failureCase(
	// RequestEvent event,
	// EventsToDispatch eventWriter,
	// String userId,
	// RemoveMonsterFromBattleTeamResponseProto.Builder resBuilder )
	// {
	// eventWriter.clearResponses();
	// resBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.FAIL_OTHER);
	// RemoveMonsterFromBattleTeamResponseEvent resEvent = new
	// RemoveMonsterFromBattleTeamResponseEvent(userId);
	// resEvent.setTag(event.getTag());
	// resEvent.setRemoveMonsterFromBattleTeamResponseProto(resBuilder.build());
	// eventWriter.writeEvent(resEvent);
	// }

	public MonsterService getMonsterService()
	{
		return monsterService;
	}

	public void setMonsterService( MonsterService monsterService )
	{
		this.monsterService = monsterService;
	}

}
