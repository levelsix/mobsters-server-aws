package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto
import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto.CompleteMiniJobStatus
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.CompleteMiniJobRequestEvent
import com.lvl6.mobsters.events.response.CompleteMiniJobResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.minijob.composite.CompleteMiniJobService
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static java.lang.String.*
import static com.lvl6.mobsters.common.utils.StringUtils.*
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent

@Component
public class CompleteMiniJobController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(CompleteMiniJobController));
	
	@Autowired
	@Property package CompleteMiniJobService completeMiniJobService;
	
	@Autowired
	@Property package CreateEventProtoUtil createEventProtoUtil;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new CompleteMiniJobRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_COMPLETE_MINI_JOB_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as CompleteMiniJobRequestEvent)
		.completeMiniJobRequestProto;
		LOG.info("reqProto=" + reqProto);

		//get stuff client sent
		val MinimumUserProto senderProto = reqProto.sender
		val String userId = senderProto.userUuid
		val Date clientTime = new Date(reqProto.clientTime)
		val String userMiniJobId = reqProto.userMiniJobUuid
		
		//determines whether or not user is charged, but if user shouldn't
		//be charged then gemCost should be 0... 
		val boolean isSpeedUp = reqProto.isSpeedUp
		//should be positive value, server will make negative
		var int gemCost = reqProto.gemCost

		val CompleteMiniJobResponseProto.Builder resBuilder =
		    CompleteMiniJobResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = CompleteMiniJobStatus.SUCCESS;
		
		val CompleteMiniJobResponseEvent resEvent =
			new CompleteMiniJobResponseEvent(userId);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        if (isEmpty(userMiniJobId)) {
        	resBuilder.status = CompleteMiniJobStatus.FAIL_OTHER
        }
        
        if ( gemCost < 0 ) { 
	        if (isSpeedUp) {
	        	LOG.warn(
	        		format("negative gemsSpent=%d and speeding up mini job completion.",
	        			gemCost )
	        	)
	        }
	        LOG.warn(
        		format("negative gemsSpent=%d, making it positive.",
        			gemCost)
        	)
        	gemCost = Math.abs(gemCost)
        }
        
        // call service if syntax is ok
        var boolean successful = false;
        var User user = null;

		try {
			// TODO: Keep track of the user currency history somehow
			user = completeMiniJobService.completeMiniJob(
				userId, userMiniJobId, clientTime, gemCost
			);
			
			successful = true;
		} catch (Exception e) {
			LOG.error(
        		"exception in CompleteMiniJobController processEvent when calling userService",
        		e);
        	resBuilder.setStatus(CompleteMiniJobStatus.FAIL_OTHER);
		}
		
        // write to client
        LOG.info("Writing event: %s", resEvent);
        eventWriter.writeEvent(resEvent);
        
        if ( successful && gemCost > 0 ) {
        	val UpdateClientUserResponseEvent resEventUpdate = createEventProtoUtil
        		.createUpdateClientUserResponseEvent( user, null, null, null, null );
            eventWriter.writeEvent( resEventUpdate );
        }
		
	}

}
