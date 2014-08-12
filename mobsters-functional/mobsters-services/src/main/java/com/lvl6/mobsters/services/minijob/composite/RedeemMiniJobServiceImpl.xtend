package com.lvl6.mobsters.services.minijob.composite

import com.lvl6.mobsters.dynamo.MiniJobForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.info.repository.StructureRepository
import com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode
import com.lvl6.mobsters.services.minijob.MiniJobExtensionLib
import com.lvl6.mobsters.services.user.UserExtensionLib
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions.*
import static com.lvl6.mobsters.services.minijob.composite.RedeemMiniJobServiceImpl.*
import java.util.Map
import com.lvl6.mobsters.dynamo.repository.MonsterForUserRepository
import com.lvl6.mobsters.dynamo.MonsterForUser
import java.util.List
import com.lvl6.mobsters.utility.common.TimeUtils
import com.lvl6.mobsters.utility.common.CollectionUtils

@Component
public class RedeemMiniJobServiceImpl implements RedeemMiniJobService {

	private static val Logger LOG = 
		LoggerFactory.getLogger(RedeemMiniJobServiceImpl)

	@Autowired
	@Property private var UserRepository userRepository
	
	@Autowired
	@Property private var MiniJobForUserRepository miniJobForUserRepository
	
	@Autowired
	@Property private var MonsterForUserRepository mfuRepository
	
	@Autowired
	@Property private var DataServiceTxManager txManager

	@Autowired
	@Property extension MiniJobExtensionLib miniJobExtension
	
	@Autowired
	@Property extension UserExtensionLib userExtension
	
	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	override User redeemMiniJob( String userId, String userMiniJobId, Date nowDate,
		int maxCash, int maxOil, Map<String, Integer> mfuIdToHealth )
	{
		val RedeemMiniJobServiceImpl.RedeemMiniJobAction action =
			new RedeemMiniJobServiceImpl.RedeemMiniJobAction(
				userId, userMiniJobId, nowDate, maxCash, maxOil,
				mfuIdToHealth, userRepository, miniJobForUserRepository,
				mfuRepository, miniJobExtension, userExtension
		)
		
		var User user = null
		var success = false
		txManager.beginTransaction()
		try {
			user = action.execute();
			success = true;
		} finally {
			if (success) {
				txManager.commit();
			} else {
				txManager.rollback();
			}
		}

		// TODO: return a user or some container holding the user data that was updated
		return user;
	}
	
	static class RedeemMiniJobAction 
	{
		val String userId
		val String userMiniJobId
		val Date nowDate
		val int maxCash
		val int maxOil
		val Map<String, Integer> mfuIdToHealth
		
		val UserRepository userRepo
		val MiniJobForUserRepository mjfuRepo
		val MonsterForUserRepository mfuRepo
		val extension MiniJobExtensionLib mjfuExt
		val extension UserExtensionLib userExt
		
		new( String userId, String userMiniJobId, Date nowDate,
			int maxCash, int maxOil, Map<String, Integer> mfuIdToHealth, 
			UserRepository userRepo, MiniJobForUserRepository mjfuRepo,
			MonsterForUserRepository mfuRepo, MiniJobExtensionLib mjfuExt,
			UserExtensionLib userExt ) 
		{
			this.userId = userId
			this.userMiniJobId = userMiniJobId
			this.nowDate = nowDate
			this.maxCash = maxCash
			this.maxOil = maxOil
			this.mfuIdToHealth = mfuIdToHealth

			this.userRepo = userRepo
			this.mjfuRepo = mjfuRepo
			this.mfuRepo = mfuRepo
			this.mjfuExt = mjfuExt
			this.userExt = userExt
			
		}
		
		// Derived properties computed when checking, then applied when updating
		var User user
		var MiniJobForUser mjfu
		var List<MonsterForUser> mfuList
		
		def User execute()
		{
			user = userRepo.load(userId)
			mjfu = mjfuRepo.load(userId, userMiniJobId)
			mfuList = mfuRepo.loadEach(userId, mfuIdToHealth.keySet)
			
			checkIfUserCanRedeem()
			
			//TODO: Update user's monsters with one extra piece
			mjfu.redeemMiniJob(user, maxCash, maxOil, mfuList, mfuIdToHealth)
			
			userRepo.save(user)
			mfuRepo.saveEach(mfuList)
			mjfuRepo.delete(mjfu)
			
			return user
		}
		
		private def void checkIfUserCanRedeem()
		{
			lvl6Precondition( 
					mjfu !== null,
					Lvl6MobstersStatusCode.FAIL_NO_MINI_JOB_EXISTS,
					LOG,
					"parameter passed in is null. userMiniJobId=%s, user=%s", 
					userMiniJobId, user)
			
			lvl6Precondition( 
					mjfu.timeCompleted !== null &&
					 	TimeUtils.isFirstEarlierThanSecond(
					 		mjfu.timeCompleted, nowDate	
					 ),
					Lvl6MobstersStatusCode.FAIL_OTHER,
					LOG,
					"mini job is not completed. userMiniJob=%s, user=%s, nowish=%t", 
					mjfu, user, nowDate)
			
			lvl6Precondition( 
					mjfu.getMiniJob !== null,
					Lvl6MobstersStatusCode.FAIL_NO_MINI_JOB_EXISTS,
					LOG,
					"parameter passed in is null. userMiniJobId=%s, user=%s", 
					userMiniJobId, user)
			
			lvl6Precondition( 
					!CollectionUtils.lacksSubstance(mfuList),
					Lvl6MobstersStatusCode.FAIL_OTHER,
					LOG,
					"no valid user monster ids sent by client, mfuIdToHealth=%s", 
					mfuIdToHealth)
			
			if (mfuList.size !== mfuIdToHealth.size)
			{
				LOG.warn(String.format(
					"short on monsters. mfuList=%s, mfuIdToHealth=%s",
					mfuList, mfuIdToHealth))
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
