package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.UserLockBoxEvent;
public class UserLockBoxEventRepository extends BaseDynamoRepository<UserLockBoxEvent>{
	public UserLockBoxEventRepository(){
		super(UserLockBoxEvent.class);
	}

}