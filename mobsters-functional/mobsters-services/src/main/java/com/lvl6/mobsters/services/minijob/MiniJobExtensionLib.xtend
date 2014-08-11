package com.lvl6.mobsters.services.minijob

import com.lvl6.mobsters.common.utils.AbstractIntComparable
import com.lvl6.mobsters.common.utils.ImmutableIntKey
import com.lvl6.mobsters.dynamo.MiniJobForUser
import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.info.IMonster
import com.lvl6.mobsters.info.MiniJob
import com.lvl6.mobsters.info.repository.MiniJobRepository
import com.lvl6.mobsters.services.user.UserExtensionLib
import java.util.Collections
import java.util.Date
import java.util.List
import java.util.Map
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.google.common.base.Preconditions.*

// TODO: Place all Extension Libraries in a parallel package structure
//       and use CheckStyle to detect attempts to use them outside the
//       Service layer.  Java package visibility is insufficient to
//       both package-per-feature and layer-scoped visibility control.


/**
 * Data Layer components may augment the domain objects to provide
 * business logic that each individual service component may access.
 * 
 * Sharing through the public interface is problematic because it
 * leads to redundant repository access due to the otherwise
 * desirable design constraint that it remain independent of the
 * domain objects that services rely onto implement its contract.
 * 
 * To attach the methods in this library to the MiniJobForUser
 * domain objects.
 * 
 * 1)  Import this class in your compilation unit
 * 2)  In the Dependency injection section, declare an extension
 *     variable of this type:
 * 
 *     extension MiniJobExtensionLib miniJobExtension
 * 
 * 3)  Invoke the methods as though they were operations of the
 *     MiniJobForUser class.  E.g,:
 * 
 *     var MiniJobForUser mjfu = mjfuRepo.load(...)
 *     mjfu.completeMiniJob( new Date() )
 * 
 * Extensions will not help with stateful extensions (e.g. navigation
 * to a MiniJob from a MiniJobForUser), but there are other
 * ideas in the works for those use cases.
 */
@Component
class MiniJobExtensionLib {
	@Autowired
	MiniJobRepository miniJobDefRepo;
	
	@Autowired
	extension UserExtensionLib userExtension
	
	def MiniJobForUser completeMiniJob( MiniJobForUser mjfu, Date completeTime ) 
	{
		mjfu.timeCompleted = completeTime
		return mjfu
	}
	
	def MiniJobForUser speedUpCompleteMiniJob( MiniJobForUser mjfu,
		Date completeTime, User u, int gemsToSpend ) 
	{
		u.spendGems( gemsToSpend )
		return mjfu.completeMiniJob( completeTime )
	}
	
	def void redeemMiniJob( MiniJobForUser mjfu, User u,
		int maxCash, int maxOil, List<MonsterForUser> mfuList,
		Map<String, Integer> mfuIdToHealth )
	{
		var MiniJob mj = mjfu.getMiniJob
		
		//update user currency
		u.gainCash(mj.cashReward, maxCash)
		u.gainOil(mj.oilReward, maxOil)
		u.gems = mj.gemReward + u.gems
		
		//TODO: Update user's monsters with one extra piece
		var IMonster monsterIdReward = mj.monsterReward
			
		//update monster health
		mfuList.forEach[
			MonsterForUser mfu |
			mfu.currentHealth =  mfuIdToHealth.get(
					mfu.monsterForUserUuid )
		]
		
	}
	
	// StructureContext Attachment lookup map is empty until @PostConstruct phase calls
	// doInitExtension() to load it from the config repo.
	var Map<AbstractIntComparable, StaticMiniJobContext> miniJobContextLookup =
		Collections.emptyMap
	
	public def MiniJob getMiniJob( MiniJobForUser mjfu ) {
		var StaticMiniJobContext retVal = mjfu.getAttachment(StaticMiniJobContext);
		if (retVal === null) {
			retVal = 
				miniJobContextLookup.get(
					new ImmutableIntKey(mjfu.miniJobId))
			mjfu.putAttachment(StaticMiniJobContext, retVal)
		}
		
		return retVal.miniJobDef
	}
	
	@PostConstruct
	def void doInitExtension() 
	{
		// Read MiniJob config and construct miniJobMap contents.
		miniJobContextLookup = 
			miniJobDefRepo.findAll()
				.map[ MiniJob mj | return new StaticMiniJobContext(mj) ]
				.toMap[ StaticMiniJobContext keyValue | return keyValue ]

		return
	}
}

/**
 * "Stateless" flyweight adapter for MiniJobForUser Dynamo model adapter class.
 * 
 * Stateless, in this context, means that this class is not instantiated once for each model object
 * it adapts in order to augment its state with additional derived and/or transient state.  Its
 * instances are not devoid of state, however that state is static in nature and represents a cache
 * of readily accessible information about all MiniJobs that have a certain miniJObId.
 * 
 * Because it does not wrap a specific MiniJobForUser object, its up to each adapted object to 
 * store a reference to its StaticMiniJobContext instance by handling it as an ExtensibleObject
 * attachment.
 */
class StaticMiniJobContext extends AbstractIntComparable {
	val MiniJob miniJobDef
	
	new(MiniJob miniJobDef) 
	{
		checkNotNull(miniJobDef)
		this.miniJobDef = miniJobDef
	}
	
	override getOrderingInt() {
		return miniJobDef.id
	}
	
	public def MiniJob getMiniJobDef()
	{
		return miniJobDef
	}
}
