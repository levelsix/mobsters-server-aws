package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.TaskStage;
public class TaskStageRepository extends BaseDynamoRepository<TaskStage>{
	public TaskStageRepository(){
		super(TaskStage.class);
	}

}