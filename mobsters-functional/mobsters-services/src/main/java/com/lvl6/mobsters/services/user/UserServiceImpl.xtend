package com.lvl6.mobsters.services.user

import com.lvl6.mobsters.common.utils.CollectionUtils
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.UserCredential
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed
import com.lvl6.mobsters.dynamo.repository.UserCredentialRepository
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.services.common.TimeUtils
import com.lvl6.mobsters.services.monster.MonsterService
import com.lvl6.mobsters.services.structure.StructureService
import com.lvl6.mobsters.services.structure.StructureService.CreateObstaclesReplyBuilder
import com.lvl6.mobsters.services.structure.StructureService.CreateStructuresReplyBuilder
import com.lvl6.mobsters.services.task.TaskService
import com.lvl6.mobsters.utility.lambda.Director
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions.*
import static com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode.*

@Component
class UserServiceImpl implements UserService
{
	static var LOG = LoggerFactory::getLogger(UserServiceImpl)

	@Autowired
	var UserRepository userRepo

	@Autowired
	var UserDataRarelyAccessedRepository userDraRepo

	@Autowired
	var UserCredentialRepository userCredentialRepository

	@Autowired
	var DataServiceTxManager txManager

	@Autowired
	var StructureService structureService

	@Autowired
	var MonsterService monsterService

	@Autowired
	var TaskService taskService
	
	/* BEGIN READ-ONLY OPERATIONS */

	/** 
	 * At the moment, only UserCreateController uses this. No checks are made before
	 * saving this data to the db.
	 */
	override getUserCredentialByFacebookIdOrUdid(String facebookId, String udid)
	{
		var ucList = userCredentialRepository.findByFacebookId(facebookId)
		var String userId
		if (ucList.size > 1) {
			ucList = new ArrayList<UserCredential>(ucList)
			LOG.warn(
				'Found multiple UserCredentials for facebookId=%s.  Choosing the one with the lowest userId.  ucList=%s.',
				facebookId, ucList)
			Collections::sort(ucList) [o1, o2 | return o1.userId.compareTo(o2.userId)]
			userId = ucList.get(0).userId
		} else if (ucList.size === 1) {
			userId = ucList.get(0).userId
		} else {
			ucList = userCredentialRepository.findByUdid(udid)
			if (ucList.size > 1) {
				LOG.warn(
					'Found multiple UserCredentials for udid=%s.  Choosing the one with the lowest userId.  ucList=%s.',
					udid, ucList)
				Collections::sort(ucList) [o1, o2 | return o1.userId.compareTo(o2.userId)]
				userId = ucList.get(0).userId
			} else if (ucList.size === 1) {
				userId = ucList.get(0).userId
			} else {
				userId = null
			}
		}
		
		return userId
	}

	/* BEGIN TRANSACTIONAL OPERATIONS */
	
	override createFacebookUser(UserService.CreateUserReplyBuilder replyBuilder,
		String facebookId, String udid, String name, String deviceToken, int cash, int oil,
		int gems, Director<UserService.CreateUserOptionsBuilder> optionsDirector)
	{
		val createTime = TimeUtils::createNow
		val UserServiceImpl.CreateUserOptionsBuilderImpl optionsBuilder =
			new UserServiceImpl.CreateUserOptionsBuilderImpl(createTime)
		optionsDirector.apply(optionsBuilder)
		
		lvl6Precondition(
			CollectionUtils::isEmptyOrNull(
				userCredentialRepository.findByFacebookId(facebookId)
			),
			FAIL_USER_WITH_FACEBOOK_ID_EXISTS,
			'User(s) already exist with facebookId=%s', facebookId)
			
		val uc = new UserCredential()
		uc.facebookId = facebookId
		doFacebookAgnosticWork(uc, true, udid, name, deviceToken, cash, oil, gems, createTime, optionsBuilder)
	}

	override createUdidUser(
		UserService.CreateUserReplyBuilder replyBuilder, String udid,
		String name, String deviceToken, int cash, int oil, int gems,
		Director<UserService.CreateUserOptionsBuilder> optionsDirector)
	{
		val createTime = TimeUtils::createNow
		val UserServiceImpl.CreateUserOptionsBuilderImpl optionsBuilder =
			new UserServiceImpl.CreateUserOptionsBuilderImpl(createTime)
		optionsDirector.apply(optionsBuilder)
		
		lvl6Precondition(
			CollectionUtils::isEmptyOrNull(
				userCredentialRepository.findByUdid(udid)),
			FAIL_USER_WITH_UDID_ALREADY_EXISTS,
			'User(s) already exist with udid=%s', udid)
			
		val uc = new UserCredential()
		uc.udid = udid
		doFacebookAgnosticWork(uc, false, udid, name, deviceToken, cash, oil, gems, createTime, optionsBuilder)
	}
	
