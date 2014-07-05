package com.lvl6.mobsters.controllers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemRequestProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto.AchievementRedeemStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.AchievementRedeemRequestEvent;
import com.lvl6.mobsters.events.response.AchievementRedeemResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.achievement.AchievementService;

@Component
public class AchievementRedeemController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(AchievementRedeemController.class);

    @Autowired
    protected AchievementService achievementService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public AchievementRedeemController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new AchievementRedeemRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_ACHIEVEMENT_REDEEM_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final AchievementRedeemRequestProto reqProto =
            ((AchievementRedeemRequestEvent) event).getAchievementRedeemRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final int achievementId = reqProto.getAchievementId();
        Date clientTime = new Date(reqProto.getClientTime());

        // prepare to send response back to client
        AchievementRedeemResponseProto.Builder responseBuilder =
            AchievementRedeemResponseProto.newBuilder();
        responseBuilder.setStatus(AchievementRedeemStatus.SUCCESS);
        responseBuilder.setSender(senderProto);
        
        AchievementRedeemResponseEvent resEvent =
            new AchievementRedeemResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service

        // call service if syntax is ok
        try {
        	User u = achievementService.redeemAchievement(userIdString, achievementId, clientTime);
        } catch (Exception e) {
        	LOG.error(
        		"exception in AchievementRedeemController processEvent when calling userService",
        		e);
        	responseBuilder.setStatus(AchievementRedeemStatus.FAIL_OTHER);
        }

        resEvent.setAchievementRedeemResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);

    }

    public AchievementService getAchievementService() {
        return achievementService;
    }

    public void setAchievementService( AchievementService achievementService ) {
        this.achievementService = achievementService;
    }

}
