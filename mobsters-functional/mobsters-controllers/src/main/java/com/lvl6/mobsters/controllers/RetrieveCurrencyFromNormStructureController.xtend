package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureRequestProto.StructRetrieval
import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto.RetrieveCurrencyFromNormStructureStatus
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RetrieveCurrencyFromNormStructureRequestEvent
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent
import com.lvl6.mobsters.events.response.RetrieveCurrencyFromNormStructureResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.structure.composite.CollectCurrencyFromNormStructureService
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.List
import static com.lvl6.mobsters.common.utils.CollectionUtils.*
import java.util.Map
import java.util.HashMap
import java.util.AbstractMap.SimpleEntry

@Component
public class RetrieveCurrencyFromNormStructureController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(RetrieveCurrencyFromNormStructureController));
	
	@Autowired
	@Property package CollectCurrencyFromNormStructureService collectCurrencyFromNormStructureService;

	@Autowired
	@Property package CreateEventProtoUtil createEventProtoUtil;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new RetrieveCurrencyFromNormStructureRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_RETRIEVE_CURRENCY_FROM_NORM_STRUCTURE_EVENT
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as RetrieveCurrencyFromNormStructureRequestEvent)
		.retrieveCurrencyFromNormStructureRequestProto;
		LOG.info("reqProto=" + reqProto);

		val MinimumUserProtoWithMaxResources senderAndResourcesProto = reqProto.sender;
		val MinimumUserProto senderProto = senderAndResourcesProto.minUserProto
		val String userUuid = senderProto.userUuid;
		val List<StructRetrieval> structRetrievals = reqProto.structRetrievalsList;
		val Date curTime = new Date();
		val int maxCash = senderAndResourcesProto.getMaxCash();
    	val int maxOil = senderAndResourcesProto.getMaxOil();
    
		val RetrieveCurrencyFromNormStructureResponseProto.Builder resBuilder =
		    RetrieveCurrencyFromNormStructureResponseProto.newBuilder();
		resBuilder.sender = senderAndResourcesProto;
		resBuilder.status = RetrieveCurrencyFromNormStructureStatus.SUCCESS;
		
		val RetrieveCurrencyFromNormStructureResponseEvent resEvent =
			new RetrieveCurrencyFromNormStructureResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        val Map<String, Map.Entry<Date, Integer>> sfuIdToCollectTimeAndAmount =
        	new HashMap(); 
		if (lacksSubstance(structRetrievals)) {
			LOG.error("client did not send times and amounts collected from structs");
			resBuilder.status = RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER;
			
		} else {
			//convert protos to poor man's object
			structRetrievals.forEach[ structureRetrieval |
				val String structureForUserId = structureRetrieval.userStructUuid;
				
				//Java has no Pair, so using Map.Entry
				val Map.Entry<Date, Integer> entry = new SimpleEntry(
					new Date(structureRetrieval.timeOfRetrieval),
					structureRetrieval.amountCollected
				);
				sfuIdToCollectTimeAndAmount.put(structureForUserId, entry);
			]			
		}


        // call service if syntax is ok
        var boolean successful = false;
        var User u = null;
        
		if (RetrieveCurrencyFromNormStructureStatus.SUCCESS.equals(resBuilder.status)) {
			try {
				// TODO: Keep track of the user currency history somehow
				u = collectCurrencyFromNormStructureService.collectResourcesFromNormStructure(
					userUuid, maxCash, maxOil, curTime, sfuIdToCollectTimeAndAmount
				);
				
				successful = true;
			} catch (Exception e) {
				LOG.error(
	        		"exception in RetrieveCurrencyFromNormStructureController processEvent when calling userService",
	        		e);
	        	resBuilder.setStatus(RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER);
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
