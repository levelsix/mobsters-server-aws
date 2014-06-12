package com.lvl6.mobsters.dynamo.repository;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.StructureForUser;

@Component
public class StructureForUserRepository extends BaseDynamoRepositoryImpl<StructureForUser>
{
	public StructureForUserRepository()
	{
		super(
			StructureForUser.class);
	}

}