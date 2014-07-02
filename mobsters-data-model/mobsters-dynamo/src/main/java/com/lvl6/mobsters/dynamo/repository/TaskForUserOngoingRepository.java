package com.lvl6.mobsters.dynamo.repository;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;

public interface TaskForUserOngoingRepository extends BaseDynamoRepository<TaskForUserOngoing>
{
	public TaskForUserOngoing findByUserId( String userId );
}