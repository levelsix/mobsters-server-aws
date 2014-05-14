package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.UserLockBoxEvent;
@Component public class UserLockBoxEventRepository extends BaseDynamoRepository<UserLockBoxEvent>{
	public UserLockBoxEventRepository(){
		super(UserLockBoxEvent.class);
	}

}