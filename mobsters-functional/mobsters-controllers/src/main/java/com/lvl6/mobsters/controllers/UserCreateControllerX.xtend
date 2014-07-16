package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.common.utils.Director
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateRequestProto
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto.UserCreateStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.UserCreateRequestEvent
import com.lvl6.mobsters.events.response.UserCreateResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TutorialStructProto
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.structure.StructureService
import com.lvl6.mobsters.services.task.TaskService
import com.lvl6.mobsters.services.user.UserService
import com.lvl6.mobsters.services.user.UserService.CreateUserOptionsBuilder
import com.lvl6.mobsters.services.user.UserService.CreateUserReplyBuilder
import java.util.List
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.google.common.base.Preconditions.*

import static extension org.springframework.util.StringUtils.*

@Component
public class UserCreateControllerX extends EventController
{
	private static var LOG = LoggerFactory.getLogger(UserCreateControllerX);

	@Autowired
	private var UserService userService;
	
	/*
	 * @Autowired protected EventWriter eventWriter;
	 */

	public new() { 
		return;
	}

	override UserCreateRequestEvent createRequestEvent()
	{
		return new UserCreateRequestEvent()
	}

	override EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_USER_CREATE_EVENT;
	}

	override void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
	{
		val UserCreateRequestProto reqProto =
			UserCreateRequestEvent.cast(event).getUserCreateRequestProto();
		val String udid = reqProto.getUdid();
		val String name = reqProto.getName();
		val String deviceToken = reqProto.getDeviceToken();
		val String facebookId = reqProto.getFacebookId();
		val List<TutorialStructProto> structsJustBuilt = reqProto.getStructsJustBuiltList();
		
		// in case user tries hacking, don't let the amount go over tutorial default values
		val int cash = Math.min(reqProto.getCash(), ControllerConstants.TUTORIAL__INIT_CASH);
		val int oil = Math.min(reqProto.getOil(), ControllerConstants.TUTORIAL__INIT_OIL);
		val int gems = Math.min(reqProto.getGems(), ControllerConstants.TUTORIAL__INIT_GEMS);

		// Check values client sent for syntax errors. Call service only if syntax checks out.  Syntax checking is a 
		// service caller's responsibility--service will not check argument syntax.
		// TODO: Find a way to use javax.validation framework to capture syntax rules in annotations somewhere.
		checkArgument(
			udid.hasText,
			"udid must not be null, empty, or blank.  udid=%s",
			udid
		)

		// prepare to send response back to client
		val CreateUserReplyBuilderImpl replyBuilder = new CreateUserReplyBuilderImpl();

		// Prepare a Director and store it so we don't have to repeat its contents in both createUser call variants.
		val Director<CreateUserOptionsBuilder> structureDirector = [
			structsJustBuilt.forEach[ proto |
				withStructure(proto.structId, proto.coordinate.x, proto.coordinate.y)
			]
		]
		
		if (facebookId.hasText) {
			userService.createFacebookUser(
				replyBuilder, facebookId, udid, name, deviceToken, cash, oil, gems, structureDirector)
		} else {
			userService.createUdidUser(
				replyBuilder, udid, name, deviceToken, cash, oil, gems, structureDirector)
		}

		val resEvent = new UserCreateResponseEvent(udid, event.getTag(), replyBuilder.build());
		// write to client
		LOG.info("Writing event: %s", resEvent)
		eventWriter.writeEvent(resEvent);

		// TODO: FIGURE OUT IF THIS IS STILL NEEDED
		// if (!responseBuilder.getStatus()
		// 	.equals(UserCreateStatus.SUCCESS)) { return; }
		// game center id might have changed
		// null PvpLeagueFromUser means will pull from a cache instead
		// UpdateClientUserResponseEvent resEventUpdate =
		// CreateEventProtoUtil.createUpdateClientUserResponseEvent(null, null, user, null, null);
		// resEventUpdate.setTag(event.getTag());
		// eventWriter.writeEvent(resEventUpdate);
	}

	private static class CreateUserReplyBuilderImpl implements CreateUserReplyBuilder {
	    val UserCreateResponseProto.Builder protoBuilder = 
	    	UserCreateResponseProto.newBuilder();
		
		def UserCreateResponseProto.Builder build()
		{
			protoBuilder.setStatus(UserCreateStatus.SUCCESS);
			return protoBuilder;
		}
	}
	
	def void setUserService( UserService userService )
	{
		this.userService = userService;
	}

	def void setStructureService( StructureService structureService )
	{
		this.structureService = structureService;
	}

	def void setTaskService( TaskService taskService )
	{
		this.taskService = taskService;
	}
}