	private static class CreateUserOptionsBuilderImpl implements UserService.CreateUserOptionsBuilder {
		private val ArrayList<(StructureService.CreateStructureCollectionBuilder)=>void> buildSteps
		private val Date purchaseTime
		
		new(Date purchaseTime)
		{
			buildSteps = new ArrayList<(StructureService.CreateStructureCollectionBuilder)=>void>()
			this.purchaseTime = purchaseTime
		}
		
		override withStructure(int structureId, float xPosition, float yPosition) 
		{
			buildSteps += [StructureService.CreateStructureCollectionBuilder bldr|
				bldr.addStructure(structureId, xPosition, yPosition) [
					it.constructionStatus(StructureService.ConstructionStatusKind::COMPLETE_AS_EMPTY)
						.purchaseTime(purchaseTime)
				]
			]
			
			return this
		}
		
		def Director<StructureService.CreateStructureCollectionBuilder> buildDirector()
		{
			return [ StructureService.CreateStructureCollectionBuilder bldr |
				buildSteps.forEach[it.apply(bldr)]
			]
		}		
	}

	private def void doFacebookAgnosticWork(
		UserCredential uc, boolean usedFacebookId, String udid, String name, String deviceToken, 
		int cash, int oil, int gems, Date createTime, UserServiceImpl.CreateUserOptionsBuilderImpl options)
	{
		userCredentialRepository.save(uc)
		val userId = uc.userId
		
		createUser(userId, name, cash, oil, gems)
		createUserDataRarelyAccessed(userId, udid, createTime, deviceToken, usedFacebookId)
		writeStructs(userId, createTime, options)
		writeObstacles(userId)
		writeTaskCompleted(userId, createTime)
		writeMonsters(userId, createTime, usedFacebookId)
	}

	private def void createUser(String userId, String name, int cash, int oil, int gems)
	{
		userRepo.save(
			new User() => [ u|
				u.id = userId
				u.name = name
				u.admin = false
				u.gems = gems
				u.cash = cash
				u.oil = oil
				u.experience = 0
				u.level = ControllerConstants::USER_CREATE__START_LEVEL
			]
		)
	}

	private def void createUserDataRarelyAccessed(
		String userId, String udidForHistory, Date createTime, 
		String deviceToken, boolean fbIdSetOnUserCreate)
	{
		userDraRepo.save(
			new UserDataRarelyAccessed() => [udra |
				udra.userId = userId
				udra.udidForHistory = udidForHistory
				udra.deviceToken = deviceToken
				udra.fbIdSetOnUserCreate = fbIdSetOnUserCreate
				udra.lastObstacleSpawnTime = createTime
				udra.createTime = createTime
				udra.lastLogin = createTime
			]
		)
	}


	// Used merely as a placeholder because UserService.createStructures() has no reply state to propogate
	// to UserService.createUser()'s reply state.
	val UserServiceImpl.CreateStructuresReplyBuilderImpl STRUCT_REPLY_BUILDER =
		new UserServiceImpl.CreateStructuresReplyBuilderImpl()	
	
	private def void writeStructs(
		String userUuid, Date purchaseTime, UserServiceImpl.CreateUserOptionsBuilderImpl options)
	{
		LOG.info('giving user buildings')
		structureService.createStructuresForUser(STRUCT_REPLY_BUILDER, userUuid) [
			for (ii : 0 ..< ControllerConstants::TUTORIAL__EXISTING_BUILDING_IDS.length) {
				it.addStructure(
					ControllerConstants::TUTORIAL__EXISTING_BUILDING_IDS.get(ii), 
					ControllerConstants::TUTORIAL__EXISTING_BUILDING_X_POS.get(ii),
					ControllerConstants::TUTORIAL__EXISTING_BUILDING_Y_POS.get(ii)
				)[
					it.constructionStatus(
						StructureService.ConstructionStatusKind::COMPLETE_AS_FULL
					).purchaseTime(purchaseTime)
				]
			}
			
			options.buildDirector().apply(it)
		]
		
		LOG.info('gave user buildings')
	}
	
