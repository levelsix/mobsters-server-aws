package com.lvl6.mobsters.services.structure

import com.lvl6.mobsters.common.utils.AbstractIntComparable
import com.lvl6.mobsters.common.utils.ImmutableIntKey
import com.lvl6.mobsters.dynamo.StructureForUser
import com.lvl6.mobsters.info.CoordinatePair
import com.lvl6.mobsters.info.Structure
import com.lvl6.mobsters.info.repository.StructureRepository
import java.util.Collections
import java.util.Date
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
 * To attach the methods in this library to the StructureForUser
 * and ObstacleForUser domain objects.
 * 
 * 1)  Import this class in your compilation unit
 * 2)  In the Dependency injection section, declare an extension
 *     variable of this type:
 * 
 *     extension StructureExtensionLib structExtension
 * 
 * 3)  Invoke the methods as though they were operations of the
 *     StructureForUser and ObstacleForUser classes.  E.g,:
 * 
 *     var StructureForUser sfu = sfuRepo.load(...)
 *     sfu.moveTo( 20, 20 )
 * 
 * Extensions will not help with stateful extensions (e.g. navigation
 * to a Structure from a StructureForUser), but there are other
 * ideas in the works for those use cases.
 */
@Component
class StructureExtensionLib {
	@Autowired
	StructureRepository structDefRepo;
	
	public def StructureForUser moveTo( StructureForUser sfu, CoordinatePair dest )
	{
		sfu.XCoord = dest.x
		sfu.YCoord = dest.y
		return sfu
	}

	public def StructureForUser moveTo( StructureForUser sfu, float x, float y )
	{
		sfu.XCoord = x
		sfu.YCoord = y
		return sfu
	}
	
	public def StructureForUser speedUpConstruction( StructureForUser sfu, Date upgradeTime ) 
	{
		sfu.setLastRetrieved(upgradeTime);
		sfu.setComplete(true);
		return sfu
	}
	
	// StructureContext Attachment lookup map is empty until @PostConstruct phase calls
	// doInitExtension() to load it from the config repo.
	var Map<AbstractIntComparable, StaticStructureContext> structureContextLookup =
		Collections.emptyMap
	
	public def Structure getStructure( StructureForUser sfu ) {
		var StaticStructureContext retVal = sfu.getAttachment(StaticStructureContext);
		if (retVal === null) {
			retVal = 
				structureContextLookup.get(
					new ImmutableIntKey(sfu.structId))
			sfu.putAttachment(StaticStructureContext, retVal)
		}
		
		return retVal.structureDef
	}
	
	@PostConstruct
	def void doInitExtension() 
	{
		// Read Structure config and construct structureMap contents.
		structureContextLookup = 
			structDefRepo.findAll()
				.map[ Structure s | return new StaticStructureContext(s) ]
				.toMap[ StaticStructureContext keyValue | return keyValue ]

		return
	}
}

/**
 * "Stateless" flyweight adapter for StructureForUser Dynamo model adapter class.
 * 
 * Stateless, in this context, means that this class is not instantiated once for each model object
 * it adapts in order to augment its state with additional derived and/or transient state.  Its
 * instances are not devoid of state, however that state is static in nature and represents a cache
 * of readily accessible information about all structures that have a certain strucutreId.
 * 
 * Because it does not wrap a specific StructureForUser object, its up to each adapted object to 
 * store a reference to its StaticStructureContext instance by handling it as an ExtensibleObject
 * attachment.
 */
class StaticStructureContext extends AbstractIntComparable {
	val Structure structureDef
	
	new(Structure structureDef) 
	{
		checkNotNull(structureDef)
		this.structureDef = structureDef
	}
	
	override getOrderingInt() {
		return structureDef.id
	}
	
	public def Structure getStructureDef()
	{
		return structureDef
	}
}