package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Task;
@Component public class TaskRepository extends BaseDynamoRepository<Task>{
	public TaskRepository(){
		super(Task.class);
	}

}