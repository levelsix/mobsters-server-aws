package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.TaskStageForUser;

public interface TaskStageForUserRepository extends BaseDynamoCollectionRepository<TaskStageForUser, String>
{
	public List<TaskStageForUser> findByTaskForUserId( String taskForUserId );
}
