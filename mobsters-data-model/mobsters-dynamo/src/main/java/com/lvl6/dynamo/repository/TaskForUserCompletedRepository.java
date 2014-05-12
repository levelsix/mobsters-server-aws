package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.TaskForUserCompleted;
public class TaskForUserCompletedRepository extends BaseDynamoRepository<TaskForUserCompleted>{
	public TaskForUserCompletedRepository(){
		super(TaskForUserCompleted.class);
	}

}