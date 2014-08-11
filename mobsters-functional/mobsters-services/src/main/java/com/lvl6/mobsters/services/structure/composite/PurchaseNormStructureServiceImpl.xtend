package com.lvl6.mobsters.services.structure.composite

import com.lvl6.mobsters.dynamo.StructureForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.info.Structure
import com.lvl6.mobsters.info.repository.StructureRepository
import com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum
import com.lvl6.mobsters.services.structure.StructureExtensionLib
import com.lvl6.mobsters.services.user.UserExtensionLib
import com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode
import com.lvl6.mobsters.utility.values.CoordinatePair
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum.*
import static com.lvl6.mobsters.services.structure.composite.PurchaseNormStructureServiceImpl.*
import static com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions.*
import static java.lang.String.*

@Component
public class PurchaseNormStructureServiceImpl implements PurchaseNormStructureService {

	private static val Logger LOG = 
		LoggerFactory.getLogger(PurchaseNormStructureServiceImpl)

	@Autowired
	private var UserRepository userRepository
	
	@Autowired
	private var StructureForUserRepository structureForUserRepository
	
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

	// TRANSACTIONAL LOGIC
	// TODO: Figure out another way to encapsulate data 
	override Pair<User, String> purchaseStructure( String userId, int structId,
			CoordinatePair cp, Date nowDate, int gemsSpent, String resourceType,
			int resourceChange )
	{
		val PurchaseNormStructureServiceImpl.BuyStructureAction action =
			new PurchaseNormStructureServiceImpl.BuyStructureAction(
				userId, structId, cp, nowDate, gemsSpent, resourceType,
				resourceChange, userRepository, structureForUserRepository,
				structExtension, userExtension
		)
		
		var Pair<User, String> userAndUserStructId = null
		var success = false
		txManager.beginTransaction()
		try {
			userAndUserStructId = action.execute();
			success = true;
		} finally {
			if (success) {
				txManager.commit();
			} else {
				txManager.rollback();
			}
		}

		// TODO: return a user or some container holding the user data that was updated
		return userAndUserStructId;
	}
	
	static class BuyStructureAction 
	{
		val String userId
		val int structId
		val CoordinatePair cp
		val Date now
		val int gemsSpent
		val String resourceType
		val int resourceChange
		
		val UserRepository userRepo
		val StructureForUserRepository sfuRepo
		val extension StructureExtensionLib sfuExt
		val extension UserExtensionLib userExt
		
		new(String userId, int structId, CoordinatePair cp, Date now,
			int gemsSpent, String resourceType, int resourceChange,
			UserRepository userRepo, StructureForUserRepository sfuRepo,
			StructureExtensionLib sfuExt, UserExtensionLib userExt) 
		{
			this.userId = userId
			this.structId = structId
			this.cp = cp
			this.now = now
			this.gemsSpent = gemsSpent
			this.resourceType = resourceType
			this.resourceChange = resourceChange

			this.userRepo = userRepo
			this.sfuRepo = sfuRepo
			this.sfuExt = sfuExt
			this.userExt = userExt
			
		}
		
		// Derived properties computed when checking, then applied when updating
		var User user
		var Structure struct
		var Lvl6MobstersResourceEnum rsrcType
		var int cashChange
		var int oilChange
		
		def Pair<User, String> execute() {
			 user = userRepo.load(userId)
			 val sfu = new StructureForUser
			 sfu.structId = structId;
			 struct = sfu.structure
			 rsrcType = Lvl6MobstersResourceEnum::valueOf(resourceType)
			 
			 checkIfUserCanPurchase()
			 
			 sfu.beginPurchase(struct, now, cp, user, gemsSpent, cashChange, oilChange)
			 userRepo.save(user)
			 sfuRepo.save(sfu)
			 
			 //TODO: need to return a pair of User and the newly created userStructId
			 return user -> sfu.structureForUserId
		}
		
		private def void checkIfUserCanPurchase()
		{
			lvl6Precondition( 
				user !== null,
				Lvl6MobstersStatusCode.FAIL_OTHER,
				LOG,
				"parameter passed in is null. user=%s", 
				user)

			lvl6Precondition( 
				struct !== null,
				Lvl6MobstersStatusCode.FAIL_OTHER,
				LOG,
				"no structure for id=%d", 
				structId)

			val spendPurpose = [| return String.format("to purchase structure=%s", struct.toString())]
			
			user.checkCanSpendGems(gemsSpent, LOG, spendPurpose)
			
			switch valueOf(rsrcType) {
				case CASH: {
					user.checkCanSpendCash(resourceChange, LOG, spendPurpose)
					cashChange = resourceChange
				} case OIL: {
					user.checkCanSpendOil(resourceChange, LOG, spendPurpose)
					oilChange = resourceChange
				} default: {
					throwException(
		    			Lvl6MobstersStatusCode.FAIL_OTHER, "Invalid resource type name: %s", resourceType
		    		)
				}
			}
		}
	
	}

	def void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository
	}

	def void setStructureForUserRepository(
		StructureForUserRepository structureForUserRepository) {
		this.structureForUserRepository = structureForUserRepository
	}

	def void setStructureRepository(StructureRepository structureRepository) {
		this.structureRepository = structureRepository
	}

	def void setTxManager( DataServiceTxManager txManager) {
		this.txManager = txManager
	}
	
}