	static class CreateStructuresReplyBuilderImpl implements CreateStructuresReplyBuilder {
		override CreateStructuresReplyBuilder resultOk() {
			return this
		}
	}

	val UserServiceImpl.CreateObstaclesReplyBuilderImpl OBSTACLE_REPLY_BUILDER = 
		new UserServiceImpl.CreateObstaclesReplyBuilderImpl()
		
	private def writeObstacles(String userId)
	{
		LOG.info('giving user obstacles')

		structureService.createObstaclesForUser(OBSTACLE_REPLY_BUILDER, userId) [
			for (ii : 0 ..< ControllerConstants.TUTORIAL__INIT_OBSTACLE_ID.length) {
				it.addObstacle(
					ControllerConstants.TUTORIAL__INIT_OBSTACLE_ID.get(ii),
					ControllerConstants.TUTORIAL__INIT_OBSTACLE_X.get(ii),
					ControllerConstants.TUTORIAL__INIT_OBSTACLE_Y.get(ii),
					StructureService.OrientationKind::POSITION_ONE
				)
			}
		]
		
		LOG.info('gave user obstacles')
	}
	
	// A placeholder--there is no state to collect from the createObstacles() call for the 
	// createUser reply.
	static class CreateObstaclesReplyBuilderImpl implements CreateObstaclesReplyBuilder {
		override CreateObstaclesReplyBuilder resultOk() {
			return this
		}
	}

	private def writeTaskCompleted(String userId, Date createTime)
	{
		LOG.info('giving user completed tasks')
		taskService.completeTasks(userId)[
			it.taskId(ControllerConstants::TUTORIAL__FIRST_TASK_ID, createTime)
		]
		LOG.info('gave user completed tasks')
	}

	private def writeMonsters(String userId, Date createDate, boolean hadFbId)
	{
		var combineStartDate = TimeUtils::createDateAddDays(createDate, -7)
		var ArrayList<Integer> monsterIds = new ArrayList<Integer>(2)
		monsterIds += ControllerConstants::TUTORIAL__STARTING_MONSTER_ID
		if (hadFbId) {
			LOG.info('awarding facebook zucker mucker burger.')
			monsterIds += ControllerConstants::TUTORIAL__MARK_Z_MONSTER_ID
		}
		monsterService.createCompleteMonstersForUser(userId, monsterIds, combineStartDate)
	}

    /* ********************************************************************************************** */
    
	override levelUpUser(String userId, int newLevel)
			{
		var boolean success = false
		val boolean isRootTx = txManager.requireTransaction()
		try {
			var user = userRepo.load(userId)
			lvl6Precondition(user !== null, FAIL_OTHER, "no User for userId=%s", userId);
			user.level = newLevel
			userRepo.save(user)
			success = true
		} finally {
			if (success) {
				if (isRootTx) {
					txManager.commit()
				}
			} else {
				txManager.rollback()
			}
		}
	}

	override modifyUser(String userId, Director<UserService.ModifyUserBuilder> director)
			{
		val modifyUserOps =
			(new UserServiceImpl.ModifyUserBuilderImpl() => [modifySpec |
				director.apply(modifySpec)
			]).build()
		
		var boolean success = false
		val boolean isRootTx = txManager.requireTransaction()
		try {
			userRepo.save(
				userRepo.load(userId) => [ user |
					lvl6Precondition(user !== null, FAIL_OTHER, "no User for userId=%s", userId);
					modifyUserOps.forEach[it.apply(user)]
					userRepo.save(user)
				]
			)
			success = true
		} finally {
			if (success) {
				if (isRootTx) {
					txManager.commit()
			}
			} else {
				txManager.rollback()
			}
		}
	}

	static class ModifyUserBuilderImpl implements UserService.ModifyUserBuilder
	{
		val ArrayList<(User)=>void> userModificationList

		new()
		{
			userModificationList = new ArrayList<(User)=>void>(4)
		}

		def ArrayList<(User)=>void> build()
		{
			return userModificationList
		}

		override spendGems(int gemsDelta)
			{
			userModificationList.add[
				val oldGems = it.gems
				lvl6Precondition(
					oldGems >= gemsDelta, 
					FAIL_INSUFFICIENT_GEMS, 
					"User=%s cannot afford to spend %s gems", 
					it.id, oldGems
				);
				
				it.gems = oldGems - gemsDelta
			]
			
			return this
			}

