package com.lvl6.mobsters.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lvl6.mobsters.eventproto.EventApnsProto.EnableAPNSRequestProto;
import com.lvl6.mobsters.eventproto.EventApnsProto.EnableAPNSResponseProto;
import com.lvl6.mobsters.eventproto.EventApnsProto.EnableAPNSResponseProto.EnableAPNSStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.EnableAPNSRequestEvent;
import com.lvl6.mobsters.events.response.EnableAPNSResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.user.UserService;
import com.lvl6.mobsters.services.user.UserService.ModifyUserDataRarelyAccessedSpec;
import com.lvl6.mobsters.services.user.UserService.ModifyUserDataRarelyAccessedSpecBuilder;

@Component
public class EnableAPNSController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(EnableAPNSController.class);

    @Autowired
    protected UserService userService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public EnableAPNSController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new EnableAPNSRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_ENABLE_APNS_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final EnableAPNSRequestProto reqProto =
            ((EnableAPNSRequestEvent) event).getEnableAPNSRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final String deviceToken = reqProto.getDeviceToken();

        // prepare to send response back to client
        EnableAPNSResponseProto.Builder responseBuilder =
            EnableAPNSResponseProto.newBuilder();
        responseBuilder.setStatus(EnableAPNSStatus.FAIL_OTHER);
        responseBuilder.setSender(senderProto);
        EnableAPNSResponseEvent resEvent = new EnableAPNSResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        final ModifyUserDataRarelyAccessedSpecBuilder modBuilder =
            ModifyUserDataRarelyAccessedSpec.builder();
        if (StringUtils.hasText(deviceToken) && StringUtils.hasText(userIdString)) {
            modBuilder.setDeviceToken(deviceToken);

            responseBuilder.setStatus(EnableAPNSStatus.SUCCESS);
        }

        try {
            userService.modifyUserDataRarelyAccessed(userIdString, modBuilder.build());
        } catch (Exception e) {
            LOG.error(
                "exception in EnableAPNSController processEvent when calling userService",
                e);
            responseBuilder.setStatus(EnableAPNSStatus.FAIL_OTHER);
        }

        resEvent.setEnableAPNSResponseProto(responseBuilder.build());

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

}
