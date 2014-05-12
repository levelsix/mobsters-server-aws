package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.UserClan;
public class UserClanRepository extends BaseDynamoRepository<UserClan>{
	public UserClanRepository(){
		super(UserClan.class);
	}

}