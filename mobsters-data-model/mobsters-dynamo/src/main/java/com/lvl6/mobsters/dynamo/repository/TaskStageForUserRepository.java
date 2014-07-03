package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskStageForUser;
@Component public abstract class TaskStageForUserRepository extends BaseDynamoItemRepositoryImpl<TaskStageForUser>{
	public TaskStageForUserRepository(){
		super(TaskStageForUser.class);
	}

}