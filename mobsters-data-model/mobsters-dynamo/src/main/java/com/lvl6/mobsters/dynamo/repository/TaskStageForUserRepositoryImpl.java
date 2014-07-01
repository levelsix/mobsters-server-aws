package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.TaskStageForUser;
@Component public class TaskStageForUserRepositoryImpl extends BaseDynamoRepositoryImpl<TaskStageForUser>
	implements
		TaskStageForUserRepository
{
	private static final Logger LOG =
		LoggerFactory.getLogger(TaskStageForUserRepositoryImpl.class);

	public TaskStageForUserRepositoryImpl(){
		super(TaskStageForUser.class);
	}

	@Override
	public List<TaskStageForUser> findByTaskForUserId( String taskForUserId ) {
		final TaskStageForUser hashKey = new TaskStageForUser();
		hashKey.setTaskForUserId(taskForUserId);

		final DynamoDBQueryExpression<TaskStageForUser> query =
			new DynamoDBQueryExpression<TaskStageForUser>()
			// .withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<TaskStageForUser> taskStages = query(query);
		taskStages.loadAllResults();
		return taskStages;
	}
}