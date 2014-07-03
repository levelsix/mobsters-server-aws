package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
@Component public class UserRepositoryImpl extends BaseDynamoRepositoryImpl<User>
	implements
		UserRepository
{
	public UserRepositoryImpl(){
		super(User.class);
		isActive = true;//for unit test
	}

}