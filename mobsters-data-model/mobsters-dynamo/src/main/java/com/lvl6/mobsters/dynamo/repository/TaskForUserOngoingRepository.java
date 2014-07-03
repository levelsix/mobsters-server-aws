package com.lvl6.mobsters.dynamo.repository;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;

public interface TaskForUserOngoingRepository extends BaseDynamoCollectionRepository<TaskForUserOngoing, String>
{
	TaskForUserOngoing findByUserIdUserTaskId( String userId, String userTaskId );
}