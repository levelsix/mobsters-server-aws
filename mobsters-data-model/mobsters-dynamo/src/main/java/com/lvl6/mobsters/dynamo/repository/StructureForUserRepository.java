package com.lvl6.mobsters.dynamo.repository;


import com.lvl6.mobsters.dynamo.StructureForUser;

public interface StructureForUserRepository extends
	BaseDynamoCollectionRepository<StructureForUser, String>
{

	public abstract StructureForUser findByUserIdAndStructureForUserId(String userId,
			String structureForUserId);
	
}