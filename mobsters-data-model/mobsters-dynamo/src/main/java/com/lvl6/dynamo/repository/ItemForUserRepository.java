package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ItemForUser;
@Component public class ItemForUserRepository extends BaseDynamoRepository<ItemForUser>{
	public ItemForUserRepository(){
		super(ItemForUser.class);
	}

}