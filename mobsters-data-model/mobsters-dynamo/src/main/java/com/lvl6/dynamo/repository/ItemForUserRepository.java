package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.ItemForUser;
public class ItemForUserRepository extends BaseDynamoRepository<ItemForUser>{
	public ItemForUserRepository(){
		super(ItemForUser.class);
	}

}