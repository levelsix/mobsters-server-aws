//package com.lvl6.mobsters.services.structure.upgradenormstructure
//
//import java.util.Date
//
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Component
//
//import com.lvl6.mobsters.dynamo.StructureForUser
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
//import com.lvl6.mobsters.dynamo.repository.UserRepository
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.info.IStructure
//import com.lvl6.mobsters.info.Structure
//import com.lvl6.mobsters.info.repository.StructureRepository
//import com.lvl6.mobsters.services.common.Lvl6MobstersException
//import com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum
//import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode
//import com.lvl6.mobsters.services.common.TimeUtils
//import com.lvl6.mobsters.services.structure.StructureService
//import com.lvl6.mobsters.services.user.UserService
//
//@Component
//public class UpgradeNormStructureServiceTwoImpl implements
//		UpgradeNormStructureService {
//
//	private static val Logger LOG = 
//		LoggerFactory.getLogger(UpgradeNormStructureServiceImpl)
//
//	@Autowired
//	private var UserRepository userRepository
//	
//	@Autowired
//	private var UserService userService
//
//	@Autowired
//	private var StructureForUserRepository structureForUserRepository
//	
//	@Autowired
//	private var StructureService structureService
//	
//	@Autowired
//	private var StructureRepository structureRepository
//	
//	@Autowired
//	private var DataServiceTxManager txManager
//
//	// NON CRUD LOGIC
//
//	/**************************************************************************/
//
//	// BEGIN READ ONLY LOGIC
//
//	// END READ ONLY LOGIC
//	/**************************************************************************/
//
//	// CRUD LOGIC
//
//	override upgradeNormStructure(String userId, String userStructId,
//			int gemsSpent, String resourceType, int _resourceChange,
//			Date timeOfUpgrade)
//	{
//		// TODO: TRANSACTIONIFY
//		val User user = userRepository.load(userId)
//		val StructureForUser sfu = 
//			structureForUserRepository.findByUserIdAndStructureForUserId(
//				userId, userStructId)
//
//		val resourceChange = Math.abs(_resourceChange)
//		checkIfUserCanUpgradeStructure(userId, user, userStructId, sfu,
//				gemsSpent, resourceType, resourceChange, timeOfUpgrade)
//
//		// TODO: Write to currency history
//		updateUserCurrency(userId, gemsSpent, resourceType, resourceChange)
//		structureService.beginUpgradingUserStruct(sfu, timeOfUpgrade)
//		
//		return user
//	}
//	
//	private def void checkIfUserCanUpgradeStructure(String userId, User user,
//			String userStructId, StructureForUser sfu, int _gemsSpent,
//			String resourceType, int resourceChange, Date timeOfUpgrade)
//	{
//		if ( null == user || null == sfu || sfu.getLastRetrieved() == null ) {
//			LOG.error("parameter passed in is null. user=" + user + ", user struct=" + sfu + 
//			         ", userStruct's last retrieve time=" + sfu.getLastRetrieved())
//			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER)
//		}
//
//		if (!sfu.isComplete()) {
//			LOG.error("user struct is not complete yet")
//			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_CONSTRUCTION_INCOMPLETE)
//		}
//
//		val Structure currentStruct = structureRepository.findOne(sfu.getStructId())
//		val IStructure nextLevelStruct = currentStruct.getSuccessorStruct(); 
//		if (null == nextLevelStruct ) {
//			//can't upgrade since there is no further structure to upgrade to
//			LOG.error("user struct at max level already. struct is " + currentStruct)
//			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_STRUCTURE_AT_MAX_LEVEL)
//		}
//		
//		if (TimeUtils.isFirstEarlierThanSecond(timeOfUpgrade, sfu.getLastRetrieved())) {
//			LOG.error("the upgrade time " + timeOfUpgrade + " is before the last time the building was retrieved:"
//			          + sfu.getLastRetrieved())
//			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_CONSTRUCTION_INCOMPLETE)
//		}
//			
//		var gemsSpent = _gemsSpent
//		if (gemsSpent < 0) {
//			LOG.warn("gemsSpent is negative! gemsSpent=" + gemsSpent)
//	    	gemsSpent = Math.abs(_gemsSpent)
//		}
//
//		val userGems = user.gems
//		if (gemsSpent > 0 && userGems < gemsSpent) {
//	    	LOG.error("user has " + userGems + " gems; trying to spend " +
//	    			gemsSpent + " and " + resourceChange  + " " +
//	    			resourceType + " to upgrade to structure=" + nextLevelStruct)
//	    	throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS)
//	    }
//		var cashSpent = 0
//		if (Lvl6MobstersResourceEnum.CASH.name() == resourceType) {
//			if (!hasEnoughCash(user, resourceChange)) {
//				throw new Lvl6MobstersException(
//						Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_RESOURCE)
//			}
//			cashSpent = resourceChange
//		}
//
//		var oilSpent = 0
//		if (Lvl6MobstersResourceEnum.OIL.name() == resourceType) {
//			if (!hasEnoughOil(user, resourceChange)) {
//				throw new Lvl6MobstersException(
//						Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_RESOURCE)
//			}
//			oilSpent = resourceChange
//		}
//	}
//	
//	private def boolean hasEnoughCash(User user, int resourceChange) {
//		val userCash = user.cash
//		if (userCash < resourceChange) {
//			LOG.error("user error: user does not have enough cash. userCash="
//					+ userCash + "\t cashSpent=" + resourceChange)
//			return false
//		} else {
//			return true
//		}
//	}
//
//	private def boolean hasEnoughOil(User user, int resourceChange) {
//		val userOil = user.oil
//		if (userOil < resourceChange) {
//			LOG.error("user error: user does not have enough oil. userOil="
//					+ userOil + "\t oilSpent=" + resourceChange)
//			return false
//		} else {
//			return true
//		}
//	}
//
//	private def void updateUserCurrency(
//		String userUuid, int gemsSpent, 
//		String resourceType, int resourceChange)
//	{
//		userService.modifyUser(userUuid) [ bldr |
//		    if (gemsSpent > 0) {
//		    	bldr.spendGems(gemsSpent)
//		    }
//		    switch(Lvl6MobstersResourceEnum.valueOf(resourceType)) {
//		    	case Lvl6MobstersResourceEnum::CASH : {
//		    		bldr.spendCash(resourceChange)
//		    	}
//		    	case Lvl6MobstersResourceEnum::OIL : {
//		    		bldr.spendOil(resourceChange)
//		    	}
//		    	default : {
//		    		throw new IllegalArgumentException(
//		    			String.format( 
//		    				"Unexpected resource type: %s", 
//		    				resourceType))
//		    	}
//		    }
//		]
//	}
//
//	def void setUserRepository(UserRepository userRepository) {
//		this.userRepository = userRepository
//	}
//
//	def void setUserService(UserService userService) {
//		this.userService = userService
//	}
//
//	def void setStructureForUserRepository(
//		StructureForUserRepository structureForUserRepository) {
//		this.structureForUserRepository = structureForUserRepository
//	}
//
//	def void setStructureService(StructureService StructureService) {
//		this.structureService = StructureService
//	}
//
//	def void setStructureRepository(StructureRepository structureRepository) {
//		this.structureRepository = structureRepository
//	}
//
//	def void setTxManager( DataServiceTxManager txManager) {
//		this.txManager = txManager
//	}
//}
