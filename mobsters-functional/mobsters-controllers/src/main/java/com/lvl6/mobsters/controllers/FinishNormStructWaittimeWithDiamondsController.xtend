package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto.FinishNormStructWaittimeStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.FinishNormStructWaittimeWithDiamondsRequestEvent
import com.lvl6.mobsters.events.response.FinishNormStructWaittimeWithDiamondsResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Date
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.services.structure.upgradenormstructure.UpgradeNormStructureService
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent

@Component
public class FinishNormStructWaittimeWithDiamondsController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(FinishNormStructWaittimeWithDiamondsController));
	
	@Autowired
	@Property package UpgradeNormStructureService upgradeNormStructureService;

	@Autowired
	@Property package CreateEventProtoUtil createEventProtoUtil;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new FinishNormStructWaittimeWithDiamondsRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as FinishNormStructWaittimeWithDiamondsRequestEvent)
		.finishNormStructWaittimeWithDiamondsRequestProto;
		LOG.info("reqProto=" + reqProto);

		val MinimumUserProto senderProto = reqProto.sender;
		val String userUuid = senderProto.userUuid;
		val String userStructId = reqProto.userStructUuid;
		//userstruct's lastRetrieved will start with this date
		val Date clientTime = new Date(reqProto.getTimeOfSpeedup);
		val int gemCostToSpeedUp = reqProto.gemCostToSpeedup;
		
		val FinishNormStructWaittimeWithDiamondsResponseProto.Builder resBuilder =
		    FinishNormStructWaittimeWithDiamondsResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = FinishNormStructWaittimeStatus.SUCCESS;
		
		val FinishNormStructWaittimeWithDiamondsResponseEvent resEvent =
			new FinishNormStructWaittimeWithDiamondsResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        if (gemCostToSpeedUp <= 0)
        {
        	LOG.error("client sent invalid gemCost")
        	resBuilder.status = FinishNormStructWaittimeStatus.FAIL_OTHER;
        }

		var User u = null;
		var boolean successful = false;
        // call service if syntax is ok
        if (FinishNormStructWaittimeStatus.SUCCESS.equals(resBuilder.status)) {
			try {
					u = upgradeNormStructureService.speedUpConstructingUserStruct(
						userUuid, userStructId, gemCostToSpeedUp, clientTime
					);
					successful = true;
					
			} catch (Exception e) {
				LOG.error(
		    		"exception in FinishNormStructWaittimeWithDiamondsController processEvent when calling service",
		    		e);
		    	resBuilder.setStatus(FinishNormStructWaittimeStatus.FAIL_OTHER);
			}
		}
        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);
	
		if (successful) {
        	val UpdateClientUserResponseEvent resEventUpdate =
        		createEventProtoUtil.createUpdateClientUserResponseEvent(u, null, null, null, null);
        	eventWriter.writeEvent(resEventUpdate);
        }	
	}
	
}
