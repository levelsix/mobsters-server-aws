package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.User;
public class UserRepository extends BaseDynamoRepository<User>{
	public UserRepository(){
		super(User.class);
	}

}