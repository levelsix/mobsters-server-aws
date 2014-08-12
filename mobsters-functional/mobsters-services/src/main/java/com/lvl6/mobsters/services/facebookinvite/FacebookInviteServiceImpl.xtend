package com.lvl6.mobsters.services.facebookinvite

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
import com.lvl6.mobsters.dynamo.repository.UserFacebookInviteForSlotRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.info.repository.StructureRepository
import com.lvl6.mobsters.services.user.UserExtensionLib
import java.util.Date
import java.util.List
import java.util.Map
import java.util.Set
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.services.facebookinvite.FacebookInviteServiceImpl.*

import static com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions.*
import static com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode.*

@Component
public class FacebookInviteServiceImpl implements FacebookInviteService {

	private static val Logger LOG = 
		LoggerFactory.getLogger(FacebookInviteServiceImpl)

	@Autowired
	@Property private var UserRepository userRepository
	
	@Autowired
	@Property private var UserFacebookInviteForSlotRepository ufifsRepo
	
	@Autowired
	@Property private var DataServiceTxManager txManager

	@Autowired
	@Property extension UserExtensionLib userExtension
	
	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	override void inviteFbUsers( String userId,
 		Map<String, Pair<String, Integer>> fbIdToUserStruct,
		Date nowDate )
	{
		val FacebookInviteServiceImpl.InviteFbUserAction action =
			new FacebookInviteServiceImpl.InviteFbUserAction(
				userId, fbIdToUserStruct, nowDate, ufifsRepo,
				userRepository ,userExtension
		)
		
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

	}
	
	static class InviteFbUserAction 
	{
		val String userId
		val Map<String, Pair<String, Integer>> fbIdToUserStruct
		val Date now
		
		
		val UserFacebookInviteForSlotRepository ufifsRepo
		val UserRepository userRepo
		val extension UserExtensionLib userExt
		
		new( String userId,
 			Map<String, Pair<String, Integer>> fbIdToUserStruct,
			Date now, UserFacebookInviteForSlotRepository ufifsRepository,
			UserRepository userRepo, UserExtensionLib userExt ) 
		{
			this.userId = userId
			this.now = now
			this.fbIdToUserStruct = fbIdToUserStruct

			this.ufifsRepo = ufifsRepository
			this.userRepo = userRepo
			this.userExt = userExt
			
		}
		
		// Derived properties computed when checking, then applied when updating
		var User user
		var List<UserFacebookInviteForSlot> existingInvites
		var Map<String, Pair<String, Integer>> newInvites
		
		def void execute() {
			 //user = userRepo.load(userId)
			 
			 existingInvites = ufifsRepo.loadEach(userId, fbIdToUserStruct.keySet)
			 
			 checkIfUserCanInvite()
			 
			 
			//so there are some new facebook ids that the user/inviter is using
			val Set<String> blackListFbIds = existingInvites.map[ UserFacebookInviteForSlot ufifs |
				ufifs.recipientFacebookId
			].toSet
			
			//extract out the new facebook ids
			ufifsRepo.saveEach(
				fbIdToUserStruct.filter[ String fbId, Pair<String, Integer> userStructIdLvl |
					!blackListFbIds.contains(fbId)
				].entrySet.map[ Map.Entry<String, Pair<String, Integer>> fbIdToUserStructIdAndFbLvl |
					//and convert to domain object
					new UserFacebookInviteForSlot => [ UserFacebookInviteForSlot ufifs |
						ufifs.userId = userId
						ufifs.recipientFacebookId = fbIdToUserStructIdAndFbLvl.key
						ufifs.timeOfInvite = now
						ufifs.userStructId = fbIdToUserStructIdAndFbLvl.value.key
						ufifs.userStructFbLvl = fbIdToUserStructIdAndFbLvl.value.value
					]
				]
			)
			 
		}
		
		private def void checkIfUserCanInvite()
		{
			
			lvl6Precondition( 
				fbIdToUserStruct.size > existingInvites.size,
				FAIL_OTHER,
				LOG,
				"all the facebook ids have been used already. existingFbIds=%s, requestedFbIds=%s", 
				existingInvites, fbIdToUserStruct)

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
