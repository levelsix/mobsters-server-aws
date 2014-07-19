//package com.lvl6.mobsters.controllers;
//
//import java.sql.Timestamp;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyRequestProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto.UpdateUserCurrencyStatus;
//import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.UpdateUserCurrencyRequestEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateUserCurrencyResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//import com.lvl6.mobsters.services.common.Lvl6MobstersException;
//import com.lvl6.mobsters.services.user.UserService;
//import com.lvl6.mobsters.services.user.UserService.ModifyUserSpec;
//import com.lvl6.mobsters.services.user.UserService.ModifyUserSpecBuilder;
//
//@Component
//public class UpdateUserCurrencyController extends EventController {
//
//    private static Logger LOG = LoggerFactory.getLogger(UpdateUserCurrencyController.class);
//
//    @Autowired
//    protected UserService userService;
//    
//    @Autowired
//    protected CreateEventProtoUtil createEventProtoUtil; 
//
//    /*
//     * @Autowired protected EventWriter eventWriter;
//     */
//
//    public UpdateUserCurrencyController() {}
//
//    @Override
//    public RequestEvent createRequestEvent() {
//        return new UpdateUserCurrencyRequestEvent();
//    }
//
//    @Override
//    public EventProtocolRequest getEventType() {
//        return EventProtocolRequest.C_UPDATE_USER_CURRENCY_EVENT;
//    }
//
//    @Override
//    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
//    {
//        final UpdateUserCurrencyRequestProto reqProto =
//            ((UpdateUserCurrencyRequestEvent) event).getUpdateUserCurrencyRequestProto();
//        
//        //get values sent from the client (the request proto)
//        final MinimumUserProto senderProto = reqProto.getSender();
//        final String userIdString = senderProto.getUserUuid();
//
//        //all positive numbers, server will change to negative
//        int cashSpent = reqProto.getCashSpent();
//        int oilSpent = reqProto.getOilSpent();
//        int gemsSpent = reqProto.getGemsSpent();
//        
//        String reason = reqProto.getReason();
//        String details = reqProto.getDetails();
//        Timestamp clientTime = new Timestamp(reqProto.getClientTime());
//
//        // prepare to send response back to client
//        UpdateUserCurrencyResponseProto.Builder responseBuilder =
//            UpdateUserCurrencyResponseProto.newBuilder();
//        responseBuilder.setStatus(UpdateUserCurrencyStatus.FAIL_OTHER);
//        responseBuilder.setSender(senderProto);
//        
//        UpdateUserCurrencyResponseEvent resEvent = new UpdateUserCurrencyResponseEvent(userIdString);
//        resEvent.setTag(event.getTag());
//
//        // Check values client sent for syntax errors. Call service only if
//        // syntax checks out ok; prepare arguments for service
//        final ModifyUserSpecBuilder modBuilder = ModifyUserSpec.builder();
//        boolean validRequest = false;
//        if ( 0 != cashSpent ) {
//        	validRequest = true;
//        	modBuilder.decrementCash(cashSpent);
//        }
//        if ( 0 != oilSpent ) {
//        	validRequest = true;
//        	modBuilder.decrementOil(oilSpent);
//        }
//        if ( 0 != gemsSpent ) {
//        	validRequest = true;
//        	modBuilder.decrementGems(gemsSpent);
//        }
//        
//        if (validRequest) {
//            responseBuilder.setStatus(UpdateUserCurrencyStatus.SUCCESS);
//        }
//
//        // call service if syntax is ok
//        boolean successful = false;
//        User u = null;
//        if (responseBuilder.getStatus() == UpdateUserCurrencyStatus.SUCCESS) {
//            try {
//                u = userService.modifyUser(userIdString, modBuilder.build());
//                // TODO: Keep track of the user currency history somehow
//                
//                successful = true;
//                
//            } catch(Lvl6MobstersException lvl6E) {
//            	//TODO: Figure out if catching exception here and doing something or let it bubble up
//            } catch (Exception e) {
//                LOG.error(
//                    "exception in UpdateUserCurrencyController processEvent when calling userService",
//                    e);
//                responseBuilder.setStatus(UpdateUserCurrencyStatus.FAIL_OTHER);
//            }
//        }
//
//        resEvent.setUpdateUserCurrencyResponseProto(responseBuilder.build());
//
//        // write to client
//        LOG.info("Writing event: " + resEvent);
//        eventWriter.writeEvent(resEvent);
//
//        if (successful) {
//        	UpdateClientUserResponseEvent resEventUpdate =
//        		createEventProtoUtil.createUpdateClientUserResponseEvent(u, null, null, null, null);
//        	eventWriter.writeEvent(resEventUpdate);
//        }
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
//	public CreateEventProtoUtil getCreateEventProtoUtil()
//	{
//		return createEventProtoUtil;
//	}
//
//	public void setCreateEventProtoUtil( CreateEventProtoUtil createEventProtoUtil )
//	{
//		this.createEventProtoUtil = createEventProtoUtil;
//	}
//
//}
