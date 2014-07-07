package com.lvl6.mobsters.dynamo.repository;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.StructureForUser;

@Component
public abstract class StructureForUserRepository extends BaseDynamoItemRepositoryImpl<StructureForUser>
{
	public StructureForUserRepository()
	{
		super(
			StructureForUser.class);
	}

}