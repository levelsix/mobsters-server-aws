package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
@Component public abstract class TaskForUserOngoingRepository extends BaseDynamoItemRepositoryImpl<TaskForUserOngoing>{
	public TaskForUserOngoingRepository(){
		super(TaskForUserOngoing.class);
	}

}