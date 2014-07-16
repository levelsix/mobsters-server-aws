package com.lvl6.mobsters.dynamo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.StructureForUser;

@Component
public class StructureForUserRepositoryImpl extends
	BaseDynamoCollectionRepositoryImpl<StructureForUser, String>
		implements
			StructureForUserRepository
{
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(MiniJobForUserRepositoryImpl.class);

	
	public StructureForUserRepositoryImpl()
	{
		super( StructureForUser.class, "structureForUserId", String.class );
	}

	@Override
	public StructureForUser findByUserIdAndStructureForUserId(
			String userId,
			String structureForUserId )
	{
		return load( userId, structureForUserId );
	}
	
}