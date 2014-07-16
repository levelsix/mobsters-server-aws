package com.lvl6.mobsters.services.user;

import static com.lvl6.mobsters.services.common.Lvl6MobstersConditions.lvl6Precondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.dynamo.repository.UserCredentialRepository;
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.dynamo.repository.UserRepositoryImpl;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructOrientation;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TutorialStructProto;
import com.lvl6.mobsters.server.ControllerConstants;
import com.lvl6.mobsters.services.common.Lvl6MobstersException;
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode;
import com.lvl6.mobsters.services.common.TimeUtils;
import com.lvl6.mobsters.services.monster.MonsterService;
import com.lvl6.mobsters.services.structure.StructureService;
import com.lvl6.mobsters.services.task.TaskService;
import com.lvl6.mobsters.services.task.TaskService.CreateUserTasksCompletedSpec;
import com.lvl6.mobsters.services.task.TaskService.CreateUserTasksCompletedSpecBuilder;

@Component
// @Transactional
public class UserServiceImpl implements UserService
{
	private static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserRepositoryImpl userRepo;

	@Autowired
	UserDataRarelyAccessedRepository userDraRepo;

	@Autowired
	UserCredentialRepository userCredentialRepository;
	
	@Autowired
	DataServiceTxManager txManager;
	
	@Autowired
	StructureService structureService;
	
	@Autowired
	MonsterService monsterService;
	
	@Autowired
	TaskService taskService;

	/*
	@Override
	@Transactional(
		propagation = Propagation.SUPPORTS,
		isolation = Isolation.READ_COMMITTED,
		readOnly = true)
	public User findById( String id )
	{
		User retVal = userRepo.findOne(id);
		return retVal;
	}

	@Override
	@Transactional(
		propagation = Propagation.SUPPORTS,
		isolation = Isolation.READ_COMMITTED,
		readOnly = true)
	public User findByIdWithClan( String id )
	{
		final User retVal = userRepo.findByIdWithClan(id);

		return retVal;
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void updateUserResources(
		String id,
		int cashDelta,
		int experienceDelta,
		int gemsDelta,
		int oilDelta )
	{
		// TODO: This can be done better using a @Modifying / @Query annotation pair on a
		// single repository method, doing the update in-place without a fetch after all

		final User retVal = userRepo.findOne(id);
		if (cashDelta != 0) {
			retVal.setCash(retVal.getCash()
				+ cashDelta);
		}
		if (experienceDelta != 0) {
			retVal.setExperience(retVal.getExperience()
				+ experienceDelta);
		}
		if (gemsDelta != 0) {
			retVal.setGems(retVal.getGems()
				+ gemsDelta);
		}
		if (oilDelta != 0) {
			retVal.setOil(retVal.getOil()
				+ oilDelta);
		}

		userRepo.save(retVal);
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void updateUsersResources( Iterable<ChangeUserResourcesRequest> actions )
	{
		for (final ChangeUserResourcesRequest action : actions) {
			updateUserResources(action.getId(), action.getCashDelta(),
				action.getExperienceDelta(), action.getGemsDelta(), action.getOilDelta());
		}

	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void saveUser( User modifiedUser )
	{
		userRepo.save(modifiedUser);
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void createUser( User newUser )
	{
		userRepo.save(newUser);
	}

	@Override
	public ChangeUserResourcesRequest getChangeUserResourcesRequest(
		String id,
		int cashDelta,
		int experienceDelta,
		int gemsDelta,
		int oilDelta )
	{
		final ChangeUserResourcesRequest retVal =
			new ChangeUserResourcesRequest(id, cashDelta, experienceDelta, gemsDelta, oilDelta);

		return retVal;
	}
	 */

