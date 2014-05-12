package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.TaskStageForUser;
public class TaskStageForUserRepository extends BaseDynamoRepository<TaskStageForUser>{
	public TaskStageForUserRepository(){
		super(TaskStageForUser.class);
	}

}