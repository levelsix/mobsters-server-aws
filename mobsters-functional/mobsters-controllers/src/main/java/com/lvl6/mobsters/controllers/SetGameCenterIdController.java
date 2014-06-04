package com.lvl6.mobsters.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto.SetGameCenterIdStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.SetGameCenterIdRequestEvent;
import com.lvl6.mobsters.events.response.SetGameCenterIdResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.user.UserService;
import com.lvl6.mobsters.services.user.UserService.ModifyUserDataRarelyAccessedSpec;
import com.lvl6.mobsters.services.user.UserService.ModifyUserDataRarelyAccessedSpecBuilder;

@Component
public class SetGameCenterIdController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(SetGameCenterIdController.class);

    @Autowired
    protected UserService userService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public SetGameCenterIdController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new SetGameCenterIdRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_SET_GAME_CENTER_ID_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final SetGameCenterIdRequestProto reqProto =
            ((SetGameCenterIdRequestEvent) event).getSetGameCenterIdRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final String gameCenterId = reqProto.getGameCenterId();

        // prepare to send response back to client
        SetGameCenterIdResponseProto.Builder responseBuilder =
            SetGameCenterIdResponseProto.newBuilder();
        responseBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
        responseBuilder.setSender(senderProto);
        if (null != gameCenterId) {
            responseBuilder.setGameCenterId(gameCenterId);
        }
        SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        final ModifyUserDataRarelyAccessedSpecBuilder modBuilder =
            ModifyUserDataRarelyAccessedSpec.builder(userIdString);
        if (StringUtils.hasText(gameCenterId) && StringUtils.hasText(userIdString)) {
            modBuilder.setGameCenterIdNotNull(gameCenterId);

            responseBuilder.setStatus(SetGameCenterIdStatus.SUCCESS);
        }

        // call service if syntax is ok
        if (responseBuilder.getStatus() == SetGameCenterIdStatus.SUCCESS) {
            try {
                userService.modifyUserDataRarelyAccessed(modBuilder.build());
            } catch (Exception e) {
                LOG.error(
                    "exception in SetGameCenterIdController processEvent when calling userService",
                    e);
                responseBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
            }
        }

        resEvent.setSetGameCenterIdResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        try {
            eventWriter.writeEvent(resEvent);
        } catch (Exception e) {
            LOG.error("fatal exception in SetGameCenterIdController processRequestEvent", e);
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
    // SetGameCenterIdResponseProto.Builder resBuilder )
    // {
    // eventWriter.clearResponses();
    // resBuilder.setStatus(SetGameCenterIdStatus.FAIL_OTHER);
    // SetGameCenterIdResponseEvent resEvent = new SetGameCenterIdResponseEvent(userId);
    // resEvent.setTag(event.getTag());
    // resEvent.setSetGameCenterIdResponseProto(resBuilder.build());
    // eventWriter.writeEvent(resEvent);
    // }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService( UserService userService ) {
        this.userService = userService;
    }

}
