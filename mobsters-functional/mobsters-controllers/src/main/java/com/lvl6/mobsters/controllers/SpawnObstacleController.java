package com.lvl6.mobsters.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto.SpawnObstacleStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SpawnObstacleRequestEvent;
import com.lvl6.mobsters.events.response.SpawnObstacleResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.MinimumObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.common.TimeUtils;
import com.lvl6.mobsters.services.structure.StructureService;
import com.lvl6.mobsters.services.structure.StructureService.CreateUserObstaclesSpec;
import com.lvl6.mobsters.services.structure.StructureService.CreateUserObstaclesSpecBuilder;


@Component
public class SpawnObstacleController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(SpawnObstacleController.class);

    @Autowired
    protected StructureService structureService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public SpawnObstacleController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new SpawnObstacleRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_SPAWN_OBSTACLE_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final SpawnObstacleRequestProto reqProto =
            ((SpawnObstacleRequestEvent) event).getSpawnObstacleRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final Date clientTime = 
            TimeUtils.createDateFromTime(
            	reqProto.getCurTime());
        final List<MinimumObstacleProto> mopList = reqProto.getProspectiveObstaclesList();

        // prepare to send response back to client
        final SpawnObstacleResponseProto.Builder responseBuilder =
            SpawnObstacleResponseProto.newBuilder();
        responseBuilder.setStatus(SpawnObstacleStatus.FAIL_OTHER);
        responseBuilder.setSender(senderProto);
        SpawnObstacleResponseEvent resEvent =
            new SpawnObstacleResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        final CreateUserObstaclesSpecBuilder modBuilder = CreateUserObstaclesSpec.builder();
        if (!CollectionUtils.lacksSubstance(mopList)) {
            
        	for (MinimumObstacleProto mop : mopList) {
        		//TODO: Figure out more efficient way to get key.
        		String userObstacleId = (new ObstacleForUser()).getObstacleForUserId();
    			
    			modBuilder.setObstacleId(userObstacleId, mop.getObstacleId());
    			modBuilder.setXCoord(userObstacleId, (int) mop.getCoordinate().getX());
    			modBuilder.setYCoord(userObstacleId, (int) mop.getCoordinate().getY());
    			modBuilder.setOrientation(userObstacleId, mop.getOrientation().name());
    		}

            responseBuilder.setStatus(SpawnObstacleStatus.SUCCESS);
        }

        // call service if syntax is ok
        if (responseBuilder.getStatus() == SpawnObstacleStatus.SUCCESS) {
            try {
                structureService.createObstaclesForUser(userIdString, modBuilder.build());
                // TODO: Ensure that the user's lastMiniJobGenerated time is updated to now,
                // er, clientTime
                
            } catch (Exception e) {
                LOG.error(
                    "exception in SpawnObstacleController processEvent when calling userService",
                    e);
                responseBuilder.setStatus(SpawnObstacleStatus.FAIL_OTHER);
            }
        }

        resEvent.setSpawnObstacleResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        try {
            eventWriter.writeEvent(resEvent);
        } catch (Exception e) {
            LOG.error("fatal exception in SpawnObstacleController processRequestEvent", e);
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
    // SpawnObstacleResponseProto.Builder resBuilder )
    // {
    // eventWriter.clearResponses();
    // resBuilder.setStatus(SpawnObstacleStatus.FAIL_OTHER);
    // SpawnObstacleResponseEvent resEvent = new SpawnObstacleResponseEvent(userId);
    // resEvent.setTag(event.getTag());
    // resEvent.setSpawnObstacleResponseProto(resBuilder.build());
    // eventWriter.writeEvent(resEvent);
    // }

    public StructureService getStructureService() {
        return structureService;
    }

    public void setStructureService( StructureService structureService ) {
        this.structureService = structureService;
    }

}
