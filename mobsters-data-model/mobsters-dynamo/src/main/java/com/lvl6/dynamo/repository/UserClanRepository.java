package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.UserClan;
@Component public class UserClanRepository extends BaseDynamoRepository<UserClan>{
	public UserClanRepository(){
		super(UserClan.class);
	}

}