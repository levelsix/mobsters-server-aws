package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.repository.filter.Director;
import com.lvl6.mobsters.dynamo.repository.filter.IBooleanConditionBuilder;
import com.lvl6.mobsters.dynamo.repository.filter.IIntConditionBuilder;

public interface QuestForUserRepository extends BaseDynamoCollectionRepository<QuestForUser, Integer>
{
	public List<QuestForUser> findByUserIdAndIsCompleteAndQuestIdIn(
		final String userId,
		final boolean isComplete,
		final Collection<Integer> questIds );
		
	public interface QuestForUserConditionBuilder {
		QuestForUserConditionBuilder questId(Director<IIntConditionBuilder> director);
		
		QuestForUserConditionBuilder complete(Director<IBooleanConditionBuilder> director);
		
		QuestForUserConditionBuilder redeemed(Director<IBooleanConditionBuilder> director);
	}
	
	public List<QuestForUser> findByUserId( String userId );
	
	public List<QuestForUser> findByUserId( String userId, Director<QuestForUserConditionBuilder> filterDirector );
}