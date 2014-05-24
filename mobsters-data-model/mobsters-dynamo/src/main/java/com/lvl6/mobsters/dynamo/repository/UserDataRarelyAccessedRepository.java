package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
@Component public class UserDataRarelyAccessedRepository extends BaseDynamoRepository<UserDataRarelyAccessed>{
	public UserDataRarelyAccessedRepository(){
		super(UserDataRarelyAccessed.class);
		isActive = true;//for unit test
	}

}