package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.User;
@Component public class UserRepository extends BaseDynamoRepository<User>{
	public UserRepository(){
		super(User.class);
	}

}