	@Override
	public void createFacebookUser(
		CreateUserReplyBuilder replyBuilder, String facebookId, 
		String udid, String name, String deviceToken, 
		int cash, int oil, int gems,
		Director<CreateUserOptionsBuilder> options)
	{
		lvl6Precondition(
			CollectionUtils.isEmptyOrNull(
				userCredentialRepository.findByFacebookId(facebookId)
			),
			Lvl6MobstersStatusCode.FAIL_USER_WITH_FACEBOOK_ID_EXISTS,
			"User(s) already exist with facebookId=%s", 
			facebookId
		);
		
		final UserCredential uc = new UserCredential();
		uc.setFacebookId(facebookId);
		
		// TODO Common other stuff
		doFacebookAgnosticWork(
			uc, facebookId, udid, 
			name, deviceToken, 
			cash, oil, gems, options);
	}

	@Override
	/**
	 * At the moment, only UserCreateController uses this. No checks are made before
	 * saving this data to the db.
	 */
	public void createUdidUser(
		CreateUserReplyBuilder replyBuilder, 
		String udid, String name, String deviceToken, 
		int cash, int oil, int gems,
		Director<CreateUserOptionsBuilder> options) {
		lvl6Precondition(
			CollectionUtils.isEmptyOrNull(
				userCredentialRepository.findByUdid(udid)
			),
			Lvl6MobstersStatusCode.FAIL_USER_WITH_UDID_ALREADY_EXISTS,
			"User(s) already exist with udid=%s", 
			udid
		);
		
		final UserCredential uc = new UserCredential();
		uc.setUdid(udid);
		
		doFacebookAgnosticWork(
			uc, null, udid, 
			name, deviceToken, 
			cash, oil, gems, options);
	}

	private void doFacebookAgnosticWork(
		final UserCredential uc, 
		final String facebookId, final String udid,
		final String name, final String deviceToken, 
		final int cash, final int oil, final int gems,
		final Director<CreateUserOptionsBuilder> director ) 
	{
		userCredentialRepository.save(uc);
		final String userId = uc.getUserId();

		final Date createTime = TimeUtils.createNow();
		final CreateUserOptionsBuilderImpl optionsBldr =
			new CreateUserOptionsBuilderImpl();
		director.apply(optionsBldr);
		
		createUser(userId, name, cash, oil, gems);
		createUserDataRarelyAccessed(userId, udid, createTime, deviceToken, facebookId!=null);
		writeStructs(userId, createTime, optionsBldr);
		writeObstacles(userId);
		writeTaskCompleted(userId, createTime);
		writeMonsters(userId, createTime, facebookId);
	}
	
	private void createUser(
		String userId, String name, int cash, int oil, int gems
	) {
		final User u = new User();
		
		u.setId(userId);
		u.setName(name);
		u.setAdmin(false);
		u.setGems(gems);
		u.setCash(cash);
		u.setOil(oil);
		
		// TODO: Figure out correct amount
		// u.setExperience((new Random()).nextInt(10));
		u.setExperience(0);
		u.setLevel(ControllerConstants.USER_CREATE__START_LEVEL);
		
		userRepo.save(u);
	}

	private void createUserDataRarelyAccessed(
		String userId,
		String udidForHistory,
		Date createTime,
		String deviceToken,
		boolean fbIdSetOnUserCreate )
	{
		UserDataRarelyAccessed udra = new UserDataRarelyAccessed();
		udra.setUserId(userId);
		udra.setUdidForHistory(udidForHistory);
		udra.setLastLogin(createTime);
		udra.setDeviceToken(deviceToken);
		udra.setCreateTime(createTime);
		udra.setFbIdSetOnUserCreate(fbIdSetOnUserCreate);
		udra.setLastObstacleSpawnTime(createTime);
		
		userDraRepo.save(udra);
	}
	
