package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.LockBoxItem;
@Component public class LockBoxItemRepository extends BaseDynamoRepository<LockBoxItem>{
	public LockBoxItemRepository(){
		super(LockBoxItem.class);
	}

}