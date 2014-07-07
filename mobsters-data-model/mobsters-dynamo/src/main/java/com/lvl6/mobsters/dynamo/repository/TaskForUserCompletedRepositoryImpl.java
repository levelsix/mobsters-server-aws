package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
@Component public class TaskForUserCompletedRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<TaskForUserCompleted, Integer>
	implements
		TaskForUserCompletedRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(TaskForUserCompletedRepositoryImpl.class);

	public TaskForUserCompletedRepositoryImpl(){
		super(TaskForUserCompleted.class, "taskId", Integer.TYPE);
	}
 
	@Override
	public List<TaskForUserCompleted> findByUserId( final String userId )
	{
		return loadAll(userId);
	}
	
}