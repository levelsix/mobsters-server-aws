package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.LockBoxItem;
public class LockBoxItemRepository extends BaseDynamoRepository<LockBoxItem>{
	public LockBoxItemRepository(){
		super(LockBoxItem.class);
	}

}