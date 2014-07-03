package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
@Component public class TaskForUserOngoingRepositoryImpl extends BaseDynamoRepositoryImpl<TaskForUserOngoing>
	implements
		TaskForUserOngoingRepository{
	public TaskForUserOngoingRepositoryImpl(){
		super(TaskForUserOngoing.class);
	}

	@Override
	public TaskForUserOngoing findByUserId( String userId )
	{
		return load( userId );
	}

}