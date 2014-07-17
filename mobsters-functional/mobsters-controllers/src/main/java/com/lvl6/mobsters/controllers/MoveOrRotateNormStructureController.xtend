package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto.MoveOrRotateNormStructureStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.MoveOrRotateNormStructureRequestEvent
import com.lvl6.mobsters.events.response.MoveOrRotateNormStructureResponseEvent
import com.lvl6.mobsters.info.CoordinatePair
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.structure.StructureService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
public class MoveOrRotateNormStructureController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(MoveOrRotateNormStructureController));
	
	@Autowired
	@Property package StructureService structureService;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new MoveOrRotateNormStructureRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as MoveOrRotateNormStructureRequestEvent)
		.moveOrRotateNormStructureRequestProto;

		val MinimumUserProto senderProto = reqProto.sender;
		val String userUuid = senderProto.userUuid;
		val String userStructId = reqProto.userStructUuid;
		
		//7/16/2014 at the moment, only move, not rotate will be used
//		val MoveOrRotateNormStructType type = reqProto.type;
		val CoordinatePair newCoords = new CoordinatePair(
			reqProto.curStructCoordinates.x,
			reqProto.curStructCoordinates.y
		) 
//		val StructOrientation orientation = reqProto.orientationNew;

		val MoveOrRotateNormStructureResponseProto.Builder resBuilder =
		    MoveOrRotateNormStructureResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = MoveOrRotateNormStructureStatus.SUCCESS;
		
		val MoveOrRotateNormStructureResponseEvent resEvent =
			new MoveOrRotateNormStructureResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service

        // call service if syntax is ok
		try {
			structureService.moveUserStructure(
				userUuid, userStructId, newCoords
			);
			
		} catch (Exception e) {
			LOG.error(
        		"exception in MoveOrRotateNormStructureController processEvent when calling userService",
        		e);
        	resBuilder.setStatus(MoveOrRotateNormStructureStatus.FAIL_OTHER);
		}
		
        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);
		
	}
	
}
