package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.ClanForUser;

public interface ClanForUserRepository extends BaseDynamoRepository<ClanForUser>
{
	public List<ClanForUser> findByUserId( String userId );
	
}