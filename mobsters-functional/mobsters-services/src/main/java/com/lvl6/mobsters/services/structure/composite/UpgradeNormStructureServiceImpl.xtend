package com.lvl6.mobsters.services.structure.composite

import com.lvl6.mobsters.dynamo.StructureForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.info.repository.StructureRepository
import com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode
import com.lvl6.mobsters.services.common.TimeUtils
import com.lvl6.mobsters.services.structure.StructureExtensionLib
import com.lvl6.mobsters.services.structure.StructureService
import com.lvl6.mobsters.services.structure.composite.UpgradeNormStructureService
import com.lvl6.mobsters.services.user.UserExtensionLib
import com.lvl6.mobsters.services.user.UserService
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.services.common.Lvl6MobstersConditions.*

@Component
public class UpgradeNormStructureServiceImpl implements UpgradeNormStructureService {

	private static val Logger LOG = 
		LoggerFactory.getLogger(UpgradeNormStructureServiceImpl)

	@Autowired
	private var UserRepository userRepository
	
	@Autowired
	private var UserService userService

	@Autowired
	private var StructureForUserRepository structureForUserRepository
	
	@Autowired
	private var StructureService structureService
	
	@Autowired
	private var StructureRepository structureRepository
	
	@Autowired
	private var DataServiceTxManager txManager

	@Autowired
	extension StructureExtensionLib structExtension
	
	@Autowired
	extension UserExtensionLib userExtension

	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// CRUD LOGIC

	override upgradeNormStructure(String userId, String userStructId,
			int gemsSpent, String resourceType, int _resourceChange,
			Date timeOfUpgrade)
	{
		val UpgradeNormStructureServiceImpl.UpgradeNormStructureAction action =
			new UpgradeNormStructureServiceImpl.UpgradeNormStructureAction(
				userId, userStructId, gemsSpent, resourceType, _resourceChange, timeOfUpgrade,
				userRepository, structureForUserRepository, structExtension, userExtension
			);
		
		var success = false
		txManager.beginTransaction()
		try {
			action.execute();
			success = true;
		} finally {
			if (success) {
				txManager.commit();
			} else {
				txManager.rollback();
			}
		}

		// Don't expose the User object, although it could be returned by the Action if that were desired....
		return null;
	}
	
	static class UpgradeNormStructureAction 
	{
		val String userId
		val String userStructId
		var int gemsSpent
		val String resourceType
		var int resourceChange
		val Date timeOfUpgrade
		
		val UserRepository userRepo
		val StructureForUserRepository sfuRepo
		val extension StructureExtensionLib sfuExt
		val extension UserExtensionLib userExt
		
		// Derived properties computed when checking, then applied when updating
		var User user
		var StructureForUser sfu
		var int userGems
		var int cashToSpend
		var int oilToSpend
		
		new(String userId, String userStructId,
			int gemsSpent, String resourceType, int resourceChange,
			Date timeOfUpgrade, UserRepository userRepo,
			StructureForUserRepository sfuRepo,
			StructureExtensionLib sfuExt, UserExtensionLib userExt) 
		{
			this.userId = userId
			this.userStructId = userStructId
			this.gemsSpent = gemsSpent
			this.resourceType = resourceType
			this.resourceChange = Math.abs(resourceChange)
			this.timeOfUpgrade = timeOfUpgrade

			this.userRepo = userRepo
			this.sfuRepo = sfuRepo
			this.sfuExt = sfuExt
			this.userExt = userExt
			
			// Derived properties computed when checking, then applied when updating
			this.user = null
			this.sfu = null
			this.userGems = 0
			this.cashToSpend = 0
			this.oilToSpend = 0
		}
		
		def void execute() {
			user = userRepo.load(userId)
			sfu = sfuRepo.findByUserIdAndStructureForUserId(userId, userStructId)
			
			checkIfUserCanUpgradeStructure()
	
			// TODO: Write to currency history
//			updateUserCurrency()	
//			sfu.speedUpConstruction(timeOfUpgrade).moveTo(495, 284)
			sfu.beginTimedUpgrade(timeOfUpgrade, user, gemsSpent, cashToSpend, oilToSpend)
			
			userRepo.save(user)
			sfuRepo.save(sfu)
		}
		
		private def void checkIfUserCanUpgradeStructure()
		{
			lvl6Precondition( 
				(user !== null && sfu !== null && sfu.getLastRetrieved() !== null),
				Lvl6MobstersStatusCode.FAIL_OTHER,
				LOG,
				"parameter passed in is null. user=%s, user struct=%s, userStruct's last retrieve time=%s", 
				user, sfu, sfu.getLastRetrieved())
	
			lvl6Precondition(
				sfu.isComplete(),
				Lvl6MobstersStatusCode.FAIL_CONSTRUCTION_INCOMPLETE,
				LOG,
				"user struct is not complete yet")
	
			val currentStruct = sfu.structure
			val nextLevelStruct = currentStruct.successorStruct
			
			//can't upgrade if there is no further structure to upgrade to
			lvl6Precondition(
				nextLevelStruct !== null,
				Lvl6MobstersStatusCode.FAIL_STRUCTURE_AT_MAX_LEVEL,
				LOG,
				"user struct at max level already.  Current structure is %s", 
				currentStruct)
			
			lvl6Precondition(
				TimeUtils.isFirstEarlierThanSecond(sfu.getLastRetrieved(), timeOfUpgrade),
				Lvl6MobstersStatusCode.FAIL_CONSTRUCTION_INCOMPLETE,
				LOG,
				"the upgrade time %s is before the last time the building was retrieved: %s",
				timeOfUpgrade, sfu.getLastRetrieved())
		
			if (gemsSpent < 0) {
				LOG.warn("gemsSpent is negative! gemsSpent=%d", gemsSpent)
		    	gemsSpent = Math.abs(gemsSpent)
			}
	
			val spendPurpose = [| return String.format("to upgrade to structure=%s", nextLevelStruct.toString())]
			
			userGems = user.gems
			user.checkCanSpendGems(gemsSpent, LOG, spendPurpose)
		    
		    switch (resourceType) {
				case Lvl6MobstersResourceEnum.CASH.name: {
					user.checkCanSpendCash(resourceChange, LOG, spendPurpose)
					cashToSpend = resourceChange
				}
				case Lvl6MobstersResourceEnum.OIL.name: {
		    		user.checkCanSpendOil(resourceChange, LOG, spendPurpose)
		    		oilToSpend = resourceChange
		    	}
		    	default: {
			    	// TODO: Default case is optional.  Is it an error to specify some other string?
		    		throwException(
		    			Lvl6MobstersStatusCode.FAIL_OTHER, "Invalid resource type name: %s", resourceType
		    		)
		    	}
		    }	
		}
	
		/*private def void updateUserCurrency()
		{
			user.spendGems(gemsSpent)
			user.spendCash(cashToSpend)
			user.spendOil(oilToSpend)
		}*/
	}

	override speedUpConstructingUserStruct( String userId, String userStructId, int gemCost, Date now ) {
		// TODO port this next...
		return null
	}

	def void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository
	}

	def void setUserService(UserService userService) {
		this.userService = userService
	}

	def void setStructureForUserRepository(
		StructureForUserRepository structureForUserRepository) {
		this.structureForUserRepository = structureForUserRepository
	}

	def void setStructureService(StructureService StructureService) {
		this.structureService = StructureService
	}

	def void setStructureRepository(StructureRepository structureRepository) {
		this.structureRepository = structureRepository
	}

	def void setTxManager( DataServiceTxManager txManager) {
		this.txManager = txManager
	}
}
