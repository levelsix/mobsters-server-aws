package com.lvl6.mobsters.services.structure

import com.lvl6.mobsters.dynamo.StructureForUser
import com.lvl6.mobsters.info.CoordinatePair

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
class StructureExtensionLib {
	public def boolean moveTo( StructureForUser sfu, CoordinatePair dest )
	{
		sfu.XCoord = dest.x
		sfu.YCoord = dest.y
		return true
	}

	public def boolean moveTo( StructureForUser sfu, float x, float y )
	{
		sfu.XCoord = x
		sfu.YCoord = y
		return true
	}
}