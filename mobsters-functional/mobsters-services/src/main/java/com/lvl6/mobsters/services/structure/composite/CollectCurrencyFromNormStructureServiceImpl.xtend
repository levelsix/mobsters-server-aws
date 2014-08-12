package com.lvl6.mobsters.services.structure.composite

import com.lvl6.mobsters.dynamo.StructureForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
import com.lvl6.mobsters.dynamo.repository.UserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.info.Structure
import com.lvl6.mobsters.info.repository.StructureRepository
import com.lvl6.mobsters.services.structure.StructureExtensionLib
import com.lvl6.mobsters.services.user.UserExtensionLib
import com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode
import java.util.ArrayList
import java.util.Date
import java.util.List
import java.util.Map
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions.*
import static com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum.*
import static java.lang.String.*

@Component
public class CollectCurrencyFromNormStructureServiceImpl implements CollectCurrencyFromNormStructureService {

	private static val Logger LOG = 
		LoggerFactory.getLogger(CollectCurrencyFromNormStructureServiceImpl)

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
	override collectResources( String userId, int maxCash,
			int maxOil, Date now,
			Map<String, Pair<Date, Integer>> sfuIdToCollectTimeAndAmount )
	{
		val CollectCurrencyFromNormStructureServiceImpl.CollectCurrencyAction action =
			new CollectCurrencyFromNormStructureServiceImpl.CollectCurrencyAction(
				userId, maxCash, maxOil, now, sfuIdToCollectTimeAndAmount,
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

		// TODO: return a user or some container holding the user data that was updated
		return null;
	}
	
	static class CollectCurrencyAction 
	{
		val String userId
		val int maxCash
		val int maxOil
		val Date now
		val Map<String, Pair<Date, Integer>> sfuIdToCollectTimeAndAmount
		
		val UserRepository userRepo
		val StructureForUserRepository sfuRepo
		val extension StructureExtensionLib sfuExt
		val extension UserExtensionLib userExt
		
		new(String userId, int maxCash, int maxOil, Date now,
			Map<String, Pair<Date, Integer>> sfuIdToCollectTimeAndAmount,
			UserRepository userRepo, StructureForUserRepository sfuRepo,
			StructureExtensionLib sfuExt, UserExtensionLib userExt) 
		{
			this.userId = userId
			this.maxCash = maxCash
			this.maxOil = maxOil
			this.now = now
			this.sfuIdToCollectTimeAndAmount = sfuIdToCollectTimeAndAmount

			this.userRepo = userRepo
			this.sfuRepo = sfuRepo
			this.sfuExt = sfuExt
			this.userExt = userExt
			
		}
		
		// Derived properties computed when checking, then applied when updating
		var User user
		var List<StructureForUser> sfuList
		var int cashGained
		var int oilGained
		var List<Pair<StructureForUser, Date>> sfuAndRetrievalTime
		
		def void execute() {
			user = userRepo.load(userId)
			sfuList = sfuRepo.loadEach(userId, sfuIdToCollectTimeAndAmount.keySet);
			
			checkIfUserCanCollect()
	
			// TODO: Write to currency history
//			sfu.beginTimedUpgrade(timeOfUpgrade, user, gemsSpent, cashToSpend, oilToSpend)
			
			userRepo.save(user.gainCash(cashGained, maxCash).gainOil(oilGained, maxOil))
			//process the structures and mark them as completely built
			sfuRepo.saveEach(
				sfuAndRetrievalTime.map[Pair<StructureForUser, Date> sfuAndDate |
					sfuAndDate.key
					.collectResource(
						sfuAndDate.value
					)
				]
			)
		}
		
		private def void checkIfUserCanCollect()
		{
			lvl6Precondition( 
				user !== null,
				Lvl6MobstersStatusCode.FAIL_OTHER,
				LOG,
				"parameter passed in is null. user=%s", 
				user)
				
			if ( sfuList.size !== sfuIdToCollectTimeAndAmount.size ) {
				LOG.error(
					format("(will continue processing) userStructs in db
						   inconsistent with client's requested structs.
						   structsInDb=%s, client's structs=%s",
						   sfuList, sfuIdToCollectTimeAndAmount)
			 	)
			}
			
			//contains the new retrieval time for the StructureForUser  
			//TODO: figure out if can just update the StructureForUser
			sfuAndRetrievalTime = new ArrayList<Pair<StructureForUser, Date>>
			
			//TODO: Should this logic be put into an extension library?
			//store for later processing the valid StructureForUser objects
			sfuList.forEach[ StructureForUser sfu |
				
				var Structure struct = sfu.structure
				var srg = struct?.resourceGenerator
				if (null === srg) {
					LOG.warn(
						format( "Structure with id=%d does not exist, ergo UserStruct invalid=%s",
							sfu.structId, sfu )
					)
					return
				}
				
				//sum up total cash and oil collected and collect the new retrieval times
				var Pair<Date, Integer> collectTimeAndAmount =
					sfuIdToCollectTimeAndAmount.get(sfu.structureForUserId)
				
				switch valueOf(srg.resourceTypeGenerated) {
					case CASH: {
						sfuAndRetrievalTime.add( sfu -> collectTimeAndAmount.key )
						cashGained += collectTimeAndAmount.value
					} case OIL: {
						sfuAndRetrievalTime.add( sfu -> collectTimeAndAmount.key )
						oilGained += collectTimeAndAmount.value
					} default: {
						LOG.error(
							format( "(will continue processing) invalid resourceType: %s",
								srg )
						)
					}
				}
			]
			
			if (user.cash + cashGained > maxCash) {
				LOG.warn(
					format("(will continue processing) client allowed user to go over max :(, user=%s, cashGained=%d, maxCash=%d",
						user, cashGained, maxCash
					)
				)
			}
			
			if (user.oil + oilGained > maxOil) {
				LOG.warn(
					format("(will continue processing) client allowed user to go over max :(, user=%s, oilGained=%d, maxOil=%d",
						user, oilGained, maxOil
					)
				)
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
