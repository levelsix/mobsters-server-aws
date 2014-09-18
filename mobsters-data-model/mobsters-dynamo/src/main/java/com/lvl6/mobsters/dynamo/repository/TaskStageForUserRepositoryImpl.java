package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.dynamo.TaskStageForUser;

public class TaskStageForUserRepositoryImpl 
	extends BaseDynamoCollectionRepositoryImpl<TaskStageForUser, Integer>
	implements TaskStageForUserRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(TaskStageForUserRepositoryImpl.class);

	public TaskStageForUserRepositoryImpl(){
		super(TaskStageForUser.class, "stageNum", Integer.TYPE);
	}

	@Override
	public List<TaskStageForUser> findByTaskForUserId( String taskForUserId ) {
		return loadAll(taskForUserId);
	}
}