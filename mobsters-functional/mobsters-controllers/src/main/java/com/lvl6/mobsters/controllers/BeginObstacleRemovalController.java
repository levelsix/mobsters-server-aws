package com.lvl6.mobsters.controllers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto.BeginObstacleRemovalStatus;
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.BeginObstacleRemovalRequestEvent;
import com.lvl6.mobsters.events.response.BeginObstacleRemovalResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.structure.BeginObstacleRemovalService;
import com.lvl6.mobsters.utility.common.TimeUtils;

@Component
public class BeginObstacleRemovalController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(BeginObstacleRemovalController.class);

    @Autowired
    protected BeginObstacleRemovalService beginObstacleRemovalService;
    
    @Autowired
    protected CreateEventProtoUtil createEventProtoUtil; 

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public BeginObstacleRemovalController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new BeginObstacleRemovalRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_ACHIEVEMENT_REDEEM_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final BeginObstacleRemovalRequestProto reqProto =
            ((BeginObstacleRemovalRequestEvent) event).getBeginObstacleRemovalRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final Date clientTime = 
            TimeUtils.createDateFromTime(
            	reqProto.getCurTime());
        final int gemsSpent = reqProto.getGemsSpent();
        final int resourceChange = reqProto.getResourceChange();
        String resourceType = reqProto.getResourceType().name();
        String userObstacleId = reqProto.getUserObstacleUuid();

        // prepare to send response back to client
        BeginObstacleRemovalResponseProto.Builder responseBuilder =
            BeginObstacleRemovalResponseProto.newBuilder();
        responseBuilder.setStatus(BeginObstacleRemovalStatus.SUCCESS);
        responseBuilder.setSender(senderProto);
        
        BeginObstacleRemovalResponseEvent resEvent =
            new BeginObstacleRemovalResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service

        // call service if syntax is ok
        boolean successful = false;
        User u = null;
        try {
        	u = beginObstacleRemovalService.initiateRemoveObstacle(
        		userIdString, userObstacleId, clientTime, gemsSpent,
        		resourceType, resourceChange);
        	
            successful = true;
            
        } catch (Exception e) {
        	LOG.error(
        		"exception in BeginObstacleRemovalController processEvent when calling userService",
        		e);
        	responseBuilder.setStatus(BeginObstacleRemovalStatus.FAIL_OTHER);
        }

        resEvent.setBeginObstacleRemovalResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);
        
        if (successful) {
        	UpdateClientUserResponseEvent resEventUpdate =
        		createEventProtoUtil.createUpdateClientUserResponseEvent(u, null, null, null, null);
        	eventWriter.writeEvent(resEventUpdate);
        }

    }

	public BeginObstacleRemovalService getBeginObstacleRemovalService()
	{
		return beginObstacleRemovalService;
	}

	public void setBeginObstacleRemovalService(
		BeginObstacleRemovalService beginObstacleRemovalService )
	{
		this.beginObstacleRemovalService = beginObstacleRemovalService;
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
