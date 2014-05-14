package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TaskForUserCompleted;
@Component public class TaskForUserCompletedRepository extends BaseDynamoRepository<TaskForUserCompleted>{
	public TaskForUserCompletedRepository(){
		super(TaskForUserCompleted.class);
	}

}