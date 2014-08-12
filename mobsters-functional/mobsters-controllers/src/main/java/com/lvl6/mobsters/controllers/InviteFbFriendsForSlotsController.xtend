package com.lvl6.mobsters.controllers;

import static com.lvl6.mobsters.common.utils.CollectionUtils.*
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsRequestProto.FacebookInviteStructure
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto.InviteFbFriendsForSlotsStatus
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.InviteFbFriendsForSlotsRequestEvent
import com.lvl6.mobsters.events.response.InviteFbFriendsForSlotsResponseEvent
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithFacebookId
import com.lvl6.mobsters.server.EventController
import java.util.Date
import java.util.List
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.lvl6.mobsters.services.facebookinvite.FacebookInviteService
import java.util.Map

@Component
public class InviteFbFriendsForSlotsController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(InviteFbFriendsForSlotsController));
	
	@Autowired
	@Property private var FacebookInviteService fbInviteService
	
	@Autowired
	@Property package CreateEventProtoUtil createEventProtoUtil;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new InviteFbFriendsForSlotsRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as InviteFbFriendsForSlotsRequestEvent)
		.inviteFbFriendsForSlotsRequestProto;

		val MinimumUserProtoWithFacebookId senderProto = reqProto.sender;
		val String userUuid = senderProto.getMinUserProto().userUuid;
		val List<FacebookInviteStructure> invites = reqProto.getInvitesList();
		
		//TODO: Find out the method that gets the current time that
		//other classes use, thought it was in TimeUtils...
		val Date curTime = new Date((new Date()).getTime());
		
		//set some values to send to the client (the response proto)
		val InviteFbFriendsForSlotsResponseProto.Builder resBuilder =
		    InviteFbFriendsForSlotsResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = InviteFbFriendsForSlotsStatus.SUCCESS;
		
		val InviteFbFriendsForSlotsResponseEvent resEvent =
			new InviteFbFriendsForSlotsResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
		if ( lacksSubstance(invites) ) {
			resBuilder.status = InviteFbFriendsForSlotsStatus.FAIL_OTHER
		}

        // call service if syntax is ok
        var boolean successful = false;
        
		try {
			var Map<String, Pair<String, Integer>> fbIdToUserStruct = invites.toMap[
				FacebookInviteStructure fis |
				fis.fbFriendId
			].mapValues[
				FacebookInviteStructure fis |
				fis.userStructUuid -> fis.userStructFbLvl
			]
			
			//TODO: Notify the users with the fbIds in the new invites
			//that they have a request
			fbInviteService.inviteFbUsers(
				userUuid, fbIdToUserStruct, curTime
			);
			
			successful = true;
		} catch (Exception e) {
			LOG.error(
        		"exception in InviteFbFriendsForSlotsController processEvent when calling userService",
        		e);
        	resBuilder.setStatus(InviteFbFriendsForSlotsStatus.FAIL_OTHER);
		}
		
        // write to client
        LOG.info("Writing event: %s", resEvent);
        eventWriter.writeEvent(resEvent);
        
        if (successful) {
        	
        }
		
	}

}
