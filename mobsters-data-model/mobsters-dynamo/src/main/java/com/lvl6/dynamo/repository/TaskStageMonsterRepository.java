package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TaskStageMonster;
@Component public class TaskStageMonsterRepository extends BaseDynamoRepository<TaskStageMonster>{
	public TaskStageMonsterRepository(){
		super(TaskStageMonster.class);
	}

}