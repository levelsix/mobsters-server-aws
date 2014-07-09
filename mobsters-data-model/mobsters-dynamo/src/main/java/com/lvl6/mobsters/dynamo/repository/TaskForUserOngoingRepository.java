package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;

public interface TaskForUserOngoingRepository extends BaseDynamoCollectionRepository<TaskForUserOngoing, String>
{
	TaskForUserOngoing findByUserIdTaskForUserId( String userId, String taskForUserId );
	
	List<TaskForUserOngoing> findByUserId( String userId );;

}