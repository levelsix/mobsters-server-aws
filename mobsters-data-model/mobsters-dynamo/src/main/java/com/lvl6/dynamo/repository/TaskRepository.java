package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Task;
public class TaskRepository extends BaseDynamoRepository<Task>{
	public TaskRepository(){
		super(Task.class);
	}

}