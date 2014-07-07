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
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.BeginObstacleRemovalRequestEvent;
import com.lvl6.mobsters.events.response.BeginObstacleRemovalResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.achievement.AchievementService;

@Component
public class BeginObstacleRemovalController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(BeginObstacleRemovalController.class);

    @Autowired
    protected StructureService beginObstacleRemovalService;

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
        final Date clientTime = new Date(reqProto.getCurTime());
        final int gemsSpent = reqProto.getGemsSpent();
        final int resourceChange = reqProto.getResourceChange();
        String rtStr = reqProto.getResourceType().name();
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
        try {
        	User u = beginObstacleRemovalService.redeemAchievement(userIdString, beginObstacleRemovalId, clientTime);
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

    }

    public AchievementService getAchievementService() {
        return beginObstacleRemovalService;
    }

    public void setAchievementService( AchievementService beginObstacleRemovalService ) {
        this.beginObstacleRemovalService = beginObstacleRemovalService;
    }

}
