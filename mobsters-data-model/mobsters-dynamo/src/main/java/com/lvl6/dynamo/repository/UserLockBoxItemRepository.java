package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.UserLockBoxItem;
@Component public class UserLockBoxItemRepository extends BaseDynamoRepository<UserLockBoxItem>{
	public UserLockBoxItemRepository(){
		super(UserLockBoxItem.class);
	}

}