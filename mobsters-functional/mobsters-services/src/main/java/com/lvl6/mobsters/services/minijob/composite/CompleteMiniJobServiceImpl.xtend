package com.lvl6.mobsters.services.minijob.composite

import com.lvl6.mobsters.dynamo.MiniJobForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.services.minijob.MiniJobExtensionLib
import com.lvl6.mobsters.services.user.UserExtensionLib
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions.*
import static com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode.*

@Component
public class CompleteMiniJobServiceImpl implements CompleteMiniJobService {

	private static val Logger LOG = 
		LoggerFactory.getLogger(CompleteMiniJobServiceImpl)

	@Autowired
	@Property private var UserRepository userRepository
	
	@Autowired
	@Property private var MiniJobForUserRepository miniJobForUserRepository
	
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
	override User completeMiniJob( String userId, String userMiniJobId,
			Date nowDate, int gemsSpent )
	{
		val CompleteMiniJobServiceImpl.CompleteMiniJobAction action =
			new CompleteMiniJobServiceImpl.CompleteMiniJobAction(
				userId, userMiniJobId, nowDate, gemsSpent, userRepository,
				miniJobForUserRepository, miniJobExtension, userExtension
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
	
	static class CompleteMiniJobAction 
	{
		val String userId
		val String userMiniJobId
		val Date nowDate
		val int gemsSpent
		
		val UserRepository userRepo
		val MiniJobForUserRepository mjfuRepo
		val extension MiniJobExtensionLib mjfuExt
		val extension UserExtensionLib userExt
		
		new( String userId, String userMiniJobId, Date nowDate, int gemsSpent, 
			UserRepository userRepo, MiniJobForUserRepository mjfuRepo,
			MiniJobExtensionLib mjfuExt, UserExtensionLib userExt ) 
		{
			this.userId = userId
			this.userMiniJobId = userMiniJobId
			this.nowDate = nowDate
			this.gemsSpent = gemsSpent

			this.userRepo = userRepo
			this.mjfuRepo = mjfuRepo
			this.mjfuExt = mjfuExt
			this.userExt = userExt
			
		}
		
		// Derived properties computed when checking, then applied when updating
		var User user
		var MiniJobForUser mjfu
		
		def User execute()
		{
			if (gemsSpent > 0) {
				user = userRepo.load(userId)
			}
			 
			mjfu = mjfuRepo.load(userId, userMiniJobId)
			 
			checkIfUserCanComplete()
			
			if (gemsSpent > 0) {
				mjfu.completeMiniJob(nowDate)
			} else {
				mjfu.speedUpCompleteMiniJob(nowDate, user, gemsSpent)
				userRepo.save(user)
			}
			
			mjfuRepo.save(mjfu)
			
			return user
		}
		
		private def void checkIfUserCanComplete()
		{
			if (gemsSpent > 0) {
				lvl6Precondition( 
					user !== null,
					FAIL_OTHER,
					LOG,
					"parameter passed in is null. user=%s", 
					user)
					
				val spendPurpose = [| return String.format("to speed up completing UserMiniJob=%s", mjfu.toString())]
				user.checkCanSpendGems(gemsSpent, LOG, spendPurpose)
			}
			
			lvl6Precondition( 
					mjfu !== null,
					FAIL_OTHER,
					LOG,
					"parameter passed in is null. userMiniJobId=%s, user=%s", 
					userMiniJobId, user)
		}
	
	}

	
}
