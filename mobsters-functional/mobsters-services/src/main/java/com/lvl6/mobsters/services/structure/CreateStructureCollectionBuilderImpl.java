package com.lvl6.mobsters.services.structure;

import java.util.ArrayList;

import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.services.structure.StructureService.CreateStructureCollectionBuilder;
import com.lvl6.mobsters.services.structure.StructureService.CreateStructureOptionsBuilder;

public class CreateStructureCollectionBuilderImpl implements CreateStructureCollectionBuilder 
{
	private final ArrayList<CreateStructureOptionsBuilderImpl> builders = 
		new ArrayList<CreateStructureOptionsBuilderImpl>();
	private final String userId;

	CreateStructureCollectionBuilderImpl(final String userId)
	{
		this.userId = userId;
	}
	
	@Override
	public CreateStructureCollectionBuilder addStructure(
		int structureId, float xCoord, float yCoord) {
		builders.add(
			new CreateStructureOptionsBuilderImpl(structureId, xCoord, yCoord)
		);
		return this;
	}

	@Override
	public CreateStructureCollectionBuilder addStructure(
		int structureId, float xCoord, float yCoord, 
		Director<CreateStructureOptionsBuilder> director) {
		final CreateStructureOptionsBuilderImpl retVal = 
			new CreateStructureOptionsBuilderImpl(structureId, xCoord, yCoord);
		director.apply(retVal);
		builders.add(retVal);
		return this;
	}
	
//	void build( String userId, StructureService service ) {
//		service.createStructuresForUserTwo(
//			userId, new Director<CreateStructureCollectionCallBuilder>() {
//				@Override
//				public void apply(CreateStructureCollectionCallBuilder collectionBuilder) {
//					for( CreateStructureOptionsBuilderImpl itemBuilder : builders ) {
//						itemBuilder.build(collectionBuilder);
//					}
//				}
//			}
//		);
//	}
//
//	public List<CreateStructureOptionsBuilderImpl> build() {
//		return new ArrayList<CreateStructureOptionsBuilderImpl>(builders);
//	}

	ArrayList<StructureForUser> build() {
		// TODO
		return null;
	}
}
