package com.lvl6.mobsters.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.SpawnMiniJobRequestProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.SpawnMiniJobResponseProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.SpawnMiniJobResponseProto.SpawnMiniJobStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SpawnMiniJobRequestEvent;
import com.lvl6.mobsters.events.response.SpawnMiniJobResponseEvent;
import com.lvl6.mobsters.info.MiniJob;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.minijob.MiniJobService;
import com.lvl6.mobsters.services.minijob.MiniJobService.CreateUserMiniJobsSpec;
import com.lvl6.mobsters.services.minijob.MiniJobService.CreateUserMiniJobsSpecBuilder;

@Component
public class SpawnMiniJobController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(SpawnMiniJobController.class);

    @Autowired
    protected MiniJobService miniJobService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public SpawnMiniJobController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new SpawnMiniJobRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_BEGIN_MINI_JOB_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final SpawnMiniJobRequestProto reqProto =
            ((SpawnMiniJobRequestEvent) event).getSpawnMiniJobRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        Date clientTime = new Date(reqProto.getClientTime());
        int numToSpawn = reqProto.getNumToSpawn();
        int structId = reqProto.getStructId();

        // prepare to send response back to client
        SpawnMiniJobResponseProto.Builder responseBuilder =
            SpawnMiniJobResponseProto.newBuilder();
        responseBuilder.setStatus(SpawnMiniJobStatus.FAIL_OTHER);
        responseBuilder.setSender(senderProto);
        SpawnMiniJobResponseEvent resEvent =
            new SpawnMiniJobResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        final CreateUserMiniJobsSpecBuilder modBuilder = CreateUserMiniJobsSpec.builder();
        
        List<MiniJob> spawnedMiniJobs = getMiniJobService().spawnMiniJobs(numToSpawn, structId);
        if (!CollectionUtils.lacksSubstance(spawnedMiniJobs)) {
            
            for (MiniJob mj : spawnedMiniJobs) {
                //TODO: Figure out more efficient way to get key.
                String userMiniJobId = (new MiniJobForUser()).getMiniJobForUserId();
                modBuilder.setMiniJobId(userMiniJobId, mj.getId());
                modBuilder.setBaseDmgReceived(userMiniJobId, mj.getDmgDealt());
                modBuilder.setDurationMinutes(userMiniJobId, mj.getDurationMinutes());
            }

            responseBuilder.setStatus(SpawnMiniJobStatus.SUCCESS);
        }

        // call service if syntax is ok
        if (responseBuilder.getStatus() == SpawnMiniJobStatus.SUCCESS) {
            try {
                miniJobService.createMiniJobsForUser(userIdString, modBuilder.build());
            } catch (Exception e) {
                LOG.error(
                    "exception in SpawnMiniJobController processEvent when calling userService",
                    e);
                responseBuilder.setStatus(SpawnMiniJobStatus.FAIL_OTHER);
            }
        }

        resEvent.setSpawnMiniJobResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        try {
            eventWriter.writeEvent(resEvent);
        } catch (Exception e) {
            LOG.error("fatal exception in SpawnMiniJobController processRequestEvent", e);
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
    // SpawnMiniJobResponseProto.Builder resBuilder )
    // {
    // eventWriter.clearResponses();
    // resBuilder.setStatus(SpawnMiniJobStatus.FAIL_OTHER);
    // SpawnMiniJobResponseEvent resEvent = new SpawnMiniJobResponseEvent(userId);
    // resEvent.setTag(event.getTag());
    // resEvent.setSpawnMiniJobResponseProto(resBuilder.build());
    // eventWriter.writeEvent(resEvent);
    // }

    public MiniJobService getMiniJobService() {
        return miniJobService;
    }

    public void setMiniJobService( MiniJobService miniJobService ) {
        this.miniJobService = miniJobService;
    }

}
