package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TaskStage;
@Component public class TaskStageRepository extends BaseDynamoRepository<TaskStage>{
	public TaskStageRepository(){
		super(TaskStage.class);
	}

}