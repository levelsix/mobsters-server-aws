package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto.RedeemMiniJobStatus
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RedeemMiniJobRequestEvent
import com.lvl6.mobsters.events.response.RedeemMiniJobResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources
import com.lvl6.mobsters.server.EventController
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static org.springframework.util.StringUtils.*
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent
import java.util.List
import java.util.Map
import com.lvl6.mobsters.services.minijob.composite.RedeemMiniJobService

@Component
public class RedeemMiniJobController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(RedeemMiniJobController));
	
	@Autowired
	@Property package RedeemMiniJobService redeemMiniJobService;
	
	@Autowired
	@Property package CreateEventProtoUtil createEventProtoUtil;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new RedeemMiniJobRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_REDEEM_MINI_JOB_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as RedeemMiniJobRequestEvent)
		.redeemMiniJobRequestProto;
		LOG.info("reqProto=" + reqProto);

		//get stuff client sent
		val MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.sender
		val int maxCash = senderResourcesProto.maxCash
		val int maxOil = senderResourcesProto.maxOil 
		val MinimumUserProto senderProto = senderResourcesProto.minUserProto 
		
		val String userId = senderProto.userUuid
		val Date clientTime = new Date(reqProto.clientTime)
		val String userMiniJobId = reqProto.userMiniJobUuid
		
		val List<UserMonsterCurrentHealthProto> umchpList = reqProto.umchpList
		
		val RedeemMiniJobResponseProto.Builder resBuilder =
		    RedeemMiniJobResponseProto.newBuilder();
		resBuilder.sender = senderResourcesProto;
		resBuilder.status = RedeemMiniJobStatus.SUCCESS;
		
		val RedeemMiniJobResponseEvent resEvent =
			new RedeemMiniJobResponseEvent(userId);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        if (!hasText(userMiniJobId)) {
        	resBuilder.status = RedeemMiniJobStatus.FAIL_OTHER
        }
        
        //mylist.toMap[ name].mapValues[ date] 
        val Map<String, Integer> mfuIdToHealth = umchpList
        .toMap[
        	UserMonsterCurrentHealthProto umchp |
        	umchp.userMonsterUuid
    	].mapValues[
    			UserMonsterCurrentHealthProto umchp |
    			umchp.currentHealth
    	]
        
        // call service if syntax is ok
        var boolean successful = false;
        var User user = null;

		try {
			//give the user resources
			//deduct health from user monsters
			//"delete" userMiniJobId
			//TODO: Update user's monsters with one extra piece
			// TODO: Keep track of the user currency history somehow
			user = redeemMiniJobService.redeemMiniJob(
				userId, userMiniJobId, clientTime, maxCash, maxOil,
				mfuIdToHealth
			);
			
			successful = true;
		} catch (Exception e) {
			LOG.error(
        		"exception in RedeemMiniJobController processEvent when calling userService",
        		e);
        	resBuilder.setStatus(RedeemMiniJobStatus.FAIL_OTHER);
		}
		
        // write to client
        LOG.info("Writing event: %s", resEvent);
        eventWriter.writeEvent(resEvent);
        
        if ( successful ) {
        	val UpdateClientUserResponseEvent resEventUpdate = createEventProtoUtil
        		.createUpdateClientUserResponseEvent( user, null, null, null, null );
            eventWriter.writeEvent( resEventUpdate );
        }
		
	}

}
