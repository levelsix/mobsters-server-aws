package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.conditions.Director;
import com.lvl6.mobsters.conditions.IBooleanConditionBuilder;
import com.lvl6.mobsters.conditions.IIntConditionBuilder;
import com.lvl6.mobsters.dynamo.QuestForUser;

public interface QuestForUserRepository extends BaseDynamoCollectionRepository<QuestForUser, Integer>
{
	/*
	 * Use the ConditionBuilder to perform filtered queries like this:
	 * 
	 * qfuRepo.findByUserId(userId) [ bldr | bldr.complete[isTrue()].questId[in(questIds)]];
	 * 
	public List<QuestForUser> findByUserIdAndIsCompleteAndQuestIdIn(
		final String userId,
		final boolean isComplete,
		final Collection<Integer> questIds );
	 */
		
	public interface QuestForUserConditionBuilder {
		QuestForUserConditionBuilder questId(Director<IIntConditionBuilder> director);
		
		QuestForUserConditionBuilder complete(Director<IBooleanConditionBuilder> director);
		
		QuestForUserConditionBuilder redeemed(Director<IBooleanConditionBuilder> director);
	}
	
	public List<QuestForUser> findByUserId( String userId );
	
	public List<QuestForUser> findByUserIdAndAll( String userId, Director<QuestForUserConditionBuilder> filterDirector );
	
	public List<QuestForUser> findByUserIdAndAny( String userId, Director<QuestForUserConditionBuilder> filterDirector );
}