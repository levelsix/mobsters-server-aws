package com.lvl6.mobsters.services.user;

import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
// import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository;

public interface UserDataRarelyAccessedService {

	//CONTROLLER LOGIC STUFF****************************************************************
	
	
	//RETRIEVE STUFF****************************************************************
	public abstract UserDataRarelyAccessed getUserDataRarelyAccessedByUserId(
			String userId);	

	//INSERT STUFF****************************************************************
	
	
	//SAVING STUFF****************************************************************
	public abstract void saveUserDataRarelyAccessed(UserDataRarelyAccessed udra);
	
	//UPDATING STUFF****************************************************************
	public abstract void setGameCenterId(UserDataRarelyAccessed udra,
			String gameCenterId);
	
	//setters and getters for the Setter Dependency Injection (or something)****************************************************************
	// public abstract UserDataRarelyAccessedRepository getUserDataRarelyAccessedRepository();
	// public abstract void setUserDataRarelyAccessedRepository(
	//		UserDataRarelyAccessedRepository userDataRarelyAccessedRepository);
}
