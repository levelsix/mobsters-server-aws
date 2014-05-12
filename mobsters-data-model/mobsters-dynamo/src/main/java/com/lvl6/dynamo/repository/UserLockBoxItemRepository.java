package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.UserLockBoxItem;
public class UserLockBoxItemRepository extends BaseDynamoRepository<UserLockBoxItem>{
	public UserLockBoxItemRepository(){
		super(UserLockBoxItem.class);
	}

}