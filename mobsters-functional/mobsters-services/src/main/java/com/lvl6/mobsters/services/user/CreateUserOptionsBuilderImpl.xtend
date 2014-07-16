package com.lvl6.mobsters.services.user

import com.lvl6.mobsters.common.utils.Director
import com.lvl6.mobsters.services.structure.StructureService.CreateStructureCollectionBuilder
import java.util.ArrayList

class CreateUserOptionsBuilderImpl implements UserService.CreateUserOptionsBuilder {
	val ArrayList<(CreateStructureCollectionBuilder)=>void> structureSpecList
	
	new()
	{
		structureSpecList = new ArrayList<(CreateStructureCollectionBuilder)=>void>();
	}
	
	override withStructure(
		int structureId, float xPosition, float yPosition
	) {
		structureSpecList += [ CreateStructureCollectionBuilder bldr |
			bldr.addStructure(structureId,xPosition,yPosition)
			return
		]
		
		return this
	}
	
	def Director<CreateStructureCollectionBuilder> buildDirector()
	{
		return [ CreateStructureCollectionBuilder bldr |
			structureSpecList.forEach[
				it.apply(bldr)
			]
		]
	}
}