	private void writeStructs(
		final String userId,
		final Date purchaseTime,
		final CreateUserOptionsBuilderImpl optionsBldr )
	{
		final Date lastRetrievedTime = TimeUtils.createDateAddDays(purchaseTime, -7);
		int[] buildingIds = ControllerConstants.TUTORIAL__EXISTING_BUILDING_IDS;
		float[] xPositions = ControllerConstants.TUTORIAL__EXISTING_BUILDING_X_POS;
		float[] yPositions = ControllerConstants.TUTORIAL__EXISTING_BUILDING_Y_POS;
		LOG.info("giving user buildings");
		final int numBuildings = buildingIds.length;

		// upon creation, the user should be able to retrieve from these buildings
		for (int index = 0; index < numBuildings; index++ ) {
//			structureService.createStructuresForUser(userId);
//			createBuilder.setStructureId(userStructureId, buildingIds[index]);
//			createBuilder.setXCoord(userStructureId, xPositions[index]);
//			createBuilder.setYCoord(userStructureId, yPositions[index]);
//			createBuilder.setPurchaseTime(userStructureId, purchaseTime);
//			createBuilder.setLastRetrievedTime(userStructureId, lastRetrievedTime);
//			createBuilder.setComplete(userStructureId, true);
		}

		// upon creation, the user should NOT be able to retrieve from these buildings
//		for (int index = 0; index < optionsBldr.size(); index++ ) {
//			// TODO: Perhaps find more efficient way to get an id.
//			String userStructureId = UUID.randomUUID().toString();
//
//			TutorialStructProto tsp = optionsBldr.get(index);
//
//			createBuilder.setStructureId(userStructureId, tsp.getStructId());
//			createBuilder.setXCoord(userStructureId, tsp.getCoordinate()
//				.getX());
//			createBuilder.setYCoord(userStructureId, tsp.getCoordinate()
//				.getY());
//			createBuilder.setPurchaseTime(userStructureId, purchaseTime);
//			createBuilder.setLastRetrievedTime(userStructureId, purchaseTime);
//			createBuilder.setComplete(userStructureId, true);
//		}

		LOG.info("gave user buildings");
	}

