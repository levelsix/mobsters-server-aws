package com.lvl6.mobsters.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementProgressRequestProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementProgressResponseProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementProgressResponseProto.AchievementProgressStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.AchievementProgressRequestEvent;
import com.lvl6.mobsters.events.response.AchievementProgressResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventAchievementProto.UserAchievementProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.achievement.AchievementService;
import com.lvl6.mobsters.services.achievement.AchievementService.ModifyUserAchievementsSpec;
import com.lvl6.mobsters.services.achievement.AchievementService.ModifyUserAchievementsSpecBuilder;
import com.lvl6.mobsters.services.common.TimeUtils;

@Component
public class AchievementProgressController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(AchievementProgressController.class);

    @Autowired
    protected AchievementService achievementService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public AchievementProgressController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new AchievementProgressRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_ACHIEVEMENT_PROGRESS_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final AchievementProgressRequestProto reqProto =
            ((AchievementProgressRequestEvent) event).getAchievementProgressRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final List<UserAchievementProto> uapList = reqProto.getUapListList();
        final Date clientTime = 
            TimeUtils.createDateFromTime(
            	reqProto.getClientTime());

        // prepare to send response back to client
        AchievementProgressResponseProto.Builder responseBuilder =
            AchievementProgressResponseProto.newBuilder();
        responseBuilder.setStatus(AchievementProgressStatus.FAIL_OTHER);
        responseBuilder.setSender(senderProto);

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        final ModifyUserAchievementsSpecBuilder modBuilder = ModifyUserAchievementsSpec.builder();
        if (!CollectionUtils.lacksSubstance(uapList)) {
            for (final UserAchievementProto uap : uapList) {
                int achievementId = uap.getAchievementId();

                // TODO: check if achievement id exists

                modBuilder.setProgressAbsolute(achievementId, uap.getProgress());

                //NOTE: Client can clobber existing data 
                //e.g. if AchievementForUser is already complete, but client sends
                //new progress and sets isComplete to false, the AchievementForUser
                //goes from complete to incomplete, which should not happen.
                boolean isComplete = uap.getIsComplete();
                if (isComplete) {
                    modBuilder.setIsComplete(achievementId);
                    modBuilder.setTimeComplete(achievementId, clientTime);
                }
            }

            responseBuilder.setStatus(AchievementProgressStatus.SUCCESS);
        }

        // call service if syntax is ok
        if (responseBuilder.getStatus() == AchievementProgressStatus.SUCCESS) {
            try {
                achievementService.modifyAchievementsForUser(userIdString, modBuilder.build());
            } catch (Exception e) {
                LOG.error(
                    "exception in AchievementProgressController processEvent when calling userService",
                    e);
                responseBuilder.setStatus(AchievementProgressStatus.FAIL_OTHER);
            }
        }

        AchievementProgressResponseEvent resEvent =
        	new AchievementProgressResponseEvent(userIdString, event.getTag(), responseBuilder);
       
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
