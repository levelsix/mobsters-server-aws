package com.lvl6.mobsters.services.user

import java.util.ArrayList
import com.lvl6.mobsters.services.structure.StructureService.CreateStructureCollectionBuilder

class CreateUserOptionsBuilderImpl implements UserService.CreateUserOptionsBuilder {
	val ArrayList structureSpecList
	
	new()
	{
		structureSpecList = new ArrayList();
	}
	
	override withStructure(
		int structureId, float xPosition, float yPosition
	) {
		structureSpecList += [ CreateStructureCollectionBuilder bldr |
			bldr.addStructure(structureId,xPosition,yPosition)
		]
		return this
	}
	
}