package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.ClanForUser;

public interface ClanForUserRepository extends BaseDynamoCollectionRepository<ClanForUser, String>
{
	public List<ClanForUser> findByUserId( String userId );
	
}