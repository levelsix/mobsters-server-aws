package com.lvl6.mobsters.services.quest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;

public interface QuestService
{

	// NON CRUD LOGIC******************************************************************

	// CRUD LOGIC******************************************************************
	
	public List<QuestForUser> findByUserId( String userId );
	/**************************************************************************/

	public Map<Integer, Collection<QuestJobForUser>> findByUserIdAndQuestIdIn( String userId, Collection<Integer> questIds);

}
