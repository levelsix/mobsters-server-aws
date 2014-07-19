//package com.lvl6.mobsters.controllers;
//
//import java.sql.Timestamp;
//import java.util.Date;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesRequestProto;
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto;
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto.ExchangeGemsForResourcesStatus;
//import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.ExchangeGemsForResourcesRequestEvent;
//import com.lvl6.mobsters.events.response.ExchangeGemsForResourcesResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.ResourceType;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
//import com.lvl6.mobsters.server.EventController;
//import com.lvl6.mobsters.services.common.Lvl6MobstersException;
//import com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum;
//import com.lvl6.mobsters.services.user.UserService;
//import com.lvl6.mobsters.services.user.UserService.ModifyUserSpec;
//import com.lvl6.mobsters.services.user.UserService.ModifyUserSpecBuilder;
//
//@Component
//public class ExchangeGemsForResourcesController extends EventController {
//
//    private static Logger LOG = LoggerFactory.getLogger(ExchangeGemsForResourcesController.class);
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
//    public ExchangeGemsForResourcesController() {}
//
//    @Override
//    public RequestEvent createRequestEvent() {
//        return new ExchangeGemsForResourcesRequestEvent();
//    }
//
//    @Override
//    public EventProtocolRequest getEventType() {
//        return EventProtocolRequest.C_EXCHANGE_GEMS_FOR_RESOURCES_EVENT;
//    }
//
//    @Override
//    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
//    {
//        final ExchangeGemsForResourcesRequestProto reqProto =
//            ((ExchangeGemsForResourcesRequestEvent) event).getExchangeGemsForResourcesRequestProto();
//        
//        //get values sent from the client (the request proto)
//        final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
//        final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
//        final String userIdString = senderProto.getUserUuid();
//
//        int numGems = reqProto.getNumGems();
//        int numResources = reqProto.getNumResources();
//        ResourceType resourceType = reqProto.getResourceType();
//        Date curDate = new Timestamp(reqProto.getClientTime());
//        int maxCash = senderResourcesProto.getMaxCash();
//        int maxOil = senderResourcesProto.getMaxOil();
//        
//        // prepare to send response back to client
//        ExchangeGemsForResourcesResponseProto.Builder responseBuilder =
//            ExchangeGemsForResourcesResponseProto.newBuilder();
//        responseBuilder.setStatus(ExchangeGemsForResourcesStatus.FAIL_OTHER);
//        responseBuilder.setSender(senderResourcesProto);
//        
//        ExchangeGemsForResourcesResponseEvent resEvent = new ExchangeGemsForResourcesResponseEvent(userIdString);
//        resEvent.setTag(event.getTag());
//
//        // Check values client sent for syntax errors. Call service only if
//        // syntax checks out ok; prepare arguments for service
//        final ModifyUserSpecBuilder modBuilder = ModifyUserSpec.builder();
//        boolean validRequest = false;
//        if ( resourceType.name().equals(Lvl6MobstersResourceEnum.CASH.name()) && 0 != numResources ) {
//        	validRequest = true;
//        	modBuilder.incrementCash( numResources, maxCash);
//        }
//        if ( resourceType.name().equals( Lvl6MobstersResourceEnum.OIL.name() ) && 0 != numResources ) {
//        	validRequest = true;
//        	modBuilder.incrementOil(numResources, maxOil);
//        }
//        if ( 0 != numGems ) {
//        	validRequest = true;
//        	modBuilder.decrementGems(numGems);
//        }
//        
//        if (validRequest) {
//            responseBuilder.setStatus(ExchangeGemsForResourcesStatus.SUCCESS);
//        }
//
//        // call service if syntax is ok
//        boolean successful = false;
//        User u = null;
//        if (responseBuilder.getStatus() == ExchangeGemsForResourcesStatus.SUCCESS) {
//            try {
//                u = userService.modifyUser(userIdString, modBuilder.build());
//                // TODO: Keep track of the user currency history somehow
//                
//                successful = true;
//            } catch(Lvl6MobstersException lvl6E) {
//            	//TODO: Figure out if catching exception here and doing something or let it bubble up
//            } catch (Exception e) {
//                LOG.error(
//                    "exception in ExchangeGemsForResourcesController processEvent when calling userService",
//                    e);
//                responseBuilder.setStatus(ExchangeGemsForResourcesStatus.FAIL_OTHER);
//            }
//        }
//
//        resEvent.setExchangeGemsForResourcesResponseProto(responseBuilder.build());
//
//        // write to client
//        LOG.info("Writing event: " + resEvent);
//        eventWriter.writeEvent(resEvent);
//
//        if (successful) {
//            UpdateClientUserResponseEvent resEventUpdate =
//            	createEventProtoUtil.createUpdateClientUserResponseEvent(u, null, null, null, null);
//            eventWriter.writeEvent(resEventUpdate);
//        }
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
