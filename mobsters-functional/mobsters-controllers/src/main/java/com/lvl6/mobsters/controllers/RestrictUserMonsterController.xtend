package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.eventproto.EventMonsterProto.RestrictUserMonsterResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.RestrictUserMonsterResponseProto.RestrictUserMonsterStatus
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RestrictUserMonsterRequestEvent
import com.lvl6.mobsters.events.response.RestrictUserMonsterResponseEvent
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import java.util.List
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.lvl6.mobsters.services.monster.MonsterService

@Component
public class RestrictUserMonsterController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(RestrictUserMonsterController));
	
	@Autowired
	@Property package MonsterService monsterService;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new RestrictUserMonsterRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_RESTRICT_USER_MONSTER_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as RestrictUserMonsterRequestEvent)
		.restrictUserMonsterRequestProto;

		val MinimumUserProto senderProto = reqProto.sender;
		val String userUuid = senderProto.userUuid;
		val List<String> userMonsterIds = reqProto.userMonsterUuidsList;
		
		//set some values to send to the client (the response proto)
		val RestrictUserMonsterResponseProto.Builder resBuilder =
		    RestrictUserMonsterResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = RestrictUserMonsterStatus.SUCCESS;
		
		val RestrictUserMonsterResponseEvent resEvent =
			new RestrictUserMonsterResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service

        // call service if syntax is ok
		try {
			monsterService.restrictUserMonsters(
				userUuid, userMonsterIds
			);
			
		} catch (Exception e) {
			LOG.error(
        		"exception in RestrictUserMonsterController processEvent when calling userService",
        		e);
        	resBuilder.setStatus(RestrictUserMonsterStatus.FAIL_OTHER);
		}
		
        // write to client
        LOG.info("Writing event: %s", resEvent);
        eventWriter.writeEvent(resEvent);
		
	}
	
}
