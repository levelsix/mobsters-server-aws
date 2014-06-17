package com.lvl6.mobsters.services.quest;

import java.util.List;

import com.lvl6.mobsters.dynamo.QuestForUser;

public interface QuestService
{

	// NON CRUD LOGIC******************************************************************

	// CRUD LOGIC******************************************************************
	
	public List<QuestForUser> findByUserId( String userId );
	/**************************************************************************/


}
