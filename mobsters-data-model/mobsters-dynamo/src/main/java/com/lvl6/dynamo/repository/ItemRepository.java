package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Item;
@Component public class ItemRepository extends BaseDynamoRepository<Item>{
	public ItemRepository(){
		super(Item.class);
	}

}