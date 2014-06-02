package com.lvl6.mobsters.services.user;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.repository.UserRepository;

public interface UserService {

	//CONTROLLER LOGIC STUFF****************************************************************
	
	
	//RETRIEVE STUFF****************************************************************
	public abstract User getUserByUserId(
			String userId);	

	//INSERT STUFF****************************************************************
	
	
	//SAVING STUFF****************************************************************
	public abstract void saveUser(User user);
	
	//UPDATING STUFF****************************************************************
	
	//setters and getters for the Setter Dependency Injection (or something)****************************************************************
	public abstract UserRepository getUserRepository();
	public abstract void setUserRepository(
			UserRepository userRepository);
	
}
