package com.lvl6.mobsters.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto.SetGameCenterIdStatus;
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil;
import com.lvl6.mobsters.events.ControllerResponseEvents;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SetGameCenterIdRequestEvent;
import com.lvl6.mobsters.events.response.SetGameCenterIdResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.user.UserDataRarelyAccessedService;

@Component
public class SetGameCenterIdController extends EventController {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	@Autowired
	protected UserDataRarelyAccessedService userDataRarelyAccessedService;

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
		if (gameCenterId != null && gameCenterId.isEmpty()) {
			gameCenterId = null;
		}

		SetGameCenterIdResponseProto.Builder resBuilder = SetGameCenterIdResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		if (null != gameCenterId) {
			resBuilder.setGameCenterId(gameCenterId);
		}

		try {
			UserDataRarelyAccessed user = getUserDataRarelyAccessedService()
					.getUserDataRarelyAccessedByUserId(userId);

			boolean legit = writeChangesToDb(user, gameCenterId);

			if (legit) {
				resBuilder.setStatus(SetGameCenterIdStatus.SUCCESS);
			} else {
				resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
			}

			SetGameCenterIdResponseProto resProto = resBuilder.build();
			SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(senderProto.getUserUuid());
			resEvent.setSetGameCenterIdResponseProto(resProto);
			eventWriter.writeEvent(resEvent);

			if (legit) {
				// game center id might have changed
				// null PvpLeagueFromUser means will pull from a cache instead

				UpdateClientUserResponseEvent resEventUpdate =  CreateEventProtoUtil
						.createUpdateClientUserResponseEvent(null, null, user,
								null, null);
				resEventUpdate.setTag(event.getTag());
				eventWriter.writeEvent(resEventUpdate);

			}

		} catch(ConditionalCheckFailedException e) {
			//TODO: version was probably out of date... meaning some other thread updated this item after you loaded it but before you save it
			//handle this case
			//need to reread and do some more logic, basically call this method again...
			log.error("optimistic lock exception in SetGameCenterIdController processEvent", e);
			// don't let the client hang
			try {
				eventWriter.clearResponses();
				resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
				SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(userId);
				resEvent.setTag(event.getTag());
				resEvent.setSetGameCenterIdResponseProto(resBuilder.build());
				eventWriter.writeEvent(resEvent);
			} catch (Exception e2) {
				log.error("optimistic lock exception2  in SetGameCenterIdController processEvent", e);
			}	
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

	private boolean writeChangesToDb(UserDataRarelyAccessed user,
			String gameCenterId) {
		try {
			
			return true;
		} catch (Exception e) {
			log.error("problem with updating user game center id. user=" + user +
					"\t gameCenterId=" + gameCenterId);
		}
		return false;
	}


	public UserDataRarelyAccessedService getUserDataRarelyAccessedService() {
		return userDataRarelyAccessedService;
	}

	public void setUserDataRarelyAccessedService(
			UserDataRarelyAccessedService userDataRarelyAccessedService) {
		this.userDataRarelyAccessedService = userDataRarelyAccessedService;
	}

}
