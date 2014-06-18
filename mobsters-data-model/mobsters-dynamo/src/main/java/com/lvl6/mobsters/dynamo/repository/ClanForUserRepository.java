package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

public interface ClanForUserRepository
{
	public List<ClanForUser> findByUserId( String userId );
}