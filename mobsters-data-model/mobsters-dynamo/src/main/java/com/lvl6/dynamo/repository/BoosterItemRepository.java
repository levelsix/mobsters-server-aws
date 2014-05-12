package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.BoosterItem;
public class BoosterItemRepository extends BaseDynamoRepository<BoosterItem>{
	public BoosterItemRepository(){
		super(BoosterItem.class);
	}

}