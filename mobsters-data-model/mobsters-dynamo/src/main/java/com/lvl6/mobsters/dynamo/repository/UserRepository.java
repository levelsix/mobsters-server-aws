package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
@Component public abstract class UserRepository extends BaseDynamoItemRepositoryImpl<User>{
	public UserRepository(){
		super(User.class);
	}

}