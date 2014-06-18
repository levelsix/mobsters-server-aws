package com.lvl6.mobsters.services.clan;

import java.util.List;

import com.lvl6.mobsters.dynamo.ClanForUser;

public interface ClanService
{

	// NON CRUD LOGIC******************************************************************

	// CRUD LOGIC******************************************************************

	public List<ClanForUser> findByUserId( String userId );
	/**************************************************************************/

}
