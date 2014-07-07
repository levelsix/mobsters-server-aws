package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.TaskStageForUser;

public interface TaskStageForUserRepository extends BaseDynamoCollectionRepository<TaskStageForUser, Integer>
{
	public List<TaskStageForUser> findByTaskForUserId( String taskForUserId );
}
