package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskStageForUser;
@Component public class TaskStageForUserRepository extends BaseDynamoRepositoryImpl<TaskStageForUser>{
	public TaskStageForUserRepository(){
		super(TaskStageForUser.class);
	}

}