package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TaskForUserOngoing;
@Component public class TaskForUserOngoingRepository extends BaseDynamoRepository<TaskForUserOngoing>{
	public TaskForUserOngoingRepository(){
		super(TaskForUserOngoing.class);
	}

}