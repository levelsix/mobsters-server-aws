package com.lvl6.mobsters.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.BeginMiniJobRequestProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.BeginMiniJobResponseProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.BeginMiniJobResponseProto.BeginMiniJobStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.BeginMiniJobRequestEvent;
import com.lvl6.mobsters.events.response.BeginMiniJobResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.common.TimeUtils;
import com.lvl6.mobsters.services.minijob.MiniJobService;
import com.lvl6.mobsters.services.minijob.MiniJobService.ModifyUserMiniJobsSpec;
import com.lvl6.mobsters.services.minijob.MiniJobService.ModifyUserMiniJobsSpecBuilder;

@Component
public class BeginMiniJobController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(BeginMiniJobController.class);

    @Autowired
    protected MiniJobService miniJobService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public BeginMiniJobController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new BeginMiniJobRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_BEGIN_MINI_JOB_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final BeginMiniJobRequestProto reqProto =
            ((BeginMiniJobRequestEvent) event).getBeginMiniJobRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final Date clientTime = TimeUtils.createDateFromTime(reqProto.getClientTime());
        final Set<String> userMonsterIds = new HashSet<String>(reqProto.getUserMonsterUuidsList());
        final String userMiniJobId = reqProto.getUserMiniJobUuid();

        // prepare to send response back to client
        BeginMiniJobResponseProto.Builder responseBuilder =
            BeginMiniJobResponseProto.newBuilder();
        responseBuilder.setStatus(BeginMiniJobStatus.FAIL_OTHER);
        responseBuilder.setSender(senderProto);
        BeginMiniJobResponseEvent resEvent =
            new BeginMiniJobResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        final ModifyUserMiniJobsSpecBuilder modBuilder = ModifyUserMiniJobsSpec.builder();
        if (!CollectionUtils.lacksSubstance(userMonsterIds)) {
            responseBuilder.setStatus(BeginMiniJobStatus.SUCCESS);
        }

        // call service if syntax is ok
        if (responseBuilder.getStatus() == BeginMiniJobStatus.SUCCESS) {
            try {
                miniJobService.modifyMiniJobsForUser(userIdString, new Director<MiniJobService.ModifyUserMiniJobsSpecBuilder>() {
					@Override
					public void apply(ModifyUserMiniJobsSpecBuilder builder) {
						// TODO: Consider checking to make sure the userMonsterIds are in fact
			            // the user's monsters
			            // TODO: Consider checking to make sure the user has the specified mini job
			        	// HINT: Both of the above TODO items are aspects of semantic validation, not
			        	//       syntax validation.  As such, they are verification checks that belong
			        	//       encapsulated within a service boundary and should _not_ be addressed 
			        	//       directly by Controller logic--semantics are not a Controller's 
			        	//       assigned responsibility.
			            builder.startJob(userMiniJobId, userMonsterIds, clientTime);
					}
				});
            } catch (Exception e) {
                LOG.error(
                    "exception in BeginMiniJobController processEvent when calling userService",
                    e);
                responseBuilder.setStatus(BeginMiniJobStatus.FAIL_OTHER);
            }
        }

        resEvent.setBeginMiniJobResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        try {
            eventWriter.writeEvent(resEvent);
        } catch (Exception e) {
            LOG.error("fatal exception in BeginMiniJobController processRequestEvent", e);
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
    // BeginMiniJobResponseProto.Builder resBuilder )
    // {
    // eventWriter.clearResponses();
    // resBuilder.setStatus(BeginMiniJobStatus.FAIL_OTHER);
    // BeginMiniJobResponseEvent resEvent = new BeginMiniJobResponseEvent(userId);
    // resEvent.setTag(event.getTag());
    // resEvent.setBeginMiniJobResponseProto(resBuilder.build());
    // eventWriter.writeEvent(resEvent);
    // }

    public MiniJobService getMiniJobService() {
        return miniJobService;
    }

    public void setMiniJobService( MiniJobService miniJobService ) {
        this.miniJobService = miniJobService;
    }

}
