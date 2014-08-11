package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto.NormStructWaitCompleteStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.NormStructWaitCompleteRequestEvent
import com.lvl6.mobsters.events.response.NormStructWaitCompleteResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.structure.StructureService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.List
import java.util.Date
import static com.lvl6.mobsters.utility.common.CollectionUtils.*

@Component
public class NormStructWaitCompleteController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(NormStructWaitCompleteController));
	
	@Autowired
	@Property package StructureService structureService;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new NormStructWaitCompleteRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_NORM_STRUCT_WAIT_COMPLETE_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as NormStructWaitCompleteRequestEvent)
		.normStructWaitCompleteRequestProto;

		val MinimumUserProto senderProto = reqProto.sender;
		val String userUuid = senderProto.userUuid;
		val List<String> userStructIdList = reqProto.userStructUuidList;
		val Date clientTime = new Date(reqProto.curTime);
		
		val NormStructWaitCompleteResponseProto.Builder resBuilder =
		    NormStructWaitCompleteResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = NormStructWaitCompleteStatus.SUCCESS;
		
		val NormStructWaitCompleteResponseEvent resEvent =
			new NormStructWaitCompleteResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        if (lacksSubstance(userStructIdList))
        {
        	LOG.error("client did not send any UserStructure ids")
        	resBuilder.status = NormStructWaitCompleteStatus.FAIL_OTHER;
        }

        // call service if syntax is ok
        if (NormStructWaitCompleteStatus.SUCCESS.equals(resBuilder.status)) {
			try {
					structureService.finishConstructingCompletedUserStructures(
						userUuid, userStructIdList, clientTime
					);
				
			} catch (Exception e) {
				LOG.error(
		    		"exception in NormStructWaitCompleteController processEvent when calling userService",
		    		e);
		    	resBuilder.setStatus(NormStructWaitCompleteStatus.FAIL_OTHER);
			}
		}
        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);
		
	}
	
}
