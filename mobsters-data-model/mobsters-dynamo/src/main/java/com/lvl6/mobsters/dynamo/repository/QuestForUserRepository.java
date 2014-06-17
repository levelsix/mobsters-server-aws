package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import com.lvl6.mobsters.dynamo.QuestForUser;

public interface QuestForUserRepository extends BaseDynamoRepository<QuestForUser>
{

	
	public List<QuestForUser> findByUserIdAndIsCompleteAndQuestIdIn(
		final String userId,
		final boolean isComplete,
		final Collection<Integer> questIds );
	
}