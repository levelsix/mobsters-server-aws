package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TaskStageForUser;
@Component public class TaskStageForUserRepository extends BaseDynamoRepository<TaskStageForUser>{
	public TaskStageForUserRepository(){
		super(TaskStageForUser.class);
	}

}