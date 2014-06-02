package com.lvl6.mobsters.services.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository;

@Component
public class UserDataRarelyAccessedServiceImpl implements UserDataRarelyAccessedService {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	@Autowired
	protected UserDataRarelyAccessedRepository userDataRarelyAccessedRepository;

	
	//CONTROLLER LOGIC STUFF****************************************************************
	
	
	//RETRIEVE STUFF****************************************************************
	@Override
	public UserDataRarelyAccessed getUserDataRarelyAccessedByUserId(String userId) {
		UserDataRarelyAccessed udra =
				getUserDataRarelyAccessedRepository().load(userId);
		return udra;
	}

	//INSERT STUFF****************************************************************
	
	
	//SAVING STUFF****************************************************************
	@Override
	public void saveUserDataRarelyAccessed(UserDataRarelyAccessed udra) {
		getUserDataRarelyAccessedRepository().save(udra);
	}
	
	//UPDATING STUFF****************************************************************
	@Override
	public void setGameCenterId(UserDataRarelyAccessed udra,
			String gameCenterId) {
		udra.setGameCenterId(gameCenterId);
		saveUserDataRarelyAccessed(udra);
	}
	
	//setters and getters for the Setter Dependency Injection (or something)****************************************************************
	public UserDataRarelyAccessedRepository getUserDataRarelyAccessedRepository() {
		return userDataRarelyAccessedRepository;
	}
	
	public void setUserDataRarelyAccessedRepository(
			UserDataRarelyAccessedRepository userDataRarelyAccessedRepository) {
		this.userDataRarelyAccessedRepository = userDataRarelyAccessedRepository;
	}
	
}
