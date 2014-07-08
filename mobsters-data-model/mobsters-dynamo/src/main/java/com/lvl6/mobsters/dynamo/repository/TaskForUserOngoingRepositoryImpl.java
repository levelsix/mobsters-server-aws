package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
@Component public class TaskForUserOngoingRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<TaskForUserOngoing, String>
	implements
		TaskForUserOngoingRepository{
	public TaskForUserOngoingRepositoryImpl(){
		super(TaskForUserOngoing.class, "taskForUserId", String.class);
	}

	@Override
	public TaskForUserOngoing findByUserIdTaskForUserId( String userId, String taskForUserId )
	{
		return load( userId, taskForUserId );
	}

	//EXPECTED: should always be one TaskForUserOngoing
	@Override
	public List<TaskForUserOngoing> findByUserId( String userId )
	{
		return loadAll( userId );
	}

}