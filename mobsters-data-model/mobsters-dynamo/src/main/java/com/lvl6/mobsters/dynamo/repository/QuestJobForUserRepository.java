package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestJobForUser;

@Component
public interface QuestJobForUserRepository extends BaseDynamoCollectionRepository<QuestJobForUser, Integer>
{
	public List<QuestJobForUser> findByUserIdAndQuestIdIn(
		final String userId,
		final Collection<Integer> questIds );
}