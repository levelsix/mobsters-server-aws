package com.lvl6.mobsters.services.taskforuserongoing;

import java.util.List;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.repository.UserRepository;

public interface TaskForUserOngoingService {

	//CONTROLLER LOGIC STUFF****************************************************************
	
	
	//RETRIEVE STUFF****************************************************************
	public abstract TaskForUserOngoing getUserByUserId(String userId);	

	//INSERT STUFF****************************************************************
	
	
	//SAVING STUFF****************************************************************
	public abstract void saveTaskForUserOngoing(TaskForUserOngoing tfuo);
	
	public abstract void saveTaskForUserOngoingList(List<TaskForUserOngoing> tfuoList);
	
	//UPDATING STUFF****************************************************************
	
	//setters and getters for the Setter Dependency Injection (or something)****************************************************************
	public abstract UserRepository getUserRepository();
	public abstract void setUserRepository(
			UserRepository userRepository);
	
}
