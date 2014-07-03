package com.lvl6.mobsters.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpResponseProto.LevelUpStatus;
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.LevelUpRequestEvent;
import com.lvl6.mobsters.events.response.LevelUpResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.common.Lvl6MobstersException;
import com.lvl6.mobsters.services.user.UserService;

@Component
public class LevelUpController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(LevelUpController.class);

    @Autowired
    protected UserService userService;
    
    @Autowired
    protected CreateEventProtoUtil createEventProtoUtil; 

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public LevelUpController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new LevelUpRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_LEVEL_UP_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final LevelUpRequestProto reqProto =
            ((LevelUpRequestEvent) event).getLevelUpRequestProto();
        
        //get values sent from the client (the request proto)
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final int newLevel = reqProto.getNextLevel();

        // prepare to send response back to client
        LevelUpResponseProto.Builder responseBuilder =
            LevelUpResponseProto.newBuilder();
        responseBuilder.setStatus(LevelUpStatus.SUCCESS);
        responseBuilder.setSender(senderProto);
        
        LevelUpResponseEvent resEvent = new LevelUpResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        try {
        	User u = userService.levelUpUser(userIdString, newLevel);

        	UpdateClientUserResponseEvent resEventUpdate =
        		createEventProtoUtil.createUpdateClientUserResponseEvent(u, null, null, null, null);
        	eventWriter.writeEvent(resEventUpdate);
        } catch(Lvl6MobstersException lvl6E) {
        	//TODO: Figure out if catching exception here and doing something or let it bubble up
        } catch (Exception e) {
        	LOG.error(
        		"exception in LevelUpController processEvent when calling userService",
        		e);
        	responseBuilder.setStatus(LevelUpStatus.FAIL_OTHER);
        }

        resEvent.setLevelUpResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService( UserService userService ) {
        this.userService = userService;
    }

	public CreateEventProtoUtil getCreateEventProtoUtil()
	{
		return createEventProtoUtil;
	}

	public void setCreateEventProtoUtil( CreateEventProtoUtil createEventProtoUtil )
	{
		this.createEventProtoUtil = createEventProtoUtil;
	}

}