		override decrementGems(int gemsDelta)
			{
			userModificationList.add[it.gems = Math.max(it.gems - gemsDelta, 0)]
			return this
			}

		override incrementGems(int gemsDelta)
		{
			userModificationList.add[it.gems = it.gems + gemsDelta]
			return this
		}

		override spendCash(int cashDelta)
		{
			userModificationList.add[
				val oldCash = it.cash
				lvl6Precondition(
					oldCash >= cashDelta, 
					FAIL_INSUFFICIENT_CASH, 
					"User=%s cannot afford to spend %s cash", 
					it.id, oldCash
				);
				
				it.cash = oldCash - cashDelta
			]
			
			return this
	}

		override decrementCash(int cashDelta)
	{
			userModificationList.add[it.cash = Math.max(it.cash - cashDelta, 0)]
			return this
		}

		override incrementCash(int cashDelta, int maxCash)
		{
			userModificationList.add[it.cash = Math.min(it.cash + cashDelta, maxCash)]
			return this
		}

		override spendOil(int oilDelta)
		{
			userModificationList.add[
				val oldOil = it.oil
				lvl6Precondition(
					oldOil >= oilDelta, 
					FAIL_INSUFFICIENT_OIL, 
					"User=%s cannot afford to spend %s oil", 
					it.id, oldOil
				);
				
				it.oil = oldOil - oilDelta
			]
			
			return this
		}

		override decrementOil(int oilDelta)
			{
			userModificationList.add[it.oil = Math.max(it.oil - oilDelta, 0)]
			return this
			}

		override incrementOil(int oilDelta, int maxOil)
			{
			userModificationList.add[it.oil = Math.min(it.oil + oilDelta, maxOil)]
			return this
			}

		override setExpRelative(int expDelta)
			{
			userModificationList.add[it.experience = Math.max(it.experience + expDelta, 0)]
			return this
			}
		
		override levelUpUser(int newLevel) {
			userModificationList.add[it.level = newLevel]
			return this
		}
	}


	/** 
	 */
	override modifyUserDataRarelyAccessed(String userId,
		Director<UserService.ModifyUserDataRarelyAccessedBuilder> director)
	{
		val ArrayList<(UserDataRarelyAccessed)=>void> modifyUserOps =
			(new UserServiceImpl.ModifyUserDataRarelyAccessedBuilderImpl() => [specBuilder|
				director.apply(specBuilder)
			]).build()
		
		var boolean success = false
		val boolean isRootTx = txManager.requireTransaction()
		try {
			userDraRepo.save(
				userDraRepo.load(userId) => [ udra |
					lvl6Precondition( 
						udra!== null, FAIL_OTHER, 
						"No UserDataRarelyAccessed object found for userUuid=%s", 
						userId
					)
	
					modifyUserOps.forEach[
						it.apply(udra)
					]		
				]
			)
			success = true
		} finally {
			if (success) {
				if (isRootTx) {
					txManager.commit()
				}
			} else {
				txManager.rollback()
			}
		}
	}

	static class ModifyUserDataRarelyAccessedBuilderImpl 
		implements UserService.ModifyUserDataRarelyAccessedBuilder
	{
		val ArrayList<(UserDataRarelyAccessed)=>void> usersDraModificationsSet

		new()
		{
			usersDraModificationsSet = new ArrayList<(UserDataRarelyAccessed)=>void>()
		}

		def ArrayList<(UserDataRarelyAccessed)=>void> build()
		{
			return usersDraModificationsSet
		}

		override setGameCenterIdNotNull(String nonNullGameCenterId)
		{
			usersDraModificationsSet.add[it.gameCenterId=nonNullGameCenterId]
			return this
		}

		override setDeviceToken(String deviceToken)
		{
			usersDraModificationsSet.add[it.deviceToken = deviceToken]
			return this
		}

		override setAvatarMonsterId(int monsterId)
		{
			usersDraModificationsSet.add[it.avatarMonsterId = monsterId]
			return this
		}
	}

    /* BEGIN DEPENDENCY INJECTION */

	def void setUserRepo(UserRepository userRepo)
	{
		this.userRepo = userRepo
	}

	def void setUserDraRepo(UserDataRarelyAccessedRepository userDraRepo)
	{
		this.userDraRepo = userDraRepo
	}

	def void setUserCredentialRepository(UserCredentialRepository userCredentialRepository)
	{
		this.userCredentialRepository = userCredentialRepository
	}
}
