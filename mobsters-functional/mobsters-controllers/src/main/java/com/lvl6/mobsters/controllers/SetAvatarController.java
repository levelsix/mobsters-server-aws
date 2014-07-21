//package com.lvl6.mobsters.controllers;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.eventproto.EventUserProto.SetAvatarMonsterRequestProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.SetAvatarMonsterResponseProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.SetAvatarMonsterResponseProto.SetAvatarMonsterStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.SetAvatarMonsterRequestEvent;
//import com.lvl6.mobsters.events.response.SetAvatarMonsterResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//import com.lvl6.mobsters.services.user.UserService;
//import com.lvl6.mobsters.services.user.UserService.ModifyUserDataRarelyAccessedSpec;
//import com.lvl6.mobsters.services.user.UserService.ModifyUserDataRarelyAccessedSpecBuilder;
//
//@Component
//public class SetAvatarController extends EventController {
//
//    private static Logger LOG = LoggerFactory.getLogger(SetAvatarController.class);
//
//    @Autowired
//    protected UserService userService;
//
//    /*
//     * @Autowired protected EventWriter eventWriter;
//     */
//
//    public SetAvatarController() {}
//
//    @Override
//    public RequestEvent createRequestEvent() {
//        return new SetAvatarMonsterRequestEvent();
//    }
//
//    @Override
//    public EventProtocolRequest getEventType() {
//        return EventProtocolRequest.C_SET_AVATAR_MONSTER_EVENT;
//    }
//
//    @Override
//    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
//    {
//        final SetAvatarMonsterRequestProto reqProto =
//            ((SetAvatarMonsterRequestEvent) event).getSetAvatarMonsterRequestProto();
//        final MinimumUserProto senderProto = reqProto.getSender();
//        final String userIdString = senderProto.getUserUuid();
//        final int monsterId = reqProto.getMonsterId();
//
//        // prepare to send response back to client
//        SetAvatarMonsterResponseProto.Builder responseBuilder =
//            SetAvatarMonsterResponseProto.newBuilder();
//        responseBuilder.setStatus(SetAvatarMonsterStatus.FAIL_OTHER);
//        responseBuilder.setSender(senderProto);
//        SetAvatarMonsterResponseEvent resEvent = new SetAvatarMonsterResponseEvent(userIdString);
//        resEvent.setTag(event.getTag());
//
//        // Check values client sent for syntax errors. Call service only if
//        // syntax checks out ok; prepare arguments for service
//        final ModifyUserDataRarelyAccessedSpecBuilder modBuilder =
//            ModifyUserDataRarelyAccessedSpec.builder();
//        if (monsterId > 0) {
//            modBuilder.setAvatarMonsterId(monsterId);
//
//            responseBuilder.setStatus(SetAvatarMonsterStatus.SUCCESS);
//        }
//
//        try {
//            userService.modifyUserDataRarelyAccessed(userIdString, modBuilder.build());
//        } catch (Exception e) {
//            LOG.error(
//                "exception in SetAvatarMonsterController processEvent when calling userService",
//                e);
//            responseBuilder.setStatus(SetAvatarMonsterStatus.FAIL_OTHER);
//        }
//
//        resEvent.setSetAvatarMonsterResponseProto(responseBuilder.build());
//
//        // write to client
//        LOG.info("Writing event: " + resEvent);
//        eventWriter.writeEvent(resEvent);
//
//    }
//
//    public UserService getUserService() {
//        return userService;
//    }
//
//    public void setUserService( UserService userService ) {
//        this.userService = userService;
//    }
//
//}
