package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
@Component public class TaskForUserCompletedRepository extends BaseDynamoRepository<TaskForUserCompleted>{
	public TaskForUserCompletedRepository(){
		super(TaskForUserCompleted.class);
	}

}