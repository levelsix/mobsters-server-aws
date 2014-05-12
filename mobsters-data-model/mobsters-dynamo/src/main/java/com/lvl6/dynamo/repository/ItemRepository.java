package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Item;
public class ItemRepository extends BaseDynamoRepository<Item>{
	public ItemRepository(){
		super(Item.class);
	}

}