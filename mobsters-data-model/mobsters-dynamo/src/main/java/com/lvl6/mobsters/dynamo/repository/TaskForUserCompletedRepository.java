package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
@Component public abstract class TaskForUserCompletedRepository extends BaseDynamoItemRepositoryImpl<TaskForUserCompleted>{
	public TaskForUserCompletedRepository(){
		super(TaskForUserCompleted.class);
	}

}