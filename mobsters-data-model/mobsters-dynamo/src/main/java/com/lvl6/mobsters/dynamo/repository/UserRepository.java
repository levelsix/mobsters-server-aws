package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
@Component public class UserRepository extends BaseDynamoRepositoryImpl<User>{
	public UserRepository(){
		super(User.class);
		isActive = true;//for unit test
	}

}