	private void writeObstacles( final String userId )
	{
		LOG.info("giving user obstacles");
		String orientation = StructOrientation.POSITION_1.name();

//		final CreateUserObstaclesSpecBuilder createBuilder = CreateUserObstaclesSpec.builder();
//		for (int index = 0; index < ControllerConstants.TUTORIAL__INIT_OBSTACLE_ID.length; index++ ) {
//			// TODO: Perhaps find more efficient way to get an id.
//			final String obstacleForUserId = (new ObstacleForUser()).getObstacleForUserId();
//			final int obstacleId = ControllerConstants.TUTORIAL__INIT_OBSTACLE_ID[index];
//			createBuilder.setObstacleId(obstacleForUserId, obstacleId);
//
//			final int posX = ControllerConstants.TUTORIAL__INIT_OBSTACLE_X[index];
//			createBuilder.setXCoord(obstacleForUserId, posX);
//
//			final int posY = ControllerConstants.TUTORIAL__INIT_OBSTACLE_Y[index];
//			createBuilder.setYCoord(obstacleForUserId, posY);
//			createBuilder.setOrientation(obstacleForUserId, orientation);
//		}
//
//		structureService.createObstaclesForUser(userId, createBuilder.build());
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

	@Override
	public User levelUpUser( String userId, int newLevel ) {
		User user = userRepo.load(userId);
		
		if (null == user) {
			throw new IllegalArgumentException(
			"no User for userId="
				+ userId);
		}
		
		user.setLevel(newLevel);
		userRepo.save(user);
		return user;
	}
	
	 @Override
	 public User modifyUser( String userId, ModifyUserSpec modifySpec ) {
		 final Set<UserFunc> userOps =
			 modifySpec.getUserModificationsSet();
	
		 User user = userRepo.load(userId);

			if (null == user) {
				throw new IllegalArgumentException(
				"no User for userId="
					+ userId);
			}

			// Mutate the object

			for (UserFunc userOp : userOps) {
				userOp.apply(user);
			}

			userRepo.save(user);
			return user;
	 }
	
	static class ModifyUserSpecBuilderImpl implements ModifyUserSpecBuilder
	{
		final Set<UserFunc> usersModificationsSet;

		ModifyUserSpecBuilderImpl()
		{
			usersModificationsSet = new HashSet<UserFunc>();
		}

		@Override
		public ModifyUserSpec build()
		{
			return new ModifyUserSpec( usersModificationsSet );
		}

		@Override
		public ModifyUserSpecBuilder decrementGems( int gemsDelta )
		{
			usersModificationsSet.add( new DecrementGems( gemsDelta ));
			return this;
		}
		
		@Override
		public ModifyUserSpecBuilder incrementGems( int gemsDelta )
		{
			usersModificationsSet.add( new IncrementGems( gemsDelta ));
			return this;
		}

		@Override
		public ModifyUserSpecBuilder decrementCash( int cashDelta )
		{
			usersModificationsSet.add( new DecrementCash( cashDelta ) );
			return this;
		}
		
		@Override
		public ModifyUserSpecBuilder incrementCash( int cashDelta, int maxCash )
		{
			usersModificationsSet.add( new IncrementCash( cashDelta, maxCash ));
			return this;
		}

		@Override
		public ModifyUserSpecBuilder decrementOil( int oilDelta )
		{
			usersModificationsSet.add( new DecrementOil( oilDelta ));
			return this;
		}
		
		@Override
		public ModifyUserSpecBuilder incrementOil( int oilDelta, int maxOil )
		{
			usersModificationsSet.add( new IncrementOil( oilDelta, maxOil ));
			return this;
		}

		@Override
		public ModifyUserSpecBuilder setExpRelative( int expDelta )
		{
			usersModificationsSet.add( new SetExpRelative( expDelta ));
			return this;
		}

	}
	
	static class DecrementGems implements UserFunc {
		private int gemsDelta; //should be a positive number

		public DecrementGems( int gemsDelta )
		{
			super();
			this.gemsDelta = gemsDelta;
		}

		@Override
		public void apply( User u )
		{
			if (0 > gemsDelta && u.isAdmin()) {
				LOG.info("admin wants to add gems when should be decrementing, whatevs. unsignedGemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
			} else {
				LOG.error("gemsDelta is illegally negative. gemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newGems = u.getGems() - gemsDelta;
			
			if (0 > newGems) {
				LOG.error("user does not have enough gems to spend. gemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS);
			}
			
			u.setGems(newGems);
		}
	}
	
	static class IncrementGems implements UserFunc {
		private int gemsDelta; //should be a positive number
		
		public IncrementGems( int gemsDelta )
		{
			super();
			this.gemsDelta = gemsDelta;
		}
		
		@Override
		public void apply( User u )
		{
			if ( 0 > gemsDelta )
			{
				LOG.error("gemsDelta is illegally negative. gemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			u.setGems( u.getGems() + gemsDelta );
		}
	}
	

	static class DecrementCash implements UserFunc {
		private int cashDelta; //should be a positive number

		public DecrementCash( int cashDelta )
		{
			super();
			this.cashDelta = cashDelta;
		}

		@Override
		public void apply( User u )
		{
			if (0 > cashDelta && u.isAdmin()) {
				LOG.info("admin wants to add cash when should be decrementing, whatevs. unsignedCashDelta="
					+ cashDelta
					+ ", user="
					+ u);
			} else {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newCash = u.getCash() - cashDelta;
			
			if (0 > newCash) {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_CASH);
			}
			
			u.setCash(newCash);
		}
	}
	
	static class IncrementCash implements UserFunc {
		private int cashDelta; //should be a positive number
		private int maxCash;
		
		public IncrementCash( int cashDelta, int maxCash )
		{
			super();
			this.cashDelta = cashDelta;
			this.maxCash = maxCash;
		}
		
		@Override
		public void apply( User u )
		{
			if (0 > cashDelta) {
				LOG.error("cashDelta is illegally negative. cashDelta="
					+ cashDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newCash = u.getCash() + cashDelta;
			
			if ( newCash > maxCash ) {
				LOG.warn("cashDelta gives user more than maxCash. limiting to maxCash. cashDelta="
					+ cashDelta
					+ ", user="
					+ u
					+ ", maxCash="
					+ maxCash);
				newCash = maxCash;
			}
			
			u.setCash(newCash);
		}
	}
	
	static class DecrementOil implements UserFunc {
		private int oilDelta; //should be a positive number

		public DecrementOil( int oilDelta )
		{
			super();
			this.oilDelta = oilDelta;
		}

		@Override
		public void apply( User u )
		{
			if (0 > oilDelta && u.isAdmin()) {
				LOG.info("admin wants to add oil when should be decrementing, whatevs. unsignedOilDelta="
					+ oilDelta
					+ ", user="
					+ u);
			} else {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newOil = u.getOil() + oilDelta;
			
			if (0 > newOil) {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_OIL);
			}
			
			u.setOil(newOil);
		}
	}
	
	static class IncrementOil implements UserFunc {
		private int oilDelta; //should be a positive number
		private int maxOil;
		
		public IncrementOil( int oilDelta, int maxOil )
		{
			super();
			this.oilDelta = oilDelta;
			this.maxOil = maxOil;
		}
		
		@Override
		public void apply( User u )
		{
			if (0 > oilDelta) {
				LOG.error("oilDelta is illegally negative. oilDelta="
					+ oilDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newOil = u.getOil() + oilDelta;
			
			if ( newOil > maxOil ) {
				LOG.warn("oilDelta gives user more than maxOil. limiting to maxOil. oilDelta="
					+ oilDelta
					+ ", user="
					+ u
					+ ", maxOil="
					+ maxOil);
				newOil = maxOil;
			}
			
			u.setOil(newOil);
		}
	}
	
	static class SetExpRelative implements UserFunc {
		private int expDelta;

		public SetExpRelative( int expDelta )
		{
			super();
			this.expDelta = expDelta;
		}

		@Override
		public void apply( User u )
		{
			int newExp = u.getExperience() + expDelta;
			u.setExperience(newExp);
		}
	}	
	
	/**************************************************************************/
	
	@Override
	public void modifyUserDataRarelyAccessed(
		String userId,
		ModifyUserDataRarelyAccessedSpec modifySpec )
	{
		// get whatever we need from the database
		final Set<UserDataRarelyAccessedFunc> userOps =
			modifySpec.getUsersDraModificationsSet();

		UserDataRarelyAccessed udra = userDraRepo.load(userId);

		if (null == udra) { throw new IllegalArgumentException(
			"no UserDataRarelyAccessed for userId="
				+ userId); }

		// Mutate the object

		for (UserDataRarelyAccessedFunc userOp : userOps) {
			userOp.apply(udra);
		}

		userDraRepo.save(udra);
	}

	static class ModifyUserDataRarelyAccessedSpecBuilderImpl
		implements
			ModifyUserDataRarelyAccessedSpecBuilder
	{
		final Set<UserDataRarelyAccessedFunc> usersDraModificationsSet;

		ModifyUserDataRarelyAccessedSpecBuilderImpl()
		{
			usersDraModificationsSet = new HashSet<UserDataRarelyAccessedFunc>();
		}

		@Override
		public ModifyUserDataRarelyAccessedSpec build()
		{
			return new ModifyUserDataRarelyAccessedSpec(usersDraModificationsSet);
		}

		@Override
		public ModifyUserDataRarelyAccessedSpecBuilder setGameCenterIdNotNull(
			String nonNullGameCenterId )
		{
			usersDraModificationsSet.add(new SetGameCenterIdNotNull(nonNullGameCenterId));
			return this;
		}

		@Override
		public ModifyUserDataRarelyAccessedSpecBuilder setDeviceToken( String deviceToken )
		{
			usersDraModificationsSet.add(new SetDeviceToken(deviceToken));
			return this;
		}

		@Override
		public ModifyUserDataRarelyAccessedSpecBuilder setAvatarMonsterId( int monsterId )
		{
			usersDraModificationsSet.add(new SetAvatarMonsterId(monsterId));
			return null;
		}
	}

	static class SetGameCenterIdNotNull implements UserDataRarelyAccessedFunc
	{
		private String gameCenterId;

		SetGameCenterIdNotNull( String gameCenterId )
		{
			this.gameCenterId = gameCenterId;
		}

		@Override
		public void apply( UserDataRarelyAccessed udra )
		{
			udra.setGameCenterId(gameCenterId);
		}
	}

	static class SetDeviceToken implements UserDataRarelyAccessedFunc
	{
		private String deviceToken;

		SetDeviceToken( String deviceToken )
		{
			this.deviceToken = deviceToken;
		}

		@Override
		public void apply( UserDataRarelyAccessed udra )
		{
			udra.setDeviceToken(deviceToken);
		}
	}
	
	static class SetAvatarMonsterId implements UserDataRarelyAccessedFunc
	{
		private int monsterId;
		
		SetAvatarMonsterId( int monsterId ) {
			this.monsterId = monsterId;
		}
		
		@Override
		public void apply( UserDataRarelyAccessed udra ) {
			udra.setAvatarMonsterId(monsterId);
		}
	}

	/**************************************************************************/

	/**
	 * At the moment, only UserCreateController uses this. No checks are made before
	 * saving this data to the db.
	 */
	@Override
	public String getUserCredentialByFacebookIdOrUdid(String facebookId, String udid)
	{
		List<UserCredential> ucList = userCredentialRepository.findByFacebookId(facebookId);
		final String userId;

		if (ucList.size() > 1) {
			ucList = new ArrayList<UserCredential>(ucList);
			LOG.warn(
				"Found multiple UserCredentials for facebookId=%s.  Choosing the one with the lowest userId.  ucList=%s.",
				facebookId, ucList);
			
			// TODO: Figure out if sorting is necessary.
			Collections.sort(ucList, new Comparator<UserCredential>() {
				@Override
				public int compare(UserCredential o1, UserCredential o2) {
					return o1.getUserId().compareTo(o2.getUserId());
				}
			});
			
			userId = ucList.get(0).getUserId();
		} else if (ucList.size() == 1) {
			userId = ucList.get(0).getUserId();
		} else {		
			ucList = userCredentialRepository.findByUdid(udid);
			
			if (ucList.size() > 1) {
				LOG.warn(
					"Found multiple UserCredentials for udid=%s.  Choosing the one with the lowest userId.  ucList=%s.",
					udid, ucList);
	
				// TODO: Figure out if sorting is necessary.
				Collections.sort(ucList, new Comparator<UserCredential>() {
					@Override
					public int compare(UserCredential o1, UserCredential o2) {
						return o1.getUserId().compareTo(o2.getUserId());
					}
				});
	
				userId = ucList.get(0).getUserId();
			} else if (ucList.size() == 1) {
				userId = ucList.get(0).getUserId();
			} else {
				userId = null;
			}
		}
		
		return null;
	}

	// for the dependency injection
	public UserRepository getUserRepo()
	{
		return userRepo;
	}

	public void setUserRepo( UserRepositoryImpl userRepo )
	{
		this.userRepo = userRepo;
	}

	public UserDataRarelyAccessedRepository getUserDraRepo()
	{
		return userDraRepo;
	}

	public void setUserDraRepo( UserDataRarelyAccessedRepository userDraRepo )
	{
		this.userDraRepo = userDraRepo;
	}

	public UserCredentialRepository getUserCredentialRepository()
	{
		return userCredentialRepository;
	}

	public void setUserCredentialRepository( UserCredentialRepository userCredentialRepository )
	{
		this.userCredentialRepository = userCredentialRepository;
	}

}
