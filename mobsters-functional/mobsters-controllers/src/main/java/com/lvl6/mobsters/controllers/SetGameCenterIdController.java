package com.lvl6.mobsters.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.lvl6.events.RequestEvent;
import com.lvl6.events.request.SetGameCenterIdRequestEvent;
import com.lvl6.events.response.SetGameCenterIdResponseEvent;
import com.lvl6.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto.SetGameCenterIdStatus;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.server.EventWriter;

@Component
public class SetGameCenterIdController extends EventController {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected EventWriter eventWriter;

	public SetGameCenterIdController() {
	}

	@Override
	public RequestEvent createRequestEvent() {
		return new SetGameCenterIdRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType() {
		return EventProtocolRequest.C_SET_GAME_CENTER_ID_EVENT;
	}

	@Override
	protected void processRequestEvent(RequestEvent event) throws Exception {
		SetGameCenterIdRequestProto reqProto = ((SetGameCenterIdRequestEvent) event).getSetGameCenterIdRequestProto();

		MinimumUserProto senderProto = reqProto.getSender();
		String userId = senderProto.getUserUuid();
		String gameCenterId = reqProto.getGameCenterId();
		if (gameCenterId != null && gameCenterId.isEmpty())
			gameCenterId = null;

		SetGameCenterIdResponseProto.Builder resBuilder = SetGameCenterIdResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		if (null != gameCenterId) {
			resBuilder.setGameCenterId(gameCenterId);
		}

		try {
			User user = getUserRepository().load(senderProto.getUserUuid());

			// boolean isDifferent =
			// checkIfNewTokenDifferent(user.getGameCenterId(), gameCenterId);
			//boolean legit = writeChangesToDb(user, gameCenterId);

			user.setGameCenterId(gameCenterId);
			getUserRepository().save(user);
			resBuilder.setStatus(SetGameCenterIdStatus.SUCCESS);
				//resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);

			SetGameCenterIdResponseProto resProto = resBuilder.build();
			SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(senderProto.getUserUuid());
			resEvent.setSetGameCenterIdResponseProto(resProto);
			getEventWriter().writeEvent(resEvent);

			// game center id might have changed
			// null PvpLeagueFromUser means will pull from hazelcast instead

			
			// TODO: Make a service for this in mobsters-services
			UpdateClientUserResponseEvent resEventUpdate = null;// MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null);
			resEventUpdate.setTag(event.getTag());
			getEventWriter().writeEvent(resEventUpdate);

		} catch(ConditionalCheckFailedException e) {
			//TODO: version was probably out of date... meaning some other thread updated this item after you loaded it but before you save it
			//handle this case
			
		}
		catch (Exception e) {
			log.error("exception in SetGameCenterIdController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
				SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(userId);
				resEvent.setTag(event.getTag());
				resEvent.setSetGameCenterIdResponseProto(resBuilder.build());
				getEventWriter().writeEvent(resEvent);
			} catch (Exception e2) {
				log.error("exception2 in SetGameCenterIdController processEvent", e);
			}
		}
	}



	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public EventWriter getEventWriter() {
		return eventWriter;
	}

	public void setEventWriter(EventWriter eventWriter) {
		this.eventWriter = eventWriter;
	}
}
