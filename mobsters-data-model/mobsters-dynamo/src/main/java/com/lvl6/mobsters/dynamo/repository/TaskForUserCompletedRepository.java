package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.TaskForUserCompleted;

public interface TaskForUserCompletedRepository extends BaseDynamoCollectionRepository<TaskForUserCompleted, String>
{
	public List<TaskForUserCompleted> findByUserId( String userId );
}
