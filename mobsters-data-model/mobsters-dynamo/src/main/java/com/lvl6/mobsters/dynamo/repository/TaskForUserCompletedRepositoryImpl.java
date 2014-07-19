package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
@Component public class TaskForUserCompletedRepositoryImpl extends BaseDynamoRepositoryImpl<TaskForUserCompleted>
	implements
		TaskForUserCompletedRepository
{
	
	private static final Logger LOG =
		LoggerFactory.getLogger(TaskForUserCompletedRepositoryImpl.class);

	public TaskForUserCompletedRepositoryImpl(){
		super(TaskForUserCompleted.class);
	}
 
	@Override
	public List<TaskForUserCompleted> findByUserId( final String userId )
	{
		final TaskForUserCompleted hashKey = new TaskForUserCompleted();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<TaskForUserCompleted> query =
			new DynamoDBQueryExpression<TaskForUserCompleted>()
			// .withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<TaskForUserCompleted> taskForUserCompletedsForUser = query(query);
		taskForUserCompletedsForUser.loadAllResults();
		return taskForUserCompletedsForUser;
	}
	
}