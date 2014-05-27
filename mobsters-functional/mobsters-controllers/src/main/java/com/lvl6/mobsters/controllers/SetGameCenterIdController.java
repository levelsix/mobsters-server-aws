package com.lvl6.mobsters.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto.SetGameCenterIdStatus;
import com.lvl6.mobsters.events.ControllerResponseEvents;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SetGameCenterIdRequestEvent;
import com.lvl6.mobsters.events.response.SetGameCenterIdResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
public class SetGameCenterIdController extends EventController {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	@Autowired
	protected UserDataRarelyAccessedRepository userDataRarelyAccessedRepository;

/*	@Autowired
	protected EventWriter eventWriter;*/

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
	protected void processRequestEvent(RequestEvent event, ControllerResponseEvents eventWriter) throws Exception {
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
			UserDataRarelyAccessed user = getUserDataRarelyAccessedRepository().load(senderProto.getUserUuid());

			// boolean isDifferent =
			// checkIfNewTokenDifferent(user.getGameCenterId(), gameCenterId);
			//boolean legit = writeChangesToDb(user, gameCenterId);

			user.setGameCenterId(gameCenterId);
			getUserDataRarelyAccessedRepository().save(user);
			resBuilder.setStatus(SetGameCenterIdStatus.SUCCESS);
				//resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);

			SetGameCenterIdResponseProto resProto = resBuilder.build();
			SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(senderProto.getUserUuid());
			resEvent.setSetGameCenterIdResponseProto(resProto);
			eventWriter.writeEvent(resEvent);

			// game center id might have changed
			// null PvpLeagueFromUser means will pull from hazelcast instead

			
			// TODO: Make a service for this in mobsters-services
			UpdateClientUserResponseEvent resEventUpdate = null;// MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null);
			resEventUpdate.setTag(event.getTag());
			eventWriter.writeEvent(resEventUpdate);

		} catch(ConditionalCheckFailedException e) {
			//TODO: version was probably out of date... meaning some other thread updated this item after you loaded it but before you save it
			//handle this case
			//need to reread and do some more logic, basically call this method again...
			
		}
		catch (Exception e) {
			log.error("exception in SetGameCenterIdController processEvent", e);
			// don't let the client hang
			try {
				eventWriter.clearResponses();
				resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
				SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(userId);
				resEvent.setTag(event.getTag());
				resEvent.setSetGameCenterIdResponseProto(resBuilder.build());
				eventWriter.writeEvent(resEvent);
			} catch (Exception e2) {
				log.error("exception2 in SetGameCenterIdController processEvent", e);
			}
		}
	}




	public UserDataRarelyAccessedRepository getUserDataRarelyAccessedRepository() {
		return userDataRarelyAccessedRepository;
	}

	public void setUserDataRarelyAccessedRepository(
			UserDataRarelyAccessedRepository userDataRarelyAccessedRepository) {
		this.userDataRarelyAccessedRepository = userDataRarelyAccessedRepository;
	}

}
