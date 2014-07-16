package com.lvl6.mobsters.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto.UserCreateStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.UserCreateRequestEvent;
import com.lvl6.mobsters.events.response.UserCreateResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TutorialStructProto;
import com.lvl6.mobsters.server.ControllerConstants;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.monster.MonsterService;
import com.lvl6.mobsters.services.structure.StructureService;
import com.lvl6.mobsters.services.task.TaskService;
import com.lvl6.mobsters.services.user.UserService;
import com.lvl6.mobsters.services.user.UserService.CreateUserReplyBuilder;

@Component
public class UserCreateController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(UserCreateController.class);

	@Autowired
	protected UserService userService;

	@Autowired
	protected StructureService structureService;
	
	@Autowired
	protected TaskService taskService;

	@Autowired
	protected MonsterService monsterService;
	
	/*
	 * @Autowired protected EventWriter eventWriter;
	 */

	public UserCreateController()
	{}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new UserCreateRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_USER_CREATE_EVENT;
	}

	@Override
	protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
	{
		final UserCreateRequestProto reqProto =
			((UserCreateRequestEvent) event).getUserCreateRequestProto();
		final String udid = reqProto.getUdid();
		final String name = reqProto.getName();
		final String deviceToken = reqProto.getDeviceToken();
		final String facebookId = reqProto.getFacebookId();
		final List<TutorialStructProto> structsJustBuilt = 
			reqProto.getStructsJustBuiltList();

		// in case user tries hacking, don't let the amount go over tutorial default values
		final int cash = Math.min(reqProto.getCash(), ControllerConstants.TUTORIAL__INIT_CASH);
		final int oil = Math.min(reqProto.getOil(), ControllerConstants.TUTORIAL__INIT_OIL);
		final int gems = Math.min(reqProto.getGems(), ControllerConstants.TUTORIAL__INIT_GEMS);

		// prepare to send response back to client
		CreateUserReplyProtoBuilderImpl replyBuilder =
			new CreateUserReplyProtoBuilderImpl();

		// Check values client sent for syntax errors. Call service only if
		// syntax checks out ok; prepare arguments for service
		// NOTE: since service also kind of relies on syntax checking
		/*
		userService
			    .structures( 
			    	structsJustBuilt.map[ proto |
			    	    CreateStructureCallBuilder.newBuilder() => [ bldr |
			    	        bldr
			    	        	.buildingId( proto.buildingId )
			    	        	.xCoord( proto.xCoord )
			    	        	.yCoord( proto.yCoord )
			    	    ]
			        ]
			    );		
		
			callResult = 
				userService.createUser( callBuilder.build() );
		*/

		// write to client
		UserCreateResponseEvent resEvent = 
			new UserCreateResponseEvent(udid, event.getTag(), replyBuilder.getBuilder());
		LOG.info("Writing event: " + resEvent);
		eventWriter.writeEvent(resEvent);

		// TODO: FIGURE OUT IF THIS IS STILL NEEDED
		// if (!responseBuilder.getStatus()
		// .equals(UserCreateStatus.SUCCESS)) { return; }
		// game center id might have changed
		// null PvpLeagueFromUser means will pull from a cache instead
		// UpdateClientUserResponseEvent resEventUpdate =
		// CreateEventProtoUtil.createUpdateClientUserResponseEvent(null, null, user, null, null);
		// resEventUpdate.setTag(event.getTag());
		// eventWriter.writeEvent(resEventUpdate);
	}

	private static class CreateUserReplyProtoBuilderImpl implements CreateUserReplyBuilder {
	    final UserCreateResponseProto.Builder protoBuilder = 
	    	UserCreateResponseProto.newBuilder();
		
		public UserCreateResponseProto.Builder getBuilder() {
			protoBuilder.setStatus(UserCreateStatus.SUCCESS);
			return protoBuilder;
		}
	}

	void setUserService( final UserService userService )
	{
		this.userService = userService;
	}
	
	void setStructureService( final StructureService structureService )
	{
		this.structureService = structureService;
	}

	void setTaskService( final TaskService taskService )
	{
		this.taskService = taskService;
	}
}
