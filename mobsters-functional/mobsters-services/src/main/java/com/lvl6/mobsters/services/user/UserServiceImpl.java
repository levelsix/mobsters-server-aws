package com.lvl6.mobsters.services.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.repository.UserRepository;

@Component
public class UserServiceImpl implements UserService {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	@Autowired
	protected UserRepository userRepository;

	
	//CONTROLLER LOGIC STUFF****************************************************************
	
	
	//RETRIEVE STUFF****************************************************************
	@Override
	public User getUserByUserId(String userId) {
		User user = getUserRepository().load(userId);
		return user;
	}

	//INSERT STUFF****************************************************************
	
	
	//SAVING STUFF****************************************************************
	@Override
	public void saveUser(User user) {
		getUserRepository().save(user);
	}
	
	//UPDATING STUFF****************************************************************
	
	//setters and getters for the Setter Dependency Injection (or something)****************************************************************
	@Override
	public UserRepository getUserRepository() {
		return userRepository;
	}
	
	@Override
	public void setUserRepository(
			UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
}
