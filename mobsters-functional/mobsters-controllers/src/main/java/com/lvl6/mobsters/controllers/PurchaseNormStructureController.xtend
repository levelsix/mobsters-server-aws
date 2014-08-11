package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto.PurchaseNormStructureStatus
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.PurchaseNormStructureRequestEvent
import com.lvl6.mobsters.events.response.PurchaseNormStructureResponseEvent
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.ResourceType
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.structure.composite.PurchaseNormStructureService
import com.lvl6.mobsters.utility.values.CoordinatePair
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static java.lang.String.*

@Component
public class PurchaseNormStructureController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(PurchaseNormStructureController));
	
	@Autowired
	@Property package PurchaseNormStructureService purchaseNormStructureService;
	
	@Autowired
	@Property package CreateEventProtoUtil createEventProtoUtil;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new PurchaseNormStructureRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_PURCHASE_NORM_STRUCTURE_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as PurchaseNormStructureRequestEvent)
		.purchaseNormStructureRequestProto;
		LOG.info("reqProto=" + reqProto);

		//get stuff client sent
		val MinimumUserProto senderProto = reqProto.sender;
		val String userUuid = senderProto.userUuid;
		val int structId = reqProto.structId;
		val CoordinatePair cp = new CoordinatePair(reqProto.getStructCoordinates().getX(), reqProto.getStructCoordinates().getY());
		val Date timeOfPurchase = new Date(reqProto.timeOfPurchase);
		//positive value, need to convert to negative when updating user
		var int gemsSpent = reqProto.gemsSpent;
		// positive means refund, negative means charge user
		val int resourceChange = reqProto.resourceChange;
		val ResourceType rt = reqProto.resourceType;
		val String resourceType = rt.name;

		val PurchaseNormStructureResponseProto.Builder resBuilder =
		    PurchaseNormStructureResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = PurchaseNormStructureStatus.SUCCESS;
		
		val PurchaseNormStructureResponseEvent resEvent =
			new PurchaseNormStructureResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        if (gemsSpent < 0) {
        	LOG.warn(
        		format("client sent a negative gemsSpent=%d Converting to positive",
        			gemsSpent
        		))
        	gemsSpent = Math.abs(gemsSpent)
        }
        
        if (resourceChange > 0) {
        	LOG.error(
        		format("client sent positive resourceType=%s, resourceChange=%d. Assumed user can't be refunded.",
        			resourceType, resourceChange
        		)
        	)
        	resBuilder.status = PurchaseNormStructureStatus.FAIL_OTHER;
        }

		if ( gemsSpent === 0 && resourceChange === 0 ) {
			LOG.error(
        		format("client sent 0 gems and 0 resources. resourceType=%s.",
        			resourceType
        		)
        	)
        	resBuilder.status = PurchaseNormStructureStatus.FAIL_OTHER;
		}

        // call service if syntax is ok
        var boolean successful = false;
        var Pair<User, String> userAndUserStructId = null;

		try {
			// TODO: Keep track of the user currency history somehow
			userAndUserStructId = purchaseNormStructureService
				.purchaseStructure(userUuid, structId, cp, timeOfPurchase, gemsSpent,
					resourceType, resourceChange
			);
			
			resBuilder.setUserStructUuid(userAndUserStructId.value)
			successful = true;
		} catch (Exception e) {
			LOG.error(
        		"exception in PurchaseNormStructureController processEvent when calling userService",
        		e);
        	resBuilder.setStatus(PurchaseNormStructureStatus.FAIL_OTHER);
		}
		
        // write to client
        LOG.info("Writing event: %s", resEvent);
        eventWriter.writeEvent(resEvent);
        
        if (successful) {
        	val UpdateClientUserResponseEvent resEventUpdate = createEventProtoUtil
        		.createUpdateClientUserResponseEvent(userAndUserStructId.key, null, null, null, null);
            eventWriter.writeEvent(resEventUpdate);
        }
		
	}

}
