package com.lvl6.mobsters.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto.UserCreateStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.UserCreateRequestEvent;
import com.lvl6.mobsters.events.response.UserCreateResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructOrientation;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TutorialStructProto;
import com.lvl6.mobsters.server.ControllerConstants;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.common.TimeUtils;
import com.lvl6.mobsters.services.monster.MonsterService;
import com.lvl6.mobsters.services.structure.StructureService;
import com.lvl6.mobsters.services.structure.StructureService.CreateUserObstaclesSpec;
import com.lvl6.mobsters.services.structure.StructureService.CreateUserObstaclesSpecBuilder;
import com.lvl6.mobsters.services.structure.StructureService.CreateUserStructuresSpec;
import com.lvl6.mobsters.services.structure.StructureService.CreateUserStructuresSpecBuilder;
import com.lvl6.mobsters.services.task.TaskService;
import com.lvl6.mobsters.services.task.TaskService.CreateUserTasksCompletedSpec;
import com.lvl6.mobsters.services.task.TaskService.CreateUserTasksCompletedSpecBuilder;
import com.lvl6.mobsters.services.user.UserService;

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
		final Date createTime = new Date();
		final List<TutorialStructProto> structsJustBuilt = reqProto.getStructsJustBuiltList();

		// in case user tries hacking, don't let the amount go over tutorial default values
		final int cash = Math.min(reqProto.getCash(), ControllerConstants.TUTORIAL__INIT_CASH);
		final int oil = Math.min(reqProto.getOil(), ControllerConstants.TUTORIAL__INIT_OIL);
		final int gems = Math.min(reqProto.getGems(), ControllerConstants.TUTORIAL__INIT_GEMS);

		// prepare to send response back to client
		UserCreateResponseProto.Builder responseBuilder = UserCreateResponseProto.newBuilder();
		responseBuilder.setStatus(UserCreateStatus.SUCCESS);
		UserCreateResponseEvent resEvent = new UserCreateResponseEvent(udid);
		resEvent.setTag(event.getTag());

		// Check values client sent for syntax errors. Call service only if
		// syntax checks out ok; prepare arguments for service
		// NOTE: since service also kind of relies on syntax checking

		UserCredential uc = null;
		try {
			uc = userService.createUserCredential(facebookId, udid);
			
			CreateUserCallBuilder callBuilder = CreateUserCallBuilderImpl.newBuilder();
			List<CreateStructureCallBuilder> innerBuilder = new ArrayList<CreateStructureCallBuilder>();
			
			callBuilder
				.facebookId(facebookId)
				.udid(udid)
				.name(name)
				.cash(cash)
				.oil(oil)
				.gems(gems)
				.deviceToken(deviceToken)
				.createTime(createTime)
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
		
			callResult = userService.createUser( callBuilder.build() );
		} catch (Exception e) {
			// TODO: Consider making a hierarchy of exceptions, one for each
			// UserCreateStatus
			LOG.error(
				"exception in UserCreateController processEvent when calling userService", e);
			responseBuilder.setStatus(UserCreateStatus.FAIL_OTHER);
			//uc = new UserCredential();
		}

		resEvent.setUserCreateResponseProto(responseBuilder.build());
		// write to client
		LOG.info("Writing event: "
			+ resEvent);
		try {
			eventWriter.writeEvent(resEvent);
		} catch (Exception e) {
			LOG.error("fatal exception in UserCreateController processRequestEvent", e);
		}

		if (!responseBuilder.getStatus()
			.equals(UserCreateStatus.SUCCESS)) { return; }

		// TODO: FIGURE OUT IF THIS IS STILL NEEDED
		// game center id might have changed
		// null PvpLeagueFromUser means will pull from a cache instead
		// UpdateClientUserResponseEvent resEventUpdate =
		// CreateEventProtoUtil.createUpdateClientUserResponseEvent(null, null, user, null, null);
		// resEventUpdate.setTag(event.getTag());
		// eventWriter.writeEvent(resEventUpdate);

		String userId = uc.getUserId();
		// TAKE INTO ACCOUNT THE PROPERTIES SENT IN BY CLIENT
		try {
			// TODO: create an entry in user and user_data_rarely_accessed tables
			writeUser(name, cash, oil, gems, userId, udid, createTime, deviceToken, facebookId );
			writeStructs(userId, createTime, structsJustBuilt);
			writeObstacles(userId);
			writeTaskCompleted(userId, createTime);
			writeMonsters(userId, createTime, facebookId);
			
		} catch (Exception e) {
			LOG.error(
				"exception in UserCreateController processEvent when initializing user", e);
		}
		//if using xtend expression language (higher programming language using java underneath)
		//this is how to populate the necessary data before calling the service:
		//pojo = factory.createWriteStructs(userId, createTime, structsJustBuilt);
		//this is how to actually build the object that will be passed into the service class
		//pojo.writeStructs()
	}

	private void writeUser(
		final String name,
		final int cash,
		final int oil,
		final int gems,
		String userId,
		String udid,
		Date createTime,
		String deviceToken,
		String fbId )
	{
		userService.createUser(userId, name, cash, oil, gems);
		boolean fbIdSetOnUserCreate = true;
		if (!StringUtils.hasText(fbId)) {
			fbIdSetOnUserCreate = false;
		}
		userService.createUserDataRarelyAccessed(userId, udid, createTime, deviceToken, fbIdSetOnUserCreate);
	}
	
	private void writeStructs(
		final String userId,
		final Date purchaseTime,
		final List<TutorialStructProto> structsJustBuilt )
	{

		final CreateUserStructuresSpecBuilder createBuilder =
			CreateUserStructuresSpec.builder();

		final Date lastRetrievedTime = TimeUtils.createDateAddDays(purchaseTime, -7);
		int[] buildingIds = ControllerConstants.TUTORIAL__EXISTING_BUILDING_IDS;
		float[] xPositions = ControllerConstants.TUTORIAL__EXISTING_BUILDING_X_POS;
		float[] yPositions = ControllerConstants.TUTORIAL__EXISTING_BUILDING_Y_POS;
		LOG.info("giving user buildings");
		final int numBuildings = buildingIds.length;

		// upon creation, the user should be able to retrieve from these buildings
		for (int index = 0; index < numBuildings; index++ ) {
			// TODO: Perhaps find more efficient way to get an id.
			String userStructureId = (new StructureForUser()).getId();

			createBuilder.setStructureId(userStructureId, buildingIds[index]);
			createBuilder.setXCoord(userStructureId, xPositions[index]);
			createBuilder.setYCoord(userStructureId, yPositions[index]);
			createBuilder.setPurchaseTime(userStructureId, purchaseTime);
			createBuilder.setLastRetrievedTime(userStructureId, lastRetrievedTime);
			createBuilder.setComplete(userStructureId, true);
		}

		// upon creation, the user should NOT be able to retrieve from these buildings
		for (int index = 0; index < structsJustBuilt.size(); index++ ) {
			// TODO: Perhaps find more efficient way to get an id.
			String userStructureId = UUID.randomUUID().toString();

			TutorialStructProto tsp = structsJustBuilt.get(index);

			createBuilder.setStructureId(userStructureId, tsp.getStructId());
			createBuilder.setXCoord(userStructureId, tsp.getCoordinate()
				.getX());
			createBuilder.setYCoord(userStructureId, tsp.getCoordinate()
				.getY());
			createBuilder.setPurchaseTime(userStructureId, purchaseTime);
			createBuilder.setLastRetrievedTime(userStructureId, purchaseTime);
			createBuilder.setComplete(userStructureId, true);
		}

		//if using xtend expression language, this is how to call the service with
		//the xtend created java object
		//structureService.createStructuresForUser(pojo.userId, createBuilder.build());
		structureService.createStructuresForUser(userId, createBuilder.build());
		LOG.info("gave user buildings");
	}

	private void writeObstacles( final String userId )
	{
		LOG.info("giving user obstacles");
		String orientation = StructOrientation.POSITION_1.name();

		final CreateUserObstaclesSpecBuilder createBuilder = CreateUserObstaclesSpec.builder();
		for (int index = 0; index < ControllerConstants.TUTORIAL__INIT_OBSTACLE_ID.length; index++ ) {
			// TODO: Perhaps find more efficient way to get an id.
			final String obstacleForUserId = (new ObstacleForUser()).getObstacleForUserId();
			final int obstacleId = ControllerConstants.TUTORIAL__INIT_OBSTACLE_ID[index];
			createBuilder.setObstacleId(obstacleForUserId, obstacleId);

			final int posX = ControllerConstants.TUTORIAL__INIT_OBSTACLE_X[index];
			createBuilder.setXCoord(obstacleForUserId, posX);

			final int posY = ControllerConstants.TUTORIAL__INIT_OBSTACLE_Y[index];
			createBuilder.setYCoord(obstacleForUserId, posY);
			createBuilder.setOrientation(obstacleForUserId, orientation);
		}

		structureService.createObstaclesForUser(userId, createBuilder.build());
		LOG.info("gave user obstacles");
	}

	private void writeTaskCompleted( String userId, Date createTime )
	{
		LOG.info("giving user completed tasks");
		CreateUserTasksCompletedSpecBuilder createBuilder = CreateUserTasksCompletedSpec.builder();
		int cityId = ControllerConstants.TUTORIAL__CITY_ONE_ID;
	  	int assetIdOne = ControllerConstants.TUTORIAL__CITY_ONE_ASSET_NUM_FOR_FIRST_DUNGEON;
	  	// TODO: Create the configuration data class that returns the correct value
	  	int taskIdOne = 0; //TaskRetrieveUtils.getTaskIdForCityElement(cityId, assetIdOne);
	  	createBuilder.setTimeOfEntry(taskIdOne, createTime);
	  	
	  	int assetIdTwo = ControllerConstants.TUTORIAL__CITY_ONE_ASSET_NUM_FOR_SECOND_DUNGEON;
	  	// TODO: Create the configuration data class that returns the correct value
	  	int taskIdTwo = 0; //TaskRetrieveUtils.getTaskIdForCityElement(cityId, assetIdTwo);
	  	createBuilder.setTimeOfEntry(taskIdTwo, createTime);
	  	
	  	taskService.createTasksForUserCompleted(userId, createBuilder.build());
	  	LOG.info("gave user completed tasks");
	  	
	}
	
	private void writeMonsters(String userId, Date createDate, String fbId) {
		//String sourceOfPieces = ControllerConstants.MFUSOP__USER_CREATE;
		
		//so the user will get monsters that are completed already, thus usable
		Date combineStartDate = TimeUtils.createDateAddDays(createDate, -7);
	  	
		List<Integer> monsterIds = new ArrayList<Integer>();
	  	monsterIds.add(ControllerConstants.TUTORIAL__STARTING_MONSTER_ID);
	  	
	  	if (StringUtils.hasText(fbId)) {
	  		LOG.info("awarding facebook zucker mucker burger.");
	  		monsterIds.add(ControllerConstants.TUTORIAL__MARK_Z_MONSTER_ID);
	  	}
	  	
	  	monsterService.createCompleteMonstersForUser(userId, monsterIds, combineStartDate);
	}

	// private void failureCase(
	// RequestEvent event,
	// EventsToDispatch eventWriter,
	// String userId,
	// UserCreateResponseProto.Builder resBuilder )
	// {
	// eventWriter.clearResponses();
	// resBuilder.setStatus(UserCreateStatus.FAIL_OTHER);
	// UserCreateResponseEvent resEvent = new UserCreateResponseEvent(userId);
	// resEvent.setTag(event.getTag());
	// resEvent.setUserCreateResponseProto(resBuilder.build());
	// eventWriter.writeEvent(resEvent);
	// }

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService( UserService userService )
	{
		this.userService = userService;
	}

	public StructureService getStructureService()
	{
		return structureService;
	}

	public void setStructureService( StructureService structureService )
	{
		this.structureService = structureService;
	}

	public TaskService getTaskService()
	{
		return taskService;
	}

	public void setTaskService( TaskService taskService )
	{
		this.taskService = taskService;
	}

}
